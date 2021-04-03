package com.example.chat_example_with_socketio.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.chat_example_with_socketio.entity.chat.Chat;
import com.example.chat_example_with_socketio.entity.chat.enums.MessageType;
import com.example.chat_example_with_socketio.entity.chat.repository.ChatRepository;
import com.example.chat_example_with_socketio.entity.chat_room.ChatRoom;
import com.example.chat_example_with_socketio.entity.chat_room.enums.Authority;
import com.example.chat_example_with_socketio.entity.chat_room.repository.ChatRoomRepository;
import com.example.chat_example_with_socketio.entity.image.chatRoom.ChatRoomImage;
import com.example.chat_example_with_socketio.entity.image.chatRoom.repository.ChatRoomImageRepository;
import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import com.example.chat_example_with_socketio.entity.image.user.repository.UserImageRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.payload.request.ChangeTitleRequest;
import com.example.chat_example_with_socketio.payload.request.MessageRequest;
import com.example.chat_example_with_socketio.payload.response.MessageResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService{

    private final SocketIOServer server;

    private final UserRepository userRepository;
    private final ChatRoomImageRepository chatRoomImageRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final JwtProvider jwtProvider;

    private final UserImageRepository userImageRepository;

    @Value("${image.chat.dir}")
    private String imageChatDir;

    @Override
    public void connect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if(!jwtProvider.validateToken(token)) {
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
    public void joinRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");

        if(user == null) {
            System.out.println("user not found");
            client.disconnect();
            return;
        }

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByChatRoomIdAndUserId(chatId, user.getUserId());
        if(optionalChatRoom.isPresent()) {
            System.out.println("user already joined");
            client.disconnect();
            return;
        }

        String title;
        Authority authority;
        ChatRoom chatRoom = chatRoomRepository.findTop1ByChatRoomId(chatId);
        if(chatRoom != null) {
            title = chatRoom.getTitle();
            authority = Authority.USER;
        }else {
            title = user.getName() + "님의 채팅방";
            authority = Authority.ADMIN;

            chatRoomImageRepository.save(
                    ChatRoomImage.builder()
                    .chatId(chatId)
                    .imageName("default")
                    .build()
            );
        }

        client.joinRoom(chatId);
        printLog(chatId, user, "joinRoom : ");

        Chat chat = chatRepository.save(
                Chat.builder()
                .chatId(chatId)
                .message(user.getName() + "님이 참여하였습니다.")
                .messageType(MessageType.INFO)
                .writeAt(LocalDateTime.now())
                .userId(user.getUserId())
                .userName(user.getName())
                .build()
        );

        chatRoomRepository.save(
                ChatRoom.builder()
                .chatRoomId(chatId)
                .authority(authority)
                .title(title)
                .build()
        );

        sendInfo(user, chat);
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

        client.leaveRoom(chatId);
        chatRoomRepository.deleteByChatRoomIdAndUserId(chatId, user.getUserId());
        ChatRoomImage chatRoomImage = chatRoomImageRepository.findByChatId(chatId)
                .orElseThrow(RuntimeException::new);

        File file = new File(imageChatDir, chatRoomImage.getImageName());
        if(file.exists())
            file.delete();

        chatRoomImageRepository.deleteByChatId(chatId);

        Chat chat = chatRepository.save(
                Chat.builder()
                        .chatId(chatId)
                        .message("님이 채팅방을 떠났습니다")
                        .writeAt(LocalDateTime.now())
                        .messageType(MessageType.INFO)
                        .userId(user.getUserId())
                        .userName(user.getName())
                        .build()
        );

        sendInfo(user, chat);
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

        printLog(messageRequest.getChatId(), user, "send user");

        Chat chat = chatRepository.save(
                Chat.builder()
                .userId(user.getUserId())
                .userName(user.getName())
                .messageType(MessageType.MESSAGE)
                .chatId(messageRequest.getChatId())
                .message(messageRequest.getMessage())
                .writeAt(LocalDateTime.now())
                .build()
        );

        send(user, chat, oUserImage.get().getImageName());
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

        printLog(changeTitleRequest.getChatId(), user, "title update : ");
        Chat chat = chatRepository.save(
                Chat.builder()
                        .chatId(changeTitleRequest.getChatId())
                        .message("님이 채팅방의 이름을 " + changeTitleRequest.getTitle() + "로 바꾸셨습니다.")
                        .writeAt(LocalDateTime.now())
                        .messageType(MessageType.INFO)
                        .userId(user.getUserId())
                        .build()
        );

        sendInfo(user, chat);
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

        printLog(chatId, user, "change image : ");
        Chat chat = chatRepository.save(
                Chat.builder()
                .chatId(chatId)
                .message("님이 채팅방 이미지를 변경했습니다.")
                .messageType(MessageType.INFO)
                .writeAt(LocalDateTime.now())
                .userId(user.getUserId())
                .userName(user.getName())
                .build()
        );

        sendInfo(user, chat);
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

    private void sendInfo(User user, Chat chat) {
        server.getRoomOperations(chat.getChatId()).sendEvent("info",
                MessageResponse.builder()
                .messageType(chat.getMessageType())
                .chatId(chat.getChatId())
                .messge(chat.getMessage())
                .writeAt(LocalDateTime.now())
        );
    }

    private void send(User user, Chat chat, String imageName) {
        server.getRoomOperations(chat.getChatId()).sendEvent("message",
                MessageResponse.builder()
                .name(user.getName())
                .messge(chat.getMessage())
                .writeAt(LocalDateTime.now())
                .chatId(chat.getChatId())
                .messageType(chat.getMessageType())
                .isMine(user.getUserId().equals(chat.getUserId()))
                .imageName(imageName)
                .build()
        );
    }

    private void printLog(String chatId, User user, String message) {
        String stringDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        System.out.println("[" + stringDate + "] " + "chatId:" + chatId + message + user.getName());
    }
}
