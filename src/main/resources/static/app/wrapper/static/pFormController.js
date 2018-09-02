angular
    .module('altairApp')
    	.controller("formCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
	        function ($scope,$rootScope,$state,$timeout) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.CEduLutForm.";

	    	  $scope.addForm=function(){
	    		  $state.go('restricted.pages.formTab',{param:0});
	    	  }
	    	  $scope.update=function(id){
          	 	
	    			$state.go('restricted.pages.formTab',{param:id});
	    	
	    			    	    	
	    		}
	          $scope.pmenuGrid = {
		                dataSource: {
		                   
		                    transport: {
		                    	read:  {
		                            url: "/user/angular/CEduLutForm",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST"
		                        },
		                        update: {
		                            url: "/user/service/editing/update/"+$scope.domain+"",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST",
		                            complete: function(e) {
		                            	 $("#notificationUpdate").trigger('click');
		                    		}
		                        },
		                        destroy: {
		                            url: "/user/service/editing/delete/"+$scope.domain+"",
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
		                        		 formname: { type: "string", validation: { required: true } },
		                        		 icon: { type: "string", validation: { required: true } },
		                        		 appid: { type: "number", validation: { required: true } }
		                             }
		                         }
		                     },
		                    pageSize: 8,
		                    serverPaging: true,
		                    serverFiltering: true,
		                    serverSorting: true
		                },
		                filterable:{
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
		                toolbar: kendo.template($("#add").html()),
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
								{ field: "formname", title: "<span data-translate='Нэр'></span>"},
								{ field: "icon", title: "<span data-translate='Икон'></span>"},
								{ field: "appid", title: "<span data-translate='Application'></span>"},
								{template: kendo.template($("#rep").html()),  width: "140px" },
								{ command: [ "destroy"], title: "&nbsp;", width: "140px" }
	                      ],
	                      editable: "inline",
	                      autoBind: true,
		            }

        }]
    )
