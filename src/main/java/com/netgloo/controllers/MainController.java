package com.netgloo.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.netgloo.service.Services;
import com.netgloo.service.SmtpMailSender;
import com.netgloo.IzrApplication;
import com.netgloo.dao.UserDao;
import com.netgloo.models.DataSourceResult;
import com.netgloo.models.FileUpload;
import com.netgloo.models.LnkMainFormT2;
import com.netgloo.models.LnkMainUser;
import com.netgloo.models.LnkMenurole;
import com.netgloo.models.LnkNewsLog;
import com.netgloo.models.LnkOrgyplanreport;
import com.netgloo.models.LnkRiskLaw;
import com.netgloo.models.LnkRiskdir;
import com.netgloo.models.LnkUserrole;
import com.netgloo.models.LutAuditResults;
import com.netgloo.models.LutCategory;
import com.netgloo.models.LutDepartment;
import com.netgloo.models.LutExpProgcategory;
import com.netgloo.models.LutFincategory;
import com.netgloo.models.LutFlashNews;
import com.netgloo.models.LutLaw;
import com.netgloo.models.LutMenu;
import com.netgloo.models.LutPosition;
import com.netgloo.models.LutRisk;
import com.netgloo.models.LutRole;
import com.netgloo.models.LutUser;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.models.SubAuditOrganization;
import com.netgloo.repo.LnkMenuRepository;

@RestController
public class MainController {

	@Autowired
	private SmtpMailSender smtpMailSender;

	@Autowired
	private UserDao dao;

	@Autowired
	Services services;

	@Autowired
	private LnkMenuRepository lpo;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@RequestMapping(value = "/getMessages", method = RequestMethod.GET)
	public String getMessages() {
		JSONObject result = new JSONObject();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LutUser currentUser = null;
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			currentUser = (LutUser) dao
					.getHQLResult("from LutUser t where t.username = '" + userDetail.getUsername() + "'", "current");
		}
		JSONArray messageArray = new JSONArray();
		JSONArray alertArray = new JSONArray();
		List<LutFlashNews> rs = (List<LutFlashNews>) dao.getHQLResult("from LutFlashNews t order by t.createdat desc",
				"list");
		List<Long> logs = (List<Long>) dao.getHQLResult("select t.newsid from LnkNewsLog t where t.userid = "
				+ ((currentUser != null) ? currentUser.getId() : 0), "list");
		if (rs.size() > 0) {
			for (LutFlashNews news : rs) {

				JSONObject temp = new JSONObject();
				temp.put("id", news.getId());
				temp.put("title", news.getTitle());
				temp.put("description", news.getDescription());
				temp.put("createdat", news.getCreatedat());
				temp.put("status", news.getStatus());

				boolean isread = false;
				for (Long t : logs) {
					if (t == news.getId()) {
						isread = true;
					}
				}

				temp.put("isread", isread);

				if (news.getStatus() == 1) {
					alertArray.put(temp);
				} else {
					messageArray.put(temp);
				}
			}
		}
		result.put("messages", messageArray);
		result.put("alerts", alertArray);
		return result.toString();
	}

	@RequestMapping(value = "/changeUserPassword", method = RequestMethod.POST)
	public boolean changeUserPassword(@RequestBody String jsonStr) {
		JSONObject jsonObj = new JSONObject(jsonStr);
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LutUser loguser = (LutUser) dao
				.getHQLResult("from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");
		if (loguser != null && jsonObj.has("old_password") && jsonObj.has("new_password")
				&& jsonObj.has("new_password_confirm")
				&& jsonObj.getString("new_password").equals(jsonObj.getString("new_password_confirm"))) {
			if (passwordEncoder.matches(jsonObj.getString("old_password"), loguser.getPassword())) {
				loguser.setPassword(passwordEncoder.encode(jsonObj.getString("new_password")));
				dao.PeaceCrud(loguser, "LutUser", "save", (long) 0, 0, 0, null);
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = "/markAsReadMessage/{msgid}", method = RequestMethod.GET)
	public boolean markAsReadMessage(@PathVariable Long msgid) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			LutUser currentUser = (LutUser) dao
					.getHQLResult("from LutUser t where t.username = '" + userDetail.getUsername() + "'", "current");
			if (currentUser != null) {
				LutFlashNews msg = (LutFlashNews) dao.getHQLResult("from LutFlashNews t where t.id = " + msgid,
						"current");
				if (msg != null) {
					LnkNewsLog log = (LnkNewsLog) dao.getHQLResult(
							"from LnkNewsLog t where t.newsid = " + msgid + " and t.userid = " + currentUser.getId(),
							"current");
					if (log == null) {
						log = new LnkNewsLog();
						log.setNewsid(msg.getId());
						log.setUserid(currentUser.getId());
						dao.PeaceCrud(log, "LnkNewsLog", "save", (long) 0, 0, 0, null);
					}
					return true;
				}
			}
		}
		return false;
	}

	@RequestMapping(value = "/core/resource/{domain}", method = RequestMethod.GET, produces = {
			"application/json; charset=UTF-8" })
	public @ResponseBody String tree(@PathVariable String domain) {
		try {

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr = new JSONArray();

			UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			LutUser loguser = (LutUser) dao
					.getHQLResult("from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");
			if (!(auth instanceof AnonymousAuthenticationToken)) {

				if (domain.equalsIgnoreCase("LutMenu")) {
					List<LutMenu> rs = (List<LutMenu>) dao.getHQLResult("from LutMenu t  order by t.orderid", "list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getMenuname());
						arr.put(obj);
					}
				}
				if (domain.equalsIgnoreCase("LutDepartment")) {
					List<LutDepartment> rs = (List<LutDepartment>) dao
							.getHQLResult("from LutDepartment t  order by t.id", "list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getDepartmentname());
						arr.put(obj);
					}
				}
				if (domain.equalsIgnoreCase("LutRole")) {

					if (loguser == null) {
						List<LutRole> rs = (List<LutRole>) dao.getHQLResult("from LutRole t  order by t.id", "list");
						for (int i = 0; i < rs.size(); i++) {
							JSONObject obj = new JSONObject();
							obj.put("value", rs.get(i).getId());
							obj.put("text", rs.get(i).getRolename());
							obj.put("id", rs.get(i).getId());
							obj.put("title", rs.get(i).getRolename());
							arr.put(obj);
						}
					} else {
						if (loguser.getIsstate() == false) {
							List<LutRole> rs = (List<LutRole>) dao.getHQLResult("from LutRole t  where t.isstate=0",
									"list");
							for (int i = 0; i < rs.size(); i++) {
								JSONObject obj = new JSONObject();
								obj.put("value", rs.get(i).getId());
								obj.put("text", rs.get(i).getRolename());
								obj.put("id", rs.get(i).getId());
								obj.put("title", rs.get(i).getRolename());
								arr.put(obj);
							}
						} else {
							List<LutRole> rs = (List<LutRole>) dao.getHQLResult("from LutRole t  order by t.id",
									"list");
							for (int i = 0; i < rs.size(); i++) {
								JSONObject obj = new JSONObject();
								obj.put("value", rs.get(i).getId());
								obj.put("text", rs.get(i).getRolename());
								obj.put("id", rs.get(i).getId());
								obj.put("title", rs.get(i).getRolename());
								arr.put(obj);
							}
						}
					}
				}
				if (domain.equalsIgnoreCase("LutPosition")) {

					if (loguser == null) {
						List<LutPosition> rs = (List<LutPosition>) dao.getHQLResult("from LutPosition t  order by t.id",
								"list");

						for (int i = 0; i < rs.size(); i++) {
							JSONObject obj = new JSONObject();
							obj.put("value", rs.get(i).getId());
							obj.put("text", rs.get(i).getPositionname());
							arr.put(obj);
						}
					} else {
						if (loguser.getIsstate() == true) {
							List<LutPosition> rs = (List<LutPosition>) dao
									.getHQLResult("from LutPosition t  order by t.id", "list");

							for (int i = 0; i < rs.size(); i++) {
								JSONObject obj = new JSONObject();
								obj.put("value", rs.get(i).getId());
								obj.put("text", rs.get(i).getPositionname());
								arr.put(obj);
							}
						} else {
							List<LutPosition> rs = (List<LutPosition>) dao
									.getHQLResult("from LutPosition t  where t.isstate=0", "list");

							for (int i = 0; i < rs.size(); i++) {
								JSONObject obj = new JSONObject();
								obj.put("value", rs.get(i).getId());
								obj.put("text", rs.get(i).getPositionname());
								arr.put(obj);
							}
						}
					}

				}
				if (domain.equalsIgnoreCase("tez")) {
					List<SubAuditOrganization> rs = (List<SubAuditOrganization>) dao.getHQLResult(
							"from SubAuditOrganization t where t.orgcode like '1__000000%' order by t.id ", "list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getOrgname());
						arr.put(obj);
					}
				}
				if (domain.equalsIgnoreCase("LutFincategory")) {
					List<LutFincategory> rs = (List<LutFincategory>) dao
							.getHQLResult("from LutFincategory t order by t.id ", "list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getFincategoryname());
						arr.put(obj);
					}
				}
				if (domain.equalsIgnoreCase("aures")) {
					List<LutAuditResults> rs = (List<LutAuditResults>) dao
							.getHQLResult("from LutAuditResults t order by t.id ", "list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getResultname());
						arr.put(obj);
					}
				}
				if (domain.equalsIgnoreCase("LutCategory")) {
					List<LutCategory> rs = (List<LutCategory>) dao.getHQLResult("from LutCategory t order by t.id",
							"list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getCategoryname());
						arr.put(obj);
					}
				}

				if (domain.equalsIgnoreCase("LutExpProgcategory")) {
					List<LutExpProgcategory> rs = (List<LutExpProgcategory>) dao
							.getHQLResult("from LutExpProgcategory t order by t.id ", "list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getProgname());
						arr.put(obj);
					}
				}
			}
			return arr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/core/sel/{domain}/{id}", method = RequestMethod.GET, produces = {
			"application/json; charset=UTF-8" })
	public @ResponseBody String trees(@PathVariable String domain, @PathVariable int id) {

		try {

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr = new JSONArray();
			if (!(auth instanceof AnonymousAuthenticationToken)) {

				if (domain.equalsIgnoreCase("ttz")) {
					System.out.println("json STR " + id);
					SubAuditOrganization tez = (SubAuditOrganization) dao
							.getHQLResult("from SubAuditOrganization t where t.id='" + id + "'", "current");

					System.out.println("json STR " + tez.getOrgcode());
					List<SubAuditOrganization> rs = (List<SubAuditOrganization>) dao
							.getHQLResult("from SubAuditOrganization t where t.orgcode like '"
									+ tez.getOrgcode().substring(0, 3) + "___0000%' order by t.id ", "list");
					for (int i = 0; i < rs.size(); i++) {
						JSONObject obj = new JSONObject();
						obj.put("value", rs.get(i).getId());
						obj.put("text", rs.get(i).getOrgname());
						obj.put("code", tez.getOrgcode().substring(0, 3));
						arr.put(obj);
					}
				}
				if (domain.equalsIgnoreCase("code")) {

					SubAuditOrganization tez = (SubAuditOrganization) dao
							.getHQLResult("from SubAuditOrganization t where t.id='" + id + "'", "current");
					JSONObject obj = new JSONObject();
					obj.put("code", tez.getOrgcode().substring(0, 6));
					arr.put(obj);
				}
				if (domain.equalsIgnoreCase("editorg")) {
					if (id != 0) {
						SubAuditOrganization org = (SubAuditOrganization) dao
								.getHQLResult("from SubAuditOrganization t where t.id='" + id + "'", "current");
						// SubAuditOrganization tez=(SubAuditOrganization)
						// dao.getHQLResult("from SubAuditOrganization t where
						// t.orgcode like '"+org.getOrgcode().substring(0,
						// 3)+"000000%'", "current");
						// SubAuditOrganization ttz=(SubAuditOrganization)
						// dao.getHQLResult("from SubAuditOrganization t where
						// t.orgcode='"+org.getOrgcode().substring(0,
						// 6)+"000%'", "current");
						// System.out.println("json STR "+tez.getId()+
						// ttz.getId());
						JSONObject obj = new JSONObject();
						obj.put("id", org.getId());
						obj.put("orgcode", org.getOrgcode());
						obj.put("orgname", org.getOrgname());
						obj.put("fincategoryid", org.getFincategoryid());
						obj.put("catid", org.getCategoryid());
						obj.put("progid", org.getProgid());
						obj.put("departmentid", org.getDepartmentid());
						obj.put("regid", org.getRegid());
						obj.put("stateregid", org.getStateregid());
						obj.put("createdate", org.getCreatedate());
						obj.put("fsorg", org.getFsorg());
						obj.put("taxorg", org.getTaxorg());
						obj.put("ndorg", org.getNdorg());
						obj.put("headorder", org.getHeadorder());
						obj.put("web", org.getWeb());
						obj.put("email", org.getEmail());
						obj.put("fax", org.getFax());
						obj.put("phone", org.getPhone());
						obj.put("address", org.getAddress());
						obj.put("headfullname", org.getHeadfullname());
						obj.put("headreg", org.getHeadreg());
						obj.put("heademail", org.getHeademail());
						obj.put("headphone", org.getHeadphone());
						obj.put("headprof", org.getHeadprof());
						obj.put("accfullname", org.getAccfullname());
						obj.put("accprof", org.getAccprof());
						obj.put("accemail", org.getAccemail());
						obj.put("accphone", org.getAccphone());
						obj.put("accwyear", org.getAccwyear());
						obj.put("headsurname", org.getHeadsurname());
						obj.put("headpos", org.getHeadpos());
						obj.put("headwyear", org.getHeadwyear());

						obj.put("accsurname", org.getAccsurname());
						obj.put("accpos", org.getAccpos());
						obj.put("headwnum", org.getHeadwnum());
						obj.put("comwnum", org.getComwnum());
						obj.put("serwnum", org.getSerwnum());
						obj.put("otherwnum", org.getOtherwnum());
						obj.put("conwnum", org.getConwnum());
						/*
						 * obj.put("budget1", org.getPlan1());
						 * obj.put("budget2", org.getPlan2());
						 * obj.put("budget3", org.getPlan3());
						 * obj.put("complation1", org.getReport1());
						 * obj.put("complation2", org.getReport2());
						 * obj.put("complation3", org.getReport3());
						 * obj.put("ar1", org.getAuditresult1()); obj.put("ar2",
						 * org.getAuditresult2()); obj.put("ar3",
						 * org.getAuditresult3());
						 */

						String scheck = "[{";
						if (org.getBanks() == null) {
							obj.put("banks", "no");
						} else {

							String check = org.getBanks().substring(0, 2);
							// System.out.println("0000000000,"+check+",");
							if (check.equalsIgnoreCase(scheck)) {
								String Banks = "{a1:" + org.getBanks() + "}";
								JSONObject banksobj = new JSONObject(Banks);
								obj.put("banks", banksobj);
							} else {
								obj.put("banks", "yes" + org.getBanks());
							}
						}

						if (org.getStatebanks() == null) {
							obj.put("statebanks", "no");
						} else {

							String check = org.getStatebanks().substring(0, 2);
							// System.out.println("0000000000,"+check+",");
							if (check.equalsIgnoreCase(scheck)) {
								String stateBanks = "{a1:" + org.getStatebanks() + "}";
								JSONObject statebanksobj = new JSONObject(stateBanks);
								obj.put("statebanks", statebanksobj);
							} else {
								obj.put("statebanks", "yes" + org.getStatebanks());
							}
						}

						if (org.getStatedir() == null) {
							obj.put("statedir", "no");
						} else {

							String check = org.getStatedir().substring(0, 2);
							// System.out.println("0000000000,"+check+",");
							if (check.equalsIgnoreCase(scheck)) {
								String sdirs = "{a1:" + org.getStatedir() + "}";
								JSONObject sdirsobj = new JSONObject(sdirs);
								obj.put("statedir", sdirsobj);
							} else {
								obj.put("statedir", "yes" + org.getStatedir());
							}
						}

						if (org.getOwndir() == null) {
							obj.put("owndir", "no");
						} else {

							String check = org.getOwndir().substring(0, 2);
							// System.out.println("0000000000,"+check+",");
							if (check.equalsIgnoreCase(scheck)) {
								String xdirs = "{a1:" + org.getOwndir() + "}";
								JSONObject xdirsobj = new JSONObject(xdirs);
								obj.put("owndir", xdirsobj);
							} else {
								obj.put("owndir", "yes" + org.getOwndir());
							}
						}

						List<LnkOrgyplanreport> YPR = (List<LnkOrgyplanreport>) dao.getHQLResult(
								"from LnkOrgyplanreport t where t.orgid='" + id + "' order by t.year desc", "list");

						if (YPR.size() > 0) {
							JSONArray arrrr = new JSONArray();
							int number = 0;
							for (int i = 0; i < YPR.size(); i++) {
								if (number < 3) {
									JSONObject objj = new JSONObject();
									objj.put("year", YPR.get(i).getYear());
									objj.put("plan", YPR.get(i).getPlan());
									objj.put("report", YPR.get(i).getReport());
									objj.put("auditresult", YPR.get(i).getAuditresult());
									arrrr.put(objj);
									number = number + 1;
								}

							}

							obj.put("ypr", arrrr);
						}

						return obj.toString();
					}

					else {
						JSONObject obj = new JSONObject();
						obj.put("tez", "");
						obj.put("ttz", "");
						obj.put("id", 0);

						return obj.toString();
					}

				}
				if (domain.equalsIgnoreCase("editrisk")) {
					if (id == 0) {
						return null;
					} else {
						LutRisk org = (LutRisk) dao.getHQLResult("from LutRisk t where t.id='" + id + "'", "current");
						JSONObject obj = new JSONObject();
						obj.put("id", org.getId());
						obj.put("riskname", org.getRiskname());
						obj.put("type", org.getRisktype());
						obj.put("othertext", org.getOthertext());
						obj.put("other", org.getOther());
						/*
						 * obj.put("lname", org.getLawname()); obj.put("lzuil",
						 * org.getLawzuil()); obj.put("lzaalt",
						 * org.getLawzaalt()); obj.put("sname",
						 * org.getStandartname()); obj.put("szuil",
						 * org.getStandartzuil()); obj.put("szaalt",
						 * org.getStandartzaalt()); obj.put("other",
						 * org.getOthers());
						 */

						List<LnkRiskLaw> rlaws = (List<LnkRiskLaw>) dao
								.getHQLResult("from LnkRiskLaw t where t.riskid='" + id + "'", "list");
						JSONArray arrlaw = new JSONArray();
						JSONArray arrstan = new JSONArray();
						for (int i = 0; i < rlaws.size(); i++) {
							JSONObject objl = new JSONObject();
							JSONObject objs = new JSONObject();
							if (rlaws.get(i).getLawcategory() == 1) {
								objl.put("lawid", rlaws.get(i).getLawid());
								objl.put("zuilid", rlaws.get(i).getZuilid());
								objl.put("lname", rlaws.get(i).getLname());
								objl.put("lzuil", rlaws.get(i).getLzuil());
								objl.put("lzaalt", rlaws.get(i).getLzaalt());
								objl.put("zaalt", rlaws.get(i).getZaalt());
								arrlaw.put(objl);
							} else if (rlaws.get(i).getLawcategory() == 2) {
								objs.put("lawid", rlaws.get(i).getLawid());
								objs.put("zuilid", rlaws.get(i).getZuilid());
								objs.put("lname", rlaws.get(i).getLname());
								objs.put("lzuil", rlaws.get(i).getLzuil());
								objs.put("lzaalt", rlaws.get(i).getLzaalt());
								objs.put("zaalt", rlaws.get(i).getZaalt());
								arrstan.put(objs);
							}

						}
						obj.put("risklaws", arrlaw);
						obj.put("riskstandarts", arrstan);
						List<LnkRiskdir> dirs = (List<LnkRiskdir>) dao
								.getHQLResult("from LnkRiskdir t where t.riskid='" + id + "'", "list");
						JSONArray arrrr = new JSONArray();

						for (int i = 0; i < dirs.size(); i++) {
							JSONObject objj = new JSONObject();
							objj.put("id", dirs.get(i).getDirid());
							arrrr.put(objj);
						}
						obj.put("dirz", arrrr);

						return obj.toString();
					}
				}
				return arr.toString();
			}
			return arr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/core/list/{domain}", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult customers(@PathVariable String domain, @RequestBody String request,
			HttpServletRequest req) throws HttpRequestMethodNotSupportedException {
		Long count = (long) 0;
		List<?> rs = null;
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LutUser loguser = (LutUser) dao
				.getHQLResult("from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");

		DataSourceResult result = new DataSourceResult();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {

			if (domain.equalsIgnoreCase("LutMenu")) {
				List<LutMenu> wrap = new ArrayList<LutMenu>();

				rs = dao.kendojson(request, domain);
				count = (long) dao.resulsetcount(request, domain);

				for (int i = 0; i < rs.size(); i++) {
					LutMenu or = (LutMenu) rs.get(i);
					LutMenu cor = new LutMenu();
					cor.setId(or.getId());
					cor.setUicon(or.getUicon());
					cor.setIsactive(or.getIsactive());
					cor.setMenuname(or.getMenuname());
					cor.setStateurl(or.getStateurl());
					cor.setOrderid(or.getOrderid());
					cor.setParentid(or.getParentid());

					wrap.add(cor);
				}
				result.setData(wrap);
				result.setTotal(count);
			} else if (domain.equalsIgnoreCase("LutRole")) {
				List<LutRole> wrap = new ArrayList<LutRole>();

				rs = dao.kendojson(request, domain);
				count = (long) dao.resulsetcount(request, domain);

				for (int i = 0; i < rs.size(); i++) {
					LutRole or = (LutRole) rs.get(i);
					LutRole cor = new LutRole();
					cor.setId(or.getId());
					cor.setRolename(or.getRolename());
					cor.setRoleauth(or.getRoleauth());
					cor.setAccessid(or.getAccessid());
					wrap.add(cor);
				}
				result.setData(wrap);
				result.setTotal((long) count);
			} else if (domain.equalsIgnoreCase("LutUser")) {

				if (loguser == null) {
					List<LutUser> wrap = new ArrayList<LutUser>();

					rs = dao.kendojson(request, domain);
					count = (long) dao.resulsetcount(request, domain);

					for (int i = 0; i < rs.size(); i++) {
						LutUser or = (LutUser) rs.get(i);
						LutUser cor = new LutUser();
						List<LnkUserrole> rel = or.getLnkUserroles();
						// JSONArray arr=new JSONArray();
						String str = "";
						if (rel.size() > 0) {
							for (int y = 0; y < rel.size(); y++) {
								LnkUserrole rl = rel.get(y);
								str = str + "," + rl.getRoleid();
							}

							cor.setRoleid(str.substring(1));
						}

						cor.setId(or.getId());
						cor.setDepartmentid(or.getDepartmentid());
						cor.setFamilyname(or.getFamilyname());
						cor.setGivenname(or.getGivenname());
						cor.setEmail(or.getEmail());
						cor.setIsactive(or.getIsactive());
						cor.setIsstate(or.getIsstate());
						cor.setMobile(or.getMobile());
						cor.setPositionid(or.getPositionid());
						cor.setUsername(or.getUsername());
						cor.setPassword(or.getPassword());
						wrap.add(cor);
					}
					result.setData(wrap);
					result.setTotal((long) count);
				} else {
					if (loguser.getIsstate() == true) {

						List<LutUser> wrap = new ArrayList<LutUser>();

						rs = dao.kendojson(request, domain);
						count = (long) dao.resulsetcount(request, domain);

						for (int i = 0; i < rs.size(); i++) {
							LutUser or = (LutUser) rs.get(i);
							LutUser cor = new LutUser();
							List<LnkUserrole> rel = or.getLnkUserroles();
							// JSONArray arr=new JSONArray();
							String str = "";
							if (rel.size() > 0) {
								for (int y = 0; y < rel.size(); y++) {
									LnkUserrole rl = rel.get(y);
									str = str + "," + rl.getRoleid();
								}

								cor.setRoleid(str.substring(1));
							}

							cor.setId(or.getId());
							cor.setDepartmentid(or.getDepartmentid());
							cor.setFamilyname(or.getFamilyname());
							cor.setGivenname(or.getGivenname());
							cor.setEmail(or.getEmail());
							cor.setIsactive(or.getIsactive());
							cor.setIsstate(or.getIsstate());
							cor.setMobile(or.getMobile());
							cor.setPositionid(or.getPositionid());
							cor.setUsername(or.getUsername());
							cor.setPassword(or.getPassword());
							wrap.add(cor);
						}
						result.setData(wrap);
						result.setTotal((long) count);
					} else {
						List<LutUser> wrap = new ArrayList<LutUser>();
						List<LutUser> rss = (List<LutUser>) dao.getHQLResult(
								"from LutUser t where t.departmentid='" + loguser.getDepartmentid() + "'", "list");

						// rs= dao.kendojson(request, domain);
						// count=(long) dao.resulsetcount(request, domain);

						for (int i = 0; i < rss.size(); i++) {
							LutUser or = (LutUser) rss.get(i);
							LutUser cor = new LutUser();
							List<LnkUserrole> rel = or.getLnkUserroles();
							String str = "";
							if (rel.size() > 0) {
								for (int y = 0; y < rel.size(); y++) {
									LnkUserrole rl = rel.get(y);
									str = str + "," + rl.getRoleid();
								}

								cor.setRoleid(str.substring(1));
							}

							cor.setId(or.getId());
							cor.setDepartmentid(or.getDepartmentid());
							cor.setFamilyname(or.getFamilyname());
							cor.setGivenname(or.getGivenname());
							cor.setEmail(or.getEmail());
							cor.setIsactive(or.getIsactive());
							cor.setIsstate(or.getIsstate());
							cor.setMobile(or.getMobile());
							cor.setPositionid(or.getPositionid());
							cor.setUsername(or.getUsername());
							cor.setPassword(or.getPassword());
							wrap.add(cor);
						}
						count = (long) rss.size();
						result.setData(wrap);
						result.setTotal((long) count);
					}
				}
			}

			else if (domain.equalsIgnoreCase("LutDepartment")) {
				List<LutDepartment> wrap = new ArrayList<LutDepartment>();

				rs = dao.kendojson(request, domain);
				count = (long) dao.resulsetcount(request, domain);

				for (int i = 0; i < rs.size(); i++) {
					LutDepartment or = (LutDepartment) rs.get(i);
					LutDepartment cor = new LutDepartment();
					cor.setId(or.getId());
					cor.setDepartmentname(or.getDepartmentname());
					cor.setShortname(or.getShortname());
					cor.setEmail(or.getEmail());
					cor.setPhone(or.getPhone());
					cor.setAddress(or.getAddress());
					cor.setIsactive(or.getIsactive());
					cor.setWeb(or.getWeb());
					cor.setReg(or.getReg());
					cor.setLicnum(or.getLicnum());
					cor.setIsstate(or.getIsstate());
					cor.setLicexpiredate(or.getLicexpiredate());

					wrap.add(cor);
				}
				result.setData(wrap);
				result.setTotal((long) count);
			} else if (domain.equalsIgnoreCase("LutPosition")) {
				List<LutPosition> wrap = new ArrayList<LutPosition>();

				rs = dao.kendojson(request, domain);
				count = (long) dao.resulsetcount(request, domain);

				for (int i = 0; i < rs.size(); i++) {
					LutPosition or = (LutPosition) rs.get(i);
					LutPosition cor = new LutPosition();
					cor.setId(or.getId());
					cor.setPositionname(or.getPositionname());
					cor.setOrderid(or.getOrderid());
					cor.setIsactive(or.getIsactive());
					cor.setIsstate(or.getIsstate());
					wrap.add(cor);
				}
				result.setData(wrap);
				result.setTotal((long) count);
			} else if (domain.equalsIgnoreCase("SubAuditOrganization")) {

				if (loguser == null) {

					List<SubAuditOrganization> wrap = new ArrayList<SubAuditOrganization>();

					rs = dao.kendojson(request, domain);
					count = (long) dao.resulsetcount(request, domain);

					for (int i = 0; i < rs.size(); i++) {
						SubAuditOrganization or = (SubAuditOrganization) rs.get(i);
						SubAuditOrganization cor = new SubAuditOrganization();
						cor.setId(or.getId());
						cor.setOrgname(or.getOrgname());
						cor.setOrgcode(or.getOrgcode());
						cor.setDepartmentid(or.getDepartmentid());
						cor.setCategoryid(or.getCategoryid());
						cor.setFincategoryid(or.getFincategoryid());
						cor.setProgid(or.getProgid());
						cor.setRegid(or.getRegid());
						cor.setStateregid(or.getStateregid());
						cor.setFsorg(or.getFsorg());
						cor.setTaxorg(or.getTaxorg());
						cor.setNdorg(or.getNdorg());
						cor.setHeadorder(or.getHeadorder());
						cor.setBanks(or.getBanks());
						cor.setStatebanks(or.getStatebanks());
						cor.setWeb(or.getWeb());
						cor.setEmail(or.getEmail());
						cor.setAddress(or.getAddress());
						cor.setPhone(or.getPhone());
						cor.setFax(or.getFax());
						cor.setHeadfullname(or.getHeadfullname());
						cor.setHeademail(or.getHeademail());
						cor.setHeadphone(or.getHeadphone());
						cor.setHeadreg(or.getHeadreg());
						cor.setHeadprof(or.getHeadprof());
						cor.setAccfullname(or.getAccfullname());
						cor.setAccphone(or.getAccphone());
						cor.setAccemail(or.getAccemail());
						cor.setAccwyear(or.getAccwyear());
						cor.setAccprof(or.getAccprof());
						cor.setHeadsurname(or.getHeadsurname());
						cor.setHeadpos(or.getHeadpos());
						cor.setHeadwyear(or.getHeadwyear());
						cor.setAccsurname(or.getAccsurname());
						cor.setAccpos(or.getAccpos());
						cor.setAuditresult1(or.getAuditresult1());
						cor.setAuditresult2(or.getAuditresult2());
						cor.setAuditresult3(or.getAuditresult3());
						cor.setStatedir(or.getStatedir());
						cor.setOwndir(or.getOwndir());
						cor.setHeadwnum(or.getHeadwnum());
						cor.setComwnum(or.getComwnum());
						cor.setSerwnum(or.getSerwnum());
						cor.setConwnum(or.getConwnum());
						cor.setOtherwnum(or.getOtherwnum());
						cor.setPlan1(or.getPlan1());
						cor.setPlan2(or.getPlan2());
						cor.setPlan3(or.getPlan3());
						cor.setReport1(or.getReport1());
						cor.setReport2(or.getReport2());
						cor.setReport3(or.getReport3());

						wrap.add(cor);
					}
					result.setData(wrap);
					result.setTotal((long) count);
				} else {
					if (loguser.getIsstate() == true) {

						if (loguser.getDepartmentid() == 2) {
							List<SubAuditOrganization> wrap = new ArrayList<SubAuditOrganization>();
							JSONObject obj = new JSONObject(request);
							obj.remove("custom");
							System.out.println("lalallala");
							rs = dao.kendojson(obj.toString(), domain);
							count = (long) dao.resulsetcount(obj.toString(), domain);

							for (int i = 0; i < rs.size(); i++) {
								SubAuditOrganization or = (SubAuditOrganization) rs.get(i);
								SubAuditOrganization cor = new SubAuditOrganization();
								cor.setId(or.getId());
								cor.setOrgname(or.getOrgname());
								cor.setOrgcode(or.getOrgcode());
								cor.setDepartmentid(or.getDepartmentid());
								cor.setCategoryid(or.getCategoryid());
								cor.setFincategoryid(or.getFincategoryid());
								cor.setProgid(or.getProgid());
								cor.setRegid(or.getRegid());
								cor.setStateregid(or.getStateregid());
								cor.setFsorg(or.getFsorg());
								cor.setTaxorg(or.getTaxorg());
								cor.setNdorg(or.getNdorg());
								cor.setHeadorder(or.getHeadorder());
								cor.setBanks(or.getBanks());
								cor.setStatebanks(or.getStatebanks());
								cor.setWeb(or.getWeb());
								cor.setEmail(or.getEmail());
								cor.setAddress(or.getAddress());
								cor.setPhone(or.getPhone());
								cor.setFax(or.getFax());
								cor.setHeadfullname(or.getHeadfullname());
								cor.setHeademail(or.getHeademail());
								cor.setHeadphone(or.getHeadphone());
								cor.setHeadreg(or.getHeadreg());
								cor.setHeadprof(or.getHeadprof());
								cor.setAccfullname(or.getAccfullname());
								cor.setAccphone(or.getAccphone());
								cor.setAccemail(or.getAccemail());
								cor.setAccwyear(or.getAccwyear());
								cor.setAccprof(or.getAccprof());
								cor.setCreatedate(or.getCreatedate());

								wrap.add(cor);
							}
							result.setData(wrap);
							result.setTotal((long) count);
						} else {
							List<SubAuditOrganization> wrap = new ArrayList<SubAuditOrganization>();

							rs = dao.kendojson(request, domain);
							count = (long) dao.resulsetcount(request, domain);
							System.out.println("#######################################" + rs.size());
							System.out.println(count);
							for (int i = 0; i < rs.size(); i++) {
								SubAuditOrganization or = (SubAuditOrganization) rs.get(i);
								SubAuditOrganization cor = new SubAuditOrganization();
								cor.setId(or.getId());
								cor.setOrgname(or.getOrgname());
								cor.setOrgcode(or.getOrgcode());
								cor.setDepartmentid(or.getDepartmentid());
								cor.setCategoryid(or.getCategoryid());
								cor.setFincategoryid(or.getFincategoryid());
								cor.setProgid(or.getProgid());
								cor.setRegid(or.getRegid());
								cor.setStateregid(or.getStateregid());
								cor.setFsorg(or.getFsorg());
								cor.setTaxorg(or.getTaxorg());
								cor.setNdorg(or.getNdorg());
								cor.setHeadorder(or.getHeadorder());
								cor.setBanks(or.getBanks());
								cor.setStatebanks(or.getStatebanks());
								cor.setWeb(or.getWeb());
								cor.setEmail(or.getEmail());
								cor.setAddress(or.getAddress());
								cor.setPhone(or.getPhone());
								cor.setFax(or.getFax());
								cor.setHeadfullname(or.getHeadfullname());
								cor.setHeademail(or.getHeademail());
								cor.setHeadphone(or.getHeadphone());
								cor.setHeadreg(or.getHeadreg());
								cor.setHeadprof(or.getHeadprof());
								cor.setAccfullname(or.getAccfullname());
								cor.setAccphone(or.getAccphone());
								cor.setAccemail(or.getAccemail());
								cor.setAccwyear(or.getAccwyear());
								cor.setAccprof(or.getAccprof());
								cor.setCreatedate(or.getCreatedate());

								wrap.add(cor);
							}
							result.setData(wrap);
							result.setTotal((long) count);
						}
					} else {
						List<SubAuditOrganization> wrap = new ArrayList<SubAuditOrganization>();
						List<SubAuditOrganization> rss = (List<SubAuditOrganization>) dao.getHQLResult(
								"from SubAuditOrganization t where t.departmentid='" + loguser.getDepartmentid() + "'",
								"list");

						for (int i = 0; i < rss.size(); i++) {
							SubAuditOrganization or = (SubAuditOrganization) rss.get(i);
							SubAuditOrganization cor = new SubAuditOrganization();
							cor.setId(or.getId());
							cor.setOrgname(or.getOrgname());
							cor.setOrgcode(or.getOrgcode());
							cor.setDepartmentid(or.getDepartmentid());
							cor.setCategoryid(or.getCategoryid());
							cor.setFincategoryid(or.getFincategoryid());
							cor.setProgid(or.getProgid());
							cor.setRegid(or.getRegid());
							cor.setStateregid(or.getStateregid());
							cor.setFsorg(or.getFsorg());
							cor.setTaxorg(or.getTaxorg());
							cor.setNdorg(or.getNdorg());
							cor.setHeadorder(or.getHeadorder());
							cor.setBanks(or.getBanks());
							cor.setStatebanks(or.getStatebanks());
							cor.setWeb(or.getWeb());
							cor.setEmail(or.getEmail());
							cor.setAddress(or.getAddress());
							cor.setPhone(or.getPhone());
							cor.setFax(or.getFax());
							cor.setHeadfullname(or.getHeadfullname());
							cor.setHeademail(or.getHeademail());
							cor.setHeadphone(or.getHeadphone());
							cor.setHeadreg(or.getHeadreg());
							cor.setHeadprof(or.getHeadprof());
							cor.setAccfullname(or.getAccfullname());
							cor.setAccphone(or.getAccphone());
							cor.setAccemail(or.getAccemail());
							cor.setAccwyear(or.getAccwyear());
							cor.setAccprof(or.getAccprof());
							cor.setCreatedate(or.getCreatedate());

							wrap.add(cor);
						}
						count = (long) rss.size();
						result.setData(wrap);
						result.setTotal((long) count);

					}
				}

			}

			return result;
		}
		return null;
	}

	@RequestMapping(value = "/core/{action}/{domain}", method = RequestMethod.POST)
	public @ResponseBody String update(Model model, @RequestBody String jsonString, @PathVariable String action,
			@PathVariable String domain) throws JSONException, ClassCastException {
		System.out.println("json STR " + jsonString);
		try {
			Class<?> classtoConvert;
			JSONObject obj = new JSONObject(jsonString);
			JSONObject resp = new JSONObject();

			String domainName = domain;
			System.out.println(domain);
			classtoConvert = Class.forName(domain);
			Gson gson = new Gson();
			Object object = gson.fromJson(obj.toString(), classtoConvert);

			if (action.equals("update")) {

				if (!obj.has("models")) {

					if (domainName.equalsIgnoreCase("com.netgloo.models.LutUser")) {
						JSONObject str = new JSONObject(jsonString);

						dao.PeaceCrud(null, "LnkUserrole", "delete", (long) str.getLong("id"), 0, 0, "userid");

						LutUser cr = (LutUser) dao.getHQLResult("from LutUser t where t.id='" + str.getInt("id") + "'",
								"current");
						// cr.setLutDepartment(lutDepartment);(str.getLong("lpid"));
						cr.setFamilyname(str.getString("familyname"));
						cr.setGivenname(str.getString("givenname"));
						cr.setDepartmentid(str.getLong("departmentid"));
						cr.setPositionid(String.valueOf(str.getInt("positionid")));
						cr.setEmail(str.getString("email"));
						cr.setMobile(str.getString("mobile"));
						cr.setUsername(str.getString("username"));
						// cr.setProlevelid(cr.getProlevelid());
						if (cr.getPassword().equalsIgnoreCase(str.getString("password"))) {
							cr.setPassword(str.getString("password"));
						} else {
							cr.setPassword(passwordEncoder.encode(str.getString("password")));
						}

						cr.setIsactive(str.getBoolean("isactive"));
						dao.PeaceCrud(cr, domainName, "update", str.getLong("id"), 0, 0, null);

						LnkUserrole rl = new LnkUserrole();
						rl.setRoleid(str.getLong("roleid"));
						rl.setUserid(cr.getId());
						dao.PeaceCrud(rl, "CLnkUserRole", "save", (long) 0, 0, 0, null);

					} else if (domainName.equalsIgnoreCase("com.netgloo.models.LnkMainUser")) {

						JSONObject str = new JSONObject(jsonString);
						List<LnkMainUser> cr = (List<LnkMainUser>) dao
								.getHQLResult("from LnkMainUser t where t.userid='" + str.getInt("id")
										+ "' and t.appid='" + str.getInt("appid") + "'", "list");
						if (cr.size() > 0) {
							if (str.getInt("configid") == 0) {
								dao.PeaceCrud(null, "LnkMainUser", "delete", (long) cr.get(0).getId(), 0, 0, null);
							}
						} else {
							LnkMainUser lm = new LnkMainUser();
							lm.setAppid(str.getLong("appid"));
							lm.setUserid(str.getLong("id"));
							lm.setPositionid(str.getLong("positionid"));
							dao.PeaceCrud(lm, "LnkMainUser", "save", (long) 0, 0, 0, null);

						}
					}

					else if (domainName.equalsIgnoreCase("com.netgloo.models.MainAuditRegistration")) {

						JSONObject str = new JSONObject(jsonString);
						MainAuditRegistration cr = (MainAuditRegistration) dao.getHQLResult(
								"from MainAuditRegistration t where t.id='" + str.getLong("id") + "'", "current");
						if (!str.isNull("auditname")) {
							cr.setAuditname(str.getString("auditname"));
						}
						if (!str.isNull("orgcode")) {
							cr.setOrgcode(str.getString("orgcode"));
						}
						if (!str.isNull("orgname")) {
							cr.setOrgname(str.getString("orgname"));
						}

						dao.PeaceCrud(cr, "MainAuditRegistration", "update", (long) cr.getId(), 0, 0, null);
					}

					else {
						int id = (int) obj.getInt("id");
						dao.PeaceCrud(object, domainName, "update", (long) id, 0, 0, null);
					}
				}

				else {
					JSONArray rs = (JSONArray) obj.get("models");
					System.out.println("rs obj " + rs);
					for (int i = 0; i < rs.length(); i++) {
						String str = rs.get(i).toString();
						JSONObject batchobj = new JSONObject(str);
						Object bobj = gson.fromJson(batchobj.toString(), classtoConvert);
						int upid = batchobj.getInt("id");
						dao.PeaceCrud(bobj, domainName, "update", (long) upid, 0, 0, null);
					}

				}
				if (domainName.equalsIgnoreCase("com.netgloo.models.LutDepartment")) {
					JSONObject str = new JSONObject(jsonString);
					UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
							.getPrincipal();
					LutUser loguser = (LutUser) dao.getHQLResult(
							"from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");

					LutDepartment cr = (LutDepartment) dao
							.getHQLResult("from LutDepartment t where t.id='" + str.getInt("id") + "'", "current");
					if (str.getLong("isstate") == 1) {
						// cr.setLutDepartment(lutDepartment);(str.getLong("lpid"));
						if (obj.has("departmentname")) {
							cr.setDepartmentname(str.getString("departmentname"));
						}
						if (obj.has("email")) {
							cr.setEmail(str.getString("email"));
						}
						if (obj.has("phone")) {
							cr.setPhone(str.getString("phone"));
						}
						if (obj.has("web")) {
							cr.setWeb(str.getString("web"));
						}
						if (obj.has("shortname")) {
							cr.setShortname(str.getString("shortname"));
						}
						if (obj.has("address")) {
							cr.setAddress(str.getString("address"));
						}
						cr.setIsstate(str.getLong("isstate"));
						cr.setIsactive(str.getLong("isactive"));
						dao.PeaceCrud(cr, domainName, "update", (long) str.getLong("id"), 0, 0, null);
					} else {
						if (str.getLong("isactive") == 1) {

							if (obj.has("reg")) {

								LutDepartment depdup = (LutDepartment) dao.getHQLResult(
										"from LutDepartment t where t.reg='" + str.getString("reg") + "'", "current");
								if (depdup != null) {
									resp.put("re", 2);
									return resp.toString();
								} else {
									LutUser usrdup = (LutUser) dao.getHQLResult(
											"from LutUser t where t.username='" + str.getString("reg") + "'",
											"current");
									if (usrdup != null) {
										resp.put("re", 1);
										return resp.toString();
									} else {

										LutUser auser = (LutUser) dao.getHQLResult(
												"from LutUser t where t.username='" + cr.getReg() + "'", "current");

										if (auser != null) {
											if (cr.getReg() == str.getString("reg")) {
												auser.setIsactive(true);
												auser.setDepartmentid(loguser.getDepartmentid());
												auser.setFamilyname(str.getString("departmentname"));
												auser.setGivenname(str.getString("departmentname"));
												auser.setEmail(str.getString("email"));
												auser.setMobile(str.getString("phone"));
												dao.PeaceCrud(auser, "LutUser", "update", (auser.getId()), 0, 0, null);
											} else {
												auser.setIsactive(true);
												auser.setDepartmentid(loguser.getDepartmentid());
												auser.setFamilyname(str.getString("departmentname"));
												auser.setGivenname(str.getString("departmentname"));
												auser.setEmail(str.getString("email"));
												auser.setMobile(str.getString("phone"));
												auser.setIsstate(false);
												auser.setUsername(str.getString("reg"));
												auser.setPassword(passwordEncoder.encode(str.getString("reg")));

												dao.PeaceCrud(auser, "LutUser", "update", (auser.getId()), 0, 0, null);
											}
										} else {

											LutUser usr = new LutUser();

											usr.setDepartmentid(loguser.getDepartmentid());
											usr.setFamilyname(str.getString("departmentname"));
											usr.setGivenname(str.getString("departmentname"));
											usr.setEmail(str.getString("email"));
											usr.setMobile(str.getString("phone"));
											usr.setIsstate(false);
											usr.setUsername(str.getString("reg"));
											cr.setParentid(loguser.getDepartmentid());
											usr.setPassword(passwordEncoder.encode(str.getString("reg")));
											long vIn = str.getLong("isactive");
											boolean vOut = vIn != 0;
											usr.setIsactive(vOut);
											usr.setPositionid("7");
											dao.PeaceCrud(usr, "LutUser", "save", (long) 0, 0, 0, null);

											LnkUserrole rl = new LnkUserrole();
											rl.setRoleid(2599);
											rl.setUserid(usr.getId());
											dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);
										}

										cr.setReg(str.getString("reg"));
										cr.setParentid(loguser.getDepartmentid());
										cr.setDepartmentname(str.getString("departmentname"));
										cr.setEmail(str.getString("email"));
										cr.setPhone(str.getString("phone"));
										cr.setWeb(str.getString("web"));
										cr.setShortname(str.getString("shortname"));
										cr.setAddress(str.getString("address"));
										cr.setLicnum(str.getString("licnum"));
										cr.setLicexpiredate(str.getString("licexpiredate"));
										cr.setIsstate(str.getLong("isstate"));
										cr.setIsactive(1);

										dao.PeaceCrud(cr, domainName, "update", (long) str.getLong("id"), 0, 0, null);
										return "true";
									}
								}
							} else {
								resp.put("re", 3);
								return resp.toString();
							}
						} else {
							cr.setParentid(loguser.getDepartmentid());
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
							dao.PeaceCrud(cr, domainName, "update", (long) str.getLong("id"), 0, 0, null);

							LutUser auser = (LutUser) dao
									.getHQLResult("from LutUser t where t.username='" + cr.getReg() + "'", "current");

							if (auser != null) {
								auser.setIsactive(false);
								dao.PeaceCrud(auser, "LutUser", "update", (auser.getId()), 0, 0, null);
							}
						}
					}
				}

			} else if (action.equals("delete")) {

				if (domainName.equalsIgnoreCase("com.netgloo.models.LutDepartment")) {
					dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);
					LutDepartment stp = (LutDepartment) dao
							.getHQLResult("from LutDepartment t where t.id='" + obj.getLong("id") + "'", "current");
					if (stp.getIsstate() == 0) {

						LutUser usr = (LutUser) dao
								.getHQLResult("from LutUser t where t.username='" + stp.getReg() + "'", "current");
						dao.PeaceCrud(usr, "LutUser", "delete", usr.getId(), 0, 0, null);
					}
				} else if (domainName.equalsIgnoreCase("com.netgloo.models.SubAuditOrganization")) {
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult(
							"from SubAuditOrganization t where t.id='" + obj.getLong("id") + "'", "current");
					org.setIsactive(false);
					dao.PeaceCrud(org, "SubAuditOrganization", "update", org.getId(), 0, 0, null);
				} else if (domainName.equalsIgnoreCase("com.netgloo.models.LnkMainFormT2")) {
					List<LnkMainFormT2> ncat = (List<LnkMainFormT2>) dao.getHQLResult(
							"from LnkMainFormT2 t where t.rtype=" + obj.getLong("rtype") + " and t.dirid = "
									+ obj.getLong("dirid") + " and t.factorid = " + obj.getLong("factorid")
									+ " and t.riskid = " + obj.getLong("riskid") + " and t.mid = " + obj.getLong("mid"),
							"list");
					// LnkMainFormT2 form = (LnkMainFormT2)
					// dao.getHQLResult("from LnkMainFormT2 t where t.rtype="+
					// obj.getLong("rtype")+" and t.dirid = " +
					// obj.getLong("dirid") + " and t.factorid = " +
					// obj.getLong("factorid") + " and t.riskid = " +
					// obj.getLong("riskid") + " and t.mid = " +
					// obj.getLong("mid"), "current");
					for (LnkMainFormT2 t : ncat) {
						dao.PeaceCrud(t, "LnkMainFormT2", "delete", t.getId(), 0, 0, null);
					}
					// dao.PeaceCrud(form, domainName, "delete", form.getId(),
					// 0, 0, null);
				} else if (domainName.equalsIgnoreCase("com.netgloo.models.LutRisk")) {
					List<LnkRiskLaw> ncat = (List<LnkRiskLaw>) dao
							.getHQLResult("from LnkRiskLaw t where t.riskid='" + obj.getLong("id") + "'", "list");
					for (int f = 0; f < ncat.size(); f++) {
						dao.PeaceCrud(null, "LnkRiskLaw", "delete", (long) ncat.get(f).getId(), 0, 0, null);
					}
					List<LnkRiskdir> nncat = (List<LnkRiskdir>) dao
							.getHQLResult("from LnkRiskdir t where t.riskid='" + obj.getLong("id") + "'", "list");
					for (int f = 0; f < nncat.size(); f++) {
						dao.PeaceCrud(null, "LnkRiskdir", "delete", (long) nncat.get(f).getId(), 0, 0, null);
					}
					dao.PeaceCrud(object, "LutRisk", "delete", (long) obj.getLong("id"), 0, 0, null);
				} else if (domainName.equalsIgnoreCase("com.netgloo.models.LutLaw")) {
					List<LutLaw> ncat = (List<LutLaw>) dao
							.getHQLResult("from LutLaw t where t.parentid='" + obj.getLong("id") + "'", "list");
					if (ncat.size() > 0) {
						System.out.println("00000000000000000");
						return "false";
					} else {
						dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);
					}
				} else if (domainName.equalsIgnoreCase("com.netgloo.models.LutUser")) {
					LutUser ncat = (LutUser) dao.getHQLResult("from LutUser t where t.id='" + obj.getLong("id") + "'",
							"current");
					ncat.setIsactive(false);
					dao.PeaceCrud(ncat, domainName, "update", obj.getLong("id"), 0, 0, null);
				} else {
					dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);
				}

			} else if (action.equals("create")) {

				if (domainName.equalsIgnoreCase("com.netgloo.models.LutUser")) {

					JSONObject str = new JSONObject(jsonString);

					LutUser cr = new LutUser();
					cr.setFamilyname(str.getString("familyname"));
					cr.setGivenname(str.getString("givenname"));
					cr.setDepartmentid(str.getLong("departmentid"));
					cr.setPositionid(str.getString("positionid"));
					cr.setEmail(str.getString("email"));
					cr.setMobile(str.getString("mobile"));
					cr.setUsername(str.getString("username"));

					cr.setPassword(passwordEncoder.encode(str.getString("password")));
					cr.setIsactive(str.getBoolean("isactive"));
					dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);

					LnkUserrole rl = new LnkUserrole();
					rl.setRoleid(str.getLong("roleid"));
					rl.setUserid(cr.getId());
					dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);

				}

				else if (domainName.equalsIgnoreCase("com.netgloo.models.LutDepartment")) {
					JSONObject str = new JSONObject(jsonString);

					LutDepartment cr = new LutDepartment();
					if (str.getLong("isstate") == 1) {
						// cr.setLutDepartment(lutDepartment);(str.getLong("lpid"));
						cr.setDepartmentname(str.getString("departmentname"));
						cr.setEmail(str.getString("email"));
						cr.setPhone(str.getString("phone"));
						cr.setWeb(str.getString("web"));
						cr.setShortname(str.getString("shortname"));
						cr.setAddress(str.getString("address"));
						cr.setIsstate(str.getLong("isstate"));
						cr.setIsactive(str.getLong("isactive"));
						dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);
					} else {
						UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
								.getPrincipal();
						LutUser loguser = (LutUser) dao.getHQLResult(
								"from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");

						LutDepartment dep = (LutDepartment) dao.getHQLResult(
								"from LutDepartment t where t.reg='" + str.getString("reg") + "'", "current");

						if (dep != null) {
							resp.put("re", 1);
							return resp.toString();
						}

						else {
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
							cr.setParentid(loguser.getDepartmentid());

							dao.PeaceCrud(cr, domainName, "save", (long) 0, 0, 0, null);
						}
						LutUser dup = (LutUser) dao
								.getHQLResult("from LutUser t where t.username='" + cr.getReg() + "'", "current");

						if (dup != null) {
							resp.put("re", 2);
							return resp.toString();
						} else {
							LutUser usr = new LutUser();

							usr.setDepartmentid(loguser.getDepartmentid());
							usr.setFamilyname(str.getString("departmentname"));
							usr.setGivenname(str.getString("departmentname"));
							usr.setEmail(str.getString("email"));
							usr.setMobile(str.getString("phone"));
							usr.setIsstate(false);
							usr.setUsername(str.getString("reg"));
							usr.setPassword(passwordEncoder.encode(str.getString("reg")));
							long vIn = str.getLong("isactive");
							boolean vOut = vIn != 0;
							usr.setIsactive(vOut);
							usr.setPositionid("7");
							dao.PeaceCrud(usr, "LutUser", "save", (long) 0, 0, 0, null);

							LnkUserrole rl = new LnkUserrole();
							rl.setRoleid(2599);
							rl.setUserid(usr.getId());
							dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);
						}
					}
				}

				/*
				 * else
				 * if(domainName.equalsIgnoreCase("com.netgloo.models.LutLaw")){
				 * JSONObject str= new JSONObject(jsonString);
				 * 
				 * LutLaw cr= new LutLaw(); if(str.has("parenid")){
				 * cr.setLawname(str.getString("lawname")); } else{}
				 * 
				 * 
				 * }
				 */
				else {
					dao.PeaceCrud(object, domainName, "save", (long) 0, 0, 0, null);
				}

			}
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping(value = "/core/defaultSuccess", method = RequestMethod.GET)
	public String defaultSuccess(HttpServletRequest req, HttpServletResponse res) {
		try {
			req.setCharacterEncoding("utf-8");
			UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			JSONObject js = new JSONObject();
			Collection<?> coll = userDetail.getAuthorities();
			Iterator<?> itr = coll.iterator();
			while (itr.hasNext()) {
				String rolename = itr.next().toString();

				System.out.println("uj" + rolename);

				// String returnS=dao.loginedUserViewAuthority(userDetail, id);
				if ("ROLE_SUPER".equals(rolename)) {
					js.put("url", "restricted.dashboard");
				} else {

					LutUser loguser = (LutUser) dao.getHQLResult(
							"from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");

					LutMenu mnu = (LutMenu) dao.getHQLResult("from LutMenu t where t.id='"
							+ loguser.getLnkUserroles().get(0).getLutRole().getAccessid() + "'", "current");
					js.put("url", mnu.getStateurl());

				}
			}

			return js.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/core/parentmenus", method = RequestMethod.GET)
	public String parentmenus(HttpServletRequest req) throws ClassNotFoundException, JSONException {
		// List<LutMenu> rel=(List<LutMenu>) dao.getHQLResult("from LutMenu t
		// where t.stateurl!='#'", "list");
		List<LutMenu> rel = (List<LutMenu>) dao
				.getHQLResult("from LutMenu t where t.parentid is null order by t.orderid ", "list");
		JSONArray arr = new JSONArray();
		for (int i = 0; i < rel.size(); i++) {
			JSONObject fistList = new JSONObject();
			fistList.put("id", rel.get(i).getId());
			fistList.put("title", rel.get(i).getMenuname());
			fistList.put("text", rel.get(i).getMenuname());
			fistList.put("value", rel.get(i).getId());
			fistList.put("parent_id", rel.get(i).getParentid());

			arr.put(fistList);

			if (rel.get(i).getLutMenus().size() > 0) {

				List<LutMenu> chi = rel.get(i).getLutMenus();

				for (int j = 0; j < chi.size(); j++) {
					LutMenu rs = chi.get(j);
					JSONObject fistList1 = new JSONObject();
					fistList1.put("id", rs.getId());
					fistList1.put("title", rs.getMenuname());
					fistList1.put("text", rel.get(i).getMenuname());
					fistList1.put("value", rs.getId());
					fistList1.put("parent_id", rs.getParentid());
					arr.put(fistList1);

					if (rs.getLutMenus().size() > 0) {
						for (int c = 0; c < rs.getLutMenus().size(); c++) {
							LutMenu rc = rs.getLutMenus().get(c);
							JSONObject fistList2 = new JSONObject();
							fistList2.put("id", rc.getId());
							fistList2.put("title", rc.getMenuname());
							fistList2.put("text", rc.getMenuname());
							fistList2.put("value", rc.getId());
							fistList2.put("parent_id", rc.getParentid());
							arr.put(fistList2);

						}
					}
				}

			}

		}
		JSONObject wmap = new JSONObject();
		wmap.put("options", arr);

		return wmap.toString();
	}

	@RequestMapping(value = "/core/rjson/{id}/{path}", method = RequestMethod.GET, produces = {
			"application/json; charset=UTF-8" })
	public @ResponseBody String rjson(HttpServletRequest req, @PathVariable long id, @PathVariable String path) {
		try {

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			Collection<?> coll = userDetail.getAuthorities();
			Iterator<?> itr = coll.iterator();
			System.out.println("odoo end");
			while (itr.hasNext()) {
				String rolename = itr.next().toString();

				// String returnS=dao.loginedUserViewAuthority(userDetail, id);
				if ("ROLE_SUPER".equals(rolename)) {
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> wmap = new HashMap<String, Object>();
					wmap.put("rcreate", 1);
					wmap.put("rupdate", 1);
					wmap.put("rdelete", 1);
					wmap.put("rread", 1);
					wmap.put("rexport", 1);
					result.add(wmap);
					return mapper.writeValueAsString(wmap);
				} else {
					LutUser loguser = (LutUser) dao.getHQLResult("from LutUser t where t.id='" + id + "'", "current");
					LutMenu lm = (LutMenu) dao.getHQLResult("from LutMenu t where t.stateurl='" + path + "'",
							"current");

					List<LnkUserrole> us = loguser.getLnkUserroles();
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> wmap = new HashMap<String, Object>();

					JSONObject obj = new JSONObject();

					obj.put("rcreate", 0);
					obj.put("rupdate", 0);
					obj.put("rdelete", 0);
					obj.put("rread", 0);
					obj.put("rexport", 0);

					for (int u = 0; u < us.size(); u++) {
						System.out.println("@@" + us.get(u).getRoleid());
						List<LnkMenurole> rs = (List<LnkMenurole>) dao
								.getHQLResult("from LnkMenurole t where t.roleid='" + us.get(u).getRoleid()
										+ "' and t.menuid=" + lm.getId() + "", "list");
						if (rs.size() > 0) {
							if (rs.get(0).getRcreate() != 0) {
								obj.remove("rcreate");
								obj.put("rcreate", rs.get(0).getRcreate());
							}
							if (rs.get(0).getRupdate() != 0) {
								obj.remove("rupdate");
								obj.put("rupdate", rs.get(0).getRupdate());
							}
							if (rs.get(0).getRdelete() != 0) {
								obj.remove("rdelete");
								obj.put("rdelete", rs.get(0).getRdelete());
							}
							if (rs.get(0).getRread() != 0) {
								obj.remove("rread");
								obj.put("rread", rs.get(0).getRread());
							}
							if (rs.get(0).getRexport() != 0) {
								obj.remove("rexport");
								obj.put("rexport", rs.get(0).getRexport());
							}
						}
					}
					return obj.toString();
					// return mapper.writeValueAsString(wmap);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return path;

	}

	@RequestMapping(value = "/core/ujson", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody String ujson(HttpServletRequest req) {
		try {
			// List<Map<String, Object>> result = new ArrayList<Map<String,
			// Object>>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				JSONObject result = new JSONObject();
				LutUser loguser = null;
				UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				loguser = (LutUser) dao
						.getHQLResult("from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");
				Collection<?> coll = userDetail.getAuthorities();
				Iterator<?> itr = coll.iterator();
				long userid = 0;
				String roles = "";
				boolean rolesuper = false;
				while (itr.hasNext()) {
					String rolename = itr.next().toString();
					if ("ROLE_SUPER".equals(rolename) && rolename.length() > 0) {
						rolesuper = true;
					}
				}

				if (rolesuper) {
					result = services.getUjson(roles, true, loguser, userDetail);
				} else {
					for (int i = 0; i < loguser.getLnkUserroles().size(); i++) {
						roles = roles + "," + loguser.getLnkUserroles().get(i).getRoleid();
					}
					result = services.getUjson(roles, false, loguser, userDetail);
				}
				System.out.println("done ujson");
				return result.toString();
			}

			/*
			 * ObjectMapper mapper = new ObjectMapper(); return
			 * mapper.writeValueAsString(result);
			 */
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/core/mjson", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody String mjson(HttpServletRequest req) {
		try {

			JSONObject result = new JSONObject();
			LutUser loguser = null;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				Collection<?> coll = userDetail.getAuthorities();
				Iterator<?> itr = coll.iterator();
				loguser = (LutUser) dao
						.getHQLResult("from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");
				String roles = "";
				boolean rolesuper = false;
				while (itr.hasNext()) {
					String rolename = itr.next().toString();
					if ("ROLE_SUPER".equals(rolename) && rolename.length() > 0) {
						rolesuper = true;
					}
				}

				if (rolesuper) {
					result = services.getMjson(roles, true, loguser, userDetail);
				} else {
					for (int i = 0; i < loguser.getLnkUserroles().size(); i++) {
						roles = roles + "," + loguser.getLnkUserroles().get(i).getRoleid();
					}
					result = services.getMjson(roles, false, loguser, userDetail);
				}

				System.out.println("done mjson");

			}

			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * @RequestMapping(value="/core/mjson",method=RequestMethod.GET,
	 * produces={"application/json; charset=UTF-8"})
	 * 
	 * @Cacheable("mjson") public @ResponseBody String mjson(HttpServletRequest
	 * req){ try{ System.out.println("mjson"); List<Map<String, Object>> result
	 * = new ArrayList<Map<String, Object>>(); ArrayList<Long> arrlist = new
	 * ArrayList<Long>();
	 * 
	 * //LutUser loguser= null; Authentication auth =
	 * SecurityContextHolder.getContext().getAuthentication();
	 * 
	 * 
	 * if (!(auth instanceof AnonymousAuthenticationToken)) { UserDetails
	 * userDetail =
	 * (UserDetails)SecurityContextHolder.getContext().getAuthentication().
	 * getPrincipal(); LutUser loguser=(LutUser)
	 * dao.getHQLResult("from LutUser t where t.username='"+userDetail.
	 * getUsername()+"'", "current");
	 * 
	 * System.out.println("uname"+userDetail.getUsername()); Collection<?>
	 * coll=userDetail.getAuthorities(); Iterator<?> itr=coll.iterator();
	 * 
	 * while(itr.hasNext()){ String rolename = itr.next().toString();
	 * 
	 * //String returnS=dao.loginedUserViewAuthority(userDetail, id);
	 * if("ROLE_SUPER".equalsIgnoreCase(rolename)){ // List<LutMenu>
	 * rs=mpo.findById((long) 0); List<LutMenu> rs=(List<LutMenu>) dao.
	 * getHQLResult("from LutMenu t where t.parentid is null order by t.orderid desc"
	 * , "list"); if(rs.size()>0){ for(int i=0;i<rs.size();i++){
	 * Map<String,Object> wmap=new HashMap<String, Object>(); wmap.put("id",
	 * rs.get(i).getId()); wmap.put("title", rs.get(i).getMenuname());
	 * 
	 * wmap.put("icon", rs.get(i).getUicon());
	 * 
	 * List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
	 * 
	 * if(rs.get(i).getLutMenus().size()>0){ List<LutMenu>
	 * chi=rs.get(i).getLutMenus();
	 * 
	 * for(int j=0;j<rs.get(i).getLutMenus().size();j++){ Map<String,Object>
	 * child=new HashMap<String, Object>(); child.put("title",
	 * chi.get(j).getMenuname()); child.put("link", chi.get(j).getStateurl());
	 * childs.add(child); }
	 * 
	 * } else{ wmap.put("link", rs.get(i).getStateurl()); } wmap.put("submenu",
	 * childs); result.add(wmap); } }
	 * 
	 * } else{ List<LnkUserrole> rol=loguser.getLnkUserroles();
	 * List<LnkMenurole> roleset=(List<LnkMenurole>)
	 * rol.get(0).getLutRole().getLnkMenuroles(); if(roleset.size()>0){ for(int
	 * i=0;i<roleset.size();i++){ Map<String,Object> wmap=new HashMap<String,
	 * Object>(); if(roleset.get(i).getRread()>0 &&
	 * roleset.get(i).getLutMenu().getParentid()==null){ LutMenu
	 * lm=roleset.get(i).getLutMenu();
	 * 
	 * if(result.size()==0){ arrlist.add(lm.getId()); wmap.put("id",
	 * lm.getId()); wmap.put("title", lm.getMenuname());
	 * 
	 * wmap.put("icon", lm.getUicon());
	 * 
	 * List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
	 * if(roleset.get(i).getLutMenu().getLutMenus().size()>0){ List<LutMenu>
	 * chi=roleset.get(i).getLutMenu().getLutMenus();
	 * 
	 * for(int j=0;j<chi.size();j++){ LutMenu rs=chi.get(j);
	 * 
	 * for(int w=0;w<roleset.size();w++){
	 * if(roleset.get(w).getMenuid()==rs.getId()){
	 * if(roleset.get(w).getRread()>0){ Map<String,Object> child=new
	 * HashMap<String, Object>(); child.put("title", rs.getMenuname());
	 * 
	 * List<LutMenu> third=rs.getLutMenus(); if(third.size()>0){
	 * List<Map<String, Object>> tchilds = new ArrayList<Map<String, Object>>();
	 * for(int t=0; t<third.size(); t++){ LutMenu tlm=third.get(t);
	 * 
	 * for(int f=0;f<roleset.size();f++){
	 * if(roleset.get(f).getMenuid()==tlm.getId()){
	 * if(roleset.get(w).getRread()>0){ List<LnkMenurole>
	 * tl=tlm.getLnkMenuroles(); Map<String,Object> tchild=new HashMap<String,
	 * Object>(); tchild.put("title", tlm.getMenuname()); tchild.put("link",
	 * tlm.getStateurl()); tchilds.add(tchild); } } } } child.put("submenu",
	 * tchilds); } else{ child.put("link", rs.getStateurl()); }
	 * childs.add(child); } } }
	 * 
	 * }
	 * 
	 * } else{ wmap.put("link", roleset.get(i).getLutMenu().getStateurl()); }
	 * wmap.put("submenu", childs); result.add(wmap);
	 * 
	 * } else{ boolean retval2 = arrlist.contains(lm.getId());
	 * if(retval2!=true){ arrlist.add(lm.getId());
	 * 
	 * wmap.put("id", lm.getId()); wmap.put("title", lm.getMenuname());
	 * 
	 * wmap.put("icon", lm.getUicon());
	 * 
	 * List<Map<String, Object>> childs = new ArrayList<Map<String, Object>>();
	 * if(roleset.get(i).getLutMenu().getLutMenus().size()>0){ List<LutMenu>
	 * chi=roleset.get(i).getLutMenu().getLutMenus();
	 * 
	 * for(int j=0;j<chi.size();j++){ LutMenu rs=chi.get(j);
	 * 
	 * for(int w=0;w<roleset.size();w++){
	 * if(roleset.get(w).getMenuid()==rs.getId()){
	 * if(roleset.get(w).getRread()>0){ Map<String,Object> child=new
	 * HashMap<String, Object>(); child.put("title", rs.getMenuname());
	 * 
	 * List<LutMenu> third=rs.getLutMenus(); if(third.size()>0){
	 * List<Map<String, Object>> tchilds = new ArrayList<Map<String, Object>>();
	 * for(int t=0; t<third.size(); t++){ LutMenu tlm=third.get(t);
	 * 
	 * for(int f=0;f<roleset.size();f++){
	 * if(roleset.get(f).getMenuid()==tlm.getId()){
	 * if(roleset.get(w).getRread()>0){ List<LnkMenurole>
	 * tl=tlm.getLnkMenuroles(); Map<String,Object> tchild=new HashMap<String,
	 * Object>(); tchild.put("title", tlm.getMenuname()); tchild.put("link",
	 * tlm.getStateurl()); tchilds.add(tchild); } } } } child.put("submenu",
	 * tchilds); } else{ child.put("link", rs.getStateurl()); }
	 * childs.add(child); } } }
	 * 
	 * }
	 * 
	 * } else{ wmap.put("link", roleset.get(i).getLutMenu().getStateurl()); }
	 * wmap.put("submenu", childs); result.add(wmap); } }
	 * 
	 * } } } for(int y=0;y<rol.size();y++){
	 * 
	 * } }
	 * 
	 * } } ObjectMapper mapper = new ObjectMapper(); return
	 * mapper.writeValueAsString(result);
	 * 
	 * 
	 * } catch(Exception e){ e.printStackTrace(); return null; } }
	 */

	@ResponseBody
	@RequestMapping(value = "/core/action/delete/{domain}/{id}", method = RequestMethod.GET)
	public void delete(@PathVariable int id, @PathVariable String domain, HttpServletRequest req)
			throws ClassNotFoundException, JSONException {
		try {
			String appPath = req.getSession().getServletContext().getRealPath("");
			if (domain.equalsIgnoreCase("com.macronote.macrocms.domain.complex.ClientDynamicContent")) {
				/*
				 * ClientDynamicContent rel=(ClientDynamicContent)
				 * dao.getHQLResult("from ClientDynamicContent t where t.id="+id
				 * +"", "current"); String path=rel.getFile(); //clean-up String
				 * delPath = appPath + File.separator + path; File destination =
				 * new File(delPath);
				 * 
				 * 
				 * if(destination.delete()){
				 * System.out.println(destination.getName() + " is deleted!");
				 * }else{ System.out.println("Delete operation is failed."); }
				 * 
				 * dao.MacroCmsAction(null, domain, "delete", id, 0, 0, null);
				 * dao.
				 * getUniqueSqlResult("delete from client_catcon_rel where conID="
				 * +id+"");
				 */
			} else {
				dao.PeaceCrud(null, domain, "delete", (long) id, 0, 0, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/core/action/read/{domain}/{id}", method = RequestMethod.GET)
	public Object read(@PathVariable long id, @PathVariable String domain, HttpServletRequest req)
			throws ClassNotFoundException, JSONException {
		try {
			List<LnkMenurole> rel = lpo.findById(id);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < rel.size(); i++) {
				Map<String, Object> wmap = new HashMap<String, Object>();
				wmap.put("menuid", rel.get(i).getLutMenu().getId());
				wmap.put("roleid", rel.get(i).getLutRole().getId());
				wmap.put("create", rel.get(i).getRcreate());
				wmap.put("read", rel.get(i).getRread());
				wmap.put("update", rel.get(i).getRupdate());
				wmap.put("delete", rel.get(i).getRdelete());
				wmap.put("export", rel.get(i).getRexport());
				result.add(wmap);
			}
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/core/rolesubmit", method = RequestMethod.PUT, produces = {
			"application/json; charset=UTF-8" })
	public @ResponseBody String ajaxsubmit(@RequestBody String jsonString) throws JSONException {
		System.out.println(jsonString);
		JSONArray rs = new JSONArray(jsonString);

		for (int i = 0; i < rs.length(); i++) {
			String str = rs.get(i).toString();
			JSONObject batch = new JSONObject(str);

			String roleauth = batch.getString("roleauth");
			String rolename = batch.getString("rolename");
			int access = batch.getInt("accessid");
			int roleid = batch.getInt("roleid");
			System.out.println("sss" + roleid);
			if (roleid == 0) {
				LutRole object = new LutRole();
				object.setRolename(rolename);
				object.setRoleauth(roleauth);
				object.setAccessid(access);
				dao.PeaceCrud(object, "Category", "save", (long) 0, 0, 0, null);

				JSONArray mn = (JSONArray) batch.get("ilist");

				if (mn.length() > 0) {
					for (int j = 0; j < mn.length(); j++) {
						JSONObject itr = mn.getJSONObject(j);
						int menuid = Integer.parseInt(itr.get("menuid").toString());
						System.out.println("menuid" + menuid);
						int create = 0;
						int read = 0;
						int update = 0;
						int destroy = 0;
						int export = 0;
						JSONArray ids = (JSONArray) itr.get("ids");
						if (ids.length() > 0) {
							for (int c = 0; c < ids.length(); c++) {
								int r = (int) ids.get(c);
								switch (r) {
								case 1:
									create = 1;
									break;
								case 2:
									read = 1;
									break;
								case 3:
									update = 1;
									break;
								case 4:
									destroy = 1;
									break;
								case 5:
									export = 1;
									break;
								}
							}
						}
						LutMenu current = (LutMenu) dao.PeaceCrud(null, "LutMenu", "current", (long) menuid, 0, 0, "");

						LnkMenurole rmenu = new LnkMenurole();
						// rmenu.setLutMenu(current);
						rmenu.setMenuid(current.getId());
						rmenu.setRoleid(object.getId());
						rmenu.setRcreate(create);
						rmenu.setRread(read);
						rmenu.setRupdate(update);
						rmenu.setRdelete(destroy);
						rmenu.setRexport(export);
						rmenu.setOrderid(current.getOrderid());
						dao.PeaceCrud(rmenu, "Category", "save", (long) 0, 0, 0, null);

					}

				}
				return "true";
			} else {
				LutRole object1 = (LutRole) dao.getHQLResult("from LutRole t where t.id=" + roleid + "", "current");
				object1.setRoleauth(roleauth);
				object1.setRolename(rolename);
				object1.setAccessid(access);
				dao.PeaceCrud(object1, "Category", "update", (long) roleid, 0, 0, null);

				dao.PeaceCrud(null, "LnkMenurole", "delete", (long) roleid, 0, 0, "roleid");

				JSONArray mn = (JSONArray) batch.get("ilist");

				if (mn.length() > 0) {
					for (int j = 0; j < mn.length(); j++) {
						JSONObject itr = mn.getJSONObject(j);
						int menuid = Integer.parseInt(itr.get("menuid").toString());
						System.out.println("menuid" + menuid);
						int create = 0;
						int read = 0;
						int update = 0;
						int destroy = 0;
						int export = 0;
						JSONArray ids = (JSONArray) itr.get("ids");
						if (ids.length() > 0) {
							for (int c = 0; c < ids.length(); c++) {
								int r = (int) ids.get(c);
								switch (r) {
								case 1:
									create = 1;
									break;
								case 2:
									read = 1;
									break;
								case 3:
									update = 1;
									break;
								case 4:
									destroy = 1;
									break;
								case 5:
									export = 1;
									break;
								}
							}
						}
						LutMenu mnu = (LutMenu) dao.getHQLResult("from LutMenu t where t.id=" + menuid + "", "current");
						LnkMenurole rmenu = new LnkMenurole();
						rmenu.setRoleid(object1.getId());
						rmenu.setMenuid(mnu.getId());
						rmenu.setRcreate(create);
						rmenu.setRread(read);
						rmenu.setRupdate(update);
						rmenu.setRdelete(destroy);
						rmenu.setRexport(export);
						rmenu.setOrderid(mnu.getOrderid());
						dao.PeaceCrud(rmenu, "Category", "save", (long) 0, 0, 0, null);

					}
					return "true";
				}
			}

		}

		return "true";

	}

	@RequestMapping(value = "/service/send-mail", method = RequestMethod.PUT)
	public @ResponseBody String ajaxconf(@RequestBody String jsonString) throws JSONException, MessagingException {

		System.out.println(jsonString);

		JSONObject obj = new JSONObject(jsonString);
		List<LutUser> rs = (List<LutUser>) dao
				.getHQLResult("from LutUser t where t.email='" + obj.getString("username") + "'", "list");
		if (rs.size() > 0) {

			LutUser lus = rs.get(0);
			String uuid = UUID.randomUUID().toString();

			if (lus.getEmail() != null && lus.getEmail().length() > 0) {
				smtpMailSender.send(lus.getEmail(), "username" + " : " + lus.getUsername(), "password" + " : " + uuid);
				lus.setPassword(passwordEncoder.encode(uuid));
				dao.PeaceCrud(lus, "lu", "update", lus.getId(), 0, 0, null);
			}
		}

		return "true";
	}

	@RequestMapping(value = "/core/useradd/{id}", method = RequestMethod.PUT)
	public @ResponseBody String ajaxuser(@RequestBody String jsonString) throws JSONException, MessagingException {
		JSONObject obj = new JSONObject(jsonString);
		JSONObject result = new JSONObject();

		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LutUser loguser = (LutUser) dao
				.getHQLResult("from LutUser t where t.username='" + userDetail.getUsername() + "'", "current");

		// List<LutUser> orgs=(List<LutUser>) dao.getHQLResult("from LutUser t
		// where t.orgcode='"+obj.getString("orgcode")+"'", "list");
		if (obj.getLong("id") == 0) {
			LutUser norg = new LutUser();
			norg.setEmail(obj.getString("mail"));
			norg.setFamilyname(obj.getString("fname"));
			norg.setGivenname(obj.getString("gname"));
			norg.setUsername(obj.getString("uname"));
			norg.setMobile(obj.getString("phone"));
			norg.setPassword(passwordEncoder.encode(obj.getString("pass")));

			// norg.setRoleid(obj.getString("pos"));
			norg.setPositionid(obj.getString("pos"));
			norg.setDepartmentid(obj.getLong("org"));
			norg.setIsactive(true);
			norg.setIsstate(obj.getBoolean("isst"));

			dao.PeaceCrud(norg, "LutUser", "save", (long) 0, 0, 0, null);

			if (loguser != null && loguser.getIsstate() == false) {
				LnkUserrole rl = new LnkUserrole();
				rl.setRoleid(obj.getLong("roles"));
				rl.setUserid(norg.getId());
				dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);
			} else {
				JSONArray arr = obj.getJSONArray("roles");

				for (int a = 0; a < arr.length(); a++) {

					LnkUserrole rl = new LnkUserrole();
					rl.setRoleid(arr.getLong(a));
					rl.setUserid(norg.getId());
					dao.PeaceCrud(rl, "LnkUserRole", "save", (long) 0, 0, 0, null);

				}
			}

			result.put("re", 0);

		} else {
			LutUser norg = (LutUser) dao.getHQLResult("from LutUser t where t.id='" + obj.getLong("id") + "'",
					"current");
			norg.setEmail(obj.getString("mail"));
			norg.setFamilyname(obj.getString("fname"));
			norg.setGivenname(obj.getString("gname"));
			norg.setUsername(obj.getString("uname"));
			norg.setMobile(obj.getString("phone"));
			if (norg.getPassword().equalsIgnoreCase(obj.getString("pass"))) {
				norg.setPassword(obj.getString("pass"));
			} else {
				norg.setPassword(passwordEncoder.encode(obj.getString("pass")));
			}
			norg.setPositionid(obj.getString("pos"));
			norg.setDepartmentid(obj.getLong("org"));
			norg.setIsactive(true);
			norg.setIsstate(obj.getBoolean("isst"));
			dao.PeaceCrud(norg, "LutUser", "update", obj.getLong("id"), 0, 0, null);

			if (loguser != null && loguser.getIsstate() == false) {
				if (obj.has("roles")) {
					LnkUserrole rl = (LnkUserrole) dao
							.getHQLResult("from LnkUserrole t where t.userid='" + obj.getLong("id") + "'", "current");
					rl.setRoleid(obj.getLong("roles"));
					rl.setUserid(norg.getId());
					dao.PeaceCrud(rl, "LnkUserRole", "update", rl.getId(), 0, 0, null);
				}
			} else {
				if (obj.has("roles")) {

					// List<LnkUserrole> ur=(List<LnkUserrole>)
					// dao.getHQLResult("from LnkUserrole t where
					// t.userid='"+obj.getLong("id")+"'", "list");
					/*
					 * for(int f=0;f<ur.size();f++){ dao.PeaceCrud(null,
					 * "LnkUserrole", "delete", (long) ur.get(f).getId(), 0, 0,
					 * null); }
					 */

					System.out.println("lala");

					dao.PeaceCrud(null, "LnkUserrole", "delete", (long) obj.getLong("id"), 0, 0, "userid");

					JSONArray arr = obj.getJSONArray("roles");

					if (arr.length() > 0) {
						for (int a = 0; a < arr.length(); a++) {
							LnkUserrole rusr = new LnkUserrole();
							rusr.setRoleid(arr.getLong(a));
							rusr.setUserid(obj.getLong("id"));
							dao.PeaceCrud(rusr, "LnkUserrole", "save", (long) 0, 0, 0, null);

						}
					}
				}

			}

			result.put("re", 1);
		}

		return result.toString();

	}

	@RequestMapping(value = "/core/import/orgs", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFilenafile(@RequestParam("files") MultipartFile uploadfile, HttpServletRequest req) {

		try {
			InputStream fis = uploadfile.getInputStream();
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet st = workbook.getSheetAt(0);
			for (int i = 1; i < st.getLastRowNum() + 1; i++) {
				Row rw = st.getRow(i);
				SubAuditOrganization lr = (SubAuditOrganization) dao
						.getHQLResult("from SubAuditOrganization t where t.orgname='"
								+ rw.getCell(0).getStringCellValue() + "' order by t.id", "current");
				if (lr != null) {

					System.out.println("da" + rw.getCell(0).getStringCellValue());
					int type = rw.getCell(1).getCellType();

					switch (type) {

					case Cell.CELL_TYPE_NUMERIC:
						if (rw.getCell(1) != null) {
							String strCellValue = null;
							 Double value = rw.getCell(1).getNumericCellValue();
			                    Long longValue = value.longValue();
			                    System.out.println("sss"+longValue.toString());
			                    strCellValue = new String(longValue.toString());
			                    
							lr.setOrgcode(strCellValue);
						}
						break;
					case Cell.CELL_TYPE_STRING:
						if (rw.getCell(1).getStringCellValue() != null) {
							String strCellValue = null;
							 Double value = Double.parseDouble(rw.getCell(1).getStringCellValue());
			                    Long longValue = value.longValue();
			                    System.out.println("sss"+longValue.toString());
			                    strCellValue = new String(longValue.toString());
							lr.setOrgcode(strCellValue);
						}

						break;
					}
					/*
					 * if(rw.getCell(5)!=null){ lr.setProgid((long)
					 * rw.getCell(5).getNumericCellValue()); }
					 */
					if (rw.getCell(6) != null) {
						int tp = rw.getCell(6).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(6) != null) {
								lr.setPhone(String.valueOf(rw.getCell(6).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(6).getStringCellValue() != null) {

								lr.setPhone(rw.getCell(6).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(7) != null) {
						int tp = rw.getCell(7).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(7) != null) {
								lr.setRegid((long) rw.getCell(7).getNumericCellValue());
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(7).getStringCellValue() != null) {

								lr.setRegid(Long.parseLong(rw.getCell(7).getStringCellValue()));
							}

							break;
						}
					}
					if (rw.getCell(8) != null) {
						int tp = rw.getCell(8).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(8) != null) {
								lr.setStateregid(String.valueOf(rw.getCell(8).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(8).getStringCellValue() != null) {

								lr.setStateregid(rw.getCell(8).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(9) != null) {
						int tp = rw.getCell(9).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(9) != null) {
								lr.setFsorg(String.valueOf(rw.getCell(9).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(9).getStringCellValue() != null) {

								lr.setFsorg(rw.getCell(9).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(10) != null) {
						int tp = rw.getCell(10).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(10) != null) {
								lr.setTaxorg(String.valueOf(rw.getCell(10).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(10).getStringCellValue() != null) {

								lr.setTaxorg(rw.getCell(10).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(11) != null) {
						int tp = rw.getCell(11).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(11) != null) {
								lr.setNdorg(String.valueOf(rw.getCell(11).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(11).getStringCellValue() != null) {

								lr.setNdorg(rw.getCell(11).getStringCellValue());
							}

							break;
						}
						// lr.setNdorg(rw.getCell(11).getStringCellValue());
					}
					if (rw.getCell(12) != null) {
						int tp = rw.getCell(12).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(12) != null) {
								lr.setHeadorder(String.valueOf(rw.getCell(12).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(12).getStringCellValue() != null) {

								lr.setHeadorder(rw.getCell(12).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(13) != null) {
						int tp = rw.getCell(13).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(13) != null) {
								lr.setBanks(String.valueOf(rw.getCell(13).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(13).getStringCellValue() != null) {

								lr.setBanks(rw.getCell(13).getStringCellValue());
							}

							break;
						}
						// lr.setBanks(rw.getCell(13).getStringCellValue());
					}
					if (rw.getCell(14) != null) {
						int tp = rw.getCell(14).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(14) != null) {
								lr.setStatebanks(String.valueOf(rw.getCell(14).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(14).getStringCellValue() != null) {

								lr.setStatebanks(rw.getCell(14).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(15) != null) {
						int tp = rw.getCell(15).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(15) != null) {
								lr.setWeb(String.valueOf(rw.getCell(15).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(15).getStringCellValue() != null) {

								lr.setWeb(rw.getCell(15).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(16) != null) {
						int tp = rw.getCell(16).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(16) != null) {
								lr.setEmail(String.valueOf(rw.getCell(16).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(16).getStringCellValue() != null) {

								lr.setEmail(rw.getCell(16).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(17) != null) {
						int tp = rw.getCell(17).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(17) != null) {
								lr.setAddress(String.valueOf(rw.getCell(17).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(17).getStringCellValue() != null) {

								lr.setAddress(rw.getCell(17).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(18) != null) {
						int tp = rw.getCell(18).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(18) != null) {
								lr.setFax(String.valueOf(rw.getCell(18).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(18).getStringCellValue() != null) {

								lr.setFax(rw.getCell(18).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(19) != null) {
						int tp = rw.getCell(19).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(19) != null) {
								lr.setHeadsurname(String.valueOf(rw.getCell(19).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(19).getStringCellValue() != null) {

								lr.setHeadsurname(rw.getCell(19).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(20) != null) {
						int tp = rw.getCell(20).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(20) != null) {
								lr.setHeadfullname(String.valueOf(rw.getCell(20).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(20).getStringCellValue() != null) {

								lr.setHeadfullname(rw.getCell(20).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(21) != null) {
						int tp = rw.getCell(21).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(21) != null) {
								lr.setHeadpos(String.valueOf(rw.getCell(21).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(21).getStringCellValue() != null) {

								lr.setHeadpos(rw.getCell(21).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(22) != null) {
						int tp = rw.getCell(22).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(22) != null) {
								lr.setHeadphone(String.valueOf(rw.getCell(22).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(22).getStringCellValue() != null) {

								lr.setHeadphone(rw.getCell(22).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(23) != null) {
						int tp = rw.getCell(23).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(23) != null) {
								lr.setHeademail(String.valueOf(rw.getCell(23).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(23).getStringCellValue() != null) {

								lr.setHeademail(rw.getCell(23).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(24) != null) {
						int tp = rw.getCell(24).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(24) != null) {
								lr.setHeadwyear(String.valueOf(rw.getCell(24).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(24).getStringCellValue() != null) {

								lr.setHeadwyear(rw.getCell(24).getStringCellValue());
							}

							break;
						}
					}

					if (rw.getCell(25) != null) {
						int tp = rw.getCell(25).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(25) != null) {
								lr.setHeadprof(String.valueOf(rw.getCell(25).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(25).getStringCellValue() != null) {

								lr.setHeadprof(rw.getCell(25).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(26) != null) {
						int tp = rw.getCell(26).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(26) != null) {
								lr.setAccsurname(String.valueOf(rw.getCell(26).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(26).getStringCellValue() != null) {

								lr.setAccsurname(rw.getCell(26).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(27) != null) {
						int tp = rw.getCell(27).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(27) != null) {
								lr.setAccfullname(String.valueOf(rw.getCell(27).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(27).getStringCellValue() != null) {

								lr.setAccfullname(rw.getCell(27).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(28) != null) {
						int tp = rw.getCell(28).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(28) != null) {
								lr.setAccpos(String.valueOf(rw.getCell(28).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(28).getStringCellValue() != null) {

								lr.setAccpos(rw.getCell(28).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(29) != null) {
						int tp = rw.getCell(29).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(29) != null) {
								lr.setAccphone(String.valueOf(rw.getCell(29).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(29).getStringCellValue() != null) {

								lr.setAccphone(rw.getCell(29).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(30) != null) {
						int tp = rw.getCell(30).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(30) != null) {
								lr.setAccemail(String.valueOf(rw.getCell(30).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(30).getStringCellValue() != null) {

								lr.setAccemail(rw.getCell(30).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(31) != null) {
						int tp = rw.getCell(31).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(31) != null) {
								lr.setAccwyear(String.valueOf(rw.getCell(31).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(31).getStringCellValue() != null) {

								lr.setAccwyear(rw.getCell(31).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(32) != null) {
						int tp = rw.getCell(32).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(32) != null) {
								lr.setAccprof(String.valueOf(rw.getCell(32).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(32).getStringCellValue() != null) {

								lr.setAccprof(rw.getCell(32).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(33) != null) {
						int tp = rw.getCell(33).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(33) != null) {
								lr.setBudget1(String.valueOf(rw.getCell(33).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(33).getStringCellValue() != null) {

								lr.setBudget1(rw.getCell(33).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(34) != null) {
						int tp = rw.getCell(34).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(34) != null) {
								lr.setBudget2(String.valueOf(rw.getCell(34).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(34).getStringCellValue() != null) {

								lr.setBudget2(rw.getCell(34).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(35) != null) {
						int tp = rw.getCell(35).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(35) != null) {
								lr.setBudget3(String.valueOf(rw.getCell(35).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(35).getStringCellValue() != null) {

								lr.setBudget3(rw.getCell(35).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(36) != null) {
						int tp = rw.getCell(36).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(36) != null) {
								lr.setComplation1(String.valueOf(rw.getCell(36).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(36).getStringCellValue() != null) {

								lr.setComplation1(rw.getCell(36).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(37) != null) {
						int tp = rw.getCell(37).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(37) != null) {
								lr.setComplation2(String.valueOf(rw.getCell(37).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(37).getStringCellValue() != null) {

								lr.setComplation2(rw.getCell(37).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(38) != null) {
						int tp = rw.getCell(38).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(38) != null) {
								lr.setComplation3(String.valueOf(rw.getCell(38).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(38).getStringCellValue() != null) {

								lr.setComplation3(rw.getCell(38).getStringCellValue());
							}

							break;
						}
					}

					if (rw.getCell(39) != null) {
						int tp = rw.getCell(39).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(39) != null) {
								lr.setAr1(String.valueOf(rw.getCell(39).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(39).getStringCellValue() != null) {

								lr.setAr1(rw.getCell(39).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(40) != null) {
						int tp = rw.getCell(40).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(40) != null) {
								lr.setAr2(String.valueOf(rw.getCell(40).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(40).getStringCellValue() != null) {

								lr.setAr2(rw.getCell(40).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(41) != null) {
						int tp = rw.getCell(41).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(41) != null) {
								lr.setAr3(String.valueOf(rw.getCell(41).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(41).getStringCellValue() != null) {

								lr.setAr3(rw.getCell(41).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(42) != null) {
						int tp = rw.getCell(42).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(42) != null) {
								lr.setHeadwnum(String.valueOf(rw.getCell(42).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(42).getStringCellValue() != null) {

								lr.setHeadwnum(rw.getCell(42).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(43) != null) {
						int tp = rw.getCell(43).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(43) != null) {
								lr.setComwnum(String.valueOf(rw.getCell(43).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(43).getStringCellValue() != null) {

								lr.setComwnum(rw.getCell(43).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(44) != null) {
						int tp = rw.getCell(44).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(44) != null) {
								lr.setSerwnum(String.valueOf(rw.getCell(44).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(44).getStringCellValue() != null) {

								lr.setSerwnum(rw.getCell(44).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(45) != null) {
						int tp = rw.getCell(45).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(45) != null) {
								lr.setConwnum(String.valueOf(rw.getCell(45).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(45).getStringCellValue() != null) {

								lr.setConwnum(rw.getCell(45).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(46) != null) {
						int tp = rw.getCell(46).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(46) != null) {
								lr.setOtherwnum(String.valueOf(rw.getCell(46).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(46).getStringCellValue() != null) {

								lr.setOtherwnum(rw.getCell(46).getStringCellValue());
							}

							break;
						}
					}
					if (rw.getCell(47) != null) {
						int tp = rw.getCell(47).getCellType();

						switch (tp) {

						case Cell.CELL_TYPE_NUMERIC:
							if (rw.getCell(47) != null) {
								lr.setCreatedate(String.valueOf(rw.getCell(47).getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_STRING:
							if (rw.getCell(47).getStringCellValue() != null) {

								lr.setCreatedate(rw.getCell(47).getStringCellValue());
							}

							break;
						}
					}

					dao.PeaceCrud(lr, "SubAuditOrganization", "update", lr.getId(), 0, 0, null);
				}

			}

			return "true";
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
	}

	@RequestMapping(value = "/core/orgadd/{id}", method = RequestMethod.PUT)
	public @ResponseBody String ajaxo(@RequestBody String jsonString) throws JSONException, MessagingException {
		JSONObject obj = new JSONObject(jsonString);
		JSONObject result = new JSONObject();

		if (obj.has("orgcode") && obj.has("regid")) {

			String aa = obj.get("orgcode").toString();
			String bb = obj.get("regid").toString();
			int a = aa.length();
			int b = bb.length();
			if (a == 16) {
				List<SubAuditOrganization> orgs = (List<SubAuditOrganization>) dao.getHQLResult(
						"from SubAuditOrganization t where t.orgcode='" + obj.getString("orgcode") + "'", "list");
				if (obj.getLong("id") == 0) {
					if (orgs.size() == 0) {
						try {
							SubAuditOrganization norg = new SubAuditOrganization();
							if (obj.has("orgname")) {
								norg.setOrgname(obj.getString("orgname"));
							}
							if (obj.has("orgcode")) {
								norg.setOrgcode(obj.getString("orgcode"));
							}
							if (obj.has("departmentid")) {
								norg.setDepartmentid(obj.getInt("departmentid"));
							}
							if (obj.has("regid")) {
								norg.setRegid(obj.getLong("regid"));
							}
							if (obj.has("stateregid")) {
								norg.setStateregid(obj.getString("stateregid"));
							}
							if (obj.has("createdate")) {
								norg.setCreatedate(obj.getString("createdate"));
							}
							if (obj.has("fsorg")) {
								norg.setFsorg(obj.getString("fsorg"));
							}
							if (obj.has("taxorg")) {
								norg.setTaxorg(obj.getString("taxorg"));
							}
							if (obj.has("ndorg")) {
								norg.setNdorg(obj.getString("ndorg"));
							}
							if (obj.has("headorder")) {
								norg.setHeadorder(obj.getString("headorder"));
							}
							if (obj.has("web")) {
								norg.setWeb(obj.getString("web"));
							}
							if (obj.has("email")) {
								norg.setEmail(obj.getString("email"));
							}
							if (obj.has("fax")) {
								norg.setFax(obj.getString("fax"));
							}
							if (obj.has("phone")) {
								norg.setPhone(obj.getString("phone"));
							}
							if (obj.has("address")) {
								norg.setAddress(obj.getString("address"));
							}
							if (obj.has("headfullname")) {
								norg.setHeadfullname(obj.getString("headfullname"));
							}
							if (obj.has("headreg")) {
								norg.setHeadreg(obj.getString("headreg"));
							}
							if (obj.has("heademail")) {
								norg.setHeademail(obj.getString("heademail"));
							}
							if (obj.has("headphone")) {
								norg.setHeadphone(obj.getString("headphone"));
							}
							if (obj.has("headprof")) {
								norg.setHeadprof(obj.getString("headprof"));
							}
							if (obj.has("accfullname")) {
								norg.setAccfullname(obj.getString("accfullname"));
							}
							if (obj.has("accprof")) {
								norg.setAccprof(obj.getString("accprof"));
							}
							if (obj.has("accemail")) {
								norg.setAccemail(obj.getString("accemail"));
							}
							if (obj.has("accphone")) {
								norg.setAccphone(obj.getString("accphone"));
							}
							if (obj.has("accwyear")) {
								norg.setAccwyear(obj.getString("accwyear"));
							}

							if (obj.has("headsurname")) {
								norg.setHeadsurname(obj.getString("headsurname"));
							}
							if (obj.has("headpos")) {
								norg.setHeadpos(obj.getString("headpos"));
							}
							if (obj.has("headwyear")) {
								norg.setHeadwyear(obj.getString("headwyear"));
							}
							if (obj.has("ar1")) {
								norg.setAuditresult1(obj.getInt("ar1"));
							}
							if (obj.has("ar2")) {
								norg.setAuditresult2(obj.getInt("ar2"));
							}
							if (obj.has("ar3")) {
								norg.setAuditresult3(obj.getInt("ar3"));
							}
							if (obj.has("accsurname")) {
								norg.setAccsurname(obj.getString("accsurname"));
							}
							if (obj.has("accpos")) {
								norg.setAccpos(obj.getString("accpos"));
							}
							if (obj.has("headwnum")) {
								norg.setHeadwnum(obj.getString("headwnum"));
							}
							if (obj.has("comwnum")) {
								norg.setComwnum(obj.getString("comwnum"));
							}
							if (obj.has("serwnum")) {
								norg.setSerwnum(obj.getString("serwnum"));
							}
							if (obj.has("otherwnum")) {
								norg.setOtherwnum(obj.getString("otherwnum"));
							}
							if (obj.has("conwnum")) {
								norg.setConwnum(obj.getString("conwnum"));
							}

							if (obj.has("statedir")) {
								norg.setStatedir(obj.getString("statedir"));
							}
							if (obj.has("owndir")) {
								norg.setOwndir(obj.getString("owndir"));
							}
							if (obj.has("banks")) {
								norg.setBanks(obj.getString("banks"));
							}
							if (obj.has("statebanks")) {
								norg.setStatebanks(obj.getString("statebanks"));
							}

							if (obj.has("budget1")) {
								norg.setPlan1(String.valueOf(obj.get("budget1")));
							}
							if (obj.has("budget2")) {
								norg.setPlan2(String.valueOf(obj.get("budget2")));
							}
							if (obj.has("budget3")) {
								norg.setPlan3(String.valueOf(obj.get("budget3")));
							}
							if (obj.has("complation1")) {
								norg.setReport1(String.valueOf(obj.get("complation1")));
							}
							if (obj.has("complation2")) {
								norg.setReport2(String.valueOf(obj.get("complation2")));
							}
							if (obj.has("complation3")) {
								norg.setReport3(String.valueOf(obj.get("complation3")));
							}
							if (obj.has("fincategoryid")) {
								norg.setFincategoryid(obj.getInt("fincategoryid"));
							} else {
								norg.setFincategoryid(45);
							}
							if (obj.has("catid")) {
								norg.setCategoryid(obj.getInt("catid"));
							} else {
								norg.setCategoryid(80);
							}
							if (obj.has("progid")) {
								norg.setProgid(obj.getInt("progid"));
							} else {
								norg.setProgid(77);
							}
							norg.setIsactive(true);
							dao.PeaceCrud(norg, "SubAuditOrganization", "save", (long) 0, 0, 0, null);

							if (obj.has("budget1")) {
								String y1 = obj.get("year1").toString();
								int y11 = y1.length();
								if (y11 == 4) {
									LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
											.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
													+ "' and t.year='" + obj.getInt("year1") + "'", "current");
									if (b1 == null) {
										LnkOrgyplanreport lpr = new LnkOrgyplanreport();
										lpr.setYear(obj.getLong("year1"));
										lpr.setOrgid(norg.getId());
										System.out.println("vvvvvvvvv" + obj.get("budget1"));
										lpr.setPlan(String.valueOf(obj.get("budget1")));
										lpr.setReport("0");
										lpr.setAuditresult(0);
										dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
									} else {
										System.out.println("vvvvvvvvv" + obj.getLong("budget1"));
										b1.setPlan(String.valueOf(obj.get("budget1")));
										dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
									}
								} else {
									result.put("re", 31);
								}

							}

							if (obj.has("budget2")) {
								String y2 = obj.get("year2").toString();
								int y22 = y2.length();
								if (y22 == 4) {
									LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
											.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
													+ "' and t.year='" + obj.getInt("year2") + "'", "current");
									if (b1 == null) {
										LnkOrgyplanreport lpr = new LnkOrgyplanreport();
										lpr.setYear(obj.getLong("year2"));
										lpr.setOrgid(norg.getId());
										lpr.setPlan(String.valueOf(obj.get("budget2")));
										lpr.setReport("0");
										lpr.setAuditresult(0);
										dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
									} else {
										b1.setPlan(String.valueOf(obj.get("budget2")));
										dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
									}
								} else {
									result.put("re", 31);
								}
							}
							if (obj.has("budget3")) {
								String y3 = obj.get("year3").toString();
								int y33 = y3.length();
								if (y33 == 4) {
									LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
											.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
													+ "' and t.year='" + obj.getInt("year3") + "'", "current");
									if (b1 == null) {
										LnkOrgyplanreport lpr = new LnkOrgyplanreport();
										lpr.setYear(obj.getLong("year3"));
										lpr.setOrgid(norg.getId());
										lpr.setPlan(String.valueOf(obj.get("budget3")));
										lpr.setReport("0");
										lpr.setAuditresult(0);
										dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
									} else {
										b1.setPlan(String.valueOf(obj.get("budget3")));
										dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
									}
								} else {
									result.put("re", 31);
								}
							}
							if (obj.has("complation1")) {
								LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
										.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
												+ "' and t.year='" + obj.getInt("year1") + "'", "current");
								if (b1 == null) {
									LnkOrgyplanreport lpr = new LnkOrgyplanreport();
									lpr.setYear(obj.getLong("year1"));
									lpr.setOrgid(norg.getId());
									lpr.setReport(String.valueOf(obj.get("complation1")));
									lpr.setPlan("0");
									lpr.setAuditresult(0);
									dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
								} else {
									b1.setReport(String.valueOf(obj.get("complation1")));
									dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
								}
							}
							if (obj.has("complation2")) {
								LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
										.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
												+ "' and t.year='" + obj.getInt("year2") + "'", "current");
								if (b1 == null) {
									LnkOrgyplanreport lpr = new LnkOrgyplanreport();
									lpr.setYear(obj.getLong("year2"));
									lpr.setOrgid(norg.getId());
									lpr.setReport(String.valueOf(obj.get("complation2")));
									lpr.setPlan("0");
									lpr.setAuditresult(0);
									dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
								} else {
									b1.setReport(String.valueOf(obj.get("complation2")));
									dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
								}
							}
							if (obj.has("complation3")) {
								LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
										.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
												+ "' and t.year='" + obj.getInt("year3") + "'", "current");
								if (b1 == null) {
									LnkOrgyplanreport lpr = new LnkOrgyplanreport();
									lpr.setYear(obj.getLong("year3"));
									lpr.setOrgid(norg.getId());
									lpr.setReport(String.valueOf(obj.get("complation3")));
									lpr.setPlan("0");
									lpr.setAuditresult(0);
									dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
								} else {
									b1.setReport(String.valueOf(obj.get("complation3")));
									dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
								}
							}
							if (obj.has("ar1")) {
								System.out.println("fakaa");
								LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
										.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
												+ "' and t.year='" + obj.getInt("year1") + "'", "current");
								if (b1 == null) {
									LnkOrgyplanreport lpr = new LnkOrgyplanreport();
									lpr.setYear(obj.getLong("year1"));
									lpr.setOrgid(norg.getId());
									lpr.setAuditresult(obj.getLong("ar1"));
									dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
								} else {
									b1.setAuditresult(obj.getLong("ar1"));
									dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
								}
							}
							if (obj.has("ar2")) {
								System.out.println("fakaa");
								LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
										.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
												+ "' and t.year='" + obj.getInt("year2") + "'", "current");
								if (b1 == null) {
									LnkOrgyplanreport lpr = new LnkOrgyplanreport();
									lpr.setYear(obj.getLong("year2"));
									lpr.setOrgid(norg.getId());
									lpr.setAuditresult(obj.getLong("ar2"));
									dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
								} else {
									b1.setAuditresult(obj.getLong("ar2"));
									dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
								}
							}
							if (obj.has("ar3")) {
								System.out.println("fakaa");
								LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
										.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
												+ "' and t.year='" + obj.getInt("year3") + "'", "current");
								if (b1 == null) {
									LnkOrgyplanreport lpr = new LnkOrgyplanreport();
									lpr.setYear(obj.getLong("year3"));
									lpr.setOrgid(norg.getId());
									lpr.setAuditresult(obj.getLong("ar3"));
									dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
								} else {
									b1.setAuditresult(obj.getLong("ar3"));
									dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
								}
							}

							result.put("re", 0);
						} catch (Exception e) {
							e.printStackTrace();
							result.put("re", 21);
						}

					} else {
						result.put("re", 2);
					}

				} else {
					try {
						SubAuditOrganization norg = new SubAuditOrganization();
						norg.setId(obj.getLong("id"));
						if (obj.has("orgname")) {
							norg.setOrgname(obj.getString("orgname"));
						}
						if (obj.has("orgcode")) {
							norg.setOrgcode(obj.getString("orgcode"));
						}
						if (obj.has("departmentid")) {
							norg.setDepartmentid(obj.getInt("departmentid"));
						}
						if (obj.has("regid")) {
							norg.setRegid(obj.getLong("regid"));
						}
						if (obj.has("stateregid")) {
							norg.setStateregid(obj.getString("stateregid"));
						}
						if (obj.has("createdate")) {
							norg.setCreatedate(obj.getString("createdate"));
						}
						if (obj.has("fsorg")) {
							norg.setFsorg(obj.getString("fsorg"));
						}
						if (obj.has("taxorg")) {
							norg.setTaxorg(obj.getString("taxorg"));
						}
						if (obj.has("ndorg")) {
							norg.setNdorg(obj.getString("ndorg"));
						}
						if (obj.has("headorder")) {
							norg.setHeadorder(obj.getString("headorder"));
						}
						if (obj.has("web")) {
							norg.setWeb(obj.getString("web"));
						}
						if (obj.has("email")) {
							norg.setEmail(obj.getString("email"));
						}
						if (obj.has("fax")) {
							norg.setFax(obj.getString("fax"));
						}
						if (obj.has("phone")) {
							norg.setPhone(obj.getString("phone"));
						}
						if (obj.has("address")) {
							norg.setAddress(obj.getString("address"));
						}
						if (obj.has("headfullname")) {
							norg.setHeadfullname(obj.getString("headfullname"));
						}
						if (obj.has("headreg")) {
							norg.setHeadreg(obj.getString("headreg"));
						}
						if (obj.has("heademail")) {
							norg.setHeademail(obj.getString("heademail"));
						}
						if (obj.has("headphone")) {
							norg.setHeadphone(obj.getString("headphone"));
						}
						if (obj.has("headprof")) {
							norg.setHeadprof(obj.getString("headprof"));
						}
						if (obj.has("accfullname")) {
							norg.setAccfullname(obj.getString("accfullname"));
						}
						if (obj.has("accprof")) {
							norg.setAccprof(obj.getString("accprof"));
						}
						if (obj.has("accemail")) {
							norg.setAccemail(obj.getString("accemail"));
						}
						if (obj.has("accphone")) {
							norg.setAccphone(obj.getString("accphone"));
						}
						if (obj.has("accwyear")) {
							norg.setAccwyear(obj.getString("accwyear"));
						}

						if (obj.has("headsurname")) {
							norg.setHeadsurname(obj.getString("headsurname"));
						}
						if (obj.has("headpos")) {
							norg.setHeadpos(obj.getString("headpos"));
						}
						if (obj.has("headwyear")) {
							norg.setHeadwyear(obj.getString("headwyear"));
						}
						if (obj.has("ar1")) {
							norg.setAuditresult1(obj.getInt("ar1"));
						}
						if (obj.has("ar2")) {
							norg.setAuditresult2(obj.getInt("ar2"));
						}
						if (obj.has("ar3")) {
							norg.setAuditresult3(obj.getInt("ar3"));
						}
						if (obj.has("accsurname")) {
							norg.setAccsurname(obj.getString("accsurname"));
						}
						if (obj.has("accpos")) {
							norg.setAccpos(obj.getString("accpos"));
						}
						if (obj.has("headwnum")) {
							norg.setHeadwnum(obj.getString("headwnum"));
						}
						if (obj.has("comwnum")) {
							norg.setComwnum(obj.getString("comwnum"));
						}
						if (obj.has("serwnum")) {
							norg.setSerwnum(obj.getString("serwnum"));
						}
						if (obj.has("otherwnum")) {
							norg.setOtherwnum(obj.getString("otherwnum"));
						}
						if (obj.has("conwnum")) {
							norg.setConwnum(obj.getString("conwnum"));
						}

						if (obj.has("statedir")) {
							norg.setStatedir(obj.getString("statedir"));
						}
						if (obj.has("owndir")) {
							norg.setOwndir(obj.getString("owndir"));
						}
						if (obj.has("banks")) {
							norg.setBanks(obj.getString("banks"));
						}
						if (obj.has("statebanks")) {
							norg.setStatebanks(obj.getString("statebanks"));
						}
						norg.setIsactive(true);
						if (obj.has("fincategoryid")) {
							norg.setFincategoryid(obj.getInt("fincategoryid"));
						} else {
							norg.setFincategoryid(45);
						}
						if (obj.has("catid")) {
							norg.setCategoryid(obj.getInt("catid"));
						} else {
							norg.setCategoryid(80);
						}
						if (obj.has("progid")) {
							norg.setProgid(obj.getInt("progid"));
						} else {
							norg.setProgid(77);
						}

						if (obj.has("budget1")) {
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + obj.getLong("id")
											+ "' and t.year='" + obj.getInt("year1") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year1"));
								lpr.setOrgid(obj.getLong("id"));
								lpr.setPlan(String.valueOf(obj.get("budget1")));
								lpr.setReport("0");
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setPlan(String.valueOf(obj.get("budget1")));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
							norg.setPlan1(String.valueOf(obj.get("budget1")));
							;
						}

						if (obj.has("budget2")) {
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + obj.getLong("id")
											+ "' and t.year='" + obj.getInt("year2") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year2"));
								lpr.setOrgid(obj.getLong("id"));
								lpr.setPlan(String.valueOf(obj.get("budget2")));
								lpr.setReport("0");
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setPlan(String.valueOf(obj.getString("budget2")));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
							norg.setPlan2(String.valueOf(obj.get("budget2")));
						}
						if (obj.has("budget3")) {
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + obj.getLong("id")
											+ "' and t.year='" + obj.getInt("year3") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year3"));
								lpr.setOrgid(obj.getLong("id"));
								lpr.setPlan(String.valueOf(obj.get("budget3")));
								lpr.setReport("0");
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setPlan(String.valueOf(obj.getString("budget3")));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
							norg.setPlan3(String.valueOf(obj.get("budget3")));
						}
						if (obj.has("complation1")) {
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + obj.getLong("id")
											+ "' and t.year='" + obj.getInt("year1") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year1"));
								lpr.setOrgid(obj.getLong("id"));
								lpr.setReport(String.valueOf(obj.getString("complation1")));
								lpr.setPlan("0");
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setReport(String.valueOf(obj.get("complation1")));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
							norg.setReport1(String.valueOf(obj.get("complation1")));
						}
						if (obj.has("complation2")) {
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + obj.getLong("id")
											+ "' and t.year='" + obj.getInt("year2") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year2"));
								lpr.setOrgid(obj.getLong("id"));
								lpr.setReport(String.valueOf(obj.getString("complation2")));
								lpr.setPlan("0");
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setReport(String.valueOf(obj.getString("complation2")));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
							norg.setReport2(String.valueOf(obj.get("complation2")));
						}
						if (obj.has("complation3")) {
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + obj.getLong("id")
											+ "' and t.year='" + obj.getInt("year3") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year3"));
								lpr.setOrgid(obj.getLong("id"));
								lpr.setReport(String.valueOf(obj.getString("complation3")));
								lpr.setPlan("0");
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setReport(String.valueOf(obj.getString("complation3")));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
							norg.setReport3(String.valueOf(obj.get("complation3")));
						}
						if (obj.has("ar1")) {
							// System.out.println("fakaa");
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
											+ "' and t.year='" + obj.getInt("year1") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year1"));
								lpr.setOrgid(norg.getId());
								lpr.setAuditresult(obj.getLong("ar1"));
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setAuditresult(obj.getLong("ar1"));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
						}
						if (obj.has("ar2")) {
							// System.out.println("fakaa");
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
											+ "' and t.year='" + obj.getInt("year2") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year2"));
								lpr.setOrgid(norg.getId());
								lpr.setAuditresult(obj.getLong("ar2"));
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setAuditresult(obj.getLong("ar2"));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
						}
						if (obj.has("ar3")) {
							// System.out.println("fakaa");
							LnkOrgyplanreport b1 = (LnkOrgyplanreport) dao
									.getHQLResult("from LnkOrgyplanreport t where t.orgid='" + norg.getId()
											+ "' and t.year='" + obj.getInt("year3") + "'", "current");
							if (b1 == null) {
								LnkOrgyplanreport lpr = new LnkOrgyplanreport();
								lpr.setYear(obj.getLong("year3"));
								lpr.setOrgid(norg.getId());
								lpr.setAuditresult(obj.getLong("ar3"));
								dao.PeaceCrud(lpr, "LnkOrgyplanreport", "save", (long) 0, 0, 0, null);
							} else {
								b1.setAuditresult(obj.getLong("ar3"));
								dao.PeaceCrud(b1, "LnkOrgyplanreport", "update", (b1.getId()), 0, 0, null);
							}
						}
						dao.PeaceCrud(norg, "SubAuditOrganization", "update", (obj.getLong("id")), 0, 0, null);

						result.put("re", 1);
					} catch (Exception e) {
						e.printStackTrace();
						result.put("re", 21);
					}
				}

			} else {
				if (a != 16) {
					result.put("re", 8);
				} else {
					result.put("re", 9);
				}
			}

		} else {
			if (obj.has("orgcode") && obj.getString("regid") == null) {
				result.put("re", 5);
			} else if (obj.has("regid")) {
				result.put("re", 6);
			} else {
				result.put("re", 3);
			}
		}

		return result.toString();

	}

	@RequestMapping(value = "/api/lang/{key}", method = RequestMethod.GET)
	public String saveForm(@PathVariable String key, HttpServletRequest req) throws IOException {

		// List<CLutLang> it=(List<CLutLang>) langRepo.findAll();

		JSONArray main = new JSONArray();
		JSONObject obj = new JSONObject();

		/*
		 * if(key.equalsIgnoreCase("en")){ for(int i=0;i<it.size();i++){
		 * obj.put(it.get(i).getNameMn(),it.get(i).getNameEn()); main.put(obj);
		 * } }else{ for(int i=0;i<it.size();i++){
		 * obj.put(it.get(i).getNameEn(),it.get(i).getNameMn()); main.put(obj);
		 * } }
		 */

		return obj.toString();

	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Principal user(Principal user) {
		return user;
	}

	/*
	 * @RequestMapping(value="/user", method = RequestMethod.GET) public
	 * Principal user(Principal user) { return user; }
	 * 
	 * @RequestMapping(value = "/login", method = RequestMethod.GET) public
	 * String login(@RequestParam(value = "error", required = false) String
	 * error,
	 * 
	 * @RequestParam(value = "logout", required = false) String logout,
	 * HttpServletRequest request) {
	 * 
	 * HttpSession session = request.getSession();
	 * session.setMaxInactiveInterval(1*60); System.out.println("logged out");
	 * return "/"; }
	 * 
	 * @RequestMapping(value="/logout", method = RequestMethod.POST) public
	 * String logoutPage (HttpServletRequest request, HttpServletResponse
	 * response) { Authentication auth =
	 * SecurityContextHolder.getContext().getAuthentication(); if (auth !=
	 * null){ new SecurityContextLogoutHandler().logout(request, response,
	 * auth); } System.out.println("logged out"); return
	 * "redirect:/login?logout";//You can redirect wherever you want, but
	 * generally it's a good practice to show login screen again. }
	 */

}
