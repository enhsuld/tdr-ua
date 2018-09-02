/*
 *  Altair Admin angularjs
 *  controller
 */

angular
    .module('altairApp')
    .controller('mainCtrl', [
        function () {}
    ])
    .controller('main_headerCtrl2', [
        '$timeout',
        '$scope',
        '$window',
        'mainService',
        '$http',
        'sweet',
        function ($timeout,$scope,$window,mainService,$http,sweet) {
        	$scope.p = {};
        	
        	$scope.submitChangePasswordReq = function(){
          	  mainService.withdata("post","/changeUserPassword",$scope.p).then(function(data){
          		  if (data == true){
          			UIkit.modal("#changePasswordDialog").hide();
          			sweet.show('Мэдээлэл', 'Нууц үг амжилттай солигдлоо.', 'success');
          		  }
          	  });
        	}
        	
        	$scope.getCurrentUser = function(){
        		$http({ method: 'GET', url: '/core/ujson' })
                .then(function (data) {
                	$scope.user = data.data.ujson[0];
                })
                
        	}

        }
    ])
;
