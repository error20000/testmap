var baseUrl = '../';
var changePwdUrl = baseUrl + "api/user/changePWD";
var logoutUrl = baseUrl + "api/user/logout";
var isLoginUrl = baseUrl + "api/user/isLogin";


new Vue({
    el: '#app',
    data() {
        return {
          sysName: "Platform ",
          sysUserName: "",
          menus: [],
      	  preloading: false,
          //pwd
          pwdFormVisible: false,
          pwdLoading: false,
          pwdFormRules: {
            oldPwd: [
              { required: true, message: "Please enter the original password.", trigger: "blur" }
            ],
            newPwd: [{ required: true, message: "Please enter the new password.", trigger: "blur" }],
            newPwd2: [
              { required: true, message: "Please enter the new password again.", trigger: "blur" },
              {
                validator: (rule, value, callback) => {
                  if (value !== this.pwdForm.newPwd) {
                    callback(new Error("Passwords does not match!"));
                  } else {
                    callback();
                  }
                },
                trigger: "blur"
              }
            ]
          },
          pwdForm: {
            oldPwd: "",
            newPwd: "",
            newPwd2: ""
          }
        };
      },
      methods: {
        handleSelect: function(index) {
          this.showIframe(index);
        },
		showIframe: function(index){
			let url = "";
			
			switch (Number(index)) {
			case 1:
				url = 'user.html';
				break;
			case 2:
				url = 'options.html';
				break;
			case 3:
				url = 'comment.html';
				break;
				
			default:
				break;
			}
			$('.content-iframe').attr('src', url);
		},
        //update pwd
        handlepwdChange: function() {
          this.pwdFormVisible = true;
          this.pwdForm = {
            oldPwd: "",
            newPwd: "",
            newPwd2: ""
          };
        },
        pwdChangeClose: function() {
          this.pwdFormVisible = false;
          this.pwdLoading = false;
          this.$refs.pwdForm.resetFields();
        },
        pwdChange: function() {
          this.$refs.pwdForm.validate(valid => {
            if (valid) {
              this.$confirm('Confirmation of submission?', 'Tips', {}).then(() => {
                var params = Object.assign({}, this.pwdForm);
                delete params.newPwd2;
                var self = this;
                this.pwdLoading = true;
                ajaxReq(changePwdUrl, params, function(res) {
                  self.addLoading = false;
                  if (res.code > 0) {
                    self.$message({
                      message: "success",
                      type: "success"
                    });
                    self.addFormVisible = false;
                    localStorage.removeItem('user');
                    parent.window.location.href = "login.html";
                  }else if(res.code == -206){
					self.$message({
							message: 'Missing parameters.',
							type: 'warning'
						})
					}else if(res.code == -213){
						self.$message({
							message: 'Incorrect password. ',
							type: 'warning'
						})
					}else if(res.code == -111){
						self.$message({
							message: 'Not logged in. ',
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
        },
        //login
        logout: function() {
          this.$confirm("Confirmation of logout", "Tips", {
            //type: 'warning'
          }).then(() => {
              var self = this;
              var params = {};
              ajaxReq(logoutUrl, params, function(res) {
                if (res.code > 0) {
                    localStorage.removeItem('user');
                  parent.window.location.href = "login.html";
                }else{
                	self.$message({
						message: 'failed',
						type: 'warning'
					})
                }
              });
            }).catch(() => {});
        },
        isLogin: function(cb) {
			var params = {};
			ajaxReq(isLoginUrl, params, function(res){
				if(res.code <= 0){
					parent.window.location.href = "login.html";
				}else{
					if(typeof cb == 'function'){
						cb();
					}
					localStorage.setItem('user', JSON.stringify(res.data));
				}
			});
        }
      },
      mounted: function() {

  		this.user = JSON.parse(localStorage.getItem('user'));
  		if(this.user　==　null){
  			parent.window.location.href = "login.html";
  		}
  		if(this.user.admin != 1){
  			alert("no auth!");
  			parent.window.location.href = "login.html";
  		}
		loginUserId = this.user.pid;
  		this.sysUserName = this.user.username;
		this.isLogin();
		this.preloading = true;
      }
  });
