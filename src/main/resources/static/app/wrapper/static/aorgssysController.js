angular
    .module('altairApp')
    	.controller("aorgssysCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
	        function ($scope,$rootScope,$state,$timeout) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutIndependent.";

	          $scope.pmenuGrid = {
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
		                        		 isactive: { type: "boolean", validation: { required: true } },
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
								{ field: "name", title: "Нэр"},
								{ field: "reg", title: "УБ дугаар"},
								{ field: "licnum", title: "ТЗ-н дугаар"},
								{ field: "licexpiredate", title: "ТЗ-н хүчинтэй хугацаа"},	
								{ field: "phone", title: "Утас"},
								{ field: "address", title: "Хаяг"},
								{ field: "web", title: "Веб хуудас"},	
								{ field: "isactive", title: "Идвэхтэй эсэх"},
								{ command: ["edit", "destroy"], title: "&nbsp;", width: "232px" }
	                      ],
	                      editable: "popup",
	                      autoBind: true,
		            }

        }]
    )
