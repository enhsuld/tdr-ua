angular
    .module('altairApp')
    	.controller("majorCtrl",['$rootScope','$timeout','$scope','cfpLoadingBar','user_data','mainService','sweet','$state','$stateParams','mainobj','Upload','fileUpload',
	        function ($rootScope,$timeout,$scope,cfpLoadingBar,user_data,mainService,sweet,$state,$stateParams,mainobj,Upload,fileUpload) {       	
    		
    		
    			$scope.mainobj=mainobj;
    			$scope.domain="com.nbb.models.fn.LnkAuditFile.";
    			
    			$scope.iscompany=user_data.iscompany;
    			    			
    			$scope.modalZegwarExcel = function(){
    				mainService.withdomain('get', '/api/files/3/'+mainobj.orgtype).
					then(function(data){
						$scope.formdata=data[0];
						modalUpdate.show();
					});
    			}
    			
    			var modalNotloh = UIkit.modal("#modal_notloh_zuil", {modal: false, keyboard: false, bgclose: false, center: false});
        	  	$scope.addnew = function(){
        	  		$scope.sendBtn=true;
        	  		modalNotloh.show();
        	  		$scope.description=null;
        	  		$scope.uploadfile=null;
        	  		progressbar.addClass("uk-hidden");
        	  	}
        	  	

    			$scope.categoryDropDownEditor = function(container, options) {
    		        var editor = $('<textarea ng-disabled="!iscompany"  class="k-textbox md-bg-red-100" style="float:left;height:100%;" data-bind="value: ' + options.field + '"></textarea>')
    		        .appendTo(container);
    		    }
    			
    			$scope.auditorEditor = function(container, options) {
    		        var editor = $('<textarea ng-disabled="iscompany"  class="k-textbox md-bg-red-100" style="float:left;height:100%;" data-bind="value: ' + options.field + '"></textarea>')
    		        .appendTo(container);
    		    }
    			
    			$scope.fileGrid = {
	                dataSource: {	                   
	                    autoSync:true,
	                	transport: {
	                    	read:  {
	                            url: "/core/list/LnkAuditFile",
	                            contentType:"application/json; charset=UTF-8",     
	                            data: {"custom":"where appid="+mainobj.id+""},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
	                            	$(".k-grid").data("kendoGrid").dataSource.read(); 
	                    		}
	                        },
	                        destroy: {
	                            url: "/core/delete/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST"
	                        },
	                        create: {
	                        	url: "/core/create/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",                                    
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
	                             	id: { editable: false,nullable: true},
	                             	filename: { type: "string",editable:false, validation: { required: true } },
	                            	mimetype: { type: "string",editable:false, validation: { required: true } },
	                             	fileurl: { type: "string",editable:false, defaultValue:'#'},
	                             }
	                         }
	                     },
	                    pageSize: 10,
	                    serverFiltering: true,
	                    serverPaging: true,
	                    serverSorting: true
	                },
	                //toolbar: ["create"],
	                //toolbar: kendo.template($("#add").html()),		
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
	                	 	  {title: "#",template: "<span class='row-number'></span>", width:"60px"},
	                          { field:"filename",template:"<a  class='uk-text-primary uk-margin-remove' href='/api/files/#:appid#/#:id#' target='_self' download='#:filename#'>#:filename#</a>", title: "Файлын нэр" },
	                          { field: "mimetype", title:"Хэмжээ", template:"<span>#:mimetype#</span>"}, 		                         
	                          { field:"description",  editor: $scope.categoryDropDownEditor, title: "Тайлбар"},
	                          { field: "comment", title:"Аудиторийн санал",  editor: $scope.auditorEditor, template:"<span class='uk-text-danger'>#:comment#</span>"},
	                          {template: kendo.template($("#downNotloh").html()), width: "200px"}
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
	                      
	          };  	
			    var $formValidatenotloh = $('#form_validation_notloh');

   	            $formValidatenotloh
   	                .parsley()
   	                .on('form:validated',function() {
   	                    $scope.$apply();
   	                })
   	                .on('field:validated',function(parsleyField) {
   	                    if($(parsleyField.$element).hasClass('md-input')) {
   	                        $scope.$apply();
   	                    }
   	                });
    			 
    			
    			$scope.submitUploadNotlohZuil = function() {
  			       $scope.sendBtn=false;
  			       if ($scope.formUpload.uploadfile.$valid && $scope.uploadfile) {
  			    	    bar.css("width", "0%").text("0%");
                         progressbar.removeClass("uk-hidden");
                         $scope.uploadNotlohZuil($scope.uploadfile, $scope.mainobj.id);
  			       }
  		        };
 	 		        
  		        $scope.uploadNotlohZuil = function (file,i) {
  		    	   var xurl="";
  		    	   if(i!=0){
  		    		   xurl ='/api/file/upload/audit/'+i;
  		    	   }
  		    	   
  		          Upload.upload({
  		              url: xurl,
  		              data: {file: file, 'description': $scope.description}
  		          }).then(function (resp) {
  		        	  progressbar.removeClass("uk-hidden");
  		              console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
  		              $(".notloh .k-grid").data("kendoGrid").dataSource.read(); 
  		              if(resp.data.excel){
  	                      UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
  	                    modalNotloh.hide();
  		              }
  		              else if(resp.data==false){
  		            	  UIkit.notify("Excel загвар тохирохгүй байна.", {status:'error'});
  		              }
  		            modalNotloh.hide();
  		          }, function (resp) {
  		              console.log('Error status: ' + resp.status);
  		          }, function (evt) {
  		              var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
  		             
  		              percent = progressPercentage;
                        bar.css("width", percent+"%").text(percent+"%");                    
  		              console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
  		          });
  		       };
  		       
    			
    			if(user_data.iscompany){
    				$scope.fileGrid.toolbar= kendo.template($("#add").html());
    				$scope.fileGrid.columns.push({ command: ["destroy"], title: "&nbsp;", width: "140px" });
    				$scope.fileGrid.editable=true;
    			}
    			else{
    				$scope.fileGrid.editable=true;
    			}
    			
    			
    			
    			$scope.domain1="com.nbb.models.fn.LnkAuditForm.";

    		    $scope.mainGridOptions = {
		          dataSource: {
		        	  autoSync: true,
		        	  transport: {
		        		   read:  {
	                            url: "/core/list/LnkAuditForm",
	                            contentType:"application/json; charset=UTF-8",     
	                            data: { "custom":"where appid="+$stateParams.issueId+" and parentid is null","sort":[{field: 'id', dir: 'asc'}]},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain1+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
		                         	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
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
                         	    id:  { nullable: false, type: "number"},
                                 parentid: { nullable: true, type: "number" },
                                 formid: { type: "number" },
                                 data1: { type: "string",editable:false },
                                 data2: { type: "string",editable:false },
                                 data3: { type: "string" ,editable:false},
                                 data4: { type: "string" },
                                 data5: { type: "string" },
                                 data6: { type: "string" },
                                 data7: { type: "string" },
                                 data8: { type: "string" ,editable:false},
                              }		                    
                         }
                     },
		            pageSize: 50,
		            serverPaging: true,
		            serverSorting: true
		          },
		          sortable: true,
		          pageable: true,
		          dataBound: function () {
		        	  
		        	  var grid =this;
		        	  grid.element.delegate("tbody>tr", "dblclick", function () {
		        	      grid.expandRow($(this));
		        	  });
  	              },
		        /*  dataBound: function() {
		            this.expandRow(this.tbody.find("tr.k-master-row").first());
		          },*/
		          columns: [
	                    { field: "data1", title: "Дугаар", width:90 },
	                    { field: "data2", title:"Чанарын баталгаажуулалтын асуулга" },
	                ],
		          editable: true
		        };
		
    		    var aj=[{"text":"Тийм","value":1},{"text":"Үгүй","value":0},{"text":"Үл хамаарах","value":2}];
    		    
		        $scope.ordersGridOptions = function(dataItem) {
		          return {
		            dataSource: {
		              autoSync: true,
		              transport: {
		            		read:  {
	            			    url: "/core/list/LnkAuditForm",
	                            contentType:"application/json; charset=UTF-8",     
	                            data: { "custom":"where appid="+$stateParams.issueId+" and data8=1","sort":[{field: 'id', dir: 'asc'}]},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/core/update/"+$scope.domain1+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
		                         	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
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
                        	    id:  { nullable: false, type: "number"},
                                parentid: { nullable: true, type: "number" },
                                formid: { type: "number" },
                                data1: { type: "string",editable:false },
                                data2: { type: "string",editable:false },
                                data3: { type: "string" ,editable:false},
                                data4: { type: "number" },
                                data5: { type: "boolean" },
                                data6: { type: "boolean" },
                                data7: { type: "string" ,editable:false},
                                data8: { type: "string" ,editable:false},
                                data9: { type: "string"},
                             }	                    
                         }
                      },
		              serverPaging: true,
		              serverSorting: true,
		              serverFiltering: true,
		              group: {
                          field: "data3"
                        },
		              pageSize: 50,
		              filter: { field: "parentid", operator: "eq", value: dataItem.formid }
		            },
		            scrollable: true,
		        //    groupable:true,
		            sortable: true,
		            pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },/*
		            dataBound: function() {
		              this.expandRow(this.tbody.find("tr.k-master-row").first());
		            },*/
		            columns: [
	                    { field: "data1", title: "№", width:50 },
	                    { field: "data2", title:"Чанарын баталгаажуулалтын асуулга", width:250},
	                    { field: "data3", title:"Бүлэг", hidden: true, width:300},
	                    { field: "data4", title:"Шийдвэр", values:aj, width:100},
	                    { field: "data9", title:"Тайлбар", editor: $scope.categoryDropDownEditor, width:250},
	                    { field: "data7", title:"Загвар татах / харах", template: kendo.template($("#downtemp").html()), attributes: {
	                        "class": "myClass",
	                        style: "text-align: right"
	                    }, width: 200},
	                    { field: "data8", title:"Маягт хавсрах", template: kendo.template($("#uptemp").html()), width: 100}
	                ],
		            editable: true
		          };
		        };
		        
		        $scope.planid=$stateParams.issueId;
    			
		        $scope.exportExcel = function(dataItem){
		        	 $rootScope.content_preloader_show();
		          	 mainService.withdomain('get','/api/excel/verify/nbb/'+dataItem.appid+'/'+dataItem.formid).then(function(response){
		           		 if(response!=false){
		           			 var link = document.createElement('a');
    	 					 link.href = '/api/excel/export/nbb/'+dataItem.appid+'/'+dataItem.formid;
    	 					 link.download = "Filename";
    	 					 link.click();	
    	 					 if(dataItem.data7=='АТ'){
    	 						 setTimeout(function(){
            	 					 $rootScope.content_preloader_hide();
    	                         }, 5000);
    	 					 }
    	 					 else{
    	 						 setTimeout(function(){
            	 					 $rootScope.content_preloader_hide();
    	                         }, 1000);
    	 					 }
    	 					
		           		 }
		           		 else{
		           			 sweet.show('Анхаар!', 'Excel тайлан оруулаагүй байна !!!', 'error');
		           			 $rootScope.content_preloader_hide();
		           		 }
		           		
		           	 });
	            }
		        $("#spreadSheetZagwarView").kendoSpreadsheet();
		        $scope.viewExcel =function(dataItem){
		        	 $rootScope.content_preloader_show();
		          	 mainService.withdomain('get','/api/excel/verify/nbb/'+dataItem.appid+'/'+dataItem.formid).then(function(response){
		           		 if(response!=false){
		           			$scope.xlsx=true;
			         	    $scope.purl='/api/excel/export/nbb/'+dataItem.appid+'/'+dataItem.formid;
			         	    var xhr = new XMLHttpRequest();
				           	xhr.open('GET',  $scope.purl, true);
				           	xhr.responseType = 'blob';
				           	 
				           	xhr.onload = function(e) {
				           	  if (this.status == 200) {
				           	    // get binary data as a response
				           	    var blob = this.response;
				           	    var spreadsheet = $("#spreadSheetZagwarView").data("kendoSpreadsheet");
				 		            spreadsheet.fromFile(blob);				 		          
							   		UIkit.modal("#modal_excel_file", {center: false}).show();
				           	  }
				           	  else{
				           		  sweet.show('Анхаар!', 'Файл устгагдсан байна.', 'error');
				           	  }
				           	};
				           	$rootScope.content_preloader_hide();
				           	xhr.send();    	 					
		           		 }
		           		 else{
		           			 sweet.show('Анхаар!', 'Excel тайлан оруулаагүй байна !!!', 'error');
		           			 $rootScope.content_preloader_hide();
		           		 }
		           		
		           	 });
	            }
		        
		    	
		        $scope.picture=function(x){    	         	   
	         	   if(x.split('.').length>0){
	         		   if(x.split('.')[x.split('.').length-1]=='jpeg' || x.split('.')[x.split('.').length-1]=='jpg' || x.split('.')[x.split('.').length-1]=='png' || x.split('.')[x.split('.').length-1]=='gif'){
	         			   return true;
	         		   }
	         		  
	         	   }
	         	   return false;
	            }
		        
		        var modalpdf = UIkit.modal("#modal_pdf", {modal: false, keyboard: false, bgclose: false, center: false});
		        
		        $("#spreadSheetFileView").kendoSpreadsheet();
		        $scope.viewExcelNotloh =function(dataItem){
		        	$scope.xlsx=false;
		        	$scope.pdf=false;
		        	$scope.img=false;
		        	if(dataItem.mimetype=='xlsx' || dataItem.mimetype=='xls'){
		        		$scope.xlsx=true;
		        		$scope.purl='/api/file/download/'+dataItem.appid+'/'+dataItem.id;
		         	    var xhr = new XMLHttpRequest();
			           	xhr.open('GET',  $scope.purl, true);
			           	xhr.responseType = 'blob';
			           	 
			           	xhr.onload = function(e) {
			           	  if (this.status == 200) {
			           	    // get binary data as a response
			           	    var blob = this.response;
			           	    var spreadsheet = $("#spreadSheetFileView").data("kendoSpreadsheet");
			 		            spreadsheet.fromFile(blob);		 		          
			 		           UIkit.modal("#modal_notloh_file", {modal: false, keyboard: false, bgclose: false, center: false}).show();
			           	  }
			           	  else{
			           		  sweet.show('Анхаар!', 'Файл устгагдсан байна.', 'error');
			           	  }
			           	};
			           	 
			           	xhr.send();
		        	}
		        	if(dataItem.mimetype=='pdf'){
		        		$scope.pdf=true;
		        		$scope.pdfurl='/api/files/'+dataItem.appid+'/'+dataItem.id;
		        		
		        		var url = '/api/files/'+dataItem.appid+'/'+dataItem.id;
		        		// The workerSrc property shall be specified.
		        		PDFJS.workerSrc = '//mozilla.github.io/pdf.js/build/pdf.worker.js';

		        		var pdfDoc = null,
		        		    pageNum = 1,
		        		    pageRendering = false,
		        		    pageNumPending = null,
		        		    scale = 0.8,
		        		    canvas = document.getElementById('the-canvas'),
		        		    ctx = canvas.getContext('2d');

		        		function renderPage(num) {
		        		  pageRendering = true;
		        		  // Using promise to fetch the page
		        		  pdfDoc.getPage(num).then(function(page) {
		        		    var viewport = page.getViewport(scale);
		        		    canvas.height = viewport.height;
		        		    canvas.width = viewport.width;

		        		    // Render PDF page into canvas context
		        		    var renderContext = {
		        		      canvasContext: ctx,
		        		      viewport: viewport
		        		    };
		        		    var renderTask = page.render(renderContext);

		        		    // Wait for rendering to finish
		        		    renderTask.promise.then(function() {
		        		      pageRendering = false;
		        		      if (pageNumPending !== null) {
		        		        // New page rendering is pending
		        		        renderPage(pageNumPending);
		        		        pageNumPending = null;
		        		      }
		        		    });
		        		  });

		        		  // Update page counters
		        		  document.getElementById('page_num').textContent = pageNum;
		        		}

		        		/**
		        		 * If another page rendering in progress, waits until the rendering is
		        		 * finised. Otherwise, executes rendering immediately.
		        		 */
		        		function queueRenderPage(num) {
		        		  if (pageRendering) {
		        		    pageNumPending = num;
		        		  } else {
		        		    renderPage(num);
		        		  }
		        		}

		        		/**
		        		 * Displays previous page.
		        		 */
		        		function onPrevPage() {
		        		  if (pageNum <= 1) {
		        		    return;
		        		  }
		        		  pageNum--;
		        		  queueRenderPage(pageNum);
		        		}
		        		document.getElementById('prev').addEventListener('click', onPrevPage);

		        		/**
		        		 * Displays next page.
		        		 */
		        		function onNextPage() {
		        		  if (pageNum >= pdfDoc.numPages) {
		        		    return;
		        		  }
		        		  pageNum++;
		        		  queueRenderPage(pageNum);
		        		}
		        		document.getElementById('next').addEventListener('click', onNextPage);

		        		/**
		        		 * Asynchronously downloads PDF.
		        		 */
		        		PDFJS.getDocument(url).then(function(pdfDoc_) {
		        		  pdfDoc = pdfDoc_;
		        		  document.getElementById('page_count').textContent = pdfDoc.numPages;

		        		  // Initial/first page rendering
		        		  renderPage(pageNum);
		        		});
		        		
		        		modalpdf.show();		        		
		        	}
		        	if($scope.picture(dataItem.filename)){
		        		$scope.img=true;
		        		$scope.purl='/api/files/'+dataItem.appid+'/'+dataItem.id;
		        		UIkit.modal("#modal_notloh_file", {modal: false, keyboard: false, bgclose: false, center: false}).show();
		        	}
	            }
		        
		        var $formValidate = $('#form_validation');

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
	         
	            
	            

	            $scope.selectize_val_options = [
	                { value: 'Нийт орлого', label: 'Нийт орлого' },
	                { value: 'Нийт зардал', label: 'Нийт зардал' },
	                { value: 'Нийт хөрөнгө', label: 'Нийт хөрөнгө' },
	                { value: 'Цэвэр хөрөнгө', label: 'Цэвэр хөрөнгө' }
	            ];

	            $scope.selectize_val_config = {
	                maxItems: 1,
	                valueField: 'value',
	                labelField: 'label',
	                create: false,
	                placeholder: 'Данс сонгох...',
	                onChange: function() {
	                    $timeout(function() {
	                        $formValidate.parsley().validate();
	                    })
	                }
	            };
	            
	            var modal_a_4 = UIkit.modal("#modal_a_4", {modal: false, keyboard: false, bgclose: false, center: false});
		        
		        $scope.selectA4 = function(x,y){
		        	
		        	$scope.sendBtn=true;
		        	$rootScope.content_preloader_show();           
			       	mainService.withdomain('get','/api/excel/a4/'+$scope.planid).then(function(response){
		           		modal_a_4.show();
		           		$rootScope.content_preloader_hide();           
		           		$scope.a4=response;
		           	});
	        	}
		        
		        
		        $scope.selectB31 = function(x,y){
		        	
		        	$scope.sendBtn=true;
		        //	$rootScope.content_preloader_show(); 
		        	
		        	$state.go('restricted.work.accSurvey',{planid:$scope.planid,formid:y});
		        	
			       /*	mainService.withdomain('get','/api/excel/a4/'+$scope.planid).then(function(response){
		           		$rootScope.content_preloader_hide();           
		           		$scope.a4=response;
		           	});*/
	        	}
		        
		        
	            $scope.submitA4 = function() {
			       $scope.sendBtn=false;
			       console.log($scope.a4data);
			       mainService.withdata('post','/api/excel/a4/'+$scope.planid, $scope.a4data).then(function(response){
			    	   console.log("asd");
		           });
		        };
		        
		        
		        var modal_upload = UIkit.modal("#modal_excel_upload", {modal: false, keyboard: false, bgclose: false, center: false});
		        $scope.uploadExcel = function(x,y){
		        	 //cfpLoadingBar.start();
		        	 $scope.sendBtn=true;
		        	 progressbar.addClass("uk-hidden");
		        	 modal_upload.show();
		        	 $scope.noteid=y;
	        	}
		        
		    	
			    $scope.submitUpload = function() {
			       $scope.sendBtn=false;
			       if ($scope.formUpload.uploadfile.$valid && $scope.uploadfile) {
			    	   bar.css("width", "0%").text("0%");
                       progressbar.removeClass("uk-hidden");
			           $scope.upload($scope.uploadfile, $scope.noteid);
			       }
		        };
		     
    		    
    			var select = UIkit.uploadSelect($("#file_upload-select"), settings),
                drop   = UIkit.uploadDrop($("#file_upload-drop"), settings);
 
			    $('.dropify').dropify();

			    $('.dropify-mn').dropify({
	                messages: {
	                    default: 'Excel тайлан оруулна',
	                    replace: 'Excel тайлан шинээр оруулах бол энд дарна уу',
	                    remove:  'Солих',
	                    error:   'Алдаа үүслээ'
	                }
	            });

			    $scope.fileChange = function(){
			    	$scope.errList=[];
			    }
			    var modal = UIkit.modal("#modal_header_footer_print", {modal: false, keyboard: false, bgclose: false, center: false});
				$scope.modalExcel =function(){		
    				modal.show();
    				$scope.sendBtn=true;
    				$scope.file=null;
    				$scope.ars=[];
    				progressbar.addClass("uk-hidden");
    			}
				
			   $scope.submit = function() {
				   $scope.sendBtn=false;				  
			       if ($scope.form.file.$valid && $scope.file) {
			    	   bar.css("width", "0%").text("0%");
                       progressbar.removeClass("uk-hidden");
			           $scope.upload($scope.file,0);
			       }
		       };

		      // upload on file select or drop
		       $scope.upload = function (file,i) {
		    	   var xurl="";
		    	   if(i==0){
		    		   xurl ='/api/excel/upload/zagwarExcel/'+$stateParams.issueId;
		    	   }
		    	   else{
		    		   xurl ='/api/excel/upload/form/'+$stateParams.issueId+'/'+i;
		    	   }
		    	   
		           Upload.upload({
		              url: xurl,
		              data: {file: file, 'username': $scope.username}
		           }).then(function (resp) {
		        	  progressbar.removeClass("uk-hidden");
		              console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
		              $scope.errList=resp.data;
		              if(resp.data.excel){
	                      UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                      modal.hide();
		              }
		              else if(resp.data==false){
		            	  UIkit.notify("Excel загвар тохирохгүй байна.", {status:'error'});
		            	  $scope.sendBtn=true;
		              }
		              else if(resp.data.excel==false && resp.data.support==false){
		            	  UIkit.notify("Excel загвар тохирохгүй байна. "+resp.data.sheetname+"", {status:'error'});
		            	  $scope.sendBtn=true;
		              }
		              else{
		            	  console.log(resp);
		            	  $scope.errorAccount=true;
		            	  if(resp.data.support){
		            		  UIkit.notify("Алдаа үүслээ.", {status:'warning'});		            		 		            		
		            	  }
		            	  else{
		            		  console.log(resp);
		            		  if(resp.data!=null){
		            			  $scope.forms=resp.data;
		            			  angular.forEach($scope.forms, function(value, key){
		            				  UIkit.notify(value.fname+ " маягтыг амжилттай хавсарлаа.", {status:'success'});
            			          });
		            			//  console.log($scope.ordersGridOptions.return.dataSource.filter);
		            			 /* $scope.ordersGridOptions.return.dataSource.transport.read.data={
		      	  	    				"custom":"where appid= '"+$stateParams.issueId+"' " 
		      	  	    		  }*/
		      						
		            			  
		            			  $(".k-grid").data("kendoGrid").dataSource.read();
		            		  }
		            		  else{
		            			  UIkit.notify("Excel загвар тохирохгүй байна.", {status:'error'});
		            		  }		            		 
		            		  progressbar.addClass("uk-hidden");
		            	  }
		            	  $scope.ars=resp.data.error;
		            	  $scope.sendBtn=true;
		            	//  modal.hide();
			             // modal_upload.hide();
		              }
		              progressbar.addClass("uk-hidden");
		          }, function (resp) {
		              console.log('Error status: ' + resp.status);
		          }, function (evt) {
		              var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
		             
		              percent = progressPercentage;
                      bar.css("width", percent+"%").text(percent+"%");                    
		              console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
		          });
		       };

		       
		        $scope.categoryDropDownEditor = function(container, options) {
			        var editor = $('<textarea cols="30" rows="2" class="k-textbox md-bg-red-100" style="float:left;height:100%;" data-bind="value: ' + options.field + '"></textarea>')
			        .appendTo(container);
			    }
   			        
   			$scope.errorAccount=false;        
   			//$scope.ars=[];
   		    var progressbar = $(".file_upload-progressbar"),
               bar         = progressbar.find('.uk-progress-bar'),
               settings    = {

                   action: '/api/excel/upload/zagwarExcel/'+$stateParams.issueId, // upload url

                   allow : '*.(xlsx,xls)', // allow only images

                   loadstart: function() {
                       bar.css("width", "0%").text("0%");
                       progressbar.removeClass("uk-hidden");
                   },

                   progress: function(percent) {
                       percent = Math.ceil(percent);
                       bar.css("width", percent+"%").text(percent+"%");
                   },

                   allcomplete: function(data) {

                       bar.css("width", "100%").text("100%");

                       setTimeout(function(){
                           progressbar.addClass("uk-hidden");
                       }, 250);
                       if(JSON.parse(data).excel){
                       	modal.hide();
                       }
                       else{
                           $scope.errorAccount=true;
                           UIkit.notify("Алдаа үүслээ.", {status:'warning'});
                           $scope.ars=JSON.parse(data).error;
                           modal.hide();
                       }
                   }
               };
    		
   		    
   		    
   		    
    	 }
    ]);
