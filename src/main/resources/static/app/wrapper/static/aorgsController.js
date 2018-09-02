angular
    .module('altairApp')
    	.controller("aorgsCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutIndependent.";
	    	  if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/info/list/LutIndependent",
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
			                        		 name: { type: "string", validation: { required: true } },
			                        		 reg: { type: "string", validation: { required: true } },
			                        		 licecpiredate: { type: "string", validation: { required: true } },
			                        		 licnum: { type: "string", validation: { required: true } },	
			                        		 phone: { type: "string", validation: { required: true } },
			                        		 address: { type: "string", validation: { required: true } },
			                        		 web: { type: "string", validation: { required: true } },
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
			                toolbar: ["excel", "pdf"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "name", title: "Нэр"},
									{ field: "reg", title: "УБ дугаар"},
									{ field: "licnum", title: "ТЗ-н дугаар"},
									{ field: "licexpiredate", title: "ТЗ-н хүчинтэй хугацаа"},	
									{ field: "phone", title: "Утас"},
									{ field: "address", title: "Хаяг"},
									{ field: "web", title: "Веб хуудас"}
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
	    	  else {
	    		  function init(){
	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
	   		   			.then(function(data){
	   		   				console.log(data);
	   		   				if(data.rread==1){

	   						$scope.puserGrid = {
	 		                dataSource: {
	 		                   
	 		                    transport: {
	 		                    	read:  {
	 		                    		url: "/info/list/LutIndependent",
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
	 		                        		 name: { type: "string", validation: { required: true } },
	 		                        		 reg: { type: "string", validation: { required: true } },
	 		                        		 licecpiredate: { type: "string", validation: { required: true } },
	 		                        		 licnum: { type: "string", validation: { required: true } },	
	 		                        		 phone: { type: "string", validation: { required: true } },
	 		                        		 address: { type: "string", validation: { required: true } },
	 		                        		 web: { type: "string", validation: { required: true } },
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
	 		               // columnMenu:true, 
	 		                resizable: true,
	 		               // toolbar: ["excel", "pdf"],
	 		                pageable: {
	 		                    refresh: true,
	 		                    pageSizes: true,
	 		                    buttonCount: 5
	 		                },
	 		                columns: [
	 		                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
	 								{ field: "name", title: "Нэр"},
	 								{ field: "reg", title: "УБ дугаар"},
	 								{ field: "licnum", title: "ТЗ-н дугаар"},
	 								{ field: "licexpiredate", title: "ТЗ-н хүчинтэй хугацаа"},	
	 								{ field: "phone", title: "Утас"},
	 								{ field: "address", title: "Хаяг"},
	 								{ field: "web", title: "Веб хуудас"}
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
	 	   					
	 					}
	 					else if(data.rexport==1){
	 						$scope.puserGrid.toolbar=["excel","pdf"];
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
