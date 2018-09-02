angular
    .module('altairApp')
    	.controller("appFormCtrl", [
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
    	                           'app_data',
    	                           'p_manager',
    	                           'p_specialist',
    	                           'user_data',
	        function ($scope,$rootScope,utils,$state,$stateParams,$window,$timeout,form_data,sweet,mainService,fileUpload,app_data,p_manager,p_specialist,user_data) { 
    	        
        	  
        	   $('.dropify').dropify();

                	    
	    	    $scope.form=form_data;
	    	  
	            $scope.notes_data = form_data;
	            $scope.manager=p_manager;
	            $scope.specialist=p_specialist;
	            	            
	            $scope.user_data = user_data[0];
	            
	            // hide note form
	            $scope.noteActive = false;

	            $scope.specialist = {
	                    options:  $scope.specialist
	                };
	            
	            $scope.manager = {
	                    options: $scope.manager
	                };
	            

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
                
                $scope.disselect=false;
                $scope.app=app_data[0];
                
                $scope.data={
                		managerid:0,
                		officerid:0
                }
                
                if($scope.app.statusid==0 || $scope.app.statusid==2){
                	$scope.xoff=true;
                }
                else{
                	$scope.data.managerid=$scope.app.managerid;
                	$scope.data.officerid=$scope.app.officerid;
                	$scope.xoff=false;
                	$scope.disselect=true;
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
                
                
                $scope.loadOnSend = function() {
                	   mainService.withdata('put','/izr/user/application/send/'+$stateParams.id, $scope.data)
		   		   			.then(function(data){
		   		   				if(data){
		   			   				sweet.show('Мэдээлэл', 'Амжилттай илгээлээ.', 'success');
		   			   			    $state.go('restricted.pages.claim');	
		   		   				}
		   			   				
		   	   			});	

                };
                
                
	            // open a note
	            $scope.openNote = function($event,$parentIndex,$index,note) {
	                $event.preventDefault();

	                $scope.noteActive = true;

	                $('.notes_list').children('li').removeClass('md-list-item-active');

	                $($event.currentTarget).parent('li').addClass('md-list-item-active');
	               
	                angular.element('.md-card-content .dropify-clear').triggerHandler('click');
	                
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
				   				angular.element('.md-card-content .dropify-clear').triggerHandler('click');
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
	            
	            $scope.master = {};
	            
	            
	            
	            $scope.saveAttach = function($event) {
	                 $event.preventDefault();    
	                 var parentIndex = $scope.note_form.parentIndex,
	                 index = $scope.note_form.index,
	                 title = $scope.note_form.title;
	                 content = $scope.note_form.content;
	              //   file_form = $scope.noteForm.atfile;
	                 planid = $stateParams.param;
	                 var data = JSON.stringify($scope.note_form, null, 2);   

	                 $scope.noteForm = angular.copy($scope.master);
	                 angular.element('.dropify-clear').triggerHandler('click');
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
		   					
		   					sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		   				}
				   				
		   			});	                
	             }
	            
	            $scope.delete = function(item){
	            	console.log(item);
	            	var parentIndex = $scope.note_form.parentIndex,
	                index = $scope.note_form.index;         
	            	
			        sweet.show({
			        	   title: 'Баталгаажуулалт',
	   		            text: 'Та энэ файлыг устгахдаа итгэлтэй байна уу?',
	   		            type: 'warning',
	   		            showCancelButton: true,
	   		            confirmButtonColor: '#DD6B55',
	   		            confirmButtonText: 'Тийм',
			    	    cancelButtonText: 'Үгүй',
	   		            closeOnConfirm: false,
	   		            closeOnCancel: false,
			            showLoaderOnConfirm: true
			          
			        }, function(inputvalue) {
			        	 if (inputvalue) {
			        		 mainService.withdomain('delete','/izr/user/application/removeAttach/'+item.id)
	 				   			.then(function(){
	 				   			  var index = -1;
	 				   			  for (var i = 0; i < $scope.note_form.lnkAppAttachedFiles.length; i++) {
	 				   			    if ($scope.note_form.lnkAppAttachedFiles[i].id == item.id) {
		 				   			      index = i;
					   			      }
	 				   			  }
	 				   			  $scope.note_form.lnkAppAttachedFiles.splice(index,1);
	 				   			  sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
					   			});	
	 		            }else{
	 		                sweet.show('Анхаар!', 'Устгах үйлдэл хийгдсэнгүй!!!', 'error');
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
