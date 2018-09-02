angular
    .module('altairApp')
    	.controller("applicationCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'p_apps',
    	                           'p_forms',
    	                           'p_users',
    	                           'p_specialist',
    	                           'p_pos',
    	                           'sweet',
    	                           'mainService',
	        function ($scope,$rootScope,$state,$timeout,p_apps,p_forms,p_users,p_specialist,p_pos,sweet,mainService) { 
    	                        	   
	    	  $scope.domain="netgloo.models.CApplicationRegistration.";

	    	  $scope.apps=p_apps;
	/*    	  $scope.status= {
	    		  stat:[
	    		   {   value: 0,
                  text: "Хадгалсан",                     
              },
              {   value: 1,
                  text: "Илгээсэн",                     
              },                
              {value: 2,
                  text: "Хянагдаж байгаа",                     
              },
              {value: 3,
                  text: "Засварт буцаасан",                     
              },
              {value: 4,
                  text: "Татгалзсан",                     
              },
              
              {value: 5,
                  text: "Баталгаажсан",                     
              }
              ]};*/
	    	  
	    	  $scope.step= {
		    		  st:[
		    		   {   value: 0,
	                  text: "",                     
	              },
	              {   value: 1,
	                  text: "Эрхлэгч",                     
	              },                
	              {value: 2,
	                  text: "Захирал",                     
	              },
	              {value: 3,
	                  text: "НБГ Мэргэжилтэн",                     
	              },
	              {value: 4,
	                  text: "НБГ Хэлтсийн дарга",                     
	              },
	              
	              {value: 5,
	                  text: "НБГ Дарга",                     
	              }
	              ]};
	    	      
                  
                  console.log(p_specialist);  
	    	  console.log($scope.step);
	    	  $scope.appgo=function(i){
	    		  mainService.withdomain('put','/user/application/create/'+i)
		   			.then(function(data){
		   				console.log(data);
		   				if(data.success=='true'){
			   				$state.go('restricted.pages.applicationForm',{userid:data.userid,param:i,id:data.id});
		   				}
		   				else{
		   					sweet.show('Анхаар!', 'Алдаа үүслээ', 'error');
		   				}
			   				
		   			});	
	    	  }
	    	  $scope.update=function(item){
	    			$state.go('restricted.pages.applicationForm',{userid:item.userid,param:item.formid,id:item.id});
	    			    	    	
	    		},
	    		
    		  $scope.delete=function(item){
    			 mainService.withdomain('delete','/user/service/CApplicationRegistration/'+item.id)
		   			.then(function(data){
		   				console.log(data);
		   				$(".k-grid").data("kendoGrid").dataSource.read(); 
			   				
		   			});	 
    		  },
	    	  
	          $scope.pmenuGrid = {
		                dataSource: {
		                   
		                    transport: {
		                    	read:  {
		                            url: "/user/angular/CMyApplicationRegistration",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST"
		                        },
		                       
		                        destroy: {
		                            url: "/user/service/editing/delete/"+$scope.domain+"",
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
		                        		 id: { editable: false,nullable: true},
		                        		 username: { type: "string", validation: { required: true } },
		                        		 appid: { type: "string", validation: { required: true } },
		                        		 formid: { type: "integer", validation: { required: true } },
		                        		 submissiondate: { type: "string", validation: { required: true } },
		                        		 statusid: { type: "integer", validation: { required: true } },
		                        		 stepid: { type: "integer", validation: { required: true } },
		                        		 approvedate: { type: "string", validation: { required: true } },		                        		 
		                        		 managerid: { type: "integer", validation: { required: true } },
		                        		 officerid: { type: "integer", validation: { required: true } },	
		                        		 lpname: { type: "string", validation: { required: true } },
		                        		 lpreg: { type: "integer", validation: { required: true } },   
		                        		 userposition: { type: "integer", validation: { required: true } },		                        		
		                        		 userid: { type: "integer", validation: { required: true } }
		                             }
		                         }
		                     },
		                    pageSize: 8,
		                    serverPaging: true,
		                    serverFiltering: true,
		                    serverSorting: true
		                },
		                filterable: { mode: "row"},
		                sortable: true,
		                //columnMenu:true, 
		                resizable: true,
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
								{ field: "username", title: "<span data-translate='Нэр'></span>"},
								{ field: "formid", title: "<span data-translate='Өргөдлийн төрөл'></span>", values:p_forms, width: "350px"},
								{template: kendo.template($("#statusid").html()), title: "<span data-translate='Өргөдлийн явц'></span>", width: "150px" },								
								{ field: "stepid", title: "<span data-translate='Шалгаж байгаа'></span>", values:$scope.step.st},
								{ field: "officerid", title: "<span data-translate='Мэргэжилтэн'></span>", values:p_specialist},
								{ field: "managerid", title: "<span data-translate='Эрхлэгч'></span>", values:p_users},
								{ field: "submissiondate", title: "<span data-translate='Бүртгэгдсэн огноо'></span>"},									
								
								{template: kendo.template($("#update").html()),  width: "230px" }
								],
	                      //editable: "inline",
	                      //autoBind: true,
		            }

        }]
    )
