var baseUrl = '../';

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
    data: function(){
		return {
			sysName:'后台管理',
			collapsed:false,
			sysUserName: 'admin',
			sysUserAvatar: '',
			map: '',
			zoom: 12,
			path: [],
			canDraw: true,
			canMove: false,
			polyline: '',
			
			//新增界面数据
			addFormVisible: false,//新增界面是否显示
			addLoading: false, //loading
			addForm: {
				local: '',
				path:'',
				type:'',
				option: '',
				content:''
			},
			addFormRules:{
				type: [
					{  required: true, message: 'please choose', trigger: 'blur' }
				],
				option: [
					{ validator: (rule, value, callback) => {
				          if(this.addForm.type === 0 && value === "") {
				            callback(new Error('please choose your feelings!'));
				          } else {
				            callback();
				          }
					}, trigger: 'blur' }
				],
				content: [
					{ validator: (rule, value, callback) => {
				          if(this.addForm.type === 1 && value === "") {
				            callback(new Error('please write down your feelings!'));
				          } else {
				            callback();
				          }
					}, trigger: 'blur' }
				]
			},
			typeOptions:[
				{value: '1',lable: 'It is not safe here.'},
				{value: '2',lable: 'It is safe here.'},
				{value: '3',lable: 'The traffic is so bad here.'},
				{value: '4',lable: 'The traffic is good here.'},
				{value: '5',lable: 'It is so noisy here.'},
				{value: '6',lable: 'It is so quiet here.'},
				{value: '7',lable: 'Here is in a mess.'},
				{value: '8',lable: 'It is so beautiful here.'},
				{value: '9',lable: 'I like local food.'},
				{value: '10',lable: 'I want to be here next time.'},
				{value: '11',lable: 'I will not be here next time.'}
			]
		}
	},
	methods: {
		getLocation(){
            /*var options={
                enableHighAccuracy:true, 
                maximumAge:1000
            }
            if(navigator.geolocation){
                //浏览器支持geolocation
                navigator.geolocation.getCurrentPosition(this.onSuccess, this.onError);
                
            }else{
                //浏览器不支持geolocation
            	alert("浏览器不支持地理定位。"); 
            }*/
			//PC test
            this.showMap(30.67, 104.06);
        },
        onSuccess(position){
			console.log("===========position============");
			console.log(position);
            //返回用户位置
            //经度
            var lng = position.coords.longitude;
            //纬度
            var lat = position.coords.latitude;
            
            //google 
            this.showMap(lat, lng);
            /*var latlon = latitude+','+longitude; 
            var url = 'http://maps.google.cn/maps/api/geocode/json?latlng='+latlon+'&language=CN'; 
            $.ajax({ 
	            type: "GET", 
	            url: url, 
	            beforeSend: function(){ 
	            	$("#google_geo").html('正在定位...'); 
	            }, 
	            success: function (json) { 
		            if(json.status=='OK'){ 
			            var results = json.results; 
			            $.each(results,function(index,array){ 
				            if(index==0){ 
				            	$("#google_geo").html(array['formatted_address']); 
				            } 
			            }); 
		            } 
	            }, 
	            error: function (XMLHttpRequest, textStatus, errorThrown) { 
	            	$("#google_geo").html(latlon+"地址位置获取失败"); 
	            } 
            }); */
        },
        onError(error){ 
			console.log("===========error============");
			console.log(error);
			switch(error.code) { 
				case error.PERMISSION_DENIED: 
					alert("定位失败,用户拒绝请求地理定位"); 
				break; 
				case error.POSITION_UNAVAILABLE: 
					alert("定位失败,位置信息是不可用"); 
				break; 
				case error.TIMEOUT: 
					alert("定位失败,请求获取用户位置超时"); 
				break; 
				case error.UNKNOWN_ERROR: 
					alert("定位失败,未知错误"); 
				break; 
			} 
		},
		showMap: function (lat, lng) {
			var center = {
		        lat: lat,
		        lng: lng
	        };
	        this.map = new google.maps.Map(document.getElementById('gmap'), {
		        center: center,
		        draggable: false,
		        zoom: this.zoom
		        // mapTypeId: google.maps.MapTypeId.ROADMAP
	        });
	        this.initControl();
	        this.initMousedown();
	        this.initMousemove();
	        this.initMouseup();
		},
		//创建控件
		initControl: function(){
			
		},
		handleControl: function(){
			
		},
		//自定义事件
		initMousedown: function(){
			let _this = this;
			google.maps.event.addListener(this.map, 'mousedown', function(event) {
				_this.handleMousedown(event);
			});
		},
		initMousemove: function(){
			let _this = this;
			google.maps.event.addListener(this.map, 'mousemove', function(event) {
				_this.handleMousemove(event);
			});
		},
		initMouseup: function(){
			let _this = this;
			google.maps.event.addListener(this.map, 'mouseup', function(event) {
				_this.handleMouseup(event);
			});
		},
		handleMousedown: function(event){
			console.log("handleMousedown");
			if(this.canDraw){
				let center = event.latLng;
				let point = {
						lat: center.lat(),
						lng: center.lng()
				};
				console.log(point.lat+", "+point.lng);
				this.path.push(point);
				this.polyline = new google.maps.Polyline({
					map: this.map,
					path: this.path
				});
				//开始移动
				this.canMove = true;
			}
		},
		handleMousemove: function(event){
			if(this.canMove){
				let center = event.latLng;
				let point = {
						lat: center.lat(),
						lng: center.lng()
				};
				console.log(point.lat+", "+point.lng);
				this.path.push(point);
				this.polyline.setPath(this.path);
			}
		},
		handleMouseup: function(event){
			console.log("handleMouseup");
			let center = event.latLng;
			let point = {
					lat: center.lat(),
					lng: center.lng()
			};
			console.log(point.lat+", "+point.lng);
			this.canMove = false;
			//弹出窗口
			//this.addFormVisible = true;
			
			
			/*var myCity = new google.maps.Circle({
				  center:{lat: point.lat, lng: point.lng},
				  radius:1,
				  strokeColor:"#0000FF",
				  strokeOpacity:0.8,
				  strokeWeight:2,
				  fillColor:"#0000FF",
				  fillOpacity:0.4
				  });

				myCity.setMap(this.map);*/
		},
		//新增评论
		addClose: function () {
			this.addFormVisible = false;
			this.addLoading = false;
			this.$refs.addForm.resetFields();
		},
		addSubmit: function () {
			this.$refs.addForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						var url = baseUrl + "api/content/add";
						var params = Object.assign({}, this.addForm);
						params.path = this.path;
						var self = this;
						this.addLoading = true;
						ajaxReq(url, params, function(res){
							self.addLoading = false;
							if(res.code > 0){
								self.$message({
									message: '新增成功',
									type: 'success'
								});
								self.addFormVisible = false;
								self.getList();
							}else{
								self.$message({
									message: res.msg,
									type: 'warning'
								})
							}
						});
					});
				}
			});
		},
		//退出登录
		logout: function () {
			this.$confirm('确认退出吗?', '提示', {
				//type: 'warning'
			}).then(() => {
				var url = baseUrl + "api/user/logout";
				var params = {};
				ajaxReq(url, params, function(res){
					if(res.code > 0){
						parent.window.location.href = "login.html";
					}
				});
			}).catch(() => {

			});
		},
		//判断登录
		isLogin: function (cb) {
			var url = baseUrl + "api/user/isLogin";
			var params = {};
			ajaxReq(url, params, function(res){
				if(res.code <= 0){
					window.location.href = "login.html";
				}else{
					if(typeof cb == 'function'){
						cb();
					}
				}
			});
		},
	},
	mounted: function() {
		this.isLogin(this.getLocation);
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
