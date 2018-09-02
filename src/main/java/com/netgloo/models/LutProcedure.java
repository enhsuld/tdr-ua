package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.netgloo.models.LnkDirectionProcedure;
import com.netgloo.models.LnkTryoutProcedure;


/**
 * The persistent class for the LUT_PROCEDURE database table.
 * 
 */
@Entity
@Table(name="LUT_PROCEDURE")
@NamedQuery(name="LutProcedure.findAll", query="SELECT l FROM LutProcedure l")
public class LutProcedure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String adescribe;
	
	private String aprocedure;

	private String standart;

	private String way;
	
	//bi-directional many-to-one association to LnkDirectionProcedure
		@OneToMany(mappedBy="lutProcedure")
		private List<LnkDirectionProcedure> lnkDirectionProcedures;

		//bi-directional many-to-one association to LnkTryoutProcedure
		@OneToMany(mappedBy="lutProcedure")
		private List<LnkTryoutProcedure> lnkTryoutProcedures;

	public LutProcedure() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAdescribe() {
		return this.adescribe;
	}

	public void setAdescribe(String adescribe) {
		this.adescribe = adescribe;
	}

	public String getAprocedure() {
		return this.aprocedure;
	}

	public void setAprocedure(String aprocedure) {
		this.aprocedure = aprocedure;
	}

	public String getStandart() {
		return this.standart;
	}

	public void setStandart(String standart) {
		this.standart = standart;
	}

	public String getWay() {
		return this.way;
	}

	public void setWay(String way) {
		this.way = way;
	}
	
	public List<LnkDirectionProcedure> getLnkDirectionProcedures() {
		return this.lnkDirectionProcedures;
	}

	public void setLnkDirectionProcedures(List<LnkDirectionProcedure> lnkDirectionProcedures) {
		this.lnkDirectionProcedures = lnkDirectionProcedures;
	}

	public LnkDirectionProcedure addLnkDirectionProcedure(LnkDirectionProcedure lnkDirectionProcedure) {
		getLnkDirectionProcedures().add(lnkDirectionProcedure);
		lnkDirectionProcedure.setLutProcedure(this);

		return lnkDirectionProcedure;
	}

	public LnkDirectionProcedure removeLnkDirectionProcedure(LnkDirectionProcedure lnkDirectionProcedure) {
		getLnkDirectionProcedures().remove(lnkDirectionProcedure);
		lnkDirectionProcedure.setLutProcedure(null);

		return lnkDirectionProcedure;
	}

	public List<LnkTryoutProcedure> getLnkTryoutProcedures() {
		return this.lnkTryoutProcedures;
	}

	public void setLnkTryoutProcedures(List<LnkTryoutProcedure> lnkTryoutProcedures) {
		this.lnkTryoutProcedures = lnkTryoutProcedures;
	}

	public LnkTryoutProcedure addLnkTryoutProcedure(LnkTryoutProcedure lnkTryoutProcedure) {
		getLnkTryoutProcedures().add(lnkTryoutProcedure);
		lnkTryoutProcedure.setLutProcedure(this);

		return lnkTryoutProcedure;
	}

	public LnkTryoutProcedure removeLnkTryoutProcedure(LnkTryoutProcedure lnkTryoutProcedure) {
		getLnkTryoutProcedures().remove(lnkTryoutProcedure);
		lnkTryoutProcedure.setLutProcedure(null);

		return lnkTryoutProcedure;
	}

}