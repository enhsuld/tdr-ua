angular
    .module('altairApp')
    	.controller("swapCtrl",['$rootScope','$scope','user_data','reason','quata','mainService','sweet','$state','$stateParams','breason',
	        function ($rootScope,$scope,user_data,reason,quata,mainService,sweet,$state,$stateParams,breason) {       	
    		
    		
    			console.log($rootScope.user);
    			
    			$scope.breason=breason;
    			console.log($scope.breason);
    			
    			$scope.role=user_data.authorities[0].authority;
    			$scope.quata=quata[0];
    			
    		
    		
    			var modal = UIkit.modal("#modal_update");
    	    	
    			
    			
    			$scope.selectize_a_data = {
                    options: []
                };
    	    	
    	    	$scope.selectize_b_data = {
                        options: []
                    };

    	    	
    	    	$scope.selectize_b_data.options=reason;
    	    	
                $scope.selectize_a_config = {
                    plugins: {
                        'disable_options': {
                            disableOptions: ["c1","c2"]
                        }
                    },
                    create: false,
                    maxItems: 1,
                    placeholder: 'Сонголт',
                    optgroupField: 'parent_id',
                    optgroupLabelField: 'title',
                    optgroupValueField: 'ogid',
                    valueField: 'value',
                    labelField: 'text',
                    searchField: 'text'
                };
    			$scope.domain="com.netgloo.models.LnkAuditOrganization.";
    			
    			
	            $scope.loading=true;
	            
	            $scope.submitForm=function(){
	            	$scope.loading=false;
	         	    mainService.withdata('put','/au/create/quata', $scope.quata)
	  		   			.then(function(data){
	  		   				if(data){
	  		   					modal.hide();
	  			   				sweet.show('Мэдээлэл', 'Амжилттай илгээлээ.', 'success');
	  			   			    $state.go('restricted.pages.claim');	
	  		   				}
	  		   				else{
		  		   				modal.hide();
	  			   				sweet.show('Анхаар', 'Error...', 'error');
	  		   				}
	  	   			});	
	            }	            
	            
	            $scope.loadOnSend=function(){	            	
	            	sweet.show({
			        	   title: 'Баталгаажуулалт',
	   		            text: 'Үйлдэлийг үргэлжлүүлэх үү?',
	   		            type: 'warning',
	   		            showCancelButton: true,
	   		            confirmButtonColor: '#DD6B55',
	   		            confirmButtonText: 'Тийм',
			    	    cancelButtonText: 'Үгүй',
	   		            closeOnConfirm: false,
	   		            closeOnCancel: false
			          
			        },  function(inputvalue) {
			        	if(inputvalue){
			        		 mainService.withdata('put','/au/update/quata/'+$stateParams.id)
			  		   			.then(function(data){
			  		   				if(data){
			  		   					modal.hide();
			  			   				sweet.show('Мэдээлэл', 'Амжилттай илгээлээ.', 'success');
			  			   			    $state.go('restricted.pages.quata');	
			  		   				}
			  		   				else{
				  		   				modal.hide();
			  			   				sweet.show('Анхаар', 'Алдаа...', 'error');
			  		   				}
			  	   			});
			        	}
			        	else{
			        		sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
			        	} 			        
			    
			       });       	  
	            }
	            $scope.back= "";
	            $scope.loadOnBack=function(){
	            	
	            	sweet.show({
			        	   title: 'Баталгаажуулалт',
	   		            text: 'Үйлдэлийг үргэлжлүүлэх үү?',
	   		            type: 'warning',
	   		            showCancelButton: true,
	   		            confirmButtonColor: '#DD6B55',
	   		            confirmButtonText: 'Тийм',
			    	    cancelButtonText: 'Үгүй',
	   		            closeOnConfirm: false,
	   		            closeOnCancel: false
			          
			        },  function(inputvalue) {
			        	if(inputvalue){
			            	   mainService.withdata('put','/au/back/quata/'+$stateParams.id, $scope.back)
			  		   			.then(function(data){
			  		   				if(data){
			  		   					modal.hide();
			  			   				sweet.show('Мэдээлэл', 'Амжилттай буцаагдлаа.', 'success');
			  			   			    $state.go('restricted.pages.quata');	
			  		   				}
			  		   				else{
				  		   				modal.hide();
			  			   				sweet.show('Анхаар', 'Алдаа...', 'error');
			  		   				}
			  	   			});
			        	}
			        	else{
			        		sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
			        	} 	    
			    
			       }); 	            	            	            	
	            }
	            
	            $scope.selectize_a_data = {
                    options: [
                        {
                            id: 1,
                            title: "Шилжүүлэх",
                            value: "1",
                            parent_id: 1
                        },
                        {
                            id: 2,
                            title: "Устгах",
                            value: "2",
                            parent_id: 1
                        }	               
                    ]
                };
	            
	            $scope.selectize_b_config = {
                    plugins: {
                        'disable_options': {
                            disableOptions: ["c1","c2"]
                        }
                    },
                    create: false,
                    maxItems: 1,
                    placeholder: 'Сонголт',
                    optgroupField: 'parent_id',
                    optgroupLabelField: 'title',
                    optgroupValueField: 'ogid',
                    valueField: 'value',
                    labelField: 'text',
                    searchField: 'text'
                };

                $scope.selectize_a_config = {
                    plugins: {
                        'disable_options': {
                            disableOptions: ["c1","c2"]
                        }
                    },
                    create: false,
                    maxItems: 1,
                    placeholder: 'Сонголт',
                    optgroupField: 'parent_id',
                    optgroupLabelField: 'title',
                    optgroupValueField: 'ogid',
                    valueField: 'value',
                    labelField: 'title',
                    searchField: 'title'
                };
                
                var $formValidate = $('#form_validation_config');

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
                var $formValidate = $('#form_validation_config_new');

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
                
                $scope.conf={
                		id:0,
                		division:0,
                		decision:0,
                		description:'',
                		olddata:{}
                }
                var modalUpdateConf = UIkit.modal("#modal_config");
            	$scope.submitForm = function($event){
            		console.log($scope.conf);
            		mainService.withdata('put', '/au/conf/quata',  $scope.conf).
    				then(function(data){
  	    				modalUpdateConf.hide();
  	    				$event.preventDefault();
  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
              			$(".k-grid").data("kendoGrid").dataSource.read(); 
    				});
    	       }
            	
                $scope.nw={
                		id:$stateParams.id,
                		orgid:0,
                		division:0,
                		description:''                		
                }
            	
                var modalUpdateConfNew = UIkit.modal("#modal_config_new");
            	$scope.submitFormNew = function($event){
            		console.log($scope.nw);
            		mainService.withdata('put', '/au/conf/new/quata',  $scope.nw).
    				then(function(data){
    					modalUpdateConfNew.hide();
  	    				$event.preventDefault();
  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
              			$(".k-grid").data("kendoGrid").dataSource.read(); 
    				});
    	       }
	            
            
            	
	            $scope.config = function(item){
	            	$scope.conf.olddata=item;
	            	$scope.conf.id=item.id;
	            	$scope.conf.description='';
	     	    	$scope.item=item; 
    			/*	sweet.show({
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
  			        });*/
      	      }
	            
	            $scope.update=function(item){	 
	            	$state.go('restricted.pages.swap',{id:item.id});
	            }
	            $scope.selectize_new_data = {
	                    options: []
	                };
	            $scope.addNew= function(){
	            	console.log($stateParams.id);
	            	 mainService.withdomain('get', '/au/resource/PreAuditRegistration/'+$stateParams.id).
		    			then(function(data){
		    				$scope.formdata=data[0];
		    				$scope.selectize_new_data.options=data;
		    				modalUpdateConfNew.show();
		    			});  
	            }
	            
	            $scope.puserGrid = {
	                dataSource: {
	                   
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkAuditOrganization",
	                            contentType:"application/json; charset=UTF-8",  
	                            data:{"custom":"where quataid="+$stateParams.id+""},
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
	                             	id: { type: "number", editable: false,nullable: false},
	                             	ayear: { type: "number",editable: false},	                             	
	                             	orgcode: { type: "string", editable: false},
	                             	orgname: { type: "string",editable: true},
	                             	reasonid: { type: "number"},
	                             	quataid: { type: "number"},
	                             	preid: { type: "number"},
	                             }	                    
	                         }
	                     },
	                     autoSync: true,
	                    pageSize: 10,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                excel: {
	                    fileName: "Export.xlsx",
	                    allPages: true,
	                    filterable: true
	                },
	                pdf: {
	                    allPages: true,
	                    avoidLinks: true,
	                    paperSize: "A4",
	                    margin: { top: "2cm", left: "1cm", right: "1cm", bottom: "1cm" },
	                    landscape: true,
	                    repeatHeaders: true,
	                    template: $("#page-template").html(),
	                    scale: 0.8
	                },
	                filterable: {
                   	 	mode: "row"
                    },
	                sortable: true,
	                batch: true,
	                resizable: true,
	                pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },
	                columns: [
	                	      {title: "#",template: "<span class='row-number'></span>", width:"60px"},
	                          { field:"orgcode", title: "Байгууллагын код",width: 200 },
	                          { field:"orgname", title: "Байгууллагын нэр"},
	                          { field:"reasonid", values:reason, title: "Шийдвэрлэлт",width: 200  }],
	                          dataBound: function () {
		   	   		                var rows = this.items();
		   	   		                  $(rows).each(function () {
		   	   		                      var index = $(this).index() + 1 
		   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
		   	   		                      var rowLabel = $(this).find(".row-number");
		   	   		                      $(rowLabel).html(index);
		   	   		                  });
		   	   		  	           },
	                      editable: false
	            };
	            
	          
	          
	        	if($scope.quata.step==1 && $scope.role=='ROLE_FIRST' || $scope.quata.step==5 && $scope.role=='ROLE_FIRST'){
    				 $scope.manager=true;
    				 $scope.puserGrid.editable=true;
    				 $scope.puserGrid.columns.push({ template: kendo.template($("#extendd").html()), width: "140px"});
    				 $scope.puserGrid.toolbar= kendo.template($("#export").html());
    			}
    			if($scope.quata.step==5 && $scope.role=='ROLE_FIRST'){
    				$scope.backed=true;
    				$scope.manager=true;
    			}
    			else if($scope.quata.step==2 && $scope.role=='ROLE_SECOND'){    				
    				$scope.terguuleh=true;
    				 $scope.puserGrid.editable=false;
    			}    			
    			else if($scope.quata.step==3 && $scope.role=='ROLE_ALL'){
    				$scope.all=true;
    			}
	            
    			if($scope.role=='ROLE_SECOND' && $scope.quata.step==4){
    				 $scope.puserGrid.toolbar= kendo.template($("#export").html());
    				 $scope.puserGrid.columns.push({ template: kendo.template($("#extendd").html()), width: "140px"});
    			} 
    			
    			if($scope.role=='ROLE_FIRST' && $scope.quata.step==4){
	   				 $scope.puserGrid.toolbar= kendo.template($("#export").html());
	   				 $scope.puserGrid.columns.push({ template: kendo.template($("#extendd").html()), width: "140px"});
	   			} 
    			else{
    				//$scope.puserGrid.toolbar=["excel","pdf"];
    			}
    			
	        	/*if($scope.role=='ROLE_FIRST' && $scope.quata.step==1 || $scope.role=='ROLE_FIRST' && $scope.quata.step==4){
    				$scope.puserGrid.toolbar=["excel","pdf"];
    			}*/
            
	        }
    ]);
