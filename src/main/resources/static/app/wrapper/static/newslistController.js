angular
    .module('altairApp')
    .controller('nlistCtrl', [
        '$state',
        '$stateParams',
        '$scope',
        'utils',        
        'nlist',
        'ntype',
        'mainService',       
        'PagerService',
        function ($state,$stateParams,$scope,utils,nlist,ntype,mainService,PagerService) {
        	
        
             $scope.dummyItems = _.range(1, 151); // dummy array of items to be paged
             $scope.pager = {};
             $scope.setPage = setPage;

             initController();

             function initController() {
                 // initialize to page 1
                 $scope.setPage(1);
             }

             function setPage(page) {
                 if (page < 1 || page > $scope.pager.totalPages) {
                     return;
                 }

                 // get pager object from service
                 $scope.pager = PagerService.GetPager(nlist.length, page,5);

                 // get current page of items
                 $scope.newslist = nlist.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
             }
             
        	
           $scope.snewslist=nlist;
           $scope.ntype=ntype;
           
           $scope.filter=function(i){
        	   mainService.withdomain('get', '/info/newsfilter/'+i)
        	   .then(function(data){
        		   nlist=data;
        		   initController();
        	   });  
           }
           
           $scope.more =function(i){
        	   $state.go("restricted.pages.newsmore",{param:i});
           }
           
        }
    ]);