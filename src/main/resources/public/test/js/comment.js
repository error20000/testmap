var baseUrl = parent.window.baseUrl || '../';

var queryUrl = baseUrl + "api/content/findPage";
var addUrl = baseUrl + "api/content/add";
var modUrl = baseUrl + "api/content/update";
var delUrl = baseUrl + "api/content/delete";
var excelUrl = baseUrl + "api/content/excel";
var userUrl = baseUrl + "api/user/findAll";
var optionsUrl = baseUrl + "api/options/findAll";
var listMapUrl = baseUrl + "api/content/findList";

var ajaxReq = parent.window.ajaxReq || "";


var myvue = new Vue({
	    el: '#app',
	    data: function(){
	    	return {
	    		activeTab: 'table',
				filters: {
					user: '',
					start: '',
					end: ''
				},
				list: [],
				total: 0,
				page: 1,
				rows: 10,
				listLoading: false,
				sels: [],
				preloading: false,
				
				userOptions: [],
				typeOptions: [
					{pid: 1, name: 'Point'},
					{pid: 2, name: 'Line'},
					{pid: 3, name: 'Rectangle'},
					{pid: 4, name: 'Circle'},
					{pid: 5, name: 'Polygon'},
				],
				optOptions: [],
				
				center: {
					lat: 35.954652,
					lng: -83.925869
				},
				zoom: 12,
				map: '',
				markers: [],
				colors: COLORS,
				vmap: '',
				vmarker: '',
				
				//view
				mapFormVisible: false,
				viewData: '',
				
				
				end: ''
			}
		},
		methods: {
			formatDate: function(date){
				return parent.window.formatDate(date, 'yyyy-MM-dd HH:mm:ss');
			},
			formatUser: function(row){
				let value = row.user;
				for (var i = 0; i < this.userOptions.length; i++) {
					if(row.user == this.userOptions[i].pid){
						value = this.userOptions[i].username;
						break;
					}
				}
				return value;
			},
			formatType: function(row){
				let value = row.type;
				for (var i = 0; i < this.typeOptions.length; i++) {
					if(row.type == this.typeOptions[i].pid){
						value = this.typeOptions[i].name;
						break;
					}
				}
				return value;
			},
			handleSizeChange: function (val) {
				this.rows = val;
				this.getList();
			},
			handleCurrentChange: function (val) {
				this.page = val;
				this.getList();
			},
			handleUser: function(){
				let _this = this;
				ajaxReq(userUrl, {}, function(res){
					if(res.code > 0){
						_this.userOptions = res.data;
					}
				});
			},
			handleOpt: function(){
				let _this = this;
				ajaxReq(optionsUrl, {}, function(res){
					if(res.code > 0){
						_this.optOptions = res.data;
					}
				});
			},
			findUser: function(userId){
				let user = {};
				for (var i = 0; i < this.userOptions.length; i++) {
					if(userId == this.userOptions[i].pid){
						user = this.userOptions[i];
						break;
					}
				}
				return user;
			},
			query:function(){
				this.getList();
				this.showMap();
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
			getExcel: function(){
				this.query();
				var params = "";
				for ( var key in this.filters) {
					if(this.filters[key]){
						params += "&"+key+"="+this.filters[key];
					}
				}
				params += "&userId="+parent.window.loginUserId;
				parent.window.open(excelUrl+(params ? "?"+params.substring(1) : ""));
			},
			//map
			showMap: function () {
		        this.map = new google.maps.Map(document.getElementById('gmap'), {
			        center: this.center,
			        zoom: this.zoom
			        // mapTypeId: google.maps.MapTypeId.ROADMAP
		        });
		        //My location
		        let marker = new google.maps.Marker({
		            position: this.center,
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
					position: this.center
				});
				google.maps.event.addListener(marker, 'click', function(event) {
					infowindow.open(this.map, marker);
				});
		        //data
		        this.getListMap();
			},
			getListMap: function () {
				var self = this;
				var params = {};
				for ( var key in this.filters) {
					if(this.filters[key]){
						params[key] = this.filters[key];
					}
				}
				ajaxReq(listMapUrl, params, function(res){
					self.handleResQuery(res, function(){
						for (var i = 0; i < res.data.length; i++) {
							let marker = self.drawMarker(res.data[i], i, self.map);
							self.markers.push(marker);
						}
					});
				});
			},
			drawMarker: function(data, index, map){
				if(!data.path){
					return;
				}
				let paths = JSON.parse(data.path);
				if(paths.length == 0){
					return;
				}
				
				let content = [];
				content.push("<p>User: "+this.formatUser(data)+"</p>");
				content.push("<p>Date: "+data.date+"</p>");
				content.push("<p>Location: "+data.local+"</p>");
				content.push("<p>Options: </p>");
				let color = this.findUser(data.user).color || "#000000"; //this.colors[data.user % this.colors.length]; //index % this.colors.length
				let options = data.option;
				if(options){
					let opt = options.split(",");
					for (var i = 0; i < opt.length; i++) {
						for (var j = 0; j < this.optOptions.length; j++) {
							if(opt[i] == this.optOptions[j].pid){
								content.push("<p>"+(i+1)+". "+this.optOptions[j].name+"</p>");
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
						map: map,
						position: paths[0]
					});
					position = paths[0];
				}else if(data.type === 2){ //line
					marker = new google.maps.Polyline({
						strokeWeight: this.polylineStrokeWeight,
						strokeColor: color,
						map: map,
						path: paths
					});
					position = paths[Math.floor(paths.length/2)];
				}else if(data.type === 3){ //space
					marker = new google.maps.Rectangle({
						strokeColor: color,
						fillColor: color,
						fillOpacity: 0.5,
						map: map
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
						map: map,
						center: paths[0],
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
						map: map,
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
					infowindow.open(map, marker);
				});
				return marker;
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
			
			getLocation(){
	            var options={
	                enableHighAccuracy:true, 
	                maximumAge:1000
	            }
	            if(navigator.geolocation){
	                //geolocation
	                navigator.geolocation.getCurrentPosition(this.onSuccess, this.onError);
	                
	            }else{
	                //not geolocation
	            	alert("Browsers do not support geolocation。"); 
	            	this.showMap();
	            }
	        },
	        onSuccess(position){
	            var lng = position.coords.longitude;
	            var lat = position.coords.latitude;
	            this.center = {lat: lat, lng: lng};
            	this.showMap();
	        },
	        onError(error){ 
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
            	this.showMap();
			},
			//view
			handleMap: function(index, data){
				this.mapFormVisible = true;
				this.viewData = data;
			},
			viewMap: function(){
				setTimeout(() => {
					let center = '';
					if(this.viewData.location){
						center = JSON.parse(this.viewData.location)
					}else{
						center = JSON.parse(this.viewData.path)[0];
					}
					this.vmap = new google.maps.Map(document.getElementById('vmap'), {
						center: center,
						zoom: this.zoom
					});
					//data
					this.vmarker = this.drawMarker(this.viewData, 0, this.vmap);
				}, 500);
			},
			viewClose: function(){
				this.mapFormVisible = false;
				if(this.vmarker){
					this.vmarker.setMap(null);
				}
				if(this.vmap){
					document.getElementById('vmap').innerHTML = "";
				}
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
							self.showMap();
						});
					});
					
				}).catch(() => {
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
			this.handleUser();
			this.handleOpt();
			this.getList();
			this.getLocation();
		}
	  });
	
	

