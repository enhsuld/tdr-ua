angular
    .module('altairApp')
    	.controller("riskeditCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'p_dir',
    	                           'p_law',
    	                           'sweet',
    	                           '$filter',
    	                           'p_edit',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,p_dir,p_law,sweet,$filter,p_edit) { 
        
    	              $scope.p_law=p_law;
        	  $scope.selectize_a_data=[
        		  {"text":"Хяналтын", "value":0},
        		  {"text":"Уламжлалт", "value":1},
        	  ];
        	  
	    	  if(p_edit==""){
	    		  $scope.risk = {						  
							"id": 0,
			                "riskname": "",	    		           
			                "risktype": "",	
			                "risklaws": [],	
			                "riskstandarts": [],		 
		            };
	    		  $scope.links=[];
	    	   }
	    	  else{
	    		  $scope.risk = p_edit;
	    		  $scope.standarts=$scope.risk.riskstandarts;
	    		  $scope.laws=$scope.risk.risklaws;
	    		  console.log(p_edit);
	    		  console.log($scope.laws);
	    		  var dirrr = [];
	    		  var l= 0;
	 	   	    	  angular.forEach($scope.risk.dirz, function(val, key) {    			  
	 	       			 
	 	       			     if(val.id!=null){
	 	       			    	 l=val.id
	 	       			    	dirrr.push(l)	 	             			 
	 	    					} 	       			 
	 	    		  });	   	    	  
	 	   	    	  $scope.risk.dir=dirrr;
	 	   	    	  $scope.links=$scope.risk.links;
	 	   	    	  
	    	  	}
	    	    
                $scope.selectize_a_config = {
                       plugins: {
                           'disable_options': {
                               disableOptions: ["c1","c2"]
                           }
                       },
                       create: false,
                       maxItems: 1,
                       placeholder: 'Сонго...',
                       optgroupField: '',
                       optgroupLabelField: 'title',
                       optgroupValueField: 'ogid',
                       valueField: 'value',
                       labelField: 'text',
                       searchField: 'title'
                   };
	    	  

	    	 var planets_data = $scope.selectize_dir = p_dir;
  			 $scope.selectize_planets_config = {
	                plugins: {
	                    'remove_button': {
	                        label     : ''
	                    }
	                },
	                maxItems: null,
	                placeholder: 'Сонгох...',
	                valueField: 'value',
	                labelField: 'text',
	                searchField: 'title',
	                create: false,
	                render: {
	                    option: function(planets_data, escape) {
	                        return  '<div class="option">' +
	                            '<span class="title">' + escape(planets_data.text) + '</span>' +
	                            '</div>';
	                    },
	                    
	                }
	            };
  			 
  			$scope.otherrisk=[]; 
  			angular.forEach($scope.p_law, function(val, key) { 
			     if(val.lawcategory==3){
			    	 $scope.otherrisk.push(val);
				 }		     
			});
  			$scope.laws=[];
  			$scope.standarts=[];
  			 
  			$scope.addxb= function() {
  				var ct={
  						lawid:$scope.la.lname,
  						zuilid:$scope.la.lzuil,
  						zaalt:$scope.la.lzaalt,
  						lname:'',
  						lzuil:'',
  						lzaalt:''
  				}
  				
  				angular.forEach($scope.p_law, function(val, key) { 
      			     if(val.value==$scope.la.lname){
      			    	ct.lname=val.text;
   					 }
      			     if(val.value==$scope.la.lzuil){
     			    	ct.lzuil=val.text;
  					 }
	      			 if(val.value==$scope.la.lzaalt){
	   			    	ct.lzaalt=val.zaalt;
					 }
  				});
  				
  				$scope.risk.risklaws.push(ct);    
  				$scope.laws.push(ct);          
  				$scope.la={};
			}
  			$scope.removexb= function(i) {
  				$scope.risk.risklaws.splice(i, 1);        	
  			}
  			$scope.addst= function() {
  				var ct={
  						lawid:$scope.st.lname,
  						zuilid:$scope.st.lzuil,
  						zaalt:$scope.st.lzaalt,
  						lname:'',
  						lzuil:'',
  						lzaalt:''
  				}
  				angular.forEach($scope.p_law, function(val, key) { 
     			     if(val.value==$scope.st.lname){
     			    	ct.lname=val.text;
  					 }
     			     if(val.value==$scope.st.lzuil){
    			    	ct.lzuil=val.text;
 					 }
	      			 if(val.value==$scope.st.lzaalt){
	   			    	ct.lzaalt=val.zaalt;
					 }
 				});
  				$scope.st={};
  				$scope.risk.riskstandarts.push(ct);    
  				$scope.standarts.push(ct);         	
			}
  			$scope.removest= function(i) {
  				$scope.risk.riskstandarts.splice(i, 1);        	
  			}
  			
  			var risk_data= $scope.selectize_risk_options = [];
  			
  			var plaw = [];
  			var slaw = [];
  			var zlaw = [];
	  	    	 
	    	angular.forEach(p_law, function(val, key) {    	
   			     if(val.lawcategory==1 && val.parent==true){
   			    	 var c={
   			    			 "value":val.value,
   			    			 "text":val.text
   			    	 };
   			    	 plaw.push(c)
				}
   			    if(val.lawcategory==2 && val.parent==true){
			    	 var c={
			    			 "value":val.value,
			    			 "text":val.text
			    	 };
			    	 slaw.push(c)
				}
   			   if(val.lawcategory==3 && val.parent==true){
			    	 var c={
			    			 "value":val.value,
			    			 "text":val.text
			    	 };
			    	 zlaw.push(c)
				}
   			    
			}); 
	    	
	    	$scope.selectize_plaw_data = plaw;
	    	$scope.selectize_slaw_data = slaw;
	    	$scope.selectize_zlaw_data = zlaw;
	    	
	    	$scope.selectize_zuil_data = [];
	    	$scope.selectize_zaalt_data = [];
	    	$scope.selectize_law_zuil_data = [];
	    	$scope.selectize_law_zaalt_data = [];
	    	
	    	$scope.stnChange = function(i){
	    		if(i!=undefined){
	  			  	mainService.withdomain('get','/au/withid/LutLaw/'+i)
		   			.then(function(data){
		   				$scope.selectize_zuil_data = data; 		   				
		   			});	
	    		}else{
	    			$scope.selectize_zuil_data=[];
	    		}
  		    }
	    	$scope.stlChange = function(i){
	    		if(i!=undefined){
	    			mainService.withdomain('get','/au/withid/LutLaw/'+i)
		   			.then(function(data){
		   				$scope.selectize_law_zuil_data = data; 		   				
		   			});	
	    		}
	    		else{
	    			$scope.selectize_law_zuil_data = []; 		   			
	    		}
  		    }
	    	$scope.stlzChange = function(i){	    		
	    		if(i!=undefined){
	    			mainService.withdomain('get','/au/withid/LutLaw/'+i)
		   			.then(function(data){
		   				$scope.selectize_law_zaalt_data = data; 		   				
		   			});	
	    		}
	    		else{
	    			$scope.selectize_law_zaalt_data = []; 		   			
	    		}
  		    }
	    	$scope.stzChange = function(i){
	    		if(i!=undefined){
	    			mainService.withdomain('get','/au/withid/LutLaw/'+i)
		   			.then(function(data){
		   				$scope.selectize_zaalt_data = data; 		   				
		   			});	
	    		}
	    		else{
	    			$scope.selectize_zaalt_data = []; 		   			
	    		}
  		    }
	    	 
    	    $scope.zaalt_config = {
                 plugins: {
                     'remove_button': {
                         label: ''
                     }
                 },
                 persist: false,
                 maxItems: 1,
                 placeholder: 'Сонгох...',
                 optgroupField: 'parent_id',
                 optgroupLabelField: 'zaalt',
                 optgroupValueField: 'ogid',
                 valueField: 'value',
                 labelField: 'zaalt',
                 searchField: 'zaalt'
             };
	   		    
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

  		    $scope.backRisk=function(){
  		    	$state.go('restricted.pages.risk');
  		    }
  			$scope.addRisk = function() {	
  				 $scope.risk.rlinks=[];
  				 angular.forEach($scope.links, function(val, key) { 
       			     if(val.value!=null){
       			    	var c=val.value 
       			    	$scope.risk.rlinks.push(c)
					}
    			 });
  				 /*angular.forEach($scope.p_law, function(val, key) { 
       			     if(val.value==$scope.risk.other){
       			    	$scope.risk.othertext=val.text;
					}
    			 });*/
  				 //$scope.riskstandarts=$scope.standarts;
  				// $scope.risklaws=$scope.laws;
  			
  				 console.log($scope.risk);
    		     mainService.withdata('PUT','/info/Riskadd/'+$scope.risk.id, $scope.risk)
	   			.then(function(data){		   				
	   				if(data.re==1){
	   				sweet.show('Мэдээлэл', 'Амжилттай засагдлаа.', 'success');
	   				$state.go('restricted.pages.risk');
	   				}
	   				if(data.re==0){
	   				sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');	   	
	   				$state.go('restricted.pages.risk');
	   				} 		   			 				
	   			});	 
         }
}
]);
