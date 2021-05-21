package com.example.chat_example_with_socketio.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.chat_example_with_socketio.entity.chat.Chat;
import com.example.chat_example_with_socketio.entity.chat.enums.MessageType;
import com.example.chat_example_with_socketio.entity.chat.repository.ChatRepository;
import com.example.chat_example_with_socketio.entity.chat_room.ChatRoom;
import com.example.chat_example_with_socketio.entity.chat_room.Rooms;
import com.example.chat_example_with_socketio.entity.chat_room.enums.Authority;
import com.example.chat_example_with_socketio.entity.chat_room.repository.ChatRoomRepository;
import com.example.chat_example_with_socketio.entity.chat_room.repository.RoomsRepository;
import com.example.chat_example_with_socketio.entity.image.chatRoom.ChatRoomImage;
import com.example.chat_example_with_socketio.entity.image.chatRoom.repository.ChatRoomImageRepository;
import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import com.example.chat_example_with_socketio.entity.image.user.repository.UserImageRepository;
import com.example.chat_example_with_socketio.entity.solo_chat.SoloChat;
import com.example.chat_example_with_socketio.entity.solo_chat.repository.SoloChatRepository;
import com.example.chat_example_with_socketio.entity.solo_chat_room.SoloChatRoom;
import com.example.chat_example_with_socketio.entity.solo_chat_room.repository.SoloChatRoomRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.UserDeviceToken;
import com.example.chat_example_with_socketio.entity.user.repository.UserDeviceTokenRepository;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.payload.request.*;
import com.example.chat_example_with_socketio.payload.response.MessageResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import com.example.chat_example_with_socketio.util.FCMUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {

    private final SocketIOServer server;

    private final UserRepository userRepository;
    private final ChatRoomImageRepository chatRoomImageRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RoomsRepository roomRepository;
    private final UserDeviceTokenRepository userDeviceTokenRepository;
    private final SoloChatRepository soloChatRepository;
    private final SoloChatRoomRepository soloChatRoomRepository;

    private final JwtProvider jwtProvider;
    private final FCMUtil fcmUtil;

    private final UserImageRepository userImageRepository;

    @Value("${image.chat.dir}")
    private String imageChatDir;

    @Override
    public void connect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if(jwtProvider.validateToken(token)) {
            System.out.println("Invalid token");
            client.disconnect();
        }

        User user;
        try {
            user = userRepository.findByUserId(jwtProvider.getUserId(token))
                    .orElseThrow(RuntimeException::new);
            client.set("user", user);
            System.out.println("Connected : " + user.getName() + "SessionId : " + client.getSessionId());
        }catch (Exception e) {
            System.out.println("User Not Found");
            client.disconnect();
        }
    }

    @Override
    public void disconnect(SocketIOClient client) {
        System.out.println("DisconnectedId : " + client.getSessionId());
    }

    @Override
    public void joinRoom(SocketIOClient client, JoinChatRoomRequest joinChatRoomRequest) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("user not found");
            client.disconnect();
            return;
        }

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByChatIdAndUserId(joinChatRoomRequest.getChatId(), user.getUserId());
        if(optionalChatRoom.isPresent()) {
            System.out.println("user already joined");
            client.disconnect();
            return;
        }

        UserDeviceToken userDeviceToken = userDeviceTokenRepository.findByUserId(user.getUserId());

        if(userDeviceToken == null) {
            System.out.println("디바이스 토큰이 없음");
            client.disconnect();
            return;
        }

        String title;
        Authority authority;
        String category;
        String deviceToken = userDeviceToken.getToken();

        ChatRoom chatRoom = chatRoomRepository.findTop1ByChatId(joinChatRoomRequest.getChatId());
        if(chatRoom != null) {
            title = chatRoom.getTitle();
            authority = Authority.USER;
            category = chatRoom.getCategory();
        }else {
            title = user.getName() + "님의 채팅방";
            authority = Authority.ADMIN;
            category = joinChatRoomRequest.getChatCategory();

            chatRoomImageRepository.save(
                    ChatRoomImage.builder()
                    .chatId(joinChatRoomRequest.getChatId())
                    .imageName("default")
                    .build()
            );

            roomRepository.save(
                    Rooms.builder()
                    .chatId(joinChatRoomRequest.getChatId())
                    .createAt(LocalDateTime.now())
                    .chatLength(0)
                    .build()
            );
        }
        Rooms room = roomRepository.findByChatId(joinChatRoomRequest.getChatId());

        client.joinRoom(joinChatRoomRequest.getChatId());
        printLog(joinChatRoomRequest.getChatId(), user, "joinRoom : ");

        Chat chat = chatRepository.save(
                Chat.builder()
                .id(room.getChatLength() + 1)
                .chatId(joinChatRoomRequest.getChatId())
                .message(user.getName() + "님이 참여하였습니다.")
                .messageType(MessageType.INFO)
                .writeAt(LocalDateTime.now())
                .userId(user.getUserId())
                .userName(user.getName())
                .build()
        );

        chatRoomRepository.save(
                ChatRoom.builder()
                        .chatId(joinChatRoomRequest.getChatId())
                        .authority(authority)
                        .deviceToken(deviceToken)
                        .title(title)
                        .category(category)
                        .build()
        );

        sendInfo(chat, room);
    }

    @Override
    public void leaveRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저가 없음");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(chatId)) {
            System.out.println("방이 없음");
            client.disconnect();
            return;
        }
        Optional<ChatRoomImage> chatRoomImage = chatRoomImageRepository.findByChatId(chatId);
        if(chatRoomImage.isPresent()) {
            System.out.println("챗방 이미지 없음");
            client.disconnect();
        }

        List<ChatRoom> list = chatRoomRepository.findAllByChatId(chatId);
        if(list.size() == 1) {
            File file = new File(imageChatDir, chatRoomImage.get().getImageName());
            if(file.exists())
                file.delete();

            chatRoomImageRepository.deleteByChatId(chatId);
        }

        client.leaveRoom(chatId);
        chatRoomRepository.deleteByChatIdAndUserId(chatId, user.getUserId());

        Rooms room = roomRepository.findByChatId(chatId);

        Chat chat = chatRepository.save(
                Chat.builder()
                        .id(room.getChatLength() + 1)
                        .chatId(chatId)
                        .message(user.getName() + "님이 채팅방을 떠났습니다")
                        .writeAt(LocalDateTime.now())
                        .messageType(MessageType.INFO)
                        .userId(user.getUserId())
                        .userName(user.getName())
                        .build()
        );


        sendInfo(chat, room);
    }

    @Override
    public void sendMessage(SocketIOClient client, MessageRequest messageRequest) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저가 없음");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(messageRequest.getChatId())) {
            System.out.println("채팅방이 없음");
            client.disconnect();
            return;
        }

        Optional<UserImage> oUserImage = userImageRepository.findByUserId(user.getUserId());

        if(!oUserImage.isPresent()) {
            System.out.println("user image not found");
            client.disconnect();
            return;
        }
        Rooms room = roomRepository.findByChatId(messageRequest.getChatId());

        printLog(messageRequest.getChatId(), user, "send user");

        Chat chat = chatRepository.save(
                Chat.builder()
                .id(room.getChatLength() + 1)
                .userId(user.getUserId())
                .userName(user.getName())
                .messageType(MessageType.MESSAGE)
                .chatId(messageRequest.getChatId())
                .message(messageRequest.getMessage())
                .writeAt(LocalDateTime.now())
                .build()
        );


        send(user, chat, oUserImage.get().getImageName(), room);
    }

    @Override
    public void changeTitle(SocketIOClient client, ChangeTitleRequest changeTitleRequest) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저가 없음");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(changeTitleRequest.getChatId())) {
            System.out.println("채팅방 없음");
            client.disconnect();
            return;
        }
        Rooms room = roomRepository.findByChatId(changeTitleRequest.getChatId());



        printLog(changeTitleRequest.getChatId(), user, "title update : ");
        Chat chat = chatRepository.save(
                Chat.builder()
                        .id(room.getChatLength() + 1)
                        .chatId(changeTitleRequest.getChatId())
                        .message(user.getName() + "님이 채팅방의 이름을 " + changeTitleRequest.getTitle() + "로 바꾸셨습니다.")
                        .writeAt(LocalDateTime.now())
                        .messageType(MessageType.INFO)
                        .userId(user.getUserId())
                        .build()
        );


        sendInfo(chat, room);
    }

    @Override
    public void changeChatRoomImage(SocketIOClient client, String chatId) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저가 없음");
            client.disconnect();
            return;
        }

        if(client.getAllRooms().contains(chatId)) {
            System.out.println("채팅방이 없음");
            client.disconnect();
            return;
        }
        Rooms room = roomRepository.findByChatId(chatId);

        printLog(chatId, user, "change image : ");
        Chat chat = chatRepository.save(
                Chat.builder()
                .id(room.getChatLength() + 1)
                .chatId(chatId)
                .message(user.getName() + "님이 채팅방 이미지를 변경했습니다.")
                .messageType(MessageType.INFO)
                .writeAt(LocalDateTime.now())
                .userId(user.getUserId())
                .userName(user.getName())
                .build()
        );

        sendInfo(chat, room);
    }

    @Override
    public void changeMessage(SocketIOClient client, String chatId) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("user not found");
            client.disconnect();
            return;
        }

        server.getRoomOperations(chatId).sendEvent("changeMessage");
    }

    @Override
    public void giveAuthority(SocketIOClient client, ChangeAuthorityRequest changeAuthorityRequest) {
        User user = client.get("user");

        User info = userRepository.findByInfo(changeAuthorityRequest.getUserInfo())
                .orElse(null);

        if(user == null) {
            System.out.println("user not found");
            client.disconnect();
            return;
        }

        if(client.getAllRooms().contains(changeAuthorityRequest.getChatId())) {
            System.out.println("room not found");
            client.disconnect();
            return;
        }

        if(info == null) {
            System.out.println("info not found");
            client.disconnect();
            return;
        }

        printLog(changeAuthorityRequest.getChatId(), user, "give authorities : ");
        Rooms room = roomRepository.findByChatId(changeAuthorityRequest.getChatId());
        Chat chat = chatRepository.save(
                Chat.builder()
                        .id(room.getChatLength() + 1)
                        .chatId(changeAuthorityRequest.getChatId())
                        .message(user.getName() + "님이 " + info.getName() + "에게 " + changeAuthorityRequest.getAuthority().toString() + "권한으로 바꾸었습니다.")
                        .messageType(MessageType.INFO)
                        .writeAt(LocalDateTime.now())
                        .userId(user.getUserId())
                        .userName(user.getName())
                        .build()
        );


        sendInfo(chat, room);
    }

    private void sendInfo(Chat chat, Rooms rooms) {
        server.getRoomOperations(chat.getChatId()).sendEvent("info",
                MessageResponse.builder()
                .messageId(chat.getId())
                .messageId(rooms.getChatLength() + 1)
                .messageType(chat.getMessageType())
                .chatId(chat.getChatId())
                .messge(chat.getMessage())
                .writeAt(LocalDateTime.now())
        );

        roomRepository.save(
            rooms.addLength()
        );

        fcmUtil.sendFCM(getDeviceTokens(chat), chat.getUserName(), chat.getMessage());
    }

    private void send(User user, Chat chat, String imageName, Rooms rooms) {
        server.getRoomOperations(chat.getChatId()).sendEvent("message",
                MessageResponse.builder()
                .messageId(chat.getId())
                .name(user.getName())
                .messge(chat.getMessage())
                .writeAt(LocalDateTime.now())
                .chatId(chat.getChatId())
                .messageType(chat.getMessageType())
                .isMine(user.getUserId().equals(chat.getUserId()))
                .imageName(imageName)
                .build()
        );

        roomRepository.save(
                rooms.addLength()
        );

        fcmUtil.sendFCM(getDeviceTokens(chat), chat.getUserName(), chat.getMessage());
    }

    private List<String> getDeviceTokens(Chat chat) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByChatId(chat.getChatId());
        List<String> tokens = new ArrayList<>();

        for(ChatRoom chatRoom : chatRoomList) {
            if(chatRoom.getDeviceToken() != null) {
                tokens.add(chatRoom.getDeviceToken());
            }else {
                System.out.println(chat.getUserName() + "님의 디바이스토큰이 없습니다.");
            }
        }

        return tokens;
    }

    private void printLog(String chatId, User user, String message) {
        String stringDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println("[" + stringDate + "] " + "chatId:" + chatId + message + user.getName());
    }



    @Override
    public void joinSoloRoom(SocketIOClient client, JoinSoloRoomRequest joinSoloRoomRequest) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저가 없음");
            client.disconnect();
            return;
        }

        Optional<SoloChatRoom> optionalSoloChatRoom = soloChatRoomRepository.findByUserIdAndFriendsInfo(user.getUserId(), joinSoloRoomRequest.getFriendInfo());
        if(!optionalSoloChatRoom.isPresent()) {
            System.out.println("유저가 이미 참여중임");
            client.disconnect();
            return;
        }

        Optional<User> friend = userRepository.findByInfo(joinSoloRoomRequest.getFriendInfo());
        if(friend.isPresent()) {
            System.out.println("유저가 이미 참여중임");
            client.disconnect();
            return;
        }

        UserDeviceToken userDeviceToken = userDeviceTokenRepository.findByUserId(user.getUserId());
        UserDeviceToken friendDeviceToken = userDeviceTokenRepository.findByUserId(friend.get().getUserId());
        if(userDeviceToken == null || friendDeviceToken.getToken() == null) {
            System.out.println("유저 디바이스 토큰이 없음");
            client.disconnect();
            return;
        }

        Optional<UserImage> friendImage = userImageRepository.findByUserId(friend.get().getUserId());
        Optional<UserImage> userImage = userImageRepository.findByUserId(user.getUserId());
        if(!userImage.isPresent() || !friendImage.isPresent()) {
            System.out.println("유저 이미지가 없음");
            client.disconnect();
            return;
        }
        Rooms rooms = roomRepository.save(
                Rooms.builder()
                        .chatId(joinSoloRoomRequest.getChatId())
                        .chatLength(0)
                        .createAt(LocalDateTime.now())
                        .build()
        );

        client.joinRoom(joinSoloRoomRequest.getChatId());
        printLog(joinSoloRoomRequest.getChatId(), user, "joinRoom : ");

        SoloChat soloChat = soloChatRepository.save(
                SoloChat.builder()
                .id(rooms.getChatLength() + 1)
                .chatId(joinSoloRoomRequest.getChatId())
                .message(friend.get().getName() + "님과 이제 대화합니다.")
                .messageType(MessageType.INFO)
                .writeAt(LocalDateTime.now())
                .userId(user.getUserId())
                .userName(user.getName())
                .userImageName(userImage.get().getImageName())
                .build()
        );

        soloChatRoomRepository.save(
                SoloChatRoom.builder()
                .userId(user.getUserId())
                .chatId(joinSoloRoomRequest.getChatId())
                .deviceToken(userDeviceToken.getToken())
                .friendsImageName(friendImage.get().getImageName())
                .friendsInfo(friend.get().getInfo())
                .build()
        );

        soloChatRoomRepository.save(
                SoloChatRoom.builder()
                .chatId(joinSoloRoomRequest.getChatId())
                .userId(friend.get().getUserId())
                .deviceToken(friendDeviceToken.getToken())
                .friendsImageName(userImage.get().getImageName())
                .friendsInfo(user.getInfo())
                .build()
        );

        sendSoloInfo(soloChat, rooms, userDeviceToken, friendDeviceToken);
    }

    @Override
    public void leaveSoloRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");
        if(user == null) {
            System.out.println("유저가 없음");
            client.disconnect();
            return;
        }

        if(!client.getAllRooms().contains(chatId)) {
            System.out.println("방이 없음");
            client.disconnect();
            return;
        }

        client.leaveRoom(chatId);
        soloChatRepository.deleteByChatId(chatId);
        soloChatRoomRepository.deleteByChatId(chatId);
    }

    @Override
    public void sendSoloMessage(SocketIOClient client, MessageRequest messageRequest) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("유저가 없음");
            client.disconnect();
            return;
        }

        Optional<SoloChatRoom> optionalChatRoom = soloChatRoomRepository
                .findByChatIdAndUserId(messageRequest.getChatId(), user.getUserId());
        if(optionalChatRoom.isPresent()) {
            System.out.println("챗방이 없음");
            client.disconnect();
            return;
        }

        Optional<UserImage> oUserImage = userImageRepository.findByUserId(user.getUserId());

        if(!oUserImage.isPresent()) {
            System.out.println("user image not found");
            client.disconnect();
            return;
        }

        Optional<User> mine = userRepository.findByUserId(user.getUserId());
        Optional<User> friend = userRepository.findByInfo(optionalChatRoom.get().getFriendsInfo());
        if(mine.isPresent() || friend.isPresent()) {
            System.out.println("유저가 없습니다.");
            client.disconnect();
            return;
        }

        UserDeviceToken userDeviceToken = userDeviceTokenRepository
                .findByUserId(mine.get().getUserId());
        UserDeviceToken friendDeviceToken = userDeviceTokenRepository
                .findByUserId(friend.get().getUserId());
        if(userDeviceToken == null || friendDeviceToken == null) {
            System.out.println("유저 토큰이 없음");
            client.disconnect();
            return;
        }

        printLog(messageRequest.getChatId(), user, "send user");
        Rooms room = roomRepository.findByChatId(messageRequest.getChatId());

        SoloChat soloChat = soloChatRepository.save(
                SoloChat.builder()
                        .chatId(messageRequest.getChatId())
                        .id(room.getChatLength()+1)
                        .message(messageRequest.getMessage())
                        .userName(user.getName())
                        .userImageName(oUserImage.get().getImageName())
                        .messageType(MessageType.MESSAGE)
                        .writeAt(LocalDateTime.now())
                        .build()
        );

        sendSolo(user, soloChat, room, userDeviceToken.getToken(), friendDeviceToken.getToken());
    }

    private void sendSoloInfo(SoloChat soloChat, Rooms rooms, UserDeviceToken userDeviceToken, UserDeviceToken friendDeviceToken) {
        server.getRoomOperations(soloChat.getChatId()).sendEvent("info",
                MessageResponse.builder()
                        .messageId(rooms.getChatLength() + 1)
                        .messageType(soloChat.getMessageType())
                        .chatId(soloChat.getChatId())
                        .messge(soloChat.getMessage())
                        .writeAt(LocalDateTime.now())
        );

        roomRepository.save(
                roomRepository.findByChatId(soloChat.getChatId())
                        .addLength()
        );

        List<String> tokens = new ArrayList<>();
        tokens.add(userDeviceToken.getToken());
        tokens.add(friendDeviceToken.getToken());

        fcmUtil.sendFCM(tokens, soloChat.getUserName(), soloChat.getMessage());
    }

    private void sendSolo(User user, SoloChat soloChat, Rooms rooms, String userDeviceToken, String friendDeviceToken) {
        server.getRoomOperations(soloChat.getChatId()).sendEvent("info",
                MessageResponse.builder()
                        .messageId(rooms.getChatLength() + 1)
                        .isMine(user.getUserId().equals(soloChat.getUserId()))
                        .imageName(soloChat.getUserImageName())
                        .messageType(soloChat.getMessageType())
                        .chatId(soloChat.getChatId())
                        .messge(soloChat.getMessage())
                        .writeAt(LocalDateTime.now())
                        .messge(soloChat.getMessage())
                .build()
        );

        roomRepository.save(
                roomRepository.findByChatId(soloChat.getChatId())
                        .addLength()
        );

        List<String> tokens = new ArrayList<>();
        tokens.add(userDeviceToken);
        tokens.add(friendDeviceToken);

        fcmUtil.sendFCM(tokens, soloChat.getUserName(), soloChat.getMessage());
    }
}
