package com.example.chat_example_with_socketio.service.solo_chat_room;

import com.example.chat_example_with_socketio.entity.chat.Chat;
import com.example.chat_example_with_socketio.entity.solo_chat.SoloChat;
import com.example.chat_example_with_socketio.entity.solo_chat.repository.SoloChatRepository;
import com.example.chat_example_with_socketio.entity.solo_chat_room.SoloChatRoom;
import com.example.chat_example_with_socketio.entity.solo_chat_room.repository.SoloChatRoomRepository;
import com.example.chat_example_with_socketio.entity.user.User;
import com.example.chat_example_with_socketio.entity.user.repository.UserRepository;
import com.example.chat_example_with_socketio.error.exceptions.MessageNotFoundException;
import com.example.chat_example_with_socketio.error.exceptions.UserNotFoundException;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import com.example.chat_example_with_socketio.payload.response.SoloRoomResponse;
import com.example.chat_example_with_socketio.security.JwtProvider;
import com.example.chat_example_with_socketio.service.solo_chat_room.SoloChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoloChatRoomServiceImpl implements SoloChatRoomService {

    private final SoloChatRoomRepository soloChatRoomRepository;
    private final UserRepository userRepository;
    private final SoloChatRepository soloChatRepository;

    private final JwtProvider jwtProvider;

    @Override
    public List<SoloRoomResponse> getChatRoom(String token, PageRequest pageRequest) {
        User user = userRepository.findByUserId(jwtProvider.getUserId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<SoloChatRoom> soloChatRoomServices = soloChatRoomRepository.findAllByUserId(user.getUserId(), pageRequest);
        List<SoloRoomResponse> responses = new ArrayList<>();

        for(SoloChatRoom soloChatRoom : soloChatRoomServices) {
            SoloChat soloChat = soloChatRepository.findByChatIdOrderByWriteAtDesc(soloChatRoom.getChatId())
                    .orElseThrow(MessageNotFoundException::new);

            SoloRoomResponse soloRoomResponse = SoloRoomResponse.builder()
                    .chatId(soloChatRoom.getChatId())
                    .friendImageName(soloChatRoom.getFriendsImageName())
                    .topMessage(soloChat.getMessage())
                    .friendName(soloChat.getUserName())
                    .build();

            responses.add(soloRoomResponse);
        }

        return responses;
    }
}
