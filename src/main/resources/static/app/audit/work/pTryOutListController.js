angular
    .module('altairApp')
    	.controller("tryOutlistCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
    	                           'audit_dir',
    	                           'work_type',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data,audit_dir,work_type) { 
    	                        	   
    	         	      
    	      
    	      $scope.addworklist = function(){
    	    	  $state.go('restricted.pages.tryout',{param: 0});	    
    	      }
    	      $scope.edit = function(id){
    	    	  $state.go('restricted.pages.tryout',{param: id});  
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
			        		 mainService.withdomain('delete','/my/deleteTryOut/'+id)
	 				   			.then(function(){
	 				   			$("#notificationSuccess").trigger('click');
                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
	 				   				sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
					   			});	
	 		            }else{
	 		                sweet.show('Анхаар!', 'Устгах үйлдэл хийгдсэнгүй!!!', 'error');
	 		            }    		
			        });
    	      }
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutTryout.";
	    	  if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/my/list/LutTryout",
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
			                        		 adirid: { type: "number",validation: { required: true } },
			                        		 workid: { type: "number",validation: { required: true } },     
			                        		 formdesc: { type: "string", validation: { required: true } },
			                        		 text: { type: "string",validation: { required: true } },
			                        		 rtext: { type: "string",validation: { required: true } },
			                             }
			                         }
			                     },
			                    pageSize: 5,
			                    serverPaging: true,
			                    serverFiltering: true,
			                    serverSorting: true
			                },
			                filterable: {
	                            mode: "row"
	                        },
	                        excel: {
     	   		                fileName: "Organization Export.xlsx",
     	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
     	   		                filterable: true,
     	   		                allPages: true
     	   		            },
			                sortable: true,
			                columnMenu:true, 
			                resizable: true,
			                //toolbar: ["create"],
			                toolbar: kendo.template($("#addworklist").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									
									{ field: "formdesc", title: "Сорил" +"<span data-translate=''></span>", width:250},
									{ field: "text", title: "Зорилго" +"<span data-translate=''></span>", width:250},
									{ field: "adirid", title: "Аудитын чиглэл" +"<span data-translate=''></span>", width:250, values:audit_dir},
									{ field: "rtext", title: "Харъяалагдах эрсдлүүд" +"<span data-translate=''></span>", width:250},
																			
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
		                    		url: "/my/list/LutAuditWork",
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
		                        		 adirid: { type: "number",validation: { required: true } },
		                        		 workid: { type: "number",validation: { required: true } },     
		                        		 formdesc: { type: "string", validation: { required: true } },
		                        		 text: { type: "string",validation: { required: true } },   
		                        		 rtext: { type: "string",validation: { required: true } },
		                             }
		                         }
		                     },
		                    pageSize: 5,
		                    serverPaging: true,
		                    serverFiltering: true,
		                    serverSorting: true
		                },
		                filterable: {
                            mode: "row"
                        },
                        excel: {
 	   		                fileName: "Organization Export.xlsx",
 	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
 	   		                filterable: true,
 	   		                allPages: true
 	   		            },
		                sortable: true,
		                columnMenu:true, 
		                resizable: true,
		                //toolbar: ["create"],
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
									{ field: "formdesc", title: "Сорил" +"<span data-translate=''></span>", width:250},
									{ field: "text", title: "Зорилго" +"<span data-translate=''></span>", width:250},
									{ field: "adirid", title: "Аудитын чиглэл" +"<span data-translate=''></span>", width:250, values:audit_dir},
									{ field: "rtext", title: "Харъяалагдах эрсдлүүд" +"<span data-translate=''></span>", width:250},
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
					$scope.puserGrid.columns.push({ template: kendo.template($("#extend").html()), width: "236px"})
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
