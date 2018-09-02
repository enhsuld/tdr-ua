angular
    .module('altairApp')
    	.controller("auditYearCtrl",['$scope','user_data','mainService','$state','sweet',
	        function ($scope,user_data,mainService,$state,sweet) {       	
    
    			$scope.domain="com.netgloo.models.LutAuditYear.";
    			var isfl=[{"text":"Тийм","value":true },{"text":"Үгүй","value": false}];
    			var $formValidate = $('#form_val');
	    		
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
    			
	    		var modalUpdate = UIkit.modal("#modal_update");
	    	      $scope.isupdate = 0;
	    	      $scope.org = function(){
	    	    	  $scope.formdata={};
	    	    	  $scope.formdata.id=0;
	    	    	  $scope.formdata.isactive=0;
	    	    	  modalUpdate.show();
	    	      }
	    	      
	    	      $scope.edit = function(id){
	    	    	  mainService.withdomain('get', '/my/resource/LutAuditYear/'+id).
		    			then(function(data){
		    				$scope.formdata=data[0];
		    				modalUpdate.show();
		    			});  
	    	      }
	    	      
	    	      $scope.submitForm = function($event){
	    	    	  if($scope.formdata.isactive==1){
	    	    		  $scope.formdata.isactive=true;
	    	    	  }else{
	    	    		  $scope.formdata.isactive=false;
	    	    	  }
	    	    	  if($scope.formdata.id==0){
	    	    		  mainService.withdata('POST', '/au/create/'+$scope.domain,  $scope.formdata).
	  	    				then(function(data){
		  	    				modalUpdate.hide();
		  	    				$event.preventDefault();
		  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		  		   				$("#notificationSuccess").trigger('click');
		              			$(".k-grid").data("kendoGrid").dataSource.read(); 
	  	    				});
	    	    	  }else{
	    	    		  mainService.withdata('POST', '/au/update/'+$scope.domain,  $scope.formdata).
		    				then(function(data){
		  	    				modalUpdate.hide();
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
	  			        		 mainService.withdata('POST', '/au/delete/'+$scope.domain,  $scope.formdata).
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
	    		
	     			if(user_data[0].role=="ROLE_SUPER"){
    				  $scope.puserGrid = {
    			                dataSource: {
    			                   
    			                    transport: {
    			                    	read:  {
    			                            url: "/au/list/LutAuditYear",
    			                            contentType:"application/json; charset=UTF-8",                                    
    			                            type:"POST"
    			                        },
    			                        update: {
    			                            url: "/au/update/"+$scope.domain+"",
    			                            contentType:"application/json; charset=UTF-8",                                    
    			                            type:"POST"
    			                        },
    			                        destroy: {
    			                            url: "/au/delete/"+$scope.domain+"",
    			                            contentType:"application/json; charset=UTF-8",                                    
    			                            type:"POST",
    			                            complete: function(e) {
    			                            	 $("#notificationDestroy").trigger('click');
    			                    		}
    			                        },
    			                        create: {
    			                            url: "/au/create/"+$scope.domain+"",
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
    			                             	id: { type: "number", editable: false,nullable: false},                                       	
    			                             	audityear: { type: "number", validation: { required: true} },                            
    			                            	isactive: { type: "boolean" }
    			                             }	                    
    			                         }
    			                     },
    			                    pageSize: 8,
    			                    serverPaging: true,
    			                    serverSorting: true,
    			                    serverFiltering: true
    			                },
    			                //toolbar: ["create"],
    			                toolbar: kendo.template($("#addorg").html()),
    			                filterable:{
	   			                	 mode: "row"
	   			                },
    			                sortable: true,
    			                resizable: true,
    			                columnMenu:true, 
    			                pageable: {
    			                    refresh: true,
    			                    pageSizes: true,
    			                    buttonCount: 5
    			                },
    			                columns: [
    			                          { field:"audityear", title: "Огноо"},
    			                          { field:"isactive", title: "Идэвхитэй эсэх",values:isfl},
    			                          {template: kendo.template($("#extend").html()), width: "200px"}],
    			            };
     	    	  }
     	    	  else {
     	    		  function init(){
     	   	    		 mainService.withdomain('get','/core/rjson/'+userdata1[0].id+'/'+$state.current.name+'.')
     	   		   			.then(function(data){
     	   		   				console.log(data);
     	   		   				if(data.rread==1){

     	   		   			  $scope.puserGrid = {
     	   			                dataSource: {
     	   			                   
     	   			                    transport: {
     	   			                    	read:  {
     	   			                            url: "/au/list/LutAuditYear",
     	   			                            contentType:"application/json; charset=UTF-8",                                    
     	   			                            type:"POST"
     	   			                        },
     	   			                        update: {
     	   			                            url: "/au/update/"+$scope.domain+"",
     	   			                            contentType:"application/json; charset=UTF-8",                                    
     	   			                            type:"POST"
     	   			                        },
     	   			                        destroy: {
     	   			                            url: "/au/delete/"+$scope.domain+"",
     	   			                            contentType:"application/json; charset=UTF-8",                                    
     	   			                            type:"POST",
     	   			                            complete: function(e) {
     	   			                            	 $("#notificationDestroy").trigger('click');
     	   			                    		}
     	   			                        },
     	   			                        create: {
     	   			                            url: "/au/create/"+$scope.domain+"",
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
     	   			                             	id: { type: "number", editable: false,nullable: false},                                       	
     	   			                             	audityear: { type: "number", validation: { required: true} },                            
     	   			                            	isactive: { type: "boolean" }
     	   			                             }	                    
     	   			                         }
     	   			                     },
     	   			                    pageSize: 8,
     	   			                    serverPaging: true,
     	   			                    serverSorting: true,
     	   			                    serverFiltering: true
     	   			                },
     	   			                //toolbar: ["create"],
	     	   			            filterable:{
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
     	   			                          { field:"audityear", title: "Огноо"},
     	   			                          { field:"isactive", title: "Идэвхитэй эсэх",values:isfl},
     	   			                          ],
     	   			            };
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
	        }
    ]);
