package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.LutCategory;
import com.netgloo.models.LutFactor;


/**
 * The persistent class for the LNK_FACTOR_CATEGORY database table.
 * 
 */
@Entity
@Table(name="LNK_FACTOR_CATEGORY")
@NamedQuery(name="LnkFactorCategory.findAll", query="SELECT l FROM LnkFactorCategory l")
public class LnkFactorCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_FACTOR_CATEGORY_SEQ", sequenceName="LNK_FACTOR_CATEGORY_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_FACTOR_CATEGORY_SEQ")
	private long id;
	
	private long catid;
	
	private long factorid;

	//bi-directional many-to-one association to LutCategory
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="catid", insertable= false, updatable= false)
	private LutCategory lutCategory;

	//bi-directional many-to-one association to LutFactor
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="factorid", insertable= false, updatable= false)
	private LutFactor lutFactor;

	public LnkFactorCategory() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutCategory getLutCategory() {
		return this.lutCategory;
	}

	public void setLutCategory(LutCategory lutCategory) {
		this.lutCategory = lutCategory;
	}

	public LutFactor getLutFactor() {
		return this.lutFactor;
	}

	public void setLutFactor(LutFactor lutFactor) {
		this.lutFactor = lutFactor;
	}

	public long getCatid() {
		return catid;
	}

	public void setCatid(long catid) {
		this.catid = catid;
	}

	public long getFactorid() {
		return factorid;
	}

	public void setFactorid(long factorid) {
		this.factorid = factorid;
	}

}