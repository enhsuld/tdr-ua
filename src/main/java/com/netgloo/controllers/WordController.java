package com.netgloo.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger.OutputField;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.hazelcast.com.eclipsesource.json.JsonArray;
import com.netgloo.service.FileUploadService;

import com.netgloo.dao.UserDao;
import com.netgloo.models.LnkRiskTryout;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.models.SubAuditOrganization;

@RestController
@RequestMapping("/word")
public class WordController {

	@Autowired
    private UserDao dao;	    
    
    @Autowired
    FileUploadService fileUploadService;
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    
    @RequestMapping(value="/{fname}/{mid}/{id}",method=RequestMethod.GET)
    public void exportGuitsetgeh(@PathVariable String fname, @PathVariable long id, @PathVariable long mid,HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {
    
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			String appPath = req.getServletContext().getRealPath("");	
        	
        	if(fname.equalsIgnoreCase("akt")){
        		System.out.println("matar2");
        		FileInputStream wis = null;
    			File wordFile = new File(appPath+"/assets/zagvarfile/akt_done.docx");    			 
    			//wis = new FileInputStream(wordFile);
    			MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+mid+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				LnkRiskTryout tailan = (LnkRiskTryout) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.id="+id+"", "current");
				
    			
    			boolean mergedOutput = false;
    			
    			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
    			
    			List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

    			// Instance 1
    			Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

    			// Instance 2
    			//map = new HashMap<DataFieldName, String>();
    			
    			map.put(new DataFieldName("org_name"), au.getOrgname());
    			map.put(new DataFieldName("dir_name"), au.getHeadfullname());
    			map.put(new DataFieldName("au_name"), main.getAudityear());
    			map.put(new DataFieldName("tovch_utga"), tailan.getData20());
    			map.put(new DataFieldName("urdagavar"), tailan.getB46data2());
    			map.put(new DataFieldName("huulistandart"), tailan.getB46data1());
    			map.put(new DataFieldName("huuliundeslel"), tailan.getB46data3());
    			map.put(new DataFieldName("zurchil_tovch_utga"), tailan.getData20());
    			map.put(new DataFieldName("bielelt_ognoo"), tailan.getB462data4());
    			map.put(new DataFieldName("sig_1"), tailan.getB462data1());
    			map.put(new DataFieldName("sig_2"), tailan.getB462data2());
    			map.put(new DataFieldName("sig_3"), tailan.getB462data2());
    			map.put(new DataFieldName("dun"), tailan.getData21());
    	
    			data.add(map);		
    			
    			String xname="akt"+au.getOrgcode().trim();
				xname = URLEncoder.encode(xname,"UTF-8"); 
				
    			if (mergedOutput) {

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
        	else if(fname.equalsIgnoreCase("albanshaardlaga")){
        		FileInputStream wis = null;
    			File wordFile = new File(appPath+"/assets/zagvarfile/alban_shaardlaga_done.docx");    			 
    			//wis = new FileInputStream(wordFile);
    			MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+mid+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				LnkRiskTryout tailan = (LnkRiskTryout) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.id="+id+"", "current");
				
    			
    			boolean mergedOutput = false;
    			
    			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
    			
    			List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

    			// Instance 1
    			Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

    			// Instance 2
    			//map = new HashMap<DataFieldName, String>();
    			
    			map.put(new DataFieldName("org_name"), au.getOrgname());
    			map.put(new DataFieldName("dir_name"), au.getHeadfullname());
    			map.put(new DataFieldName("au_name"), main.getAudityear());
    			map.put(new DataFieldName("tovch_utga"), tailan.getData20());
    			map.put(new DataFieldName("urdagavar"), tailan.getB46data2());
    			map.put(new DataFieldName("huulistandart"), tailan.getB46data1());
    			map.put(new DataFieldName("huuliundeslel"), tailan.getB46data3());
    			map.put(new DataFieldName("zurchil_tovch_utga"), tailan.getData20());
    			map.put(new DataFieldName("bielelt_ognoo"), tailan.getB462data4());
    			map.put(new DataFieldName("sig_1"), tailan.getB462data1());
    			map.put(new DataFieldName("sig_2"), tailan.getB462data2());
    			map.put(new DataFieldName("dun"), tailan.getData21());
    	
    			data.add(map);		
    			
    			String xname="albanShaardlaga"+au.getOrgcode().trim();
				xname = URLEncoder.encode(xname,"UTF-8"); 
				
    			if (mergedOutput) {

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

        	else if(fname.equalsIgnoreCase("ajiltan")){
        		FileInputStream wis = null;
    			File wordFile = new File(appPath+"/assets/zagvarfile/ajiltan_shaardlaga_done.docx");    		
    			MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+mid+"'", "current");
				SubAuditOrganization au = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id='"+main.getOrgid()+"'", "current");
				LnkRiskTryout tailan = (LnkRiskTryout) dao.getHQLResult("from LnkRiskTryout t where t.mid='"+main.getId()+"' and t.id="+id+"", "current");
				
    			
    			boolean mergedOutput = false;
    			
    			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(wordFile);
    			
    			List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

    			// Instance 1
    			Map<DataFieldName, String> map = new HashMap<DataFieldName, String>();

    			// Instance 2
    			//map = new HashMap<DataFieldName, String>();
    			
    			map.put(new DataFieldName("org_name"), au.getOrgname());
    			map.put(new DataFieldName("dir_name"), au.getHeadfullname());
    			map.put(new DataFieldName("au_name"), main.getAudityear());
    			map.put(new DataFieldName("tovch_utga"), tailan.getData20());
    			map.put(new DataFieldName("urdagavar"), tailan.getB464data2());
    			map.put(new DataFieldName("huulistandart"), tailan.getB464data1());
    			map.put(new DataFieldName("huuliundeslel"), tailan.getB46data3());
    			map.put(new DataFieldName("zurchil_tovch_utga"), tailan.getData20());
    			map.put(new DataFieldName("bielelt_ognoo"), tailan.getB464data6());
    			map.put(new DataFieldName("sig_1"), tailan.getB462data1());
    			map.put(new DataFieldName("sig_2"), tailan.getB462data2());
    			map.put(new DataFieldName("dun"), tailan.getData21());
    			map.put(new DataFieldName("ajiltan"), tailan.getB464data4());
    	
    			data.add(map);		
    			
    			String xname="sahilga"+au.getOrgcode().trim();
				xname = URLEncoder.encode(xname,"UTF-8"); 
				
    			if (mergedOutput) {

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
    
}
