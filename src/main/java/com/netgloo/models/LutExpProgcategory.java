package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the LUT_EXP_PROGCATEGORY database table.
 * 
 */
@Entity
@Table(name="LUT_EXP_PROGCATEGORY")
@NamedQuery(name="LutExpProgcategory.findAll", query="SELECT l FROM LutExpProgcategory l")
public class LutExpProgcategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String progname;

	//bi-directional many-to-one association to SubAuditOrganization
/*	@OneToMany(mappedBy="lutExpProgcategory")
	private List<SubAuditOrganization> subAuditOrganizations;*/

	public LutExpProgcategory() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getProgname() {
		return this.progname;
	}

	public void setProgname(String progname) {
		this.progname = progname;
	}

/*	public List<SubAuditOrganization> getSubAuditOrganizations() {
		return this.subAuditOrganizations;
	}

	public void setSubAuditOrganizations(List<SubAuditOrganization> subAuditOrganizations) {
		this.subAuditOrganizations = subAuditOrganizations;
	}

	public SubAuditOrganization addSubAuditOrganization(SubAuditOrganization subAuditOrganization) {
		getSubAuditOrganizations().add(subAuditOrganization);
		subAuditOrganization.setLutExpProgcategory(this);

		return subAuditOrganization;
	}

	public SubAuditOrganization removeSubAuditOrganization(SubAuditOrganization subAuditOrganization) {
		getSubAuditOrganizations().remove(subAuditOrganization);
		subAuditOrganization.setLutExpProgcategory(null);

		return subAuditOrganization;
	}*/

}