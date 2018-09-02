angular
    .module('altairApp')
    .controller('ndetCtrl', [
        '$state',
        '$stateParams',
        '$scope',
        'utils',        
        'ntype',
        'nmore',
        'rnlist',
        'mainService',       
        function ($state,$stateParams,$scope,utils,ntype,nmore,rnlist,mainService) {

           $scope.newslist=nmore;
           $scope.ntype=ntype; 
           $scope.newstext=nmore.newstext;           
           if($scope.newslist.afls=="no"){        	  
        	   $scope.nofile=true;
           }
           else{
        	   $scope.nofile=false;
           }               	
           $scope.back= function() {
        	   $state.go('restricted.pages.newslist');				
		}
           $scope.rnlist=rnlist;
           
           
           $scope.show =function(x){
        	   $scope.purl=x;
           }
           
           $scope.picture=function(x){
        	   if(x!=undefined){
        		   if(x.split('.').length>0){
            		   if(x.split('.')[x.split('.').length-1]=='jpeg' || x.split('.')[x.split('.').length-1]=='jpg' || x.split('.')[x.split('.').length-1]=='png' || x.split('.')[x.split('.').length-1]=='gif'){
            			   return true;
            		   }
            		  
            	   }
        	   }
        	   
        	   return false;
           }
           
           $scope.more=function(i){
          	 	
      			$state.go('restricted.pages.newsmore',{param: i});
      	
      			    	    	
      		}
   		}           
    ]);