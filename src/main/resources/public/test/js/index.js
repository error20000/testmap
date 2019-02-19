var baseUrl = '../';
var optionsUrl = baseUrl + "api/options/all";

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
			userLocalCenter: '',
			userLocalMarker: '',
			
			tracePath: [],
			traceMarkers: [],
			
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
			uploadProgressVisible: false,
			uploadProgress: 0,
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
		handleProgress: function(event, file, fileList){
			let _this = this;
			let percent = event.percent;
			this.uploadProgressVisible = true;
			this.uploadProgress = Math.floor(percent);
			if(percent == 100){
				setTimeout(() => {
					_this.uploadProgressVisible = false;
					_this.uploadProgress = 0;
				}, 1000);
			}
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
            this.userLocalCenter = JSON.stringify({lat: lat, lng: lng});
            
            //google 
            this.showMap(lat, lng);

            //watch
            //navigator.geolocation.watchPosition(this.watchPosition);
            //秒级
            this.onWatchPosition();
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
		onWatchPosition: function(){
			let _this = this;
			setInterval(() => {
				 navigator.geolocation.getCurrentPosition(_this.watchPosition, _this.onError);
			}, 1000);
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
	        this.drawLocal(center);
	        //control
	        this.initControl();
	        //query data
			this.handleType();
		},
		drawLocal: function(center){
	        this.userLocalMarker = new google.maps.Marker({
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
			google.maps.event.addListener(this.userLocalMarker, 'click', function(event) {
				infowindow.open(this.map, this.userLocalMarker);
			});
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
	        this.initControlTrace();
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
		clearTraceData: function(){
			this.tracePath = [];
		},
		changeActive: function(div){
			$('#controlDiv .draw').removeClass('active');
			$('#'+div).addClass('active');
		},
		clearActive: function(){
			$('#controlDiv .draw').removeClass('active');
		},
		
		changeTrace: function(div){
			$('#controlDiv .trace').removeClass('active');
			$('#'+div).addClass('active');
			$('.trace_tips').show();
		},
		clearTrace: function(){
			$('#controlDiv .trace').removeClass('active');
			$('.trace_tips').hide();
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
			let has = $('#pointDiv').hasClass('active');
			if(has){
				this.clearActive();
				return;
			}
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
			let has = $('#lineDiv').hasClass('active');
			if(has){
				this.clearActive();
				return;
			}
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
			let has = $('#spaceDiv').hasClass('active');
			if(has){
				this.clearActive();
				return;
			}
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
			let has = $('#circleDiv').hasClass('active');
			if(has){
				this.clearActive();
				return;
			}
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
			let has = $('#polygonDiv').hasClass('active');
			if(has){
				this.clearActive();
				return;
			}
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
				if(event.wa){
					event.wa.preventDefault();
				}
				if(event.va){
					event.va.preventDefault();
				}
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
				let self = this;
				ajaxReq(optionsUrl, {}, function(res){
					if(res.code > 0){
						self.typeOptions = [];
						for (var i = 0; i < res.data.length; i++) {
							self.typeOptions.push({
								value: res.data[i].pid, label: res.data[i].name
							});
						}
						self.addFormVisible = true;
					}
				});
				
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
			content.push("<p>Options: </p>");
			let color = this.user.color || "#000000"; //this.colors[0]; //index % this.colors.length
			let options = data.option;
			if(options){
				let opt = options.split(",");
				for (var i = 0; i < opt.length; i++) {
					for (var j = 0; j < this.typeOptions.length; j++) {
						if(opt[i] == this.typeOptions[j].value){
							content.push("<p>"+(i+1)+". "+this.typeOptions[j].label+"</p>");
						}
					}
				}
			}
			content.push("<p>Comment: </p>");
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
					let sw = {lat: paths[0].lat, lng: paths[0].lng};
					let ne = {lat: paths[1].lat, lng: paths[1].lng};
					let temp = new google.maps.LatLngBounds(sw, ne);
					position = temp.getCenter();
				}else{
					marker.setBounds({
						south: paths[1].lat,
						west: paths[1].lng,
						north: paths[0].lat,
						east: paths[0].lng
					});
					let sw = {lat: paths[1].lat, lng: paths[1].lng};
					let ne = {lat: paths[0].lat, lng: paths[0].lng};
					let temp = new google.maps.LatLngBounds(sw, ne);
					position = temp.getCenter();
				}
			}else if(data.type === 4){ //circle
				marker = new google.maps.Circle({
					strokeColor: color,
					fillColor: color,
					fillOpacity: 0.5,
					map: this.map,
					center: paths[0]
				});
				position = paths[0];
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
				//center
				let sw = {lat: 0, lng:0};
				let ne = {lat: 0, lng:0};
				for (var i = 0; i < paths.length; i++) {
					if(i == 0){
						sw.lat = paths[i].lat;
						sw.lng = paths[i].lng;
						ne.lat = paths[i].lat;
						ne.lng = paths[i].lng;
					}else{
						if(paths[i].lat < sw.lat){
							sw.lat = paths[i].lat;
						}
						if(paths[i].lng < sw.lng){
							sw.lng = paths[i].lng;
						}
						if(paths[i].lat > ne.lat){
							ne.lat = paths[i].lat;
						}
						if(paths[i].lng > ne.lng){
							ne.lng = paths[i].lng;
						}
					}
				}
				let temp = new google.maps.LatLngBounds(sw, ne);
				position = temp.getCenter();
			}
			
			var infowindow = new google.maps.InfoWindow({
				content: content.join(''),
				position: position
			});
			
			//event
			google.maps.event.addListener(marker, 'click', function(event) {
				infowindow.open(this.map, marker);
			});
			return marker;
		},

		//trace control
		initControlTrace: function(){
			let div = document.getElementById('traceDiv');
			let _this = this;
			google.maps.event.addDomListener(div, 'click', function(event) {
				console.log(event);
				_this.handleControlTrace(event);
			});
		},
		handleControlTrace: function(event){
			//change
			let has = $('#traceDiv').hasClass('active');
			if(has){
				this.clearTrace();
				this.canTrace = false;
				this.traceSubmit();
				return;
			}
			this.changeTrace('traceDiv');
			//clear
			this.clearTraceData();
			//trace
			this.canTrace = true;
		},
		watchPosition: function(position){
			let lng = position.coords.longitude;
			let lat = position.coords.latitude;
			let center = {lat: lat, lng: lng};
			console.log("watchPosition");
			//local
			this.userLocalMarker.setPosition(center);
			//trace
			if(this.canTrace){
				console.log("drawTrace");
				let lng = position.coords.longitude;
				let lat = position.coords.latitude;
				this.tracePath.push(center);
			}
		},
		traceSubmit: function(){
			console.log(this.tracePath);
			if(this.tracePath.length == 0){
				return;
			}
			if(!this.user){
				return;
			}
			let _this = this;
			let url = baseUrl + "api/trace/add";
			let params = {};
			params.user = this.user.pid;
			params.local = this.userLocalCenter;
			params.path = JSON.stringify(this.tracePath);
			ajaxReq(url, params, function(res){
				if(res.code > 0){
					_this.$message({
						message: 'success',
						type: 'success'
					});
					_this.getTraceList();
				}else{
					_this.$message({
						message: 'failed',
						type: 'warning'
					})
				}
			});
		},
		drawTrace: function(data, index){
			if(!data.path){
				return;
			}
			let paths = JSON.parse(data.path);
			if(paths.length == 0){
				return;
			}
			
			let color = this.user.color || "#000000";
			let marker;
			let position;
			marker = new google.maps.Polyline({
				strokeWeight: this.polylineStrokeWeight,
				strokeColor: color,
				map: this.map,
				path: paths
			});
			position = paths[Math.floor(paths.length/2)];
			
			var infowindow = new google.maps.InfoWindow({
				content: "trace path.",
				position: position
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
					//clear
					if(_this.markers.length > 0){
						for (var i = 0; i < _this.markers.length; i++) {
							_this.markers[i].setMap(null);
						}
					}
					_this.markers = [];
					//draw
					for (var i = 0; i < res.data.length; i++) {
						let marker = _this.drawMarker(res.data[i], i);
						_this.markers.push(marker);
						//event
						google.maps.event.addListener(marker, 'mousedown', function(event) {
							_this.handleMousedown(event);
							if(event.wa){
								event.wa.preventDefault();
							}
							if(event.va){
								event.va.preventDefault();
							}
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
		getTraceList: function(){
			var url = baseUrl + "api/trace/findList";
			var params = {
					user: this.user.pid
			};
			var _this = this;
			ajaxReq(url, params, function(res){
				if(res.code > 0){
					console.log('trace data');
					console.log(res.data);
					//clear
					if(_this.traceMarkers.length > 0){
						for (var i = 0; i < _this.traceMarkers.length; i++) {
							_this.traceMarkers[i].setMap(null);
						}
					}
					_this.traceMarkers = [];
					//draw
					for (var i = 0; i < res.data.length; i++) {
						let marker = _this.drawTrace(res.data[i], i);
						_this.traceMarkers.push(marker);
						//event
						/*google.maps.event.addListener(marker, 'mousedown', function(event) {
							_this.handleMousedown(event);
							if(event.wa){
								event.wa.preventDefault();
							}
							if(event.va){
								event.va.preventDefault();
							}
						});
						google.maps.event.addListener(marker, 'mousemove', function(event) {
							_this.handleMousemove(event);
						});
						google.maps.event.addListener(marker, 'mouseup', function(event) {
							_this.handleMouseup(event);
						});*/
					}
				}else{
					_this.$message({
						message: 'Failure to load trace data.',
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
					self.getTraceList();
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
						params.local = this.userLocalCenter;
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
					localStorage.setItem('user', JSON.stringify(res.data));
				}
			});
		},
	},
	mounted: function() {
		this.user = JSON.parse(localStorage.getItem('user'));
		if(this.user == null){
	   		window.location.href = "login.html";
		}
		loginUserId = this.user.pid;
		this.isLogin(this.getLocation);
	}
  });

