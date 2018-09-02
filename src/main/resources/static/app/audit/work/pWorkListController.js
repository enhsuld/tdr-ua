angular
    .module('altairApp')
    	.controller("worklistCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
    	                           'audit_dir',
    	                           'au_work',
    	                           'au_levels',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data,audit_dir,au_work,au_levels) { 
    	                        	   
    	      var aj=au_work;
    	      var init={"text":"ROOT","value":"0"};	    	
    	      aj.push(init);
    	      var istrial=[{"text":"Үгүй","value":false},{"text":"Тийм","value":true}]
    	     
    	      var fileis=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}]
    	         	      
    	      $scope.addworklist = function(){
    	    	  $state.go('restricted.pages.workform',{param: 0});	    
    	      }
    	      $scope.edit = function(id){
    	    	  $state.go('restricted.pages.workform',{param: id});	    
    	      }
    	      $scope.deleteWork = function(id){
    	    	 
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
			        		 mainService.withdomain('delete','/my/deleteWork/'+id)
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
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutAuditWork.";
	    	  if(user_data[0].role=="ROLE_SUPER"){
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
			                        		 workformname: { type: "string", validation: { required: true } },
			                        		 text: { type: "string",validation: { required: true } },
			                        		 filename: { type: "string", validation: { required: true } },
			                        		 filepath: { type: "string",validation: { required: true } },
			                        		 formdescribe: { type: "string",validation: { required: true } },
			                        		 isdettrial: { type: "boolean",validation: { required: true } },
			                        		 isfile: { type: "number",validation: { required: true } },
			                        		 orderid: { type: "number",validation: { required: true } },
			                        		 parentid: { type: "number",validation: { required: true } },
			                        		 auditdirid: { type: "number",validation: { required: true } },     
			                        		 levelid: { type: "number",validation: { required: true } },  
			                        		 isscore: { type: "boolean",validation: { required: true } },
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
			                resizable: true,
			                toolbar: kendo.template($("#addworklist").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [									
									{ field: "workformname", title: "Ажлын нэр"+ "<span data-translate=''></span>" ,width: 300 },
									{ field: "parentid", title: "Эцэг" +"<span data-translate=''></span>",width: 300,values:aj},
									{ field: "levelid", title: "Аудитын үе шат" +"<span data-translate=''></span>",width: 150,values:au_levels},
									{ field: "rtext", title: "Аудитын төрөл" +"<span data-translate=''></span>",width: 250},
									{ field: "text", title: "Зорилго" +"<span data-translate=''></span>", width:300},
									{ field: "orderid", title: "Дараалал" +"<span data-translate=''></span>",width: 100},
									{ field: "isscore", title: "Гүйцэтгэлд хамаарах эсэх" +"<span data-translate=''></span>",width: 200,values:istrial},
									{ field: "isfile", title: "Маягттай эсэх" +"<span data-translate=''></span>",width: 100,values:fileis},
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
		                        		 workformname: { type: "string", validation: { required: true } },
		                        		 text: { type: "string",validation: { required: true } },
		                        		 filename: { type: "string", validation: { required: true } },
		                        		 filepath: { type: "string",validation: { required: true } },
		                        		 formdescribe: { type: "string",validation: { required: true } },
		                        		 isdettrial: { type: "boolean",validation: { required: true } },
		                        		 isfile: { type: "number",validation: { required: true } },
		                        		 orderid: { type: "number",validation: { required: true } },
		                        		 parentid: { type: "number",validation: { required: true } },
		                        		 auditdirid: { type: "number",validation: { required: true } },     
		                        		 levelid: { type: "number",validation: { required: true } },  
		                        		 isscore: { type: "boolean",validation: { required: true } },
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
		                resizable: true,
		                //toolbar: ["create"],
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [	
							{ field: "workformname", title: "Ажлын нэр"+ "<span data-translate=''></span>" ,width: 300 },
							{ field: "parentid", title: "Эцэг" +"<span data-translate=''></span>",width: 300,values:aj},
							{ field: "levelid", title: "Аудитын үе шат" +"<span data-translate=''></span>",width: 150,values:au_levels},
							{ field: "rtext", title: "Аудитын төрөл" +"<span data-translate=''></span>",width: 250},
							{ field: "text", title: "Зорилго" +"<span data-translate=''></span>", width:300},
							{ field: "orderid", title: "Дараалал" +"<span data-translate=''></span>",width: 100},
							{ field: "isscore", title: "Гүйцэтгэлд хамаарах эсэх" +"<span data-translate=''></span>",width: 200,values:istrial},
							{ field: "isfile", title: "Маягттай эсэх" +"<span data-translate=''></span>",width: 100,values:fileis},

		                      ],
		            }
	          if(data.rcreate==1){	   		   					  
	   					if(data.rexport==1){
	   						$scope.puserGrid.toolbar= kendo.template($("#addworklist").html());
	   					}
	   					else{
	   					  $scope.puserGrid.toolbar= kendo.template($("#add").html());
	   					}
					}
					else if(data.rexport==1){
						$scope.puserGrid.toolbar= kendo.template($("#export").html());
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
