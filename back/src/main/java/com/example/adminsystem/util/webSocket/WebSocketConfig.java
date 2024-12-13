package com.example.adminsystem.util.webSocket;


import com.example.adminsystem.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private TokenUtil tokenUtil;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/channel/user")//设置连接路径和处理
                .setAllowedOrigins("*")
                .addInterceptors(new MyWebSocketInterceptor());//设置拦截器
    }
    /**
     * 自定义拦截器拦截WebSocket请求
     * 初次握手访问后，将前端自定义协议头Sec-WebSocket-Protocol原封不动返回回去，否则会报错
     */
    class MyWebSocketInterceptor implements HandshakeInterceptor {
        //前置拦截一般用来注册用户信息，绑定 WebSocketSession
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            System.out.println("前置拦截~~");
            if (!(request instanceof ServletServerHttpRequest)) return true;
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String userName = "";
            HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
            if (!servletRequest.getHeader("Sec-WebSocket-Protocol").isEmpty()) {
                userName = tokenUtil.parseToken(servletRequest.getHeader("Sec-WebSocket-Protocol")).get("userName");
                httpResponse.addHeader("Sec-WebSocket-Protocol", servletRequest.getHeader("Sec-WebSocket-Protocol"));
            }
            attributes.put("userName", userName);
            return true;
        }
        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
            System.out.println("后置拦截~~");
        }
    }
}
