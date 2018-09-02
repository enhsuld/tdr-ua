angular
    .module('altairApp')
    	.controller("acatCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'sweet',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,sweet) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutCategory.";
	    	  var $formValidate = $('#form_val');
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
  			
	    		var modalUpdate = UIkit.modal("#modal_update");
	    	      $scope.isupdate = 0;
	    	      $scope.org = function(){
	    	    	  $scope.formdata={};
	    	    	  $scope.formdata.id=0;
	    	    	  modalUpdate.show();
	    	      }
	    	      
	    	      $scope.edit = function(id){
	    	    	  mainService.withdomain('get', '/my/resource/LutCategory/'+id).
		    			then(function(data){
		    				$scope.formdata=data[0];
		    				modalUpdate.show();
		    			});  
	    	      }
	    	      
	    	      $scope.submitForm = function($event){
	    	    	  
	    	    	  if($scope.formdata.id==0){
	    	    		  mainService.withdata('POST', '/info/create/'+$scope.domain,  $scope.formdata).
	  	    				then(function(data){
		  	    				modalUpdate.hide();
		  	    				$event.preventDefault();
		  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		  		   				$("#notificationSuccess").trigger('click');
		              			$(".k-grid").data("kendoGrid").dataSource.read(); 
	  	    				});
	    	    	  }else{
	    	    		  mainService.withdata('POST', '/info/update/'+$scope.domain,  $scope.formdata).
		    				then(function(data){
		  	    				modalUpdate.hide();
		  	    				$event.preventDefault();
		  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
			  		   			$("#notificationSuccess").trigger('click');
	                			$(".k-grid").data("kendoGrid").dataSource.read(); 
		    				});
	    	    	  }
	    	    	  
	    	      }
	    	      $scope.deleteO = function(id){
	     	    	 
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
	    	  
	    	      if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/info/list/LutCategory",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        update: {
			                            url: "/info/update/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        destroy: {
			                            url: "/info/delete/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST",
			                            complete: function(e) {
			                            	 $("#notificationDestroy").trigger('click');
			                    		}
			                        },
			                        create: {
			                            url: "/info/create/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST",
			                            complete: function(e) {
			                            	$("#notificationSuccess").trigger('click');
			                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
			                    		}
			                        },
			                        parameterMap: function(options) {
			                       	 return JSON.stringify(options);
			                       }
			                    },
			                    schema: {
			                     	data:"data",
			                     	total:"total",
			                         model: {                                	
			                             id: "id",		                         	
			                        	 fields: {   
			                        		 id: { editable: false,nullable: true},
			                        		 categoryname: { type: "string", validation: { required: true } },  		 
			                             }
			                         }
			                     },
			                    pageSize: 8,
			                    serverPaging: true,
			                    serverFiltering: true,
			                    serverSorting: true
			                },
			                filterable:{
   			                	 mode: "row"
   			                },
			                sortable: true,
			                columnMenu:true, 
			                resizable: true,
			                excel: {
     	   		                fileName: "Organization Export.xlsx",
     	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
     	   		                filterable: true,
     	   		                allPages: true
     	   		            },
			                //toolbar: ["create"],
			                toolbar: kendo.template($("#addorg").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									{ field: "categoryname", title: "Ангилал"},							
									{template: kendo.template($("#extend").html()), width: "200px"}
		                      ],
			            }
	    	  }
	    	  else{
	  		function init(){
  	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
  		   			.then(function(data){
  		   				console.log(data);
  		   				if(data.rread==1){

	          $scope.puserGrid = {
		                dataSource: {
		                   
		                    transport: {
		                    	read:  {
		                    		url: "/info/list/LutCategory",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST"
		                        },
		                        update: {
		                            url: "/info/update/"+$scope.domain+"",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST"
		                        },
		                        destroy: {
		                            url: "/info/delete/"+$scope.domain+"",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST",
		                            complete: function(e) {
		                            	 $("#notificationDestroy").trigger('click');
		                    		}
		                        },
		                        create: {
		                            url: "/info/create/"+$scope.domain+"",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST",
		                            complete: function(e) {
		                            	$("#notificationSuccess").trigger('click');
		                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
		                    		}
		                        },
		                        parameterMap: function(options) {
		                       	 return JSON.stringify(options);
		                       }
		                    },
		                    schema: {
		                     	data:"data",
		                     	total:"total",
		                         model: {                                	
		                             id: "id",		                         	
		                        	 fields: {   
		                        		 id: { editable: false,nullable: true},
		                        		 categoryname: { type: "string", validation: { required: true } },
		                        		 	                        		 		                        		 
		                             }
		                         }
		                     },
		                    pageSize: 8,
		                    serverPaging: true,
		                    serverFiltering: true,
		                    serverSorting: true
		                },
		                filterable:{
			                	 mode: "row"
			                },
		                sortable: true,
		                columnMenu:true, 
		                excel: {
 	   		                fileName: "Organization Export.xlsx",
 	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
 	   		                filterable: true,
 	   		                allPages: true
 	   		            },
		                resizable: true,
		                //toolbar: ["create"],
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
		                          { field: "categoryname", title: "Ангилал"},								
	                      ],
		            }
	          		if(data.rcreate==1){	   		   					  
	   					if(data.rexport==1){
	   						$scope.puserGrid.toolbar= kendo.template($("#addorg").html());
	   					}
	   					else{
	   						$scope.puserGrid.toolbar= kendo.template($("#add").html());
	   					}
					}
					else if(data.rexport==1){
						$scope.puserGrid.toolbar= kendo.template($("#export").html());
					}
		          	if(data.rupdate==1 && data.rdelete==1){
		          		$scope.puserGrid.columns.push({ template: kendo.template($("#extend").html()), width: "250px"})
					}
					else if(data.rupdate==1 && data.rdelete==0){
						$scope.puserGrid.columns.push({ template: kendo.template($("#extendu").html()), width: "100px"}   )
					}
					else if(data.rupdate==0 && data.rdelete==1){
						$scope.puserGrid.columns.push({ template: kendo.template($("#extendd").html()), width: "100px"}   )
					}
				}
				else{
					$state.go('restricted.pages.error404');
				}			
		 });
	}

init();	            

}
    	                           }
]);
