var baseUrl = '../';
var loginUrl = baseUrl + 'api/user/login';
var registerUrl = baseUrl + 'api/user/register';

function ajaxReq(url, param, callback, cp){
	$.ajax({
		   dataType: "json",
		   type: "POST",
		   url: url,
		   data: param,
		   success: function(data){
				if (typeof callback === "function") {
					callback(data, cp);
				}
		   },
		   error: function(data){
		   }
		});
}

  new Vue({
      el: '#app',
      data: function() {
        return {
            logining: false,
            ruleForm: {
              username: '',
              password: ''
            },
            rules: {
              username: [
                { required: true, message: 'Please enter your username.', trigger: 'blur' },
              ],
              password: [
                { required: true, message: 'Please enter your password.', trigger: 'blur' },
              ]
            },
            checked: true,
            // register
			addFormVisible: false,// show register window
			addLoading: false, // loading
			addForm: {
				username: '',
				password:'',
				password2:'',
				nick:''
			},
			addFormRules:{
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
			}
			
          }
      },
      methods: {
          handleReset: function() {
            this.$refs.ruleForm.resetFields();
          },
          handleSubmit: function(ev) {
            this.$refs.ruleForm.validate((valid) => {
              if (valid) {
                var self = this;
				this.logining = true;
				var params = {
						username: this.ruleForm.username,
						password: this.ruleForm.password
				};
				ajaxReq(loginUrl, params, function(res){
					self.logining = false;
					if(res.code > 0){
						// sessionStorage.setItem('checked', self.checked);
						sessionStorage.setItem('user', JSON.stringify(res.data));
						// sessionStorage.setItem('loginTime', new
						// Date().getTime());
						if(res.data.admin === 1){
							window.location.href = 'admin.html';
						}else{
							window.location.href = 'index.html';
						}
					}else{
						self.$message({
							message: 'failed',
							type: 'warning'
						})
					}
				});
              } else {
                console.log('error submit!!');
                return false;
              }
            });
          },
          handleRegister(){
  			this.addForm = {
				username: '',
				password: '',
				password2:'',
				nick:''
			};
			 this.addFormVisible = true;
          },
          regClose(){
  			this.addFormVisible = false;
  			this.addLoading = false;
  			this.$refs.addForm.resetFields();
          },
          addRegister(){
        	  this.$refs.addForm.validate((valid) => {
  				if (valid) {
  					this.$confirm('Confirmation of submission?', 'Tips', {}).then(() => {
  						var params = Object.assign({}, this.addForm);
  						var self = this;
  						this.addLoading = true;
  						ajaxReq(registerUrl, params, function(res){
  							self.addLoading = false;
  							if(res.code > 0){
  								self.$message({
  									message: 'success',
  									type: 'success'
  								});
  								self.addFormVisible = false;
  							}else if(res.code == -206){
  								self.$message({
  									message: 'Missing parameters.',
  									type: 'warning'
  								})
  							}else if(res.code == -105){
  								self.$message({
  									message: 'Username already exist. ',
  									type: 'warning'
  								})
  							}else{
  								self.$message({
  									message: 'failed',
  									type: 'warning'
  								})
  							}
  						});
  					});
  				}
  			});
          }
        },
    	mounted: function() {
    		
    	}
    });

