angular
    .module('altairApp')
    .controller('validationCtrl', [
        '$scope',
        '$rootScope',
        'mainService',
        '$stateParams',
        'p_form',
        'sweet',
        '$state',
        function ($scope,$rootScope,mainService,$stateParams,p_form,sweet,$state) {

        	$('.dropify').dropify();
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
          
            
            $scope.form={
            		id:0,
            		formname:'',
            		appid:0,
            		icon:'',
            		startdate:'',
            		enddate:'',
            		ceduFormTabs:[]
            }
            
			if($stateParams.param==0){
				 $scope.form={
		               		id:0,
		               		formname:'',
		               		appid:0,
		               		icon:'',
		               		startdate:'',
		               		enddate:'',
		               		ceduFormTabs:[]
		               }  
		                
		           
		            
		            $scope.input={
		            	tabname:'',
		            	ceduFormNotes:[]
		            }
			}
			else{
				 $scope.form=p_form;
			}
            
           
              
            $scope.add = function() {
            	console.log($scope.form);
                $scope.form.ceduFormTabs.push($scope.input);
                $scope.input = {
                		tabname:'',
                		ceduFormNotes:[]
                };
            };
            
           
            
            $scope.formNote=false;
            
            $scope.addNote = function() {
            	 $scope.formNote=true;
            };
            
              
            $scope.remove = function(index) {
            	$scope.form.ceduFormTabs.splice(index, 1);
            };
            
            $scope.removeNote = function(index) {
            	$scope.form.ceduFormTabs[$scope.vs].ceduFormNotes.splice(index, 1);
            };
            
            // date range
            var $dp_start = $('#uk_dp_start'),
                $dp_end = $('#uk_dp_end');

            var start_date = UIkit.datepicker($dp_start, {
                format:'DD.MM.YYYY'
            });

            var end_date = UIkit.datepicker($dp_end, {
                format:'DD.MM.YYYY'
            });

            $dp_start.on('change',function() {
                end_date.options.minDate = $dp_start.val();
            });

            $dp_end.on('change',function() {
                start_date.options.maxDate = $dp_end.val();
            });
            
            
            $scope.note={
            		title:'',
            		isapprove:false,
            		isfile:false,
            		isform:false,
            		filesize:0
            }
                        
            $scope.addFormNote = function(index) {    
                $scope.form.ceduFormTabs[index].ceduFormNotes.push($scope.note);
                $scope.note={};
            };
            
          
            
            $scope.addOpen = function(index){
            	$scope.vs=index;
            }
            
            $scope.selectize_val_options = [
                { value: 1, label: 'Багшлах эрх' },
                { value: 2, label: 'Мэргэжлийн зэрэг' },
                { value: 3, label: 'Шагнал' },
                { value: 4, label: 'Тусгай зөвшөөрөл' },
                { value: 5, label: 'Кабинет' },
                { value: 6, label: '1212' },
                { value: 7, label: '888' },
            ];

            $scope.selectize_val_config = {
                maxItems: 1,
                valueField: 'value',
                labelField: 'label',
                create: false,
                placeholder: 'Choose...'
            };
            
            
            $scope.mainSubmit = function(){
             	 mainService.withdata('put','/izr/core/mainform/CEduLutForm',$scope.form)
		   			.then(function(data){
		   				if(data){
		   					sweet.show('Мэдээлэл', 'Амжилттай хадгаллаа.', 'success');	
    		   				$state.go('restricted.pages.form');	
		   				}		   			
	   			});
           }

        }
    ]);