angular
    .module('altairApp')
    	.controller("excelCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
    	                           'LutAuditDir',
    function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data,LutAuditDir) { 
    	            
	    //var spreadsheet = $("#spreadSheetView").data("kendoSpreadsheet");
    	   
	    $scope.files=[];
	    $scope.onSuccess = function(d) {
	    	
	    	 $scope.files.push(d.response);
	    	 var blob = d.response.fileurl;
    	     console.log(blob);
            
            var xhr = new XMLHttpRequest();
        	xhr.open('GET', d.response.fileurl, true);
        	xhr.responseType = 'blob';
        	 
        	xhr.onload = function(e) {
        	  if (this.status == 200) {
        	    // get binary data as a response
        	    var blob = this.response;
        	    var spreadsheet = $("#spreadSheetView").data("kendoSpreadsheet");
		            spreadsheet.fromFile(blob);
        	  }
        	  else{
        		  sweet.show('Анхаар!', 'Файл устгагдсан байна.', 'error');
        	  }
        	};
        	xhr.send();
	            
       }
    }
]);
