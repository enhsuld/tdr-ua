altairApp
    .service('detectBrowser', [
        '$window',
        function($window) {
            // http://stackoverflow.com/questions/22947535/how-to-detect-browser-using-angular
            return function() {
                var userAgent = $window.navigator.userAgent,
                    browsers  = {
                        chrome  : /chrome/i,
                        safari  : /safari/i,
                        firefox : /firefox/i,
                        ie      : /internet explorer/i
                    };

                for ( var key in browsers ) {
                    if ( browsers[key].test(userAgent) ) {
                        return key;
                    }
                }
                return 'unknown';
            }
        }
    ])
     .factory('customLoader', function ($http, $q) {
	    return function (options) {
	      var deferred = $q.defer();
	   /*   $http({
	    	   method:'GET',
		       url:'/audit/api/lang/' + options.key 
	      }).success(function (data) {
	        deferred.resolve(data);
	      }).error(function () {
	        deferred.reject(options.key);
	      });*/
	      
	      return deferred.promise;
	    }
	})
  .factory('downloadService', ['$q', '$timeout', '$window',
        function($q, $timeout, $window) {
            return {
                download: function(name) {

                    var defer = $q.defer();

                    $timeout(function() {
                            $window.location = 'download?name=' + name;

                        }, 1000)
                        .then(function() {
                            defer.resolve('success');
                        }, function() {
                            defer.reject('error');
                        });
                    return defer.promise;
                }
            };
        }
    ])
   .service('sessionService',[
       '$rootScope',
       '$http',
       '$location',
       function($rootScope,$http,$location,$state) {
    	   

	    var session = {};
	    session.login = function(data) {
	        return $http.post("/login", "username=" + data.username +
	        "&password=" + data.password, {
	            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
	        } ).then(function(data) {
	        	var success=data.data;
	        	if(success!=false){
	        		// alert("hooo noo success");
	        		 $rootScope.authenticated = true;
	        		 localStorage.setItem("session", {});
	        	}
	        	else{
	        		  $rootScope.authenticated = false;
	        	}
	        }, function(data) {
	           // alert("error logging in");
	        });
	    };

	    session.logout = function() {
	   		  $http.post("/logout", {}).success(function() {
	   		    $rootScope.authenticated = false;
	   		    $location.path("/login");
	   		    localStorage.setItem("session", false);
	   		  }).error(function(data) {
	   		    $rootScope.authenticated = false;
	   		  });
	   		};

	    session.isLoggedIn = function() {
	        return localStorage.getItem("session") !== null;
	    };
	    return session;
	}])
	
	.service('fileUpload', ['$http','$q','sweet', function ($http,$q,sweet) {
	    this.uploadFileToUrl = function(uploadUrl, file){
	    	var deferred = $q.defer();
	        var fd = new FormData();
	        fd.append('file', file);
	        $http.post(uploadUrl, file, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}
	        })
           .then(function (response) {
        	   deferred.resolve(response.data);
	        });
	        
	        return deferred.promise;
	    }
	}])

	.service('mainService', function ($http, $q) {
		var master= {};
		
	    this.add=function(item){    	
	    	master= angular.copy(item);
	    	return master;
	    }
	    this.view=function(){    
	    	
	    	return master;
	    }
	    
	    this.getDetail=function(curl){
			var deferred = $q.defer();

	        $http({
				method:'GET',
	        	url:curl
	        })
	        .then(function (response) {
	            if (response.status == 200) {
	                deferred.resolve(response.data);
	            }
	            else {
	                deferred.reject('Error getting');
	            }
	        });

	        return deferred.promise;
		}
	    
	    this.withdomain=function(method,url){
	    	var deferred = $q.defer();
	        $http({
	            method:method,           
	            url:url           
	        })
	        .then(function (response) {
	        	 deferred.resolve(response.data);
	        });
	
	        return deferred.promise;
	    }
	    
	    this.withdata=function(method,url,data){
	    	var deferred = $q.defer();
	
	        $http({
	        	 method:method,
	             url:url,
	             data: data
	        })
	        .then(function (response) {
	            if (response.status == 200) {
	                deferred.resolve(response.data);
	            }
	            else {
	            	deferred.reject('Error occured doing action withdata');
	            }
	        });
	
	        return deferred.promise;
	    }
	    
	    this.http=function(method,url){
	    	var deferred = $q.defer();
	
	        $http({
	            method:method,
	            url:url
	        })
	        .then(function (response) {
	            if (response.status == 200) {
	                deferred.resolve(response.data);
	            }
	            else {
	                deferred.reject('Error occured doing action withoutdata');
	            }
	        });
	
	        return deferred.promise;
	    }
	    
	    this.http=function(method,url,data){
	    	var deferred = $q.defer();
	
	        $http({
	        	 method:method,
	             url:url,
	            data: data
	        })
	        .then(function (response) {
	            if (response.status == 200) {
	                deferred.resolve(response.data);
	            }
	            else {
	            	deferred.reject('Error occured doing action withdata');
	            }
	        });
	
	        return deferred.promise;
	    }
	})
    .service('preloaders', [
        '$rootScope',
        '$timeout',
        'utils',
        function($rootScope,$timeout,utils) {
            $rootScope.content_preloader_show = function(style,variant,container,width,height) {
                var $body = $('body');
                if(!$body.find('.content-preloader').length) {
                    var image_density = utils.isHighDensity() ? '@2x' : '',
                        width = width ? width : 48,
                        height = height ? height : 48;

                    var preloader_content = (style == 'regular')
                        ? '<img src="assets/img/spinners/spinner' + image_density + '.gif" alt="" width="32" height="32">'
                        : '<div class="md-preloader"><svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="'+height+'" width="'+width+'" viewbox="0 0 75 75"><circle cx="37.5" cy="37.5" r="33.5" stroke-width="8"/></svg></div>';

                    var thisContainer = (typeof container !== 'undefined') ? $(container) : $body;

                    thisContainer.append('<div class="content-preloader content-preloader-'+variant+'" style="height:'+height+'px;width:'+width+'px;margin-left:-'+width/2+'px">' + preloader_content + '</div>');
                    $timeout(function() {
                        $('.content-preloader').addClass('preloader-active');
                    });
                }
            };
            $rootScope.content_preloader_hide = function() {
                var $body = $('body');
                if($body.find('.content-preloader').length) {
                    // hide preloader
                    $('.content-preloader').removeClass('preloader-active');
                    // remove preloader
                    $timeout(function() {
                        $('.content-preloader').remove();
                    }, 500);
                }
            };

        }
    ])
;