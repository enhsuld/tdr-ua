angular
    .module('altairApp')
    	.controller("quatapersonCtrl",['$scope','user_data','mainService','sweet','$state','p_cat','p_year','reason',
	        function ($scope,user_data,mainService,sweet,$state,p_cat,p_year,reason) {       	
    		
    			var modal = UIkit.modal("#modal_update");
    	    	console.log(p_year[0].value);
    			$scope.role=user_data[0].role;
    			$scope.domain="com.netgloo.models.MainAuditRegistration.";
    			
    			
    			$scope.update=function(item){
    				if(item.autype==2 || item.autype==3){
    					$state.go('restricted.pages.configusernongov',{id:item.id,stepid:item.stepid});
    				}
    				else{
    					$state.go('restricted.pages.configuser',{id:item.id,stepid:item.stepid});
    				}
    				
    			}
    			
    			$scope.editAuditName = function(obj){
    				if (obj.id != null && obj.id != undefined){
    					$scope.auditData = {};
        				$scope.auditData.id = obj.id;
        				if (obj.auditname != null && obj.auditname != undefined){
        					$scope.auditData.auditname = obj.auditname;
        				}
                    	UIkit.modal("#editAuditNamePopup").show();
    				}
    				
                }
    			
    			$scope.submitAuditName = function(){
    				mainService.withdata("post","/au/update/com.netgloo.models.MainAuditRegistration.",$scope.auditData).then(function(response){
    					if (response){
    						$(".k-grid").data("kendoGrid").dataSource.read();
    						UIkit.modal("#editAuditNamePopup").hide();
    					}
    					
    				});
    			}
    			  
    			var aj=[{"text":"Хувиарлаагүй","value":"1"},{"text":"Хувиарласан","value":"2"},{"text":"Баталсан","value":"3"},{"text":"Баталсан","value":"5"},{"text":"Баталсан","value":"6"},{"text":"Буцаасан","value":"4"}];
	            
	     
	    		function init(){
	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
	   		   			.then(function(data){   		   				
	   		   				if(data.rread==1){
	   		   					
		   		   		       $scope.puserGrid = {
		   			                dataSource: {
		   			                    autoSync: true,
		   			                    transport: {
		   			                    	read:  {
		   			                            url: "/au/list/MainAuditRegistration",
		   			                            contentType:"application/json; charset=UTF-8",     
		   			                            data:{"custom":"where depid="+user_data[0].depid+" and  audityear="+p_year[0].value+" and isenabled=1", "depid":user_data[0].depid},
		   			                            type:"POST"
		   			                        },
		   			                        update: {
		   			                            url: "/core/update/"+$scope.domain+"",
		   			                            contentType:"application/json; charset=UTF-8",                                    
		   			                            type:"POST",
		   			                            complete: function(e) {
			   			                         	if(e.responseText=="false"){			 		                            		
			   		                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
			   		                            	}else{
			   		                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
			   		                            	}
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
		   			                             	id: { type: "number", editable: false,nullable: false},
		   			                             	gencode: { type: "string", editable: false },	                             	
		   			                             	orgtype: { type: "number" , editable: false},
		   			                             	orgname: { type: "string" , editable: true},
		   			                             	autype: { type: "number" , editable: false},
		   			                             	data1: { type: "string" , editable: false},
		   			                             	data2: { type: "string" , editable: false},
		   			                          		data3: { type: "string" , editable: false},	                                          	
		   			                       			data4: { type: "string", editable: false },
		   			                    			data5: { type: "string", editable: false },	   
		   			                    			data6: { type: "string", editable: false },
		   			                    			data7: { type: "string", editable: false},	
		   			                    			depid: { type: "number", editable: false},	
		   			                    			terguuleh: { type: "string",editable: false },	
		   			                    			stepid: { type: "string" , editable: false },	
		   			                    			auditname: { type: "string", editable: true },	
		   			                    			isenabled: { type: "boolean", editable: false },	
		   			                             }	                    
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
		   			                reorderable: true,
		   			                resizable: true,
		   			                columnMenu: true,
		   			                pageable: {
		   			                    refresh: true,
		   			                    pageSizes: true,
		   			                    buttonCount: 5
		   			                },
		   			                columns: [
		   			                		  {title: "#",template: "<span class='row-number'></span>", width:"60px", locked: true,lockable: false},
		   			                          { field:"gencode", title: "Код", width:"200px",locked: true,},        
		   			                          { field:"orgtype", title: "Ангилал",values:p_cat, width:"200px" },
		   			                          { field:"orgname", title: "Байгууллагын нэр",width:"200px",locked: true,},
		   			                          { field:"orgcode", title: "Байгууллагын Код",width:"200px",locked: true,},
		   			                          { field:"autype", title: "Төрөл",values:reason,width:"200px"},
		   			                          { field:"data1", title: "Тэргүүлэх аудитор",filterable:false,width: 250},		   			                        
		   			                          { field:"data2", title: "Менежер",filterable:false,width: 250},
		   			                          { field:"data3", title: "Ахлах аудитор",filterable:false,width: 250},
		   			                          { field:"data4", title: "Аудитор",filterable:false,width: 250},	 
		   			                          { field:"data5", title: "Ахлах шинжээч",filterable:false,width: 250},
		   			                          { field:"data6", title: "Шинжээч",filterable:false,width: 250},
		   			                          { field:"data7", title: "Гэрээт ажилтан",filterable:false,width: 250},
		   			                          { field:"terguuleh", title: "Хараат бус аудитын компани",filterable:false,width: 250},
		   			                          { field:"stepid",template: kendo.template($("#step").html()),width: 200, title: "Төлөв"},
		   			                          { field:"auditname", width: 200, title: "Аудитын нэр",locked: true},
		   			                          ],
		   			                     /*  template:"<span style='cursor:pointer;' ng-click='editAuditName(dataItem)'>#:auditname#</span>"*/
		   			                       dataBound: function () {
					   	   		                var rows = this.items();
					   	   		                  $(rows).each(function () {
					   	   		                      var index = $(this).index() + 1 
					   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
					   	   		                      var rowLabel = $(this).find(".row-number");
					   	   		                      $(rowLabel).html(index);
					   	   		                  });
					   	   		  	           },
		   			                      editable: true
		   			            };
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
	   		   					if(data.rupdate==1 && $scope.role=="ROLE_FIRST"){	   		   						
	   		   						$scope.puserGrid.columns.push({template: kendo.template($('#update').html()),  width: 140})
	   		   					}
		   		   				if(data.rupdate==1 && $scope.role=="ROLE_SECOND"){	   		   						
	   		   						$scope.puserGrid.columns.push({template: kendo.template($('#update2').html()),  width: 140})
	   		   					}
	   		   					if(data.rdelete==1){
	   		   						$scope.puserGrid.columns.push({command:["destroy"],  width: 80})
	   		   					}
	   		   				}
	   		   				else{
	   		   					$state.go('error.404');
	   		   				}			
	   		   		 });
	   	    	}
   			
	    		init();	            
	        
	        }
    ]);
