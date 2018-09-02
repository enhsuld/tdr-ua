angular
    .module('altairApp')
    	.controller("StatsCapCtrl", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',    	                           
    	                           'sweet',
    	                           'mainService',  
    	                           'p_cap',
	        function ($scope,$rootScope,$state,$timeout,sweet,mainService,p_cap) { 
    	                        	   
	    	 	    	
	    	  $scope.cap=p_cap;
	    	  console.log(p_cap)
	    	  
	    	  var a=$scope.cap.a;
    		  var b=$scope.cap.b;
			  var c=$scope.cap.c;
			  var d=$scope.cap.d;
			  var e=$scope.cap.e;
			  var f=a+b+c+d+e;
	    	  

	           	
	        	// donut chart
	        var c3chart_donut_id = 'c3_chart_donut';

	        if ( $('#'+c3chart_donut_id).length ) {

	            var c3chart_donut = c3.generate({
	                bindto: '#'+c3chart_donut_id,
	                data: {
	                    columns: [
	                        ['Заах аргач='+a, a],
	                        ['Тэргүүлэх='+b, b],
	                        ['Зөвлөх='+c, c],
	                        ['Бусад зэрэг='+d, d],
	                        ['Ахлах='+e, e],                              
	                    ],
	                    type : 'donut',
	                    
	                },
	                donut: {
	                    title: "Нийт="+f ,
	                    width: 40
	                },
	                color: {
	                    pattern: ['#2196f3', '#0097a7', '#222222', '#555555', '$999999']
	                }
	            });
	           

	        }
	        
	        
	     // donut chart2
	        var c4chart_donut_id = 'c4_chart_donut';

	        if ( $('#'+c4chart_donut_id).length ) {

	            var c4chart_donut = c3.generate({
	                bindto: '#'+c4chart_donut_id,
	                data: {
	                    columns: [
	                        ['Өвчтэй='+c, c],
	                        ['Чөлөөтэй='+d, d],
	                        ['Тасалсан='+e, e]
	                    ],
	                    type : 'donut',
	                    
	                },
	                donut: {
	                    title: "Ирц",
	                    width: 40
	                },
	                color: {
	                    pattern: ['#ffa000', '#2196f3', '#e53935']
	                }
	            });                

	        }  

           
	  
        }]
    )
