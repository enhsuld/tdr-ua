angular
    .module('altairApp')
    	.controller("riskCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'p_dir',
    	                           'p_law',
    	                           'sweet',
    	                           '$filter',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,p_dir,p_law,sweet,$filter) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutRisk.";
	    	  
	    	  $scope.addstandart=false;
   	   		  $scope.addother=false;
   	   	      $scope.addlaw=false;
	    	  
	    	  $scope.lawzaalt=true;
	    	  $scope.lawzuil=true;
	    	  $scope.selectize_y_data="";
	    	  $scope.selectize_x_data="";
	    	  $scope.selectize_z_data="";
	    	  
	    	  var risktype=[
        		  {"text":"Хяналтын", "value":0},
        		  {"text":"Уламжлалт", "value":1},
        	  ];
	    	  
	    	  $scope.res=function(){
	    		  $state.go('restricted.pages.riskedit',{param: 0});	
  				}
	    	  $scope.update=function(i){    		  
    			  $state.go('restricted.pages.riskedit',{param: i});	    			    	    	
	    	  }
	    	  
	    	  	   $scope.selectize_a_data = [];
	    	    
	               $scope.selectize_a_config = {
	                       plugins: {
	                           'disable_options': {
	                               disableOptions: ["c1","c2"]
	                           }
	                       },
	                       create: false,
	                       maxItems: 1,
	                       placeholder: 'Сонго...',
	                       optgroupField: 'parent_id',
	                       optgroupLabelField: 'title',
	                       optgroupValueField: 'ogid',
	                       valueField: 'value',
	                       labelField: 'text',
	                       searchField: 'title'
	                   };
	    	  
	               
	               
	               $scope.newhuuli=function(){
	            		$scope.link = {	      		               
	      		                "parentid": "",
	      		                "parentid1": null,
	      		                "parentid2": null,
	      		                "parentid3": null	      		               		                
	      		            };
	            	   $scope.addstandart=false;
	            	   $scope.addother=false;
	            	   var plaw = [];
	    	  	    	 
	     	    	  angular.forEach(p_law, function(val, key) {    			  
	         			 
	         			     if(val.parentid==0 && val.lawcategory==1){
	         			    	 var c={
	         			    			 "value":val.value,
	         			    			 "text":val.text
	         			    	 };
	         			    	 
	      					}
	         			     plaw.push(c)
	      			}); 
	     	    	 $scope.selectize_a_data = plaw;
            	       }
	               
	               
	               
      	    		$scope.newstandart=function(){
      	    			$scope.link = {	      		               
	      		                "parentid": "",
	      		                "parentid1": null,
	      		                "parentid2": null,
	      		                "parentid3": null	      		               		                
	      		            };
      	    		   $scope.addlaw=false;
 	            	   $scope.addother=false;
 	            	  var plaw = [];
	    	  	    	 
 	   	    	  angular.forEach(p_law, function(val, key) {    			  
 	       			 
 	       			     if(val.parentid==0 && val.lawcategory==2){
 	       			    	 var c={
 	       			    			 "value":val.value,
 	       			    			 "text":val.text
 	       			    	 };
 	       			    	 
 	    					}
 	       			     plaw.push(c)
 	    			}); 
 	   	    	 $scope.selectize_a_data = plaw;
            	       }
      	    		
      	    		
      	    		$scope.newother=function(){
      	    			$scope.link = {	      		               
	      		                "parentid": "",
	      		                "parentid1": null,
	      		                "parentid2": null,
	      		                "parentid3": null	      		               		                
	      		            };
      	    		   $scope.addstandart=false;
 	            	   $scope.addlaw=false;
		 	            	  var plaw = [];
			    	  	    	 
		 	   	    	  angular.forEach(p_law, function(val, key) {    			  
		 	       			 
		 	       			     if(val.parentid==0 && val.lawcategory==3){
		 	       			    	 var c={
		 	       			    			 "value":val.value,
		 	       			    			 "text":val.text
		 	       			    	 };
		 	       			    	 
		 	    					}
		 	       			     plaw.push(c)
		 	    			}); 
		 	   	    	 $scope.selectize_a_data = plaw;
            	       } 
	               
	    	
	    	 var planets_data = $scope.selectize_dir = p_dir;
  			 $scope.selectize_planets_config = {
	                plugins: {
	                    'remove_button': {
	                        label     : ''
	                    }
	                },
	                maxItems: null,
	                placeholder: 'Сонгох...',
	                valueField: 'value',
	                labelField: 'text',
	                searchField: 'title',
	                create: false,
	                render: {
	                    option: function(planets_data, escape) {
	                        return  '<div class="option">' +
	                            '<span class="title">' + escape(planets_data.text) + '</span>' +
	                            '</div>';
	                    },
	                    
	                }
	            };
  			 
  			$scope.changezuil=function(){
	    			console.log($scope.link.parentid);
     	    	   mainService.withdomain('GET', '/info/sel/parent/'+$scope.link.parentid)
     	    	.then(function(data){		
     	    		console.log(data);  	    	               	    		
     	    		if(data==null || data==""){
     	    		$scope.selectize_z_data=data;
     	    		sweet.show('Мэдээлэл', 'Энэ хуульд зүйл байхгүй байна зүйл нэмнэ үү.', 'error');
     	    		}
     	    		else{
     	    			$scope.selectize_z_data=data;
     	    			$scope.lawzuil=false;
     	    		}
     	    		
	   			});	
     	       }
	    		
	    		$scope.changezaalt11=function(){
	    			console.log($scope.link.parentid1);
     	    	   mainService.withdomain('GET', '/info/sel/parent/'+$scope.link.parentid1)
     	    	.then(function(data){		
     	    		console.log(data);  	    	               	    		
     	    		if(data==null || data==""){
     	    		$scope.selectize_y_data=data;
     	    		sweet.show('Мэдээлэл', 'Энэ хуульд зүйл байхгүй байна зүйл нэмнэ үү.', 'error');
     	    		}
     	    		else{
     	    		$scope.selectize_y_data=data;
     	    		$scope.lawzaalt=false;    	    		
     	    		}
     	    		
	   			});	
     	       } 
  			$scope.addRisk = function() {
				 var mdl = UIkit.modal(".uk-modal");
	    		     mainService.withdata('PUT','/info/Riskadd/'+$scope.risk.id, $scope.risk)
 		   			.then(function(data){
 		   				if(data.re==2){
 		   				sweet.show('Мэдээлэл', 'Хуулийн заалтаа сонгоно уу.', 'error');
 		   				}
 		   				if(data.re==1){
 		   				sweet.show('Мэдээлэл', 'Амжилттай засагдлаа.', 'success');
 		   				mdl.hide();
 		   				$(".k-grid").data("kendoGrid").dataSource.read(); 
 		   				}
 		   				if(data.re==0){
 		   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
 		   				mdl.hide();
 		   				$(".k-grid").data("kendoGrid").dataSource.read(); 
 		   				} 		   			 				
 		   			});	 
                    
          }
  			$scope.addlinks= function() {  						
  				if($scope.link.parentid3!=null){    					
  					if($scope.links==""){
  						 var linkee = [];
	 	  	 	  	    	 
 	  	 	   	    	  angular.forEach(p_law, function(val, key) {    			  
 	  	 	       			 
 	  	 	       			     if(val.value==$scope.link.parentid3){
 	  	 	       			    	 var l={
 	  	 	       			    			 "value":val.value,
 	  	 	       			    			 "text":val.text
 	  	 	       			    	 };
 	  	 	       			    	 linkee.push(l)
 	  	 	             			 console.log(linkee)
 	  	 	    					} 	       			 
 	  	 	    			}); 
 	  					$scope.links= $scope.links.concat(linkee);
 	  					sweet.show('Мэдээлэл', 'Холбоос нэмэгдлээ.', 'success');
  					}
					else{
						angular.forEach($scope.links, function(val, key) {    			  
	  	 	       			 
	 	       			     if(val.value==$scope.link.parentid3){
	 	       			    	sweet.show('Мэдээлэл', 'Уг холбоос давхцаж байна.', 'error');
	 	    					} 
	 	       			     else{
	 	       			    	 var linkee = [];
	 	  	 	  	    	 
	 	  	 	   	    	  angular.forEach(p_law, function(val, key) {    			  
	 	  	 	       			 
	 	  	 	       			     if(val.value==$scope.link.parentid3){
	 	  	 	       			    	 var l={
	 	  	 	       			    			 "value":val.value,
	 	  	 	       			    			 "text":val.text
	 	  	 	       			    	 };
	 	  	 	       			    	 linkee.push(l)
	 	  	 	             			 console.log(linkee)
	 	  	 	    					} 	       			 
	 	  	 	    			}); 
	 	  					$scope.links= $scope.links.concat(linkee);
	 	  					sweet.show('Мэдээлэл', 'Холбоос нэмэгдлээ.', 'success');
	 	  					}
	 	    			}); 
					}  				
  				}
  				else{   					
  					if($scope.links==""){  						
 						 var linkee = [];
	 	  	 	  	    	 
	  	 	   	    	  angular.forEach(p_law, function(val, key) {    			  
	  	 	       			 
	  	 	       			     if(val.value==$scope.link.parentid2){
	  	 	       			    	 var l={
	  	 	       			    			 "value":val.value,
	  	 	       			    			 "text":val.text
	  	 	       			    	 };
	  	 	       			    	 linkee.push(l)
	  	 	             			 console.log(linkee)
	  	 	    					} 	       			 
	  	 	    			}); 
	  					$scope.links= $scope.links.concat(linkee);
	  					sweet.show('Мэдээлэл', 'Холбоос нэмэгдлээ.', 'success');
 					}
					else{
						console.log($scope.links);
						angular.forEach($scope.links, function(val, key) {    			  
	  	 	       			 
	 	       			     if(val.value==$scope.link.parentid2){
	 	       			    	sweet.show('Мэдээлэл', 'Уг холбоос давхцаж байна.', 'error');
	 	    					} 
	 	       			     else{
	 	       			    	console.log($scope.links);
	 	       			    	 var linkee = [];
	 	  	 	  	    	 
	 	  	 	   	    	  angular.forEach(p_law, function(val, key) {    			  
	 	  	 	       			 
	 	  	 	       			     if(val.value==$scope.link.parentid2){
	 	  	 	       			    	 var l={
	 	  	 	       			    			 "value":val.value,
	 	  	 	       			    			 "text":val.text
	 	  	 	       			    	 };
	 	  	 	       			    	 linkee.push(l)
	 	  	 	             			 console.log(linkee)
	 	  	 	    					} 	       			 
	 	  	 	    			}); 
	 	  					$scope.links= $scope.links.concat(linkee);
	 	  					sweet.show('Мэдээлэл', 'Холбоос нэмэгдлээ.', 'success');
	 	  					}
	 	    			}); 
					} 
  				}  				
			}
  			 
  			if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                            url: "/info/list/LutRisk",
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
			                            url: "/user/service/editing/create/"+$scope.domain+"",
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
			                        		 riskname: { type: "string", validation: { required: true } },
			                        		 risktype: { type: "number", validation: { required: true } }/*,
			                        		 lawcategory: { type: "number", validation: { required: true } }*/			                        		 
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
			                //columnMenu:true, 
			                resizable: true,
			                toolbar: [{template: $("#Radd").html()},"excel","pdf"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "riskname", title: "Нэр",width: "240px"},
									{ field: "risktype", title: "Төрөл",values:risktype, width: "140px"},
									//{ field: "lawcategory", title: "Хууль"},									
									{ template: kendo.template($("#dirs").html()), title: "Аудитын чиглэл"},
									{ template: kendo.template($("#RUD").html()),  width: "240px"}
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
		                      editable: "popup",
		                      autoBind: true,
			            }	    		  
	    	  }
	    	  else{function init(){
	  	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
	  		   			.then(function(data){
	  		   				
	  		   				if(data.rread==1){

		          $scope.puserGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                            url: "/info/list/LutRisk",
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
			                            url: "/user/service/editing/create/"+$scope.domain+"",
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
			                        		 riskname: { type: "string", validation: { required: true } },
			                        		 risktype: { type: "number", validation: { required: true } }/*,
			                        		 lawcategory: { type: "number", validation: { required: true } }*/
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
			                //columnMenu:true, 
			                resizable: true,
			                //toolbar: ["create"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
			                	{ title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "riskname", title: "Нэр"},
									{ field: "risktype", title: "Төрөл",values:risktype,width: "140px"},
									//{ field: "lawcategory", title: "Хууль"},
									{ template: kendo.template($("#dirs").html()), title: "Аудитын чиглэл"},
									//{ command: ["edit", "destroy"], title: "&nbsp;", width: "232px" }
									
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
		                      editable: "popup",
		                      autoBind: true,
			            }
		          if(data.rcreate==1){	   		   					  
		   					if(data.rexport==1){
		   					 $scope.puserGrid.toolbar=[{template: $("#Radd").html()},"excel","pdf"]
		   					}
		   					else{
		   					 $scope.puserGrid.toolbar=kendo.template($("#Radd").html());
		   					}
						}
						else if(data.rexport==1){
							$scope.puserGrid.toolbar=["excel","pdf"];
						}
						if(data.rupdate==1 && data.rdelete==1){
							$scope.puserGrid.columns.push({ template: kendo.template($("#RUD").html()),  width: "240px"})
						}
						else if(data.rupdate==1 && data.rdelete==0){
							$scope.puserGrid.columns.push({ template: kendo.template($("#RU").html()),  width: "120px"})
						}
						else if(data.rupdate==0 && data.rdelete==1){
							$scope.puserGrid.columns.push({ template: kendo.template($("#RD").html()),  width: "120px"})
						}
					}
					else{
						$state.go('error.404');
					}			
			 });
		}

	init();	            
}
	  		
}
]);
