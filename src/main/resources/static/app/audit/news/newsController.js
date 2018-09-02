angular
    .module('altairApp')
    	.controller("newsCtrl",['$scope','user_data',function ($scope,user_data) {
    		$scope.domain = "com.netgloo.models.LutFlashNews";
    		$scope.message_types = [{"text": "Message", "value":"0"},{"text": "Alert", "value":"1"}];
    		$scope.proleGrid = {
	                dataSource: {
	                   
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LutFlashNews",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain+".",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST"
	                        },
	                        destroy: {
	                            url: "/core/delete/"+$scope.domain+".",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST"
	                        },
	                        create: {
	                            url: "/core/create/"+$scope.domain+".",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
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
	                            	id: { editable: false,nullable: true, defaultValue:0},
	                              	title: { type: "string", validation: { required: true } },
	                              	status: { type: "number", validation: { required: true } },
	                              	description: { type: "string", validation: { required: true }}                           
	                              }
	                         }
	                     },
	                    pageSize: 8,
	                    serverPaging: true,
	                    serverFiltering: true,
	                    serverSorting: true
	                },
	                toolbar: ["create"],
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
	                          { field:"title", title: "Гарчиг"},
	                          { field:"description", title: "Агуулга",editor: function(container, options) {
	                              $('<textarea md-input textarea-autosize cols="30" rows="4" class="md-input" data-bind="value: ' + options.field + '"></textarea>').appendTo(container);
	                          }
	                          },
	                          { field:"status", title: "Төрөл", values:$scope.message_types},
	                          { command:["edit","destroy"], title: "&nbsp;", width: "250px" }
	                          ],
	                      editable: "popup",
	                      edit: function (e) 
	                      {
	                          var editWindow = this.editable.element.data("kendoWindow");
	                          editWindow.wrapper.css({ width: 600 });
	                      }
	            };
    	}
    	                          
]);