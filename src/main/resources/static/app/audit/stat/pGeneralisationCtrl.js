angular
    .module('altairApp')
    	.controller("dt_table_tools", [
    	                           '$scope',
    	                           '$rootScope',
    	                           '$state',
    	                           '$timeout',
    	                           'sweet',
    	                           'mainService',
    	                           'user_data',
    function ($scope,$rootScope,$state,$timeout,sweet,mainService,user_data) {     								   
    		
		var modal = UIkit.modal("#modal_update");
    	
		$scope.role=user_data[0].role;
		    	
		$scope.domain="com.netgloo.models.MainAuditRegistration.";
		
		
		$scope.read=function(item){
			$state.go('restricted.pages.mainwork',{issueId:item.id,stepid:item.stepid,typeid:item.orgtype});
		}
		  
		var aj=[{"text":"Хувиарлаагүй","value":"1"},{"text":"Хувиарласан","value":"2"},{"text":"Баталсан","value":"3"},{"text":"Буцаасан","value":"4"}];
		
		var alevel=[{"text":"Төлөвлөх үе шат","value":"3"},{"text":"Гүйцэтгэх үе шат","value":"4"},{"text":"Тайлагналын үе шат","value":"5"},{"text":"Тайлагналын дараах","value":"6"}];
        
		var destype=[{"text":"Зөвлөмж өгөх","value":1},{"text":"Төлбөрийн акт тогтоох","value":2},{"text":"Албан шаардлага өгөх","value":3},{"text":"Сахилгын шийтгэл ногдуулахаар шилжүүлэх","value":4},{"text":"Хууль хяналтын байгууллагад асуудлыг шилжүүлэх","value":5}];

        $scope.puserGrid = {
            dataSource: {
                autoSync: true,
                transport: {
                	read:  {
                        url: "/au/list/LnkRiskTryout",
                        contentType:"application/json; charset=UTF-8",           
                        data:{"custom":"where data23=1 and data25=0"},
                        type:"POST"
                    },
                    parameterMap: function(options) {
                   	 return JSON.stringify(options);
                   }
                },
                schema: {
                 	data:"data",
                 	total:"total",
                 	 model: {                                	
                         id: "id",		   			                           	                    
                     }
                 },
                pageSize: 5,
                serverPaging: true,
                serverSorting: true,
                serverFiltering: true
            },
            toolbar:["excel","pdf"],
          filterable: {
         	 mode: "row"
          },
          excel: {
	                fileName: "Organization Export.xlsx",
	                proxyURL: "//demos.telerik.com/kendo-ui/service/export",
	                filterable: true,
	                allPages: true
	            },
            sortable: true,
            resizable: true,
            pageable: {
                refresh: true,
                pageSizes: true,
                buttonCount: 5
            },
            columns: [
      		  { title: "#",template: "<span class='row-number' style='float:left;width:100%; text-align:center'></span>", width:50},      		 
      		  { field:"mainAuditRegistration.gencode", title: "Аудитын чиглэл", width: "200px" },	
      		  { field:"mainAuditRegistration.orgcode", title: "Аудитын чиглэл", width: "200px" },	
      		  { field:"mainAuditRegistration.orgname", title: "Аудитын чиглэл", width: "200px" },	
      		  { field:"data1", title: "Аудитын чиглэл", width: "200px" },	
      		  { field:"data20", title: "Тухайн аудитын чиглэлд нөлөөлөх эрсдэлд хамаарах төлөвлөсөн горим сорилын үр дүнг (нөхцөл байдал) товч тодорхойлох",editor: $scope.categoryDropDownEditor,template: "#=(data20==null) ? '' : data20#"  , width: 450 },	
      		  { field:"data21", title: "Мөнгөн дүн (Сая төгрөгөөр)", width: "150px" },	
      		  { field:"b46data1", editor: $scope.categoryDropDownEditor, title: "<span style='vertical-align:middle'>Зөрчсөн стандарт, хууль, тогтоомжийн нэр, заалт</span>",width: 450, template:"# if (b46data1 != null) { # <span class='tulgaltRed'>#:b46data1#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
      		  { field:"b46data2", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Зөрчил гарсан шалтгаан, үр дагавар</span>",width: "200px", template:"# if (b46data2 != null) { # <span class='tulgaltRed'>#:b46data2#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
      		  { field:"b46data3", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Асуудлыг шийдвэрлэхэд үндэслэл болгосон стандарт,  хууль, тогтоомжийн нэр, заалт</span>",width: 450, template:"# if (b46data3 != null) { # <span class='tulgaltRed'>#:b46data3#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
      		  { field:"b46data4", values:destype, title: "<span style='vertical-align:middle'>Залруулаагүй буруу илэрхийлэл буюу алдаа, зөрчилтэй асуудлыг шийдвэрлэсэн байдал</span>",width: "200px"},
      		  { field:"b46data5", editor: $scope.categoryDropDownEditor,title: "<span style='vertical-align:middle'>Тайлбар</span>",width: "200px", template:"# if (b46data5 != null) { # <span class='tulgaltRed'>#:b46data5#</span> # } else { # <span class='lala md-bg-red-100'></span> # } #"},
      		 /* { 
                        	 template: kendo.template($("#update").html()),  width: "150px" 
                    }*/],
                   dataBound: function () {
   		                var rows = this.items();
   		                  $(rows).each(function () {
   		                      var index = $(this).index() + 1 
   		                      + ($(".k-grid").data("kendoGrid").dataSource.pageSize() * ($(".k-grid").data("kendoGrid").dataSource.page() - 1));;
   		                      var rowLabel = $(this).find(".row-number");
   		                      $(rowLabel).html(index);
   		                  });
   	   		              var grid = this;
   			              grid.tbody.find("tr").dblclick(function (e) {
   			                  var dataItem = grid.dataItem(this);
   			                  $scope.read(dataItem);
   			              });
   		  	           },
                  editable: "popup"
        };
							   
    								   
/*	    $scope.dtOptions = DTOptionsBuilder
        .fromSource('/au/audits')
        .withDOM("<'dt-uikit-header'<'uk-grid'<'uk-width-medium-2-3'l><'uk-width-medium-1-3'f>>>" +
            "<'uk-overflow-container'tr>" +
            "<'dt-uikit-footer'<'uk-grid'<'uk-width-medium-3-10'i><'uk-width-medium-7-10'p>>>")
        .withPaginationType('full_numbers')
        // Active Buttons extension
        .withButtons([
            {
                extend:    'copyHtml5',
                text:      '<i class="uk-icon-files-o"></i> Copy',
                titleAttr: 'Copy'
            },
            {
                extend:    'print',
                text:      '<i class="uk-icon-print"></i> Print',
                titleAttr: 'Print'
            },
            {
                extend:    'excelHtml5',
                text:      '<i class="uk-icon-file-excel-o"></i> XLSX',
                titleAttr: ''
            },
            {
                extend:    'csvHtml5',
                text:      '<i class="uk-icon-file-text-o"></i> CSV',
                titleAttr: 'CSV'
            },
            {
                extend:    'pdfHtml5',
                text:      '<i class="uk-icon-file-pdf-o"></i> PDF',
                titleAttr: 'PDF'
            }
        ]);
	   $scope.dtColumnDefs = [
        DTColumnDefBuilder.newColumnDef(0).withTitle('Name'),
        DTColumnDefBuilder.newColumnDef(1).withTitle('Position'),
        DTColumnDefBuilder.newColumnDef(2).withTitle('Office'),
        DTColumnDefBuilder.newColumnDef(3).withTitle('Extn.'),
        DTColumnDefBuilder.newColumnDef(4).withTitle('Start date'),
        DTColumnDefBuilder.newColumnDef(5).withTitle('Salary')
    ];*/
    					            
	  /*  $scope.dtInstance = {}; 
	    $scope.dtOptions = DTOptionsBuilder.newOptions()             
        .withOption('ajax', {
        	dataSrc: "data",
            url: "/user/data/service/AnnualRegistration",
            type: "POST"
        })          
        .withDataProp('data')
        .withOption('createdRow', function(row, data, dataIndex) {
            $compile(angular.element(row).contents())($scope);
        })
        .withOption('processing', true) //for show progress bar
		.withOption('serverSide', true)   
        .withPaginationType('full_numbers')
        .withDisplayLength(10)
	    .withOption('aaSorting',[0,'asc']);*/
	    
	    
/*	    $scope.dtOptions = DTOptionsBuilder
	    .newOptions()             
        .withOption('ajax', {
        	dataSrc: "data",
            url: "/au/list/MainAuditRegistrationAu",
            type: "POST"
        })          
        .withDataProp('data')
        .withDOM("<'dt-uikit-header'<'uk-grid'<'uk-width-medium-2-3'l><'uk-width-medium-1-3'f>>>" +
            "<'uk-overflow-container'tr>" +
            "<'dt-uikit-footer'<'uk-grid'<'uk-width-medium-3-10'i><'uk-width-medium-7-10'p>>>")
        .withPaginationType('full_numbers')
        // Active Buttons extension
        .withButtons([
            {
                extend:    'copyHtml5',
                text:      '<i class="uk-icon-files-o"></i> Copy',
                titleAttr: 'Copy'
            },
            {
                extend:    'print',
                text:      '<i class="uk-icon-print"></i> Print',
                titleAttr: 'Print'
            },
            {
                extend:    'excelHtml5',
                text:      '<i class="uk-icon-file-excel-o"></i> XLSX',
                titleAttr: ''
            },
            {
                extend:    'csvHtml5',
                text:      '<i class="uk-icon-file-text-o"></i> CSV',
                titleAttr: 'CSV'
            },
            {
                extend:    'pdfHtml5',
                text:      '<i class="uk-icon-file-pdf-o"></i> PDF',
                titleAttr: 'PDF'
            }
        ]);

         $scope.dtColumns = [
            DTColumnBuilder.newColumn("licensenum", "Тусгай зөвшөөрлийн дугаар"),
            DTColumnBuilder.newColumn("reporttype", "Тайлан / Төлөвлөгөө"),
            DTColumnBuilder.newColumn("xtype", "Төрөл"),
            DTColumnBuilder.newColumn("lictype", "Тз-ийн төрөл"),
            DTColumnBuilder.newColumn("reportyear", "Тайлант огноо"),
            DTColumnBuilder.newColumn("repstatusid", "Төлөв"),
            DTColumnBuilder.newColumn("submissiondate", "Илгээсэн огноо"),
            DTColumnBuilder.newColumn("officerid", "Хүлээн авсан  мэргэжилтэн"),
            DTColumnBuilder.newColumn("approveddate", "Хүлээн авсан огноо")
        ] */
    }
]);
