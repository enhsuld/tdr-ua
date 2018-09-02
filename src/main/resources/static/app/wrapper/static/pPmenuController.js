angular
    .module('altairApp')
    	.controller("menuCtrl",['$scope','p_menu','user_data','mainService','$state','sweet',
	        function ($scope,p_menu,user_data,mainService,$state,sweet) {       	
	    		var aj=p_menu;
	    		var init={"text":"ROOT","value":"0"};	    	
				aj.push(init);
				
				console.log(user_data);
	        	$scope.domain="com.netgloo.models.LutMenu.";
	        	
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
	    	    	  $scope.formdata.parentid=0;
	    	    	  modalUpdate.show();
	    	      }
	    	      
	    	      $scope.edit = function(id){
	    	    	  mainService.withdomain('get', '/my/resource/LutMenu/'+id).
		    			then(function(data){
		    				$scope.formdata=data[0];
		    				modalUpdate.show();
		    			});  
	    	      }
	    	      
	    	      $scope.submitForm = function($event){
	    	    	  
	    	    	  if($scope.formdata.id==0){
	    	    		  mainService.withdata('POST', '/core/create/'+$scope.domain,  $scope.formdata).
	  	    				then(function(data){
		  	    				modalUpdate.hide();
		  	    				$event.preventDefault();
		  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		  		   				$("#notificationSuccess").trigger('click');
		              			$(".k-grid").data("kendoGrid").dataSource.read(); 
	  	    				});
	    	    	  }else{
	    	    		  mainService.withdata('POST', '/core/update/'+$scope.domain,  $scope.formdata).
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
	    	      
	    	      var isfile_data = $scope.selectize_isfile_options = aj;
	     			
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
	        		
		        	
		            $scope.pmenuGrid = {
		                dataSource: {
		                   
		                    transport: {
		                    	read:  {
		                            url: "/core/list/LutMenu",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST"
		                        },
		                        update: {
		                            url: "/core/update/"+$scope.domain+"",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST",
		                            complete: function(e) {
		                            	$(".k-grid").data("kendoGrid").dataSource.read(); 
		                    		}
		                        },
		                        destroy: {
		                            url: "/core/delete/"+$scope.domain+"",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST"
		                        },
		                        create: {
		                        	url: "/core/create/"+$scope.domain+"",
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
		                             	id: { editable: false,nullable: true},
		                             	menuname: { type: "string", validation: { required: true } },
		                             	stateurl: { type: "string", defaultValue:'#'},
		                                 uicon: { type: "string"},
		                                 parentid: { type: "number",defaultValue:0},
		                                 orderid: { type: "number" }
		                             }
		                         }
		                     },
		                    pageSize: 10,
		                    serverFiltering: true,
		                    serverPaging: true,
		                    serverSorting: true
		                },
		                //toolbar: ["create"],
		                toolbar: kendo.template($("#addorg").html()),
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
		                          { field:"menuname", title: "Нэр /Mn/" },
		                          { field: "stateurl", title:"URL" },
		                          { field: "uicon", title:"IKON"},
		                          { field: "parentid", values: aj, title:"Эцэг цэс"},
		                          { field: "orderid", title:"Дараалал", width: "200px" },
		                          {template: kendo.template($("#extend").html()), width: "200px"}],
		                      
		            };
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
		   		 	                            url: "/core/list/LutMenu",
		   		 	                            contentType:"application/json; charset=UTF-8",                                    
		   		 	                            type:"POST"
		   		 	                        },
		   		 	                        update: {
		   		 	                            url: "/core/update/"+$scope.domain+"",
		   		 	                            contentType:"application/json; charset=UTF-8",                                    
		   		 	                            type:"POST",
		   		 	                            complete: function(e) {
		   		 	                            	$(".k-grid").data("kendoGrid").dataSource.read(); 
		   		 	                    		}
		   		 	                        },
		   		 	                        destroy: {
		   		 	                            url: "/core/delete/"+$scope.domain+"",
		   		 	                            contentType:"application/json; charset=UTF-8",                                    
		   		 	                            type:"POST"
		   		 	                        },
		   		 	                        create: {
		   		 	                        	url: "/core/create/"+$scope.domain+"",
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
		   		 	                             	id: { editable: false,nullable: true},
		   		 	                             	menuname: { type: "string", validation: { required: true } },
		   		 	                             	stateurl: { type: "string", defaultValue:'#'},
		   		 	                                 uicon: { type: "string"},
		   		 	                                 parentid: { type: "number",defaultValue:0},
		   		 	                                 orderid: { type: "number" }
		   		 	                             }
		   		 	                         }
		   		 	                     },
		   		 	                    pageSize: 10,
		   		 	                    serverFiltering: true,
		   		 	                    serverPaging: true,
		   		 	                    serverSorting: true
		   		 	                },
		   		 	                //toolbar: ["create"],
			   		 	            filterable:{
		   			                	 mode: "row"
		   			                },
		   		 	                sortable: true,
		   		 	                columnMenu:true, 
		   		 	                resizable: true,
		   		 	                pageable: {
		   		 	                    refresh: true,
		   		 	                    pageSizes: true,
		   		 	                    buttonCount: 5
		   		 	                },
		   		 	                columns: [
		   		 	                          { field:"menuname", title: "Нэр /Mn/" },
		   		 	                          { field: "stateurl", title:"URL" },
		   		 	                          { field: "uicon", title:"IKON"},
		   		 	                          { field: "parentid", values: aj, title:"Эцэг цэс"},
		   		 	                          { field: "orderid", title:"Дараалал", width: "200px" },
		   		 	                          //{ command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }
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
