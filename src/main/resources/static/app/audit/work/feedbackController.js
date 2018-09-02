angular
    .module('altairApp')
    	.controller("feedbackController", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'mainService',
    	                           'user_data',
    	                           'messages',
    	                           'variables',
    	                           'feedback_categories',
    	                           'optgroups',
    	                           '$filter',
	        function ($scope,$rootScope,$state,$timeout,mainService,user_data,messages,variables,feedback_categories,optgroups,$filter) {
    	       
    	       $scope.message_reply = "";                 	   
    	       $scope.feedbackCategories = feedback_categories;
    	       $scope.optgroups = optgroups;
    	       $scope.user_data = user_data[0];
    	       
    	       $scope.checkPermission = function(){
    	    	   return $scope.user_data.rolefeedback.indexOf("newsadmin");
    	       }
    	       
    	       $scope.findRootCategoryById = function(id){
    	    	   return $filter('filter')($scope.optgroups, {value: id})[0];
   	           }
    	       
    	       $scope.findCategoryById = function(id){
    	    	   return $filter('filter')($scope.feedbackCategories, {value: id})[0];
   	           }
    	       
        	   $rootScope.toBarActive = true;

               $scope.$on('$destroy', function() {
                   $rootScope.toBarActive = false;
               });
               
               if (messages.data == null || messages.data == undefined){
            	   $scope.messages = [];
               }
               else{
            	   $scope.messages = messages.data;
               }
               
               $scope.feedbackSelectConfig = {
   	                create: false,
   	                maxItems: 1,
   	                placeholder: 'Сонгох...',
   	                optgroupField: 'parentid',
   	                optgroupLabelField: 'text',
   	                optgroupValueField: 'value',
   	                optgroups: $scope.optgroups,
   	                valueField: 'value',
   	                labelField: 'text',
   	                searchField: 'text',
   	                preload: true
   	             };
               
               var $mailbox = $('#mailbox');

               // show message details
               $mailbox.on('click', '.md-card-list ul > li', function(e) {

                   if ( !$(e.target).closest('.md-card-list-item-menu').length && !$(e.target).closest('.md-card-list-item-select').length ) {

                       var $this = $(this);

                       if (!$this.hasClass('item-shown')) {
                           // get height of clicked message
                           var el_min_height = $this.height() + $this.children('.md-card-list-item-content-wrapper').actual("height");

                           // hide opened message
                           $mailbox.find('.item-shown').velocity("reverse", {
                               begin: function (elements) {
                                   $(elements).removeClass('item-shown').children('.md-card-list-item-content-wrapper').hide().velocity("reverse");
                               }
                           });

                           // show message
                           $this.velocity({
                               marginTop: 40,
                               marginBottom: 40,
                               marginLeft: -20,
                               marginRight: -20,
                               minHeight: el_min_height
                           }, {
                               duration: 300,
                               easing: variables.easing_swiftOut,
                               begin: function (elements) {
                                   $(elements).addClass('item-shown');
                               },
                               complete: function (elements) {
                                   // show: message content, reply form
                                   $(elements).children('.md-card-list-item-content-wrapper').show().velocity({
                                       opacity: 1
                                   });

                                   // scroll to message
                                   var container = $('body'),
                                       scrollTo = $(elements);
                                   container.animate({
                                       scrollTop: scrollTo.offset().top - $('#page_content').offset().top - 8
                                   }, 1000, variables.bez_easing_swiftOut);

                               }
                           });
                       }
                   }

               });
               // hide message on: outside click, esc button
               $(document).on('click keydown', function(e) {
                   if (
                       ( !$(e.target).closest('li.item-shown').length ) || e.which == 27
                   ) {
                       $mailbox.find('.item-shown').velocity("reverse", {
                           begin: function(elements) {
                               $(elements).removeClass('item-shown').children('.md-card-list-item-content-wrapper').hide().velocity("reverse");
                           }
                       });
                   }
               });

    	        $scope.feedback = {};
    	          
    	        var feedbackmodal = UIkit.modal("#mailbox_new_message");
    	        
    	        $scope.submitFeedbackRequest = function(){
    	        	mainService.withdata("POST","/work/create/com.netgloo.models.LutFeedback.",$scope.feedback).then(function(response){
    	        		if (response.status){
    	        			$scope.messages.unshift(response.data);
    	        			$scope.feedback = {};
    	        			feedbackmodal.hide();
    	        		}
    	        	});
    	        }
    	        
    	        $scope.findById = function(array, id){
    	        	return $filter('filter')(array, {id: id})[0];
    	        }
    	        
    	        $scope.replyFeedback = function(message_reply,parentid, inputid){
    	        	mainService.withdata("POST","/work/create/com.netgloo.models.LutFeedback.",{description:message_reply, parentid: parentid}).then(function(response){
    	        		if (response.status){
    	        			if ($scope.findById($scope.messages,parentid) != null && $scope.findById($scope.messages,parentid) != undefined){
    	        				$scope.findById($scope.messages,parentid).lutFeedbacks.push(response.data);
    	        			}
    	        			
    	        			$scope.message_reply = "";
    	        			$("#mailbox_reply_"+inputid).val("");
    	        		}
    	        	});
    	        }
    	    }]);