angular
    .module('altairApp')
    	.controller("neditCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'ntype',
    	                           'fileUpload',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,ntype,fileUpload) { 
    	                        	   
	    	  $scope.domain="com.netgloo.models.LutNews.";
	    	  $rootScope.toBarActive = true;
	    		
	             $scope.$on('$destroy', function() {
	                 $rootScope.toBarActive = false;
	             });
	    	  
	    	  var formdataDir = new FormData();
	    	  
	    	  $('.dropify').dropify();

	            $('.dropify-fr').dropify({
	                messages: {
	                    default: 'Glissez-déposez un fichier ici ou cliquez',
	                    replace: 'Glissez-déposez un fichier ou cliquez pour remplacer',
	                    remove:  'Supprimer',
	                    error:   'Désolé, le fichier trop volumineux'
	                }
	            });
	    	  
	    	  $scope.ckeditor_content ="";

	            $scope.ckeditor_options = {
	                customConfig: '../../assets/js/custom/ckeditor_config.js'
	            }
	    	  
	            var planets_data = $scope.selectize_cat = ntype;
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
	  			$scope.formdata="";
	  			$scope.afileees=[];
	  			$scope.formdata.afiless=[];
	  			
	  			
	  			
	  			
	  			
	  			$scope.fileRemove = function(y){
             		mainService.withdomain('get', '/info/remove/newsafile/'+y).
       				then(function(data){
       					for(var i = $scope.currentInitialFiles.length - 1; i >= 0; i--) {
       					    if($scope.currentInitialFiles[i].id == y) {
       					    	$scope.currentInitialFiles.splice(i, 1);
       					    }
       					}       					
       				});
             	}
 
         	    $scope.onSuccess = function (e) {
         	    	console.log("Success (" + e.operation + ") :: " + getFileInfo(e));
         	    	$("#fileList").trigger("click");
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
                	   //$scope.formdata.afiless.push(fobj);
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
	  			 			
	  			
	  			
	  			
	  			
	  			
	  			
	  			
	  			/*$scope.onSelect = function(e) {
                    var message = $.map(e.files, function(file) { return file.name; }).join(',');
                    console.log("event :: select (" + message + ")");                   
                }
	  			$scope.onSuccess = function (e) {
         	    	console.log("Success (" + e.operation + ") :: " + getFileInfo(e));
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
                	   $scope.formdata.afiless.push(fobj);
                	   console.log($scope.formdata.afiless);
                	   $scope.afileees.push(fobj);
                       var info = file.name;

                       // File size is not available in all browsers
                       if (file.size > 0) {
                           info  += " (" + Math.ceil(file.size / 1024) + " KB)";
                       }
                       return info;
                   }).join(", ");
                }*/
         	   	
         	   
	  			 $scope.res=function(){
	   				$scope.formdata = {
	   		                "id":0,   	
	   		                "imgurl":null,
	   		                "afiless": [],
	   		            }; 	
	   				$scope.currentInitialFiles = [];
	   				console.log($scope.formdata);
	   				var formdataDir = new FormData();
	   				angular.element('.dropify-clear').triggerHandler('click');	   				   				
	   				}
	  			/*$scope.afiles=[
            		{"fileurl":"",},
                	];*/
	  			 
	  			$scope.getTheFiles = function ($files) {
	                angular.forEach($files, function (value, key) {
	                	formdataDir.append("filess", value);
	                	console.log(value);
	                	$scope.formdata.removeFile = 1; 
	                	console.log(formdataDir.append);
	                });
	            };
	            console.log(formdataDir.append);
	            $scope.delMe=function(i){
		   	    	 mainService.withdomain('delete','/info/ndelete/'+i)
		   			.then(function(){
		   				$scope.formdata = null;
		   				$(".k-grid").data("kendoGrid").dataSource.read(); 	   			   
		   				
		   			});			
		   		}
	  			 
	  			$scope.addNews = function(event) {	  
	  				
	  				$scope.formdata.afiless=$scope.currentInitialFiles;
	  				
	  				event.preventDefault();	  					 
	  			    
	  			    var x = {isfile: "false"};	  			    
	     			 formdataDir.append("filess", x);
	     			 
	  			    
	  			    var data = JSON.stringify($scope.formdata);   
	  			   
	  			    formdataDir.append("obj", data);    
	  			    console.log(formdataDir);
	  			    fileUpload.uploadFileToUrl('/info/Newsadd', formdataDir)
	   			   	.then(function(data){ 
	   			   	$state.go('restricted.pages.newslist');		   				
		   			});	
	                          
	          }
	  			
	  			$scope.update=function(vdata){
		    		  console.log(vdata)
		    		  		    		  
		    		   $scope.currentInitialFiles=[];      	
		    		  console.log(vdata.lnkNewsAfiles);
             		 for (var i = 0; i < vdata.lnkNewsAfiles.length; i++) {
             			var obj={
             					id:vdata.lnkNewsAfiles[i].id,	
             					name:vdata.lnkNewsAfiles[i].afilename,             					
             					extension:vdata.lnkNewsAfiles[i].afileext,	
             					filename:vdata.lnkNewsAfiles[i].afilename,
             					fileurl:vdata.lnkNewsAfiles[i].afileurl,
             					fsize:vdata.lnkNewsAfiles[i].afilesize	
             			};
             			$scope.currentInitialFiles.push(obj);
             		}
		    		  		    		  
		    		  
		    		  var cats = [];
		    		  var cat = "";
		    		  angular.forEach(vdata.lnkNewstypes, function(value, key) {		    			  
		    			  //console.log(vdata.lnkNewstypes.newsid);
		    			     if(value.newsid!=null){
		    			    	 cat=value.newstypeid		 						
		 					}
		    			     cats.push(cat)
		    			     console.log(cats)
		 			});    
		    		  
		    	
	              	
		    		  angular.element('.dropify-clear').triggerHandler('click');
		    		  if(vdata.showid==0){
		    			  $scope.formdata = {
			   		                "id": vdata.id,
			   		                "newstitle": vdata.newstitle,	    		           
			   		                "imgurl": vdata.imgurl,
			   		                "ishighlight": vdata.ishighlight,
			   		              	"cats": cats,  
			   		              	"newstext": vdata.newstext, 
			   		              	"isfiles": vdata.isfile, 
			   		              	"showid": true,
			   		            }; 
 		              	}
		    		  else{
		    			  $scope.formdata = {
			   		                "id": vdata.id,
			   		                "newstitle": vdata.newstitle,	    		           
			   		                "imgurl": vdata.imgurl,
			   		                "ishighlight": vdata.ishighlight,
			   		              	"cats": cats,  
			   		              	"newstext": vdata.newstext, 
			   		              	"isfiles": vdata.isfile, 
			   		              	"showid": false,
			   		            }; 
		    		  }					
		    		}
	  			
	    	  if(user_data[0].role=="ROLE_SUPER"){
	    		  $scope.puserGrid = {
			                dataSource: {			                   
			                    transport: {
			                    	read:  {
			                    		url: "/info/list/LutNews",
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
			                        		 newstitle: { type: "string", validation: { required: true } },
			                        		 newstextsh: { type: "string", validation: { required: true } },
			                        		 imgurl: { type: "string", validation: { required: true } },
			                        		 newsdate: { type: "string", validation: { required: true } },
			                        		 newstype: { type: "number", validation: { required: true } },
			                        		 ishighlight: { type: "boolean" }
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
			                toolbar: [{template: $("#Nadd").html()},"excel","pdf"],
			                pageable: {
			                    refresh: true,
			                    pageSizes: true,
			                    buttonCount: 5
			                },
			                columns: [
									{ field: "newstitle", title: "Гарчиг"},
									{ field: "newstextsh", title: "Мэдээний текст"},	
									{ field: "imgurl", title: "Зураг"},
									{ field: "newsdate", title: "Огноо"},		
									{ field: "newstype", title: "Мэдээний төрөл"},
									{ field: "ishighlight", title: "Онцгой эсэх"},		
									{ template: kendo.template($("#NUD").html()),  width: "240px"}
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
  				                    		url: "/info/list/LutNews",
  				                            contentType:"application/json; charset=UTF-8",      
  				                            data:{"custom":"where  userid="+user_data[0].id+""},
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
  				                            type:"POST"
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
  				                        		 newstitle: { type: "string", validation: { required: true } },  				                        		
  				                        		 imgurl: { type: "string", validation: { required: true } },
  				                        		 newsdate: { type: "string", validation: { required: true } },  				                        		
  				                        		 ishighlight: { type: "boolean" }
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
  				                //toolbar: [{template: $("#Nadd").html()},"excel","pdf"],
  				                pageable: {
  				                    refresh: true,
  				                    pageSizes: true,
  				                    buttonCount: 5
  				                },
  				                columns: [
  				                	{title: "#",template: "<span class='row-number'></span>", width:"60px"},
									{ field: "newstitle", title: "Гарчиг"},  										
									{template: "<div class='customer-photo'" +
                                        "style='background-image: url(#:data.imgurl#);'></div> <img style='height: 80px;' src='#:data.imgurl#'/>",
                                     field: "imgurl", title:"Зураг",width: 150},
									{ field: "newsdate", title: "Огноо"},		
									///{ field: "newstype", title: "Мэдээний төрөл"},
									{ field: "ishighlight", title: "Онцгой эсэх"},		
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
	   					  $scope.puserGrid.toolbar=[{template: $("#Nadd").html()},"excel","pdf"];
	   					}
	   					else{
	   					  $scope.puserGrid.toolbar=[{template: $("#Nadd").html()},"excel","pdf"];
	   					}
					}
					else if(data.rexport==1){
						$scope.puserGrid.toolbar=["excel","pdf"];
					}
	          if(data.rupdate==1 && data.rdelete==1){
					$scope.puserGrid.columns.push({ template: kendo.template($("#NUD").html()),  width: "240px"})
				}
				else if(data.rupdate==1 && data.rdelete==0){
					$scope.puserGrid.columns.push({ template: kendo.template($("#NU").html()),  width: "120px"})
				}
				else if(data.rupdate==0 && data.rdelete==1){
					$scope.puserGrid.columns.push({ template: kendo.template($("#ND").html()),  width: "120px"})
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
