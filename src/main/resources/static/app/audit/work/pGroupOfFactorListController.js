angular
    .module('altairApp')
    	.controller("groupListCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data) { 
    	                        	   
    	      $scope.formdata = {};
    	      var modalUpdate = UIkit.modal("#modal_update");
    	      $scope.add = function(){
    	    	  $scope.formdata = {};
    	    	  $scope.formdata.id = 0;
    	    	  modalUpdate.show();
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
    	      
              $scope.selectize_a_data = {
                  options: [
                      {
                          id: 1,
                          title: "Үндсэн",
                          value: "0"
                      },
                      {
                          id: 2,
                          title: "Туслах",
                          value: "1"
                      }
                  ]
              };

              $scope.selectize_a_config = {
                  plugins: {
                      'disable_options': {
                          disableOptions: ["c1","c2"]
                      }
                  },
                  create: false,
                  maxItems: 1,
                  placeholder: 'Төрөл сонгох...',
                  optgroupField: 'parent_id',
                  optgroupLabelField: 'title',
                  optgroupValueField: 'ogid',
                  valueField: 'value',
                  labelField: 'title',
                  searchField: 'title',
                  onInitialize: function(selectize){
                      selectize.on('change', function() {
                          console.log('on "change" event fired');
                      });
                      selectize.on('focus', function() {
                          console.log('on "focus" event fired');
                      });
                      selectize.on('dropdown_open', function() {
                          console.log('on "dropdown_open" event fired');
                      });
                  }
              };
         		
    	      $scope.edit = function(id){
    	    	  mainService.withdomain('get', '/my/resource/LutGroupOfFactor/'+id).
	    			then(function(data){
	    				$scope.formdata = {};
	    				$scope.formdata=data[0];   	    
	    				modalUpdate.show();
	    			});	  
    	      }
    	      $scope.domain="com.netgloo.models.LutGroupOfFactor.";
    	      $scope.submitForm = function(){
    	    	  mainService.withdata('POST', '/info/create/'+$scope.domain, $scope.formdata).
	    			then(function(data){
	    				$("#notificationSuccess").trigger('click');
	    				$(".k-grid").data("kendoGrid").dataSource.read(); 
	    				modalUpdate.hide();
	    				$scope.formdata = {};
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
			                    		url: "/my/list/LutGroupOfFactor",
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
			                        		 orderid: { type: "number",validation: { required: true } },
			                             }
			                         }
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
			                toolbar: kendo.template($("#addworklist").html()),
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									{ field: "name", title: "Бүлгийн нэр" +"<span data-translate=''></span>"},
									{ field: "orderid", title: "Дараалал" +"<span data-translate=''></span>", width:120},
									{template: kendo.template($("#extend").html()), width: "250px"}
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
		                    		url: "/my/list/LutGroupOfFactor",
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
		                        		 ftype: { type: "number",validation: { required: true } },
		                        		 orderid: { type: "number",validation: { required: true } },
		                             }
		                         }
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
									{ field: "name", title: "Бүлгийн нэр" +"<span data-translate=''></span>"},
									{ field: "ftype", title: "Бүлгийн төрөл" +"<span data-translate=''></span>"},
									{ field: "orderid", title: "Дараалал" +"<span data-translate=''></span>", width:120},
									//{ command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }
		                      ],
	                      editable: "popup",
	                      autoBind: true,
		            }
	          if(data.rcreate==1){	   		   					  
	   					if(data.rexport==1){
	   						$scope.puserGrid.toolbar= kendo.template($("#addworklist").html());
	   					}
	   					else{
	   					  $scope.puserGrid.toolbar= kendo.template($("#add").html());
	   					}
					}
					else if(data.rexport==1){
						$scope.puserGrid.toolbar= kendo.template($("#export").html());
					}
	          if(data.rupdate==1 && data.rdelete==1){
					$scope.puserGrid.columns.push({ template: kendo.template($("#extend").html()), width: "250px"})
				}
				else if(data.rupdate==1 && data.rdelete==0){
					$scope.puserGrid.columns.push({ template: kendo.template($("#extendu").html()), width: "120px"}   )
				}
				else if(data.rupdate==0 && data.rdelete==1){
					$scope.puserGrid.columns.push({ template: kendo.template($("#extendd").html()), width: "120px"}   )
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
