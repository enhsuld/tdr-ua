angular
    .module('altairApp')
    	.controller("appFormManCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           'utils',
    	                           '$state',
    	                           '$stateParams',
    	                           '$window',
    	                           '$timeout',
    	                           'form_data',
    	                           'sweet',
    	                           'mainService',
    	                           'fileUpload',
    	                           'commentor',
    	                           'user_data',
	        function ($scope,$rootScope,utils,$state,$stateParams,$window,$timeout,form_data,sweet,mainService,fileUpload,commentor,user_data) { 
    	        
        	  
    	                        	   
        	    $('.dropify').dropify();
	    	    $scope.form=form_data;
	    	  
	            $scope.notes_data = form_data;
	            
	            $('.dropify-fr').dropify({
	                messages: {
	                    default: 'Файлаа сонгоно уу',
	                    replace: 'Файлаа солих',
	                    remove:  'Болих',
	                    error:   'Алдаа үүслээ'
	                }
	            });
	            	            
	            $scope.user_data = user_data[0];
	            
	            // hide note form
	            $scope.noteActive = false;

	            $scope.specialist = {
	                    options:  $scope.specialist
	                };
	            
	            $scope.manager = {
	                    options: $scope.manager
	                };
	            
	            $scope.ujson=commentor;
	            console.log($scope.ujson);

                $scope.expertConfig = {
                    create: false,
                    maxItems: 1,
                    placeholder: 'Мэргэжилтэн сонгох',
                    optgroupField: 'parent_id',
                    optgroupLabelField: 'text',
                    optgroupValueField: 'value',
                    valueField: 'value',
                    labelField: 'text',
                    searchField: 'text'
                };
                
                $scope.managerConfig = {
                    create: false,
                    maxItems: 1,
                    placeholder: 'Менежер сонгох',
                    optgroupField: 'parent_id',
                    optgroupLabelField: 'text',
                    optgroupValueField: 'value',
                    valueField: 'value',
                    labelField: 'text',
                    searchField: 'text'
                };
                
                
                
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
                
                var $formSub = $('#form_sub');

                $formSub
                    .parsley()
                    .on('form:validated',function() {
                        $scope.$apply();
                    })
                    .on('field:validated',function(parsleyField) {
                        if($(parsleyField.$element).hasClass('md-input')) {
                            $scope.$apply();
                        }
                    });
                
                
                $scope.loadOnSend = function() {
                	   mainService.withdata('put','/izr/user/application/send/'+$stateParams.id, $scope.data)
		   		   			.then(function(data){
		   		   				if(data){
		   			   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		   			   				init();
		   		   				}
		   			   				
		   	   			});	

                };
                
                $scope.manager = {
                    options: [
						{
						    id: 1,
						    title: "Зөвшөөрөх",
						    value: "2",
						    parent_id: 1
						},
                        {
                            id: 2,
                            title: "Багшруу буцаах",
                            value: "0",
                            parent_id: 1
                        }
                    ]
                };
                
                $scope.directorOptions = {
                        options: [                            
                            {
                                id: 1,
                                title: "Илгээх",
                                value: "3",
                                parent_id: 1
                            },
                            {
                                id: 2,
                                title: "Багшруу буцаах",
                                value: "0",
                                parent_id: 1
                            }
                        ]
                    };
                
                $scope.officer= {
                        options: [
                           
                            {
                                id: 1,
                                title: "Хэлэлцэгдэх шатанд",
                                value: "4",
                                parent_id: 1
                            },
                            {
                                id: 2,
                                title: "Багшруу буцаах",
                                value: "0",
                                parent_id: 1
                            }
                        ]
                    };
                
                $scope.director=false;
            	$scope.other=false;
            	$scope.last=false;
            	
            	console.log($scope.notes_data);

            	$scope.selectize_a_data={
            		options:[]
            	}
            	     
                // date range
                var $dp_start = $('#uk_dp_start'),
                    $dp_end = $('#uk_dp_end');

                var start_date = UIkit.datepicker($dp_start, {
                    format:'DD.MM.YYYY'
                });

                var end_date = UIkit.datepicker($dp_end, {
                    format:'DD.MM.YYYY'
                });

                $dp_start.on('change',function() {
                    end_date.options.minDate = $dp_start.val();
                });

                $dp_end.on('change',function() {
                    start_date.options.maxDate = $dp_end.val();
                });
                
                            	
                if($scope.ujson[0].position==117 && $scope.notes_data.stepid==1){
                	$scope.selectize_a_data= $scope.manager;
                	$scope.director=false;
                	$scope.other=true;
                }
                else if($scope.ujson[0].position==127 && $scope.notes_data.stepid==2){
                	console.log($scope.selectize_a_data);
                	$scope.selectize_a_data= $scope.directorOptions;
                	console.log($scope.selectize_a_data);
                	$scope.director=true;
                	$scope.other=false;
                }
                else if($scope.ujson[0].position==167 && $scope.notes_data.stepid==3){
                	$scope.selectize_a_data= $scope.officer;
                	$scope.director=false;
                	$scope.other=true;
                }
                else if($scope.ujson[0].position==168 && $scope.notes_data.stepid==4 && $scope.notes_data.statusid==1){
                	$scope.selectize_a_data= $scope.officer;
                	$scope.director=false;
                	$scope.last=true;
                	
                }
                else if($scope.ujson[0].position==168 && $scope.notes_data.statusid==4){
                	$scope.selectize_a_data= $scope.officer;
                	$scope.director=false;
                	$scope.last=false;
                	
                }
                    $scope.selectize_a_config = {
                        create: false,
                        maxItems: 1,
                        placeholder: 'Сонголт...',
                        optgroupField: 'parent_id',
                        optgroupLabelField: 'title',
                        optgroupValueField: 'ogid',
                        valueField: 'value',
                        labelField: 'title',
                        searchField: 'title'
                    };
                
            	$scope.des={
    	    			appid:$stateParams.id
    	    	}
    	    	
    	    	$scope.submitMainDesicion = function(event) {	
            		  event.preventDefault();
                      
    	    		   mainService.withdata('put','/izr/user/application/submitMainComment', $scope.des)
    		   			.then(function(data){	
    		   				console.log(data);
    		   				if($scope.ujson[0].position==117){
    		   					$state.go('restricted.pages.incomeApps');	
    		   				}
    		   				else if($scope.ujson[0].position==127){
    		   					$state.go('restricted.pages.incomeAppsDir');	
    		   				}
    		   				else if($scope.ujson[0].position==167){
    		   					$state.go('restricted.pages.decidedAppsSp');	
    		   				}
    		   				$scope.notes_data.lnkMainComment.push(data);
    		   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');	
    		   				
    	   			});	 
                             
                }
            	
               $scope.submitLast = function(event) {	
          		   event.preventDefault();
  	    		   mainService.withdata('put','/izr/user/application/submitMainDecision', $scope.des)
  		   			.then(function(data){	
  		   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');	
  		   				$state.go('restricted.pages.decidedAppsMb');	
  		   			});	 
                           
              }
            	
            	var formdataDir = new FormData();
       	        
 	            $scope.getTheUFiles = function ($files) {
 	                angular.forEach($files, function (value, key) {
 	                	formdataDir.append("files", value);
 	                });
 	            };
            	
            	$scope.submitDirector = function(event) {	
          		   event.preventDefault();
          	       
    	            var ruleno = $scope.des.ruleno,
                    appstatus = $scope.des.appstatus,
    	            comtext = $scope.des.comtext,
                    appid = $scope.des.appid;
    	            
    	            var data = JSON.stringify($scope.des, null, 2);   
    	            
    	            formdataDir.append("obj", data);    	            
    	            
	                fileUpload.uploadFileToUrl('/izr/user/application/saveDirComment', formdataDir)
	              	.then(function(data){  
		   				if(data){	   				
		   					$scope.notes_data.lnkMainComment.push(data);
		   					formdataDir = new FormData();
		   					$scope.des={};
		   					if($scope.ujson[0].position==117){
    		   					$state.go('restricted.pages.incomeApps');	
    		   				}
    		   				else if($scope.ujson[0].position==127){
    		   					$state.go('restricted.pages.incomeAppsDir');	
    		   				}
		   					angular.element('.dropify-clear').triggerHandler('click');
		   					sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		   				}
				   				
		   			});	
                           
                }
                
	            // open a note
	            $scope.openNote = function($event,$parentIndex,$index,note) {
	                $event.preventDefault();

	                $scope.noteActive = true;

	                $('.notes_list').children('li').removeClass('md-list-item-active');

	                $($event.currentTarget).parent('li').addClass('md-list-item-active');
	               
	                $scope.note_form = {
	                    parentIndex: $parentIndex,
	                    index: $index,
	                    id: note.id,
	                    title: note.title,
	                    filesize:note.filesize,
	                    isfile:note.isfile,
	                    planid:$stateParams.id,
	                    lnkAppNotes: note.lnkAppNotes,
	                    lnkAppAttachedFiles: note.lnkAppAttachedFiles,
	                    content:''
	                };
	                
	                if(note.lnkAppNotes.length>0){
	                	$scope.note_form.content=note.lnkAppNotes[0].info
	                }
	                
	                $('.dropify').dropify();
	                angular.element($window).resize();
	            };

	            
	            // save note
	            $scope.saveNote = function($event) {
	                $event.preventDefault();         
	                // get variables from active note
	                var parentIndex = $scope.note_form.parentIndex,
	                    index = $scope.note_form.index,
	                    title = $scope.note_form.title;
	                    content = $scope.note_form.content;
	                    planid = $stateParams.id;

	                var data = JSON.stringify($scope.note_form, null, 2);
	                
	                mainService.withdata('put','/izr/user/application/saveNote', data)
			   			.then(function(data){
			   				if(data){
				   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
				   				//init();
				   				console.log(data);
				   				console.log($scope.note_form.lnkAppNotes);
				   			    $scope.note_form.lnkAppNotes.splice(index, 1);
				   			    
				   			    $scope.note_form.lnkAppNotes.push(data);
				   			    console.log($scope.note_form.lnkAppNotes);
			   				}
				   				
		   			});          
	            }
	            
	            var formdata = new FormData();
	            $scope.getTheFiles = function ($files) {
	                angular.forEach($files, function (value, key) {
	                    formdata.append("files", value);
	                });
	            };
	            $scope.saveAttach = function($event) {
	                 $event.preventDefault();    
	                 var parentIndex = $scope.note_form.parentIndex,
	                 index = $scope.note_form.index,
	                 title = $scope.note_form.title;
	                 content = $scope.note_form.content;
	              //   file_form = $scope.noteForm.atfile;
	                 planid = $stateParams.param;
	                 var data = JSON.stringify($scope.note_form, null, 2);               
	                 formdata.append("obj", data);
	                 if($scope.note_form.isform==1){
	                	 $scope.note_form.images.splice(index, 1)
	                 }
	                 fileUpload.uploadFileToUrl('/izr/user/application/saveAttach', formdata)
	              	.then(function(data){  
		   				if(data){	   				
		   					
		   					$scope.note_form.lnkAppAttachedFiles.splice(index, 1);
		   					$scope.note_form.lnkAppAttachedFiles.push(data);
		   					//init();
		   					formdata = new FormData();
		   					angular.element('.dropify-clear').triggerHandler('click');
		   					angular.element('.lala .dropify-clear').triggerHandler('click');
		   					angular.element('.last1 .dropify-clear').triggerHandler('click');
		              		angular.element('.last2 .dropify-clear').triggerHandler('click');
		              		angular.element('.last3 .dropify-clear').triggerHandler('click');
		              		angular.element('.last4 .dropify-clear').triggerHandler('click');
		              		angular.element('.last5 .dropify-clear').triggerHandler('click');
		              		angular.element('.last6 .dropify-clear').triggerHandler('click');
		   					sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		   				}
				   				
		   			});	                
	             }
	            
	            
	            
	            var lpreg=$scope.user_data.id;
	            
	            $scope.tooloptions = {

        			tools: [
        				"bold","italic","underline","strikethrough","justifyLeft","justifyCenter","justifyRight","justifyFull",
        				"insertUnorderedList","insertOrderedList","indent","outdent","createLink","unlink","insertImage","insertFile",
        				"subscript","superscript","createTable","addRowAbove","addRowBelow","addColumnLeft","addColumnRight","deleteRow",
        				"deleteColumn","viewHtml","formatting","cleanFormatting","fontName","fontSize","foreColor","backColor"
        			],

        		imageBrowser: {
        			 messages: {
                         dropFilesHere: "Drop files here"
                        },
                        transport: {
               				read: {
               					url:"/imagebrowser/read",	        			
               						type: "GET",
               						data: {"lpreg":lpreg},
               						dataType: "json"
               					},
               				destroy: {
               					url:"/imagebrowser/destroy/"+lpreg,	        			
               						type: "POST",
               						dataType: "json"
               					},
           					uploadUrl:"/imagebrowser/upload/"+lpreg,	
               				thumbnailUrl: "/imagebrowser/thumbnail",
               				imageUrl:function(e){
               					return "/" + e;
               				}
           				 },
    				},
    				fileBrowser: {
                        messages: {
                            dropFilesHere: "Drop files here"
                        },
                        transport: {
                    		 read: {
                    			 	url:"/imagebrowser/read/file",	        			
       	                        type: "POST",
       	                        dataType: "json"
       	        			},
                            destroy: {
                                url: "/imagebrowser/destroy",
                                type: "POST"
                            },
                        	 create: "/imagebrowser/create",
                        	 fileUrl:function(e){
        	        		    return "/" + e;
        	        		 }, 
                            uploadUrl: "/imagebrowser/upload"
                       }
                    }
        		};

        }]
    )
