altairApp
    .config([
        '$stateProvider',
        '$urlRouterProvider',        
        '$httpProvider',
        function ($stateProvider, $urlRouterProvider,$httpProvider) {

           $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
        	
    	   $stateProvider   
        	  .state("restricted.pages.pmenu", {
                  url: "/menu",
                  templateUrl: 'app/wrapper/static/pPmenuView.html',
                  controller: 'menuCtrl',
                  resolve: {                    	
                      deps: ['$ocLazyLoad', function($ocLazyLoad) {
                          return $ocLazyLoad.load(['lazy_KendoUI',
                                                   'lazy_parsleyjs',
                              'app/wrapper/static/pPmenuController.js'
                          ]);
                      }],
                        p_menu: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutMenu' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                }
		         
                  },	                
                  data: {
                      pageTitle: 'Цэс'
                  }
              })
              
              .state("restricted.pages.helps", {
                    url: "/helps",
                    templateUrl: 'app/wrapper/static/helpView.html',
                    controller: 'helpsCtrl',
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/helpController.js'
                            ],{serie: true});
                        }],
                        help_data: function($http){
                            return $http({ method: 'GET', url: 'data/help_faq.json' })
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
                        }
                    },
                    data: {
                        pageTitle: 'Тусламж'
                    }
                })
              
             .state("restricted.pages.prole", {
                    url: "/role",
                    templateUrl: 'app/wrapper/static/pProleView.html',
                    controller: 'roleCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                 'lazy_parsleyjs',
                                'app/wrapper/static/pProleController.js'
                            ]);
                        }],
                        /*donelist: function($http){
                            return $http({ method: 'GET', url: '/core/parentmenus' })
                                .then(function (data) {
                                    return data.data;
                                });
                        },*/
                        p_menu: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutMenu' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Эрх'
                    }
                })
                
                .state("restricted.pages.organizations", {
                    url: "/organization",
                    templateUrl: 'app/wrapper/static/pOrganizationView.html',
                    controller: 'orgNewCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                                     'lazy_parsleyjs',
                                'app/wrapper/static/pOrganizationController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Байгууллага'
                    }
                })  
                 .state("restricted.pages.indorganizations", {
                    url: "/indorganization",
                    templateUrl: 'app/wrapper/static/pOrganizationIndView.html',
                    controller: 'indorgNewCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                                     'lazy_parsleyjs',
                                'app/wrapper/static/pOrganizationIndController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Хувийн аудитын байгууллага'
                    }
                })  
                  
                 .state("restricted.pages.orglist", {
                    url: "/orglist",
                    templateUrl: 'app/wrapper/static/pOrglistView.html',
                    controller: 'orglistCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/pOrglistController.js'
                            ]);
                        }],
                        p_dep: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutDepartment' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_fin: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutFincategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_cat: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_prog: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutExpProgcategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Үйлчлүүлэгч Байгууллага'
                    }
                })
                  .state("restricted.pages.orglistIndp", {
                    url: "/orglistIndp",
                    templateUrl: 'app/wrapper/static/pOrganizationView.html',
                    controller: 'orglistIndpCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([ 'lazy_KendoUI',
                                'app/wrapper/static/pOrglistIndpController.js'
                            ]);
                        }],
                        p_dep: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutDepartment' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_fin: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutFincategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_cat: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                    
		                },
		                p_prog: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutExpProgcategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Үйлчлүүлэгч Байгууллага'
                    }
                })
                
                .state("restricted.pages.orgform", {
                    url: "/orgaddform/:param",
                    templateUrl: 'app/wrapper/static/OrgFormView.html',
                    controller: 'orgformCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                            	'lazy_parsleyjs',
								'lazy_wizard', 
                            	'lazy_KendoUI',
								'lazy_ionRangeSlider',
                                'lazy_masked_inputs',
                                'lazy_character_counter',								
								 
                                'app/wrapper/static/OrgFormController.js'
                            ]);
                        }],
                        p_dep: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutDepartment' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_fin: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutFincategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_cat: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutCategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_prog: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutExpProgcategory' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_tez: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/tez' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_aures: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/aures' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_edit: function($http,$stateParams){
		                    return $http({ method: 'GET', url: '/core/sel/editorg/' +$stateParams.param})
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                }
                
                    },	                
                    data: {
                        pageTitle: 'Байгууллага нэмэх'
                    }
                })
                
                .state("restricted.pages.orgformIndp", {
                url: "/orgaddformIndp/:param",
                templateUrl: 'app/wrapper/static/OrgFormIndpView.html',
                controller: 'orgformIndpCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load(['lazy_KendoUI',
							'lazy_ionRangeSlider',
                            'lazy_masked_inputs',
                            'lazy_character_counter',
							'lazy_wizard',  
							'lazy_parsleyjs',
                            'app/wrapper/static/OrgFormIndpController.js'
                        ], {serie:true});
                    }],
                    p_dep: function($http){
	                    return $http({ method: 'GET', url: '/core/resource/LutDepartment' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
	                p_fin: function($http){
	                    return $http({ method: 'GET', url: '/core/resource/LutFincategory' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
	                p_cat: function($http){
	                    return $http({ method: 'GET', url: '/core/resource/LutCategory' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
	                p_prog: function($http){
	                    return $http({ method: 'GET', url: '/core/resource/LutExpProgcategory' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
	                p_tez: function($http){
	                    return $http({ method: 'GET', url: '/core/resource/tez' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
	                p_edit: function($http,$stateParams){
	                    return $http({ method: 'GET', url: '/core/sel/editorg/' +$stateParams.param})
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                }
            
                },	                
                data: {
                    pageTitle: 'Байгууллага нэмэх'
                }
            })
                .state("restricted.pages.puser", {
                    url: "/user",
                    templateUrl: 'app/wrapper/static/pPuserView.html',
                    controller: 'userCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([ 'lazy_KendoUI',                               
                                'app/wrapper/static/pPuserController.js'
                            ]);
                        }],
                        p_org: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutDepartment' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_role: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutRole' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                
                    },	                
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })
              
                .state("restricted.pages.puserIndp", {
                    url: "/userIndp",
                    templateUrl: 'app/wrapper/static/pPuserViewIndp.html',
                    controller: 'userCtrlIndp',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([ 'lazy_KendoUI',                               
                                'app/wrapper/static/pPuserControllerIndp.js'
                            ]);
                        }],
                        p_org: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutDepartment' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_role: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutRole' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/core/resource/LutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                
                    },	                
                    data: {
                        pageTitle: 'Хэрэглэгч'
                    }
                })
                
                .state("restricted.pages.plistConsult", {
                    url: "/plistConsult",
                    templateUrl: 'app/wrapper/legalperson/pPlistView.html',
                    controller: 'plistConsultCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/legalperson/plistConsultController.js'
                            ]);
                        }],
                        p_profflevel: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/LutProfflevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },		                
                    },	                
                    data: {
                        pageTitle: 'Зөвлөх'
                    }
                })
                .state("restricted.pages.plistlead", {
                    url: "/plistLead",
                    templateUrl: 'app/wrapper/legalperson/pPlistView.html',
                    controller: 'plistLeadCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/legalperson/plistLeadController.js'
                            ]);
                        }],
                        p_profflevel: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/LutProfflevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },		                
                    },	                
                    data: {
                        pageTitle: 'Тэргүүлэх'
                    }
                })
                .state("restricted.pages.plistteach", {
                    url: "/plistTeach",
                    templateUrl: 'app/wrapper/legalperson/pPlistView.html',
                    controller: 'plistTeachCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/legalperson/plistTeachController.js'
                            ]);
                        }],
                        p_profflevel: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/LutProfflevel' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },		                
                    },	                
                    data: {
                        pageTitle: 'Заах Аргач'
                    }
                })
                
             
                
                .state("restricted.pages.position", {
                    url: "/position",
                    templateUrl: 'app/wrapper/static/pOrganizationView.html',
                    controller: 'positionCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                                     'lazy_parsleyjs',
                                'app/wrapper/static/pPositionController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Албан тушаал'
                    }
                })
                .state("restricted.pages.tworkers", {
                    url: "/tworkers",
                    templateUrl: 'app/wrapper/legalperson/pworkerslistView.html',
                    controller: 'tworkersCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/legalperson/ptworkersController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Сургуулийн ажилчид'
                    }
                })

                 .state("restricted.pages.procedure", {
                    url: "/procedure",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'procedureCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/procedureController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Горим'
                    }
                })
                .state("restricted.pages.notice", {
                    url: "/notice",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'noticeCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/noticeController.js'
                            ]);
                        }]
    	                
                    },	                
                    data: {
                        pageTitle: 'Батламж мэдэгдэл'
                    }
                })              

                 .state("restricted.pages.kworkers", {
                    url: "/kworkers",
                    templateUrl: 'app/wrapper/legalperson/pworkerslistView.html',
                    controller: 'kworkersCtrl',

                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/legalperson/pkworkersController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Цэцэрлэгийн ажилчид'
                    }
                })
                .state("restricted.pages.law", {
                  url: "/law",
                  templateUrl: 'app/wrapper/static/lawsViewM.html',
                  controller: 'lawCtrl',
                  resolve: {                    	
                      deps: ['$ocLazyLoad', function($ocLazyLoad) {
                          return $ocLazyLoad.load(['lazy_KendoUI',
                              'app/wrapper/static/lawController.js'
                          ]);
                      }],                
                  },	                
                  data: {
                      pageTitle: 'Хууль'
                  }
              })
              
              .state("restricted.pages.stot", {
                  url: "/standatrs",
                  templateUrl: 'app/wrapper/static/standartViewM.html',
                  controller: 'standartCtrl',
                  resolve: {                    	
                      deps: ['$ocLazyLoad', function($ocLazyLoad) {
                          return $ocLazyLoad.load(['lazy_KendoUI',
                              'app/wrapper/static/standartController.js'
                          ]);
                      }],       
                  },	                
                  data: {
                      pageTitle: 'Стандарт'
                  }
              })

                .state("restricted.pages.risk", {
                    url: "/risk",
                    templateUrl: 'app/wrapper/static/riskView.html',
                    controller: 'riskCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/riskController.js'
                            ]);
                        }],
    	                p_dir: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/LutAuditDir' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },
    	                p_law: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/LutLaw' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },    	              
                    },	                
                    data: {
                        pageTitle: 'Эрсдэл'
                    }
                })
                .state("restricted.pages.riskedit", {
                    url: "/riskedit/:param",
                    templateUrl: 'app/wrapper/static/riskeditView.html',
                    controller: 'riskeditCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/riskeditController.js'
                            ]);
                        }],
    	                p_dir: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/LutAuditDir' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },
    	                p_law: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/LutLawRisk' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },
    	                p_edit: function($http,$stateParams){
		                    return $http({ method: 'GET', url: '/core/sel/editrisk/' +$stateParams.param})
		                        .then(function (data) {                                	
		                            return data.data;
		                        });
		                       
		                }
                    },	                
                    data: {
                        pageTitle: 'Эрсдэл'
                    }
                })
                   .state("restricted.pages.advice", {
                    url: "/advice",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'adviceCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/adviceController.js'
                            ]);
                        }]
                    },	                
                    data: {
                        pageTitle: 'Зөвлөмж'
                    }
                })
                .state("restricted.pages.conflict", {
                    url: "/conflict",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'conflictCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_idle_timeout',
                                'app/wrapper/static/conflictController.js'
                            ]);
                        }],
    	                p_orgs: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/SubAuditOrganization' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },
                    },	                
                    data: {
                        pageTitle: 'Зөрчил'
                    }
                })
                .state("restricted.pages.accplan", {
                    url: "/accplan",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'accplanCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/accplanController.js'
                            ]);
                        }]
                    },	
                    
                    data: {
                        pageTitle: 'Дансны төлөвлөгөө'
                    }
                })
                .state("restricted.pages.finblcpref", {
                    url: "/finblcpref",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'finblcprefCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/finblcprefController.js'
                            ]);
                        }]
                    },	                
                    data: {
                        pageTitle: 'Санхүүгийн тайлангийн үзүүлэлт'
                    }
                })
                .state("restricted.pages.finblcname", {
                    url: "/finblcname",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'finblcnameCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/finblcnameController.js'
                            ]);
                        }]
                    },	                
                    data: {
                        pageTitle: 'Санхүүгийн тайлангийн нэрс'
                    }
                })
                .state("restricted.pages.actlist", {
                    url: "/actlist",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'actlistCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/actlistController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Актын жагсаалт'
                    }
                })
                .state("restricted.pages.newstype", {
                    url: "/newstype",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'ntypeCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/ntypeController.js'
                            ]);
                        }],   	               
                    },	                
                    data: {
                        pageTitle: 'Мэдээний төрөл'
                    }
                })
                .state("restricted.pages.nedit", {
                    url: "/nedit",
                    templateUrl: 'app/wrapper/static/newseditView.html',
                    controller: 'neditCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_ckeditor',
                                'lazy_dropify',
                                'lazy_KendoUI', 
                                'app/wrapper/static/newseditController.js'
                            ]);
                        }],
    	                ntype: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/LutNewstype' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },
                    },	                
                    data: {
                        pageTitle: 'Мэдээ оруулах хэсэг'
                    }
                })
                .state("restricted.pages.newslist", {
                    url: "/newslist",
                    templateUrl: 'app/wrapper/static/newslistView.html',
                    controller: 'nlistCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                
                                'app/wrapper/static/newslistController.js'
                            ]);
                        }],  
                        nlist: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/LutNews' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },
    	                ntype: function($http){
    	                    return $http({ method: 'GET', url: '/info/resource/LutNewstype' })
    	                        .then(function (data) {                                	
    	                            return data.data;
    	                        })
    	                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
    	                },
                    },	                
                    data: {
                        pageTitle: 'Мэдээний жагсаалт'
                    }
                })
                
               .state("restricted.pages.newsmore", {
                url: "/newsmore/:param",
                templateUrl: 'app/wrapper/static/newsdetView.html',
                controller: 'ndetCtrl',
                resolve: {                    	
                    deps: ['$ocLazyLoad', function($ocLazyLoad) {
                        return $ocLazyLoad.load(['lazy_KendoUI',
                            
                            'app/wrapper/static/newsdetController.js'
                        ]);
                    }],  
                    nmore: function($http,$stateParams){
	                    return $http({ method: 'GET', url: '/info/sel/newsmore/' +$stateParams.param})
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
	                ntype: function($http){
	                    return $http({ method: 'GET', url: '/info/resource/LutNewstype' })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
	                rnlist: function($http,$stateParams){
	                    return $http({ method: 'GET', url: '/info/sel/recentnews/'+$stateParams.param })
	                        .then(function (data) {                                	
	                            return data.data;
	                        })
	                        .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
	                },
                },	                
                data: {
                    pageTitle: 'Мэдээний дэлгэрэнгүй'
                }
            })
                .state("restricted.pages.ecoclass", {
                    url: "/ecoclass",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'ecoclassCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/ecoclassController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Эдийн засгийн ангилал'
                    }
                })
                .state("restricted.pages.aorgssys", {
                    url: "/aorgssys",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'aorgssysCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load([
                                'app/wrapper/static/aorgssysController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Хараат бус аудитын компаниудын жагсаалт'
                    }
                })
                .state("restricted.pages.aorgs", {
                    url: "/aorgs",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'aorgsCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/aorgsController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Хараат бус аудитын компаниудын жагсаалт'
                    }
                })
                .state("restricted.pages.requirement", {
                    url: "/requirement",
                    templateUrl: 'app/wrapper/static/lawsView.html',
                    controller: 'requirementCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/requirementController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Албан шаардлагын жагсаалт'
                    }
                })	

                 .state("restricted.pages.lpworkers", {
                    url: "/lpworkers",
                    templateUrl: 'app/wrapper/legalperson/pworkerslistView.html',
                    controller: 'lpworkersCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/legalperson/lpworkersController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Байгуулгын ажилчид'
                    }
                })
                
                .state("restricted.pages.error404", {
                    url: "/error404",
                    templateUrl: 'app/wrapper/static/p404View.html',
                    controller: '404Ctrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/404.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Алдаа'
                    }
                }) 

                .state("restricted.pages.job", {
                    url: "/job",
                    templateUrl: 'app/wrapper/static/pOrganizationView.html',
                    controller: 'jobCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/pJobController.js'
                            ]);
                        }],
                       
                    },	                
                    data: {
                        pageTitle: 'Мэргэжил'
                    }
                }) 
                
                .state("restricted.pages.form", {
                    url: "/form",
                    templateUrl: 'app/wrapper/static/pOrganizationView.html',
                    controller: 'formCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'app/wrapper/static/pFormController.js'
                            ]);
                        }],
                    },	                
                    data: {
                        pageTitle: 'Маягт'
                    }
                }) 
                
                .state("restricted.pages.formTab", {
                    url: "/main/form/:param",
                    templateUrl: 'app/wrapper/static/validationView.html',
                    controller: 'validationCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/static/validationController.js'
                            ]);
                        }],
                        p_form: function($http,$stateParams){
    	                    return $http({ method: 'get', url: '/user/application/form/'+$stateParams.param})
    	                    .then(function (data) {                                	
    	                        return data.data;
    	                    })
    	                    .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
    	                },
                    },	                
                    data: {
                        pageTitle: 'Маягт'
                    }
                })
                
                
                .state("restricted.pages.claim", {
                    url: "/client",
                    templateUrl: 'app/wrapper/static/applicationView.html',
                    controller: 'applicationCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/static/applicationController.js'
                            ]);
                        }],
                        p_apps: function($http){
		                    return $http({ method: 'GET', url: '/user/entity/CEduLutForm' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_forms: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/forms' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_users: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/userlist' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                
		                p_specialist: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/CLutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Миний өргөдөл'
                    }
                })
                    .state("restricted.pages.incomeApps", {
                    url: "/incomeApps",
                    templateUrl: 'app/wrapper/legalperson/inapplicationView.html',
                    controller: 'inapplicationCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/legalperson/inapplicationController.js'
                            ]);
                        }],
                        p_apps: function($http){
		                    return $http({ method: 'GET', url: '/core/entity/CEduLutForm' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },		               
		                p_specialist: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/CLutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Ирсэн өргөдөл'
                    }
                })
                  .state("restricted.pages.incomeAppsSp", {
                    url: "/incomeAppsSp",
                    templateUrl: 'app/wrapper/nbg/inapplicationView.html',
                    controller: 'inapplicationCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/nbg/inapplicationController.js'
                            ]);
                        }],
                        p_apps: function($http){
		                    return $http({ method: 'GET', url: '/core/entity/CEduLutForm' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_specialist: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/CLutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Ирсэн өргөдөл мэргэжилтэн'
                    }
                })
               
                   .state("restricted.pages.decidedAppsSp", {
                    url: "/decidedAppsSp",
                    templateUrl: 'app/wrapper/nbg/inapplicationView.html',
                    controller: 'decidedAppsSpCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/nbg/decidedAppsSpController.js'
                            ]);
                        }],
                        p_apps: function($http){
		                    return $http({ method: 'GET', url: '/core/entity/CEduLutForm' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_specialist: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/CLutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Шийдвэрлэсэн өргөдөл мэргэжилтэн'
                    }
                })
                 .state("restricted.pages.decidedAppsMb", {
                    url: "/decidedAppsMb",
                    templateUrl: 'app/wrapper/nbg/inapplicationView.html',
                    controller: 'decidedAppsMbCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/nbg/decidedAppsMbController.js'
                            ]);
                        }],
                        p_apps: function($http){
		                    return $http({ method: 'GET', url: '/core/entity/CEduLutForm' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_specialist: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/CLutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	
                    data: {
                        pageTitle: 'Шийдвэрлэсэн өргөдөл мэргэжилтэн'
                    }
                })
                    .state("restricted.pages.decidedAppsBo", {
                        url: "/decidedAppsBo",
                        templateUrl: 'app/wrapper/nbg/inapplicationView.html',
                        controller: 'decidedAppsBossCtrl',
                        resolve: {                    	
                            deps: ['$ocLazyLoad', function($ocLazyLoad) {
                                return $ocLazyLoad.load(['lazy_KendoUI',
                                    'lazy_parsleyjs',
                                    'app/wrapper/nbg/approvedAppsBossController.js'
                                ]);
                            }],
                            p_apps: function($http){
    		                    return $http({ method: 'GET', url: '/core/entity/CEduLutForm' })
    		                        .then(function (data) {                                	
    		                            return data.data;
    		                        })
    		                        .catch(function(response) {
    								    $state.go("login");
    								    $state.reload();
    								});
    		                },
    		                p_specialist: function($http){
    		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
    		                        .then(function (data) {                                	
    		                            return data.data;
    		                        })
    		                        .catch(function(response) {
    								    $state.go("login");
    								    $state.reload();
    								});
    		                },
    		                p_pos: function($http){
    		                    return $http({ method: 'GET', url: '/user/service/resourse/CLutPosition' })
    		                        .then(function (data) {                                	
    		                            return data.data;
    		                        })
    		                        .catch(function(response) {
    								    $state.go("login");
    								    $state.reload();
    								});
    		                },
                        },
                    data: {
                        pageTitle: 'Шийдвэрлэсэн өргөдөл Хэлтсийн дарга'
                    }
                })
                    .state("restricted.pages.incomeAppsDir", {
                    url: "/incomeAppsdir",
                    templateUrl: 'app/wrapper/legalperson/inapplicationView.html',
                    controller: 'inapplicationDirCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/legalperson/inapplicationDirController.js'
                            ]);
                        }],
                        p_specialist: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_pos: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/CLutPosition' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },	                
                    data: {
                        pageTitle: 'Ирсэн өргөдөл'
                    }
                })
                    .state("restricted.pages.StatsSchool", {
                    url: "/incomeAppsdir",
                    templateUrl: 'app/wrapper/legalperson/inapplicationView.html',
                    controller: 'inapplicationDirCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/legalperson/inapplicationDirController.js'
                            ])
                            .catch(function(response) {
							    $state.go("login");
							    $state.reload();
							});
                        }]
                    },	                
                    data: {
                        pageTitle: 'Статистик сургууль'
                    }
                })
                    .state("restricted.pages.StatsCap", {
                    url: "/StatsCap",
                    templateUrl: 'app/wrapper/nbg/statCapView.html',
                    controller: 'StatsCapCtrl',
                    resolve: {                    	
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'lazy_charts_c3',
                                'app/wrapper/nbg/statCapController.js'
                            ]);
                        }],
                        p_cap: function($http){
		                    return $http({ method: 'GET', url: '/user/service/statCap'})
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		               /* p_dist1: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/managerjson'})
		                        .then(function (data) {                                	
		                            return data.data;
		                        });
		                },
		                p_dist2: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/managerjson'})
		                        .then(function (data) {                                	
		                            return data.data;
		                        });
		                },
		                p_dist3: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/managerjson'})
		                        .then(function (data) {                                	
		                            return data.data;
		                        });
		                },
		                p_dist4: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/managerjson'})
		                        .then(function (data) {                                	
		                            return data.data;
		                        });
		                },*/
                    },	                
                    data: {
                        pageTitle: 'Статистик Нийслэл'
                    }
                })
                
                 .state("restricted.pages.applicationForm", {
                    url: "/application/:param/:id/:userid",
                    templateUrl: 'app/wrapper/dynamic/formView.html',
                    controller: 'appFormCtrl',                    
                    resolve: {
                        deps: ['$ocLazyLoad', function($ocLazyLoad) {
                            return $ocLazyLoad.load(['lazy_KendoUI',
                                'lazy_parsleyjs',
                                'app/wrapper/dynamic/formController.js'
                            ]);
                        }],
                        form_data: function($http,$stateParams){
                            return $http({ method: 'GET', url: '/user/application/read/CEduLutForm/'+$stateParams.param+'/'+$stateParams.id})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
                        }, 
                        user_data: function($http,$stateParams){
                            return $http({ method: 'GET', url: '/system/service/resourse/user/'+$stateParams.userid})
                                .then(function (data) {
                                    return data.data;
                                })
                                .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
                        },
                        app_data: function($http,$stateParams){
		                    return $http({ method: 'GET', url: '/system/service/resourse/app/'+$stateParams.id})
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_manager: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/managerjson'})
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
		                p_specialist: function($http){
		                    return $http({ method: 'GET', url: '/user/service/resourse/specialistjson' })
		                        .then(function (data) {                                	
		                            return data.data;
		                        })
		                        .catch(function(response) {
								    $state.go("login");
								    $state.reload();
								});
		                },
                    },
                    data: {
                        pageTitle: 'Annual plan'
                    }
              })          

        }
    ]);
