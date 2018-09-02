angular
    .module('altairApp')
    	.controller("lawCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',    	                           
    	                           '$timeout',
    	                           'mainService',
    	                           'sweet',
    	                           'user_data',
	        function ($scope,$rootScope,$state,$timeout,mainService,sweet,user_data) { 
    	                        	   
    	               	    			$scope.domain="com.netgloo.models.LutLaw.";    	               	    		
    	               	    		if(user_data[0].role=="ROLE_SUPER"){
    	               	    			
    	               	    			$scope.mainGridOptions = {
    	               	   		          dataSource: {
    	               	   		        	  //autoSync: true,
    	               	   		        	  transport: {
    	               	   		        		   read:  {
    	               	   	                            url: "/info/list/LutLaw",
    	               	   	                            contentType:"application/json; charset=UTF-8",     
    	               	   	                            data: { "custom":"where parentid is null and lawcategory=1","sort":[{field: 'id', dir: 'asc'}]},
    	               	   	                            type:"POST"
    	               	   	                        },
    	               	   	                        update: {
    	               	   	                            url: "/core/update/"+$scope.domain+"",
    	               	   	                            contentType:"application/json; charset=UTF-8",                                    
    	               	   	                            type:"POST",
    	               	   	                            complete: function(e) {
    	               	   		                         	if(e.responseText=="false"){			 		                            		
    	               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
    	               	   	                            		$(".k-grid").data("kendoGrid").dataSource.read();
    	               	   	                            	}else{
    	               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
    	               	   	                            		$(".k-grid").data("kendoGrid").dataSource.read();
    	               	   	                            	}
    	               	   		                    	}
    	               	   	                        },
		    	               	   	                  destroy: {
		    	               	   	                	  url: "/core/delete/"+$scope.domain+"",
		    	               	   	                	  contentType:"application/json; charset=UTF-8", 
		    	               	   	                	  type:"POST",
		    	               	   	                	  complete: function(e) {
		    	               	   	                		  if(e.responseText=="false"){			 		                            		
		    	               	   	                			  UIkit.notify("Алдаа үүслээ. Уг хууль/зүйл нь дотроо зүйл/заалттай эсвэл эрсдэлтэй холбогдсон байна", {status:'warning'});
		    	               	   	                			  $(".k-grid").data("kendoGrid").dataSource.read();
		    	               	   	                		  }
		    	               	   	                		  else{
		    	               	   	                			  UIkit.notify("Амжилттай устгагдлаа.", {status:'success'});
		    	               	   	                			  $(".k-grid").data("kendoGrid").dataSource.read();
		    	               	   	                		  }
		    	               	   	                	  }
		    	                                      },
		    	                                      create: {
		    	                                    	  url: "/core/create/"+$scope.domain+"",
		    	                                    	  contentType:"application/json; charset=UTF-8",                                    
	    	               	   	                          type:"POST",
		    	               	   	                    complete: function(e) {
		               	   		                         	if(e.responseText=="false"){			 		                            		
		               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
		               	   	                            	}else{
		               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
		               	   	                            		$(".k-grid").data("kendoGrid").dataSource.read();
		               	   	                            	}
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
    	               	                                id:  { nullable: false, type: "number"},
    	               	                                lawcategory: {nullable: false, type: "number",  defaultValue: 1 },
 	                	                             	lawname: { type: "string", validation: { required: true } },
    	               	                                 }		                    
    	               	                            }
    	               	                        },
    	               	   		            pageSize: 50,
    	               	   		            serverPaging: true,
    	               	   		            serverSorting: true
    	               	   		          },
    	               	   		          sortable: true,
    	               	   		          pageable: true,
    	               	   		          dataBound: function () {
    	               	   		        	  
    	               	   		        	  var grid =this;
    	               	   		        	  grid.element.delegate("tbody>tr", "dblclick", function () {
    	               	   		        	      grid.expandRow($(this));
    	               	   		        	  });
    	               	   		        	 
    	               	     	              },
    	               	   		        
    	               	     	          toolbar: ["create"],
    	               	     	          editable: 'inline',
    	               	   		          columns: [    	               	   	                    
    	               	   	                    { field:"lawname", title: "Хууль" },
    	               	   	                    { command: [{name: "edit"},	{name:"destroy"}], title: "&nbsp;", width: "270px" }
    	               	   	                ],
    	               	   	          
    	               	   		        };
    	               	    			
    	               	    			$scope.ordersGridOptions = function(dataItem) {
    	               			          return {
    	               			            dataSource: {
    	               			              //autoSync: true,
    	               			              transport: {
    	               			            		read:  {
    	               		            			    url: "/info/list/LutLaw",
    	               		                            contentType:"application/json; charset=UTF-8",     
    	               		                            data: { "custom":"where parentid is not null and lawcategory=1","sort":[{field: 'id', dir: 'asc'}]},
    	               		                            type:"POST"
    	               		                        },
	    	               		                     update: {
	 	               	   	                            url: "/core/update/"+$scope.domain+"",
	 	               	   	                            contentType:"application/json; charset=UTF-8",                                    
	 	               	   	                            type:"POST",
	 	               	   	                            complete: function(e) {
	 	               	   		                         	if(e.responseText=="false"){			 		                            		
	 	               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	 	               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
	 	               	   	                            	}else{
	 	               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	 	               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
	 	               	   	                            	}
	 	               	   		                    	}
	 	               	   	                        },
	 	               	   	                        destroy: {
	    	               	   	                	  url: "/core/delete/"+$scope.domain+"",
	    	               	   	                	  contentType:"application/json; charset=UTF-8", 
	    	               	   	                	  type:"POST",
	    	               	   	                	  complete: function(e) {
	    	               	   	                		  if(e.responseText=="false"){			 		                            		
	    	               	   	                			  UIkit.notify("Алдаа үүслээ. Уг хууль/зүйл нь дотроо зүйл/заалттай эсвэл эрсдэлтэй холбогдсон байна", {status:'warning'});
	    	               	   	                			  $(".dgrid").data("kendoGrid").dataSource.read();
	    	               	   	                		  }
	    	               	   	                		  else{
	    	               	   	                			  UIkit.notify("Амжилттай устгагдлаа.", {status:'success'});
	    	               	   	                			  $(".dgrid").data("kendoGrid").dataSource.read();
	    	               	   	                		  }
	    	               	   	                	  }
	 	               	   	                        },
	 	               	   	                        create: {
	    	                                    	  url: "/core/create/"+$scope.domain+"",
	    	                                    	  contentType:"application/json; charset=UTF-8",                                    
    	               	   	                          type:"POST",
    	               	   	                          	data: { "parentid":dataItem.id},
	    	               	   	                      complete: function(e) {
	               	   		                         	if(e.responseText=="false"){			 		                            		
	               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
	               	   	                            	}else{
	               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
	               	   	                            	}
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
    	               	                        	    id:  { nullable: false, type: "number"},
    	               	                                parentid: { nullable: false, type: "number", defaultValue: dataItem.id},    	               	                            
	                	                             	lawname: { type: "string", validation: { required: true } },    
	                	                             	lawcategory: {nullable: false, type: "number",  defaultValue: 1 },
    	               	                             }	                    
    	               	                         }
    	               	                      },
    	               			              serverPaging: true,
    	               			              serverSorting: true,
    	               			              serverFiltering: true,    	               			              
    	               			              pageSize: 50,
    	               			              filter: { field: "parentid", operator: "eq", value: dataItem.id }
    	               			            },
    	               			            scrollable: true,
    	               			            sortable: true,
    	               			            pageable: {
    	               		                    refresh: true,
    	               		                    pageSizes: true,
    	               		                    buttonCount: 5
    	               		                },
    	               		                toolbar: ["create"],
    	               			            columns: [
    	               			            	{ field:"lawname", title: "Зүйл" },
	                	                        { command: ["edit", "destroy"], title: "&nbsp;", width: "270px" }
    	               		                ],
    	               		             editable: "inline"
    	               			          };
    	               			        };
    	               			     $scope.ordersGridOptions1 = function(dataItem) {
   	               			          return {
   	               			            dataSource: {
   	               			              transport: {
   	               			            read:  {
           		            			    url: "/info/list/LutLaw",
           		                            contentType:"application/json; charset=UTF-8",     
           		                            data: { "custom":"where parentid is not null and lawcategory=1","sort":[{field: 'id', dir: 'asc'}]},
           		                            type:"POST"
           		                        },
               		                     update: {
            	   	                            url: "/core/update/"+$scope.domain+"",
            	   	                            contentType:"application/json; charset=UTF-8",                                    
            	   	                            type:"POST",
            	   	                            complete: function(e) {
            	   		                         	if(e.responseText=="false"){			 		                            		
            	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
            	   	                            		$(".dgrid1").data("kendoGrid").dataSource.read();
            	   	                            	}else{
            	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
            	   	                            		$(".dgrid1").data("kendoGrid").dataSource.read();
            	   	                            	}
            	   		                    	}
            	   	                        },
            	   	                        destroy: {
               	   	                	  url: "/core/delete/"+$scope.domain+"",
               	   	                	  contentType:"application/json; charset=UTF-8", 
               	   	                	  type:"POST",
               	   	                	  complete: function(e) {
               	   	                		  if(e.responseText=="false"){			 		                            		
               	   	                			  UIkit.notify("Алдаа үүслээ. Уг хууль/зүйл нь дотроо зүйл/заалттай эсвэл эрсдэлтэй холбогдсон байна", {status:'warning'});
               	   	                			  $(".dgrid1").data("kendoGrid").dataSource.read();
               	   	                		  }
               	   	                		  else{
               	   	                			  UIkit.notify("Амжилттай устгагдлаа.", {status:'success'});
               	   	                			  $(".dgrid1").data("kendoGrid").dataSource.read();
               	   	                		  }
               	   	                	  }
            	   	                        },
            	   	                        create: {
                                    	  url: "/core/create/"+$scope.domain+"",
                                    	  contentType:"application/json; charset=UTF-8",                                    
           	   	                          type:"POST",
           	   	                          	data: { "parentid":dataItem.id},
               	   	                      complete: function(e) {
       	   		                         	if(e.responseText=="false"){			 		                            		
       	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
       	   	                            		$(".dgrid1").data("kendoGrid").dataSource.read();
       	   	                            	}else{
       	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
       	   	                            		$(".k-grid1").data("kendoGrid").dataSource.read();
       	   	                            	}
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
                	                             	id:  { nullable: false, type: "number"},
	               	                                parentid: { nullable: false, type: "number", defaultValue: dataItem.id},    	               	                            
                	                             	lawname: { type: "string", validation: { required: true } },    
                	                             	lawcategory: {nullable: false, type: "number",  defaultValue: 1 },
                	                             	zaalt: { type: "string", validation: { required: true } },	 
   	               	                             }	                    
   	               	                         }
   	               	                      },
   	               			              serverPaging: true,
   	               			              serverSorting: true,
   	               			              serverFiltering: true,   	               			             
   	               			              pageSize: 50,
   	               			              filter: { field: "parentid", operator: "eq", value: dataItem.id }
   	               			            },
   	               			            scrollable: true,
   	               			            sortable: true,
   	               			            pageable: {
   	               		                    refresh: true,
   	               		                    pageSizes: true,
   	               		                    buttonCount: 5
   	               		                },
   	               		                toolbar: ["create"],
   	               			            columns: [
   	               			            		{ field:"zaalt", title: "Заалт" ,width: "120px"},
	                	                        { field:"lawname", title: "Нэр" },
	                	                        { command: ["edit", "destroy"], title: "&nbsp;", width: "270px" }
   	               		                ],
   	               		             editable: "inline"
   	               			          };
   	               			        };    	               	    			
    	               	    		}
    	               	    		else{
    	               	    			function initq(){
    	    	           	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
    	    	           	   	    		 
    	    	           	   		   			.then(function(data){
    	    	           	   		   				if(data.rread==1){  	           	   		   			
    	    	           	   		   			$scope.mainGridOptions = {
    	    	    	               	   		          dataSource: {
    	    	    	               	   		        	  //autoSync: true,
    	    	    	               	   		        	  transport: {
    	    	    	               	   		        		   read:  {
    	    	    	               	   	                            url: "/info/list/LutLaw",
    	    	    	               	   	                            contentType:"application/json; charset=UTF-8",     
    	    	    	               	   	                            data: { "custom":"where parentid is null and lawcategory=1","sort":[{field: 'id', dir: 'asc'}]},
    	    	    	               	   	                            type:"POST"
    	    	    	               	   	                        },
    	    	    	               	   	                        update: {
    	    	    	               	   	                            url: "/core/update/"+$scope.domain+"",
    	    	    	               	   	                            contentType:"application/json; charset=UTF-8",                                    
    	    	    	               	   	                            type:"POST",
    	    	    	               	   	                            complete: function(e) {
    	    	    	               	   		                         	if(e.responseText=="false"){			 		                            		
    	    	    	               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
    	    	    	               	   	                            		$(".k-grid").data("kendoGrid").dataSource.read();
    	    	    	               	   	                            	}else{
    	    	    	               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
    	    	    	               	   	                            		$(".k-grid").data("kendoGrid").dataSource.read();
    	    	    	               	   	                            	}
    	    	    	               	   		                    	}
    	    	    	               	   	                        },
    	    			    	               	   	                  destroy: {
    	    			    	               	   	                	  url: "/core/delete/"+$scope.domain+"",
    	    			    	               	   	                	  contentType:"application/json; charset=UTF-8", 
    	    			    	               	   	                	  type:"POST",
    	    			    	               	   	                	  complete: function(e) {
    	    			    	               	   	                		  if(e.responseText=="false"){			 		                            		
    	    			    	               	   	                			  UIkit.notify("Алдаа үүслээ. Уг хууль/зүйл нь дотроо зүйл/заалттай эсвэл эрсдэлтэй холбогдсон байна", {status:'warning'});
    	    			    	               	   	                			  $(".k-grid").data("kendoGrid").dataSource.read();
    	    			    	               	   	                		  }
    	    			    	               	   	                		  else{
    	    			    	               	   	                			  UIkit.notify("Амжилттай устгагдлаа.", {status:'success'});
    	    			    	               	   	                			  $(".k-grid").data("kendoGrid").dataSource.read();
    	    			    	               	   	                		  }
    	    			    	               	   	                	  }
    	    			    	                                      },
    	    			    	                                      create: {
    	    			    	                                    	  url: "/core/create/"+$scope.domain+"",
    	    			    	                                    	  contentType:"application/json; charset=UTF-8",                                    
    	    		    	               	   	                          type:"POST",
    	    			    	               	   	                    complete: function(e) {
    	    			               	   		                         	if(e.responseText=="false"){			 		                            		
    	    			               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
    	    			               	   	                            	}else{
    	    			               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
    	    			               	   	                            		$(".k-grid").data("kendoGrid").dataSource.read();
    	    			               	   	                            	}
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
    	    	    	               	                                id:  { nullable: false, type: "number"},
    	    	    	               	                                lawcategory: {nullable: false, type: "number",  defaultValue: 1 },
    	    	 	                	                             	lawname: { type: "string", validation: { required: true } },
    	    	    	               	                                 }		                    
    	    	    	               	                            }
    	    	    	               	                        },
    	    	    	               	   		            pageSize: 50,
    	    	    	               	   		            serverPaging: true,
    	    	    	               	   		            serverSorting: true
    	    	    	               	   		          },
    	    	    	               	   		          sortable: true,
    	    	    	               	   		          pageable: true,
    	    	    	               	   		          dataBound: function () {
    	    	    	               	   		        	  
    	    	    	               	   		        	  var grid =this;
    	    	    	               	   		        	  grid.element.delegate("tbody>tr", "dblclick", function () {
    	    	    	               	   		        	      grid.expandRow($(this));
    	    	    	               	   		        	  });
    	    	    	               	   		        	   },
    	    	    	               	   		        editable: 'inline',
    	    	    	               	   		          columns: [
    	    	    	               	   	                    { field:"lawname", title: "Хууль" },
    	    	    	               	   	                    ],
    	    	    	               	   	          
    	    	    	               	   		        };
    	    	    	               	    			
    	    	    	               	    			$scope.ordersGridOptions = function(dataItem) {
    	    	    	               	    			   	    	    	               	    				
    	    	    	               			          return {
    	    	    	               			            dataSource: {
    	    	    	               			              transport: {
    	    	    	               			            		read:  {
    	    	    	               		            			    url: "/info/list/LutLaw",
    	    	    	               		                            contentType:"application/json; charset=UTF-8",     
    	    	    	               		                            data: { "custom":"where parentid is not null and lawcategory=1","sort":[{field: 'id', dir: 'asc'}]},
    	    	    	               		                            type:"POST"
    	    	    	               		                        },
    	    		    	               		                     update: {
    	    		 	               	   	                            url: "/core/update/"+$scope.domain+"",
    	    		 	               	   	                            contentType:"application/json; charset=UTF-8",                                    
    	    		 	               	   	                            type:"POST",
    	    		 	               	   	                            complete: function(e) {
    	    		 	               	   		                         	if(e.responseText=="false"){			 		                            		
    	    		 	               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
    	    		 	               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
    	    		 	               	   	                            	}else{
    	    		 	               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
    	    		 	               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
    	    		 	               	   	                            	}
    	    		 	               	   		                    	}
    	    		 	               	   	                        },
    	    		 	               	   	                        destroy: {
    	    		    	               	   	                	  url: "/core/delete/"+$scope.domain+"",
    	    		    	               	   	                	  contentType:"application/json; charset=UTF-8", 
    	    		    	               	   	                	  type:"POST",
    	    		    	               	   	                	  complete: function(e) {
    	    		    	               	   	                		  if(e.responseText=="false"){			 		                            		
    	    		    	               	   	                			  UIkit.notify("Алдаа үүслээ. Уг хууль/зүйл нь дотроо зүйл/заалттай эсвэл эрсдэлтэй холбогдсон байна", {status:'warning'});
    	    		    	               	   	                			  $(".dgrid").data("kendoGrid").dataSource.read();
    	    		    	               	   	                		  }
    	    		    	               	   	                		  else{
    	    		    	               	   	                			  UIkit.notify("Амжилттай устгагдлаа.", {status:'success'});
    	    		    	               	   	                			  $(".dgrid").data("kendoGrid").dataSource.read();
    	    		    	               	   	                		  }
    	    		    	               	   	                	  }
    	    		 	               	   	                        },
    	    		 	               	   	                        create: {
    	    		    	                                    	  url: "/core/create/"+$scope.domain+"",
    	    		    	                                    	  contentType:"application/json; charset=UTF-8",                                    
    	    	    	               	   	                          type:"POST",
    	    	    	               	   	                          	data: { "parentid":dataItem.id},
    	    		    	               	   	                      complete: function(e) {
    	    		               	   		                         	if(e.responseText=="false"){			 		                            		
    	    		               	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
    	    		               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
    	    		               	   	                            	}else{
    	    		               	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
    	    		               	   	                            		$(".dgrid").data("kendoGrid").dataSource.read();
    	    		               	   	                            	}
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
    	    	    	               	                        	    id:  { nullable: false, type: "number"},
    	    	    	               	                                parentid: { nullable: false, type: "number", defaultValue: dataItem.id},    	               	                            
    	    		                	                             	lawname: { type: "string", validation: { required: true } },    
    	    		                	                             	lawcategory: {nullable: false, type: "number",  defaultValue: 1 },
    	    	    	               	                             }	                    
    	    	    	               	                         }
    	    	    	               	                      },
    	    	    	               			              serverPaging: true,
    	    	    	               			              serverSorting: true,
    	    	    	               			              serverFiltering: true,    	               			              
    	    	    	               			              pageSize: 50,
    	    	    	               			              filter: { field: "parentid", operator: "eq", value: dataItem.id }
    	    	    	               			            },
    	    	    	               			            scrollable: true,
    	    	    	               			            sortable: true,
    	    	    	               			            pageable: {
    	    	    	               		                    refresh: true,
    	    	    	               		                    pageSizes: true,
    	    	    	               		                    buttonCount: 5
    	    	    	               		                },
    	    	    	               		                columns: [
    	    	    	               			            	//{ field: "parentid", title: "Категори", width:90},
    	    		                	                        { field:"lawname", title: "Зүйл" },
    	    		                	                        //{ command: ["edit", "destroy"], title: "&nbsp;", width: "270px" }
    	    	    	               		                ],
    	    	    	               		             editable: "inline"    	    	    	               		            	 
    	    	    	               			          }; 	  
    	    	    	               			       
    	    	    	               			        };
    	    	    	               			     $scope.ordersGridOptions1 = function(dataItem) {
    	    	   	               			          return {
    	    	   	               			            dataSource: {
    	    	   	               			            transport: {
    	    	   	               			            read:  {
    	    	           		            			    url: "/info/list/LutLaw",
    	    	           		                            contentType:"application/json; charset=UTF-8",     
    	    	           		                            data: { "custom":"where parentid is not null and lawcategory=1","sort":[{field: 'id', dir: 'asc'}]},
    	    	           		                            type:"POST"
    	    	           		                        },
    	    	               		                     update: {
    	    	            	   	                            url: "/core/update/"+$scope.domain+"",
    	    	            	   	                            contentType:"application/json; charset=UTF-8",                                    
    	    	            	   	                            type:"POST",
    	    	            	   	                            complete: function(e) {
    	    	            	   		                         	if(e.responseText=="false"){			 		                            		
    	    	            	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
    	    	            	   	                            		$(".dgrid1").data("kendoGrid").dataSource.read();
    	    	            	   	                            	}else{
    	    	            	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
    	    	            	   	                            		$(".dgrid1").data("kendoGrid").dataSource.read();
    	    	            	   	                            	}
    	    	            	   		                    	}
    	    	            	   	                        },
    	    	            	   	                        destroy: {
    	    	               	   	                	  url: "/core/delete/"+$scope.domain+"",
    	    	               	   	                	  contentType:"application/json; charset=UTF-8", 
    	    	               	   	                	  type:"POST",
    	    	               	   	                	  complete: function(e) {
    	    	               	   	                		  if(e.responseText=="false"){			 		                            		
    	    	               	   	                			  UIkit.notify("Алдаа үүслээ. Уг хууль/зүйл нь дотроо зүйл/заалттай эсвэл эрсдэлтэй холбогдсон байна", {status:'warning'});
    	    	               	   	                			  $(".dgrid1").data("kendoGrid").dataSource.read();
    	    	               	   	                		  }
    	    	               	   	                		  else{
    	    	               	   	                			  UIkit.notify("Амжилттай устгагдлаа.", {status:'success'});
    	    	               	   	                			  $(".dgrid1").data("kendoGrid").dataSource.read();
    	    	               	   	                		  }
    	    	               	   	                	  }
    	    	            	   	                        },
    	    	            	   	                        create: {
    	    	                                    	  url: "/core/create/"+$scope.domain+"",
    	    	                                    	  contentType:"application/json; charset=UTF-8",                                    
    	    	           	   	                          type:"POST",
    	    	           	   	                          	data: { "parentid":dataItem.id},
    	    	               	   	                      complete: function(e) {
    	    	       	   		                         	if(e.responseText=="false"){			 		                            		
    	    	       	   	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
    	    	       	   	                            		$(".dgrid1").data("kendoGrid").dataSource.read();
    	    	       	   	                            	}else{
    	    	       	   	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
    	    	       	   	                            		$(".k-grid1").data("kendoGrid").dataSource.read();
    	    	       	   	                            	}
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
    	    	                	                             	id:  { nullable: false, type: "number"},
    	    		               	                                parentid: { nullable: false, type: "number", defaultValue: dataItem.id},    	               	                            
    	    	                	                             	lawname: { type: "string", validation: { required: true } },    
    	    	                	                             	lawcategory: {nullable: false, type: "number",  defaultValue: 1 },
    	    	                	                             	zaalt: { type: "string", validation: { required: true } },	 
    	    	   	               	                             }	                    
    	    	   	               	                         }
    	    	   	               	                      },
    	    	   	               			              serverPaging: true,
    	    	   	               			              serverSorting: true,
    	    	   	               			              serverFiltering: true,   	               			             
    	    	   	               			              pageSize: 50,
    	    	   	               			              filter: { field: "parentid", operator: "eq", value: dataItem.id }
    	    	   	               			            },
    	    	   	               			            scrollable: true,
    	    	   	               			            sortable: true,
    	    	   	               			            pageable: {
    	    	   	               		                    refresh: true,
    	    	   	               		                    pageSizes: true,
    	    	   	               		                    buttonCount: 5
    	    	   	               		                },
    	    	   	               		                columns: [
    	    	   	               			            		{ field:"zaalt", title: "Заалт" ,width: "120px"},
    	    		                	                        { field:"lawname", title: "Нэр" },
    	    		                	                        //{ command: ["edit", "destroy"], title: "&nbsp;", width: "270px" }
    	    	   	               		                ],
    	    	   	               		             editable: "inline"
    	    	   	               			          };
    	    	   	               			        };
    	    	                	            /*if(data.rcreate==1){	   		   					  
    	    			   		   					if(data.rexport==1){
    	    			   		   					$scope.mainGridOptions.toolbar=["create","excel","pdf"];    	    			   		   					
    	    			   		   					$scope.ordersGridOptions.toolbar=["create","excel","pdf"];
    	    			   		   					$scope.ordersGridOptions1.toolbar=["create","excel","pdf"];
    	    			   		   					}
    	    			   		   					else{
    	    			   		   					$scope.mainGridOptions.toolbar=["create"];
    	    			   		   					}
    	    		   		   					}
    	    		   		   					else if(data.rexport==1){
    	    		   		   					$scope.mainGridOptions.toolbar=["excel","pdf"];
	    			   		   					$scope.ordersGridOptions.toolbar=["excel","pdf"];
	    			   		   					$scope.ordersGridOptions1.toolbar=["excel","pdf"];
    	    		   		   					}    	    	                	          
    	    	                	            if(data.rupdate==1 && data.rdelete==1){
    	    	                	            	$scope.mainGridOptions.columns.push({ command: ["edit", "destroy"], title: "&nbsp;", width: "270px" });
    	    	                	            	
    	    		   		   					}
    	    	                	            else if(data.rupdate==1 && data.rdelete==0){
    	    	                	            	$scope.mainGridOptions.columns.push({ command: ["edit",], title: "&nbsp;", width: "270px" });
    	    		   		   					}
    	    	                	            else if(data.rupdate==0 && data.rdelete==1){
    	    	                	            	$scope.mainGridOptions.columns.push({ command: ["delete",], title: "&nbsp;", width: "270px" });
    	    		   		   					}*/
    	    		   		   				}
    	    		   		   				else{
    	    		   		   					$state.go('restricted.pages.error404');
    	    		   		   				}			
    	    		   		   		 });
    	    		   	    	}
    	    	   			
    	    		    		initq();
    	               	    		}
    	               	    			            
    		        
    		        }
    	    ]);