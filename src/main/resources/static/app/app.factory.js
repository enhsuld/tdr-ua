altairApp
    .factory('windowDimensions', [
        '$window',
        function($window) {
            return {
                height: function() {
                    return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
                },
                width: function() {
                    return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
                }
            }
        }
    ])
    .factory('PagerService', [
	    function PagerService() {
	        // service definition
	        var service = {};
	
	        service.GetPager = GetPager;
	
	        return service;
	
	        // service implementation
	        function GetPager(totalItems, currentPage, pageSize) {
	            // default to first page
	            currentPage = currentPage || 1;
	
	            // default page size is 10
	            pageSize = pageSize || 10;
	
	            // calculate total pages
	            var totalPages = Math.ceil(totalItems / pageSize);
	
	            var startPage, endPage;
	            if (totalPages <= 10) {
	                // less than 10 total pages so show all
	                startPage = 1;
	                endPage = totalPages;
	            } else {
	                // more than 10 total pages so calculate start and end pages
	                if (currentPage <= 6) {
	                    startPage = 1;
	                    endPage = 10;
	                } else if (currentPage + 4 >= totalPages) {
	                    startPage = totalPages - 9;
	                    endPage = totalPages;
	                } else {
	                    startPage = currentPage - 5;
	                    endPage = currentPage + 4;
	                }
	            }
	
	            // calculate start and end item indexes
	            var startIndex = (currentPage - 1) * pageSize;
	            var endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);
	
	            // create an array of pages to ng-repeat in the pager control
	            var pages = _.range(startPage, endPage + 1);
	
	            // return object with all pager properties required by the view
	            return {
	                totalItems: totalItems,
	                currentPage: currentPage,
	                pageSize: pageSize,
	                totalPages: totalPages,
	                startPage: startPage,
	                endPage: endPage,
	                startIndex: startIndex,
	                endIndex: endIndex,
	                pages: pages
	            };
	        }
	    }
    ])
    .factory('utils', [
        function () {
            return {
                // Util for finding an object by its 'id' property among an array
                findByItemId: function findById(a, id) {
                    for (var i = 0; i < a.length; i++) {
                        if (a[i].item_id == id) return a[i];
                    }
                    return null;
                },
                findById: function findById(a, id) {
                    for (var i = 0; i < a.length; i++) {
                        if (a[i].id == id) return a[i];
                    }
                    return null;
                },
                // serialize form
                serializeObject: function (form) {
                    var o = {};
                    var a = form.serializeArray();
                    $.each(a, function () {
                        if (o[this.name] !== undefined) {
                            if (!o[this.name].push) {
                                o[this.name] = [o[this.name]];
                            }
                            o[this.name].push(this.value || '');
                        } else {
                            o[this.name] = this.value || '';
                        }
                    });
                    return o;
                },
                // high density test
                isHighDensity: function () {
                    return ((window.matchMedia && (window.matchMedia('only screen and (min-resolution: 124dpi), only screen and (min-resolution: 1.3dppx), only screen and (min-resolution: 48.8dpcm)').matches || window.matchMedia('only screen and (-webkit-min-device-pixel-ratio: 1.3), only screen and (-o-min-device-pixel-ratio: 2.6/2), only screen and (min--moz-device-pixel-ratio: 1.3), only screen and (min-device-pixel-ratio: 1.3)').matches)) || (window.devicePixelRatio && window.devicePixelRatio > 1.3));
                },
                // touch device test
                isTouchDevice: function () {
                    return !!('ontouchstart' in window);
                },
                // local storage test
                lsTest: function () {
                    var test = 'test';
                    try {
                        localStorage.setItem(test, test);
                        localStorage.removeItem(test);
                        return true;
                    } catch (e) {
                        return false;
                    }
                },
                // show/hide card
                card_show_hide: function (card, begin_callback, complete_callback, callback_element) {
                    $(card)
                        .velocity({
                            scale: 0,
                            opacity: 0.2
                        }, {
                            duration: 400,
                            easing: [0.4, 0, 0.2, 1],
                            // on begin callback
                            begin: function () {
                                if (typeof begin_callback !== 'undefined') {
                                    begin_callback(callback_element);
                                }
                            },
                            // on complete callback
                            complete: function () {
                                if (typeof complete_callback !== 'undefined') {
                                    complete_callback(callback_element);
                                }
                            }
                        })
                        .velocity('reverse');
                }
            };
        }]
    )
;

angular.module("ConsoleLogger", [])
    .factory("PrintToConsole", ["$rootScope", function ($rootScope) {
        var handler = { active: false };
        handler.toggle = function () { handler.active = !handler.active; };
        if (handler.active) {
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                console.log("$stateChangeStart --- event, toState, toParams, fromState, fromParams");
                console.log(arguments);
            });
            $rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
                console.log("$stateChangeError --- event, toState, toParams, fromState, fromParams, error");
                console.log(arguments);
            });
            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
                console.log("$stateChangeSuccess --- event, toState, toParams, fromState, fromParams");
                console.log(arguments);
            });
            $rootScope.$on('$viewContentLoading', function (event, viewConfig) {
                console.log("$viewContentLoading --- event, viewConfig");
                console.log(arguments);
            });
            $rootScope.$on('$viewContentLoaded', function (event) {
                console.log("$viewContentLoaded --- event");
                console.log(arguments);
            });
            $rootScope.$on('$stateNotFound', function (event, unfoundState, fromState, fromParams) {
                console.log("$stateNotFound --- event, unfoundState, fromState, fromParams");
                console.log(arguments);
            });
        };
        return handler;
    }]);