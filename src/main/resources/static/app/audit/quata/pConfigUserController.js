angular
    .module('altairApp')
    	.controller("configuser",['$scope','user_data','p_position','mainService','sweet','$state','$stateParams','org_data','breason','p_cat',
	        function ($scope,user_data,p_position,mainService,sweet,$state,$stateParams,org_data,breason,p_cat) {   
    		
    			$scope.orgdata= org_data;
    			$scope.breason=breason;
    			$scope.terguulehBack=false;
    			$scope.role=user_data[0].role;
    		
    			if($stateParams.stepid==1 && $scope.role=='ROLE_FIRST'){
    				$scope.manager=true;
    			}
    			else if($stateParams.stepid==2 && $scope.role=='ROLE_SECOND'){
    				$scope.terguuleh=true;
    			}
    			else if($stateParams.stepid==3 && $scope.role=='ROLE_ALL'){
    				$scope.all=true;
    			}
    			else if($stateParams.stepid==4 && $scope.role=='ROLE_FIRST'){
    				$scope.backed=true;
    			}
    			
    			else if($scope.role=='ROLE_SECOND' && $stateParams.stepid>2){
    				$scope.terguulehBack=true;
    			}
 
    			var modal = UIkit.modal("#modal_update");
    	    	
    			angular.forEach(p_cat, function(item, key){
    				console.log(item);
    			     if(item.value==org_data.otype){
    			    	 $scope.otype=item.text;
    			     }
    			});
    		
    			$scope.domain="com.netgloo.models.LnkMainUser.";
    			
    			
    			var aj=[{"text":"Тийм","value":"1"},{"text":"Үгүй","value":"0"}];
    			
	            $scope.loading=true;
	            
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
			         	    mainService.withdata('put','/audit/au/create/quata', $scope.quata)
			  		   			.then(function(data){
			  		   				if(data){
			  		   					modal.hide();
			  			   				sweet.show('Мэдээлэл', 'Амжилттай илгээлээ.', 'success');
			  			   			    $state.go('restricted.pages.quataperson');	
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
			        		$scope.loading=false;

			            	   mainService.withdata('put','/au/back/config/'+$stateParams.id, $scope.back)
			  		   			.then(function(data){
			  		   				if(data){
			  		   					modal.hide();
			  			   				sweet.show('Мэдээлэл', 'Амжилттай буцаагдлаа.', 'success');
			  			   			    $state.go('restricted.pages.quataperson');			  			   			    
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
	            
	            $scope.loadOnChange=function(){
	            	
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
			        	/*	 mainService.withdata('POST', '/info/delete/'+$scope.domain,  $scope.formdata).
				    			then(function(data){
				    				$("#notificationSuccess").trigger('click');
	                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
		 				   			sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
				    			});*/
	 		            }else{
	 		                sweet.show('Анхаар!', 'Устгах үйлдэл хийгдсэнгүй!!!', 'error');
	 		            }    		
			        });
	            }
	            
	            
	            $scope.loadOnSend=function(){
	            	   mainService.withdata('put','/au/distribute/'+$stateParams.stepid+'/'+$stateParams.id)
	  		   			.then(function(data){
	  		   				if(data){
	  		   					modal.hide();
	  			   				sweet.show('Мэдээлэл', 'Амжилттай илгээлээ.', 'success');
	  			   			    $state.go('restricted.pages.quataperson');	
	  		   				}
	  		   				else{
		  		   				modal.hide();
	  			   				sweet.show('Анхаар', 'Error...', 'error');
	  		   				}
	  	   			});
	            }
	            
	            $scope.update=function(item){	            	
	            	$state.go('restricted.pages.swap',{id:item.id});
	            }
	            
	            $scope.puserGrid = {
	                dataSource: {
	                	autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/ConfigUser",
	                            contentType:"application/json; charset=UTF-8",  
	                            data:{"appid":""+$stateParams.id+""},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",      
	                            data:{"appid":""+$stateParams.id+""},
	                            type:"POST",
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
	                             	positionid: { type: "number",editable: false,},
	                             	familyname: { type: "string",editable: false,},
	                             	givenname: { type: "string",editable: false,},
	                             	configid: { type: "number",defaultValue:0},
	                             }	                    
	                         }
	                     },
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
	                          { field:"familyname", title: "Овог",width: 200 },
	                          { field:"givenname", title: "Нэр",width: 200 },
	                          { field:"positionid", values:p_position,title: "Албан тушаал"},
	                          { field:"configid", title: "Сонголт",width: 200, values:aj}
	                          ],
	                      //editable: true
	            };
	            
	            if($stateParams.stepid==1 && $scope.role=="ROLE_FIRST" || $stateParams.stepid==4 && $scope.role=="ROLE_FIRST"){	   		   						
	            	$scope.puserGrid.editable=true
	            	$scope.puserGrid.toolbar=["excel","pdf"];
  					}
	            else{$scope.puserGrid.editable=false
	            	$scope.puserGrid.toolbar=["excel","pdf"];
	            	}
	        }
    ]);
