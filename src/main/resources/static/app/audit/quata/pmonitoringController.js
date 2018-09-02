angular
    .module('altairApp')
    	.controller("monitoringCtrl",['$scope','user_data','mainService','sweet','$state','p_cat','au_levels','reason',
	        function ($scope,user_data,mainService,sweet,$state,p_cat,au_levels,reason) {       	
    		
    			var modal = UIkit.modal("#modal_update");
    	    	
    			$scope.role=user_data[0].role;
    			    	
    			$scope.domain="com.netgloo.models.MainAuditRegistration.";
    			
    			
    			$scope.read=function(item){
    				$state.go('restricted.pages.mainwork',{issueId:item.id,stepid:item.stepid,typeid:item.orgtype});
    			}
    			  
    			var aj=[{"text":"Хувиарлаагүй","value":"1"},{"text":"Хувиарласан","value":"2"},{"text":"Баталсан","value":"3"},{"text":"Буцаасан","value":"4"}];
    			
    			var alevel=[{"text":"Төлөвлөх үе шат","value":"3"},{"text":"Гүйцэтгэх үе шат","value":"4"},{"text":"Тайлагналын үе шат","value":"5"},{"text":"Тайлагналын дараах","value":"6"}];
	            
	     
    			 $scope.puserGrid = {
			                dataSource: {
			                    autoSync: true,
			                    transport: {
			                    	read:  {
			                            url: "/au/list/MainAuditRegistration",
			                            contentType:"application/json; charset=UTF-8",           
			                            data:{"userid":user_data[0].id, "depid":user_data[0].depid, "isactive":1},
			                            type:"POST"
			                        },
			                        update: {
			                            url: "/core/update/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        destroy: {
			                          //  url: "/core/delete/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST",
			                            complete: function(e) {
			                            	 $("#notificationDestroy").trigger('click');
			                    		}
			                        },
			                        create: {
			                            url: "/core/create/"+$scope.domain+"",
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
			                         }
			                     },
			                    pageSize: 5,
			                    serverPaging: true,
			                    serverSorting: true,
			                    serverFiltering: true
			                },
	   			             
	                         filterable: {
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
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
	   			          /*   dataBound: function (e) {
	   			              var grid = this;
	   			              grid.tbody.find("tr").dblclick(function (e) {
	   			                  var dataItem = grid.dataItem(this);
	   			                  $scope.read(dataItem);
	   			              });
	   			            },*/
			                columns: [
			                		  {title: "#",template: "<span class='row-number'></span>", width:"60px"},
			                          { field:"gencode", title: "Код", width: 150},      
			                          { field:"orgname", title: "Байгууллагын нэр",width: 200},
			                          { field:"orgtype", title: "Ангилал",values:p_cat, width: 100 },	
			                          { field:"autype", title: "Төрөл",values:reason, width: 150 },
			                          { field:"audityear", title: "Аудитын жил",width: 100},
			                          { field:"stepid", title: "Аудитын үе шат",width: 150, values:alevel},
			                     	  {template: kendo.template($("#aper").html()), title: "Аудитор", width: 150},	
			                     	  {template: kendo.template($("#mper").html()), title: "Менежер", width: 150},	
			                          {template: kendo.template($("#tper").html()), title: "Тэргүүлэх аудитор", width: 150},	
			                          /*{ field:"auditors", title: "Аудитор",filterable:false,width: 150},		   			                        
			                          { field:"checkers", title: "Шинжээч",filterable:false,width: 150},
			                          { field:"managers", title: "Менежер",filterable:false,width: 150},
			                          { field:"terguuleh", title: "Тэргүүлэх аудитор",filterable:false,width: 150},*/
			                          {template: kendo.template($("#read").html()), width: "90px"}		                      
	   			                      ],
	   			                      dataBound: function () {
			   	   		                var rows = this.items();
			   	   		                  $(rows).each(function () {
			   	   		                      var index = $(this).index() + 1 
			   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
			   	   		                      var rowLabel = $(this).find(".row-number");
			   	   		                      $(rowLabel).html(index);
			   	   		                  });
				   	   		              var grid = this;
				   			              grid.tbody.find("tr").dblclick(function (e) {
				   			                  var dataItem = grid.dataItem(this);
				   			                  $scope.read(dataItem);
				   			              });
			   	   		  	           },
			                      editable: false
			            };          
	        
	        }
    ]);
