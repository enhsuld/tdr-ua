package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the LNK_MAIN_FORM database table.
 * 
 */
@Entity
@Table(name="LNK_MAIN_FORM")
@NamedQuery(name="LnkMainForm.findAll", query="SELECT l FROM LnkMainForm l")
public class LnkMainForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String atdate;

	private String fileext;

	private String filename;

	private long filesize;

	private String fileurl;

	private long stepid;

	private long wid;
	
	private long mid;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JoinColumn(name="mid", insertable=false, updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public LnkMainForm() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getAtdate() {
		return this.atdate;
	}

	public void setAtdate(String atdate) {
		this.atdate = atdate;
	}

	public String getFileext() {
		return this.fileext;
	}

	public void setFileext(String fileext) {
		this.fileext = fileext;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilesize() {
		return this.filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getFileurl() {
		return this.fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public long getStepid() {
		return this.stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public long getWid() {
		return this.wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}

}