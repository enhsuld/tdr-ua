angular
    .module('altairApp')
    .controller('main_headerCtrl', [
        '$timeout',
        '$rootScope',
        '$scope',
        '$window',
        'sessionService',
        'user_data',
        '$http',
        'Idle',
        '$state',
        '$location',
        function ($timeout,$rootScope,$scope,$window,sessionService,user_data,$http,Idle,$state,$location) {
        	  $scope.isLoggedIn = sessionService.isLoggedIn;
           
	    	  $scope.logout = sessionService.logout;
	    	  $('.dropify').dropify();
        	  
        	  //console.log(user_data[0]);

	    	/*  $scope.logout = function($state){	    		 
	    	  	  $http.post('/logout', {}).then(function($state) {
	    	  		    $rootScope.authenticated = false;
	    	  		    $rootScope.user = {};
	    	  		 
	    	  		    $state.go('login');
	    	  		  });
	    	  }*/
	    	  
	    	  $http.get("/getMessages")
	    	    .then(function(response) {
	    	        $scope.messages = response.data;
	    	        if ($scope.messages.messages.filter(function(x){ return x.isread == false; }).length > 0){
	    	        	$scope.showMessage($scope.messages.messages.filter(function(x){ return x.isread == false; })[0]);
	    	        	//$cookies.put("last_message_id",$scope.messages.messages.filter(function(x){ return x.isread == false; })[0].id);
	    	        }
	    	        else if ($scope.messages.alerts.filter(function(x){ return x.isread == false; }).length > 0){
	    	        	$scope.showMessage($scope.messages.alerts.filter(function(x){ return x.isread == false; })[0]);
	    	        	//$cookies.put("last_message_id",$scope.messages.alerts.filter(function(x){ return x.isread == false; })[0].id);
	    	        }
	    	        $scope.unread_msgs = $scope.messages.messages.filter(function(x){ return x.isread == false; }).length;
	    	        $scope.unread_alerts = $scope.messages.alerts.filter(function(x){ return x.isread == false; }).length;
	    	    });
	    	  var changePasswordDialogModal = UIkit.modal("#changePasswordDialog");
	  		    $scope.changePasswordDialog=function(){
	  		    	changePasswordDialogModal.show();
	  		    }
	    	  $rootScope.user = user_data[0];
	    	  $scope.user=user_data[0];
	    	  $scope.user_data = {
	                  name: "Lue Feest",
	                  avatar: "assets/img/avatars/avatar_11_tn.png",
	                  alerts: [
	                      {
	                          "title": "Hic expedita eaque.",
	                          "content": "Nemo nemo voluptatem officia voluptatum minus.",
	                          "type": "warning"
	                      },
	                      {
	                          "title": "Voluptatibus sed eveniet.",
	                          "content": "Tempora magnam aut ea.",
	                          "type": "success"
	                      },
	                      {
	                          "title": "Perferendis voluptatem explicabo.",
	                          "content": "Enim et voluptatem maiores ab fugiat commodi aut repellendus.",
	                          "type": "danger"
	                      },
	                      {
	                          "title": "Quod minima ipsa.",
	                          "content": "Vel dignissimos neque enim ad praesentium optio.",
	                          "type": "primary"
	                      }
	                  ],
	                  messages: [
	                      {
	                          "title": "Reiciendis aut rerum.",
	                          "content": "In adipisci amet nostrum natus recusandae animi fugit consequatur.",
	                          "sender": "Korbin Doyle",
	                          "color": "cyan"
	                      },
	                      {
	                          "title": "Tenetur commodi animi.",
	                          "content": "Voluptate aut quis rerum laborum expedita qui eaque doloremque a corporis.",
	                          "sender": "Alia Walter",
	                          "color": "indigo",
	                          "avatar": "assets/img/avatars/avatar_07_tn.png"
	                      },
	                      {
	                          "title": "At quia quis.",
	                          "content": "Fugiat rerum aperiam et deleniti fugiat corporis incidunt aut enim et distinctio.",
	                          "sender": "William Block",
	                          "color": "light-green"
	                      },
	                      {
	                          "title": "Incidunt sunt.",
	                          "content": "Accusamus necessitatibus officia porro nisi consectetur dolorem.",
	                          "sender": "Delilah Littel",
	                          "color": "blue",
	                          "avatar": "assets/img/avatars/avatar_02_tn.png"
	                      },
	                      {
	                          "title": "Porro ut.",
	                          "content": "Est vitae magni eum expedita odit quisquam natus vel maiores.",
	                          "sender": "Amira Hagenes",
	                          "color": "amber",
	                          "avatar": "assets/img/avatars/avatar_09_tn.png"
	                      }
	                  ]
	              };
	    	  
		    	  $scope.changeUserDataDialog = function(){
		    		  UIkit.modal("#changeUserdataDialog").show();
		    	  }

	              $scope.alerts_length = $scope.user_data.alerts.length;
	              $scope.messages_length = $scope.user_data.messages.length;
	              $scope.showMessage = function(news){
	            	  if (news.isread == false){
	            		  $http.get("/markAsReadMessage/" + news.id)
			  	    	    .then(function(response) {
			  	    	    	if (response == true){
			  	    	    		news.isread = true;
			  	    	    		if (news.status == 1){
			  	    	    			$scope.unread_alerts = $scope.unread_alerts - 1;
			  	    	    		}
			  	    	    		else{
			  	    	    			$scope.unread_msgs = $scope.unread_msgs - 1;
			  	    	    		}
			  	    	    	}
			  	    	    });
	            	  }
	            	  
	            	  UIkit.modal.alert("<h2>" + news.title + "</h2><p class='uk-overflow-container' style='white-space: pre-line;text-align: justify;'>"+news.description+"<br><span class='uk-text-muted'>"+news.createdat+"</span></p>")
	              }

	          $('#menu_top').children('[data-uk-dropdown]').on('show.uk.dropdown', function(){
	              $timeout(function() {
	                  $($window).resize();
	              },280)
	          });
	
	       // append modal to <body>
	          $('body').append('<div class="uk-modal" id="modal_idle">' +
	              '<div class="uk-modal-dialog">' +
	                  '<div class="uk-modal-header">' +
	                      '<h3 class="uk-modal-title">Анхааруулга!</h3>' +
	                  '</div>' +
	                  '<p>Системийн аюулгүй байдлын үүднээс идэвхигүй 9 минут болоход анхааруулга идэвхижэж 1 минутын дараа холболтыг автоматаар таслана. </p>' +
	                  '<p>Холболтоо таслахыг хүсэхгүй байвал үйлдэл хийнэ үү. </p>' +
	                  '<p>Үлдсэн хугацаа <span class="uk-text-bold md-color-red-500" id="sessionSecondsRemaining"></span> секунт.</p>' +
	              '</div>' +
	          '</div>');
	
	          var modal = UIkit.modal("#modal_idle", {
	                  bgclose: false
	              }),
	              $sessionCounter = $('#sessionSecondsRemaining');
	
	          Idle.watch();
	          $scope.$on('IdleWarn', function(e, countdown) {
	              modal.show();
	              $sessionCounter.html(countdown)
	          });
	
	          $scope.$on('IdleEnd', function() {
	              modal.hide();
	              $sessionCounter.html('');
	          });
	
	          $scope.$on('IdleTimeout', function() {
	              modal.hide();
	              $http.post("/logout", {}).success(function($state) {
	  	   		    $rootScope.authenticated = false;
	  	   		    $location.path("/login");
	  	   		    localStorage.setItem("session", false);
	  	   		  });
	             
	          });
        }
    ])
    .config(function(IdleProvider, KeepaliveProvider) {
        // configure Idle settings
        IdleProvider.idle(540); // in seconds
        IdleProvider.timeout(60); // in seconds
        IdleProvider.keepalive(false);
    });