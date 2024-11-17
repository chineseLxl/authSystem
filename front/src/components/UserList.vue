<template>
	<el-container>
		<el-header>
			<el-card>
				<div class="menuHeader">
					<el-row>
						<el-col :span="2">
							&nbsp;
						</el-col>
						<el-col :span="6">
							<el-row>
								<el-col :span="5">
									<div class="headerInputTitle">
										用户ID ：
									</div>
								</el-col>
								<el-col :span="13">
									<el-input placeholder="请输入用户ID" v-model="inputMenuNum" clearable>
									</el-input>
								</el-col>
								<el-col :span="6">
									&nbsp;
								</el-col>
							</el-row>
						</el-col>
						<el-col :span="12">
							<div class="grid-content bg-purple-light">
								<el-row>
									<el-col :span="3">
										<div class="headerInputTitle">
											用户名 ：
										</div>
									</el-col>
									<el-col :span="10">
										<el-input placeholder="请输入用户名" v-model="inputMenuName" clearable>
										</el-input>
									</el-col>
								</el-row>
							</div>
						</el-col>
						<el-col :span="4">
							<el-button type="primary">查询</el-button>
							<el-button>重置</el-button>
						</el-col>
					</el-row>
				</div>
			</el-card>
		</el-header>

		<el-main>
			<el-dialog v-model="updateMenuForm" title="编辑菜单" width="800">
				<UpdateMenuVue :menuForm="updateForm"></UpdateMenuVue>
			</el-dialog>
			
			<el-card>
				<el-container>
					<el-header>
						<el-row>
							<el-col :span="20">
								用户列表
							</el-col>
							<el-col :span="4">
								<el-button type="primary" @click="addMenu()">
									<el-icon>
										<Plus />
									</el-icon>
									<span>新增</span>
								</el-button>
								<el-button circle @click="winReload()">
									<el-icon>
										<el-icon>
											<RefreshLeft />
										</el-icon>
									</el-icon>
								</el-button>

							</el-col>
						</el-row>
					</el-header>
					<el-main class="mainTable">
						<el-table :data="userInfoList" style="width: 100%" row-key="auth">
							<el-table-column fixed prop="id" label="用户id" width="200">
							</el-table-column>
							<el-table-column prop="username" label="用户名" width="250">
							</el-table-column>
							<el-table-column prop="roles" label="角色" width="300">
							</el-table-column>
							<el-table-column prop="status" label="状态" width="200">
								<template v-slot="scope">
									<div class="type-login-div" v-if="scope.row.status == '1'">
										在线
									</div>
									<div class="type-exit-div" v-if="scope.row.status == '0'">
										离线
									</div>
								</template>
							</el-table-column>
							<el-table-column fixed="right" label="操作" width="300">
								<template v-slot="scope">
									<el-button @click="updateBtn(scope.row)" type="primary" size="small">编辑</el-button>
									<el-button type="warning" size="small" @click="exitWebSocket(scope.row)"
										:disabled="scope.row.systemExit == 0 ? true : false">强制下线</el-button>
									<el-button type="danger" size="small" @click="removeBtn(scope.row)"
										:disabled="scope.row.systemDel == 0 ? true : false">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-main>
				</el-container>

			</el-card>
		</el-main>
	</el-container>
</template>

<script type="text/javascript">
	import {http} from "@/js/http"
	// import {websocket} from "@/js/webSocket"
	export default {
		data() {
			return {
				userInfoList: [],
			}
		},
		methods: {
			winReload() {
				location.reload()
			},
			getLoginUserInfo() {
				http.post("/api/system/getLoginUserInfo",{
					
				}).then((res) => {
					this.userInfoList = res.data.data
				}).catch((e) => {
					console.log(e)
				})
			},
			exitWebSocket(row) {
				console.log(row)
				http.post("/api/system/outLoginUser",{
					"username":row.username,
				}).then((res) => {
					console.log(res)
				}).catch((e) => {
					console.log(e)
				})
			}
		},
		mounted() {
			this.getLoginUserInfo()
		}
	}
</script>

<style>
	.headerInputTitle {
		padding-top: 0.4rem;
		font-family: "PingFang SC";
		font-size: 0.85rem;
	}
	
	.mainTable {
		padding-top: 0px;
	}
	
	.el-select {
		min-width: 10rem;
	}
	
	.type-login-div {
		background-color: #12c919;
		text-align: center;
		height: 1.5rem;
		width: 2.5rem;
		border-radius: 0.5rem;
		color: white;
	}
	
	.type-exit-div {
		background-color: #b7b7b7;
		text-align: center;
		height: 1.5rem;
		width: 2.5rem;
		border-radius: 0.5rem;
		color: white;
	}
</style>