var baseUrl = parent.window.baseUrl || '../';

var queryUrl = baseUrl + "api/options/findPage";
var addUrl = baseUrl + "api/options/add";
var modUrl = baseUrl + "api/options/update";
var delUrl = baseUrl + "api/options/delete";

var ajaxReq = parent.window.ajaxReq || "";


var myvue = new Vue({
	    el: '#app',
	    data: function(){
	    	return {
				filters: {
					name: ''
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
					name: [
						{  required: true, message: 'Please enter the option label.', trigger: 'blur' }
					]
				},
				//edit
				editFormVisible: false,
				editLoading: false,
				editForm: {},
				editFormRules: {
					name: [
						{  required: true, message: 'Please enter the option label.', trigger: 'blur' }
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
						pid: '',
						name: '',
						status: 0
				};
			},
			//edit
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//view
			handleView: function (index, row) {
				this.viewFormVisible = true;
				this.viewForm = Object.assign({}, row);
			},
			//del
			handleDel: function (index, row) {
				this.$confirm('Are you sure to delete the record? It can not be restored after deletion. ', 'Tips', {
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
			selsChange: function (sels) {
				this.sels = sels;
			},
			//res
			toLoginHtml: function(){
                localStorage.removeItem('user');
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
	
	

