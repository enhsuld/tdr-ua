angular
    .module('altairApp')
    	.controller("validationListCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
    	                           'LutAuditDir',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data,LutAuditDir) { 
    	        $scope.auditDirList = LutAuditDir;
    	        
    	        $scope.risks = [];
    	        $scope.tryouts = [];
    	        $scope.factors = [];
    	        
    	        $scope.riskdir_config = {
	                create: false,
	                maxItems: 1,
	                placeholder: 'Сонгох...',
	                valueField: 'value',
	                labelField: 'text',
	            };
    	        
    	        $scope.loadRisks = function(v){
    	        	$scope.risks = [];
    	        	$scope.tryouts = [];
    	        	if (v != undefined){
    	        		mainService.withdomain("GET", "/au/withid/LnkRiskdir/"+v).then(function(data){
        	        		$scope.risks = data;
        	        	});
    	        	}
    	        	
    	        }
    	        
    	        $scope.loadTryouts = function(v){
    	        	$scope.tryouts = [];
    	        	if (v != undefined){
	    	        	mainService.withdomain("GET", "/au/withid/LnkTryoutRisk/"+v).then(function(data){
	    	        		$scope.tryouts = data;
	    	        	});
    	        	}
    	        }
    	        
    	        $scope.loadFactors = function(formdata){
    	        	console.log(formdata);
    	        	if (formdata.adirid && formdata.riskid && formdata.tryoutid){
    	        		mainService.withdata("POST", "/au/lutfactors",formdata).then(function(data){
	    	        		$scope.factors = data;
	    	        	});
    	        	}
    	        }
    	        
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
    	        	var sel=[{"text":"Үгүй","value":"0"},{"text":"Тийм","value":"1"}];
    	        	var source_data = $scope.selectize_source_options = sel;
    	        	$scope.selectize_source_config = {
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
    	   	                    option: function(source_data, escape) {
    	   	                        return  '<div class="option">' +
    	   	                            '<span class="title">' + escape(source_data.text) + '</span>' +
    	   	                            '</div>';
    	   	                    }
    	   	                }
    	   	            };
    	        	var val=[{"text":"ROOT","value":"0"},
    	        	         {"text":"Санхүүгийн байдлын тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"1"},
    	        	         {"text":"Санхүүгийн үр дүнгийн тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"2"},
    	        	         {"text":"Мөнгөн гүйлгээний тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"3"},
    	        	         {"text":"Санхүүгийн тайлан хоорондын тулгалт буюу уялдаа","value":"4"},
    	        	         {"text":"Санхүүгийн тайлан, тодруулга хоорондын тулгалт буюу уялдаа","value":"5"}];
    	        	var val_data = $scope.selectize_val_options = val;
    	        	$scope.selectize_val_config = {
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
    	   	                    option: function(val_data, escape) {
    	   	                        return  '<div class="option">' +
    	   	                            '<span class="title">' + escape(val_data.text) + '</span>' +
    	   	                            '</div>';
    	   	                    }
    	   	                }
    	   	            };
    	        	var balance=[{"text":"Эхний үлдэгдэл","value":"1"},
    	        	         {"text":"Эцсийн үлдэгдэл","value":"2"},
    	        	         {"text":"Өмнөх оны гүйцэтгэл ","value":"21"},
    	        	         {"text":"Тайлант оны гүйцэтгэл","value":"22"}];
    	        	var balance_data = $scope.selectize_balance_options = balance;
    	        	$scope.selectize_balance_config = {
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
    	   	                    option: function(balance_data, escape) {
    	   	                        return  '<div class="option">' +
    	   	                            '<span class="title">' + escape(balance_data.text) + '</span>' +
    	   	                            '</div>';
    	   	                    }
    	   	                }
    	   	            };
    	      var modaladd = UIkit.modal("#modal_add");
    	      $scope.addValid = function($event){
    	    	  $scope.formdata={};
    	    	  $scope.formdata.isformula1=1;
    	    	  $scope.formdata.isformula2=1;
    	    	  $scope.formdata.balanceid=1;
    	    	  $event.preventDefault();
    	    	  modaladd.show();

    	      }
    	      $scope.edit = function($event,id){
    	    	  $event.preventDefault();
    	    	  mainService.withdomain('get', '/my/resource/LutValidation/'+id).
	    			then(function(data){
	    				$scope.formdata=data[0];
	    				$scope.formdata.isformula1=1;
	        	    	$scope.formdata.isformula2=1;
	    				if ($scope.formdata.adirid != undefined){
	    					mainService.withdomain("GET", "/au/withid/LnkRiskdir/"+$scope.formdata.adirid).then(function(data){
		    	        		$scope.risks = data;
		    	        	});
	    				}
	    				if ($scope.formdata.riskid != undefined){
	    					mainService.withdomain("GET", "/au/withid/LnkTryoutRisk/"+$scope.formdata.riskid).then(function(data){
		    	        		$scope.tryouts = data;
		    	        	});
	    				}
	    				modaladd.show();
	    			});
    	      }
    	      $scope.domain="com.netgloo.models.LutValidation.";
    	      $scope.submitForm = function($event){
    	    	  mainService.withdata('POST', '/info/create/'+$scope.domain,  $scope.formdata).
	    			then(function(data){
	    				modaladd.hide();
	    				$event.preventDefault();
		   				sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		   				$("#notificationSuccess").trigger('click');
		   				$(".k-grid").data("kendoGrid").dataSource.read(); 
	    			});
    	      }
    	      
    	      $scope.deleteTry = function(id){
    	    	 
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
			        		 mainService.withdata('POST', '/info/delete/'+$scope.domain,  $scope.formdata).
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
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                    		url: "/my/list/LutValidation",
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
			                        		 code1: { type: "string",validation: { required: true } },	 
			                        		 code2: { type: "string",validation: { required: true } },	 
			                        		 title1: { type: "string",validation: { required: true } },	 
			                        		 title2: { type: "string",validation: { required: true } },	 
			                        		 balanceid: { type: "number",validation: { required: true } },	 
			                        		 position1: { type: "string",validation: { required: true } },	 
			                        		 position2: { type: "string",validation: { required: true } },	 
			                        		 isformula1: { type: "number",validation: { required: true } },	 
			                        		 isformula2: { type: "number",validation: { required: true } },	 
			                        		 valid: { type: "number",validation: { required: true } },	
			                             }
			                         }
			                     },
			                     group: {
	                                  field: "valid",values:val
	                                },
			                    pageSize: 5,
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
			                columnMenu:true, 
			                resizable: true,
			                toolbar: kendo.template($("#addValid").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                        { field: "valid", title: "Тулгалт" +"<span data-translate=''></span>",values:val,width:"300px",hidden:true},
			                        { field: "balanceid", title: "Үлдэгдэл" +"<span data-translate=''></span>",values:balance,width:"150px"},
			                        { field: "title1", title: "Үзүүлэлт 1" +"<span data-translate=''></span>",width:"250px"},
			                        { field: "code1", title: "Дансны код 1" +"<span data-translate=''></span>",width:"150px"},
			                        { field: "position1", title: "Байршил 1" +"<span data-translate=''></span>",width:"150px"},
			                        { field: "isformula1", title: "Формула 1" +"<span data-translate=''></span>",values:sel,width:"150px"},
			                        { field: "title2", title: "Үзүүлэлт 2" +"<span data-translate=''></span>",width:"350px"},
									{ field: "code2", title: "Дансны код 2" +"<span data-translate=''></span>",width:"150px"},
		                          	{ field: "position2", title: "Байршил 2" +"<span data-translate=''></span>",width:"150px"},
		                          	{ field: "isformula2", title: "Формула 2" +"<span data-translate=''></span>",values:sel,width:"150px"},
									{template: kendo.template($("#validUD").html()), width: "200px"}
		                      ],
		                      editable: "popup",
		                      autoBind: true,
			            }
	    	  }
	    	  else{
	  		function init(){
  	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
  		   			.then(function(data){
  		   				console.log(data);
  		   				if(data.rread==1){

	          $scope.puserGrid = {
		                dataSource: {
		                   
		                    transport: {
		                    	read:  {
		                    		url: "/my/list/LutValidation",
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
		                        		 id: { editable: false,nullable: true},
		                        		 code1: { type: "string",validation: { required: true } },	 
		                        		 code2: { type: "string",validation: { required: true } },	 
		                        		 title1: { type: "string",validation: { required: true } },	 
		                        		 title2: { type: "string",validation: { required: true } },	 
		                        		 balanceid: { type: "number",validation: { required: true } },	 
		                        		 position1: { type: "string",validation: { required: true } },	 
		                        		 position2: { type: "string",validation: { required: true } },	 
		                        		 isformula1: { type: "number",validation: { required: true } },	 
		                        		 isformula2: { type: "number",validation: { required: true } },	 
		                        		 valid: { type: "number",validation: { required: true } },	
		                             }
		                         }
		                     },
		                     group: {
                                 field: "valid",values:val
                               },
		                    pageSize: 5,
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
		                columnMenu:true, 
		                resizable: true,
		                //toolbar: ["create"],
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
								{ field: "valid", title: "Тулгалт" +"<span data-translate=''></span>",values:val,width:"300px",hidden:true},
								{ field: "balanceid", title: "Үлдэгдэл" +"<span data-translate=''></span>",values:balance,width:"150px"},
								{ field: "title1", title: "Үзүүлэлт 1" +"<span data-translate=''></span>",width:"250px"},
								{ field: "code1", title: "Дансны код 1" +"<span data-translate=''></span>",width:"150px"},
								{ field: "position1", title: "Байршил 1" +"<span data-translate=''></span>",width:"150px"},
								{ field: "isformula1", title: "Формула 1" +"<span data-translate=''></span>",values:sel,width:"150px"},
								{ field: "title2", title: "Үзүүлэлт 2" +"<span data-translate=''></span>",width:"350px"},
								{ field: "code2", title: "Дансны код 2" +"<span data-translate=''></span>",width:"150px"},
								{ field: "position2", title: "Байршил 2" +"<span data-translate=''></span>",width:"150px"},
								{ field: "isformula2", title: "Формула 2" +"<span data-translate=''></span>",values:sel,width:"150px"},
		                          	
		                      ],
	                      autoBind: true,
		            }
	          if(data.rcreate==1){	   		   					  
	   					if(data.rexport==1){
	   						$scope.puserGrid.toolbar= kendo.template($("#addValid").html());
	   					}
	   					else{
	   					  $scope.puserGrid.toolbar= kendo.template($("#addVal").html());
	   					}
					}
					else if(data.rexport==1){
						$scope.puserGrid.toolbar= kendo.template($("#export").html());
					}
	          if(data.rupdate==1 && data.rdelete==1){
					$scope.puserGrid.columns.push({ template: kendo.template($("#validUD").html()), width: "250px"})
				}
				else if(data.rupdate==1 && data.rdelete==0){
					$scope.puserGrid.columns.push({ template: kendo.template($("#validu").html()), width: "100px"}   )
				}
				else if(data.rupdate==0 && data.rdelete==1){
					$scope.puserGrid.columns.push({ template: kendo.template($("#validd").html()), width: "100px"}   )
				}
				}
				else{
					$state.go('restricted.pages.error404');
				}			
		 });
	}

	init();	            

   }
}
]);
