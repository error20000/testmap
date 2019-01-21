var baseUrl = '../';

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
                { required: true, message: '请输入账号', trigger: 'blur' },
              ],
              password: [
                { required: true, message: '请输入密码', trigger: 'blur' },
              ]
            },
            checked: true
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
				var url = baseUrl+"api/user/login";
				var params = {
						username: this.ruleForm.username,
						password: this.ruleForm.password
				};
				ajaxReq(url, params, function(res){
					self.logining = false;
					if(res.code > 0){
						//sessionStorage.setItem('checked', self.checked);
						//sessionStorage.setItem('user', JSON.stringify(res.data[0]));
						//sessionStorage.setItem('loginTime', new Date().getTime());
						if(res.data.admin === 1){
							window.location.href = 'index.html';
						}else{
							window.location.href = 'index.html';
						}
					}else{
						self.$message({
							message: res.msg,
							type: 'warning'
						})
					}
				});
              } else {
                console.log('error submit!!');
                return false;
              }
            });
          }
        },
    	mounted: function() {
    		
    	}
    });

