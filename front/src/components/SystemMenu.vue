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
										菜单ID ：
									</div>
								</el-col>
								<el-col :span="13">
									<el-input placeholder="请输入菜单ID" v-model="inputMenuNum" clearable>
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
											菜单名称 ：
										</div>
									</el-col>
									<el-col :span="10">
										<el-input placeholder="请输入菜单名称" v-model="inputMenuName" clearable>
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
								菜单列表
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
						<el-table :data="tableData" style="width: 100%" row-key="auth">
							<el-table-column fixed prop="id" label="菜单id" width="150">
							</el-table-column>
							<el-table-column prop="path" label="路由地址" width="300">
							</el-table-column>
							<el-table-column prop="name" label="菜单名称" width="120">
							</el-table-column>
							<!-- <el-table-column prop="component" label="vue组件名" width="120">
							</el-table-column> -->
							<el-table-column prop="auth" label="权限标识" width="200">
							</el-table-column>
							<el-table-column prop="type" label="类型" width="70">
								<template v-slot="scope">
									<div class="type-catalogue-div" v-if="scope.row.type =='catalogue'">
										目录
									</div>
									<div class="type-page-div" v-if="scope.row.type =='page'">
										页面
									</div>
									<div class="type-api-div" v-if="scope.row.type =='api'">
										事件
									</div>
								</template>
							</el-table-column>
							<el-table-column prop="date" label="操作时间" width="120">
							</el-table-column>
							<el-table-column fixed="right" label="操作" width="150">
								<template v-slot="scope">
									<el-button @click="updateBtn(scope.row)" type="primary" size="small">编辑</el-button>
									<el-button type="danger" size="small" @click="removeBtn(scope.row)"
										:disabled="scope.row.systemDel == 0 ? true : false">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
						<el-dialog v-model="dialogTableVisible" title="新建菜单" width="800">
							<!-- <el-form :inline="true"> -->
							<el-form :model="ruleForm" :rules="rules" ref="ruleForm">
								<el-form-item label="菜单名称" prop="menuName">
									<el-input v-model="ruleForm.menuName"></el-input>
								</el-form-item>
								<el-form-item label="父菜单" prop="CatalogueMenu" v-if="ruleForm.radio != 3">
									<el-select filterable placeholder="请选择" clearable v-model="ruleForm.CatalogueMenu">
										<el-option :label="item.cname" :value="item.id"
											v-for="(item,index) in menuCatalogueList" />
									</el-select>
								</el-form-item>
								<el-form-item label="父菜单" prop="CatalogueMenu" v-if="ruleForm.radio === 3">
									<el-select filterable placeholder="请选择" clearable v-model="ruleForm.CatalogueMenu">
										<el-option :label="item.pname" :value="item.id"
											v-for="(item,index) in pageList" />
									</el-select>
								</el-form-item>
								<el-form-item label="路由地址" prop="menuPath" v-if="ruleForm.radio != 3">
									<el-input v-model="ruleForm.menuPath"></el-input>
								</el-form-item>
								<el-form-item label="权限标识" prop="auth" v-if="ruleForm.radio == 1">
									<el-input v-model="ruleForm.auth" placeholder="请输入权限标识 例如:(xxx:xxx)"></el-input>
								</el-form-item>
								<el-form-item label="权限标识" prop="auth" v-if="ruleForm.radio != 1">
									<el-input v-model="ruleForm.auth" placeholder="请输入权限标识 例如:(xxx:xxx:xxx)"></el-input>
								</el-form-item>
								<el-form-item label="前端页面" prop="htmlFileName" v-if="ruleForm.radio == 2">
									<el-select filterable placeholder="请选择前端页面" v-model="ruleForm.htmlFileName"
										clearable>
										<el-option :label=item :value=item v-for="(item,index) in htmlFileList" />
									</el-select>
								</el-form-item>
								<el-form-item label="菜单类型" prop="radio">
									<el-radio-group v-model="ruleForm.radio" @click="resetForm('ruleForm')">
										<el-radio :value="1">菜单目录</el-radio>
										<el-radio :value="2">菜单页面</el-radio>
										<el-radio :value="3">按钮</el-radio>
									</el-radio-group>
								</el-form-item>
								<el-form-item label="控制台删除" prop="systemDel">
									<el-radio-group v-model="ruleForm.systemDel">
										<el-radio :value="0">否</el-radio>
										<el-radio :value="1">是</el-radio>
									</el-radio-group>
								</el-form-item>
								<br />
								<div style="text-align: center;">
									<el-button type="primary" @click="submitForm('ruleForm')">创建菜单</el-button>
									<el-button @click="resetForm('ruleForm')">重置</el-button>
								</div>
							</el-form>
						</el-dialog>
					</el-main>
				</el-container>

			</el-card>
		</el-main>
	</el-container>

</template>

<script>
	import {
		ref,
		toRaw
	} from "vue";
	import {
		ElMessageBox
	} from 'element-plus';
	import {
		http
	} from "@/js/http"
	import UpdateMenuVue from "./UpdateMenu.vue";

	export default {
		components: {
			UpdateMenuVue
		},
		props: {
			menuForm: Object,
			required: true,
		},
		data() {
			return {
				tableData: [],
				dialogTableVisible: false,
				updateMenuForm: false,
				updateForm: {},
				childrenData: [],
				roleList: [],
				menuCatalogueList: [],
				htmlFileList: [],
				pageList: [],
				ruleForm: {
					radio: 1,
					systemDel: 0,
					htmlFileName: "",
					menuName: "",
					menuPath: "",
					CatalogueMenu: null,
					auth: "",
				},
				rules: {
					menuName: [{
						required: true,
						message: '请输入菜单名称',
						trigger: 'blur'
					}],
					menuPath: [{
						required: true,
						message: '请输入路由地址',
						trigger: 'blur'
					}],
					auth: [{
						required: true,
						message: '请输入权限标识（xxx:xxx:xxx）',
						trigger: 'blur'
					}],
					CatalogueMenu: [{
						required: true,
						message: '请选择父菜单',
						trigger: 'change'
					}],
					htmlFileName: [{
						required: true,
						message: '请选择前端页面',
						trigger: 'change'
					}],
				}
			}
		},
		methods: {
			updateBtn(row) {
				console.log(toRaw(row));
				console.log(row.id);
				this.updateMenuForm = true;
				this.updateForm = toRaw(row);
			},
			removeBtn(row) {
				ElMessageBox.confirm(
						'是否删除此菜单？',
						'Warning', {
							confirmButtonText: 'OK',
							cancelButtonText: 'Cancel',
							type: 'warning',
						}
					)
					.then(() => {
						console.log(row);
						console.log(row.id);
						http.post("/api/system/delMenu", {
							"id": row.id,
							"type": row.type,
						}).then((res) => {
							console.log(res)
						}).catch((res) => {
							console.log(res)
						})
					})
					.catch(() => {
						
					})
			},
			winReload() {
				location.reload()
			},
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						const formData = toRaw(this.ruleForm);
						console.log(formData)
						http.post("/api/system/addMenu", {
							radio: formData.radio,
							htmlFileName: formData.htmlFileName,
							menuName: formData.menuName,
							menuPath: formData.menuPath,
							catalogueMenu: formData.CatalogueMenu,
							auth: formData.auth,
							systemDel: formData.systemDel,
						}).then((res) => {
							console.log(res)
							ElMessageBox.alert(res.data.msg, '新增菜单事件', {
								confirmButtonText: 'OK',
							})
						}).catch((res) => {
							ElMessageBox.alert("服务器繁忙，请联系管理员", '新增菜单事件', {
								confirmButtonText: 'OK',
							})
						})
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
			},
			getMenuCatalogue() {
				http.post("/api/system/getCatalogue").then((res) => {
					console.log(res)
					this.menuCatalogueList = res.data.data
					console.log(res.data.data)
					// this.menuCatalogueList.unshift({
					// 	"rid": 0,
					// 	"pname": "根目录"
					// })
				}).catch((res) => {
					console.log(res)
				})
			},
			getMenus() {
				http.post("/api/system/getMenus").then((res) => {
					this.tableData = res.data.data
					console.log(this.tableData)
				}).catch((res) => {

				})
			},
			getPages() {
				http.post("/api/system/getPages").then((res) => {
					console.log(res)
					this.pageList = res.data.data
				}).catch((res) => {

				})
			},
			clickChildrenRoute(row) {
				this.dialogTableVisible = true
				this.childrenData = row.children
				console.log(row.children)
			},
			addMenu() {
				this.dialogTableVisible = true
			}
		},
		mounted() {

			// 获取菜单数据
			this.getMenus()
			this.getMenuCatalogue()
			this.getPages()
			console.log(this.tableData)
			http.post("/api/getRoles").then((res) => {
				this.roleList = res.data.data
			}).catch((res) => {

			})
			http.post("/api/loadHtmlDir").then((res) => {
				this.htmlFileList = res.data.data
			}).catch((res) => {

			})
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

	.type-page-div {
		background-color: #67c23a;
		text-align: center;
		height: 1.5rem;
		width: 2.5rem;
		border-radius: 0.5rem;
		color: white;
	}

	.type-catalogue-div {
		background-color: #409eff;
		text-align: center;
		height: 1.5rem;
		width: 2.5rem;
		border-radius: 0.5rem;
		color: white;
	}

	.type-api-div {
		background-color: #ffaa00;
		text-align: center;
		height: 1.5rem;
		width: 2.5rem;
		border-radius: 0.5rem;
		color: white;
	}
</style>