angular
    .module('altairApp')
    	.controller("ntypeCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutNewstype.";
	    	  if(user_data==""){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/info/list/LutNewstype",
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
			                        		 newstypename: { type: "string", validation: { required: true } },
			                        		
			                        		 	                        		 		                        		 
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
			                toolbar: ["create"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									{ field: "newstypename", title: "Төрлийн нэр"},
																							
									{ command: ["edit", "destroy"], title: "&nbsp;", width: "232px" }
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
		                    		url: "/info/list/LutNewstype",
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
		                        		 newstypename: { type: "string", validation: { required: true } },		                        		 
		                        		 	                        		 		                        		 
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
		                //toolbar: ["create"],
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
								{ field: "newstypename", title: "Төрлийн нэр"},																					
								//{ command: ["edit", "destroy"], title: "&nbsp;", width: "232px" }
	                      ],
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
					$scope.puserGrid.columns.push({ command: ["edit", "destroy"], title: "&nbsp;", width: "232px" })
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

init();	            

}
    	                           }
]);
