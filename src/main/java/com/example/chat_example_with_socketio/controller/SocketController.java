package com.example.chat_example_with_socketio.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.chat_example_with_socketio.payload.request.*;
import com.example.chat_example_with_socketio.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SocketController {

    private final SocketIOServer server;

    private final SocketService socketService;

    @PostConstruct
    public void chatSocket() {
        server.addConnectListener(socketService::connect);
        server.addDisconnectListener(socketService::disconnect);
        server.addEventListener("joinRoom", JoinChatRoomRequest.class,
                (client, data, ackSender) -> { socketService.joinRoom(client, data); });
        server.addEventListener("leaveRoom", String.class,
                (client, data, ackSender) -> { socketService.leaveRoom(client, data); });
        server.addEventListener("sendMessage", MessageRequest.class,
                (client, data, ackSender) -> { socketService.sendMessage(client, data); });
        server.addEventListener("changeTitle", ChangeTitleRequest.class,
                ((client, data, ackSender) -> { socketService.changeTitle(client, data); }));
        server.addEventListener("changeImage", String.class,
                ((client, data, ackSender) -> { socketService.changeChatRoomImage(client, data); }));
        server.addEventListener("changeMessage", String.class,
                ((client, data, ackSender) -> { socketService.changeMessage(client, data); }));
        server.addEventListener("changeAuthority", ChangeAuthorityRequest.class,
                ((client, data, ackSender) -> { socketService.giveAuthority(client, data); }));

        server.addEventListener("joinSoloRoom", JoinSoloRoomRequest.class,
                ((client, data, ackSender) -> { socketService.joinSoloRoom(client, data); }));
        server.addEventListener("leaveSoloRoom", String.class,
                (((client, data, ackSender) -> { socketService.leaveSoloRoom(client, data); })));
        server.addEventListener("sendSoloMessage", MessageRequest.class,
                (((client, data, ackSender) -> { socketService.sendSoloMessage(client, data); })));

    }
}
