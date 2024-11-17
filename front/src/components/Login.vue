<template>
	<div>
		<el-row type="flex" justify="center">
			<el-col :span="12">

				<div class="grid-content bg-purple-light loginMain">
					<el-row>
						<el-col :span="12">
							<div class="grid-content bg-purple">&nbsp;</div>
						</el-col>
						<el-col :span="8">
							<div>
								<el-input v-model="username" placeholder="请输入账号"></el-input>
								<el-input v-model="password" placeholder="请输入密码" show-password></el-input>
								<el-button type="primary" :loading="false" @click="loginFun()">登录</el-button>
							</div>
						</el-col>
					</el-row>
				</div>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import {http} from '@/js/http.js'
	import {router,resetRouter} from '@/router/router';
	import {
		createWebHistory,
		createRouter
	} from "vue-router";
	// const modules = import.meta.glob('@/components/**/*.vue')  // 导入
	export default {
		data() {
			return {
				username: '',
				password: '',
			}
		},
		mounted() {
			// console.log(router.getRoutes())
			router.getRoutes().forEach(r => {
				if(r.name != "login" && r.name != "index") {
					router.removeRoute(r.name)
				}
			})
			
		},
		methods: {
			loginFun() {
				console.log("点击")
				http.put("/api/login", {
					username: this.username,
					password: this.password
				}).then(res => {
					if(res.data.code == 1) {
						resetRouter()
						console.log(router.match)
						sessionStorage.setItem("routeList", "")
						sessionStorage.setItem("jwt", res.data.data.jwt)
						const routeList = res.data.data.routers;
						console.log(routeList)
						// 缓存路由地址
						sessionStorage.setItem("routeList", JSON.stringify(routeList))

						console.log(router.getRoutes())
						this.$router.replace('/index')
						// window.location.replace("/index")
					}
				}).catch(err => {
					console.log(err)
				})
			}
		}
	}
</script>

<style>
	.loginMain {
		height: 100%;
		margin-top: 20%;
		margin-bottom: 30%;
		background-color: #f1f4fc;
	}
</style>