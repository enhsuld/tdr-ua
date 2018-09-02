angular
    .module('altairApp')
    	.controller("critListCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
    	                           'crit',
    	                           'audit_dir',
    	                           'groupOfFact',
    	                           'work_type',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data,crit,audit_dir,groupOfFact,work_type) { 
    	                        	   
    	      $scope.formdata = {};
    	      $scope.formdata.deleteCrit = [];
    	      $scope.formdata.crits = [];
    	      
    	      init();
      		function init(){
      			$scope.domain = "LutAuditWork";
      			
      			if($state.params.param!=0){
      				mainService.withdomain('get', '/my/resource/LutFactor/'+$state.params.param).
	    			then(function(data){
	    				$scope.formdata=data[0];  
	    				$scope.formdata.crits = crit;
	    				$scope.formdata.deleteCrit = [];
	    				console.log(data[0]);
	    			});	
      			}
      			else{
      				$scope.formdata = {};
      				$scope.formdata.id = 0;
      				$scope.formdata.deleteCrit = [];
      				$scope.formdata.crits = [];
      			}
  	    	}
    	      
    	      var modalUpdate = UIkit.modal("#modal_update");
    	      $scope.isupdate = 0;
    	      $scope.addCrit = function(){
    	    	  $scope.isupdate = 0;
    	    	  $scope.criterion = {};
    	    	  $scope.criterion.id = 0;
    	    	  modalUpdate.show();
    	      }
    	      $scope.editCrit = function(crit){
    	    	  $scope.criterion = crit;
    	    	  $scope.isupdate=1;
    	    	  modalUpdate.show();
    	      }
   			var notices_data = $scope.selectize_notices_options = audit_dir;
   			$scope.risk_option=[];
   			$scope.changeDirection = function(i){
   				$scope.seldir=i;
   				mainService.withdomain('get', '/au/withid/LnkRiskdir/'+i).
	 			then(function(data){
	 				$scope.risk_option=data;
	 			});	  
   			}
   			
   			var treatment_data= $scope.selectize_treatment_options = [];
   			$scope.changeRisk = function(id){
   				mainService.withdomain('get','/au/withids/LnkTryoutRisk/'+$scope.seldir+'/'+id)
	   			.then(function(data){ 		   				
	   				$scope.selectize_treatment_options = treatment_data = data; 		   				
	   			});	 
   			}
   			
  			$scope.selectize_notices_config = {
  	                plugins: {
  	                    'remove_button': {
  	                        label     : ''
  	                    }
  	                },
  	                maxItems: 1,
  	                minItems:1,
  	                valueField: 'value',
  	                labelField: 'text',
  	                searchField: 'text',
  	                create: false,
  	                render: {
  	                    option: function(notices_data, escape) {
  	                        return  '<div class="option">' +
  	                            '<span class="title">' + escape(notices_data.text) + '</span>' +
  	                            '</div>';
  	                    }
  	                }
  	            };
  			
  			 var work_type_data = $scope.selectize_work_type_options = work_type;
    			
    			$scope.selectize_work_type_config = {
    	                plugins: {
    	                    'remove_button': {
    	                        label     : ''
    	                    }
    	                },
    	                maxItems: null,
    	                minItems:1,
    	                valueField: 'value',
    	                labelField: 'text',
    	                searchField: 'text',
    	                create: false,
    	                render: {
    	                    option: function(work_type_data, escape) {
    	                        return  '<div class="option">' +
    	                            '<span class="title">' + escape(work_type_data.text) + '</span>' +
    	                            '</div>';
    	                    }
    	                }
    	            };
  			
  			var groupOfFact_data = $scope.selectize_groupOfFact_options = groupOfFact;
  			
  			$scope.selectize_groupOfFact_config = {
  	                plugins: {
  	                    'remove_button': {
  	                        label     : ''
  	                    }
  	                },
  	                maxItems: 1,
  	                minItems:1,
  	                valueField: 'value',
  	                labelField: 'text',
  	                searchField: 'text',
  	                create: false,
  	                render: {
  	                    option: function(groupOfFact_data, escape) {
  	                        return  '<div class="option">' +
  	                            '<span class="title">' + escape(groupOfFact_data.text) + '</span>' +
  	                            '</div>';
  	                    }
  	                }
  	            };
    	      
  			var $formModelValidate = $('#form_crit_dir');
     		
  			$formModelValidate
	               .parsley()
	               .on('form:validated',function() {
	                  $scope.$apply();
	               })
	               .on('field:validated',function(parsleyField) {
	                   if($(parsleyField.$element).hasClass('md-input')) {
	                      $scope.$apply();
	                   }
	            });
  			
    	    var $formValidate = $('#form_au_dir');
         		
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
    	      $scope.backtoview = function(){
    	    	  $state.go('restricted.pages.factor');	
    	      }
    	      $scope.addCritForm = function(data){
    	    	  if($scope.isupdate==1){
    	    	  }
    	    	  else{
    	    		  $scope.formdata.crits.push(data);
        	    	  data = {};
    	    	  }
    	    	  $scope.isupdate = 0;
    	    	  $scope.criterion = {};  
    	    	  modalUpdate.hide();
    	      }
    	      
    	      $scope.edit = function(id){
    	    	  mainService.withdomain('get', '/my/resource/LutFactorCriterion/'+id).
	    			then(function(data){
	    				$scope.formdata = {};
	    				$scope.formdata=data[0];   	    
	    				modalUpdate.show();
	    			});	  
    	      }
    	      
    	      $scope.removeCrit = function(crit,$index){
     	    	 sweet.show({
 			        	   title: 'Баталгаажуулалт',
 	   		            text: 'Та энэ файлыг устгахдаа итгэлтэй байна уу?',
 	   		            type: 'warning',
 	   		            showCancelButton: true,
 	   		            confirmButtonColor: '#DD6B55',
 	   		            confirmButtonText: 'Тийм',
 			    	    cancelButtonText: 'Үгүй',
 	   		            closeOnConfirm: true,
 	   		            closeOnCancel: false
 			          
 			        }, function(inputvalue) {
 			        	 if (inputvalue) {
 			        		if(crit.saved==1){
 			        			$scope.formdata.deleteCrit.push(crit);
 			        			$scope.formdata.crits.splice($index, 1);
 			        		}
 			        		else{
 			        			$scope.formdata.crits.splice($index, 1);
 			        		}
 	 		            }else{
 	 		                sweet.show('Анхаар!', 'Устгах үйлдэл хийгдсэнгүй!!!', 'error');
 	 		            }    		
 			        });
     	      }
    	      $scope.submitForm = function(){
    	    	  
    	    	  sweet.show({
		        	   title: 'Баталгаажуулалт',
   		            text: 'Хадгалахдаа итгэлтэй байна уу?',
   		            type: 'warning',
   		            showCancelButton: true,
   		            confirmButtonColor: '#DD6B55',
   		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
   		            closeOnConfirm: false,
   		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 console.log($scope.formdata);
		        		 mainService.withdata('put','/my/addFactor', $scope.formdata)
			    			.then(function(data){
			    				sweet.show('Амжилттай!', 'Амжилттай хадгалагдлаа', 'success');
			    				$state.go('restricted.pages.factor');	 
			    			});
		        	
 		            }else{
 		                sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
 		            }    		
		        });
    	      }
    	      
    	      $scope.deleteTry = function(id){
    	    	 sweet.show({
			        	   title: 'Баталгаажуулалт',
	   		            text: 'Та энэ файлыг устгахдаа итгэлтэй байна уу?',
	   		            type: 'warning',
	   		            showCancelButton: true,
	   		            confirmButtonColor: '#DD6B55',
	   		            confirmButtonText: 'Тийм',
			    	    cancelButtonText: 'Үгүй',
	   		            closeOnConfirm: false,
	   		            closeOnCancel: false
			          
			        }, function(inputvalue) {
			        	 if (inputvalue) {
			        		 $scope.formdata = {};
			        		 $scope.formdata.id = id;
			        		 mainService.withdata('POST', '/info/delete/'+$scope.domain,  $scope.formdata).
				    			then(function(data){
				    				$("#notificationSuccess").trigger('click');
	                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
		 				   			sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
				    			});
			        	
	 		            }else{
	 		                sweet.show('Анхаар!', 'Устгах үйлдэл хийгдсэнгүй!!!', 'error');
	 		            }    		
			        });
    	      }
	}
]);
