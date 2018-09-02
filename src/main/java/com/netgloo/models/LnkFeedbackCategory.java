package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_MENU database table.
 * 
 */
@Entity
@Table(name="LNK_FEEDBACK_CATEGORIES")
@NamedQuery(name="LnkFeedbackCategory.findAll", query="SELECT l FROM LnkFeedbackCategory l")
public class LnkFeedbackCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_FEEDBACK_CATEGORIES_SEQ", sequenceName="LNK_FEEDBACK_CATEGORIES_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_FEEDBACK_CATEGORIES_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String title;
	
	private Long parentid;
		
	//bi-directional many-to-one association to LutMenu
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentid",nullable = true,insertable=false,updatable=false)
	@JsonBackReference
	private LnkFeedbackCategory lnkFeedbackCategory;

	//bi-directional many-to-one association to LutMenu
	@OneToMany(mappedBy="lnkFeedbackCategory")
	@JsonBackReference
	private List<LnkFeedbackCategory> lnkFeedbackCategories;
	
	//bi-directional many-to-one association to LnkNewstype
	@OneToMany(mappedBy="lnkFeedbackCategory")
	@JsonBackReference
	private List<LutFeedback> lutFeedbacks;

	public LnkFeedbackCategory() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public List<LnkFeedbackCategory> getLnkFeedbackCategories() {
		return this.lnkFeedbackCategories;
	}

	public void setLnkFeedbackCategories(List<LnkFeedbackCategory> LnkFeedbackCategories) {
		this.lnkFeedbackCategories = LnkFeedbackCategories;
	}

	public LnkFeedbackCategory addLutMenus(LnkFeedbackCategory LnkFeedbackCategories) {
		getLnkFeedbackCategories().add(LnkFeedbackCategories);
		LnkFeedbackCategories.setLnkFeedbackCategory(this);

		return LnkFeedbackCategories;
	}

	public LnkFeedbackCategory removeLutMenus(LnkFeedbackCategory LnkFeedbackCategories) {
		getLnkFeedbackCategories().remove(LnkFeedbackCategories);
		LnkFeedbackCategories.setLnkFeedbackCategory(null);

		return LnkFeedbackCategories;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LnkFeedbackCategory getLnkFeedbackCategory() {
		return lnkFeedbackCategory;
	}

	public void setLnkFeedbackCategory(LnkFeedbackCategory lnkFeedbackCategory) {
		lnkFeedbackCategory = lnkFeedbackCategory;
	}
	
	public List<LutFeedback> getLutFeedbacks() {
		return this.lutFeedbacks;
	}

	public void setLutFeedbacks(List<LutFeedback> lutFeedback) {
		this.lutFeedbacks = lutFeedback;
	}

	public LutFeedback addLutFeedbacks(LutFeedback lutFeedback) {
		getLutFeedbacks().add(lutFeedback);
		lutFeedback.setLnkFeedbackCategory(this);

		return lutFeedback;
	}

	public LutFeedback removeLutFeedbacks(LutFeedback lutFeedback) {
		getLutFeedbacks().remove(lutFeedback);
		lutFeedback.setLnkFeedbackCategory(null);

		return lutFeedback;
	}

}