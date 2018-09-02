package com.netgloo.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.netgloo.dao.UserDao;
import com.netgloo.models.DataSourceResult;
import com.netgloo.models.FinJournal;
import com.netgloo.models.LutAuditWork;
import com.netgloo.models.LutDepartment;
import com.netgloo.models.LutMenu;
import com.netgloo.models.LutReason;
import com.netgloo.models.LutUser;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.repo.LnkMenuRepository;


@RestController
@RequestMapping("/fin")
public class FinController {
		
	@Autowired
    private UserDao dao;
	 
    @Autowired
    private LnkMenuRepository lpo;	
    
	
	@RequestMapping(value = "/list/{domain}", method= RequestMethod.POST)
    public @ResponseBody DataSourceResult customers(@PathVariable String domain, @RequestBody String request, HttpServletRequest req) throws HttpRequestMethodNotSupportedException, JSONException {
		Long count=(long) 0;
		List<?> rs = null;
		UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");

		DataSourceResult result = new DataSourceResult();	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("LutMenu")){
				List<LutMenu> wrap = new ArrayList<LutMenu>();
				
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutMenu or=(LutMenu) rs.get(i);
					LutMenu cor=new LutMenu();
					cor.setId(or.getId());
					cor.setUicon(or.getUicon());
					cor.setIsactive(or.getIsactive());
					cor.setMenuname(or.getMenuname());
					cor.setStateurl(or.getStateurl());
					cor.setOrderid(or.getOrderid());
					wrap.add(cor);
				}
				ObjectMapper mapper = new ObjectMapper();
				mapper.setSerializationInclusion(Include.NON_NULL);
				result.setData(rs);	
				result.setTotal(count);
			}

		/*	else if(domain.equalsIgnoreCase("FinJournal")){
				rs= dao.kendojson(request, domain);
			
				result.setData(rs);	
				result.setTotal(rs.size());
			}*/
			else{
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
			
				result.setData(rs);	
				result.setTotal((long) count);
			}
			return  result;
		}
		return null;
	}
	

	/*@RequestMapping(value = "/resource/{domain}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String tree(@PathVariable String domain) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
			
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				
				if(domain.equalsIgnoreCase("FinSurveyDirection")){
					 List<FinSurveyDirection> rs=(List<FinSurveyDirection>) dao.getHQLResult("from FinSurveyDirection t  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
					 	JSONObject obj=new JSONObject();      	
					 	obj.put("value", rs.get(i).getCode());
					 	obj.put("text", rs.get(i).getName());			        		
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
	}*/
	
	
    @RequestMapping(value = "/resource/{domain}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String treeReource(@PathVariable String domain,@PathVariable long id) {
	try{

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JSONArray arr=new JSONArray();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("LutDepartment")){
				List<LutDepartment> aw=  (List<LutDepartment>) dao.getHQLResult("from LutDepartment t where t.id='"+id+"'", "list");
				 for(int i=0; i<aw.size();i++){
					 JSONObject obj=new JSONObject();   
					 obj.put("id", aw.get(i).getId());
					 obj.put("text",  aw.get(i).getDepartmentname());
					 
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
			else if(domain.equalsIgnoreCase("MainAuditRegistration")){
				MainAuditRegistration aw=(MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(aw);
			}

		}
        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
    @RequestMapping(value = "/survey/{domain}/{id}/{amount}/{planid}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String totalAmount(@PathVariable String domain,@PathVariable long id,@PathVariable long planid,@PathVariable double amount) {
	try{

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JSONArray arr=new JSONArray();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			if(domain.equalsIgnoreCase("accAmount")){
				List<Object> aw= new ArrayList<>();
				if(id==0){
					aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where (t.planid="+planid+" and  t.data10>"+amount+") or (t.planid="+planid+" and  t.data10>"+amount+")  group by t.data10", "list");
				}
				else if(id==0 && amount==0){
					aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t order by t.id asc", "list");
				}
				else{
					aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where ( t.planid="+planid+" and  t.data8 like '"+id+"%' and t.data10>"+amount+") or ( t.planid="+planid+" and  t.data9 like '"+id+"%'  and t.data10>"+amount+")  group by t.data10", "list");
				}
				Double sum=(double) 0;
				for (Object aRow : aw) {
				    sum = sum + Double.parseDouble(aRow.toString());
				}
				System.out.println("##"+sum);
				return String.valueOf(sum);
			}
		/*	if(domain.equalsIgnoreCase("accAmountSearchType2")){
				List<Object> aw= new ArrayList<>();
				if(id==0){
					aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where (t.data10>"+amount+") or (t.data10>"+amount+")  group by t.data10 order by t.id asc", "list");
				}
				else if(id==0 && amount==0){
					aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t order by t.id asc", "list");
				}
				else{
					aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where (t.data8 like '"+id+"%' and t.data10>"+amount+") or (t.data9 like '"+id+"%'  and t.data10>"+amount+")  group by t.data10 order by t.id asc", "list");
				}
				Double sum=(double) 0;
				for (Object aRow : aw) {
				    sum = sum + Double.parseDouble(aRow.toString());
				}
				System.out.println("##"+sum);
				return String.valueOf(sum);
			}*/
			
			else if(domain.equalsIgnoreCase("searchType3")){
				List<Object> ass;
				List<Object> aw;
			
				
				if(id==0){
					ass=  (List<Object>) dao.getHQLResult("from FinJournal t where t.planid="+planid+" order by t.id asc", "list");
					Double sum=(double) 0;
					for (Object aRow : ass) {
					    sum = sum + Double.parseDouble(aRow.toString());
					}
					aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where  (t.planid="+planid+" and t.data10>"+sum*amount/100+") or (t.planid="+planid+" and t.data10>"+sum*amount/100+")  group by t.data10", "list");
				}
				else{
					ass=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where (t.planid="+planid+" and  t.data8 like '"+id+"%') or (t.planid="+planid+" and  t.data9 like '"+id+"%')  group by t.data10", "list");
					
					Double sum=(double) 0;
					for (Object aRow : ass) {
					    sum = sum + Double.parseDouble(aRow.toString());
					}
					System.out.println("p"+sum*amount/100);
					aw=  (List<Object>) dao.getHQLResult("select t.data10 from FinJournal t where (t.planid="+planid+" and t.data10>"+sum*amount/100+" and  t.data8 like '"+id+"%') or (t.planid="+planid+" and t.data10>"+sum*amount/100+" and t.data9 like '"+id+"%')  order by t.id asc", "list");
				}
				
				Double sum=(double) 0;
				for (Object aRow : aw) {
				    sum = sum + Double.parseDouble(aRow.toString());
				}
				System.out.println("##"+sum);
				return String.valueOf(sum);
			}
			else if(domain.equalsIgnoreCase("searchType4")){
				List<Object> ass;
				List<Object[]> aw;
			
				
				if(id==0){
					aw=  (List<Object[]>) dao.jData((int) amount, 0, "", "", "select t.data10, t.amount from FinJournal t ", null);
				}
				else{
					aw=  (List<Object[]>) dao.jData((int) amount, 0, "", "", "select t.data10, t.amount, t.id from FinJournal t where (t.data8 like '"+id+"%') or (t.data9 like '"+id+"%') ", null);
				}
				
				Double sum=(double) 0;
				Double sumError=(double) 0;
				String str="";
				for (Object[] aRow : aw) {
				    sum = sum + Double.parseDouble(aRow[0].toString());
				    sumError = sumError + Double.parseDouble(aRow[1].toString());
				    str=str+","+aRow[2].toString();
				}
				JSONObject robj=new JSONObject();
				robj.put("sum", sum);
				robj.put("sumError", sumError);
				robj.put("ids", str.substring(1,str.length()));
				return robj.toString();
			}
			else if(domain.equalsIgnoreCase("searchType5")){
				List<Object[]> aw;
			
				
				if(id==0){
					aw=  (List<Object[]>) dao.jData((int) amount, 0, "", "", "select t.data10, t.amount from FinJournal t order by t.data10 desc", null);
				}
				else{
					aw=  (List<Object[]>) dao.jData((int) amount, 0, "", "", "select t.data10, t.amount, t.id from FinJournal t where (t.data8 like '"+id+"%') or (t.data9 like '"+id+"%')  order by t.data10 desc", null);
				}
				String str="";
				Double sum=(double) 0;
				Double sumError=(double) 0;
				for (Object[] aRow : aw) {
				    sum = sum + Double.parseDouble(aRow[0].toString());
				    sumError = sumError + Double.parseDouble(aRow[1].toString());
				    str=str+","+aRow[2].toString();
				}
				JSONObject robj=new JSONObject();
				robj.put("sum", sum);
				robj.put("sumError", sumError);
				robj.put("ids", str.substring(1,str.length()));
				return robj.toString();
			}
			else if(domain.equalsIgnoreCase("searchType1")){
				List<Object[]> aw;
			
				aw=  (List<Object[]>) dao.getNativeSQLResult("select t.data10, t.amount, t.id, dbms_random.value() rnd from FIN_JOURNAL t where t.planid="+planid+" and data10>"+amount+" and ((t.data8 like '"+id+"%') or (t.data9 like '"+id+"%')) order by rnd", "list");
				
				Double sum=(double) 0;
				Double sumError=(double) 0;
				String str="";
				for (Object[] aRow : aw) {
				    sum = sum + Double.parseDouble(aRow[0].toString());
				    sumError = sumError + Double.parseDouble(aRow[1].toString());
				    str=str+","+aRow[2].toString();
				}
				JSONObject robj=new JSONObject();
				robj.put("sum", sum);
				robj.put("sumError", sumError);
				if(str.length()>0){
					robj.put("ids", str.substring(1,str.length()));
				}
				else{
					robj.put("ids", 0);
				}
				return robj.toString();
			}
			else if(domain.equalsIgnoreCase("searchType2")){
				List<Object> ass;
				List<Object[]> aw;
			
				
				if(id==0){
					aw=  (List<Object[]>) dao.jData((int) amount, 0, "", "", "select t.data10, t.amount, DBMS_RANDOM.VALUE() rnd from FinJournal t order by rnd", null);
				}
				else{
					aw=  (List<Object[]>) dao.jData((int) amount, 0, "", "", "select t.data10, t.amount, t.id, DBMS_RANDOM.VALUE() rnd from FinJournal t where (t.planid="+planid+" and  t.data8 like '"+id+"%') or (t.planid="+planid+" and  t.data9 like '"+id+"%') order by rnd", null);
				}
				
				Double sum=(double) 0;
				Double sumError=(double) 0;
				String str="";
				for (Object[] aRow : aw) {
				    sum = sum + Double.parseDouble(aRow[0].toString());
				    sumError = sumError + Double.parseDouble(aRow[1].toString());
				    str=str+","+aRow[2].toString();
				}
				JSONObject robj=new JSONObject();
				robj.put("sum", sum);
				robj.put("sumError", sumError);
				robj.put("ids", str.substring(1,str.length()));
				return robj.toString();
			}
			else if(domain.equalsIgnoreCase("searchType6")){
				List<Object[]> aw;
			
				aw=  (List<Object[]>) dao.getNativeSQLResult("select t.data10, t.amount, t.id, dbms_random.value() rnd from FIN_JOURNAL t where  (t.planid="+planid+" and t.data8 like '"+id+"%') or (t.planid="+planid+" and t.data9 like '"+id+"%') order by rnd", "list");
				
				Double sum=(double) 0;
				Double sumError=(double) 0;
				String str="";
				for (Object[] aRow : aw) {
				    sum = sum + Double.parseDouble(aRow[0].toString());
				    sumError = sumError + Double.parseDouble(aRow[1].toString());
				    str=str+","+aRow[2].toString();
				}
				JSONObject robj=new JSONObject();
				robj.put("sum", sum);
				robj.put("sumError", sumError);
				if(str.length()>0){
					robj.put("ids", str.substring(1,str.length()));
				}
				else{
					robj.put("ids", 0);
				}
				return robj.toString();
			}
			
			
			
			else if(domain.equalsIgnoreCase("accTotalAmount")){
				List<Object> aw=  (List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where (t.planid="+planid+" and  t.data8 like '"+id+"%') or (t.planid="+planid+" and  t.data9 like '"+id+"%')  group by t.data10 ", "list");
				Double sum=(double) 0;
				for (Object aRow : aw) {
				    sum = sum + Double.parseDouble(aRow.toString());
				}
				System.out.println("##"+sum);
				return String.valueOf(sum);
			}
			else if(domain.equalsIgnoreCase("amount")){
				List<Object> aw=(List<Object>) dao.getHQLResult("select sum(t.data10) from FinJournal t where t.planid="+planid+"  group by t.data10", "list");
				Double sum=(double) 0;
				for (Object aRow : aw) {
				   sum = sum + Double.parseDouble(aRow.toString());
				}				
				return String.valueOf(sum);		
			}
			else if(domain.equalsIgnoreCase("totalAccError")){
				List<Object> aw;
				if(id==0){
					 aw=  (List<Object>) dao.getHQLResult("select sum(t.amount) from FinJournal t where  (t.planid="+planid+" and t.data8 like '"+id+"%') or (t.planid="+planid+" and t.data9 like '"+id+"%')  group by t.data10 ", "list");
				}
				else{
					 aw=  (List<Object>) dao.getHQLResult("select sum(t.amount) from FinJournal t where (t.planid="+planid+" and t.data10>"+amount+" and t.data8 like '"+id+"%') or (t.planid="+planid+" and t.data10>"+amount+" and t.data9 like '"+id+"%')  group by t.amount", "list");
				}
				Double sum=(double) 0;
				for (Object aRow : aw) {
					   sum = sum + Double.parseDouble(aRow.toString());
				}				
				return String.valueOf(sum);			
			}
			else if(domain.equalsIgnoreCase("totalError")){
				 List<Object[]> aw=(List<Object[]>) dao.getHQLResult("select sum(t.a), sum(t.b), sum(t.c), sum(t.d), sum(t.e) from FinJournal t  where t.planid="+planid+" and t.a=0 or t.b=0 or t.c=0 or t.d=0 or t.e=0  group by t.a, t.b ,t.c ,t.d ,t.e  order by t.id asc", "list");
				 int sum=(int) 0;
				 for (Object[] aRow : aw) {
					 if(aRow[0].toString().equalsIgnoreCase("false")){
						 sum=sum+1;
					 }
					 if(aRow[1].toString().equalsIgnoreCase("false")){
						 sum=sum+1;
					 }
					 if(aRow[2].toString().equalsIgnoreCase("false")){
						 sum=sum+1;
					 }
					 if(aRow[3].toString().equalsIgnoreCase("false")){
						 sum=sum+1;
					 }
					 if(aRow[4].toString().equalsIgnoreCase("false")){
						 sum=sum+1;
					 }
				    //sum = sum + Integer.parseInt(aRow[0].toString())+Integer.parseInt(aRow[1].toString())+Integer.parseInt(aRow[2].toString())+Integer.parseInt(aRow[3].toString())+Integer.parseInt(aRow[4].toString());
				 }
				 System.out.println("@"+sum);
				 return String.valueOf(sum);		
			}

		}
        return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
    
    @RequestMapping(value="/export/{type}/{mid}",method=RequestMethod.GET)
	public boolean checklicense(@PathVariable String type,@PathVariable long mid,HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {
		JsonObject obj= new JsonObject();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (type.equalsIgnoreCase("nbb")){
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				FileInputStream fis = null;
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+mid+"'", "current");
				Path currentRelativePath = Paths.get("");
				String appPath = req.getServletContext().getRealPath(""); 	 
				String realpath = currentRelativePath.toAbsolutePath().toString();
				File file = new File(main.getTuuver());
				fis = new FileInputStream(file);
				
				Workbook workbook = WorkbookFactory.create(fis);
	        	FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	     					
				String xname=main.getOrgname().trim();
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
		        System.out.println("excel exported");
				return true;
	    		
			}
		}
		return false;
	}
	
    @RequestMapping(value="/survey/export/{id}",method=RequestMethod.POST)
	public boolean checklicense(@PathVariable long id, HttpServletRequest req, @RequestBody String request, HttpServletResponse response) throws JSONException, DocumentException, Exception {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			JSONObject obj= new JSONObject(request);
			MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
			Path currentRelativePath = Paths.get("");
			String realpath = currentRelativePath.toAbsolutePath().toString();
			//File file = new File(realpath+File.separator+main.getExcelurl());
			
			File file = null;
			File files1 = null;
			String appPath = req.getServletContext().getRealPath("");	

			file = new File(appPath+"/assets/zagvarfile/tuuver.xlsx");
			
			if(!file.exists()){
				return false;
			}
			else{
				fis = new FileInputStream(file);
				
				Workbook workbook = WorkbookFactory.create(fis);
	        	FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        	    Sheet sh = workbook.getSheet("sheet1"); 
        	    
        	    String str="";
        	    if(obj.getInt("searchType")==1){
        	    	str=" >"+obj.getInt("t1");
        	    }
        	    if(obj.getInt("searchType")==2){
        	    	str=obj.getInt("t2")+"%";
        	    }
        	    if(obj.getInt("searchType")==3){
        	    	str=obj.getDouble("t3")+"%";
        	    }
        	    if(obj.getInt("searchType")==4){
        	    	str=obj.getInt("t4")+"ш";
        	    }
        	    if(obj.getInt("searchType")==5){
        	    	str=obj.getInt("t5")+"ш";
        	    }
        	    
        	    Row currentRow = sh.getRow(3);
        	    if(currentRow!=null){
        			Cell cell1 = currentRow.getCell(3);
        			Cell cell2 = currentRow.getCell(8);    			
        			cell1.setCellValue(obj.getString("lavlagaa"));
        			cell2.setCellValue(obj.getLong("totalAccAmount"));
        	    }
    
    			
    			Row row4 = sh.getRow(4);
     			Cell cell3 = row4.getCell(3);
     			Cell cell4 = row4.getCell(8);
     			cell3.setCellValue(main.getOrgname());
    			cell4.setCellValue(obj.getLong("totalError"));
    			    			
    			Row row5 = sh.getRow(5);
     			Cell cell6 = row5.getCell(3);
     			Cell cell7 = row5.getCell(8);
     			if(obj.getString("searchType").equalsIgnoreCase("6")){
     				cell6.setCellValue(obj.getInt("t6"));
     			}
     			else{
     				cell6.setCellValue(obj.getString("dans"));
     			}
     			
    			cell7.setCellValue(obj.getDouble("errorPercentage"));
    			
    			Row row6 = sh.getRow(6);
     			Cell cell8 = row6.getCell(3);
     			Cell cell9 = row6.getCell(8);
     			cell8.setCellValue(obj.getInt("year"));
    			//cell9.setCellValue(obj.getString("totalAccAmount"));
    			
    			Row row7 = sh.getRow(7);
     			Cell cell10 = row7.getCell(3);
     			Cell cell11 = row7.getCell(8);
     			cell10.setCellValue(obj.getInt("psize"));
    			cell11.setCellValue(obj.getLong("totalAmount"));
    			
    			Row row8 = sh.getRow(8);
     			Cell cell12 = row8.getCell(3);
     			Cell cell13 = row8.getCell(8);
     			cell12.setCellValue(obj.getString("searchText")+" "+str);
    			cell13.setCellValue(obj.getLong("totalAccError"));
    			List<FinJournal> fin=null;
    			if(obj.has("ids")){
    				fin = (List<FinJournal>) dao.getHQLResult("from FinJournal t where t.planid='"+obj.getInt("planid")+"' and t.id in("+obj.getString("ids")+")  order by t.id desc", "list");
    			}
    			else{
    				fin = (List<FinJournal>) dao.getHQLResult("from FinJournal t where t.planid='"+obj.getInt("planid")+"' and (data8 like '"+obj.getInt("account")+"%') or (data9 like '"+obj.getInt("account")+"%') order by t.id desc", "list");
    			}
    			for(int i=13;i<sh.getLastRowNum()+1;i++){    			
    				Row r = sh.getRow(i);
    				if(r!=null){
    					sh.removeRow(r);   				
    				}					
    			}
    		
    			
        	    for(int i=0;i<fin.size();i++){
        	    	
        	    	Row row14 =null;
        	    	if(sh.getRow(i+13)==null){
        	    		row14= sh.createRow(i+13);
        	    	}else{
        	    		row14= sh.getRow(i+13);
        	    	}
        	    	Cell c1 = null;
        	    	if(row14.getCell(0)==null){
        	    		c1=row14.createCell(0);
        	    	}
        	    	else{
        	    		c1=row14.getCell(0);
        	    	}
        	    	
        	    	Cell c2 = null;
        	    	if(row14.getCell(1)==null){
        	    		c2=row14.createCell(1);
        	    	}
        	    	else{
        	    		c2=row14.getCell(1);
        	    	}
        	    	
        	    	Cell c3 = null;
        	    	if(row14.getCell(2)==null){
        	    		c3=row14.createCell(2);
        	    	}
        	    	else{
        	    		c3=row14.getCell(2);
        	    	}
        	    	
        	    	Cell c4 = null;
        	    	if(row14.getCell(3)==null){
        	    		c4=row14.createCell(3);
        	    	}
        	    	else{
        	    		c4=row14.getCell(3);
        	    	}
        	    	
        	    	Cell c5 = null;
        	    	if(row14.getCell(4)==null){
        	    		c5=row14.createCell(4);
        	    	}
        	    	else{
        	    		c5=row14.getCell(4);
        	    	}
        	    	
        	    	Cell c6 = null;
        	    	if(row14.getCell(5)==null){
        	    		c6=row14.createCell(5);
        	    	}
        	    	else{
        	    		c6=row14.getCell(5);
        	    	}
        	    	
        	    	Cell c7 = null;
        	    	if(row14.getCell(6)==null){
        	    		c7=row14.createCell(6);
        	    	}
        	    	else{
        	    		c7=row14.getCell(6);
        	    	}
        	    	
        	    	Cell c8 = null;
        	    	if(row14.getCell(7)==null){
        	    		c8=row14.createCell(7);
        	    	}
        	    	else{
        	    		c8=row14.getCell(7);
        	    	}
        	    	
        	    	Cell c9 = null;
        	    	if(row14.getCell(8)==null){
        	    		c9=row14.createCell(8);
        	    	}
        	    	else{
        	    		c9=row14.getCell(8);
        	    	}
        	    	
        	    	Cell c10 = null;
        	    	if(row14.getCell(9)==null){
        	    		c10=row14.createCell(9);
        	    	}
        	    	else{
        	    		c10=row14.getCell(9);
        	    	}
        	    	
        	    	Cell c11 = null;
        	    	if(row14.getCell(10)==null){
        	    		c11=row14.createCell(10);
        	    	}
        	    	else{
        	    		c11=row14.getCell(10);
        	    	}
        	    	
        	    	Cell c12 = null;
        	    	if(row14.getCell(11)==null){
        	    		c12=row14.createCell(11);
        	    	}
        	    	else{
        	    		c12=row14.getCell(11);
        	    	}
        	    	
        	    	Cell c13 = null;
        	    	if(row14.getCell(12)==null){
        	    		c13=row14.createCell(12);
        	    	}
        	    	else{
        	    		c13=row14.getCell(12);
        	    	}
        	    	
        	    	Cell c14 = null;
        	    	if(row14.getCell(13)==null){
        	    		c14=row14.createCell(13);
        	    	}
        	    	else{
        	    		c14=row14.getCell(13);
        	    	}
        	    	
        	    	Cell c15 = null;
        	    	if(row14.getCell(14)==null){
        	    		c15=row14.createCell(14);
        	    	}
        	    	else{
        	    		c15=row14.getCell(14);
        	    	}
        	    	        	    	        	    	
        	    	c1.setCellValue(i+1);
        	    	c2.setCellValue(fin.get(i).getData1());
        	    	c3.setCellValue(fin.get(i).getData2());
        	    	c4.setCellValue(fin.get(i).getData4());
        	    	//c5.setCellValue(fin.get(i).getData4());
        	    	c6.setCellValue(fin.get(i).getData8());
        	    	c7.setCellValue(fin.get(i).getData9());
        	    	c8.setCellValue(fin.get(i).getData10());
        	    	if(fin.get(i).isA()){
        	    		c9.setCellValue(1);
        	    	}
        	    	else{
        	    		c9.setCellValue(0);
        	    	}
        	    	if(fin.get(i).isB()){
        	    		c10.setCellValue(1);
        	    	}
        	    	else{
        	    		c10.setCellValue(0);
        	    	}
        	    	if(fin.get(i).isC()){
        	    		c11.setCellValue(1);
        	    	}
        	    	else{
        	    		c11.setCellValue(0);
        	    	}
        	    	if(fin.get(i).isD()){
        	    		c12.setCellValue(1);
        	    	}
        	    	else{
        	    		c12.setCellValue(0);
        	    	}
        	    	if(fin.get(i).isE()){
        	    		c13.setCellValue(1);
        	    	}
        	    	else{
        	    		c13.setCellValue(0);
        	    	}
        	    	
        	    	c14.setCellValue(fin.get(i).getAmount());
        	    	c15.setCellValue(fin.get(i).getDescription());       	    	
        	    }
		  
    			fis.close();
				String uuid = UUID.randomUUID().toString()+".xlsx";
	            FileOutputStream out = new FileOutputStream(appPath+"/uploads"+ File.separator+uuid);
	            workbook.write(out);
				out.close();
			
				main.setTuuver(appPath+"/uploads"+ File.separator+uuid);
				
				dao.PeaceCrud(main, "MainAuditRegistration", "update",(long) main.getId(), 0, 0, null);
		        System.out.println("excel uploaded");
				return true;
			}
    		
		}
		return false;
	}

}
