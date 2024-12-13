package com.example.adminsystem.util.webSocket;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler implements WebSocketHandler {
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userName = session.getAttributes().get("userName").toString();
        SESSIONS.put(userName, session);
        System.out.println(String.format("成功建立连接~ userName: %s", userName));
    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msg = message.getPayload().toString();
        System.out.println(msg);
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("连接出错");
        if (session.isOpen()) {
            session.close();
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭,status:" + closeStatus);
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    /**
     * 指定发消息
     *
     * @param message
     */
    public static void sendMessage(String userName, String message) {
        WebSocketSession webSocketSession = SESSIONS.get(userName);
        if (webSocketSession == null || !webSocketSession.isOpen()) return;
        try {
            webSocketSession.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发消息
     *
     * @param message
     */
    public static void fanoutMessage(String message) {
        SESSIONS.keySet().forEach(us -> sendMessage(us, message));
    }
    // 向指定用户发送信息
    public void sendUserMessage(String userName, String message) {
        sendMessage(userName,message);
        System.out.println(SESSIONS);
    }
    // 返回当前SESSIONS
    public Map<String, WebSocketSession> getAllSessions() {
        return SESSIONS;
    }
    // 断开webSocket连接
    public void exitWebSocket(String username) throws IOException {
        if (SESSIONS.get(username) != null) {
            SESSIONS.get(username).close();
            SESSIONS.remove(username);
        }
    }
    // 用户是否ws连接
    public Boolean isUserLogin(String username) throws IOException {
        if (SESSIONS.get(username) != null) {
            return true;
        } else {
            return false;
        }
    }
}
