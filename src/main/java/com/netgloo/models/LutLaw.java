package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_LAWS database table.
 * 
 */
@Entity
@Table(name="LUT_LAWS")
@NamedQuery(name="LutLaw.findAll", query="SELECT l FROM LutLaw l")
public class LutLaw implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_LAWS_SEQ", sequenceName="LUT_LAWS_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_LAWS_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String describe;

	private String lawname;
	
	private String zaalt;	
	
	private String parentid;
	
	private long lawcategory;
	
	//bi-directional many-to-one association to LutMenu
		@ManyToOne(fetch = FetchType.LAZY)
		@JsonBackReference
		@JoinColumn(name = "parentid",referencedColumnName="id",nullable = false,insertable=false,updatable=false)	
		private LutLaw lutLaw;
		
		//bi-directional many-to-one association to LutMenu
		@OneToMany(mappedBy="lutLaw")
		@JsonBackReference
		@OrderBy("id asc")
		private List<LutLaw> lutLaws;
		
		//bi-directional many-to-one association to LnkRiskTryout
				@OneToMany(mappedBy="lutLaw")
				@JsonBackReference
				private List<LnkRiskLaw> lnkRiskLaws;

		public LutLaw() {
		}

	

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getLawname() {
		return this.lawname;
	}

	public void setLawname(String lawname) {
		this.lawname = lawname;
	}

	public String getZaalt() {
		return zaalt;
	}

	public void setZaalt(String zaalt) {
		this.zaalt = zaalt;
	}

	public List<LnkRiskLaw> getLnkRiskLaws() {
		return this.lnkRiskLaws;
	}

	public void setLnkRiskLaws(List<LnkRiskLaw> lnkRiskLaws) {
		this.lnkRiskLaws = lnkRiskLaws;
	}

	public long getLawcategory() {
		return lawcategory;
	}

	public void setLawcategory(long lawcategory) {
		this.lawcategory = lawcategory;
	}



	public LnkRiskLaw addLnkRiskLaw(LnkRiskLaw lnkRiskLaw) {
		getLnkRiskLaws().add(lnkRiskLaw);
		lnkRiskLaw.setLutLaw(this);

		return lnkRiskLaw;
	}

	public LnkRiskLaw removeLnkRiskLaw(LnkRiskLaw lnkRiskLaw) {
		getLnkRiskLaws().remove(lnkRiskLaw);
		lnkRiskLaw.setLutRisk(null);

		return lnkRiskLaw;
	}
	
}