angular
    .module('altairApp')
    	.controller("feedbackCategoryController", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'feedback_categories',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,feedback_categories) {
    	                        	   console.log(feedback_categories);
    	          if (feedback_categories == undefined || feedback_categories == null){
    	        	  var feedback_categories = [];
    	          }
    	          else{
    	        	  var feedback_categories = feedback_categories;
    	          }
    	          
    	          $scope.domain="com.netgloo.models.LnkFeedbackCategory.";
    	          $scope.puserGrid = {
		                dataSource: {
		                    transport: {
		                    	read:  {
		                    		url: "/au/list/LnkFeedbackCategory",
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
		                    			 title: { required: true, type:"string"},
		                        		 parentid: {type:"number"}
		                             }
		                         }
		                     },
		                    pageSize: 8,
		                    serverPaging: true,
		                    serverFiltering: true,
		                    serverSorting: true
		                },
		                filterable:false,
		                sortable: true,
		                columnMenu:true, 
		                resizable: true,
		                toolbar: ["create"],
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
								{ field: "title", title: "Гарчиг"},
								{ field: "parentid", title: "Эцэг ангилал",values:feedback_categories},
								{ command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }
	                      ],
	                      editable: "popup",
	                      autoBind: true,
		            }
    	    }]);