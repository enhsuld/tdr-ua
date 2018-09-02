package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.netgloo.models.PreAuditRegistration;

import java.util.List;


/**
 * The persistent class for the LUT_CATEGORY database table.
 * 
 */
@Entity
@Table(name="LUT_CATEGORY")
@NamedQuery(name="LutCategory.findAll", query="SELECT l FROM LutCategory l")
public class LutCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_CATEGORY_SEQ", sequenceName="LUT_CATEGORY_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_CATEGORY_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String categoryname;

	//bi-directional many-to-one association to SubAuditOrganization
	@OneToMany(mappedBy="lutCategory")
	@JsonBackReference
	private List<SubAuditOrganization> subAuditOrganizations;
	
	//bi-directional many-to-one association to PreAuditRegistration
	@OneToMany(mappedBy="lutCategory")
	@JsonBackReference
	private List<PreAuditRegistration> preAuditRegistrations;
	
	//bi-directional many-to-one association to LnkFactorCategory
	@OneToMany(mappedBy="lutCategory")
	@JsonBackReference
	private List<LnkFactorCategory> lnkFactorCategories;

	public LutCategory() {
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCategoryname() {
		return this.categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public List<SubAuditOrganization> getSubAuditOrganizations() {
		return this.subAuditOrganizations;
	}

	public void setSubAuditOrganizations(List<SubAuditOrganization> subAuditOrganizations) {
		this.subAuditOrganizations = subAuditOrganizations;
	}

	public SubAuditOrganization addSubAuditOrganization(SubAuditOrganization subAuditOrganization) {
		getSubAuditOrganizations().add(subAuditOrganization);
		subAuditOrganization.setLutCategory(this);

		return subAuditOrganization;
	}

	public SubAuditOrganization removeSubAuditOrganization(SubAuditOrganization subAuditOrganization) {
		getSubAuditOrganizations().remove(subAuditOrganization);
		subAuditOrganization.setLutCategory(null);

		return subAuditOrganization;
	}
	
	
	public List<PreAuditRegistration> getPreAuditRegistrations() {
		return this.preAuditRegistrations;
	}

	public void setPreAuditRegistrations(List<PreAuditRegistration> preAuditRegistrations) {
		this.preAuditRegistrations = preAuditRegistrations;
	}

	public PreAuditRegistration addPreAuditRegistration(PreAuditRegistration preAuditRegistration) {
		getPreAuditRegistrations().add(preAuditRegistration);
		preAuditRegistration.setLutCategory(this);

		return preAuditRegistration;
	}

	public PreAuditRegistration removePreAuditRegistration(PreAuditRegistration preAuditRegistration) {
		getPreAuditRegistrations().remove(preAuditRegistration);
		preAuditRegistration.setLutCategory(null);

		return preAuditRegistration;
	}

	public List<LnkFactorCategory> getLnkFactorCategories() {
		return this.lnkFactorCategories;
	}

	public void setLnkFactorCategories(List<LnkFactorCategory> lnkFactorCategories) {
		this.lnkFactorCategories = lnkFactorCategories;
	}

	public LnkFactorCategory addLnkFactorCategory(LnkFactorCategory lnkFactorCategory) {
		getLnkFactorCategories().add(lnkFactorCategory);
		lnkFactorCategory.setLutCategory(this);

		return lnkFactorCategory;
	}

	public LnkFactorCategory removeLnkFactorCategory(LnkFactorCategory lnkFactorCategory) {
		getLnkFactorCategories().remove(lnkFactorCategory);
		lnkFactorCategory.setLutCategory(null);

		return lnkFactorCategory;
	}
}