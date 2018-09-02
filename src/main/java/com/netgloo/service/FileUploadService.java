package com.netgloo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netgloo.IzrApplication;
import com.netgloo.dao.UserDao;
import com.netgloo.models.FileUpload;
import com.netgloo.models.FinAbw;
import com.netgloo.models.FinAsset;
import com.netgloo.models.FinBudget;
import com.netgloo.models.FinCbw;
import com.netgloo.models.FinCt1a;
import com.netgloo.models.FinCt2a;
import com.netgloo.models.FinCt3a;
import com.netgloo.models.FinCt4a;
import com.netgloo.models.FinCtt1;
import com.netgloo.models.FinCtt2;
import com.netgloo.models.FinCtt3;
import com.netgloo.models.FinCtt4;
import com.netgloo.models.FinCtt5;
import com.netgloo.models.FinCtt6;
import com.netgloo.models.FinCtt7;
import com.netgloo.models.FinCtt8;
import com.netgloo.models.FinCtt9;
import com.netgloo.models.FinFormB1;
import com.netgloo.models.FinFormB12;
import com.netgloo.models.FinFormB21;
import com.netgloo.models.FinInventory;
import com.netgloo.models.FinJournal;
import com.netgloo.models.FinNt2;
import com.netgloo.models.FinPayroll;
import com.netgloo.models.FinTgt1;
import com.netgloo.models.FinTgt1a;
import com.netgloo.models.FinTrialBalance;
import com.netgloo.models.FsForm241;
import com.netgloo.models.FsForm242;
import com.netgloo.models.FsForm243;
import com.netgloo.models.FsFormA4;
import com.netgloo.models.LnkDirectionNotice;
import com.netgloo.models.LnkMainFormT2;
import com.netgloo.models.LnkRiskT2;
import com.netgloo.models.LnkRiskTryout;
import com.netgloo.models.LnkTryoutConfSource;
import com.netgloo.models.LnkTryoutConfType;
import com.netgloo.models.LutAuditDir;
import com.netgloo.models.LutRisk;
import com.netgloo.models.LutTryout;
import com.netgloo.models.LutUser;
import com.netgloo.models.LutValidation;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.models.StsCheckVariable;
import com.netgloo.models.SubAuditOrganization;
import com.netgloo.repo.FileUploadRepository;

@Service("FileUploadService")
public class FileUploadService {

	@Autowired
	private UserDao dao;

	@Autowired
	FileUploadRepository fileUploadRepository;

	// Retrieve file
	public FileUpload findByFilename(String filename) {
		return fileUploadRepository.findByFilename(filename);
	}

	// Upload the file
	public FileUpload uploadFile(MultipartFile mfile, String path,String downdir) throws IllegalStateException, IOException {

		String mimeType = mfile.getContentType();
		String filename = mfile.getOriginalFilename();
		int index=filename.lastIndexOf('.');
		String lastOne=(filename.substring(index +1));
		//String [] arr=filename.split('.');
		//String lastOne = arr[arr.length-1];
		System.out.println(mfile.getOriginalFilename());;
		System.out.println(lastOne);;
		long size = mfile.getSize();

		String uuid = UUID.randomUUID().toString()+"."+lastOne;
		
		
		File folder = new File(path);
		if(!folder.exists()){
			folder.mkdirs();
		}
		if (!mfile.isEmpty()) {
			try {
				Files.copy(mfile.getInputStream(), Paths.get(path, uuid));
			} catch (IOException|RuntimeException e) {
				System.out.println("aldaa");
				String furl = downdir+ "/" +filename;
				//FileUpload newFile = new FileUpload(id,filename, size, mimeType,furl);
				//return newFile;
			}
		} else {
			System.out.println("empty"); 
			return null;
		}

		String furl = downdir+ "/" +uuid;

		FileUpload newFile = new FileUpload();
		newFile.setFilename(filename);
		newFile.setFilesize(size);
		newFile.setMimetype(lastOne);
		newFile.setFileurl(furl);

		fileUploadRepository.save(newFile);

		return newFile;

	}

	/*  @ResponseBody
 	@RequestMapping(value = "/api/upload_image",method = RequestMethod.POST)
 	public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
 		if (!file.isEmpty()) {
 			String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
 			Files.copy(file.getInputStream(), Paths.get(ROOT, filename));
 			JSONObject result = new JSONObject();
 			result.put("result", "/images/"+filename);
 			return result.toString();
 		} else {
 			return null;
 		}
 	}*/
	// Upload the file
	public FileUpload uploadFinExcel(MultipartFile mfile,long mid,String ayear,String orgcode,long orgtype,Integer stepid,String path,String DOW_DIR) throws IllegalStateException, IOException, org.apache.poi.ss.formula.FormulaParseException{

		String mimeType = mfile.getContentType();
		// String filename = mfile.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String filename = System.currentTimeMillis() + "_" + uuid;
		long size = mfile.getSize();
		String SAVE_DIR = IzrApplication.ROOT;

		File folder = new File(path);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String fpath = path+ File.separator +filename+"."+FilenameUtils.getExtension(mfile.getOriginalFilename());
		File nfile = new File(fpath);

		if (!mfile.isEmpty()) {
			try {  	    		  
				Files.copy(mfile.getInputStream(), Paths.get(path, filename+"."+FilenameUtils.getExtension(mfile.getOriginalFilename())));
			} catch (IOException|RuntimeException e) {
				System.out.println("aldaa");
				//return null;
			}
		} else {
			System.out.println("empty"); 
			return null;
		}

		String furl= DOW_DIR+ File.separator +filename;

		FileUpload newFile = new FileUpload();
		newFile.setFilename(filename);
		newFile.setFilesize(size);
		newFile.setMimetype(mimeType);
		newFile.setFileurl(furl);
		fileUploadRepository.save(newFile);

		InputStream fis =  mfile.getInputStream();

		Workbook workbook = new XSSFWorkbook(fis);

		int numberOfSheets = workbook.getNumberOfSheets();
		int count=0;
		JSONObject arr= new JSONObject();

		here: for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);

			System.out.println("sheetname: "+sheet.getSheetName());

			if(sheet.getSheetName().equalsIgnoreCase("11.CTT6")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT6 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt6", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				System.out.println("sheetname: sss");
				//List<FinCtt5> datas = ;
				List<FinCtt6> datas = new ArrayList<FinCtt6>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt6 form = new FinCtt6();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();

							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}

								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"11.CTT6"); 
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						datas.add(form);
						// dao.PeaceCrud(form, "FinCtt5", "save", (long) 0, 0, 0, null);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"11.CTT6"); 

			}
			else if(sheet.getSheetName().equalsIgnoreCase("23.TRIAL BALANCE")){
				//dao.getNativeSQLResult("DELETE FROM FIN_TRIAL_BALANCE WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinTrialBalance", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinTrialBalance> datas = new ArrayList<FinTrialBalance>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinTrialBalance form = new FinTrialBalance();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"23.TRIAL BALANCE"); 
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinTrialBalance", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"23.TRIAL BALANCE"); 

			}
			else if(sheet.getSheetName().equalsIgnoreCase("25.CBWS")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CBWS WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCbw", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCbw> datas = new ArrayList<FinCbw>(); 
				for(int k=6; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCbw form = new FinCbw();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"25.CBWS"); 
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						datas.add(form);
						//dao.PeaceCrud(form, "FinCbw", "save", (long) 0, 0, 0, null);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"25.CBWS"); 
			}

			else if(sheet.getSheetName().equalsIgnoreCase( "24.ABWS" )){
				//dao.getNativeSQLResult("DELETE FROM FIN_ABWS WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinAbw", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinAbw> datas = new ArrayList<FinAbw>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinAbw form = new FinAbw();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"24.ABWS");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						datas.add(form);
						// dao.PeaceCrud(form, "FinAbw", "save", (long) 0, 0, 0, null);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"24.ABWS");
			}

			else if(sheet.getSheetName().equalsIgnoreCase( "21.TGT1A" )){
				//dao.getNativeSQLResult("DELETE FROM FIN_TGT1A WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinTgt1a", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinTgt1a> datas = new ArrayList<FinTgt1a>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinTgt1a form = new FinTgt1a();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}

								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"21.TGT1A");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//  dao.PeaceCrud(form, "FinTgt1a", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"21.TGT1A");
			}

			else if(sheet.getSheetName().equalsIgnoreCase( "6.Payroll" )){
				//dao.getNativeSQLResult("DELETE FROM FIN_PAYROLL WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinPayroll", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinPayroll> datas = new ArrayList<FinPayroll>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinPayroll form = new FinPayroll();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));

								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));

								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));

								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));

								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));

								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));

								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"6.Payroll");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						datas.add(form);
						// dao.PeaceCrud(form, "FinAsset", "save", (long) 0, 0, 0, null);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"6.Payroll");
			}

			else if(sheet.getSheetName().equalsIgnoreCase( "5.Inventory" )){
				//dao.getNativeSQLResult("DELETE FROM FIN_INVENTORY WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinInventory", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinInventory> datas = new ArrayList<FinInventory>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinInventory form = new FinInventory();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}

								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"5.Inventory");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						datas.add(form);
						//dao.PeaceCrud(form, "FinInventory", "save", (long) 0, 0, 0, null);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"5.Inventory");
			}

			else if(sheet.getSheetName().equalsIgnoreCase("4.Assets")){
				//dao.getNativeSQLResult("DELETE FROM FIN_ASSETS WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinAsset", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinAsset> datas = new ArrayList<FinAsset>(); 

				System.out.println("!!! asset");
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);

					FinAsset form = new FinAsset();
					Iterator cellIterator = row.cellIterator();

					FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
					while (cellIterator.hasNext()) {

						Cell cell = (Cell) cellIterator.next();
						switch (evaluator.evaluateInCell(cell).getCellType()) 
						{
						case Cell.CELL_TYPE_STRING:
							if (cell.getColumnIndex() == 0) {
								form.setData1(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 1) {
								form.setData2(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 2) {
								form.setData3(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 3) {
								form.setData4(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 4) {
								form.setData5(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 5) {
								form.setData6(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 6) {
								form.setData7(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 7) {
								form.setData8(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 8) {
								form.setData9(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 9) {
								form.setData10(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 10) {
								form.setData11(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 11) {
								form.setData12(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 12) {
								form.setData13(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 13) {
								form.setData14(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 14) {
								form.setData15(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 15) {
								form.setData16(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 16) {
								form.setData17(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 17) {
								form.setData18(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 18) {
								form.setData19(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 19) {
								form.setData20(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 20) {
								form.setData21(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 21) {
								form.setData22(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 22) {
								form.setData23(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 23) {
								form.setData24(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 24) {
								form.setData25(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 25) {
								form.setData26(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 26) {
								form.setData27(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 27) {
								form.setData28(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 28) {
								form.setData29(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 29) {
								form.setData30(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 30) {
								form.setData31(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 31) {
								form.setData32(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 32) {
								form.setData33(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 33) {
								form.setData34(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 34) {
								form.setData35(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 35) {
								form.setData36(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 36) {
								form.setData37(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 37) {
								form.setData38(cell.getStringCellValue());
							}
							if (cell.getColumnIndex() == 38) {
								form.setData39(cell.getStringCellValue());
							}
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (cell.getColumnIndex() == 0) {
								form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 1) {
								form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 2) {
								form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 3) {
								form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 4) {
								form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 5) {
								form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 6) {
								form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 7) {
								form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 8) {
								form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 9) {
								form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 10) {
								form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 11) {
								form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 12) {
								form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 13) {
								form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 14) {
								form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 15) {
								form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 16) {
								form.setData17(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 17) {
								form.setData18(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 18) {
								form.setData19(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 19) {
								form.setData20(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 20) {
								form.setData21(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 21) {
								form.setData22(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 22) {
								form.setData23(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 23) {
								form.setData24(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 24) {
								form.setData25(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 25) {
								form.setData26(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 26) {
								form.setData27(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 27) {
								form.setData28(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 28) {
								form.setData29(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 29) {
								form.setData30(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 30) {
								form.setData31(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 31) {
								form.setData32(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 32) {
								form.setData33(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 33) {
								form.setData34(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 34) {
								form.setData35(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 35) {
								form.setData36(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 36) {
								form.setData37(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 37) {
								form.setData38(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							if (cell.getColumnIndex() == 38) {
								form.setData39(NumberToTextConverter.toText(cell.getNumericCellValue()));
							}
							break;
						case Cell.CELL_TYPE_BLANK:
							if (cell.getColumnIndex() == 1) {
								
								dao.inserBatch(datas,"4.Assets");   
								continue here;
							}
							break;
						}
					}
					form.setOrgcode(orgcode);
					form.setStepid(stepid);
					form.setCyear(ayear);form.setPlanid(mid);
					form.setOrgcatid(orgtype);
					datas.add(form);
					//dao.PeaceCrud(form, "FinAsset", "save", (long) 0, 0, 0, null);
					count = count + 1;
				}
				System.out.println("@@@ ddd"+datas.size());
				dao.inserBatch(datas,"4.Assets");     
			}

			else if(sheet.getSheetName().equalsIgnoreCase("14.Journal")){
				//dao.getNativeSQLResult("DELETE FROM FIN_JOURNAL WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinJournal", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinJournal> datas = new ArrayList<FinJournal>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinJournal form = new FinJournal();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 17) {
									form.setData18(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 18) {
									form.setData19(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 19) {
									form.setData20(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 20) {
									form.setData21(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 17) {
									form.setData18(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 18) {
									form.setData19(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 19) {
									form.setData20(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 20) {
									form.setData21(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;		                                
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"14.Journal");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinJournal", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"3.Journal");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("15.Journal")){
				//dao.getNativeSQLResult("DELETE FROM FIN_JOURNAL WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinJournal", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinJournal> datas = new ArrayList<FinJournal>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinJournal form = new FinJournal();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 17) {
									form.setData18(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 18) {
									form.setData19(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 19) {
									form.setData20(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 20) {
									form.setData21(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 17) {
									form.setData18(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 18) {
									form.setData19(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 19) {
									form.setData20(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 20) {
									form.setData21(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;		                                
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"3.Journal");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinJournal", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"3.Journal");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("19.Budget")){
				//dao.getNativeSQLResult("DELETE FROM FIN_BUDGET WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinBudget", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinBudget> datas = new ArrayList<FinBudget>(); 
				for(int k=5; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinBudget form = new FinBudget();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"19.Budget");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						datas.add(form);
						//dao.PeaceCrud(form, "FinBudget", "save", (long) 0, 0, 0, null);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"19.Budget");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("13.CTT8")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT8 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt8", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt8> datas = new ArrayList<FinCtt8>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt8 form = new FinCtt8();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"13.CTT8");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCtt8", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"13.CTT8");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("14.CTT9")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT9 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt9", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt9> datas = new ArrayList<FinCtt9>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt9 form = new FinCtt9();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"14.CTT9");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCtt8", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"14.CTT9");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("12.CTT7")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT7 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt7", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt7> datas = new ArrayList<FinCtt7>(); 
				for(int k=9; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt7 form = new FinCtt7();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"12.CTT7");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCtt7", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"12.CTT7");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("7.CTT2")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT2 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt2", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt2> datas = new ArrayList<FinCtt2>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt2 form = new FinCtt2();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"7.CTT2");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCtt8", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"7.CTT2");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("10.CTT5")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT5 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt5", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt5> datas = new ArrayList<FinCtt5>(); 
				for(int k=8; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt5 form = new FinCtt5();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									dao.inserBatch(datas,"10.CTT5");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						// dao.PeaceCrud(form, "FinCtt5", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true); 
					}
				}
				
				dao.inserBatch(datas,"10.CTT5");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("9.CTT4")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT4 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt4", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt4> datas = new ArrayList<FinCtt4>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt4 form = new FinCtt4();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"9.CTT4");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCtt3", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true); 
					}
				}
				
				dao.inserBatch(datas,"9.CTT4");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("8.CTT3")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT3 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt3", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt3> datas = new ArrayList<FinCtt3>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt3 form = new FinCtt3();
						Iterator cellIterator = row.cellIterator();

						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 15) {
									form.setData16(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 16) {
									form.setData17(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									dao.inserBatch(datas,"8.CTT3");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setCyear(ayear);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCtt2", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				dao.inserBatch(datas,"8.CTT3");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("6.CTT1")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CTT1 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCtt1", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCtt1> datas = new ArrayList<FinCtt1>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCtt1 form = new FinCtt1();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									form.setData8(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 8) {
									form.setData9(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 9) {
									form.setData10(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 10) {
									form.setData11(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 11) {
									form.setData12(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 12) {
									form.setData13(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 13) {
									form.setData14(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 14) {
									form.setData15(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									dao.inserBatch(datas,"6.CTT1");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCtt1", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				dao.inserBatch(datas,"6.CTT1");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("22.NT2")){
				//dao.getNativeSQLResult("DELETE FROM FIN_NT2 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinNt2", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinNt2> datas = new ArrayList<FinNt2>(); 
				for(int k=6; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinNt2 form = new FinNt2();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}

								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									dao.inserBatch(datas,"22.NT2");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinNt2", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				dao.inserBatch(datas,"22.NT2");
			}
			else if(sheet.getSheetName().equalsIgnoreCase("20.TGT1")){
				//dao.getNativeSQLResult("DELETE FROM FIN_TGT1 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinTgt1", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinTgt1> datas = new ArrayList<FinTgt1>();
				System.out.println("sheet.getLastRowNum() ===== " + sheet.getLastRowNum());
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinTgt1 form = new FinTgt1();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 2) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 7) {
									long a=0;
									CellValue cellValue = evaluator.evaluate(cell);
									a=(long) cellValue.getNumberValue();

									//form.setData6("as");
									form.setData6(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 2) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 7) {
									long a=0;
									CellValue cellValue = evaluator.evaluate(cell);
									a=(long) cellValue.getNumberValue();

									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_FORMULA:		                            
								if (cell.getColumnIndex() == 7) {
									long a=0;
									CellValue cellValue = evaluator.evaluate(cell);
									a=(long) cellValue.getNumberValue();

									form.setData6(String.valueOf(a));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 3) {
									dao.inserBatch(datas,"20.TGT1");   
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);
						form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinTgt1", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				dao.inserBatch(datas,"20.TGT1");     
			}
			else if(sheet.getSheetName().equalsIgnoreCase("5.CT4A")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CT4A WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCt4a", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCt4a> datas = new ArrayList<FinCt4a>(); 
				for(int k=6; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCt4a form = new FinCt4a();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 5) {
									form.setData6(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 6) {
									form.setData7(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"5.CT4A"); 
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCt4a", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"5.CT4A"); 
			}
			else if(sheet.getSheetName().equalsIgnoreCase("4.CT3A")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CT3A WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCt3a", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCt3a> datas = new ArrayList<FinCt3a>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCt3a form = new FinCt3a();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"4.CT3A");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinCt3a", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				
				dao.inserBatch(datas,"4.CT3A"); 
			}
			else if(sheet.getSheetName().equalsIgnoreCase("3.CT2A")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CT2A WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCt2a", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCt2a> datas = new ArrayList<FinCt2a>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCt2a form = new FinCt2a();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:		                            	
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"st2a");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinSt2a", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				System.out.println("end bn"+datas.size());
				dao.inserBatch(datas,"st2a"); 




			}
			else if(sheet.getSheetName().equalsIgnoreCase("2.CT1A")){
				//dao.getNativeSQLResult("DELETE FROM FIN_CT1A WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
				dao.PeaceCrud(null, "FinCt1a", "multidelete", (long) 0, 0, 0, "where planid="+mid+" and stepid="+stepid+"");
				List<FinCt1a> datas = new ArrayList<FinCt1a>(); 
				for(int k=7; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinCt1a form = new FinCt1a();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:
								if (cell.getColumnIndex() == 0) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(cell.getStringCellValue());
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (cell.getColumnIndex() == 0) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 1) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"2.CT1A");
									continue here;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);
						form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						//dao.PeaceCrud(form, "FinSt1a", "save", (long) 0, 0, 0, null);
						datas.add(form);
						count = count + 1;
					}  catch (Exception e) {
						arr.put("count",count-1);
						arr.put("response",true);
					}
				}
				System.out.println("@@@###"+datas.size());
				dao.inserBatch(datas,"2.CT1A");                

			}

		}

		Sheet sheet = workbook.getSheet("2.CT1A");
		
		String[] codes241Str = {"1","31","32","33","34","35","36","2","37","39","4","41","42","5","6"};
		Integer[] codes241Num = {1,31,32,33,34,35,36,2,37,39,4,41,42,5,6};
		JSONObject datas241 = new JSONObject();
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Row tempRow = sheet.getRow(0);
		for(int kk=7;kk<sheet.getLastRowNum();kk++){
			Row currentRow = sheet.getRow(kk);
			if (currentRow != null){
				Cell balanceCodeCell = currentRow.getCell(0);
				if (balanceCodeCell != null){
					for(int jj=0; jj<codes241Num.length;jj++){
						Cell cell2 = tempRow.createCell(1);
						cell2.setCellFormula("value(A"+(kk+1)+")");
						CellValue cellValue2 = evaluator.evaluate(cell2);
						
						if (((cellValue2.getCellType() == Cell.CELL_TYPE_STRING) && (cellValue2.getStringValue().equalsIgnoreCase(codes241Str[jj]))) || ((cellValue2.getCellType() == Cell.CELL_TYPE_NUMERIC) && (cellValue2.getNumberValue() == codes241Num[jj]))){
							JSONObject currentRowData = new JSONObject();
							for(int cellIterator=0; cellIterator<=3;cellIterator++){
								Cell currentCellData = currentRow.getCell(cellIterator);
								if (currentCellData != null){
									if (currentCellData.getCellType() == Cell.CELL_TYPE_FORMULA){
										cell2 = tempRow.createCell(2);
										cell2.setCellFormula(currentCellData.getCellFormula());
										CellValue cellValueData = evaluator.evaluate(cell2);
										currentRowData.put("cell"+cellIterator, (cellValueData.getCellType() == Cell.CELL_TYPE_STRING) ? cellValueData.getStringValue() : ((cellValueData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValueData.getNumberValue() : ""));
									}
									else{
										currentRowData.put("cell"+cellIterator, (currentCellData.getCellType() == Cell.CELL_TYPE_STRING) ? currentCellData.getStringCellValue() : ((currentCellData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? currentCellData.getNumericCellValue() : ""));
									}
								}
							}
							datas241.put("row"+jj, currentRowData);
						}
					}
				}
			}
		}
		
		List<FsForm241> fs241 = new ArrayList<FsForm241>(); 
		for(int jj=0; jj<codes241Str.length;jj++){
			if (datas241.has("row" + jj) && !datas241.isNull("row" + jj)){
				JSONObject dataObj = datas241.getJSONObject("row" + jj);
				FsForm241 rrow2 = new FsForm241();
				
				if (dataObj.has("cell0") && !dataObj.isNull("cell0")){
					Object dataCell = dataObj.get("cell0");
					if(dataCell instanceof Integer){
						rrow2.setAccCode(String.valueOf(dataObj.getInt("cell0")));
					}
					else if (dataCell instanceof String){
						rrow2.setAccCode(dataObj.getString("cell0"));
					}
					else if(dataCell instanceof Double){
						rrow2.setAccCode(String.valueOf((long)dataObj.getDouble("cell0")));
					}
					else if(dataCell instanceof Long){
						rrow2.setAccCode(String.valueOf(dataObj.getLong("cell0")));
					}
				}
				
				if (dataObj.has("cell1") && !dataObj.isNull("cell1")){
					Object dataCell = dataObj.get("cell1");
					if(dataCell instanceof Integer){
						rrow2.setAccName(String.valueOf(dataObj.getInt("cell1")));
					}
					else if (dataCell instanceof String){
						rrow2.setAccName(dataObj.getString("cell1"));
					}
					else if(dataCell instanceof Double){
						rrow2.setAccName(String.valueOf(dataObj.getDouble("cell1")));
					}
					else if(dataCell instanceof Long){
						rrow2.setAccName(String.valueOf(dataObj.getLong("cell1")));
					}
				}
				
				if (dataObj.has("cell2") && !dataObj.isNull("cell2")){
					Object dataCell = dataObj.get("cell2");
					if(dataCell instanceof Integer){
						rrow2.setData2(dataObj.getInt("cell2"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell2").trim().length() > 0){
						rrow2.setData2(Double.parseDouble(dataObj.getString("cell2")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData2(dataObj.getDouble("cell2"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData2(dataObj.getLong("cell2"));
					}
				}
				
				if (dataObj.has("cell3") && !dataObj.isNull("cell3")){
					Object dataCell = dataObj.get("cell3");
					if(dataCell instanceof Integer){
						rrow2.setData3(dataObj.getInt("cell3"));
					}
					else if ((dataCell instanceof String) && (dataObj.getString("cell3").trim().length() > 0)){
						rrow2.setData3(Double.parseDouble(dataObj.getString("cell3")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData3(dataObj.getDouble("cell3"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData3(dataObj.getLong("cell3"));
					}
				}
				
				rrow2.setData6(rrow2.getData3() - rrow2.getData2());
				rrow2.setData7((rrow2.getData2() == 0) ? 0 : (rrow2.getData3()/rrow2.getData2()*100-100));
				rrow2.setPlanid(mid);
				rrow2.setOrderid(jj);
				fs241.add(rrow2);
			}
		}
		
		dao.getNativeSQLResult("DELETE FROM FS_FORM_241 WHERE PLANID='" + mid + "'", "delete"); 
		dao.inserBatch(fs241,"fs241");


		sheet = workbook.getSheet("20.TGT1");
		String[] codes242Str = {"3","131","1310","1340","120004","122","14","6","61","62","63","64","65"};
		Integer[] codes242Num = {3,131,1310,1340,120004,122,14,6,61,62,63,64,65};
		datas241 = new JSONObject();
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		tempRow = sheet.getRow(0);
		for(int kk=7;kk<sheet.getLastRowNum();kk++){
			Row currentRow = sheet.getRow(kk);
			if (sheet.isColumnHidden(0)){
				Cell balanceCodeCell = currentRow.getCell(2);
				if (balanceCodeCell != null){
					for(int jj=0; jj<codes242Num.length;jj++){
						Cell cell2 = tempRow.createCell(1);
						cell2.setCellFormula("value(C"+(kk+1)+")");
						CellValue cellValue2 = evaluator.evaluate(cell2);
						
						if (((cellValue2.getCellType() == Cell.CELL_TYPE_STRING) && (cellValue2.getStringValue().equalsIgnoreCase(codes242Str[jj]))) || ((cellValue2.getCellType() == Cell.CELL_TYPE_NUMERIC) && (cellValue2.getNumberValue() == codes242Num[jj]))){
							JSONObject currentRowData = new JSONObject();
							for(int cellIterator=2; cellIterator<=7;cellIterator++){
								Cell currentCellData = currentRow.getCell(cellIterator);
								if(currentCellData!=null){
									if (currentCellData.getCellType() == Cell.CELL_TYPE_FORMULA){
										cell2 = tempRow.createCell(2);
										cell2.setCellFormula(currentCellData.getCellFormula());
										CellValue cellValueData = evaluator.evaluate(cell2);
										currentRowData.put("cell"+cellIterator, (cellValueData.getCellType() == Cell.CELL_TYPE_STRING) ? cellValueData.getStringValue() : ((cellValueData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValueData.getNumberValue() : ""));
									}
									else{
										currentRowData.put("cell"+cellIterator, (currentCellData.getCellType() == Cell.CELL_TYPE_STRING) ? currentCellData.getStringCellValue() : ((currentCellData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? currentCellData.getNumericCellValue() : ""));
									}
								}
								
							}
							datas241.put("row"+jj, currentRowData);
						}
					}
				}
			}
			else{
				Cell balanceCodeCell = currentRow.getCell(2);
				if (balanceCodeCell != null){
					for(int jj=0; jj<codes242Num.length;jj++){
						Cell cell2 = tempRow.createCell(1);
						cell2.setCellFormula("value(A"+(kk+1)+")");
						CellValue cellValue2 = evaluator.evaluate(cell2);
						
						if (((cellValue2.getCellType() == Cell.CELL_TYPE_STRING) && (cellValue2.getStringValue().equalsIgnoreCase(codes242Str[jj]))) || ((cellValue2.getCellType() == Cell.CELL_TYPE_NUMERIC) && (cellValue2.getNumberValue() == codes242Num[jj]))){
							JSONObject currentRowData = new JSONObject();
							for(int cellIterator=0; cellIterator<=5;cellIterator++){
								Cell currentCellData = currentRow.getCell(cellIterator);
								if (currentCellData.getCellType() == Cell.CELL_TYPE_FORMULA){
									cell2 = tempRow.createCell(2);
									cell2.setCellFormula(currentCellData.getCellFormula());
									CellValue cellValueData = evaluator.evaluate(cell2);
									currentRowData.put("cell"+cellIterator, (cellValueData.getCellType() == Cell.CELL_TYPE_STRING) ? cellValueData.getStringValue() : ((cellValueData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValueData.getNumberValue() : ""));
								}
								else{
									currentRowData.put("cell"+(cellIterator+2), (currentCellData.getCellType() == Cell.CELL_TYPE_STRING) ? currentCellData.getStringCellValue() : ((currentCellData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? currentCellData.getNumericCellValue() : ""));
								}
							}
							datas241.put("row"+jj, currentRowData);
						}
					}
				}
			}
		}
		
		List<FsForm242> fs242 = new ArrayList<FsForm242>(); 
		for(int jj=0; jj<codes242Str.length;jj++){
			if (datas241.has("row" + jj) && !datas241.isNull("row" + jj)){
				JSONObject dataObj = datas241.getJSONObject("row" + jj);
				FsForm242 rrow2 = new FsForm242();
				
				if (dataObj.has("cell2") && !dataObj.isNull("cell2")){
					Object dataCell = dataObj.get("cell2");
					if(dataCell instanceof Integer){
						rrow2.setAccCode(String.valueOf(dataObj.getInt("cell2")));
					}
					else if (dataCell instanceof String){
						rrow2.setAccCode(dataObj.getString("cell2"));
					}
					else if(dataCell instanceof Double){
						rrow2.setAccCode(String.valueOf((long)dataObj.getDouble("cell2")));
					}
					else if(dataCell instanceof Long){
						rrow2.setAccCode(String.valueOf(dataObj.getLong("cell2")));
					}
				}
				
				if (dataObj.has("cell3") && !dataObj.isNull("cell3")){
					Object dataCell = dataObj.get("cell3");
					if(dataCell instanceof Integer){
						rrow2.setAccName(String.valueOf(dataObj.getInt("cell3")));
					}
					else if (dataCell instanceof String){
						rrow2.setAccName(dataObj.getString("cell3"));
					}
					else if(dataCell instanceof Double){
						rrow2.setAccName(String.valueOf(dataObj.getDouble("cell3")));
					}
					else if(dataCell instanceof Long){
						rrow2.setAccName(String.valueOf(dataObj.getLong("cell3")));
					}
				}
				
				if (dataObj.has("cell4") && !dataObj.isNull("cell4")){
					Object dataCell = dataObj.get("cell4");
					if(dataCell instanceof Integer){
						rrow2.setData1(dataObj.getInt("cell4"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell4").trim().length() > 0){
						rrow2.setData1(Double.parseDouble(dataObj.getString("cell4")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData1(dataObj.getDouble("cell4"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData1(dataObj.getLong("cell4"));
					}
				}
				
				if (dataObj.has("cell5") && !dataObj.isNull("cell5")){
					Object dataCell = dataObj.get("cell5");
					if(dataCell instanceof Integer){
						rrow2.setData2(dataObj.getInt("cell5"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell5").trim().length() > 0){
						rrow2.setData2(Double.parseDouble(dataObj.getString("cell5")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData2(dataObj.getDouble("cell5"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData2(dataObj.getLong("cell5"));
					}
				}
				
				if (dataObj.has("cell6") && !dataObj.isNull("cell6")){
					Object dataCell = dataObj.get("cell6");
					if(dataCell instanceof Integer){
						rrow2.setData3(dataObj.getInt("cell6"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell6").trim().length() > 0){
						rrow2.setData3(Double.parseDouble(dataObj.getString("cell6")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData3(dataObj.getDouble("cell6"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData3(dataObj.getLong("cell6"));
					}
				}
				
				if (dataObj.has("cell7") && !dataObj.isNull("cell7")){
					Object dataCell = dataObj.get("cell7");
					if(dataCell instanceof Integer){
						rrow2.setData4(dataObj.getInt("cell7"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell7").trim().length() > 0){
						rrow2.setData4(Double.parseDouble(dataObj.getString("cell7")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData4(dataObj.getDouble("cell7"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData4(dataObj.getLong("cell7"));
					}
				}
				
				rrow2.setData5(rrow2.getData3() - rrow2.getData1());
				rrow2.setData6(rrow2.getData4() - rrow2.getData2());
				rrow2.setData7(rrow2.getData4() - rrow2.getData3());
				rrow2.setData8((rrow2.getData3() == 0) ? 0 : (rrow2.getData4()/rrow2.getData3()*100-100));
				rrow2.setPlanid(mid);
				rrow2.setOrderid(jj);
				fs242.add(rrow2);
			}
		}
		
		dao.getNativeSQLResult("DELETE FROM FS_FORM_242 WHERE PLANID='" + mid + "'", "delete"); 
		dao.inserBatch(fs242,"fs242");  
		
		String[] codes243Str = {"2","21","210","2101","2102","2103","2104","2105","2106","2107","2108","2109","211","2111","2112","212","2121","2122","213","2131","2132","22","2200","2260","23"};
		Integer[] codes243Num = {2,21,210,2101,2102,2103,2104,2105,2106,2107,2108,2109,211,2111,2112,212,2121,2122,213,2131,2132,22,2200,2260,23};
		JSONObject datas243 = new JSONObject();
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		tempRow = sheet.getRow(0);
		for(int kk=7;kk<sheet.getLastRowNum();kk++){
			Row currentRow = sheet.getRow(kk);
			
			if (sheet.isColumnHidden(0)){
				Cell balanceCodeCell = currentRow.getCell(2);
				if (balanceCodeCell != null){
					for(int jj=0; jj<codes243Num.length;jj++){
						Cell cell2 = tempRow.createCell(1);
						cell2.setCellFormula("value(C"+(kk+1)+")");
						CellValue cellValue2 = evaluator.evaluate(cell2);
						
						if (((cellValue2.getCellType() == Cell.CELL_TYPE_STRING) && (cellValue2.getStringValue().equalsIgnoreCase(codes243Str[jj]))) || ((cellValue2.getCellType() == Cell.CELL_TYPE_NUMERIC) && (cellValue2.getNumberValue() == codes243Num[jj]))){
							JSONObject currentRowData = new JSONObject();
							for(int cellIterator=2; cellIterator<=7;cellIterator++){
								Cell currentCellData = currentRow.getCell(cellIterator);
								if(currentCellData!=null){
									if (currentCellData.getCellType() == Cell.CELL_TYPE_FORMULA){
										cell2 = tempRow.createCell(2);
										cell2.setCellFormula(currentCellData.getCellFormula());
										CellValue cellValueData = evaluator.evaluate(cell2);
										currentRowData.put("cell"+cellIterator, (cellValueData.getCellType() == Cell.CELL_TYPE_STRING) ? cellValueData.getStringValue() : ((cellValueData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValueData.getNumberValue() : ""));
									}
									else{
										currentRowData.put("cell"+cellIterator, (currentCellData.getCellType() == Cell.CELL_TYPE_STRING) ? currentCellData.getStringCellValue() : ((currentCellData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? currentCellData.getNumericCellValue() : ""));
									}
								}
								
							}
							datas243.put("row"+jj, currentRowData);
						}
					}
				}
			}
			else{
				Cell balanceCodeCell = currentRow.getCell(0);
				if (balanceCodeCell != null){
					for(int jj=0; jj<codes243Num.length;jj++){
						Cell cell2 = tempRow.createCell(1);
						cell2.setCellFormula("value(A"+(kk+1)+")");
						CellValue cellValue2 = evaluator.evaluate(cell2);
						
						if (((cellValue2.getCellType() == Cell.CELL_TYPE_STRING) && (cellValue2.getStringValue().equalsIgnoreCase(codes243Str[jj]))) || ((cellValue2.getCellType() == Cell.CELL_TYPE_NUMERIC) && (cellValue2.getNumberValue() == codes243Num[jj]))){
							JSONObject currentRowData = new JSONObject();
							for(int cellIterator=0; cellIterator<=5;cellIterator++){
								Cell currentCellData = currentRow.getCell(cellIterator);
								if (currentCellData.getCellType() == Cell.CELL_TYPE_FORMULA){
									cell2 = tempRow.createCell(2);
									cell2.setCellFormula(currentCellData.getCellFormula());
									CellValue cellValueData = evaluator.evaluate(cell2);
									currentRowData.put("cell"+cellIterator, (cellValueData.getCellType() == Cell.CELL_TYPE_STRING) ? cellValueData.getStringValue() : ((cellValueData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValueData.getNumberValue() : ""));
								}
								else{
									currentRowData.put("cell"+(cellIterator+2), (currentCellData.getCellType() == Cell.CELL_TYPE_STRING) ? currentCellData.getStringCellValue() : ((currentCellData.getCellType() == Cell.CELL_TYPE_NUMERIC) ? currentCellData.getNumericCellValue() : ""));
								}
							}
							datas243.put("row"+jj, currentRowData);
						}
					}
				}
			}
		}
		//System.out.println(datas243.toString());
		List<FsForm243> fs243 = new ArrayList<FsForm243>(); 
		for(int jj=0; jj<codes243Str.length;jj++){
			if (datas243.has("row" + jj) && !datas243.isNull("row" + jj)){
				JSONObject dataObj = datas243.getJSONObject("row" + jj);
				FsForm243 rrow2 = new FsForm243();
				
				if (dataObj.has("cell2") && !dataObj.isNull("cell2")){
					Object dataCell = dataObj.get("cell2");
					if(dataCell instanceof Integer){
						rrow2.setAccCode(String.valueOf(dataObj.getInt("cell2")));
					}
					else if (dataCell instanceof String){
						rrow2.setAccCode(dataObj.getString("cell2"));
					}
					else if(dataCell instanceof Double){
						rrow2.setAccCode(String.valueOf((long)dataObj.getDouble("cell2")));
					}
					else if(dataCell instanceof Long){
						rrow2.setAccCode(String.valueOf(dataObj.getLong("cell2")));
					}
				}
				
				if (dataObj.has("cell3") && !dataObj.isNull("cell3")){
					Object dataCell = dataObj.get("cell3");
					if(dataCell instanceof Integer){
						rrow2.setAccName(String.valueOf(dataObj.getInt("cell3")));
					}
					else if (dataCell instanceof String){
						rrow2.setAccName(dataObj.getString("cell3"));
					}
					else if(dataCell instanceof Double){
						rrow2.setAccName(String.valueOf(dataObj.getDouble("cell3")));
					}
					else if(dataCell instanceof Long){
						rrow2.setAccName(String.valueOf(dataObj.getLong("cell3")));
					}
				}
				
				if (dataObj.has("cell4") && !dataObj.isNull("cell4")){
					Object dataCell = dataObj.get("cell4");
					if(dataCell instanceof Integer){
						rrow2.setData1(dataObj.getInt("cell4"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell4").trim().length() > 0){
						rrow2.setData1(Double.parseDouble(dataObj.getString("cell4")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData1(dataObj.getDouble("cell4"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData1(dataObj.getLong("cell4"));
					}
				}
				
				if (dataObj.has("cell5") && !dataObj.isNull("cell5")){
					Object dataCell = dataObj.get("cell5");
					if(dataCell instanceof Integer){
						rrow2.setData2(dataObj.getInt("cell5"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell5").trim().length() > 0){
						rrow2.setData2(Double.parseDouble(dataObj.getString("cell5")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData2(dataObj.getDouble("cell5"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData2(dataObj.getLong("cell5"));
					}
				}
				
				if (dataObj.has("cell6") && !dataObj.isNull("cell6")){
					Object dataCell = dataObj.get("cell6");
					if(dataCell instanceof Integer){
						rrow2.setData3(dataObj.getInt("cell6"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell6").trim().length() > 0){
						rrow2.setData3(Double.parseDouble(dataObj.getString("cell6")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData3(dataObj.getDouble("cell6"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData3(dataObj.getLong("cell6"));
					}
				}
				
				if (dataObj.has("cell7") && !dataObj.isNull("cell7")){
					Object dataCell = dataObj.get("cell7");
					if(dataCell instanceof Integer){
						rrow2.setData4(dataObj.getInt("cell7"));
					}
					else if (dataCell instanceof String && dataObj.getString("cell7").trim().length() > 0){
						rrow2.setData4(Double.parseDouble(dataObj.getString("cell7")));
					}
					else if(dataCell instanceof Double){
						rrow2.setData4(dataObj.getDouble("cell7"));
					}
					else if(dataCell instanceof Long){
						rrow2.setData4(dataObj.getLong("cell7"));
					}
				}
				
				rrow2.setData5(rrow2.getData3() - rrow2.getData1());
				rrow2.setData6(rrow2.getData4() - rrow2.getData2());
				rrow2.setData7(rrow2.getData4() - rrow2.getData3());
				rrow2.setData8((rrow2.getData5() == 0) ? 0 : (rrow2.getData6()/rrow2.getData5()*100-100));
				rrow2.setPlanid(mid);
				rrow2.setOrderid(jj);
				fs243.add(rrow2);
			}
		}
		
		dao.getNativeSQLResult("DELETE FROM FS_FORM_243 WHERE PLANID='" + mid + "'", "delete"); 
		dao.inserBatch(fs243,"fs243");  

		List<LutValidation> tu=(List<LutValidation>) dao.getHQLResult("from LutValidation t order by t.id desc","list");
		dao.getNativeSQLResult("DELETE FROM STS_CHECK_VARIABLES WHERE PLANID='" + mid + "' and stepid = " + stepid, "delete"); 
		if(stepid==3){
			dao.PeaceCrud(null, "LnkMainFormT2", "multidelete", (long) 0, 0, 0, "where rtype=2 and mid="+mid+" and stepid="+stepid+""); 
		}
		/*if(stepid==4){
			dao.PeaceCrud(null, "LnkMainFormT2", "multidelete", (long) 0, 0, 0, "where mid="+mid+" and stepid="+stepid+""); 
		}*/
		List<StsCheckVariable> datas = new ArrayList<StsCheckVariable>(); 
		String str="";
			for(int k=0; k<tu.size();k++){

				StsCheckVariable sts=new StsCheckVariable();
				long a=0;
				long b=0;
				Row row = sheet.createRow(k);
				if (tu.get(k).getIsformula1() == 1 && tu.get(k).getIsformula2() == 1) {
					Cell cell1 = row.createCell(0);
					cell1.setCellFormula(tu.get(k).getPosition1());
					CellValue cellValue1 = evaluator.evaluate(cell1);

					a = (long) cellValue1.getNumberValue();
					sts.setData6(a);

					Cell cell2 = row.createCell(1);
					cell2.setCellFormula(tu.get(k).getPosition2());
					CellValue cellValue2 = evaluator.evaluate(cell2);
					b = (long) cellValue2.getNumberValue();
					sts.setData9(b);

					
					sts.setData1(tu.get(k).getTitle1());
					sts.setData2(tu.get(k).getTitle2());
					sts.setData3(tu.get(k).getBalanceid());
					sts.setData4(tu.get(k).getCode1());
					sts.setData5(tu.get(k).getPosition1());
					sts.setData7(tu.get(k).getCode2());
					sts.setData8(tu.get(k).getPosition2());
					sts.setOrgcode(orgcode);
					sts.setStepid(stepid);
					sts.setPlanid(mid);
					sts.setOrgcatid(orgtype);
					sts.setData10(sts.getData6() - sts.getData9());
					sts.setValid(tu.get(k).getValid());
					if(sts.getData6() - sts.getData9()>1 || sts.getData6() - sts.getData9()<-1){
						if(tu.get(k).getFactorid()!=null && tu.get(k).getTryoutid()!=null){
							if(tu.get(k).getFactorid()>0 && tu.get(k).getTryoutid()>0){
								LnkMainFormT2 t2= new  LnkMainFormT2();
								t2.setDirid(tu.get(k).getAdirid());
								t2.setFactorid(tu.get(k).getFactorid());
								t2.setRiskid(tu.get(k).getRiskid());
								t2.setRtype(2);
								t2.setDecid(1);
								t2.setMid(mid);
								t2.setStepid(stepid);
								t2.setDescription(tu.get(k).getTitle1()+" - "+tu.get(k).getTitle2());
								dao.PeaceCrud(t2, "LnkMainFormT2", "save",(long) 0, 0, 0, null);
								
								LnkRiskT2 tt=new LnkRiskT2();
							    tt.setT2id(t2.getId());
							    tt.setRiskid(t2.getRiskid());
			    			    tt.setDirid(tu.get(k).getAdirid());
			    			    tt.setTryid(tu.get(k).getTryoutid());
			    			    tt.setRtype(2);
			    			    tt.setDescription(tu.get(k).getTitle1()+" - "+tu.get(k).getTitle2());					    			  
			    			    dao.PeaceCrud(tt, "LnkRiskT2", "save", (long) 0, 0, 0, null);
			    			    System.out.println("end1");
			    			    if(stepid==4){
			    			    	  System.out.println("end2");			    			    	 
			    			    	  LnkRiskT2 lk=tt;
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
						    		 /* for(LnkRiskLaw item:ris.getLnkRiskLaws()){
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
						    		  }*/
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
						    		  lrt.setData21(" "+String.valueOf(sts.getData6() - sts.getData9()));
						    		  lrt.setData23(0);
						    		  lrt.setData25(0);
						    		  lrt.setData26(String.valueOf(stepid));
						    		  lrt.setMid(mid);
						    		  if(ll.size()>0){						    			  
						    			  dao.PeaceCrud(lrt, "LnkRiskTryout", "update", (long) lrt.getId(), 0, 0, null);
						    		  }
						    		  else{
						    			  dao.PeaceCrud(lrt, "LnkRiskTryout", "save", (long) 0, 0, 0, null);
						    		  }
						    		  str=str+","+lrt.getId();
			    			    }						
							}
						}					
						
					}
					datas.add(sts);
				}
			}
	
		 System.out.println("@@"+str);
		 System.out.println("@@"+str.length());
		 if(str.length()>0){
			 List<LnkRiskTryout> ll=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+mid+" and t.id not in ("+str.substring(1, str.length())+")", "list"); 
			 for(LnkRiskTryout item:ll){
				 if(item.getLnkRiskT2().getLnkMainFormT2().getStepid()==4 && item.getLnkRiskT2().getLnkMainFormT2().getRtype()==2){
					 dao.PeaceCrud(item, "LnkRiskTryout", "delete", (long) item.getId(), 0, 0, null);
				 }
			 }
		 }
		 else{
			 List<LnkRiskTryout> ll=(List<LnkRiskTryout>) dao.getHQLResult("from LnkRiskTryout t where t.mid="+mid+"", "list"); 
			 for(LnkRiskTryout item:ll){
				 if(item.getLnkRiskT2().getLnkMainFormT2().getStepid()==4 && item.getLnkRiskT2().getLnkMainFormT2().getRtype()==2 && item.getData17()==null && item.getData18()==null && item.getData19()==null && item.getData20()==null && item.getData22()==null){
					 System.out.println("$$"+item.getId());
					 dao.PeaceCrud(item, "LnkRiskTryout", "delete", (long) item.getId(), 0, 0, null);
				 }
			 }
		 }
		 

		 dao.inserBatch(datas,"tulgalt"); 

		// materiallag bdlig todorhoiloh
		dao.getNativeSQLResult("DELETE FROM FS_FORM_A4 WHERE PLANID='" + mid + "' and STEPID='"+stepid+"'", "delete");
		//niit orlogo
		sheet = workbook.createSheet("A4");
		Row row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellFormula("VLOOKUP(1,'3.CT2A'!A8:D308,4,FALSE)+VLOOKUP(145,'3.CT2A'!A8:D308,4,FALSE)");
		CellValue cellValue1 = evaluator.evaluate(cell1);
		Double sumA4 = cellValue1.getNumberValue();
		FsFormA4 a4 = new FsFormA4();
		a4.setData1("1");
		a4.setData2(String.valueOf(sumA4));
		a4.setData3(String.valueOf(sumA4/100*0.2));
		a4.setData4(String.valueOf(sumA4/100));
		a4.setData5(String.valueOf(sumA4/100*2));
		a4.setPlanid(mid);
		a4.setStepid(stepid);
		a4.setOrgcode(orgcode);
		a4.setOrgcatid(orgtype);
		a4.setCyear(ayear);
		a4.setIsselect(0);
		a4.setPercent("0");
		dao.PeaceCrud(a4, "FsFormA4", "save", (long) 0, 0, 0, null);
		
		cell1 = row.createCell(1);
		cell1.setCellFormula("VLOOKUP(2,'3.CT2A'!A8:D308,4,FALSE)+VLOOKUP(225,'3.CT2A'!A8:D308,4,FALSE)");
		cellValue1 = evaluator.evaluate(cell1);
		sumA4 = cellValue1.getNumberValue();
		a4 = new FsFormA4();
		a4.setData1("2");
		a4.setData2(String.valueOf(sumA4));
		a4.setData3(String.valueOf(sumA4/100*0.2));
		a4.setData4(String.valueOf(sumA4/100));
		a4.setData5(String.valueOf(sumA4/100*2));
		a4.setPlanid(mid);
		a4.setStepid(stepid);
		a4.setOrgcode(orgcode);
		a4.setOrgcatid(orgtype);
		a4.setCyear(ayear);
		a4.setIsselect(0);
		a4.setPercent("0");
		dao.PeaceCrud(a4, "FsFormA4", "save", (long) 0, 0, 0, null);
		
		cell1 = row.createCell(1);
		cell1.setCellFormula("VLOOKUP(3,'2.CT1A'!A8:D308,4,FALSE)");
		cellValue1 = evaluator.evaluate(cell1);
		sumA4 = cellValue1.getNumberValue();
		
		a4 = new FsFormA4();
		a4.setData1("3");
		a4.setData2(String.valueOf(sumA4));
		a4.setData3(String.valueOf(sumA4/100*0.2));
		a4.setData4(String.valueOf(sumA4/100));
		a4.setData5(String.valueOf(sumA4/100*2));
		a4.setPlanid(mid);
		a4.setStepid(stepid);
		a4.setOrgcode(orgcode);
		a4.setOrgcatid(orgtype);
		a4.setCyear(ayear);
		a4.setIsselect(0);
		a4.setPercent("0");
		dao.PeaceCrud(a4, "FsFormA4", "save", (long) 0, 0, 0, null);
		
		cell1 = row.createCell(1);
		cell1.setCellFormula("VLOOKUP(5,'2.CT1A'!A8:D308,4,FALSE)");
		cellValue1 = evaluator.evaluate(cell1);
		sumA4 = cellValue1.getNumberValue();
		
		a4 = new FsFormA4();
		a4.setData1("4");
		a4.setData2(String.valueOf(sumA4));
		a4.setData3(String.valueOf(sumA4/100*0.2));
		a4.setData4(String.valueOf(sumA4/100));
		a4.setData5(String.valueOf(sumA4/100*2));
		a4.setPlanid(mid);
		a4.setStepid(stepid);
		a4.setOrgcode(orgcode);
		a4.setOrgcatid(orgtype);
		a4.setCyear(ayear);
		a4.setIsselect(0);
		a4.setPercent("0");
		dao.PeaceCrud(a4, "FsFormA4", "save", (long) 0, 0, 0, null);

		fis.close();

		return newFile;

	}

	// Upload from the other file
	public FileUpload uploadfromExcel(MultipartFile mfile,long mid,String ayear,String orgcode,long orgtype,long stepid,String path,String sheetname,String furl,long fid) throws IllegalStateException, IOException {

		String filename = mfile.getOriginalFilename();

		String fpath = path+ File.separator +filename;
		File nfile = new File(fpath);

		FileInputStream fis = null;
		fis = new FileInputStream(nfile);

		Workbook workbook = new XSSFWorkbook(fis);

		int numberOfSheets = workbook.getNumberOfSheets();
		int count=0;
		JSONObject arr= new JSONObject();
		UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+userDetail.getUsername()+"'", "current");
		here1: for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			System.out.println("sheet name is :" + sheet.getSheetName());

			if(sheet.getSheetName().equalsIgnoreCase(sheetname)&& furl.equalsIgnoreCase("b21")){
				List<FinFormB21> datas = new ArrayList<FinFormB21>();
				MainAuditRegistration main = (MainAuditRegistration) dao.getHQLResult("from MainAuditRegistration t where t.id='"+mid+"'", "current");
				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				String aucode="";
				Row rowHead = sheet.getRow(6);
				Cell cellHead = rowHead.getCell(4);
				switch (evaluator.evaluateInCell(cellHead).getCellType()) 
				{
				case Cell.CELL_TYPE_STRING:
					aucode=cellHead.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					aucode=NumberToTextConverter.toText(cellHead.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_BLANK:
					break;
				}
				if(main.getGencode().equalsIgnoreCase(aucode)){
					SubAuditOrganization org = (SubAuditOrganization) dao.getHQLResult("from SubAuditOrganization t where t.orgcode='"+main.getOrgcode()+"'", "current");
					Row row = sheet.getRow(9);
					Cell cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType()) 
					{
					case Cell.CELL_TYPE_STRING:
						org.setOrgname(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setOrgname(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(10);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setRegid(Long.parseLong(cell.getStringCellValue()));
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setRegid(Long.parseLong(NumberToTextConverter.toText(cell.getNumericCellValue())));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(11);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setStateregid(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setStateregid(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(12);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setCreatedate(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setCreatedate(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(13);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setFsorg(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setFsorg(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(14);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setTaxorg(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setTaxorg(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(15);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setNdorg(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setNdorg(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(16);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setHeadorder(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setHeadorder(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(17);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setBanks(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setBanks(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(18);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setStatebanks(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setStatebanks(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(19);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setWeb(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setWeb(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(20);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setEmail(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setEmail(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(21);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setAddress(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setAddress(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(22);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setPhone(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setPhone(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(23);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setFax(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setFax(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(25);
					cell = row.getCell(5);
					String fullname="";
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						fullname = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						fullname = NumberToTextConverter.toText(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(26);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						fullname = fullname + " " + cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						fullname = fullname + " " +NumberToTextConverter.toText(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					org.setHeadfullname(fullname);
					row = sheet.getRow(27);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setHeadprof(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setHeadprof(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(28);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setHeadphone(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setHeadphone(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(29);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setHeademail(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setHeademail(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(30);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setHeadorder(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setHeadorder(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(31);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setHeadprof(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setHeadprof(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(32);
					cell = row.getCell(5);
					fullname="";
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						fullname = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						fullname = NumberToTextConverter.toText(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(33);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						fullname = fullname + " " + cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						fullname = fullname + " " +NumberToTextConverter.toText(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					org.setAccfullname(fullname);
					row = sheet.getRow(34);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setAccprof(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setAccprof(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(35);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setAccphone(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setAccphone(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(36);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setAccemail(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setAccemail(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(37);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setAccwyear(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setAccwyear(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					row = sheet.getRow(38);
					cell = row.getCell(5);
					switch (evaluator.evaluateInCell(cell).getCellType())   
					{    	
					case Cell.CELL_TYPE_STRING:
						org.setAccprof(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						org.setAccprof(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					}
					dao.PeaceCrud(org, "SubAuditOrganization", "update",(long) org.getId(), 0, 0, null);

					String tempData1="";
					for(int k=9; k <= 38;k++){
						row = sheet.getRow(k);
						try {
							Iterator cellIterator = row.cellIterator();
							FinFormB21 form = new FinFormB21();
							while (cellIterator.hasNext()) {
								cell = (Cell) cellIterator.next();
								switch (evaluator.evaluateInCell(cell).getCellType()) 
								{
								case Cell.CELL_TYPE_STRING:

									if (cell.getColumnIndex() == 2) {
										form.setData1(cell.getStringCellValue());
										tempData1 = cell.getStringCellValue();
									}
									if (cell.getColumnIndex() == 3) {
										form.setData3(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}

									break;
								case Cell.CELL_TYPE_NUMERIC:

									if (cell.getColumnIndex() == 2) {
										form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
										tempData1 = NumberToTextConverter.toText(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == 3) {
										form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									if (cell.getColumnIndex() == 2) {
										form.setData1(tempData1);
									}
									break;
								}
							}
							form.setPart(1);
							form.setOrgcode(orgcode);
							form.setStepid(stepid);
							form.setCyear(ayear);
							form.setPlanid(mid);
							form.setOrgcatid(orgtype);
							form.setFilelnkid(fid);
							form.setUserid(loguser.getId());
							datas.add(form);
							count = count + 1;

						}  catch (Exception e) {
							System.out.println("ene bol catch:"+datas.size());
							dao.inserBatch(datas,"FinFormB21"); 
						}
					}
					tempData1="";
					for(int k=39; k <= 44;k++){
						row = sheet.getRow(k);
						try {
							Iterator cellIterator = row.cellIterator();
							FinFormB21 form = new FinFormB21();
							while (cellIterator.hasNext()) {
								cell = (Cell) cellIterator.next();
								switch (evaluator.evaluateInCell(cell).getCellType()) 
								{
								case Cell.CELL_TYPE_STRING:

									if (cell.getColumnIndex() == 2) {
										form.setData1(cell.getStringCellValue());
										tempData1 = cell.getStringCellValue();
									}
									if (cell.getColumnIndex() == 4) {
										form.setData3(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}

									break;
								case Cell.CELL_TYPE_NUMERIC:

									if (cell.getColumnIndex() == 2) {
										form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
										tempData1 = NumberToTextConverter.toText(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == 4) {
										form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									if (cell.getColumnIndex() == 2) {
										form.setData1(tempData1);
									}
									break;
								}
							}
							form.setPart(2);
							form.setOrgcode(orgcode);
							form.setStepid(stepid);
							form.setCyear(ayear);
							form.setPlanid(mid);
							form.setOrgcatid(orgtype);
							form.setFilelnkid(fid);
							form.setUserid(loguser.getId());
							datas.add(form);

						}  catch (Exception e) {
							System.out.println("ene bol catch:"+datas.size());
							dao.inserBatch(datas,"FinFormB21"); 
						}
					}
					tempData1="";
					String tempData2="";
					for(int k=45;k<=62;k++){
						row = sheet.getRow(k);
						try {
							Iterator cellIterator = row.cellIterator();
							FinFormB21 form = new FinFormB21();
							while (cellIterator.hasNext()) {
								cell = (Cell) cellIterator.next();
								switch (evaluator.evaluateInCell(cell).getCellType()) 
								{
								case Cell.CELL_TYPE_STRING:

									if (cell.getColumnIndex() == 2) {
										form.setData1(cell.getStringCellValue());
										tempData1 = cell.getStringCellValue();
									}
									if (cell.getColumnIndex() == 3) {
										form.setData2(cell.getStringCellValue());
										tempData2 = cell.getStringCellValue();
									}
									if (cell.getColumnIndex() == 4) {
										form.setData3(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_NUMERIC:

									if (cell.getColumnIndex() == 2) {
										form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
										tempData1 = NumberToTextConverter.toText(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == 3) {
										form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
										tempData2 = NumberToTextConverter.toText(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == 4) {
										form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_BLANK:
									if (cell.getColumnIndex() == 2) {
										form.setData1(tempData1);
									}
									if (cell.getColumnIndex() == 3) {
										form.setData2(tempData2);
									}
									break;
								}
							}
							form.setPart(3);
							form.setOrgcode(orgcode);
							form.setStepid(stepid);
							form.setCyear(ayear);
							form.setPlanid(mid);
							form.setOrgcatid(orgtype);
							form.setFilelnkid(fid);
							form.setUserid(loguser.getId());
							datas.add(form);

						}  catch (Exception e) {
							System.out.println("ene bol catch:"+datas.size());
							dao.inserBatch(datas,"FinFormB21"); 
						}
					}
					for(int k=63;k<=66;k++){
						row = sheet.getRow(k);
						try {
							Iterator cellIterator = row.cellIterator();
							FinFormB21 form = new FinFormB21();
							while (cellIterator.hasNext()) {
								cell = (Cell) cellIterator.next();
								switch (evaluator.evaluateInCell(cell).getCellType()) 
								{
								case Cell.CELL_TYPE_STRING:

									if (cell.getColumnIndex() == 2) {
										form.setData1(cell.getStringCellValue());
										tempData1 = cell.getStringCellValue();
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(cell.getStringCellValue());
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}

									break;
								case Cell.CELL_TYPE_NUMERIC:

									if (cell.getColumnIndex() == 2) {
										form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
										tempData1 = NumberToTextConverter.toText(cell.getNumericCellValue());
									}
									if (cell.getColumnIndex() == 5) {
										form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 6) {
										form.setData5(NumberToTextConverter.toText(cell.getNumericCellValue()));
									}
									if (cell.getColumnIndex() == 7) {
										form.setData6(cell.getStringCellValue());
									}
									break;
								case Cell.CELL_TYPE_BLANK:

									break;
								}
							}
							form.setPart(4);
							form.setOrgcode(orgcode);
							form.setStepid(stepid);
							form.setCyear(ayear);
							form.setPlanid(mid);
							form.setOrgcatid(orgtype);
							form.setFilelnkid(fid);
							form.setUserid(loguser.getId());
							datas.add(form);

						}  catch (Exception e) {
							System.out.println("ene bol catch:"+datas.size());
							dao.inserBatch(datas,"FinFormB21"); 
						}
					}
				}
				System.out.println("ene bol ok:"+datas.size());
				dao.inserBatch(datas,"FinFormB21");

			}
			else if(sheet.getSheetName().equalsIgnoreCase(sheetname)&& furl.equalsIgnoreCase("b1")){
				List<FinFormB1> datas = new ArrayList<FinFormB1>(); 
				for(int k=11; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinFormB1 form = new FinFormB1();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:

								if (cell.getColumnIndex() == 1) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData2(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 3) {
									form.setData3(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 4) {
									form.setData4(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 5) {
									form.setData5(cell.getStringCellValue());
								}

								break;
							case Cell.CELL_TYPE_NUMERIC:

								if (cell.getColumnIndex() == 1) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 3) {
									form.setData3(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 4) {
									form.setData4(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									
									dao.inserBatch(datas,"FinFormB1"); 
									continue here1;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);
						form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						form.setFilelnkid(fid);
						form.setUserid(loguser.getId());
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						
						dao.inserBatch(datas,"FinFormB1"); 
						continue here1;
					}
				}
				
				dao.inserBatch(datas,"FinFormB1"); 

			}
			else if(sheet.getSheetName().equalsIgnoreCase(sheetname)&&furl.equalsIgnoreCase("b12")){
				List<FinFormB12> datas = new ArrayList<FinFormB12>(); 
				for(int k=11; k <= sheet.getLastRowNum();k++){
					Row row = sheet.getRow(k);
					try {
						FinFormB12 form = new FinFormB12();
						Iterator cellIterator = row.cellIterator();
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
						while (cellIterator.hasNext()) {

							Cell cell = (Cell) cellIterator.next();
							switch (evaluator.evaluateInCell(cell).getCellType()) 
							{
							case Cell.CELL_TYPE_STRING:

								if (cell.getColumnIndex() == 1) {
									form.setData1(cell.getStringCellValue());
								}
								if (cell.getColumnIndex() == 2) {
									form.setData2(cell.getStringCellValue());
								}

								break;
							case Cell.CELL_TYPE_NUMERIC:

								if (cell.getColumnIndex() == 1) {
									form.setData1(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								if (cell.getColumnIndex() == 2) {
									form.setData2(NumberToTextConverter.toText(cell.getNumericCellValue()));
								}
								break;
							case Cell.CELL_TYPE_BLANK:
								if (cell.getColumnIndex() == 1) {
									System.out.println("@@@"+datas.size()+"FinFormB12");
									dao.inserBatch(datas,"FinFormB12"); 
									continue here1;
								}
								break;
							}
						}
						form.setOrgcode(orgcode);
						form.setStepid(stepid);
						form.setCyear(ayear);
						form.setPlanid(mid);
						form.setOrgcatid(orgtype);
						form.setFilelnkid(fid);
						form.setUserid(loguser.getId());
						datas.add(form);
						count = count + 1;

					}  catch (Exception e) {
						System.out.println("@@@"+datas.size()+"FinFormB12");
						dao.inserBatch(datas,"FinFormB12"); 
						continue here1;
					}
				}
				System.out.println("@@@"+datas.size()+"FinFormB12");
				dao.inserBatch(datas,"FinFormB12"); 
			}

		}

		fis.close();

		return null;
	}

}