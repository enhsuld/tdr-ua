package com.netgloo.controllers;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.netgloo.service.AuditService;
import com.netgloo.dao.UserDao;
import com.netgloo.models.DataSourceResult;
import com.netgloo.models.FinFormG1;
import com.netgloo.models.FinFormG2;
import com.netgloo.models.LnkAuditOrganization;
import com.netgloo.models.LnkCommentMain;
import com.netgloo.models.LnkConfLog;
import com.netgloo.models.LnkDirectionNotice;
import com.netgloo.models.LnkFeedbackCategory;
import com.netgloo.models.LnkMainFormT2;
import com.netgloo.models.LnkMainTransition;
import com.netgloo.models.LnkMainUser;
import com.netgloo.models.LnkRiskLaw;
import com.netgloo.models.LnkRiskT2;
import com.netgloo.models.LnkRiskTryout;
import com.netgloo.models.LnkRiskdir;
import com.netgloo.models.LnkTryoutConfMethod;
import com.netgloo.models.LnkTryoutConfSource;
import com.netgloo.models.LnkTryoutConfType;
import com.netgloo.models.LnkTryoutFile;
import com.netgloo.models.LnkTryoutRisk;
import com.netgloo.models.LutAuditDir;
import com.netgloo.models.LutAuditLevel;
import com.netgloo.models.LutFactor;
import com.netgloo.models.LutFactorCriterion;
import com.netgloo.models.LutFormB1;
import com.netgloo.models.LutGroupOfFactor;
import com.netgloo.models.LutLaw;
import com.netgloo.models.LutPosition;
import com.netgloo.models.LutQuataDecision;
import com.netgloo.models.LutReason;
import com.netgloo.models.LutRisk;
import com.netgloo.models.LutRole;
import com.netgloo.models.LutTryout;
import com.netgloo.models.LutUser;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.models.PreAuditRegistration;
import com.netgloo.models.SourceData;
import com.netgloo.models.SubAuditOrganization;
import com.netgloo.models.LutAuditYear;
import com.netgloo.models.LutCategory;
import com.netgloo.repo.MainRegistrationRepository;

@RestController
@RequestMapping("/au")
public class AuditController {

	@Autowired
    private UserDao dao;
	
	@Autowired
	MainRegistrationRepository mrepo;
	
	@Autowired
	private AuditService aservice;

	@RequestMapping(path="/audits", method=RequestMethod.GET)
	public List<MainAuditRegistration> getAllEmployees(){
		System.out.println("asdasd"+aservice.getAuditById(2844).getOrgcode());
		return aservice.getAllAudits();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String get(@RequestParam("username") String username) {
		
		List<LutAuditYear> ye = (List<LutAuditYear>) dao.getHQLResult("from LutAuditYear t where t.isactive=1", "list");
		List<LutUser> user = (List<LutUser>) dao.getHQLResult("from LutUser t where t.isactive=1 and t.username='BatsukhTs'", "list");
		List<Object[]> cc = (List<Object[]>) dao.getHQLResult("select t.id from MainAuditRegistration t left join t.lnkMainUsers l  where t.audityear="+ye.get(0).getAudityear()+" and t.id=l.appid and t.isactive=1 and t.isenabled=1  and t.stepid>2 and l.userid='"+user.get(0).getId()+"'", "list");
		String ids="";
		for(Object item:cc){
			if(ids.length()>0){
				ids=ids+","+item;
			}
			else{
				ids=String.valueOf(item);
			}
		}
		
		JSONObject str= new JSONObject();
		
		str.put("custom", " where id in ("+ids.substring(0, ids.length())+")");
		
		//str.put("custom", " where id in ("+ids.substring(0, ids.length())+") and isactive=1 and isenabled=1 and stepid>2");
		//str.put("custom", " , LnkMainUser l where t.id = l.appid and l.userid="+str.getString("userid")+" and t.isactive=1 and t.isenabled=1 and t.stepid>2");
		Long count=(long) 0;
		List<MainAuditRegistration> rs = null;		
		rs= (List<MainAuditRegistration>) dao.getHQLResult("from MainAuditRegistration t where t.id in("+ids+") order by t.id asc", "list");
		
		JSONArray arr= new JSONArray();
		
		if(rs.size()>0){
			for(int i=0;i<rs.size();i++){
				MainAuditRegistration or=(MainAuditRegistration) rs.get(i);
				/*if(or.getLnkMainUsers().size()>0){
					for(int j=0;j<or.getLnkMainUsers().size();j++){
						if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("4") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("2")  || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("1")){
							terguuleh=terguuleh+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
						}
						else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("7") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("6") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
							if(or.getLnkMainUsers().get(j).getLutUser().getFamilyname()!=null){
								auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else{
								auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							
						}
						else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
							checkers=checkers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
						}
						else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("5")){
							managers=managers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
						}
						
					}
				}*/
				
				JSONArray lusers= new JSONArray();
				for(LnkMainUser item: or.getLnkMainUsers()){
					JSONObject obj =new JSONObject();
					obj.put("id", or.getId());
					obj.put("username", item.getLutUser().getUsername());
					obj.put("password", item.getLutUser().getPassword());
					obj.put("givenName", item.getLutUser().getGivenname());
					obj.put("familyName", item.getLutUser().getFamilyname());
					obj.put("position", item.getLutUser().getPositionid());
					lusers.put(obj);
				}
				
				
				JSONObject obj =new JSONObject();
				obj.put("id", or.getId());
				obj.put("autype", or.getAutype());
				obj.put("depid", or.getDepid());
				obj.put("gencode", or.getGencode());
				obj.put("auditname", or.getAuditname());
				obj.put("regnum", or.getOrgid());
				obj.put("orgcode", or.getOrgcode());
				obj.put("orgname", or.getOrgname());
				obj.put("orgtype", or.getOrgtype());
				obj.put("lnkUsers", lusers);
				arr.put(obj);
			}
		}	
		
		return arr.toString();
	}
	
	
/*    @Autowired
    private CamService camService;
    
    private final static Logger logger = LoggerFactory.getLogger(AuditController.class);

    @RequestMapping(value = "api/cam", method = RequestMethod.GET, produces = "image/jpeg")
    public @ResponseBody byte[] getFile()  {
        logger.info("Cam API access");
        try {
            return camService.snap();
        } catch (IOException e) {
            logger.error("unable to access webcam", e);
            throw new HttpClientErrorException(HttpStatus.EXPECTATION_FAILED, "error accessing webcam");
        }
    }
    
	@RequestMapping(value="/decode",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String decode(HttpServletRequest req) throws JSONException, NotFoundException, ChecksumException, FormatException{
		String appPath = req.getServletContext().getRealPath(""); 	 
		String filePath = appPath+"/uploads/scr.png";
		int size = 250;
		String fileType = "png";
		File myFile = new File(filePath);
		try {
			
			InputStream barCodeInputStream = new FileInputStream(myFile);
			BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);

			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Reader reader = new MultiFormatReader();
			Result result = reader.decode(bitmap);

			System.out.println("Barcode text is " + result.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nYou have successfully created QR Code.");
		return fileType;
	}
	
	@RequestMapping(value="/qr",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String treatmentqr(HttpServletRequest req) throws JSONException, NotFoundException, ChecksumException, FormatException{
		String appPath = req.getServletContext().getRealPath(""); 	 
		String myCodeText = "baatarbal";
		String filePath = appPath+"/uploads/bal.png";
		int size = 250;
		String fileType = "png";
		File myFile = new File(filePath);
		try {
			
			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			
			// Now with zxing version 3.2.1 you could change border size (white border size to just 1)
			hintMap.put(EncodeHintType.MARGIN, 1);  default = 4 
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
					size, hintMap);
			int CrunchifyWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
					BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			ImageIO.write(image, fileType, myFile);
			
			InputStream barCodeInputStream = new FileInputStream(myFile);
			BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);

			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Reader reader = new MultiFormatReader();
			Result result = reader.decode(bitmap);

			System.out.println("Barcode text is " + result.getText());
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nYou have successfully created QR Code.");
		return fileType;
	}*/
	
	@RequestMapping(value="/submit/formg1",method=RequestMethod.POST,produces={"application/json; charset=UTF-8"})
	public Boolean submitformg1(@RequestBody String jsonString) throws JSONException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		JSONObject jsonObj = new JSONObject(jsonString);
		if (jsonObj.has("mid") && !jsonObj.isNull("mid")){
			Boolean isNew = false;
			FinFormG1 obj= (FinFormG1) dao.getHQLResult("from FinFormG1 t where t.mid='"+jsonObj.getLong("mid")+"'", "current");
			if (obj == null){
				isNew = true;
				obj = new FinFormG1();
			}
			for(int i=1;i<=26;i++){
				for(int j=1;j<=2;j++){
					if (jsonObj.has("row"+i+"_"+j) && !jsonObj.isNull("row"+i+"_"+j)){
						obj.setField("row"+i+"_"+j, jsonObj.getString("row"+i+"_"+j));
					}
					else{
						obj.setField("row"+i+"_"+j, "");
					}
				}
			}
			obj.setMid(jsonObj.getLong("mid"));
			if (isNew){
				dao.PeaceCrud(obj, "FinFormG1", "save", (long) 0, 0, 0, null);
			}
			else{
				dao.PeaceCrud(obj, "FinFormG1", "update", (long) obj.getId(), 0, 0, null);
			}
			return true;
		}
		return false;
	}
	
	@RequestMapping(value="/submit/formg2",method=RequestMethod.POST,produces={"application/json; charset=UTF-8"})
	public Boolean submitformg2(@RequestBody String jsonString) throws JSONException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		JSONObject jsonObj = new JSONObject(jsonString);
		if (jsonObj.has("mid") && !jsonObj.isNull("mid")){
			Boolean isNew = false;
			FinFormG2 obj= (FinFormG2) dao.getHQLResult("from FinFormG2 t where t.mid='"+jsonObj.getLong("mid")+"'", "current");
			MainAuditRegistration mid= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+jsonObj.getLong("mid")+"'", "current");
			if (mid != null){
				if (obj == null){
					isNew = true;
					obj = new FinFormG2();
				}
				for(int i=1;i<=18;i++){
					for(int j=1;j<=3;j++){
						if (jsonObj.has("row"+i+"_"+j) && !jsonObj.isNull("row"+i+"_"+j)){
							obj.setField("row"+i+"_"+j, jsonObj.getString("row"+i+"_"+j));
						}
						else{
							obj.setField("row"+i+"_"+j, "");
						}
					}
				}
				
				
				if (jsonObj.has("reporttype") && !jsonObj.isNull("reporttype")){
					mid.setReporttype(jsonObj.getLong("reporttype"));
					dao.PeaceCrud(mid, "MainAuditRegistration", "update", (long) mid.getId(), 0, 0, null);
				}
				
				obj.setMid(jsonObj.getLong("mid"));
				if (isNew){
					dao.PeaceCrud(obj, "FinFormG2", "save", (long) 0, 0, 0, null);
				}
				else{
					dao.PeaceCrud(obj, "FinFormG2", "update", (long) obj.getId(), 0, 0, null);
				}
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value="/submit/treatment",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String treatment(@RequestBody String jsonString) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 JSONObject obj= new JSONObject(jsonString);
			 LnkRiskTryout lrt= (LnkRiskTryout) dao.getHQLResult("from LnkRiskTryout t where t.id='"+obj.getLong("id")+"'", "current");
			 lrt.setData17(obj.getString("data17"));
			 lrt.setData18(obj.getString("data18"));
			 lrt.setData19(obj.getString("data19"));
			 lrt.setData20(obj.getString("data20"));
			 lrt.setData21(obj.getString("data21"));
			 lrt.setData22(obj.getString("data22"));
			 dao.PeaceCrud(lrt, "LnkRiskTryout", "update", (long) lrt.getId(), 0, 0, null);
			 
			 JSONArray arr= obj.getJSONArray("lnkTryoutFiles");
			 for(int i=0;i<arr.length();i++){
				 JSONObject aobj=(JSONObject) arr.get(i);
				 if(aobj.getInt("id")==0){
					 LnkTryoutFile fl=new LnkTryoutFile();
					 fl.setExtension(aobj.getString("extension"));
					 fl.setFileurl(aobj.getString("fileurl"));
					 fl.setFsize(aobj.getDouble("fsize"));
					 fl.setName(aobj.getString("name"));
					 fl.setLnkRiskTryout(lrt);
					 dao.PeaceCrud(fl, "LnkTryoutFile", "save", (long) 0, 0, 0, null);
				 }
				
			 }
			 
			 return "true";
		}
		else{
			 return "false";
		}
	 	 
	}
	
	@RequestMapping(value = "/guitsetgel", method = RequestMethod.GET)
    public @ResponseBody String orgid() {
		List<Object[]> rs=(List<Object[]>) dao.getHQLResult("select t.id, t.mid, r.id from LnkMainFormT2 t, LnkRiskT2 r where t.id=r.t2id and r.data4>20 and t.rtype=2 and stepid=3 order by t.id", "list");
		for(Object[] it:rs){
			LnkRiskT2 lk=(LnkRiskT2) dao.getHQLResult("from LnkRiskT2 t where t.t2id="+it[0].toString()+" order by t.id", "current");
			List<LnkRiskTryout> lr=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.rt2id="+lk.getId()+" order by t.id", "list");
			System.out.println(it[0].toString());
			System.out.println(it[1].toString());
			if(lr.size()==0){
				LnkRiskTryout lrt=new LnkRiskTryout();
				
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
				lrt.setTryid(lk.getTryid());
				lrt.setRt2id(lk.getId());
				lrt.setRiskid(lk.getRiskid());
				lrt.setDirid(lk.getDirid());
				lrt.setData1(dir.getName());
				lrt.setData2(ris.getRiskname());
				lrt.setData3(String.valueOf(lk.getData1()*lk.getData2()*lk.getData3()));
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
				lrt.setMid(Long.parseLong(it[1].toString()));
				
				dao.PeaceCrud(lrt, "LnkRiskTryout", "save", (long) 0, 0, 0, null);
			}
		};
		return "true";		
	}
	
	@RequestMapping(value = "/remove/treatment/file/{id}", method = RequestMethod.GET)
    public @ResponseBody String removeItem(@PathVariable long id) {
		dao.PeaceCrud(null, "LnkTryoutFile", "delete", (long) id, 0, 0, null);
		return "true";		
	}
	    	
	@RequestMapping(value = "/resource/{domain}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String tree(@PathVariable String domain) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
				
				if(domain.equalsIgnoreCase("LutAuditDir")){
					 List<LutAuditDir> rs=(List<LutAuditDir>) dao.getHQLResult("from LutAuditDir t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				
				if(domain.equalsIgnoreCase("LnkFeedbackCategory")){
					List<LnkFeedbackCategory> rs=(List<LnkFeedbackCategory>) dao.getHQLResult("from LnkFeedbackCategory t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getTitle());		
						 	obj.put("parentid", rs.get(i).getParentid());
			        		arr.put(obj);        	
			        	}
				}
				
				if(domain.equalsIgnoreCase("LnkFeedbackCategoryChilds")){
					List<LnkFeedbackCategory> rs=(List<LnkFeedbackCategory>) dao.getHQLResult("from LnkFeedbackCategory t where t.parentid is not null order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getTitle());		
						 	obj.put("parentid", rs.get(i).getParentid());
			        		arr.put(obj);        	
			        	}
				}
				
				if(domain.equalsIgnoreCase("LnkFeedbackCategoryGroups")){
					List<LnkFeedbackCategory> rs=(List<LnkFeedbackCategory>) dao.getHQLResult("from LnkFeedbackCategory t where t.parentid is null order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getTitle());		
						 	obj.put("parentid", rs.get(i).getParentid());
			        		arr.put(obj);        	
			        	}
				}
				
				if(domain.equalsIgnoreCase("LutFactorCriterion")){
					 List<LutFactorCriterion> rs=(List<LutFactorCriterion>) dao.getHQLResult("from LutFactorCriterion t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("LutRisk")){
					 List<LutRisk> rs=(List<LutRisk>) dao.getHQLResult("from LutRisk t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getRiskname());						 	
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("LutGroupOfFactor")){
					 List<LutGroupOfFactor> rs=(List<LutGroupOfFactor>) dao.getHQLResult("from LutGroupOfFactor t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}	
			
				if(domain.equalsIgnoreCase("LutAuditLevel")){
					 List<LutAuditLevel> rs=(List<LutAuditLevel>) dao.getHQLResult("from LutAuditLevel t order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("id", rs.get(i).getId());
						 	obj.put("name", rs.get(i).getLevelname());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				
				if(domain.equalsIgnoreCase("LutAuditYear")){
					 List<LutAuditYear> rs=(List<LutAuditYear>) dao.getHQLResult("from LutAuditYear t where t.isactive=1  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getAudityear());
						 	obj.put("text", rs.get(i).getAudityear());			        		
			        		arr.put(obj);        	
			        	}		
				}	
				if(domain.equalsIgnoreCase("LutCategory")){
					 List<LutCategory> rs=(List<LutCategory>) dao.getHQLResult("from LutCategory t  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getCategoryname());			        		
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("LutRole")){
					 List<LutRole> rs=(List<LutRole>) dao.getHQLResult("from LutRole t  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getRolename());			        		
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("LutPosition")){
					 List<LutPosition> rs=(List<LutPosition>) dao.getHQLResult("from LutPosition t  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getPositionname());			        		
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("LutReason")){
					 List<LutReason> rs=(List<LutReason>) dao.getHQLResult("from LutReason t  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
			        		arr.put(obj);        	
			        	}		
				}
				if(domain.equalsIgnoreCase("LutQuataDecision")){
					 List<LutQuataDecision> rs=(List<LutQuataDecision>) dao.getHQLResult("from LutQuataDecision t  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getName());			        		
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
	
	@RequestMapping(value = "/lutfactors", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String lutfactors(@RequestBody String jsonstr) {
		JSONObject jsonobj = new JSONObject(jsonstr);
		JSONArray arr=new JSONArray();
		if (jsonobj.has("adirid") && !jsonobj.isNull("adirid") && jsonobj.has("riskid") && !jsonobj.isNull("riskid") && jsonobj.has("tryoutid") && !jsonobj.isNull("tryoutid")){
			List<LutFactor> rs=(List<LutFactor>) dao.getHQLResult("from LutFactor where dirid = " + jsonobj.getLong("adirid") + " and riskid = " + jsonobj.getLong("riskid") + " and tryid = " + jsonobj.getLong("tryoutid") + " order by id", "list");
			 for(int i=0;i<rs.size();i++){
			 	JSONObject obj=new JSONObject();      
			 	obj.put("value", rs.get(i).getId());
				obj.put("text", rs.get(i).getFactorname());
	       		arr.put(obj);        	
	       	}   
		}
		
		return arr.toString();
	}
	
	
	@RequestMapping(value = "/withid/{domain}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String withid(@PathVariable String domain,@PathVariable long id, Principal pr) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				if(domain.equalsIgnoreCase("PreAuditRegistration")){
					PreAuditRegistration rs=(PreAuditRegistration) dao.getHQLResult("from PreAuditRegistration t where t.id="+id+"  order by t.id", "current");
					JSONObject obj=new JSONObject();      	
				 	obj.put("year", rs.getAudityear());
				 	obj.put("orgname", rs.getOrgname());	
				 	obj.put("step", rs.getStepid());	
				 	obj.put("catname", rs.getLutCategory().getCategoryname()); 
	        		arr.put(obj);     
				}
				if(domain.equalsIgnoreCase("MainAuditRegistration")){
					
					UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Collection<?> coll=userDetail.getAuthorities();
					Iterator<?> itr=coll.iterator();
					LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+pr.getName()+"'", "current");
					
					while(itr.hasNext()){
						String rolename = itr.next().toString();	
						System.out.println("rolename"+rolename);
						if(loguser.getDepartmentid()==2 || loguser.getDepartmentid()==4 || loguser.getDepartmentid()==6){
							MainAuditRegistration rs=(MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+id+"  order by t.id", "current");
							JSONObject obj=new JSONObject();      	
						 	obj.put("year", rs.getAudityear());	
						 	obj.put("autype", rs.getAutype()); 
						 	obj.put("stepid", rs.getStepid()); 
						 	obj.put("gencode", rs.getGencode()); 
						 	obj.put("orgtype", rs.getOrgtype()); 
						 	obj.put("orgname", rs.getOrgname()); 
						 	obj.put("aper", rs.getAper()); 
						 	obj.put("a2per", rs.getA2per()); 
						 	obj.put("a3per", rs.getA3per()); 
						 	obj.put("mper", rs.getMper()); 
						 	obj.put("m2per", rs.getM2per()); 
						 	obj.put("m3per", rs.getM3per()); 
						 	obj.put("tper", rs.getTper()); 
						 	obj.put("t2per", rs.getT2per()); 
						 	obj.put("t3per", rs.getT3per());  
						 	obj.put("reporttype", rs.getReporttype());
						 	obj.put("orgcode", rs.getOrgcode());
			        		arr.put(obj); 
			        		return arr.toString();
						}
						else if(rolename.equalsIgnoreCase("ROLE_FIRST") || rolename.equalsIgnoreCase("ROLE_ALL")  || rolename.toUpperCase().equalsIgnoreCase("ROLE_CHECK")){
							System.out.println("bbb"+rolename);
							MainAuditRegistration rs=(MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+id+"  order by t.id", "current");
							JSONObject obj=new JSONObject();      	
						 	obj.put("year", rs.getAudityear());	
						 	obj.put("autype", rs.getAutype()); 
						 	obj.put("stepid", rs.getStepid()); 
						 	obj.put("gencode", rs.getGencode()); 
						 	obj.put("orgtype", rs.getOrgtype()); 
						 	obj.put("orgname", rs.getOrgname()); 
						 	obj.put("aper", rs.getAper()); 
						 	obj.put("a2per", rs.getA2per()); 
						 	obj.put("a3per", rs.getA3per()); 
						 	obj.put("mper", rs.getMper()); 
						 	obj.put("m2per", rs.getM2per()); 
						 	obj.put("m3per", rs.getM3per()); 
						 	obj.put("tper", rs.getTper()); 
						 	obj.put("t2per", rs.getT2per()); 
						 	obj.put("t3per", rs.getT3per());  
						 	obj.put("reporttype", rs.getReporttype());
						 	obj.put("orgcode", rs.getOrgcode());
			        		arr.put(obj); 
			        		return arr.toString();
						}
						else if(!rolename.equalsIgnoreCase("ROLE_FIRST")){  
							System.out.println("aaa"+rolename);
						   
						    List<LnkMainUser> us=(List<LnkMainUser>) dao.getHQLResult("from LnkMainUser t where t.appid="+id+" and userid="+loguser.getId()+"  order by t.id", "list");
						    if(us.size()>0){
						    	MainAuditRegistration rs=(MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+id+"  order by t.id", "current");
								JSONObject obj=new JSONObject();      	
							 	obj.put("year", rs.getAudityear());	
							 	obj.put("autype", rs.getAutype()); 
							 	obj.put("stepid", rs.getStepid()); 
							 	obj.put("gencode", rs.getGencode()); 
							 	obj.put("orgtype", rs.getOrgtype()); 
							 	obj.put("orgname", rs.getOrgname()); 
							 	obj.put("aper", rs.getAper()); 
							 	obj.put("a2per", rs.getA2per()); 
							 	obj.put("a3per", rs.getA3per()); 
							 	obj.put("mper", rs.getMper()); 
							 	obj.put("m2per", rs.getM2per()); 
							 	obj.put("m3per", rs.getM3per()); 
							 	obj.put("tper", rs.getTper()); 
							 	obj.put("t2per", rs.getT2per()); 
							 	obj.put("t3per", rs.getT3per());  
							 	obj.put("reporttype", rs.getReporttype());
							 	obj.put("orgcode", rs.getOrgcode());
				        		arr.put(obj); 
				        		return arr.toString();
						    }	
						    else{
						    	return "false";
						    }
						}	
					}
					    
				}
				if(domain.equalsIgnoreCase("orgdata")){
					MainAuditRegistration rs=(MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+id+"  order by t.id", "current");
					JSONObject obj=new JSONObject();			 	
				 	obj.put("orgname", rs.getOrgname());	
				 	obj.put("code", rs.getGencode());	
				 	obj.put("otype", rs.getOrgtype());	
				 	//obj.put("type", rs.getOrgtype()); 
	        		//arr.put(obj);     
	        		return obj.toString();
				}
				if(domain.equalsIgnoreCase("LnkCommentPre")){
					 List<LnkCommentMain> rs=(List<LnkCommentMain>) dao.getHQLResult("from LnkCommentMain t  where t.preid="+id+" order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("date", rs.get(i).getCdate());
						 	obj.put("text", rs.get(i).getMcomment());
						 	obj.put("name", rs.get(i).getUsername());						 	
			        		arr.put(obj);        	
			        	}   
				}
				if(domain.equalsIgnoreCase("LnkCommentMain")){
					 List<LnkCommentMain> rs=(List<LnkCommentMain>) dao.getHQLResult("from LnkCommentMain t  where t.mainid="+id+" order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("date", rs.get(i).getCdate());
						 	obj.put("text", rs.get(i).getMcomment());
						 	obj.put("name", rs.get(i).getUsername());						 	
			        		arr.put(obj);        	
			        	}   
				}
				if(domain.equalsIgnoreCase("workdata")){
					 List<LnkMainTransition> rs=(List<LnkMainTransition>) dao.getHQLResult("from LnkMainTransition t  where t.mid="+id+" order by t.id desc", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      	
						 	obj.put("id", rs.get(i).getId());
						 	obj.put("mid", rs.get(i).getMid());
						 	obj.put("wid", rs.get(i).getWid());
							obj.put("stepid", rs.get(i).getStepid());
						 	obj.put("levelid", rs.get(i).getLevelid());
			        		arr.put(obj);        	
			        	}   
				}
				if(domain.equalsIgnoreCase("LnkRiskdir")){
					 List<LnkRiskdir> rs=(List<LnkRiskdir>) dao.getHQLResult("from LnkRiskdir t  where t.dirid="+id+" order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();      
						 	if(rs.get(i).getLutRisk()!=null){
						 		obj.put("value", rs.get(i).getLutRisk().getId());
							 	obj.put("text", rs.get(i).getLutRisk().getRiskname());
						 	}
			        		arr.put(obj);        	
			        	}   
				}
				if(domain.equalsIgnoreCase("LnkTryoutRisk")){
					 List<LnkTryoutRisk> rs=(List<LnkTryoutRisk>) dao.getHQLResult("from LnkTryoutRisk t  where t.riskid="+id+" order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();  
						 	if(rs.get(i).getLutTryout()!=null){
							 	obj.put("value", rs.get(i).getLutTryout().getId());
							 	obj.put("text", rs.get(i).getLutTryout().getFormdesc());
				        		arr.put(obj);    
						 	}
			        	}   
				}
				if(domain.equalsIgnoreCase("LnkDirectionNoticeProcedureRisk")){
					 List<LnkDirectionNotice> rs=(List<LnkDirectionNotice>) dao.getHQLResult("from LnkDirectionNotice t  where t.directionid="+id+" order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();  
						 	if(rs.get(i).getLutNotice()!=null){
							 	obj.put("value", rs.get(i).getLutNotice().getId());
							 	obj.put("text", rs.get(i).getLutNotice().getName());
				        		arr.put(obj);    
						 	}
			        	}   
				}
				if(domain.equalsIgnoreCase("LutLaw")){
					 List<LutLaw> rs=(List<LutLaw>) dao.getHQLResult("from LutLaw t  where t.parentid='"+id+"' order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
						 	JSONObject obj=new JSONObject();  
						 	obj.put("value", rs.get(i).getId());
						 	obj.put("text", rs.get(i).getLawname());
						 	obj.put("zaalt", rs.get(i).getZaalt());
			        		arr.put(obj);    
			        	}   
				}
				else if (domain.equalsIgnoreCase("LnkMainFormT2")){
					List<LnkMainFormT2> rs=(List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t  where t.parentid='"+id+"' order by t.id", "list");
				}				
			}
			return arr.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/withids/{domain}/{fid}/{lid}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String withids(@PathVariable String domain,@PathVariable long fid,@PathVariable long lid) {
		try{
				
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			JSONArray arr=new JSONArray();
				
				if(domain.equalsIgnoreCase("LnkTryoutRisk")){
					List<LnkTryoutRisk> rs=(List<LnkTryoutRisk>) dao.getHQLResult("from LnkTryoutRisk t where t.dirid="+fid+" and t.riskid="+lid+"  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
					 	JSONObject obj=new JSONObject();      	
						if(rs.get(i).getLutTryout()!=null){
						 	obj.put("value", rs.get(i).getLutTryout().getId());
						 	obj.put("text", rs.get(i).getLutTryout().getFormdesc());
			        		arr.put(obj);    
					 	}       	
		        	}     
				}	
				if(domain.equalsIgnoreCase("LutFactor")){
					List<LutFactor> rs=(List<LutFactor>) dao.getHQLResult("from LutFactor t where t.tryid="+lid+" and t.riskid="+fid+"  order by t.id", "list");
					 for(int i=0;i<rs.size();i++){
					 	JSONObject obj=new JSONObject();      	
					 	obj.put("value", rs.get(i).getId());
					 	obj.put("text", rs.get(i).getFactorname());
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
	
	@RequestMapping(value = "/list/{domain}", method= RequestMethod.POST)
    public @ResponseBody DataSourceResult customers(@PathVariable String domain, @RequestBody String request, HttpServletRequest req) throws HttpRequestMethodNotSupportedException {
		Long count=(long) 0;
		List<?> rs = null;		
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy");
	    Date date1 = new Date();
		String special = dateFormat1.format(date1);
		DataSourceResult result = new DataSourceResult();	
			here:
			if(domain.equalsIgnoreCase("LutFormB1")){
				List<LutFormB1> wrap = new ArrayList<LutFormB1>();
				JSONObject str= new JSONObject(request);	
				str.put("field", "orderid");
				str.put("dir", "ASC");
				str.put("custom", "where planid='"+str.getLong("mid")+"' and stepid='"+str.getLong("stepid")+"'");
				rs= dao.kendojson(str.toString(), domain);
				if(rs.size()>0){
					for(int i=0;i<rs.size();i++){
						LutFormB1 or=(LutFormB1) rs.get(i);
						LutFormB1 cor=new LutFormB1();
						cor.setId(or.getId());
						cor.setPlanid(or.getPlanid());
						cor.setStepid(or.getStepid());
						cor.setCyear(or.getCyear());
						cor.setOrgcode(or.getOrgcode());
						cor.setGencode(or.getGencode());
						cor.setOrderid(or.getOrderid());
						cor.setData1(or.getData1());
						cor.setData2(or.getData2());
						cor.setData3(or.getData3());
						cor.setData4(or.getData4());
						cor.setData5(or.getData5());
						wrap.add(cor);
					}
				}
				else{
					dao.getNativeSQLResult("DELETE FROM LUT_FORM_B1 WHERE PLANID='" + str.getLong("mid")+ "' and STEPID='"+str.getLong("stepid")+"'", "delete");
					MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+str.getLong("mid")+"'", "current");
					List<SourceData> fs=  (List<SourceData>) dao.getHQLResult("from SourceData t where t.formname='"+str.getString("sheetname")+"'", "list");
					List<LutFormB1> datas = new ArrayList<LutFormB1>(); 
					for(int k=0;k<fs.size();k++){
						LutFormB1 form = new LutFormB1();
						form.setCyear(main.getAudityear());
						form.setPlanid(main.getId());
						form.setStepid(str.getLong("stepid"));
						form.setOrgcode(main.getOrgcode());
						form.setGencode(main.getGencode());
						form.setOrderid(k+1);
						form.setData1(fs.get(k).getText());
						datas.add(form);
					}
	                dao.inserBatch(datas,"gLutFormB1"); 
	                
					str.put("field", "orderid");
					str.put("dir", "ASC");
					str.put("custom", "where planid='"+str.getLong("mid")+"' and stepid='"+str.getLong("stepid")+"'");
					rs= dao.kendojson(str.toString(), domain);
					
					for(int i=0;i<rs.size();i++){
						LutFormB1 or=(LutFormB1) rs.get(i);
						LutFormB1 cor=new LutFormB1();
						cor.setId(or.getId());
						cor.setPlanid(or.getPlanid());
						cor.setStepid(or.getStepid());
						cor.setCyear(or.getCyear());
						cor.setOrgcode(or.getOrgcode());
						cor.setGencode(or.getGencode());
						cor.setOrderid(or.getOrderid());
						cor.setData1(or.getData1());
						cor.setData2(or.getData2());
						cor.setData3(or.getData3());
						cor.setData4(or.getData4());
						cor.setData5(or.getData5());
						wrap.add(cor);
					}
					
				}
				
				result.setData(wrap);					
				result.setTotal(wrap.size());
			}
			else if(domain.equalsIgnoreCase("LutFactor")){
				List<LutFactor> wrap = new ArrayList<LutFactor>();
				JSONObject str= new JSONObject(request);
				System.out.println("eee"+str.toString());
				
			/*	JSONArray arro = new JSONArray();
				JSONObject jobj= new JSONObject();	
				jobj.append("field", "gorderid");
				jobj.append("dir", "desc");
				jobj.
				arro.put(jobj);
				str.put("sort", arro);*/
				//str.put("sort", [{field: gorderid, dir: desc}]);
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				List<LnkMainFormT2> lm=(List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.mid="+str.getString("mid")+" order by t.id", "list");
				for(int i=0;i<rs.size();i++){
					LutFactor or=(LutFactor) rs.get(i);
					LutFactor cor=new LutFactor();
					
					cor.setId(or.getId());
					cor.setFactorname(or.getFactorname());
					//cor.setDecid(or.getDecid());
					cor.setDirid(or.getDirid());
					cor.setDescription(or.getDescription());
					cor.setFnumber(or.getFnumber());
					cor.setGorderid(or.getGorderid());
					cor.setOrderid(or.getOrderid());
					cor.setRiskid(or.getRiskid());
					cor.setRisknames("");
					cor.setGroupid(or.getGroupid());
					cor.setCriname(or.getCrition());
					
					cor.setDirname("");
					for(int c=0;c<lm.size();c++){
						if(lm.get(c).getFactorid()==or.getId()){
							 LnkMainFormT2 tt=lm.get(c);
							cor.setId(or.getId());						
							cor.setDecid(tt.getDecid());
							cor.setDescription(tt.getDescription());
						}
					 }				
					
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}  
		
			else if(domain.equalsIgnoreCase("LutFactorCustom")){
				domain="LutFactor";
				List<LutFactor> wrap = new ArrayList<LutFactor>();
				JSONObject str= new JSONObject(request);
				System.out.println("eee"+str.toString());
				
			//	rs= dao.kendojson(request, domain);
			//	count=(long) dao.resulsetcount(request, domain);
				
				List<LnkMainFormT2> lm=(List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.mid="+str.getString("mid")+" and t.stepid="+str.getString("stepid")+" and t.rtype=1 order by t.groupid", "list");
				for(int i=0;i<lm.size();i++){
					LutFactor or=(LutFactor) lm.get(i).getLutFactor();
					LutFactor cor=new LutFactor();
					
					cor.setId(or.getId());
					cor.setFactorname(or.getFactorname());
					cor.setDecid(or.getDecid());
					cor.setDirid(or.getDirid());
					cor.setDescription(lm.get(i).getDescription());
					cor.setFnumber(or.getFnumber());
					cor.setGorderid(or.getGorderid());
					cor.setRiskid(or.getRiskid());
					cor.setRisknames("");
				
					//cor.setGroupid(or.getGroupid());
					cor.setCriname(or.getCrition());
				    cor.setGroupid(lm.get(i).getGroupid());
					
					cor.setDirname("");
					for(int c=0;c<lm.size();c++){
						if(lm.get(c).getFactorid()==or.getId()){
							LnkMainFormT2 tt=lm.get(c);
							cor.setId(or.getId());						
							cor.setDecid(tt.getDecid());
							cor.setRtype(tt.getRtype());
							//cor.set
							for(LnkRiskT2 t2: tt.getLnkRiskT2s()){
								LutTryout tr= (LutTryout) dao.getHQLResult("from LutTryout t where t.id="+t2.getTryid()+" order by t.id", "current");
								cor.setTreatment(tr.getText());
							}
						//	cor.setDescription(tt.getDescription());
							//cor.setGroupid(tt.getGroupid());
						}
					 }
					String crition="";
					if(or.getLutFactorCriterions().size()>0){
						for(int c=0;c<or.getLutFactorCriterions().size();c++){
							crition=crition+" , "+or.getLutFactorCriterions().get(c).getName();
						}
						cor.setCrition(crition.substring(3, crition.length()));
					}					
					
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(lm.size());
			}
			else if(domain.equalsIgnoreCase("LnkMainFormT2")){
				List<LnkRiskT2> wrap = new ArrayList<LnkRiskT2>();
				
				//rs= dao.kendojson(request, domain);
				
				JSONObject str= new JSONObject(request);	
				//str.put("custom", "where mid='"+str.getLong("mid")+"'");
				rs= dao.kendojson(str.toString(), domain);
				count=(long) dao.resulsetcount(str.toString(), domain);
				for(int i=0;i<rs.size();i++){					
					LnkMainFormT2 or=(LnkMainFormT2) rs.get(i);
					if(or.getLnkRiskT2s().size()>0 && or.getDecid()==1){
						for(int y=0;y<or.getLnkRiskT2s().size();y++){
							LnkRiskT2 cor=new LnkRiskT2();
							cor.setId(or.getLnkRiskT2s().get(y).getId());
							cor.setDirid(or.getDirid());		
							cor.setRtype(or.getLnkRiskT2s().get(y).getRtype());
							cor.setDirname(or.getLutAuditDir().getName());
							cor.setRiskid(or.getLnkRiskT2s().get(y).getLutRisk().getId());
							cor.setRiskname(or.getLnkRiskT2s().get(y).getLutRisk().getRiskname());	
							cor.setData1(or.getLnkRiskT2s().get(y).getData1());
							cor.setData2(or.getLnkRiskT2s().get(y).getData2());
							cor.setData3(or.getLnkRiskT2s().get(y).getData3());
							cor.setData4(or.getLnkRiskT2s().get(y).getData4());
							cor.setData5(or.getLnkRiskT2s().get(y).isData5());
							cor.setData6(or.getLnkRiskT2s().get(y).isData6());
							cor.setData7(or.getLnkRiskT2s().get(y).isData7());
							cor.setT2id(or.getLnkRiskT2s().get(y).getT2id());
							cor.setDescription(or.getLnkRiskT2s().get(y).getDescription());
							wrap.add(cor);
						}
					}					
				}
				result.setData(wrap);					
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutAuditYear")){
				List<LutAuditYear> wrap = new ArrayList<LutAuditYear>();
				
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutAuditYear or=(LutAuditYear) rs.get(i);
					LutAuditYear cor=new LutAuditYear();
					cor.setId(or.getId());
					cor.setAudityear(or.getAudityear());
					cor.setIsactive(or.getIsactive());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			} 
			else if(domain.equalsIgnoreCase("LutAuditDir")){
				List<LutAuditDir> wrap = new ArrayList<LutAuditDir>();
				
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LutAuditDir or=(LutAuditDir) rs.get(i);
					LutAuditDir cor=new LutAuditDir();
					cor.setId(or.getId());
					cor.setName(or.getName());
					cor.setShortname(or.getShortname());
					
					//cor.setOrgcode(or.getOrgcode());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LnkAuditOrganization")){
				List<LnkAuditOrganization> wrap = new ArrayList<LnkAuditOrganization>();
				
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
								
				for(int i=0;i<rs.size();i++){
					LnkAuditOrganization or=(LnkAuditOrganization) rs.get(i);
					LnkAuditOrganization cor=new LnkAuditOrganization();
					cor.setId(or.getId());
					cor.setAyear(or.getAyear());
					cor.setOrgcode(or.getOrgcode());
					cor.setOrgname(or.getOrgname());
					cor.setQuataid(or.getQuataid());
					cor.setPreid(or.getPreid());
					cor.setReasonid(or.getReasonid());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("MainAuditRegistrationAu")){
				List<MainAuditRegistration> wrap = new ArrayList<MainAuditRegistration>();
				
				domain="MainAuditRegistration";				
				JSONObject str= new JSONObject(request);
				List<LutAuditYear> ye = (List<LutAuditYear>) dao.getHQLResult("from LutAuditYear t where t.isactive=1", "list");
				//List<Object[]> cr=  (List<Object[]>) dao.getHQLResult("select t.appid, m.gencode from LnkMainUser t, MainAuditRegistration m where m.id=t.appid and m.isactive=1 and m.isenabled=1 and t.userid='"+str.getString("userid")+"'", "list");
				List<Object[]> cc=  (List<Object[]>) dao.getHQLResult("select t.id from  MainAuditRegistration t left join t.lnkMainUsers l  where t.audityear="+ye.get(0).getAudityear()+" and t.id=l.appid and t.isactive=1 and t.isenabled=1  and t.stepid>2 and l.userid='"+str.getInt("userid")+"' group by t.id", "list");
				
				if(cc.size()>0 && str.getInt("depid")!=2){
					//ArrayList<Long> arr = (ArrayList<Long>) cr;
					
					ArrayList<Object[]> arr = (ArrayList<Object[]>) cc;
					String ids="";
					for(Object item:cc){
						if(ids.length()>0){
							ids=ids+","+item;
						}
						else{
							ids=String.valueOf(item);
						}
					}
					
					System.out.println("@@ size "+cc.size());
					System.out.println("@@"+ids);
					System.out.println("@@"+cc.toString());
					if(str.has("isarchive")){
						if(str.getBoolean("isarchive") && ye.size()>0){
							str.put("custom", " where id in ("+ids.substring(0, ids.length())+") and isactive=1 and audityear<"+ye.get(0).getAudityear()+" and isenabled=1 and stepid>2");
						}
						else{
							str.put("custom", " where id in ("+ids.substring(0, ids.length())+") and isactive=1 and isenabled=1 and stepid>2");
						}
					}
					else{
						str.put("custom", " where id in ("+ids.substring(0, ids.length())+") and isactive=1 and audityear="+ye.get(0).getAudityear()+" and isenabled=1 and stepid>2");
					}
					
					//str.put("custom", " where id in ("+ids.substring(0, ids.length())+") and isactive=1 and isenabled=1 and stepid>2");
					//str.put("custom", " , LnkMainUser l where t.id = l.appid and l.userid="+str.getString("userid")+" and t.isactive=1 and t.isenabled=1 and t.stepid>2");
				
					rs= dao.kendojson(str.toString(), domain);
					count=(long) dao.resulsetcount(str.toString(), domain);
					if(rs.size()>0){
						for(int i=0;i<rs.size();i++){
							MainAuditRegistration or=(MainAuditRegistration) rs.get(i);
							MainAuditRegistration cor=new MainAuditRegistration();
							String terguuleh="";
							String auditors="";
							String checkers="";
							String managers="";
							
							if(or.getLnkMainUsers().size()>0){
								for(int j=0;j<or.getLnkMainUsers().size();j++){
									if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("4") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("2")  || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("1")){
										terguuleh=terguuleh+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
									}
									else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("7") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("6") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
										if(or.getLnkMainUsers().get(j).getLutUser().getFamilyname()!=null){
											auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
										}
										else{
											auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
										}
										
									}
									else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
										checkers=checkers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
									}
									else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("5")){
										managers=managers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
									}
									
								}
							}
							if(or.getIsactive()){
								for(int y=0;y<or.getLnkMainUsers().size();y++){
									if(or.getLnkMainUsers().get(y).getUserid()==str.getLong("userid")){
										cor.setId(or.getId());					
										cor.setGencode(or.getGencode());
										cor.setOrgtype(or.getOrgtype());
										cor.setStepid(or.getStepid());
										cor.setOrgname(or.getOrgname());
										cor.setIsactive(or.getIsactive());
										cor.setStartdate(or.getStartdate());
										cor.setEnddate(or.getEnddate());
										cor.setAper(or.getAper());
										cor.setA2per(or.getA2per());
										cor.setA3per(or.getA3per());
										cor.setMper(or.getMper());
										cor.setM2per(or.getM2per());
										cor.setM3per(or.getM3per());
										cor.setTper(or.getTper());
										cor.setT2per(or.getT2per());
										cor.setT3per(or.getT3per());
										if(terguuleh.length()>0){
											cor.setTerguuleh(terguuleh.substring(0, terguuleh.length()-2));
										}
										if(auditors.length()>0){
											cor.setAuditors(auditors.substring(0, auditors.length()-2));
										}
										if(checkers.length()>0){
											cor.setCheckers(checkers.substring(0, checkers.length()-2));
										}
										if(managers.length()>0){
											cor.setManagers(managers.substring(0, managers.length()-2));
										}
										cor.setAutype(or.getAutype());
										cor.setDepid(or.getDepid());
										cor.setAudityear(or.getAudityear());						
										wrap.add(cor);
									}
								}
							}
							
						}
					}		
					result.setData(wrap);	
					result.setTotal(count);
				}	
				else{
					UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
					if(loguser.getPositionid().equalsIgnoreCase("1") || loguser.getPositionid().equalsIgnoreCase("2")){
						str.put("custom", "where  isactive=1 and isenabled=1  and stepid>2 ");
						rs= dao.kendojson(str.toString(), domain);
						count=(long) dao.resulsetcount(str.toString(), domain);
						if(rs.size()>0){
							for(int i=0;i<rs.size();i++){
								MainAuditRegistration or=(MainAuditRegistration) rs.get(i);
								MainAuditRegistration cor=new MainAuditRegistration();
								String terguuleh="";
								String auditors="";
								String checkers="";
								String managers="";
								
								if(or.getLnkMainUsers().size()>0){
									for(int j=0;j<or.getLnkMainUsers().size();j++){
										if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("4") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("2")  || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("1")){
											terguuleh=terguuleh+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
										}
										else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("7") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("6") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
											if(or.getLnkMainUsers().get(j).getLutUser().getFamilyname()!=null){
												auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
											}
											else{
												auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
											}
											
										}
										else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
											checkers=checkers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
										}
										else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("5")){
											managers=managers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
										}
										
									}
								}
								if(or.getIsactive()){
									cor.setId(or.getId());					
									cor.setGencode(or.getGencode());
									cor.setOrgtype(or.getOrgtype());
									cor.setStepid(or.getStepid());
									cor.setOrgname(or.getOrgname());
									cor.setIsactive(or.getIsactive());
									cor.setStartdate(or.getStartdate());
									cor.setEnddate(or.getEnddate());
									cor.setAper(or.getAper());
									cor.setA2per(or.getA2per());
									cor.setA3per(or.getA3per());
									cor.setMper(or.getMper());
									cor.setM2per(or.getM2per());
									cor.setM3per(or.getM3per());
									cor.setTper(or.getTper());
									cor.setT2per(or.getT2per());
									cor.setT3per(or.getT3per());
									if(terguuleh.length()>0){
										cor.setTerguuleh(terguuleh.substring(0, terguuleh.length()-2));
									}
									if(auditors.length()>0){
										cor.setAuditors(auditors.substring(0, auditors.length()-2));
									}
									if(checkers.length()>0){
										cor.setCheckers(checkers.substring(0, checkers.length()-2));
									}
									if(managers.length()>0){
										cor.setManagers(managers.substring(0, managers.length()-2));
									}
									cor.setAutype(or.getAutype());
									cor.setDepid(or.getDepid());
									cor.setAudityear(or.getAudityear());						
									wrap.add(cor);
								}
								
							}
						}		
						result.setData(wrap);	
						result.setTotal(count);
					}					
				}
			}
			else if(domain.equalsIgnoreCase("MainAuditRegistration")){
				List<MainAuditRegistration> wrap = new ArrayList<MainAuditRegistration>();
				
				JSONObject str= new JSONObject(request);
				
				//UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				//LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				
				if(str.getInt("depid")==2){
					str.remove("custom");
					str.put("custom", "where isenabled=1 and isactive=1");
				}
				
				rs= dao.kendojson(str.toString(), domain);
				count=(long) dao.resulsetcount(str.toString(), domain);
								
				for(int i=0;i<rs.size();i++){
					MainAuditRegistration or=(MainAuditRegistration) rs.get(i);
					MainAuditRegistration cor=new MainAuditRegistration();
					
					String terguuleh="";
					String auditors="";
					String checkers="";
					String managers="";
					
					String data1="";
					String data2="";
					String data3="";
					String data4="";
					String data5="";
					String data6="";
					String data7="";
					String data8="";
					
					if(or.getLnkMainUsers().size()>0){
						for(int j=0;j<or.getLnkMainUsers().size();j++){
							if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("4") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("2") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("1")){
								data1=data1+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("5")){
								data2=data2+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("6")){
								data3=data3+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("7")){
								if(or.getLnkMainUsers().get(j).getLutUser().getFamilyname()!=null){
									data4=data4+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
								}
								else{
									data4=data4+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
								}								
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("11")){
								data5=data5+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
								data6=data6+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("13732")){
								data7=data7+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("13732")){
								data7=data7+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							
							/*if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("4") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("2") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("1")){
								terguuleh=terguuleh+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("7") || or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("6")){
								if(or.getLnkMainUsers().get(j).getLutUser().getFamilyname()!=null){
									auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
								}
								else{
									auditors=auditors+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
								}
								
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("8")){
								checkers=checkers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}
							else if(or.getLnkMainUsers().get(j).getLutUser().getPositionid().equalsIgnoreCase("5")){
								managers=managers+or.getLnkMainUsers().get(j).getLutUser().getFamilyname().substring(0, 1)+"."+or.getLnkMainUsers().get(j).getLutUser().getGivenname()+" , ";
							}*/
							
						}
					}
					
					cor.setId(or.getId());
					cor.setGencode(or.getGencode());
					cor.setOrgtype(or.getOrgtype());
					cor.setStepid(or.getStepid());
					cor.setOrgname(or.getOrgname());
					cor.setAutype(or.getAutype());
					cor.setDepid(or.getDepid());
					cor.setOrgid(or.getOrgid());
					cor.setReporttype(or.getReporttype());
					cor.setIsactive(or.getIsactive());
					cor.setEnddate(or.getEnddate());
					cor.setStartdate(or.getStartdate());
					cor.setOrgcode(or.getOrgcode());
					cor.setAper(or.getAper());
					cor.setA2per(or.getA2per());
					cor.setA3per(or.getA3per());
					cor.setMper(or.getMper());
					cor.setM2per(or.getM2per());
					cor.setM3per(or.getM3per());
					cor.setTper(or.getTper());
					cor.setT2per(or.getT2per());
					cor.setT3per(or.getT3per());
					cor.setAuditname(or.getAuditname());
					cor.setIsenabled(or.isIsenabled());
					if(terguuleh.length()>0){
						cor.setTerguuleh(terguuleh.substring(0, terguuleh.length()-2));
					}
					if(auditors.length()>0){
						cor.setAuditors(auditors.substring(0, auditors.length()-2));
					}
					if(checkers.length()>0){
						cor.setCheckers(checkers.substring(0, checkers.length()-2));
					}
					if(managers.length()>0){
						cor.setManagers(managers.substring(0, managers.length()-2));
					}
					if(terguuleh.length()>0){
						cor.setTerguuleh(terguuleh.substring(0, terguuleh.length()-2));
					}
					if(data1.length()>0){
						cor.setData1(data1.substring(0, data1.length()-2));
					}
					if(data2.length()>0){
						cor.setData2(data2.substring(0, data2.length()-2));
					}
					if(data3.length()>0){
						cor.setData3(data3.substring(0, data3.length()-2));
					}
					if(data4.length()>0){
						cor.setData4(data4.substring(0, data4.length()-2));
					}
					if(data5.length()>0){
						cor.setData5(data5.substring(0, data5.length()-2));
					}
					if(data6.length()>0){
						cor.setData6(data6.substring(0, data6.length()-2));
					}
					if(data7.length()>0){
						cor.setData7(data7.substring(0, data7.length()-2));
					}
					cor.setAuditname(or.getAuditname());
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			} 
			else if(domain.equalsIgnoreCase("ConfigUser")){
				List<LutUser> wrap = new ArrayList<LutUser>();
				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				
				domain="LutUser";				
				JSONObject str= new JSONObject(request);
				str.put("custom", "where departmentid="+loguser.getDepartmentid()+" and isstate=1");
				
						
				rs= dao.kendojson(str.toString(), domain);
				count=(long) dao.resulsetcount(str.toString(), domain);
								
				for(int i=0;i<rs.size();i++){
					LutUser or=(LutUser) rs.get(i);
					List<LnkMainUser> cr=  (List<LnkMainUser>) dao.getHQLResult("from LnkMainUser t where t.userid='"+or.getId()+"' and t.appid='"+str.getInt("appid")+"'", "list");
					
					LutUser cor=new LutUser();
					cor.setId(or.getId());
					cor.setGivenname(or.getGivenname());
					cor.setFamilyname(or.getFamilyname());
					cor.setPositionid(or.getPositionid());
					if(cr.size()>0){
						cor.setConfigid("1");
					}
					else{
						cor.setConfigid("0");
					}
					
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("ConfigCom")){
				List<LutUser> wrap = new ArrayList<LutUser>();
				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				domain="LutUser";				
				JSONObject str= new JSONObject(request);
				str.put("custom", "where  departmentid="+loguser.getDepartmentid()+" and isstate!=2 and isactive=1");
				
						
				rs= dao.kendojson(str.toString(), domain);
				count=(long) dao.resulsetcount(str.toString(), domain);
					
				List<Long> cr=  (List<Long>) dao.getHQLResult("select t.userid from LnkMainUser t where t.appid='"+str.getInt("appid")+"'", "list");
				
				for(int i=0;i<rs.size();i++){
					LutUser or=(LutUser) rs.get(i);
					
					
					LutUser cor=new LutUser();
					cor.setId(or.getId());
					cor.setGivenname(or.getGivenname());
					cor.setFamilyname(or.getFamilyname());
					cor.setPositionid(or.getPositionid());					
					if(cr.indexOf(or.getId())>-1){
						cor.setConfigid("1");
					}
					else{
						cor.setConfigid("0");
					}
					
					wrap.add(cor);
				}
				result.setData(wrap);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("PreAuditRegistration")){
				List<PreAuditRegistration> wrap = new ArrayList<PreAuditRegistration>();
				UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
				System.out.println("ssssssssssss"+loguser.getLnkUserroles().get(0).getLutRole().getRoleauth());
				domain="PreAuditRegistration";				
				JSONObject str= new JSONObject(request);
				if(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_FIRST") || loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_SECOND")){
					str.put("custom", "where depid="+loguser.getDepartmentid()+"");
				}
				rs= dao.kendojson(str.toString(), domain);
				count=(long) dao.resulsetcount(str.toString(), domain);
				
				result.setData(rs);	
				result.setTotal(count);
			}
			else if(domain.equalsIgnoreCase("LutFlashNews")){
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				result.setData(rs);	
				result.setTotal(count);
			}
			else {
				rs= dao.kendojson(request, domain);
				count=(long) dao.resulsetcount(request, domain);
				
				result.setData(rs);	
				result.setTotal((long) count);
			}
			return  result;

	}
	
	@RequestMapping(value="/distribute/{stepid}/{id}",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String distribute(@PathVariable long stepid,@PathVariable long id) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 
			 
			 MainAuditRegistration ma=(MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id="+id+"", "current");
			 
			 if(stepid==4){
				 ma.setStepid(2);
				 dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) 0, 0, 0, null);
			 }
			 else{
				 ma.setIsactive(true);
				 ma.setStepid(stepid+1);
				 dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) 0, 0, 0, null);
			 }		 	
			 
			 return "true";
		}
		else{
			 return "false";
		}
	 	 
	 }
	
	@RequestMapping(value="/update/quata/{id}",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String updateapp(@PathVariable long id) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 
			 PreAuditRegistration rs= (PreAuditRegistration) dao.getHQLResult("from PreAuditRegistration t where t.id='"+id+"'  order by t.id", "current");
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			
			 System.out.println(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth());
			 
			 if(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_FIRST")){
				 rs.setStepid(2);
				 dao.PeaceCrud(rs, "PreAuditRegistration", "update", (long) rs.getId(), 0, 0, null);		
			 }
			 else if(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_SECOND")){
				 rs.setStepid(3);
				 dao.PeaceCrud(rs, "PreAuditRegistration", "update", (long) rs.getId(), 0, 0, null);	
			 }
		 	 else if(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_ALL")){
		 		 rs.setStepid(4);
				 dao.PeaceCrud(rs, "PreAuditRegistration", "update", (long) rs.getId(), 0, 0, null);	
		 		 if(rs.getLnkAuditOrganizations().size()>0){
		 			 List<MainAuditRegistration> ms=new ArrayList<MainAuditRegistration>();
		 			 int a=0;
					 for(int i=0;i<rs.getLnkAuditOrganizations().size();i++){
						 LnkAuditOrganization lk=rs.getLnkAuditOrganizations().get(i);
						 //List<MainAuditRegistration> ml= (List<MainAuditRegistration>) dao.getHQLResult("from MainAuditRegistration t where t.orgtype="+rs.getOrgtype()+" and t.depid="+rs.getDepid()+" and t.audityear="+rs.getAudityear()+"", "list");
						 MainAuditRegistration ma=new MainAuditRegistration();
						 
						/* int a=0;
						 a=ml.size()+1;*/
						 a=a+1;
						 ma.setOrgname(lk.getOrgname());
						 ma.setOrgcode(lk.getOrgcode());
						 ma.setStartdate("0");
						 ma.setEnddate("0");
						 ma.setAudityear(rs.getAudityear());
						 ma.setOrgtype(rs.getOrgtype());
						 ma.setAutype(lk.getReasonid());
						 ma.setStepid(1);
						 ma.setDepid(rs.getDepid());
						 ma.setGencode(rs.getShortcode()+"-"+lk.getAyear()+"/"+a+"/"+rs.getQtype() +"-"+rs.getLutCategory().getCategoryname());
						 ma.setOrgid(lk.getAuditorgid());
						 ms.add(ma);
						// ma.setGencode(rs.getShortcode()+"-"+lk.getAyear()+"/"+ml.size()+1+"-"+rs.getLutCategory().getCategoryname());
						 //dao.PeaceCrud(ma, "MainAuditRegistration", "save", (long) 0, 0, 0, null);		
						 
						 if(lk.getReasonid()==2){
							 lk.setDistribution(true);
							 dao.PeaceCrud(lk, "LnkAuditOrganization", "update", (long) lk.getId(), 0, 0, null);		
						 }
					 }
					 //dao.getHQLResult("", "current");
					 dao.inserBatch(ms, "upquata");
				 }
			 }
			 
			 return "true";
		}
		else{
			 return "false";
		}
	 	 
	 }
	
	
	@RequestMapping(value="/back/quata/{id}",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String backapp(@PathVariable long id,@RequestBody String jsonString) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 
			 PreAuditRegistration rs= (PreAuditRegistration) dao.getHQLResult("from PreAuditRegistration t where t.id='"+id+"'  order by t.id", "current");
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			
			 System.out.println(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth());
			 DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 
			 if(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_FIRST")){
				 rs.setStepid(5);
				 dao.PeaceCrud(rs, "PreAuditRegistration", "update", (long) rs.getId(), 0, 0, null);
				 JSONObject obj= new JSONObject(jsonString);
				 //LnkCommentMain cmt= (LnkCommentMain) dao.getHQLResult("from LnkCommentMain t where t.preid='"+id+"'  order by t.id", "current");	
				 
				 LnkCommentMain addmcmt=new LnkCommentMain();
				 addmcmt.setMcomment(obj.getString("text"));
				 addmcmt.setUserid(loguser.getId());
				 addmcmt.setUsername(loguser.getGivenname());
				 addmcmt.setDesid(5);
				 addmcmt.setPreid(id);	
				 addmcmt.setCdate(special);
				
				 dao.PeaceCrud(addmcmt, "LnkCommentMain", "save", (long) 0, 0, 0, null);	
				 
				 
			 }
			 else if(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_SECOND")){
				 rs.setStepid(5);
				 dao.PeaceCrud(rs, "PreAuditRegistration", "update", (long) rs.getId(), 0, 0, null);	
				 
				 JSONObject obj= new JSONObject(jsonString);
				 //LnkCommentMain cmt= (LnkCommentMain) dao.getHQLResult("from LnkCommentMain t where t.preid='"+id+"'  order by t.id", "current");	
				 
				 LnkCommentMain addmcmt=new LnkCommentMain();
				 addmcmt.setMcomment(obj.getString("text"));
				 addmcmt.setUserid(loguser.getId());
				 addmcmt.setUsername(loguser.getGivenname());
				 addmcmt.setDesid(5);
				 addmcmt.setPreid(id);		
				 addmcmt.setCdate(special);
				
				 dao.PeaceCrud(addmcmt, "LnkCommentMain", "save", (long) 0, 0, 0, null);
			 }			 
			 return "true";
		}
		else{
			 return "false";
		}
	 	 
	 }
	
    @RequestMapping(value = "/resource/{domain}/{id}", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public @ResponseBody String tree(@PathVariable String domain,@PathVariable long id) {
	try{

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		JSONArray arr=new JSONArray();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			
			UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			if(domain.equalsIgnoreCase("PreAuditRegistration")){
				PreAuditRegistration pa = (PreAuditRegistration) dao.getHQLResult("from PreAuditRegistration t where t.id="+id+" order by t.id", "current");
				String str="";
				for(int i=0; i<pa.getLnkAuditOrganizations().size();i++){
					str=str+","+pa.getLnkAuditOrganizations().get(i).getAuditorgid();
				}	
				List<SubAuditOrganization> rs=(List<SubAuditOrganization>) dao.getHQLResult("from SubAuditOrganization t where t.departmentid='"+loguser.getDepartmentid()+"' and t.categoryid='"+pa.getOrgtype()+"' and t.id not in ("+str.substring(1, str.length())+") order by t.id", "list");
				
				 for(int i=0; i<rs.size();i++){
					 System.out.println("##"+rs.size());
					 JSONObject obj=new JSONObject();   
					 obj.put("id", rs.get(i).getId());
					 obj.put("code", rs.get(i).getOrgcode());
					 obj.put("title", rs.get(i).getOrgname());
					 obj.put("value", rs.get(i).getId());
					 arr.put(obj);
				}
			}
			else if(domain.equalsIgnoreCase("formg1")){
				FinFormG1 obj= (FinFormG1) dao.getHQLResult("from FinFormG1 t where t.mid='"+id+"'", "current");
				if (obj!=null){
					ObjectMapper mapper = new ObjectMapper(); 
					return mapper.writeValueAsString(obj);
				}
				else{
					return new JSONObject().toString();
				}
			}
			
			else if(domain.equalsIgnoreCase("formg2")){
				FinFormG2 obj= (FinFormG2) dao.getHQLResult("from FinFormG2 t where t.mid='"+id+"'", "current");
				if (obj!=null){
					ObjectMapper mapper = new ObjectMapper(); 
					return mapper.writeValueAsString(obj);
				}
				else{
					return new JSONObject().toString();
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
	
	@RequestMapping(value="/conf/quata",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String configapp(@RequestBody String jsonString) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 JSONObject obj= new JSONObject(jsonString);
			 
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			 LnkAuditOrganization lnka = (LnkAuditOrganization) dao.getHQLResult("from LnkAuditOrganization t where t.id="+obj.getInt("id")+" order by t.id", "current");
			 LnkConfLog confl = new LnkConfLog();
			 confl.setUserid(loguser.getId());
			 confl.setConfdat(special);
			 confl.setDescription(obj.getString("description"));
			 confl.setOlddata(obj.getJSONObject("olddata").toString());
			 dao.PeaceCrud(confl, "LnkAuditOrganization", "save", (long) 0, 0, 0, null);
			 
			 if(obj.getString("decision").equalsIgnoreCase("1")){
				 lnka.setReasonid(Long.parseLong(obj.getString("division")));				
				 dao.PeaceCrud(lnka, "LnkAuditOrganization", "update", (long) lnka.getId(), 0, 0, null);	
				 MainAuditRegistration ma=null;
				 if(lnka.getAuditorgid()!=0){
					 ma = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.orgid="+lnka.getAuditorgid()+" and t.isenabled=1 order by t.id", "current");
				 }
				 else{
					 ma = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.orgcode='"+lnka.getOrgcode()+"' and t.isenabled=1 order by t.id", "current");
				 }
				 ma.setAutype(Long.parseLong(obj.getString("division")));
				 dao.PeaceCrud(ma, "MainAuditRegistration", "update", (long) ma.getId(), 0, 0, null);	
			 }
			 else{
				 //MainAuditRegistration ma=null;
				 if(lnka.getAuditorgid()!=0){					 
					 List<MainAuditRegistration> ma = (List<MainAuditRegistration>) dao.getHQLResult("from MainAuditRegistration t where t.orgid="+lnka.getAuditorgid()+" and t.isenabled=1 order by t.id", "list");
					 for(MainAuditRegistration item:ma){
						 item.setIsenabled(false);
						 dao.PeaceCrud(item, "MainAuditRegistration", "update", (long) item.getId(), 0, 0, null);
					 }
				 }
				 else{
					 List<MainAuditRegistration> ma = (List<MainAuditRegistration>)  dao.getHQLResult("from MainAuditRegistration t where t.orgcode='"+lnka.getOrgcode()+"' and t.isenabled=1 order by t.id", "list");
					 for(MainAuditRegistration item:ma){
						 item.setIsenabled(false);
						 dao.PeaceCrud(item, "MainAuditRegistration", "update", (long) item.getId(), 0, 0, null);
					 }
				 }
					
				 PreAuditRegistration pa = (PreAuditRegistration) dao.getHQLResult("from PreAuditRegistration t where t.id="+lnka.getPreid()+" order by t.id", "current");
				 pa.setTotal(pa.getTotal()-1);
				 dao.PeaceCrud(pa, "PreAuditRegistration", "update", (long) pa.getId(), 0, 0, null);		
				 dao.PeaceCrud(lnka, "LnkAuditOrganization", "delete", (long) lnka.getId(), 0, 0, null);	
			 }
		
			 return "true";
		}
		else{
			 return "false";
		}
	 	 
	}
	
	@RequestMapping(value="/conf/new/quata",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String configappnew(@RequestBody String jsonString) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 JSONObject obj= new JSONObject(jsonString);
			 
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			 PreAuditRegistration pr=(PreAuditRegistration) dao.getHQLResult("from PreAuditRegistration t where t.id="+obj.getString("id")+" order by t.id", "current");
			 SubAuditOrganization org=(SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.id="+obj.getString("orgid")+" order by t.id", "current");
			 LnkAuditOrganization lnka = new LnkAuditOrganization();
			 lnka.setAyear(Integer.parseInt(pr.getAudityear()));
			 lnka.setAuditorgid(obj.getLong("orgid"));
			 lnka.setOrgtype(pr.getOrgtype());
		/*	 if(lk.getReasonid()==2){
				 lk.setDistribution(true);
				 dao.PeaceCrud(lk, "LnkAuditOrganization", "update", (long) lk.getId(), 0, 0, null);		
			 }*/
			 lnka.setDistribution(true);
			 lnka.setPreid(pr.getId());
			 lnka.setOrgcode(org.getOrgcode());
			 lnka.setOrgname(org.getOrgname());
			 lnka.setQuataid(pr.getId());
			 lnka.setReasonid(obj.getLong("division"));
			 dao.PeaceCrud(lnka, "LnkAuditOrganization", "save", (long) 0, 0, 0, null);			   
			 
			 
			 LnkConfLog confl = new LnkConfLog();
			 confl.setUserid(loguser.getId());
			 confl.setConfdat(special);
			 confl.setDescription(obj.getString("description"));
			 dao.PeaceCrud(confl, "LnkAuditOrganization", "save", (long) 0, 0, 0, null);
			 
			 pr.setTotal(pr.getTotal()+1);
			 dao.PeaceCrud(pr, "PreAuditRegistration", "update", (long) pr.getId(), 0, 0, null);		
			 List<MainAuditRegistration> ml= (List<MainAuditRegistration>) dao.getHQLResult("from MainAuditRegistration t where t.orgtype="+pr.getOrgtype()+" and t.depid="+pr.getDepid()+" and t.audityear="+pr.getAudityear()+"", "list");

			 int a=ml.size()+1;
			 
			 MainAuditRegistration ma=new MainAuditRegistration();
	
			 ma.setOrgname(org.getOrgname());
			 ma.setOrgcode(org.getOrgcode());
			 ma.setStartdate("0");
			 ma.setEnddate("0");
			 ma.setAudityear(pr.getAudityear());
			 ma.setOrgtype(pr.getOrgtype());
			 ma.setAutype(lnka.getReasonid());
			 ma.setStepid(1);
			 ma.setDepid(pr.getDepid());
			 ma.setOrgid(lnka.getAuditorgid());
			 ma.setIsenabled(true);
			 ma.setGencode(pr.getShortcode()+"-"+lnka.getAyear()+"/"+a+"/"+pr.getOrgtype() +"-"+org.getLutCategory().getCategoryname());

			// ma.setGencode(rs.getShortcode()+"-"+lk.getAyear()+"/"+ml.size()+1+"-"+rs.getLutCategory().getCategoryname());
			 dao.PeaceCrud(ma, "MainAuditRegistration", "save", (long) 0, 0, 0, null);		
			 
			
		
			 return "true";
		}
		else{
			 return "false";
		}
	 	 
	}
	
	@RequestMapping(value="/create/quata",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String sendapp(@RequestBody String jsonString) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 JSONObject obj= new JSONObject(jsonString);
			 
			 int year;
			 year=obj.getInt("ayear");			 
			 //year=year-1;
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			 
			 List<PreAuditRegistration> rs=(List<PreAuditRegistration>) dao.getHQLResult("from PreAuditRegistration t where t.depid="+loguser.getDepartmentid()+" and t.audityear='"+year+"' and t.orgtype='"+obj.getInt("category")+"' order by t.id", "list");
			 System.out.println("@@"+jsonString+rs.size());;
			 
			 if(rs.size()==0){
				
				
				 
				 List<SubAuditOrganization> sa=(List<SubAuditOrganization>) dao.getHQLResult("from SubAuditOrganization t where t.departmentid='"+loguser.getDepartmentid()+"' and t.categoryid='"+obj.getInt("category")+"' order by t.id", "list");
				 
				 PreAuditRegistration pr=new PreAuditRegistration();
				 pr.setAudityear(obj.getString("ayear"));
				 pr.setOrgtype(obj.getInt("category"));
				 pr.setOrgname(loguser.getLutDepartment().getDepartmentname());
				 pr.setShortcode(loguser.getLutDepartment().getShortname());
				 pr.setStepid(1);
				 pr.setDepid(loguser.getDepartmentid());
				 pr.setQtype(obj.getString("qtype"));
				 pr.setTotal(sa.size());
				 dao.PeaceCrud(pr, "PreAuditRegistration", "save", (long) 0, 0, 0, null);			   
				 
				 if(sa.size()>0){
					 List<LnkAuditOrganization> lnkAudit = new ArrayList<LnkAuditOrganization>(); 
					 for(int i=0;i<sa.size();i++){
						 SubAuditOrganization org=sa.get(i);
						 LnkAuditOrganization lk=new LnkAuditOrganization();
						 lk.setAyear(obj.getInt("ayear"));
						 lk.setOrgcode(String.valueOf(org.getOrgcode()));
						 lk.setOrgcode(String.valueOf(org.getOrgcode()));
						 lk.setAuditorgid(org.getId());
						 lk.setOrgname(org.getOrgname());
						 lk.setOrgtype(obj.getInt("category"));
						 lk.setReasonid(1);
						 lk.setQuataid(pr.getId());
						 lk.setPreid(pr.getId());
						 lnkAudit.add(lk);
						 //dao.PeaceCrud(lk, "LnkAuditOrganization", "save", (long) 0, 0, 0, null);			   
					 }
					 dao.inserBatch(lnkAudit, "quata");
				 }
				 
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
	@RequestMapping(value="/back/config/{id}",method=RequestMethod.PUT,produces={"application/json; charset=UTF-8"})
	public @ResponseBody String backconfig(@PathVariable long id,@RequestBody String jsonString) throws JSONException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			 
			MainAuditRegistration rs= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+id+"'  order by t.id", "current");
			 UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
			
			 System.out.println(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth());
			 DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		     Date date1 = new Date();
			 String special = dateFormat1.format(date1);
			 
			 if(loguser.getLnkUserroles().get(0).getLutRole().getRoleauth().equalsIgnoreCase("ROLE_SECOND")){
				 rs.setStepid(4);
				 dao.PeaceCrud(rs, "MainAuditRegistration", "update", (long) rs.getId(), 0, 0, null);	
				 
				 JSONObject obj= new JSONObject(jsonString);
				 //LnkCommentMain cmt= (LnkCommentMain) dao.getHQLResult("from LnkCommentMain t where t.preid='"+id+"'  order by t.id", "current");	
				 
				 LnkCommentMain addmcmt=new LnkCommentMain();
				 addmcmt.setMcomment(obj.getString("text"));
				 addmcmt.setUserid(loguser.getId());
				 addmcmt.setUsername(loguser.getGivenname());
				 addmcmt.setDesid(5);
				 addmcmt.setMainid(id);		
				 addmcmt.setCdate(special);
				
				 dao.PeaceCrud(addmcmt, "LnkCommentMain", "save", (long) 0, 0, 0, null);
			 }			 
			 return "true";
		}
		else{
			 return "false";
		}
	 	 
	 }
	@RequestMapping(value = "/{action}/{domain}", method=RequestMethod.POST)
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
				   
				   if(domainName.equalsIgnoreCase("com.netgloo.models.LutUser")){
				    	  JSONObject str= new JSONObject(jsonString);
				    	 					  
				   }
				   else if (domainName.equalsIgnoreCase("com.netgloo.models.MainAuditRegistration")){
					   JSONObject str= new JSONObject(jsonString);
					   if (str.has("id") && !str.isNull("id") && str.has("auditname") && !str.isNull("auditname")){
						   MainAuditRegistration form= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id = "+str.getLong("id"), "current");
						   if (form != null){
							   form.setAuditname(str.getString("auditname"));
							   dao.PeaceCrud(form, "MainAuditRegistration", "update", (long) form.getId(), 0, 0, null);
						   }
					   }
				   }
				   else if (domainName.equalsIgnoreCase("com.netgloo.models.LnkMainFormT2")){
					   int id=(int)obj.getInt("id");
					   LnkMainFormT2 form= (LnkMainFormT2) dao.getHQLResult("from LnkMainFormT2 t where t.id = "+id, "current");
					   
					   MainAuditRegistration mn= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id = "+form.getMid(), "current");
					   
					   if (form!=null){
						   form.setDecid(obj.getLong("decid"));
						   form.setDescription(obj.getString("description"));
						   form.setDirid(obj.getLong("dirid"));
						   form.setFactorid(obj.getLong("factorid"));
						   form.setMid(obj.getLong("mid"));
						   form.setRiskid(obj.getLong("riskid"));
						   form.setRtype(obj.getInt("rtype"));
						   form.setGroupid(obj.getInt("groupid"));
						   form.setStepid(obj.getInt("levelid"));
						   if (obj.has("criname")){
							   form.setCriname(obj.getString("criname"));
						   }
						   if (obj.has("dirname")){
							   form.setDirname(obj.getString("dirname"));
						   }
						   if (obj.has("riskname")){
							   form.setRiskname(obj.getString("riskname"));
						   }
						   if (obj.has("crition") && !obj.isNull("crition")){
							   form.setCriname(obj.getString("crition"));
							   LutFactorCriterion c = new LutFactorCriterion();
							   c.setFactorid(form.getFactorid());
							   c.setName(obj.getString("crition"));
							   dao.PeaceCrud(c, "LutFactorCriterion", "save", (long) 0, 0, 0, null);
						   }
					   }
					   dao.PeaceCrud(form, domainName, "update", (long) id, 0, 0, null);
					   if (obj.has("treatmentid")){
						   LutTryout tryout = (LutTryout) dao.getHQLResult("from LutTryout t where t.id = "+obj.getLong("treatmentid"), "current");
						   if (tryout != null){
							   for(LnkTryoutConfMethod a : tryout.getLnkTryoutConfMethods()){
								   a.setTryid(tryout.getId());
								   dao.PeaceCrud(a, "LnkTryoutConfMethod", "update", a.getId(), 0, 0, null);
							   }
							   for(LnkTryoutConfSource a : tryout.getLnkTryoutConfSources()){
								   a.setTryid(tryout.getId());
								   dao.PeaceCrud(a, "LnkTryoutConfSource", "update", a.getId(), 0, 0, null);
							   }
							   for(LnkTryoutConfType a : tryout.getLnkTryoutConfTypes()){
								   a.setTryid(tryout.getId());
								   dao.PeaceCrud(a, "LnkTryoutConfType", "update", a.getId(), 0, 0, null);
							   }
						   }
					   }
					   
					   if (obj.has("riskhuulilist") && (!obj.isNull("riskhuulilist"))){
						   JSONArray laws = obj.getJSONArray("riskhuulilist");
						   if (laws.length() > 0){
							   for(int i=0;i<laws.length();i++){
								   JSONObject temp = (JSONObject) laws.get(i);
								   if (temp.has("law") && temp.has("zaalt") && temp.has("zuil") && (!temp.isNull("law")) && (!temp.isNull("zaalt")) && (!temp.isNull("zuil"))){
									   JSONObject lawobj = temp.getJSONObject("law");
									   JSONObject zaaltobj = temp.getJSONObject("zaalt");
									   JSONObject zuilobj = temp.getJSONObject("zuil");
									   LnkRiskLaw risk_law = new LnkRiskLaw();
									   risk_law.setRiskid(form.getRiskid());
									   risk_law.setLawid(lawobj.getLong("value"));
									   risk_law.setLname(lawobj.getString("text"));
									   risk_law.setZaalt(zaaltobj.getLong("value"));
									   risk_law.setLzaalt(zaaltobj.getString("text"));
									   risk_law.setZuilid(zuilobj.getLong("value"));
									   risk_law.setLzuil(zuilobj.getString("text"));
									   risk_law.setLawcategory((long) 1);
									   System.out.println(risk_law.toString());
									   dao.PeaceCrud(risk_law, "LnkRiskLaw", "save", (long) 0, 0, 0, null);
								   }
							   }
						   }
					   }
					   
					   if (obj.has("confMethod") && (!obj.isNull("confMethod"))){
						   JSONArray confMethods = obj.getJSONArray("confMethod");
						   for(int i=0;i<confMethods.length();i++){
							   LnkTryoutConfMethod m = new LnkTryoutConfMethod();
							   m.setMethodid(confMethods.getLong(i));
							   m.setTryid(obj.getLong("treatmentid"));
							   dao.PeaceCrud(m, "LnkTryoutConfMethod", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (obj.has("confSourceI") && (!obj.isNull("confSourceI"))){
						   JSONArray confSourceIs = obj.getJSONArray("confSourceI");
						   for(int i=0;i<confSourceIs.length();i++){
							   LnkTryoutConfSource m = new LnkTryoutConfSource();
							   m.setSourceid(confSourceIs.getLong(i));
							   m.setTryid(obj.getLong("treatmentid"));
							   m.setTypeid(1);
							   dao.PeaceCrud(m, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (obj.has("confSourceO") && (!obj.isNull("confSourceO"))){
						   JSONArray confSourceOs = obj.getJSONArray("confSourceO");
						   for(int i=0;i<confSourceOs.length();i++){
							   LnkTryoutConfSource m = new LnkTryoutConfSource();
							   m.setSourceid(confSourceOs.getLong(i));
							   m.setTryid(obj.getLong("treatmentid"));
							   m.setTypeid(0);
							   dao.PeaceCrud(m, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (obj.has("conftype") && (!obj.isNull("conftype"))){
						   JSONArray conftypes = obj.getJSONArray("conftype");
						   for(int i=0;i<conftypes.length();i++){
							   LnkTryoutConfType m = new LnkTryoutConfType();
							   m.setTypeid(conftypes.getLong(i));
							   m.setTryid(obj.getLong("treatmentid"));
							   dao.PeaceCrud(m, "LnkTryoutConfType", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (obj.has("other") && (!obj.isNull("other"))){
						   LutLaw other = new LutLaw();
						   other.setLawname(obj.getString("other"));
						   other.setLawcategory((long) 3);
						   dao.PeaceCrud(other, "LutLaw", "save", (long) 0, 0, 0, null);
						   LnkRiskLaw risk_law_other = new LnkRiskLaw();
						   risk_law_other.setRiskid(form.getRiskid());
						   risk_law_other.setLawid(other.getId());
						   risk_law_other.setLname(other.getLawname());
						   risk_law_other.setLawcategory(3);
						   dao.PeaceCrud(risk_law_other, "LnkRiskLaw", "save", (long) 0, 0, 0, null);
					   }
					   
					   if (obj.has("riskstandartlist") && (!obj.isNull("riskstandartlist"))){
						   JSONArray laws = obj.getJSONArray("riskstandartlist");
						   if (laws.length() > 0){
							   for(int i=0;i<laws.length();i++){
								   JSONObject temp = (JSONObject) laws.get(i);
								   if (temp.has("law") && temp.has("zaalt") && temp.has("zuil") && (!temp.isNull("law")) && (!temp.isNull("zaalt")) && (!temp.isNull("zuil"))){
									   JSONObject lawobj = temp.getJSONObject("law");
									   JSONObject zaaltobj = temp.getJSONObject("zaalt");
									   JSONObject zuilobj = temp.getJSONObject("zuil");
									   LnkRiskLaw risk_law = new LnkRiskLaw();
									   risk_law.setRiskid(form.getRiskid());
									   risk_law.setLawid(lawobj.getLong("value"));
									   risk_law.setLname(lawobj.getString("text"));
									   risk_law.setZaalt(zaaltobj.getLong("value"));
									   risk_law.setLzaalt(zaaltobj.getString("text"));
									   risk_law.setZuilid(zuilobj.getLong("value"));
									   risk_law.setLzuil(zuilobj.getString("text"));
									   risk_law.setLawcategory((long) 2);
									   dao.PeaceCrud(risk_law, "LnkRiskLaw", "save", (long) 0, 0, 0, null);
								   }
								   
							   }
						   }
					   }
					   
					   if(obj.getLong("levelid")==4 || mn.getAutype()==2){
	    				   	  LnkRiskT2 lk=(LnkRiskT2) dao.getHQLResult("from LnkRiskT2 t where t.t2id="+form.getId()+" and t.riskid="+obj.getLong("riskid")+"", "current");
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
				    		  lrt.setData3("75");
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
				    		  lrt.setData26(obj.getString("levelid"));
				    		  lrt.setMid(obj.getLong("mid"));
				    		  if(ll.size()>0){
				    			  dao.PeaceCrud(lrt, "LnkRiskTryout", "update", (long) lrt.getId(), 0, 0, null);
				    		  }
				    		  else{
				    			  dao.PeaceCrud(lrt, "LnkRiskTryout", "save", (long) 0, 0, 0, null);
				    		  }
				    		  
			    		  }
					   
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
			   dao.PeaceCrud(object, domainName, "delete", obj.getLong("id"), 0, 0, null);			  
		   }
		   else if(action.equals("create")){
			  if(domainName.equalsIgnoreCase("com.netgloo.models.LutRisk")){
			    	   JSONObject str= new JSONObject(jsonString);
			    	   LutRisk risk= new LutRisk();
					   risk.setRiskname(str.getString("risk"));
					   risk.setRisktype(2);
					   dao.PeaceCrud(risk, domainName, "save", (long) 0, 0, 0, null); 	
					   
					   LnkRiskdir riskdir= new LnkRiskdir();
					   riskdir.setRiskid(risk.getId());
					   riskdir.setDirid(obj.getLong("dirid"));
					   dao.PeaceCrud(riskdir, domainName, "save", (long) 0, 0, 0, null);
					   
					   JSONObject robj= new JSONObject();
					   robj.put("text", risk.getRiskname());
					   robj.put("value", risk.getId());
					   return robj.toString();
			   }
			  else if(domainName.equalsIgnoreCase("com.netgloo.models.LnkMainFormT2")){
		    	   JSONObject str= new JSONObject(jsonString);
		    	   JSONObject robj= new JSONObject();
		    	   List<LnkMainFormT2> oldlist = (List<LnkMainFormT2>) dao.getHQLResult("from LnkMainFormT2 t where t.dirid = " + str.getLong("dir") + " and t.mid = " + str.getLong("mid") + " and t.factorid = " + str.getLong("factorid") + " and t.riskid = " + str.getLong("riskid") + " and t.groupid = "+str.getLong("groupid")+"", "list");
		    	   if (oldlist.isEmpty()){
			    	   LnkMainFormT2 lm= new LnkMainFormT2();
			    	   lm.setDirid(str.getLong("dir"));
			    	   lm.setFactorid(str.getLong("factorid"));
			    	   lm.setMid(str.getLong("mid"));
			    	   lm.setRiskid(str.getLong("riskid"));
			    	   lm.setGroupid(str.getInt("groupid"));
			    	   lm.setDecid(1);
			    	   lm.setRtype(1);
			    	   lm.setStepid(str.getInt("levelid"));
			    	   lm.setDescription(str.getString("description"));
			    	   if (str.has("crition") && !str.isNull("crition")){
			    		   lm.setCriname(str.getString("crition"));
						   LutFactorCriterion c = new LutFactorCriterion();
						   c.setFactorid(lm.getFactorid());
						   c.setName(str.getString("crition"));
						   dao.PeaceCrud(c, "LutFactorCriterion", "save", (long) 0, 0, 0, null);
					   }
					   dao.PeaceCrud(lm, domainName, "save", (long) 0, 0, 0, null); 	
					   
					   if (str.has("riskhuulilist") && (!str.isNull("riskhuulilist"))){
						   JSONArray laws = str.getJSONArray("riskhuulilist");
						   if (laws.length() > 0){
							   for(int i=0;i<laws.length();i++){
								   JSONObject temp = (JSONObject) laws.get(i);
								   if (temp.has("law") && temp.has("zaalt") && temp.has("zuil") && (!temp.isNull("law")) && (!temp.isNull("zaalt")) && (!temp.isNull("zuil"))){
									   JSONObject lawobj = temp.getJSONObject("law");
									   JSONObject zaaltobj = temp.getJSONObject("zaalt");
									   JSONObject zuilobj = temp.getJSONObject("zuil");
									   LnkRiskLaw risk_law = new LnkRiskLaw();
									   risk_law.setRiskid(lm.getRiskid());
									   risk_law.setLawid(lawobj.getLong("value"));
									   risk_law.setLname(lawobj.getString("text"));
									   risk_law.setZaalt(zaaltobj.getLong("value"));
									   risk_law.setLzaalt(zaaltobj.getString("text"));
									   risk_law.setZuilid(zuilobj.getLong("value"));
									   risk_law.setLzuil(zuilobj.getString("text"));
									   risk_law.setLawcategory((long) 1);
									   dao.PeaceCrud(risk_law, "LnkRiskLaw", "save", (long) 0, 0, 0, null);
								   }
							   }
						   }
					   }
					   
					   if (str.has("confMethod") && (!str.isNull("confMethod"))){
						   JSONArray confMethods = str.getJSONArray("confMethod");
						   for(int i=0;i<confMethods.length();i++){
							   LnkTryoutConfMethod m = new LnkTryoutConfMethod();
							   m.setMethodid(confMethods.getLong(i));
							   m.setTryid(str.getLong("treatmentid"));
							   dao.PeaceCrud(m, "LnkTryoutConfMethod", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (str.has("confSourceI") && (!str.isNull("confSourceI"))){
						   JSONArray confSourceIs = str.getJSONArray("confSourceI");
						   for(int i=0;i<confSourceIs.length();i++){
							   LnkTryoutConfSource m = new LnkTryoutConfSource();
							   m.setSourceid(confSourceIs.getLong(i));
							   m.setTryid(str.getLong("treatmentid"));
							   m.setTypeid(1);
							   dao.PeaceCrud(m, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (str.has("confSourceO") && (!str.isNull("confSourceO"))){
						   JSONArray confSourceOs = str.getJSONArray("confSourceO");
						   for(int i=0;i<confSourceOs.length();i++){
							   LnkTryoutConfSource m = new LnkTryoutConfSource();
							   m.setSourceid(confSourceOs.getLong(i));
							   m.setTryid(str.getLong("treatmentid"));
							   m.setTypeid(0);
							   dao.PeaceCrud(m, "LnkTryoutConfSource", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (str.has("conftype") && (!str.isNull("conftype"))){
						   JSONArray conftypes = str.getJSONArray("conftype");
						   for(int i=0;i<conftypes.length();i++){
							   LnkTryoutConfType m = new LnkTryoutConfType();
							   m.setTypeid(conftypes.getLong(i));
							   m.setTryid(str.getLong("treatmentid"));
							   dao.PeaceCrud(m, "LnkTryoutConfType", "save", (long) 0, 0, 0, null);
						   }
					   }
					   
					   if (str.has("other") && (!str.isNull("other"))){
						   LutLaw other = new LutLaw();
						   other.setLawname(str.getString("other"));
						   other.setLawcategory((long) 3);
						   dao.PeaceCrud(other, "LutLaw", "save", (long) 0, 0, 0, null);
						   LnkRiskLaw risk_law_other = new LnkRiskLaw();
						   risk_law_other.setRiskid(lm.getRiskid());
						   risk_law_other.setLawid(other.getId());
						   risk_law_other.setLname(other.getLawname());
						   risk_law_other.setLawcategory(3);
						   dao.PeaceCrud(risk_law_other, "LnkRiskLaw", "save", (long) 0, 0, 0, null);
					   }
					   
					   if (str.has("riskstandartlist") && (!str.isNull("riskstandartlist"))){
						   JSONArray laws = str.getJSONArray("riskstandartlist");
						   if (laws.length() > 0){
							   for(int i=0;i<laws.length();i++){
								   JSONObject temp = (JSONObject) laws.get(i);
								   if (temp.has("law") && temp.has("zaalt") && temp.has("zuil") && (!temp.isNull("law")) && (!temp.isNull("zaalt")) && (!temp.isNull("zuil"))){
									   JSONObject lawobj = temp.getJSONObject("law");
									   JSONObject zaaltobj = temp.getJSONObject("zaalt");
									   JSONObject zuilobj = temp.getJSONObject("zuil");
									   LnkRiskLaw risk_law = new LnkRiskLaw();
									   risk_law.setRiskid(lm.getRiskid());
									   risk_law.setLawid(lawobj.getLong("value"));
									   risk_law.setLname(lawobj.getString("text"));
									   risk_law.setZaalt(zaaltobj.getLong("value"));
									   risk_law.setLzaalt(zaaltobj.getString("text"));
									   risk_law.setZuilid(zuilobj.getLong("value"));
									   risk_law.setLzuil(zuilobj.getString("text"));
									   risk_law.setLawcategory((long) 2);
									   dao.PeaceCrud(risk_law, "LnkRiskLaw", "save", (long) 0, 0, 0, null);
								   }
								   
							   }
						   }
					   }
					   
					   List<LnkRiskT2> t2=(List<LnkRiskT2>) dao.getHQLResult("from LnkRiskT2 t where t.t2id="+lm.getId()+" and t.riskid="+str.getLong("riskid")+"", "list");
	    			   if(t2.size()==0){		
	    				  LnkRiskT2 tt=new LnkRiskT2();						    			  
		    			  tt.setRiskid(str.getLong("riskid"));
		    			  tt.setT2id(lm.getId());
		    			  tt.setDirid(lm.getDirid());
		    			  tt.setRtype(lm.getRtype());
		    			  tt.setTryid(str.getLong("treatmentid"));
		    			  tt.setDescription(str.getString("description"));
		    			  dao.PeaceCrud(tt, "LnkRiskT2", "save", (long) 0, 0, 0, null);
	    			   }
	    			   else{
	    				  LnkRiskT2 tt=t2.get(0);						    			  
		    			  tt.setRiskid(str.getLong("riskid"));
		    			  tt.setT2id(lm.getId());
		    			  tt.setDirid(lm.getDirid());
		    			  tt.setRtype(lm.getRtype());
		    			  tt.setTryid(str.getLong("treatmentid"));
		    			  tt.setDescription(str.getString("description"));
		    			  dao.PeaceCrud(tt, "LnkRiskT2", "update", (long) tt.getId(), 0, 0, null);
	    			   }
	    			   
	    			   
					   MainAuditRegistration mn= (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id = "+str.getLong("mid") , "current");
					   
					   
	    			   if(str.getLong("levelid")==4 || mn.getAutype()==2){
	    				   	  LnkRiskT2 lk=(LnkRiskT2) dao.getHQLResult("from LnkRiskT2 t where t.t2id="+lm.getId()+" and t.riskid="+str.getLong("riskid")+"", "current");
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
				    		  lrt.setData3("75");
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
				    		  lrt.setData26(str.getString("levelid"));
				    		  lrt.setMid(str.getLong("mid"));
				    		  if(ll.size()>0){
				    			  dao.PeaceCrud(lrt, "LnkRiskTryout", "update", (long) lrt.getId(), 0, 0, null);
				    		  }
				    		  else{
				    			  dao.PeaceCrud(lrt, "LnkRiskTryout", "save", (long) 0, 0, 0, null);
				    		  }
				    		  
			    		  }
			
				      robj.put("robj", true);
		    	   }
		    	   else{
		    		   robj.put("robj", false);
		    	   }

				   return robj.toString();
			   }
			   else if(domainName.equalsIgnoreCase("com.netgloo.models.LutTryout")){
		    	   JSONObject str= new JSONObject(jsonString);
		    	   LutTryout risk= new LutTryout();
				   risk.setAdirid(str.getLong("dirid"));
				   risk.setFormdesc(str.getString("treatment"));
				   risk.setText(str.getString("treatment"));
				   dao.PeaceCrud(risk, domainName, "save", (long) 0, 0, 0, null); 	
				   
				   
				   LnkTryoutRisk riskdir= new LnkTryoutRisk();
				   riskdir.setRiskid(str.getLong("riskid"));
				   riskdir.setDirid(obj.getLong("dirid"));
				   riskdir.setTryoutid(risk.getId());
				   dao.PeaceCrud(riskdir, domainName, "save", (long) 0, 0, 0, null);
				   
				   JSONObject robj= new JSONObject();
				   robj.put("text", risk.getText());
				   robj.put("value", risk.getId());
				   return robj.toString();
			   }

			   else if(domainName.equalsIgnoreCase("com.netgloo.models.LutLaw")){
				   JSONObject str= new JSONObject(jsonString);
				   LutLaw law = new LutLaw();
				   law.setLawname(str.getString("lawname"));
				   law.setLawcategory(str.getLong("lawcategory"));
				   dao.PeaceCrud(law, domainName, "save", (long) 0, 0, 0, null);
				   return new JSONObject().put("text",law.getLawname()).put("value", law.getId()).toString();
			   }
			   else if(domainName.equalsIgnoreCase("com.netgloo.models.LutFactor")){
				   JSONObject str= new JSONObject(jsonString);
				   LutFactor f = new LutFactor();
				   f.setDirid(str.getLong("dirid"));
				   f.setFactorname(str.getString("factorname"));
				   f.setGroupid(str.getLong("groupid"));
				   f.setRiskid(str.getLong("riskid"));
				   f.setTryid(str.getLong("treatmentid"));
				   f.setIsactive(0);
				   dao.PeaceCrud(f, domainName, "save", (long) 0, 0, 0, null);
				   return new JSONObject().put("text",f.getFactorname()).put("value", f.getId()).toString();
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
	
	
	@RequestMapping(value = "/getLnkMainFormT2", method=RequestMethod.POST)
    public @ResponseBody String getLnkMainFormT2(@RequestBody String jsonString){
		JSONObject result = new JSONObject();
		JSONObject obj = new JSONObject(jsonString);
		if (obj.has("dirid") && obj.has("factorid") && obj.has("riskid") && obj.has("mid")){
			LnkMainFormT2 form = (LnkMainFormT2) dao.getHQLResult("from LnkMainFormT2 t where t.dirid = " + obj.getLong("dirid") + " and t.factorid = " + obj.getLong("factorid") + " and t.riskid = " + obj.getLong("riskid") + " and t.mid = " + obj.getLong("mid"), "current");
			if (form != null){
				result.put("id", form.getId());
				result.put("decid", form.getDecid());
				result.put("description", form.getDescription());
				result.put("dirid", form.getDirid());
				result.put("criname", form.getCriname());
				result.put("factorid", form.getFactorid());
				result.put("riskid", form.getRiskid());
				result.put("groupid", form.getGroupid());
				result.put("mid", form.getMid());
				result.put("rtype", form.getRtype());
				result.put("dirname", form.getDirname());
				result.put("riskname", form.getRiskname());
				for(LnkRiskT2 t2: form.getLnkRiskT2s()){
					result.put("treatmentid", t2.getTryid());
				}
				return result.toString();
			}
		}
		return null;
	}
	
	
	
	
	
}
