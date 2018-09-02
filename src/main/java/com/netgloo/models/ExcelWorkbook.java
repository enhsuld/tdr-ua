package com.netgloo.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ExcelWorkbook {

	private String fileName;
	private Collection<ExcelWorksheet> sheets = new ArrayList<ExcelWorksheet>();
	
	public void addExcelWorksheet(ExcelWorksheet sheet) {
		sheets.add(sheet);
	}
	
	public String toJson(boolean pretty) {
		try {
			if(pretty) {
				return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(this);	
			} else {
				return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(this);	
			}
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	// GET/SET
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Collection<ExcelWorksheet> getSheets() {
		return sheets;
	}

	public void setSheets(Collection<ExcelWorksheet> sheets) {
		this.sheets = sheets;
	}

}
