import {
	createApp
} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {
	router,
	initMenu
} from './router/router.js'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {isNotEmpty} from '@/js/isNotEmpty.js'
// import {websocket} from "@/js/webSocket"


var app = createApp(App)


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component)
}



async function init() {
	if (localStorage.getItem("jwt")) {
		await initMenu("/system");
	}
}

init()

// 导航守卫
router.beforeEach((to, from, next) => {
	if(to.path === "/index") {
		let jwt = localStorage.getItem("jwt");
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
	}
	console.log(localStorage.getItem("jwt"))
	// init()
	window.document.title = to.meta.title == undefined ? '默认标题' : to.meta.title

	// if (sessionStorage.getItem("jwt") == "" && router.path != "/") {
	// 	router.replace("/")
	// }
	next()
	// await init()
	// if(to.path === '/index' && router.getRoutes().length == 2) {
	// 	location.reload()
	// }
	// next()
})

// // 切换账号后更新路由 后置路由刷新页面
router.afterEach((to, from, next) => {
	if (to.path === '/index' && router.getRoutes().length == 2) {
		console.log(localStorage.getItem("jwt"))
		console.log(isNotEmpty(localStorage.getItem("jwt")))
		if (isNotEmpty(localStorage.getItem("jwt"))) {
			location.reload()
		} else {
			router.replace("/")
		}
	}
})

const debounce = (fn, delay) => {
	let timer
	return (...args) => {
		if (timer) {
			clearTimeout(timer)
		}
		timer = setTimeout(() => {
			fn(...args)
		}, delay)
	}
}

// 防抖函数 防止重复渲染
const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
	constructor(callback) {
		callback = debounce(callback, 200);
		super(callback);
	}
}



async function call() {
	app.use(router).use(ElementPlus).mount('#app')
}
call()