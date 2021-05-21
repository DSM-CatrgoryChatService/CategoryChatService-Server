package com.example.chat_example_with_socketio.service.message;

import com.example.chat_example_with_socketio.entity.chat.Chat;
import com.example.chat_example_with_socketio.entity.chat.repository.ChatRepository;
import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import com.example.chat_example_with_socketio.entity.image.user.repository.UserImageRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.error.exceptions.MessageNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.UserImageNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.UserNotFoundException;
import com.example.chat_example_with_socketio.payload.request.UpdateMessageRequest;
import com.example.chat_example_with_socketio.payload.response.MessageResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final UserImageRepository userImageRepository;

    private final JwtProvider jwtProvider;

    @Override
    public List<MessageResponse> getMessages(String token, String chatId, Pageable pageable) {
        User mine = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<Chat> chatPage = chatRepository.findAllByChatId(chatId, pageable);

        List<MessageResponse> response = new ArrayList<>();

        for(Chat chat : chatPage) {
            UserImage userImage = userImageRepository.findByUserId(chat.getUserId())
                    .orElseThrow(UserImageNotFoundException::new);

            MessageResponse messageResponse = MessageResponse.builder()
                    .chatId(chatId)
                    .messageType(chat.getMessageType())
                    .name(chat.getUserName())
                    .messge(chat.getMessage())
                    .writeAt(chat.getWriteAt())
                    .imageName(userImage.getImageName())
                    .isMine(mine.getUserId().equals(chat.getUserId()))
                    .build();

            response.add(messageResponse);
        }

        return response;
    }

    @Override
    public void updateMessage(String token, UpdateMessageRequest updateMessageRequest) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        Chat chat = chatRepository.findByChatIdAndId(updateMessageRequest.getChatId(), updateMessageRequest.getMessageId())
                .orElseThrow(MessageNotFoundException::new);

        chatRepository.save(
                chat.updateMessage(updateMessageRequest.getMessage())
        );
    }

    @Override
    public void deleteMessage(String token, Integer messageId, String chatId) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        Chat chat = chatRepository.findByChatIdAndId(chatId, messageId)
                .orElseThrow(MessageNotFoundException::new);

        chatRepository.save(
                chat.updateMessage("메세지가 삭제되었습니다.")
        );
    }
}
