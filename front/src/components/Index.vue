<template>
	<el-row class="tac">
		<el-col :span="2">
			<h5 class="mb-2">Default title</h5>
			<!-- <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" @click="handleClick"> -->
			<el-menu default-active="2">
				<el-sub-menu :index=index :path=item.path v-for="(item,index) in routeData.group">
					<template #title>
						<!-- <el-icon>
							<location />
						</el-icon> -->
						<span>{{item.name}}</span>
					</template>
					<el-menu-item-group v-for="(i,j) in item.children" @click="JumpRoute(item.path+'/'+i.path)">
						<el-menu-item :index='`${index}_${j}`'>{{i.name}}</el-menu-item>
					</el-menu-item-group>
				</el-sub-menu>
				<el-menu-item :index=index+routeData.group.length v-for="(item,index) in routeData.menu" @click="JumpRoute(item.path)">
					<el-icon><icon-menu /></el-icon>
					<span>{{item.name}}</span>
				</el-menu-item>
			</el-menu>
		</el-col>
		<el-col :span="21">
			<h5 class="mb-2"><el-button @click="outlogin()">退出登录</el-button></h5>
			<iframe id="menuDiv" src="/user/userList" frameborder="0" width="100%"></iframe>
		</el-col>
	</el-row>
</template>

<script>
	import {http} from '@/js/http.js'
	import {router} from '@/router/router';


	export default {
		data() {
			return {
				routeData: {
					'menu': [],
					'group': []
				}
			}
		},
		created() {

		},
		mounted() {
			console.log(router.getRoutes())
			// console.log(router.options.routes)
			// 根据路由添加侧边栏标签
			for (var i = 0; i < router.options.routes.length; i++) {
				if (router.options.routes[i].path == '/' && router.options.routes[i].children != null) {
					for (var j = 0; j < router.options.routes[i].children.length; j++) {
						if (router.options.routes[i].children[j].children.length != 0) {
							this.routeData.group.push(router.options.routes[i].children[j])
						} else {
							this.routeData.menu.push(router.options.routes[i].children[j])
						}
					}
				}
			}
			// var ws = new WebSocket('ws://localhost:8081/myWebSocket');
			
			// ws.onopen = function(event) {
			// 	console.log("Connection open ...");
			// 	ws.send("Hello Server!");
			// };
			
			// ws.onmessage = function(event) {
			// 	console.log("Received from server: " + event.data);
			// };
			
			// ws.onclose = function(event) {
			// 	console.log("Connection closed.");
			// };
			// this.GetRouteFun();
			// console.log("首页")
			
		},
		methods: {
			handleOpen(key, keyPath) {
				console.log(key, keyPath);
			},
			handleClose(key, keyPath) {
				console.log(key, keyPath);
			},
			handleClick(key, keyPath) {
				console.log(key, keyPath);
			},
			// 子页面跳转
			JumpRoute(r) {
				console.log(r)
				document.getElementById('menuDiv').setAttribute('src', r)
			},
			outlogin() {
				router.replace("/")
				sessionStorage.setItem("jwt", "")
				sessionStorage.setItem("routeList", "")
			}
			// GetRouteFun() {
			// 	http.post("/getRoute", {
			// 		Headers: {
			// 			'token': sessionStorage.getItem('token')
			// 		}
			// 	}).then(res => {
			// 		console.log(res)
			// 		if(res.data.code == 0) {
			// 			// this.$router.replace('/index')
			// 			console.log(res.data)
			// 		}
			// 	}).catch(err => {
			// 		console.log(err)
			// 	})
			// }
		}
	}
</script>

<style>
	#menuDiv {
		height: 100%;
	}
	body {
		margin: 0px;
		height: 100%;
	}
	html,
	#app,
	.el-container {
	  height: 100%;
	}
	.el-row {
		height: 87.5%;
	}
</style>