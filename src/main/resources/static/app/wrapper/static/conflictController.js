angular
    .module('altairApp')
    	.controller("conflictCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'p_orgs',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,p_orgs) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutConflict.";
	    	  if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/info/list/LutConflict",
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
			                        		 conflict: { type: "string", validation: { required: true } },
			                        		 account: { type: "string", validation: { required: true } },
			                        		 total: { type: "string", validation: { required: true } },
			                        		 orgid: { type: "number", validation: { required: true } },
			                        		 resolve: { type: "string", validation: { required: true } },
			                        		 auditorid: { type: "number", validation: { required: true } },
			                        		 confdate: { type: "string"}		                        		 
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
			                resizable: true,
			                excel: {
     	   		                fileName: "Organization Export.xlsx",
     	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
     	   		                filterable: true,
     	   		                allPages: true
     	   		            },
			                toolbar: ["create"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "conflict", title: "Утга", width: "300px"},
									{ field: "account", title: "Данс"},
									{ field: "total", title: "Мөнгөн дүн"},
									{ field: "orgid", title: "Байгууллага", values:p_orgs},
									{ field: "resolve", title: "Шийдвэрлэлт"},
									{ field: "auditorid", title: "Төрөл"},
									{ field: "confdate", title: "Огноо"},
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
			                    		url: "/info/list/LutConflict",
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
			                        		 conflict: { type: "string", validation: { required: true } },
			                        		 account: { type: "string", validation: { required: true } },
			                        		 total: { type: "string", validation: { required: true } },
			                        		 orgid: { type: "number", validation: { required: true } },
			                        		 resolve: { type: "string", validation: { required: true } },
			                        		 auditorid: { type: "number", validation: { required: true } },
			                        		 confdate: { type: "string"}		                        		 
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
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "conflict", title: "Утга" ,width: "450px"},
									{ field: "account", title: "Данс",width: "250px"},
									{ field: "total", title: "Мөнгөн дүн",width: "250px"},
									{ field: "orgid", title: "Байгууллага", values:p_orgs,width: "350px"},
									{ field: "resolve", title: "Шийдвэрлэлт",width: "250px"},
									{ field: "auditorid", title: "Төрөл",width: "250px"},
									{ field: "confdate", title: "Огноо",width: "150px"},
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

	init();	}
	  		          

}
]);
