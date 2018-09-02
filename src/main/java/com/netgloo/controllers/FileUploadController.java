package com.netgloo.controllers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.netgloo.models.FileUpload;
import com.netgloo.repo.FileUploadRepository;

import com.netgloo.IzrApplication;
import com.netgloo.dao.UserDao;

@RestController
@RequestMapping("/file")
public class FileUploadController {

	@Autowired
	FileUploadRepository fileUploadRepository;
	
	@Autowired
    private UserDao dao;
    
	 @RequestMapping(value = "/salary/upload", method = RequestMethod.POST)
	    @ResponseBody
	    public ResponseEntity<?> uploadExcelFile(
        @RequestParam("files") MultipartFile uploadfile, HttpServletRequest req) {
	      
	      try {
	    	  
	    		String filename = uploadfile.getOriginalFilename();
	    		int index=filename.lastIndexOf('.');
	    		String lastOne=(filename.substring(index +1));
	    		long size = uploadfile.getSize();

	    		String uuid = UUID.randomUUID().toString()+"."+lastOne;
	    		String SAVE_DIR = IzrApplication.ROOT;
	    		
	    		String appPath = req.getServletContext().getRealPath(""); 	 
	    		String path=appPath+"/"+SAVE_DIR+"/salary/";
	    		
	    		File folder = new File(path);
	    		if(!folder.exists()){
	    			folder.mkdirs();
	    		}
	    		if (!uploadfile.isEmpty()) {
	    			/*try {
	    				Files.copy(uploadfile.getInputStream(), Paths.get(path, uuid));
	    			} catch (IOException|RuntimeException e) {
	    				System.out.println("aldaa");
	    				String furl = SAVE_DIR+ "/" +filename;
	    			}*/
	    		} else {
	    			System.out.println("empty"); 
	    			return null;
	    		}

	    		
	    		
	    		Workbook workbook = new XSSFWorkbook(uploadfile.getInputStream());
	    		DataFormatter objDefaultFormat = new DataFormatter();
	    		FormulaEvaluator objFormulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
	    		
	    		int numberOfSheets = workbook.getNumberOfSheets();
	    		int count=0;
	    		for (int i = 0; i < numberOfSheets; i++) {
	    			Sheet sheet = workbook.getSheetAt(i);
	    				    			
	    			for(int k=3; k <= sheet.getLastRowNum() ;k++){
						 Row row = sheet.getRow(k);	
						 
						 //Iterator cellIterator = row.cellIterator();
						 String columnLetter = CellReference.convertNumToColString(3);
						 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				
						 int c=k+1;
						 if(row!=null){
							 Cell inCell = row.createCell(row.getLastCellNum());
							 inCell.setCellFormula("VALUE(AE"+c+"/AD"+c+"*100)");
							 CellValue cellValue2= evaluator.evaluate(inCell);
							 
							 Cell inCell1 = row.createCell(row.getLastCellNum());
							 inCell1.setCellFormula("VALUE((AD"+c+"-L"+c+"-N"+c+")*VALUE(AE"+c+"/AD"+c+"*100)/100)");
							 CellValue cellValue3=evaluator.evaluate(inCell1);
							 
							 Cell inCell2 = row.createCell(row.getLastCellNum());
							 inCell2.setCellFormula("VALUE((AD"+c+"-L"+c+"-N"+c+"-AQ"+c+")*10/100-7000+(O"+c+"+P"+c+")*1/100)");
							 CellValue cellValue4=evaluator.evaluate(inCell2);
							 
							 Cell inCell3 = row.createCell(row.getLastCellNum());
							 inCell3.setCellFormula("+AQ"+c+"-AE"+c+"");
							 CellValue cellValue5=evaluator.evaluate(inCell3);
							 
							 Cell inCell4 = row.createCell(row.getLastCellNum());
							 inCell4.setCellFormula("+AR"+c+"-AF"+c+"");
							 CellValue cellValue6=evaluator.evaluate(inCell4);
						 }
						
	    			}
	    		}
	    		
	    		CreationHelper createHelper = workbook.getCreationHelper();
    		    Sheet sheet = workbook.createSheet("new sheet");

    		    // Create a row and put some cells in it. Rows are 0 based.
    		    Row row = sheet.createRow((short)0);
    		    // Create a cell and put a value in it.
    		    Cell cell = row.createCell(0);
    		    cell.setCellValue(1);

    		    // Or do it on one line.
    		    row.createCell(1).setCellValue(1.2);
    		    row.createCell(2).setCellValue(
    		         createHelper.createRichTextString("This is a string"));
    		    row.createCell(3).setCellValue(true);
    		
    		    FileOutputStream fileOut = new FileOutputStream(path+"/"+uuid+".xlsx");
    		    workbook.write(fileOut);
    		    fileOut.close();
    		    
    		    String furl = SAVE_DIR+ "/salary/" +uuid;

	    		FileUpload newFile = new FileUpload();
	    		newFile.setFilename(filename);
	    		newFile.setFilesize(size);
	    		newFile.setMimetype(lastOne);
	    		newFile.setFileurl(furl);

	    		fileUploadRepository.save(newFile);

			    HttpHeaders httpHeaders = new HttpHeaders();
			    return new ResponseEntity<FileUpload>(newFile,httpHeaders,HttpStatus.OK);
	      }
	      catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
	 }
	
	 @RequestMapping(value = "/upload/nafiles", method = RequestMethod.POST)
	    @ResponseBody
	    public ResponseEntity<?> uploadFilenafile(
	        @RequestParam("files") MultipartFile uploadfile, HttpServletRequest req) {
	      
	      try {
	    	  
	    	//String [] str=uploadfile.getOriginalFilename().split(".");
	    	String mimeType = uploadfile.getContentType();
	  		String filename = uploadfile.getOriginalFilename();
			int index=filename.lastIndexOf('.');
			long size = uploadfile.getSize();
			String SAVE_DIR = IzrApplication.ROOT+"/newsAttach";
			String lastOne=(filename.substring(index +1));
			String uuid = UUID.randomUUID().toString()+"."+lastOne;
			
			String appPath = req.getServletContext().getRealPath(""); 	 
			String path=appPath+"/"+SAVE_DIR;
			File folder = new File(path);
			if(!folder.exists()){
				folder.mkdirs();
			}
			String fpath = SAVE_DIR+ File.separator +filename;
			System.out.println("aaa");
			if (!uploadfile.isEmpty()) {
				try {  	    		  
					Files.copy(uploadfile.getInputStream(), Paths.get(path, uuid));
				} catch (IOException|RuntimeException e) {
					System.out.println("aldaa");
				}
			} 
			String furl= SAVE_DIR+ "/" +uuid;
			
			FileUpload newFile = new FileUpload();
			newFile.setFilename(filename);
			newFile.setFilesize(size);
			newFile.setMimetype(mimeType);
			newFile.setFileurl(furl);
			newFile.setName(uploadfile.getOriginalFilename());
			fileUploadRepository.save(newFile);
			HttpHeaders httpHeaders = new HttpHeaders();
		    return new ResponseEntity<FileUpload>(newFile,httpHeaders,HttpStatus.OK);
	      }
	      catch (Exception e) {
	        System.out.println(e.getMessage());
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
	    }
	
	
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
        @RequestParam("files") MultipartFile uploadfile,@RequestParam("mid") long mid,@RequestParam("userid") long userid, HttpServletRequest req) {
      
      try {
    	  
    		String filename = uploadfile.getOriginalFilename();
    		int index=filename.lastIndexOf('.');
    		String lastOne=(filename.substring(index +1));
    		long size = uploadfile.getSize();

    		String uuid = UUID.randomUUID().toString()+"."+lastOne;
    		String SAVE_DIR = IzrApplication.ROOT;
    		
    		String appPath = req.getServletContext().getRealPath(""); 	 
    		String path=appPath+"/"+SAVE_DIR+"/"+userid+"/"+mid;
    		
    		File folder = new File(path);
    		if(!folder.exists()){
    			folder.mkdirs();
    		}
    		if (!uploadfile.isEmpty()) {
    			try {
    				Files.copy(uploadfile.getInputStream(), Paths.get(path, uuid));
    			} catch (IOException|RuntimeException e) {
    				System.out.println("aldaa");
    				String furl = SAVE_DIR+ "/" +filename;
    			}
    		} else {
    			System.out.println("empty"); 
    			return null;
    		}

    		String furl = SAVE_DIR+ "/"+userid+"/"+mid+"/" +uuid;

    		FileUpload newFile = new FileUpload();
    		newFile.setFilename(filename);
    		newFile.setFilesize(size);
    		newFile.setMimetype(lastOne);
    		newFile.setFileurl(furl);

    		fileUploadRepository.save(newFile);

		    HttpHeaders httpHeaders = new HttpHeaders();
		    return new ResponseEntity<FileUpload>(newFile,httpHeaders,HttpStatus.OK);
      }
      catch (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
    
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> removeFile(@RequestParam("fileNames") String filename, HttpServletRequest req) {
      
      try {
    	  FileUpload fl = fileUploadRepository.findByFilename(filename);
    	  
          if (fl == null) {
              return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
          }
          
          File file = new File(req.getServletContext().getRealPath("")+"/"+fl.getFileurl());
	  	  if(file.delete()){
	  			System.out.println(file.getName() + " is deleted!");
	  	  }else{
	  			System.out.println("Delete operation is failed.");
	  	  }

          fileUploadRepository.delete(fl);
          return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
    
}
