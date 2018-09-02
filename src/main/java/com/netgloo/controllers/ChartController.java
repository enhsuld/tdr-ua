package com.netgloo.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.RestController;

import com.netgloo.dao.UserDao;
import com.netgloo.models.LnkRiskTryout;
import com.netgloo.models.LutAuditYear;



@Controller
@RestController
@RequestMapping("/chart")
public class ChartController {


	@Autowired
    private UserDao dao;
	    
	@RequestMapping(value = "/{type}/{userid}", method= RequestMethod.GET)
    public @ResponseBody String Stat(@PathVariable String type, @PathVariable Long userid, HttpServletRequest req) throws HttpRequestMethodNotSupportedException {
		List<LutAuditYear> lrt= (List<LutAuditYear>) dao.getHQLResult("from LutAuditYear t where t.isactive=1", "list");
		if(type.equalsIgnoreCase("plotBands")){
			JSONArray arr = new JSONArray(); 
			List<Object[]> mau = (List<Object[]>) dao.getHQLResult("select  d.departmentname, count(t.depid), sum(t.aper)+sum(t.a2per)+sum(t.a3per),  sum(t.mper)+ sum(t.m2per)+sum(t.m3per), sum(t.tper)+sum(t.t2per)+sum(t.t3per), d.shortname from LutDepartment d left join d.mainAuditRegistrations t where t.audityear='"+lrt.get(0).getAudityear()+"' and t.autype=1 and d.id=t.depid and  t.isenabled=1 and t.stepid>2  and d.shortname !=null group by d.departmentname, d.shortname  order by d.departmentname asc", "list");
		
			for(int i = 0; i < mau.size(); i++) {
				JSONObject obj= new JSONObject();
				obj.put("depname", mau.get(i)[5]);
				obj.put("acount", mau.get(i)[1]);			 	
				obj.put("auditor", Math.round(Double.parseDouble(mau.get(i)[2].toString())/(3*Double.parseDouble(mau.get(i)[1].toString()))));
				obj.put("manager", Math.round(Double.parseDouble(mau.get(i)[3].toString())/(3*Double.parseDouble(mau.get(i)[1].toString()))));
				obj.put("terguuleh", Math.round(Double.parseDouble(mau.get(i)[4].toString())/(3*Double.parseDouble(mau.get(i)[1].toString()))));
				arr.put(obj);
			}
			return arr.toString();
		}
		else if(type.equalsIgnoreCase("userStat")){
			JSONArray arr = new JSONArray(); 
			List<Object[]> mau = (List<Object[]>) dao.getHQLResult("select t.orgname, t.stepid, t.aper, t.a2per, t.a3per, t.mper, t.m2per, t.m3per, t.tper, t.t2per, t.t3per,  t.aper+t.a2per+t.a3per as sda,  t.mper+ t.m2per+t.m3per, t.tper+t.t2per+t.t3per from MainAuditRegistration  t, LnkMainUser u where t.audityear='"+lrt.get(0).getAudityear()+"' and  u.userid="+userid+" and t.id=u.appid and  t.isenabled=1 and t.stepid>2  order by sda asc", "list");
		
			for(int i = 0; i < mau.size(); i++) {
				JSONObject obj= new JSONObject();
				obj.put("orgname", mau.get(i)[0]);
				obj.put("stepid", mau.get(i)[1]);
				
				obj.put("aper", mau.get(i)[2]);	
				obj.put("a2per", mau.get(i)[3]);	
				obj.put("a3per", mau.get(i)[4]);
				
				obj.put("mper", mau.get(i)[5]);	
				obj.put("m2per", mau.get(i)[6]);	
				obj.put("m3per", mau.get(i)[7]);	
				
				obj.put("tper", mau.get(i)[8]);	
				obj.put("t2per", mau.get(i)[9]);	
				obj.put("t3per", mau.get(i)[10]);	
				
				obj.put("auditor", Math.round(Double.parseDouble(mau.get(i)[11].toString())/3));
				obj.put("manager", Math.round(Double.parseDouble(mau.get(i)[12].toString())/3));
				obj.put("terguuleh", Math.round(Double.parseDouble(mau.get(i)[13].toString())/3));
				arr.put(obj);
			}
			return arr.toString();
		}
		else if(type.equalsIgnoreCase("catStat")){
			JSONArray arr = new JSONArray(); 	
			JSONArray deps = new JSONArray(); 	
			JSONArray counts = new JSONArray(); 
			JSONArray ppttz = new JSONArray(); 
			JSONArray pptez = new JSONArray(); 
			JSONArray countttz = new JSONArray(); 
			JSONArray counttez = new JSONArray(); 
			
			System.out.println("sdasdasd"+lrt.get(0).getAudityear());
			if(lrt!=null){
				
			}
			List<Object[]> mau = (List<Object[]>) dao.getHQLResult("select  d.shortname, count(t.depid), sum(t.aper)+sum(t.a2per)+sum(t.a3per),  sum(t.mper)+ sum(t.m2per)+sum(t.m3per), sum(t.tper)+sum(t.t2per)+sum(t.t3per) from LutDepartment d left join d.mainAuditRegistrations t where t.audityear='"+lrt.get(0).getAudityear()+"' and t.autype=1 and d.id=t.depid and t.orgtype =12 and  t.isenabled=1 and t.stepid>2  and d.shortname !=null group by d.shortname  order by d.shortname asc", "list");
			List<Object[]> tez = (List<Object[]>) dao.getHQLResult("select  d.shortname, count(t.depid), sum(t.aper)+sum(t.a2per)+sum(t.a3per),  sum(t.mper)+ sum(t.m2per)+sum(t.m3per), sum(t.tper)+sum(t.t2per)+sum(t.t3per) from LutDepartment d left join d.mainAuditRegistrations t where t.audityear='"+lrt.get(0).getAudityear()+"' and t.autype=1 and d.id=t.depid and t.orgtype =11 and  t.isenabled=1 and t.stepid>2  and d.shortname !=null group by d.shortname  order by d.shortname asc", "list");
			for(int i = 0; i < mau.size(); i++) {
				JSONObject obj= new JSONObject();
				obj.put("orgname", mau.get(i)[0]);
				obj.put("count", mau.get(i)[1]);
				
				obj.put("auditor", Math.round(Double.parseDouble(mau.get(i)[2].toString())/(3*Double.parseDouble(mau.get(i)[1].toString()))));
				obj.put("manager", Math.round(Double.parseDouble(mau.get(i)[3].toString())/(3*Double.parseDouble(mau.get(i)[1].toString()))));
				obj.put("terguuleh", Math.round(Double.parseDouble(mau.get(i)[4].toString())/(3*Double.parseDouble(mau.get(i)[1].toString()))));
				arr.put(obj);
				deps.put(mau.get(i)[0]);
				counts.put(mau.get(i)[1]);
				ppttz.put(Math.round(Double.parseDouble(mau.get(i)[4].toString())/(3*Double.parseDouble(mau.get(i)[1].toString())))+Math.round(Double.parseDouble(mau.get(i)[3].toString())/(3*Double.parseDouble(mau.get(i)[1].toString())))+Math.round(Double.parseDouble(mau.get(i)[2].toString())/(3*Double.parseDouble(mau.get(i)[1].toString()))));
			}
			for(int i = 0; i < tez.size(); i++) {
				JSONObject obj= new JSONObject();				
				counttez.put(tez.get(i)[1]);
				pptez.put(Math.round(Double.parseDouble(tez.get(i)[4].toString())/(3*Double.parseDouble(tez.get(i)[1].toString())))+Math.round(Double.parseDouble(tez.get(i)[3].toString())/(3*Double.parseDouble(tez.get(i)[1].toString())))+Math.round(Double.parseDouble(tez.get(i)[2].toString())/(3*Double.parseDouble(tez.get(i)[1].toString()))));
			}
			JSONObject obj= new JSONObject();
			obj.put("deps", deps);
			obj.put("countttz", counts);
			obj.put("ppttz", ppttz);
			obj.put("counttez", counttez);
			obj.put("pptez", pptez);
			return obj.toString();
		}
		else if(type.equalsIgnoreCase("userStatOrg")){
			JSONArray arr = new JSONArray(); 
			List<Object[]> mau = (List<Object[]>) dao.getHQLResult("select u.familyname, u.givenname, count(c.mid) as sda  from MainAuditRegistration  t, LutUser u, LnkMainUser l, LnkMainTransition c where t.audityear='"+lrt.get(0).getAudityear()+"' and  t.depid="+userid+" and l.positionid not in (2,3,4,5,12) and c.mid=t.id and c.stepid=5 and u.id=l.userid and t.id=l.appid and t.isactive=1 and u.isstate=1 and  t.isenabled=1 and t.stepid>2 group by u.givenname, u.familyname, l.userid, u.username  order by sda asc", "list");
		
			JSONArray yaxis = new JSONArray(); 
			JSONArray xaxis = new JSONArray(); 
			
			for(int i = 0; i < mau.size(); i++) {
				String fname=mau.get(i)[0].toString();
				if(fname.length()>0){
					yaxis.put(mau.get(i)[0].toString().substring(0,1)+"."+mau.get(i)[1].toString());
				}else{
					yaxis.put(mau.get(i)[1].toString());
				}
				
				xaxis.put(mau.get(i)[2]);
			}
			JSONObject obj= new JSONObject();
			obj.put("yaxis", yaxis);
			obj.put("xaxis", xaxis);
			return obj.toString();
		}
		else{
			return null;
		}
	}
   
}
