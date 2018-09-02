angular
    .module('altairApp')
    	.controller("customTreatmentCtrl",['$rootScope','$scope','$window','user_data','mainService','sweet','$state','$stateParams','mainobj','dirs','conftype','confMethod','confSourceI','confSourceO','LutGroupOfFactor',
	        function ($rootScope,$scope,$window,user_data,mainService,sweet,$state,$stateParams,mainobj) {   
    		
	    		 $rootScope.toBarActive = true;
	
	             $scope.$on('$destroy', function() {
	                 $rootScope.toBarActive = false;
	             });
             
    			$scope.project=mainobj;
    			
    			$scope.mid=$stateParams.issueId;
    				
    			$scope.role=user_data[0].role;
    			$scope.isRiskCreated = false;
    			// masked inputs
                var $maskedInput = $('.masked_input');
                if($maskedInput.length) {
                    $maskedInput.inputmask();
                }
                
                $scope.addMorePostParameters = function (e) {
                    e.data = {"mid": $stateParams.issueId, "userid": user_data[0].id};
                };
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
    		
         	    $scope.submitForm = function(){
                  	mainService.withdata('put', '/au/submit/treatment',  $scope.treatment).
       				then(function(data){
       					  modal.hide();
 		   				  sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
                 		  $(".k-grid").data("kendoGrid").dataSource.read(); 
       				});
                }

         	      $scope.back=function(){	            	
  	            	$state.go('restricted.pages.mainwork',{issueId:$stateParams.issueId,stepid:$stateParams.stepid,typeid:$stateParams.typeid});	        
  	            }
    		    var modal = UIkit.modal("#modal_update");
    		   
    		   
             	$scope.update = function(item){
             		 $('#modal_update').on({

         		        'show.uk.modal': function(){
         		        	$scope.currentInitialFiles = [];    	
         		        },

         		        'hide.uk.modal': function(){
         		       	 	$scope.currentInitialFiles = [];
         		            console.log("Element is not visible.");
         		        }
         		     });
             		 modal.show();
             		 $scope.currentInitialFiles=[];
             		 
             		 $scope.treatment={};
             		 $scope.treatment=item;
             	
             		 for (var i = 0; i < $scope.treatment.lnkTryoutFiles.length; i++) {
             			var obj={
             					id:$scope.treatment.lnkTryoutFiles[i].id,	
             					name:$scope.treatment.lnkTryoutFiles[i].name,
             					filename:$scope.treatment.lnkTryoutFiles[i].filename,
             					extension:$scope.treatment.lnkTryoutFiles[i].extension,	
             					size:$scope.treatment.lnkTryoutFiles[i].fsize	
             			};
             			$scope.currentInitialFiles.push(obj);
             		}
             		//$scope.treatment.lnkTryoutFiles=[];
     	        }
             	
             	$scope.fileRemove = function(y){
             		mainService.withdomain('get', '/au/remove/treatment/file/'+y).
       				then(function(data){
       					for(var i = $scope.currentInitialFiles.length - 1; i >= 0; i--) {
       					    if($scope.currentInitialFiles[i].id == y) {
       					    	$scope.currentInitialFiles.splice(i, 1);
       					    }
       					}
       					for(var i = $scope.treatment.lnkTryoutFiles.length - 1; i >= 0; i--) {
       					    if($scope.treatment.lnkTryoutFiles[i].id == y) {
       					    	$scope.treatment.lnkTryoutFiles.splice(i, 1);
       					    }
       					}
       				});
             	}
             	$scope.success=function(){
             		//alert();
             	};
         	    $scope.onSuccess = function (e) {
         	    	console.log("Success (" + e.operation + ") :: " + getFileInfo(e));
         	    	$scope.success();
         	    };
         	    function getFileInfo(e) {
                   return $.map(e.files, function(file) {
                	   var fobj={
                			   id:0,
                			   fileurl:e.response.fileurl,
                			   filename:e.response.filename,
                			   name:e.response.filename,
                			   extension:e.response.mimetype,
                			   fsize:e.response.filesize
                	   }
                	   $scope.treatment.lnkTryoutFiles.push(fobj);
                	   $scope.currentInitialFiles.push(fobj);
                       var info = file.name;

                       // File size is not available in all browsers
                       if (file.size > 0) {
                           info  += " (" + Math.ceil(file.size / 1024) + " KB)";
                       }
                       return info;
                   }).join(", ");
                }
                $scope.onSelect = function(e) {
                    var message = $.map(e.files, function(file) { return file.name; }).join(", ");
                }
                
          
    			$scope.domain="com.netgloo.models.LnkMainUser.";
    			
    			$scope.categoryDropDownEditor = function(container, options) {
			        var editor = $('<textarea cols="30" rows="4" class="k-textbox" style="float:left;height:100%;" data-bind="value: ' + options.field + '"></textarea>')
			        .appendTo(container);
			    }
    			
    			$scope.calendarEditor = function(container, options) {
			        var editor = $('<input class="md-input" required type="text" id="uk_dp_1" data-uk-datepicker="{format:"DD.MM.YYYY"}" data-bind="value: ' + options.field + '" md-input ng-model="treatment.data17">')
			        .appendTo(container);
			    }
    				            
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
 					var decision=[{"text":"Хамааралгүй","value":"0"},{"text":"Эрсдэлтэй","value":"1"},{"text":"Эрсдэлгүй","value":"2"}];	
 				   	   					
				$scope.puserGrid = {
	                dataSource: {
	                    autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+""},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
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
	                             	id: { type: "number", editable: false,nullable: false},
	                             	dirid: { type: "number",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data2: { type: "string",editable: false},
	                             	data3: { type: "string",editable: false},
	                             	data12: { type: "string",editable: false},
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                sortable: true,
	                resizable: true,
	                excel: {
		                fileName: "Төлөвлөсөн горим, сорил, эрсдэл.xlsx",
		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
		                filterable: true,
		                allPages: true
		            },
	               
	                pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },
	                excel: {
	                fileName: "Treatment Export.xlsx",
	                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
	                filterable: true,
	                allPages: true
	            },
	                columns: [
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>",locked: true, width:50},
	                		  { field:"data1", title: "Аудитын чиглэл",locked: true, width: "200px" },	
	                		  { field:"data2", title: "Илрүүлсэн эрсдэл",locked: true, width: "200px" },	
	                		  { field:"data12", title: "Хэрэгжүүлэхээр төлөвлөсөн горим сорил",locked: true, width: "200px" },	
	                		  { title: "<span style='vertical-align:middle'>Цуглуулсан нотлох зүйлийн</span>" ,columns:[{title:"Огноо",width: "100px",template: "#=(data17==null) ? '' : data17#" ,field:"data17"},{title:"Нэр",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data18==null) ? '' : data18#"  ,field:"data18"},{title:"Товч агуулга",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data19==null) ? '' : data19#"  ,field:"data19"}]},	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "200px" },	
	                		  { field:"data22", title: "<span style='vertical-align:middle'>Нотлох зүйлийн ишлэл</span>", editor: $scope.categoryDropDownEditor,width: "200px"},
	                		  { title: "<span style='vertical-align:middle'>Хавсралт файл</span>", template: kendo.template($("#files").html()),width: "200px"},
	                		  { 
   		                          	 template: kendo.template($("#update").html()),  width: "110px",locked: true, 
   		                      }],
		                      dataBound: function () {
		   	   		                var rows = this.items();
		   	   		                  $(rows).each(function () {
		   	   		                      var index = $(this).index() + 1 
		   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
		   	   		                      var rowLabel = $(this).find(".row-number");
		   	   		                      $(rowLabel).html(index);
		   	   		                  });
		   	   		                  
		   	   		              var grid = this;
		   			              grid.tbody.find("tr").dblclick(function (e) {
		   			                  var dataItem = grid.dataItem(this);
		   			                  $scope.update(dataItem);
		   			              });
			   	   		         
		   			              
		   	   		  	           },
		   	   		  	  editable: false
	            };
				if($scope.role=="ROLE_AUDIT" && $stateParams.wstep!=3){
	            	$scope.puserGrid.editable=true;
	            	$scope.puserGrid.toolbar=kendo.template($("#export").html());
				}
				else if($scope.role=="INDP" && $stateParams.wstep!=3){	   		   						
					$scope.puserGrid.editable=true;
	            	$scope.puserGrid.toolbar=kendo.template($("#export").html());
				}
	            else{
	            	$scope.puserGrid.editable=false
	            	$scope.puserGrid.toolbar=kendo.template($("#export").html());
            	}
				 
	        }
    ]);
