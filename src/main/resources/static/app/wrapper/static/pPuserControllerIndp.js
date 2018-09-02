angular
    .module('altairApp')
    	.controller("userCtrlIndp",['$scope','p_org','p_pos','p_role','user_data','mainService','$state','sweet',
	        function ($scope,p_org,p_pos,p_role,user_data,mainService,$state,sweet) {       	
    		  /*  var aj=p_uni;
	    		var init={"text":"ROOT","value":"0"};	    	
				aj.push(init);*/
			
    		
    			$scope.domain="com.netgloo.models.LutUser.";
    			
    			//console.log(user_data);
    			if(user_data==!null){
    				$scope.udepid=user_data[0].depid;
    			}
    			else{
    				
    			}
    			
    			//console.log($scope.udepid);
    			$scope.selectize_a_data=p_org;
    			$scope.selectize_b_data= p_pos;
    			$scope.selectize_c_data= p_role;
			    var planets_data = $scope.selectize_role = p_role;
    			
    		    			
    			$scope.res=function(){
    				$scope.ud = {
    		                "id":0,
    		                "org": $scope.udepid,	    		           
    		                "pos": "",
    		                "fname": "",
    		                "gname": "",	    		           
    		                "phone": "",
    		                "mail": "",
    		                "uname": "",	    		           
    		                "pass": "",
    		                "roles": "",
    		                "isac": "",
    		                "isst": false
    		            };			
    				
    			}   			
    				
				$scope.addUser = function() {
					 var mdl = UIkit.modal(".uk-modal");
  	    		     mainService.withdata('PUT','/core/useradd/'+$scope.ud.id, $scope.ud)
	  		   			.then(function(data){
	  		   				mdl.hide();
	  		   				$(".k-grid").data("kendoGrid").dataSource.read(); 
	  		   				/*if(data.re==1){
	  		   					sweet.show('Мэдээлэл', 'Амжилттай засагдлаа.', 'success');
	  		   				}  		   				
	  		   				if(data.re==0){
	  		   					sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
	  		   				}		 */  				
	  		   			});	 
                         
               }
				
    			$scope.selectize_a_config = {
    	                plugins: {
    	                    'disable_options': {
    	                        disableOptions: ["c1","c2"]
    	                    }
    	                },
    	                create: false,
    	                maxItems: 1,
    	                placeholder: 'Сонгох...',
    	                optgroupField: 'parent_id',
    	                optgroupLabelField: 'title',
    	                optgroupValueField: 'ogid',
    	                valueField: 'value',
    	                labelField: 'text',
    	                searchField: 'title',	                	
    	            };
    			
    			/* $scope.selectize_planets_config = {
	                plugins: {
	                    'remove_button': {
	                        label     : ''
	                    }
	                },
	                maxItems: null,
	                valueField: 'id',
	                labelField: 'title',
	                searchField: 'title',
	                create: false,
	                render: {
	                    option: function(planets_data, escape) {
	                        return  '<div class="option">' +
	                            '<span class="title">' + escape(planets_data.title) + '</span>' +
	                            '</div>';
	                    },
	                    item: function(planets_data, escape) {
	                        return '<div class="item"><a href="' + escape(planets_data.url) + '" target="_blank">' + escape(planets_data.title) + '</a></div>';
	                    }
	                }
	            };*/
                
                $scope.update=function(vdata){
                	
                	var cars = vdata.roleid;
                	var array = vdata.roleid.split(",");
                	
                	console.log(array);
					$scope.ud = {						  
    		                "id": vdata.id,
    		                "org": vdata.departmentid,	    		           
    		                "pos": vdata.positionid,
    		                "fname": vdata.familyname,
    		                "gname": vdata.givenname,	    		           
    		                "phone": vdata.mobile,
    		                "mail": vdata.email,
    		                "uname": vdata.username,	    
    		                "pass": vdata.password,
    		                "isac": vdata.isactive,
    		                "isst": vdata.isstate,
    		                roles: array,
    		            };
					
					//$scope.ud.roles= vdata.roleid;
					console.log($scope.ud.roles);
	            	/*
	               	    		    
	    			mainService.getDetail('/core/action/read/'+$scope.domain+'/'+vdata.id)
	    			then(function(data){	    			
	    				$scope.data = data;
	    		        angular.forEach($scope.data, function(value, key){
	    		          
	    		        });    				
	    			});	*/   		
	    			    	    	
	    		}
    			
                
             
                
    			if(user_data==""){
  	    		 $scope.puserGrid = {
  		                dataSource: {
  		                   
  		                    transport: {
  		                    	read:  {
  		                            url: "/core/list/LutUser",
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
  		                             	roleid: { type: "string"},
  		                             	givenname: { type: "string"},
  		                             	familyname: { type: "string"},
  		                             	mobile: { type: "string"},	                                          	
  		                            	username: { type: "string", validation: { required: true} },
  		                            	password: { type: "string", validation: { required: true} },	                            
  		                            	isactive: { type: "boolean" },
  		                            	isst: { type: "boolean" }
  		                             }	                    
  		                         }
  		                    
  		                     },
  		                    pageSize: 8,
  		                    serverPaging: true,
  		                    serverSorting: true,
  		                    serverFiltering: true
  		                },
  		                toolbar: [{template: $("#add").html()},"excel","pdf"],
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
  		                          { field:"departmentid", title: "Байгууллага",values:p_org, width: 250 },
  		                          { field:"positionid", title: "Албан тушаал", values:p_pos, width: 150 },
  		                          { field:"familyname", title: "Овог",width: 150},
  		                          { field:"givenname", title: "Нэр",width: 150 },     
  		                          { field:"mobile", title: "Утас",width: 150},
  		                          { field:"email", title: "E-mail",width: 150},
  		                          { field:"roleid", title: "Эрх" ,width: 150},	     
  		                          { field:"username", title: "Нэвтрэх нэр" ,width: 150},	                          
  		                          { field:"password", title: "Нууц үг" ,width: 150},  
  		                          { field:"isactive", title: "Идэвхитэй эсэх" ,width: 150},  
  		                          { field:"isstate", title: "Төрийн эсэх" ,width: 150},  
  		                          { template: kendo.template($("#update").html()),  width: "240px"}
  		                          ],
  		                      editable: "popup"
  		            }
  	    	  }
  	    	  else {
  	    		  function init(){
  	   	    		 mainService.withdomain('get','/core/rjson/'+user_data[0].id+'/'+$state.current.name+'.')
  	   		   			.then(function(data){
  	   		   				console.log(data);
  	   		   				if(data.rread==1){

  	   		   			 $scope.puserGrid = {
  	   		                dataSource: {
  	   		                   
  	   		                    transport: {
  	   		                    	read:  {
  	   		                            url: "/core/list/LutUser",
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
  	   		                             	id: { type: "id", editable: false,nullable: false},
  	   		                             	departmentid: { type: "number",  validation: { required: true } },	                             	
  	   		                             	email: { type: "string"},
  	   		                             	positionid: { type: "number"},
  	   		                             	roleid: { type: "string"},
  	   		                             	givenname: { type: "string"},
  	   		                             	familyname: { type: "string"},
  	   		                             	mobile: { type: "string"},	                                          	
  	   		                            	username: { type: "string", validation: { required: true} },
  	   		                            	password: { type: "string", validation: { required: true} },	                            
  	   		                            	isactive: { type: "boolean" },
  	   		                            	isst: { type: "boolean" }
  	   		                             }	                    
  	   		                         }
  	   		                     },
  	   		                    pageSize: 8,
  	   		                    serverPaging: true,
  	   		                    serverSorting: true,
  	   		                    serverFiltering: true
  	   		                },
  	   		                //toolbar: ["create"],
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
  	   		                          { field:"departmentid", title: "Байгууллага",values:p_org, width: 250 },
  	   		                          { field:"positionid", title: "Албан тушаал", values:p_pos, width: 150 },
  	   		                          { field:"familyname", title: "Овог",width: 150},
  	   		                          { field:"givenname", title: "Нэр",width: 150 },     
  	   		                          { field:"mobile", title: "Утас",width: 150},
  	   		                          { field:"email", title: "E-mail",width: 150},
  	   		                          { field:"roleid", title: "Эрх" ,width: 200},	     
  	   		                          { field:"username", title: "Нэвтрэх нэр" ,width: 150},	                          
  	   		                          { field:"password", title: "Нууц үг" ,width: 150},  
  	   		                          { field:"isactive", title: "Идэвхитэй эсэх" ,width: 150}, 
  	   		                          { field:"isstate", title: "Төрийн эсэх" ,width: 150},  
  	   		                          //{ command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }
  	   		                          ],
  	   		                      editable: "popup"
  	   		            };
  	 	          		if(data.rcreate==1){	   		   					  
  	 	   					if(data.rexport==1){
  	 	   					  $scope.puserGrid.toolbar=[{template: $("#add").html()},"excel","pdf"]
  	 	   					}
  	 	   					else{
  	 	   					  $scope.puserGrid.toolbar=kendo.template($("#add").html());
  	 	   					}
  	 					}
  	 					else if(data.rexport==1){
  	 						$scope.puserGrid.toolbar=["excel","pdf"];
  	 					}
  	 	          		if(data.rupdate==1 && data.rdelete==1){
  						$scope.puserGrid.columns.push({ template: kendo.template($("#update").html()),  width: "240px"})
  					}
  					else if(data.rupdate==1 && data.rdelete==0){
  						$scope.puserGrid.columns.push({ template: kendo.template($("#updateu").html()),  width: "120px"})
  					}
  					else if(data.rupdate==0 && data.rdelete==1){
  						$scope.puserGrid.columns.push({ template: kendo.template($("#updated").html()),  width: "120px"})
  					}
  	 				}
  	 				else{
  	 					$state.go('restricted.pages.error404');
  	 				}			
  	 		 });
  	 	}
  	    		  init();	
  	    	  };     
            //console.log(data);
	        }
    ]);
