package com.netgloo.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.util.IOUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
//import com.hazelcast.com.eclipsesource.json.JsonArray;
import com.netgloo.service.FileUploadService;
import com.netgloo.service.SmtpMailSender;


import com.netgloo.IzrApplication;
import com.netgloo.dao.UserDao;
import com.netgloo.models.FileUpload;
import com.netgloo.models.FsFormA4;
import com.netgloo.models.LnkDirectionNotice;
import com.netgloo.models.LnkMainAttach;
import com.netgloo.models.LnkMainComment;
import com.netgloo.models.LnkMainForm;
import com.netgloo.models.LnkMainFormT2;
import com.netgloo.models.LnkMainTransition;
import com.netgloo.models.LnkMainWork;
import com.netgloo.models.LnkRiskLaw;
import com.netgloo.models.LnkRiskT2;
import com.netgloo.models.LnkRiskTryout;
import com.netgloo.models.LnkRiskdir;
import com.netgloo.models.LnkTryoutConfSource;
import com.netgloo.models.LnkTryoutConfType;
import com.netgloo.models.LnkWorkCategory;
import com.netgloo.models.LutAuditDir;
import com.netgloo.models.LutAuditWork;
import com.netgloo.models.LutFactor;
import com.netgloo.models.LutFactorCriterion;
import com.netgloo.models.LutFeedback;
import com.netgloo.models.LutLaw;
import com.netgloo.models.LutRisk;
import com.netgloo.models.LutTryout;
import com.netgloo.models.LutUser;
import com.netgloo.models.MainAuditRegistration;

@RestController
public class WorkController {
	@Autowired
	private SmtpMailSender smtpMailSender;

	@Autowired
	private UserDao dao;


	@Autowired
	FileUploadService fileUploadService;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@RequestMapping(value = "/work/{action}/{domain}", method=RequestMethod.POST)
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

					if(domainName.equalsIgnoreCase("com.netgloo.models.LnkMainFormT2")){
						JSONObject str= new JSONObject(jsonString);
						List<LnkMainFormT2> rs=(List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.factorid="+str.getLong("id")+" and t.dirid="+str.getLong("dirid")+" and t.riskid="+str.getLong("riskid")+"  and t.mid="+str.getLong("mid")+" order by t.id", "list");
						LutFactor lr=(LutFactor) dao.getHQLResult("from LutFactor t where t.id='"+str.getLong("id")+"'", "current");
						if(lr.getTryid()>0){
							if(rs.size()>0){
								LnkMainFormT2 lm=rs.get(0);
								if(!str.isNull("description")){
									lm.setDescription(str.getString("description"));
								}

								if(str.getLong("decid")!=lm.getDecid()){
									lm.setDecid(str.getLong("decid"));
									if(str.getInt("decid")==1){
										if(!str.isNull("riskid")){
											List<LnkRiskT2> t2=(List<LnkRiskT2>) dao.getHQLResult("from LnkRiskT2 t where t.t2id="+lm.getId()+" and t.riskid="+str.getLong("riskid")+" and t.t2id="+lm.getId()+"", "list");
											if(t2.size()==0){		
												LnkRiskT2 tt=new LnkRiskT2();						    			  
												//tt.setRiskid(str.getLong("riskid"));
												tt.setRiskid(lm.getRiskid());
												tt.setT2id(lm.getId());
												tt.setDirid(lm.getDirid());
												tt.setTryid(lr.getTryid());
												tt.setRtype(lm.getRtype());
												if(!str.isNull("description")){
													tt.setDescription(str.getString("description"));
												}
												dao.PeaceCrud(tt, "LnkRiskT2", "save", (long) 0, 0, 0, null);
											}
											else{
												LnkRiskT2 tt=t2.get(0);						    			  
												// tt.setRiskid(str.getLong("riskid"));
												tt.setRiskid(lm.getRiskid());
												tt.setT2id(lm.getId());
												tt.setDirid(lm.getDirid());
												tt.setTryid(lr.getTryid());
												tt.setRtype(lm.getRtype());
												if(!str.isNull("description")){
													tt.setDescription(str.getString("description"));
												}
												dao.PeaceCrud(tt, "LnkRiskT2", "update", (long) tt.getId(), 0, 0, null);
											}
										}

									}
									else{
										dao.PeaceCrud(null, "LnkRiskT2", "delete", (long) lm.getId(), 0, 0, "t2id");
									}
								}
								lm.setCriname(str.getString("criname"));
								lm.setStepid(3);
								dao.PeaceCrud(lm, "LnkMainFormT2", "update", (long) lm.getId(), 0, 0, null);
							}
							else{		
								LnkMainFormT2 lm = new LnkMainFormT2();				    		  
								JSONArray arr=new JSONArray();

								if(!str.isNull("risks")){
									arr=str.getJSONArray("risks");
								}
								lm.setMid(str.getLong("mid"));
								lm.setFactorid(str.getLong("id"));
								if(!str.isNull("description")){
									lm.setDescription(str.getString("description"));
								}
								lm.setRtype(str.getInt("rtype"));
								lm.setDirid(str.getLong("dirid"));
								lm.setDecid(str.getLong("decid"));
								lm.setCriname(str.getString("criname"));
								lm.setStepid(3);
								lm.setRiskid(str.getLong("riskid"));
								dao.PeaceCrud(lm, "LnkMainFormT2", "save", (long) 0, 0, 0, null);


								if(str.getInt("decid")==1){
									LnkRiskT2 tt=new LnkRiskT2();
									if(!str.isNull("riskid")){
										tt.setT2id(lm.getId());
										tt.setRiskid(lm.getRiskid());
										tt.setDirid(lm.getDirid());
										tt.setTryid(lr.getTryid());
										tt.setRtype(lm.getRtype());
										if(!str.isNull("description")){
											tt.setDescription(str.getString("description"));
										}					    			  
										//List<LnkRiskT2> t2=(List<LnkRiskT2>) dao.getHQLResult("from LnkRiskT2 t where t.riskid="+str.getInt("riskid")+" and t.t2id="+lm.getId()+"", "list");
										dao.PeaceCrud(tt, "LnkRiskT2", "save", (long) 0, 0, 0, null);
									}				    			
								}
								else{
									dao.PeaceCrud(null, "LnkRiskT2", "delete", (long) lm.getId(), 0, 0, "t2id");
								}

							}

							System.out.println("lalala"+jsonString);	
							return "true";
						}		
						else{
							return "tryNotFound";
						}		  
					}
					else if(domainName.equalsIgnoreCase("com.netgloo.models.FsFormA4")){
						JSONObject str= new JSONObject(jsonString);
						if (str.has("id")){
							FsFormA4 lk=(FsFormA4) dao.getHQLResult("from FsFormA4 t where t.id="+str.getLong("id")+"", "current");
							if (lk != null){
								lk.setDescription1(str.getString("description1"));
								lk.setDescription2(str.getString("description2"));
								lk.setDescription3(str.getString("description3"));
								/*lk.setIsselect(1);
							   lk.setPercent(str.getString("percent"));*/

								dao.PeaceCrud(lk, "FsFormA4", "save", (long) 0, 0, 0, null);
								return "true";
							}
							else{
								return "false";
							}
						}
						else{
							return "false";
						}

					}
					else if(domainName.equalsIgnoreCase("com.netgloo.models.LnkRiskT2")){
						JSONObject str= new JSONObject(jsonString);
						LnkRiskT2 lk=(LnkRiskT2) dao.getHQLResult("from LnkRiskT2 t where t.id="+str.getLong("id")+"", "current");
						lk.setData1(str.getLong("data1"));
						lk.setData2(str.getLong("data2"));
						lk.setData3(str.getLong("data3"));
						lk.setData4(str.getLong("data1")*str.getLong("data2")*str.getLong("data3"));
						if(str.getLong("data1")*str.getLong("data2")*str.getLong("data3")>20){
							lk.setData7(true);
							lk.setData6(false);
							lk.setData5(false);
							LutRisk ris=(LutRisk) dao.getHQLResult("from LutRisk t where t.id="+lk.getRiskid()+"", "current");
							LutAuditDir dir=(LutAuditDir) dao.getHQLResult("from LutAuditDir t where t.id="+lk.getDirid()+"", "current");
							LutTryout tr=(LutTryout) dao.getHQLResult("from LutTryout t where t.id="+lk.getTryid()+"", "current");
							List<LnkDirectionNotice> lnkDirectionNotices=dir.getLnkDirectionNotices();
							String notice="";
							String law="";
							String zuil="";
							String zaalt="";
							String slaw="";
							String szuil="";
							String szaalt="";
							String other="";
							for(LnkDirectionNotice item:lnkDirectionNotices){
								notice=notice+","+item.getLutNotice().getName();
							}
							for(LnkRiskLaw item:ris.getLnkRiskLaws()){
								if(item.getLutLawr().getLawcategory()==1){
									if(item.getLutLawr().getParentid()==null){
										law=law+","+item.getLname();
									}
									if(item.getLzuil()!=null){
										zuil=zuil+","+item.getLzuil();
									}
									if(item.getLzaalt()!=null){
										zaalt=zaalt+"-"+item.getLzaalt();
									}
								}
								else if(item.getLutLawr().getLawcategory()==2){
									if(item.getLutLawr().getParentid()==null){
										slaw=slaw+","+item.getLname();
									}
									if(item.getLzuil()!=null){
										szuil=szuil+","+item.getLzuil();
									}
									if(item.getLzaalt()!=null){
										szaalt=szaalt+"-"+item.getLzaalt();
									}
								}
							}
							other=other+"-"+ris.getOthertext();
							String conftype="";
							String sourceD="";
							String sourceG="";
							if(tr!=null){
								if(tr.getLnkTryoutConfTypes()!=null){
									for(LnkTryoutConfType item:tr.getLnkTryoutConfTypes()){
										conftype=conftype+","+item.getLutConfirmationType().getName();
									}
								}
								if(tr.getLnkTryoutConfSources()!=null){
									for(LnkTryoutConfSource item:tr.getLnkTryoutConfSources()){
										if(item.getLutConfirmationSource().getType().equalsIgnoreCase("0")){
											sourceD=sourceD+","+item.getLutConfirmationSource().getName();
										}
										else{
											sourceG=sourceG+","+item.getLutConfirmationSource().getName();
										}

									}
								}
							}


							List<LnkRiskTryout> ll=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.dirid="+lk.getDirid()+" and t.riskid="+lk.getRiskid()+" and t.tryid="+lk.getTryid()+" and t.rt2id="+lk.getId()+"", "list");

							LnkRiskTryout lrt=null;
							if(ll.size()>0){
								lrt=ll.get(0);
							}
							else{
								lrt=new LnkRiskTryout();
							}
							lrt.setTryid(lk.getTryid());
							lrt.setRt2id(lk.getId());
							lrt.setRiskid(lk.getRiskid());
							lrt.setDirid(lk.getDirid());
							lrt.setData1(dir.getName());
							lrt.setData2(ris.getRiskname());
							lrt.setData3(String.valueOf(str.getLong("data1")*str.getLong("data2")*str.getLong("data3")));
							lrt.setData4(notice.substring(1, notice.length()));
							if(law.length()>0){
								lrt.setData5(law.substring(1, law.length()));
							}
							if(zuil.length()>0){
								lrt.setData6(zuil.substring(1, zuil.length()));
							}
							if(zaalt.length()>0){
								lrt.setData7(zaalt.substring(1, zaalt.length()));
							}
							if(slaw.length()>0){
								lrt.setData8(slaw.substring(1, slaw.length()));
							}
							if(szuil.length()>0){
								lrt.setData9(szuil.substring(1, szuil.length()));
							}
							if(szaalt.length()>0){
								lrt.setData10(szaalt.substring(1, szaalt.length()));
							}
							if(other.length()>0){
								lrt.setData11(other.substring(1, other.length()));
							}

							lrt.setData12(tr.getFormdesc());
							lrt.setData13(tr.getFormdesc());
							lrt.setData14(conftype);
							lrt.setData15(sourceD);
							lrt.setData16(sourceG);
							lrt.setData23(0);
							lrt.setData25(0);
							lrt.setMid(str.getLong("mid"));
							if(ll.size()>0){
								dao.PeaceCrud(lrt, "LnkRiskTryout", "update", (long) lrt.getId(), 0, 0, null);
							}
							else{
								dao.PeaceCrud(lrt, "LnkRiskTryout", "save", (long) 0, 0, 0, null);
							}

						}
						else{
							dao.PeaceCrud(null, "LnkRiskTryout", "delete", (long) lk.getId(), 0, 0, "rt2id");
						}
						if(str.getLong("data1")*str.getLong("data2")*str.getLong("data3")>25 && str.getLong("data1")*str.getLong("data2")*str.getLong("data3")<51){
							lk.setData6(true);
							lk.setData7(false);
							lk.setData5(false);
						}
						if(str.getLong("data1")*str.getLong("data2")*str.getLong("data3")<25 && str.getLong("data1")*str.getLong("data2")*str.getLong("data3")>0){
							lk.setData5(true);
							lk.setData6(false);
							lk.setData7(false);
						}
						if(!str.isNull("description")){
							lk.setDescription(str.getString("description"));
						}				    	  
						lk.setDirid(str.getLong("dirid"));
						lk.setRiskid(str.getLong("riskid"));
						lk.setT2id(str.getLong("t2id"));
						dao.PeaceCrud(lk, "LnkRiskT2", "save", (long) lk.getId(), 0, 0, null);

						return "true";
					}
					else{
						int id=(int)obj.getInt("id");
						dao.PeaceCrud(object, domainName, "update", (long) id, 0, 0, null);
						return "true";
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
					return "true";

				}			  
			}
			else if(action.equals("delete")){
				if (domainName.equalsIgnoreCase("com.netgloo.models.LnkRiskLaw")){
					obj = new JSONObject(jsonString);
					if (obj.has("riskid") && obj.has("ZaaltId")){
						List<LnkRiskLaw> lawrisk =  (List<LnkRiskLaw>) dao.getHQLResult("from LnkRiskLaw t where t.riskid="+obj.getString("riskid")+ " and t.lawid = "+obj.getString("ZaaltId"), "list");
						if (lawrisk.size() > 0){
							for(LnkRiskLaw l : lawrisk){
								dao.PeaceCrud(l, domainName, "delete", l.getId(), 0, 0, null);	
							}
							return "true";
						}
					}
					return "false";
				}
				else{
					dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);			
				}

			}
			else if(action.equals("create")){
				if (domainName.equalsIgnoreCase("com.netgloo.models.LutFeedback")){
					UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					obj = new JSONObject(jsonString);
					if (obj.has("description") && !obj.isNull("description")){
						LutFeedback feedback = new LutFeedback();
						feedback.setDescription(obj.getString("description"));
						feedback.setUsername(userDetail.getUsername());
						if (obj.has("categoryid") && !obj.isNull("categoryid")){
							feedback.setCategoryid(obj.getLong("categoryid"));
						}
						if (obj.has("parentid") && !obj.isNull("parentid")){
							feedback.setParentid(obj.getLong("parentid"));
						}
						dao.PeaceCrud(feedback, domainName, "save", (long) 0, 0, 0, null);
						JSONObject result = new JSONObject();
						result.put("id", feedback.getId());
						result.put("description", feedback.getDescription());
						result.put("categoryid", feedback.getCategoryid());
						result.put("parentid", feedback.getParentid());
						result.put("createdat", feedback.getCreatedat());
						result.put("username", feedback.getUsername());
						return new JSONObject().put("status", true).put("data", result).toString();
					}
					else{
						return "false";
					}
				}
				else{
					dao.PeaceCrud(object, domainName, "save", (long) 0, 0, 0, null);	
					return "true";
				}
			}		  

		}
		catch(Exception  e){
			e.printStackTrace();
		}
		return "false";

	}

	@RequestMapping(value = "/work/resource/{type}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	public @ResponseBody String tree(@PathVariable String type,@PathVariable long id) {
		try{

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
			if (!(auth instanceof AnonymousAuthenticationToken)) {	
				if(type.equalsIgnoreCase("critid")){
					List<LutFactorCriterion> rs=(List<LutFactorCriterion>) dao.getHQLResult("from LutFactorCriterion t where t.factorid="+id+" order by t.id", "list");
					for(int i=0;i<rs.size();i++){
						JSONObject obj=new JSONObject();      	
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getName());			        		
						arr.put(obj);        	
					}		
				}
				else if(type.equalsIgnoreCase("riskid")){
					List<LnkRiskdir> rs=(List<LnkRiskdir>) dao.getHQLResult("from LnkRiskdir t where t.dirid="+id+" order by t.id", "list");
					for(int i=0;i<rs.size();i++){
						JSONObject obj=new JSONObject();  
						LutRisk lr=rs.get(i).getLutRisk();

						//obj.put("value", lr.getId());
						//	obj.put("text", lr.getRiskname());			        		
						//arr.put(obj);    
						arr.put(lr.getRiskname());
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

	@RequestMapping(value = "/work/{typeid}/{mid}", method = RequestMethod.GET)
	public @ResponseBody String work(@PathVariable long typeid,@PathVariable long mid) {
		try{

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			MainAuditRegistration ma=  (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+mid+"", "current");

			JSONArray arr=new JSONArray();

			Collection<Object> aw=  (Collection<Object>) dao.getHQLResult("select t.id, t.workformname, t.orderid, t.levelid,t.fileurl,t.filename, t.parentid, t.isfile, t.fname, t.isscore, t.text, d.typeid  from LutAuditWork t, LnkWorkCategory c , LnkWorkAuType d where c.catid="+ma.getOrgtype()+" and t.id=c.workid and t.id=d.workid order by t.orderid", "list");

			Iterator<Object> ldata =aw.iterator(); 
			while (ldata.hasNext()) {
				Object[] curr = (Object[]) ldata.next();	
				//List<LnkMainTransition> mt=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+curr[0].toString()+" and t.mid="+ma.getId()+"", "list");
				JSONObject obj=new JSONObject();  

				/*if(mt.size()>0){
					obj.put("step", mt.get(0).getStepid());
				}
				else{
					obj.put("step", 0);
				}*/
				obj.put("step", 0);		 	
				obj.put("id", curr[0].toString());
				if(curr[2]!=null){
					obj.put("workname", curr[1].toString());
				}
				if(curr[2]!=null){
					obj.put("orderid", curr[2].toString());	
				}
				if(curr[6]!=null){
					obj.put("parentid", curr[6].toString());	
				}
				if(curr[5]!=null){
					obj.put("catname", curr[5].toString()); 
				}
				if(curr[3]!=null){
					obj.put("levelid", curr[3].toString()); 
				}
				if(curr[4]!=null){
					obj.put("fileurl", curr[4].toString()); 
				}
				if(curr[7]!=null){
					obj.put("isfile", curr[7].toString()); 
				}
				if(curr[8]!=null){
					obj.put("fname", curr[8].toString()); 
				}
				if(curr[9]!=null){
					obj.put("isscore", curr[9].toString()); 
				}
				if(curr[10]!=null){
					obj.put("content", curr[10].toString()); 
				}
				obj.put("allchildtrue", "0");	
				obj.put("haschild", false);			
				if(curr[11].toString().equalsIgnoreCase(String.valueOf(ma.getAutype()))){
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


	@RequestMapping(value="/work/{typeid}/imp",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String sendapp(@RequestBody String jsonString,@PathVariable int typeid) throws JSONException, JsonProcessingException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JSONObject obj= new JSONObject(jsonString);
			ObjectMapper mapper = new ObjectMapper(); 
			if(typeid==1){				
				List<LnkMainWork> cr=  (List<LnkMainWork>) dao.getHQLResult("from LnkMainWork t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+"", "list");
				return mapper.writeValueAsString(cr);
			}
			else if(typeid==2){
				List<LnkMainForm> cr=  (List<LnkMainForm>) dao.getHQLResult("from LnkMainForm t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+"", "list");
				return mapper.writeValueAsString(cr);
			}
			else if(typeid==3){
				List<LnkMainComment> cr=  (List<LnkMainComment>) dao.getHQLResult("from LnkMainComment t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+"", "list");
				return mapper.writeValueAsString(cr);
			}
			else if(typeid==4){
				List<LnkMainAttach> cr=  (List<LnkMainAttach>) dao.getHQLResult("from LnkMainAttach t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+"", "list");
				return mapper.writeValueAsString(cr);
			}
			else{
				return "false";
			} 



		}
		else{
			return "false";
		}

	}

	@RequestMapping(value="/work/comment",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String sendapp(@RequestBody String jsonString) throws JSONException, JsonProcessingException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date date1 = new Date();
			String special = dateFormat1.format(date1);
			JSONObject obj= new JSONObject(jsonString);
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LutUser loguser=(LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			System.out.println("end irchlee"+jsonString);
			LnkMainComment cm= new LnkMainComment();

			cm.setNotedate(special);
			cm.setNote(obj.getString("comment"));
			cm.setWid(obj.getLong("wid"));
			cm.setPositionid(Long.parseLong(loguser.getPositionid()));
			cm.setMid(obj.getLong("mid"));
			cm.setUsername(loguser.getFamilyname().substring(0, 1)+"."+loguser.getGivenname());
			dao.PeaceCrud(cm, "LnkMainComment", "save",(long) 0, 0, 0, null);

			ObjectMapper mapper = new ObjectMapper(); 

			return mapper.writeValueAsString(cm);
		}
		else{
			return "false";
		}

	}

	@RequestMapping(value="/work/work",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String work(@RequestBody String jsonString) throws JSONException, JsonProcessingException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date date1 = new Date();
			String special = dateFormat1.format(date1);
			JSONObject obj= new JSONObject(jsonString);
			ObjectMapper mapper = new ObjectMapper(); 			 
			List<LnkMainWork> cr=  (List<LnkMainWork>) dao.getHQLResult("from LnkMainWork t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+"", "list");
			LnkMainWork cm=null;
			if(cr.size()>0){
				cm= cr.get(0);
				cm.setNotedate(special);
				cm.setNote(obj.getString("note"));
				cm.setWid(obj.getLong("wid"));
				cm.setMid(obj.getLong("mid"));
				dao.PeaceCrud(cm, "LnkMainWork", "update", (long) 0, 0, 0, null);

				return mapper.writeValueAsString(cm);
			}
			else{
				cm= new LnkMainWork();
				cm.setNotedate(special);
				cm.setNote(obj.getString("note"));
				cm.setWid(obj.getLong("wid"));
				cm.setMid(obj.getLong("mid"));
				dao.PeaceCrud(cm, "LnkMainWork", "save",(long) 0, 0, 0, null);

				return mapper.writeValueAsString(cm);
			}			 			

		}
		else{
			return "false";
		}

	}

	@RequestMapping(value="/work/attach",method=RequestMethod.POST)
	public String workFile(@RequestParam("files") List<Object> files,@RequestParam("obj") String jsonstring, MultipartRequest mpr, HttpServletRequest req) throws JSONException, DocumentException, Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {

			JSONObject obj= new JSONObject(jsonstring);
			MultipartFile mfile =  null;
			ObjectMapper mapper = new ObjectMapper(); 			
			mfile = (MultipartFile) files.get(files.size()-1);
			if (mfile != null) {	    		 
				String appPath = req.getServletContext().getRealPath(""); 	 
				DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				Date date1 = new Date();
				String special = dateFormat1.format(date1);
				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				//String SAVE_DIR = IzrApplication.ROOT;         
				MainAuditRegistration mn= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+obj.getLong("mid")+"'", "current");
				String SAVE_DIR =appPath+"/" +IzrApplication.ROOT + "/" +loguser.getLutDepartment().getId()+"/"+obj.getLong("mid")+"/"+mn.getLnkMainForms().size();
				String DOW_DIR = "uploads" + "/" +loguser.getLutDepartment().getId()+"/"+obj.getLong("mid")+"/"+mn.getLnkMainForms().size();

				FileUpload rupload= fileUploadService.uploadFile(mfile, SAVE_DIR,DOW_DIR); 	 

				if(rupload!=null){
					LnkMainAttach cm=new LnkMainAttach();
					cm.setWid(obj.getLong("wid"));
					cm.setMid(obj.getLong("mid"));
					cm.setFileext(rupload.getMimetype());
					cm.setFileurl(rupload.getFileurl());
					cm.setAtdate(special);
					cm.setFilename(rupload.getFilename());
					dao.PeaceCrud(cm, "LnkMainAttach", "save",(long) 0, 0, 0, null);
					String filename=mfile.getName();
					String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

					String excel = "xls";
					if (extension.equals(excel) || extension.equals("xlsx")){
						FileUpload rupload11= fileUploadService.uploadfromExcel(mfile,obj.getLong("mid"), mn.getAudityear(),mn.getOrgcode(),mn.getOrgtype(),mn.getStepid(),SAVE_DIR,obj.getString("sheetname"),obj.getString("fileurl"),cm.getId());
					}			         


					return mapper.writeValueAsString(cm);
				}

			}
			return "false";

		}
		return null;
	}

	@RequestMapping(value="/work/excel",method=RequestMethod.POST)
	public String workExcel(@RequestParam("files") Object files,@RequestParam("obj") String jsonstring, MultipartRequest mpr, HttpServletRequest req) throws JSONException, DocumentException, Exception {
		System.out.println("ene bol work excel");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			JSONObject obj= new JSONObject(jsonstring);
			MultipartFile mfile =  null;
			ObjectMapper mapper = new ObjectMapper(); 			
			mfile = (MultipartFile)files;
			if (mfile != null) {	    		 
				String appPath = req.getSession().getServletContext().getRealPath("");	
				//String appPath = req.getServletContext().getContextPath();
				DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
				Date date1 = new Date();
				String special = dateFormat1.format(date1);
				String specialFile = df.format(date1);
				MainAuditRegistration mn= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+obj.getLong("mid")+"'", "current");
				String SAVE_DIR =appPath+File.separator +IzrApplication.ROOT + File.separator +loguser.getLutDepartment().getId()+File.separator+loguser.getUsername()+File.separator+mn.getOrgcode()+File.separator+obj.getLong("mid");
				SAVE_DIR.trim();
				String DOW_DIR = "uploads" + File.separator +loguser.getLutDepartment().getId()+File.separator+loguser.getUsername()+File.separator+mn.getOrgcode()+File.separator+obj.getLong("mid");

				FileUpload rupload= fileUploadService.uploadFinExcel(mfile,obj.getLong("mid"), mn.getAudityear(),mn.getOrgcode(),mn.getOrgtype(),obj.getInt("levelid"),SAVE_DIR,DOW_DIR);
				if(rupload!=null){
					LnkMainForm cm=new LnkMainForm();
					cm.setWid(obj.getLong("wid"));
					cm.setMid(obj.getLong("mid"));
					cm.setFileext(rupload.getMimetype());
					cm.setFileurl(rupload.getFileurl()+"."+FilenameUtils.getExtension(mfile.getOriginalFilename()));
					cm.setAtdate(special);
					cm.setFilename(mfile.getOriginalFilename());
					dao.PeaceCrud(cm, "LnkMainForm", "save",(long) 0, 0, 0, null);

					return mapper.writeValueAsString(cm);
				}

			}
			return "false";

		}
		return null;
	}

	@RequestMapping(value="/work/form",method=RequestMethod.POST)
	public String workForm(@RequestParam("files") Object files,@RequestParam("obj") String jsonstring, MultipartHttpServletRequest request, HttpServletRequest req) throws JSONException, DocumentException, Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {

			JSONObject obj= new JSONObject(jsonstring);
			MultipartFile mfile =  null;
			ObjectMapper mapper = new ObjectMapper(); 			
			mfile = (MultipartFile)files;
			if (mfile != null) {	    		 
				//String appPath = req.getSession().getServletContext().getRealPath("");	
				String appPath = request.getServletContext().getRealPath("");
				DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				Date date1 = new Date();
				String special = dateFormat1.format(date1);
				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				//String SAVE_DIR = IzrApplication.ROOT;         
				MainAuditRegistration mn= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+obj.getLong("mid")+"'", "current");
				String SAVE_DIR =appPath+"/" +IzrApplication.ROOT + "/" +loguser.getLutDepartment().getShortname().trim()+"/"+loguser.getUsername()+"/"+mn.getOrgcode()+"/"+obj.getLong("mid")+"/"+mn.getLnkMainForms().size();
				String DOW_DIR = "uploads" + "/" +loguser.getLutDepartment().getShortname().trim()+"/"+loguser.getUsername()+"/"+mn.getOrgcode()+"/"+obj.getLong("mid")+"/"+mn.getLnkMainForms().size();

				FileUpload rupload= fileUploadService.uploadFile(mfile, SAVE_DIR,DOW_DIR);
				if(rupload!=null){
					LnkMainForm cm=new LnkMainForm();
					cm.setWid(obj.getLong("wid"));
					cm.setMid(obj.getLong("mid"));
					cm.setFileext(rupload.getMimetype());
					cm.setFileurl(rupload.getFileurl());
					cm.setAtdate(special);
					cm.setFilename(rupload.getFilename());
					dao.PeaceCrud(cm, "LnkMainForm", "save",(long) 0, 0, 0, null);

					return mapper.writeValueAsString(cm);
				}

			}
			return "false";

		}
		return null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String fileName = request.getParameter("fileName");

		if(fileName == null || fileName.equals("")){

			throw new ServletException("File Name can't be null or empty");

		}

		File file = new File(request.getServletContext().getAttribute("FILES_DIR")+File.separator+fileName);

		if(!file.exists()){

			throw new ServletException("File doesn't exists on server.");

		}

		System.out.println("File location on server::"+file.getAbsolutePath());

		ServletContext ctx = getServletContext();

		InputStream fis = new FileInputStream(file);

		String mimeType = ctx.getMimeType(file.getAbsolutePath());

		response.setContentType(mimeType != null? mimeType:"application/octet-stream");

		response.setContentLength((int) file.length());

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");



		ServletOutputStream os       = response.getOutputStream();

		byte[] bufferData = new byte[1024];

		int read=0;

		while((read = fis.read(bufferData))!= -1){

			os.write(bufferData, 0, read);

		}

		os.flush();

		os.close();

		fis.close();

		System.out.println("File downloaded at client successfully");

	}

	@RequestMapping(value="/work/file/show/{id}",method=RequestMethod.GET)
	public void getFile(@PathVariable("id") long id,HttpServletRequest req, HttpServletResponse response) {
		try {
			// get your file as InputStream
			LnkMainForm mn= (LnkMainForm) dao.getHQLResult("from LnkMainForm t where t.id='"+id+"'", "current");
			String appPath = req.getServletContext().getRealPath("");
			File file = new File(appPath+"/"+mn.getFileurl());
			InputStream fis = new FileInputStream(file);
			// copy it to response's OutputStream
			IOUtils.copy(fis, response.getOutputStream());
			response.flushBuffer();

			// org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			//  log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
			throw new RuntimeException("IOError writing file to output stream");
		}

	}


	/*@RequestMapping(value="/work/file/show/{id}",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
		public @ResponseBody String show(@PathVariable long id,HttpServletRequest req,String[] args ) throws JSONException, IOException, InvalidFormatException{

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				 LnkMainForm mn= (LnkMainForm) dao.getHQLResult("from LnkMainForm t where t.id='"+id+"'", "current");
				 String appPath = req.getServletContext().getRealPath("");
				 File file = new File(appPath+"/"+mn.getFileurl());

				 //FileUtils.deleteDirectory(new File(mn.getFileurl()));
		        //	String args[]= new String[20];
					FileInputStream inp = new FileInputStream( file );
					Options options = new Options();
					options.addOption("s", "sourceFile", true, appPath+"/"+mn.getFileurl());
					options.addOption("df", "dateFormat", true, "The template to use for fomatting dates into strings.");
					options.addOption("?", "help", true, "This help text.");
					options.addOption(new Option("percent", "Parse percent values as floats."));
					options.addOption(new Option("empty", "Include rows with no data in it."));
					options.addOption(new Option("pretty", "To render output as pretty formatted json."));
					options.addOption(new Option("fillColumns", "To fill rows with null values until they all have the same size."));
					CommandLineParser parser = new BasicParser();
					CommandLine cmd = null;
					try {
						cmd = parser.parse(options, args);
					} catch(ParseException e) {
						help(options);
						return null; 
					}

					if(cmd.hasOption("?")) {
						help(options);
						return null; 
					}

					ExcelToJsonConverterConfig config = ExcelToJsonConverterConfig.create(file.getAbsolutePath(),cmd);
					String valid = config.valid();
					if(valid!=null) {
						System.out.println(valid);
						help(options);
						return null; 
					}

					ExcelWorkbook book = ExcelToJsonConverter.convert(config);
					String json = book.toJson(config.isPretty());
					System.out.println(json);

					return json;

					Workbook workbook = WorkbookFactory.create( inp );

					// Get the first Sheet.
					Sheet sheet = workbook.getSheetAt( 0 );

					    // Start constructing JSON.

					JSONObject mainObj = new JSONObject();

					mainObj.put("activeSheet", sheet.getSheetName());

					JSONArray sheets = new JSONArray();

				    JSONObject json = new JSONObject();

				    // Iterate through the rows.
				    JSONArray rows = new JSONArray();
				    for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); )
				    {
				        Row row = rowsIT.next();
				        JSONObject jRow = new JSONObject();

				        // Iterate through the cells.
				        JSONArray cells = new JSONArray();
				        for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
				        {
				            Cell cell = cellsIT.next();
				            JSONObject inner = new JSONObject();

				            XSSFCellStyle style = (XSSFCellStyle) cell.getCellStyle();
				            XSSFFont font = style.getFont();
				            XSSFColor colour = font.getXSSFColor();
				            inner.put("background", "#FFC7CE");
				            if(cell.getCellType()==1){
				            	inner.put("value", cell.getStringCellValue());
				            }
				            else{
				            	inner.put("value", cell.getNumericCellValue());
				            }

				            cells.put(inner);
				        }
				        jRow.put( "cells", cells );
				        rows.put( jRow );
				    }




				    json.put( "name", sheet.getSheetName() );
				    json.put( "rows", rows );


				    sheets.put(json);

				    mainObj.put("sheets", sheets);

				// Get the JSON text.
				return mainObj.toString();
			}
			else{
				 return "false";
			}

		 }

		private static void help(Options options) {
			HelpFormatter formater = new HelpFormatter();
			formater.printHelp("java -jar excel-to-json.jar", options);
		}*/

	/*	@RequestMapping(value="/work/file/show/{id}",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ActionForward showfile(@PathVariable long id,HttpServletRequest req, HttpServletResponse response) throws JSONException, IOException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 LnkMainForm mn= (LnkMainForm) dao.getHQLResult("from LnkMainForm t where t.id='"+id+"'", "current");
			 String appPath = req.getServletContext().getRealPath("");
			 File file = new File(appPath+"/"+mn.getFileurl());

			 //FileUtils.deleteDirectory(new File(mn.getFileurl()));

    		 if(file.delete()){
    			 System.out.println(file.getName() + " is deleted!");
    		 }else{
    			 System.out.println("Delete operation is failed.");
    		 }

    		 dao.PeaceCrud(mn, "LnkMainForm", "delete", id, 0, 0, null);

		  	 return "true";
		}
		else{
			 return "false";
		}

	 }*/

	private ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}


	@RequestMapping(value="/work/file/{delete}/{id}",method=RequestMethod.DELETE,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String delete(@PathVariable long id, @PathVariable String delete, HttpServletRequest req) throws JSONException, IOException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			if(delete.equals("deleteAttach")){
				LnkMainAttach mn= (LnkMainAttach) dao.getHQLResult("from LnkMainAttach t where t.id='"+id+"'", "current");
				String appPath = req.getServletContext().getRealPath("");
				File file = new File(appPath+"/"+mn.getFileurl());

				//FileUtils.deleteDirectory(new File(mn.getFileurl()));
				System.out.println(appPath);	
				if(file.delete()){
					System.out.println(file.getName() + " is deleted!");
				}else{
					System.out.println("Delete operation is failed.");
				}

				dao.PeaceCrud(mn, "LnkMainAttach", "delete", id, 0, 0, null);
			}
			else if(delete.equals("delete")){
				LnkMainForm mn= (LnkMainForm) dao.getHQLResult("from LnkMainForm t where t.id='"+id+"'", "current");
				String appPath = req.getServletContext().getRealPath("");
				File file = new File(appPath+"/"+mn.getFileurl());

				//FileUtils.deleteDirectory(new File(mn.getFileurl()));
				if(file.delete()){
					System.out.println(file.getName() + " is deleted!");
				}else{
					System.out.println("Delete operation is failed.");
				}

				dao.PeaceCrud(mn, "LnkMainForm", "delete", id, 0, 0, null);
			}

			return "true";
		}
		else{
			return "false";
		}

	}
	
	@RequestMapping(value="/work/stepup/all/{mid}/{stepid}",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String stepUpAll(@PathVariable long mid, @PathVariable long stepid) throws JSONException, JsonProcessingException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date date1 = new Date();
			String special = dateFormat1.format(date1);
			ObjectMapper mapper = new ObjectMapper(); 			 
			List<LnkMainTransition> cr=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.levelid="+stepid+" and t.mid="+mid+"", "list");
			if(cr.size()>0){
				for(LnkMainTransition item:cr){
					item.setLevelid(stepid+1);
					dao.PeaceCrud(item, "LnkMainTransition", "update", (long) item.getId(), 0, 0, null);
				}
			}
			else{
				/*LnkMainTransition cm=new LnkMainTransition();
				cm.setWid(obj.getLong("wid"));
				cm.setMid(obj.getLong("mid"));
				cm.setParentid(work.getParentid());
				cm.setLevelid(work.getLevelid());

				dao.PeaceCrud(cm, "LnkMainTransition", "save",(long) 0, 0, 0, null);
				for(LnkMainTransition item:cr){
					item.setLevelid(stepid+1);
					dao.PeaceCrud(item, "LnkMainTransition", "update", (long) item.getId(), 0, 0, null);
				}*/
			}
			return "true";

		}
		else{
			return "false";
		}

	}

	@RequestMapping(value="/work/stepup",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String stepup(@RequestBody String jsonString) throws JSONException, JsonProcessingException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date date1 = new Date();
			String special = dateFormat1.format(date1);
			JSONObject obj= new JSONObject(jsonString);
			ObjectMapper mapper = new ObjectMapper(); 			 
			List<LnkMainTransition> cr=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+"", "list");
			LutAuditWork work=  (LutAuditWork) dao.getHQLResult("from LutAuditWork t where t.id="+obj.getLong("wid")+"", "current");
			List<LnkMainTransition> parent=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+work.getParentid()+" and t.mid="+obj.getLong("mid")+"", "list");
			LnkMainTransition cm=null;
			MainAuditRegistration ma=  (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+obj.getLong("mid")+"", "current");;
			System.out.println("cr size: "+cr.size());
			if(cr.size()>1){
				for(int i=0;i<cr.size()-1;i++){
					cm= cr.get(i);
					dao.PeaceCrud(cm, "LnkMainTransition", "delete", (long) cm.getId(), 0, 0, null);
				}				
			}
			if(cr.size()>0){
				cm= cr.get(cr.size()-1);
				//LnkMainTransition par= null;
				List<LnkMainTransition> cr1=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and parentid="+cm.getParentid()+"", "list");
				if(parent.size()>0){
					for(LnkMainTransition par:parent){
						//par = parent.get(0);
						if(obj.getString("role").equalsIgnoreCase("ROLE_AUDIT") || obj.getString("role").equalsIgnoreCase("INDP")){
							List<LnkMainTransition> cr2=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and parentid="+cm.getParentid()+" and stepid='3'", "list");
							if(cr1.size()-1==cr2.size()){
								cm.setPstep(3);
								par.setStepid(3);
							}else{
								cm.setPstep(0);
								par.setStepid(0);
							}
						}
						else if(obj.getString("role").equalsIgnoreCase("ROLE_FIRST")){
							List<LnkMainTransition> cr2=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and parentid="+cm.getParentid()+" and stepid='4'", "list");
							if(cr1.size()-1==cr2.size()){
								cm.setPstep(4);
								par.setStepid(4);
							}else{
								cm.setPstep(0);
								par.setStepid(0);
							}
						}
						else if(obj.getString("role").equalsIgnoreCase("ROLE_SECOND")){
							List<LnkMainTransition> cr2=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and parentid="+cm.getParentid()+" and stepid='5'", "list");
							if(cr1.size()-1==cr2.size()){
								cm.setPstep(5);
								par.setStepid(5);
							}else{
								cm.setPstep(0);
								par.setStepid(0);
							}
						}
						dao.PeaceCrud(par, "LnkMainTransition", "update", (long) par.getId(), 0, 0, null);
					}
				}
					

				if(obj.getString("role").equalsIgnoreCase("ROLE_AUDIT") || obj.getString("role").equalsIgnoreCase("INDP")){
					cm.setStepid(3);
				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_FIRST")){
					cm.setStepid(4);
				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_SECOND")){
					cm.setStepid(5);
				}
				cm.setWid(obj.getLong("wid"));
				cm.setMid(obj.getLong("mid"));
				cm.setLevelid(work.getLevelid());
				cm.setParentid(work.getParentid());
				dao.PeaceCrud(cm, "LnkMainTransition", "update", (long) cm.getId(), 0, 0, null);

				if(obj.getString("role").equalsIgnoreCase("ROLE_AUDIT") || obj.getString("role").equalsIgnoreCase("INDP")){

					System.out.println("audit");

					List<LnkWorkCategory> wk=  (List<LnkWorkCategory>) dao.getHQLResult("from LutAuditWork t, LnkWorkCategory cat, LnkWorkAuType au where t.id=au.workid and au.typeid="+ma.getAutype()+" and  t.isscore=1 and t.levelid="+work.getLevelid()+" and cat.workid=t.id and cat.catid="+ma.getOrgtype()+"", "list");	

					List<Object[]> mt=  (List<Object[]>) dao.getHQLResult("select w.id, w.fname from LnkMainTransition t, LutAuditWork w where t.wid=w.id and w.isscore=1 and t.mid="+obj.getLong("mid")+" and t.stepid>=3 and t.levelid="+work.getLevelid()+"", "list");

					System.out.println("end wk "+wk.size());
					System.out.println("end mt "+mt.size());
					
					long aper=mt.size()*100/wk.size();
					long st=ma.getStepid();
					if(aper==100){
						// ma.setStepid(st+1);
					}

					if(work.getLevelid()==3){
						ma.setAper(aper);
					}
					else if(work.getLevelid()==4){
						ma.setA2per(aper);
					}
					else if(work.getLevelid()==5){
						ma.setA3per(aper);
					} 
					dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);


				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_FIRST")){

					System.out.println("manager");

					List<LnkWorkCategory> wk=  (List<LnkWorkCategory>) dao.getHQLResult("from LutAuditWork t, LnkWorkCategory cat, LnkWorkAuType au where t.id=au.workid and au.typeid="+ma.getAutype()+" and  t.isscore=1 and t.levelid="+work.getLevelid()+" and cat.workid=t.id and cat.catid="+ma.getOrgtype()+"", "list");	

					List<Object[]> mt=  (List<Object[]>) dao.getHQLResult("select w.id, w.fname from LnkMainTransition t, LutAuditWork w where t.wid=w.id and w.isscore=1 and t.mid="+obj.getLong("mid")+" and t.stepid>=4 and t.levelid="+work.getLevelid()+"", "list");

					long mper=mt.size()*100/wk.size();
					long st=ma.getStepid();
					if(mper==100){
						// ma.setStepid(st+1);
					}
					if(work.getLevelid()==3){
						ma.setMper(mper);
					}
					else if(work.getLevelid()==4){
						ma.setM2per(mper);
					}
					else if(work.getLevelid()==5){
						ma.setM3per(mper);
					} 
					dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);
				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_SECOND")){
					ma=  (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+obj.getLong("mid")+"", "current");
					List<LnkWorkCategory> wk=  (List<LnkWorkCategory>) dao.getHQLResult("from LutAuditWork t, LnkWorkCategory cat, LnkWorkAuType au where t.id=au.workid and au.typeid="+ma.getAutype()+" and  t.isscore=1 and t.levelid="+work.getLevelid()+" and cat.workid=t.id and cat.catid="+ma.getOrgtype()+"", "list");	


					List<Object[]> mt=  (List<Object[]>) dao.getHQLResult("select w.id, w.fname from LnkMainTransition t, LutAuditWork w where t.wid=w.id and w.isscore=1 and t.mid="+obj.getLong("mid")+" and t.stepid=5 and t.levelid="+work.getLevelid()+"", "list");

					long tper=mt.size()*100/wk.size();
					long st=ma.getStepid();
					if(tper==100){
						ma.setStepid(st+1);
					}
					if(work.getLevelid()==3){
						ma.setTper(tper);
					}
					else if(work.getLevelid()==4){
						ma.setT2per(tper);
					}
					else if(work.getLevelid()==5){
						ma.setT3per(tper);
					} 
					dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);
				}
				cm.setLevelid(ma.getStepid());
				return mapper.writeValueAsString(cm);
			}
			else{
				cm=new LnkMainTransition();

				List<LnkMainTransition> cr1=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t, LutAuditWork w where t.wid=w.id and w.isscore=1 and t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and t.parentid="+cm.getParentid()+"", "list");

				if(obj.getString("role").equalsIgnoreCase("ROLE_AUDIT") || obj.getString("role").equalsIgnoreCase("INDP")){
					cm.setStepid(3);

					List<LnkMainTransition> cr2=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and parentid="+cm.getParentid()+" and stepid='3'", "list");
					if(cr1.size()-1==cr2.size()){
						cm.setPstep(3);
					}else{
						cm.setPstep(0);
					}
				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_FIRST")){
					cm.setStepid(4);
					List<LnkMainTransition> cr2=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and parentid="+cm.getParentid()+" and stepid='4'", "list");
					if(cr1.size()==(cr2.size()-1)){
						cm.setPstep(4);
					}else{
						cm.setPstep(0);
					}
				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_SECOND")){
					cm.setStepid(5);
					List<LnkMainTransition> cr2=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+" and parentid="+cm.getParentid()+" and stepid='5'", "list");
					if(cr1.size()==(cr2.size()-1)){
						cm.setPstep(5);
					}else{
						cm.setPstep(0);
					}
				}
				cm.setWid(obj.getLong("wid"));
				cm.setMid(obj.getLong("mid"));
				cm.setParentid(work.getParentid());
				cm.setLevelid(work.getLevelid());

				dao.PeaceCrud(cm, "LnkMainTransition", "save",(long) 0, 0, 0, null);

				if(obj.getString("role").equalsIgnoreCase("ROLE_AUDIT") || obj.getString("role").equalsIgnoreCase("INDP")){					
					List<LnkWorkCategory> wk=  (List<LnkWorkCategory>) dao.getHQLResult("from LutAuditWork t, LnkWorkCategory cat, LnkWorkAuType au where t.id=au.workid and au.typeid="+ma.getAutype()+" and  t.isscore=1 and t.levelid="+work.getLevelid()+" and cat.workid=t.id and cat.catid="+ma.getOrgtype()+"", "list");	

					List<Object[]> mt=  (List<Object[]>) dao.getHQLResult("select w.id, w.fname from LnkMainTransition t, LutAuditWork w where t.wid=w.id and w.isscore=1 and t.mid="+obj.getLong("mid")+" and t.stepid>=3 and t.levelid="+work.getLevelid()+"", "list");

					System.out.println("end wk "+wk.size());
					System.out.println("end mt "+mt.size());
					
					long aper=mt.size()*100/wk.size();
					long st=ma.getStepid();
					if(aper==100){
						// ma.setStepid(st+1);
					}
					if(work.getLevelid()==3){
						ma.setAper(aper);
					}
					else if(work.getLevelid()==4){
						ma.setA2per(aper);
					}
					else if(work.getLevelid()==5){
						ma.setA3per(aper);
					} 

					dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);

				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_FIRST")){
					ma=  (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+obj.getLong("mid")+"", "current");
					List<LnkWorkCategory> wk=  (List<LnkWorkCategory>) dao.getHQLResult("from LutAuditWork t, LnkWorkCategory cat, LnkWorkAuType au where t.id=au.workid and au.typeid="+ma.getAutype()+" and  t.isscore=1 and t.levelid="+work.getLevelid()+" and cat.workid=t.id and cat.catid="+ma.getOrgtype()+"", "list");	

					List<Object[]> mt=  (List<Object[]>) dao.getHQLResult("select w.id, w.fname from LnkMainTransition t, LutAuditWork w where t.wid=w.id and w.isscore=1 and t.mid="+obj.getLong("mid")+" and t.stepid>=4 and t.levelid="+work.getLevelid()+"", "list");

					long mper=mt.size()*100/wk.size();

					long st=ma.getStepid();
					if(mper==100){
						// ma.setStepid(st+1);
					}
					if(work.getLevelid()==3){
						ma.setMper(mper);
					}
					else if(work.getLevelid()==4){
						ma.setM2per(mper);
					}
					else if(work.getLevelid()==5){
						ma.setM3per(mper);
					} 
					dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);

					cm.setLevelid(work.getLevelid());
				}
				else if(obj.getString("role").equalsIgnoreCase("ROLE_SECOND")){
					List<LnkWorkCategory> wk=  (List<LnkWorkCategory>) dao.getHQLResult("from LutAuditWork t, LnkWorkCategory cat, LnkWorkAuType au where t.id=au.workid and au.typeid="+ma.getAutype()+" and  t.isscore=1 and t.levelid="+work.getLevelid()+" and cat.workid=t.id and cat.catid="+ma.getOrgtype()+"", "list");	

					List<Object[]> mt=  (List<Object[]>) dao.getHQLResult("select w.id, w.fname from LnkMainTransition t, LutAuditWork w where t.wid=w.id and w.isscore=1 and t.mid="+obj.getLong("mid")+" and t.stepid=5 and t.levelid="+work.getLevelid()+"", "list");

					long tper=mt.size()*100/wk.size(); 
					long st=ma.getStepid();
					if(tper==100){
						ma.setStepid(st+1);
					}
					if(work.getLevelid()==3){
						ma.setTper(tper);
					}
					else if(work.getLevelid()==4){
						ma.setT2per(tper);
					}
					else if(work.getLevelid()==5){
						ma.setT3per(tper);
					} 
					dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);

				}
				cm.setLevelid(ma.getStepid());
				return mapper.writeValueAsString(cm);
			}			 			

		}
		else{
			return "false";
		}

	}

	@RequestMapping(value="/work/stepdown",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String stepdown(@RequestBody String jsonString) throws JSONException, JsonProcessingException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date date1 = new Date();
			String special = dateFormat1.format(date1);
			JSONObject obj= new JSONObject(jsonString);
			ObjectMapper mapper = new ObjectMapper(); 			 
			List<LnkMainTransition> cr=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.wid="+obj.getLong("wid")+" and t.mid="+obj.getLong("mid")+"", "list");
			LutAuditWork work=  (LutAuditWork) dao.getHQLResult("from LutAuditWork t where t.id="+obj.getLong("wid")+"", "current");
			LnkMainTransition cm=null;
			MainAuditRegistration ma=null;
			if(cr.size()>0){
				cm= cr.get(0);
				/*cm.setStepid(1);

				cm.setWid(obj.getLong("wid"));
				cm.setMid(obj.getLong("mid"));
				cm.setLevelid(work.getLevelid());
				dao.PeaceCrud(cm, "LnkMainTransition", "update", (long) cm.getId(), 0, 0, null);*/
				
				for(LnkMainTransition item:cr){
					item.setStepid(1);
					item.setWid(obj.getLong("wid"));
					item.setMid(obj.getLong("mid"));
					item.setLevelid(work.getLevelid());
					dao.PeaceCrud(item, "LnkMainTransition", "update", (long) item.getId(), 0, 0, null);
				}

				ma=  (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+obj.getLong("mid")+"", "current");
				List<LnkWorkCategory> wk=  (List<LnkWorkCategory>) dao.getHQLResult("from LutAuditWork t, LnkWorkCategory cat where t.isscore=1 and t.levelid="+work.getLevelid()+" and cat.workid=t.id and cat.catid="+ma.getOrgtype()+"", "list");	

				List<LnkMainTransition> mt=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.mid="+obj.getLong("mid")+" and t.stepid>=3 and t.levelid="+work.getLevelid()+"", "list");

				if(obj.getString("role").equalsIgnoreCase("ROLE_SECOND")){
					List<LnkMainTransition> mtm=  (List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t where t.mid="+obj.getLong("mid")+" and t.stepid>=4 and t.levelid="+work.getLevelid()+"", "list");
					long mper=mtm.size()*100/wk.size();

					long st=ma.getStepid();
					if(mper==100){
						// ma.setStepid(st+1);
					}
					if(work.getLevelid()==3){
						ma.setMper(mper);
						ma.setTper(mper);
					}
					else if(work.getLevelid()==4){
						ma.setM2per(mper);
						ma.setT2per(mper);
					}
					else if(work.getLevelid()==5){
						ma.setM3per(mper);
						ma.setT3per(mper);
					}  
				}
				long aper=mt.size()*100/wk.size();
				long st=ma.getStepid();
				if(aper==100){
					// ma.setStepid(st+1);
				}
				if(work.getLevelid()==3){
					ma.setAper(aper);
				}
				else if(work.getLevelid()==4){
					ma.setA2per(aper);
				}
				else if(work.getLevelid()==5){
					ma.setA3per(aper);
				} 

				dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);

				cm.setLevelid(ma.getStepid());

				return mapper.writeValueAsString(cm);
			}
			else{
				cm.setLevelid(ma.getStepid());
				return mapper.writeValueAsString(cm);
			}			 			

		}
		else{
			return "false";
		}

	}

	@RequestMapping(value="/work/laws",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String searchlaws(@RequestParam long parentid, @RequestParam long type) throws JSONException, JsonProcessingException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			JSONArray temp = new JSONArray();
			List<LutLaw> laws = (List<LutLaw>) dao.getHQLResult("from LutLaw t where t.parentid = " + parentid + " and t.lawcategory = " + type, "list");
			for(LutLaw l : laws){
				JSONObject t = new JSONObject();
				t.put("text", l.getLawname());
				t.put("value", l.getId());
				temp.put(t);
			}
			return temp.toString();
		}
		else{
			return null;
		}
	}
}
