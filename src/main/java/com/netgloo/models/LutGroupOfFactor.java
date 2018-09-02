package com.netgloo.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.netgloo.models.LutFactor;

import java.util.List;


/**
 * The persistent class for the LUT_GROUP_OF_FACTOR database table.
 * 
 */
@Entity
@Table(name="LUT_GROUP_OF_FACTOR")
@NamedQuery(name="LutGroupOfFactor.findAll", query="SELECT l FROM LutGroupOfFactor l")
public class LutGroupOfFactor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_GROUP_OF_FACTOR_SEQ", sequenceName="LUT_GROUP_OF_FACTOR_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_GROUP_OF_FACTOR_SEQ")
	private long id;

	private String name;
	
	private int orderid;
	
	private int ftype;

	//bi-directional many-to-one association to LutFactor
	@OneToMany(mappedBy="lutGroupOfFactor")
	private List<LutFactor> lutFactors;

	public LutGroupOfFactor() {
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
	
	public int getFtype() {
		return ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}

	public List<LutFactor> getLutFactors() {
		return this.lutFactors;
	}

	public void setLutFactors(List<LutFactor> lutFactors) {
		this.lutFactors = lutFactors;
	}

	public LutFactor addLutFactor(LutFactor lutFactor) {
		getLutFactors().add(lutFactor);
		lutFactor.setLutGroupOfFactor(this);

		return lutFactor;
	}

	public LutFactor removeLutFactor(LutFactor lutFactor) {
		getLutFactors().remove(lutFactor);
		lutFactor.setLutGroupOfFactor(null);

		return lutFactor;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

}