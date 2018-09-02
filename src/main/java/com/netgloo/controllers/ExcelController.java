package com.netgloo.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger.OutputField;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.hazelcast.com.eclipsesource.json.JsonArray;
import com.netgloo.service.FileUploadService;
import com.google.gson.JsonObject;
import com.netgloo.dao.UserDao;
import com.netgloo.models.FinFormG1;
import com.netgloo.models.FinFormG2;
import com.netgloo.models.FinNt2;
import com.netgloo.models.FinTgt1;
import com.netgloo.models.FsFormA4;
import com.netgloo.models.LnkMainForm;
import com.netgloo.models.LnkMainFormT2;
import com.netgloo.models.LnkMainUser;
import com.netgloo.models.LnkRiskT2;
import com.netgloo.models.LnkRiskTryout;
import com.netgloo.models.LutAuditDir;
import com.netgloo.models.LutAuditWork;
import com.netgloo.models.LutExpProgcategory;
import com.netgloo.models.LutFormB1;
import com.netgloo.models.LutPosition;
import com.netgloo.models.LutReason;
import com.netgloo.models.LutUser;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.models.SourceData;
import com.netgloo.models.SubAuditOrganization;

@RestController
@RequestMapping("/excel")
public class ExcelController {

	@Autowired
	private UserDao dao;


	@Autowired
	FileUploadService fileUploadService;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@RequestMapping(value="/json/{fname}/{furl}/{id}",method=RequestMethod.GET)
	public String exportJson(@PathVariable String fname,@PathVariable String furl, @PathVariable long id, HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			String appPath = req.getServletContext().getRealPath("");	

			if(fname.equalsIgnoreCase("b1")){
				System.out.println("sheet oldson b1");
				return "true";
			}
			else if(fname.equalsIgnoreCase("b23")){
				return "false";
			}
		}
		return furl;
	}
	
	@RequestMapping(value="/check/{type}/{id}",method=RequestMethod.POST)
	public Boolean check(@PathVariable String type, @PathVariable long id,HttpServletRequest req) throws JSONException, DocumentException, Exception {
		String appPath = req.getServletContext().getRealPath("");	
		if (type.equalsIgnoreCase("auditAttachment")){
			LutAuditWork work = (LutAuditWork) dao.getHQLResult("from LutAuditWork t where t.fileurl='gb5155'", "current");
			if (work != null){
				List<LnkMainForm> mainform = (List<LnkMainForm>) dao.getHQLResult("from LnkMainForm t where t.mid="+id+" and t.wid = " +work.getId()+" order by t.id desc", "list");
				if (mainform != null && !mainform.isEmpty()){
					System.out.println(appPath+File.separator+mainform.get(0).getFileurl());
					File files = new File(appPath+File.separator+mainform.get(0).getFileurl());
					if (files.exists()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@RequestMapping(value="/verify/{type}/{id}",method=RequestMethod.POST)
	public String checklicense(@PathVariable String type, @PathVariable long id,HttpServletRequest req) throws JSONException, DocumentException, Exception {
		JSONObject obj= new JSONObject();
		if (type.equalsIgnoreCase("sublicense")){
			MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
			List<SubAuditOrganization> org = (List<SubAuditOrganization>) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "list");
			if(org.size()>0){
				if(org.size()==1){
					obj.put("single", true);
				}else if(org.size()>1){
					obj.put("multiple", true);
				}
			}
			else{
				obj.put("nofound", true);
			}
			return obj.toString();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value="/{fname}/{furl}/{id}",method=RequestMethod.GET)
	public void exportOrgInfo(@PathVariable String fname,@PathVariable String furl, @PathVariable long id, HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			FileInputStream fis1 = null;
			FileInputStream fis2 = null;
			File files = null;
			File files1 = null;
			File files2 = null;
			String appPath = req.getServletContext().getRealPath("");	

			files = new File(appPath+"/assets/zagvarfile/working_papers.xlsx");
			files1 = new File(appPath+"/assets/zagvarfile/working_papers-В-STAUS-II.xlsx");
			files2 = new File(appPath+"/assets/zagvarfile/STAUSEXPORT.xlsx");

			fis = new FileInputStream(files);
			fis2 = new FileInputStream(files2);
			Workbook workbook = new XSSFWorkbook(fis);

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setBorderBottom((short)1);
			cellStyle.setBorderLeft((short)1);
			cellStyle.setBorderRight((short)1);
			cellStyle.setBorderTop((short)1);
			cellStyle.setWrapText(true);
			cellStyle.setAlignment(cellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(cellStyle.VERTICAL_CENTER);

			CellStyle cellStyleLeft = workbook.createCellStyle();
			cellStyleLeft.setBorderBottom((short)1);
			cellStyleLeft.setBorderLeft((short)1);
			cellStyleLeft.setBorderRight((short)1);
			cellStyleLeft.setBorderTop((short)1);
			cellStyleLeft.setWrapText(true);
			cellStyleLeft.setAlignment(cellStyle.ALIGN_JUSTIFY);
			cellStyleLeft.setVerticalAlignment(cellStyle.VERTICAL_CENTER);
			
			fis1 = new FileInputStream(files1);
			Workbook workbook1 = new XSSFWorkbook(fis1);

			CellStyle cellStyle1 = workbook1.createCellStyle();
			cellStyle1.setBorderBottom((short)1);
			cellStyle1.setBorderLeft((short)1);
			cellStyle1.setBorderRight((short)1);
			cellStyle1.setBorderTop((short)1);
			cellStyle1.setWrapText(true);
			cellStyle1.setAlignment(cellStyle1.ALIGN_CENTER);
			cellStyle1.setVerticalAlignment(cellStyle1.VERTICAL_CENTER);

			CellStyle cellStyleLeft1 = workbook1.createCellStyle();
			cellStyleLeft1.setBorderBottom((short)1);
			cellStyleLeft1.setBorderLeft((short)1);
			cellStyleLeft1.setBorderRight((short)1);
			cellStyleLeft1.setBorderTop((short)1);
			cellStyleLeft1.setWrapText(true);
			cellStyleLeft1.setAlignment(cellStyle1.ALIGN_JUSTIFY);
			cellStyleLeft1.setVerticalAlignment(cellStyle1.VERTICAL_CENTER);
			
			
			Workbook workbook2 = new XSSFWorkbook(fis2);

			CellStyle cellStyle2 = workbook2.createCellStyle();
			cellStyle2.setBorderBottom((short)1);
			cellStyle2.setBorderLeft((short)1);
			cellStyle2.setBorderRight((short)1);
			cellStyle2.setBorderTop((short)1);
			cellStyle2.setWrapText(true);
			cellStyle2.setAlignment(cellStyle1.ALIGN_CENTER);
			cellStyle2.setVerticalAlignment(cellStyle1.VERTICAL_CENTER);
			
			if(fname.equalsIgnoreCase("export") && furl.equalsIgnoreCase("org")){
				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");	
				List<SubAuditOrganization> lr=null;
				if(loguser==null){
					lr=(List<SubAuditOrganization>) dao.getHQLResult("from SubAuditOrganization t  where t.isactive=1 order by t.id", "list");
				}
				else{
					lr=(List<SubAuditOrganization>) dao.getHQLResult("from SubAuditOrganization t where t.departmentid="+loguser.getDepartmentid()+" and t.isactive=1 order by t.id", "list");
				}
				List<LutExpProgcategory> ex= (List<LutExpProgcategory>) dao.getHQLResult("from LutExpProgcategory t order by t.id", "list");
				Sheet sheet = workbook2.getSheetAt(0);
				for(int i=1;i<lr.size();i++){					
					Row row = sheet.createRow(i);
					Cell cell = row.createCell(0);
					cell.setCellValue(lr.get(i).getOrgname());
					Cell cell1 = row.createCell(1);
					cell1.setCellValue(lr.get(i).getOrgcode());
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(lr.get(i).getLutDepartment().getDepartmentname());
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(lr.get(i).getLutCategory().getCategoryname());
					Cell cell4 = row.createCell(4);
					cell4.setCellValue(lr.get(i).getLutFincategory().getFincategoryname());
					Cell cell5 = row.createCell(5);
					for(LutExpProgcategory lu:ex){
						if(lu.getId()==lr.get(i).getProgid()){
							if(ex!=null){
								cell5.setCellValue(lu.getProgname());
							}
						}
					}
					
					Cell cell6 = row.createCell(6);
					cell6.setCellValue(lr.get(i).getPhone());
					Cell cell7 = row.createCell(7);
					
					
					cell7.setCellValue(lr.get(i).getRegid());
					Cell cell8 = row.createCell(8);
					cell8.setCellValue(lr.get(i).getStateregid());
					Cell cell9 = row.createCell(9);
					cell9.setCellValue(lr.get(i).getFsorg());
					Cell cell10 = row.createCell(10);
					cell10.setCellValue(lr.get(i).getTaxorg());
					Cell cell11 = row.createCell(11);
					cell11.setCellValue(lr.get(i).getNdorg());
					Cell cell12 = row.createCell(12);
					cell12.setCellValue(lr.get(i).getHeadorder());
					Cell cell13 = row.createCell(13);
					cell13.setCellValue(lr.get(i).getBanks());
				
					Cell cell14 = row.createCell(14);
					cell14.setCellValue(lr.get(i).getStatebanks());
					Cell cell15 = row.createCell(15);
					cell15.setCellValue(lr.get(i).getWeb());
					Cell cell16 = row.createCell(16);
					cell16.setCellValue(lr.get(i).getEmail());
					Cell cell17 = row.createCell(17);
					cell17.setCellValue(lr.get(i).getAddress());
					Cell cell18 = row.createCell(18);
					cell18.setCellValue(lr.get(i).getFax());
					Cell cell19 = row.createCell(19);
					cell19.setCellValue(lr.get(i).getHeadsurname());
					Cell cell20 = row.createCell(20);
					cell20.setCellValue(lr.get(i).getHeadfullname());
					Cell cell21 = row.createCell(21);
					cell21.setCellValue(lr.get(i).getHeadpos());
					Cell cell22 = row.createCell(22);
					cell22.setCellValue(lr.get(i).getHeadphone());
					Cell cell23 = row.createCell(23);
					cell23.setCellValue(lr.get(i).getHeademail());
					
					Cell cell24 = row.createCell(24);
					cell24.setCellValue(lr.get(i).getHeadwyear());
					Cell cell25 = row.createCell(25);
					cell25.setCellValue(lr.get(i).getHeadprof());
					Cell cell26 = row.createCell(26);
					cell26.setCellValue(lr.get(i).getAccsurname());
					Cell cell27 = row.createCell(27);
					cell27.setCellValue(lr.get(i).getAccfullname());
					Cell cell28 = row.createCell(28);
					cell28.setCellValue(lr.get(i).getAccpos());
					Cell cell29 = row.createCell(29);
					cell29.setCellValue(lr.get(i).getAccphone());
					Cell cell30 = row.createCell(30);
					cell30.setCellValue(lr.get(i).getAccemail());
					Cell cell31 = row.createCell(31);
					cell31.setCellValue(lr.get(i).getAccwyear());
					Cell cell32 = row.createCell(32);
					cell32.setCellValue(lr.get(i).getAccprof());
					
					
					Cell cell33 = row.createCell(33);
					cell33.setCellValue(lr.get(i).getBudget1());
					
					Cell cell34 = row.createCell(34);
					cell34.setCellValue(lr.get(i).getBudget2());
					Cell cell35 = row.createCell(35);
					cell35.setCellValue(lr.get(i).getBudget3());
					Cell cell36 = row.createCell(36);
					cell36.setCellValue(lr.get(i).getComplation1());
					Cell cell37 = row.createCell(37);
					cell37.setCellValue(lr.get(i).getComplation2());
					Cell cell38 = row.createCell(38);
					cell38.setCellValue(lr.get(i).getComplation3());
					Cell cell39 = row.createCell(39);
					cell39.setCellValue(lr.get(i).getAr1());
					Cell cell40 = row.createCell(40);
					cell40.setCellValue(lr.get(i).getAr2());
					Cell cell41 = row.createCell(41);
					cell41.setCellValue(lr.get(i).getAr3());
					Cell cell42 = row.createCell(42);
					cell42.setCellValue(lr.get(i).getHeadwnum());
					
					Cell cell43 = row.createCell(43);
					cell43.setCellValue(lr.get(i).getComwnum());
					Cell cell44 = row.createCell(44);
					cell44.setCellValue(lr.get(i).getSerwnum());
					Cell cell45 = row.createCell(45);
					cell45.setCellValue(lr.get(i).getConwnum());
					
					Cell cell46 = row.createCell(46);
					cell46.setCellValue(lr.get(i).getOtherwnum());
					Cell cell47 = row.createCell(47);
					cell47.setCellValue(lr.get(i).getCreatedate());
					
				}
				
				try (ServletOutputStream outputStream = response.getOutputStream()) {
					response.setContentType("application/ms-excel; charset=UTF-8");
					response.setCharacterEncoding("UTF-8");
					response.setHeader("Content-Disposition","attachment; filename*=UTF-8''excelExport.xlsx");
					workbook2.write(outputStream);
					outputStream.close();
				}
				catch (Exception e) {
					System.out.println("ishe orov");

				}
				
			}
			
			if(fname.equalsIgnoreCase("gb41")){
				System.out.println("sheet oldson gb41");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(14+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData2());
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData12());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						if (lr.get(i).getData23()==0){
							cell.setCellValue((String) "Үгүй");
						}
						else{
							cell.setCellValue((String) "Тийм");
						}
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb42")){
				System.out.println("sheet oldson gb42");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=0 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(14+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData2());
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData12());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData24());
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData22());
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb43")){
				System.out.println("sheet oldson gb43");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=1 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(14+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData2());
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData12());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						if (lr.get(i).getData23()==0){
							cell.setCellValue((String) "Үгүй");
						}
						else{
							cell.setCellValue((String) "Тийм");
						}
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb44")){
				System.out.println("sheet oldson gb44");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data25=1 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(5);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(5);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(13+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(10);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(11);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(12);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(13);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(14);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(15);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(16);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(17);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(18);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(19);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(20);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(21);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb45")){
				System.out.println("sheet oldson gb45");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data25=1 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(5);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(5);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(13+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(10);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(11);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(12);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");
						cell = row.createCell(13);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");	
						cell = row.createCell(14);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");	
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb46")){
				System.out.println("sheet oldson gb46");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=1 and t.data25=0  order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(6);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(6);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(13+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data1());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data2());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data3());
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) lr.get(i).getB46data4());
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data5());
						
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb461")){
				System.out.println("sheet oldson gb461");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=1 and t.b46data4=1 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(5);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(5);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(14+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data1());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data2());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data3());
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB461data1());
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB461data2());
						cell = row.createCell(10);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB461data3());
						cell = row.createCell(11);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB461data4());
						
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb462")){
				System.out.println("sheet oldson gb462");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=1 and t.b46data4=2 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(14+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data1());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data2());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data3());
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB462data1());
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB462data2());
						cell = row.createCell(10);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB462data3());
						cell = row.createCell(11);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB462data4());
						cell = row.createCell(12);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB462data5());
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb463")){
				System.out.println("sheet oldson gb463");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=1 and t.b46data4=3 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(14+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data1());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data2());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data3());
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB463data1());
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB463data2());
						cell = row.createCell(10);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB463data3());
						cell = row.createCell(11);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB463data4());
						cell = row.createCell(12);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB463data5());
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb464")){
				System.out.println("sheet oldson gb464");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=1 and t.b46data4=4 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(13+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB464data1());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB464data2());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB464data3());
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB464data4());
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB464data5());
						cell = row.createCell(10);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB464data6());
						cell = row.createCell(11);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB464data7());						
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}
			
			if(fname.equalsIgnoreCase("gb465")){
				System.out.println("sheet oldson gb465");
				if(workbook1.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+id+" and t.data23=1 and t.b46data4=5 order by t.id", "list");
					
					Sheet sheet = workbook1.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());
					
					for(int i=0;i<lr.size();i++){						
						row=sheet.createRow(14+i);						
						cell = row.createCell(0);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((long) i+1);											
						cell = row.createCell(1);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData1());
						cell = row.createCell(2);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) "");						
						cell = row.createCell(3);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData20());
						cell = row.createCell(4);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getData21());
						cell = row.createCell(5);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data1());
						cell = row.createCell(6);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data2());
						cell = row.createCell(7);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB46data3());
						cell = row.createCell(8);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB465data1());
						cell = row.createCell(9);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB465data2());
						cell = row.createCell(10);
						cell.setCellStyle(cellStyleLeft1);
						cell.setCellValue((String) lr.get(i).getB465data3());
					
		        	} 															

					for(int i=workbook1.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook1.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook1.removeSheetAt(i);
							}
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 
					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-excel; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook1.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}


			}

			if(fname.equalsIgnoreCase("gb1")){
				System.out.println("sheet oldson gb1");
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(2);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(2);
					cell.setCellValue((String) main.getGencode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("report5xlsx")){
				LutAuditWork work = (LutAuditWork) dao.getHQLResult("from LutAuditWork t where t.fileurl='gb5155'", "current");
				List<LnkMainForm> mainform = null;
				if (work != null){
					mainform = (List<LnkMainForm>) dao.getHQLResult("from LnkMainForm t where t.mid="+id+" and t.wid = " +work.getId()+" order by t.id desc", "list");
				}
				if (mainform != null && !mainform.isEmpty()){
					File exfile = new File(appPath+File.separator+mainform.get(0).getFileurl());
					//files = new File(appPath+mainform.get(0).getFileurl());
					if (exfile.exists()){
						System.out.println("file found ==> " + exfile.getAbsolutePath());
						fis = new FileInputStream(exfile);

						workbook = new XSSFWorkbook(fis);

						MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
						SubAuditOrganization org=null;
						
						if(main.getOrgid()==0){
							 org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
						}
						else{
							 org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
						}
						

						for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
							Sheet tmpSheet =workbook.getSheetAt(i);
							if(!tmpSheet.getSheetName().equals(furl)){
								if(!tmpSheet.getSheetName().equalsIgnoreCase("20.TGT1") && !tmpSheet.getSheetName().equalsIgnoreCase("2.CT1A") && !tmpSheet.getSheetName().equalsIgnoreCase("3.CT2A") && !tmpSheet.getSheetName().equalsIgnoreCase("4.CT3A") && !tmpSheet.getSheetName().equalsIgnoreCase("5.CT4A")){
									workbook.removeSheetAt(i);
								}
							}
						} 

						
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

						}
					}
					else{
						System.out.println("file not found ==> " + exfile.getAbsolutePath());
					}
				}
				else{
					System.out.println("main form = null");
				}
			}
			else if(fname.equalsIgnoreCase("b1")){
				System.out.println("sheet oldson b1");
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(2);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(2);
					cell.setCellValue((String) main.getGencode());

					//   row = sheet.getRow(6);
					//   cell = row.getCell(4);
					//   cell.setCellValue((String) org.getOrgcode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("b11")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b11");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					/*     Cell cell = row.getCell(2);
    		        cell.setCellValue((String) org.getOrgname());

    		        row = sheet.getRow(7);
       		        cell = row.getCell(2);
       		        cell.setCellValue((String) main.getGencode());*/

					//   row = sheet.getRow(6);
					//   cell = row.getCell(4);
					//   cell.setCellValue((String) org.getOrgcode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("b12")){
				System.out.println("sheet oldson b12");
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					/*     Cell cell = row.getCell(2);
    		        cell.setCellValue((String) org.getOrgname());

    		        row = sheet.getRow(7);
       		        cell = row.getCell(2);
       		        cell.setCellValue((String) main.getGencode());*/

					//   row = sheet.getRow(6);
					//   cell = row.getCell(4);
					//   cell.setCellValue((String) org.getOrgcode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("b13")){

				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b13");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					/*     Cell cell = row.getCell(2);
    		        cell.setCellValue((String) org.getOrgname());

    		        row = sheet.getRow(7);
       		        cell = row.getCell(2);
       		        cell.setCellValue((String) main.getGencode());*/

					//   row = sheet.getRow(6);
					//   cell = row.getCell(4);
					//   cell.setCellValue((String) org.getOrgcode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("b14")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b14");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					/*     Cell cell = row.getCell(2);
    		        cell.setCellValue((String) org.getOrgname());

    		        row = sheet.getRow(7);
       		        cell = row.getCell(2);
       		        cell.setCellValue((String) main.getGencode());*/

					//   row = sheet.getRow(6);
					//   cell = row.getCell(4);
					//   cell.setCellValue((String) org.getOrgcode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("b15")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b15");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					/*     Cell cell = row.getCell(2);
    		        cell.setCellValue((String) org.getOrgname());

    		        row = sheet.getRow(7);
       		        cell = row.getCell(2);
       		        cell.setCellValue((String) main.getGencode());*/

					//   row = sheet.getRow(6);
					//   cell = row.getCell(4);
					//   cell.setCellValue((String) org.getOrgcode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("b23")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b23");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(4);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell cell = row.getCell(2);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(6);
					cell = row.getCell(2);
					cell.setCellValue((String) main.getGencode());

					//   row = sheet.getRow(6);
					//   cell = row.getCell(4);
					//   cell.setCellValue((String) org.getOrgcode());

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("b24")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b24");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell  cell = row.getCell(2);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(2);
					cell.setCellValue((String) main.getGencode());

					int w=11;
					int m=0;
					int rcount=1;
					for(int i=0;i<main.getLnkMainFormT2s().size();i++){
						System.out.println(org.getOrgname()+sheet.getSheetName()+"b24");
						if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size()>0){
							for(int y=0;y<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size();y++){
								row=sheet.createRow(w+m+1);
								//row=sheet.createRow(w+i+y);
								cell=row.createCell(0);		
								cell.setCellValue(rcount);
								cell.setCellStyle(cellStyle);
								cell=row.createCell(1);		       	        	

								cell.setCellStyle(cellStyleLeft);
								cell.setCellValue(main.getLnkMainFormT2s().get(i).getLutAuditDir().getName());

								cell=row.createCell(2);
								cell.setCellStyle(cellStyleLeft);
								cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLutRisk().getRiskname());
								cell=row.createCell(3);
								cell.setCellStyle(cellStyleLeft);
								cell.setCellValue("");

								w=w+1;
								rcount=rcount+1;
							}
						}
					}

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);		               
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
							}    		                	
						}
					} 



					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 


					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}

			}
			else if(fname.equalsIgnoreCase("b42")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b42");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell  cell = row.getCell(3);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(3);
					cell.setCellValue((String) main.getGencode());
					
					List<Object[]> lo=(List<Object[]>) dao.getHQLResult("select d.name, l.riskname, r.data4, d.id  from LnkMainFormT2 t, LnkRiskT2 r, LutAuditDir d, LutRisk l  where t.mid="+main.getId()+" and l.id=r.riskid and d.id=t.dirid and t.id=r.t2id and t.stepid=3 and r.data4>20", "list");

					int w=11;
					int m=0;
					int rcount=1;
					for(int i=0;i<lo.size();i++){
						row=sheet.createRow(i+12);
						cell=row.createCell(0);				       	        	
						cell.setCellValue(i+1);
						cell.setCellStyle(cellStyle);
						cell=row.createCell(1);			       	        	
						cell.setCellValue(lo.get(i)[0].toString());
						cell.setCellStyle(cellStyle);
						cell=row.createCell(2);
						cell.setCellValue(lo.get(i)[1].toString());
						cell.setCellStyle(cellStyleLeft);
						cell=row.createCell(3);
						cell.setCellValue(lo.get(i)[2].toString());
						cell.setCellStyle(cellStyle);
						
						LutAuditDir ldir = (LutAuditDir) dao.getHQLResult("from LutAuditDir t where t.id='"+lo.get(i)[3].toString()+"'", "current");
						
						String str="";
						for(int t=0; t<ldir.getLnkDirectionNotices().size();t++){
							str=str+","+ldir.getLnkDirectionNotices().get(t).getLutNotice().getName();
						}
						cell=row.createCell(4);
						if(str.length()>0){					       	        	 
							cell.setCellValue(str.substring(1,str.length()));
							cell.setCellStyle(cellStyleLeft);
						}
						else{
							cell.setCellValue(str);
							cell.setCellStyle(cellStyleLeft);
						}
						cell=row.createCell(5);
						cell.setCellValue("");
						cell.setCellStyle(cellStyleLeft);	
					}
			/*		for(int i=0;i<main.getLnkMainFormT2s().size();i++){
						System.out.println(org.getOrgname()+sheet.getSheetName()+"b24");
						if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size()>0){
							for(int y=0;y<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size();y++){
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData4()>20){
									row=sheet.createRow(w+m+1);
									cell=row.createCell(0);				       	        	
									cell.setCellValue(rcount);
									cell.setCellStyle(cellStyle);
									cell=row.createCell(1);			       	        	
									cell.setCellValue(main.getLnkMainFormT2s().get(i).getLutAuditDir().getName());
									cell.setCellStyle(cellStyle);
									cell=row.createCell(2);
									cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLutRisk().getRiskname());
									cell.setCellStyle(cellStyleLeft);
									cell=row.createCell(3);
									cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData4());
									cell.setCellStyle(cellStyle);
									String str="";
									for(int t=0; t<main.getLnkMainFormT2s().get(i).getLutAuditDir().getLnkDirectionNotices().size();t++){
										str=str+","+main.getLnkMainFormT2s().get(i).getLutAuditDir().getLnkDirectionNotices().get(t).getLutNotice().getName();
									}
									cell=row.createCell(4);
									if(str.length()>0){					       	        	 
										cell.setCellValue(str.substring(1,str.length()));
										cell.setCellStyle(cellStyleLeft);
									}
									else{
										cell.setCellValue(str);
										cell.setCellStyle(cellStyleLeft);
									}
									cell=row.createCell(5);
									cell.setCellValue("");
									cell.setCellStyle(cellStyleLeft);					
									w=w+1;	
									rcount=rcount+1;
								}			       	        			       	       
							}
						}
					}*/

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);		               
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
							}    		                	
						}
					} 

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 


					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}

			}
			else if(fname.equalsIgnoreCase("b43")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b43");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(4);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell  cell = row.getCell(5);
					cell.setCellValue((String) org.getOrgname());
					row = sheet.getRow(6);
					cell = row.getCell(5);
					cell.setCellValue((String) main.getGencode());

					int data1=0;
					int data2=0;
					int data3=0;
					int data4=0;
					int data5=0;
					int data6=0;
					int data7=0;
					int data8=0;
					int data9=0;
					int data10=0;
					int data11=0;
					int data12=0;
					int data13=0;
					
					
					List<Object[]> lo=(List<Object[]>) dao.getHQLResult("select r.data1, r.data2, r.data3, r.data4  from LnkMainFormT2 t, LnkRiskT2 r, LutAuditDir d, LutRisk l  where t.mid="+main.getId()+" and l.id=r.riskid and d.id=t.dirid and t.id=r.t2id and t.stepid=3 and r.data4>20", "list");

					int w=11;
					int m=0;
					int rcount=1;
					for(int i=0;i<lo.size();i++){
						if(lo.get(i)[0].toString().equalsIgnoreCase("1")){
							data1=data1+1;
						}
						if(lo.get(i)[0].toString().equalsIgnoreCase("2")){
							data2=data2+1;
						}
						if(lo.get(i)[0].toString().equalsIgnoreCase("3")){
							data3=data3+1;
						}
						if(lo.get(i)[0].toString().equalsIgnoreCase("4")){
							data4=data4+1;
						}
						if(lo.get(i)[0].toString().equalsIgnoreCase("5")){
							data5=data5+1;
						}
						if(lo.get(i)[1].toString().equalsIgnoreCase("1")){
							data6=data6+1;
						}
						if(lo.get(i)[1].toString().equalsIgnoreCase("2")){
							data7=data7+1;
						}
						if(lo.get(i)[1].toString().equalsIgnoreCase("3")){
							data8=data8+1;
						}
						if(lo.get(i)[1].toString().equalsIgnoreCase("4")){
							data9=data9+1;
						}
						if(lo.get(i)[1].toString().equalsIgnoreCase("5")){
							data10=data10+1;
						}
						if(lo.get(i)[2].toString().equalsIgnoreCase("1")){
							data11=data11+1;
						}
						if(lo.get(i)[2].toString().equalsIgnoreCase("2")){
							data12=data12+1;
						}
						if(lo.get(i)[2].toString().equalsIgnoreCase("3")){
							data13=data13+1;
						}
					}

		/*			for(int i=0;i<main.getLnkMainFormT2s().size();i++){
						if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size()>0){
							for(int y=0;y<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size();y++){
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1()==1){
									data1=data1+1;
								}
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1()==2){
									data2=data2+1;
								}	
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1()==3){
									data3=data1+1;
								}	
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1()==4){
									data4=data4+1;
								}	
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1()==5){
									data5=data5+1;
								}	

								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2()==1){
									data6=data6+1;
								}
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2()==2){
									data7=data7+1;
								}	
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2()==3){
									data8=data8+1;
								}	
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2()==4){
									data9=data9+1;
								}	
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2()==5){
									data10=data10+1;
								}	

								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData3()==1){
									data11=data11+1;
								}
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData3()==2){
									data12=data12+1;
								}	
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData3()==3){
									data13=data13+1;
								}
							}

						}
					}*/
					
					
					row = sheet.getRow(15);
					cell=row.getCell(0);				       	        	
					cell.setCellValue(1);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(2);			       	        	
					cell.setCellValue(data1);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(3);			       	        	
					cell.setCellValue(data2);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(4);			       	        	
					cell.setCellValue(data3);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(5);			       	        	
					cell.setCellValue(data4);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(6);			       	        	
					cell.setCellValue(data5);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(7);			       	        	
					cell.setCellValue(data6);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(8);			       	        	
					cell.setCellValue(data7);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(9);			       	        	
					cell.setCellValue(data8);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(10);			       	        	
					cell.setCellValue(data9);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(11);			       	        	
					cell.setCellValue(data10);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(12);			       	        	
					cell.setCellValue(data11);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(13);			       	        	
					cell.setCellValue(data12);
					cell.setCellStyle(cellStyle);
					cell=row.getCell(14);			       	        	
					cell.setCellValue(data13);
					cell.setCellStyle(cellStyle);
					int rt=0;
					rt=data1+data2+data3+data4+data5+data6+data7+data8+data9+data10+data11+data12+data13;
					cell=row.getCell(15);			       	        	
					cell.setCellValue(rt);
					cell.setCellStyle(cellStyle);

					cellStyle = workbook.createCellStyle();
					cellStyle.setBorderBottom((short)1);
					cellStyle.setBorderLeft((short)1);
					cellStyle.setBorderRight((short)1);
					cellStyle.setFillPattern(CellStyle.BORDER_DASH_DOT);

					if(rt>1 && rt<9){
						cell=row.getCell(16);		
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");	     
					}
					else if(rt>8 && rt<17){
						cell=row.getCell(17);	
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");	     
					}
					else if(rt>16 && rt<26){
						cell=row.getCell(18);		
						cell.setCellStyle(cellStyle);
						cell.setCellValue("");	     
					}

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);		               
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
							}    		                	
						}
					} 



					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 


					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");

					}

				}

			}
			else if(fname.equalsIgnoreCase("b51")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b51");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell  cell = row.getCell(3);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(3);
					cell.setCellValue((String) main.getGencode());

					List<Object[]> lo=(List<Object[]>) dao.getHQLResult("select d.name, l.riskname, r.data4, d.id, r.id  from LnkMainFormT2 t, LnkRiskT2 r, LutAuditDir d, LutRisk l  where t.mid="+main.getId()+" and l.id=r.riskid and d.id=t.dirid and t.id=r.t2id and t.stepid=3 and r.data4>20", "list");
					
					int w=19;
					int rcount=1;
					
					for(int i=0;i<lo.size();i++){		
						int c=w+rcount-1;
						row=sheet.createRow(c);
						cell=row.createCell(0);	
						cell.setCellStyle(cellStyle);
						cell.setCellValue(rcount);
						cell=row.createCell(1);	
						cell.setCellStyle(cellStyle);
						cell.setCellValue(lo.get(i)[0].toString());

						cell=row.createCell(2);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(lo.get(i)[1].toString());

						cell=row.createCell(3);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(lo.get(i)[2].toString());

						String str="";

						/*    for(int t=0; t<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().size();t++){
	       	        	 LnkRiskTryout tryo = main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t);
	       	        	 for(int tt=0; tt<tryo.getLutTryout().getLnkTryoutNotices().size();tt++){
		       	        	 str=str+ ","+"\n"+tryo.getLutTryout().getLnkTryoutNotices().get(tt).getLutNotice().getName();
		       	         }
	       	         }*/
						List<LnkRiskTryout> lt = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+main.getId()+" and t.rt2id='"+lo.get(i)[4].toString()+"'", "list");
						for(int r=0;r<lt.size();r++){
							LnkRiskTryout lrt=lt.get(r);
						
							cell=row.createCell(4);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData4());

							cell=row.createCell(5);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData5());

							cell=row.createCell(6);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData6());

							cell=row.createCell(7);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData7());

							cell=row.createCell(8);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData8());

							cell=row.createCell(9);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData9());

							cell=row.createCell(10);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData10());

							cell=row.createCell(11);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData11());

							cell=row.createCell(12);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(lrt.getData12());
							String law="",lzuil="",lzaalt="",standart="",szuil="",szaalt="",other="";
							//LutRisk risk = lrt.getLutRisk();
							//List<LnkRiskLaw> laws=risk.getLnkRiskLaws();

						}

						

						cell=row.createCell(13);
						cell.setCellStyle(cellStyle);
						cell=row.createCell(14);
						cell.setCellStyle(cellStyle);
						cell=row.createCell(15);
						cell.setCellStyle(cellStyle);
						rcount=rcount+1;
					}
				/*	for(int i=0;i<main.getLnkMainFormT2s().size();i++){
						if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size()>0){
							for(int y=0;y<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size();y++){
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData4()>20){
									if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().size()>0){
										for(int r=0;r<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().size();r++){
											LnkRiskTryout lrt=main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(r);
											int c=w+rcount-1;
											row=sheet.createRow(c);
											cell=row.createCell(0);	
											cell.setCellStyle(cellStyle);
											cell.setCellValue(rcount);
											cell=row.createCell(1);	
											cell.setCellStyle(cellStyle);
											cell.setCellValue(main.getLnkMainFormT2s().get(i).getLutAuditDir().getName());

											cell=row.createCell(2);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLutRisk().getRiskname());

											cell=row.createCell(3);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData4());

											String str="";

											    for(int t=0; t<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().size();t++){
						       	        	 LnkRiskTryout tryo = main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t);
						       	        	 for(int tt=0; tt<tryo.getLutTryout().getLnkTryoutNotices().size();tt++){
							       	        	 str=str+ ","+"\n"+tryo.getLutTryout().getLnkTryoutNotices().get(tt).getLutNotice().getName();
							       	         }
						       	         }
											cell=row.createCell(4);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData4());

											cell=row.createCell(5);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData5());

											cell=row.createCell(6);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData6());

											cell=row.createCell(7);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData7());

											cell=row.createCell(8);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData8());

											cell=row.createCell(9);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData9());

											cell=row.createCell(10);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData10());

											cell=row.createCell(11);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData11());

											cell=row.createCell(12);
											cell.setCellStyle(cellStyle);
											cell.setCellValue(lrt.getData12());

											String law="",lzuil="",lzaalt="",standart="",szuil="",szaalt="",other="";
											LutRisk risk = main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLutRisk();
											List<LnkRiskLaw> laws=risk.getLnkRiskLaws();

												 for(int p=0;p<laws.size();p++){					 	
											LutLaw plaw1=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+laws.get(p).getLawid()+"'", "current");

												if(plaw1.getParentid()!=null){
													LutLaw pa1=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+plaw1.getParentid()+"'", "current");
													LutLaw pa2=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+pa1.getParentid()+"'", "current");
													if(pa2.getParentid()==null){
														if(plaw1.getLawcategory()==1){
															law=law + ","+"\n"+pa2.getLawname();
															lzuil=lzuil+ ","+"\n"+pa1.getLawname();
															//lzaalt=lzaalt+ ","+"\n"+plaw1.getZaalt()+" "+plaw1.getLawname();
														}
														if(plaw1.getLawcategory()==2){
															standart=standart+ ","+"\n"+pa2.getLawname();
															szuil=szuil+ ","+"\n"+pa1.getLawname();
															//szaalt=szaalt+ ","+"\n"+plaw1.getZaalt()+" "+plaw1.getLawname();
														}

													}
													else{													
														LutLaw pa3=(LutLaw) dao.getHQLResult("from LutLaw t where t.id='"+pa2.getParentid()+"'", "current");
														if(plaw1.getLawcategory()==1){
															law=law+ ","+"\n"+pa3.getLawname();
															lzuil=lzuil+ ","+"\n"+pa2.getLawname();
															//lzaalt=lzaalt+ ","+"\n"+pa1.getZaalt()+" "+pa1.getLawname()+ ","+"\n"+plaw1.getZaalt()+" "+plaw1.getLawname();
														}
														if(plaw1.getLawcategory()==2){
															standart=standart+ ","+"\n"+pa2.getLawname();
															szuil=szuil+ ","+"\n"+pa1.getLawname();
															//szaalt=szaalt+ ","+"\n"+pa1.getZaalt()+" "+pa1.getLawname()+ ","+"\n"+plaw1.getZaalt()+" "+plaw1.getLawname();
														}
													}
												}
												else{
													if(plaw1.getLawcategory()==3){
														other=other+ ","+"\n"+plaw1.getLawname();
													}
												}
										 }
												 cell=row.createCell(5);
						       	         cell.setCellStyle(cellStyle);
						       	         if(standart.length()>0){					       	        	 
						       	        	 cell.setCellValue(standart.substring(1,standart.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(standart);
						       	         }
						       	         cell=row.createCell(6);
						       	         cell.setCellStyle(cellStyle);

						       	         if(szuil.length()>0){					       	        	 
						       	        	 cell.setCellValue(szuil.substring(1,szuil.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(szuil);
						       	         }
						       	         cell=row.createCell(7);
						       	         cell.setCellStyle(cellStyle);

						       	         if(szaalt.length()>0){					       	        	 
						       	        	 cell.setCellValue(szaalt.substring(1,szaalt.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(szaalt);
						       	         }

										 cell=row.createCell(8);
						       	         cell.setCellStyle(cellStyle);

						       	         if(law.length()>0){					       	        	 
						       	        	 cell.setCellValue(law.substring(1,law.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(law);
						       	         }
						       	         cell=row.createCell(9);
						       	         cell.setCellStyle(cellStyle);

						       	         if(lzuil.length()>0){					       	        	 
						       	        	 cell.setCellValue(lzuil.substring(1,lzuil.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(lzuil);
						       	         }

						       	         cell=row.createCell(10);
						       	         cell.setCellStyle(cellStyle);
						       	         if(lzaalt.length()>0){					       	        	 
						       	        	 cell.setCellValue(lzaalt.substring(1,lzaalt.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(lzaalt);
						       	         }

						       	         cell=row.createCell(11);
						       	         cell.setCellStyle(cellStyle);
						       	         if(other.length()>0){					       	        	 
						       	        	 cell.setCellValue(other.substring(1,other.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(other);
						       	         }
											   cell=row.createCell(12);
						       	         cell.setCellStyle(cellStyle);
						       	         str="";
						       	         for(int t=0; t<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().size();t++){
						       	        	 str=str+ ","+"\n"+main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getFormdesc();
						       	         }
						       	         if(str.length()>0){					       	        	 
						       	        	 cell.setCellValue(str.substring(1,str.length()));
						       	         }
						       	         else{
						       	        	 cell.setCellValue(str);
						       	         }
											cell=row.createCell(13);
											cell.setCellStyle(cellStyle);
											cell=row.createCell(14);
											cell.setCellStyle(cellStyle);
											cell=row.createCell(15);
											cell.setCellStyle(cellStyle);
											rcount=rcount+1;
										}
									}	       		    			
								}	
							}
						}
					}*/

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);		               
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
							}    		                	
						}
					}        		        

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 


					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");        				
					}

				}

			}
			else if(fname.equalsIgnoreCase("b52")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b52");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					System.out.println(org.getOrgname()+sheet.getSheetName());
					Cell  cell = row.getCell(3);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(3);
					cell.setCellValue((String) main.getGencode());

					int w=19;
					int m=0;
					int rcount=1;
					
					List<Object[]> lo=(List<Object[]>) dao.getHQLResult("select d.name, l.riskname, r.data4, d.id, r.id  from LnkMainFormT2 t, LnkRiskT2 r, LutAuditDir d, LutRisk l  where t.mid="+main.getId()+" and l.id=r.riskid and d.id=t.dirid and t.id=r.t2id and t.stepid=3 and r.data4>20", "list");
					
					for(int i=0;i<lo.size();i++){
						int c=w+m+1;
						row=sheet.createRow(c);
						cell=row.createCell(0);				       	        	
						cell.setCellValue(rcount);
						cell.setCellStyle(cellStyle);
						cell=row.createCell(1);			       	        	
						cell.setCellValue(lo.get(i)[0].toString());
						cell.setCellStyle(cellStyle);
						cell=row.createCell(2);
						cell.setCellValue(lo.get(i)[1].toString());
						cell.setCellStyle(cellStyle);

						String cell3="";
						String cell4="";
						String cell5="";
						String cell6="";
						String cell7="";
						
						List<LnkRiskTryout> lt = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+main.getId()+" and t.rt2id='"+lo.get(i)[4].toString()+"'", "list");
						
						for(int t=0; t<lt.size();t++){					       	            
							cell3=cell3+", "+lt.get(t).getLutTryout().getFormdesc();
							for(int r=0;r<lt.get(t).getLutTryout().getLnkTryoutConfTypes().size();r++){
								cell4=cell4+", "+lt.get(t).getLutTryout().getLnkTryoutConfTypes().get(r).getLutConfirmationType().getName();
							}
							for(int r=0;r<lt.get(t).getLutTryout().getLnkTryoutConfMethods().size();r++){
								cell5=cell5+", "+lt.get(t).getLutTryout().getLnkTryoutConfMethods().get(r).getLutConfirmationMethod().getName();
							}
							for(int r=0;r<lt.get(t).getLutTryout().getLnkTryoutConfSources().size();r++){
								if(lt.get(t).getLutTryout().getLnkTryoutConfSources().get(r).getLutConfirmationSource().getType().equalsIgnoreCase("0")){
									cell6=cell6+", "+lt.get(t).getLutTryout().getLnkTryoutConfSources().get(r).getLutConfirmationSource().getName();
								}
								else{
									cell7=cell7+", "+lt.get(t).getLutTryout().getLnkTryoutConfSources().get(r).getLutConfirmationSource().getName();
								}
							}
						}
						cell=row.createCell(3);
						if(cell3.length()>0){		
							cell.setCellValue(cell3.substring(1, cell3.length()));
							cell.setCellStyle(cellStyle);
						}
						else{
							cell.setCellValue(cell3);
							cell.setCellStyle(cellStyle);
						}

						cell=row.createCell(4);
						if(cell4.length()>0){		
							cell.setCellValue(cell4.substring(1, cell4.length()));
							cell.setCellStyle(cellStyle);
						}
						else{
							cell.setCellValue(cell4);
							cell.setCellStyle(cellStyle);
						}

						cell=row.createCell(5);
						if(cell5.length()>0){		
							cell.setCellValue(cell5.substring(1, cell5.length()));
							cell.setCellStyle(cellStyle);
						}
						else{
							cell.setCellValue(cell5);
							cell.setCellStyle(cellStyle);
						}
						cell=row.createCell(6);
						if(cell6.length()>0){		
							cell.setCellValue(cell6.substring(1, cell6.length()));
							cell.setCellStyle(cellStyle);
						}
						else{
							cell.setCellValue(cell6);
							cell.setCellStyle(cellStyle);
						}
						cell=row.createCell(7);
						if(cell7.length()>0){		
							cell.setCellValue(cell7.substring(1, cell7.length()));
							cell.setCellStyle(cellStyle);
						}
						else{
							cell.setCellValue(cell7);
							cell.setCellStyle(cellStyle);
						}

						cell=row.createCell(8);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);


						w=w+1;	
						rcount=rcount+1;
					}
			/*		for(int i=0;i<main.getLnkMainFormT2s().size();i++){
						if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size()>0){
							for(int y=0;y<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size();y++){
								if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData4()>20){
									int c=w+m+1;
									row=sheet.createRow(c);
									cell=row.createCell(0);				       	        	
									cell.setCellValue(rcount);
									cell.setCellStyle(cellStyle);
									cell=row.createCell(1);			       	        	
									cell.setCellValue(main.getLnkMainFormT2s().get(i).getLutAuditDir().getName());
									cell.setCellStyle(cellStyle);
									cell=row.createCell(2);
									cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLutRisk().getRiskname());
									cell.setCellStyle(cellStyle);

									String cell3="";
									String cell4="";
									String cell5="";
									String cell6="";
									String cell7="";
									System.out.println("cc"+main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().size());
									for(int t=0; t<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().size();t++){					       	            
										cell3=cell3+", "+main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getFormdesc();
										for(int r=0;r<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfTypes().size();r++){
											cell4=cell4+", "+main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfTypes().get(r).getLutConfirmationType().getName();
										}
										for(int r=0;r<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfMethods().size();r++){
											cell5=cell5+", "+main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfMethods().get(r).getLutConfirmationMethod().getName();
										}
										for(int r=0;r<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfSources().size();r++){
											if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfSources().get(r).getLutConfirmationSource().getType().equalsIgnoreCase("0")){
												cell6=cell6+", "+main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfSources().get(r).getLutConfirmationSource().getName();
											}
											else{
												cell7=cell7+", "+main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLnkRiskTryouts().get(t).getLutTryout().getLnkTryoutConfSources().get(r).getLutConfirmationSource().getName();
											}
										}
									}
									cell=row.createCell(3);
									if(cell3.length()>0){		
										cell.setCellValue(cell3.substring(1, cell3.length()));
										cell.setCellStyle(cellStyle);
									}
									else{
										cell.setCellValue(cell3);
										cell.setCellStyle(cellStyle);
									}

									cell=row.createCell(4);
									if(cell4.length()>0){		
										cell.setCellValue(cell4.substring(1, cell4.length()));
										cell.setCellStyle(cellStyle);
									}
									else{
										cell.setCellValue(cell4);
										cell.setCellStyle(cellStyle);
									}

									cell=row.createCell(5);
									if(cell5.length()>0){		
										cell.setCellValue(cell5.substring(1, cell5.length()));
										cell.setCellStyle(cellStyle);
									}
									else{
										cell.setCellValue(cell5);
										cell.setCellStyle(cellStyle);
									}
									cell=row.createCell(6);
									if(cell6.length()>0){		
										cell.setCellValue(cell6.substring(1, cell6.length()));
										cell.setCellStyle(cellStyle);
									}
									else{
										cell.setCellValue(cell6);
										cell.setCellStyle(cellStyle);
									}
									cell=row.createCell(7);
									if(cell7.length()>0){		
										cell.setCellValue(cell7.substring(1, cell7.length()));
										cell.setCellStyle(cellStyle);
									}
									else{
										cell.setCellValue(cell7);
										cell.setCellStyle(cellStyle);
									}

									cell=row.createCell(8);
									cell.setCellValue("");
									cell.setCellStyle(cellStyle);


									w=w+1;	
									rcount=rcount+1;
								}       		    			
							}
						}
					}*/

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);		               
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
							}    		                	
						}
					}        		        

					String xname=org.getOrgname().trim()+"-"+furl;
					xname = URLEncoder.encode(xname,"UTF-8"); 


					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".xlsx");
						workbook.write(outputStream);
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");        				
					}

				}

			}
			else if(fname.equalsIgnoreCase("b41")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b41");

					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(3);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(3);
					cell.setCellValue((String) main.getGencode());

					int w=12;
					int m=0;
					for(int i=0;i<main.getLnkMainFormT2s().size();i++){
						System.out.println(org.getOrgname()+sheet.getSheetName()+"b41");

						if(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size()>0 && main.getLnkMainFormT2s().get(i).getDecid()==1){

							for(int y=0;y<main.getLnkMainFormT2s().get(i).getLnkRiskT2s().size();y++){
								row=sheet.createRow(w+y+m);
								cell=row.createCell(0);	
								cell.setCellStyle(cellStyle);
								cell.setCellValue(w-11+y+m);
								cell=row.createCell(1);		
								cell.setCellStyle(cellStyle);
								cell.setCellValue((String)main.getLnkMainFormT2s().get(i).getLutAuditDir().getName());

								cell=row.createCell(2);
								cell.setCellStyle(cellStyle);
								cell.setCellValue((String)main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getLutRisk().getRiskname());

								cell=row.createCell(3);
								cell.setCellStyle(cellStyle);
								cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1());

								cell=row.createCell(4);
								cell.setCellStyle(cellStyle);
								cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2());

								cell=row.createCell(5);
								cell.setCellStyle(cellStyle);
								cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData3());

								cell=row.createCell(7);
								cell.setCellStyle(cellStyle);
								cell.setCellValue((boolean)main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData5());

								cell=row.createCell(8);
								cell.setCellStyle(cellStyle);
								cell.setCellValue((boolean)main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData6());

								cell=row.createCell(9);
								cell.setCellStyle(cellStyle);
								cell.setCellValue((boolean)main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData7());

								cell=row.createCell(6);
								cell.setCellStyle(cellStyle);
								cell.setCellValue(main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData4());

								cell=row.createCell(10);
								cell.setCellStyle(cellStyle);
								cell.setCellValue((String)main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getDescription());

								List<SourceData> sr = (List<SourceData>) dao.getHQLResult("from SourceData t where t.formname='"+furl+"'", "list");
								for(int s=0;s<sr.size();s++){
									if(sr.get(s).getColumn()==3 && sr.get(s).getValue()==main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData1()){
										cell=row.getCell(3);
										cell.setCellValue(sr.get(s).getText());
									}
									if(sr.get(s).getColumn()==4 && sr.get(s).getValue()==main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData2()){
										cell=row.getCell(4);
										cell.setCellValue(sr.get(s).getText());
									}
									if(sr.get(s).getColumn()==5 && sr.get(s).getValue()==main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).getData3()){
										cell=row.getCell(5);
										cell.setCellValue(sr.get(s).getText());
									}
									boolean vIn = main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData5();
									long vOut = vIn?1:0;
									if(sr.get(s).getColumn()==7 && sr.get(s).getValue()==vOut){
										cell=row.getCell(7);
										cell.setCellValue(sr.get(s).getText());
									}
									vIn = main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData6();
									vOut = vIn?1:0;
									if(sr.get(s).getColumn()==8 && sr.get(s).getValue()==vOut){
										cell=row.getCell(8);
										cell.setCellValue(sr.get(s).getText());
									}
									vIn = main.getLnkMainFormT2s().get(i).getLnkRiskT2s().get(y).isData7();
									vOut = vIn?1:0;
									if(sr.get(s).getColumn()==9 && sr.get(s).getValue()==vOut){
										cell=row.getCell(9);
										cell.setCellValue(sr.get(s).getText());
									}
								}
								w=w+y;
							}
							m=m+1;
						}
					}


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);		               
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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

			}else if(fname.equalsIgnoreCase("b22")){
				if((XSSFSheet) workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson b22");
					cellStyle.setWrapText(true);
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					LutReason lr = (LutReason) dao.getHQLResult("from LutReason t where t.id='"+main.getAutype()+"'", "current");
					List<LnkMainFormT2> lm=(List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.mid="+id+" order by t.id", "list");

					//List<LutFactor> lf=  (List<LutFactor>) dao.getHQLResult("from LutFactor t", "list");

					XSSFSheet sheet = (XSSFSheet) workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(2);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(2);
					cell.setCellValue((String) main.getGencode());

					row = sheet.getRow(9);
					cell = row.getCell(2);
					cell.setCellValue((String) lr.getName());
					row = sheet.getRow(12);

					cell = row.getCell(1);

					CellStyle cellStylem = cell.getCellStyle();
					cellStylem.setBorderLeft((short)0);
					cellStylem.setBorderRight((short)0);
					cellStylem.setAlignment(cellStylem.ALIGN_LEFT);
					int w=12;
					int d=1;
					for(int k=0;k<lm.size();k++){
						if(lm.get(k).getLutFactor().getLutGroupOfFactor().getFtype()==0 && lm.get(k).getLutFactor().getTryid()!=0 && lm.get(k).getRtype()==0){
							row=sheet.createRow((short)w);
							row.setHeightInPoints((short)30);

							cell=row.createCell(0);	
							cell.setCellStyle(cellStylem);
							cell.setCellValue(d);
							cell=row.createCell((short)1);	
							cell.setCellStyle(cellStylem);
							cell.setCellValue((String)lm.get(k).getLutFactor().getLutGroupOfFactor().getName());

							for(int col=2; col<7;col++){
								cell=row.createCell(col);	
								cell.setCellStyle(cellStylem);
							}
							CellStyle cl = workbook.createCellStyle();
							cell=row.createCell(7);	
							cl.setBorderLeft((short)1);
							cell.setCellStyle(cl);
							//List<LutFactor> lfactor=  (List<LutFactor>) dao.getHQLResult("from LutFactor t where t.id='"+lm.get(k).getFactorid()+"' order by t.orderid desc", "list");
							w=w+1;
							row=sheet.createRow(w);
							cell=row.createCell(0);	
							cell.setCellValue(lm.get(k).getLutFactor().getFnumber());
							cell.setCellStyle(cellStyle);

							cell=row.createCell(1);	
							cell.setCellValue(lm.get(k).getLutFactor().getFactorname());
							cell.setCellStyle(cellStyle);

							cell=row.createCell(2);	
							cell.setCellValue(lm.get(k).getLutFactor().getLutAuditDir().getName());
							cell.setCellStyle(cellStyle);

							cell=row.createCell(3);	
							cell.setCellStyle(cellStyle);

							cell=row.createCell(4);	
							cell.setCellStyle(cellStyle);

							cell=row.createCell(5);	
							cell.setCellStyle(cellStyle);

							cell=row.createCell(6);	
							cell.setCellStyle(cellStyle);

							if(lm.get(k).getFactorid()==lm.get(k).getLutFactor().getId()){
								cell=row.getCell(3);	

								cell.setCellValue(lm.get(k).getCriname());
								List<SourceData> sr = (List<SourceData>) dao.getHQLResult("from SourceData t where t.formname='"+furl+"' and t.column=4 and t.value="+lm.get(k).getDecid()+"", "list");
								cell=row.getCell(4);	
								if(sr.size()>0){
									cell.setCellValue(sr.get(0).getText());
								}else{
									cell.setCellValue(lm.get(k).getDecid());
								}
								String names="";
								for(int y=0;y<lm.get(k).getLnkRiskT2s().size();y++){
									if(names.equalsIgnoreCase("")){
										names=lm.get(k).getLnkRiskT2s().get(y).getLutRisk().getRiskname();
									}else{
										names=names+", "+ lm.get(k).getLnkRiskT2s().get(y).getLutRisk().getRiskname();
									}
								}
								cell=row.getCell(5);	
								cell.setCellValue(names);

								cell=row.getCell(6);	
								if(lm.get(k).getDescription()!=null){
									cell.setCellValue(lm.get(k).getDescription());
								}else{
									cell.setCellValue("");
								}
							}
							w=w+1;
							d=d+1;
						}							
					}

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						XSSFSheet tmpSheet =(XSSFSheet) workbook.getSheetAt(i);		               
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			else if(fname.equalsIgnoreCase("wordPlan")){
				System.out.println("matar2");
				FileInputStream wis = null;
				File wordFile = new File(appPath+"/assets/zagvarfile/plannig_FA_zagvar.docx");    			 
				//wis = new FileInputStream(wordFile);
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				FsFormA4 selectedMaterial = (FsFormA4) dao.getHQLResult("from FsFormA4 t where t.orgcode='"+main.getOrgcode()+"' and t.cyear = '" + main.getAudityear() + "' and t.isselect = 1 and t.planid = " + main.getId(), "current");
				boolean mergedOutput = false;

				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
				//    							System.getProperty("user.dir") + "/template.docx"));

				List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

				// Instance 1
				Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

				// Instance 2
				//map = new HashMap<DataFieldName, String>();
				int ayear=Integer.parseInt(main.getAudityear())-1;
				map.put(new DataFieldName("Org_name"), au.getOrgname());
				map.put(new DataFieldName("Au_code"), main.getGencode());
				map.put(new DataFieldName("Audit_year"), String.valueOf(ayear));
				map.put(new DataFieldName("Audit_last_year"), main.getAudityear());
				map.put(new DataFieldName("A6_dun"), "----");
				map.put(new DataFieldName("A6_huvi"), "----");
				map.put(new DataFieldName("A7_dun"), "----");
				map.put(new DataFieldName("A7_error_number"), "----");
				map.put(new DataFieldName("A8_akt_dun"), "----");
				map.put(new DataFieldName("A8_ash_dun"), "----");
				map.put(new DataFieldName("A8_zov_dun"), "----");
				map.put(new DataFieldName("A8_huvi"), "----");

				List<Long> audit_chiglel_count26 = (List<Long>) dao.getHQLResult("select count(*) from LnkMainFormT2 t where t.mid = " + main.getId() + " group by t.dirid", "list");

				List<LnkMainFormT2> audit_risk_count26 = (List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.mid = " + main.getId(), "list");

				map.put(new DataFieldName("audit_chiglel_count_2.6"), String.valueOf(audit_chiglel_count26.size()));
				map.put(new DataFieldName("audit_risk_count_2.6"), String.valueOf(audit_risk_count26.size()));

				if (selectedMaterial != null){
					SourceData sd = null;
					if (selectedMaterial.getData1().equalsIgnoreCase("1")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 32", "current");
					}
					else if (selectedMaterial.getData1().equalsIgnoreCase("2")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 33", "current");
					}
					else if (selectedMaterial.getData1().equalsIgnoreCase("3")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 34", "current");
					}
					else if (selectedMaterial.getData1().equalsIgnoreCase("4")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 35", "current");
					}
					if (sd != null){
						map.put( new DataFieldName("selected_mat_suuri"), sd.getText());
					}

					map.put(new DataFieldName("selected_mat_description"), selectedMaterial.getDescription1());
					map.put(new DataFieldName("mat_suuri_huvi"), selectedMaterial.getPercent());
					map.put(new DataFieldName("mat_suuri_dun"), selectedMaterial.getData4());
				}
				
				List<LutUser> eronkhii_auditors = (List<LutUser>) dao.getHQLResult("from LutUser t where t.positionid = 1", "list");
				if (eronkhii_auditors.size() > 0){
					map.put(new DataFieldName("eronkhii_auditor"), ((eronkhii_auditors.get(0).getFamilyname() != null && eronkhii_auditors.get(0).getFamilyname().length() > 0) ? (eronkhii_auditors.get(0).getFamilyname().substring(0, 1) + ".") : "") + eronkhii_auditors.get(0).getGivenname());
				}
				
				List<Long> t2ids = (List<Long>) dao.getHQLResult("select t.id from LnkMainFormT2 t where t.mid = " + main.getId(), "list");

				List<Long> audit_chiglel_count42 = (List<Long>) dao.getHQLResult("select count(*) from LnkRiskT2 t where t.t2id in (" + ((t2ids.size() > 0) ? (Arrays.toString(t2ids.toArray(new Long[t2ids.size()])).replace("[", "").replace("]", "")) : "0") + ") group by t.dirid", "list");

				List<LnkRiskT2> audit_risk_count42 = (List<LnkRiskT2>) dao.getHQLResult("from LnkRiskT2 t where t.t2id in (" + ((t2ids.size() > 0) ? (Arrays.toString(t2ids.toArray(new Long[t2ids.size()])).replace("[", "").replace("]", "")) : "0") + ")", "list");

				map.put(new DataFieldName("audit_chiglel_count_4.2"), String.valueOf(audit_chiglel_count42.size()));
				map.put(new DataFieldName("audit_risk_count_4.2"), String.valueOf(audit_risk_count42.size()));
				
				List<LnkMainUser> mainusers = main.getLnkMainUsers();
				String auditorsString = "АУДИТОР ...............................";
				if (mainusers != null && mainusers.size() > 0){
					for(LnkMainUser m : mainusers){
						LutPosition positionObj = (LutPosition) dao.getHQLResult("from LutPosition t where t.id = " + m.getPositionid(), "current");
						if (m.getPositionid() == 2 || m.getPositionid() == 4){
							String name = ((m.getLutUser().getFamilyname() != null && m.getLutUser().getFamilyname().length() > 0) ? (m.getLutUser().getFamilyname().substring(0, 1) + ".") : "") + m.getLutUser().getGivenname();
							map.put(new DataFieldName("terguulekh_name"), positionObj.getPositionname() + "..............................."  + name);
						}
						
						else if (m.getPositionid() == 5){
							String name = ((m.getLutUser().getFamilyname() != null && m.getLutUser().getFamilyname().length() > 0) ? (m.getLutUser().getFamilyname().substring(0, 1) + ".") : "") + m.getLutUser().getGivenname();
							map.put(new DataFieldName("manager_name"), positionObj.getPositionname() + "..............................."  + name);
						}
						
						else{
							if (auditorsString.length() > 0){
								auditorsString = auditorsString + ", ";
							}
							auditorsString = auditorsString +  ((m.getLutUser().getFamilyname() != null && m.getLutUser().getFamilyname().length() > 0) ? (m.getLutUser().getFamilyname().substring(0, 1) + ".") : "") + m.getLutUser().getGivenname();
						}
					}
				}
				
				Date today = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				
				map.put(new DataFieldName("today_date"), df.format(today));
				map.put(new DataFieldName("auditor_name"), auditorsString);
				/*List<LnkMainUser> members = main.getLnkMainUsers();
    			int membercount = 0;
    			if (members != null && members.size() > 0){
    				for(int i=0;i<members.size() && membercount<3;i++){
    					if (members.get(i).getPositionid() == ;
    				}
    			}
    			map.put(new DataFieldName("member1"), "12/10/2013");
    			map.put(new DataFieldName("mem1_alba"), "1234800");

     			map.put(new DataFieldName("member2"), "Jason");
    			map.put(new DataFieldName("mem2_alba"), "Collins Street");
    			map.put(new DataFieldName("member3"), "12/10/2013");
    			map.put(new DataFieldName("mem3_alba"), "1234800");*/
				data.add(map);		

				String xname=au.getOrgname().trim()+"-"+furl;
				xname = URLEncoder.encode(xname,"UTF-8"); 

				if (mergedOutput) {
					/*
					 * This is a "poor man's" merge, which generates the mail merge  
					 * results as a single docx, and just hopes for the best.
					 * Images and hyperlinks should be ok. But numbering 
					 * will continue, as will footnotes/endnotes.
					 *  
					 * If your resulting documents aren't opening in Word, then
					 * you probably need MergeDocx to perform the merge.
					 */

					// How to treat the MERGEFIELD, in the output?
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					//    				System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

					WordprocessingMLPackage output = org.docx4j.model.fields.merge.MailMerger.getConsolidatedResultCrude(wordMLPackage, data, true);

					//    				System.out.println(XmlUtils.marshaltoString(output.getMainDocumentPart().getJaxbElement(), true, true));

					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-word; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
						output.save(outputStream);  
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");
					}


				}
				else {
					// Need to keep the MERGEFIELDs. If you don't, you'd have to clone the docx, and perform the
					// merge on the clone.  For how to clone, see the MailMerger code, method getConsolidatedResultCrude
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					int i = 1;
					for (Map<DataFieldName, String> thismap : data) {
						org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, thismap, true);
						try (ServletOutputStream outputStream = response.getOutputStream()) {
							response.setContentType("application/ms-word; charset=UTF-8");
							response.setCharacterEncoding("UTF-8");
							response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
							wordMLPackage.save(outputStream);  
							outputStream.close();
						}
						catch (Exception e) {
							System.out.println("ishe orov");
						}
						i++;
					}			
				}
				System.out.println("Mail merge performed successfully.");    			
			}
			else if(fname.equalsIgnoreCase("wordReportTSHZ")){
				System.out.println("matar2");
				FileInputStream wis = null;
				File wordFile = new File(appPath+"/assets/zagvarfile/Audit report-template-TSHZ-last.docx");    			 
				//wis = new FileInputStream(wordFile);
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				FinFormG1 g1 = (FinFormG1) dao.getHQLResult("from FinFormG1 t where t.mid='"+main.getId()+"'", "current");
				FinFormG2 g2 = (FinFormG2) dao.getHQLResult("from FinFormG2 t where t.mid='"+main.getId()+"'", "current");
				FsFormA4 selectedMaterial = (FsFormA4) dao.getHQLResult("from FsFormA4 t where t.orgcode='"+main.getOrgcode()+"' and t.cyear = '" + main.getAudityear() + "' and t.isselect = 1 and t.planid = " + main.getId() + " and t.stepid = 4", "current");
				boolean mergedOutput = false;

				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
				//    							System.getProperty("user.dir") + "/template.docx"));

				List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

				// Instance 1
				Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

				// Instance 2
				//map = new HashMap<DataFieldName, String>();
				int ayear=Integer.parseInt(main.getAudityear())-1;
				if (main != null && au != null){

					map.put(new DataFieldName("g1c9"), main.getAuditname());
					map.put(new DataFieldName("g1f9"), main.getGencode());
					map.put(new DataFieldName("auditweblink"), au.getWeb());
					map.put(new DataFieldName("auditname"), main.getAuditname());
					//map.put(new DataFieldName("auditname"), "<![CDATA[<b>Bold.</b> <u>Underlined.</u> <i>Italic.</i> <big>Big.</big> <small>Small</small>]]>");

					map.put(new DataFieldName("b2-1_f11"), au.getOrgname());
					map.put(new DataFieldName("b2-1_f31"), au.getHeadprof());
					map.put(new DataFieldName("b2-1_f29"), au.getHeadsurname());
					map.put(new DataFieldName("b2-1_f30"), au.getHeadfullname());
					map.put(new DataFieldName("b2-1_f17"), au.getNdorg());
					map.put(new DataFieldName("b2-1_f18"), au.getHeadorder());

					if (g1 != null){
						map.put(new DataFieldName("g1e22"), g1.getRow7_1());
						map.put(new DataFieldName("g1_e16"), g1.getRow1_1());
						map.put(new DataFieldName("g1_e19"), g1.getRow4_1());
						map.put(new DataFieldName("g1_e20"), g1.getRow5_1());
						map.put(new DataFieldName("g1_e21"), g1.getRow6_1());
						map.put(new DataFieldName("g1_e22"), au.getOrgname());
						map.put(new DataFieldName("g1_e23"), g1.getRow8_1());
						map.put(new DataFieldName("g1_e24"), g1.getRow9_1());
						map.put(new DataFieldName("g1_e25"), g1.getRow10_1());
						map.put(new DataFieldName("g1_e26"), g1.getRow11_1());
						map.put(new DataFieldName("g1_e27"), g1.getRow12_1());
						map.put(new DataFieldName("g1_e28"), g1.getRow13_1());
						map.put(new DataFieldName("g1_e29"), g1.getRow14_1());
						map.put(new DataFieldName("g1_e30"), g1.getRow15_1());
						map.put(new DataFieldName("g1_e31"), g1.getRow16_1());
						map.put(new DataFieldName("g1_e32"), g1.getRow17_1());
						map.put(new DataFieldName("g1_e33"), g1.getRow18_1());
						map.put(new DataFieldName("g1_e34"), g1.getRow19_1());
						map.put(new DataFieldName("g1_e35"), g1.getRow20_1());
						map.put(new DataFieldName("g1_e36"), g1.getRow21_1());
						map.put(new DataFieldName("g1_e37"), g1.getRow22_1());
						map.put(new DataFieldName("g1_e38"), g1.getRow23_1());
						map.put(new DataFieldName("g1_e39"), g1.getRow24_1());
						map.put(new DataFieldName("g1_e40"), g1.getRow25_1());
						map.put(new DataFieldName("g1_e41"), g1.getRow26_1());
					}

					Long sumUndur = (long)0;
					Long sumDund = (long)0;
					Long sumBaga = (long)0;
					Long rt = (long)0;
					List<LnkMainFormT2> lnkmaint2s = main.getLnkMainFormT2s();
					if (lnkmaint2s != null && !lnkmaint2s.isEmpty()){
						for(LnkMainFormT2 t2 : lnkmaint2s){
							List<LnkRiskT2> riskt2s = t2.getLnkRiskT2s();
							if (riskt2s != null && !riskt2s.isEmpty()){
								for(LnkRiskT2 risk : riskt2s){
									if (risk.isData5()){
										sumUndur = sumUndur + 1;
									}
									if (risk.isData6()){
										sumDund = sumDund + 1;
									}
									if (risk.isData7()){
										sumBaga = sumBaga + 1;
									}
									if (risk.getData1() > 0 && risk.getData1() < 6){
										rt = rt+1;
									}
									if (risk.getData2() > 0 && risk.getData2() < 6){
										rt = rt+1;
									}
									if (risk.getData3() > 0 && risk.getData3() < 4){
										rt = rt+1;
									}
								}
							}
						}
					}

					map.put(new DataFieldName("b4-3_p16"), String.valueOf(rt));
					map.put(new DataFieldName("b2-6_d_count"), String.valueOf(sumUndur + sumDund + sumBaga));
					map.put(new DataFieldName("b4-1_j_count"), String.valueOf(sumUndur));
					map.put(new DataFieldName("b4-1_i_count"), String.valueOf(sumDund));
					map.put(new DataFieldName("b4-1_h_count"), String.valueOf(sumBaga));

					List<LnkMainUser> auditUsers = main.getLnkMainUsers();
					if (auditUsers != null && !auditUsers.isEmpty()){
						for(LnkMainUser u : auditUsers){
							if (u.getPositionid() == 2 || u.getPositionid() == 4){
								LutUser user = u.getLutUser();
								if (user != null){
									map.put(new DataFieldName("terguulekh_auditor"), ((user.getFamilyname() != null) ? (user.getFamilyname().substring(0, 1) + ".") : "") + user.getGivenname());
									map.put(new DataFieldName("terguulekh_auditor_tel"), user.getMobile());
									map.put(new DataFieldName("terguulekh_auditor_email"), user.getEmail());
								}
							}
							else if (u.getPositionid() == 5){
								LutUser user = u.getLutUser();
								if (user != null){
									map.put(new DataFieldName("manager_auditor"), ((user.getFamilyname() != null) ? (user.getFamilyname().substring(0, 1) + ".") : "") + user.getGivenname());
									map.put(new DataFieldName("manager_auditor_tel"), user.getMobile());
									map.put(new DataFieldName("manager_auditor_email"), user.getEmail());
								}
							}
							else if (u.getPositionid() == 6){
								LutUser user = u.getLutUser();
								if (user != null){
									map.put(new DataFieldName("akhlah_auditor"), ((user.getFamilyname() != null) ? (user.getFamilyname().substring(0, 1) + ".") : "") + user.getGivenname());
									map.put(new DataFieldName("akhlah_auditor_tel"), user.getMobile());
									map.put(new DataFieldName("akhlah_auditor_email"), user.getEmail());
								}
							}
						}
					}

					List<FinTgt1> tgt1s = (List<FinTgt1>) dao.getHQLResult("from FinTgt1 t where t.planid='"+main.getId()+"'", "list");
					if (tgt1s != null && !tgt1s.isEmpty()){
						for(FinTgt1 t : tgt1s){
							if (t.getData1() != null && t.getData1().equalsIgnoreCase("21")){
								map.put(new DataFieldName("20tgt1_g10"), t.getData5());
								map.put(new DataFieldName("20tgt1_h10"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_10_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_10_differential"), String.valueOf(data6-data5));
								}

							}
							else if (t.getData1() != null && t.getData1().equalsIgnoreCase("22")){
								map.put(new DataFieldName("20tgt1_g86"), t.getData5());
								map.put(new DataFieldName("20tgt1_h86"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_86_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_86_differential"), String.valueOf(data6-data5));
								}
							}
							else if (t.getData1() != null && t.getData1().equalsIgnoreCase("2")){
								map.put(new DataFieldName("20tgt1_g9"), t.getData5());
								map.put(new DataFieldName("20tgt1_h9"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_9_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_9_differential"), String.valueOf(data6-data5));
								}
							}
							else if (t.getData1() != null && t.getData1().equalsIgnoreCase("23")){
								map.put(new DataFieldName("20tgt1_g95"), t.getData5());
								map.put(new DataFieldName("20tgt1_h95"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_95_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_95_differential"), String.valueOf(data6-data5));
								}
							}
							else if (t.getData1() != null && t.getData1().equalsIgnoreCase("3")){
								map.put(new DataFieldName("20tgt1_g99"), t.getData5());
								map.put(new DataFieldName("20tgt1_h99"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_99_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_99_differential"), String.valueOf(data6-data5));
								}
							}
							else if (t.getData1() != null && t.getData1().equalsIgnoreCase("31")){
								map.put(new DataFieldName("20tgt1_g100"), t.getData5());
								map.put(new DataFieldName("20tgt1_h100"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_100_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_100_differential"), String.valueOf(data6-data5));
								}
							}
							else if (t.getData1() != null && t.getData1().equalsIgnoreCase("32")){
								map.put(new DataFieldName("20tgt1_g105"), t.getData5());
								map.put(new DataFieldName("20tgt1_h105"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_105_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_105_differential"), String.valueOf(data6-data5));
								}
							}
							else if (t.getData1() != null && t.getData1().equalsIgnoreCase("35")){
								map.put(new DataFieldName("20tgt1_g113"), t.getData5());
								map.put(new DataFieldName("20tgt1_h113"), t.getData6());
								if (t.getData5() != null && t.getData5().length() > 0 && t.getData6() != null && t.getData6().length() > 0){
									Double data5 = Double.parseDouble(t.getData5());
									Double data6 = Double.parseDouble(t.getData6());
									map.put(new DataFieldName("20tgt1_113_percent"), String.valueOf((data5 == 0) ? 0 : (data6/data5*100-100)) + "%");
									map.put(new DataFieldName("20tgt1_113_differential"), String.valueOf(data6-data5));
								}
							}
						}
					}
				}
				List<FinNt2> nt2s = (List<FinNt2>) dao.getHQLResult("from FinNt2 t where t.planid='"+main.getId()+"'", "list");
				if (nt2s != null && !nt2s.isEmpty()){
					for(FinNt2 t : nt2s){
						if (t.getData1() != null && t.getData1().equalsIgnoreCase("I")){
							map.put(new DataFieldName("22nt2_d8"), t.getData4());
						}
						else if (t.getData1() != null && t.getData1().equalsIgnoreCase("21")){
							map.put(new DataFieldName("22nt2_d9"), t.getData4());
						}
						else if (t.getData1() != null && t.getData1().equalsIgnoreCase("22")){
							map.put(new DataFieldName("22nt2_d88"), t.getData4());
						}
					}
				}

				Double total_dun_sum = 0.0;
				Long total_asuudal_sum = (long)0;

				List<LnkRiskTryout> lnkrisktryouts = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.data23 = 1 and t.b46data4=1", "list");
				map.put(new DataFieldName("v4-6-1_total_asuudal_count"), String.valueOf(lnkrisktryouts.size()));
				total_asuudal_sum = total_asuudal_sum + lnkrisktryouts.size();
				if (lnkrisktryouts != null && !lnkrisktryouts.isEmpty()){
					Double sumLnkRiskTryout = 0.0;
					for(int i=0;i<lnkrisktryouts.size();i++){
						if (lnkrisktryouts.get(i).getData21() != null && lnkrisktryouts.get(i).getData21().length() > 0){
							if(lnkrisktryouts.get(i).getData21().indexOf("₮")>-1){
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", "").substring(2));
							}
							else{
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", ""));
							}
						}
					}
					map.put(new DataFieldName("v4-6-1_e_sum"), String.valueOf(sumLnkRiskTryout));
					total_dun_sum = total_dun_sum + sumLnkRiskTryout;
				}
				else{
					map.put(new DataFieldName("v4-6-1_e_sum"), "0");
				}

				lnkrisktryouts = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.data23 = 1 and t.b46data4=2", "list");

				SourceData eneni = (SourceData) dao.getHQLResult("from SourceData t where t.id = 61", "current");
				SourceData zurchiltext = (SourceData) dao.getHQLResult("from SourceData t where t.id = 62", "current");

				map.put(new DataFieldName("v4-6-2_total_asuudal_count"), String.valueOf(lnkrisktryouts.size()));
				total_asuudal_sum = total_asuudal_sum + lnkrisktryouts.size();
				if (lnkrisktryouts != null && !lnkrisktryouts.isEmpty()){
					Double sumLnkRiskTryout = 0.0;
					String text462 = "";
					for(int i=0;i<lnkrisktryouts.size();i++){
						text462 = text462 + ("2.9.1."+(i+1)+" "+lnkrisktryouts.get(i).getData20() + "\n"+ eneni.getText() + " " + lnkrisktryouts.get(i).getB46data1() + " " + zurchiltext.getText() + "\n" + lnkrisktryouts.get(i).getB46data2() + "\n");
						map.put(new DataFieldName("v4-6-2_d_column_value1"), text462);
						if (lnkrisktryouts.get(i).getData21() != null && lnkrisktryouts.get(i).getData21().length() > 0){
							System.out.println(lnkrisktryouts.get(i).getData21());
							if(lnkrisktryouts.get(i).getData21().indexOf("₮")>-1){
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", "").substring(2));
							}
							else{
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", ""));
							}
							
						}
					}
					map.put(new DataFieldName("v4-6-2_e_sum"), String.valueOf(sumLnkRiskTryout));
					total_dun_sum = total_dun_sum + sumLnkRiskTryout;
				}
				else{
					map.put(new DataFieldName("v4-6-2_e_sum"), "0");
				}

				lnkrisktryouts = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.data23 = 1 and t.b46data4=3", "list");
				map.put(new DataFieldName("v4-6-3_total_asuudal_count"), String.valueOf(lnkrisktryouts.size()));
				total_asuudal_sum = total_asuudal_sum + lnkrisktryouts.size();
				if (lnkrisktryouts != null && !lnkrisktryouts.isEmpty()){
					Double sumLnkRiskTryout = 0.0;
					String text463 = "";
					for(int i=0;i<lnkrisktryouts.size();i++){
						text463 = text463 + ("2.9.2."+(i+1)+" "+lnkrisktryouts.get(i).getData20() + "\n"+ eneni.getText() + " " + lnkrisktryouts.get(i).getB46data1() + " " + zurchiltext.getText() + "\n" + lnkrisktryouts.get(i).getB46data2() + "\n");
						map.put(new DataFieldName("v4-6-3_d_column_value1"), text463);
						if (lnkrisktryouts.get(i).getData21() != null && lnkrisktryouts.get(i).getData21().length() > 0){
							if(lnkrisktryouts.get(i).getData21().indexOf("₮")>-1){
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", "").substring(2));
							}
							else{
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", ""));
							}
						}
					}
					map.put(new DataFieldName("v4-6-3_e_sum"), String.valueOf(sumLnkRiskTryout));
					total_dun_sum = total_dun_sum + sumLnkRiskTryout;
				}
				else{
					map.put(new DataFieldName("v4-6-3_e_sum"), "0");
				}

				lnkrisktryouts = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.data23 = 1 and t.b46data4=4", "list");
				map.put(new DataFieldName("v4-6-4_total_asuudal_count"), String.valueOf(lnkrisktryouts.size()));
				total_asuudal_sum = total_asuudal_sum + lnkrisktryouts.size();
				if (lnkrisktryouts != null && !lnkrisktryouts.isEmpty()){
					Double sumLnkRiskTryout = 0.0;
					String text464 = "";
					for(int i=0;i<lnkrisktryouts.size();i++){
						text464 = text464 + ("2.9.3."+(i+1)+" "+lnkrisktryouts.get(i).getData20() + "\n"+ eneni.getText() + " " + lnkrisktryouts.get(i).getB46data1() + " " + zurchiltext.getText() + "\n" + lnkrisktryouts.get(i).getB46data2() + "\n");
						map.put(new DataFieldName("v4-6-4_d_column_value1"), text464);
						if (lnkrisktryouts.get(i).getData21() != null && lnkrisktryouts.get(i).getData21().length() > 0){
							if(lnkrisktryouts.get(i).getData21().indexOf("₮")>-1){
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", "").substring(2));
							}
							else{
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", ""));
							}
						}
					}
					map.put(new DataFieldName("v4-6-4_e_sum"), String.valueOf(sumLnkRiskTryout));
					total_dun_sum = total_dun_sum + sumLnkRiskTryout;
				}
				else{
					map.put(new DataFieldName("v4-6-4_e_sum"), "0");
				}

				lnkrisktryouts = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.data23 = 1 and t.b46data4=5", "list");
				map.put(new DataFieldName("v4-6-5_total_asuudal_count"), String.valueOf(lnkrisktryouts.size()));
				total_asuudal_sum = total_asuudal_sum + lnkrisktryouts.size();
				if (lnkrisktryouts != null && !lnkrisktryouts.isEmpty()){
					Double sumLnkRiskTryout = 0.0;
					String text465 = "";
					for(int i=0;i<lnkrisktryouts.size();i++){
						text465 = text465 + ("2.9.4."+(i+1)+" "+lnkrisktryouts.get(i).getData20() + "\n"+ eneni.getText() + " " + lnkrisktryouts.get(i).getB46data1() + " " + zurchiltext.getText() + "\n" + lnkrisktryouts.get(i).getB46data2() + "\n");
						map.put(new DataFieldName("v4-6-5_d_column_value1"), text465);
						if (lnkrisktryouts.get(i).getData21() != null && lnkrisktryouts.get(i).getData21().length() > 0){
							if(lnkrisktryouts.get(i).getData21().indexOf("₮")>-1){
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", "").substring(2));
							}
							else{
								sumLnkRiskTryout = sumLnkRiskTryout + Double.parseDouble(lnkrisktryouts.get(i).getData21().replace(",", ""));
							}
						}
					}
					map.put(new DataFieldName("v4-6-5_e_sum"), String.valueOf(sumLnkRiskTryout));
					total_dun_sum = total_dun_sum + sumLnkRiskTryout;
				}
				else{
					map.put(new DataFieldName("v4-6-5_e_sum"), "0");
				}

				map.put(new DataFieldName("total_dun_sum"), String.valueOf(total_dun_sum));
				map.put(new DataFieldName("total_asuudal_sum"), String.valueOf(total_asuudal_sum));

				/*List<Long> audit_chiglel_count26 = (List<Long>) dao.getHQLResult("select count(*) from LnkMainFormT2 t where t.mid = " + main.getId() + " group by t.dirid", "list");*/

				List<LnkMainFormT2> audit_risk_count26 = (List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.mid = " + main.getId(), "list");

				/*map.put(new DataFieldName("audit_chiglel_count_2.6"), String.valueOf(audit_chiglel_count26.size()));*/
				map.put(new DataFieldName("b2-6_d_count"), String.valueOf(audit_risk_count26.size()));

				if (selectedMaterial != null){
					SourceData sd = null;
					if (selectedMaterial.getData1().equalsIgnoreCase("1")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 32", "current");
					}
					else if (selectedMaterial.getData1().equalsIgnoreCase("2")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 33", "current");
					}
					else if (selectedMaterial.getData1().equalsIgnoreCase("3")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 34", "current");
					}
					else if (selectedMaterial.getData1().equalsIgnoreCase("4")){
						sd = (SourceData) dao.getHQLResult("from SourceData t where t.id = 35", "current");
					}
					if (sd != null){
						map.put( new DataFieldName("selected_mat_suuri"), sd.getText());
					}

					map.put(new DataFieldName("v6_selected_description1"), selectedMaterial.getDescription1());
					map.put(new DataFieldName("v6_selected_description2"), selectedMaterial.getDescription2());
					map.put(new DataFieldName("v6_selected_description3"), selectedMaterial.getDescription3());

					map.put(new DataFieldName("v6_c15-18_selected_moneyvalue"), selectedMaterial.getData2());
					map.put(new DataFieldName("mat_suuri_huvi"), selectedMaterial.getPercent());
					map.put(new DataFieldName("mat_suuri_dun"), selectedMaterial.getData4());
				}


				List<Long> t2ids = (List<Long>) dao.getHQLResult("select t.id from LnkMainFormT2 t where t.mid = " + main.getId(), "list");

				List<Long> audit_chiglel_count42 = (List<Long>) dao.getHQLResult("select count(*) from LnkRiskT2 t where t.t2id in (" + ((t2ids.size() > 0) ? (Arrays.toString(t2ids.toArray(new Long[t2ids.size()])).replace("[", "").replace("]", "")) : "0") + ") group by t.dirid", "list");

				List<LnkRiskT2> audit_risk_count42 = (List<LnkRiskT2>) dao.getHQLResult("from LnkRiskT2 t where t.t2id in (" + ((t2ids.size() > 0) ? (Arrays.toString(t2ids.toArray(new Long[t2ids.size()])).replace("[", "").replace("]", "")) : "0") + ")", "list");

				map.put(new DataFieldName("v3-1_b_chiglel_count"), String.valueOf(audit_chiglel_count42.size()));
				map.put(new DataFieldName("v3-1_c_risk_count"), String.valueOf(audit_risk_count42.size()));
				map.put(new DataFieldName("v3-1_e_gorim_soril_count"), String.valueOf(audit_risk_count42.size()));

				lnkrisktryouts = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.data23 = 1", "list");
				int ersdeltei = lnkrisktryouts.size();
				lnkrisktryouts = (List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"'", "list");
				int alltryoutcount = lnkrisktryouts.size();

				map.put(new DataFieldName("gorim_soril_zurchiltei_count"), String.valueOf((alltryoutcount != 0) ? (ersdeltei*100/alltryoutcount) : 0));
				map.put(new DataFieldName("v4-3_asuudal_count"), String.valueOf(ersdeltei));
				data.add(map);

				/*Document doc = new Document(wordFile.getAbsolutePath());
    			DataSet pizzaDs = new DataSet();
    			List<FinCt1a> finct1as = (List<FinCt1a>)dao.getHQLResult("from FinCt1a t where t.planid = " + main.getId(), "list");

    			DataTable a = new DataTable();
    			pizzaDs.getTables().add(a);

    			doc.getMailMerge().execute(new String[]{"ACCOUNT","Балансын үзүүлэлт"}, new Object[]{});*/

				String xname=au.getOrgname().trim()+"-"+furl;
				xname = URLEncoder.encode(xname,"UTF-8"); 

				if (mergedOutput) {
					/*
					 * This is a "poor man's" merge, which generates the mail merge  
					 * results as a single docx, and just hopes for the best.
					 * Images and hyperlinks should be ok. But numbering 
					 * will continue, as will footnotes/endnotes.
					 *  
					 * If your resulting documents aren't opening in Word, then
					 * you probably need MergeDocx to perform the merge.
					 */

					// How to treat the MERGEFIELD, in the output?
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					//    				System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

					WordprocessingMLPackage output = org.docx4j.model.fields.merge.MailMerger.getConsolidatedResultCrude(wordMLPackage, data, true);

					//    				System.out.println(XmlUtils.marshaltoString(output.getMainDocumentPart().getJaxbElement(), true, true));

					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-word; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
						output.save(outputStream);  
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");
					}


				}
				else {
					// Need to keep the MERGEFIELDs. If you don't, you'd have to clone the docx, and perform the
					// merge on the clone.  For how to clone, see the MailMerger code, method getConsolidatedResultCrude
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					int i = 1;
					for (Map<DataFieldName, String> thismap : data) {
						org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, thismap, true);
						try (ServletOutputStream outputStream = response.getOutputStream()) {
							response.setContentType("application/ms-word; charset=UTF-8");
							response.setCharacterEncoding("UTF-8");
							response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
							wordMLPackage.save(outputStream);  
							outputStream.close();
						}
						catch (Exception e) {
							System.out.println("ishe orov");
						}
						i++;
					}			
				}
				System.out.println("Mail merge performed successfully.");    			
			}
			else if(fname.equalsIgnoreCase("wordReportOpinion1")){
				FileInputStream wis = null;
				File wordFile = new File(appPath+"/assets/zagvarfile/opinion-1.docx");    			 
				//wis = new FileInputStream(wordFile);
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				FinFormG1 g1 = (FinFormG1) dao.getHQLResult("from FinFormG1 t where t.mid='"+main.getId()+"'", "current");
				boolean mergedOutput = false;

				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
				//    							System.getProperty("user.dir") + "/template.docx"));

				List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

				// Instance 1
				Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

				// Instance 2
				//map = new HashMap<DataFieldName, String>();
				if (main != null && au != null){

					if (g1 != null){
						map.put(new DataFieldName("g1_e16"), g1.getRow1_1());
						map.put(new DataFieldName("g1_e17"), g1.getRow2_1());
						map.put(new DataFieldName("g1_e18"), g1.getRow3_1());
						map.put(new DataFieldName("g1_e22"), g1.getRow7_1());
					}

				}

				data.add(map);

				String xname=au.getOrgname().trim()+"-"+furl;
				xname = URLEncoder.encode(xname,"UTF-8"); 

				if (mergedOutput) {
					/*
					 * This is a "poor man's" merge, which generates the mail merge  
					 * results as a single docx, and just hopes for the best.
					 * Images and hyperlinks should be ok. But numbering 
					 * will continue, as will footnotes/endnotes.
					 *  
					 * If your resulting documents aren't opening in Word, then
					 * you probably need MergeDocx to perform the merge.
					 */

					// How to treat the MERGEFIELD, in the output?
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					//    				System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

					WordprocessingMLPackage output = org.docx4j.model.fields.merge.MailMerger.getConsolidatedResultCrude(wordMLPackage, data, true);

					//    				System.out.println(XmlUtils.marshaltoString(output.getMainDocumentPart().getJaxbElement(), true, true));

					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-word; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
						output.save(outputStream);  
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");
					}


				}
				else {
					// Need to keep the MERGEFIELDs. If you don't, you'd have to clone the docx, and perform the
					// merge on the clone.  For how to clone, see the MailMerger code, method getConsolidatedResultCrude
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					int i = 1;
					for (Map<DataFieldName, String> thismap : data) {
						org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, thismap, true);
						try (ServletOutputStream outputStream = response.getOutputStream()) {
							response.setContentType("application/ms-word; charset=UTF-8");
							response.setCharacterEncoding("UTF-8");
							response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
							wordMLPackage.save(outputStream);  
							outputStream.close();
						}
						catch (Exception e) {
							System.out.println("ishe orov");
						}
						i++;
					}			
				}
				System.out.println("Mail merge performed successfully.");    			
			}
			else if(fname.equalsIgnoreCase("wordReportOpinion2")){
				FileInputStream wis = null;
				File wordFile = new File(appPath+"/assets/zagvarfile/opinion-2.docx");    			 
				//wis = new FileInputStream(wordFile);
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				FinFormG1 g1 = (FinFormG1) dao.getHQLResult("from FinFormG1 t where t.mid='"+main.getId()+"'", "current");
				boolean mergedOutput = false;

				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
				//    							System.getProperty("user.dir") + "/template.docx"));

				List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

				// Instance 1
				Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

				// Instance 2
				//map = new HashMap<DataFieldName, String>();
				if (main != null && au != null){

					if (g1 != null){
						map.put(new DataFieldName("g1_e16"), g1.getRow1_1());
						map.put(new DataFieldName("g1_e17"), g1.getRow2_1());
						map.put(new DataFieldName("g1_e18"), g1.getRow3_1());
						map.put(new DataFieldName("g1_e22"), g1.getRow7_1());
					}

				}

				data.add(map);

				String xname=au.getOrgname().trim()+"-"+furl;
				xname = URLEncoder.encode(xname,"UTF-8"); 

				if (mergedOutput) {
					/*
					 * This is a "poor man's" merge, which generates the mail merge  
					 * results as a single docx, and just hopes for the best.
					 * Images and hyperlinks should be ok. But numbering 
					 * will continue, as will footnotes/endnotes.
					 *  
					 * If your resulting documents aren't opening in Word, then
					 * you probably need MergeDocx to perform the merge.
					 */

					// How to treat the MERGEFIELD, in the output?
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					//    				System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

					WordprocessingMLPackage output = org.docx4j.model.fields.merge.MailMerger.getConsolidatedResultCrude(wordMLPackage, data, true);

					//    				System.out.println(XmlUtils.marshaltoString(output.getMainDocumentPart().getJaxbElement(), true, true));

					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-word; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
						output.save(outputStream);  
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");
					}


				}
				else {
					// Need to keep the MERGEFIELDs. If you don't, you'd have to clone the docx, and perform the
					// merge on the clone.  For how to clone, see the MailMerger code, method getConsolidatedResultCrude
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					int i = 1;
					for (Map<DataFieldName, String> thismap : data) {
						org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, thismap, true);
						try (ServletOutputStream outputStream = response.getOutputStream()) {
							response.setContentType("application/ms-word; charset=UTF-8");
							response.setCharacterEncoding("UTF-8");
							response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
							wordMLPackage.save(outputStream);  
							outputStream.close();
						}
						catch (Exception e) {
							System.out.println("ishe orov");
						}
						i++;
					}			
				}
				System.out.println("Mail merge performed successfully.");    			
			}
			else if(fname.equalsIgnoreCase("wordReportOpinion3")){
				FileInputStream wis = null;
				File wordFile = new File(appPath+"/assets/zagvarfile/opinion-3.docx");    			 
				//wis = new FileInputStream(wordFile);
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				FinFormG1 g1 = (FinFormG1) dao.getHQLResult("from FinFormG1 t where t.mid='"+main.getId()+"'", "current");
				boolean mergedOutput = false;

				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
				//    							System.getProperty("user.dir") + "/template.docx"));

				List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

				// Instance 1
				Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

				// Instance 2
				//map = new HashMap<DataFieldName, String>();
				if (main != null && au != null){

					if (g1 != null){
						map.put(new DataFieldName("g1_e16"), g1.getRow1_1());
						map.put(new DataFieldName("g1_e17"), g1.getRow2_1());
						map.put(new DataFieldName("g1_e18"), g1.getRow3_1());
						map.put(new DataFieldName("g1_e22"), g1.getRow7_1());
					}

				}

				data.add(map);

				String xname=au.getOrgname().trim()+"-"+furl;
				xname = URLEncoder.encode(xname,"UTF-8"); 

				if (mergedOutput) {
					/*
					 * This is a "poor man's" merge, which generates the mail merge  
					 * results as a single docx, and just hopes for the best.
					 * Images and hyperlinks should be ok. But numbering 
					 * will continue, as will footnotes/endnotes.
					 *  
					 * If your resulting documents aren't opening in Word, then
					 * you probably need MergeDocx to perform the merge.
					 */

					// How to treat the MERGEFIELD, in the output?
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					//    				System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

					WordprocessingMLPackage output = org.docx4j.model.fields.merge.MailMerger.getConsolidatedResultCrude(wordMLPackage, data, true);

					//    				System.out.println(XmlUtils.marshaltoString(output.getMainDocumentPart().getJaxbElement(), true, true));

					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-word; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
						output.save(outputStream);  
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");
					}


				}
				else {
					// Need to keep the MERGEFIELDs. If you don't, you'd have to clone the docx, and perform the
					// merge on the clone.  For how to clone, see the MailMerger code, method getConsolidatedResultCrude
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					int i = 1;
					for (Map<DataFieldName, String> thismap : data) {
						org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, thismap, true);
						try (ServletOutputStream outputStream = response.getOutputStream()) {
							response.setContentType("application/ms-word; charset=UTF-8");
							response.setCharacterEncoding("UTF-8");
							response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
							wordMLPackage.save(outputStream);  
							outputStream.close();
						}
						catch (Exception e) {
							System.out.println("ishe orov");
						}
						i++;
					}			
				}
				System.out.println("Mail merge performed successfully.");    			
			}
			else if(fname.equalsIgnoreCase("wordReportOpinion4")){
				FileInputStream wis = null;
				File wordFile = new File(appPath+"/assets/zagvarfile/opinion-4.docx");    			 
				//wis = new FileInputStream(wordFile);
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				FinFormG1 g1 = (FinFormG1) dao.getHQLResult("from FinFormG1 t where t.mid='"+main.getId()+"'", "current");
				boolean mergedOutput = false;

				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
				//    							System.getProperty("user.dir") + "/template.docx"));

				List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

				// Instance 1
				Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

				// Instance 2
				//map = new HashMap<DataFieldName, String>();
				if (main != null && au != null){

					if (g1 != null){
						map.put(new DataFieldName("g1_e16"), g1.getRow1_1());
						map.put(new DataFieldName("g1_e17"), g1.getRow2_1());
						map.put(new DataFieldName("g1_e18"), g1.getRow3_1());
						map.put(new DataFieldName("g1_e22"), g1.getRow7_1());
					}

				}

				data.add(map);

				String xname=au.getOrgname().trim()+"-"+furl;
				xname = URLEncoder.encode(xname,"UTF-8"); 

				if (mergedOutput) {
					/*
					 * This is a "poor man's" merge, which generates the mail merge  
					 * results as a single docx, and just hopes for the best.
					 * Images and hyperlinks should be ok. But numbering 
					 * will continue, as will footnotes/endnotes.
					 *  
					 * If your resulting documents aren't opening in Word, then
					 * you probably need MergeDocx to perform the merge.
					 */

					// How to treat the MERGEFIELD, in the output?
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					//    				System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

					WordprocessingMLPackage output = org.docx4j.model.fields.merge.MailMerger.getConsolidatedResultCrude(wordMLPackage, data, true);

					//    				System.out.println(XmlUtils.marshaltoString(output.getMainDocumentPart().getJaxbElement(), true, true));

					try (ServletOutputStream outputStream = response.getOutputStream()) {
						response.setContentType("application/ms-word; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
						output.save(outputStream);  
						outputStream.close();
					}
					catch (Exception e) {
						System.out.println("ishe orov");
					}


				}
				else {
					// Need to keep the MERGEFIELDs. If you don't, you'd have to clone the docx, and perform the
					// merge on the clone.  For how to clone, see the MailMerger code, method getConsolidatedResultCrude
					org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.KEEP_MERGEFIELD);

					int i = 1;
					for (Map<DataFieldName, String> thismap : data) {
						org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, thismap, true);
						try (ServletOutputStream outputStream = response.getOutputStream()) {
							response.setContentType("application/ms-word; charset=UTF-8");
							response.setCharacterEncoding("UTF-8");
							response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+xname+".docx");   
							wordMLPackage.save(outputStream);  
							outputStream.close();
						}
						catch (Exception e) {
							System.out.println("ishe orov");
						}
						i++;
					}			
				}
				System.out.println("Mail merge performed successfully.");    			
			}
		}
	}

	@RequestMapping(value="/{fname}/{furl}/{step}/{id}",method=RequestMethod.GET)
	public void exportGuitsetgeh(@PathVariable String fname,@PathVariable String furl,@PathVariable long step, @PathVariable long id, HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			String appPath = req.getServletContext().getRealPath("");	
			File files = new File(appPath+"/assets/zagvarfile/working_papers-guitsetgel.xlsx");

			fis = new FileInputStream(files);
			Workbook workbook = new XSSFWorkbook(fis);

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setBorderBottom((short)1);
			cellStyle.setBorderLeft((short)1);
			cellStyle.setBorderRight((short)1);
			cellStyle.setBorderTop((short)1);
			cellStyle.setWrapText(true);
			cellStyle.setAlignment(cellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(cellStyle.VERTICAL_CENTER);
			if(fname.equalsIgnoreCase("gb1")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb1");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(2);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(2);
					cell.setCellValue((String) main.getGencode());
					int k=12;
					for(int i=0;i<fs.size();i++){
						LutFormB1 fb1 = fs.get(i);
						row = sheet.getRow(k+i);
						cell = row.getCell(2);
						cell.setCellValue((String) fb1.getData2());

						cell = row.getCell(3);
						cell.setCellValue((String) fb1.getData3());

						cell = row.getCell(4);
						cell.setCellValue((String) fb1.getData4());

						cell = row.getCell(5);
						cell.setCellValue((String) fb1.getData5());
					}

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb11")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb11");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");

					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(2);
					cell.setCellValue((String) "Тйим");

					row = sheet.getRow(6);
					cell = row.getCell(2);
					cell.setCellValue((String) "Тйим");


					/*int k=12;
       		        for(int i=0;i<fs.size();i++){
       		        	LutFormB1 fb1 = fs.get(i);
       		        	row = sheet.getRow(k+i);
       		        	cell = row.getCell(2);
           		        cell.setCellValue((String) fb1.getData2());

           		        cell = row.getCell(3);
           		        cell.setCellValue((String) fb1.getData3());

           		        cell = row.getCell(4);
        		        cell.setCellValue((String) fb1.getData4());

        		        cell = row.getCell(5);
        		        cell.setCellValue((String) fb1.getData5());
       		        }*/

					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb21")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb21");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(4);
					Cell cell = row.getCell(3);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(6);
					cell = row.getCell(3);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb31")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb31");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb41")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb41");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb42")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb42");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb43")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb43");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(6);
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(8);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb44")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb44");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(5);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(5);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb45")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb45");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(5);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(5);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb46")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb46");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(6);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(6);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb461")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb461");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(6);
					Cell cell = row.getCell(5);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(8);
					cell = row.getCell(5);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb462")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb462");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(6);
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(8);
					cell = row.getCell(11);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb463")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb463");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(6);
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(8);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb464")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb464");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(4);
					Cell cell = row.getCell(6);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(6);
					cell = row.getCell(6);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb465")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb465");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(6);
					Cell cell = row.getCell(4);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(8);
					cell = row.getCell(4);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb5155")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb5155");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					Row row = sheet.getRow(5);
					Cell cell = row.getCell(3);
					cell.setCellValue((String) org.getOrgname());

					row = sheet.getRow(7);
					cell = row.getCell(3);
					cell.setCellValue((String) main.getGencode());


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
			if(fname.equalsIgnoreCase("gb72")){
				if(workbook.getSheet(furl)!=null){
					System.out.println("sheet oldson gb72");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'", "current");
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
					List<LutFormB1> fs=  (List<LutFormB1>) dao.getHQLResult("from LutFormB1 t where t.planid="+id+" and t.stepid="+step+"", "list");
					Sheet sheet = workbook.getSheet(furl);
					/*Row row = sheet.getRow(6);
    		        Cell cell = row.getCell(4);
    		        cell.setCellValue((String) org.getOrgname());

    		        row = sheet.getRow(8);
       		        cell = row.getCell(4);
       		        cell.setCellValue((String) main.getGencode());*/


					for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
						Sheet tmpSheet =workbook.getSheetAt(i);
						if(!tmpSheet.getSheetName().equals(furl)){
							if(!tmpSheet.getSheetName().equalsIgnoreCase("Source_data")){
								workbook.removeSheetAt(i);
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
}
