package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.LnkWorkAuType;

import com.netgloo.models.FinFormB1;
import com.netgloo.models.FinFormB12;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_AUDIT_WORK database table.
 * 
 */
@Entity
@Table(name="LUT_AUDIT_WORK")
@NamedQuery(name="LutAuditWork.findAll", query="SELECT l FROM LutAuditWork l")
public class LutAuditWork implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String filename;

	private String filepath;

	private boolean isdettrial;
	
	private boolean isscore;

	private int isfile;

	private long orderid;
	
	private long parentid;
	
	private String fileurl;
	
	private long levelid;
	
	private String fname;

	@Lob
	private String text;

	private String workformname;
	
	@Transient
	private String rtext;

	//bi-directional many-to-one association to LnkWorkCategory
	@OneToMany(mappedBy="lutAuditWork")
	@JsonManagedReference
	@OrderBy("id")
	private List<LnkWorkCategory> lnkWorkCategories;

	//bi-directional many-to-one association to LnkWorkLevel
	@OneToMany(mappedBy="lutAuditWork")
	@JsonManagedReference
	private List<LnkWorkLevel> lnkWorkLevels;

	/*//bi-directional many-to-one association to LutAuditDir
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="auditdirid",nullable = false,insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;*/

	//bi-directional many-to-one association to LutAuditWork
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="parentid",nullable = false,insertable=false,updatable=false)
	private LutAuditWork lutAuditWork;

	//bi-directional many-to-one association to LutAuditWork
	@OneToMany(mappedBy="lutAuditWork")
	private List<LutAuditWork> lutAuditWorks;
	
	//bi-directional many-to-one association to LnkWorkAuType
	@OneToMany(mappedBy="lutAuditWork")
	private List<LnkWorkAuType> lnkWorkAuTypes;

	public LutAuditWork() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return this.filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	
	public long getLevelid() {
		return levelid;
	}

	public void setLevelid(long levelid) {
		this.levelid = levelid;
	}

	public boolean getIsdettrial() {
		return this.isdettrial;
	}

	public void setIsdettrial(boolean isdettrial) {
		this.isdettrial = isdettrial;
	}

	public int getIsfile() {
		return this.isfile;
	}

	public void setIsfile(int isfile) {
		this.isfile = isfile;
	}

	public long getOrderid() {
		return this.orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWorkformname() {
		return this.workformname;
	}

	public void setWorkformname(String workformname) {
		this.workformname = workformname;
	}

	public List<LnkWorkCategory> getLnkWorkCategories() {
		return this.lnkWorkCategories;
	}

	public void setLnkWorkCategories(List<LnkWorkCategory> lnkWorkCategories) {
		this.lnkWorkCategories = lnkWorkCategories;
	}

	public LnkWorkCategory addLnkWorkCategory(LnkWorkCategory lnkWorkCategory) {
		getLnkWorkCategories().add(lnkWorkCategory);
		lnkWorkCategory.setLutAuditWork(this);

		return lnkWorkCategory;
	}

	public LnkWorkCategory removeLnkWorkCategory(LnkWorkCategory lnkWorkCategory) {
		getLnkWorkCategories().remove(lnkWorkCategory);
		lnkWorkCategory.setLutAuditWork(null);

		return lnkWorkCategory;
	}

	public List<LnkWorkLevel> getLnkWorkLevels() {
		return this.lnkWorkLevels;
	}

	public void setLnkWorkLevels(List<LnkWorkLevel> lnkWorkLevels) {
		this.lnkWorkLevels = lnkWorkLevels;
	}

	public LnkWorkLevel addLnkWorkLevel(LnkWorkLevel lnkWorkLevel) {
		getLnkWorkLevels().add(lnkWorkLevel);
		lnkWorkLevel.setLutAuditWork(this);

		return lnkWorkLevel;
	}

	public LnkWorkLevel removeLnkWorkLevel(LnkWorkLevel lnkWorkLevel) {
		getLnkWorkLevels().remove(lnkWorkLevel);
		lnkWorkLevel.setLutAuditWork(null);

		return lnkWorkLevel;
	}
	
	public List<LnkWorkAuType> getLnkWorkAuTypes() {
		return this.lnkWorkAuTypes;
	}

	public void setLnkWorkAuTypes(List<LnkWorkAuType> lnkWorkAuTypes) {
		this.lnkWorkAuTypes = lnkWorkAuTypes;
	}

	/*public LutAuditDir getLutAuditDir() {
		return this.lutAuditDir;
	}

	public void setLutAuditDir(LutAuditDir lutAuditDir) {
		this.lutAuditDir = lutAuditDir;
	}*/

	public LutAuditWork getLutAuditWork() {
		return this.lutAuditWork;
	}

	public void setLutAuditWork(LutAuditWork lutAuditWork) {
		this.lutAuditWork = lutAuditWork;
	}

	public List<LutAuditWork> getLutAuditWorks() {
		return this.lutAuditWorks;
	}

	public void setLutAuditWorks(List<LutAuditWork> lutAuditWorks) {
		this.lutAuditWorks = lutAuditWorks;
	}

	public LutAuditWork addLutAuditWork(LutAuditWork lutAuditWork) {
		getLutAuditWorks().add(lutAuditWork);
		lutAuditWork.setLutAuditWork(this);

		return lutAuditWork;
	}

	public LutAuditWork removeLutAuditWork(LutAuditWork lutAuditWork) {
		getLutAuditWorks().remove(lutAuditWork);
		lutAuditWork.setLutAuditWork(null);

		return lutAuditWork;
	}

	public long getParentid() {
		return parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public boolean isIsscore() {
		return isscore;
	}

	public void setIsscore(boolean isscore) {
		this.isscore = isscore;
	}

	public String getRtext() {
		return rtext;
	}

	public void setRtext(String rtext) {
		this.rtext = rtext;
	}
}