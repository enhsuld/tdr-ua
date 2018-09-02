package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.FinAbw;
import com.netgloo.models.FinAsset;
import com.netgloo.models.FinBudget;
import com.netgloo.models.FinCbw;
import com.netgloo.models.FinCt1a;
import com.netgloo.models.FinCt2a;
import com.netgloo.models.FinCt3a;
import com.netgloo.models.FinCt4a;
import com.netgloo.models.FinCtt1;
import com.netgloo.models.FinCtt3;
import com.netgloo.models.FinCtt4;
import com.netgloo.models.FinCtt5;
import com.netgloo.models.FinCtt7;
import com.netgloo.models.FinCtt8;
import com.netgloo.models.FinInventory;
import com.netgloo.models.FinJournal;
import com.netgloo.models.FinNt2;
import com.netgloo.models.FinPayroll;
import com.netgloo.models.FinTgt1;
import com.netgloo.models.FinTgt1a;
import com.netgloo.models.FinTrialBalance;

import com.netgloo.models.FsFormA4;


import java.util.List;


/**
 * The persistent class for the MAIN_AUDIT_REGISTRATION database table.
 * 
 */
@Entity
@Table(name="MAIN_AUDIT_REGISTRATION")
@NamedQuery(name="MainAuditRegistration.findAll", query="SELECT m FROM MainAuditRegistration m")
public class MainAuditRegistration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAIN_AUDIT_REGISTRATION_SEQ", sequenceName="MAIN_AUDIT_REGISTRATION_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="MAIN_AUDIT_REGISTRATION_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String auditors;

	private String audityear;

	private long autype;

	private String checkers;

	private long depid;

	private String enddate;

	private String gencode;

	private boolean isactive;
	
	private boolean isenabled;
	
	private long orgid;

	private String managers;

	private String orgcode;

	private String orgname;

	private long orgtype;

	private String startdate;

	private long stepid;
	
	private long aper;
	
	private long mper;
	
	private long tper;
	
	private long a2per;
	
	private long m2per;
	
	private long t2per;
	
	private long a3per;
	
	private long m3per;
	
	private long t3per;
	
	private Long reporttype;

	private String terguuleh;
	
	private String auditname;
	
	private String tuuver;
	
	@Transient
	private String data1;
	@Transient
	private String data2;
	@Transient
	private String data3;
	@Transient
	private String data4;
	@Transient
	private String data5;
	@Transient
	private String data6;
	@Transient
	private String data7;
	@Transient
	private String data8;
	
	
	//bi-directional many-to-one association to LnkRiskTryout
	@OneToMany(mappedBy="lnkRiskT2")
	@JsonBackReference
	private List<LnkRiskTryout> lnkRiskTryouts;
	
	
	@ManyToOne
	@JoinColumn(name="depid" ,insertable=false, updatable=false)
	@JsonBackReference
	private LutDepartment lutDepartment;
	
	//bi-directional many-to-one association to LutFormB1
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LutFormB1> lutFormB1s;
	
	//bi-directional many-to-one association to FinFormB21
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<FinFormB21> finFormB21s;
	
	//bi-directional many-to-one association to FinFormB12
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<FinFormB12> finFormB12s;
	
	//bi-directional many-to-one association to FinFormB1
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<FinFormB1> finFormB1s;
	
	//bi-directional many-to-one association to FsFormA4
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<FsFormA4> fsFormA4s;
	
	//bi-directional many-to-one association to LnkMainFormT2
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LnkMainFormT2> lnkMainFormT2s;

	//bi-directional many-to-one association to FsForm241
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<FsForm241> fsForm241s;
	
	//bi-directional many-to-one association to StsCheckVariable
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<StsCheckVariable> stsCheckVariables;

	//bi-directional many-to-one association to LnkMainAttach
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LnkMainAttach> lnkMainAttaches;

	//bi-directional many-to-one association to LnkMainComment
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LnkMainComment> lnkMainComments;

	//bi-directional many-to-one association to LnkMainWork
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LnkMainWork> lnkMainWorks;

	//bi-directional many-to-one association to LnkMainUser
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LnkMainUser> lnkMainUsers;

	//bi-directional many-to-one association to LnkMainForm
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LnkMainForm> lnkMainForms;
	
	//bi-directional many-to-one association to LnkMainTransition
	@OneToMany(mappedBy="mainAuditRegistration")
	@JsonBackReference
	private List<LnkMainTransition> lnkMainTransitions;
	
	//bi-directional many-to-one association to FinAbw
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinAbw> finAbws;

		//bi-directional many-to-one association to FinAsset
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinAsset> finAssets;

		//bi-directional many-to-one association to FinBudget
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinBudget> finBudgets;

		//bi-directional many-to-one association to FinCbw
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCbw> finCbws;

		//bi-directional many-to-one association to FinCt1a
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCt1a> finCt1as;

		//bi-directional many-to-one association to FinCt2a
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCt2a> finCt2as;

		//bi-directional many-to-one association to FinCt3a
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCt3a> finCt3as;

		//bi-directional many-to-one association to FinCt4a
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCt4a> finCt4as;

		//bi-directional many-to-one association to FinCtt1
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCtt1> finCtt1s;

		//bi-directional many-to-one association to FinCtt2
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCtt3> finCtt2s;

		//bi-directional many-to-one association to FinCtt3
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCtt4> finCtt3s;

		//bi-directional many-to-one association to FinCtt4
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCtt5> finCtt4s;

		//bi-directional many-to-one association to FinCtt5
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCtt7> finCtt5s;

		//bi-directional many-to-one association to FinCtt6
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinCtt8> finCtt6s;

		//bi-directional many-to-one association to FinInventory
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinInventory> finInventories;

		//bi-directional many-to-one association to FinJournal
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinJournal> finJournals;

		//bi-directional many-to-one association to FinNt2
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinNt2> finNt2s;

		//bi-directional many-to-one association to FinPayroll
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinPayroll> finPayrolls;

		//bi-directional many-to-one association to FinTgt1
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinTgt1> finTgt1s;

		//bi-directional many-to-one association to FinTgt1a
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinTgt1a> finTgt1as;

		//bi-directional many-to-one association to FinTrialBalance
		@OneToMany(mappedBy="mainAuditRegistration")
		@JsonBackReference
		private List<FinTrialBalance> finTrialBalances;
	
	public MainAuditRegistration() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuditors() {
		return this.auditors;
	}

	public void setAuditors(String auditors) {
		this.auditors = auditors;
	}

	public String getAudityear() {
		return this.audityear;
	}

	public void setAudityear(String audityear) {
		this.audityear = audityear;
	}

	public long getAutype() {
		return this.autype;
	}

	public void setAutype(long autype) {
		this.autype = autype;
	}

	public String getCheckers() {
		return this.checkers;
	}

	public void setCheckers(String checkers) {
		this.checkers = checkers;
	}

	public long getDepid() {
		return this.depid;
	}

	public void setDepid(long depid) {
		this.depid = depid;
	}

	public String getEnddate() {
		return this.enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getGencode() {
		return this.gencode;
	}

	public void setGencode(String gencode) {
		this.gencode = gencode;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public String getManagers() {
		return this.managers;
	}

	public void setManagers(String managers) {
		this.managers = managers;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getOrgname() {
		return this.orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public long getOrgtype() {
		return this.orgtype;
	}

	public void setOrgtype(long orgtype) {
		this.orgtype = orgtype;
	}

	public String getStartdate() {
		return this.startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public long getStepid() {
		return this.stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public String getTerguuleh() {
		return this.terguuleh;
	}

	public void setTerguuleh(String terguuleh) {
		this.terguuleh = terguuleh;
	}
	
	
	public long getAper() {
		return aper;
	}

	public void setAper(long aper) {
		this.aper = aper;
	}

	public long getMper() {
		return mper;
	}

	public void setMper(long mper) {
		this.mper = mper;
	}

	public long getTper() {
		return tper;
	}

	public void setTper(long tper) {
		this.tper = tper;
	}
	
	public long getA2per() {
		return a2per;
	}

	public void setA2per(long a2per) {
		this.a2per = a2per;
	}

	public long getM2per() {
		return m2per;
	}

	public void setM2per(long m2per) {
		this.m2per = m2per;
	}

	public long getT2per() {
		return t2per;
	}

	public void setT2per(long t2per) {
		this.t2per = t2per;
	}

	public long getA3per() {
		return a3per;
	}

	public void setA3per(long a3per) {
		this.a3per = a3per;
	}

	public long getM3per() {
		return m3per;
	}

	public void setM3per(long m3per) {
		this.m3per = m3per;
	}

	public long getT3per() {
		return t3per;
	}

	public void setT3per(long t3per) {
		this.t3per = t3per;
	}
	

	public String getTuuver() {
		return tuuver;
	}

	public void setTuuver(String tuuver) {
		this.tuuver = tuuver;
	}

	public boolean isIsenabled() {
		return isenabled;
	}

	public void setIsenabled(boolean isenabled) {
		this.isenabled = isenabled;
	}
	
	public long getOrgid() {
		return orgid;
	}

	public void setOrgid(long orgid) {
		this.orgid = orgid;
	}
	
	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

	public String getData6() {
		return data6;
	}

	public void setData6(String data6) {
		this.data6 = data6;
	}

	public String getData7() {
		return data7;
	}

	public void setData7(String data7) {
		this.data7 = data7;
	}

	public String getData8() {
		return data8;
	}

	public void setData8(String data8) {
		this.data8 = data8;
	}

	public List<LnkMainAttach> getLnkMainAttaches() {
		return this.lnkMainAttaches;
	}

	public void setLnkMainAttaches(List<LnkMainAttach> lnkMainAttaches) {
		this.lnkMainAttaches = lnkMainAttaches;
	}

	public LnkMainAttach addLnkMainAttach(LnkMainAttach lnkMainAttach) {
		getLnkMainAttaches().add(lnkMainAttach);
		lnkMainAttach.setMainAuditRegistration(this);

		return lnkMainAttach;
	}

	public LnkMainAttach removeLnkMainAttach(LnkMainAttach lnkMainAttach) {
		getLnkMainAttaches().remove(lnkMainAttach);
		lnkMainAttach.setMainAuditRegistration(null);

		return lnkMainAttach;
	}

	public List<LnkMainComment> getLnkMainComments() {
		return this.lnkMainComments;
	}

	public void setLnkMainComments(List<LnkMainComment> lnkMainComments) {
		this.lnkMainComments = lnkMainComments;
	}

	public LnkMainComment addLnkMainComment(LnkMainComment lnkMainComment) {
		getLnkMainComments().add(lnkMainComment);
		lnkMainComment.setMainAuditRegistration(this);

		return lnkMainComment;
	}

	public LnkMainComment removeLnkMainComment(LnkMainComment lnkMainComment) {
		getLnkMainComments().remove(lnkMainComment);
		lnkMainComment.setMainAuditRegistration(null);

		return lnkMainComment;
	}

	public List<LnkMainWork> getLnkMainWorks() {
		return this.lnkMainWorks;
	}

	public void setLnkMainWorks(List<LnkMainWork> lnkMainWorks) {
		this.lnkMainWorks = lnkMainWorks;
	}

	public LnkMainWork addLnkMainWork(LnkMainWork lnkMainWork) {
		getLnkMainWorks().add(lnkMainWork);
		lnkMainWork.setMainAuditRegistration(this);

		return lnkMainWork;
	}

	public LnkMainWork removeLnkMainWork(LnkMainWork lnkMainWork) {
		getLnkMainWorks().remove(lnkMainWork);
		lnkMainWork.setMainAuditRegistration(null);

		return lnkMainWork;
	}

	public List<LnkMainUser> getLnkMainUsers() {
		return this.lnkMainUsers;
	}

	public void setLnkMainUsers(List<LnkMainUser> lnkMainUsers) {
		this.lnkMainUsers = lnkMainUsers;
	}

	public LnkMainUser addLnkMainUser(LnkMainUser lnkMainUser) {
		getLnkMainUsers().add(lnkMainUser);
		lnkMainUser.setMainAuditRegistration(this);

		return lnkMainUser;
	}

	public LnkMainUser removeLnkMainUser(LnkMainUser lnkMainUser) {
		getLnkMainUsers().remove(lnkMainUser);
		lnkMainUser.setMainAuditRegistration(null);

		return lnkMainUser;
	}
	
	public List<LnkMainForm> getLnkMainForms() {
		return this.lnkMainForms;
	}

	public void setLnkMainForms(List<LnkMainForm> lnkMainForms) {
		this.lnkMainForms = lnkMainForms;
	}

	public LnkMainForm addLnkMainForm(LnkMainForm lnkMainForm) {
		getLnkMainForms().add(lnkMainForm);
		lnkMainForm.setMainAuditRegistration(this);

		return lnkMainForm;
	}

	public LnkMainForm removeLnkMainForm(LnkMainForm lnkMainForm) {
		getLnkMainForms().remove(lnkMainForm);
		lnkMainForm.setMainAuditRegistration(null);

		return lnkMainForm;
	}
	
	public List<LnkMainTransition> getLnkMainTransitions() {
		return this.lnkMainTransitions;
	}

	public void setLnkMainTransitions(List<LnkMainTransition> lnkMainTransitions) {
		this.lnkMainTransitions = lnkMainTransitions;
	}

	public LnkMainTransition addLnkMainTransition(LnkMainTransition lnkMainTransition) {
		getLnkMainTransitions().add(lnkMainTransition);
		lnkMainTransition.setMainAuditRegistration(this);

		return lnkMainTransition;
	}

	public LnkMainTransition removeLnkMainTransition(LnkMainTransition lnkMainTransition) {
		getLnkMainTransitions().remove(lnkMainTransition);
		lnkMainTransition.setMainAuditRegistration(null);

		return lnkMainTransition;
	}
	
	public List<FinAbw> getFinAbws() {
		return this.finAbws;
	}

	public void setFinAbws(List<FinAbw> finAbws) {
		this.finAbws = finAbws;
	}

	public FinAbw addFinAbw(FinAbw finAbw) {
		getFinAbws().add(finAbw);
		finAbw.setMainAuditRegistration(this);

		return finAbw;
	}

	public FinAbw removeFinAbw(FinAbw finAbw) {
		getFinAbws().remove(finAbw);
		finAbw.setMainAuditRegistration(null);

		return finAbw;
	}

	public List<FinAsset> getFinAssets() {
		return this.finAssets;
	}

	public void setFinAssets(List<FinAsset> finAssets) {
		this.finAssets = finAssets;
	}

	public FinAsset addFinAsset(FinAsset finAsset) {
		getFinAssets().add(finAsset);
		finAsset.setMainAuditRegistration(this);

		return finAsset;
	}

	public FinAsset removeFinAsset(FinAsset finAsset) {
		getFinAssets().remove(finAsset);
		finAsset.setMainAuditRegistration(null);

		return finAsset;
	}

	public List<FinBudget> getFinBudgets() {
		return this.finBudgets;
	}

	public void setFinBudgets(List<FinBudget> finBudgets) {
		this.finBudgets = finBudgets;
	}

	public FinBudget addFinBudget(FinBudget finBudget) {
		getFinBudgets().add(finBudget);
		finBudget.setMainAuditRegistration(this);

		return finBudget;
	}

	public FinBudget removeFinBudget(FinBudget finBudget) {
		getFinBudgets().remove(finBudget);
		finBudget.setMainAuditRegistration(null);

		return finBudget;
	}

	public List<FinCbw> getFinCbws() {
		return this.finCbws;
	}

	public void setFinCbws(List<FinCbw> finCbws) {
		this.finCbws = finCbws;
	}

	public FinCbw addFinCbw(FinCbw finCbw) {
		getFinCbws().add(finCbw);
		finCbw.setMainAuditRegistration(this);

		return finCbw;
	}

	public FinCbw removeFinCbw(FinCbw finCbw) {
		getFinCbws().remove(finCbw);
		finCbw.setMainAuditRegistration(null);

		return finCbw;
	}

	public List<FinCt1a> getFinCt1as() {
		return this.finCt1as;
	}

	public void setFinCt1as(List<FinCt1a> finCt1as) {
		this.finCt1as = finCt1as;
	}

	public FinCt1a addFinCt1a(FinCt1a finCt1a) {
		getFinCt1as().add(finCt1a);
		finCt1a.setMainAuditRegistration(this);

		return finCt1a;
	}

	public FinCt1a removeFinCt1a(FinCt1a finCt1a) {
		getFinCt1as().remove(finCt1a);
		finCt1a.setMainAuditRegistration(null);

		return finCt1a;
	}

	public List<FinCt2a> getFinCt2as() {
		return this.finCt2as;
	}

	public void setFinCt2as(List<FinCt2a> finCt2as) {
		this.finCt2as = finCt2as;
	}

	public FinCt2a addFinCt2a(FinCt2a finCt2a) {
		getFinCt2as().add(finCt2a);
		finCt2a.setMainAuditRegistration(this);

		return finCt2a;
	}

	public FinCt2a removeFinCt2a(FinCt2a finCt2a) {
		getFinCt2as().remove(finCt2a);
		finCt2a.setMainAuditRegistration(null);

		return finCt2a;
	}

	public List<FinCt3a> getFinCt3as() {
		return this.finCt3as;
	}

	public void setFinCt3as(List<FinCt3a> finCt3as) {
		this.finCt3as = finCt3as;
	}

	public FinCt3a addFinCt3a(FinCt3a finCt3a) {
		getFinCt3as().add(finCt3a);
		finCt3a.setMainAuditRegistration(this);

		return finCt3a;
	}

	public FinCt3a removeFinCt3a(FinCt3a finCt3a) {
		getFinCt3as().remove(finCt3a);
		finCt3a.setMainAuditRegistration(null);

		return finCt3a;
	}

	public List<FinCt4a> getFinCt4as() {
		return this.finCt4as;
	}

	public void setFinCt4as(List<FinCt4a> finCt4as) {
		this.finCt4as = finCt4as;
	}

	public FinCt4a addFinCt4a(FinCt4a finCt4a) {
		getFinCt4as().add(finCt4a);
		finCt4a.setMainAuditRegistration(this);

		return finCt4a;
	}

	public FinCt4a removeFinCt4a(FinCt4a finCt4a) {
		getFinCt4as().remove(finCt4a);
		finCt4a.setMainAuditRegistration(null);

		return finCt4a;
	}

	public List<FinCtt1> getFinCtt1s() {
		return this.finCtt1s;
	}

	public void setFinCtt1s(List<FinCtt1> finCtt1s) {
		this.finCtt1s = finCtt1s;
	}

	public FinCtt1 addFinCtt1(FinCtt1 finCtt1) {
		getFinCtt1s().add(finCtt1);
		finCtt1.setMainAuditRegistration(this);

		return finCtt1;
	}

	public FinCtt1 removeFinCtt1(FinCtt1 finCtt1) {
		getFinCtt1s().remove(finCtt1);
		finCtt1.setMainAuditRegistration(null);

		return finCtt1;
	}

	public List<FinCtt3> getFinCtt2s() {
		return this.finCtt2s;
	}

	public void setFinCtt2s(List<FinCtt3> finCtt2s) {
		this.finCtt2s = finCtt2s;
	}

	public FinCtt3 addFinCtt2(FinCtt3 finCtt2) {
		getFinCtt2s().add(finCtt2);
		finCtt2.setMainAuditRegistration(this);

		return finCtt2;
	}

	public FinCtt3 removeFinCtt2(FinCtt3 finCtt2) {
		getFinCtt2s().remove(finCtt2);
		finCtt2.setMainAuditRegistration(null);

		return finCtt2;
	}

	public List<FinCtt4> getFinCtt3s() {
		return this.finCtt3s;
	}

	public void setFinCtt3s(List<FinCtt4> finCtt3s) {
		this.finCtt3s = finCtt3s;
	}

	public FinCtt4 addFinCtt3(FinCtt4 finCtt3) {
		getFinCtt3s().add(finCtt3);
		finCtt3.setMainAuditRegistration(this);

		return finCtt3;
	}

	public FinCtt4 removeFinCtt3(FinCtt4 finCtt3) {
		getFinCtt3s().remove(finCtt3);
		finCtt3.setMainAuditRegistration(null);

		return finCtt3;
	}

	public List<FinCtt5> getFinCtt4s() {
		return this.finCtt4s;
	}

	public void setFinCtt4s(List<FinCtt5> finCtt4s) {
		this.finCtt4s = finCtt4s;
	}

	public FinCtt5 addFinCtt4(FinCtt5 finCtt4) {
		getFinCtt4s().add(finCtt4);
		finCtt4.setMainAuditRegistration(this);

		return finCtt4;
	}

	public FinCtt5 removeFinCtt4(FinCtt5 finCtt4) {
		getFinCtt4s().remove(finCtt4);
		finCtt4.setMainAuditRegistration(null);

		return finCtt4;
	}

	public List<FinCtt7> getFinCtt5s() {
		return this.finCtt5s;
	}

	public void setFinCtt5s(List<FinCtt7> finCtt5s) {
		this.finCtt5s = finCtt5s;
	}

	public FinCtt7 addFinCtt5(FinCtt7 finCtt5) {
		getFinCtt5s().add(finCtt5);
		finCtt5.setMainAuditRegistration(this);

		return finCtt5;
	}

	public FinCtt7 removeFinCtt5(FinCtt7 finCtt5) {
		getFinCtt5s().remove(finCtt5);
		finCtt5.setMainAuditRegistration(null);

		return finCtt5;
	}

	public List<FinCtt8> getFinCtt6s() {
		return this.finCtt6s;
	}

	public void setFinCtt6s(List<FinCtt8> finCtt6s) {
		this.finCtt6s = finCtt6s;
	}

	public FinCtt8 addFinCtt6(FinCtt8 finCtt6) {
		getFinCtt6s().add(finCtt6);
		finCtt6.setMainAuditRegistration(this);

		return finCtt6;
	}

	public FinCtt8 removeFinCtt6(FinCtt8 finCtt6) {
		getFinCtt6s().remove(finCtt6);
		finCtt6.setMainAuditRegistration(null);

		return finCtt6;
	}

	public List<FinInventory> getFinInventories() {
		return this.finInventories;
	}

	public void setFinInventories(List<FinInventory> finInventories) {
		this.finInventories = finInventories;
	}

	public FinInventory addFinInventory(FinInventory finInventory) {
		getFinInventories().add(finInventory);
		finInventory.setMainAuditRegistration(this);

		return finInventory;
	}

	public FinInventory removeFinInventory(FinInventory finInventory) {
		getFinInventories().remove(finInventory);
		finInventory.setMainAuditRegistration(null);

		return finInventory;
	}

	public List<FinJournal> getFinJournals() {
		return this.finJournals;
	}

	public void setFinJournals(List<FinJournal> finJournals) {
		this.finJournals = finJournals;
	}

	public FinJournal addFinJournal(FinJournal finJournal) {
		getFinJournals().add(finJournal);
		finJournal.setMainAuditRegistration(this);

		return finJournal;
	}

	public FinJournal removeFinJournal(FinJournal finJournal) {
		getFinJournals().remove(finJournal);
		finJournal.setMainAuditRegistration(null);

		return finJournal;
	}

	public List<FinNt2> getFinNt2s() {
		return this.finNt2s;
	}

	public void setFinNt2s(List<FinNt2> finNt2s) {
		this.finNt2s = finNt2s;
	}

	public FinNt2 addFinNt2(FinNt2 finNt2) {
		getFinNt2s().add(finNt2);
		finNt2.setMainAuditRegistration(this);

		return finNt2;
	}

	public FinNt2 removeFinNt2(FinNt2 finNt2) {
		getFinNt2s().remove(finNt2);
		finNt2.setMainAuditRegistration(null);

		return finNt2;
	}

	public List<FinPayroll> getFinPayrolls() {
		return this.finPayrolls;
	}

	public void setFinPayrolls(List<FinPayroll> finPayrolls) {
		this.finPayrolls = finPayrolls;
	}

	public FinPayroll addFinPayroll(FinPayroll finPayroll) {
		getFinPayrolls().add(finPayroll);
		finPayroll.setMainAuditRegistration(this);

		return finPayroll;
	}

	public FinPayroll removeFinPayroll(FinPayroll finPayroll) {
		getFinPayrolls().remove(finPayroll);
		finPayroll.setMainAuditRegistration(null);

		return finPayroll;
	}

	public List<FinTgt1> getFinTgt1s() {
		return this.finTgt1s;
	}

	public void setFinTgt1s(List<FinTgt1> finTgt1s) {
		this.finTgt1s = finTgt1s;
	}

	public FinTgt1 addFinTgt1(FinTgt1 finTgt1) {
		getFinTgt1s().add(finTgt1);
		finTgt1.setMainAuditRegistration(this);

		return finTgt1;
	}

	public FinTgt1 removeFinTgt1(FinTgt1 finTgt1) {
		getFinTgt1s().remove(finTgt1);
		finTgt1.setMainAuditRegistration(null);

		return finTgt1;
	}

	public List<FinTgt1a> getFinTgt1as() {
		return this.finTgt1as;
	}

	public void setFinTgt1as(List<FinTgt1a> finTgt1as) {
		this.finTgt1as = finTgt1as;
	}

	public FinTgt1a addFinTgt1a(FinTgt1a finTgt1a) {
		getFinTgt1as().add(finTgt1a);
		finTgt1a.setMainAuditRegistration(this);

		return finTgt1a;
	}

	public FinTgt1a removeFinTgt1a(FinTgt1a finTgt1a) {
		getFinTgt1as().remove(finTgt1a);
		finTgt1a.setMainAuditRegistration(null);

		return finTgt1a;
	}

	public List<FinTrialBalance> getFinTrialBalances() {
		return this.finTrialBalances;
	}

	public void setFinTrialBalances(List<FinTrialBalance> finTrialBalances) {
		this.finTrialBalances = finTrialBalances;
	}

	public FinTrialBalance addFinTrialBalance(FinTrialBalance finTrialBalance) {
		getFinTrialBalances().add(finTrialBalance);
		finTrialBalance.setMainAuditRegistration(this);

		return finTrialBalance;
	}

	public FinTrialBalance removeFinTrialBalance(FinTrialBalance finTrialBalance) {
		getFinTrialBalances().remove(finTrialBalance);
		finTrialBalance.setMainAuditRegistration(null);

		return finTrialBalance;
	}
	
	public List<FsForm241> getFsForm241s() {
		return this.fsForm241s;
	}

	public void setFsForm241s(List<FsForm241> fsForm241s) {
		this.fsForm241s = fsForm241s;
	}

	public FsForm241 addFsForm241(FsForm241 fsForm241) {
		getFsForm241s().add(fsForm241);
		fsForm241.setMainAuditRegistration(this);

		return fsForm241;
	}

	public FsForm241 removeFsForm241(FsForm241 fsForm241) {
		getFsForm241s().remove(fsForm241);
		fsForm241.setMainAuditRegistration(null);

		return fsForm241;
	}
	
	public List<LnkMainFormT2> getLnkMainFormT2s() {
		return this.lnkMainFormT2s;
	}
	
	public List<StsCheckVariable> getStsCheckVariables() {
		return this.stsCheckVariables;
	}

	public void setLnkMainFormT2s(List<LnkMainFormT2> lnkMainFormT2s) {
		this.lnkMainFormT2s = lnkMainFormT2s;
	}

	public LnkMainFormT2 addLnkMainFormT2(LnkMainFormT2 lnkMainFormT2) {
		getLnkMainFormT2s().add(lnkMainFormT2);
		lnkMainFormT2.setMainAuditRegistration(this);

		return lnkMainFormT2;
	}

	public LnkMainFormT2 removeLnkMainFormT2(LnkMainFormT2 lnkMainFormT2) {
		getLnkMainFormT2s().remove(lnkMainFormT2);
		lnkMainFormT2.setMainAuditRegistration(null);

		return lnkMainFormT2;
	}

	public void setStsCheckVariables(List<StsCheckVariable> stsCheckVariables) {
		this.stsCheckVariables = stsCheckVariables;
	}

	public StsCheckVariable addStsCheckVariable(StsCheckVariable stsCheckVariable) {
		getStsCheckVariables().add(stsCheckVariable);
		stsCheckVariable.setMainAuditRegistration(this);

		return stsCheckVariable;
	}

	public StsCheckVariable removeStsCheckVariable(StsCheckVariable stsCheckVariable) {
		getStsCheckVariables().remove(stsCheckVariable);
		stsCheckVariable.setMainAuditRegistration(null);

		return stsCheckVariable;
	}
	
	public List<FsFormA4> getFsFormA4s() {
		return this.fsFormA4s;
	}

	public void setFsFormA4s(List<FsFormA4> fsFormA4s) {
		this.fsFormA4s = fsFormA4s;
	}

	public FsFormA4 addFsFormA4(FsFormA4 fsFormA4) {
		getFsFormA4s().add(fsFormA4);
		fsFormA4.setMainAuditRegistration(this);

		return fsFormA4;
	}

	public FsFormA4 removeFsFormA4(FsFormA4 fsFormA4) {
		getFsFormA4s().remove(fsFormA4);
		fsFormA4.setMainAuditRegistration(null);

		return fsFormA4;
	}
	
	public List<FinFormB1> getFinFormB1s() {
		return this.finFormB1s;
	}

	public void setFinFormB1s(List<FinFormB1> finFormB1s) {
		this.finFormB1s = finFormB1s;
	}

	public FinFormB1 addFinFormB1(FinFormB1 finFormB1) {
		getFinFormB1s().add(finFormB1);
		finFormB1.setMainAuditRegistration(this);

		return finFormB1;
	}

	public FinFormB1 removeFinFormB1(FinFormB1 finFormB1) {
		getFinFormB1s().remove(finFormB1);
		finFormB1.setMainAuditRegistration(null);

		return finFormB1;
	}

	public List<FinFormB12> getFinFormB12s() {
		return this.finFormB12s;
	}

	public void setFinFormB12s(List<FinFormB12> finFormB12s) {
		this.finFormB12s = finFormB12s;
	}

	public FinFormB12 addFinFormB12(FinFormB12 finFormB12) {
		getFinFormB12s().add(finFormB12);
		finFormB12.setMainAuditRegistration(this);

		return finFormB12;
	}

	public FinFormB12 removeFinFormB12(FinFormB12 finFormB12) {
		getFinFormB12s().remove(finFormB12);
		finFormB12.setMainAuditRegistration(null);

		return finFormB12;
	}
	
	public List<FinFormB21> getFinFormB21s() {
		return this.finFormB21s;
	}

	public void setFinFormB21s(List<FinFormB21> finFormB21s) {
		this.finFormB21s = finFormB21s;
	}

	public FinFormB21 addFinFormB21(FinFormB21 finFormB21) {
		getFinFormB21s().add(finFormB21);
		finFormB21.setMainAuditRegistration(this);

		return finFormB21;
	}

	public FinFormB21 removeFinFormB21(FinFormB21 finFormB21) {
		getFinFormB21s().remove(finFormB21);
		finFormB21.setMainAuditRegistration(null);

		return finFormB21;
	}
	
	public List<LutFormB1> getLutFormB1s() {
		return this.lutFormB1s;
	}

	public void setLutFormB1s(List<LutFormB1> lutFormB1s) {
		this.lutFormB1s = lutFormB1s;
	}

	public LutFormB1 addLutFormB1(LutFormB1 lutFormB1) {
		getLutFormB1s().add(lutFormB1);
		lutFormB1.setMainAuditRegistration(this);

		return lutFormB1;
	}

	public LutFormB1 removeLutFormB1(LutFormB1 lutFormB1) {
		getLutFormB1s().remove(lutFormB1);
		lutFormB1.setMainAuditRegistration(null);

		return lutFormB1;
	}

	public Long getReporttype() {
		return reporttype;
	}

	public void setReporttype(Long reporttype) {
		this.reporttype = reporttype;
	}

	public String getAuditname() {
		return auditname;
	}

	public void setAuditname(String auditname) {
		this.auditname = auditname;
	}

	public LutDepartment getLutDepartment() {
		return lutDepartment;
	}

	public void setLutDepartment(LutDepartment lutDepartment) {
		this.lutDepartment = lutDepartment;
	}

	public List<LnkRiskTryout> getLnkRiskTryouts() {
		return lnkRiskTryouts;
	}

	public void setLnkRiskTryouts(List<LnkRiskTryout> lnkRiskTryouts) {
		this.lnkRiskTryouts = lnkRiskTryouts;
	}
}