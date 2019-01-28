var baseUrl = parent.window.baseUrl || '../';

var queryUrl = baseUrl + "api/user/findPage";
var addUrl = baseUrl + "api/user/add";
var modUrl = baseUrl + "api/user/update";
var delUrl = baseUrl + "api/user/delete";
var resetPwdUrl = baseUrl + "api/user/resetPWD";

var ajaxReq = parent.window.ajaxReq || "";


var myvue = new Vue({
	    el: '#app',
	    data: function(){
	    	return {
				filters: {
					username: ''
				},
				list: [],
				total: 0,
				page: 1,
				rows: 10,
				listLoading: false,
				sels: [],
				preloading: false,
				//add
				addFormVisible: false,
				addLoading: false, 
				addForm: {},
				addFormRules: {
		              username: [
		                { required: true, message: 'Please enter the username.', trigger: 'blur' },
		              ],
		              password: [
		                { required: true, message: 'Please enter the password.', trigger: 'blur' },
		              ],
		              password2: [
						{ required: true, message: 'Please enter the password again.', trigger: 'blur' },
						{ validator: (rule, value, callback) => {
					          if (value !== this.addForm.password) {
					            callback(new Error('Passwords does not match!'));
					          } else {
					            callback();
					          }
						}, trigger: 'blur' }
				      ]
				},
				//edit
				editFormVisible: false,
				editLoading: false,
				editForm: {},
				editFormRules: {
		              username: [
		                { required: true, message: 'Please enter the username.', trigger: 'blur' },
		              ]
				},
				//reset
				pwdFormVisible: false,
				pwdLoading: false,
				pwdForm: {},
				pwdFormRules: {
		              password: [
		                { required: true, message: 'Please enter the password.', trigger: 'blur' },
		              ],
		              password2: [
						{ required: true, message: 'Please enter the password again.', trigger: 'blur' },
						{ validator: (rule, value, callback) => {
					          if (value !== this.pwdForm.password) {
					            callback(new Error('Passwords does not match!'));
					          } else {
					            callback();
					          }
						}, trigger: 'blur' }
				      ]
				},
				//view
				viewFormVisible: false,
				viewForm: {},
				
				
				end: ''
			}
		},
		methods: {
			formatDate: function(date){
				return parent.window.formatDate(date, 'yyyy-MM-dd HH:mm:ss');
			},
			handleSizeChange: function (val) {
				this.rows = val;
				this.getList();
			},
			handleCurrentChange: function (val) {
				this.page = val;
				this.getList();
			},
			//query
			getList: function () {
				var self = this;
				var params = {
					page: this.page,
					rows: this.rows
				};
				for ( var key in this.filters) {
					if(this.filters[key]){
						params[key] = this.filters[key];
					}
				}
				this.listLoading = true;
				ajaxReq(queryUrl, params, function(res){
					self.listLoading = false;
					self.handleResQuery(res, function(){
						self.total = res.total;
						self.list = res.data;
						if(self.page != 1 && self.total <= (self.page - 1) * self.rows){
							self.page = self.page - 1;
							self.getList();
						}
					});
				});
			},
			//add
			handleAdd: function () {
				this.addFormVisible = true;
				this.addForm = {
						username: '',
						password: '',
						password2: '',
						nick: '',
						admin: 0
				};
			},
			//edit
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//pwd
			handlePwd: function (index, row) {
				this.pwdFormVisible = true;
				this.pwdForm = Object.assign({}, row);
				this.pwdForm.password = '';
				this.pwdForm.password2 = '';
			},
			//view
			handleView: function (index, row) {
				this.viewFormVisible = true;
				this.viewForm = Object.assign({}, row);
			},
			//del
			handleDel: function (index, row) {
				this.$confirm('Are you sure to delete the record? ', 'Tips', {
					type: 'warning'
				}).then(() => {
					var self = this;
					this.listLoading = true;
					ajaxReq(delUrl, {pid: row.pid }, function(res){
						self.listLoading = false;
						self.handleResOperate(res, function(){
							self.getList();
						});
					});
					
				}).catch(() => {
				});
			},
			//add
			addClose: function () {
				this.addFormVisible = false;
				this.addLoading = false;
				this.$refs.addForm.resetFields();
			},
			addSubmit: function () {
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						this.$confirm('Confirmation of submission?', 'Tips', {}).then(() => {
							var params = Object.assign({}, this.addForm);
							var self = this;
							this.addLoading = true;
							ajaxReq(addUrl, params, function(res){
								self.addLoading = false;
								self.handleResOperate(res, function(){
									self.addFormVisible = false;
									self.getList();
								});
							});
						});
					}
				});
			},
			//edit
			editClose: function () {
				this.editFormVisible = false;
				this.editLoading = false;
				this.$refs.editForm.resetFields();
			},
			editSubmit: function () {
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$confirm('Confirmation of submission?', 'Tips', {}).then(() => {
							var self = this;
							this.editLoading = true;
							var params = Object.assign({}, this.editForm);
							ajaxReq(modUrl, params, function(res){
								self.editLoading = false;
								self.handleResOperate(res, function(){
									self.editFormVisible = false;
									self.getList();
								});
							});
							
						});
					}
				});
			},
			//pwd
			pwdClose: function () {
				this.pwdFormVisible = false;
				this.pwdLoading = false;
				this.$refs.pwdForm.resetFields();
			},
			pwdSubmit: function () {
				this.$refs.pwdForm.validate((valid) => {
					if (valid) {
						this.$confirm('Confirmation of submission?', 'Tips', {}).then(() => {
							var self = this;
							this.pwdLoading = true;
							var params = Object.assign({}, this.pwdForm);
							ajaxReq(resetPwdUrl, params, function(res){
								self.pwdLoading = false;
								self.handleResOperate(res, function(){
									self.pwdFormVisible = false;
									self.getList();
								});
							});
							
						});
					}
				});
			},
			selsChange: function (sels) {
				this.sels = sels;
			},
			//res
			toLoginHtml: function(){
                sessionStorage.removeItem('user');
                parent.window.location.href = "login.html";
			},
			handleResQuery: function(res, success, failed){
				this.handleRes(false, res, success, failed);
			},
			handleResOperate: function(res, success, failed){
				this.handleRes(true, res, success, failed);
			},
			handleRes: function(show, res, success, failed){
				if(res.code > 0){
					if(show){
						this.$message({
							message: 'success',
							type: 'success'
						});
					}
					if(typeof success == 'function'){
						success(res, this);
					}
				}else if(res.code == -111){
					this.$message({
						message: 'no login.',
						type: 'warning'
					});
					this.toLoginHtml();
				}else if(res.code == -201){
					this.$message({
						message: 'no auth.',
						type: 'warning'
					});
				}else{
					if(show){
						this.$message({
							message: 'failed',
							type: 'warning'
						});
					}
					if(typeof failed == 'function'){
						failed(res, this);
					}
				}
			}
		},
		mounted: function() {
			this.preloading = true;
			this.getList();
		}
	  });
	
	

