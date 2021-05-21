package com.example.chat_example_with_socketio.service.solo_message;

import com.example.chat_example_with_socketio.entity.chat.Chat;
import com.example.chat_example_with_socketio.entity.chat.repository.ChatRepository;
import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import com.example.chat_example_with_socketio.entity.image.user.repository.UserImageRepository;
import com.example.chat_example_with_socketio.entity.solo_chat.SoloChat;
import com.example.chat_example_with_socketio.entity.solo_chat.repository.SoloChatRepository;
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
public class SoloMessageServiceImpl implements SoloMessageService {

    private final UserRepository userRepository;
    private final SoloChatRepository soloChatRepository;
    private final UserImageRepository userImageRepository;

    private final JwtProvider jwtProvider;

    @Override
    public List<MessageResponse> getMessages(String token, String chatId, Pageable pageable) {
        User mine = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<SoloChat> soloChats = soloChatRepository.findAllByChatId(chatId, pageable);

        List<MessageResponse> response = new ArrayList<>();

        for(SoloChat soloChat : soloChats) {
            UserImage userImage = userImageRepository.findByUserId(soloChat.getUserId())
                    .orElseThrow(UserImageNotFoundException::new);

            MessageResponse messageResponse = MessageResponse.builder()
                    .chatId(chatId)
                    .messageType(soloChat.getMessageType())
                    .name(soloChat.getUserName())
                    .messge(soloChat.getMessage())
                    .writeAt(soloChat.getWriteAt())
                    .imageName(userImage.getImageName())
                    .isMine(mine.getUserId().equals(soloChat.getUserId()))
                    .build();

            response.add(messageResponse);
        }

        return response;
    }

    @Override
    public void updateMessage(String token, UpdateMessageRequest updateMessageRequest) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        SoloChat soloChat = soloChatRepository.findByChatIdAndId(updateMessageRequest.getChatId(), updateMessageRequest.getMessageId())
                .orElseThrow(MessageNotFoundException::new);

        soloChatRepository.save(
                soloChat.updateChat(updateMessageRequest.getMessage())
        );
    }

    @Override
    public void deleteMessage(String token, Integer messageId, String chatId) {
        userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        SoloChat chat = soloChatRepository.findByChatIdAndId(chatId, messageId)
                .orElseThrow(MessageNotFoundException::new);

        soloChatRepository.save(
                chat.updateChat("메세지가 삭제되었습니다.")
        );
    }
}
