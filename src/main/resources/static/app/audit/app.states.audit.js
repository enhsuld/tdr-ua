altairApp
    .config([
        '$stateProvider',
        '$urlRouterProvider',        
        '$httpProvider',
        function ($stateProvider, $urlRouterProvider,$httpProvider) {

        	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
        	
    	    $stateProvider  
    	    
    	    .state("restricted.pages.excel", {
                url: "/ex",
                templateUrl: 'app/audit/excel/pValidationForm.html',
                controller: 'excelCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'app/audit/excel/pValidationListCtrl.js'
                        ], {serie:true});
                    }],
    	    		LutAuditDir: function($http,$state,$stateParams){
		                return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir'  })
		                    .then(function (data) {                                	
		                        return data.data;
		                    })
		            },
	            
                },	                
                data: {
                    pageTitle: 'Цалин'
                }
            })
            
            .state("restricted.pages.generalisation", {
                url: "/generalisation",
                templateUrl: 'app/audit/stat/pGeneralisation.html',
                controller: 'dt_table_tools',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_datatables',
                            'app/audit/stat/pGeneralisationCtrl.js'
                        ]);
                    }]     
                },	                
                data: {
                    pageTitle: 'Нэгтгэл'
                }
            })
    	    
            .state("restricted.news", {
                url: "/news",
                templateUrl: 'app/audit/news/newsList.html',
                controller: 'newsCtrl',
                resolve: {
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'app/audit/news/newsController.js'
                        ]);
                    }],		              
                   
                },
                data: {
                    pageTitle: 'Мэдээлэл'
                }
           })
           .state("restricted.feedbacks", {
                url: "/feedbacks",
                templateUrl: 'app/audit/work/feedbackView.html',
                controller: 'feedbackController',
                resolve: {
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'app/audit/work/feedbackController.js'
                        ]);
                    }],
                    messages: function($http,$state){
                        return $http({ method: 'POST', url: '/au/list/LutFeedback', data: {take: 8, skip: 0, page: 1, pageSize: 1, custom: 'where parentid is null'} })
                        .then(function (data) {          
                            return data.data;
                        })
                    },
                    feedback_categories: function($http,$state){
                        return $http({ method: 'GET', url: '/au/resource/LnkFeedbackCategoryChilds' })
                            .then(function (data) {          
                                return data.data;
                            })
                    },
                    optgroups: function($http,$state){
                        return $http({ method: 'GET', url: '/au/resource/LnkFeedbackCategoryGroups' })
                            .then(function (data) {          
                                return data.data;
                            })
                    }
                    
                },
                data: {
                    pageTitle: 'Санал хүсэлт'
                }
           })
           .state("restricted.feedbackcategories", {
                url: "/feedbackcategories",
                templateUrl: 'app/audit/work/feedbackCategoryView.html',
                controller: 'feedbackCategoryController',
                resolve: {
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'app/audit/work/feedbackCategoryController.js'
                        ]);
                    }],
                    feedback_categories: function($http,$state){
                        return $http({ method: 'GET', url: '/au/resource/LnkFeedbackCategory' })
                            .then(function (data) {          
                                return data.data;
                            })
                    }
                },
                data: {
                    pageTitle: 'Санал хүсэлт'
                }
           })
    	    .state("restricted.pages.validate", {
                url: "/validate",
                templateUrl: 'app/audit/work/pValidationForm.html',
                controller: 'validationListCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_KendoUI',
                            'lazy_parsleyjs',
                            'app/audit/work/pValidationListCtrl.js'
                        ], {serie:true});
                    }],
    	    		LutAuditDir: function($http,$state,$stateParams){
		                return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir'  })
		                    .then(function (data) {                                	
		                        return data.data;
		                    })
		            },
	            
                },	                
                data: {
                    pageTitle: 'Тулгалтууд'
                }
            })
    	    .state("restricted.pages.confsource", {
                url: "/confsource",
                templateUrl: 'app/audit/work/pConfirmationView.html',
                controller: 'confSourceCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_KendoUI',
                            'lazy_parsleyjs',
                            'app/audit/work/pConfirmationSourceCtrl.js'
                        ], {serie:true});
                    }],
	            
                },	                
                data: {
                    pageTitle: 'Нотлох зүйлийн эх сурвалж'
                }
            })
    	    .state("restricted.pages.conftype", {
                url: "/conftype",
                templateUrl: 'app/audit/work/pConfirmationView.html',
                controller: 'confTypeCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_KendoUI',
                            'lazy_parsleyjs',
                            'app/audit/work/pConfirmationTypeCtrl.js'
                        ], {serie:true});
                    }],
	            
                },	                
                data: {
                    pageTitle: 'Нотлох зүйлийн төрөл'
                }
            })
    	    
    	    .state("restricted.pages.confmethod", {
                url: "/confmethod",
                templateUrl: 'app/audit/work/pConfirmationView.html',
                controller: 'confMethodCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_KendoUI',
                            'lazy_parsleyjs',
                            'app/audit/work/pConfirmationMethodCtrl.js'
                        ], {serie:true});
                    }],
	            
                },	                
                data: {
                    pageTitle: 'Нотлох зүйлийг олж авах арга зам'
                }
            })
    	    
    	    .state("restricted.pages.critoffactor", {
                url: "/critlist/:param",
                templateUrl: 'app/audit/work/pCritOfFactorListView.html',
                controller: 'critListCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_KendoUI',
                            'lazy_parsleyjs',
                            'app/audit/work/pCritOfFactorListController.js'
                        ], {serie:true});
                    }],
	                crit: function($http,$state,$stateParams){
	                    return $http({ method: 'GET', url: '/my/resource/LutCriterionOfFactor/'+$stateParams.param  })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                },
	                audit_dir: function($http,$state){
 	                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
 	                        .then(function (data) {                                	
 	                            return data.data;
 	                        })
 	                },
 	                groupOfFact: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutGroupOfFactor' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                },
	                work_type: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutCategory' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                },
                },	                
                data: {
                    pageTitle: 'Хүчин зүйлс'
                }
            })
    	    .state("restricted.pages.groupoffactor", {
                url: "/grouplist",
                templateUrl: 'app/audit/work/pGroupOfFactorListView.html',
                controller: 'groupListCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_KendoUI',
                            'lazy_parsleyjs',
                            'app/audit/work/pGroupOfFactorListController.js'
                        ], {serie:true});
                    }],
                },	                
                data: {
                    pageTitle: 'Бүлгийн жагсаалт'
                }
            })
    	    
    	    .state("restricted.pages.factor", {
                url: "/factorlist",
                templateUrl: 'app/audit/work/pFactorListView.html',
                controller: 'factorListCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'lazy_KendoUI',
                            'lazy_parsleyjs',
                            'app/audit/work/pFactorListController.js'
                        ], {serie:true});
                    }],
                    audit_dir: function($http,$state){
 	                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
 	                        .then(function (data) {                                	
 	                            return data.data;
 	                        })
 	                       .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
 	                },
 	                groupOfFact: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutGroupOfFactor' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                }
                },	                
                data: {
                    pageTitle: 'Хүчин зүйлсийн жагсаалт'
                }
            })
    	    
    	    .state("restricted.pages.formType", {
	               url: "/formTypes",
	               templateUrl: 'app/audit/fromExcel/pAformTypeListView.html',
	               controller: 'aFtypelistCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                           'lazy_KendoUI',
	                           'lazy_parsleyjs',
	                           'app/audit/fromExcel/pAformTypeListController.js'
	                       ]);
	                   }],
	               },	                
	               data: {
	                   pageTitle: 'Маягтын төрөл'
	               }
	           })
    	    
    	    .state("restricted.pages.fromexcel", {
	               url: "/formExcel/:param",
	               templateUrl: 'app/audit/fromExcel/formFromExcel.html',
	               controller: 'excelformCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                           'lazy_dropify',
	                           'lazy_ionRangeSlider',
	                           'lazy_masked_inputs',
	                           'lazy_character_counter',
	                           'lazy_wizard',  
	                           'lazy_parsleyjs',
	                           'app/audit/fromExcel/formFromExcelCtrl.js'
	                       ], {serie:true});
	                   }],
	                  fType: function($http,$state, $stateParams){
	                        return $http({ method: 'GET', url: '/my/resource/FsLutFormType/'+$stateParams.param  })
	                            .then(function (data) {
	                                return data.data[0];
	                            })
	                            .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
	                    },
	               },	                
	               data: {
	                   pageTitle: 'Маягт'
	               }
	           })
    	    
    	    .state("restricted.pages.myComment", {
                url: "/mCom",
                templateUrl: 'app/components/pages/mailboxView.html',
                controller: 'myComCtrl',
                resolve: {
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    	return $ocLazyLoad.load([
                    	                         'lazy_KendoUI',
                                                 'lazy_tablesorter',
                                                 'app/audit/work/myCommentController.js'
                                             ],{serie:true});
                    }],
                    messages: function($http,$state){
                        return $http({ method: 'GET', url: 'data/mailbox_data.json' })
                            .then(function (data) {
                                return data.data;
                            });
                    },
                    comments: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutMainComment' })
	                        .then(function (data) {  
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                }
                   
                },
                data: {
                    pageTitle: 'Mailbox'
                }
            })
    	    
    	    .state("restricted.pages.mailbox", {
                url: "/mailbox",
                templateUrl: 'app/components/pages/mailboxView.html',
                controller: 'mailboxCtrl',
                resolve: {
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                    	return $ocLazyLoad.load([
                                                 'lazy_tablesorter',
                                                 'app/components/pages/mailboxController.js'
                                             ],{serie:true});
                    }],
                    messages: function($http,$state){
                        return $http({ method: 'GET', url: 'data/mailbox_data.json' })
                            .then(function (data) {
                                return data.data;
                            })
                            .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
                    },
                    comments: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutMainComment' })
	                        .then(function (data) {  
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                }
                   
                },
                data: {
                    pageTitle: 'Mailbox'
                }
            })
    	    
    	    .state("restricted.pages.help", {
                url: "/help",
                templateUrl: 'app/audit/dashboard/helpView.html',
                controller: 'helpCtrl',
                resolve: {
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'app/audit/dashboard/helpController.js'
                        ],{serie: true});
                    }],
                    help_data: function($http,$state){
                        return $http({ method: 'GET', url: 'data/help_faq.json' })
                            .then(function (data) {
                                return data.data;
                            })
                            .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
                    }
                },
                data: {
                    pageTitle: 'Тусламж'
                }
            })
            
    	    	.state("restricted.pages.mainwork", {
	               url: "/details/:typeid/:stepid/:issueId",
	               templateUrl: 'app/audit/dashboard/dash_detailsView.html',
	               controller: 'issuesCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                                                'lazy_countUp',
	                                                'lazy_charts_peity',
	                                                'lazy_charts_easypiechart',
	                                                'lazy_charts_metricsgraphics',
	                                                'lazy_charts_chartist',
	                                                'lazy_weathericons',
	                                                'lazy_parsleyjs',
	                                                'lazy_dropify',
	                                                'lazy_ckeditor',
	                            'app/audit/dashboard/dashController.js'
	                       ]);
	                   }],
	                   au_levels: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutAuditLevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								   
								});
		                },
	                   mainobj: function($http,$state,$stateParams,$state){
		                    return $http({ method: 'GET', url: '/au/withid/MainAuditRegistration/'+$stateParams.issueId })
		                        .then(function (data) {  
		                        	if(data.data==false){
		                        		$state.go("error.Auth");
		                        	}else{
		                        		 return data.data[0];
		                        	}		                           
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								});
		                },
		               worklist: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/work/'+$stateParams.typeid+'/'+$stateParams.issueId })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								});
		               },
		               step_data: function($http,$state,$stateParams){
		            	   return $http({ method: 'GET', url: '/au/withid/workdata/'+$stateParams.issueId })
	                        .then(function (data) {                                	
	                            return data.data;
	                        });
		               }
	               },	                
	               data: {
	                   pageTitle: 'Үндсэн самбар'
	               },
	               ncyBreadcrumb: {
	                   label: 'Үндсэн самбар',
	                   parent:'restricted.pages.waudit'
	               }
	           })
               .state("restricted.pages.checking", {
                    url: "/app/monitoring",
                    templateUrl: 'app/audit/quata/pmonitoringView.html',
                    controller: 'monitoringCtrl',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/audit/quata/pmonitoringController.js'
                            ]);
                        }],
                        p_cat: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                decision: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutQuataDecision' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                au_levels: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditLevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                reason: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutReason' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
                    },
                    data: {
                        pageTitle: 'Аудит'
                    },
                    ncyBreadcrumb: {
 	                   label: 'Хуваарьт ажил',
 	                   parent:'restricted.pages'
 	               }
              })
	       //     url: "/details/:typeid/:stepid/:issueId",
        	   .state("restricted.pages.accSurvey", {
	               url: "/survey/:typeid/:stepid/:planid/:formid",
	               templateUrl: 'app/audit/survey/surveyView.html',
	               controller: 'surveyAccCtrl',
	           
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                           'lazy_KendoUI',
	                           'lazy_ionRangeSlider',
	                            'app/audit/survey/surveyAccController.js'
	                       ], {serie:true});
	                   }],
	               /*    survey_dir: function($http,$state){
		                    return $http({ method: 'GET', url: '/fin/resource/FinSurveyDirection' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								});
		               },*/
		               app_data: function($http,$state,$stateParams,$state){
		                    return $http({ method: 'GET', url: '/au/withid/MainAuditRegistration/'+$stateParams.planid })
		                        .then(function (data) {                                	
		                            return data.data[0];
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								});
		                },
		                
	               },	                
	               data: {
	                   pageTitle: 'Түүврийн жагсаалт'
	               },
                   ncyBreadcrumb: {
                       label: 'Түүврийн жагсаалт',
                       parent: function ($scope,$stateParams) {
  	                     return 'restricted.pages.mainwork({typeid:'+$scope.typeid+', stepid:'+$scope.stepid+', issueId:'+$scope.issueId+'})';
  	                   }
                   }  
	           })
	           
               .state("restricted.pages.mainworkriskG", {
	               url: "/custom/treatment/:typeid/:stepid/:issueId/:wstep",
	               templateUrl: 'app/audit/treatment/customTreatmentView.html',
	               controller: 'customTreatmentCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                    	   'lazy_parsleyjs',
	                    	   'lazy_masked_inputs',
	                    	   'lazy_KendoUI', 
	                           'app/audit/treatment/customTreatmentController.js'
	                       ]);
	                   }],
	                   mainobj: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/MainAuditRegistration/'+$stateParams.issueId })
		                        .then(function (data) {                                	
		                            return data.data[0];
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                dirs: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                conftype: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationType' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                confMethod: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationMethod' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                confSourceI: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationSourceI' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                confSourceO: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationSourceO' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                }, 
		                LutGroupOfFactor: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutGroupOfFactorOther' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                    .catch(function(response ,$state) {
						    $state.go("login");
						   
						});
	                }		               
	               },	                
	               data: {
	                   pageTitle: 'Төлөвлөсөн горим, сорилуудыг хэрэгжүүлэх'
	               }
	           })
	           .state("restricted.pages.mainworkrisk", {
	               url: "/custom/risk/:typeid/:stepid/:issueId/:wstep",
	               templateUrl: 'app/audit/customRisk/customRiskView.html',
	               controller: 'customRiskCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                    	   'lazy_parsleyjs',
	                            'app/audit/customRisk/customRiskController.js'
	                       ]);
	                   }],
	                   mainobj: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/MainAuditRegistration/'+$stateParams.issueId })
		                        .then(function (data) {                                	
		                            return data.data[0];
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                dirs: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                conftype: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationType' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                confMethod: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationMethod' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                confSourceI: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationSourceI' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                confSourceO: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationSourceO' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                }, 
		                LutGroupOfFactor: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutGroupOfFactorOther' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                    .catch(function(response ,$state) {
						    $state.go("login");
						   
						});
	                }		               
	               },	                
	               data: {
	                   pageTitle: 'Үндсэн самбар'
	               }
	           })

    	   .state("restricted.pages.tryoutlist", {
               url: "/tryOutlist",
               templateUrl: 'app/audit/work/pTryOutListView.html',
               controller: 'tryOutlistCtrl',
               resolve: {                    	
                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
                       return $ocLazyLoad.load([
                           'lazy_KendoUI',
                           'app/audit/work/pTryOutListController.js'
                       ], {serie:true});
                   }],
                   work_type: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutCategory' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                },
                  audit_dir: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                },
               },	                
               data: {
                   pageTitle: 'Горим сорил жагсаалт'
               }
           })
    	   
    	   .state("restricted.pages.tryout", {
               url: "/tryOutform/:param",
               templateUrl: 'app/audit/work/pTryOutFormView.html',
               controller: 'tryOutformCtrl',
               resolve: {                    	
                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
                       return $ocLazyLoad.load([
                           'lazy_dropify',
                           'lazy_ionRangeSlider',
                           'lazy_masked_inputs',
                           'lazy_character_counter',
                           'lazy_wizard',  
                           'lazy_KendoUI',
                           'lazy_parsleyjs',
                           'app/audit/work/pTryOutFormController.js'
                       ], {serie:true});
                   }],
                   audit_dir: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                },
	                notices: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutNotice' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                },
	                org_type: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutCategory' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                },
	                risks: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutRisk' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
	                },
	                procedures: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutProcedure' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                    .catch(function(response ,$state) {
						    $state.go("login");
						   
						});
	                },
	                conftype: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationType' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                    .catch(function(response ,$state) {
						    $state.go("login");
						   
						});
	                },
	                confMethod: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationMethod' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                    .catch(function(response ,$state) {
						    $state.go("login");
						   
						});
	                },
	                confSourceI: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationSourceI' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                    .catch(function(response ,$state) {
						    $state.go("login");
						   
						});
	                },
	                confSourceO: function($http,$state){
	                    return $http({ method: 'GET', url: '/my/core/resource/LutConfirmationSourceO' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                    .catch(function(response ,$state) {
						    $state.go("login");
						   
						});
	                }
	                
               },	                
               data: {
                   pageTitle: 'Сорил нэмэх'
               }
           })
    	   	
	    	   .state("restricted.pages.workform", {
	               url: "/workaddform/:param",
	               templateUrl: 'app/audit/work/WorkAddFormView.html',
	               controller: 'workAddformCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                           'lazy_dropify',
	                           'lazy_ionRangeSlider',
	                           'lazy_masked_inputs',
	                           'lazy_character_counter',
	                           'lazy_wizard',  
	                           'lazy_parsleyjs',
	                           'app/audit/work/WorkAddFormController.js'
	                       ], {serie:true});
	                   }],
	                   audit_dir: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                au_work: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditWork' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                work_type: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                au_level: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditLevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                    .catch(function(response ,$state) {
							    $state.go("login");
							   
							});
		                },
		                au_type: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutReason' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                }
	               },	                
	               data: {
	                   pageTitle: 'Ажил нэмэх'
	               }
	           })

	    	   .state("restricted.pages.worklist", {
	               url: "/worklist",
	               templateUrl: 'app/audit/work/pWorkListView.html',
	               controller: 'worklistCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                           'lazy_KendoUI',
	                           'app/audit/work/pWorkListController.js'
	                       ]);
	                   }],
	                   au_work: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditWork' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
	                   audit_dir: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditDir' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                au_levels: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditLevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
	               },	                
	               data: {
	                   pageTitle: 'Ажлын жагсаалт'
	               }
	           })
	           
	    	   .state("restricted.pages.auditstep", {
	               url: "/astep",
	               templateUrl: 'app/audit/work/pAuditView.html',
	               controller: 'auditStepCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
	                                                'lazy_KendoUI',
	                           'app/audit/work/pAuditStepController.js'
	                       ]);
	                   }],
	               },	                
	               data: {
	                   pageTitle: 'Аудитын Үе шат'
	               }
	           })  
	           
	           .state("restricted.pages.auditcat", {
	               url: "/acat",
	               templateUrl: 'app/audit/work/pAuditView.html',
	               controller: 'acatCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
                               'lazy_KendoUI',
                               'lazy_parsleyjs',
	                           'app/audit/work/pAcatController.js'
	                       ]);
	                   }],
	               },	                
	               data: {
	                   pageTitle: 'Аудитын Ангилал'
	               }
	           })
    	   		
	    	   .state("restricted.pages.direction", {
	               url: "/direction",
	               templateUrl: 'app/audit/work/pWorkDirectionView.html',
	               controller: 'auditDirectionCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
                               'lazy_KendoUI',
	                           'lazy_parsleyjs',
	                           'app/audit/work/pWorkDirectionController.js'
	                       ]);
	                   }],	
		                notices: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutNotice' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                risks: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutRisk' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                procedures: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutProcedure' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                }
	               },	                
	               data: {
	                   pageTitle: 'Байгууллагын жагсаалт үүсгэх'
	               }
	          })
    	   
	    	  .state("restricted.pages.audityear", {
	               url: "/au",
	               templateUrl: 'app/audit/quata/pYearView.html',
	               controller: 'auditYearCtrl',
	               resolve: {                    	
	                   deps: ['$ocLazyLoad', function($ocLazyLoad) {
	                       return $ocLazyLoad.load([
                               'lazy_KendoUI',
                               'lazy_parsleyjs',
	                           'app/audit/quata/pYearController.js'
	                       ]);
	                   }]
	               },	                
	               data: {
	                   pageTitle: ''
	               }
	          })
        	  .state("restricted.pages.quata", {
                  url: "/quata",
                  templateUrl: 'app/audit/quata/pQuataView.html',
                  controller: 'quataCtrl',
                  resolve: {                    	
                      deps: ['$ocLazyLoad', function($ocLazyLoad) {
                          return $ocLazyLoad.load([
                              'lazy_parsleyjs',
                              'lazy_KendoUI',
                              'app/audit/quata/pQuataController.js'
                          ]);
                      }],
                       p_year: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutAuditYear' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                p_cat: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                decision: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutQuataDecision' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                }
                  },	                
                  data: {
                      pageTitle: 'Байгууллагын жагсаалт үүсгэх'
                  }
              })
              
               .state("restricted.pages.swap", {
                    url: "/app/schedule/:id",
                    templateUrl: 'app/audit/quata/pScheduleView.html',
                    controller: 'swapCtrl',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/audit/quata/pScheduleController.js'
                            ]);
                        }],
                        user_data: function($http,$state){
		                    return $http({ method: 'GET', url: '/user' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                reason: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutReason' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                quata: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/PreAuditRegistration/'+$stateParams.id })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                breason: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/LnkCommentPre/'+$stateParams.id })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                }
                    },
                    data: {
                        pageTitle: 'Annual plan'
                    }
              })
              
               .state("restricted.pages.quataperson", {
                    url: "/app/list",
                    templateUrl: 'app/audit/quata/pQuataPersonView.html',
                    controller: 'quatapersonCtrl',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/audit/quata/pQuataPersonController.js'
                            ]);
                        }],
                        p_cat: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                p_year: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutAuditYear' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                decision: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutQuataDecision' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                reason: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutReason' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
                    },
                    data: {
                        pageTitle: 'Annual plan'
                    }
              })
              
                 .state("restricted.pages.configusernongov", {
                    url: "/app/config/com/:stepid/:id",
                    templateUrl: 'app/audit/quata/pConfigComView.html',
                    controller: 'configcom',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/audit/quata/pConfigComController.js'
                            ]);
                        }],
                        p_position: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                org_data: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/orgdata/'+$stateParams.id  })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                p_cat: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                breason: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/LnkCommentMain/'+$stateParams.id })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                }
                    },
                    data: {
                        pageTitle: 'Annual plan'
                    }
              })
              
              .state("restricted.pages.waudit", {
                    url: "/app/waudit",
                    templateUrl: 'app/audit/quata/pwauditView.html',
                    controller: 'wauditCtrl',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/audit/quata/pwauditController.js'
                            ]);
                        }],
                        p_cat: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                decision: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutQuataDecision' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                au_levels: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditLevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                reason: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutReason' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
                    },
                    data: {
                        pageTitle: 'Аудит'
                    },
                    ncyBreadcrumb: {
 	                   label: 'Хуваарьт ажил',
 	                   parent:'restricted.pages'
 	               }
              })
              
              .state("restricted.pages.archive", {
                    url: "/app/archive",
                    templateUrl: 'app/audit/quata/pwauditView.html',
                    controller: 'waauditCtrl',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/audit/quata/pwaauditController.js'
                            ]);
                        }],
                        p_cat: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                decision: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutQuataDecision' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                au_levels: function($http,$state){
		                    return $http({ method: 'GET', url: '/my/core/resource/LutAuditLevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                reason: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutReason' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
                    },
                    data: {
                        pageTitle: 'Аудит'
                    },
                    ncyBreadcrumb: {
 	                   label: 'Хуваарьт ажил',
 	                   parent:'restricted.pages'
 	               }
              })
              
                .state("restricted.pages.configuser", {
                    url: "/app/config/user/:stepid/:id",
                    templateUrl: 'app/audit/quata/pConfigUserView.html',
                    controller: 'configuser',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'lazy_parsleyjs',
                                'lazy_KendoUI',
                                'app/audit/quata/pConfigUserController.js'
                            ]);
                        }],
                        p_position: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                org_data: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/orgdata/'+$stateParams.id  })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                p_cat: function($http,$state){
		                    return $http({ method: 'GET', url: '/au/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                },
		                breason: function($http,$state,$stateParams){
		                    return $http({ method: 'GET', url: '/au/withid/LnkCommentMain/'+$stateParams.id })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response ,$state) {
								    $state.go("login");
								   
								});
		                }
                    },
                    data: {
                        pageTitle: 'Annual plan'
                    }
              })
        }
    ]);
