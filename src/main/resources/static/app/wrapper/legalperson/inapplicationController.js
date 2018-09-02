angular
    .module('altairApp')
    	.controller("inapplicationCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'p_apps',
    	                           'sweet',
    	                           'mainService',    	                         
    	                           'p_specialist',
    	                           'p_pos',
	        function ($scope,$rootScope,$state,$timeout,p_apps,sweet,mainService,p_specialist,p_pos) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.CApplicationRegistration.";

	    	  $scope.apps=p_apps;
	    	  
	    	  $scope.update=function(item){
	    		  console.log(item);
	    			$state.go('restricted.pages.applicationFormManager',{userid:item.userid,param:item.formid,id:item.id});
	    			    	    	
	    	  },
	    	  
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
	    	  
	          $scope.pmenuGrid = {
		                dataSource: {
		                   
		                    transport: {
		                    	read:  {
		                            url: "/izr/user/angular/ApplicationRegistrationPri",
		                            contentType:"application/json; charset=UTF-8",                                    
		                            type:"POST"
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
		                        		 appid: { type: "integer", validation: { required: true } },
		                        		 formid: { type: "integer", validation: { required: true } },
		                        		 submissiondate: { type: "string", validation: { required: true } },
		                        		 statusid: { type: "integer", validation: { required: true } },
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
		                toolbar: ["excel","pdf"],
		                excel: {
		                	allPages: true,
		                    fileName: "Users.xlsx",
		                    filterable: true
		                },
		                pdf: {
		                    allPages: true,
		                    fileName: "Users.pdf"
		                },
		                filterable: {
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
								{ field: "username", title: "<span data-translate='Нэр'></span>"},		
								{ field: "submissiondate", title: "<span data-translate='Бүртгэгдсэн огноо'></span>"},
								{template: kendo.template($("#statusid").html()), title: "<span data-translate='Өргөдлийн явц'></span>", width: "150px" },
								{ field: "stepid", title: "<span data-translate='Шалгаж байгаа'></span>", values:$scope.step.st},			
								{ field: "approvedate", title: "<span data-translate='Шийдвэрлэсэн огноо'></span>"},								
								{ field: "officerid", title: "<span data-translate='Мэргэжилтэн'></span>", values:p_specialist},																
								{ field: "userposition", title: "<span data-translate='Албан тушаал'></span>", values:p_pos},								
								{template: kendo.template($("#check").html()),  width: "130px" }
								//{ command: ["edit", "destroy"], title: "&nbsp;", width: "270px" }
	                      ]
		            }

        }]
    )
