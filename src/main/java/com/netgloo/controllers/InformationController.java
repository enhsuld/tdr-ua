package com.netgloo.controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import com.google.gson.Gson;
import com.netgloo.IzrApplication;
import com.netgloo.dao.UserDao;
import com.netgloo.models.DataSourceResult;
import com.netgloo.service.FileUploadService;
import com.netgloo.models.FileUpload;
import com.netgloo.models.LnkFeedbackCategory;
import com.netgloo.models.LnkNewsAfile;
import com.netgloo.models.LnkNewstype;
import com.netgloo.models.LnkRiskLaw;
import com.netgloo.models.LnkRiskdir;
import com.netgloo.models.LnkUserrole;
import com.netgloo.models.LutAccplan;
import com.netgloo.models.LutAct;
import com.netgloo.models.LutAuditDir;
import com.netgloo.models.LutCategory;
import com.netgloo.models.LutConflict;
import com.netgloo.models.LutDepartment;
import com.netgloo.models.LutEcoclass;
import com.netgloo.models.LutFactor;
import com.netgloo.models.LutFinblcname;
import com.netgloo.models.LutFinblcperf;
import com.netgloo.models.LutIndependent;
import com.netgloo.models.LutLaw;
import com.netgloo.models.LutNewstype;
import com.netgloo.models.LutNotice;
import com.netgloo.models.LutProcedure;
import com.netgloo.models.LutRequirement;
import com.netgloo.models.LutRisk;
import com.netgloo.models.LutUser;
import com.netgloo.models.LutNews;
import com.netgloo.models.SubAdvice;
import com.netgloo.models.SubAuditOrganization;
import com.netgloo.repo.LnkMenuRepository;
import com.netgloo.repo.MenuRepository;
import com.netgloo.repo.RoleRepository;
import com.netgloo.repo.UserRepository;


@RestController
public class InformationController {
/*
    @Autowired
    private UserDao userDao;*/
	
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
    
    @Autowired
    FileUploadService fileUploadService;
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @RequestMapping(value = "/info/resource/{domain}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String tree(@PathVariable String domain) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser=(LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			
			JSONArray arr=new JSONArray();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				
				if(domain.equalsIgnoreCase("LutLawRisk")){
					 List<LutLaw> rs=(List<LutLaw>) dao.getHQLResult("from LutLaw t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLawname());
							obj.put("zaalt", rs.get(i).getZaalt());
						 	obj.put("lawcategory", rs.get(i).getLawcategory());
						 	if(rs.get(i).getParentid()==null){
						 		obj.put("parent", true);
						 	}
						 	else{
						 		obj.put("parent", false);
						 	}
			        		arr.put(obj);        	
			        	}		
				}
				
				if(domain.equalsIgnoreCase("LutLaw")){
					 List<LutLaw> rs=(List<LutLaw>) dao.getHQLResult("from LutLaw t where t.lawcategory=1 and t.parentid=null", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLawname());
							obj.put("zaalt", rs.get(i).getZaalt());
						 	obj.put("lawcategory", rs.get(i).getLawcategory());
						 	if(rs.get(i).getParentid()==null){
						 		obj.put("parent", true);
						 	}
						 	else{
						 		obj.put("parent", false);
						 	}
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("Lutstandart")){
					 List<LutLaw> rs=(List<LutLaw>) dao.getHQLResult("from LutLaw t where t.lawcategory=2 and t.parentid=null", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLawname());
							obj.put("zaalt", rs.get(i).getZaalt());
						 	obj.put("lawcategory", rs.get(i).getLawcategory());
						 	if(rs.get(i).getParentid()==null){
						 		obj.put("parent", true);
						 	}
						 	else{
						 		obj.put("parent", false);
						 	}
			        		arr.put(obj);        	
			        	}		
				}
				/*if(domain.equalsIgnoreCase("LutLawr")){
					 List<LutLaw> rs=(List<LutLaw>) dao.getHQLResult("from LutLaw t", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLawname());
						 	obj.put("lawcategory", rs.get(i).getLawcategory());
						 	if(rs.get(i).getParentid()==null){
						 		obj.put("parentid", 0);
						 	}
						 	else{
						 		obj.put("parentid", rs.get(i).getParentid());
						 		
						 	}						 	
			        		arr.put(obj);        	
			        	}		
				}*/
				
				if(domain.equalsIgnoreCase("LutAuditDir")){
					 List<LutAuditDir> rs=(List<LutAuditDir>) dao.getHQLResult("from LutAuditDir t ", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("LutNewstype")){
					 List<LutNewstype> rs=(List<LutNewstype>) dao.getHQLResult("from LutNewstype t order by t.id asc ", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getNewstypename());			        		
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("SubAuditOrganization")){
					 List<SubAuditOrganization> rs=(List<SubAuditOrganization>) dao.getHQLResult("from SubAuditOrganization t ", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getOrgname());			        		
			        		arr.put(obj);        	
			        	}		
				}
			
				if(domain.equalsIgnoreCase("LutNews")){
					List<LutNews> rs=(List<LutNews>) dao.getHQLResult("from LutNews t where t.showid=0  order by t.id desc", "list");
					if(loguser.getIsstate()==true){
						rs=(List<LutNews>) dao.getHQLResult("from LutNews t where t.showid in (0,1) order by t.id desc", "list");
					}
					else{
						rs=(List<LutNews>) dao.getHQLResult("from LutNews t where t.showid=0  order by t.id desc", "list");
					}
					 
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("id", rs.get(i).getId());
						 	obj.put("newstitle", rs.get(i).getNewstitle());
						 	obj.put("newstext", rs.get(i).getNewstext());
						 	obj.put("imgurl", rs.get(i).getImgurl());
						 	obj.put("ishighlight", rs.get(i).getIshighlight());
						 	obj.put("newsdate", rs.get(i).getNewsdate());
						 	/*List<LnkNewstype> cats=(List<LnkNewstype>) dao.getHQLResult("from LnkNewstype t where t.newsid='"+rs.get(i).getId()+"'", "list");
						 	JSONArray carr=new JSONArray();
						 	for(int a=0;a<cats.size();a++){
						 		JSONObject cobj=new JSONObject();
						 		cobj.put("newstypeid", cats.get(a).getNewstypeid());
						 		
						 		carr.put(cobj);
						 	}
						 	
						 	obj.put("cats", carr);*/
						 	
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

    
    
    @RequestMapping(value = "/info/newsfilter/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String newslist(@PathVariable String id) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				
				 //String nids= new String();
				 JSONObject ids=new JSONObject();  
				
				 JSONArray filter=new JSONArray();
				 
				 List<LnkNewstype> filtered=(List<LnkNewstype>) dao.getHQLResult("from LnkNewstype t where t.newstypeid='"+id+"'", "list");
				 for(int b=0;b<filtered.size();b++){
				 					 		
				 		filter.put(filtered.get(b).getNewsid());				 		
				 	}			 			 
				 
				 String nids = filter.toString();
				 Integer last= nids.length();
				 Integer lst = last.sum(last, -1);
				 List<LutNews> rs=(List<LutNews>) dao.getHQLResult("from LutNews t where t.id in ("+nids.substring(1, lst)+")", "list");
				 for(int i=0;i<rs.size();i++){
					 	JSONObject obj=new JSONObject();      	
					 	obj.put("id", rs.get(i).getId());
					 	obj.put("newstitle", rs.get(i).getNewstitle());
					 	obj.put("newstext", rs.get(i).getNewstext());
					 	obj.put("imgurl", rs.get(i).getImgurl());
					 	obj.put("newsdate", rs.get(i).getNewsdate());
					 /*	List<LnkNewstype> cats=(List<LnkNewstype>) dao.getHQLResult("from LnkNewstype t where t.newsid='"+rs.get(i).getId()+"'", "list");
					 	JSONArray carr=new JSONArray();
					 	for(int a=0;a<cats.size();a++){
					 		JSONObject cobj=new JSONObject();
					 		cobj.put("newstypeid", cats.get(a).getNewstypeid());
					 		
					 		carr.put(cobj);
					 	}
					 	
					 	obj.put("cats", carr);*/
					 	
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
    
    

	
	@RequestMapping(value = "/info/list/{domain}", method= RequestMethod.POST)
    public @ResponseBody DataSourceResult customers(@PathVariable String domain, @RequestBody String request, HttpServletRequest req) throws HttpRequestMethodNotSupportedException {
		Long count=(long) 0;
		List<?> rs = null;		

		DataSourceResult result = new DataSourceResult();	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			if(domain.equalsIgnoreCase("LutLaw")){
				List<LutLaw> wrap = new ArrayList<LutLaw>();
				
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutLaw or=(LutLaw) rs.get(i);
					LutLaw cor=new LutLaw();
					cor.setId(or.getId());
					cor.setLawname(or.getLawname());
					cor.setDescribe(or.getDescribe());
					cor.setParentid(or.getParentid());					
					cor.setZaalt(or.getZaalt());
					cor.setLawcategory(or.getLawcategory());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			
			else if(domain.equalsIgnoreCase("LutNews")){
				//List<LutRisk> wrap = new ArrayList<LutRisk>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
			
				result.setData(rs);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutRisk")){
				List<LutRisk> wrap = new ArrayList<LutRisk>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
			
				result.setData(rs);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("SubAdvice")){
				List<SubAdvice> wrap = new ArrayList<SubAdvice>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					SubAdvice or=(SubAdvice) rs.get(i);
					SubAdvice cor=new SubAdvice();
					cor.setId(or.getId());
					cor.setDescription(or.getDescription());
					cor.setGivendate(or.getGivendate());
					cor.setAuditorid(or.getAuditorid());				
					cor.setOrgid(or.getOrgid());
					cor.setPlanid(or.getPlanid());
					cor.setIsimplemented(or.getIsimplemented());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutConflict")){
				List<LutConflict> wrap = new ArrayList<LutConflict>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutConflict or=(LutConflict) rs.get(i);
					LutConflict cor=new LutConflict();
					cor.setId(or.getId());
					cor.setConflict(or.getConflict());
					cor.setAccount(or.getAccount());
					cor.setTotal(or.getTotal());				
					cor.setAuditorid(or.getAuditorid());
					cor.setOrgid(or.getOrgid());
					cor.setResolve(or.getResolve());
					cor.setConfdate(or.getConfdate());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutProcedure")){
				List<LutProcedure> wrap = new ArrayList<LutProcedure>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutProcedure or=(LutProcedure) rs.get(i);
					LutProcedure cor=new LutProcedure();
					cor.setId(or.getId());
					cor.setAdescribe(or.getAdescribe());
					cor.setAprocedure(or.getAprocedure());
					cor.setStandart(or.getStandart());				
					cor.setWay(or.getWay());					
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutNotice")){
				List<LutNotice> wrap = new ArrayList<LutNotice>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutNotice or=(LutNotice) rs.get(i);
					LutNotice cor=new LutNotice();
					cor.setId(or.getId());
					cor.setAdescribe(or.getAdescribe());
					cor.setName(or.getName());
					cor.setShortname(or.getShortname());									
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutAccplan")){
			List<LutAccplan> wrap = new ArrayList<LutAccplan>();

			rs= dao.kendojson(request, domain);
			count=(long) dao.resulsetcount(request, domain);
			
			for(int i=0;i<rs.size();i++){
				LutAccplan or=(LutAccplan) rs.get(i);
				LutAccplan cor=new LutAccplan();
				cor.setId(or.getId());
				cor.setAcccode(or.getAcccode());
				cor.setAccname(or.getAccname());
				cor.setFreebalance(or.getFreebalance());
				cor.setFiscal(or.getFiscal());
				wrap.add(cor);
			}
			result.setData(wrap);	
			result.setTotal((long) count);
		}
			else if(domain.equalsIgnoreCase("LutFinblcperf")){
				List<LutFinblcperf> wrap = new ArrayList<LutFinblcperf>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutFinblcperf or=(LutFinblcperf) rs.get(i);
					LutFinblcperf cor=new LutFinblcperf();
					cor.setId(or.getId());
					cor.setCode(or.getCode());
					cor.setName(or.getName());
					cor.setAcc(or.getAcc());					
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutFinblcname")){
				List<LutFinblcname> wrap = new ArrayList<LutFinblcname>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutFinblcname or=(LutFinblcname) rs.get(i);
					LutFinblcname cor=new LutFinblcname();
					cor.setId(or.getId());
					cor.setCode(or.getCode());
					cor.setName(or.getName());										
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutEcoclass")){
				List<LutEcoclass> wrap = new ArrayList<LutEcoclass>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutEcoclass or=(LutEcoclass) rs.get(i);
					LutEcoclass cor=new LutEcoclass();
					cor.setId(or.getId());
					cor.setCode(or.getCode());
					cor.setName(or.getName());										
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutIndependent")){
				List<LutIndependent> wrap = new ArrayList<LutIndependent>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutIndependent or=(LutIndependent) rs.get(i);
					LutIndependent cor=new LutIndependent();
					cor.setId(or.getId());
					cor.setReg(or.getReg());
					cor.setName(or.getName());
					cor.setLicexpiredate(or.getLicexpiredate());
					cor.setLicnum(or.getLicnum());
					cor.setPhone(or.getPhone());
					cor.setWeb(or.getWeb());
					cor.setAddress(or.getAddress());
					cor.setIsactive(or.getIsactive());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutAct")){
				List<LutAct> wrap = new ArrayList<LutAct>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutAct or=(LutAct) rs.get(i);
					LutAct cor=new LutAct();
					cor.setId(or.getId());
					cor.setZaalt(or.getZaalt());
					cor.setActdescribe(or.getActdescribe());
										
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutRequirement")){
				List<LutRequirement> wrap = new ArrayList<LutRequirement>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutRequirement or=(LutRequirement) rs.get(i);
					LutRequirement cor=new LutRequirement();
					cor.setId(or.getId());
					cor.setZaalt(or.getZaalt());
					cor.setAdescribe(or.getAdescribe());
										
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}

			else if(domain.equalsIgnoreCase("LutCategory")){
				List<LutCategory> wrap = new ArrayList<LutCategory>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutCategory or=(LutCategory) rs.get(i);
					LutCategory cor=new LutCategory();
					cor.setId(or.getId());
					cor.setCategoryname(or.getCategoryname());					
										
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			else if(domain.equalsIgnoreCase("LutNewstype")){
				List<LutNewstype> wrap = new ArrayList<LutNewstype>();

				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				for(int i=0;i<rs.size();i++){
					LutNewstype or=(LutNewstype) rs.get(i);
					LutNewstype cor=new LutNewstype();
					cor.setId(or.getId());
					cor.setNewstypename(or.getNewstypename());					
										
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal((long) count);
			}
			
			return  result;
		}
		return null;
	}
	
	@RequestMapping(value="/info/lawadd/{id}", method=RequestMethod.PUT)
	public @ResponseBody String ajaxo(@RequestBody String jsonString,@PathVariable int id) throws JSONException, MessagingException{
		JSONObject obj= new JSONObject(jsonString); 
		JSONObject result= new JSONObject();		 
		
		if(obj.getLong("id")==0){
			LutLaw norg= new LutLaw();				
			norg.setLawname(obj.getString("lawname"));
			norg.setZaalt(obj.getString("zaalt"));		
			norg.setLawcategory(obj.getLong("lawcategory"));

			if(obj.has("parentid3")){
				norg.setParentid(obj.getString("parentid3"));
			}
			else{
				if(obj.has("parentid2")){norg.setParentid(obj.getString("parentid2"));}
				else{
					if(obj.has("parentid1")){norg.setParentid(obj.getString("parentid1"));}
					else{
						if(obj.has("parentid")){norg.setParentid(obj.getString("parentid"));}
						else{}
						}
				}
			}			
			dao.PeaceCrud(norg, "LutLaw", "save", (long) 0, 0, 0, null);
			
			 result.put("re", 0);
			
			
		}
		else{		 
			LutLaw norg=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+id+"'", "current");
			//LutLaw norg= new LutLaw();
			norg.setLawname(obj.getString("lawname"));
			if(obj.has("zaalt")){
				norg.setZaalt(obj.getString("zaalt"));	
			}							

			if(obj.has("parentid3")){
				norg.setParentid(obj.getString("parentid3"));
			}
			else{
				if(obj.has("parentid2")){norg.setParentid(obj.getString("parentid2"));}
				else{
					if(obj.has("parentid1")){norg.setParentid(obj.getString("parentid1"));}
					else{
						if(obj.has("parentid")){norg.setParentid(obj.getString("parentid"));}
						else{}
						}
				}
			}		
			dao.PeaceCrud(norg, "LutLaw", "update", (obj.getLong("id")), 0, 0, null);
			
			result.put("re", 1);
	}
		
		return result.toString(); 
		
		 
		 
	 }
	 @RequestMapping(value = "/info/sel/{domain}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
     public @ResponseBody String trees(@PathVariable String domain,@PathVariable int id) {
		
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();			
			if (!(auth instanceof AnonymousAuthenticationToken)) {				
				
				if(domain.equalsIgnoreCase("editlaw")){							
					LutLaw org=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+id+"'", "current");					
					JSONObject obj=new JSONObject();  
					obj.put("id", org.getId());
					obj.put("lawname", org.getLawname());
					obj.put("zaalt", org.getZaalt());				
					obj.put("describe", org.getDescribe());	
					obj.put("lawcategory", org.getLawcategory());	
					System.out.println(org.getParentid() + " 02020202");
					if(org.getParentid()!=null){
						LutLaw plaw=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+org.getParentid()+"'", "current");
						System.out.println(plaw.getParentid() + " 21212121");
						if(plaw.getParentid()==null){
							obj.put("parentid", org.getParentid());	
							System.out.println(obj + " ###########");
						}
						else{
							LutLaw plaw1=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+plaw.getParentid()+"'", "current");
							if(plaw1.getParentid()==null){
								obj.put("parentid", plaw.getParentid());
								obj.put("parentid1", org.getParentid());
								System.out.println(obj + " !!!!!!!!!!");
							}					
							else{
								obj.put("parentid", plaw1.getParentid());
								obj.put("parentid1", plaw.getParentid());
								obj.put("parentid2", org.getParentid());								
							}							
						}					
					}
					else{
						//obj.put("parentid", null);
					}
					
					return obj.toString();
					
				}	
				if(domain.equalsIgnoreCase("parent")){	
					List<LutLaw> laws=(List<LutLaw>) dao.getHQLResult("from LutLaw t where t.parentid='"+id+"' order by t.lawname asc", "list");						
					for(int i=0;i<laws.size();i++){
						LutLaw org=(LutLaw) laws.get(i);
						JSONObject obj=new JSONObject();
						obj.put("value", org.getId());
						obj.put("text", org.getLawname());						
						arr.put(obj);
					}
					
					return arr.toString();
					
				}
				if(domain.equalsIgnoreCase("laws")){	
					LutLaw plaw1=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+id+"'", "current");
					JSONObject obj=new JSONObject();
					if(plaw1.getParentid()!=null){
						LutLaw pa1=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+plaw1.getParentid()+"'", "current");
						LutLaw pa2=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+pa1.getParentid()+"'", "current");
						if(pa2.getParentid()==null){
							obj.put("value", id);
							obj.put("text2", pa2.getLawname());
							obj.put("text1", pa1.getLawname());										
							obj.put("text", plaw1.getLawname());
							obj.put("text", pa2.getLawname()+" - "+ pa1.getLawname()+" - "+plaw1.getZaalt()+" "+plaw1.getLawname());
							obj.put("lawcategory", plaw1.getLawcategory());
						}
						else{													
							LutLaw pa3=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+pa2.getParentid()+"'", "current");
							obj.put("value", id);
							obj.put("text3", pa3.getLawname());
							obj.put("text2", pa2.getLawname());
							obj.put("text1", pa1.getLawname());										
							obj.put("text", plaw1.getLawname());
							obj.put("text", pa3.getLawname()+" - "+ pa2.getLawname()+" - "+pa1.getZaalt()+" "+pa1.getLawname()+" - "+plaw1.getZaalt()+" "+plaw1.getLawname());
							obj.put("lawcategory", plaw1.getLawcategory());
						}
						
					}
					else{
						obj.put("value", id);
						obj.put("text", plaw1.getLawname());
						obj.put("lawcategory", plaw1.getLawcategory());
					}				
					
					
					arr.put(obj);
					return arr.toString();					
				}
				if(domain.equalsIgnoreCase("newsmore")){							
					LutNews more=(LutNews) dao.getHQLResult("from LutNews t where t.id='"+id+"'", "current");					
					JSONObject obj=new JSONObject();      	
				 	obj.put("id", more.getId());
				 	obj.put("newstitle", more.getNewstitle());
				 	obj.put("newstext", more.getNewstext());
				 	obj.put("imgurl", more.getImgurl());
				 	obj.put("newsdate", more.getNewsdate());				 	
				 	List<LnkNewstype> cats=(List<LnkNewstype>) dao.getHQLResult("from LnkNewstype t where t.newsid='"+id+"'", "list");
				 	JSONArray carr=new JSONArray();
				 	for(int a=0;a<cats.size();a++){
				 		JSONObject cobj=new JSONObject();
				 		cobj.put("newstypeid", cats.get(a).getNewstypeid());
				 		
				 		carr.put(cobj);
				 		
				 	}
				 	
				 	obj.put("cats", carr);
				 	
				 	List<LnkNewsAfile> afiles=(List<LnkNewsAfile>) dao.getHQLResult("from LnkNewsAfile t where t.newsid='"+id+"'", "list");
				 	if(afiles.size()>0){
				 		JSONArray lfiles=new JSONArray();
					 	for(int a=0;a<afiles.size();a++){
					 		JSONObject aobj=new JSONObject();
					 		aobj.put("filename", afiles.get(a).getAfilename());
					 		aobj.put("fileext", afiles.get(a).getAfileext());
					 		aobj.put("fileurl", afiles.get(a).getAfileurl());
					 		aobj.put("filesize", afiles.get(a).getAfilesize());
					 		lfiles.put(aobj);
					 		
					 	}
					 	obj.put("afls", lfiles);
				 	}
				 	else{
				 		obj.put("afls", "no");
				 	}
				 	
				 	 
					return obj.toString();
					
				}
				if(domain.equalsIgnoreCase("recentnews")){
					 List<LutNews> rs=(List<LutNews>) dao.getHQLResult("from LutNews t where t.id!='"+id+"' order by  t.newsdate desc", "list");
					 
					 for(int i=0;i<rs.size();i++){
						 if(i<6){
							 JSONObject obj=new JSONObject();      	
							 	obj.put("id", rs.get(i).getId());
							 	obj.put("newstitle", rs.get(i).getNewstitle());	
							 	obj.put("newsdate", rs.get(i).getNewsdate());
				        		arr.put(obj);				        		
						 }						 	
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
	
	 	@ResponseBody 
	 	@ResponseStatus(HttpStatus.OK)
		@RequestMapping(value="/info/ndelete/{id}",method=RequestMethod.DELETE)
		public String delete(@PathVariable long id, HttpServletRequest req) throws ClassNotFoundException, JSONException{
			try {
				
				//String appPath = req.getSession().getServletContext().getRealPath("");
			 	
			 	 LutNews lr=(LutNews) dao.getHQLResult("from LutNews t where t.id='"+id+"'", "current"); 
	    		 String delPath = lr.getImgurl();
				 System.out.println(lr.getImgurl() + " is deleted!");
			
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
		   		List<LnkNewstype> ncat=(List<LnkNewstype>) dao.getHQLResult("from LnkNewstype t where t.newsid='"+id+"'", "list");
				for(int f=0;f<ncat.size();f++){						
					dao.PeaceCrud(null, "LnkNewstype", "delete", (long) ncat.get(f).getId(), 0, 0, null);	    							
				}
				
				dao.PeaceCrud(null, "LutNews", "delete", id, 0, 0, null);		
					
			}
			catch (Exception e) {
				
			}
			return "true";
		}
	 
	 
	 @RequestMapping(value="/info/Riskadd/{id}", method=RequestMethod.PUT)
		public @ResponseBody String ajaxuser(@RequestBody String jsonString) throws JSONException, MessagingException{
			JSONObject obj= new JSONObject(jsonString); 
			JSONObject result= new JSONObject();
			
		
			if(obj.getLong("id")==0){
				LutRisk norg= new LutRisk();				
				norg.setRiskname(obj.getString("riskname"));
				norg.setRisktype(obj.getInt("type"));
				//norg.setOther(obj.getLong("other"));
				norg.setOthertext(obj.getString("othertext"));
				dao.PeaceCrud(norg, "LutRisk", "save", (long) 0, 0, 0, null);
				
				JSONArray arr= obj.getJSONArray("dir");
				
				for(int a=0;a<arr.length();a++){
					LnkRiskdir rl= new LnkRiskdir();			
				    rl.setDirid(arr.getLong(a));
				    rl.setRiskid(norg.getId());
				    dao.PeaceCrud(rl, "LnkRiskdir", "save", (long) 0, 0, 0, null);			
					
				}
				
				JSONArray larr= obj.getJSONArray("risklaws");
				
				for(int a=0;a<larr.length();a++){
					JSONObject lao= larr.getJSONObject(a);
					LnkRiskLaw rl= new LnkRiskLaw();			
				    rl.setLawid(Long.parseLong(lao.getString("lawid")));
				    rl.setRiskid(norg.getId());
				    rl.setLname(lao.getString("lname"));
				    rl.setLzaalt(lao.getString("lzaalt"));
				    rl.setLzuil(lao.getString("lzuil"));
				    rl.setZaalt(lao.getLong("zaalt"));
				    rl.setZuilid(lao.getLong("zuilid"));
				    rl.setLawcategory(1);
				    dao.PeaceCrud(rl, "LnkRiskLaw", "save", (long) 0, 0, 0, null);			
					
				}
				
				JSONArray sarr= obj.getJSONArray("riskstandarts");
				
				for(int a=0;a<sarr.length();a++){
					JSONObject lao= sarr.getJSONObject(a);
					LnkRiskLaw rl= new LnkRiskLaw();			
					 rl.setLawid(Long.parseLong(lao.getString("lawid")));
				    rl.setRiskid(norg.getId());
				    rl.setLname(lao.getString("lname"));
				    rl.setLzaalt(lao.getString("lzaalt"));
				    rl.setLzuil(lao.getString("lzuil"));
				    rl.setZaalt(lao.getLong("zaalt"));
				    rl.setZuilid(lao.getLong("zuilid"));
				    rl.setLawcategory(2);
				    dao.PeaceCrud(rl, "LnkRiskLaw", "save", (long) 0, 0, 0, null);			
					
				}
				result.put("re", 0);
				
				
			}
			else{			
				LutRisk norg = (LutRisk) dao.getHQLResult("from LutRisk t where t.id='"+obj.getLong("id")+"'", "current"); 
				//LutUser norg= new LutUser();				
				norg.setRiskname(obj.getString("riskname"));
				norg.setRisktype(obj.getInt("type"));
				//norg.setOther(obj.getLong("other"));
				norg.setOthertext(obj.getString("othertext"));
				
				if(obj.has("dir")){		
					List<LnkRiskdir> ncat=(List<LnkRiskdir>) dao.getHQLResult("from LnkRiskdir t where t.riskid='"+obj.getLong("id")+"'", "list");
					for(int f=0;f<ncat.size();f++){						
						dao.PeaceCrud(null, "LnkRiskdir", "delete", (long) ncat.get(f).getId(), 0, 0, null);	    							
					}
					
					JSONArray arr= obj.getJSONArray("dir");
					
					System.out.println("@@@@@@"+obj.getJSONArray("dir"));
					if(arr.length()>0){					
												
						for(int a=0;a<arr.length();a++){					
							LnkRiskdir rusr=new LnkRiskdir();	
							rusr.setDirid(arr.getLong(a));
							rusr.setRiskid(norg.getId());
							dao.PeaceCrud(rusr, "LnkRiskdir", "save",  (long) 0, 0, 0, null);	
					    		
							
						}
					}
				
				}	
				dao.PeaceCrud(null, "LnkRiskLaw", "delete", (long) norg.getId(), 0, 0, "riskid");	
				JSONArray larr= obj.getJSONArray("risklaws");
				
				for(int a=0;a<larr.length();a++){
					JSONObject lao= larr.getJSONObject(a);
					List<LnkRiskLaw> ncat=(List<LnkRiskLaw>) dao.getHQLResult("from LnkRiskLaw t where t.riskid='"+obj.getLong("id")+"' and t.lawid="+lao.getLong("lawid")+" and t.zuilid="+lao.getLong("zuilid")+" and t.zaalt="+lao.getLong("zaalt")+"", "list");
					if(ncat.size()==0){
						LnkRiskLaw rl= new LnkRiskLaw();			
						rl.setLawid(lao.getLong("lawid"));
					    rl.setRiskid(norg.getId());
					    rl.setLname(lao.getString("lname"));
					    rl.setLzaalt(lao.getString("lzaalt"));
					    rl.setLzuil(lao.getString("lzuil"));
					    rl.setZaalt(lao.getLong("zaalt"));
					    rl.setZuilid(lao.getLong("zuilid"));
					    rl.setLawcategory(1);
					    dao.PeaceCrud(rl, "LnkRiskLaw", "save", (long) 0, 0, 0, null);	
					}
				}
				
				
				JSONArray sarr= obj.getJSONArray("riskstandarts");
				if(sarr.length()>0){
					for(int a=0;a<sarr.length();a++){
						JSONObject lao= sarr.getJSONObject(a);
						List<LnkRiskLaw> ncat=(List<LnkRiskLaw>) dao.getHQLResult("from LnkRiskLaw t where t.riskid='"+obj.getLong("id")+"' and t.lawid="+lao.getLong("lawid")+" and t.zuilid="+lao.getLong("zuilid")+" and t.zaalt="+lao.getLong("zaalt")+"", "list");
						if(ncat.size()==0){
							LnkRiskLaw rl= new LnkRiskLaw();			
							rl.setLawid(lao.getLong("lawid"));
						    rl.setRiskid(norg.getId());
						    rl.setLname(lao.getString("lname"));
						    rl.setLzaalt(lao.getString("lzaalt"));
						    rl.setLzuil(lao.getString("lzuil"));
						    rl.setZaalt(lao.getLong("zaalt"));
						    rl.setZuilid(lao.getLong("zuilid"));
						    rl.setLawcategory(2);
						    dao.PeaceCrud(rl, "LnkRiskLaw", "save", (long) 0, 0, 0, null);	
						}
					}
				}
				
				dao.PeaceCrud(norg, "LutRisk", "update", obj.getLong("id"), 0, 0, null);
				
				result.put("re", 1);
		}
			
			return result.toString(); 	 
			 
		 }
	 
	 
	 @RequestMapping(value = "/info/remove/newsafile/{id}", method = RequestMethod.GET)
	    public @ResponseBody String removeItem(@PathVariable long id) {
			dao.PeaceCrud(null, "LnkNewsAfile", "delete", (long) id, 0, 0, null);
			return "true";		
		}
	


	@RequestMapping(value="/info/Newsadd",method=RequestMethod.POST)
		public ResponseEntity<LutNews> Newsadd( @RequestParam(value="filess", required=false) MultipartFile files,@RequestParam("obj") String jsonstring, MultipartRequest mpr, HttpServletRequest req) throws JSONException, DocumentException, Exception {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
		     MultipartFile mfile =  null;
		  	 mfile = (MultipartFile)files;
		  	 JSONObject obj= new JSONObject(jsonstring);
		  	 
		  	 String appPath = req.getServletContext().getRealPath(""); 	 
		  	
		  	 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser=(LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				 	    
			 String SAVE_DIR =appPath+"/" +IzrApplication.ROOT+ "/" +loguser.getUsername()+"/"+"newspics" ;
     	     String DOW_DIR = "uploads" + "/" +loguser.getUsername()+"/"+"newspics";
     	     
     	     System.out.println("0000000000000000   "+obj);
     	    if(obj.has("removeFile")){}
     	     else{
     	    	 mfile=null;
     	     }
      	    
      	     
      	     int afile=0;
      	     
			 if(obj.getInt("id")==0){
				 if (mfile != null) {
					 FileUpload rupload= fileUploadService.uploadFile(mfile, SAVE_DIR,DOW_DIR); 
							Date d1 = new Date();
							SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
							String special = df.format(d1);		
			        		LutNews lp = new LutNews();	        			
		        			lp.setImgurl(rupload.getFileurl());
		        			lp.setNewstitle(obj.getString("newstitle"));
		        			lp.setNewstext(obj.getString("newstext"));
		        			
		        			if(obj.has("showid")){
		        				if(obj.getBoolean("showid")==false){
		        					lp.setShowid(0);
		        				}
		        				else{
		        					lp.setShowid(1);
		        				}		        						        				
		        				}
		        			else{
		        				lp.setShowid(0);
		        				}
		        			
		        			if(obj.has("ishighlight")){
		        				lp.setIshighlight(obj.getBoolean("ishighlight"));		        				
		        				}
		        			else{
		        				lp.setIshighlight(false);
		        				}		        			
		        			lp.setNewsdate(special);
		        			if(obj.has("isfiles")){
		        				lp.setIsfile(obj.getBoolean("isfiles")); 
		        				afile=1;
		        				}
		        			else{
		        				lp.setIsfile(false);
		        				afile=0;
		        				}	
		        			lp.setUserid(loguser.getId());
		        			dao.PeaceCrud(lp, "LutNews", "save", (long) 0, 0, 0, null);	     
		        			
		        			
		    				JSONArray arr= obj.getJSONArray("cats");
		    				
		    				for(int a=0;a<arr.length();a++){				
		    						
		    					LnkNewstype rl= new LnkNewstype();			    					
		    				    rl.setNewstypeid(arr.getLong(a));
		    				    rl.setNewsid(lp.getId());
		    				    dao.PeaceCrud(rl, "LnkNewstype", "save", (long) 0, 0, 0, null);			
		    					
		    				}
		    				if(afile==1 && obj.getBoolean("isfiles")==true){
		    					if(obj.has("afiless")){
		    						JSONArray arraf= obj.getJSONArray("afiless");
				    				for(int a=0;a<arraf.length();a++){				
				    						
				    					LnkNewsAfile af= new LnkNewsAfile();		
				    					JSONObject afnu = (JSONObject) arraf.get(a); 
				    					af.setAfilename(afnu.getString("filename") );
				    					af.setAfileurl(afnu.getString("fileurl"));
				    					af.setAfileext(afnu.getString("extension"));
				    					af.setAfilesize(afnu.getLong("fsize"));
				    					af.setNewsid(lp.getId());
				    				    dao.PeaceCrud(af, "LnkNewsAfile", "save", (long) 0, 0, 0, null);			
				    					
				    				}
		    					}
		    					
		    				}
		    			
			               
		        			 return new ResponseEntity<LutNews>(lp, HttpStatus.OK);
			        	}    
				 
				 	else{	
								Date d1 = new Date();
								SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
								String special = df.format(d1);																	        		
				        		 
				        		LutNews lp = new LutNews();		        					        			
			        			lp.setNewstitle(obj.getString("newstitle"));
			        			lp.setNewstext(obj.getString("newstext"));
			        			lp.setIshighlight(obj.getBoolean("ishighlight"));
			        			lp.setNewsdate(special);			        			
			        			if(obj.has("isfiles")){
			        				lp.setIsfile(obj.getBoolean("isfiles")); 
			        				afile=1;
			        				}
			        			else{
			        				lp.setIsfile(false);
			        				afile=0;
			        				}
			        			if(obj.has("showid")){
			        				if(obj.getBoolean("showid")==false){
			        					lp.setShowid(0);
			        				}
			        				else{
			        					lp.setShowid(1);
			        				}		        						        				
			        				}
			        			else{
			        				lp.setShowid(0);
			        				}
			        			lp.setUserid(loguser.getId());
			        			dao.PeaceCrud(lp, "LutNews", "save", (long) 0, 0, 0, null);	     
			        			

			    				
			    				
			    				if(afile==1 && obj.getBoolean("isfiles")==true){
			    					if(obj.has("afiless")){
			    						JSONArray arraf= obj.getJSONArray("afiless");
			    						for(int a=0;a<arraf.length();a++){				
				    						
					    					LnkNewsAfile af= new LnkNewsAfile();		
					    					JSONObject afnu = (JSONObject) arraf.get(a); 
					    					af.setAfilename(afnu.getString("filename") );
					    					af.setAfileurl(afnu.getString("fileurl"));
					    					af.setAfileext(afnu.getString("extension"));
					    					af.setAfilesize(afnu.getLong("fsize"));
					    					af.setNewsid(lp.getId());
					    				    dao.PeaceCrud(af, "LnkNewsAfile", "save", (long) 0, 0, 0, null);			
					    					
					    				}
			    					}
			    					
			    				}
			    				JSONArray arr= obj.getJSONArray("cats");
			    				
			    				for(int a=0;a<arr.length();a++){				
			    						
			    					LnkNewstype rl= new LnkNewstype();			    					
			    				    rl.setNewstypeid(arr.getLong(a));
			    				    rl.setNewsid(lp.getId());
			    				    dao.PeaceCrud(rl, "LnkNewstype", "save", (long) 0, 0, 0, null);			
			    					
			    				}
				               
			        			 return new ResponseEntity<LutNews>(lp, HttpStatus.OK);
				        	}
			}
			else{
					if (mfile != null) {
					
						FileUpload rupload= fileUploadService.uploadFile(mfile, SAVE_DIR,DOW_DIR); 
				 	 LutNews lr=(LutNews) dao.getHQLResult("from LutNews t where t.id="+obj.getLong("id")+"", "current"); 
		    		 String delPath = lr.getImgurl();
					 System.out.println(lr.getImgurl() + " is deleted!");
				
						Date d1 = new Date();
						SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
						String special = df.format(d1);
		        		 
		        		LutNews lp= (LutNews) dao.getHQLResult("from LutNews t where t.id='"+obj.getLong("id")+"'", "current");        			
	        			lp.setImgurl(rupload.getFileurl());
	        			lp.setNewstitle(obj.getString("newstitle"));
	        			lp.setNewstext(obj.getString("newstext"));
	        			lp.setIshighlight(obj.getBoolean("ishighlight"));
	        			lp.setNewsdate(special);
	        			
	        			if(obj.has("isfiles")){
	        				lp.setIsfile(obj.getBoolean("isfiles")); 
	        				afile=1;
	        				}
	        			else{
	        				lp.setIsfile(false);
	        				afile=0;
	        				}
	        			if(obj.has("showid")){
	        				if(obj.getBoolean("showid")==false){
	        					lp.setShowid(0);
	        				}
	        				else{
	        					lp.setShowid(1);
	        				}		        						        				
	        				}
	        			else{
	        				lp.setShowid(0);
	        				}
	        			lp.setUserid(loguser.getId());
	        			dao.PeaceCrud(lp, "LutNews", "update", (obj.getLong("id")), 0, 0, null);	  
	        			
	        			
	        			if(afile==1 && obj.getBoolean("isfiles")==true){
	        				if(obj.has("afiless")){
	    						JSONArray arraf= obj.getJSONArray("afiless");
	    						
	    						if(arraf.length()>0){
	    						List<LnkNewsAfile> nafile=(List<LnkNewsAfile>) dao.getHQLResult("from LnkNewsAfile t where t.newsid='"+obj.getLong("id")+"'", "list");
	    						
	    						for(int f=0;f<nafile.size();f++){						
	    							dao.PeaceCrud(null, "LnkNewsAfile", "delete", (long) nafile.get(f).getId(), 0, 0, null);	    							
	    						}
	    						
	    						for(int a=0;a<arraf.length();a++){				
		    						
			    					LnkNewsAfile af= new LnkNewsAfile();		
			    					JSONObject afnu = (JSONObject) arraf.get(a); 
			    					af.setAfilename(afnu.getString("filename") );
			    					af.setAfileurl(afnu.getString("fileurl"));
			    					af.setAfileext(afnu.getString("extension"));
			    					af.setAfilesize(afnu.getLong("fsize"));
			    					af.setNewsid(lp.getId());
			    				    dao.PeaceCrud(af, "LnkNewsAfile", "save", (long) 0, 0, 0, null);			
			    					
			    				}
	    					}
	    					}
	    					
	    				}
	        			
	        			if(obj.has("cats")){		
	    					
	    					JSONArray arr= obj.getJSONArray("cats");
	    					
	    					System.out.println("@@@@@@"+obj.getJSONArray("cats"));
	    					if(arr.length()>0){
	    						List<LnkNewstype> ncat=(List<LnkNewstype>) dao.getHQLResult("from LnkNewstype t where t.newsid='"+obj.getLong("id")+"'", "list");
	    						for(int f=0;f<ncat.size();f++){						
	    							dao.PeaceCrud(null, "LnkNewstype", "delete", (long) ncat.get(f).getId(), 0, 0, null);	    							
	    						}
	    						
	    						for(int a=0;a<arr.length();a++){					
	    							LnkNewstype rusr=new LnkNewstype();	
	    							rusr.setNewstypeid(arr.getLong(a));
	    							rusr.setNewsid(lp.getId());
	    							dao.PeaceCrud(rusr, "LnkNewstype", "save",  (long) 0, 0, 0, null);	
	    					    		
	    							
	    						}
	    					}
	    				
	    				}
		               
	        			 return new ResponseEntity<LutNews>(lp, HttpStatus.OK);
		        	}    
			 
			 	else{	
							Date d1 = new Date();
							SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
							String special = df.format(d1);																	        		
			        		 
							LutNews lp= (LutNews) dao.getHQLResult("from LutNews t where t.id='"+obj.getLong("id")+"'", "current"); 		        					        			
		        			lp.setNewstitle(obj.getString("newstitle"));
		        			lp.setNewstext(obj.getString("newstext"));
		        			lp.setIshighlight(obj.getBoolean("ishighlight"));
		        			lp.setNewsdate(special);
		        			if(obj.has("isfiles")){
		        				lp.setIsfile(obj.getBoolean("isfiles")); 
		        				afile=1;
		        				}
		        			else{
		        				lp.setIsfile(false);
		        				afile=0;
		        				}
		        			if(obj.has("showid")){
		        				if(obj.getBoolean("showid")==false){
		        					lp.setShowid(0);
		        				}
		        				else{
		        					lp.setShowid(1);
		        				}		        						        				
		        				}
		        			else{
		        				lp.setShowid(0);
		        				}
		        			lp.setUserid(loguser.getId());
		        			dao.PeaceCrud(lp, "LutNews", "update", (obj.getLong("id")), 0, 0, null);	   
		        			
		        			
		        			if(afile==1 && obj.getBoolean("isfiles")==true){
		        				if(obj.has("afiless")){
		    						JSONArray arraf= obj.getJSONArray("afiless");
		    						
		    						if(arraf.length()>0){
		    						List<LnkNewsAfile> nafile=(List<LnkNewsAfile>) dao.getHQLResult("from LnkNewsAfile t where t.newsid='"+obj.getLong("id")+"'", "list");
		    						
		    						for(int f=0;f<nafile.size();f++){						
		    							dao.PeaceCrud(null, "LnkNewsAfile", "delete", (long) nafile.get(f).getId(), 0, 0, null);	    							
		    						}
		    						
		    						for(int a=0;a<arraf.length();a++){				
			    						
				    					LnkNewsAfile af= new LnkNewsAfile();		
				    					JSONObject afnu = (JSONObject) arraf.get(a); 
				    					af.setAfilename(afnu.getString("filename") );
				    					af.setAfileurl(afnu.getString("fileurl"));
				    					af.setAfileext(afnu.getString("extension"));
				    					af.setAfilesize(afnu.getLong("fsize"));
				    					af.setNewsid(lp.getId());
				    				    dao.PeaceCrud(af, "LnkNewsAfile", "save", (long) 0, 0, 0, null);			
				    					
				    				}
		    					}
		    					}
		    					
		    				}
		        			
		        			if(obj.has("cats")){		
		    					
		    					JSONArray arr= obj.getJSONArray("cats");
		    					
		    					System.out.println("@@@@@@"+obj.getJSONArray("cats"));
		    					if(arr.length()>0){
		    						
		    						List<LnkNewstype> ncat=(List<LnkNewstype>) dao.getHQLResult("from LnkNewstype t where t.newsid='"+obj.getLong("id")+"'", "list");
		    						for(int f=0;f<ncat.size();f++){						
		    							dao.PeaceCrud(null, "LnkNewstype", "delete", (long) ncat.get(f).getId(), 0, 0, null);	    							
		    						}
		    						
		    						for(int a=0;a<arr.length();a++){					
		    							LnkNewstype rusr=new LnkNewstype();	
		    							rusr.setNewstypeid(arr.getLong(a));
		    							rusr.setNewsid(lp.getId());
		    							dao.PeaceCrud(rusr, "LnkNewstype", "save",  (long) 0, 0, 0, null);	
		    					    		
		    							
		    						}
		    					}
		    				
		    				}
			               
		        			 return new ResponseEntity<LutNews>(lp, HttpStatus.OK);
			        	}
				
			}
			 
			 }
			 	
			
			return null;
	    }
	 	 
	 
	 @RequestMapping(value="/info/afilesadd",method=RequestMethod.POST)
		public ResponseEntity<LutNews> Newsfileadd( @RequestParam("files") MultipartFile files,@RequestParam("obj") String jsonstring, MultipartRequest mpr, HttpServletRequest req) throws JSONException, DocumentException, Exception {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
		     MultipartFile mfile =  null;
		  	 mfile = (MultipartFile)files;
		  	 JSONObject obj= new JSONObject(jsonstring);
		  	 
		  	 String appPath = req.getServletContext().getRealPath(""); 	 
		  	
		  	 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser=(LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				 	    
			 String SAVE_DIR =appPath+"/" +IzrApplication.ROOT ;
			 String DOW_DIR = "uploads" + "/" +loguser.getUsername();
   	    
			 FileUpload rupload= fileUploadService.uploadFile(mfile, SAVE_DIR,DOW_DIR); 
			 Date d1 = new Date();
			 SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			 String special = df.format(d1);				        		
	
			 String path =SAVE_DIR+"/"  +loguser.getUsername();
			 String fpath = SAVE_DIR+"/" +loguser.getUsername()+"/" +mfile.getOriginalFilename();
		    
			 File folder = new File(path);
			 if(!folder.exists()){
		     	folder.mkdirs();
			 }
		    
			 File logoorgpath = new File(fpath);
			
			 if(!logoorgpath.exists()){
				mfile.transferTo(logoorgpath);
			 }
			 
			 LutNews lp = new LutNews();	        			
			
			 dao.PeaceCrud(lp, "LutNews", "save", (long) 0, 0, 0, null);	     
			
	
			 return new ResponseEntity<LutNews>(lp, HttpStatus.OK);  
				
				 
			 }
			 	
			
			return null;
	    }
	 	 
	
	
	@RequestMapping(value = "/info/{action}/{domain}", method=RequestMethod.POST)
    public @ResponseBody String update(Model model,@RequestBody String jsonString, @PathVariable String action,@PathVariable String domain) throws JSONException,ClassCastException{
       System.out.println("json STR "+jsonString);	
       try{
    	   Class<?> classtoConvert;
		   JSONObject obj = new JSONObject(jsonString);    		
		   
		   
		   String domainName=domain;
		   System.out.println(domain);
		   classtoConvert=Class.forName(domain);
		   Gson gson = new Gson();
		   Object object = gson.fromJson(obj.toString(),classtoConvert);	
		   
		   if(action.equals("update")){
			  
			   if(!obj.has("models")){
				   
				   if(domainName.equalsIgnoreCase("com.netgloo.models.LutLaw")){
				    	  JSONObject str= new JSONObject(jsonString);
				    	  
				    	  				    	   
				    	   LutLaw cr= (LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+str.getInt("id")+"'", "current");					           
				    	   cr.setLawname(str.getString("lawname"));						   
						   cr.setParentid(str.getString("parentid"));

						   cr.setDescribe(str.getString("describe"));
						   dao.PeaceCrud(cr, domainName, "update", str.getLong("id"), 0, 0, null);				   	  
				    	  						  
				   }
				    else{
				    	  int id=(int)obj.getInt("id");
						  dao.PeaceCrud(object, domainName, "update", (long) id, 0, 0, null);
				    }			 
			   }   
			   
			   else{
				   JSONArray rs=(JSONArray) obj.get("models");
				   System.out.println("rs obj "+rs);
				   for(int i=0;i<rs.length();i++){
					   String str=rs.get(i).toString();
					   JSONObject batchobj= new JSONObject(str);  
					   Object bobj = gson.fromJson(batchobj.toString(),classtoConvert);
					   int upid=batchobj.getInt("id");					   
					   dao.PeaceCrud(bobj, domainName, "update", (long) upid, 0, 0, null); 					  
				   }
				  
			   }
			  
		   }
		   else if(action.equals("delete")){
			   if(domainName.equalsIgnoreCase("com.netgloo.models.LutDepartment")){
				   dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);
				   dao.PeaceCrud(object, "com.netgloo.models.LutUser", "delete", obj.getLong("id"), 0, 0, null);
				   dao.PeaceCrud(object, "com.netgloo.models.LnkUserrole", "delete", obj.getLong("id"), 0, 0, null);
			   }
			   else if (domainName.equalsIgnoreCase("com.netgloo.models.LutFactor")){
				   LutFactor cr=  (LutFactor) dao.getHQLResult("from LutFactor t where t.id = " + obj.getLong("id"), "current");
				   if (cr != null){
					   cr.setIsactive(0);
					   dao.PeaceCrud(cr, domainName, "update", (long) cr.getId(), 0, 0, null); 	
				   }
			   }
			   else {
				   System.out.println("ustsaan");
				   dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);
				   }		  
		   }
		   else if(action.equals("create")){
			
			    if(domainName.equalsIgnoreCase("com.netgloo.models.LutDepartment")){
			    	  JSONObject str= new JSONObject(jsonString);
			    	  
			    	   
			    	  LutDepartment cr= new LutDepartment();
			    	  if(str.getLong("isstate")==1){
			    	   //cr.setLutDepartment(lutDepartment);(str.getLong("lpid"));
					   cr.setDepartmentname(str.getString("departmentname"));
					   cr.setEmail(str.getString("email"));
					   cr.setPhone(str.getString("phone"));			
					   cr.setWeb(str.getString("web"));						   
					   cr.setShortname(str.getString("shortname"));		
					   cr.setAddress(str.getString("address"));
					   cr.setIsstate(str.getLong("isstate"));
					   cr.setIsactive(str.getLong("isactive"));
					   dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);			
			    	  }
			    	  else{
			    		  cr.setDepartmentname(str.getString("departmentname"));
						   cr.setEmail(str.getString("email"));
						   cr.setPhone(str.getString("phone"));			
						   cr.setWeb(str.getString("web"));						   
						   cr.setShortname(str.getString("shortname"));		
						   cr.setAddress(str.getString("address"));
						   cr.setReg(str.getString("reg"));
						   cr.setLicnum(str.getString("licnum"));	
						   cr.setLicexpiredate(str.getString("licexpiredate"));
						   cr.setIsstate(str.getLong("isstate"));
						   cr.setIsactive(str.getLong("isactive"));
						   
						   LutUser usr= new LutUser();		    	   
						   
						   usr.setDepartmentid(cr.getId());
						   //usr.setPositionid(str.getString("positionid"));			
						  // usr.setEmail(str.getString("email"));
						   usr.setMobile(str.getString("phone"));

						   usr.setRoleid("2599");		
						   usr.setIsstate(false);
						   usr.setUsername(str.getString("reg"));
										   
						   usr.setPassword(passwordEncoder.encode(str.getString("reg")));
						   usr.setIsactive(str.getBoolean("isactive"));
						   dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);
						   
						   LnkUserrole rl= new LnkUserrole();			
						   rl.setRoleid(2599);
						   rl.setUserid(usr.getId());
						   dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);
			    	  }
			    	  
			    	  						  
			   }
			    /*if(domainName.equalsIgnoreCase("com.netgloo.models.LutIndependent")){
			    	  JSONObject str= new JSONObject(jsonString);			    	  
			    	   
			    	  LutIndependent cr= new LutIndependent();			    	   
					   cr.setName(str.getString("name"));
					   cr.setReg(str.getString("reg"));
					   cr.setPhone(str.getString("phone"));			
					   cr.setWeb(str.getString("web"));						   
					   cr.setLicnum(str.getString("licnum"));	
					   cr.setLicexpiredate(str.getString("licexpiredate"));
					   cr.setAddress(str.getString("address"));					
					   cr.setIsactive(str.getBoolean("isactive"));
					   dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);	
					   
					   LutUser usr= new LutUser();		    	   
					   
					   usr.setDepartmentid(cr.getId());
					   //usr.setPositionid(str.getString("positionid"));			
					  // usr.setEmail(str.getString("email"));
					   usr.setMobile(str.getString("phone"));
					   usr.setRoleid(2599);		
					   usr.setIsstate(false);
					   usr.setUsername(str.getString("reg"));
									   
					   usr.setPassword(passwordEncoder.encode(str.getString("reg")));
					   usr.setIsactive(str.getBoolean("isactive"));
					   dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);
					   
					   LnkUserrole rl= new LnkUserrole();			
					   rl.setRoleid(2599);
					   rl.setUserid(usr.getId());
					   dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);
			    	  
			    	  						  
			   }*/
			    else if (domainName.equalsIgnoreCase("com.netgloo.models.LnkFeedbackCategory")){
			    	JSONObject str= new JSONObject(jsonString);
			    	if (str.has("title") && !str.isNull("title")){
			    		LnkFeedbackCategory lfc = new LnkFeedbackCategory();
			    		lfc.setTitle(str.getString("title"));
			    		if (str.has("parentid") && !str.isNull("parentid") && (str.getLong("parentid") > 0)){
			    			lfc.setParentid(str.getLong("parentid"));
			    		}
			    		dao.PeaceCrud(lfc, domainName, "save", (long) 0, 0, 0, null);
			    	}
			    	else{
			    		return "false";
			    	}
			    }
			    else{
			    	dao.PeaceCrud(object, domainName, "save", (long) 0, 0, 0, null);			   
			    }
			   
		   }		  
		   return "true";
       }
       catch(Exception  e){
    	   e.printStackTrace();
    		return null;
       }
      
    }

}
