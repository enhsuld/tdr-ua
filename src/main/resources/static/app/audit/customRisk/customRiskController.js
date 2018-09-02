angular
    .module('altairApp')
    	.controller("customRiskCtrl",['$rootScope','$scope','user_data','mainService','sweet','$state','$stateParams','mainobj','dirs','conftype','confMethod','confSourceI','confSourceO','LutGroupOfFactor',
	        function ($rootScope,$scope,user_data,mainService,sweet,$state,$stateParams,mainobj,dirs,conftype,confMethod,confSourceI,confSourceO,LutGroupOfFactor) {   
    			//$scope.risk = {};
    			$scope.project=mainobj;
    			$scope.mid=$stateParams.issueId;
    				
    			$scope.role=user_data[0].role;
    			$scope.isRiskCreated = false;
    			$scope.isFactorCreated = false;
    			$scope.dirs =dirs;
    			if($stateParams.stepid==1 && $scope.role=='ROLE_FIRST'){
    				$scope.manager=true;
    			}
    			else if($stateParams.stepid==2 && $scope.role=='ROLE_SECOND'){
    				$scope.terguuleh=true;
    			}
    			else if($stateParams.stepid==3 && $scope.role=='ROLE_ALL'){
    				$scope.all=true;
    			}
    			else if($stateParams.stepid==4 && $scope.role=='ROLE_FIRST'){
    				$scope.backed=true;
    			}
 
    			var modal = UIkit.modal("#modal_update");
    	    	
    		
    			$scope.domain="com.netgloo.models.LnkMainUser.";
    			
    			var rtype=[{"text":"УИХ-аас өгсөн үүрэг чиглэл","value":1},
    				{"text":"Ерөнхий аудитороос өгсөн үүрэг чиглэл","value":2},
    				{"text":"Олон нийтийн мэдээллийн хэрэгслээс","value":3},
    				{"text":"Аудитор өөрөө шинээр илрүүлсэн","value":4},
    				{"text":"Санхүүгийн тайлангийн тулгалтаар","value":5},
    				{"text":"Урьдчилсан шинжилгээний горим хэрэгжүүлсэн","value":6}
				];
    			
    			 $scope.rtype_data = {
	                options: []
	             };
    			 
    			 $scope.riskstandartlist = [];
    			 $scope.riskhuulilist = [];
    			 
    			 $scope.dir_data = {
    		                options: []
    		             };
    			 $scope.dir_data.options=dirs;
    			 $scope.rtype_data.options=LutGroupOfFactor;
    			 
    			
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
    			
    			var aj=[{"text":"Тийм","value":"1"},{"text":"Үгүй","value":"0"}];
    			
    		
    			var risk_data= $scope.selectize_risk_options = [];
    			$scope.riskChange = function(id){
    				$scope.isFactorCreated = false;
    				$scope.risk.crition = null;
    				/*$scope.risk.treatmentid = null;
    				$scope.selectize_treatment_options = [];
    				$scope.risk.factorid = null;
    				$scope.selectize_factor_options = [];
    				$scope.risk.riskid = null;
    				$scope.selectize_risk_options = [];*/
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
    			$scope.domainRisk="com.netgloo.models.LutRisk.";
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
    				//$scope.risk.crition = null;
    				/*$scope.risk.treatmentid = null;
    				$scope.selectize_treatment_options = [];
    				$scope.risk.factorid = null;
    				$scope.selectize_factor_options = [];*/
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
		       $scope.domainFactor="com.netgloo.models.LutFactor.";
		       var factor_data= $scope.selectize_factor_options = [];
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
    			
	    		var conftype_data = $scope.selectize_conftype_options = conftype;
	   			
	   			$scope.selectize_conftype_config = {
	   	                plugins: {
	   	                    'remove_button': {
	   	                        label     : ''
	   	                    }
	   	                },
	   	                maxItems: null,
	   	                minItems:1,
	   	                valueField: 'value',
	   	                labelField: 'text',
	   	                searchField: 'text',
	   	                create: false,
	   	                render: {
	   	                    option: function(conftype_data, escape) {
	   	                        return  '<div class="option">' +
	   	                            '<span class="title">' + escape(conftype_data.text) + '</span>' +
	   	                            '</div>';
	   	                    }
	   	                }
	   	            };
	   			var confSourceO_data = $scope.selectize_confSourceO_options = confSourceO;
	   			
	   			$scope.selectize_confSourceO_config = {
	   	                plugins: {
	   	                    'remove_button': {
	   	                        label     : ''
	   	                    }
	   	                },
	   	                maxItems: null,
	   	                minItems:1,
	   	                valueField: 'value',
	   	                labelField: 'text',
	   	                searchField: 'text',
	   	                create: false,
	   	                render: {
	   	                    option: function(confSourceO_data, escape) {
	   	                        return  '<div class="option">' +
	   	                            '<span class="title">' + escape(confSourceO_data.text) + '</span>' +
	   	                            '</div>';
	   	                    }
	   	                }
	   	            };
	   			
	   			var confSourceI_data = $scope.selectize_confSourceI_options = confSourceI;
	   			
	   			$scope.selectize_confSourceI_config = {
	   	                plugins: {
	   	                    'remove_button': {
	   	                        label     : ''
	   	                    }
	   	                },
	   	                maxItems: null,
	   	                minItems:1,
	   	                valueField: 'value',
	   	                labelField: 'text',
	   	                searchField: 'text',
	   	                create: false,
	   	                render: {
	   	                    option: function(confSourceI_data, escape) {
	   	                        return  '<div class="option">' +
	   	                            '<span class="title">' + escape(confSourceI_data.text) + '</span>' +
	   	                            '</div>';
	   	                    }
	   	                }
	   	            };
	   			
	   			var confMethod_data = $scope.selectize_confMethod_options = confMethod;
	   			
	   			$scope.selectize_confMethod_config = {
	   	                plugins: {
	   	                    'remove_button': {
	   	                        label     : ''
	   	                    }
	   	                },
	   	                maxItems: null,
	   	                minItems:1,
	   	                valueField: 'value',
	   	                labelField: 'text',
	   	                searchField: 'text',
	   	                create: false,
	   	                render: {
	   	                    option: function(confMethod_data, escape) {
	   	                        return  '<div class="option">' +
	   	                            '<span class="title">' + escape(confMethod_data.text) + '</span>' +
	   	                            '</div>';
	   	                    }
	   	                }
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
    			
	            $scope.loading=true;
	            $scope.domainLnkMainFormT2="com.netgloo.models.LnkMainFormT2.";
	            $scope.submitForm=function(){
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
		            	$scope.risk.levelid = $stateParams.stepid;
		            	$scope.loading=false;
		         	    mainService.withdata('POST', '/au/create/'+$scope.domainLnkMainFormT2,  $scope.risk)
		  		   			.then(function(data){
		  		   				if(data.robj){
		  		   					modalUrisk.hide();
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
	            
	            $scope.submitUpdateForm = function(){
	            	if ($scope.risk.groupid == undefined || $scope.risk.groupid == null){
	            		sweet.show('Анхаар', 'Эрсдэлтэй асуудлын жагсаалтад оруулах мэдээллийн эх үүсвэрийн төрлийг сонгоно уу.', 'error');
	            	}
	            	else if ($scope.risk.dirid == undefined || $scope.risk.dirid == null){
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
	            	else if ($scope.isRiskCreated && ($scope.riskstandartlist.length == 0 || $scope.riskhuulilist.length == 0 || $scope.risk.other == null || $scope.risk.other == undefined || $scope.risk.other.length == 0)){
	            		sweet.show('Анхаар', 'Эрсдэлд холбогдох хууль тогтоомж, стандартыг сонгоно уу.', 'error');
	            	}
	            	else if ($scope.confirmation && ($scope.risk.conftype == undefined || $scope.risk.conftype == null || $scope.risk.conftype.length == 0 || $scope.risk.confMethod == undefined || $scope.risk.confMethod == null || $scope.risk.confMethod.length == 0 || $scope.risk.confSourceO == undefined || $scope.risk.confSourceO == null || $scope.risk.confSourceO.length == 0 || $scope.risk.confSourceI == undefined || $scope.risk.confSourceI == null || $scope.risk.confSourceI.length == 0)){
	            		sweet.show('Анхаар', 'Горим сорилд хамаарах нотлох зүйл цуглуулах арга зүйн мэдээллийг сонгоно уу.', 'error');
	            	}
	            	else{
	            		$scope.risk.mid=$stateParams.issueId;
		            	$scope.risk.riskstandartlist = $scope.riskstandartlist;
		            	$scope.risk.riskhuulilist = $scope.riskhuulilist;
		            	$scope.risk.levelid = $stateParams.stepid;
		            	$scope.loading=false;
		         	    mainService.withdata('POST', '/au/update/'+$scope.domainLnkMainFormT2,  $scope.risk)
		  		   			.then(function(data){
		  		   				if(data){
		  		   					UIkit.modal("#modal_editrisk").hide();
		  			   				sweet.show('Мэдээлэл', 'Амжилттай хадгалагдлаа.', 'success');
		  			   				$(".custisk .k-grid").data("kendoGrid").dataSource.read(); 
		  			   			 //   $state.go('restricted.pages.quataperson');	
		  		   				}
		  		   				else{
		  		   					modalUrisk.hide();
		  			   			//	sweet.show('Анхаар', 'Error...', 'error');
		  		   				}
		  	   			});
	            	}
	            	
	            }
	   
	            var modalUrisk = UIkit.modal("#modal_risk");
	            
	            $scope.addNew=function(i){
	            	
	            	$scope.risk={};
	            	$scope.isRiskCreated = false;
	    			$scope.isFactorCreated = false;
		            $scope.zuil_list = [];
		            $scope.zaalt_list = [];
		            $scope.huulizuil_list = [];
		            $scope.huulizaalt_list = [];
		            $scope.riskstandartlist = [];
		            $scope.riskhuulilist = [];
		            $scope.confirmation = false;
	            	modalUrisk.show();    
	            }
	            
	            $scope.back=function(){	            	
	            	$state.go('restricted.pages.mainwork',{issueId:$stateParams.issueId,stepid:$stateParams.stepid,typeid:$stateParams.typeid});	        
	            }
	            
	            $scope.law_list = [];
	            
	            $scope.huuli_list = [];
	            
	            $scope.zuil_list = [];
	            
	            $scope.zaalt_list = [];
	            
	            $scope.huulizuil_list = [];
	            
	            $scope.huulizaalt_list = [];
	            
	            mainService.withdomain("get","/my/core/resource/LutLaw2").then(function(data){
	            	$scope.law_list = data;
	            });
	            
	            mainService.withdomain("get","/my/core/resource/LutLaw1").then(function(data){
	            	$scope.huuli_list = data;
	            });
	            
	            $scope.selectize_law_config = {
                    persist: false,
                    maxItems: 1,
                    valueField: 'value',
                    labelField: 'text',
                    searchField: ['text'],
                    placeholder: "Сонгох..",
                    create: function(input) {
                    	return mainService.withdata("post","/au/create/com.netgloo.models.LutLaw.", {lawname:input,lawcategory:2}).then(function(data){
                    		$scope.law_list.push(data);
                    		$scope.risklaw.LawId = data.value;
                    		return data;
                    	});
                    }
                };
	            
	            $scope.selectize_huuli_config = {
	                    persist: false,
	                    maxItems: 1,
	                    valueField: 'value',
	                    labelField: 'text',
	                    searchField: ['text'],
	                    placeholder: "Сонгох..",
	                    create: function(input) {
	                    	return mainService.withdata("post","/au/create/com.netgloo.models.LutLaw.", {lawname:input,lawcategory:1}).then(function(data){
	                    		$scope.huuli_list.push(data);
	                    		$scope.riskhuuli.LawId = data.value;
	                    		return data;
	                    	});
	                    }
	                };
	            
	            $scope.selectize_zuil_config = {
                    persist: false,
                    maxItems: 1,
                    valueField: 'value',
                    labelField: 'text',
                    searchField: ['text'],
                    placeholder: "Сонгох..",
                    create: function(input) {
                    	return mainService.withdata("post","/au/create/com.netgloo.models.LutLaw.", {lawname:input,lawcategory:2,parentid:$scope.risklaw.LawId}).then(function(data){
                    		$scope.zuil_list.push(data);
                    		$scope.risklaw.ZuilId = data.value;
                    		return data;
                    	});
                    }
                };
	            
	            $scope.selectize_zuilhuuli_config = {
	                    persist: false,
	                    maxItems: 1,
	                    valueField: 'value',
	                    labelField: 'text',
	                    searchField: ['text'],
	                    placeholder: "Сонгох..",
	                    create: function(input) {
	                    	return mainService.withdata("post","/au/create/com.netgloo.models.LutLaw.", {lawname:input,lawcategory:1,parentid:$scope.riskhuuli.LawId}).then(function(data){
	                    		$scope.zuil_list.push(data);
	                    		$scope.riskhuuli.ZuilId = data.value;
	                    		return data;
	                    	});
	                    }
	                };
	            
	            
	            $scope.pushRisk = function(){
	            	//console.log($scope.riskstandartlist.indexOf($scope.risklaw));
	            	if ($scope.riskstandartlist.map(function(e) { return e.ZaaltId; }).indexOf($scope.risklaw.ZaaltId)==-1 && $scope.risklaw.LawId != undefined && $scope.risklaw.ZuilId != undefined && $scope.risklaw.ZaaltId != undefined && $scope.risk.riskid != undefined && $scope.risk.riskid > 0 && $scope.risklaw.LawId > 0 && $scope.risklaw.ZuilId > 0 && $scope.risklaw.ZaaltId > 0){
	            		mainService.withdata("POST", "/au/create/com.netgloo.models.LnkRiskLaw.", {'riskid':$scope.risk.riskid, 'lawid':$scope.risklaw.ZaaltId}).then(function(data){
	            			if (data == true){
	            				$scope.riskstandartlist.push($scope.risklaw);
	    		            	$scope.risklaw = {};
	    		            	$scope.zuil_list = [];
	    		            	$scope.zaalt_list = [];
	            			}
	            		});
	            		
	            	}
	            	else{
	            		alert("Та энэ сонголтыг өмнө нь хийсэн байна.");
	            	}
	            }
	            
	            $scope.pushRiskHuuli = function(){
	            	if ($scope.riskhuulilist.map(function(e) { return e.ZaaltId; }).indexOf($scope.riskhuuli.ZaaltId)==-1 && $scope.riskhuuli.LawId != undefined && $scope.riskhuuli.ZuilId != undefined && $scope.riskhuuli.ZaaltId != undefined && $scope.risk.riskid != undefined && $scope.risk.riskid > 0 && $scope.riskhuuli.LawId > 0 && $scope.riskhuuli.ZuilId > 0 && $scope.riskhuuli.ZaaltId > 0){
	            		mainService.withdata("POST", "/au/create/com.netgloo.models.LnkRiskLaw.", {'riskid':$scope.risk.riskid, 'lawid':$scope.riskhuuli.ZaaltId}).then(function(data){
	            			if (data == true){
	            				$scope.riskhuulilist.push($scope.riskhuuli);
	    		            	$scope.riskhuuli = {};
	    		            	$scope.huulizuil_list = [];
	    		            	$scope.huulizaalt_list = [];
	            			}
	            		});
	            		
	            	}
	            	else{
	            		alert("Та энэ сонголтыг өмнө нь хийсэн байна.");
	            	}
	            }
	            
	            $scope.removeRisk = function(index){
	            	if (confirm("Устгах уу?")){
	            		$scope.riskstandartlist[index]['riskid'] = $scope.risk.riskid;
	            		mainService.withdata("POST","/work/delete/com.netgloo.models.LnkRiskLaw.",JSON.stringify($scope.riskstandartlist[index])).then(function(data){
	            			if (data == true){
	            				$scope.riskstandartlist.splice(index,1);
	            			}
	            		});
	            	}
	            }
	            
	            $scope.removeRiskHuuli = function(index){
	            	if (confirm("Устгах уу?")){
	            		$scope.riskhuulilist[index]['riskid'] = $scope.risk.riskid;
	            		mainService.withdata("POST","/work/delete/com.netgloo.models.LnkRiskLaw.",JSON.stringify($scope.riskhuulilist[index])).then(function(data){
	            			if (data == true){
	            				$scope.riskhuulilist.splice(index,1);
	            			}
	            		});
	            	}
	            }
	            
	            $scope.editCustomRisk = function(dirid, factorid, riskid){
	            	$scope.isRiskCreated = false;
	            	$scope.confirmation = false;
	            	mainService.withdata("post","/au/getLnkMainFormT2", {dirid:dirid, factorid:factorid, riskid:riskid, mid:$scope.mid}).then(function(response){
	            		console.log(response);
	            		$scope.risk = response;
	            		UIkit.modal("#modal_editrisk").show();
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

	            $scope.selectize_zaalt_config = {
                    persist: false,
                    maxItems: 1,
                    valueField: 'value',
                    labelField: 'text',
                    searchField: ['text'],
                    placeholder: "Сонгох..",
                    create: function(input) {
                    	return mainService.withdata("post","/au/create/com.netgloo.models.LutLaw.", {lawname:input,lawcategory:2}).then(function(data){
                    		$scope.zaalt_list.push(data);
                    		$scope.risklaw.ZaaltId = data.value;
                    		return data;
                    	});
                    }
                };
	            
	            $scope.selectize_zaalthuuli_config = {
                    persist: false,
                    maxItems: 1,
                    valueField: 'value',
                    labelField: 'text',
                    searchField: ['text'],
                    placeholder: "Сонгох..",
                    create: function(input) {
                    	return mainService.withdata("post","/au/create/com.netgloo.models.LutLaw.", {lawname:input,lawcategory:1}).then(function(data){
                    		$scope.huulizaalt_list.push(data);
                    		$scope.riskhuuli.ZaaltId = data.value;
                    		return data;
                    	});
                    }
                };
	            
	            $scope.changedStandartSelect =function(type,id){
	            	if (id != undefined && id != null){
	            		if (type == "lawNameSelect"){
		            		mainService.withdomain("GET","/work/laws?parentid=" + id + "&type=2").then(function(data){
		            			$scope.zuil_list = data;
		            			for(var i=0;i<$scope.law_list.length;i++){
			            			if ($scope.law_list[i].value == id.toString()){
			            				$scope.risklaw.law = $scope.law_list[i];
			            			}
			            		}
		            		});
		            		
		            		/*$("#lawZuilSelect" + index).prop("disabled", false);
		            		$("#lawZuilSelect" + index).data("kendoDropDownList").dataSource.read();*/
		            	}
		            	else if (type == "lawZuilSelect"){
		            		mainService.withdomain("GET","/work/laws?parentid=" + id + "&type=2").then(function(data){
	        					$scope.zaalt_list = data;
	        					for(var i=0;i<$scope.zuil_list.length;i++){
			            			if ($scope.zuil_list[i].value == id.toString()){
			            				$scope.risklaw.zuil = $scope.zuil_list[i];
			            			}
			            		}
		            		});
		            		
		            	}
		            	else if (type =="lawZaaltSelect"){
		            		for(var i=0;i<$scope.zaalt_list.length;i++){
		            			if ($scope.zaalt_list[i].value == id.toString()){
		            				$scope.risklaw.zaalt = $scope.zaalt_list[i];
		            			}
		            		}
		            	}
		            	else if (type == "lawNameSelectHuuli"){
		            		mainService.withdomain("GET","/work/laws?parentid=" + id + "&type=1").then(function(data){
		            			$scope.huulizuil_list = data;
		            			for(var i=0;i<$scope.huuli_list.length;i++){
			            			if ($scope.huuli_list[i].value == id.toString()){
			            				$scope.riskhuuli.law = $scope.huuli_list[i];
			            			}
			            		}
		            		});
		            		
		            	}
		            	else if (type == "lawZuilSelectHuuli"){
		            		mainService.withdomain("GET","/work/laws?parentid=" + id + "&type=1").then(function(data){
	        					$scope.huulizaalt_list = data;
	        					for(var i=0;i<$scope.huulizuil_list.length;i++){
			            			if ($scope.huulizuil_list[i].value == id.toString()){
			            				$scope.riskhuuli.zuil = $scope.huulizuil_list[i];
			            			}
			            		}
		            		});
		            		
		            	}
		            	else if (type =="lawZaaltSelectHuuli"){
		            		for(var i=0;i<$scope.huulizaalt_list.length;i++){
		            			if ($scope.huulizaalt_list[i].value == id.toString()){
		            				$scope.riskhuuli.zaalt = $scope.huulizaalt_list[i];
		            			}
		            		}
		            	}
	            	}
	            	
	            }
	      
	            mainService.withdomain('get','/au/resource/LutGroupOfFactor')
	   			.then(function(data){
	   				mainService.withdomain('get','/au/resource/LutRisk')
		   			.then(function(rdata){
		   				if(data){
		   					$scope.gfactor=data;	
		   					$scope.risks=rdata;
	  	   					$scope.domain="com.netgloo.models.LnkMainFormT2.";
	  	   					var decision=[{"text":"Хамааралгүй","value":"0"},{"text":"Эрсдэлтэй","value":"1"},{"text":"Эрсдэлгүй","value":"2"}];	
	  	   				   	   					
	  	   					$scope.puserGrid = {
		 		                dataSource: {
		 		                    autoSync: true,
		 		                    transport: {
		 		                    	read:  {
		 		                            url: "/au/list/LutFactorCustom",
		 		                            contentType:"application/json; charset=UTF-8",    
		 		                            data:{"mid":$stateParams.issueId, "stepid":$stateParams.stepid,"sort":[{field: "groupid", dir: "asc"}]},
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
		 		                		  { title: "#",template: "<span class='row-number'></span>", width:"60px"},
		 		                          /*{ field:"fnumber", title: "№", width: 75,filterable:false},    */  
		 		                          { field:"factorname", title: "Эрсдэл бий болох хүчин зүйлс буюу аудитын цар хүрээ",width:400},
		 		                          { field:"dirid", title: "Аудитын чиглэл", values:$scope.dirs, width: 200 },
		 		                          { field:"critid", title: "Тухайн эрсдэл болон хүчин зүйлсэд хамаарах шалгуур үзүүлэлт", template: "#=crition#", width:200},
		 		                          { field:"groupid",title: "Хүчин зүйлс", values:$scope.gfactor,hidden:true },		
		 		                          { field:"treatment",width: 400, title: "Хэрэгжүүлэхээр төлөвлөсөн горим сорил"},
		 		                          { field:"riskid",width: 400, title: "Илрүүлсэн эрсдэл",values:rdata},
		 		                          /*{ field:"decid" ,values:decision, title: "Тийм/Үгүй"},*/
		 		                          { field:"description", title: "Тайлбар",width: 400},
		 		                         
		 		                        
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
			  	   			    if($scope.role=="ROLE_AUDIT" && $stateParams.wstep!=3){	   		   						
			  		            	$scope.puserGrid.editable=true;
			  		            	$scope.puserGrid.toolbar=kendo.template($("#export").html());
			  		            	$scope.puserGrid.columns.push({template: $("#update").html(), width:250,  locked: true});
		  	  					}
			  	   			    else if($scope.role=="INDP" && $stateParams.wstep!=3){	   		   						
			  		            	$scope.puserGrid.editable=true;
			  		            	alert();
			  		            	$scope.puserGrid.toolbar=kendo.template($("#export").html());
			  		            	$scope.puserGrid.columns.push({template: $("#update").html(), width:250,  locked: true});
		  	  					}
			  		            else{
			  		            	$scope.puserGrid.editable=false
			  		            	$scope.puserGrid.toolbar=kendo.template($("#back").html());
		  		            	}
		   				 
		   					}
		   				else{
			   				sweet.show('Анхаар', 'Error...', 'error');
		   				}
	   				});			   			
  			});
	        }
    ]);
