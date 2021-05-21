package com.example.chat_example_with_socketio.service.chat_room;

import com.example.chat_example_with_socketio.entity.chat.Chat;
import com.example.chat_example_with_socketio.entity.chat.repository.ChatRepository;
import com.example.chat_example_with_socketio.entity.chat_room.ChatRoom;
import com.example.chat_example_with_socketio.entity.chat_room.enums.Authority;
import com.example.chat_example_with_socketio.entity.chat_room.repository.ChatRoomRepository;
import com.example.chat_example_with_socketio.entity.image.chatRoom.ChatRoomImage;
import com.example.chat_example_with_socketio.entity.image.chatRoom.repository.ChatRoomImageRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.error.exceptions.ChatRoomImageNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.ChatRoomNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.MessageNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.UserNotFoundException;
import com.example.chat_example_with_socketio.payload.request.ChangeAuthorityRequest;
import com.example.chat_example_with_socketio.payload.request.ChangeTitleRequest;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomImageRepository chatRoomImageRepository;
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    @Value("${image.chat.dir}")
    private String imageChatDir;

    private List<ChatRoomResponse> setChatListResponse(Page<ChatRoom> chatRoomList) {
        List<ChatRoomResponse> chatRoomResponseList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            Chat chat = chatRepository.findByChatIdOrderByWriteAtDesc(chatRoom.getChatId())
                    .orElseThrow(MessageNotFoundException::new);

            ChatRoomImage chatRoomImage = chatRoomImageRepository.findByChatId(chatRoom.getChatId())
                    .orElseThrow(ChatRoomImageNotFoundException::new);

            ChatRoomResponse chatRoomResponse = ChatRoomResponse.builder()
                    .chatId(chatRoom.getChatId())
                    .title(chatRoom.getTitle())
                    .topMessage(chat.getMessage())
                    .category(chatRoom.getCategory())
                    .imageName(chatRoomImage.getImageName())
                    .build();

            chatRoomResponseList.add(chatRoomResponse);
        }
        Collections.reverse(chatRoomResponseList);

        return chatRoomResponseList;
    }

    @Override
    public List<ChatRoomResponse> getChatRoom(String token, PageRequest pageRequest) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<ChatRoom> chatRoomList = chatRoomRepository.findAllByUserId(user.getUserId(), pageRequest);

        return setChatListResponse(chatRoomList);
    }

    @Override
    public List<ChatRoomResponse> searchWithCategory(String token, String chatCategory, PageRequest pageRequest) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<ChatRoom> chatRoomList = chatRoomRepository.findAllByCategory(chatCategory, pageRequest);

        return setChatListResponse(chatRoomList);
    }

    @Override
    public void exitRoom(String chatId, String token) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        chatRoomImageRepository.findByChatId(chatId)
                .orElseThrow(ChatRoomNotFoundException::new);

        chatRoomImageRepository.findByChatId(chatId)
                .orElseThrow(ChatRoomImageNotFoundException::new);
    }

    @Override
    public void updateTitle(ChangeTitleRequest changeTitleRequest, String token) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        ChatRoom chatRoom = chatRoomRepository.findByChatIdAndUserId(changeTitleRequest.getChatId(), user.getUserId())
                .orElseThrow(ChatRoomNotFoundException::new);

        chatRoomRepository.save(
                chatRoom.update(changeTitleRequest.getTitle())
        );
    }

    @Override
    public void updateAuthority(String token, ChangeAuthorityRequest changeAuthorityRequest) {
        User mine = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        userRepository.findByInfo(changeAuthorityRequest.getUserInfo())
                .orElseThrow(UserNotFoundException::new);

        ChatRoom user = chatRoomRepository.findByUserInfoAndChatId(changeAuthorityRequest.getUserInfo(), changeAuthorityRequest.getChatId())
                .orElseThrow(ChatRoomNotFoundException::new);

        ChatRoom leader = chatRoomRepository.findByChatIdAndUserId(changeAuthorityRequest.getChatId(), mine.getUserId())
                .orElseThrow(ChatRoomNotFoundException::new);

        if(!leader.getAuthority().equals(Authority.MANAGER) || !leader.getAuthority().equals(Authority.ADMIN)) {
            throw new RuntimeException();
        }

        chatRoomRepository.save(
                user.giveAuthority(changeAuthorityRequest.getAuthority())
        );
    }

    @SneakyThrows
    @Override
    public void changeRoomImage(String chatId, String token, MultipartFile image) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        ChatRoomImage chatRoomImage = chatRoomImageRepository.findByChatId(chatId)
                .orElseThrow(ChatRoomNotFoundException::new);

        if(!chatRoomImage.getImageName().equals("default")) {
            File file = new File(imageChatDir, chatRoomImage.getImageName());
            if (file.exists())
                file.delete();
        }
        String newImageName = UUID.randomUUID().toString();

        chatRoomImageRepository.save(chatRoomImage.update(newImageName));

        image.transferTo(new File(imageChatDir, chatRoomImage.getImageName()));
    }
}
