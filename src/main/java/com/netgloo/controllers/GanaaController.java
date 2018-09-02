package com.netgloo.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import com.google.common.io.Files;
import com.netgloo.IzrApplication;
import com.netgloo.dao.UserDao;
import com.netgloo.models.DataSourceResult;
import com.netgloo.models.FsFormA1;
import com.netgloo.models.FsFormA2;
import com.netgloo.models.FsFormA3;
import com.netgloo.models.FsFormA4;
import com.netgloo.models.FsLutFormType;
import com.netgloo.models.LnkDirectionNotice;
import com.netgloo.models.LnkDirectionProcedure;
import com.netgloo.models.LnkFactorCategory;
import com.netgloo.models.LnkMainFormT2;
import com.netgloo.models.LnkTryoutCat;
import com.netgloo.models.LnkTryoutConfMethod;
import com.netgloo.models.LnkTryoutConfSource;
import com.netgloo.models.LnkTryoutConfType;
import com.netgloo.models.LnkTryoutNotice;
import com.netgloo.models.LnkTryoutProcedure;
import com.netgloo.models.LnkTryoutRisk;
import com.netgloo.models.LnkUserrole;
import com.netgloo.models.LnkWorkAuType;
import com.netgloo.models.LnkWorkCategory;
import com.netgloo.models.LutAuditDir;
import com.netgloo.models.LutAuditLevel;
import com.netgloo.models.LutAuditWork;
import com.netgloo.models.LutAuditYear;
import com.netgloo.models.LutCategory;
import com.netgloo.models.LutConfirmationMethod;
import com.netgloo.models.LutConfirmationSource;
import com.netgloo.models.LutConfirmationType;
import com.netgloo.models.LutDepartment;
import com.netgloo.models.LutFactor;
import com.netgloo.models.LutFactorCriterion;
import com.netgloo.models.LutGroupOfFactor;
import com.netgloo.models.LutLaw;
import com.netgloo.models.LutMainComment;
import com.netgloo.models.LutMenu;
import com.netgloo.models.LutNotice;
import com.netgloo.models.LutPosition;
import com.netgloo.models.LutProcedure;
import com.netgloo.models.LutReason;
import com.netgloo.models.LutRisk;
import com.netgloo.models.LutRole;
import com.netgloo.models.LutTryout;
import com.netgloo.models.LutUser;
import com.netgloo.models.LutValidation;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.models.SubAuditOrganization;
import com.netgloo.repo.LnkMenuRepository;
import com.netgloo.repo.MenuRepository;
import com.netgloo.repo.RoleRepository;
import com.netgloo.repo.UserRepository;
import com.netgloo.service.SmtpMailSender;


@Controller
@RestController
@RequestMapping("/my")
public class GanaaController {
/*
    @Autowired
    private UserDao userDao;*/
	
	@Autowired
	private SmtpMailSender smtpMailSender;

	@Autowired
    private UserDao dao;
	    
    @Autowired
    private UserRepository upo;	  
	 
    @Autowired
    private MenuRepository mpo;	  
    
    @Autowired
    private RoleRepository rpo;	
    
    @Autowired
    private LnkMenuRepository lpo;	  
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @RequestMapping(value="/updateFsFormA4",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public String updateFsFormA4(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj= new JSONObject(jsonString);
			 List<FsFormA4> fs=  (List<FsFormA4>) dao.getHQLResult("from FsFormA4 t where t.planid="+obj.getLong("planid")+" and t.stepid="+obj.getLong("stepid")+"", "list");
			 for(int i=0;i<fs.size();i++){
				 FsFormA4 a4 = fs.get(i);
				 Double f;
				 try {
					 f = Double.valueOf(obj.getString("percent"));
					 System.out.println(f);
					 if (obj.has("id")){
						 if(a4.getId()==obj.getLong("id")){
							 a4.setIsselect(1);
							 a4.setPercent(obj.getString("percent"));
						 }else{
							 a4.setIsselect(0);
							 a4.setPercent(null);
						 }
					 }
					 
					 Double data2 = Double.valueOf(a4.getData2());
					 data2 = data2/100*f;
					 a4.setData4(String.format("%f",data2));
					 dao.PeaceCrud(a4, "LutTryout", "update",(long) a4.getId(), 0, 0, null);
			     } catch (NumberFormatException nfe) {
			         System.out.println("NumberFormatException: " + nfe.getMessage());
			     }
				 
			 }	
			 return "true";
		}
		else{
			 return "false";
		}
	}
    
    @RequestMapping(value="/exportEHuraangui/{id}/{stepid}/{furl}",method=RequestMethod.GET)
    public void exportEHuraangui(@PathVariable long id,@PathVariable long stepid,@PathVariable String furl, HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {
    

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			FileInputStream fis = null;
			String appPath = req.getServletContext().getRealPath("");	
			File files = new File(appPath+"/assets/zagvarfile/working_papers.xlsx");
			 
			fis = new FileInputStream(files);
        	Workbook workbook = new XSSFWorkbook(fis);
        	
        	
			XSSFSheet sheet = null;
			for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
                XSSFSheet tmpSheet =(XSSFSheet) workbook.getSheetAt(i);
                if(!tmpSheet.getSheetName().equals(furl)){
                	workbook.removeSheetAt(i);
                }else{
                	sheet = tmpSheet;
                }
			}

			List<LnkMainFormT2> mf=  (List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.mid="+id+"", "list");
			
			if(sheet!=null){
    			if((XSSFSheet) workbook.getSheet(furl)!=null){
    				System.out.println("sheet oldson");
    				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
    				SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
    				Row row = sheet.getRow(5);
    				System.out.println(org.getOrgname()+sheet.getSheetName());
    		        Cell cell = row.getCell(3);
    		        cell.setCellValue((String) org.getOrgname());
    		        
    		        row = sheet.getRow(7);
       		        cell = row.getCell(3);
       		        cell.setCellValue((String) main.getGencode());
	       		    
       		        int w=12;
	       		    for(int i=0;i<main.getLnkMainFormT2s().size();i++){ 
	       		    	 if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size()>0){
	       		    		 for(int y=0;y<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size();y++){
			       	        	 row=sheet.createRow(w+i+y);
			       	        	 //row=sheet.createRow(w+i+y);
			       	        	 System.out.println("row number");
			       	        	 System.out.println(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size());
			       	        	 cell=row.createCell(0);				       	        	
				       	         cell.setCellValue(i+y+1);
			       	        	 cell=row.createCell(1);			       	        	
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLutAuditDir().getName());
				       	         
			       	        	 cell=row.createCell(2);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLutRisk().getRiskname());
				       	         
				       	         cell=row.createCell(3);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1());
				       	         
				       	         cell=row.createCell(4);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2());
				       	         
				       	         cell=row.createCell(5);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData3());
				       	         
				       	     	 cell=row.createCell(6);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData4());
				       	         
				       	         cell=row.createCell(7);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData5());
				       	         
				       	         cell=row.createCell(8);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData6());
				       	         
				       	         cell=row.createCell(9);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData7());
				       	         
				       	         cell=row.createCell(10);
				       	         cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getDescription());
				       	       
			       	         }
			       	         
	       		    	 }
	       		    }
    				  
					String xname=org.getOrgname().trim()+"-"+furl;
    				xname = URLEncoder.encode(xname,"UTF-8"); 
    		        try (ServletOutputStream outputStream = response.getOutputStream()) {
        				response.setContentType("application/ms-excel; charset=UTF-8");
        				response.setCharacterEncoding("UTF-8");
        	            response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
        	            workbook.write(outputStream);
        	            outputStream.close();
        	        }
        	        catch (Exception e) {
        	        	System.out.println("ishe orov");
        			}
    		     
    			}
        	      
        	}
		}
	 }
    
    @RequestMapping(value="/exportOrgInfo/{sheetname}/{id}",method=RequestMethod.GET)
    public void exportOrgInfo(@PathVariable long id,@PathVariable String sheetname, HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {
    

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			String appPath = req.getServletContext().getRealPath("");	
			File files = new File(appPath+"/assets/zagvarfile/working_papers.xlsx");
			 
			fis = new FileInputStream(files);
        	Workbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = (XSSFSheet) workbook.getSheet(sheetname);
			MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
			SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
			//System.out.println("sheet enenene"+sheetname);
			//System.out.println("sheet oldson");
			//int year = Year.now().getValue();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			if((XSSFSheet) workbook.getSheet(sheetname)!=null){
				//System.out.println("sheet oldson");
	        	
				Row row = sheet.getRow(4);		        
		        Cell cell = row.getCell(4);
		        cell.setCellValue((String) org.getOrgname());
		        
		        row = sheet.getRow(6);
		        cell = row.getCell(4);
		        cell.setCellValue((String) main.getGencode());
		        
		        //int num=10;
		        
		        row = sheet.getRow(10);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getOrgname());		        
		        
		        row = sheet.getRow(11);
		        cell = row.getCell(5);
		        cell.setCellValue((long) org.getRegid());
		        
		        row = sheet.getRow(12);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getStateregid());
		        
		        row = sheet.getRow(13);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getCreatedate());
		        
		        row = sheet.getRow(14);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getFsorg());
		        
		        row = sheet.getRow(15);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getTaxorg());
		        
		        row = sheet.getRow(16);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getNdorg());
		        
		        row = sheet.getRow(17);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getHeadorder());
		        
		        
		        String scheck= "[";
		        //System.out.println("111111,"+scheck+",");
		        if(org.getBanks()==null){
		        	 row = sheet.getRow(18);
				        cell = row.getCell(5);
				        cell.setCellValue((String) "Мэдээлэл байхгүй");
				        
				}
		        else{
		        	String check= org.getBanks().substring(0, 1);
		        	//System.out.println("0000000000,"+check+",");
		        	if(check.equalsIgnoreCase(scheck)){
		        		String Banks = "{a1:"+org.getBanks()+"}";					
						JSONObject banksobj = new JSONObject(Banks);
						JSONArray array= banksobj.getJSONArray("a1");
						
						if(array.length()>0){
							String xbanks="";
							for(int a=0;a<array.length();a++){
								 JSONObject lao= array.getJSONObject(a);
								 xbanks= xbanks+lao.getString("xbankname") +":"+lao.getString("xbankaccount")+", ";			
							}
							row = sheet.getRow(18);
					        cell = row.getCell(5);
					        cell.setCellValue((String) xbanks);
						}
		        	}
		        
					else{
						row = sheet.getRow(18);
				        cell = row.getCell(5);
				        cell.setCellValue((String) org.getBanks());
					}
					
				}
		        
		        if(org.getStatebanks()==null){
		        	 row = sheet.getRow(19);
				        cell = row.getCell(5);
				        cell.setCellValue((String) "Мэдээлэл байхгүй");
				        
				}
		        else{
		        	String check= org.getStatebanks().substring(0, 1);
		        	if(check.equalsIgnoreCase(scheck)){
		        		//System.out.println("888");
		        		String Banks = "{a1:"+org.getStatebanks()+"}";					
						JSONObject banksobj = new JSONObject(Banks);
						JSONArray array= banksobj.getJSONArray("a1");
						
						if(array.length()>0){
							//System.out.println("333");
							String xbanks="";
							for(int a=0;a<array.length();a++){
								 JSONObject lao= array.getJSONObject(a);
								 xbanks= xbanks+lao.getString("sbankaccount")+", ";			
							}
							row = sheet.getRow(19);
					        cell = row.getCell(5);
					        cell.setCellValue((String) xbanks);
						}
		        	}		        	
					else{						
						row = sheet.getRow(19);
				        cell = row.getCell(5);
				        cell.setCellValue((String) org.getStatebanks());
					}
				}
		        
		        row = sheet.getRow(20);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getWeb());
		        
		        
		        row = sheet.getRow(21);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getEmail());
		        
		        
		        row = sheet.getRow(22);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getAddress());
		        
		        
		        row = sheet.getRow(23);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getPhone());
		        
		        
		        row = sheet.getRow(24);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getFax());
		       
		        
		        //bairshliin code
		        String str = org.getOrgcode();
		        if(str.length()==16){
		        	row = sheet.getRow(25);
			        cell = row.getCell(5);
			        cell.setCellValue((String) str.substring(11, 16));
			        
		        }
		        
		        if(org.getStatedir()==null){
		        	 row = sheet.getRow(26);
				        cell = row.getCell(5);
				        cell.setCellValue((String) "Мэдээлэл байхгүй");
				        
				}
		        else{
		        	String check= org.getStatedir().substring(0, 1);
		        	//System.out.println("0000000000,"+check+",");
		        	if(check.equalsIgnoreCase(scheck)){
		        		//System.out.println("555555");
		        		String Banks = "{a1:"+org.getStatedir()+"}";					
						JSONObject banksobj = new JSONObject(Banks);
		        		JSONArray array= banksobj.getJSONArray("a1");
						
						if(array.length()>0){
							String xbanks="";
							for(int a=0;a<array.length();a++){
								 JSONObject lao= array.getJSONObject(a);
								 xbanks= xbanks+lao.getString("sdirection")+", ";			
							}
							row = sheet.getRow(26);
					        cell = row.getCell(5);
					        cell.setCellValue((String) xbanks);
						}
		        	}		        	
					else{
						row = sheet.getRow(26);
				        cell = row.getCell(5);
				        cell.setCellValue((String) org.getStatebanks());
					}
				}
		        
		        if(org.getOwndir()==null){
		        	 row = sheet.getRow(27);
				        cell = row.getCell(5);
				        cell.setCellValue((String) "Мэдээлэл байхгүй");
				        
				}
		        else{
		        	String check= org.getOwndir().substring(0, 1);
		        	//System.out.println("0000000000,"+check+",");
		        	if(check.equalsIgnoreCase(scheck)){
		        		String Banks = "{a1:"+org.getOwndir()+"}";					
						JSONObject banksobj = new JSONObject(Banks);
		        		JSONArray array= banksobj.getJSONArray("a1");
						
						if(array.length()>0){
							String xbanks="";
							for(int a=0;a<array.length();a++){
								 JSONObject lao= array.getJSONObject(a);
								 xbanks= xbanks+lao.getString("xdirection")+", ";			
							}
							row = sheet.getRow(27);
					        cell = row.getCell(5);
					        cell.setCellValue((String) xbanks);
						}
		        	}		        	
					else{
						row = sheet.getRow(27);
				        cell = row.getCell(5);
				        cell.setCellValue((String) org.getOwndir());
					}
				}
		        
		        row = sheet.getRow(28);
		        cell = row.getCell(5);
		        cell.setCellValue(org.getHeadsurname());
		        
		        row = sheet.getRow(29);
		        cell = row.getCell(5);
		        cell.setCellValue(org.getHeadfullname());
		        
		        row = sheet.getRow(30);
		        cell = row.getCell(5);
		        cell.setCellValue(org.getHeadpos());
		             
		        row = sheet.getRow(31);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getHeadphone());
		        
		        row = sheet.getRow(32);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getHeademail());
		        
		        row = sheet.getRow(33);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getHeadwyear());
		        
		        row = sheet.getRow(34);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getHeadprof());		              
		        
		        row = sheet.getRow(35);
		        cell = row.getCell(5);
		        cell.setCellValue(org.getAccsurname());
		        
		        row = sheet.getRow(36);
		        cell = row.getCell(5);
		        cell.setCellValue(org.getAccfullname());
		        
		        row = sheet.getRow(37);
		        cell = row.getCell(5);
		        cell.setCellValue(org.getAccpos());
		             
		        row = sheet.getRow(38);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getAccphone());
		        
		        row = sheet.getRow(39);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getAccemail());
		        
		        row = sheet.getRow(40);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getAccwyear());
		        
		        row = sheet.getRow(41);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getAccprof());
		        
		        row = sheet.getRow(42);
		        cell = row.getCell(4);
		        cell.setCellValue((int) year-3);
		        
		        row = sheet.getRow(42);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getPlan1());
		        
		        row = sheet.getRow(43);
		        cell = row.getCell(4);
		        cell.setCellValue((int) year-2);
		        
		        row = sheet.getRow(43);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getPlan2());
		        
		        row = sheet.getRow(44);
		        cell = row.getCell(4);
		        cell.setCellValue((int) year-1);
		        
		        row = sheet.getRow(44);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getPlan3());
		        
		        row = sheet.getRow(45);
		        cell = row.getCell(4);
		        cell.setCellValue((int) year-3);
		        
		        row = sheet.getRow(45);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getReport1());
		        
		        row = sheet.getRow(46);
		        cell = row.getCell(4);
		        cell.setCellValue((int) year-2);
		        
		        row = sheet.getRow(46);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getReport2());
		        
		        row = sheet.getRow(47);
		        cell = row.getCell(4);
		        cell.setCellValue((int) year-1);
		        
		        row = sheet.getRow(47);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getReport3());
		        
		        row = sheet.getRow(48);
		        cell = row.getCell(3);
		        cell.setCellValue((int) year-3);
		        
		        row = sheet.getRow(54);
		        cell = row.getCell(3);
		        cell.setCellValue((int) year-2);
		        
		        row = sheet.getRow(60);
		        cell = row.getCell(3);
		        cell.setCellValue((int) year-1);
		        
		        int n1=(int) org.getAuditresult1();
		        if(n1>0){
		        	 row = sheet.getRow(47+n1);
				        cell = row.getCell(5);
				        cell.setCellValue((String) "Тийм");
		        }
		        
		        int n2=(int) org.getAuditresult2();
		        if(n2>0){
		        	 row = sheet.getRow(53+n2);
				        cell = row.getCell(5);
				        cell.setCellValue((String) "Тийм");
		        }
		        
		        int n3=(int) org.getAuditresult3();
		        if(n3>0){
		        	 row = sheet.getRow(59+n3);
				        cell = row.getCell(5);
				        cell.setCellValue((String) "Тийм");
		        }
		        int c1=0;
		        if(org.getFincategoryid()==400){
		        	c1=1;
		        }
		        if(org.getFincategoryid()==401){
		        	c1=2;
		        }
		        if(org.getFincategoryid()==403){
		        	c1=3;
		        }
		        if(org.getFincategoryid()==404){
		        	c1=4;
		        }
		        if(org.getFincategoryid()==405){
		        	c1=5;
		        }
		        if(org.getFincategoryid()==406){
		        	c1=6;
		        }
		        if(org.getFincategoryid()==402){
		        	c1=7;
		        }
		        if(org.getFincategoryid()==43){
		        	c1=8;
		        }
		        if(org.getFincategoryid()==44){
		        	c1=9;
		        }
		        if(org.getFincategoryid()==45){
		        	c1=10;
		        }
		        if(c1>0){
		        row = sheet.getRow(65+c1);
		        cell = row.getCell(5);
		        cell.setCellValue((String) "Тийм");	    
		        }
		        
		        int c2=0;
		        if(org.getCategoryid()==11){
		        	c2=1;
		        }
		        if(org.getCategoryid()==12){
		        	c2=2;
		        }
		        if(org.getCategoryid()==13){
		        	c2=3;
		        }
		        if(org.getCategoryid()==14){
		        	c2=4;
		        }
		        if(org.getCategoryid()==15){
		        	c2=5;
		        }
		        if(org.getCategoryid()==16){
		        	c2=6;
		        }
		        if(org.getCategoryid()==46){
		        	c2=7;
		        }
		        if(org.getCategoryid()==47){
		        	c2=8;
		        }
		        if(org.getCategoryid()==48){
		        	c2=9;
		        }
		        if(org.getCategoryid()==49){
		        	c2=10;
		        }
		        if(org.getCategoryid()==78){
		        	c2=11;
		        }
		        if(org.getCategoryid()==79){
		        	c2=12;
		        }
		        if(org.getCategoryid()==80){
		        	c2=13;
		        }
		        if(org.getCategoryid()==81){
		        	c2=14;
		        }
		        
		        if(c2>0){
		        	row = sheet.getRow(75+c2);
			        cell = row.getCell(5);
			        cell.setCellValue((String) "Тийм");
		        }
		        else{}
		        
		        int c3=0;
		        if(org.getProgid()==42){
		        	c3=1;
		        }
		        if(org.getProgid()==50){
		        	c3=2;
		        }
		        if(org.getProgid()==51){
		        	c3=3;
		        }
		        if(org.getProgid()==52){
		        	c3=4;
		        }
		        if(org.getProgid()==61){
		        	c3=5;
		        }
		        if(org.getProgid()==62){
		        	c3=6;
		        }
		        if(org.getProgid()==64){
		        	c3=7;
		        }
		        if(org.getProgid()==65){
		        	c3=8;
		        }
		        if(org.getProgid()==66){
		        	c3=9;
		        }
		        if(org.getProgid()==67){
		        	c3=10;
		        }
		        if(org.getProgid()==68){
		        	c3=11;
		        }
		        if(org.getProgid()==69){
		        	c3=12;
		        }
		        if(org.getProgid()==70){
		        	c3=13;
		        }
		        if(org.getProgid()==71){
		        	c3=14;
		        }
		        if(org.getProgid()==72){
		        	c3=15;
		        }
		        if(org.getProgid()==73){
		        	c3=16;
		        }
		        if(org.getProgid()==74){
		        	c3=17;
		        }
		        if(org.getProgid()==75){
		        	c3=18;
		        }
		        if(org.getProgid()==76){
		        	c3=19;
		        }
		        if(org.getProgid()==77){
		        	c3=20;
		        }
		        
		        if(c3>0){
		        	
		        	row = sheet.getRow(89+c3);
			        cell = row.getCell(5);
			        cell.setCellValue((String) "Тийм");
		        }
		        else{}
		        System.out.println("xxxx "+c1+" "+c2+" "+c3+" xxxxxxxxx");
		        /*row = sheet.getRow(48);
		        cell = row.getCell(5);
		        cell.setCellValue((int) org.getAuditresult1());
		        
		        row = sheet.getRow(49);
		        cell = row.getCell(5);
		        cell.setCellValue((int) org.getAuditresult2());
		        
		        row = sheet.getRow(50);
		        cell = row.getCell(5);
		        cell.setCellValue((int) org.getAuditresult3());
		        
		        row = sheet.getRow(51);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getLutFincategory().getFincategoryname());
		        
		        row = sheet.getRow(52);
		        cell = row.getCell(5);
		        cell.setCellValue((String) org.getLutCategory().getCategoryname());
		        		        
		        row = sheet.getRow(53);
		        cell = row.getCell(5);
		        LutExpProgcategory progid = (LutExpProgcategory) dao.getHQLResult("from LutExpProgcategory t where t.id='"+org.getProgid()+"'", "current");
		        cell.setCellValue((String) progid.getProgname());*/
		        
		        // ajillagsdiin too
			        int a= 0;
			        int b= 0;
			        int c= 0;
			        int d= 0;
			        int e= 0;
		        if(org.getHeadwnum()==null){a=0;}
		        else{
		        	String na= org.getHeadwnum().replaceAll("[^0-9]", "");
		        	if(na.isEmpty()){a=0;}
		        	else{a= Integer.parseInt(na);}
		        	}
		        if(org.getConwnum()==null){b=0;}
		        else{
		        	String nb= org.getConwnum().replaceAll("[^0-9]", "");
		        	if(nb.isEmpty()){b=0;}
		        	else{b= Integer.parseInt(nb);}
		        	}
		        if(org.getComwnum()==null){c=0;}
		        else{
		        	String nc= org.getComwnum().replaceAll("[^0-9]", "");
		        	if(nc.isEmpty()){c=0;}
		        	else{c= Integer.parseInt(nc);}
		        	}
		        if(org.getSerwnum()==null){d=0;}
		        else{
		        	String nd= org.getSerwnum().replaceAll("[^0-9]", "");
		        	if(nd.isEmpty()){d=0;}
		        	else{d= Integer.parseInt(nd);}
		        	}
		        if(org.getOtherwnum()==null){e=0;}
		        else{
		        	String ne= org.getOtherwnum().replaceAll("[^0-9]", "");
		        	if(ne.isEmpty()){e=0;}
		        	else{e= Integer.parseInt(ne);}
		        	}
		        
		        row = sheet.getRow(110);
		        cell = row.getCell(5);
		        cell.setCellValue((int) a);
		        
		        row = sheet.getRow(111);
		        cell = row.getCell(5);
		        cell.setCellValue((int) c);
		        
		        row = sheet.getRow(112);
		        cell = row.getCell(5);
		        cell.setCellValue((int) d);
		        
		        row = sheet.getRow(113);
		        cell = row.getCell(5);
		        cell.setCellValue((int) b);
		        
		        row = sheet.getRow(114);
		        cell = row.getCell(5);
		        cell.setCellValue((int) e);		     
			}
			 for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
	                Sheet tmpSheet =workbook.getSheetAt(i);
	                if(!tmpSheet.getSheetName().equals(sheetname)){
	                	if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
	                		workbook.removeSheetAt(i);
	                	}
	                }
	            } 
			  
			String xname=org.getOrgname().trim()+"-"+sheetname;
			xname = URLEncoder.encode(xname,"UTF-8"); 
	        try (ServletOutputStream outputStream = response.getOutputStream()) {
				response.setContentType("application/ms-excel; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
	            response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
	            workbook.write(outputStream);
	            outputStream.close();
	        }
			
	        catch (Exception e) {
	        	System.out.println("ishe orov");
				// TODO: handle exception
			}
		}
	}
    
    @RequestMapping(value="/exportMateriallag/{id}/{stepid}/{furl}",method=RequestMethod.GET)
    public void exportMateriallag(@PathVariable long id,@PathVariable long stepid,@PathVariable String furl, HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {
    

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			String appPath = req.getServletContext().getRealPath("");	
			File files = new File(appPath+"/assets/zagvarfile/working_papers-В-STAUS-II.xlsx");
			 
			fis = new FileInputStream(files);
        	Workbook workbook = new XSSFWorkbook(fis);
        	
			XSSFSheet sheet = null;
			
			for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
                XSSFSheet tmpSheet =(XSSFSheet) workbook.getSheetAt(i);
                if(!tmpSheet.getSheetName().equals(furl)){
                	if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
                		workbook.removeSheetAt(i);
                	}
                }
                else{
                	sheet = tmpSheet;
                }
			}
			
			MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
			SubAuditOrganization org=null;
			if(main.getOrgcode()!=null){
				org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
			}
			else{
				org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
			}
			//SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
			if(sheet!=null){
				System.out.println("sheet oldson");
				
				Row row = sheet.getRow(5);
		        Cell cell = row.getCell(3);
		        cell.setCellValue((String) main.getOrgname());
		        
		  /*      DataFormat df = workbook.createDataFormat(); 
		        short dataFormat = df.getFormat("0.00");
		        CellStyle style = workbook.createCellStyle(); 
		        style.setDataFormat(dataFormat);*/
		        
		        CellStyle cellStyle = row.getCell(4).getCellStyle();
		        cellStyle.setDataFormat(
        		workbook.getCreationHelper().createDataFormat().getFormat("#,##0.0"));
		        
		        row = sheet.getRow(7);
		        cell = row.getCell(3);
		        cell.setCellValue((String) main.getGencode());
		        
		        List<FsFormA4> a4List=  (List<FsFormA4>) dao.getHQLResult("from FsFormA4 t where t.planid="+id+" and t.stepid="+stepid+"", "list");
		        FsFormA4 selected = null;
		        for(int i=0;i<a4List.size();i++){
		        	if (a4List.get(i).getIsselect() == 1){
		        		selected = a4List.get(i);
		        	}
		        	if(a4List.get(i).getData1().equalsIgnoreCase("1")){
		        		row = sheet.getRow(14);
		 		        cell = row.getCell(2);
		        		cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()));
		        		cell.setCellStyle(cellStyle);
			        	cell = row.getCell(3);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData3()));
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(4);
				        /*cell.setCellValue((String) a4List.get(i).getData4());*/
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()) * 0.01);
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(5);
				        cell.setCellValue((String) a4List.get(0).getData5());
				        cell.setCellStyle(cellStyle);
		        	}
		       
			        if(a4List.get(i).getData1().equalsIgnoreCase("2")){
			        	row = sheet.getRow(15);
					    cell = row.getCell(2);
			        	cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()));
			        	 cell.setCellStyle(cellStyle);
			        	cell = row.getCell(3);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData3()));
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(4);
				        /*cell.setCellValue((String) a4List.get(i).getData4());*/
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()) * 0.01);
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(5);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData5()));
				        cell.setCellStyle(cellStyle);
			        }
			        
			        if(a4List.get(i).getData1().equalsIgnoreCase("3")){
			        	row = sheet.getRow(16);
				        cell = row.getCell(2);
			        	cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()));
			        	 cell.setCellStyle(cellStyle);
			        	cell = row.getCell(3);
				        cell.setCellValue((String) a4List.get(i).getData3());
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(4);
				        /*cell.setCellValue((String) a4List.get(i).getData4());*/
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()) * 0.01);
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(5);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData5()));
				        cell.setCellStyle(cellStyle);
			        }
			      
			        if(a4List.get(i).getData1().equalsIgnoreCase("4")){
			        	row = sheet.getRow(17);
				        cell = row.getCell(2);
			        	cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()));
			        	 cell.setCellStyle(cellStyle);
			        	cell = row.getCell(3);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData3()));
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(4);
				        /*cell.setCellValue((String) a4List.get(i).getData4());*/
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()) * 0.01);
				      
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(5);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData5()));
				        cell.setCellStyle(cellStyle);
			        }
			        if(a4List.get(i).getIsselect()==1){
			        	row = sheet.getRow(24);
			        	
			        	cell = row.getCell(1);
			        	cell.setCellValue((selected.getData1().equals("1")) ? "Нийт орлого" : (selected.getData1().equals("2")) ? "Нийт зардал" : (selected.getData1().equals("3")) ? "Нийт хөрөнгө" : (selected.getData1().equals("4")) ? "Цэвэр хөрөнгө" : "");
			        	 cell.setCellStyle(cellStyle);
			        	cell = row.getCell(2);
			        	cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()));
			        	 cell.setCellStyle(cellStyle);
			        	cell = row.getCell(3);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData3()));
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(4);
				        /*cell.setCellValue((String) a4List.get(i).getData4());*/
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData2()) * 0.01);
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(5);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData5()));
				        cell.setCellStyle(cellStyle);
				        cell = row.getCell(6);
				        cell.setCellValue(Double.parseDouble(a4List.get(i).getData4()));
				        cell.setCellStyle(cellStyle);
				        row = sheet.getRow(20);
				        cell = row.getCell(5);
			        	cell.setCellValue((String) a4List.get(i).getPercent()+"%");
			        	 cell.setCellStyle(cellStyle);
			        	/*row = sheet.getRow(23);
				        cell = row.getCell(4);
			        	cell.setCellValue((String) a4List.get(i).getPercent()+"%");*/
			        	
			        	row = sheet.getRow(28);
				        cell = row.getCell(2);
			        	cell.setCellValue((String) selected.getDescription1());
			        	row = sheet.getRow(30);
				        cell = row.getCell(2);
			        	cell.setCellValue((String) selected.getDescription2());
			        	row = sheet.getRow(32);
				        cell = row.getCell(2);
			        	cell.setCellValue((String) selected.getDescription3());
			        	
			        }
				}
		        /*if (selected != null){
		        	row = sheet.getRow(24);
			        cell = row.getCell(1);
		        	cell.setCellValue((selected.getData1().equals("1")) ? "Нийт орлого" : (selected.getData1().equals("2")) ? "Нийт зардал" : (selected.getData1().equals("3")) ? "Нийт хөрөнгө" : (selected.getData1().equals("4")) ? "Цэвэр хөрөнгө" : "");
		        	
		        	cell = row.getCell(2);
		        	cell.setCellValue((String) selected.getData2());
		        	
		        	cell = row.getCell(3);
		        	cell.setCellValue((String) selected.getData3());
		        	
		        	cell = row.getCell(4);
		        	cell.setCellValue((String) selected.getData4());
		        	
		        	
		        }*/
		        String xname=main.getOrgname().trim()+"-"+furl;
				xname = URLEncoder.encode(xname,"UTF-8"); 
		        try (ServletOutputStream outputStream = response.getOutputStream()) {
	  				response.setContentType("application/ms-excel; charset=UTF-8");
	  				response.setCharacterEncoding("UTF-8");
	  	            response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
	  	            workbook.write(outputStream);
	  	            outputStream.close();
	  	        }
	  	        catch (Exception e) {
	  	        	System.out.println("ishe orov");
	  				// TODO: handle exception
	  			}
			}
	       
			
		}
	 }
    
    @RequestMapping(value="/addFactor",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String addFactor(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			 
			 if(obj.getLong("id")==0){
				 LutFactor aw = new LutFactor();
				 if(obj.has("factorname")){
		    		 aw.setFactorname(obj.getString("factorname"));
		    	 }
				 if(obj.has("orderid")){
		    		 aw.setOrderid(obj.getInt("orderid"));
		    	 }
				 if(obj.has("groupid")){
		    		 aw.setGroupid(obj.getLong("groupid"));
		    		 LutGroupOfFactor gf =  (LutGroupOfFactor) dao.getHQLResult("from LutGroupOfFactor t where t.id="+obj.getLong("groupid")+"", "current");
		    		 aw.setGorderid(gf.getOrderid());
		    	 }
				 if(obj.has("dirid")){
		    		 aw.setDirid(obj.getLong("dirid"));
		    	 }
				 if(obj.has("fnumber")){
		    		 aw.setFnumber(obj.getString("fnumber"));
		    	 }
				 if(obj.has("riskid")){
		    		 aw.setRiskid(obj.getLong("riskid"));
		    	 }
				 
				 if(obj.has("tryid")){
		    		 aw.setTryid(obj.getLong("tryid"));
		    	 }
				 
				 if(obj.has("isactive")){
		    		 aw.setIsactive(obj.getLong("isactive"));
		    	 }
				 aw.setIsactive(1);
				 
				 JSONArray arrcrit = obj.getJSONArray("crits");
				 String str="";
			 	 for(int k=0;k<arrcrit.length();k++ ){
			 		 JSONObject critObj= (JSONObject) arrcrit.get(k);
			 		 str=str+", "+critObj.getString("name");
			 	 }
			 	 
			 	 if(str.length()>0){
			 		 aw.setCrition(str.substring(1, str.length()));
			 	 }

				 dao.PeaceCrud(aw, "LutFactor", "save", (long) 0, 0, 0, null);
				 
				 if(obj.has("crits")){
					 JSONArray arr = obj.getJSONArray("crits");
				 	 for(int k=0;k<arr.length();k++ ){
				 		 JSONObject critObj= (JSONObject) arr.get(k);
				 		 LutFactorCriterion fc = new LutFactorCriterion();
				 		 fc.setFactorid(aw.getId());
				 		 fc.setName(critObj.getString("name"));
				 		 dao.PeaceCrud(fc, "LutFactorCriterion", "save", (long) 0, 0, 0, null);
				 	 }
				 }
				 if(obj.has("catid")){
						JSONArray arr = obj.getJSONArray("catid");
					 	 for(int k=0;k<arr.length();k++ ){
							 LnkFactorCategory wc = new LnkFactorCategory();
							 wc.setCatid(arr.getLong(k));
							 wc.setFactorid(aw.getId());
							 dao.PeaceCrud(wc, "LnkFactorCategory", "save", (long) 0, 0, 0, null);
						 }
					 }
				 obj1.put("success", "success");
			 }
			 else{
				 LutFactor aw=(LutFactor) dao.getHQLResult("from LutFactor t where t.id='"+obj.getLong("id")+"'", "current");
				 if(obj.has("factorname")){
		    		 aw.setFactorname(obj.getString("factorname"));
		    	 }
				 if(obj.has("orderid")){
		    		 aw.setOrderid(obj.getInt("orderid"));
		    	 }
				 if(obj.has("groupid")){
					 aw.setGroupid(obj.getLong("groupid"));
		    		 LutGroupOfFactor gf =  (LutGroupOfFactor) dao.getHQLResult("from LutGroupOfFactor t where t.id="+obj.getLong("groupid")+"", "current");
		    		 aw.setGorderid(gf.getOrderid());
		    	 }
				 if(obj.has("dirid")){
		    		 aw.setDirid(obj.getLong("dirid"));
		    	 }
				 if(obj.has("fnumber")){
		    		 aw.setFnumber(obj.getString("fnumber"));
		    	 }
				 if(obj.has("riskid")){
		    		 aw.setRiskid(obj.getLong("riskid"));
		    	 }
				 if(obj.has("tryid")){
		    		 aw.setTryid(obj.getLong("tryid"));
		    	 }
				 
				 if(obj.has("isactive")){
		    		 aw.setIsactive(obj.getLong("isactive"));
		    	 }
				 JSONArray arrcrit = obj.getJSONArray("crits");
				 String str="";
			 	 for(int k=0;k<arrcrit.length();k++ ){
			 		 JSONObject critObj= (JSONObject) arrcrit.get(k);
			 		 str=str+", "+critObj.getString("name");
			 	 }
			 	 
			 	 if(str.length()>0){
			 		 aw.setCrition(str.substring(1, str.length()));
			 	 }
			 	 
				 dao.PeaceCrud(aw, "LutFactor", "update",(long) obj.getLong("id"), 0, 0, null);
				 
				 dao.PeaceCrud(null, "LnkFactorCategory", "delete", (long) obj.getLong("id"), 0, 0, "factorid");
			     if(obj.has("catid")){
						JSONArray arr = obj.getJSONArray("catid");
					 	 for(int k=0;k<arr.length();k++ ){
					 		LnkFactorCategory wc = new LnkFactorCategory();
							 wc.setCatid(arr.getLong(k));
							 wc.setFactorid(obj.getLong("id"));
							 dao.PeaceCrud(wc, "LnkFactorCategory", "save", (long) 0, 0, 0, null);
						 }
					 }
				
				 if(obj.has("crits")){
						JSONArray arr = obj.getJSONArray("crits");
					 	 for(int k=0;k<arr.length();k++ ){
					 		 JSONObject critObj= (JSONObject) arr.get(k);
					 		 
					 		 if(critObj.has("saved")){
					 			LutFactorCriterion fc=  (LutFactorCriterion) dao.getHQLResult("from LutFactorCriterion t where t.id="+critObj.getLong("id")+"", "current");
					 			fc.setName(critObj.getString("name"));
					 			dao.PeaceCrud(fc, "LutFactorCriterion", "update",(long) critObj.getLong("id"), 0, 0, null);
					 		 }
					 		 else{
					 			 LutFactorCriterion fc = new LutFactorCriterion();
					 			 fc.setFactorid(aw.getId());
						 		 fc.setName(critObj.getString("name"));
						 		 dao.PeaceCrud(fc, "LutFactorCriterion", "save", (long) 0, 0, 0, null);
					 		 }
					 		 
					 	 }
					 }
				 if(obj.has("deleteCrit")){
						JSONArray arr = obj.getJSONArray("deleteCrit");
					 	 for(int k=0;k<arr.length();k++ ){
					 		 JSONObject critObj= (JSONObject) arr.get(k);
					 		 LutFactorCriterion fc=  (LutFactorCriterion) dao.getHQLResult("from LutFactorCriterion t where t.id="+critObj.getLong("id")+"", "current");
					 		dao.PeaceCrud(fc, "LutFactorCriterion", "delete", (long) fc.getId(), 0, 0, null);
					 	 }
					 }
				 
				 
				 obj1.put("success", "success");
			 }
			 return obj1.toString();
		}
		else{
			 return "false";
		}
	}
    
    @RequestMapping(value="/uploadfromexcel",method=RequestMethod.POST)
	public @ResponseBody String uploadfromexcel( @RequestParam("files") MultipartFile files,@RequestParam("obj") String jsonstring, MultipartRequest mpr, HttpServletRequest req) throws JSONException, DocumentException, Exception, IOException  {
    

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			MultipartFile mfile =  null;
		  	mfile = (MultipartFile)files;
		  	int count=0;
            JSONObject arr= new JSONObject();
            JSONObject obj= new JSONObject(jsonstring);
            String appPath = req.getSession().getServletContext().getRealPath("");	
			String SAVE_DIR =appPath+"/" +IzrApplication.ROOT + "/" +loguser.getLutDepartment().getShortname().trim()+"/"+loguser.getUsername();
	        FileInputStream fis = null;
	        if (mfile != null) {
	        	
	        	FsLutFormType ft = (FsLutFormType) dao.getHQLResult("from FsLutFormType t where t.id='"+obj.getLong("formid")+"'", "current");
	        	String path = SAVE_DIR+ File.separator  + ft.getFormname();
	        	
	        	File folder = new File(path);
        	    if(!folder.exists()){
        	    	System.out.println("uussen");
        	     	folder.mkdirs();
    			}
	        	
        	    String fpath = SAVE_DIR + "/" + ft.getFormname() + "/" + mfile.getOriginalFilename();
	        	File logoorgpath = new File(fpath);
	        	mfile.transferTo(logoorgpath);
	            fis = new FileInputStream(logoorgpath);
	        	Workbook workbook = new XSSFWorkbook(fis);
	           
	            int numberOfSheets = workbook.getNumberOfSheets();
	            for (int i = 0; i < numberOfSheets; i++) {
	 	           Sheet sheet = workbook.getSheetAt(i);
	 	           
	 	           if((obj.getLong("formid")==2) && (sheet.getSheetName().equalsIgnoreCase( "A1" ))){
	 	                System.out.println("first");
	 	                System.out.println(sheet.getFirstRowNum());
	 	                System.out.println("last");
	 	                System.out.println(sheet.getLastRowNum());
	 	               
	 	                here: for(int k=3; k <= sheet.getLastRowNum();k++){
	 	                	System.out.println(k);
	 	                	if(k==10 || k==16 || k==17){
	 	                		  System.out.println("continue");
	 	            	    	  continue here;
	 	            	      }
	 	                Row row = sheet.getRow(k);
	 	            	try {
	 	            	      	FsFormA1 form = new FsFormA1();
		 		                Iterator cellIterator = row.cellIterator();
	 		                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	 		                    while (cellIterator.hasNext()) {
	 		                        Cell cell = (Cell) cellIterator.next();
	 		                        switch (evaluator.evaluateInCell(cell).getCellType()) 
	 		                        {
	 		                            case Cell.CELL_TYPE_STRING:
	 		                                System.out.println("STRING");
	 		                                if (cell.getColumnIndex() == 1) {
	 		                            		form.setRecid(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 2) {
	 		                            		form.setData1(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 3) {
	 		                            		form.setData2(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 4) {
	 		                            		form.setData3(cell.getStringCellValue());
	 		                            	}
	 		                                break;
	 		                            case Cell.CELL_TYPE_NUMERIC:
	 		                            	System.out.println("NUMERIC");
	 		                            	if (cell.getColumnIndex() == 1) {
	 		                            		form.setRecid(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 2) {
	 		                            		form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 3) {
	 		                            		form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 4) {
	 		                            		form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                                break;
	 		                            case Cell.CELL_TYPE_BLANK:
	 		                            	if (cell.getColumnIndex() == 1) {
	 		                            		arr.put("count",count);
	 		  	 	            	    	 	arr.put("response",true);
	 		  	 	            	    	 	return arr.toString();
	 		                            	}
	 		                                break;
	 		                        }
	 		                    }
	 		                    form.setFormid(obj.getLong("formid"));
	 		                    //form.setAuditid(obj.getLong("auditid"));
	 		                    dao.PeaceCrud(form, "FsFormA1", "save", (long) 0, 0, 0, null);
	 		                    count = count + 1;
	 			       		    
	 	            	     }  catch (Exception e) {
	 	            	    	 arr.put("count",count-1);
	 	            	    	 arr.put("response",true);
	 	            	    	 return arr.toString();
	 	            	     }
	 	                }
	 	            }
	 	           else if((obj.getLong("formid")==3) && (sheet.getSheetName().equalsIgnoreCase( "A2" ))){
	 	                
	 	                System.out.println("first");
	 	                System.out.println(sheet.getFirstRowNum());
	 	                System.out.println("last");
	 	                System.out.println(sheet.getLastRowNum());
	 	               
	 	                for(int k=7; k <= sheet.getLastRowNum();k++){
	 	                	
	 	                	Row row = sheet.getRow(k);
	 	                	try {
	 	            	      	FsFormA2 form2 = new FsFormA2();
	 		                 
	 		                    Iterator cellIterator = row.cellIterator();
	 		                    
	 		                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	 		                    //Iterating over each cell (column wise)  in a particular row.
	 		                    while (cellIterator.hasNext()) {

	 		                        Cell cell = (Cell) cellIterator.next();
	 		                        
	 		                        switch (evaluator.evaluateInCell(cell).getCellType()) 
	 		                        {
	 		                            case Cell.CELL_TYPE_STRING:
	 		                                System.out.println("STRING");
	 		                                if (cell.getColumnIndex() == 1) {
	 		                                	form2.setRecnum(cell.getStringCellValue());
	 		                            		
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 2) {
	 		                            		form2.setData1(cell.getStringCellValue());
	 		                            		
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 9) {
	 		                            		form2.setData2(cell.getStringCellValue());
	 		                            		
	 		                            	}
	 		                            	
	 		                                break;
	 		                            case Cell.CELL_TYPE_NUMERIC:
	 		                            	System.out.println("NUMERIC");
	 		                            	if (cell.getColumnIndex() == 1) {
	 		                            		form2.setRecnum(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            		
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 2) {
	 		                            		form2.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            		
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 9) {
	 		                            		form2.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            		
	 		                            	}
	 		                            	
	 		                            	System.out.print(NumberToTextConverter.toText(cell.getNumericCellValue()) + "tt");
	 		                                break;
	 		                            case Cell.CELL_TYPE_BLANK:
	 		                            	if (cell.getColumnIndex() == 1) {
	 		                            		arr.put("count",count);
	 		  	 	            	    	 	arr.put("response",true);
	 		  	 	            	    	 	return arr.toString();
	 		                            	}
	 		                                break;
	 		                        }
	 		                    }
	 		                    form2.setFormid(obj.getLong("formid"));
	 		                    //form2.setAuditid(obj.getLong("auditid"));
	 		                    dao.PeaceCrud(form2, "FsFormA2", "save", (long) 0, 0, 0, null);
	 		                    count = count + 1;
	 	            	     }  catch (Exception e) {
	 	            	    	 arr.put("count",count-1);
	 	            	    	 arr.put("response",true);
	 	            	    	 return arr.toString();
	 	            	     }
	 	                }
	 	            }
	 	           else if((obj.getLong("formid")==4) && (sheet.getSheetName().equalsIgnoreCase( "A3" ))){
	 	               
	 	        	   	System.out.println("first");
	 	                System.out.println(sheet.getFirstRowNum());
	 	                System.out.println("last");
	 	                System.out.println(sheet.getLastRowNum());
	 	               
	 	                for(int k=6; k <= sheet.getLastRowNum();k++){
	 	                	Row row = sheet.getRow(k);
	 	                	try {
	 	            	      	FsFormA3 form3 = new FsFormA3();
	 		                    Iterator cellIterator = row.cellIterator();
	 		                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	 		                    
	 		                    while (cellIterator.hasNext()) {
	 		                    	Cell cell = (Cell) cellIterator.next();
	 		                        switch (evaluator.evaluateInCell(cell).getCellType()) 
	 		                        {
	 		                            case Cell.CELL_TYPE_STRING:
	 		                                System.out.println("STRING");
	 		                                if (cell.getColumnIndex() == 1) {
	 		                                	form3.setRecnum(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 2) {
	 		                            		form3.setData1(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 3) {
	 		                            		form3.setData2(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 4) {
	 		                            		form3.setData3(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 5) {
	 		                            		form3.setData4(cell.getStringCellValue());
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 6) {
	 		                            		form3.setData5(cell.getStringCellValue());
	 		                            	}
	 		                                break;
	 		                            case Cell.CELL_TYPE_NUMERIC:
	 		                            	System.out.println("NUMERIC");
	 		                            	if (cell.getColumnIndex() == 1) {
	 		                            		form3.setRecnum(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 2) {
	 		                            		form3.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 3) {
	 		                            		form3.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 4) {
	 		                            		form3.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 5) {
	 		                            		form3.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                            	if (cell.getColumnIndex() == 6) {
	 		                            		form3.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
	 		                            	}
	 		                                break;
	 		                            case Cell.CELL_TYPE_BLANK:
	 		                            	if (cell.getColumnIndex() == 1) {
	 		                            		arr.put("count",count);
	 		  	 	            	    	 	arr.put("response",true);
	 		  	 	            	    	 	return arr.toString();
	 		                            	}
	 		                                break;
	 		                        }
	 		                    }
	 		                    form3.setFormid(obj.getLong("formid"));
	 		                    //form2.setAuditid(obj.getLong("auditid"));
	 		                    dao.PeaceCrud(form3, "FsFormA3", "save", (long) 0, 0, 0, null);
	 		                    count = count + 1;
	 	                	 }  catch (Exception e) {
	 	            	    	 arr.put("count",count-1);
	 	            	    	 arr.put("response",true);
	 	            	    	 return arr.toString();
	 	            	     }
	 	                }
	 	            }
	        	}
	            fis.close();
	            arr.put("count",count);
   	    	 	arr.put("response",true);
		    	return arr.toString();
	        }
		}
		return "false";
	 }
    
    @RequestMapping(value="/removeAnswerComment",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public LutMainComment removeAnswerComment(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			
			 LutMainComment mc = (LutMainComment) dao.getHQLResult("from LutMainComment t where t.id='"+obj.getLong("id")+"'", "current");
			 
			 mc.setOffid("");
			 mc.setStatus(0);
			 mc.setAppdate("");
			 mc.setAnswer("");
			 dao.PeaceCrud(mc, "LutMainComment", "update",(long) obj.getLong("id"), 0, 0, null);
			 obj1.put("success", "success");
			 return mc;
		}
		else{
			 return null;
		}
		
	}
    
    @RequestMapping(value="/sendAnswerComment",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public LutMainComment sendAnswerComment(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			 
			 DateFormat dateFormat1 = new SimpleDateFormat("MM.dd.yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			
			 LutMainComment mc = (LutMainComment) dao.getHQLResult("from LutMainComment t where t.id='"+obj.getLong("id")+"'", "current");
			 
			 mc.setOffid(loguser.getGivenname());
			 if(obj.has("answer")){
					mc.setAnswer(obj.getString("answer"));
			 }
			 mc.setStatus(1);
			 mc.setAppdate(special);
			 dao.PeaceCrud(mc, "LutMainComment", "update",(long) obj.getLong("id"), 0, 0, null);
			 obj1.put("success", "success");
			
			 return mc;
		}
		else{
			 return null;
		}
		
	}
    
    @RequestMapping(value="/sendMainComment",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public LutMainComment sendMainComment(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			 
			 DateFormat dateFormat1 = new SimpleDateFormat("MM.dd.yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			
			 LutMainComment mc = new LutMainComment();
			 
			 mc.setUserid(loguser.getId());
			 mc.setUsername(loguser.getGivenname());
			 
			 if(obj.has("usermail")){
				mc.setUsermail(obj.getString("usermail"));
			 }
			 if(obj.has("title")){
					mc.setTitle(obj.getString("title"));
			 }
			 if(obj.has("text")){
					mc.setText(obj.getString("text"));
			 }
			 mc.setStatus(0);
			 mc.setSubdate(special);
			 dao.PeaceCrud(mc, "LutMainComment", "save", (long) 0, 0, 0, null);
			 obj1.put("success", mc);
			
			 return mc;
		}
		else{
			 return null;
		}
		
	}
    
    @ResponseBody 
   	@ResponseStatus(HttpStatus.OK)
   	@RequestMapping(value="/deleteTryOut/{id}", method = RequestMethod.DELETE,produces={"application/json; charset=UTF-8"})
   	public String deleteTryOut(@PathVariable long id, HttpServletRequest req) throws ClassNotFoundException, JSONException{

   		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   		if (!(auth instanceof AnonymousAuthenticationToken)) {
   			 LutTryout aw=(LutTryout) dao.getHQLResult("from LutTryout t where t.id="+id+"", "current");
   			 dao.PeaceCrud(aw, "LutTryout", "delete", (long) aw.getId(), 0, 0, null);
   			 return "true";
   		}
   		return "false";
   	}
    
    @RequestMapping(value="/updateTryOut",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public String updateTryOut(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			 
			 LutTryout lt=(LutTryout) dao.getHQLResult("from LutTryout t where t.id='"+obj.getLong("id")+"'", "current");
			
			 if(obj.has("adirid")){
				 lt.setAdirid(obj.getLong("adirid"));
			 }
			 if(obj.has("text")){
				 lt.setText(obj.getString("text"));
			 }
			 if(obj.has("formdesc")){
				 lt.setFormdesc(obj.getString("formdesc"));
			 }
			 
			 dao.PeaceCrud(lt, "LutTryout", "update",(long) obj.getLong("id"), 0, 0, null);
			 
			 dao.PeaceCrud(null, "LnkTryoutNotice", "delete", (long) lt.getId(), 0, 0, "tryoutid");
			 if(obj.has("notice")){
					JSONArray arr = obj.getJSONArray("notice");
				 	 for(int k=0;k<arr.length();k++ ){
				 		 if(arr.getLong(k)!=0){
				 			LnkTryoutNotice tn = new LnkTryoutNotice();
				 			tn.setNoticeid(arr.getLong(k));
				 			tn.setDirid(lt.getAdirid());
				 			tn.setTryoutid(lt.getId());
							dao.PeaceCrud(tn, "LnkTryoutNotice", "save", (long) 0, 0, 0, null);
				 		 }
					 }
				 }
			 dao.PeaceCrud(null, "LnkTryoutRisk", "delete", (long) lt.getId(), 0, 0, "tryoutid");
			 if(obj.has("risk")){
					JSONArray arr = obj.getJSONArray("risk");
				 	 for(int k=0;k<arr.length();k++ ){
				 		 if(arr.getLong(k)!=0){
				 			LnkTryoutRisk tr = new LnkTryoutRisk();
				 			tr.setRiskid(arr.getLong(k));
				 			tr.setDirid(lt.getAdirid());
				 			tr.setTryoutid(lt.getId());
							dao.PeaceCrud(tr, "LnkTryoutRisk", "save", (long) 0, 0, 0, null);
				 		 }
					 }
				 }
			 dao.PeaceCrud(null, "LnkTryoutProcedure", "delete", (long) lt.getId(), 0, 0, "tryoutid");
			 if(obj.has("procedure")){
					JSONArray arr = obj.getJSONArray("procedure");
				 	 for(int k=0;k<arr.length();k++ ){
				 		 if(arr.getLong(k)!=0){
				 			LnkTryoutProcedure tp = new LnkTryoutProcedure();
				 			tp.setProcedureid(arr.getLong(k));
				 			tp.setDirid(lt.getAdirid());
				 			tp.setTryoutid(lt.getId());
							dao.PeaceCrud(tp, "LnkTryoutProcedure", "save", (long) 0, 0, 0, null);
				 		 }
					 }
				 }
			 dao.PeaceCrud(null, "LnkTryoutCat", "delete", (long) lt.getId(), 0, 0, "tryoutid");
			 if(obj.has("workid")){
					JSONArray arr = obj.getJSONArray("workid");
				 	 for(int k=0;k<arr.length();k++ ){
				 		LnkTryoutCat tp = new LnkTryoutCat();
				 			tp.setCatid(arr.getLong(k));
				 			tp.setDirid(lt.getAdirid());
				 			tp.setTryoutid(lt.getId());
							dao.PeaceCrud(tp, "LnkTryoutCat", "save", (long) 0, 0, 0, null);
				 		 }
				 }
			 
			 dao.PeaceCrud(null, "LnkTryoutConfType", "delete", (long) lt.getId(), 0, 0, "tryid");
			 if(obj.has("conftype")){
				 JSONArray arr = obj.getJSONArray("conftype");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfType tp = new LnkTryoutConfType();
			 		tp.setTypeid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
					dao.PeaceCrud(tp, "LnkTryoutConfType", "save", (long) 0, 0, 0, null);
				 }
			 }
			 dao.PeaceCrud(null, "LnkTryoutConfMethod", "delete", (long) lt.getId(), 0, 0, "tryid");
			 if(obj.has("confMethod")){
				 JSONArray arr = obj.getJSONArray("confMethod");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfMethod tp = new LnkTryoutConfMethod();
			 		tp.setMethodid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
					dao.PeaceCrud(tp, "LnkTryoutConfMethod", "save", (long) 0, 0, 0, null);
				 }
			 }
			 dao.PeaceCrud(null, "LnkTryoutConfSource", "delete", (long) lt.getId(), 0, 0, "tryid");
			 if(obj.has("confSourceO")){
				 JSONArray arr = obj.getJSONArray("confSourceO");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfSource tp = new LnkTryoutConfSource();
			 		tp.setSourceid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
			 		tp.setTypeid(1);
					dao.PeaceCrud(tp, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
				 }
			 }
			 if(obj.has("confSourceI")){
				 JSONArray arr = obj.getJSONArray("confSourceI");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfSource tp = new LnkTryoutConfSource();
			 		tp.setSourceid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
			 		tp.setTypeid(0);
					dao.PeaceCrud(tp, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
				 }
			 }
			 
			 obj1.put("success", "success");
			 return obj1.toString();
		}
		else{
			 return "false";
		}
	}
    
    @RequestMapping(value="/addTryOut",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public String addTryOut(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			 
			 LutTryout lt = new LutTryout();
			 
			 if(obj.has("adirid")){
				 lt.setAdirid(obj.getLong("adirid"));
			 }
			 if(obj.has("text")){
				 lt.setText(obj.getString("text"));
			 }
			 if(obj.has("formdesc")){
				 lt.setFormdesc(obj.getString("formdesc"));
			 }
			 dao.PeaceCrud(lt, "LutTryout", "save", (long) 0, 0, 0, null);
			 
			 if(obj.has("notice")){
					JSONArray arr = obj.getJSONArray("notice");
				 	 for(int k=0;k<arr.length();k++ ){
				 		 if(arr.getLong(k)!=0){
				 			LnkTryoutNotice tn = new LnkTryoutNotice();
				 			tn.setNoticeid(arr.getLong(k));
				 			tn.setDirid(lt.getAdirid());
				 			tn.setTryoutid(lt.getId());
							dao.PeaceCrud(tn, "LnkTryoutNotice", "save", (long) 0, 0, 0, null);
				 		 }
					 }
				 }
			 if(obj.has("risk")){
					JSONArray arr = obj.getJSONArray("risk");
				 	 for(int k=0;k<arr.length();k++ ){
				 		 if(arr.getLong(k)!=0){
				 			LnkTryoutRisk tr = new LnkTryoutRisk();
				 			tr.setRiskid(arr.getLong(k));
				 			tr.setDirid(lt.getAdirid());
				 			tr.setTryoutid(lt.getId());
							dao.PeaceCrud(tr, "LnkTryoutRisk", "save", (long) 0, 0, 0, null);
				 		 }
					 }
				 }
			 if(obj.has("procedure")){
					JSONArray arr = obj.getJSONArray("procedure");
				 	 for(int k=0;k<arr.length();k++ ){
				 		 if(arr.getLong(k)!=0){
				 			LnkTryoutProcedure tp = new LnkTryoutProcedure();
				 			tp.setProcedureid(arr.getLong(k));
				 			tp.setDirid(lt.getAdirid());
				 			tp.setTryoutid(lt.getId());
							dao.PeaceCrud(tp, "LnkTryoutProcedure", "save", (long) 0, 0, 0, null);
				 		 }
					 }
				 }
			 if(obj.has("workid")){
				 JSONArray arr = obj.getJSONArray("workid");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutCat tp = new LnkTryoutCat();
			 		tp.setCatid(arr.getLong(k));
			 		tp.setDirid(lt.getAdirid());
			 		tp.setTryoutid(lt.getId());
					dao.PeaceCrud(tp, "LnkTryoutCat", "save", (long) 0, 0, 0, null);
				 }
			 }
			 if(obj.has("conftype")){
				 JSONArray arr = obj.getJSONArray("conftype");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfType tp = new LnkTryoutConfType();
			 		tp.setTypeid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
					dao.PeaceCrud(tp, "LnkTryoutConfType", "save", (long) 0, 0, 0, null);
				 }
			 }
			 if(obj.has("confMethod")){
				 JSONArray arr = obj.getJSONArray("confMethod");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfMethod tp = new LnkTryoutConfMethod();
			 		tp.setMethodid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
					dao.PeaceCrud(tp, "LnkTryoutConfMethod", "save", (long) 0, 0, 0, null);
				 }
			 }
			 if(obj.has("confSourceO")){
				 JSONArray arr = obj.getJSONArray("confSourceO");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfSource tp = new LnkTryoutConfSource();
			 		tp.setSourceid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
			 		tp.setTypeid(1);
					dao.PeaceCrud(tp, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
				 }
			 }
			 if(obj.has("confSourceI")){
				 JSONArray arr = obj.getJSONArray("confSourceI");
			 	 for(int k=0;k<arr.length();k++ ){
			 		LnkTryoutConfSource tp = new LnkTryoutConfSource();
			 		tp.setSourceid(arr.getLong(k));
			 		tp.setTryid(lt.getId());
			 		tp.setTypeid(0);
					dao.PeaceCrud(tp, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
				 }
			 }
			 
			 obj1.put("success", "success");
			 return obj1.toString();
		}
		else{
			 return "false";
		}
	}
    
    @ResponseBody 
   	@ResponseStatus(HttpStatus.OK)
   	@RequestMapping(value="/deleteDir/{id}", method = RequestMethod.DELETE,produces={"application/json; charset=UTF-8"})
   	public String deleteDir(@PathVariable long id, HttpServletRequest req) throws ClassNotFoundException, JSONException{

   		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   		if (!(auth instanceof AnonymousAuthenticationToken)) {
   			 LutAuditDir aw=(LutAuditDir) dao.getHQLResult("from LutAuditDir t where t.id="+id+"", "current");
   			 dao.PeaceCrud(aw, "LutAuditDir", "delete", (long) aw.getId(), 0, 0, null);
	   		 return "true";
   		}
   		return "false";
   	}
    
    @RequestMapping(value="/addWorkDirection",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public String addWorkDirection(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			 
			 if(obj.getLong("id")==0){
				 LutAuditDir aw = new LutAuditDir();
				 if(obj.has("name")){
		    		 aw.setName(obj.getString("name"));
		    	 }
				 if(obj.has("shortname")){
		    		 aw.setShortname(obj.getString("shortname"));
		    	 }
				 dao.PeaceCrud(aw, "LutAuditDir", "save", (long) 0, 0, 0, null);
				 if(obj.has("notice")){
						JSONArray arr = obj.getJSONArray("notice");
					 	 for(int k=0;k<arr.length();k++ ){
							 LnkDirectionNotice dn = new LnkDirectionNotice();
							 dn.setNoticeid(arr.getLong(k));
							 dn.setDirectionid(aw.getId());
							 dao.PeaceCrud(dn, "LnkDirectionNotice", "save", (long) 0, 0, 0, null);
						 }
					 }
				 if(obj.has("procedure")){
						JSONArray arr = obj.getJSONArray("procedure");
					 	 for(int k=0;k<arr.length();k++ ){
							 LnkDirectionProcedure dp = new LnkDirectionProcedure();
							 dp.setProcedureid(arr.getLong(k));
							 dp.setDirectionid(aw.getId());
							 dao.PeaceCrud(dp, "LnkDirectionProcedure", "save", (long) 0, 0, 0, null);
						 }
					 }
				 obj1.put("success", "success");
			 }
			 else{
				 LutAuditDir aw=(LutAuditDir) dao.getHQLResult("from LutAuditDir t where t.id='"+obj.getLong("id")+"'", "current");
					
				 if(obj.has("name")){
		    		 aw.setName(obj.getString("name"));
		    	 }
				 if(obj.has("shortname")){
		    		 aw.setShortname(obj.getString("shortname"));
		    	 }
				 dao.PeaceCrud(aw, "LutAuditDir", "update",(long) obj.getLong("id"), 0, 0, null);
				 
				 dao.PeaceCrud(null, "LnkDirectionNotice", "delete", (long) aw.getId(), 0, 0, "directionid");
				 if(obj.has("notice")){
					 	JSONArray arr = obj.getJSONArray("notice");
					 	 for(int k=0;k<arr.length();k++ ){
							 LnkDirectionNotice dn = new LnkDirectionNotice();
							 dn.setNoticeid(arr.getLong(k));
							 dn.setDirectionid(aw.getId());
							 dao.PeaceCrud(dn, "LnkDirectionNotice", "save", (long) 0, 0, 0, null);
						 }
					 }
				 dao.PeaceCrud(null, "LnkDirectionProcedure", "delete", (long) aw.getId(), 0, 0, "directionid");
				 if(obj.has("procedure")){
						JSONArray arr = obj.getJSONArray("procedure");
					 	 for(int k=0;k<arr.length();k++ ){
							 LnkDirectionProcedure dp = new LnkDirectionProcedure();
							 dp.setProcedureid(arr.getLong(k));
							 dp.setDirectionid(aw.getId());
							 dao.PeaceCrud(dp, "LnkDirectionProcedure", "save", (long) 0, 0, 0, null);
						 }
					 }
				 obj1.put("success", "success");
			 }
			 return obj1.toString();
		}
		else{
			 return "false";
		}
	}
    
    @RequestMapping(value="/addFsFormType",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public String addFsFormType(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 JSONObject obj= new JSONObject(jsonString);
			 FsLutFormType aw = new FsLutFormType();
			 aw.setFormname(obj.getString("name"));	
			 dao.PeaceCrud(aw, "FsLutFormType", "save", (long) 0, 0, 0, null);
			 return "true";
		}
		else{
			 return "false";
		}
	}
    
    @RequestMapping(value="/addWork",method=RequestMethod.POST)
	public @ResponseBody String addWork(@RequestParam("files") Object files,@RequestParam("obj") String jsonstring, MultipartRequest mpr, HttpServletRequest req) throws JSONException, DocumentException, Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
	     
			 JSONObject obj= new JSONObject(jsonstring);
			 JSONObject res= new JSONObject();
			 MultipartFile mfile =  null;
			 LutAuditWork aw = new LutAuditWork();
			 
			 aw.setWorkformname(obj.getString("workformname"));
	    	 if(obj.has("orderid")){
	    		 aw.setOrderid(obj.getLong("orderid"));
	    	 }
	    	 if(obj.has("fileurl")){
	    		 aw.setFileurl(obj.getString("fileurl"));
	    	 }
	    	 if(obj.has("parentid")){
	    		 aw.setParentid(obj.getLong("parentid"));
	    	 }
	    	 if(obj.has("text")){
	    		 aw.setText(obj.getString("text"));
	    	 }
	    	 if(obj.has("isdettrial")){
	    		 aw.setIsdettrial(obj.getBoolean("isdettrial"));
	    	 }
	    	 if(obj.has("au_level")){
		    	 aw.setLevelid(obj.getLong("au_level")); 
	    	 }
	    	 if(obj.has("fname")){
	    		 aw.setFname(obj.getString("fname"));
	    	 }
	    	 if(obj.getInt("isscore")==0){
	    		 aw.setIsscore(false);
	    	 }
	    	 else{
	    		 aw.setIsscore(true);
	    	 }
	    	 
		     if(obj.getInt("isfile")==0){
		    	 aw.setIsfile(obj.getInt("isfile"));
		    	 dao.PeaceCrud(aw, "LutAuditWork", "save", (long) 0, 0, 0, null);
		     }
		     else{
		    	 mfile = (MultipartFile)files;
		    	 if (mfile != null) {
		    		 	String appPath = req.getSession().getServletContext().getRealPath("");
				 	    
				 	    String SAVE_DIR = IzrApplication.ROOT;
						Date d1 = new Date();
						SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy");
						String special = df.format(d1);
		        		String str= UUID.randomUUID().toString();
		        		String path = appPath + File.separator + SAVE_DIR+ File.separator  +"workfile" + File.separator +special;
		        	    String fpath = appPath + File.separator + SAVE_DIR+ File.separator +"workfile" +File.separator +special + File.separator +mfile.getOriginalFilename();
		        		
		        	    File folder = new File(path);
		        	    if(!folder.exists()){
		        	     	folder.mkdirs();
		    			}
		        	    File logoorgpath = new File(fpath);
		        	    System.out.println("logoorgpath");
		        	    System.out.println(logoorgpath);
		        		if(!logoorgpath.exists()){
		        			System.out.println("exists");
		        			mfile.transferTo(logoorgpath);
		        		}
		        		aw.setFilepath(fpath);
		        		aw.setFilename(mfile.getOriginalFilename());
		        		aw.setIsfile(obj.getInt("isfile"));
		        		
		        		dao.PeaceCrud(aw, "LutAuditWork", "save", (long) 0, 0, 0, null);
				}   
		    	else{
		    		res.put("success", "file dutuu");
	    			return res.toString();
		    	 }
		     }	  
		     if(obj.has("categoryid")){
					JSONArray arr = obj.getJSONArray("categoryid");
				 	 for(int k=0;k<arr.length();k++ ){
						 LnkWorkCategory wc = new LnkWorkCategory();
						 wc.setCatid(arr.getLong(k));
						 wc.setWorkid(aw.getId());
						 dao.PeaceCrud(wc, "LnkWorkCategory", "save", (long) 0, 0, 0, null);
					 }
				 }
	    	 if(obj.has("reasonid")){
					JSONArray arr = obj.getJSONArray("reasonid");
				 	 for(int k=0;k<arr.length();k++ ){
				 		LnkWorkAuType wt = new LnkWorkAuType();
				 		wt.setTypeid(arr.getLong(k));
				 		wt.setWorkid(aw.getId());
						dao.PeaceCrud(wt, "LnkWorkAuType", "save", (long) 0, 0, 0, null);
					 }
				 }
		  	 res.put("success", "success");
	    	 return res.toString();
		}
		return null;
    }
    
    @RequestMapping(value="/updateWork",method=RequestMethod.POST)
	public String updateWork(@RequestParam("files") Object files,@RequestParam("obj") String jsonstring, MultipartRequest mpr, HttpServletRequest req) throws JSONException, DocumentException, Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
	     
			 JSONObject obj= new JSONObject(jsonstring);
			 JSONObject res= new JSONObject();
			 MultipartFile mfile =  null;
			 
			 LutAuditWork aw=(LutAuditWork) dao.getHQLResult("from LutAuditWork t where t.id='"+obj.getLong("id")+"'", "current");
			
			 aw.setWorkformname(obj.getString("workformname"));
	    	 if(obj.has("orderid")){
	    		 aw.setOrderid(obj.getLong("orderid"));
	    	 }
	    	 if(obj.has("parentid")){
	    		 aw.setParentid(obj.getLong("parentid"));
	    	 }
	    	 if(obj.has("text")){
	    		 aw.setText(obj.getString("text"));
	    	 }
	    	 if(obj.has("fileurl")){
	    		 aw.setFileurl(obj.getString("fileurl"));
	    	 }
	    	 if(obj.has("fname")){
	    		 aw.setFname(obj.getString("fname"));
	    	 }
	    	 if(obj.has("isdettrial")){
	    		 aw.setIsdettrial(obj.getBoolean("isdettrial"));
	    	 }
	    	 if(obj.has("au_level")){
		    	 aw.setLevelid(obj.getLong("au_level"));
	    	 }
	    	 aw.setIsfile(obj.getInt("isfile"));
	    	 
	    	 if(obj.getInt("isscore")==0){
	    		 aw.setIsscore(false);
	    	 }
	    	 else{
	    		 aw.setIsscore(true);
	    	 }
	    	 
		     if(obj.getInt("isfile")==0){
		    	 String appPath = req.getSession().getServletContext().getRealPath("");
	    		 String delPath = appPath + aw.getFilepath();
				 File file = new File(delPath);
		        	
				 if(file.exists()){
		   			System.out.println(file.getName() + " bn!");
				 }else{
		   			System.out.println("bhgu.");
		   	 	 }
		   		 if(file.delete()){
		   			System.out.println(file.getName() + " is deleted!");
		   		 }else{
		   			System.out.println("Delete operation is failed.");
		   	 	 }
		   		 aw.setFilename(null);
		   		 aw.setFilepath(null);
		    	 dao.PeaceCrud(aw, "LutAuditWork", "update",(long) obj.getLong("id"), 0, 0, null);
		     }
		     else{
		    	 if(obj.has("removeFile")){
		    		 if(obj.getInt("removeFile")==1){
		    			 mfile = (MultipartFile)files;
				    	 if (mfile != null) {
				    		 String appPath = req.getSession().getServletContext().getRealPath("");
				    		 String delPath = appPath + aw.getFilepath();
							 System.out.println(aw.getFilepath() + " is deleted!");
						
							 File file = new File(delPath);
					        	
							 if(file.exists()){
					   			System.out.println(file.getName() + " bn!");
							 }else{
					   			System.out.println("bhgu.");
					   	 	 }
					   		 if(file.delete()){
					   			System.out.println(file.getName() + " is deleted!");
					   		 }else{
					   			System.out.println("Delete operation is failed.");
					   	 	 }
						 	    String SAVE_DIR = IzrApplication.ROOT;
								Date d1 = new Date();
								SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy");
								String special = df.format(d1);
								
								UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
								LutUser loguser=(LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
					
				        		String str= UUID.randomUUID().toString();
				        		String path = appPath + File.separator + SAVE_DIR+ File.separator  +loguser.getUsername() + File.separator +special;
				        	    String fpath = appPath + File.separator + SAVE_DIR+ File.separator +loguser.getUsername() +File.separator +special + File.separator +mfile.getOriginalFilename();
				        		
				        	    File folder = new File(path);
				        	    if(!folder.exists()){
				        	     	folder.mkdirs();
				    			}
				        	    
				        	    File logoorgpath = new File(fpath);
				        	    System.out.println("logoorgpath");
				        	    System.out.println(logoorgpath);
				        		if(!logoorgpath.exists()){
				        			System.out.println("exists");
				        			mfile.transferTo(logoorgpath);
				        		}
				        		String root = req.getContextPath();
				        		String path1 = root + File.separator + SAVE_DIR+ File.separator +loguser.getUsername()+File.separator + special + File.separator + mfile.getOriginalFilename();
				        		String ext = Files.getFileExtension(path1); 
				        		aw.setFilepath(path1);
				        		aw.setFilename(mfile.getOriginalFilename());
				        		aw.setIsfile(obj.getInt("isfile"));
				        		dao.PeaceCrud(aw, "LutAuditWork", "update",(long) obj.getLong("id"), 0, 0, null);
						}   
				    	else{
				    		res.put("success", "file dutuu");
			    			return res.toString();
				    	 }
		    		 }
		    		 else{
		    			 dao.PeaceCrud(aw, "LutAuditWork", "update",(long) obj.getLong("id"), 0, 0, null);
		    		 }
		    	 }
		    	 else{
		    		 
	    			 dao.PeaceCrud(aw, "LutAuditWork", "update",(long) obj.getLong("id"), 0, 0, null);
	    		 }
		     }	
		     dao.PeaceCrud(null, "LnkWorkCategory", "delete", (long) obj.getLong("id"), 0, 0, "workid");
		     if(obj.has("categoryid")){
					JSONArray arr = obj.getJSONArray("categoryid");
				 	 for(int k=0;k<arr.length();k++ ){
						 LnkWorkCategory wc = new LnkWorkCategory();
						 wc.setCatid(arr.getLong(k));
						 wc.setWorkid(obj.getLong("id"));
						 dao.PeaceCrud(wc, "LnkWorkCategory", "save", (long) 0, 0, 0, null);
					 }
				 }
		     dao.PeaceCrud(null, "LnkWorkAuType", "delete", (long) obj.getLong("id"), 0, 0, "workid");
	    	 if(obj.has("reasonid")){
					JSONArray arr = obj.getJSONArray("reasonid");
				 	 for(int k=0;k<arr.length();k++ ){
				 		LnkWorkAuType wt = new LnkWorkAuType();
				 		wt.setTypeid(arr.getLong(k));
				 		wt.setWorkid(aw.getId());
						dao.PeaceCrud(wt, "LnkWorkAuType", "save", (long) 0, 0, 0, null);
					 }
				 }
		  	 res.put("success", "success");
	    	 return res.toString();
		}
		return null;
    }
    
    @ResponseBody 
   	@ResponseStatus(HttpStatus.OK)
   	@RequestMapping(value="/deleteFtype/{id}", method = RequestMethod.DELETE,produces={"application/json; charset=UTF-8"})
   	public String deleteFtype(@PathVariable long id, HttpServletRequest req) throws ClassNotFoundException, JSONException{

   		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   		if (!(auth instanceof AnonymousAuthenticationToken)) {
   			FsLutFormType aw=(FsLutFormType) dao.getHQLResult("from FsLutFormType t where t.id="+id+"", "current");
   			dao.PeaceCrud(aw, "FsLutFormType", "delete", (long) aw.getId(), 0, 0, null);
   			return "true";
   		}
   		return "false";
   	}
    
    @ResponseBody 
   	@ResponseStatus(HttpStatus.OK)
   	@RequestMapping(value="/deleteWork/{id}", method = RequestMethod.DELETE,produces={"application/json; charset=UTF-8"})
   	public String deleteWork(@PathVariable long id, HttpServletRequest req) throws ClassNotFoundException, JSONException{

   		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   		if (!(auth instanceof AnonymousAuthenticationToken)) {
   			
   			 LutAuditWork aw=(LutAuditWork) dao.getHQLResult("from LutAuditWork t where t.id="+id+"", "current");
   			 String appPath = req.getSession().getServletContext().getRealPath("");
			 String delPath = appPath + aw.getFilepath();
			 System.out.println(aw.getFilepath() + " is deleted!");
			 File file = new File(delPath);
	        	
			 if(file.exists()){
	   			System.out.println(file.getName() + " bn!");
				 }else{
	   			System.out.println("bhgu.");
	   	 	 }
	        
	   		 if(file.delete()){
	   			System.out.println(file.getName() + " is deleted!");
	   		 }else{
	   			System.out.println("Delete operation is failed.");
	   	 	 }
	   		 dao.PeaceCrud(aw, "LutAuditWork", "delete", (long) aw.getId(), 0, 0, null);
   			 return "true";
   		}
   		return "false";
   	}
    
    @RequestMapping(value = "/list/{domain}", method= RequestMethod.POST)
    public @ResponseBody DataSourceResult customers(@PathVariable String domain, @RequestBody String request, HttpServletRequest req) throws HttpRequestMethodNotSupportedException {
		Long count=(long) 0;
		List<?> rs = null;		

		DataSourceResult result = new DataSourceResult();	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("LutValidation")){
				List<LutValidation> wrap = new ArrayList<LutValidation>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutValidation or=(LutValidation) rs.get(i);
					LutValidation cor=new LutValidation();
					cor.setId(or.getId());
					cor.setTitle1(or.getTitle1());
					cor.setTitle2(or.getTitle2());
					cor.setBalanceid(or.getBalanceid());
					cor.setCode1(or.getCode1());
					cor.setCode2(or.getCode2());
					cor.setPosition1(or.getPosition1());
					cor.setPosition2(or.getPosition2());
					cor.setIsformula1(or.getIsformula1());
					cor.setIsformula2(or.getIsformula2());
					cor.setAdirid(or.getAdirid());
					cor.setRiskid(or.getRiskid());
					cor.setTryoutid(or.getTryoutid());
					cor.setValid(or.getValid());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutConfirmationSource")){
				List<LutConfirmationSource> wrap = new ArrayList<LutConfirmationSource>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutConfirmationSource or=(LutConfirmationSource) rs.get(i);
					LutConfirmationSource cor=new LutConfirmationSource();
					cor.setId(or.getId());
					cor.setName(or.getName());
					cor.setType(or.getType());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutConfirmationMethod")){
				List<LutConfirmationMethod> wrap = new ArrayList<LutConfirmationMethod>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutConfirmationMethod or=(LutConfirmationMethod) rs.get(i);
					LutConfirmationMethod cor=new LutConfirmationMethod();
					cor.setId(or.getId());
					cor.setName(or.getName());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutConfirmationType")){
				List<LutConfirmationType> wrap = new ArrayList<LutConfirmationType>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutConfirmationType or=(LutConfirmationType) rs.get(i);
					LutConfirmationType cor=new LutConfirmationType();
					cor.setId(or.getId());
					cor.setName(or.getName());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("FsFormA4m")){
				List<FsFormA4> wrap = new ArrayList<FsFormA4>();
				
				domain="FsFormA4";				
				JSONObject str= new JSONObject(request);
				str.put("custom", "where planid='"+str.getLong("mid")+"' and stepid='"+str.getLong("stepid")+"'");
				
				
				rs= dao.kendojson(str.toString(), domain);
				
				for(int i=0;i<rs.size();i++){
					FsFormA4 or=(FsFormA4) rs.get(i);
					FsFormA4 cor=new FsFormA4();
					cor.setId(or.getId());
					cor.setData1(or.getData1());
					cor.setData2(or.getData2());
					cor.setData3(or.getData3());
					cor.setData4(or.getData4());
					cor.setData5(or.getData5());
					cor.setData6(or.getData6());
					cor.setStepid(or.getStepid());
					cor.setOrgcatid(or.getOrgcatid());
					cor.setOrgcode(or.getOrgcode());
					cor.setCyear(or.getCyear());
					cor.setIsselect(or.getIsselect());
					cor.setPercent(or.getPercent());
					cor.setDescription1(or.getDescription1());
					cor.setDescription2(or.getDescription2());
					cor.setDescription3(or.getDescription3());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(wrap.size());
			} 
			else if(domain.equalsIgnoreCase("LutFactorCriterion")){
				List<LutFactorCriterion> wrap = new ArrayList<LutFactorCriterion>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutFactorCriterion or=(LutFactorCriterion) rs.get(i);
					LutFactorCriterion cor=new LutFactorCriterion();
					cor.setId(or.getId());
					cor.setName(or.getName());
					cor.setFactorid(or.getFactorid());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutFactor")){
				List<LutFactor> wrap = new ArrayList<LutFactor>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutFactor or=(LutFactor) rs.get(i);
					LutFactor cor=new LutFactor();
					cor.setId(or.getId());
					cor.setFactorname(or.getFactorname());
					cor.setDirid(or.getDirid());
					cor.setOrderid(or.getOrderid());
					cor.setGroupid(or.getGroupid());
					cor.setFnumber(or.getFnumber());
					cor.setRiskid(or.getRiskid());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutGroupOfFactor")){
				List<LutGroupOfFactor> wrap = new ArrayList<LutGroupOfFactor>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutGroupOfFactor or=(LutGroupOfFactor) rs.get(i);
					LutGroupOfFactor cor=new LutGroupOfFactor();
					cor.setId(or.getId());
					cor.setName(or.getName());
					cor.setOrderid(or.getOrderid());
					cor.setFtype(or.getFtype());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("FsLutFormType")){
				List<FsLutFormType> wrap = new ArrayList<FsLutFormType>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					FsLutFormType or=(FsLutFormType) rs.get(i);
					FsLutFormType cor=new FsLutFormType();
					cor.setId(or.getId());
					cor.setFormname(or.getFormname());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutTryout")){
				List<LutTryout> wrap = new ArrayList<LutTryout>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutTryout or=(LutTryout) rs.get(i);
					LutTryout cor=new LutTryout();
					cor.setId(or.getId());
					cor.setAdirid(or.getAdirid());
					cor.setWorkid(or.getWorkid());
					cor.setFormdesc(or.getFormdesc());
					cor.setText(or.getText());
					 String names="";
					for(int y=0;y<or.getLnkTryoutRisks().size();y++){
						 if(names.equalsIgnoreCase("")){
							 names=or.getLnkTryoutRisks().get(y).getLutRisk().getRiskname();
						 }else{
							 names = names+", "+ or.getLnkTryoutRisks().get(y).getLutRisk().getRiskname();
						 }
					}
					cor.setRtext(names);
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutReason")){
				List<LutReason> wrap = new ArrayList<LutReason>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutReason or=(LutReason) rs.get(i);
					LutReason cor=new LutReason();
					cor.setId(or.getId());
					cor.setName(or.getName());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutAuditLevel")){
				List<LutAuditLevel> wrap = new ArrayList<LutAuditLevel>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutAuditLevel or=(LutAuditLevel) rs.get(i);
					LutAuditLevel cor=new LutAuditLevel();
					cor.setId(or.getId());
					cor.setLevelname(or.getLevelname());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutAuditWork")){
				List<LutAuditWork> wrap = new ArrayList<LutAuditWork>();
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutAuditWork or=(LutAuditWork) rs.get(i);
					LutAuditWork cor=new LutAuditWork();
					cor.setId(or.getId());
					cor.setFilename(or.getFilename());
					cor.setParentid(or.getParentid());
					cor.setWorkformname(or.getWorkformname());
					cor.setIsfile(or.getIsfile());
					cor.setFilepath(or.getFilepath());
					cor.setText(or.getText());
					cor.setIsscore(or.isIsscore());
					cor.setOrderid(or.getOrderid());
					cor.setIsdettrial(or.getIsdettrial());
					cor.setLevelid(or.getLevelid());
					String names="";
					for(int y=0;y<or.getLnkWorkAuTypes().size();y++){
						 if(names.equalsIgnoreCase("")){
							 names=or.getLnkWorkAuTypes().get(y).getLutReason().getName();
						 }else{
							 names = names+", "+ or.getLnkWorkAuTypes().get(y).getLutReason().getName();
						 }
					}
					cor.setRtext(names);
					
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			return  result;
		}
		return null;
	}
    
    @RequestMapping(value="/submitUserPro",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public String ajaxconf(@RequestBody String jsonString) throws JSONException{
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 System.out.println(jsonString);
			 JSONObject obj1 = new JSONObject();
			 JSONObject obj= new JSONObject(jsonString);
			 LutUser loguser=(LutUser) dao.getHQLResult("from LutUser t where t.id='"+obj.getLong("id")+"'", "current");
			
			 if( obj.getBoolean("passchanged")){
				 String currentpassString = obj.getString("currentpassword");
				 String passString = obj.getString("password");
				 if(passwordEncoder.matches(currentpassString,passString)){
					 String passwd = passwordEncoder.encode(obj.getString("newpassword"));
					 
					 if(obj.has("givenname")){
						 loguser.setGivenname(obj.getString("givenname"));
					 }
					 if(obj.has("familyname")){
						 loguser.setFamilyname(obj.getString("familyname"));
					 }
					 if(obj.has("mail")){
						 loguser.setEmail(obj.getString("mail"));
					 }
					 if(obj.has("mobile")){
						 loguser.setMobile(obj.getString("mobile"));
					 }
					 loguser.setPassword(passwd);
					 dao.PeaceCrud(loguser, "LutUser", "update",(long) obj.getLong("id"), 0, 0, null);
					 obj1.put("success", "success");
				 }
				 else{
					 obj1.put("success", "invalidpass");	
				 }
			 }
			 else{
				 if(obj.has("givenname")){
					 loguser.setGivenname(obj.getString("givenname"));
				 }
				 if(obj.has("familyname")){
					 loguser.setFamilyname(obj.getString("familyname"));
				 }
				 if(obj.has("mail")){
					 loguser.setEmail(obj.getString("mail"));
				 }
				 if(obj.has("mobile")){
					 loguser.setMobile(obj.getString("mobile"));
				 }
				 dao.PeaceCrud(loguser, "LutUser", "update",(long) obj.getLong("id"), 0, 0, null);
				 obj1.put("success", "success");
			 }
			 return obj1.toString();
		}
		else{
			 return "false";
		}
	}
	
    @RequestMapping(value = "/getSelectFsFormA4/{domain}/{planid}/{stepid}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String getSelectFsFormA4(@PathVariable String domain, @PathVariable long planid,@PathVariable long stepid) {
	try{

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JSONArray arr=new JSONArray();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("FsFormA4")){
				List<FsFormA4> aw=  (List<FsFormA4>) dao.getHQLResult("from FsFormA4 t where planid='"+planid+"' and stepid='"+stepid+"' and isselect=1", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("percent", aw.get(i).getPercent());
					 obj.put("description1", aw.get(i).getDescription1());
					 obj.put("description2", aw.get(i).getDescription2());
					 obj.put("description3", aw.get(i).getDescription3());
					 arr.put(obj);
				 }	
			}
		}
        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
    @RequestMapping(value = "/selectFsFormA4Percent/{planid}/{stepid}", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
    public Boolean selectFsFormA4Percent(@PathVariable long planid,@PathVariable long stepid, @RequestParam String jsonstr) {
	try{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JSONObject jsonobj = new JSONObject(jsonstr);
			List<FsFormA4> aw=  (List<FsFormA4>) dao.getHQLResult("from FsFormA4 t where planid='"+planid+"' and stepid='"+stepid+"'", "list");
			 for(int i=0; i<aw.size();i++){
				 if(aw.get(i).getId() == jsonobj.getLong("id")){
					 
					 aw.get(i).setIsselect(1);
					 aw.get(i).setPercent(jsonobj.getString("percent"));
				 }
				 else{
					 aw.get(i).setIsselect(0);
					 aw.get(i).setPercent("");
				 }
				 dao.PeaceCrud(aw.get(i), "FsFormA4", "update", aw.get(i).getId(), 0, 0, null);
			 }	
			 return true;
			
		}
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
    @RequestMapping(value = "/resource/{domain}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String tree(@PathVariable String domain,@PathVariable long id) {
	try{

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JSONArray arr=new JSONArray();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("LutCategory")){
				List<LutCategory> aw=  (List<LutCategory>) dao.getHQLResult("from LutCategory t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("categoryname",  aw.get(i).getCategoryname());
					 
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutMenu")){
				List<LutMenu> aw=  (List<LutMenu>) dao.getHQLResult("from LutMenu t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("menuname",  aw.get(i).getMenuname());
					 obj.put("stateurl",  aw.get(i).getStateurl());
					 obj.put("uicon",  aw.get(i).getUicon());
					 obj.put("parentid",  aw.get(i).getParentid());
					 obj.put("orderid",  aw.get(i).getOrderid());
					 
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutAuditYear")){
				List<LutAuditYear> aw=  (List<LutAuditYear>) dao.getHQLResult("from LutAuditYear t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("audityear",  aw.get(i).getAudityear());
					 obj.put("isactive",  aw.get(i).getIsactive());
					 
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutPosition")){
				List<LutPosition> aw=  (List<LutPosition>) dao.getHQLResult("from LutPosition t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("positionname",  aw.get(i).getPositionname());
					 obj.put("orderid",  aw.get(i).getOrderid());
					 obj.put("isactive",  aw.get(i).getIsactive());
					 
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutDepartment")){
				List<LutDepartment> aw=  (List<LutDepartment>) dao.getHQLResult("from LutDepartment t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("address", aw.get(i).getAddress());
					 obj.put("phone", aw.get(i).getPhone());
					 obj.put("reg", aw.get(i).getReg());
					 obj.put("licnum", aw.get(i).getLicnum());
					 obj.put("licexpiredate", aw.get(i).getLicexpiredate());
					 obj.put("departmentname", aw.get(i).getDepartmentname());
					 obj.put("shortname", aw.get(i).getShortname());
					 obj.put("isactive", aw.get(i).getIsactive());
					 obj.put("isstate", aw.get(i).getIsstate());
					 obj.put("email", aw.get(i).getEmail());
					 obj.put("web", aw.get(i).getWeb());
					 
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutValidation")){
				List<LutValidation> aw=  (List<LutValidation>) dao.getHQLResult("from LutValidation t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("title1", aw.get(i).getTitle1());
					 obj.put("title2", aw.get(i).getTitle2());
					 obj.put("balanceid", aw.get(i).getBalanceid());
					 obj.put("code1", aw.get(i).getCode1());
					 obj.put("code2", aw.get(i).getCode2());
					 obj.put("position1", aw.get(i).getPosition1());
					 obj.put("position2", aw.get(i).getPosition2());
					 obj.put("isformula1", aw.get(i).getIsformula1());
					 obj.put("isformula2", aw.get(i).getIsformula2());
					 obj.put("valid", aw.get(i).getValid());
					 obj.put("adirid", aw.get(i).getAdirid());
					 obj.put("riskid", aw.get(i).getRiskid());
					 obj.put("tryoutid", aw.get(i).getTryoutid());
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("FsFormA4")){
				List<FsFormA4> aw=  (List<FsFormA4>) dao.getHQLResult("from FsFormA4 t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("data1", aw.get(i).getData1());
					 obj.put("data2", aw.get(i).getData2());
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutConfirmationSource")){
				List<LutConfirmationSource> aw=  (List<LutConfirmationSource>) dao.getHQLResult("from LutConfirmationSource t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("name", aw.get(i).getName());
					 obj.put("type", aw.get(i).getType());
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutConfirmationMethod")){
				List<LutConfirmationMethod> aw=  (List<LutConfirmationMethod>) dao.getHQLResult("from LutConfirmationMethod t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("name", aw.get(i).getName());
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutConfirmationType")){
				List<LutConfirmationType> aw=  (List<LutConfirmationType>) dao.getHQLResult("from LutConfirmationType t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("name", aw.get(i).getName());
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutCriterionOfFactor")){
				List<LutFactorCriterion> aw=  (List<LutFactorCriterion>) dao.getHQLResult("from LutFactorCriterion t where t.factorid='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("name", aw.get(i).getName());
					 obj.put("factorid", aw.get(i).getFactorid());
					 obj.put("saved", 1);
					 arr.put(obj);
				 }	
			}
			else if(domain.equalsIgnoreCase("LutFactorCriterion")){
				LutFactorCriterion aw=(LutFactorCriterion) dao.getHQLResult("from LutFactorCriterion t where t.id='"+id+"'", "current");
				 JSONObject obj=new JSONObject();      	
				 obj.put("id", aw.getId());
				 obj.put("name", aw.getName());
				 obj.put("factorid", aw.getFactorid());
				 arr.put(obj);		
			}
			else if(domain.equalsIgnoreCase("LutFactor")){
				LutFactor aw=(LutFactor) dao.getHQLResult("from LutFactor t where t.id='"+id+"'", "current");
				 JSONObject obj=new JSONObject();      	
				 obj.put("id", aw.getId());
				 obj.put("factorname", aw.getFactorname());
				 obj.put("dirid", aw.getDirid());
				 obj.put("groupid", aw.getGroupid());
				 obj.put("orderid", aw.getOrderid());
				 obj.put("fnumber", aw.getFnumber());
				 obj.put("riskid", aw.getRiskid());
				 obj.put("tryid", aw.getTryid());
				 JSONArray catid = new JSONArray();
					
				 for(int i=0;i<aw.getLnkFactorCategories().size();i++){
					 catid.put(aw.getLnkFactorCategories().get(i).getCatid());
				 }
				 obj.put("catid", catid);
				 
				 arr.put(obj);		
			}
			else if(domain.equalsIgnoreCase("LutGroupOfFactor")){
				LutGroupOfFactor aw=(LutGroupOfFactor) dao.getHQLResult("from LutGroupOfFactor t where t.id='"+id+"' order by t.orderid desc", "current");
				 JSONObject obj=new JSONObject();      	
				 obj.put("id", aw.getId());
				 obj.put("name", aw.getName());
				 obj.put("orderid", aw.getOrderid());
				 obj.put("ftype", aw.getFtype());
				 arr.put(obj);		
			}
			else if(domain.equalsIgnoreCase("FsLutFormType")){
				
				List<FsLutFormType> rs=(List<FsLutFormType>) dao.getHQLResult("from FsLutFormType t where t.id='"+id+"'", "list");
				 for(int i=0;i<rs.size();i++){
					 	JSONObject obj=new JSONObject();      	
					 	obj.put("id", rs.get(i).getId());
					 	obj.put("formname", rs.get(i).getFormname());
		        		arr.put(obj);        	
		        	}
			}
			else if(domain.equalsIgnoreCase("MyComment")){
				
				List<LutMainComment> rs=(List<LutMainComment>) dao.getHQLResult("from LutMainComment t where t.userid='"+id+"'", "list");
				 for(int i=0;i<rs.size();i++){
					 	JSONObject obj=new JSONObject();      	
					 	obj.put("id", rs.get(i).getId());
					 	obj.put("username", rs.get(i).getUsername());
					 	obj.put("usermail", rs.get(i).getUsermail());
					 	obj.put("userid", rs.get(i).getUserid());
					 	obj.put("text", rs.get(i).getText());
					 	obj.put("answer", rs.get(i).getAnswer());
					 	obj.put("status", rs.get(i).getStatus());
					 	obj.put("offid", rs.get(i).getOffid());
					 	obj.put("subdate", rs.get(i).getSubdate());
					 	obj.put("appdate", rs.get(i).getAppdate());
					 	obj.put("title", rs.get(i).getTitle());
		        		arr.put(obj);        	
		        	}
			}
			else if(domain.equalsIgnoreCase("LutTryout")){
				LutTryout aw=(LutTryout) dao.getHQLResult("from LutTryout t where t.id='"+id+"'", "current");
				 JSONObject obj=new JSONObject();      	
				 obj.put("id", aw.getId());
				 obj.put("adirid", aw.getAdirid());
				 obj.put("workid", aw.getWorkid());
				 obj.put("text", aw.getText());
				 obj.put("formdesc", aw.getFormdesc());
				 
				 JSONArray notice = new JSONArray();
					
				 for(int i=0;i<aw.getLnkTryoutNotices().size();i++){
					 notice.put(aw.getLnkTryoutNotices().get(i).getNoticeid());
				 }
				 obj.put("notice", notice);
				 
				 JSONArray risk = new JSONArray();
					
				 for(int i=0;i<aw.getLnkTryoutRisks().size();i++){
					 risk.put(aw.getLnkTryoutRisks().get(i).getRiskid());
				 }
				 obj.put("risk", risk);
				 
				 JSONArray procedure = new JSONArray();
				 for(int i=0;i<aw.getLnkTryoutProcedures().size();i++){
					 procedure.put(aw.getLnkTryoutProcedures().get(i).getProcedureid());
				 }
				 obj.put("procedure", procedure);
				 
				 JSONArray workid = new JSONArray();
				 for(int i=0;i<aw.getLnkTryoutCats().size();i++){
					 workid.put(aw.getLnkTryoutCats().get(i).getCatid());
				 }
				 obj.put("workid", workid);
				 
				 JSONArray conftype = new JSONArray();
				 for(int i=0;i<aw.getLnkTryoutConfTypes().size();i++){
					 conftype.put(aw.getLnkTryoutConfTypes().get(i).getTypeid());
				 }
				 obj.put("conftype", conftype);
				 
				 JSONArray confMethod = new JSONArray();
				 for(int i=0;i<aw.getLnkTryoutConfMethods().size();i++){
					 confMethod.put(aw.getLnkTryoutConfMethods().get(i).getMethodid());
				 }
				 obj.put("confMethod", confMethod);
				 
				 JSONArray confSourceO = new JSONArray();
				 for(int i=0;i<aw.getLnkTryoutConfSources().size();i++){
					 if(aw.getLnkTryoutConfSources().get(i).getTypeid()==1){
						 confSourceO.put(aw.getLnkTryoutConfSources().get(i).getSourceid());
					 }
				 }
				 obj.put("confSourceO", confSourceO);
				 
				 JSONArray confSourceI = new JSONArray();
				 for(int i=0;i<aw.getLnkTryoutConfSources().size();i++){
					 if(aw.getLnkTryoutConfSources().get(i).getTypeid()==0){
						 confSourceI.put(aw.getLnkTryoutConfSources().get(i).getSourceid());
					 }
				 }
				 obj.put("confSourceI", confSourceI);
				 
				 arr.put(obj);		
			}
			else if(domain.equalsIgnoreCase("LnkDirectionNoticeProcedureRisk")){
				LutAuditDir aw=(LutAuditDir) dao.getHQLResult("from LutAuditDir t where t.id='"+id+"'", "current");
				 JSONObject obj=new JSONObject();      	
				 
				 JSONArray notice = new JSONArray();
				
				 for(int i=0;i<aw.getLnkDirectionNotices().size();i++){
					 JSONObject noticeObj=new JSONObject();
					 LutNotice ln=(LutNotice) dao.getHQLResult("from LutNotice t where t.id='"+aw.getLnkDirectionNotices().get(i).getNoticeid()+"'", "current");
					 noticeObj.put("value", ln.getId());
					 noticeObj.put("text", ln.getName());
					 notice.put(noticeObj);
				 }
				 obj.put("notice", notice);
				 
				 JSONArray procedure = new JSONArray();
				 
				 for(int i=0;i<aw.getLnkDirectionProcedures().size();i++){
					 JSONObject procedureObj=new JSONObject();
					 LutProcedure lp=(LutProcedure) dao.getHQLResult("from LutProcedure t where t.id='"+aw.getLnkDirectionProcedures().get(i).getProcedureid()+"'", "current");
					 procedureObj.put("value", lp.getId());
					 procedureObj.put("text", lp.getAprocedure());
					 procedure.put(procedureObj);
				 }
				 obj.put("procedure", procedure);
				 

				 JSONArray risk = new JSONArray();
				 
				 for(int i=0;i<aw.getLnkRiskdirs().size();i++){
					 JSONObject riskObj=new JSONObject();
					 LutRisk lp=(LutRisk) dao.getHQLResult("from LutRisk t where t.id='"+aw.getLnkRiskdirs().get(i).getRiskid()+"'", "current");
					 riskObj.put("value", lp.getId());
					 riskObj.put("text", lp.getRiskname());
					 risk.put(riskObj);
				 }
				 obj.put("risk", risk);
				 
				 arr.put(obj);		
			}
			else if(domain.equalsIgnoreCase("LutAuditDir")){
				LutAuditDir aw=(LutAuditDir) dao.getHQLResult("from LutAuditDir t where t.id='"+id+"'", "current");
				 JSONObject obj=new JSONObject();      	
				 obj.put("id", aw.getId());
				 
				 obj.put("name", aw.getName());
				 obj.put("shortname", aw.getShortname());
				 
				 JSONArray notice = new JSONArray();
				
				 for(int i=0;i<aw.getLnkDirectionNotices().size();i++){
					 notice.put(aw.getLnkDirectionNotices().get(i).getNoticeid());
				 }
				 obj.put("notice", notice);
				 
				 JSONArray procedure = new JSONArray();
				 for(int i=0;i<aw.getLnkDirectionProcedures().size();i++){
					 procedure.put(aw.getLnkDirectionProcedures().get(i).getProcedureid());
				 }
				 obj.put("procedure", procedure);
				 arr.put(obj);		
			}
			else if(domain.equalsIgnoreCase("LutAuditWork")){
				LutAuditWork aw=(LutAuditWork) dao.getHQLResult("from LutAuditWork t where t.id='"+id+"'", "current");
				 JSONObject obj=new JSONObject();      	
				 obj.put("id", aw.getId());
				 obj.put("workformname", aw.getWorkformname());
				 obj.put("orderid", aw.getOrderid());
				 obj.put("parentid", aw.getParentid());
				 obj.put("isfile", aw.getIsfile());
				 obj.put("isdettrial", aw.getIsdettrial());
				 obj.put("text", aw.getText());
				 obj.put("filename", aw.getFilename());
				 obj.put("filepath", aw.getFilepath());
				 obj.put("fileurl", aw.getFileurl());
				 obj.put("fname", aw.getFname());
				 obj.put("isscore", aw.isIsscore());
				 obj.put("au_level", aw.getLevelid());
				 JSONArray categoryid = new JSONArray();
				
				 for(int i=0;i<aw.getLnkWorkCategories().size();i++){
					 categoryid.put(aw.getLnkWorkCategories().get(i).getCatid());
				 }
				 obj.put("categoryid", categoryid);
				 JSONArray reasonid = new JSONArray();
				 for(int i=0;i<aw.getLnkWorkAuTypes().size();i++){
					 reasonid.put(aw.getLnkWorkAuTypes().get(i).getTypeid());
				 }
				 obj.put("reasonid", reasonid);
				 arr.put(obj);		
			}
		}
        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	 @RequestMapping(value = "/core/resource/{domain}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
     public @ResponseBody String tree(@PathVariable String domain) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				
				if(domain.equalsIgnoreCase("LutConfirmationMethod")){
					 List<LutConfirmationMethod> rs=(List<LutConfirmationMethod>) dao.getHQLResult("from LutConfirmationMethod t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutConfirmationSourceO")){
					 List<LutConfirmationSource> rs=(List<LutConfirmationSource>) dao.getHQLResult("from LutConfirmationSource t where type='1' order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutLaw1")){
					 List<LutLaw> rs=(List<LutLaw>) dao.getHQLResult("from LutLaw t where t.parentid is null and t.lawcategory = 1 order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLawname());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutLaw2")){
					 List<LutLaw> rs=(List<LutLaw>) dao.getHQLResult("from LutLaw t where t.parentid is null and t.lawcategory = 2 order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLawname());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutAuditDir")){
					 List<LutAuditDir> rs=(List<LutAuditDir>) dao.getHQLResult("from LutAuditDir t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutConfirmationSourceI")){
					 List<LutConfirmationSource> rs=(List<LutConfirmationSource>) dao.getHQLResult("from LutConfirmationSource t  where type='0' order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutConfirmationType")){
					 List<LutConfirmationType> rs=(List<LutConfirmationType>) dao.getHQLResult("from LutConfirmationType t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutFactor")){
					 List<LutFactor> rs=(List<LutFactor>) dao.getHQLResult("from LutFactor t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getFactorname());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutGroupOfFactor")){
					 List<LutGroupOfFactor> rs=(List<LutGroupOfFactor>) dao.getHQLResult("from LutGroupOfFactor t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutMainComment")){
					 List<LutMainComment> rs=(List<LutMainComment>) dao.getHQLResult("from LutMainComment t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("id", rs.get(i).getId());
						 	obj.put("username", rs.get(i).getUsername());
						 	obj.put("usermail", rs.get(i).getUsermail());
						 	obj.put("userid", rs.get(i).getUserid());
						 	obj.put("text", rs.get(i).getText());
						 	obj.put("answer", rs.get(i).getAnswer());
						 	obj.put("status", rs.get(i).getStatus());
						 	obj.put("offid", rs.get(i).getOffid());
						 	obj.put("subdate", rs.get(i).getSubdate());
						 	obj.put("appdate", rs.get(i).getAppdate());
						 	obj.put("title", rs.get(i).getTitle());
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutProcedure")){
					 List<LutProcedure> rs=(List<LutProcedure>) dao.getHQLResult("from LutProcedure t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getAprocedure());			        		
			        		arr.put(obj);        	
			        	}		
				}
				else if(domain.equalsIgnoreCase("LutRisk")){
					 List<LutRisk> rs=(List<LutRisk>) dao.getHQLResult("from LutRisk t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getRiskname());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutNotice")){
					 List<LutNotice> rs=(List<LutNotice>) dao.getHQLResult("from LutNotice t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutReason")){
					 List<LutReason> rs=(List<LutReason>) dao.getHQLResult("from LutReason t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutAuditLevel")){
					 List<LutAuditLevel> rs=(List<LutAuditLevel>) dao.getHQLResult("from LutAuditLevel t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLevelname());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutCategory")){
					 List<LutCategory> rs=(List<LutCategory>) dao.getHQLResult("from LutCategory t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getCategoryname());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutAuditWork")){
					 List<LutAuditWork> rs=(List<LutAuditWork>) dao.getHQLResult("from LutAuditWork t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getWorkformname());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutAuditDir")){
					 List<LutAuditDir> rs=(List<LutAuditDir>) dao.getHQLResult("from LutAuditDir t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutGroupOfFactorOther")){
					 List<LutGroupOfFactor> rs=(List<LutGroupOfFactor>) dao.getHQLResult("from LutGroupOfFactor t where t.ftype=1 order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				else if(domain.equalsIgnoreCase("LutUser")){
					UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					JSONObject js = new JSONObject();
					
					LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
					JSONObject obj=new JSONObject();      	
					obj.put("id", loguser.getId());
					obj.put("givenname", loguser.getGivenname());
					obj.put("familyname", loguser.getFamilyname());
					obj.put("username", loguser.getUsername());
					obj.put("departmentid", loguser.getDepartmentid());
					obj.put("password", loguser.getPassword());
					
					List<LutDepartment> ds=(List<LutDepartment>) dao.getHQLResult("from LutDepartment t where t.id="+loguser.getDepartmentid()+" order by t.id", "list");
					if(ds.size()>0){
						obj.put("departmentname", ds.get(0).getDepartmentname());
					}
					obj.put("mail", loguser.getEmail());
					obj.put("mobile", loguser.getMobile());
					
					obj.put("positionid", loguser.getPositionid());
					List<LutPosition> ps=(List<LutPosition>) dao.getHQLResult("from LutPosition t where t.id="+loguser.getPositionid()+" order by t.id", "list");
					if(ps.size()>0){
						obj.put("positionname", ps.get(0).getPositionname());
					}
					
					obj.put("roleid", loguser.getRoleid());
					List<LnkUserrole> rs=(List<LnkUserrole>) dao.getHQLResult("from LnkUserrole t where t.userid="+loguser.getId()+" order by t.id", "list");
					if(rs.size()>0){
						
						List<LutRole> ur=(List<LutRole>) dao.getHQLResult("from LutRole t where t.id="+rs.get(0).getRoleid()+" order by t.id", "list");
						if(ur.size()>0){
							obj.put("rolename", ur.get(0).getRolename());
						}
					}
			        arr.put(obj);     
				}	
			}		    	
	        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
