import Login from '@/components/Login.vue'
import Index from '@/components/Index.vue'
const modules = import.meta.glob('@/components/*.vue')
import {
	createWebHistory,
	createRouter,
} from "vue-router";
const routes = [ //配置路由，这里是个数组
	{ //每一个链接都是一个对象
		path: '/', //链接路径
		name: 'login', //路由名称，
		component: Login, //对应的组件模板
		meta: {
			title: "登录",
		}
	},
	{
		path: '/index',
		name: 'index',
		component: Index,
		meta: {
			title: "管理页",
		},
		children: []
	},
	// {
	// 	path: '/system',
	// 	children: [{
	// 			path: 'user',
	// 			name: '用户管理',
	// 			component: UserSystem,
	// 			meta: {
	// 				title: "路由管理"
	// 			},
	// 			children: [

	// 			]
	// 		},
	// 		{
	// 			path: 'route',
	// 			name: '路由管理',
	// 			// component: RouteSystem,
	// 			meta: {
	// 				title: "路由管理"
	// 			},
	// 			children: [{
	// 				path: 'addRoute',
	// 				name: '添加路由',
	// 				component: AddRoute,
	// 			}]
	// 		},
	// 	]
	// }

]

var router = createRouter({
	history: createWebHistory(),
	routes,
});

const resetRouter = () => {
	const newRouter = createRouter({
		history: createWebHistory(),
		routes,
	});
	router.matcher = newRouter.matcher // 新路由实例matcer，赋值给旧路由实例的matcher，（相当于replaceRouter）
}


// await router.beforeEach(async(to, from, next) => {
// 	var systemRoute = {
// 		"path": "/system",
// 		"children": [],
// 	}
// 	for (let value of JSON.parse(localStorage.getItem("routeList"))) {
// 		const comp = import("@/components/" + value.component);
// 		var routeObj = {
// 			// "path": "/system" + value.path,
// 			"path": value.path,
// 			"name": value.pname,
// 			"meta": value.meta,
// 			"component": import("@/components/" + value.component),
// 			"children": []
// 			// "component": modules[`${value.component}`],
// 		}
// 		systemRoute.children.push(routeObj)
// 	}
// 	routes.push(systemRoute)
// 	router.addRoute(systemRoute)

// 	next()

// })
// 管理控制台动态路由
function initMenu() {
	var systemRoute = {
		"path": "/",
		"name": "system",
		"children": [],
	}
	for (let value of JSON.parse(localStorage.getItem("routeList"))) {
		let comp = ""
		// 子路由设置
		var clist = []
		if (value.cname == "根目录") {
			for (let p of value.pageList) {
				var routeObj = {
					// "path": "/system" + value.path,
					"path": value.path + "/" + p.path,
					"name": p.pname,
					"meta": {
						"fmenu": "-",
					},
					// "auth": value.auth,
					// "component": defineAsyncComponent(() => import("@/components/" + value.component)),
					"component": modules[`/src/components/${p.component}`],
					"children": []
				}
				systemRoute.children.push(routeObj)
			}
			continue;
		}
		if (value.pageList.length != 0) {
			for (let cValue of value.pageList) {
				// console.log(cValue.component)
				// console.log(modules[`/src/components/${cValue.component}`])
				clist.push({
					"path": cValue.path,
					"name": cValue.pname,
					"meta": {
						"fmenu": value.pname
					},
					"auth": cValue.auth,
					// "component": defineAsyncComponent(() => import("@/components/" + cValue.component)),
					// "component": (resolve) => require([`@/components/${cValue.component}`], resolve),
					// "component": () => import(`@/components/${cValue.component}`),
					// component: () => import(`@/components/` + cValue.component),
					"component": modules[`/src/components/${cValue.component}`],
					"children": [],
				})
			}
			console.log(clist)
		} else {
			clist = []
		}

		// if(clist.length == 0) {
		// 	comp = () => import("@/components/" + value.component)
		// } else {
		// 	comp = ""
		// }
		var routeObj = {
			// "path": "/system" + value.path,
			"path": value.path,
			"name": value.cname,
			"meta": {
				"fmenu": "-",
			},
			// "auth": value.auth,
			"children": clist,
			// "component": modules[`${value.component}`],
		}
		systemRoute.children.push(routeObj)
	}
	// routes.push(systemRoute)
	router.addRoute(systemRoute)
	router.options.routes.push(systemRoute)
	// console.log(sessionStorage.getItem("routeList"))
}

export {
	router,
	routes,
	resetRouter,
	initMenu,
}