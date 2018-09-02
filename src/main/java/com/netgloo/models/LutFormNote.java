package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_FORM_NOTES database table.
 * 
 */
@Entity
@Table(name="LUT_FORM_NOTES")
@NamedQuery(name="LutFormNote.findAll", query="SELECT l FROM LutFormNote l")
public class LutFormNote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private BigDecimal audittype;

	private BigDecimal filecount;

	private BigDecimal fsize;

	private BigDecimal inptype;

	private BigDecimal isapproval;

	private BigDecimal isfile;

	private BigDecimal isform;

	private String note;

	private String notecontent;

	private BigDecimal onoffid;

	private BigDecimal transid;

	private String url;

	//bi-directional many-to-one association to LnkComment
	@OneToMany(mappedBy="lutFormNote")
	private List<LnkComment> lnkComments;

	public LutFormNote() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAudittype() {
		return this.audittype;
	}

	public void setAudittype(BigDecimal audittype) {
		this.audittype = audittype;
	}

	public BigDecimal getFilecount() {
		return this.filecount;
	}

	public void setFilecount(BigDecimal filecount) {
		this.filecount = filecount;
	}

	public BigDecimal getFsize() {
		return this.fsize;
	}

	public void setFsize(BigDecimal fsize) {
		this.fsize = fsize;
	}

	public BigDecimal getInptype() {
		return this.inptype;
	}

	public void setInptype(BigDecimal inptype) {
		this.inptype = inptype;
	}

	public BigDecimal getIsapproval() {
		return this.isapproval;
	}

	public void setIsapproval(BigDecimal isapproval) {
		this.isapproval = isapproval;
	}

	public BigDecimal getIsfile() {
		return this.isfile;
	}

	public void setIsfile(BigDecimal isfile) {
		this.isfile = isfile;
	}

	public BigDecimal getIsform() {
		return this.isform;
	}

	public void setIsform(BigDecimal isform) {
		this.isform = isform;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNotecontent() {
		return this.notecontent;
	}

	public void setNotecontent(String notecontent) {
		this.notecontent = notecontent;
	}

	public BigDecimal getOnoffid() {
		return this.onoffid;
	}

	public void setOnoffid(BigDecimal onoffid) {
		this.onoffid = onoffid;
	}

	public BigDecimal getTransid() {
		return this.transid;
	}

	public void setTransid(BigDecimal transid) {
		this.transid = transid;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<LnkComment> getLnkComments() {
		return this.lnkComments;
	}

	public void setLnkComments(List<LnkComment> lnkComments) {
		this.lnkComments = lnkComments;
	}

	public LnkComment addLnkComment(LnkComment lnkComment) {
		getLnkComments().add(lnkComment);
		lnkComment.setLutFormNote(this);

		return lnkComment;
	}

	public LnkComment removeLnkComment(LnkComment lnkComment) {
		getLnkComments().remove(lnkComment);
		lnkComment.setLutFormNote(null);

		return lnkComment;
	}

}