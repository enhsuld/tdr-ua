angular
    .module('altairApp')
    	.controller("lpworkersCtrl",['$scope',
	        function ($scope) {       	
    		 			
    		
    		$scope.domain="com.netgloo.models.CLutUser.";
	            $scope.puserGrid = {
	                dataSource: {
	                   
	                    transport: {
	                    	read:  {
	                            url: "/izr/user/angular/CLutUserLp",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/izr/user/service/editing/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
	                            	$(".user  .k-grid").data("kendoGrid").dataSource.read(); 
	                    		}
	                        },
	                        destroy: {
	                            url: "/izr/user/service/editing/delete/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST"
	                        },
	                        create: {
	                            url: "/izr/user/service/editing/create/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
	                            	$(".user .k-grid").data("kendoGrid").dataSource.read(); 
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
	                            	 id: { type: "number", editable: false,nullable: false},
		                             	lname: { type: "string", validation: { required: true} },
		                             	fname: { type: "string", validation: { required: true} },
		                             	regnum: { type: "string", validation: { required: true} },
		                             	lpname: { type: "string", validation: { required: true}},
		                             	positionname: { type: "string",  validation: { required: true } }
	                             		                            	
	                             }	                    
	                         }
	                     },
	                    pageSize: 8,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                toolbar: ["excel","pdf"],
	                excel: {
	                	allPages: true,
	                    fileName: "Users.xlsx",
	                    filterable: true
	                },
	                pdf: {
	                    allPages: true,
	                    fileName: "Users.pdf"
	                },
	                filterable: {
                        mode: "row"
                    },
	                sortable: true,
	                resizable: true,
	                pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },
	                columns: [
							{ field:"lname", title: "Овог", width:150 },
							{ field:"fname", title: "Нэр", width:150 },
							{ field:"regnum", title: "Регистер", width:150},
							{ field:"lpname", title: "Байгууллага", width:100 },  
							{ field:"positionname", title: "Албан тушаал", width:150 } 
	                          ],
	                      //editable: "popup"
	            };
            
	        }
    ]);
