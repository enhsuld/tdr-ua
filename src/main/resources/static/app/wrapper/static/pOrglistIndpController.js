angular
    .module('altairApp')
    	.controller("orglistIndpCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'p_dep',
    	                           'p_fin',
    	                           'p_cat',
    	                           'p_prog',
    	                           'user_data',
    	                           'mainService',
	        function ($scope,$rootScope,$state,$timeout,p_dep,p_fin,p_cat,p_prog,user_data,mainService) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.SubAuditOrganization.";
	    	  
	    	  $scope.org=function(){	    		  
	    			$state.go('restricted.pages.orgformIndp',{param: 0});	    			    	    	
	    	  },	
	    	  $scope.edit=function(i){    		  
	    			$state.go('restricted.pages.orgformIndp',{param: i});	    			    	    	
	    	  }
	    	  
	    	  
	    	  
	    	  if(user_data[0].role=="ROLE_SUPER"){
	
	    		  $scope.pmenuGrid = {
			                dataSource: {
			                   
			                    transport: {
			                    	read:  {
			                            url: "/core/list/SubAuditOrganization",
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
			                        		 departmentid: { type: "number"},		                        		
			                        		 phone: { type: "string",validation: { required: true } },
			                        		 orgname: { type: "string", validation: { required: true} },	
			                        		 orgcode: { type: "string", validation: { required: true} }, 
			                        		 departmentid: { type: "number" },
			                        		 categotyid: { type: "number" },
			                        		 fincategoryid: { type: "number" },
			                        		 progid: { type: "number" },
			                        		 regid: { type: "number" },
			                        		 stateregid: { type: "string" },
			                        		 fsorg: { type: "string" },
			                        		 taxorg: { type: "string" },
			                        		 ndorg: { type: "string" },
			                        		 headorder: { type: "string" },		                        		
			                        		 banks: { type: "string" },
			                        		 statebanks: { type: "string" },
			                        		 web: { type: "string" },
			                        		 email: { type: "string" },
			                        		 address: { type: "string" },
			                        		 phone: { type: "string" },
			                        		 fax: { type: "string" },
			                        		 headfullname: { type: "string" },
			                        		 heademail: { type: "string" },
			                        		 headreg: { type: "string" },
			                        		 headphone: { type: "string" },
			                        		 headprof: { type: "string" },
			                        		 accfullname: { type: "string" },
			                        		 accphone: { type: "string" },
			                        		 accemail: { type: "string" },
			                        		 accwyear: { type: "string" },
			                        		 accprof: { type: "string" },
			                        		 createdate: { type: "string" }
			                        		 		                        		 
			                             }
			                         }
			                     },
			                    pageSize: 8,
			                    serverPaging: true,
			                    serverFiltering: true,
			                    serverSorting: true
			                },
			                toolbar: kendo.template($("#addorg").html()),
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
									{ field: "orgname", locked: true, title: "Нэр"+ "<span data-translate=''></span>" ,width: 450 },								
									{ field: "orgcode", title: "Байгууллагын код" +"<span data-translate=''></span>",width: 220, locked: true},							
									{ field: "departmentid", title: "Аудит хийх байгууллага" +"<span data-translate=''></span>", values: p_dep, width: 250},
									{ field: "categotyid", title: "Ангилал" +"<span data-translate=''></span>", values: p_cat, width: 250},
									{ field: "fincategoryid", title: "Санхүүжилтийн хэлбэр" +"<span data-translate=''></span>", values: p_fin, width: 250},
									{ field: "progid", title: "Төсвийн зарлагын хөтөлбөрийн ангилал" +"<span data-translate=''></span>", values: p_prog, width: 250},
									{ field: "phone", title: "Утас" +"<span data-translate=''></span>" ,width: 150},
									{ field: "regid", title: "Регистрийн дугаар" +"<span data-translate=''></span>",width: 220},
									{ field: "stateregid", title: "УБ-ийн дугаар "  +"<span data-translate=''></span>",width: 220},
									{ field: "fsorg", title: "Санхүүгийн тайлан тушаадаг газар " +"<span data-translate=''></span>",width: 220},
									{ field: "taxorg", title: "Харилцагч татварын байгууллага" +"<span data-translate=''></span>",width: 220},
									{ field: "ndorg", title: "Харилцагч НД-ын байгууллага " +"<span data-translate=''></span>",width: 220},
									{ field: "headorder", title: "Удирдлагын томилдог албан тушаал " +"<span data-translate=''></span>",width: 220},
									{ field: "banks", title: "Харилцагч арилжааны банк, дансны дугаар " +"<span data-translate=''></span>",width: 220},
									{ field: "statebanks", title: "Төрийн сан дахь харилцах банкны дугаар" +"<span data-translate=''></span>",width: 220},
									{ field: "web", title: "Цахим хуудас " +"<span data-translate=''></span>",width: 220},
									{ field: "eamil", title: "И-мэйл " +"<span data-translate=''></span>",width: 220},
									{ field: "address", title: "Албан ёсны хаяг " +"<span data-translate=''></span>",width: 220},
									{ field: "fax", title: "Факс " +"<span data-translate=''></span>",width: 220},
									{ field: "headfullname", title: "Удирдлагын овог, нэр " +"<span data-translate=''></span>",width: 220},
									{ field: "headreg", title: "Регистр " +"<span data-translate=''></span>",width: 220},
									{ field: "headphone", title: "Утас " +"<span data-translate=''></span>",width: 220},
									{ field: "heademail", title: "И-мэйл " +"<span data-translate=''></span>",width: 220},
									{ field: "headproff", title: "Мэргэжил " +"<span data-translate=''></span>",width: 220},
									{ field: "accfullname", title: "Нягтлангийн овог, нэр " +"<span data-translate=''></span>",width: 220},
									{ field: "accphone", title: "Утас " +"<span data-translate=''></span>",width: 220},
									{ field: "accemail", title: "И-мэйл  " +"<span data-translate=''></span>",width: 220},
									{ field: "accwyear", title: "Ажилласан жил " +"<span data-translate=''></span>",width: 220},
									{ field: "accprof", title: "Мэргэжил " +"<span data-translate=''></span>",width: 220},
									{ field: "createdate", title: "Байгуулагдсан огноо " +"<span data-translate=''></span>",width: 220},
									{template: kendo.template($("#extend").html()), width: "200px"}
									 	                          
		                      ],	                     
			            }  
 	    		}
 	    		else{
 	    			function initq(){
     	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
     	   		   			.then(function(data){
     	   		   				console.log(data);
     	   		   				if(data.rread==1){
          	        	
     	   		   			 $scope.pmenuGrid = {
     	   			                dataSource: {
     	   			                   
     	   			                    transport: {
     	   			                    	read:  {
     	   			                            url: "/core/list/SubAuditOrganization",
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
     	   			                        		 departmentid: { type: "number"},		                        		
     	   			                        		 phone: { type: "string",validation: { required: true } },
     	   			                        		 orgname: { type: "string", validation: { required: true} },	
     	   			                        		 orgcode: { type: "string", validation: { required: true} }, 
     	   			                        		 departmentid: { type: "number" },
     	   			                        		 categotyid: { type: "number" },
     	   			                        		 fincategoryid: { type: "number" },
     	   			                        		 progid: { type: "number" },
     	   			                        		 regid: { type: "number" },
     	   			                        		 stateregid: { type: "string" },
     	   			                        		 fsorg: { type: "string" },
     	   			                        		 taxorg: { type: "string" },
     	   			                        		 ndorg: { type: "string" },
     	   			                        		 headorder: { type: "string" },		                        		
     	   			                        		 banks: { type: "string" },
     	   			                        		 statebanks: { type: "string" },
     	   			                        		 web: { type: "string" },
     	   			                        		 email: { type: "string" },
     	   			                        		 address: { type: "string" },
     	   			                        		 phone: { type: "string" },
     	   			                        		 fax: { type: "string" },
     	   			                        		 headfullname: { type: "string" },
     	   			                        		 heademail: { type: "string" },
     	   			                        		 headreg: { type: "string" },
     	   			                        		 headphone: { type: "string" },
     	   			                        		 headprof: { type: "string" },
     	   			                        		 accfullname: { type: "string" },
     	   			                        		 accphone: { type: "string" },
     	   			                        		 accemail: { type: "string" },
     	   			                        		 accwyear: { type: "string" },
     	   			                        		 accprof: { type: "string" },
     	   			                        		 createdate: { type: "string" }
     	   			                        		 		                        		 
     	   			                             }
     	   			                         }
     	   			                     },
     	   			                    pageSize: 8,
     	   			                    serverPaging: true,
     	   			                    serverFiltering: true,
     	   			                    serverSorting: true
     	   			                },
     	   			                //toolbar: kendo.template($("#addorg").html()),
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
     	   									{ field: "orgname", locked: true, title: "Нэр"+ "<span data-translate=''></span>" ,width: 450 },														
     	   									{ field: "departmentid", title: "Аудит хийх байгууллага" +"<span data-translate=''></span>", values: p_dep, width: 250},
     	   									{ field: "phone", title: "Утас" +"<span data-translate=''></span>" ,width: 150},
     	   									{ field: "regid", title: "Регистрийн дугаар" +"<span data-translate=''></span>",width: 220},
     	   									{ field: "stateregid", title: "УБ-ийн дугаар "  +"<span data-translate=''></span>",width: 220},
     	   									{ field: "fsorg", title: "Санхүүгийн тайлан тушаадаг газар " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "taxorg", title: "Харилцагч татварын байгууллага" +"<span data-translate=''></span>",width: 220},
     	   									{ field: "ndorg", title: "Харилцагч НД-ын байгууллага " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "headorder", title: "Удирдлагын томилдог албан тушаал " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "banks", title: "Харилцагч арилжааны банк, дансны дугаар " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "statebanks", title: "Төрийн сан дахь харилцах банкны дугаар" +"<span data-translate=''></span>",width: 220},
     	   									{ field: "web", title: "Цахим хуудас " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "email", title: "И-мэйл " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "address", title: "Албан ёсны хаяг " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "fax", title: "Факс " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "headfullname", title: "Удирдлагын овог, нэр " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "headreg", title: "Регистр " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "headphone", title: "Утас " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "heademail", title: "И-мэйл " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "headprof", title: "Мэргэжил " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "accfullname", title: "Нягтлангийн овог, нэр " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "accphone", title: "Утас " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "accemail", title: "И-мэйл  " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "accwyear", title: "Ажилласан жил " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "accprof", title: "Мэргэжил " +"<span data-translate=''></span>",width: 220},
     	   									{ field: "createdate", title: "Байгуулагдсан огноо " +"<span data-translate=''></span>",width: 220},
     	   									//{template: kendo.template($("#extend").html()), width: "200px"}
     	   									 	                          
     	   		                      ],	                     
     	   			            };   
          	            if(data.rcreate==1){	   		   					  
	   		   					if(data.rexport==1){
	   		   					  $scope.pmenuGrid.toolbar=kendo.template($("#addorg").html())
	   		   					}
	   		   					else{
	   		   					  $scope.pmenuGrid.toolbar=kendo.template($("#addorg").html())
	   		   					}
 		   					}
 		   					else if(data.rexport==1){
 		   						$scope.pmenuGrid.toolbar=["excel","pdf"];
 		   					}    	    	                	          
          	            if(data.rupdate==1 && data.rdelete==1){    	    	                	            	
          	            	$scope.pmenuGrid.columns.push({ template: kendo.template($("#extend").html()), width: "236px"}  ) 
 		   					}
          	            else if(data.rupdate==1 && data.rdelete==0){
 		   						//{ command: ["edit", "destroy"], title: "&nbsp;", width: "270px" }
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
			
  		initq();
 	    		}
	    	           
	         
        }]
    )
    
 