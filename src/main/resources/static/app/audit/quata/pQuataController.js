angular
    .module('altairApp')
    	.controller("quataCtrl",['$scope','user_data','p_year','p_cat','decision','mainService','sweet','$state',
	        function ($scope,user_data,p_year,p_cat,decision,mainService,sweet,$state) {       	
    		
    		
    			var modal = UIkit.modal("#modal_update");
    		
    			$scope.role=user_data[0].role;
    			
    			$scope.selectize_a_data = {
                    options: []
                };
    	    	
    	    	$scope.selectize_b_data = {
                        options: []
                    };

    	    	$scope.selectize_a_data.options=p_year;
    	    	
    	    	$scope.selectize_b_data.options=p_cat;
    	    	
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
    			$scope.domain="com.netgloo.models.PreAuditRegistration.";
    			
    			
    			 var $formValidate = $('#quataform');

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
    			
	            $scope.loading=true;
	            
	            $scope.quata={
	            		qtype:"СТА"
	            }
	            
	            $scope.submitForm=function(){
	            	         	
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
			        		$scope.loading=false;
			         	    mainService.withdata('put','/au/create/quata', $scope.quata)
			  		   			.then(function(data){
			  		   				if(data){
			  		   					modal.hide();
			  		   					$(".k-grid").data("kendoGrid").dataSource.read(); 
			  			   				sweet.show('Мэдээлэл', 'Амжилттай илгээлээ.', 'success');
			  			   				$scope.loading=true;
			  		   				}
			  		   				else{
				  		   				modal.hide();
			  			   				sweet.show('Анхаар', 'Алдаа...', 'error');
			  			   				$scope.loading=true;
			  		   				}
			  			   				
			  	   			});	
			            	
			        	}
			        	else{
			        		sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
			        	} 	    
			    
			       }); 
	            	
	            }
	            
	            
	            $scope.update=function(item){	  
	            	$state.go('restricted.pages.swap',{id:item.id});
	            }
	            
	            
	            function init(){
 	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
 	   		   			.then(function(data){
 	   		   				if(data.rread==1){		
		   	   		   			$scope.puserGrid = {
		   	   		                dataSource: {		   	   		                   
		   	   		                    transport: {
		   	   		                    	read:  {
		   	   		                            url: "/au/list/PreAuditRegistration",
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
		   	   		                             	id: { type: "number", editable: false,nullable: false},
		   	   		                             	departmentid: { type: "number",  validation: { required: true } },	                             	
		   	   		                             	email: { type: "string"},
		   	   		                             	positionid: { type: "number"},
		   	   		                             	roleid: { type: "number"},
		   	   		                             	givenname: { type: "string"},
		   	   		                             	familyname: { type: "string"},
		   	   		                             	mobile: { type: "string"},	                                          	
		   	   		                            	username: { type: "string", validation: { required: true} },
		   	   		                            	password: { type: "string", validation: { required: true} },	                            
		   	   		                            	isactive: { type: "boolean" }
		   	   		                             }	                    
		   	   		                         }
		   	   		                     },
		   	   		                    pageSize: 5,
		   	   		                    serverPaging: true,
		   	   		                    serverSorting: true,
		   	   		                    serverFiltering: true
		   	   		                },
		   	   		                //toolbar: kendo.template($("#add").html()),
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
		   	   		                columnMenu:true, 
		   	   		                pageable: {
		   	   		                    refresh: true,
		   	   		                    pageSizes: true,
		   	   		                    buttonCount: 5
		   	   		                },
		   	   		                columns: [
		   	   		                		  {title: "#",template: "<span class='row-number'></span>", width:"60px"},
		   	   		                          { field:"audityear", title: "Тайлангийн он", width: 150 },
		   	   		                          { field:"orgtype", title: "Ангилал",values:p_cat, width: 150 },
		   	   		                          { field:"orgname", title: "Байгууллага"},
		   	   		                          { field:"total", title: "Нийт тоо" ,width: 150},     
		   	   		                          { field:"stepid", values:decision, title: "Төлөв",width: 150},
		   	   		                          { 
		   	   		                          	 template: kendo.template($("#update").html()),  width: "240px" 
		   	   		                          }],
			   	   		                    dataBound: function () {
				   	   		                var rows = this.items();
				   	   		                  $(rows).each(function () {
				   	   		                      var index = $(this).index() + 1 
				   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
				   	   		                      var rowLabel = $(this).find(".row-number");
				   	   		                      $(rowLabel).html(index);
				   	   		                  });
				   	   		  	           },
		   	   		                       editable: "popup"
		   	   		            }; 	   		   
		   	   		   	if($scope.role=='ROLE_FIRST'){
		    				$scope.puserGrid.toolbar=kendo.template($("#add").html());
		    			}
			        	else{
			        		$scope.puserGrid.toolbar=["excel","pdf"];
			        	}
		 				}
		 				else{
							$state.go('restricted.pages.error404');
		 				}			
		   	 		 });
		   	 	}
 	    		init();	
	        }
    ]);
