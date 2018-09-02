angular
    .module('altairApp')
    	.controller("positionCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'sweet',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,sweet) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutPosition.";
	    	  var isfl=[{"text":"Тийм","value":true },{"text":"Үгүй","value": false}];
	    	  var $formPosition = $('#form_position');
	    		
	    		$formPosition
	            .parsley()
	            .on('form:validated',function() {
	               $scope.$apply();
	            })
	            .on('field:validated',function(parsleyField) {
	                if($(parsleyField.$element).hasClass('md-input')) {
	                   $scope.$apply();
	                }
	            });
	    		var isfile_data = $scope.selectize_isfile_options = isfl;
     			
     			$scope.selectize_isfile_config = {
     	                plugins: {
     	                    'remove_button': {
     	                        label     : ''
     	                    }
     	                },
     	                maxItems: 1,
     	                minItems:1,
     	                valueField: 'value',
     	                labelField: 'text',
     	                searchField: 'text',
     	                create: false,
     	                render: {
     	                    option: function(isfile_data, escape) {
     	                        return  '<div class="option">' +
     	                            '<span class="title">' + escape(isfile_data.text) + '</span>' +
     	                            '</div>';
     	                    }
     	                }
     	            };
	    		var modalPosition = UIkit.modal("#modal_position");
	    	     
	    	      $scope.org = function(){
	    	    	  $scope.formdata={};
	    	    	  $scope.formdata.id=0;
	    	    	  $scope.formdata.isactive=true;
	    	    	  modalPosition.show();
	    	      }
	    	      $scope.edit = function(id){
	    	    	  mainService.withdomain('get', '/my/resource/LutPosition/'+id).
		    			then(function(data){
		    				$scope.formdata=data[0];
		    				modalPosition.show();
		    			});  
	    	      }
	    	      
	    	      $scope.domain="com.netgloo.models.LutPosition.";
	    	      $scope.submitForm = function($event){
	    	    	  if($scope.formdata.isactive==1){
	    	    		  $scope.formdata.isactive=true;
	    	    	  }else{
	    	    		  $scope.formdata.isactive=false;
	    	    	  }
	    	    	  if($scope.formdata.id==0){
	    	    		  mainService.withdata('POST', '/core/create/'+$scope.domain,  $scope.formdata).
	  	    				then(function(data){
	  	    					modalPosition.hide();
		  	    				$event.preventDefault();
		  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		  		   				$("#notificationSuccess").trigger('click');
		              			$(".k-grid").data("kendoGrid").dataSource.read(); 
	  	    				});
	    	    	  }else{
	    	    		  mainService.withdata('POST', '/core/update/'+$scope.domain,  $scope.formdata).
		    				then(function(data){
		    					modalPosition.hide();
		  	    				$event.preventDefault();
		  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		  		   				$("#notificationSuccess").trigger('click');
		              			$(".k-grid").data("kendoGrid").dataSource.read(); 
		    				});
	    	    	  }
	    	    	  
	    	      }
	    	      $scope.deleteO = function(id){
	      	    	 
	    				sweet.show({
	  			        	   title: 'Баталгаажуулалт',
	  	   		            text: 'Та энэ файлыг устгахдаа итгэлтэй байна уу?',
	  	   		            type: 'warning',
	  	   		            showCancelButton: true,
	  	   		            confirmButtonColor: '#DD6B55',
	  	   		            confirmButtonText: 'Тийм',
	  			    	    cancelButtonText: 'Үгүй',
	  	   		            closeOnConfirm: false,
	  	   		            closeOnCancel: false
	  			          
	  			        }, function(inputvalue) {
	  			        	 if (inputvalue) {
	  			        		 $scope.formdata = {};
	  			        		 $scope.formdata.id = id;
	  			        		 mainService.withdata('POST', '/core/delete/'+$scope.domain,  $scope.formdata).
	  				    			then(function(data){
	  				    				$("#notificationSuccess").trigger('click');
	  	                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
	  		 				   			sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
	  				    			});
	  	 		            }else{
	  	 		                sweet.show('Анхаар!', 'Устгах үйлдэл хийгдсэнгүй!!!', 'error');
	  	 		            }    		
	  			        });
	      	      }
	    	  
    	      if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.pmenuGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                            url: "/core/list/LutPosition",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        update: {
			                            url: "/core/update/"+$scope.domain+"",
			                            contentType:"application/json; charset=UTF-8",                                    
			                            type:"POST"
			                        },
			                        destroy: {
			                            url: "/core/delete/"+$scope.domain+"",
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
			                        	 fields: {   
			                        		 id: { editable: false,nullable: true},
			                        		 positionname: { type: "string", validation: { required: true } },
			                        		 orderid: { type: "number", validation: { required: true } },
			                        		 isactive: { type: "boolean", validation: { required: true } },
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
			                sortable: true,
			                columnMenu:true, 
			                resizable: true,
			                //toolbar: ["create"],
			                toolbar: kendo.template($("#addPosition").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									{ field: "positionname", title: "Нэр"+ "<span data-translate='Name'></span>"},
									{ field: "orderid", title: "Дараалал"+ "<span data-translate='Name'></span>"},
									{ field: "isactive", title: "Идвэхтэй эсэх"+ "<span data-translate='Name'></span>",values:isfl},
									{template: kendo.template($("#extend").html()), width: "200px"}
		                      ],
		          }
	    	  }
	    	  else {
	    		  function init(){
	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
	   		   			.then(function(data){
	   		   				console.log(data);
	   		   				if(data.rread==1){

	   		   				$scope.pmenuGrid = {
	   				                dataSource: {
	   				                   
	   				                    transport: {
	   				                    	read:  {
	   				                            url: "/core/list/LutPosition",
	   				                            contentType:"application/json; charset=UTF-8",                                    
	   				                            type:"POST"
	   				                        },
	   				                        update: {
	   				                            url: "/core/update/"+$scope.domain+"",
	   				                            contentType:"application/json; charset=UTF-8",                                    
	   				                            type:"POST"
	   				                        },
	   				                        destroy: {
	   				                            url: "/core/delete/"+$scope.domain+"",
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
	   				                        	 fields: {   
	   				                        		 id: { editable: false,nullable: true},
	   				                        		 positionname: { type: "string", validation: { required: true } },
	   				                        		 orderid: { type: "number", validation: { required: true } },
	   				                        		 isactive: { type: "boolean", validation: { required: true } },
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
	   				                sortable: true,
	   				                columnMenu:true, 
	   				                resizable: true,
	   				               // toolbar: ["create"],
	   				                pageable: {
	   				                    refresh: true,
	   				                    pageSizes: true,
	   				                    buttonCount: 5
	   				                },
	   				                columns: [
											{ field: "positionname", title: "Нэр"+ "<span data-translate='Name'></span>"},
											{ field: "orderid", title: "Дараалал"+ "<span data-translate='Name'></span>"},
											{ field: "isactive", title: "Идвэхтэй эсэх"+ "<span data-translate='Name'></span>",values:isfl},
	   			                      ],
	   			          }
	 	          		if(data.rcreate==1){	   		   					  
	 	   					if(data.rexport==1){
	 	   						$scope.pmenuGrid.toolbar= kendo.template($("#addorg").html());
	 	   					}
	 	   					else{
	 	   						$scope.pmenuGrid.toolbar= kendo.template($("#add").html());
	 	   					}
	 					}
	 					else if(data.rexport==1){
	 						$scope.pmenuGrid.toolbar= kendo.template($("#export").html());
	 					}
	 	          		if(data.rupdate==1 && data.rdelete==1){
	 	          			$scope.pmenuGrid.columns.push({ template: kendo.template($("#extend").html()), width: "250px"})
						}
						else if(data.rupdate==1 && data.rdelete==0){
							$scope.pmenuGrid.columns.push({ template: kendo.template($("#extendu").html()), width: "100px"}   )
						}
						else if(data.rupdate==0 && data.rdelete==1){
							$scope.pmenuGrid.columns.push({ template: kendo.template($("#extendd").html()), width: "100px"}   )
						}
	 				}
	 				else{
	 					$state.go('restricted.pages.error404');
	 				}			
	 		 });
	 	}
	    		  init();	
	    	  }; 
	    	  
	    	  

	          

        }]
    )
