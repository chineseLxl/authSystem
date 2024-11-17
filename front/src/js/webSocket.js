import {
	router,
} from '@/router/router.js'

let jwt = sessionStorage.getItem("jwt");
let websocket = new WebSocket("ws://localhost:8081/channel/user", [jwt]);

// 连接断开
websocket.onclose = e => {
	console.log(`连接关闭: code=${e.code}, reason=${e.reason}`)
	// sessionStorage.setItem("jwt","")
	// location.replace("/")
	router.replace("/")
}
// 收到消息
websocket.onmessage = e => {
	console.log(`收到消息：${e.data}`);
}
// 异常
websocket.onerror = e => {
	console.log("连接异常")
	console.error(e)
}
// 连接打开
websocket.onopen = e => {
	console.log("连接打开");

	// 创建连接后，往服务器连续写入3条消息
	websocket.send("会话测试111111");

	// 由客户端主动断开连接
	// websocket.close();
}

export {
	websocket
}
