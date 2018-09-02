angular
    .module('altairApp')
    .controller('issuesCtrl', [
        '$rootScope',
        '$scope',
        '$state',
        '$stateParams',
        'utils',
        '$window',
        'variables',
        'mainobj',
        'au_levels',
        'worklist',
        'user_data',
        'step_data',
        'mainService',
        'sweet',
        'fileUpload',
        'downloadService',
        '$ocLazyLoad',
        function ($rootScope,$scope,$state,$stateParams,utils,$window,variables,mainobj,au_levels,worklist,user_data,step_data,mainService,sweet,fileUpload,downloadService,$ocLazyLoad) {
        	
        	$("#input-file-a").on("change", function() {    		     
                $scope.loadedFile = this.files[0];
            });
        	
        	angular.element($window).resize();
        	 
            $rootScope.pageHeadingActive = true;
            var formdataDir = new FormData();
            $('.dropify').dropify();
            $("#spreadSheetView").kendoSpreadsheet();
            $("#spreadSheetFileView").kendoSpreadsheet();
            $('.dropify-fr').dropify({
                messages: {
                    default: 'Файлаа сонгоно уу',
                    replace: 'Өөр файлаар солих',
                    remove:  'Болих',
                    error:   'Алдаа үүслээ'
                }
            });
            
            $('.dropify-staus').dropify({
                messages: {
                    default: 'Файлаа сонгоно уу',
                    replace: 'Өөр файлаар солих',
                    remove:  'Болих',
                    error:   'Зөвхөн MS Excel 2007 болон түүнээс хойшхи хувилбараар үүсгэсэн .xlsx өргөтгөлтэй файлыг хавсаргана уу!'
                }
            });
            
            $scope.picture=function(x){
         	   
         	   if(x.split('.').length>0){
         		   if(x.split('.')[x.split('.').length-1]=='jpeg' || x.split('.')[x.split('.').length-1]=='jpg' || x.split('.')[x.split('.').length-1]=='png' || x.split('.')[x.split('.').length-1]=='gif'){
         			   return true;
         		   }
         		  
         	   }
         	   return false;
            }
            $scope.excelChecker=function(x){
          	   
          	   if(x.split('.').length>0){
          		   if(x.split('.')[x.split('.').length-1]=='xlsx' || x.split('.')[x.split('.').length-1]=='xls'){
          			   return true;
          		   }
          		  
          	   }
          	   return false;
             }
          
                
        	$scope.categoryDropDownEditor = function(container, options) {
		        var editor = $('<textarea cols="30" rows="4" class="k-textbox md-bg-red-100" style="float:left;height:100%;" data-bind="value: ' + options.field + '"></textarea>')
		        .appendTo(container);
		    }
        	
            $scope.stepdata=step_data;
            //console.log(step_data);
            
            $scope.project=mainobj;
            var c=0;
            c=parseFloat(Math.round((mainobj.aper+mainobj.a2per+mainobj.a3per+mainobj.mper+mainobj.m2per+mainobj.m3per+mainobj.tper+mainobj.t2per+mainobj.t3per)/9 * 100) / 100).toFixed(2);
            
            $scope.mainPercent=c;
            
            $scope.tabs=au_levels;
            
            $scope.stepid=$stateParams.stepid;
            
            $scope.worklist=worklist;
            angular.forEach($scope.worklist,function(value,index){
            	  angular.forEach($scope.stepdata,function(val,ind){
                     if(value.id==val.wid){
                    	 value.step=val.stepid;
                     }
                  });
            })
            
            function color(){
            	 angular.forEach($scope.worklist,function(value,index){
                	 if(value.parentid==0){
               		  var tr=0;
               		  var ct=0;
               		  var dc=0;
               		  var wc=0;
               		  var pc=0;
               		  angular.forEach($scope.worklist,function(val,ind){
               			  if(value.id==val.parentid){
               				  tr=tr+1;
               				  if(val.step>=3){
               					  ct=ct+1;
               				  }
               				  if(val.step>=4){
               					  wc=wc+1;
              				  }
               				  if(val.step>=5){
              					  pc=pc+1;
             				  }
               				  if(val.step==1){
             					  dc=dc+1;
            				  }
               				  value.haschild=true;
               			  }               			 
               		  })
               		  if(ct==tr && dc==0){
               			  value.allchildtrue=3;
               		  }
               		  if(wc==tr && dc==0){
             			  value.allchildtrue=4;
             		  }
               		  if(pc==tr && dc==0){
            			  value.allchildtrue=5;
            		  }
               		  if(tr>0 && dc>0){
               			 value.allchildtrue=1;
               		  }
               	  }            
                })
            }
            
            color();
            
            $scope.user=user_data[0];
            
            $scope.excel={
            		sheetName:'',
            		furl:''
            }
            $scope.file=false;
            $scope.editable=false;
            
            var newStep=0;            
            $scope.openNote = function($event,$parentIndex,$index,note) {
                $event.preventDefault();
                $scope.noteActive = true;
                $scope.sstepid=note.levelid;
                newStep=note.levelid;
                $('.notes_list').children('li').removeClass('md-list-item-active');

                $($event.currentTarget).parent('li').addClass('md-list-item-active');
        
                $scope.note_form = {
                    parentIndex: $parentIndex,
                    index: $index,
                    id: note.id,
                    title: note.workname,
                    step: note.step,
                    isscore: note.isscore,
                    content: note.content,
                    fileurl:note.fileurl,
                    isfile:note.isfile,
                    mid: $stateParams.issueId,
                    wid: note.id,
                    role:$scope.user.role,
                    fname: note.fname,
                    levelid:note.levelid
                };
                                   
                if($scope.note_form.fileurl=='b1'){
                	$scope.excel.sheetName='Б-1';
                }
                else if($scope.note_form.fileurl=='b11'){
                	$scope.excel.sheetName='Б-1.1';
                }
                else if($scope.note_form.fileurl=='b12'){
                	$scope.excel.sheetName='Б-1.2';
                }
                else if($scope.note_form.fileurl=='b13'){
                	$scope.excel.sheetName='Б-1.3';
                }
                else if($scope.note_form.fileurl=='b14'){
                	$scope.excel.sheetName='Б-1.4';
                }
                else if($scope.note_form.fileurl=='b15'){
                	$scope.excel.sheetName='Б-1.5';
                }
                else if($scope.note_form.fileurl=='b23'){
                	$scope.excel.sheetName='Б-2.3';
                }
                else if($scope.note_form.fileurl=='b24'){
                	$scope.excel.sheetName='Б-2.4';
                }
                else if($scope.note_form.fileurl=='b41'){
                	$scope.excel.sheetName='Б-4.1';
                }
                else if($scope.note_form.fileurl=='b42'){
                	$scope.excel.sheetName='Б-4.2';
                }
                else if($scope.note_form.fileurl=='b43'){
                	$scope.excel.sheetName='Б-4.3';
                }
                else if($scope.note_form.fileurl=='b51'){
                	$scope.excel.sheetName='Б-5.1';
                }
                else if($scope.note_form.fileurl=='b52'){
                	$scope.excel.sheetName='Б-5.2';
                }
                else if($scope.note_form.fileurl=='b22'){
                	$scope.excel.sheetName='Б-2.2';
                }
                else if($scope.note_form.fileurl=='wordPlan'){
                	$scope.excel.sheetName='Б-2.2';
                }
                else if($scope.note_form.fileurl=='b3'){
                	$scope.excel.sheetName='Б-3';
                }
                else if($scope.note_form.fileurl=='b21'){
                	$scope.excel.sheetName='Б-2.1';
                }
                else if($scope.note_form.fileurl=='gb1'){
                	$scope.excel.sheetName='В-1';
                }
                else if($scope.note_form.fileurl=='gb11'){
                	$scope.excel.sheetName='В-1.1';
                }
                else if($scope.note_form.fileurl=='gb21'){
                	$scope.excel.sheetName='В-2';
                }
                else if($scope.note_form.fileurl=='gb31'){
                	$scope.excel.sheetName='В-3.1';
                }
                else if($scope.note_form.fileurl=='gb41'){
                	$scope.excel.sheetName='В-4.1';
                }
                else if($scope.note_form.fileurl=='gb42'){
                	$scope.excel.sheetName='В-4.2';
                }
                else if($scope.note_form.fileurl=='gb43'){
                	$scope.excel.sheetName='В-4.3';
                }
                else if($scope.note_form.fileurl=='gb44'){
                	$scope.excel.sheetName='В-4.4';
                }
                else if($scope.note_form.fileurl=='gb45'){
                	$scope.excel.sheetName='В-4.5';
                }
                else if($scope.note_form.fileurl=='gb46'){
                	$scope.excel.sheetName='В-4.6';
                }
                else if($scope.note_form.fileurl=='gb461'){
                	$scope.excel.sheetName='В-4.6.1';
                }
                else if($scope.note_form.fileurl=='gb462'){
                	$scope.excel.sheetName='В-4.6.2';
                }
                else if($scope.note_form.fileurl=='gb463'){
                	$scope.excel.sheetName='В-4.6.3';
                }
                else if($scope.note_form.fileurl=='gb464'){
                	$scope.excel.sheetName='В-4.6.4';
                }
                else if($scope.note_form.fileurl=='gb465'){
                	$scope.excel.sheetName='В-4.6.5';
                }
                else if($scope.note_form.fileurl=='gb5155'){
                	$scope.excel.sheetName='В-5';
                }
                else if($scope.note_form.fileurl=='gb72'){
                	$scope.excel.sheetName='В-7.2';
                }
                $scope.excel.furl=$scope.note_form.fileurl;
                $scope.message.wid=note.id;
                $scope.work.wid=note.id;
                $scope.form.wid=note.id;
                $scope.attach.wid=note.id;
                angular.element($window).resize();
                if($scope.user.role=='ROLE_AUDIT' && $scope.note_form.isscore=="true" && $scope.note_form.step==0){ 
                	$scope.audit=true;
                	$scope.editable=true;
                }
                else if($scope.user.role=='INDP' && $scope.note_form.isscore=="true" && $scope.note_form.step==0){  
                	$scope.audit=true;
                	$scope.editable=true;
                }
                else if($scope.user.role=='INDP' && $scope.note_form.isscore=="true" && $scope.note_form.step==1){  
                	$scope.audit=true;
                	$scope.editable=true;
                }
                else if($scope.user.role=='ROLE_AUDIT' && $scope.note_form.isscore=="true" &&  $scope.note_form.step==1){  
                	$scope.audit=true;
                	$scope.editable=true;
                }
                else if($scope.user.role=='ROLE_AUDIT' && $scope.note_form.isscore=="false"){  
                	$scope.file=true;
                }
                else if($scope.user.role=='ROLE_FIRST'  && $scope.note_form.step==3){
                	$scope.manager=true;
                }
                else if($scope.user.role=='ROLE_SECOND'  && $scope.note_form.step==4){
                	$scope.terguuleh=true;
                }  
                else if($scope.user.role=='ROLE_SECOND'  && $scope.note_form.step==5){
                	$scope.terguuleh=true;
                }
                else{
                	$scope.audit=false;
                	$scope.manager=false;
                	$scope.terguuleh=false;
                }
                $scope.showBtn=true;
                if(note.parentid=="0"){
                	$scope.showBtn=false;
                }
                
                $scope.notscored=false;
                if($scope.user.role=='ROLE_AUDIT' && note.haschild==false && $scope.note_form.isscore=="false" && ($scope.note_form.step==0 || $scope.note_form.step==1)){  
                	$scope.notscored=true;
                	$scope.audit=false;
                }
              
                
                mainService.withdata('put','/work/3/imp', $scope.message)
		   			.then(function(data){
		   				if(data){
		   					$scope.message.comment='';
		   					$scope.mlist=data;
		   					$scope.mlist_5count = 0;
		   					$scope.mlist_2count = 0;
		   					for(var i=0; i<$scope.mlist.length;i++){
		   						if ($scope.mlist[i].positionid == 5){
		   							$scope.mlist_5count++;
		   						}
		   						else if ($scope.mlist[i].positionid == 2){
		   							$scope.mlist_2count++;
		   						}
		   					}
		   				}
		   				else{
			   				sweet.show('Анхаар', 'Error...', 'error');
		   				}
	   			});
            };
        
     	
            
            $scope.excelDownload=function(){
                $("#spreadsheet").kendoSpreadsheet();
                
                var spreadsheet = $("#spreadsheet").data("kendoSpreadsheet");
                var modalfull = UIkit.modal("#modal_full");
            	  mainService.withdata('put','/excel/download/'+$scope.mid, $scope.excel)
            	  .then(function (data, status, headers, config) {
					    spreadsheet.fromFile(data[0].xlsx);
				     	 modalfull.show();
					});
            }
            
            $scope.gg2selectconfig = {
	                create: false,
	                maxItems: 1,
	                placeholder: 'Сонгох...',
	                optgroupField: 'parent_id',
	                optgroupLabelField: 'text',
	                optgroupValueField: 'ogid',
	                valueField: 'value',
	                labelField: 'text',
	                searchField: 'text'
	             };
            
            $scope.gg2selectreporttypeconfig = {
	                create: false,
	                maxItems: 1,
	                placeholder: 'Тайлангийн хэлбэр сонгох',
	                optgroupField: 'parent_id',
	                optgroupLabelField: 'text',
	                optgroupValueField: 'ogid',
	                valueField: 'value',
	                labelField: 'text',
	                searchField: 'text'
	             };
            
            $scope.gg2selectoptions=[{"text":"Тийм","value":"1"},
				{"text":"Үгүй","value":"2"}
			];
            
            $scope.gg2selectersdeloptions=[{"text":"Эрсдэлтэй","value":"1"},
				{"text":"Эрсдэлгүй","value":"2"}
			];
            
            $scope.gg2selectshaardlagaloptions=[{"text":"Шаардлагатай","value":"1"},
				{"text":"Шаардлагагүй","value":"2"}
			];
            
            $scope.gg2reporttypeoptions=[{"text":"Зөрчилгүй санал дүгнэлт","value":"1"},
				{"text":"Хязгаарлалттай санал дүгнэлт","value":"2"},
				{"text":"Сөрөг санал дүгнэлт","value":"3"},
				{"text":"Санал дүгнэлт өгөхөөс татгалзах","value":"4"}
			];
            
            $scope.showGG1 = function(){
            	mainService.withdomain("GET","/au/resource/formg1/"+$stateParams.issueId).then(function(response){
            		$scope.gg1 = response;
            		UIkit.modal("#modal_gg1").show();
            	})
            }
            
            $scope.submitGG1 = function(gg1){
            	gg1.mid = $stateParams.issueId;
            	mainService.withdata("POST","/au/submit/formg1",gg1).then(function(response){
            		if (response){
            			sweet.show('Мэдээлэл', 'Амжилттай хадгалагдлаа.', 'success');
            			UIkit.modal("#modal_gg1").hide();
            		}
            	});
            }
            
            $scope.showGG2 = function(){
            	mainService.withdomain("GET","/au/resource/formg2/"+$stateParams.issueId).then(function(response){
            		$scope.gg2 = response;
            		if ($scope.project.reporttype != null && $scope.project.reporttype != undefined){
            			$scope.gg2.reporttype = $scope.project.reporttype;
            		}
            		UIkit.modal("#modal_gg2").show();
            	})
            }
            
            $scope.submitGG2 = function(gg2){
            	gg2.mid = $stateParams.issueId;
            	mainService.withdata("POST","/au/submit/formg2",gg2).then(function(response){
            		if (response){
            			sweet.show('Мэдээлэл', 'Амжилттай хадгалагдлаа.', 'success');
            			//UIkit.modal("#modal_gg2").hide();
            		}
            	});
            }
            
            $scope.stepUpAll=function(i){
            	sweet.show({
		        	   title: 'Баталгаажуулалт',
 		            text: 'Та энэ үйлдлийг хийхдээ итгэлтэй байна уу?',
 		            type: 'warning',
 		            showCancelButton: true,
 		            confirmButtonColor: '#DD6B55',
 		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
 		            closeOnConfirm: false,
 		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 mainService.withdomain('put','/work/stepup/all/'+$stateParams.issueId+'/'+i)        	     
		 	   			.then(function(data){
		 	   				if(data){
		 	   					
		 	   					$scope.audit=false;
		 	                	$scope.manager=false;
		 	                	$scope.terguuleh=false;
		 	                	
		 	                	angular.forEach($scope.worklist, function(value, key){
		 	                	      if(value.id == data.parentid)
		 	                	         value.step=data.pstep;
		 	                	   });
		 	                	
		 	                	if(i==3){
		 	                		sweet.show('Мэдээлэл', 'Гүйцэтгэлийн шатанд шилжлээ.', 'warning');
		 	                	}
		 	                	else if(i==4){
		 	                		sweet.show('Мэдээлэл', 'Тайлагналын шатанд шилжлээ.', 'warning');
		 	                	}
		 	                	else if(i==5){
		 	                		sweet.show('Мэдээлэл', 'Тайлагналын дараах шатанд шилжлээ.', 'success');
		 	                	}
		 	                	else{
		 			   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		 	                	}
		 		   				init();
		 		   				color();
		 	   				}
		 	   				else{
		 	   					modalwork.hide();
		 		   				sweet.show('Анхаар', 'Error...', 'error');
		 	   				}
		  	   			});	
		        	
		            }else{
		                sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
		            }    		
		        });
            }
            
            $scope.stepUp=function(){
            	sweet.show({
		        	   title: 'Баталгаажуулалт',
 		            text: 'Та энэ үйлдлийг хийхдээ итгэлтэй байна уу?',
 		            type: 'warning',
 		            showCancelButton: true,
 		            confirmButtonColor: '#DD6B55',
 		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
 		            closeOnConfirm: false,
 		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 mainService.withdata('put','/work/stepup', $scope.note_form)        	     
		 	   			.then(function(data){
		 	   				if(data){
		 	   					
		 	   					$scope.worklist[$scope.note_form.index].step=data.stepid;
		 	   					$scope.audit=false;
		 	                	$scope.manager=false;
		 	                	$scope.terguuleh=false;
		 	                	
		 	                	angular.forEach($scope.worklist, function(value, key){
		 	                	      if(value.id == data.parentid)
		 	                	         value.step=data.pstep;
		 	                	   });
		 	                	
		 	                	if($scope.stepid==3){
		 	                		
		 	                		if(data.levelid==4){
		 	                			sweet.show('Мэдээлэл', 'Гүйцэтгэлийн шатанд шилжлээ.', 'warning');
		 	                		}	                		
		 	                		else{
		 	                			sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		 	                		}
		 	                	}
		 	                	else if($scope.stepid==4){
		 	                		if(data.levelid==5){
		 	                			sweet.show('Мэдээлэл', 'Тайлагналын шатанд шилжлээ.', 'warning');
		 	                		}	      
		 	                		else{
		 	                			sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		 	                		}
		 	                	}
		 	                	else if($scope.stepid==5){
		 	                		if(data.levelid==6){
		 	                			sweet.show('Мэдээлэл', 'Тайлагналын дараах шатанд шилжлээ.', 'success');
		 	                		}	 
		 	                		else{
		 	                			sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		 	                		}
		 	                	}
		 	                	else{
		 			   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
		 	                	}
		 		   				init();
		 		   				color();
		 	   				}
		 	   				else{
		 	   					modalwork.hide();
		 		   				sweet.show('Анхаар', 'Error...', 'error');
		 	   				}
		  	   			});	
		        	
		            }else{
		                sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
		            }    		
		        });
            }
        	$scope.exportExcel21 = function(x,y,z){
	           	 $rootScope.content_preloader_show();           
	           	 mainService.withdomain('post','/excel/verify/sublicense/'+z).then(function(response){
	           		 if(response.multiple){
	           			 sweet.show('Анхаар!', 'Байгууллагын код давхардсан байна. Үйлчлүүлэгч байгууллагаа шалгана уу !!!', 'error');
							 $rootScope.content_preloader_hide();
	           		 }
	           		 if(response.single){
	           			 mainService.withdomain('get','/my/exportOrgInfo/'+y+'/'+z)
	         				.then(function(response){
	         					 var link = document.createElement('a');
	     	 					 link.href = '/my/exportOrgInfo/'+y+'/'+z;
	     	 					 link.download = "Filename";
	     	 					 link.click();	
	     	 					 $rootScope.content_preloader_hide();
	            			});
	           		 }
	           		 if(response.nofound){
	           			 sweet.show('Анхаар!', ''+mainobj.orgcode+' кодтой байгуууллага олдсонгүй. Үйлчлүүлэгч байгууллагаа шалгана уу !!!', 'error');
							 $rootScope.content_preloader_hide();
	           		 }
	           	 });
            }
            $scope.exportExcelSheet = function(x,y,z,s){
	           	 $rootScope.content_preloader_show();           
	           	 mainService.withdomain('post','/excel/verify/sublicense/'+z).then(function(response){
	           		 if(response.multiple){
	           			 sweet.show('Анхаар!', 'Байгууллагын код давхардсан байна. Үйлчлүүлэгч байгууллагаа шалгана уу !!!', 'error');
							 $rootScope.content_preloader_hide();
	           		 }
	           		 if(response.single){
	           			 mainService.withdomain('get','/excel/'+x+'/'+y+'/'+s+'/'+z)
	         				.then(function(response){
	         					 var link = document.createElement('a');
	     	 					 link.href = '/excel/'+x+'/'+y+'/'+s+'/'+z;
	     	 					 link.download = "Filename";
	     	 					 link.click();	
	     	 					 $rootScope.content_preloader_hide();
	            			});
	           		 }
	           		 if(response.nofound){
	           			 sweet.show('Анхаар!', ''+mainobj.orgcode+' кодтой байгуууллага олдсонгүй. Үйлчлүүлэгч байгууллагаа шалгана уу !!!', 'error');
							 $rootScope.content_preloader_hide();
	           		 }
	           	 });
            }
            
            $scope.exportExcelWord = function(x,y,z){
            	 $rootScope.content_preloader_show();           
            	 mainService.withdomain('post','/excel/verify/sublicense/'+z).then(function(response){
            		 if(response.multiple){
            			 sweet.show('Анхаар!', 'Байгууллагын код давхардсан байна. Үйлчлүүлэгч байгууллагаа шалгана уу !!!', 'error');
 						 $rootScope.content_preloader_hide();
            		 }
            		 if(response.single){
            			 mainService.withdomain('get','/excel/'+x+'/'+y+'/'+z)
          				.then(function(response){
          					 var link = document.createElement('a');
      	 					 link.href = '/excel/'+x+'/'+y+'/'+z;
      	 					 link.download = "Filename";
      	 					 link.click();	
      	 					 $rootScope.content_preloader_hide();
             			});
            		 }
            		 if(response.nofound){
            			 sweet.show('Анхаар!', ''+mainobj.orgcode+' кодтой байгуууллага олдсонгүй. Үйлчлүүлэгч байгууллагаа шалгана уу !!!', 'error');
						 $rootScope.content_preloader_hide();
            		 }
            	 });
            }
            
            $scope.downloadReport5 = function(mid){
            	mainService.withdomain("POST","/excel/check/auditAttachment/"+mid).then(function(response){
            		if (response){
            			$scope.exportExcelWord('report5xlsx','Report',mid);
            		}
            		else{
            			sweet.show('Анхаар', 'Тайлан татаагүй байна.', 'error');
            		}
            	})
            }
            
            $scope.stepDown=function(){
            	sweet.show({
		        	   title: 'Баталгаажуулалт',
 		            text: 'Та энэ үйлдлийг хийхдээ итгэлтэй байна уу?',
 		            type: 'warning',
 		            showCancelButton: true,
 		            confirmButtonColor: '#DD6B55',
 		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
 		            closeOnConfirm: false,
 		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 mainService.withdata('put','/work/stepdown', $scope.note_form)        	     
		 	   			.then(function(data){
		 	   				if(data){
		 	   					$scope.worklist[$scope.note_form.index].step=data.stepid;
		 	   					$scope.audit=false;
		 	                	$scope.manager=false;
		 	                	$scope.terguuleh=false;
		 	                	sweet.show('Анхаар!', 'Амжилттай буцлаа.', 'success');
		 		   				init();
		 	   				}
		 	   				else{
		 		   				sweet.show('Анхаар', 'Error...', 'error');
		 	   				}
		  	   			});	
		        	
		            }else{
		                sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
		            }    		
		        });
            }
            
            var a1 = UIkit.modal("#modal_a1");

            $scope.showData=function(i){
            	if(i==1){
            		a1.show();
            		var sel=[{"text":"Бага","value":"Бага"},{"text":"Дунд","value":"Дунд"},{"text":"Их","value":"Их"}];
            	   $scope.domain="com.netgloo.models.FsFormA1.";
         	       $scope.puserGrid = {
		                dataSource: {
		                    autoSync: true,
		                    transport: {
		                    	read:  {
		                            url: "/au/list/FsFormA1",
		                            contentType:"application/json; charset=UTF-8",           
		                            data:{"sort":[{'field':'id','dir':'asc'}]},
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
		                             	recid: { type: "number",  editable: false },	                             	
		                             	data1: { type: "string", editable: false,},
		                             	data2: { type: "string"},
		                             	data3: { type: "string"},
		                             }	                    
		                         }
		                     },
		                     batch: true,
		                    pageSize: 50,
		                    serverPaging: true,
		                    serverSorting: true,
		                    serverFiltering: true
		                },
   			             
                         filterable: {
                        	 mode: "row"
                         },
		                sortable: true,
		                resizable: true,
		                toolbar: ["save", "cancel","excel"],
		                pageable: {
		                    refresh: true,
		                    pageSizes: true,
		                    buttonCount: 5
		                },
		                columns: [
		                          { field:"recid", title: "№", width: 60},      
		                          { field:"data1", title: "Байгууллагын үйл ажиллагааг зохицуулж буй хууль тогтоомж, эрх зүйн баримт бичгүүд"},
		                          { field:"data2", title: "Хамааралтай хууль эрх зүйн онцлог", width: 200 },		   			                         
		                          { field:"data3", title: "Эрсдлийн нөлөөллийг тодорхойлох /бага, дунд, их/",values:sel,width: 100},	                      
   			                      ],
   			                   editable: $scope.editable
		            };
            	}
            	else if(i==5){
                    var tradinational = UIkit.modal("#modal_tradinational");
            		tradinational.show();
             		var sel=[{"text":"Бага","value":"Бага"},{"text":"Дунд","value":"Дунд"},{"text":"Их","value":"Их"}];
             	    $scope.domain="com.netgloo.models.FsFormA1.";
          	        $scope.puserGrid = {
 		                dataSource: {
 		                    autoSync: true,
 		                    transport: {
 		                    	read:  {
 		                            url: "/au/list/FsFormA1",
 		                            contentType:"application/json; charset=UTF-8",           
 		                            data:{"sort":[{'field':'id','dir':'asc'}]},
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
 		                             	recid: { type: "number",  editable: false },	                             	
 		                             	data1: { type: "string", editable: false,},
 		                             	data2: { type: "string"},
 		                             	data3: { type: "string"},
 		                             }	                    
 		                         }
 		                     },
 		                     batch: true,
 		                    pageSize: 50,
 		                    serverPaging: true,
 		                    serverSorting: true,
 		                    serverFiltering: true
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
 		                resizable: true,
 		                toolbar: ["save", "cancel","excel"],
 		                pageable: {
 		                    refresh: true,
 		                    pageSizes: true,
 		                    buttonCount: 5
 		                },
 		                columns: [
 		                          { field:"recid", title: "№", width: 60},      
 		                          { field:"data1", title: "Байгууллагын үйл ажиллагааг зохицуулж буй хууль тогтоомж, эрх зүйн баримт бичгүүд"},
 		                          { field:"data2", title: "Хамааралтай хууль эрх зүйн онцлог", width: 200 },		   			                         
 		                          { field:"data3", title: "Эрсдлийн нөлөөллийг тодорхойлох /бага, дунд, их/",values:sel,width: 100},	                      
    			                      ],
    			                      editable: $scope.editable
 		            };
            	}
            }
            
            
            
         	function init(){
	    		 mainService.withdomain('get','/au/withid/MainAuditRegistration/'+$stateParams.issueId )
		   			.then(function(data){
		   			    $scope.project=data[0];
		   			    mainobj=data[0];
		   			    c=parseFloat(Math.round((mainobj.aper+mainobj.a2per+mainobj.a3per+mainobj.mper+mainobj.m2per+mainobj.m3per+mainobj.tper+mainobj.t2per+mainobj.t3per)/9 * 100) / 100).toFixed(2);
		   	            
		   	            $scope.mainPercent=c;
		   			 /*   angular.forEach(tabs,function(value,index){
		   			    	$('#tabid'+value.ext).removeClass('uk-disabled');		   			    	
		                })*/
		   			});	
	    	}
         	
            $scope.message={
            		mid:$stateParams.issueId,
            		recipient:user_data[0].gname            		
            }
            
            $scope.work={
            		mid:$stateParams.issueId,
            		recipient:user_data[0].gname            		
            }
            
            $scope.form={
            		mid:$stateParams.issueId,
            		recipient:user_data[0].gname            		
            }
            
            $scope.attach={
            		mid:$stateParams.issueId,
            		noteid:0,
            		recipient:user_data[0].gname            		
            }
            
            
            $scope.ckeditor_options = {
                customConfig: '../../assets/js/custom/ckeditor_config.js'
            }
            
            
            // work
            
            var modalrisk = UIkit.modal("#modal_risk");
           
            
            var modalUrisk = UIkit.modal("#modal_u_risk");
           
           
            $scope.showUlamjlaltRiskGuitsetgel=function(){
            	 $state.go('restricted.pages.mainworkriskG',{issueId:$stateParams.issueId,stepid:$stateParams.stepid,typeid:$stateParams.typeid,wstep:$scope.note_form.step});	        
            }
            
            
    	    
            $scope.showRisk=function(){
            	 /*mainService.withdomain('get','/au/resource/LutFactorCriterion')
	 				.then(function(data){
	    			    $scope.crits=data;	  	
	    			});	*/
	             mainService.withdomain('get','/au/resource/LutRisk')
	 			.then(function(data){
	    			     $scope.risknames=data;	  	
	    			});	
	     	    mainService.withdomain('get','/au/resource/LutAuditDir')
	 				.then(function(data){
	 			    $scope.dirs=data;	   			 
	 			});	
            	modalrisk.show();                  
	          	mainService.withdomain('get','/au/resource/LutGroupOfFactor')
		   			.then(function(data){
		   				mainService.withdomain('get','/au/resource/LutRisk')
			   			.then(function(rdata){
			   				if(data){
			   					$scope.gfactor=data;	
			   					$scope.risks=rdata;
		  	   					$scope.domain="com.netgloo.models.LnkMainFormT2.";
		  	   					var decision=[{"text":"Хамааралгүй","value":"0"},{"text":"Эрсдэлтэй","value":"1"},{"text":"Эрсдэлгүй","value":"2"}];	
		  	   				   	   					
		  	   					$scope.riskGrid = {
			 		                dataSource: {
			 		                    autoSync: true,
			 		                    transport: {
			 		                    	read:  {
			 		                            url: "/au/list/LutFactor",
			 		                            contentType:"application/json; charset=UTF-8",    
			 		                            data:{"mid":$stateParams.issueId,"isactive":1,"custom":"where isactive=1 and tryid!=0","sort":[{field: "groupid", dir: "asc"}]},
			 		                            type:"POST"
			 		                        },
			 		                        update: {
			 		                            url: "/work/update/"+$scope.domain+"",
			 		                            contentType:"application/json; charset=UTF-8",
			 		                            data:{"mid":$stateParams.issueId},
			 		                            type:"POST",
			 		                            complete: function(e) {
			 		                            	if(e.responseText=="tryNotFound"){			 		                            		
			 		                            		UIkit.notify("Алдаа үүслээ. Тухайн чиглэлд горим бүртгэгдээгүй байна.", {status:'warning'});
			 		                            	}else{
			 		                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
			 		                            	}
			 		                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
			 		                    		}
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
			 		                             	fnumber: { type: "string",  editable: false },	                             	
			 		                             	factorname: { type: "string", editable: false,},
			 		                             	groupid: { type: "number", editable: false},
			 		                             	dirid: { type: "number",editable: false},
			 		                             	decid: { type: "number"},
			 		                             	critid: { type: "number",editable: false},
			 		                             	riskid: { type: "number",editable: false},
			 		                             	risknames: { type: "string"},
			 		                             	risks: {},
			 		                             	description: { type: "string"}
			 		                             }	                    
			 		                         }
			 		                     },
			 		                     group: {
			                                  field: "groupid",values:$scope.gfactor
			                                },
			 		                    pageSize: 20,
			 		                    serverPaging: true,
			 		                    serverSorting: true,
			 		                    serverFiltering: true
			 		                },
			 		                sortable: true,
			 		                resizable: true,
			 		                toolbar: kendo.template($("#b22Export").html()),
			 		                pageable: {
			 		                    refresh: true,
			 		                    pageSizes: true,
			 		                    buttonCount: 5
			 		                },
			 		                excel: {
		    	   		                fileName: "Organization Export.xlsx",
		    	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
		    	   		                filterable: true,
		    	   		                allPages: true
		    	   		            },
			 		                columns: [
		 		                          { field:"fnumber", title: "№", width: 70,filterable:false},      
		 		                          { field:"factorname", title: "Хүчин зүйлсийн жагсаалт", width:300},			 		                         
		 		                          { field:"critid", title: "Аудитын чиглэлд нөлөөлж болзошгүй хүчин зүйлсэд хамаарах шалгуур үзүүлэлт", template: "#=criname#", width:300},
		 		                          { field:"riskid",width: 300, title: "Илрүүлсэн эрсдэл",values:rdata},
		 		                          { field:"dirid", title: "Аудитын чиглэл", values:$scope.dirs, width: 200 },
		 		                          { field:"decid" ,values:decision, title: "Тийм/Үгүй",width: 150},
		 		                          { field:"description", title: "Нотлох зүйлийн ишлэл", editor: $scope.categoryDropDownEditor, template:"# if (description != null) { # <span class='tulgaltRed'>#:description#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
		 		                          { field:"groupid",title: "Хүчин зүйлс", values:$scope.gfactor,hidden:true },		
		 		                         
		 			                      ],
			 			                  editable: $scope.editable
			 		            };
		  	   				 var multiSelectArrayToString = function (item) {
		  	   					 console.log(item);
			  	   		        return item.risks.join(', ');
			  	   		    };
		  	   		    
			  	   		      
				  	   		    function riskDropDownEditor(container, options) {
				  	   		    	 $("<select multiple='multiple' data-bind='value : risks'/>")
					                 .appendTo(container,options)
					                 .kendoMultiSelect({
					 	                autoBind: true,
					 	                dataSource: {
					 		                transport: {
					 		                	 read: "/work/resource/riskid/"+options.model.dirid+""
					 		                }
					 	                }
					                 });
					             }
		  	   		      
		  	   					function critDropDownEditor(container, options) {
		  	   						console.log(options);
					                 $('<input  ng-click="checkboxClicked(dataItem)" data-bind="value:' + options.field + '"/>')
					                 .appendTo(container,options)
					                 .kendoDropDownList({
					 	                autoBind: true,
					 	                dataTextField: "text",
					 	                dataValueField:"value",
					 	                dataSource: {
					 		                transport: {
					 		                	 read: "/work/resource/critid/"+options.model.id+""
					 		                }
					 	                }
					                 });
					             }
		  	   				 
					       	     function categoryDropDownEditor(container, options) {
					             	console.log(container);
					                 $('<input required data-bind="value:' + options.field + '"/>')
					                 .appendTo(container)
					                 .kendoDropDownList({
					 	                autoBind: true,
					 	                dataTextField: "text",
					 	                dataValueField:"value",
					 	                dataSource: {
					 		                transport: {
					 		                	 read: "/au/resource/LutAuditDir",
					 		                	 parameterMap: function(options) {
				 		                       	 return JSON.stringify(options);
				 		                       }
					 		                }
					 	                }
					                 });
					             }	   				 
			   					}
			   				else{
				   				sweet.show('Анхаар', 'Error...', 'error');
			   				}
		   				});			   			
	  			});
               
            }
           
            var modal_mate = UIkit.modal("#modal_mate");
            $scope.fdata = {};
            $scope.hi = function(dataitem){
            	var mate=[{"text":"Нийт орлого","value":"1"},{"text":"Нийт зардал","value":"2"},{"text":"Нийт хөрөнгө","value":"3"},{"text":"Цэвэр хөрөнгө ","value":"4"}];	
            	//var data={};
            	$scope.ddata = dataitem;
            	$scope.ddata.isselect = 1;
            	$scope.selectedUzuulelt = dataitem.id;
            	console.log($scope.ddata);
            	$scope.ddata.percent = $scope.fdata.percent;
            	/*mainService.withdata('post', '/my/selectFsFormA4Percent/'+$stateParams.issueId+"/" +$scope.sstepid,$scope.ddata).then(function(data){
    				
    			});*/
            	$scope.changeee($scope.ddata);
            	
            }
         
            $scope.changeee = function(data){
            	if (Number(data.percent) >= 0.2 && Number(data.percent) <= 2){
            		data.data3 = data.data2/100*data.percent;
                	data.planid=$stateParams.issueId;
                	data.stepid=$scope.sstepid;
                	data.id = $scope.selectedUzuulelt;
                	mainService.withdata('put','/my/updateFsFormA4', data)
    	   			.then(function(data){
    	   				$("#notificationSuccess").trigger('click');
            			$(".k-grid").data("kendoGrid").dataSource.read();
    	   			});
            	}
            	
            }
            
            $scope.submitDescription = function(ddata){
            	console.log(ddata);
            	ddata['percent'] = $scope.fdata.percent;
            	mainService.withdata("post","/work/update/com.netgloo.models.FsFormA4.",ddata).then(function(data){
            		console.log(data);
            		if (data == true){
            			$("#notificationSuccess").trigger('click');
            			$(".k-grid").data("kendoGrid").dataSource.read();
            		}
            	});
            }
            
            $scope.showCompute=function(){
            	modal_mate.show();     
            	$scope.fdata = {};
            	mainService.withdomain('get', '/my/getSelectFsFormA4/FsFormA4/'+$stateParams.issueId+'/'+$scope.sstepid).
    			then(function(data){
    				var mate=[{"text":"Нийт орлого","value":"1"},{"text":"Нийт зардал","value":"2"},{"text":"Нийт хөрөнгө","value":"3"},{"text":"Цэвэр хөрөнгө ","value":"4"}];
    				
    				if(data.length>0){
    					
    					$scope.fdata=data[0];
    					
    					$scope.hi($scope.fdata);
        				$scope.selectedUzuulelt = $scope.fdata.id;
    				}
    			});
	   					var mate=[{"text":"Нийт орлого","value":"1"},{"text":"Нийт зардал","value":"2"},{"text":"Нийт хөрөнгө","value":"3"},{"text":"Цэвэр хөрөнгө ","value":"4"}];	
	   	            	
	   	            	$scope.domain1="com.netgloo.models.FsFormA4.";
	   	            	$scope.pdataGrid = {
	   			                dataSource: {
	   			                   
	   			                    transport: {
	   			                    	read:  {
	   			                    		url: "/my/list/FsFormA4m",
	   			                            contentType:"application/json; charset=UTF-8",     
	   			                            data:{"mid":""+$stateParams.issueId+"","stepid":""+$scope.sstepid+""},
	   			                            type:"POST"
	   			                        },
	   			                        update: {
	   			                            url: "/info/update/"+$scope.domain1+"",
	   			                            contentType:"application/json; charset=UTF-8",                                    
	   			                            type:"POST"
	   			                        },
	   			                        destroy: {
	   			                            url: "/info/delete/"+$scope.domain1+"",
	   			                            contentType:"application/json; charset=UTF-8",                                    
	   			                            type:"POST",
	   			                            complete: function(e) {
	   			                            	 $("#notificationDestroy").trigger('click');
	   			                    		}
	   			                        },
	   			                        create: {
	   			                            url: "/info/create/"+$scope.domain1+"",
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
	   			                        		 data1: { type: "number",validation: { required: true } },
	   			                        		 data2: { type: "number",validation: { required: true } },   
	   			                        		 data3: { type: "number",validation: { required: true }}, 
	   			                        		 data4: { type: "number",validation: { required: true } },     
	   			                        		 data5: { type: "number",validation: { required: true } },    
	   			                        		 data6: { type: "string",validation: { required: true } },
	   			                        		 orgcode: { type: "string", validation: { required: true } },
	   			                        		 orgcatid: { type: "string", validation: { required: true } },
	   			                        		 cyear: { type: "string",validation: { required: true } }
	   			                             }
	   			                         }
	   			                     },
	   			                     group: {
	   	                                 field: "cyear"
	   	                               },
	   			                    pageSize: 5,
	   			                    serverPaging: true,
	   			                    serverFiltering: true,
	   			                    serverSorting: true
	   			                },
	   			                filterable: false,
	   			                sortable: true,
	   			                columnMenu:false, 
	   			                resizable: true,
	   			                
	   			                pageable: {
	   			                    refresh: true,
	   			                    pageSizes: true,
	   			                    buttonCount: 5
	   			                },
	   			                columns: [
	   									{ field: "data1", title: "Материаллаг байдлын суурь үзүүлэлт" +"<span data-translate=''></span>",values:mate},
	   									{ field: "data2", title: "Санхүүгийн тайлангийн дүн" +"<span data-translate=''></span>",format: "{0:n}"},
	   									{ field: "data3", title: "Түвшин 0.2%" +"<span data-translate=''></span>",format: "{0:n}"},
	   									{ field: "data4", title: "Сонгосон түвшин" +"<span data-translate=''></span>",format: "{0:n}"},
	   									{ field: "data5", title: "Түвшин 2%" +"<span data-translate=''></span>",format: "{0:n}"},
	   									{ field: "cyear", title: "Тайлант он" +"<span data-translate=''></span>",hidden:true },
	   									{template: kendo.template($("#example").html()), title:"Сонгох",width:150}
	   									
	   		                      ],
	   		                      editable: $scope.editable,
	   		                      autoBind: true,
	   			            }
	   				
            };
         // work
            
            var modalrisksummary = UIkit.modal("#modal_risk_summary");
            
            $scope.showRiskSummary=function(){
            	modalrisksummary.show();  
              	$scope.LnkRiskT2="com.netgloo.models.LnkRiskT2.";
					var bdata = [{"text":"Тийм","value":true},{"text":"Үгүй","value":false}];
					var rtype = [{"text":"ТАБ-СТА-Б-2.2","value":0},{"text":"ТАБ-СТА-Б-2.3","value":2},{"text":"ТАБ-СТА-Б-2.5","value":1}];
					var data1=[{"text":"Сонгоно уу","value":"0"},{"text":"1-Ховор тохиолдох","value":"1"},{"text":"2-Тохиолдох магадлалтай","value":"2"},{"text":"3-Тохиолдох боломжтой","value":"3"},{"text":"4-Тохиолдох боломж маш өндөр","value":"4"},{"text":"5-Тохиолдохоос зайлсхийх боломжгүй","value":"5"}];	
					var data2=[{"text":"Сонгоно уу","value":"0"},{"text":"1-Нөлөө байхгүй","value":"1"},{"text":"2-Бага зэргийн нөлөөтэй","value":"2"},{"text":"3-Нөлөөтэй гэхдээ хянах боломжтой","value":"3"},{"text":"4-Их нөлөөтэй","value":"4"},{"text":"5-Маш их нөлөөтэй","value":"5"}];	 
					var data3=[{"text":"Сонгоно уу","value":"0"},{"text":"1-Их үр дүнтэй","value":"1"},{"text":"2-Сайжруулах шаардлагатай","value":"2"},{"text":"3-Хангалттай бус","value":"3"}];	 
					$scope.datab = {};
					$scope.datab.bdata = bdata;
					$scope.datab.data1 = data1;
					$scope.datab.data2 = data2;
					$scope.datab.data3 = data3;
					
					$scope.riskSummaryGrid = {
	                dataSource: {
	                    autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkMainFormT2",
	                            contentType:"application/json; charset=UTF-8",           
	                            //data:{"mid":$stateParams.issueId,"sort":[{'field':'orderid','dir':'asc'},{'field':'gorderid','dir':'asc'}]},
	                            data:{"custom":"where mid="+$stateParams.issueId+" and stepid!=4"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.LnkRiskT2+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                            	if(e.responseText=="false"){			 		                            		
		                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
		                            	}else{
		                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
		                            	}
	                    			$(".risksum .k-grid").data("kendoGrid").dataSource.read(); 
	                    		}
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
	                             	riskname: { type: "string", editable: false,},
	                             	dirid: { type: "number"},
	                             	riskid: { type: "number"},
	                             	t2id: { type: "number"},
	                             	data1: { type: "number"},
	                             	data2: { type: "number"},
	                             	data3: { type: "number"},
	                             	rtype: { type: "number", editable: false},
	                             	data4: { type: "number", editable: false},
	                             	data5: { type: "boolean", editable: false},
	                             	data6: { type: "boolean", editable: false},
	                             	data7: { type: "boolean", editable: false},
	                             	description: { type: "string"}
	                             }	                    
	                         }
	                     },
	                     group: {
                          field: "dirname",template: "#=dirname#"
                        },
	                    pageSize: 20,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                sortable: true,
	                resizable: true,
	                //toolbar: ["excel","pdf"],
	                toolbar: kendo.template($("#eHuraangui").html()),
	                pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },
                      columns: [{
                            field: "riskname",
                            title: "Илрүүлсэн эрсдэл",
                            width: 420
                        },
                        {
                            field: "rtype",
                            title: "Эх сурвалж",
                            values:rtype,
                            width: 150
                        },
                        {
                            title: "Эрсдэлийн үнэлгээ",
                            columns: [
								{
								    title: "Эрсдэлийн үнэлгээний шалгуур",
								    columns: [ {
								        field: "data1",
								        values:data1,
								        title:"Эрсдэл тохиолдох </br> магадлал",
								        width: 200
								    },{
								        field: "data2",
								        values:data2,
								        title:"Эрсдэлийн нөлөөлөл",
								        width: 200
								    },{
								        field: "data3",
								        values:data3,
								        title:"Дотоод хяналтын </br> үр дүнтэй байдал",
								        width: 200
								    }]
								},
                              {
                                field: "data4",
                                title: "Эрсдэлийн үнэлгээний </br> түвшин",
                                width: 200
                              },
                              {
							    title: "Эрсдэлийн үнэлгээ",
							    columns: [ {
							        field: "data5",
							        values:bdata,
							        title:"Бага 1-25",
							        width: 100
							    },{
							        field: "data6",
							        values:bdata,
							        title:"Дунд 26-50",
							        width: 100
							    },{
							        field: "data7",
							        values:bdata,
							        title:"Их 51-75",
							        width: 100
							    }]
							  },{
                                field: "description",
                                title: "Нотлох зүйлийн ишлэл",
                                width: 200
                            },{
                                field: "dirname",
                                title: "Аудитийн чиглэл",
                                hidden: true
                            }]
                        }],
                        editable: $scope.editable
	            }; 	
            }
   
           
            
            $("#spreadsheet").kendoSpreadsheet();
            
            var spreadsheet = $("#spreadsheet").data("kendoSpreadsheet");
            var modalfull = UIkit.modal("#modal_full");
            $scope.showExcelFile=function(x,y,z){
          	    mainService.withdomain('get','/excel/json/'+x+'/'+y+'/'+z)
	   			.then(function(data){
	   				if(data){
	   					spreadsheet.fromJSON({ sheets: sheets });	   				 
	   					modalfull.show();
	   				}
	   				else{
		   				sweet.show('Анхаар', 'Error...', 'error');
	   				}
	  			});
            }
            
            $scope.viewFile=function(x){
	        	   mainService.withdomain('get','/work/file/show/'+x)
		   			.then(function(data){
		   				if(data){
		   					$rootScope.content_preloader_hide();
		   					spreadsheet.fromFile(data);
		   					//spreadsheet.fromJSON({ sheets: data.sheets });
		   				 
		   				}
		   				else{
			   				sweet.show('Анхаар', 'Error...', 'error');
		   				}
	   			});
            	
                  
            	/*$.getJSON(x)
                .done(function (sheets) {
                    spreadsheet.fromJSON({ sheets: sheets });
                });
            	
            	jQuery.get(x, function(data) {
            		spreadsheet.fromFile(data);
            	});*/
	        	   var modalfull = UIkit.modal("#modal_full");
	        	   spreadsheet.fromFile(data);
            	modalfull.show();
            	
       /*     	var workbook = Workbook.Load(x);  
                
            	 spreadsheet.fromJSON(workbook);*/
              /*  $.getJSON("/uploads/ХОА/auditor/1590000000084170/6153/12/gazarchin.xlsx")
                .done(function (sheets) {
                	console.log(sheets);
                    spreadsheet.fromJSON({ sheets: sheets });
                }); */  
           }
            
            $scope.delFile=function(x){
            	
            	sweet.show({
		        	   title: 'Баталгаажуулалт',
		            text: 'Та энэ үйлдлийг хийхдээ итгэлтэй байна уу?',
		            type: 'warning',
		            showCancelButton: true,
		            confirmButtonColor: '#DD6B55',
		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
		            closeOnConfirm: false,
		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 $rootScope.content_preloader_show();
		            	   
		            	   mainService.withdomain('delete','/work/file/delete/'+x)
		  		   			.then(function(data){
		  		   				if(data){
		  		   					$rootScope.content_preloader_hide();
		  		   					sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		  			   				for(var i = $scope.files.length - 1; i >= 0; i--){
		  			   			        if($scope.files[i].id == x){
		  			   			            $scope.files.splice(i,1);
		  			   			        }
		  			   			    }
		  		   				}
		  		   				else{
		  			   				sweet.show('Анхаар', 'Error...', 'error');
		  		   				}
		  	   			});
		        	
		            }else{
		                sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
		            }    		
		        });
          	
          }
            $scope.delAttach=function(x){
            	
            	sweet.show({
		        	   title: 'Баталгаажуулалт',
		            text: 'Та энэ үйлдлийг хийхдээ итгэлтэй байна уу?',
		            type: 'warning',
		            showCancelButton: true,
		            confirmButtonColor: '#DD6B55',
		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
		            closeOnConfirm: false,
		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 $rootScope.content_preloader_show();
		             	   mainService.withdomain('delete','/work/file/deleteAttach/'+x)
		   		   			.then(function(data){
		   		   				if(data){
		   		   					$rootScope.content_preloader_hide();
		   		   					sweet.show('Мэдээлэл', 'Үйлдэл амжилттай.', 'success');
		   			   				for(var i = $scope.files.length - 1; i >= 0; i--){
		   			   			        if($scope.files[i].id == x){
		   			   			            $scope.files.splice(i,1);
		   			   			        }
		   			   			    }
		   		   				}
		   		   				else{
		   		   					modalfile.hide();
		   			   				sweet.show('Анхаар', 'Error...', 'error');
		   		   				}
		   	   			});
		        	
		            }else{
		                sweet.show('Анхаар!', 'Үйлдэл хийгдсэнгүй!!!', 'error');
		            }    		
		        });
           }
       
            // work
            
            var modalwork = UIkit.modal("#modal_work");
            
            
            $scope.showWork=function(){
            	  $rootScope.content_preloader_show();
            	   mainService.withdata('put','/work/1/imp', $scope.work)
 		   			.then(function(data){
 		   				if(data){
 		   					$rootScope.content_preloader_hide();
 		   					modalwork.show();
 		   					if(data.length>0){
 		   						$scope.work.note=data[0].note;
 		   					}
 		   					else{
 		   						$scope.work.note='';
 		   					}
 		   				}
 		   				else{
 			   				sweet.show('Анхаар', 'Error...', 'error');
 		   				}
 	   			});
            	
            }
            
            $scope.submitWork=function(){
            	$scope.loading=false;
         	    mainService.withdata('put','/work/work', $scope.work)
  		   			.then(function(data){
  		   				if(data){
  		   					//modalwork.hide();
  			   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
  		   				}
  		   				else{
  		   					modalwork.hide();
  			   				sweet.show('Анхаар', 'Error...', 'error');
  		   				}
  	   			});	
            }
            
            // excel
            
            var modalExcel = UIkit.modal("#modal_excel");
            
            $scope.showExcelSpreadsheet = function(item){
            	var xhr = new XMLHttpRequest();
            	xhr.open('GET', item.fileurl, true);
            	xhr.responseType = 'blob';
            	 
            	xhr.onload = function(e) {
            	  if (this.status == 200) {
            	    // get binary data as a response
            	    var blob = this.response;
            	    var spreadsheet = $("#spreadSheetView").data("kendoSpreadsheet");
  		            spreadsheet.fromFile(blob);
  		          
			   		UIkit.modal("#modalSpreadsheet").show();
            	  }
            	  else{
            		  sweet.show('Анхаар!', 'Файл устгагдсан байна.', 'error');
            	  }
            	};
            	 
            	xhr.send();
            }
            $scope.mid=$stateParams.issueId;
            $scope.exportOrgInfo=function(){
            	
            	sweet.show({
		        	   title: 'Баталгаажуулалт',
 		            text: 'Та энэ файлыг устгахдаа итгэлтэй байна уу?',
 		            type: 'warning',
 		            showCancelButton: true,
 		            confirmButtonColor: '#DD6B55',
 		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
 		            closeOnConfirm: false,
 		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 	$scope.formdata ={};
		             		$scope.formdata.planid = $stateParams.issueId;
		          	   		mainService.withdomain('GET','/my/exportOrgInfo/'+$stateParams.issueId)
		          	   		.then(function(data){
		          	   			sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
		          	   		});
		            }else{
		                sweet.show('Анхаар!', 'Алдаа гарлаа!!!', 'error');
		            }    		
		        });
            }
            $scope.exportMateriallag=function(){
            	
            	sweet.show({
		        	   title: 'Баталгаажуулалт',
 		            text: 'Та энэ файлыг устгахдаа итгэлтэй байна уу?',
 		            type: 'warning',
 		            showCancelButton: true,
 		            confirmButtonColor: '#DD6B55',
 		            confirmButtonText: 'Тийм',
		    	    cancelButtonText: 'Үгүй',
 		            closeOnConfirm: false,
 		            closeOnCancel: false
		          
		        }, function(inputvalue) {
		        	 if (inputvalue) {
		        		 	$scope.formdata ={};
		             		$scope.formdata.planid = $stateParams.issueId;
		          	   		mainService.withdomain('GET','/my/exportMateriallag/'+$stateParams.issueId+'/'+$scope.sstepid)
		          	   		.then(function(data){
		          	   			sweet.show('Анхаар!', 'Файл амжилттай устлаа.', 'success');
		          	   		});
		            }else{
		                sweet.show('Анхаар!', 'Алдаа гарлаа!!!', 'error');
		            }    		
		        });
            }
            
            $scope.showExcelTailan=function(){
            	   formdataDir = new FormData();
            	   $rootScope.content_preloader_show();
            	   mainService.withdata('put','/work/2/imp', $scope.attach)
 		   			.then(function(data){
 		   				if(data){
 		   					$rootScope.content_preloader_hide();
 		   					modalExcel.show();
 		   					if(data.length>0){
 		   						$scope.files=data;
 		   					}
 		   					else{
 		   						$scope.files=[];
 		   					}
 		   				}
 		   				else{
 			   				sweet.show('Анхаар', 'Error...', 'error');
 		   				}
 	   			});
            	
            }
            
            
            // form
            
            var modalform = UIkit.modal("#modal_form");
            
            
            $scope.showForm=function(){
            	   formdataDir = new FormData();
            	   $rootScope.content_preloader_show();
            	   mainService.withdata('put','/work/2/imp', $scope.attach)
 		   			.then(function(data){
 		   				if(data){
 		   					$rootScope.content_preloader_hide();
 		   					modalform.show();
 		   					if(data.length>0){
 		   						$scope.files=data;
 		   					}
 		   					else{
 		   						$scope.files=[];
 		   					}
 		   				}
 		   				else{
 			   				sweet.show('Анхаар', 'Error...', 'error');
 		   				}
 	   			});
            	
            }
            
            $scope.unUpload=true;
            $scope.submitExcel= function() {
            		$scope.form.levelid = $scope.sstepid;
        		   var data = JSON.stringify($scope.form);   
        		   formdataDir.append("obj", data);    
        		   $rootScope.content_preloader_show();
        		   $scope.unUpload=false;
        		   fileUpload.uploadFileToUrl('/work/excel', formdataDir)
  			   	.then(function(data){ 
  			   		if(data){
  			   			$rootScope.content_preloader_hide();
  			   			$scope.files.push(data);
  			   			sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
  			   			
	  			   		var spreadsheet = $("#spreadSheetView").data("kendoSpreadsheet");
	  		            spreadsheet.fromFile($scope.loadedFile);
	  		            $scope.unUpload=true;
  			   			UIkit.modal("#modalSpreadsheet").show();
  			   		}
  			   		else{
  			   			sweet.show('Мэдээлэл', 'Файлын нэр солино уу!!!.', 'error');
  			   		}
 	   				//$state.go('restricted.pages.worklist');	
  			   		formdataDir = new FormData();
  			   		angular.element('.dropify-clear').triggerHandler('click');
 	   			});	
              }
            
        	$scope.submitForm= function() {
       		   var data = JSON.stringify($scope.form);   
       		   formdataDir.append("obj", data);    
       		   fileUpload.uploadFileToUrl('/work/form', formdataDir)
 			   	.then(function(data){ 
 			   		if(data){
 			   			$scope.files.push(data);
 			   			sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
 			   		}
 			   		else{
 			   			sweet.show('Мэдээлэл', 'Файлын нэр солино уу!!!.', 'error');
 			   		}
	   				//$state.go('restricted.pages.worklist');	
 			   		formdataDir = new FormData();
 			   		angular.element('.dropify-clear').triggerHandler('click');
	   			});	
             }
        	
        	// comment
            
            var modalcomment = UIkit.modal("#modal_comment");
            
            $scope.showComment=function(){
         	   mainService.withdata('put','/work/3/imp', $scope.message)
		   			.then(function(data){
		   				if(data){
		   					modalcomment.show();
		   					$scope.message.comment='';
		   					$scope.mlist=data;
		   				}
		   				else{
			   				sweet.show('Анхаар', 'Error...', 'error');
		   				}
	   			});
         	   console.log($scope.mlist);
         	
            }
            
            $scope.submitComment=function(){
            	$scope.loading=false;
         	    mainService.withdata('put','/work/comment', $scope.message)
  		   			.then(function(data){
  		   				if(data){
  		   					$scope.mlist.push(data);
  		   					if (data.positionid == 5){
  		   						$scope.mlist_5count++;
  		   					}
  		   					else if (data.positionid == 2){
  		   						$scope.mlist_2count++;
  		   					}
  		   					$scope.message.comment='';
  		   					//modalcomment.hide();
  			   				//sweet.show('Мэдээлэл', 'Амжилттай илгээлээ.', 'success');
  		   				}
  		   				else{
  		   					modalcomment.hide();
  			   				sweet.show('Анхаар', 'Error...', 'error');
  		   				}
  	   			});	
            }
            
            
            // file
            
            var modalfile = UIkit.modal("#modal_file");            
            
            $scope.showFile=function(){
            	   formdataDir = new FormData();
            	   formdataAttach = new FormData();
            	   $rootScope.content_preloader_show();
            	   mainService.withdata('put','/work/4/imp', $scope.attach)
 		   			.then(function(data){
 		   				if(data){
 		   					$rootScope.content_preloader_hide();
 		   					modalfile.show();
 		   					if(data.length>0){
 		   						$scope.files=data;
 		   					}
 		   					else{
 		   						$scope.files=[];
 		   					}
 		   				}
 		   				else{
 			   				sweet.show('Анхаар', 'Error...', 'error');
 		   				}
 	   			});
            	
            }
            
            $scope.showGrid241 = function(){
            	$scope.note241grid = {
    	                dataSource: {
    	                   
    	                    transport: {
    	                    	read:  {
    	                            url: "/au/list/FsForm241",
    	                            contentType:"application/json; charset=UTF-8",                                    
    	                            type:"POST",
    	                            data:{"custom":"where planid = " + $stateParams.issueId},
    	                        },
    	                        parameterMap: function(options) {
    	                       	 return JSON.stringify(options);
    	                       }
    	                    },
    	                    schema: {
    	                     	data:"data",
    	                     	total:"total"
    	                     },
    	                    pageSize: 15,
    	                    serverPaging: true,
    	                    serverFiltering: true,
    	                    serverSorting: true,
    	                    sort: { field: "orderid", dir: "asc" }
    	                },
    	                filterable: false,
    	                sortable: true,
    	                resizable: true,
    	                toolbar:["excel"],
    	                pageable: {
    	                    refresh: true,
    	                    pageSizes: true,
    	                    buttonCount: 5
    	                },
    	                columns: [
    	                          { title: "Код", columns:[{field:"accCode", title:"A", width:100}], width:100},
    	                          { title: "Дансны нэр", columns:[{field:"accName", title:"1",width:350}], width:350},
    	                          /*{ title: "Өмнөх оны эцсийн үлдэгдэл", columns:[{field:"data1", title:"2",format: "{0:n}"}]},*/
    	                          { title: "Тайлант оны эхний үлдэгдэл", columns:[{field:"data2", title:"3",format: "{0:n}"}]},
    	                          { title: "Тайлант оны эцсийн үлдэгдэл", columns:[{field:"data3", title:"4",format: "{0:n}"}]},
    	                          {
    	                        	  title:"Зөрүү",
	                        		  columns:[{
	                        			  title: "Дүн",
	                        			  columns:[{title:"7",field: "data6",format: "{0:n}"}]
	                        		  },
	                        		  {
	                        			  title: "Хувь",
	                        			  columns:[{title:"8",field: "data7",template: '#=kendo.format("{0:p}", data7 / 100)#'}]
	                        		  }]
    	                          }
    	                          ],
    	                          editable: $scope.editable
    	            };
            	UIkit.modal("#modalForm241").show();
            }
            var balance=[{"text":"Эхний үлдэгдэл","value":"1"},
   	         {"text":"Эцсийн үлдэгдэл","value":"2"},
   	         {"text":"Өмнөх оны гүйцэтгэл ","value":"21"},
   	         {"text":"Тайлант оны гүйцэтгэл","value":"22"}];
            
            var tval=[{"text":"ROOT","value":"0"},
   	         {"text":"Санхүүгийн байдлын тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"1"},
   	         {"text":"Санхүүгийн үр дүнгийн тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"2"},
   	         {"text":"Мөнгөн гүйлгээний тайлан дахь үзүүлэлтийн тулгалт буюу уялдаа","value":"3"},
   	         {"text":"Санхүүгийн тайлан хоорондын тулгалт буюу уялдаа","value":"4"},
   	         {"text":"Санхүүгийн тайлан, тодруулга хоорондын тулгалт буюу уялдаа","value":"5"}];
            
            function tulgaltInit(){
            		$scope.tulgaltGrid = {
    	                dataSource: {    	                   
    	                    transport: {
    	                    	read:  {
    	                            url: "/au/list/StsCheckVariable",
    	                            contentType:"application/json; charset=UTF-8",                                    
    	                            type:"POST",
    	                            data:{"custom":"where planid = " + $stateParams.issueId +" and stepid = " + $scope.note_form.levelid},
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
    	                             	data1: { type: "string", editable: false},
    	                             	data2: { type: "string", editable: false},
    	                             	data3: { type: "string", editable: false},
    	                             	data4: { type: "string", editable: false},
    	                             	data5: { type: "string", editable: false},
    	                            	data6: { type: "number", editable: false},
    	                             	data7: { type: "string", editable: false},
    	                            	data8: { type: "string", editable: false},
    	                            	data9: { type: "number", editable: false},
    	                            	data10: { type: "number", editable: false}
    	                             }	                    
    	                         }
    	                     },
    	                     group: {
                                 field: "valid",values:tval
                               },
    	                    pageSize: 14,
    	                    serverPaging: true,
    	                    serverFiltering: true,
    	                    serverSorting: true,
    	                    sort: { field: "id", dir: "asc" }
    	                },
    	                filterable: {
    	                	mode:"row"
    	                },
    	                sortable: true,
    	                resizable: true,
    	                toolbar:["excel"],
    	                pageable: {
    	                    refresh: true,
    	                    pageSizes: true,
    	                    buttonCount: 5
    	                },
    	                excel: {
	   		                fileName: "Tulgalt Export.xlsx",
	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
	   		                filterable: true,
	   		                allPages: true
	   		            },
    	                columns: [
    	                		  { field: "valid", title: "Тулгалт" +"<span data-translate=''></span>",values:tval,width:"300px"},
    	                          { title: "Харьцуулсан үзүүлэлт", columns:[{field:"data1", title:"1"},{field:"data2", title:"2"}]},
    	                          { title: "Үлдэгдэл", columns:[{field:"data3", title:"3",values:balance}]},
    	                          { title: "Дансны код-А", columns:[{field:"data4", title:"4"}]}, 
    	                          { title: "Дансны код-Б", columns:[{field:"data7", title:"7"}]},   
    	                          { title: "Дүн-А", columns:[{field:"data6", title:"8", format: "{0:n}"}]},    	                          	                         
    	                          { title: "Дүн-Б", columns:[{field:"data9", title:"9", format: "{0:n}"}]},
    	                          { title: "Зөрүү", columns:[{field:"data10", format: "{0:n}", title:"10", template:"# if (data10 != 0) { # <span class='tulgaltRed'>#=kendo.format('{0:n}', data10)#</span> # } else { # #=kendo.format('{0:n}', data10)# # } #"}]},
   
    	                          ],
    	                      editable: $scope.editable,
    	                      dataBound: function(){
    	                    	  $(".tulgaltRed").parent().addClass("md-bg-red-100");
    	    	                },
    	            };     
            }
            
            $scope.showTulgaltPopup = function(){
            	tulgaltInit();
            	UIkit.modal("#modalFormTulgalt").show();
            }
            
            $scope.showGrid242 = function(){
            	$scope.note242grid = {
    	                dataSource: {
    	                   
    	                    transport: {
    	                    	read:  {
    	                            url: "/au/list/FsForm242",
    	                            contentType:"application/json; charset=UTF-8",                                    
    	                            type:"POST",
    	                            data:{"custom":"where planid = " + $stateParams.issueId},
    	                        },
    	                        parameterMap: function(options) {
    	                       	 return JSON.stringify(options);
    	                       }
    	                    },
    	                    schema: {
    	                     	data:"data",
    	                     	total:"total"
    	                     },
    	                    pageSize: 14,
    	                    serverPaging: true,
    	                    serverFiltering: true,
    	                    serverSorting: true,
    	                    sort: { field: "orderid", dir: "asc" }
    	                },
    	                filterable: false,
    	                sortable: true,
    	                resizable: true,
    	                toolbar:["excel"],
    	                pageable: {
    	                    refresh: true,
    	                    pageSizes: true,
    	                    buttonCount: 5
    	                },
    	                columns: [
    	                          { title: "Код", columns:[{field:"accCode", title:"A", width:100}], width:100},
    	                          { title: "Орлогын ангилал", columns:[{field:"accName", title:"1",width:350}], width:350},
    	                          { title: "Өмнөх оны", columns:[{title:"Батлагдсан төлөвлөгөө",columns:[{title:"2",field: "data1",format: "{0:n}"}]},{title:"Гүйцэтгэл",columns:[{title:"3",field: "data2",format: "{0:n}"}]}]},
    	                          { title: "Тайлант оны", columns:[{title:"Батлагдсан төлөвлөгөө",columns:[{title:"4",field: "data3",format: "{0:n}"}]},{title:"Гүйцэтгэл",columns:[{title:"5",field: "data4",format: "{0:n}"}]}]},
    	                          { title: "Зөрүү", columns:[{title:"Батлагдсан төлөвлөгөө",columns:[{title:"6",field: "data5",format: "{0:n}"}]},{title:"Гүйцэтгэл",columns:[{title:"7",field: "data6",format: "{0:n}"}]}]},
    	                          { title: "Тайлант оны өсөлт/бууралт",columns:[{title:"Дүн",columns:[{title: "8",field: "data7",format: "{0:n}"}]},{title: "Хувь",columns:[{title:"11",field: "data8",template: '#=kendo.format("{0:p}", data8 / 100)#'}]}]}
    	                          ],
    	                          editable: $scope.editable,
    	            };
            	UIkit.modal("#modalForm242").show();
            }
            
            $scope.showGrid243 = function(){
            	$scope.note243grid = {
    	                dataSource: {
    	                   
    	                    transport: {
    	                    	read:  {
    	                            url: "/au/list/FsForm243",
    	                            contentType:"application/json; charset=UTF-8",                                    
    	                            type:"POST",
    	                            data:{"custom":"where planid = " + $stateParams.issueId},
    	                        },
    	                        parameterMap: function(options) {
    	                       	 return JSON.stringify(options);
    	                       }
    	                    },
    	                    schema: {
    	                     	data:"data",
    	                     	total:"total"
    	                     },
    	                    pageSize: 14,
    	                    serverPaging: true,
    	                    serverFiltering: true,
    	                    serverSorting: true,
    	                    sort: { field: "orderid", dir: "asc" }
    	                },
    	                filterable: false,
    	                sortable: true,
    	                resizable: true,
    	                toolbar:["excel"],
    	                pageable: {
    	                    refresh: true,
    	                    pageSizes: true,
    	                    buttonCount: 5
    	                },
    	                columns: [
    	                          { title: "Код", columns:[{field:"accCode", title:"A", width:100}], width:100},
    	                          { title: "Зардлын ангилал", columns:[{field:"accName", title:"1",width:350}], width:350},
    	                          { title: "Өмнөх оны", columns:[{title:"Батлагдсан төлөвлөгөө",columns:[{title:"2",field: "data1",format: "{0:n}"}]},{title:"Гүйцэтгэл",columns:[{title:"3",field: "data2",format: "{0:n}"}]}]},
    	                          { title: "Тайлант оны", columns:[{title:"Батлагдсан төлөвлөгөө",columns:[{title:"6",field: "data3",format: "{0:n}"}]},{title:"Гүйцэтгэл",columns:[{title:"7",field: "data4",format: "{0:n}"}]}]},
    	                          { title: "Зөрүү", columns:[{title:"Батлагдсан төлөвлөгөө",columns:[{title:"8",field: "data5",format: "{0:n}"}]},{title:"Гүйцэтгэл",columns:[{title:"9",field: "data6",format: "{0:n}"}]}]},
    	                          { title: "Тайлант оны өсөлт/бууралт", columns:[{title:"Дүн",columns:[{title:"10",field: "data7",format: "{0:n}"}]},{title:"Хувь",columns:[{title:"11",field: "data8",template: '#=kendo.format("{0:p}", data8 / 100)#'}]}]},
    	                          ],
    	                      editable: false
    	            };
            	UIkit.modal("#modalForm243").show();
            }
            
            var formdataAttach = new FormData();
    		$scope.getTheUFilesAttach = function ($files) {
                  angular.forEach($files, function (value, key) {
                	  formdataAttach.append("files", value);
                  });
                  
                  if ($files.length > 0){
                  	$scope.loadedFile = $files[0];
                  } 
            };
            
		
			
        	$scope.submitFile= function(event) {
				$scope.newAttach={
					noteid:0,
					sheetname : '',
					fileurl : '',
					mid : 0,
					wid : 0
				};
        		$scope.newAttach.noteid=$scope.note_form.id;
        		$scope.newAttach.sheetname = $scope.excel.sheetName;
        		$scope.newAttach.fileurl = $scope.note_form.fileurl;
        		$scope.newAttach.mid = $scope.note_form.mid;
        		$scope.newAttach.wid = $scope.note_form.wid;
        		 var data = JSON.stringify($scope.newAttach);   
        		 formdataAttach.append("obj", data);    
        		 fileUpload.uploadFileToUrl('/work/attach', formdataAttach)
  			   	 	.then(function(data){ 
	  			   		if(data){
	  			   			$scope.files.push(data);
	  			   			sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');
	  			   		}
	  			   		else{
	  			   			sweet.show('Мэдээлэл', 'Файлын нэр солино уу!!!.', 'error');
	  			   		}
	  			   		$('.dropify-preview').css("display:none");
						//angular.element('.dropify-clear').triggerHandler('click');
	  			   		$("#lalar").parent().parent().find(".dropify-clear").trigger('click');	
	  			   	//	event.preventDefault();
	  			   		$("#newsletterform")[0].reset();
	  			   	$scope.newAttach={};
	  			    formdataAttach.delete('obj');
	  			    formdataAttach.delete('files');
	  			   	formdataAttach = null;
	  			    formdataAttach = new FormData();
 	   			});
            }
        	
            $scope.getTheUFiles = function ($files) {
                angular.forEach($files, function (value, key) {
                	formdataDir.append("files", value);
                });
                
                if ($files.length > 0){
                	$scope.loadedFile = $files[0];
                }
                
            };
            
            var $formValidate = $('#form_comment');

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
            
            
            var $formValidateWork = $('#form_work');

            $formValidateWork
                .parsley()
                .on('form:validated',function() {
                    $scope.$apply();
                })
                .on('field:validated',function(parsleyField) {
                    if($(parsleyField.$element).hasClass('md-input')) {
                        $scope.$apply();
                    }
                });
            
            
            $scope.openClose=function(){
            	 $scope.noteActive = false;
            	 angular.element($window).resize();
            }
  
            
            // circular statistics
            $scope.stat_conversions_data = [5,3,9,6,5,9,7];
            $scope.stat_conversions_options = {
                height: 64,
                width: 96,
                fill: ["#d84315"],
                padding: 0.2
            };

            $scope.epc_user_messages = {
                barColor:'#03a9f4',
                scaleColor: false,
                trackColor: '#f5f5f5',
                lineWidth: 5,
                size: 110,
                easing: variables.bez_easing_swiftOut
            };

            $scope.epc_tasks_list = {
                barColor:'#9c27b0',
                scaleColor: false,
                trackColor: '#f5f5f5',
                lineWidth: 5,
                size: 110,
                easing: variables.bez_easing_swiftOut
            };

            $scope.epc_orders = {
                barColor:'#009688',
                scaleColor: false,
                trackColor: '#f5f5f5',
                lineWidth: 5,
                size: 110,
                easing: variables.bez_easing_swiftOut
            };

            $scope.epc_user_registrations = {
                barColor:'#607d8b',
                scaleColor: false,
                trackColor: '#f5f5f5',
                lineWidth: 5,
                size: 110,
                easing: variables.bez_easing_swiftOut
            };
            var modalgb1 = UIkit.modal("#modal_gb1");
            $scope.showgb1=function(){
            	modalgb1.show();  
            	$scope.LutFormB1="com.netgloo.models.LutFormB1.";
					
					$scope.gb1Grid = {
	                dataSource: {
	                    autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LutFormB1",
	                            contentType:"application/json; charset=UTF-8",           
	                            //data:{"mid":$stateParams.issueId,"sort":[{'field':'orderid','dir':'asc'},{'field':'gorderid','dir':'asc'}]},
	                            data:{"mid":$stateParams.issueId,"stepid":$scope.sstepid,"sheetname":$scope.excel.sheetName},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.LutFormB1+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                    			$(".gb1 .k-grid").data("kendoGrid").dataSource.read(); 
	                    		}
	                        },
	                        destroy: {
	                            url: "/core/delete/"+$scope.LutFormB1+"",
	                            contentType:"application/json; charset=UTF-8",                                    
	                            type:"POST",
	                            complete: function(e) {
	                            	 $("#notificationDestroy").trigger('click');
	                    		}
	                        },
	                        create: {
	                            url: "/core/create/"+$scope.LutFormB1+"",
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
	                             	id: { type: "number", editable: false,nullable: false},                        	
	                             	cyear: { type: "string", editable: false,},
	                             	gencode: { type: "string"},
	                             	orgcode: { type: "string"},
	                             	planid: { type: "number"},
	                             	orderid: { type: "number", editable: false},
	                             	stepid: { type: "number"},
	                             	data1: { type: "string", editable: false},
	                             	data2: { type: "string"},
	                             	data3: { type: "string"},
	                             	data4: { type: "string"},
	                             	data5: { type: "string"}
	                             }	                    
	                         }
	                     },
	                    pageSize: 10,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                sortable: true,
	                resizable: true,
	                toolbar: kendo.template($("#eGuitsetgeh").html()),
	                pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },
                      columns: [
						{
						    field: "orderid",
						    title: "№",
						    width: 50
						},
                        {
                            title: "Хариуцлагын талаарх ойлголтыг бүрдүүлэх баримтын",
                            columns: [ {
						        field: "data1",
						        title:"Нэр",
						        width: 400
						    },
						    {
						        field: "data2",
						        title:"Огноо",
						        width: 200
						    }
						    ,{
						        field: "data3",
						        title:"Дугаар",
						        width: 200
						    }]
                        },
                        {
                            field: "data4",
                            title: "Ажлын баримтын код",
                            width: 320
                        },
                        {
                            field: "data5",
                            title: "Тайлбар",
                            width: 420
                        },
                    ],
                    editable: $scope.editable,
	            };
            }
             
            var modalgb41 = UIkit.modal("#modal_work_gb41");
            $scope.showGB41= function(){
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesno=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				modalgb41.show();	   					
				$scope.showGBGRID41 = {
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
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID41 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	data20: { type: "string",editable: false},
	                             	data17: { type: "string",editable: false},
	                             	data18: { type: "string",editable: false},
	                             	data19: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false},
	                             	data22: { type: "string",editable: false},
	                             	data23: { type: "number",editable: true,defaultValue:0},
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data2", title: "Илрүүлсэн эрсдэл", width: "200px" },	
	                		  { field:"data12", title: "Хэрэгжүүлэхээр төлөвлөсөн горим сорил", width: "200px" },	
	                		  { title: "<span style='vertical-align:middle'>Цуглуулсан нотлох зүйлийн</span>" ,columns:[{title:"Огноо",width: "100px",template: "#=(data17==null) ? '' : data17#" ,field:"data17"},{title:"Нэр",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data18==null) ? '' : data18#"  ,field:"data18"},{title:"Товч агуулга",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data19==null) ? '' : data19#"  ,field:"data19"}]},	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		/*  { field:"data22", title: "<span style='vertical-align:middle'>Нотлох зүйлийн ишлэл</span>", editor: $scope.categoryDropDownEditor,width: "200px"},*/
	                		  { title: "<span style='vertical-align:middle'>Нотлох зүйлийн ишлэл</span>", template: kendo.template($("#files").html()),width: "200px"},
	                		  { field:"data23", values:yesno,title: "<span style='vertical-align:middle'>Тухайн үр дүнг алдаа, зөрчилд тооцох эсэх</span>",width: "200px"},
	                		 /* { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }*/],
                      dataBound: function () {
   		                var rows = this.items();
   		                  $(rows).each(function () {
   		                      var index = $(this).index() + 1 
   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
   		                      var rowLabel = $(this).find(".row-number");
   		                      $(rowLabel).html(index);
   		                  });
   		                  
   	   		         
		              
   		  	          },
	   	   		  	  editable: $scope.editable
	            };
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID41.toolbar=kendo.template($("#exportExcel").html());
            }
            
       
        
            var modalgb42 = UIkit.modal("#modal_work_gb42");
            $scope.showGB42= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesno=[{"text":"Үгүй","value":"0"},{"text":"Тийм","value":"1"}];	
				   	   					
				$scope.showGBGRID42 = {
	                dataSource: {
	                    autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=0"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID42 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	data20: { type: "string",editable: false},
	                             	data17: { type: "string",editable: false},
	                             	data18: { type: "string",editable: false},
	                             	data19: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false},
	                             	data22: { type: "string",editable: false},
	                             	data24: { type: "string",editable: true},
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    batch:true,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data2", title: "Илрүүлсэн эрсдэл", width: "200px" },	
	                		  { field:"data12", title: "Хэрэгжүүлэхээр төлөвлөсөн горим сорил", width: "200px" },	
	                		  { title: "<span style='vertical-align:middle'>Цуглуулсан нотлох зүйлийн</span>" ,columns:[{title:"Огноо",width: "100px",template: "#=(data17==null) ? '' : data17#" ,field:"data17"},{title:"Нэр",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data18==null) ? '' : data18#"  ,field:"data18"},{title:"Товч агуулга",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data19==null) ? '' : data19#"  ,field:"data19"}]},	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { field:"data22", title: "<span style='vertical-align:middle'>Нотлох зүйлийн ишлэл</span>", editor: $scope.categoryDropDownEditor,width: "200px"},
	                		  { field:"data24", title: "<span style='vertical-align:middle'>Алдаа, зөрчилд тооцохгүй гэж үзсэн шалтгаан</span>", editor: $scope.categoryDropDownEditor,width: "400px",template:"# if (data24 != null) { # <span class='tulgaltRed'>#:data24#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		 /* { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }*/],
		                      dataBound: function () {
		   	   		                var rows = this.items();
		   	   		                  $(rows).each(function () {
		   	   		                      var index = $(this).index() + 1 
		   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
		   	   		                      var rowLabel = $(this).find(".row-number");
		   	   		                      $(rowLabel).html(index);
		   	   		                  });
		   	   		                  
			   	   		         
		   			              
		   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb42.show();
				//$scope.showGBGRID42.toolbar=["excel","pdf"];
				$scope.showGBGRID42.toolbar=kendo.template($("#exportExcel").html());
				/*if($scope.editable){
					$scope.showGBGRID42.toolbar=["save"];
				}*/
				
            }
            
            var modalgb43 = UIkit.modal("#modal_work_gb43");
            $scope.showGB43= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID43 = {
	                dataSource: {
	                    autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID43 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	data20: { type: "string",editable: false},
	                             	data17: { type: "string",editable: false},
	                             	data18: { type: "string",editable: false},
	                             	data19: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false},
	                             	data22: { type: "string",editable: false},
	                             	data25: { type: "number"},
	                             	data26: { type: "string",editable: true},
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data2", title: "Илрүүлсэн эрсдэл", width: "200px" },	
	                		  { field:"data12", title: "Хэрэгжүүлэхээр төлөвлөсөн горим сорил", width: "200px" },	
	                		  { title: "<span style='vertical-align:middle'>Цуглуулсан нотлох зүйлийн</span>" ,columns:[{title:"Огноо",width: "100px",template: "#=(data17==null) ? '' : data17#" ,field:"data17"},{title:"Нэр",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data18==null) ? '' : data18#"  ,field:"data18"},{title:"Товч агуулга",editor: $scope.categoryDropDownEditor,width: "200px", template: "#=(data19==null) ? '' : data19#"  ,field:"data19"}]},	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { title: "<span style='vertical-align:middle'>Нотлох зүйлийн ишлэл</span>", template: kendo.template($("#files").html()),width: "200px"},
	                		  { field:"data25", values:yesnodata, title: "<span style='vertical-align:middle'>Алдаа, зөрчлийг аудитын явцад залруулах боломжтой эсэх</span>",width: "150px"},
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
		   	   		  	  editable: $scope.editable
	            };
				modalgb43.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID43.toolbar=kendo.template($("#exportExcel").html());
			/*	if($scope.editable){
					$scope.showGBGRID43.toolbar=["save"];
				}*/
            }
            
            var modalgb44 = UIkit.modal("#modal_work_gb44");
            $scope.showGB44= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var zalangilarl=[{"text":"Нягтлан бодох бүртгэлийн бодлогын өөрчлөлттэй холбоотой","value":1},{"text":"Тайлант хугацаанд гарсан нягтлан бодох бүртгэлийн тооцооллын алдаа","value":2},{"text":"Өмнөх тайлант хугацаанд гарсан нягтлан бодох бүртгэлийн тооцооллын алдаа","value":3},{"text":"Нягтлан бодох бүртгэлийн тооцооллын өөрчлөлттэй холбоотой","value":4}];	
				   	   					
				$scope.showGBGRID44 = {
	                dataSource: {
	                  //  autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data25=1"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID44 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	data1: { editable: false},
	                             	data20: {editable: false},
	                             	data21: { editable: false},
	                             	b44data1: { type: "string",editable: true},
	                             	b44data2: { type: "string",editable: true},
	                             	b44data3: { type: "string",editable: true},
	                             	b44data4: { type: "string",editable: true},
	                             	b44data5: { type: "string",editable: true},
	                             	b44data6: { type: "string",editable: true},
	                             	b44data7: { type: "string",editable: true},
	                             	b44data8: { type: "string",editable: true},
	                             	b44data9: { type: "string",editable: true},
	                             	b44data10: { type: "string",editable: true},
	                             	b44data11: { type: "string"},
	                             	b44data12: { type: "string",editable: true},
	                             	b44data13: { type: "number",defaultValue:0,editable: true},
	                             	b44data14: { type: "string",editable: true},
	                             	b44data15: { type: "string",editable: true},
	                             	b44data16: { type: "string"},
	                             	b44data17: { type: "string",editable: true},
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    batch:true,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { title: "<span style='vertical-align:middle'>Залруулга хийх тайлан (сая төгрөгөөр)</span>" ,columns:[{title:"Санхүүгийн тайлан",width: "100px",columns:[{title:"Санхүүгийн байдлын тайлан",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data1",template:"# if (b44data1 != null) { # <span class='tulgaltRed'>#:b44data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Үр дүнгийн тайлан",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data2",template:"# if (b44data2 != null) { # <span class='tulgaltRed'>#:b44data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Өмчийн өөрчлөлтийн тайлан",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data3",template:"# if (b44data3 != null) { # <span class='tulgaltRed'>#:b44data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Мөнгөн гүйлгээний тайлан",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data4",template:"# if (b44data4 != null) { # <span class='tulgaltRed'>#:b44data4#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"}]},{title:"Санхүүгийн тайлангийн тодруулга",editor: $scope.categoryDropDownEditor,width: "200px",columns:[{title:"Мөнгө, түүнтэй адилтгах хөрөнгийн тодруулга",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data5",template:"# if (b44data5 != null) { # <span class='tulgaltRed'>#:b44data5#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Үндсэн хөрөнгийн тодруулга",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data6",template:"# if (b44data6 != null) { # <span class='tulgaltRed'>#:b44data6#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Бараа материалын тодруулга",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data7",template:"# if (b44data7 != null) { # <span class='tulgaltRed'>#:b44data7#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Авлагын тодруулга",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data8",template:"# if (b44data8 != null) { # <span class='tulgaltRed'>#:b44data8#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Өглөгийн тодруулга",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data9",template:"# if (b44data9 != null) { # <span class='tulgaltRed'>#:b44data9#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Урт хугацаат өр төлбөрийн тодруулга",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data10",template:"# if (b44data10 != null) { # <span class='tulgaltRed'>#:b44data10#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Бусад",editor: $scope.categoryDropDownEditor,width: "100px", field:"b44data11",template:"# if (b44data11 != null) { # <span class='tulgaltRed'>#:b44data11#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"}]},{title:"Төсвийн гүйцэтгэлийн тайлан",width: "200px", field:"b44data12", editor: $scope.categoryDropDownEditor, template:"# if (b44data12 != null) { # <span class='tulgaltRed'>#:b44data12#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"}]},
	                		  { field:"b44data13",values:zalangilarl, title: "<span style='vertical-align:middle'>Залруулгын ангилал</span>",width: "200px"},
	                		  { field:"b44data14", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр заалт </span>",width: "200px", template:"# if (b44data14 != null) { # <span class='tulgaltRed'>#:b44data14#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b44data15", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Залруулга хийхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px", template:"# if (b44data15 != null) { # <span class='tulgaltRed'>#:b44data15#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b44data16", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Нотлох зүйлсийн ишлэл (Дугаар)</span>",width: "200px", template:"# if (b44data16 != null) { # <span class='tulgaltRed'>#:b44data16#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b44data17", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px", template:"# if (b44data17 != null) { # <span class='tulgaltRed'>#:b44data17#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		 /* { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }*/],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				//$scope.showGBGRID44.dataSource.read(); 
				modalgb44.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID44.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID44.toolbar=["save"];
				}
            }
            
            var modalgb45 = UIkit.modal("#modal_work_gb45");
            $scope.showGB45= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID45 = {
	                dataSource: {
	                   // autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data25=1"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID45 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                            	data1: { editable: false},
	                             	data20: {editable: false},
	                             	data21: { editable: false},
	                            	b45data1: { type: "string",editable: true},
	                             	b45data2: { type: "string",editable: true},
	                             	b45data3: { type: "string",editable: true},
	                             	b45data4: { type: "string",editable: true},
	                             	b45data5: { type: "string",editable: true},
	                             	b45data6: { type: "string",editable: true},
	                             	b45data7: { type: "string",editable: true},
	                             	b45data8: { type: "string",editable: true},
	                             	b45data9: { type: "string",editable: true},
	                             	b45data10: { type: "string",editable: true},
	                             	b45data11: { type: "string"}
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"b45data1",editor: $scope.categoryDropDownEditor, title: "Дансны нэр", width: "150px", template:"# if (b45data1 != null) { # <span class='tulgaltRed'>#:b45data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},	
	                		  { title: "<span style='vertical-align:middle'>Залруулга хийх тайлан (сая төгрөгөөр)</span>" ,columns:[{title:"Санхүүгийн байдлын тайлан",width: "100px",columns:[{title:"Дебит",editor: $scope.categoryDropDownEditor,field:"b45data2",width: "100px", template:"# if (b45data2 != null) { # <span class='tulgaltRed'>#:b45data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Кредит",editor: $scope.categoryDropDownEditor,field:"b45data3",width: "100px", template:"# if (b45data3 != null) { # <span class='tulgaltRed'>#:b45data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"}]},{title:"Үр дүнгийн тайлан",width: "200px",columns:[{title:"Дебит",editor: $scope.categoryDropDownEditor,field:"b45data4",width: "100px", template:"# if (b45data4 != null) { # <span class='tulgaltRed'>#:b45data4#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Кредит",editor: $scope.categoryDropDownEditor,field:"b45data5",width: "100px", template:"# if (b45data5 != null) { # <span class='tulgaltRed'>#:b45data5#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"}]},{title:"Өмчийн өөрчлөлтийн тайлан",width: "200px",columns:[{title:"Дебит",editor: $scope.categoryDropDownEditor,field:"b45data6",width: "100px", template:"# if (b45data6 != null) { # <span class='tulgaltRed'>#:b45data6#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Кредит",editor: $scope.categoryDropDownEditor,field:"b45data7",width: "100px", template:"# if (b45data7 != null) { # <span class='tulgaltRed'>#:b45data7#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"}]},{title:"Мөнгөн гүйлгээний тайлан",width: "200px",columns:[{title:"Дебит",editor: $scope.categoryDropDownEditor,field:"b45data8",width: "100px", template:"# if (b45data8 != null) { # <span class='tulgaltRed'>#:b45data8#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},{title:"Кредит",editor: $scope.categoryDropDownEditor,field:"b45data9",width: "100px", template:"# if (b45data9 != null) { # <span class='tulgaltRed'>#:b45data9#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"}]}]},
	                		  { field:"b45data10",editor: $scope.categoryDropDownEditor, title: "<span style='vertical-align:middle'>Нотлох зүйлсийн ишлэл (Дугаар)</span>",width: "200px", template:"# if (b45data10 != null) { # <span class='tulgaltRed'>#:b45data10#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b45data11",editor: $scope.categoryDropDownEditor, title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px", template:"# if (b45data11 != null) { # <span class='tulgaltRed'>#:b45data11#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		 /* { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }*/],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb45.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID45.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID45.toolbar=["save"];
				}
            }
            
            var modalgb46 = UIkit.modal("#modal_work_gb46");
            $scope.showGB46= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var destype=[{"text":"Зөвлөмж өгөх","value":1},{"text":"Төлбөрийн акт тогтоох","value":2},{"text":"Албан шаардлага өгөх","value":3},{"text":"Сахилгын шийтгэл ногдуулахаар шилжүүлэх","value":4},{"text":"Хууль хяналтын байгууллагад асуудлыг шилжүүлэх","value":5}];	
				   	   					
				$scope.showGBGRID46 = {
	                dataSource: {
	                    //autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1 and data25=0"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID43 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                            	data1: { editable: false},
	                             	data20: {editable: false},
	                             	data21: { editable: false},
	                             	b46data1: { type: "string",editable: true},
	                             	b46data2: { type: "string",editable: true},
	                             	b46data3: { type: "string",editable: true},
	                             	b46data4: { type: "number",editable: true},
	                             	b46data5: { type: "string",editable: true},
	                             	data17: { type: "string",editable: false},
	                             	data18: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false},
	                             	data22: { type: "string",editable: false},
	                             	data25: { type: "number"},
	                             	data26: { type: "string",editable: true},
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    batch: true,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { field:"b46data1", editor: $scope.categoryDropDownEditor, title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт</span>",width: "200px", template:"# if (b46data1 != null) { # <span class='tulgaltRed'>#:b46data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b46data2", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px", template:"# if (b46data2 != null) { # <span class='tulgaltRed'>#:b46data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b46data3", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Асуудлыг шийдвэрлэхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px", template:"# if (b46data3 != null) { # <span class='tulgaltRed'>#:b46data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b46data4", values:destype, title: "<span style='vertical-align:middle'>Залруулаагүй буруу илэрхийлэл буюу алдаа, зөрчилтэй асуудлыг шийдвэрлэсэн байдал</span>",width: "200px"},
	                		  { field:"b46data5", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px", template:"# if (b46data5 != null) { # <span class='tulgaltRed'>#:b46data5#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		 /* { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }*/],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb46.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID46.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID46.toolbar=["save"];
				}
            }
            
            var modalgb461 = UIkit.modal("#modal_work_gb461");
            $scope.showGB461= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID461 = {
	                dataSource: {
	                    //autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1 and b46data4=1"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID461 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	b461data1: { type: "string",editable: true},
	                             	b461data2: { type: "string",editable: true},
	                             	b461data3: { type: "string",editable: true},
	                             	b461data4: { type: "string",editable: true},
	                             	b46data1: { type: "string",editable: false},
	                             	b46data2: { type: "string",editable: false},
	                             	b46data3: { type: "string",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data25: { type: "number"},
	                             	data26: { type: "string",editable: true},
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true,
	                    batch: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { field:"b46data1", title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт</span>",width: "200px"},
	                		  { field:"b46data2", title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px"},
	                		  { field:"b46data3", title: "<span style='vertical-align:middle'>Асуудлыг шийдвэрлэхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px"},
	                		  { field:"b461data1", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр</span>",width: "200px", template:"# if (b461data1 != null) { # <span class='tulgaltRed'>#:b461data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b461data2", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах байгууллага, эрх бүхий албан тушаалтны санал / тайлбар</span>",width: "200px", template:"# if (b461data2 != null) { # <span class='tulgaltRed'>#:b461data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b461data3", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Менежментийн захидалд тусгах зөвлөмжийг боловсруулж оруулна уу</span>",width: "200px", template:"# if (b461data3 != null) { # <span class='tulgaltRed'>#:b461data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b461data4", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px", template:"# if (b461data4 != null) { # <span class='tulgaltRed'>#:b461data4#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		 /* { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }*/],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb461.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID461.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID461.toolbar=["save"];
				}
            }
            
            var modal_work_managementZ = UIkit.modal("#modal_work_managementZ");
            $scope.showGridMZ= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				   	   					
				$scope.showMZgrid = {
	                dataSource: {
	                    autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1 and b46data4=1"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID461 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	b461data1: { type: "string",editable: true},
	                             	b461data2: { type: "string",editable: true},
	                             	b461data3: { type: "string",editable: true},
	                             	b461data4: { type: "string",editable: true},
	                             	b46data1: { type: "string",editable: false},
	                             	b46data2: { type: "string",editable: false},
	                             	b46data3: { type: "string",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data25: { type: "number"},
	                             	data26: { type: "string",editable: true},
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
	               
	                pageable: {
	                    refresh: true,
	                    pageSizes: true,
	                    buttonCount: 5
	                },
	                excel: {
	                fileName: "Export.xlsx",
	                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
	                filterable: true,
	                allPages: true
	            },
	                columns: [
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data20", title: "Илрүүлэлт",template: "#=(data20==null) ? '' : data20#"  , width: 200 },
	                		  { field:"b461data3",title: "Зөвлөмж",width: "200px"},
	                		  { field:"data2", title: "Эрсдэл", width: "200px" },
	                		  { field:"b461data1",title: "Хариуцах ажилтан",width: "200px"},
	                		  { field:"b461data2",title: "Төсвийн захирагчийн хариу",width: "200px"},
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
		   	   		  	  editable: false
	            };
				modal_work_managementZ.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID461.toolbar=kendo.template($("#exportExcel").html());
            }
            
            var modalgb462 = UIkit.modal("#modal_work_gb462");
            $scope.showGB462= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID462 = {
	                dataSource: {
	                    //autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1 and b46data4=2"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID462 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	b462data1: { type: "string",editable: true},
	                             	b462data2: { type: "string",editable: true},
	                             	b462data3: { type: "string",editable: true},
	                            	b462data4: { type: "string",editable: true},
	                             	b462data5: { type: "string",editable: true},
	                            	b46data1: { type: "string",editable: false},
	                             	b46data2: { type: "string",editable: false},
	                             	b46data3: { type: "string",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false}
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    batch: true,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Зөрчил дутагдлын товч утга",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Дүн (Төгрөгөөр)", width: "150px" },	
	                		  { field:"b46data1", title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт </span>",width: "200px"},
	                		  { field:"b46data2", title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px"},
	                		  { field:"b46data3", title: "<span style='vertical-align:middle'>Төлбөрийн акт тогтоох үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px"},
	                		  { field:"b462data1", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр (1 дүгээр гарын үсэг)</span>",width: "200px", template:"# if (b462data1 != null) { # <span class='tulgaltRed'>#:b462data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b462data2", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр (2 дүгээр гарын үсэг)</span>",width: "200px", template:"# if (b462data2 != null) { # <span class='tulgaltRed'>#:b462data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b462data3", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах байгууллага, эрх бүхий албан тушаалтны санал / тайлбар</span>",width: "200px", template:"# if (b462data3 != null) { # <span class='tulgaltRed'>#:b462data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b462data4", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Төлбөрийн актын биелэлтийг тооцох сүүлийн хугацаа</span>",width: "200px", template:"# if (b462data4 != null) { # <span class='tulgaltRed'>#:b462data4#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b462data5", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px", template:"# if (b462data5 != null) { # <span class='tulgaltRed'>#:b462data5#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { 
		                          	 template: kendo.template($("#akt").html()),  width: "120px" 
	                		  }],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb462.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID462.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID462.toolbar=["save"];
				}
            }
            
            var modalgb463 = UIkit.modal("#modal_work_gb463");
            $scope.showGB463= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID463 = {
	                dataSource: {
	                    //autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1 and b46data4=3"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID463 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                              	b46data1: { type: "string",editable: false},
	                             	b46data2: { type: "string",editable: false},
	                             	b46data3: { type: "string",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false},
	                             	b463data1: { type: "string",editable: true},
	                             	b463data2: { type: "string",editable: true},
	                             	b463data3: { type: "string",editable: true},
	                             	b463data4: { type: "string",editable: true},
	                             	b463data5: { type: "string",editable: true}
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true,
	                    batch: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Зөрчил дутагдлын товч утга",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Дүн (Төгрөгөөр)", width: "150px" },	
	                		  { field:"b46data1", title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт </span>",width: "200px"},
	                		  { field:"b46data2", title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px"},
	                		  { field:"b46data3", title: "<span style='vertical-align:middle'>Асуудлыг шийдвэрлэхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px"},
	                		  { field:"b463data1", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр (1 дүгээр гарын үсэг)</span>",width: "200px", template:"# if (b463data1 != null) { # <span class='tulgaltRed'>#:b463data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b463data2", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр (2 дүгээр гарын үсэг)</span>",width: "200px", template:"# if (b463data2 != null) { # <span class='tulgaltRed'>#:b463data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b463data3", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах байгууллага, эрх бүхий албан тушаалтны санал / тайлбар</span>",width: "200px", template:"# if (b463data3 != null) { # <span class='tulgaltRed'>#:b463data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b463data4", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Төлбөрийн актын биелэлтийг тооцох сүүлийн хугацаа</span>",width: "200px", template:"# if (b463data4 != null) { # <span class='tulgaltRed'>#:b463data4#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { field:"b463data5", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px", template:"# if (b463data5 != null) { # <span class='tulgaltRed'>#:b463data5#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
	                		  { 
		                          	 template: kendo.template($("#albanshaardlaga").html()),  width: "120px" 
	                		  }],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb463.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID463.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID463.toolbar=["save"];
				}
            }
/*            
            var modalgb464 = UIkit.modal("#modal_work_gb464");
            $scope.showGB464= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID464 = {
	                dataSource: {
	                    autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID464 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	b464data1: { type: "string",editable: true},
	                             	b464data2: { type: "string",editable: true},
	                             	b464data3: { type: "string",editable: true},
	                             	b464data4: { type: "string",editable: true},
	                             	b464data5: { type: "string",editable: true},
	                             	b464data6: { type: "string",editable: true},
	                             	b464data7: { type: "string",editable: true},
	                             	b46data1: { type: "string",editable: false},
	                             	b46data2: { type: "string",editable: false},
	                             	b46data3: { type: "string",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false}
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { field:"b464data1", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт </span>",width: "200px"},
	                		  { field:"b464data2", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px"},
	                		  { field:"b464data3", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Асуудлыг шийдвэрлэхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px"},
	                		  { field:"b464data4", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр</span>",width: "200px"},
	                		  { field:"b464data5", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны санал / тайлбар</span>",width: "200px"},
	                		  { field:"b464data6", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Биелэлт тооцох сүүлийн хугацаа</span>",width: "200px"},
	                		  { field:"b464data7", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px"},
	                		  { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb464.show();
				$scope.showGBGRID464.toolbar=["excel","pdf"];
            }*/
            
            var modalgb464 = UIkit.modal("#modal_work_gb464");
            $scope.showGB464= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID464 = {
	                dataSource: {
	                    //autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1  and b46data4=4"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID464 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	b464data1: { type: "string",editable: true},
	                             	b464data2: { type: "string",editable: true},
	                             	b464data3: { type: "string",editable: true},
	                             	b464data4: { type: "string",editable: true},
	                             	b464data5: { type: "string",editable: true},
	                             	b464data6: { type: "string",editable: true},
	                             	b464data7: { type: "string",editable: true},
	                             	b46data1: { type: "string",editable: false},
	                             	b46data2: { type: "string",editable: false},
	                             	b46data3: { type: "string",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false}
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true,
	                    batch: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { field:"b464data1", template:"# if (b464data1 != null) { # <span class='tulgaltRed'>#:b464data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #",editor: $scope.categoryDropDownEditor, title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт </span>",width: "200px"},
	                		  { field:"b464data2", template:"# if (b464data2 != null) { # <span class='tulgaltRed'>#:b464data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px"},
	                		  { field:"b464data3", template:"# if (b464data3 != null) { # <span class='tulgaltRed'>#:b464data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Асуудлыг шийдвэрлэхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px"},
	                		  { field:"b464data4", template:"# if (b464data4 != null) { # <span class='tulgaltRed'>#:b464data4#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр</span>",width: "200px"},
	                		  { field:"b464data5", template:"# if (b464data5 != null) { # <span class='tulgaltRed'>#:b464data5#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны санал / тайлбар</span>",width: "200px"},
	                		  { field:"b464data6", template:"# if (b464data6 != null) { # <span class='tulgaltRed'>#:b464data6#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Биелэлт тооцох сүүлийн хугацаа</span>",width: "200px"},
	                		  { field:"b464data7", template:"# if (b464data7 != null) { # <span class='tulgaltRed'>#:b464data7#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px"},
	                		  { 
		                          	 template: kendo.template($("#ajiltan").html()),  width: "120px" 
	                		  }],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb464.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID464.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID464.toolbar=["save"];
				}
            }
            
            var conftype = $scope.selectize_conftype_options=[];
            var confMethod_data = $scope.selectize_confMethod_options=[];
            var confSourceO_data = $scope.selectize_confSourceO_options=[];
            var confSourceI_data = $scope.selectize_confSourceI_options=[];
            var rtype_data =  $scope.rtype_data_options=[];
            var factor_data= $scope.selectize_factor_options = [];
            $scope.confMethod=[];
            $scope.confSourceI=[];
            $scope.confSourceO=[];
            $scope.LutGroupOfFactor=[];
            $scope.rtype_data=
            {
        		options:[]
            }
            $scope.dir_data={
        		options:[]
            };
            $scope.rtype_data={
        		options:[]
            };
            $scope.risk={};
            $scope.huulizaalt_list=[];
            $scope.huulizuil_list=[];
            $scope.zuil_list=[];
            $scope.huuli_list=[];
            $scope.law_list=[];
            $scope.selectize_treatment_options=[];
            $scope.selectize_risk_options=[];
            $scope.zaalt_list=[];
            
            $scope.rtype_config = {
	            plugins: {
	                'disable_options': {
	                    disableOptions: ["c1","c2"]
	                }
	            },
	            create: false,
	            maxItems: 1,
	            placeholder: 'Сонгох...',
	            optgroupField: 'parent_id',
	            optgroupLabelField: 'text',
	            optgroupValueField: 'ogid',
	            valueField: 'value',
	            labelField: 'text',
	            searchField: 'text'
	         };
			
			 $scope.dir_config = {
                plugins: {
                    'disable_options': {
                        disableOptions: ["c1","c2"]
                    }
                },
                create: false,
                maxItems: 1,
                placeholder: 'Сонгох...',
                optgroupField: 'parent_id',
                optgroupLabelField: 'text',
                optgroupValueField: 'ogid',
                valueField: 'value',
                labelField: 'text',
                searchField: 'text'
            };
			 
			var risk_data= $scope.selectize_risk_options = [];
			$scope.riskChange = function(id){
				$scope.isFactorCreated = false;
				$scope.risk.crition = null;
 				mainService.withdomain('get','/au/withid/LnkRiskdir/'+id)
	   			.then(function(data){
	   				$scope.seldir=id;
	   				$scope.selectize_risk_options = $scope.selectize_deposidid_options = data; 		   				
	   			});	
 				mainService.withdomain('get','/au/withid/LnkDirectionNoticeProcedureRisk/'+id)
	   			.then(function(data){	
	   				$scope.selectize_notice_options = $scope.selectize_notice_options = data; 		   				
	   			});	
 			}
            
			$scope.treatment=false;
		    $scope.risk_config = {
                plugins: {
                    'remove_button': {
                        label: ''
                    }
                },
                persist: false,
                maxItems: 1,
                placeholder: 'Сонгох...',
                optgroupField: 'parent_id',
                optgroupLabelField: 'text',
                optgroupValueField: 'ogid',
                valueField: 'value',
                labelField: 'text',
                searchField: 'text',
                create: function(input) {
                	var obj={
                		dirid:	$scope.seldir,
                		risk:input
                	}
                	
            	 	mainService.withdata('POST', '/au/create/'+$scope.domainRisk,  obj).
	    				then(function(data){
	    					$scope.selectize_risk_options.push(data);
	    					$scope.treatment=true;
	    					$scope.isRiskCreated = true;
	    					$scope.risk.riskid=data.value;
	    					UIkit.notify("Эрсдэл амжилттай нэмэгдлээ", {status:'success',timeout : 1000,pos:'top-right'})
	    				});	         
                    return false;
                }
            };
		    
			var treatment_data= $scope.selectize_treatment_options = [];
			$scope.treatmentChange = function(id){
				$scope.isFactorCreated = false;
				if ($scope.seldir != undefined && id != undefined){
					mainService.withdomain('get','/au/withids/LnkTryoutRisk/'+$scope.seldir+'/'+id)
 		   			.then(function(data){ 		   				
 		   				$scope.selectize_treatment_options = treatment_data = data; 		   				
 		   			});	
				}
 				
 			}
			
		    $scope.confirmation=false;
		    $scope.newnotice=true;
		    $scope.domainTryout="com.netgloo.models.LutTryout.";
	        $scope.selectize_treatment_config = {
                plugins: {
                    'remove_button': {
                        label: ''
                    }
                },
                persist: false,
                maxItems: 1,
                placeholder: 'Сонгох...',
                optgroupField: 'parent_id',
                optgroupLabelField: 'text',
                optgroupValueField: 'ogid',
                valueField: 'value',
                labelField: 'text',
                searchField: 'text',
                create: function(input) {
                	var obj={
                		dirid:	$scope.seldir,
                		riskid:	$scope.risk.riskid,
                		treatment:input
                	}
                	
            	 	mainService.withdata('POST', '/au/create/'+$scope.domainTryout,  obj).
    				then(function(data){
    					$scope.selectize_treatment_options.push(data);
    					$scope.confirmation=true;
    					$scope.risk.treatmentid=false;
    					$scope.newnotice=false;
    					$scope.risk.treatmentid=data.value;
    					UIkit.notify("Горим амжилттай нэмэгдлээ", {status:'success',timeout : 1000,pos:'top-right'})
    				});	         
                    return false;
                }
            };
		    $scope.factorChange = function(id){
			   $scope.isFactorCreated = false;
			   //$scope.risk.crition = null;
			   if (id != undefined){
				   mainService.withdomain('get','/au/withids/LutFactor/'+$scope.risk.riskid+'/'+id)
		   			.then(function(data){ 		   				
		   				$scope.selectize_factor_options = factor_data = data; 		   				
		   			});	
			   }
 				
 		    }
	        $scope.selectize_factor_config = {
                plugins: {
                    'remove_button': {
                        label: ''
                    }
                },
                persist: false,
                maxItems: 1,
                placeholder: 'Сонгох...',
                optgroupField: 'parent_id',
                optgroupLabelField: 'text',
                optgroupValueField: 'ogid',
                valueField: 'value',
                labelField: 'text',
                searchField: 'text',
                create: function(input) {
                	var obj={
                		groupid: $scope.risk.groupid,
                		dirid:	$scope.seldir,	                		
                		riskid:	$scope.risk.riskid,
                		treatmentid:$scope.risk.treatmentid,
                		factorname:input
                	}
            	 	mainService.withdata('POST', '/au/create/'+$scope.domainFactor,  obj).
    				then(function(data){
    					$scope.selectize_factor_options.push(data);
    					$scope.risk.factorid=data.value;
    					/*$scope.confirmation=true;
    					$scope.risk.treatmentid=false;
    					$scope.newnotice=false;
    					$scope.risk.treatmentid=data.value;*/
    					$scope.isFactorCreated = true;
    					UIkit.notify("Хүчин зүйл амжилттай нэмэгдлээ", {status:'success',timeout : 1000,pos:'top-right'})
    				});	         
                    return false;
                }
            };
	        $scope.domainLnkMainFormT2="com.netgloo.models.LnkMainFormT2.";
	        $scope.submitFormB25=function(){
            	if ($scope.risk.groupid == undefined || $scope.risk.groupid == null){
            		sweet.show('Анхаар', 'Эрсдэлтэй асуудлын жагсаалтад оруулах мэдээллийн эх үүсвэрийн төрлийг сонгоно уу.', 'error');
            	}
            	else if ($scope.risk.dir == undefined || $scope.risk.dir == null){
            		sweet.show('Анхаар', 'Эрсдэлтэй асуудлын жагсаалтад оруулах мэдээллийн эх үүсвэрийн төрлийг сонгоно уу.', 'error');
            	}
            	else if ($scope.risk.riskid == undefined || $scope.risk.riskid == null){
            		sweet.show('Анхаар', 'Эрсдэлтэй асуудлыг товч тодорхойлох сонголтыг сонгоно уу.', 'error');
            	}
            	else if ($scope.risk.treatmentid == undefined || $scope.risk.treatmentid == null){
            		sweet.show('Анхаар', 'Эрсдэлтэй асуудлыг товч тодорхойлох сонголтыг сонгоно уу.', 'error');
            	}
            	else if ($scope.risk.factorid == undefined || $scope.risk.factorid == null){
            		sweet.show('Анхаар', 'Хүчин зүйл сонгоно уу.', 'error');
            	}
            /*	else if ($scope.isRiskCreated && ($scope.riskstandartlist.length == 0 || $scope.riskhuulilist.length == 0 || $scope.risk.other == null || $scope.risk.other == undefined || $scope.risk.other.length == 0)){
            		sweet.show('Анхаар', 'Эрсдэлд холбогдох хууль тогтоомж, стандартыг сонгоно уу.', 'error');
            	}*/
            	else if ($scope.confirmation && ($scope.risk.conftype == undefined || $scope.risk.conftype == null || $scope.risk.conftype.length == 0 || $scope.risk.confMethod == undefined || $scope.risk.confMethod == null || $scope.risk.confMethod.length == 0 || $scope.risk.confSourceO == undefined || $scope.risk.confSourceO == null || $scope.risk.confSourceO.length == 0 || $scope.risk.confSourceI == undefined || $scope.risk.confSourceI == null || $scope.risk.confSourceI.length == 0)){
            		sweet.show('Анхаар', 'Горим сорилд хамаарах нотлох зүйл цуглуулах арга зүйн мэдээллийг сонгоно уу.', 'error');
            	}
            	else{
            		$scope.risk.mid=$stateParams.issueId;
	            	$scope.risk.riskstandartlist = $scope.riskstandartlist;
	            	$scope.risk.riskhuulilist = $scope.riskhuulilist;
	            	$scope.risk.levelid =newStep;
	            	$scope.loading=false;
	         	    mainService.withdata('POST', '/au/create/'+$scope.domainLnkMainFormT2,  $scope.risk)
	  		   			.then(function(data){
	  		   				if(data.robj){
	  		   				//	modalUriskNew.hide();
	  		   					modalgb25.show();
	  			   				sweet.show('Мэдээлэл', 'Амжилттай бүртгэлээ.', 'success');
	  			   				$(".custisk .k-grid").data("kendoGrid").dataSource.read(); 
	  			   			 //   $state.go('restricted.pages.quataperson');	
	  		   				}
	  		   				else{
	  		   					//modalUrisk.hide();
	  			   				sweet.show('Анхаар', 'Эрсдлийг өмнө нь тодорхойлсон байна.', 'error');
	  		   				}
	  	   			});
            	}
            	
            }
	        
            var modalgb25 = UIkit.modal("#modal_b_25", {modal: false, keyboard: false, bgclose: false, center: false});
            
            var modalgb254 = UIkit.modal("#modal_b_254", {modal: false, keyboard: false, bgclose: false, center: false});
            
        	var modalUriskNew = UIkit.modal("#modal_b_25_new", {modal: false, keyboard: false, bgclose: false, center: false});
    		$scope.risk={};
    		
            $scope.addNew=function(i){
  	   			mainService.withdomain('get','/my/core/resource/LutAuditDir')
	   				.then(function(data){
	   					$scope.dir_data.options=data;
	   			});
	   		
	   			mainService.withdomain('get','/my/core/resource/LutConfirmationMethod')
	   				.then(function(data){
	   					$scope.confMethod=data;
	   			});
	   			mainService.withdomain('get','/my/core/resource/LutConfirmationSourceI')
	   				.then(function(data){
	   					$scope.confSourceI=data;
	   			});
 		
	   			mainService.withdomain('get','/my/core/resource/LutConfirmationSourceO')
	   				.then(function(data){
	   					$scope.confSourceO=data;
	   			});
	   			mainService.withdomain('get','/my/core/resource/LutGroupOfFactorOther')
	   				.then(function(data){
	   					$scope.LutGroupOfFactor=data;
	   				    $scope.rtype_data.options=data;
	   				   
	   			});  
	   			//modalgb25.show();
	   			modalUriskNew.show();            	
            }
            
            $scope.showUlamjlaltRisk=function(){
            	//$state.go('restricted.pages.mainworkrisk',{issueId:$stateParams.issueId,stepid:$scope.note_form.levelid,typeid:$stateParams.typeid,wstep:$scope.note_form.step});	     
            	if(newStep==3){
            		modalgb25.show();
                 	mainService.withdomain('get','/au/resource/LutAuditDir')
		 				.then(function(data){
		 			    $scope.dirs=data;	   			 
		 			});	
            	
            	
	            mainService.withdomain('get','/au/resource/LutGroupOfFactor')
	   			.then(function(data){
	   				mainService.withdomain('get','/au/resource/LutRisk')
		   			.then(function(rdata){
		   				if(data){
		   					$scope.gfactor=data;	
		   					$scope.risks=rdata;
	  	   					$scope.domain="com.netgloo.models.LnkMainFormT2.";
	  	   					var decision=[{"text":"Хамааралгүй","value":"0"},{"text":"Эрсдэлтэй","value":"1"},{"text":"Эрсдэлгүй","value":"2"}];	
	  	   			
	  	   				   	$scope.puserGridM25 = {
			 		                dataSource: {
			 		                    autoSync: true,
			 		                    transport: {
			 		                    	read:  {
			 		                            url: "/au/list/LutFactorCustom",
			 		                            contentType:"application/json; charset=UTF-8",    
			 		                            data:{"mid":$stateParams.issueId, "stepid":"3","sort":[{field: "groupid", dir: "asc"}]},
			 		                            type:"POST"
			 		                        },
			 		                        update: {
			 		                            url: "/work/update/"+$scope.domain+"",
			 		                            contentType:"application/json; charset=UTF-8",
			 		                            data:{"mid":$stateParams.issueId},
			 		                            type:"POST",
			 		                            complete: function(e) {
			 		                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
			 		                    		}
			 		                        },
			 		                        destroy: {
			 		                            url: "/core/delete/"+$scope.domain+"",
			 		                            contentType:"application/json; charset=UTF-8",                                    
			 		                            type:"POST",
			 		                            data: function(e){
			 		                            	
			 		                            },
			 		                            complete: function(e) {
			 		                            	 $("#notificationDestroy").trigger('click');
			 		                    		}
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
			 		                             	id: { type: "number", editable: false,nullable: false},
			 		                             	fnumber: { type: "string",  editable: false },	                             	
			 		                             	factorname: { type: "string", editable: false,},
			 		                             	groupid: { type: "number", editable: false},
			 		                             	dirid: { type: "number",editable: false},
			 		                             	decid: { type: "number",editable: false},
			 		                             	critid: { type: "number",editable: false},
			 		                             	riskid: { type: "number",editable: false},
			 		                             	risknames: { type: "string",editable: false},
			 		                             	treatment: { type: "string",editable: false},
			 		                             	risks: {},
			 		                             	description: { type: "string",editable: false}
			 		                             }	                    
			 		                         }
			 		                     },
			 		                   /*  group: {
			                                  field: "groupid",values:$scope.gfactor
			                                },*/
			 		                    pageSize: 10,
			 		                    serverPaging: true,
			 		                    serverSorting: true,
			 		                    serverFiltering: true
			 		                },
			 		                sortable: true,
			 		                resizable: true,
			 		                groupable:true,
			 		                height: function(){
			 		                    return $(window).height()-100;
			 		                },
			 		                pageable: {
			 		                    refresh: true,
			 		                    pageSizes: true,
			 		                    buttonCount: 5
			 		                },
			 		                excel: {
		    	   		                fileName: "Organization Export.xlsx",
		    	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
		    	   		                filterable: true,
		    	   		                allPages: true
		    	   		            },
			 		                columns: [
			 		                		  { title: "#",template: "<span class='row-number'></span>", width:60},
			 		                		  { field:"groupid",title: "Хүчин зүйлс", values:$scope.gfactor,width:200},		
			 		                		  { field:"factorname", title: "Эрсдэл бий болох хүчин зүйлс буюу аудитын цар хүрээ",width:400},	
			 		                	      { field:"dirid", title: "Аудитын чиглэл", values:$scope.dirs, width: 200 },		 		                         	 		                        
			 		                          { field:"critid", title: "Тухайн эрсдэл болон хүчин зүйлсэд хамаарах шалгуур үзүүлэлт", template: "#=crition#", width:200},		 		                         
			 		                          { field:"treatment",width: 400, title: "Хэрэгжүүлэхээр төлөвлөсөн горим сорил"},
			 		                          { field:"riskid",width: 400, title: "Илрүүлсэн эрсдэлийн товч утга",values:rdata},
			 		                          { field:"description", title: "Тайлбар",width: 400},		 		                        
			 			                      ],
			 			                      editable:false,
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
	  	   				   	   	
	  	   				   	 
	  	   					
	  	   						$scope.role=user_data[0].role;
			  	   			    if($scope.role=="ROLE_AUDIT" && $scope.note_form.step!=3){	   		   						
			  		            	$scope.puserGridM25.editable=true;
			  		            	$scope.puserGridM25.toolbar=kendo.template($("#exportCustomExport").html());
			  		            	$scope.puserGridM25.columns.push({template: $("#updateCustomRisk").html(), width:250});
		  	  					}
			  	   			    else if($scope.role=="INDP" && $scope.note_form.step!=3){	   		   						
			  		            	$scope.puserGridM25.editable=true;
			  		            	$scope.puserGridM25.toolbar=kendo.template($("#exportCustomExport").html());
			  		            	$scope.puserGridM25.columns.push({template: $("#updateCustomRisk").html(), width:250});
		  	  					}
			  		            else{
			  		            	$scope.puserGridM25.editable=false
			  		            	$scope.puserGridM25.toolbar=kendo.template($("#back").html());
		  		            	}
		   				 
		   					}
		   				else{
			   				sweet.show('Анхаар', 'Error...', 'error');
		   				}
	   				});			   			
	   			});
            	}
            	else{
            		modalgb254.show();
	                 	mainService.withdomain('get','/au/resource/LutAuditDir')
		 				.then(function(data){
		 			    $scope.dirs=data;	   			 
		 			});	
            	
            	
	            mainService.withdomain('get','/au/resource/LutGroupOfFactor')
	   			.then(function(data){
	   				mainService.withdomain('get','/au/resource/LutRisk')
		   			.then(function(rdata){
		   				if(data){
		   					$scope.gfactor=data;	
		   					$scope.risks=rdata;
	  	   					$scope.domain="com.netgloo.models.LnkMainFormT2.";
	  	   					var decision=[{"text":"Хамааралгүй","value":"0"},{"text":"Эрсдэлтэй","value":"1"},{"text":"Эрсдэлгүй","value":"2"}];	
	  	   				
	  	   				   	   
	  	   						$scope.puserGridM254 = {
			 		                dataSource: {
			 		                    autoSync: true,
			 		                    transport: {
			 		                    	read:  {
			 		                            url: "/au/list/LutFactorCustom",
			 		                            contentType:"application/json; charset=UTF-8",    
			 		                            data:{"mid":$stateParams.issueId, "stepid":"4","sort":[{field: "groupid", dir: "asc"}]},
			 		                            type:"POST"
			 		                        },
			 		                        update: {
			 		                            url: "/work/update/"+$scope.domain+"",
			 		                            contentType:"application/json; charset=UTF-8",
			 		                            data:{"mid":$stateParams.issueId},
			 		                            type:"POST",
			 		                            complete: function(e) {
			 		                    			$(".k-grid").data("kendoGrid").dataSource.read(); 
			 		                    		}
			 		                        },
			 		                        destroy: {
			 		                            url: "/core/delete/"+$scope.domain+"",
			 		                            contentType:"application/json; charset=UTF-8",                                    
			 		                            type:"POST",
			 		                            data: function(e){
			 		                            	
			 		                            },
			 		                            complete: function(e) {
			 		                            	 $("#notificationDestroy").trigger('click');
			 		                    		}
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
			 		                             	id: { type: "number", editable: false,nullable: false},
			 		                             	fnumber: { type: "string",  editable: false },	                             	
			 		                             	factorname: { type: "string", editable: false,},
			 		                             	groupid: { type: "number", editable: false},
			 		                             	dirid: { type: "number",editable: false},
			 		                             	decid: { type: "number",editable: false},
			 		                             	critid: { type: "number",editable: false},
			 		                             	riskid: { type: "number",editable: false},
			 		                             	risknames: { type: "string",editable: false},
			 		                             	treatment: { type: "string",editable: false},
			 		                             	risks: {},
			 		                             	description: { type: "string",editable: false}
			 		                             }	                    
			 		                         }
			 		                     },
			 		                   /*  group: {
			                                  field: "groupid",values:$scope.gfactor
			                                },*/
			 		                    pageSize: 10,
			 		                    serverPaging: true,
			 		                    serverSorting: true,
			 		                    serverFiltering: true
			 		                },
			 		                sortable: true,
			 		                resizable: true,
			 		                groupable:true,
			 		                height: function(){
			 		                    return $(window).height()-100;
			 		                },
			 		                pageable: {
			 		                    refresh: true,
			 		                    pageSizes: true,
			 		                    buttonCount: 5
			 		                },
			 		                excel: {
		    	   		                fileName: "Organization Export.xlsx",
		    	   		                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
		    	   		                filterable: true,
		    	   		                allPages: true
		    	   		            },
			 		                columns: [
			 		                		  { title: "#",template: "<span class='row-number'></span>", width:60},
			 		                		  { field:"groupid",title: "Хүчин зүйлс", values:$scope.gfactor,width:200},		
			 		                		  { field:"factorname", title: "Эрсдэл бий болох хүчин зүйлс буюу аудитын цар хүрээ",width:400},	
			 		                	      { field:"dirid", title: "Аудитын чиглэл", values:$scope.dirs, width: 200 },		 		                         	 		                        
			 		                          { field:"critid", title: "Тухайн эрсдэл болон хүчин зүйлсэд хамаарах шалгуур үзүүлэлт", template: "#=crition#", width:200},		 		                         
			 		                          { field:"treatment",width: 400, title: "Хэрэгжүүлэхээр төлөвлөсөн горим сорил"},
			 		                          { field:"riskid",width: 400, title: "Илрүүлсэн эрсдэлийн товч утга",values:rdata},
			 		                          { field:"description", title: "Тайлбар",width: 400},		 		                        
			 			                      ],
			 			                      editable:false,
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
		  	   					
		  	   						$scope.role=user_data[0].role;
				  	   			    if($scope.role=="ROLE_AUDIT" && $scope.note_form.step!=3){	   		   						
				  		            	$scope.puserGridM254.editable=true;
				  		            	$scope.puserGridM254.toolbar=kendo.template($("#exportCustomExport").html());
				  		            	$scope.puserGridM254.columns.push({template: $("#updateCustomRisk").html(), width:250});
			  	  					}
				  	   			    else if($scope.role=="INDP" && $scope.note_form.step!=3){	   		   						
				  		            	$scope.puserGridM254.editable=true;
				  		            	$scope.puserGridM254.toolbar=kendo.template($("#exportCustomExport").html());
				  		            	$scope.puserGridM254.columns.push({template: $("#updateCustomRisk").html(), width:250});
			  	  					}
				  		            else{
				  		            	$scope.puserGridM254.editable=false
				  		            	$scope.puserGridM254.toolbar=kendo.template($("#back").html());
			  		            	}
			   				 
			   					}
			   				else{
				   				sweet.show('Анхаар', 'Error...', 'error');
			   				}
		   				});			   			
		   			});
            	}
            	
            	/*$scope.load_url="";
            	$ocLazyLoad.load('app/audit/customRisk/customRiskController2.js').then(function(){
					$scope.load_url = 'app/audit/customRisk/customRiskView2.html';
				});*/
        	/*	mainService.withdomain('get','/my/core/resource/LutAuditDir')
	   				.then(function(data){
	   					$scope.dir_data.options=data.data;
	   			});
	   		
	   			mainService.withdomain('get','/my/core/resource/LutConfirmationMethod')
	   				.then(function(data){
	   					$scope.confMethod=data.data;
	   			});
	   			mainService.withdomain('get','/my/core/resource/LutConfirmationSourceI')
	   				.then(function(data){
	   					$scope.confSourceI=data.data;
	   			});
			
	   			mainService.withdomain('get','/my/core/resource/LutConfirmationSourceO')
	   				.then(function(data){
	   					$scope.confSourceO=data.data;
	   			});
	   			mainService.withdomain('get','/my/core/resource/LutGroupOfFactorOther')
	   				.then(function(data){
	   					$scope.LutGroupOfFactor=data.data;
	   				    $scope.rtype_data_options=data.data;
	   				   
	   			});*/
       
            }
            
            $scope.editCustomRisk = function(dirid, factorid, riskid){
            	$scope.isRiskCreated = false;
            	$scope.confirmation = false;
            	mainService.withdata("post","/au/getLnkMainFormT2", {dirid:dirid, factorid:factorid, riskid:riskid, mid:$scope.mid}).then(function(response){
            		console.log(response);
            		$scope.risk = response;
            		
            		mainService.withdomain('get','/my/core/resource/LutAuditDir')
	   				.then(function(data){
	   					$scope.dir_data.options=data;
		   			});
		   		
		   			mainService.withdomain('get','/my/core/resource/LutConfirmationMethod')
		   				.then(function(data){
		   					$scope.confMethod=data;
		   			});
		   			mainService.withdomain('get','/my/core/resource/LutConfirmationSourceI')
		   				.then(function(data){
		   					$scope.confSourceI=data;
		   			});
	 		
		   			mainService.withdomain('get','/my/core/resource/LutConfirmationSourceO')
		   				.then(function(data){
		   					$scope.confSourceO=data;
		   			});
		   			mainService.withdomain('get','/my/core/resource/LutGroupOfFactorOther')
		   				.then(function(data){
		   					$scope.LutGroupOfFactor=data;
		   				    $scope.rtype_data.options=data;		   				   
		   			});
	   			
            		modalUriskNew.show();
            		//UIkit.modal("#modal_editrisk").show();
            	});
            	
            }
            
            $scope.deleteCustomRisk = function(dirid, factorid, riskid,rtype){
            	if (confirm("Эрсдлийг устгах уу?")){
            		$scope.isRiskCreated = false;
	            	$scope.confirmation = false;
	            	$scope.isFactorCreated = false;
	            	$scope.risk = {};
	            	mainService.withdata("post","/core/delete/"+$scope.domain, {dirid:dirid, factorid:factorid, riskid:riskid,rtype:rtype, mid:$scope.mid}).then(function(response){
	            		$(".k-grid").data("kendoGrid").dataSource.read();
	            	});
            	}
            }
            
            var modalgb465 = UIkit.modal("#modal_work_gb465");
            $scope.showGB465= function(){
            	
    			$scope.domain="com.netgloo.models.LnkRiskTryout.";
				var yesnodata=[{"text":"Үгүй","value":0},{"text":"Тийм","value":1}];	
				   	   					
				$scope.showGBGRID465 = {
	                dataSource: {
	                    //autoSync: true,
	                    transport: {
	                    	read:  {
	                            url: "/au/list/LnkRiskTryout",
	                            contentType:"application/json; charset=UTF-8",    
	                            data:{"custom":"where mid="+$stateParams.issueId+" and data23=1 and b46data4=5"},
	                            type:"POST"
	                        },
	                        update: {
	                            url: "/work/update/"+$scope.domain+"",
	                            contentType:"application/json; charset=UTF-8",
	                            data:{"mid":$stateParams.issueId},
	                            type:"POST",
	                            complete: function(e) {
	                               	if(e.responseText=="false"){			 		                            		
	                            		UIkit.notify("Алдаа үүслээ.", {status:'warning'});
	                            	}else{
	                            		UIkit.notify("Амжилттай хадгаллаа.", {status:'success'});
	                            	}
	                               	$(".showGBGRID465 .k-grid").data("kendoGrid").dataSource.read(); 
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
	                             	b465data1: { type: "string",editable: true},
	                             	b465data2: { type: "string",editable: true},
	                             	b465data3: { type: "string",editable: true},
	                               	b46data1: { type: "string",editable: false},
	                             	b46data2: { type: "string",editable: false},
	                             	b46data3: { type: "string",editable: false},
	                             	data1: { type: "string",editable: false},
	                             	data20: { type: "string",editable: false},
	                             	data21: { type: "string",editable: false}
	                             }	                    
	                         }
	                     },
	                    pageSize: 20,
	                    serverPaging: true,
	                    serverSorting: true,
	                    serverFiltering: true,
	                    batch: true
	                },
	                sortable: true,
	                resizable: true,
	               
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
	                		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},
	                		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
	                		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 200 },	
	                		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
	                		  { field:"b46data1", title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт </span>",width: "200px"},
	                		  { field:"b46data2", title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px"},
	                		  { field:"b46data3", title: "<span style='vertical-align:middle'>Асуудлыг шийдвэрлэхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: "200px"},
	                		  { field:"b465data1", template:"# if (b465data1 != null) { # <span class='tulgaltRed'>#:b465data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #",  editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах ажилтны албан тушаал, овог нэр</span>",width: "200px"},
	                		  { field:"b465data2", template:"# if (b465data2 != null) { # <span class='tulgaltRed'>#:b465data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #",  editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Хариуцах байгууллага, эрх бүхий албан тушаалтны санал / тайлбар</span>",width: "200px"},
	                		  { field:"b465data3", template:"# if (b465data3 != null) { # <span class='tulgaltRed'>#:b465data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #",  editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px"},
	                		 /* { 
			                          	 template: kendo.template($("#update").html()),  width: "150px" 
			                      }*/],
		                        dataBound: function () {
	   	   		                var rows = this.items();
	   	   		                  $(rows).each(function () {
	   	   		                      var index = $(this).index() + 1 
	   	   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
	   	   		                      var rowLabel = $(this).find(".row-number");
	   	   		                      $(rowLabel).html(index);
	   	   		                  });
		   	   		                  
			   	   		         
		   			              
   	   		  	           },
		   	   		  	  editable: $scope.editable
	            };
				modalgb465.show();
				//$scope.showGBGRID41.toolbar=["excel","pdf"];
				$scope.showGBGRID465.toolbar=kendo.template($("#exportExcel").html());
				if($scope.editable){
					$scope.showGBGRID465.toolbar=["save"];
				}
            }
            
            var modalt1 = UIkit.modal("#modal_work_t1");
            $scope.showT1= function(t){
            	$state.go('restricted.pages.accSurvey',{typeid:$stateParams.typeid,planid:$stateParams.issueId,stepid:$scope.note_form.levelid,formid:t});
            }
            
        	var modalpdf = UIkit.modal("#modal_pdf");
        	$scope.show =function(x){
          	    $scope.purl=x;
             }
            $scope.showExcel =function(x){
         	    $scope.purl=x;
         	    var xhr = new XMLHttpRequest();
	           	xhr.open('GET',  $scope.purl, true);
	           	xhr.responseType = 'blob';
	           	 
	           	xhr.onload = function(e) {
	           	  if (this.status == 200) {
	           	    // get binary data as a response
	           	    var blob = this.response;
	           	    var spreadsheet = $("#spreadSheetFileView").data("kendoSpreadsheet");
	 		            spreadsheet.fromFile(blob);
	 		          
				   		UIkit.modal("#modal_excel_file").show();
	           	  }
	           	  else{
	           		  sweet.show('Анхаар!', 'Файл устгагдсан байна.', 'error');
	           	  }
	           	};
	           	 
	           	xhr.send();
            }
        }
    ]);