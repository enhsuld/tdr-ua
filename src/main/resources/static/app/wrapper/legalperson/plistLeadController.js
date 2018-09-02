angular
    .module('altairApp')
    	.controller("plistLeadCtrl",['$scope','p_profflevel',
	        function ($scope,p_profflevel) {       	
    		 			
    		
    		$scope.domain="com.netgloo.models.CLutHaveprofflevellist.";
	            $scope.puserGrid = {
	                dataSource: {
	                   
	                    transport: {
	                    	read:  {
	                            url: "/izr/user/angular/CLutHaveprofflevellistLead",
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
		                             	teacherlname: { type: "string", validation: { required: true} },
		                             	teacherfname: { type: "string", validation: { required: true} },
		                             	teacherregnum: { type: "string", validation: { required: true} },
		                             	profflevelnum: { type: "string", validation: { required: true} },
		                             	grantdate: { type: "string", validation: { required: true}},
		                             	expiredate: { type: "string", validation: { required: true}},	                             		
		                             	decnum: { type: "string", validation: { required: true}},
		                             	edulevel: { type: "string", validation: { required: true}},	    
		                             	teacherschool: { type: "string", validation: { required: true} },
		                             	teacherproff: { type: "string", validation: { required: true} }
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
	                          { field:"teacherschool", title: "Сургууль", width:250 },  
	                          { field:"teacherproff", title: "Албан тушаал", width:350 }, 	
	                          { field:"decnum", title: "Тушаалын дугаар", width:150 },	          
	                          { field:"teacherlname", title: "Овог", width:150 },
	                          { field:"teacherfname", title: "Нэр", width:150 },
	                          { field:"teacherregnum", title: "Регистер", width:150},                         
	                          { field:"profflevelnum", title: "МЗ-н дугаар", width:150 },  
	                          { field:"grantdate", title: "Олгосон огноо", width:150 },  
	                          { field:"expiredate", title: "Дуусах огноо", width:150},	                         
	                          { command: ["edit"], title: "&nbsp;", width: "140px" }],
	                      editable: "popup"
	            };
            
	        }
    ]);
