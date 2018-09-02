angular
    .module('altairApp')
    	.controller("orgformIndpCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           'utils',
    	                           '$timeout',
    	                           'mainService',
    	                           'p_dep',
    	                           'p_tez',
    	                           'p_fin',
    	                           'p_cat',
    	                           'p_prog',
    	                           'sweet',
    	                           'p_edit',
	        function ($scope,$rootScope,$state,utils,$timeout,mainService,p_dep,p_tez,p_fin,p_cat,p_prog,sweet,p_edit) {
    	                        	   
        	  
               //if (i!=0){}
               $scope.orgdata=p_edit;
               console.log($scope.orgdata);
               $scope.enterValidation = function(){
            	    return true;
            	};

            	$scope.exitValidation = function(){
            	    return true;
            	};
            	//example using context object
            	$scope.exitValidation = function(context){
            		if(		$scope.orgdata.orgcode!=null && 
            				$scope.orgdata.departmentid!=null && 
            				$scope.orgdata.fincategoryid!=null && 
            				$scope.orgdata.catid!=null && 
            				$scope.orgdata.progid!=null && 
            				$scope.orgdata.orgname!=null 
            				){return true;}
            		else{
            			sweet.show('Мэдээлэл', 'Мэдээлэл дутуу байна.', 'error');
            		};
            		
            	}
            	
                              
               $scope.selectize_a_data = {
                       options: [p_tez]
                   };
             
               $scope.selectize_c_data = {
                       options: [p_dep]
                   };
               $scope.selectize_d_data = {
                       options: [p_fin]
                   };
               $scope.selectize_e_data = {
                       options: [p_cat]
                   };
               $scope.selectize_f_data = {
                       options: [p_prog]
                   };
               $scope.selectize_a_config = {
                       plugins: {
                           'disable_options': {
                               disableOptions: ["c1","c2"]
                           }
                       },
                       create: false,
                       maxItems: 1,
                       placeholder: 'Сонго...',
                       optgroupField: 'parent_id',
                       optgroupLabelField: 'title',
                       optgroupValueField: 'ogid',
                       valueField: 'value',
                       labelField: 'text',
                       searchField: 'title'
                   };
               $scope.selectize_b_data = {
                       options: []
                   };
               
               $scope.choose=function(){ 
            	   console.log($scope.orgdata.tez);
            	   mainService.withdata('GET','/core/sel/ttz/'+$scope.orgdata.tez)
            	   .then(function(data){	
		   				
		   				var a=data;
		   				var b="";
		   				angular.forEach(a, function(value, key){
		   			     if(value.code!=null){
		   						b=value.code;
		   					}
		   				});
		   				$scope.orgdata.orgcode=b;
		   			 $scope.selectize_b_data = {
		                       options: [data]
		                   };
		             
		               
		               $scope.selectize_b_config = {
		                       plugins: {
		                           'disable_options': {
		                               disableOptions: ["c1","c2"]
		                           }
		                       },
		                       create: false,
		                       maxItems: 1,
		                       placeholder: 'Сонго...',
		                       optgroupField: 'parent_id',
		                       optgroupLabelField: 'title',
		                       optgroupValueField: 'ogid',
		                       valueField: 'value',
		                       labelField: 'text',
		                       searchField: 'title'
		                   };
		   				});
            	   
            	  
               } 
               $scope.choose1=function(){               	   
            	   mainService.withdata('GET','/core/sel/code/'+$scope.orgdata.ttz)
            	   .then(function(data){
            		   var b="";
		   				angular.forEach(data, function(value, key){
		   			     if(value.code!=null){
		   						b=value.code;
		   					}
		   			  $scope.orgdata.orgcode=b;
		   				});
		   				});
            	  
               }
               $scope.addOrg = function() {	    
   		    		console.log($scope.orgdata.id);
	    		   mainService.withdata('PUT','/core/orgadd/'+$scope.orgdata.id, $scope.orgdata)
		   			.then(function(data){			   				
		   				if(data.re==1){
		   					sweet.show('Мэдээлэл', 'Амжилттай засагдлаа.', 'success');
		   					$state.go('restricted.pages.orglistIndp');
		   				}
		   				if(data.re==2){
		   					sweet.show('Мэдээлэл', 'Байгууллагын код давхцаж байна.', 'error');		   					
		   				}
		   				if(data.re==0){
		   					sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		   					$state.go('restricted.pages.orglistIndp');
		   				}		   				
		   			});	 
                       
            }
        }]
    )
    
    


