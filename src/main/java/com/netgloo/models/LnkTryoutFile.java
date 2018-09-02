package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_TRYOUT_FILES database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_FILES")
@NamedQuery(name="LnkTryoutFile.findAll", query="SELECT l FROM LnkTryoutFile l")
public class LnkTryoutFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_TRYOUT_FILE_SEQ", sequenceName="LNK_TRYOUT_FILE_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_TRYOUT_FILE_SEQ")
	private long id;

	private String fileurl;
	
	private double fsize;
	
	private String extension;
	
	private String name;

	//bi-directional many-to-one association to LnkRiskTryout
	@ManyToOne
	@JoinColumn(name="LRTID")
	@JsonBackReference
	private LnkRiskTryout lnkRiskTryout;

	public LnkTryoutFile() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileurl() {
		return this.fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public LnkRiskTryout getLnkRiskTryout() {
		return this.lnkRiskTryout;
	}

	public void setLnkRiskTryout(LnkRiskTryout lnkRiskTryout) {
		this.lnkRiskTryout = lnkRiskTryout;
	}

	public double getFsize() {
		return fsize;
	}

	public void setFsize(double fsize) {
		this.fsize = fsize;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}