package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.netgloo.models.LnkTryoutCat;

import com.netgloo.models.LnkTryoutConfMethod;
import com.netgloo.models.LnkTryoutConfSource;
import com.netgloo.models.LnkTryoutConfType;

import java.util.List;


/**
 * The persistent class for the LUT_TRYOUT database table.
 * 
 */
@Entity
@Table(name="LUT_TRYOUT")
@NamedQuery(name="LutTryout.findAll", query="SELECT l FROM LutTryout l")
public class LutTryout implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_TRYOUT_SEQ", sequenceName="LUT_TRYOUT_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_TRYOUT_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private long adirid;

	private String formdesc;

	private String text;

	private long workid;
	
	@Transient
	private String rtext;
	
	//bi-directional many-to-one association to LnkTryoutConfMethod
	@OneToMany(mappedBy="lutTryout")
	private List<LnkTryoutConfMethod> lnkTryoutConfMethods;

	//bi-directional many-to-one association to LnkTryoutConfSource
	@OneToMany(mappedBy="lutTryout")
	private List<LnkTryoutConfSource> lnkTryoutConfSources;

	//bi-directional many-to-one association to LnkTryoutConfType
	@OneToMany(mappedBy="lutTryout")
	private List<LnkTryoutConfType> lnkTryoutConfTypes;
	
	//bi-directional many-to-one association to LnkTryoutCat
	@OneToMany(mappedBy="lutTryout")
	private List<LnkTryoutCat> lnkTryoutCats;

	//bi-directional many-to-one association to LnkTryoutNotice
	@OneToMany(mappedBy="lutTryout")
	private List<LnkTryoutNotice> lnkTryoutNotices;

	//bi-directional many-to-one association to LnkTryoutProcedure
	@OneToMany(mappedBy="lutTryout")
	private List<LnkTryoutProcedure> lnkTryoutProcedures;

	//bi-directional many-to-one association to LnkTryoutRisk
	@OneToMany(mappedBy="lutTryout")
	private List<LnkTryoutRisk> lnkTryoutRisks;
	
	
	//bi-directional many-to-one association to LnkRiskTryout
	@OneToMany(mappedBy="lutTryout")
	private List<LnkRiskTryout> lnkRiskTryouts;
	
	
	public LutTryout() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAdirid() {
		return this.adirid;
	}

	public void setAdirid(long adirid) {
		this.adirid = adirid;
	}

	public String getFormdesc() {
		return this.formdesc;
	}

	public void setFormdesc(String formdesc) {
		this.formdesc = formdesc;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getWorkid() {
		return this.workid;
	}

	public void setWorkid(long workid) {
		this.workid = workid;
	}

	public List<LnkTryoutNotice> getLnkTryoutNotices() {
		return this.lnkTryoutNotices;
	}

	public void setLnkTryoutNotices(List<LnkTryoutNotice> lnkTryoutNotices) {
		this.lnkTryoutNotices = lnkTryoutNotices;
	}

	public LnkTryoutNotice addLnkTryoutNotice(LnkTryoutNotice lnkTryoutNotice) {
		getLnkTryoutNotices().add(lnkTryoutNotice);
		lnkTryoutNotice.setLutTryout(this);

		return lnkTryoutNotice;
	}

	public LnkTryoutNotice removeLnkTryoutNotice(LnkTryoutNotice lnkTryoutNotice) {
		getLnkTryoutNotices().remove(lnkTryoutNotice);
		lnkTryoutNotice.setLutTryout(null);

		return lnkTryoutNotice;
	}

	public List<LnkTryoutProcedure> getLnkTryoutProcedures() {
		return this.lnkTryoutProcedures;
	}

	public void setLnkTryoutProcedures(List<LnkTryoutProcedure> lnkTryoutProcedures) {
		this.lnkTryoutProcedures = lnkTryoutProcedures;
	}

	public LnkTryoutProcedure addLnkTryoutProcedure(LnkTryoutProcedure lnkTryoutProcedure) {
		getLnkTryoutProcedures().add(lnkTryoutProcedure);
		lnkTryoutProcedure.setLutTryout(this);

		return lnkTryoutProcedure;
	}

	public LnkTryoutProcedure removeLnkTryoutProcedure(LnkTryoutProcedure lnkTryoutProcedure) {
		getLnkTryoutProcedures().remove(lnkTryoutProcedure);
		lnkTryoutProcedure.setLutTryout(null);

		return lnkTryoutProcedure;
	}

	public List<LnkTryoutRisk> getLnkTryoutRisks() {
		return this.lnkTryoutRisks;
	}

	public void setLnkTryoutRisks(List<LnkTryoutRisk> lnkTryoutRisks) {
		this.lnkTryoutRisks = lnkTryoutRisks;
	}

	public LnkTryoutRisk addLnkTryoutRisk(LnkTryoutRisk lnkTryoutRisk) {
		getLnkTryoutRisks().add(lnkTryoutRisk);
		lnkTryoutRisk.setLutTryout(this);

		return lnkTryoutRisk;
	}

	public LnkTryoutRisk removeLnkTryoutRisk(LnkTryoutRisk lnkTryoutRisk) {
		getLnkTryoutRisks().remove(lnkTryoutRisk);
		lnkTryoutRisk.setLutTryout(null);

		return lnkTryoutRisk;
	}
	
	public List<LnkTryoutCat> getLnkTryoutCats() {
		return this.lnkTryoutCats;
	}

	public void setLnkTryoutCats(List<LnkTryoutCat> lnkTryoutCats) {
		this.lnkTryoutCats = lnkTryoutCats;
	}

	public LnkTryoutCat addLnkTryoutCat(LnkTryoutCat lnkTryoutCat) {
		getLnkTryoutCats().add(lnkTryoutCat);
		lnkTryoutCat.setLutTryout(this);

		return lnkTryoutCat;
	}

	public LnkTryoutCat removeLnkTryoutCat(LnkTryoutCat lnkTryoutCat) {
		getLnkTryoutCats().remove(lnkTryoutCat);
		lnkTryoutCat.setLutTryout(null);

		return lnkTryoutCat;
	}

	public List<LnkRiskTryout> getLnkRiskTryouts() {
		return this.lnkRiskTryouts;
	}

	public void setLnkRiskTryouts(List<LnkRiskTryout> lnkRiskTryouts) {
		this.lnkRiskTryouts = lnkRiskTryouts;
	}

	public LnkRiskTryout addLnkRiskTryout(LnkRiskTryout lnkRiskTryout) {
		getLnkRiskTryouts().add(lnkRiskTryout);
		lnkRiskTryout.setLutTryout(this);

		return lnkRiskTryout;
	}

	public LnkRiskTryout removeLnkRiskTryout(LnkRiskTryout lnkRiskTryout) {
		getLnkRiskTryouts().remove(lnkRiskTryout);
		lnkRiskTryout.setLutTryout(null);

		return lnkRiskTryout;
	}

	public List<LnkTryoutConfMethod> getLnkTryoutConfMethods() {
		return lnkTryoutConfMethods;
	}

	public void setLnkTryoutConfMethods(List<LnkTryoutConfMethod> lnkTryoutConfMethods) {
		this.lnkTryoutConfMethods = lnkTryoutConfMethods;
	}

	public List<LnkTryoutConfSource> getLnkTryoutConfSources() {
		return lnkTryoutConfSources;
	}

	public void setLnkTryoutConfSources(List<LnkTryoutConfSource> lnkTryoutConfSources) {
		this.lnkTryoutConfSources = lnkTryoutConfSources;
	}

	public List<LnkTryoutConfType> getLnkTryoutConfTypes() {
		return lnkTryoutConfTypes;
	}

	public void setLnkTryoutConfTypes(List<LnkTryoutConfType> lnkTryoutConfTypes) {
		this.lnkTryoutConfTypes = lnkTryoutConfTypes;
	}

	public String getRtext() {
		return rtext;
	}

	public void setRtext(String rtext) {
		this.rtext = rtext;
	}
	
	
}