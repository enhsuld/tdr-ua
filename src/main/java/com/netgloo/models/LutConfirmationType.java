package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.netgloo.models.LnkTryoutConfType;


/**
 * The persistent class for the LUT_CONFIRMATION_TYPE database table.
 * 
 */
@Entity
@Table(name="LUT_CONFIRMATION_TYPE")
@NamedQuery(name="LutConfirmationType.findAll", query="SELECT l FROM LutConfirmationType l")
public class LutConfirmationType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_CONFIRMATION_TYPE_SEQ", sequenceName="LUT_CONFIRMATION_TYPE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_CONFIRMATION_TYPE_SEQ")
	private long id;

	private String name;

	//bi-directional many-to-one association to LnkTryoutConfType
	@OneToMany(mappedBy="lutConfirmationType")
	private List<LnkTryoutConfType> lnkTryoutConfTypes;

	public LutConfirmationType() {
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

	public List<LnkTryoutConfType> getLnkTryoutConfTypes() {
		return this.lnkTryoutConfTypes;
	}

	public void setLnkTryoutConfTypes(List<LnkTryoutConfType> lnkTryoutConfTypes) {
		this.lnkTryoutConfTypes = lnkTryoutConfTypes;
	}

	public LnkTryoutConfType addLnkTryoutConfType(LnkTryoutConfType lnkTryoutConfType) {
		getLnkTryoutConfTypes().add(lnkTryoutConfType);
		lnkTryoutConfType.setLutConfirmationType(this);

		return lnkTryoutConfType;
	}

	public LnkTryoutConfType removeLnkTryoutConfType(LnkTryoutConfType lnkTryoutConfType) {
		getLnkTryoutConfTypes().remove(lnkTryoutConfType);
		lnkTryoutConfType.setLutConfirmationType(null);

		return lnkTryoutConfType;
	}

}