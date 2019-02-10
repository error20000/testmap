var baseUrl = '../';
var optionsUrl = baseUrl + "api/options/all";

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

Vue.use(VueQuillEditor);

new Vue({
    el: '#app',
    data: function(){
		return {
			sysName:'',
			collapsed:false,
			sysUserName: '',
			sysUserAvatar: '',
			user: '',
			colors: COLORS,
			map: '',
			zoom: 12,
			canDraw: false,
			canMove: false,
			mousedownListener: '',
			mousemoveListener: '',
			mouseupListener: '',
			polylineStrokeWeight: 8,
			drawType: 0, 
			drawStart: '',
			drawEnd: '',
			path: [],
			marker: '',
			markers: [],
			
			//新增界面数据
			addFormVisible: false,//新增界面是否显示
			addLoading: false, //loading
			addForm: {
				local: '',
				path:'',
				type:'',
				option: [],
				content:''
			},
			addFormRules:{
				option: [
					{ validator: (rule, value, callback) => {
				          if(this.addForm.option.length === 0 && !this.addForm.content) {
				            callback(new Error('please choose your feelings!'));
				          } else {
				            callback();
				          }
					}, trigger: 'blur' }
				],
				content: [
					{ validator: (rule, value, callback) => {
				          if(!this.addForm.content && this.addForm.option.length === 0) {
				            callback(new Error('please write down your feelings!'));
				          } else {
				            callback();
				          }
					}, trigger: 'blur' }
				]
			},
			typeOptions:[
				{value: '1',label: 'It is not safe here.'},
				{value: '2',label: 'It is safe here.'},
				{value: '3',label: 'The traffic is so bad here.'},
				{value: '4',label: 'The traffic is good here.'},
				{value: '5',label: 'It is so noisy here.'},
				{value: '6',label: 'It is so quiet here.'},
				{value: '7',label: 'Here is in a mess.'},
				{value: '8',label: 'It is so beautiful here.'},
				{value: '9',label: 'I like local food.'},
				{value: '10',label: 'I want to be here next time.'},
				{value: '11',label: 'I will not be here next time.'}
			],
			uploadUrl: baseUrl + 'api/file/uploadImg',
			editorOption: {
				modules: {
					toolbar: {
						container: [
				              /*['bold', 'italic', 'underline', 'strike'],
				              ['blockquote', 'code-block'],
				              [{ 'header': 1 }, { 'header': 2 }],
				              [{ 'list': 'ordered'}, { 'list': 'bullet' }],
				              [{ 'script': 'sub'}, { 'script': 'super' }],
				              [{ 'indent': '-1'}, { 'indent': '+1' }],
				              [{ 'direction': 'rtl' }],
				              [{ 'size': ['small', false, 'large', 'huge'] }],
				              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
				              [{ 'color': [] }, { 'background': [] }],
				              [{ 'font': [] }],
				              [{ 'align': [] }],
				              ['clean'],
				              ['link', 'image']*/
							  ['bold', 'image']
				            ],
				            handlers: {
							    // handlers object will be merged with default handlers object
							    'image': function(value) {
							    	$('#uploadBtn').click();
							    }

						  }
					}
				}
		          
		    },
		    editorImage: ''
		}
	},
	methods: {

		onEditorBlur(editor) {
			//console.log('editor blur!', editor)
		},
		onEditorFocus(editor) {
			//console.log('editor focus!', editor)
		},
		onEditorReady(editor) {
			//console.log('editor ready!', editor)
		},
		onEditorChange({ editor, html, text }) {
			//console.log('editor change!', editor, html, text)
		    this.content = html;
		},
		handleSuccessUpload(res, file){
			console.log(res);
			let dataUrl = '/'+res.data.path;
			let editor = this.$refs.myQuillEditor.quill;
   			let index = (editor.getSelection() || {}).index || editor.getLength();
   			editor.insertEmbed(index, 'image', dataUrl, 'user');
		},
		
		getLocation(){
            var options={
                enableHighAccuracy:true, 
                maximumAge:1000
            }
            if(navigator.geolocation){
                //浏览器支持geolocation
                navigator.geolocation.getCurrentPosition(this.onSuccess, this.onError);
                
            }else{
                //浏览器不支持geolocation
            	alert("Browsers do not support geolocation。"); 
            }
        },
        onSuccess(position){
			console.log("===========position============");
			console.log(position);
            //返回用户位置
            //经度
            var lng = position.coords.longitude;
            //纬度
            var lat = position.coords.latitude;
            //记录位置
            this.addForm.local = JSON.stringify({lat: lat, lng: lng});
            
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
					alert("Location failure, user refuses to request geolocation."); 
				break; 
				case error.POSITION_UNAVAILABLE: 
					alert("Location failure, location information is unavailable."); 
				break; 
				case error.TIMEOUT: 
					alert("Location failure, request for user location timeout."); 
				break; 
				case error.UNKNOWN_ERROR: 
					alert("Location failure, unknown error"); 
				break; 
			} 
			//PC test
            this.showMap(35.954652, -83.925869);
		},
		
		showMap: function (lat, lng) {
			var center = {
		        lat: lat,
		        lng: lng
	        };
	        this.map = new google.maps.Map(document.getElementById('gmap'), {
		        center: center,
		        zoom: this.zoom
		        // mapTypeId: google.maps.MapTypeId.ROADMAP
	        });
	        //My location
	        let marker = new google.maps.Marker({
	            position: center,
	            map: this.map,
	            icon: {
	            	path: google.maps.SymbolPath.CIRCLE,
	            	scale: 8,
	            	fillColor: 'blue',
	            	fillOpacity: 1,
	            	strokeColor: '#ffffff',
	            	strokeWeight: 2
	            }
	        });
	        let infowindow = new google.maps.InfoWindow({
				content: 'My location.',
				position: center
			});
			google.maps.event.addListener(marker, 'click', function(event) {
				infowindow.open(this.map, marker);
			});
	        //control
	        this.initControl();
	        //query data
			this.handleType();
		},
		initControl(){
			var div = document.getElementById('controlDiv');
			div.style.display = 'block';
			this.map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(div);
			
	        this.initControlPoint();
	        this.initControlLine();
	        this.initControlSpace();
	        this.initControlCircle();
	        this.initControlPolygon();
		},
		clearControlListener: function(){
			if(this.mousedownListener){
				this.mousedownListener.remove();
			}
			if(this.mousemoveListener){
				this.mousemoveListener.remove();
			}
			if(this.mouseupListener){
				this.mouseupListener.remove();
			}
			//draggable
			this.map.setOptions({draggable:true});
			//clear status
			this.canDraw = false;
			this.canMove = false;
		},
		clearDrawData: function(){
			this.path = [];
			this.marker = '';
		},
		changeActive: function(div){
			/*let pe = event.target.parentNode.parentNode.childNodes;
			for (var i = 0; i < pe.length; i++) {
				if(String(pe[i].className).indexOf('control-button') != -1){
					pe[i].className = 'control-button';
				}
			}
			event.target.parentNode.className = 'control-button active';*/
			$('#controlDiv button').removeClass('active');
			$('#'+div).addClass('active');
		},
		clearActive: function(){
			$('#controlDiv button').removeClass('active');
		},
		
		//point control
		initControlPoint: function(){
			let div = document.getElementById('pointDiv');
			let _this = this;
			google.maps.event.addDomListener(div, 'click', function(event) {
				_this.handleControlPoint(event);
			});
		},
		handleControlPoint: function(event){
			//clear
			this.clearControlListener();
			this.clearDrawData();
			//change
			this.changeActive('pointDiv');
			//draw
			this.canDraw = true;
			//draggable
			this.map.setOptions({draggable:false});
			//type
			this.drawType = 1;
			//event
	        this.initMousedown();
	        //this.initMousemove();
	        this.initMouseup();
		},
		//line control
		initControlLine: function(){
			let div = document.getElementById('lineDiv');
			let _this = this;
			google.maps.event.addDomListener(div, 'click', function(event) {
				_this.handleControlLine(event);
			});
		},
		handleControlLine: function(event){
			//clear
			this.clearControlListener();
			this.clearDrawData();
			//change
			this.changeActive('lineDiv');
			//draw
			this.canDraw = true;
			//draggable
			this.map.setOptions({draggable:false});
			//type
			this.drawType = 2;
			//event
	        this.initMousedown();
	        this.initMousemove();
	        this.initMouseup();
		},
		//space control
		initControlSpace: function(){
			let div = document.getElementById('spaceDiv');
			let _this = this;
			google.maps.event.addDomListener(div, 'click', function(event) {
				_this.handleControlSpace(event);
			});
		},
		handleControlSpace: function(event){
			//clear
			this.clearControlListener()
			this.clearDrawData();
			//change
			this.changeActive('spaceDiv');
			//draw
			this.canDraw = true;
			//draggable
			this.map.setOptions({draggable:false});
			//type
			this.drawType = 3;
			//event
	        this.initMousedown();
	        this.initMousemove();
	        this.initMouseup();
		},
		//circle control
		initControlCircle: function(){
			let div = document.getElementById('circleDiv');
			let _this = this;
			google.maps.event.addDomListener(div, 'click', function(event) {
				_this.handleControlCircle(event);
			});
		},
		handleControlCircle: function(event){
			//clear
			this.clearControlListener();
			this.clearDrawData();
			//change
			this.changeActive('circleDiv');
			//draw
			this.canDraw = true;
			//draggable
			this.map.setOptions({draggable:false});
			//type
			this.drawType = 4;
			//event
	        this.initMousedown();
	        this.initMousemove();
	        this.initMouseup();
		},
		//polygon control
		initControlPolygon: function(){
			let div = document.getElementById('polygonDiv');
			let _this = this;
			google.maps.event.addDomListener(div, 'click', function(event) {
				console.log(event);
				_this.handleControlPolygon(event);
			});
		},
		handleControlPolygon: function(event){
			//clear
			this.clearControlListener();
			this.clearDrawData();
			//change
			this.changeActive('polygonDiv');
			//draw
			this.canDraw = true;
			//draggable
			this.map.setOptions({draggable:false});
			//type
			this.drawType = 5;
			//event
	        this.initMousedown();
	        this.initMousemove();
	        this.initMouseup();
		},
		initMousedown: function(){
			console.log("initMousedown");
			let _this = this;
			this.mousedownListener = google.maps.event.addListener(this.map, 'mousedown', function(event) {
				_this.handleMousedown(event);
				event.wa.preventDefault();
			});
		},
		initMousemove: function(){
			console.log("initMousemove");
			let _this = this;
			this.mousemoveListener = google.maps.event.addListener(this.map, 'mousemove', function(event) {
				_this.handleMousemove(event);
			});
		},
		initMouseup: function(){
			console.log("initMouseup");
			let _this = this;
			this.mouseupListener = google.maps.event.addListener(this.map, 'mouseup', function(event) {
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
				this.drawStart = point;
				this.drawEnd = point;
				this.path.push(point);
				//move
				this.canMove = true;
				if(this.drawType === 1){ //point
					this.marker = new google.maps.Marker({
						map: this.map,
						position: point
					});
				}else if(this.drawType === 2){ //line
					this.marker = new google.maps.Polyline({
						strokeWeight: this.polylineStrokeWeight,
						map: this.map,
						path: this.path
					});
					//event
					let _this = this;
					google.maps.event.addListener(this.marker, 'mouseup', function(event) {
						_this.handleMouseup(event);
					});
				}else if(this.drawType === 3){ //space
					this.path[1] = this.path[0];
					this.marker = new google.maps.Rectangle({
						map: this.map,
						bounds: {
							south: this.path[0].lat,
							west: this.path[0].lng,
							north: this.path[1].lat,
							east: this.path[1].lng
						}
					});
					//event
					let _this = this;
					google.maps.event.addListener(this.marker , 'mousemove', function(event) {
						_this.handleMousemove(event);
					});
					google.maps.event.addListener(this.marker , 'mouseup', function(event) {
						_this.handleMouseup(event);
					});
				}else if(this.drawType === 4){ //circle
					this.marker = new google.maps.Circle({
						map: this.map,
						center: this.path[0],
						radius: 0
					});
					//event
					let _this = this;
					google.maps.event.addListener(this.marker , 'mousemove', function(event) {
						_this.handleMousemove(event);
					});
					google.maps.event.addListener(this.marker , 'mouseup', function(event) {
						_this.handleMouseup(event);
					});
					
				}else if(this.drawType === 5){ //polygon
					this.marker = new google.maps.Polygon({
						map: this.map,
						paths: this.path
					});
					//event
					let _this = this;
					google.maps.event.addListener(this.marker , 'mousemove', function(event) {
						_this.handleMousemove(event);
					});
					google.maps.event.addListener(this.marker , 'mouseup', function(event) {
						_this.handleMouseup(event);
					});
				}
				
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
				this.drawEnd = point;

				if(this.drawType === 1){ //point
					//do nothing
				}else if(this.drawType === 3){ //space
					this.path.splice(1, 1, point);
					if(Math.sign(this.drawStart.lng) == Math.sign(this.drawEnd.lng) 
							&& this.drawEnd.lng - this.drawStart.lng >= 0
							|| Math.sign(this.drawStart.lng) != Math.sign(this.drawEnd.lng)
							&& this.drawEnd.lng - this.drawStart.lng <= 0){
						this.marker.setBounds({
							south: this.path[0].lat,
							west: this.path[0].lng,
							north: this.path[1].lat,
							east: this.path[1].lng
						});
					}else{
						this.marker.setBounds({
							south: this.path[1].lat,
							west: this.path[1].lng,
							north: this.path[0].lat,
							east: this.path[0].lng
						});
					}
					
				}else if(this.drawType === 4){ //circle
					this.path.splice(1, 1, point);
					let c = this.lonLatToMercator(this.path[0].lng, this.path[0].lat);
					let p = this.lonLatToMercator(this.path[1].lng, this.path[1].lat);
					this.marker.setRadius(Math.sqrt(Math.pow(p.x-c.x,2) + Math.pow(p.y-c.y,2)));
				}else if(this.drawType === 2 || this.drawType === 5){ //line polygon
					this.path.push(point);
					this.marker.setPath(this.path);
				}
			}
		},
		handleMouseup: function(event){
			console.log("handleMouseup");
			if(this.canDraw){
				//clear
				this.clearControlListener();
				this.clearActive();
				//show window
				this.addFormVisible = true;
			}
			
		},
		lonLatToMercator: function(lon, lat){
			var toX = lon * 20037508.342789244 / 180;
            var toY = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
            toY = toY * 20037508.342789244 / 180;
            return {
            	x: toX,
            	y: toY
            };
		},
		
		
		drawMarker: function(data, index){
			if(!data.path){
				return;
			}
			let paths = JSON.parse(data.path);
			if(paths.length == 0){
				return;
			}
			
			let content = [];
			content.push("Options: ");
			let color = this.colors[0]; //index % this.colors.length
			let options = data.option;
			if(options){
				let opt = options.split(",");
				for (var i = 0; i < opt.length; i++) {
					for (var j = 0; j < this.typeOptions.length; j++) {
						if(opt[i] == this.typeOptions[j].value){
							content.push("&nbsp;&nbsp;"+this.typeOptions[j].label);
						}
					}
				}
			}
			content.push("<br/>");
			content.push("Comment: ");
			content.push(data.content);
			
			let marker;
			let position;
			if(data.type === 1){ //point
				marker = new google.maps.Marker({
					map: this.map,
					position: paths[0]
				});
				position = paths[0];
			}else if(data.type === 2){ //line
				marker = new google.maps.Polyline({
					strokeWeight: this.polylineStrokeWeight,
					strokeColor: color,
					map: this.map,
					path: paths
				});
				position = paths[Math.floor(paths.length/2)];
			}else if(data.type === 3){ //space
				marker = new google.maps.Rectangle({
					strokeColor: color,
					fillColor: color,
					fillOpacity: 0.5,
					map: this.map
				});
				if(Math.sign(paths[0].lng) == Math.sign(paths[1].lng) 
						&& paths[1].lng - paths[0].lng >= 0
						|| Math.sign(paths[0].lng) != Math.sign(paths[1].lng)
						&& paths[1].lng - paths[0].lng <= 0){
					marker.setBounds({
						south: paths[0].lat,
						west: paths[0].lng,
						north: paths[1].lat,
						east: paths[1].lng
					});
				}else{
					marker.setBounds({
						south: paths[1].lat,
						west: paths[1].lng,
						north: paths[0].lat,
						east: paths[0].lng
					});
				}
			}else if(data.type === 4){ //circle
				marker = new google.maps.Circle({
					strokeColor: color,
					fillColor: color,
					fillOpacity: 0.5,
					map: this.map,
					center: paths[0]
				});
				let c = this.lonLatToMercator(paths[0].lng, paths[0].lat);
				let p = this.lonLatToMercator(paths[1].lng, paths[1].lat);
				marker.setRadius(Math.sqrt(Math.pow(p.x-c.x,2) + Math.pow(p.y-c.y,2)));
			}else if(data.type === 5){ //polygon
				marker = new google.maps.Polygon({
					strokeColor: color,
					fillColor: color,
					fillOpacity: 0.5,
					map: this.map,
					paths: paths
				});
			}
			
			var infowindow = new google.maps.InfoWindow({
				content: content.join('<br/>'),
				position: paths[0]
			});
			
			//event
			google.maps.event.addListener(marker, 'click', function(event) {
				infowindow.open(this.map, marker);
			});
			return marker;
		},
		//reset
		reset: function(){
			this.addForm = {
				local: '',
				path: '',
				type: '',
				option: [],
				content: ''
			};
			this.path = [];
			this.marker.setMap(null);
			this.marker = "";
		},
		//query
		getList: function(){

			var url = baseUrl + "api/content/findList";
			var params = {
					user: this.user.pid
			};
			var _this = this;
			ajaxReq(url, params, function(res){
				if(res.code > 0){
					console.log(res.data);
					for (var i = 0; i < res.data.length; i++) {
						let marker = _this.drawMarker(res.data[i], i);
						_this.markers.push(marker);
						//event
						google.maps.event.addListener(marker, 'mousedown', function(event) {
							_this.handleMousedown(event);
							event.wa.preventDefault();
						});
						google.maps.event.addListener(marker, 'mousemove', function(event) {
							_this.handleMousemove(event);
						});
						google.maps.event.addListener(marker, 'mouseup', function(event) {
							_this.handleMouseup(event);
						});
					}
				}else{
					_this.$message({
						message: 'Failure to obtain data.',
						type: 'warning'
					});
				}
			});
		},
		handleType(){
			let self = this;
			ajaxReq(optionsUrl, {}, function(res){
				if(res.code > 0){
					self.typeOptions = [];
					for (var i = 0; i < res.data.length; i++) {
						self.typeOptions.push({
							value: res.data[i].pid, label: res.data[i].name
						});
					}
					self.getList();
				}
			});
		},
		//add
		addClose: function () {
			//clear
			this.clearControlListener();
			this.reset();
			this.addFormVisible = false;
			this.addLoading = false;
			this.$refs.addForm.resetFields();
		},
		addSubmit: function () {
			this.$refs.addForm.validate((valid) => {
				if (valid) {
					this.$confirm('Confirmation of submission?', 'Tips', {}).then(() => {
						var url = baseUrl + "api/content/add";
						var params = Object.assign({}, this.addForm);
						params.type = this.drawType;
						params.path = JSON.stringify(this.path);
						params.option = params.option.join(',');
						var self = this;
						this.addLoading = true;
						ajaxReq(url, params, function(res){
							self.addLoading = false;
							if(res.code > 0){
								self.$message({
									message: 'success',
									type: 'success'
								});
								self.addFormVisible = false;
								self.getList();
								self.reset();
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
		//退出登录
		logout: function () {
			this.$confirm('Confirmation of withdrawal?', 'Tips', {
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
					sessionStorage.setItem('user', JSON.stringify(res.data));
				}
			});
		},
	},
	mounted: function() {
		this.user = JSON.parse(sessionStorage.getItem('user'));
		if(this.user == null){
	   		window.location.href = "login.html";
		}
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
