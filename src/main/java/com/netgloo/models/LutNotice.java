package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.netgloo.models.LnkDirectionNotice;
import com.netgloo.models.LnkTryoutNotice;


/**
 * The persistent class for the LUT_NOTICE database table.
 * 
 */
@Entity
@Table(name="LUT_NOTICE")
@NamedQuery(name="LutNotice.findAll", query="SELECT l FROM LutNotice l")
public class LutNotice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String adescribe;

	private String name;

	private String shortname;
	
	//bi-directional many-to-one association to LnkDirectionNotice
	@OneToMany(mappedBy="lutNotice")
	@JsonManagedReference
	private List<LnkDirectionNotice> lnkDirectionNotices;

	//bi-directional many-to-one association to LnkTryoutNotice
	@OneToMany(mappedBy="lutNotice")
	@JsonManagedReference
	private List<LnkTryoutNotice> lnkTryoutNotices;

	public LutNotice() {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	
	public List<LnkDirectionNotice> getLnkDirectionNotices() {
		return this.lnkDirectionNotices;
	}

	public void setLnkDirectionNotices(List<LnkDirectionNotice> lnkDirectionNotices) {
		this.lnkDirectionNotices = lnkDirectionNotices;
	}

	public LnkDirectionNotice addLnkDirectionNotice(LnkDirectionNotice lnkDirectionNotice) {
		getLnkDirectionNotices().add(lnkDirectionNotice);
		lnkDirectionNotice.setLutNotice(this);

		return lnkDirectionNotice;
	}

	public LnkDirectionNotice removeLnkDirectionNotice(LnkDirectionNotice lnkDirectionNotice) {
		getLnkDirectionNotices().remove(lnkDirectionNotice);
		lnkDirectionNotice.setLutNotice(null);

		return lnkDirectionNotice;
	}

	public List<LnkTryoutNotice> getLnkTryoutNotices() {
		return this.lnkTryoutNotices;
	}

	public void setLnkTryoutNotices(List<LnkTryoutNotice> lnkTryoutNotices) {
		this.lnkTryoutNotices = lnkTryoutNotices;
	}

	public LnkTryoutNotice addLnkTryoutNotice(LnkTryoutNotice lnkTryoutNotice) {
		getLnkTryoutNotices().add(lnkTryoutNotice);
		lnkTryoutNotice.setLutNotice(this);

		return lnkTryoutNotice;
	}

	public LnkTryoutNotice removeLnkTryoutNotice(LnkTryoutNotice lnkTryoutNotice) {
		getLnkTryoutNotices().remove(lnkTryoutNotice);
		lnkTryoutNotice.setLutNotice(null);

		return lnkTryoutNotice;
	}

}