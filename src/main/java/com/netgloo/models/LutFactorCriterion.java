package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.LutFactor;


/**
 * The persistent class for the LUT_FACTOR_CRITERION database table.
 * 
 */
@Entity
@Table(name="LUT_FACTOR_CRITERION")
@NamedQuery(name="LutFactorCriterion.findAll", query="SELECT l FROM LutFactorCriterion l")
public class LutFactorCriterion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_FACTOR_CRITERION_SEQ", sequenceName="LUT_FACTOR_CRITERION_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_FACTOR_CRITERION_SEQ")
	private long id;

	private String name;
	
	private long factorid;

	//bi-directional many-to-one association to LnkMainFormT2
/*	@OneToMany(mappedBy="lutFactorCriterion")
	@JsonBackReference
	private List<LnkMainFormT2> lnkMainFormT2s;*/
	
	//bi-directional many-to-one association to LutFactor
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonManagedReference
	@JoinColumn(name="factorid",insertable=false,updatable=false)
	private LutFactor lutFactor;

	public LutFactorCriterion() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LutFactor getLutFactor() {
		return this.lutFactor;
	}

	public void setLutFactor(LutFactor lutFactor) {
		this.lutFactor = lutFactor;
	}

	public long getFactorid() {
		return factorid;
	}

	public void setFactorid(long factorid) {
		this.factorid = factorid;
	}
/*	public List<LnkMainFormT2> getLnkMainFormT2s() {
		return this.lnkMainFormT2s;
	}

	public void setLnkMainFormT2s(List<LnkMainFormT2> lnkMainFormT2s) {
		this.lnkMainFormT2s = lnkMainFormT2s;
	}*/

}