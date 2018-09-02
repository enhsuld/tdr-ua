angular
    .module('altairApp')
    	.controller("excelformCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           'utils',
    	                           '$timeout',
    	                           'mainService',
    	                           'fileUpload',
    	                           'sweet',
    	                           'fType',
	        function ($scope,$rootScope,$state,utils,$timeout,mainService,fileUpload,sweet,fType) {
    	   
    	    var formdataDir = new FormData();
    	    
    	    var $formValidate = $('#user_add_form');
    		
    		$formValidate
            .parsley()
            .on('form:validated',function() {
               $scope.$apply();
            })
            .on('field:validated',function(parsleyField) {
                if($(parsleyField.$element).hasClass('md-input')) {
                   $scope.$apply();
                }
            });
    	    
    		 $scope.formname = fType;
    		 
    		
    		init();
    		
    		function init(){
    			
	    	}
   	        
	        $scope.getTheUFiles = function ($files) {
	                angular.forEach($files, function (value, key) {
	                	formdataDir.append("files", value);
	                });
	            };
	            
    	    $scope.form = {};
			
    		$scope.uploadExcel = function(e){
    			
    			e.preventDefault();
    			sweet.show({
		        	   title: 'Баталгаажуулалт',
		            text: 'Та энэ үйлдлийг хийхдээ итгэлтэй байна уу?',
		            type: 'warning',
		            showCancelButton: true,
		            confirmButtonColor: '#DD6B55',
		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
		            closeOnConfirm: true,
		            closeOnCancel: true
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		                 $scope.content_preloader_show();
		                 $scope.form.formid = $state.params.param;
		                 var data = JSON.stringify($scope.form, null, 2);               
		                 formdataDir.append("obj", data);
		                 
		                 fileUpload.uploadFileToUrl('/my/uploadfromexcel', formdataDir)
		              	.then(function(data){
			   				if(data){	
			   					console.log(data);
			   					formdata = new FormData();
			   					$scope.content_preloader_hide();
			   					angular.element('.dropify-clear').triggerHandler('click');
			   					$scope.zarga={};
			   					if(data.response==true){
			   						sweet.show('Мэдээлэл', 'Амжилттай хадгалагдсан нийт : '+ data.count, 'success');
			   					}
			   				}
					   				
			   			});	
		        	
		        		
		            }else{
		                sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
		            }    		
		        });
    		}
			
			$scope.backtoview = function(){
				$state.go('restricted.pages.worklist');
			}
			
   		 $('.dropify').dropify();

         $('.dropify-fr').dropify({
             messages: {
                 default: 'Файлаа сонгоно уу',
                 replace: 'Дарж хуулах',
                 remove:  'Устгах',
                 error:   'Алдаа гарлаа'
             }
         });
   			
        }]
    )
    
    


