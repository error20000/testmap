var baseUrl = '../';
var changePwdUrl = baseUrl + "api/user/changePWD";
var logoutUrl = baseUrl + "api/user/logout";
var isLoginUrl = baseUrl + "api/user/isLogin";

function ajaxReq(url, param, callback, cp){
	$.ajax({
		   dataType: "json",
		   type: "POST",
		   url: url,
		   data: param,
		   success: function(data){
			   	if(data.code == -203 || data.code == -111){ // token 超时
			   		parent.window.location.href = "login.html";
			   	}
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
                    sessionStorage.removeItem('user');
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
                    sessionStorage.removeItem('user');
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
					sessionStorage.setItem('user', JSON.stringify(res.data));
				}
			});
        }
      },
      mounted: function() {

  		this.user = JSON.parse(sessionStorage.getItem('user'));
  		if(this.user　==　null){
  			parent.window.location.href = "login.html";
  		}
  		if(this.user.admin != 1){
  			alert("no auth!");
  			parent.window.location.href = "login.html";
  		}
  		this.sysUserName = this.user.username;
		this.isLogin();
		this.preloading = true;
      }
  });

function formatDate(d, s){
    var date = new Date();
    if(d){
        if(typeof d == 'object'){
            date = d;
        }else{
            if(isNaN(d)){
                date = new Date(d.replace(/-/g, "/").replace(/年/g, "/").replace(/月/g, "/").replace(/日/g, " ").replace(/时/g, ":").replace(/分/g, ":").replace(/秒/g, ""));
            }else{
                d = String(d).length == 10 ? d + "000" : String(d).length == 13 ? d : new Date().getTime() + Number(d);
                date = new Date(Number(d));
            }
        }
    }
    var weekday = ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var weekdayS = ["日","一","二","三","四","五","六"];
    var weekdayEn = ["Sunday","Monday","Tuesday","Wednesday","Thursday ","Friday","Saturday"];
    var weekdayEnS = ["Sun.","Mon.","Tues.","Wed.","Thurs. ","Fri.","Sat."];
    var t = String(s);
    t = t.replace('yyyy', date.getFullYear());
    t = t.replace('yy', date.getYear);
    t = t.replace('MM', (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : (date.getMonth()+1));
    t = t.replace('M', (date.getMonth()+1));
    t = t.replace('dd', date.getDate() < 10 ? "0"+date.getDate() : date.getDate());
    t = t.replace('d', date.getDate());
    t = t.replace('HH', date.getHours() < 10 ? "0"+date.getHours() : date.getHours());
    t = t.replace('H', date.getHours());
    t = t.replace('mm', date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes());
    t = t.replace('m', date.getMinutes());
    t = t.replace('ss', date.getSeconds() < 10 ? "0"+date.getSeconds() : date.getSeconds());
    t = t.replace('s', date.getSeconds());
    t = t.replace('S', date.getMilliseconds());
    t = t.replace('en:ww', weekdayEn[date.getDay()]);
    t = t.replace('en:w', weekdayEnS[date.getDay()]);
    t = t.replace('cn:ww', weekday[date.getDay()]);
    t = t.replace('cn:w', weekdayS[date.getDay()]);
    t = t.replace('ww', weekday[date.getDay()]);
    t = t.replace('w', weekdayS[date.getDay()]);
    return t;
};
