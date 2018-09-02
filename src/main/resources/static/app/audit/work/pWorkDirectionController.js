angular
    .module('altairApp')
    	.controller("auditDirectionCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
    	                           'notices',
    	                           'risks',
    	                           'procedures',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data,notices,risks,procedures) {    
    	                        	   
    	                            var $formValidate = $('#form_au_dir');
    	                       		
    	                       		$formValidate
    	                               .parsley()
    	                               .on('form:validated',function() {
    	                                  $scope.$apply();
    	                               })
    	                               .on('field:validated',function(parsleyField) {
    	                                   if($(parsleyField.$element).hasClass('md-input')) {
    	                                      $scope.$apply();
    	                                   }
    	                               });
    	                        	  
    	                        	   var procedures_data = $scope.selectize_procedures_options = procedures;
    	                      			
    	                      			$scope.selectize_procedures_config = {
    	                      	                plugins: {
    	                      	                    'remove_button': {
    	                      	                        label     : ''
    	                      	                    }
    	                      	                },
    	                      	                maxItems: null,
    	                      	                minItems:1,
    	                      	                valueField: 'value',
    	                      	                labelField: 'text',
    	                      	                searchField: 'text',
    	                      	                create: false,
    	                      	                render: {
    	                      	                    option: function(procedures_data, escape) {
    	                      	                        return  '<div class="option">' +
    	                      	                            '<span class="title">' + escape(procedures_data.text) + '</span>' +
    	                      	                            '</div>';
    	                      	                    }
    	                      	                }
    	                      	            };
    	                      			
    	                      			var risks_data = $scope.selectize_risks_options = risks;
    	                      			
    	                      			$scope.selectize_risks_config = {
    	                      	                plugins: {
    	                      	                    'remove_button': {
    	                      	                        label     : ''
    	                      	                    }
    	                      	                },
    	                      	                maxItems: null,
    	                      	                minItems:1,
    	                      	                valueField: 'value',
    	                      	                labelField: 'text',
    	                      	                searchField: 'text',
    	                      	                create: false,
    	                      	                render: {
    	                      	                    option: function(risks_data, escape) {
    	                      	                        return  '<div class="option">' +
    	                      	                            '<span class="title">' + escape(risks_data.text) + '</span>' +
    	                      	                            '</div>';
    	                      	                    }
    	                      	                }
    	                      	            };
    	                      			
    	                      			var notices_data = $scope.selectize_notices_options = notices;
    	                       			
    	                       			$scope.selectize_notices_config = {
    	                       	                plugins: {
    	                       	                    'remove_button': {
    	                       	                        label     : ''
    	                       	                    }
    	                       	                },
    	                       	                maxItems: null,
    	                       	                minItems:1,
    	                       	                valueField: 'value',
    	                       	                labelField: 'text',
    	                       	                searchField: 'text',
    	                       	                create: false,
    	                       	                render: {
    	                       	                    option: function(notices_data, escape) {
    	                       	                        return  '<div class="option">' +
    	                       	                            '<span class="title">' + escape(notices_data.text) + '</span>' +
    	                       	                            '</div>';
    	                       	                    }
    	                       	                }
    	                       	            };
    	      $scope.formdata = {};
    	      $scope.add = function(){
    	    	  $scope.formdata = {};
    	    	  $scope.formdata.id = 0;
    	      }
    	                       			
    	      $scope.submitForm = function(){
    	    	 
    	    	  mainService.withdata('put','/my/addWorkDirection', $scope.formdata)
		   			.then(function(data){	
		   			 sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		   			$("#notificationSuccess").trigger('click');
   				    $(".k-grid").data("kendoGrid").dataSource.read(); 
		   			 $('#closemodal').trigger('click');
		   				
	   			});	
    	    	  
    	      }
    	      
    	      $scope.update=function(i){
    	    	  mainService.withdomain('get', '/my/resource/LutAuditDir/'+i).
	    			then(function(data){
	    				$scope.formdata=data[0];   	            	    				
	    				console.log($scope.formdata);
	    			});	  
  	        	}
 	    	
    	      $scope.delet=function(i){
 	    			
 	    			mainService.withdomain('delete','/my/deleteDir/'+i)
			   			.then(function(){
			   				$("#notificationSuccess").trigger('click');
			   				$(".k-grid").data("kendoGrid").dataSource.read(); 
			   				sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
		   			});	
  	        	
  	        }
    	                       			
	    	  $scope.domain="com.netgloo.models.LutAuditDir.";
	    	  if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/au/list/LutAuditDir",
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
	 		                        		 shortname: { type: "string", validation: { required: true } },
			                        		                        		 		                        		 
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
			                //toolbar: ["create"],
			                toolbar: kendo.template($("#add").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									{ field: "name", title: "Нэр"},
	 								{ field: "shortname", title: "Товч нэр"},
	 								{ template: kendo.template($("#lol").html()), width: "236px"}  							
									//{ command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }
		                      ],
		                      //editable: "popup",
		                      autoBind: true,
			            }
	    	  }
	    	  else{
	    		  function init(){
	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
	   		   			.then(function(data){
	   		   				if(data.rread==1){
				 	          $scope.puserGrid = {
				 		                dataSource: {
				 		                   
				 		                    transport: {
				 		                    	read:  {
				 		                    		url: "/au/list/LutAuditDir",
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
				 		                        		 shortname: { type: "string", validation: { required: true } },
				 		                        		                        		 		                        		 
				 		                             }
				 		                         }
				 		                     },
				 		                    pageSize: 8,
				 		                    serverPaging: true,
				 		                    serverFiltering: true,
				 		                    serverSorting: true
				 		                },
				 		                excel: {
		         	   		                fileName: "Organization Export.xlsx",
		         	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
		         	   		                filterable: true,
		         	   		                allPages: true
		         	   		            },
				 		                filterable:{
	     	   			                	 mode: "row"
	     	   			                },
				 		                sortable: true,
				 		                resizable: true,
				 		                //toolbar: ["create"],
				 		                pageable: {
				 		                    refresh: true,
				 		                    pageSizes: true,
				 		                    buttonCount: 5
				 		                },
				 		                columns: [
				 								{ field: "name", title: "Нэр"},
				 								{ field: "shortname", title: "Товч нэр"},
				 	                      ],
				 	                      //editable: "popup",
				 	                      autoBind: true,
				 		            }
				 	        if(data.rcreate==1){	   		   					  
		 	   					if(data.rexport==1){
		 	   					  $scope.puserGrid.toolbar=["excel","pdf"];
		 	   					}
		 	   					else{
		 	   					  $scope.puserGrid.toolbar= kendo.template($("#add").html());
		 	   					}
		 					}
		 					else if(data.rexport==1){
		 						$scope.puserGrid.toolbar=["excel","pdf"];
		 					}
			 	          	if(data.rupdate==1 && data.rdelete==1){
								$scope.puserGrid.columns.push({ template: kendo.template($("#lol").html()), width: "236px"});
							}
							else if(data.rupdate==1 && data.rdelete==0){
								$scope.puserGrid.columns.push({ template: kendo.template($("#lolu").html()), width: "80px"});
							}
							else if(data.rupdate==0 && data.rdelete==1){
								$scope.puserGrid.columns.push({ template: kendo.template($("#lold").html()), width: "80px"});
							}
	 				}
	 				else{
	 					$state.go('error.404');
	 				}			
	 		 });
	 	}

	    		  init();	    
	    	  }
	  		        

}
]);
