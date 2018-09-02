package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;


import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_RISKS database table.
 * 
 */
@Entity
@Table(name="LUT_RISKS")
@NamedQuery(name="LutRisk.findAll", query="SELECT l FROM LutRisk l")
public class LutRisk implements Serializable {

	@JsonView(Views.Public.class) 
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_RISKS_SEQ", sequenceName="LUT_RISKS_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_RISKS_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	//private int lawcategory;

	private String riskname;

	private int risktype;

	private Long other;
	private String othertext;

	public class Views {
		public class Public {

		}

		private String dirid;
		public String getDirid() {
			return this.dirid;
		}

		public void setDirid(String dirid) {
			this.dirid = dirid;
		}
	}

	//bi-directional many-to-one association to LnkUserrole
	@OneToMany(mappedBy="lutRisk")
	@OrderBy("id asc")
	@JsonManagedReference
	private List<LnkRiskdir> lnkRiskdirs;


	//bi-directional many-to-one association to LnkRiskT2
	@OneToMany(mappedBy="lutRisk")
	@JsonBackReference
	private List<LnkRiskT2> lnkRiskT2s;

	//bi-directional many-to-one association to LnkRiskTryout
	@OneToMany(mappedBy="lutRisk")
	@JsonBackReference
	private List<LnkRiskTryout> lnkRiskTryouts;

	//bi-directional many-to-one association to LnkRiskTryout
	@OneToMany(mappedBy="lutRisk")
	@JsonBackReference
	private List<LnkRiskLaw> lnkRiskLaws;


	public LutRisk() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/*public int getLawcategory() {
		return this.lawcategory;
	}

	public void setLawcategory(int lawcategory) {
		this.lawcategory = lawcategory;
	}*/

	public String getRiskname() {
		return this.riskname;
	}

	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	public int getRisktype() {
		return this.risktype;
	}

	public void setRisktype(int risktype) {
		this.risktype = risktype;
	}

	public List<LnkRiskT2> getLnkRiskT2s() {
		return this.lnkRiskT2s;
	}

	public void setLnkRiskT2s(List<LnkRiskT2> lnkRiskT2s) {
		this.lnkRiskT2s = lnkRiskT2s;
	}

	public LnkRiskT2 addLnkRiskT2(LnkRiskT2 lnkRiskT2) {
		getLnkRiskT2s().add(lnkRiskT2);
		lnkRiskT2.setLutRisk(this);

		return lnkRiskT2;
	}

	public LnkRiskT2 removeLnkRiskT2(LnkRiskT2 lnkRiskT2) {
		getLnkRiskT2s().remove(lnkRiskT2);
		lnkRiskT2.setLutRisk(null);

		return lnkRiskT2;
	}

	@JsonBackReference
	public List<LnkRiskdir> getLnkRiskdirs() {
		return this.lnkRiskdirs;
	}

	public void setLnkRiskdirs(List<LnkRiskdir> lnkRiskdirs) {
		this.lnkRiskdirs = lnkRiskdirs;
	}

	public LnkRiskdir addLnkRiskdir(LnkRiskdir lnkRiskdir) {
		getLnkRiskdirs().add(lnkRiskdir);
		lnkRiskdir.setLutRisk(this);

		return lnkRiskdir;
	}

	public LnkRiskdir removeLnkRiskdir(LnkRiskdir lnkRiskdir) {
		getLnkRiskdirs().remove(lnkRiskdir);
		lnkRiskdir.setLutRisk(null);

		return lnkRiskdir;
	}

	public List<LnkRiskTryout> getLnkRiskTryouts() {
		return this.lnkRiskTryouts;
	}

	public void setLnkRiskTryouts(List<LnkRiskTryout> lnkRiskTryouts) {
		this.lnkRiskTryouts = lnkRiskTryouts;
	}

	public LnkRiskTryout addLnkRiskTryout(LnkRiskTryout lnkRiskTryout) {
		getLnkRiskTryouts().add(lnkRiskTryout);
		lnkRiskTryout.setLutRisk(this);

		return lnkRiskTryout;
	}

	public LnkRiskTryout removeLnkRiskTryout(LnkRiskTryout lnkRiskTryout) {
		getLnkRiskTryouts().remove(lnkRiskTryout);
		lnkRiskTryout.setLutRisk(null);

		return lnkRiskTryout;
	}


	public List<LnkRiskLaw> getLnkRiskLaws() {
		return this.lnkRiskLaws;
	}

	public void setLnkRiskLaws(List<LnkRiskLaw> lnkRiskLaws) {
		this.lnkRiskLaws = lnkRiskLaws;
	}

	public LnkRiskLaw addLnkRiskLaw(LnkRiskLaw lnkRiskLaw) {
		getLnkRiskLaws().add(lnkRiskLaw);
		lnkRiskLaw.setLutRisk(this);

		return lnkRiskLaw;
	}

	public LnkRiskLaw removeLnkRiskLaw(LnkRiskLaw lnkRiskLaw) {
		getLnkRiskLaws().remove(lnkRiskLaw);
		lnkRiskLaw.setLutRisk(null);

		return lnkRiskLaw;
	}

	public Long getOther() {
		return other;
	}

	public void setOther(Long other) {
		this.other = other;
	}

	public String getOthertext() {
		return othertext;
	}

	public void setOthertext(String othertext) {
		this.othertext = othertext;
	}

}