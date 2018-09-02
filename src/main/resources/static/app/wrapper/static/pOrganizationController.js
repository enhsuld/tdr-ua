angular
    .module('altairApp')
    	.controller("orgNewCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'user_data',
    	                           'mainService',
    	                           'sweet',
	        function ($scope,$rootScope,$state,$timeout,user_data,mainService,sweet) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutDepartment.";
	    	  var isfl=[{"text":"Тийм","value":1 },{"text":"Үгүй","value": 0}];
	    	  
	    	 // var $formValidate = $('#form_crit_dir');
	    		
	    	     var $formValidate = $('#form_crit_dir');

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
    	    	  $scope.formdata.isstate=0;
    	    	  modalUpdate.show();
    	      }
    	      
    	      $scope.edit = function(id){
    	    	  mainService.withdomain('get', '/my/resource/LutDepartment/'+id).
	    			then(function(data){
	    				$scope.formdata=data[0];
	    				modalUpdate.show();
	    			});  
    	      }
    	      
    	      $scope.domain="com.netgloo.models.LutDepartment.";
    	      $scope.submitForm = function($event){
    	    	  if($scope.formdata.id==0){  		  
    	    		  
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
 			        	   $scope.formdata.isstate=1;
 		  	    		  mainService.withdata('POST', '/core/create/'+$scope.domain,  $scope.formdata).
 			    				then(function(data){
 			  	    				modalUpdate.hide();
 			  	    				$event.preventDefault();
 			  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
 			  		   				$("#notificationSuccess").trigger('click');
 			              			$(".k-grid").data("kendoGrid").dataSource.read(); 
 			    				});
 			        	}
 			        	else{
 			        		sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
 			        	} 			        
 			    
 			       });  	    		  
    	    	 
    	    	  }
    	    	  else{
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
			          
			        }, 
			        function(inputvalue) {
			        	if(inputvalue){
    	    		  $scope.formdata.isstate=1;
    	    		  mainService.withdata('POST', '/core/update/'+$scope.domain,  $scope.formdata).
	    				then(function(data){
	  	    				modalUpdate.hide();
	  	    				$event.preventDefault();
	  		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
	  		   				$("#notificationSuccess").trigger('click');
	              			$(".k-grid").data("kendoGrid").dataSource.read(); 
	    				});
			        	}
			        	else{
			        		sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
			        	}
    	    	  });
			        
			        
    	      }
    	      }
    	      $scope.deleteO = function(id){
     	    	 
    				sweet.show({
  			        	   title: 'Баталгаажуулалт',
  	   		            text: 'Та энэ байгууллагыг устгахдаа итгэлтэй байна уу?',
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
  		 				   			sweet.show('Анхаар!', 'Байгууллага амжилттай устлаа.', 'success');
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
	    		  $scope.pmenuGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                            url: "/core/list/LutDepartment",
			                            contentType:"application/json; charset=UTF-8",   
			                            data:{"custom":"where isstate=1"},
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
			                        		 address: { type: "string", validation: { required: true } },
			                        		 phone: { type: "string",validation: { required: true } },
			                        		 reg: { type: "string",validation: { required: true } },
			                        		 licnum: { type: "string" },
			                        		 licexpiredate: { type: "string"},
			                        		 departmentname: { type: "string", validation: { required: true} },
			                        		 shortname: { type: "string", validation: { required: true } },
			                        		 isactive: { type: "number" },
			                        		 isstate: { type: "number" },
			                        		 email: { type: "string", validation: { required: true} },
			                        		 web: { type: "string", validation: { required: true} }
			                        		 
			                             }
			                         }
			                     },
			                    pageSize: 8,
			                    serverPaging: true,
			                    serverFiltering: true,
			                    serverSorting: true
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
			                columnMenu:true, 
			                resizable: true,
			                toolbar: kendo.template($("#addorg").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "departmentname", title: "Нэр"+ "<span data-translate=''></span>" ,width: 250 },
									{ field: "shortname", title: "Товчилсон нэр"+ "<span data-translate=''></span>" ,width: 150 },
									{ field: "reg",title: "УБ-н дугаар" +"<span data-translate=''></span>", width:150},
									{ field: "licnum",title: "ТЗ-н дугаар" +"<span data-translate=''></span>", width:150},
									{ field: "licexpiredate",title: "ТЗ-н хүчинтэй хугацаа" +"<span data-translate=''></span>", width:150},
									{ field: "address", title: "Хаяг" +"<span data-translate=''></span>", width:250},
									{ field: "phone", title: "Утас" +"<span data-translate=''></span>" ,width: 150},
									{ field: "email", title: "Цахим шуудан" +"<span data-translate=''></span>",width: 150},
									{ field: "web", title: "Веб хуудас" +"<span data-translate=''></span>",width: 150},								
									{ field: "isactive", title: "Идэвхтэй эсэх" +"<span data-translate=''></span>",width: 150,values:isfl},
									{ field: "isstate", title: "Төрийн эсэх" +"<span data-translate=''></span>",width: 150,values:isfl},
									{template: kendo.template($("#extend").html()), width: "100px"}
		                      ],
		                      dataBound: function () {
		   	   		                var rows = this.items();
		   	   		                  $(rows).each(function () {
		   	   		                      var index = $(this).index() + 1 
		   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
		   	   		                      var rowLabel = $(this).find(".row-number");
		   	   		                      $(rowLabel).html(index);
		   	   		                  });
		   	   		  	           },
		                     // editable: "popup"
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
	   				                            url: "/core/list/LutDepartment",
	   				                            contentType:"application/json; charset=UTF-8",      
	   				                            data:{"custom":"where isstate=1"},
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
	   				                        		 address: { type: "string", validation: { required: true } },
	   				                        		 phone: { type: "string",validation: { required: true } },
	   				                        		 reg: { type: "string",validation: { required: true } },
	   				                        		 licnum: { type: "string" },
	   				                        		 licexpiredate: { type: "string"},
	   				                        		 departmentname: { type: "string", validation: { required: true} },
	   				                        		 shortname: { type: "string", validation: { required: true } },
	   				                        		 isactive: { type: "number" },
	   				                        		 isstate: { type: "number" },
	   				                        		 email: { type: "string", validation: { required: true} },
	   				                        		 web: { type: "string", validation: { required: true} }
	   				                        		 
	   				                             }
	   				                         }
	   				                     },
	   				                    pageSize: 8,
	   				                    serverPaging: true,
	   				                    serverFiltering: true,
	   				                    serverSorting: true
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
	   				                columnMenu:true, 
	   				                resizable: true,
	   				                //toolbar: ["create"],
	   				                pageable: {
	   				                    refresh: true,
	   				                    pageSizes: true,
	   				                    buttonCount: 5
	   				                },
	   				                columns: [
	   				                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
											{ field: "departmentname", title: "Нэр"+ "<span data-translate=''></span>" ,width: 250 },
											{ field: "shortname", title: "Товчилсон нэр"+ "<span data-translate=''></span>" ,width: 150 },
											{ field: "reg",title: "УБ-н дугаар" +"<span data-translate=''></span>", width:150},
											{ field: "licnum",title: "ТЗ-н дугаар" +"<span data-translate=''></span>", width:150},
											{ field: "licexpiredate",title: "ТЗ-н хүчинтэй хугацаа" +"<span data-translate=''></span>", width:150},
											{ field: "address", title: "Хаяг" +"<span data-translate=''></span>", width:250},
											{ field: "phone", title: "Утас" +"<span data-translate=''></span>" ,width: 150},
											{ field: "email", title: "Цахим шуудан" +"<span data-translate=''></span>",width: 150},
											{ field: "web", title: "Веб хуудас" +"<span data-translate=''></span>",width: 150},								
											{ field: "isactive", title: "Идэвхтэй эсэх" +"<span data-translate=''></span>",width: 150,values:isfl},
											{ field: "isstate", title: "Төрийн эсэх" +"<span data-translate=''></span>",width: 150,values:isfl},
	   										//{ command: ["edit", "destroy"], title: "&nbsp;", width: "235px" }
	   			                      ],
	   			                   dataBound: function () {
			   	   		                var rows = this.items();
			   	   		                  $(rows).each(function () {
			   	   		                      var index = $(this).index() + 1 
			   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
			   	   		                      var rowLabel = $(this).find(".row-number");
			   	   		                      $(rowLabel).html(index);
			   	   		                  });
			   	   		  	           },
	   			                      //editable: "popup"
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
	 	          			$scope.pmenuGrid.columns.push({ template: kendo.template($("#extend").html()), width: "100px"})
						}
						else if(data.rupdate==1 && data.rdelete==0){
							$scope.pmenuGrid.columns.push({ template: kendo.template($("#extendu").html()), width: "100px"}   )
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

