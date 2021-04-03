package com.example.chat_example_with_socketio.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.chat_example_with_socketio.payload.request.ChangeTitleRequest;
import com.example.chat_example_with_socketio.payload.request.MessageRequest;
import com.example.chat_example_with_socketio.service.SocketService;
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
        server.addEventListener("joinRoom", String.class,
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
    }
}
