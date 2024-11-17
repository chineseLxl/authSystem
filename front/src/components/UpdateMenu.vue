<template>
	<!-- <el-form :inline="true"> -->
	<el-form :model="ruleForm" :rules="rules" ref="ruleForm">
		<el-form-item label="菜单名称" prop="menuName">
			<el-input v-model="ruleForm.menuName"></el-input>
		</el-form-item>
		<!-- 				<el-form-item label="父菜单" prop="CatalogueMenu" v-if="ruleForm.radio != 3">
					<el-select filterable placeholder="请选择" clearable v-model="ruleForm.CatalogueMenu">
						<el-option :label="item.cname" :value="item.id"
							v-for="(item,index) in menuCatalogueList" />
					</el-select>
				</el-form-item> -->
		<el-form-item label="父菜单" prop="CatalogueMenu" v-if="ruleForm.radio === 3">
			<el-select filterable placeholder="请选择" clearable v-model="ruleForm.CatalogueMenu">
				<el-option :label="item.pname" :value="item.id" v-for="(item,index) in pageList" />
			</el-select>
		</el-form-item>
		<el-form-item label="路由地址" prop="menuPath" v-if="ruleForm.type == 'page'">
			<el-input v-model="ruleForm.menuPath"></el-input>
		</el-form-item>
		<el-form-item label="权限标识" prop="auth" v-if="ruleForm.radio == 1">
			<el-input v-model="ruleForm.auth" placeholder="请输入权限标识 例如:(xxx:xxx)"></el-input>
		</el-form-item>
		<el-form-item label="权限标识" prop="auth" v-if="ruleForm.radio != 1">
			<el-input v-model="ruleForm.auth" placeholder="请输入权限标识 例如:(xxx:xxx:xxx)"></el-input>
		</el-form-item>
		<el-form-item label="前端页面" prop="htmlFileName" v-if="ruleForm.type == 'page'">
			<el-select filterable placeholder="请选择前端页面" v-model="ruleForm.htmlFileName" clearable>
				<el-option :label=item :value=item v-for="(item,index) in htmlFileList" />
			</el-select>
		</el-form-item>
		<el-form-item label="控制台删除" prop="systemDel">
			<el-radio-group v-model="ruleForm.systemDel">
				<el-radio :value="0">否</el-radio>
				<el-radio :value="1">是</el-radio>
			</el-radio-group>
		</el-form-item>
		<br />
		<div style="text-align: center;">
			<el-button type="primary" @click="submitUpdateForm('ruleForm')">更改菜单</el-button>
			<el-button @click="resetForm('ruleForm')">重置</el-button>
		</div>
	</el-form>


</template>

<script>
	import { toRaw } from 'vue'
	import {http} from '@/js/http'
	export default {
		props: {
			menuForm: {
				type: Object,
				required: true,
			}
		},
		watch: {
			// 监听父组件传值变化
			menuForm(newVal, oldVal) {
				this.ruleForm.systemDel = newVal.systemDel
				this.ruleForm.htmlFileName = newVal.component
				this.ruleForm.menuName = newVal.name
				this.ruleForm.menuPath = newVal.path
				this.ruleForm.auth = newVal.auth
				this.ruleForm.type = newVal.type
				this.ruleForm.id = newVal.id
			}
		},
		data() {
			return {
				htmlFileList: [],
				ruleForm: {
					id: this.menuForm.id,
					type: this.menuForm.type,
					systemDel: this.menuForm.systemDel,
					htmlFileName: this.menuForm.component,
					menuName: this.menuForm.name,
					menuPath: this.menuForm.path,
					auth: this.menuForm.auth,
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
			submitUpdateForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						const formData = toRaw(this.ruleForm);
						console.log(formData)
						http.post("/api/system/updateMenu", {
							id: formData.id,
							type: formData.type,
							component: formData.htmlFileName,
							menuName: formData.menuName,
							menuPath: formData.menuPath,
							catalogueMenu: formData.CatalogueMenu,
							auth: formData.auth,
							systemDel: formData.systemDel,
						}).then((res) => {
							console.log(res)
						}).catch((res) => {
				
						})
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
		},
		mounted() {
			http.post("/api/loadHtmlDir").then((res) => {
				this.htmlFileList = res.data.data
			}).catch((res) => {

			})
		}
	}
</script>

<style>
</style>