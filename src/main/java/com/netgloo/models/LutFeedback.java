package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.MainAuditRegistration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the FIN_CTT6 database table.
 * 
 */
@Entity
@Table(name="LUT_FEEDBACKS")
@NamedQuery(name="LutFeedback.findAll", query="SELECT f FROM LutFeedback f")
public class LutFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_FEEDBACKS_SEQ", sequenceName="LUT_FEEDBACKS_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_FEEDBACKS_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String description;

	private String username;

	private String createdat;
	
	private Long categoryid;
	
	private Long parentid;
	
	//bi-directional many-to-one association to LutMenu
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentid",nullable = false,insertable=false,updatable=false)
	@JsonBackReference
	private LutFeedback lutFeedback;
	
	@OneToMany(mappedBy="lutFeedback")
	@JsonManagedReference
	private List<LutFeedback> lutFeedbacks;
	
	//bi-directional many-to-one association to LutMenu
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryid",nullable = false,insertable=false,updatable=false)
	@JsonBackReference
	private LnkFeedbackCategory lnkFeedbackCategory;

	public LutFeedback() {
		this.createdat = new Date().toString();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreatedat() {
		return createdat;
	}

	public void setCreatedat(String createdat) {
		this.createdat = createdat;
	}

	public Long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(Long categoryid) {
		this.categoryid = categoryid;
	}

	public LnkFeedbackCategory getLnkFeedbackCategory() {
		return lnkFeedbackCategory;
	}

	public void setLnkFeedbackCategory(LnkFeedbackCategory lnkFeedbackCategory) {
		lnkFeedbackCategory = lnkFeedbackCategory;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public LutFeedback getLutFeedback() {
		return lutFeedback;
	}

	public void setLutFeedback(LutFeedback lutFeedback) {
		this.lutFeedback = lutFeedback;
	}

	public List<LutFeedback> getLutFeedbacks() {
		return lutFeedbacks;
	}

	public void setLutFeedbacks(List<LutFeedback> lutFeedbacks) {
		this.lutFeedbacks = lutFeedbacks;
	}
	
	public LutFeedback addLutMenus(LutFeedback lutFeedbacks) {
		getLutFeedbacks().add(lutFeedbacks);
		lutFeedbacks.setLutFeedback(this);
		return lutFeedbacks;
	}

	public LutFeedback removeLutMenus(LutFeedback lutFeedbacks) {
		getLutFeedbacks().remove(lutFeedbacks);
		lutFeedbacks.setLutFeedback(null);
		return lutFeedbacks;
	}

}