angular
    .module('altairApp')
    	.controller("confMethodCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data) { 
    	                        	   
    	      
    	       var $formValidate = $('#form_method');
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
    	      var modal_method = UIkit.modal("#modal_method");
    	      
    	      $scope.domain="com.netgloo.models.LutConfirmationMethod.";
    	      
    	      $scope.addworklist = function(){
    	    	  $scope.formdata = {};
    	    	  $scope.formdata.id=0;
    	    	  modal_method.show();
    	      }
    	      $scope.edit = function(id){
    	    	  mainService.withdomain('get', '/my/resource/LutConfirmationMethod/'+id).
	    			then(function(data){
	    				$scope.formdata=data[0];
	    				modal_method.show();
	    			});
    	      }
    	      
    	      $scope.submitForm = function(){
    	    	  mainService.withdata('POST', '/info/create/'+$scope.domain,  $scope.formdata).
	    			then(function(data){
	    				modal_method.hide();
		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		   				$("#notificationSuccess").trigger('click');
		   				$(".k-grid").data("kendoGrid").dataSource.read(); 
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
    	      
    	      if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/my/list/LutConfirmationMethod",
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
			                        		 name: { type: "string",validation: { required: true } },
			                             }
			                         }
			                     },
			                    pageSize: 5,
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
			                toolbar: kendo.template($("#addworklist").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									{ field: "name", title: "Нотлох зүйлийг олж авах арга зам" +"<span data-translate=''></span>"},
									{template: kendo.template($("#extend").html()), width: "200px"}
		                      ],
		                      editable: "popup",
		                      autoBind: true,
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
		                    		url: "/my/list/LutConfirmationMethod",
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
		                        		 name: { type: "string",validation: { required: true } },	 
		                             }
		                         }
		                     },
		                    pageSize: 5,
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
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
		                          	{ field: "name", title: "Нотлох зүйлийг олж авах арга зам" +"<span data-translate=''></span>"},
									//{ command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }
		                      ],
	                      editable: "popup",
	                      autoBind: true,
		            }
	          if(data.rcreate==1){	   		   					  
	   					if(data.rexport==1){
	   						
	   					  //$scope.puserGrid.toolbar=["create","excel","pdf"];
	   						$scope.puserGrid.toolbar= kendo.template($("#addworklist").html());
	   					}
	   					else{
	   					  //$scope.puserGrid.toolbar=["create"];
	   					  $scope.puserGrid.toolbar= kendo.template($("#add").html());
	   					}
					}
					else if(data.rexport==1){
						$scope.puserGrid.toolbar= kendo.template($("#export").html());
						//$scope.puserGrid.toolbar=["excel","pdf"];
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
