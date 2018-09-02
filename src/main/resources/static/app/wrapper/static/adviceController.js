angular
    .module('altairApp')
    	.controller("adviceCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutRisk.";
	    	  if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                            url: "/info/list/SubAdvice",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        update: {
			                            url: "/core/update/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        destroy: {
			                            url: "/core/delete/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST",
			                            complete: function(e) {
			                            	 $("#notificationDestroy").trigger('click');
			                    		}
			                        },
			                        create: {
			                            url: "/user/service/editing/create/"+$scope.domain+"",
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
			                        		 description: { type: "string", validation: { required: true } },
			                        		 givendate: { type: "string", validation: { required: true } },
			                        		 auditorid: { type: "number", validation: { required: true } },
			                        		 orgid: { type: "number", validation: { required: true } },
			                        		 planid: { type: "number", validation: { required: true } },
			                        		 isimplemented: { type: "number", validation: { required: true } }
			                             }
			                         }
			                     },
			                    pageSize: 8,
			                    serverPaging: true,
			                    serverFiltering: true,
			                    serverSorting: true
			                },
			                filterable: { mode: "row"},
			                sortable: true,
			                //columnMenu:true, 
			                excel: {
     	   		                fileName: "Organization Export.xlsx",
     	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
     	   		                filterable: true,
     	   		                allPages: true
     	   		            },
			                resizable: true,
			                toolbar: ["create"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "description", title: "Тайлбар", width: "450px"},
									{ field: "givendate", title: "Огноо", width: "130px"},
									{ field: "auditorid", title: "Аудитор", width: "180px"},
									{ field: "orgid", title: "Байгууллага", width: "200px"},
									{ field: "planid", title: "Төлөвлөгөө", width: "200px"},
									{ field: "isimplemented", title: "Хэрэгжүүлсэн эсэх", width: "150px"},
									{ command: ["edit", "destroy"], title: "&nbsp;", width: "242px" }
		                      ],
		                      dataBound: function () {
		   	   		                var rows = this.items();
		   	   		                  $(rows).each(function () {
		   	   		                      var index = $(this).index() + 1 
		   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
		   	   		                      var rowLabel = $(this).find(".row-number");
		   	   		                      $(rowLabel).html(index);
		   	   		                  });
		   	   		  	           },
		                      editable: "popup",
		                      autoBind: true,
			            }
	    	  }
	    	  else{function init(){
	  	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
	  		   			.then(function(data){
	  		   				console.log(data);
	  		   				if(data.rread==1){

		          $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                            url: "/info/list/SubAdvice",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        update: {
			                            url: "/core/update/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        destroy: {
			                            url: "/core/delete/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST",
			                            complete: function(e) {
			                            	 $("#notificationDestroy").trigger('click');
			                    		}
			                        },
			                        create: {
			                            url: "/user/service/editing/create/"+$scope.domain+"",
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
			                        		 description: { type: "string", validation: { required: true } },
			                        		 givendate: { type: "string", validation: { required: true } },
			                        		 auditorid: { type: "number", validation: { required: true } },
			                        		 orgid: { type: "number", validation: { required: true } },
			                        		 planid: { type: "number", validation: { required: true } },
			                        		 isimplemented: { type: "number", validation: { required: true } }
			                             }
			                         }
			                     },
			                    pageSize: 8,
			                    serverPaging: true,
			                    serverFiltering: true,
			                    serverSorting: true
			                },
			                filterable: { mode: "row"},
			                sortable: true,
			                excel: {
     	   		                fileName: "Organization Export.xlsx",
     	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
     	   		                filterable: true,
     	   		                allPages: true
     	   		            },
			                //columnMenu:true, 
			                resizable: true,
			                //toolbar: ["create"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "description", title: "Тайлбар", width: "450px"},
									{ field: "givendate", title: "Огноо", width: "130px"},
									{ field: "auditorid", title: "Аудитор", width: "180px"},
									{ field: "orgid", title: "Байгууллага", width: "200px"},
									{ field: "planid", title: "Төлөвлөгөө", width: "200px"},
									{ field: "isimplemented", title: "Хэрэгжүүлсэн эсэх", width: "150px"},
									//{ command: ["edit", "destroy"], title: "&nbsp;", width: "232px" }
		                      ],
		                      dataBound: function () {
		   	   		                var rows = this.items();
		   	   		                  $(rows).each(function () {
		   	   		                      var index = $(this).index() + 1 
		   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
		   	   		                      var rowLabel = $(this).find(".row-number");
		   	   		                      $(rowLabel).html(index);
		   	   		                  });
		   	   		  	           },
		                      editable: "popup",
		                      autoBind: true,
			            }
		          if(data.rcreate==1){	   		   					  
		   					if(data.rexport==1){
		   					  $scope.puserGrid.toolbar=["create","excel","pdf"];
		   					}
		   					else{
		   					  $scope.puserGrid.toolbar=["create"];
		   					}
						}
						else if(data.rexport==1){
							$scope.puserGrid.toolbar=["excel","pdf"];
						}
		          if(data.rupdate==1 && data.rdelete==1){
						$scope.puserGrid.columns.push({ command: ["edit", "destroy"], title: "&nbsp;", width: "242px" })
					}
					else if(data.rupdate==1 && data.rdelete==0){
						$scope.puserGrid.columns.push({ command: ["edit"], title: "&nbsp;", width: "100px" })
					}
					else if(data.rupdate==0 && data.rdelete==1){
						$scope.puserGrid.columns.push({command:["destroy"],  width: "100px"})
					}
					}
					else{
						$state.go('restricted.pages.error404');
					}			
			 });
		}

	init();	   }
	  		         

}
]);
