package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * The persistent class for the LNK_MAIN_ATTACH database table.
 * 
 */
@Entity
@Table(name="LNK_MAIN_ATTACH")
@NamedQuery(name="LnkMainAttach.findAll", query="SELECT l FROM LnkMainAttach l")
public class LnkMainAttach implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_MAIN_ATTACH_SEQ", sequenceName="LNK_MAIN_ATTACH_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_MAIN_ATTACH_SEQ")
	@Column(name = "id", unique = true, nullable = false)	
	private long id;

	private String atdate;

	private String fileext;

	private String filename;

	private String fileurl;

	private long stepid;

	private long wid;
	
	private long mid;
	
	//bi-directional many-to-one association to FinFormB21
	@OneToMany(mappedBy="lnkMainAttach")
	@JsonBackReference
	private List<FinFormB21> finFormB21s;

	//bi-directional many-to-one association to FinFormB12
	@OneToMany(mappedBy="lnkMainAttach")
	@JsonBackReference
	private List<FinFormB12> finFormB12s;
	
	//bi-directional many-to-one association to FinFormB1
	@OneToMany(mappedBy="lnkMainAttach")
	@JsonBackReference
	private List<FinFormB1> finFormB1s;
	
	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="mid", insertable=false, updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public LnkMainAttach() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getAtdate() {
		return this.atdate;
	}

	public void setAtdate(String atdate) {
		this.atdate = atdate;
	}

	public String getFileext() {
		return this.fileext;
	}

	public void setFileext(String fileext) {
		this.fileext = fileext;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileurl() {
		return this.fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public long getStepid() {
		return this.stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public long getWid() {
		return this.wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}
	
	public List<FinFormB1> getFinFormB1s() {
		return this.finFormB1s;
	}

	public void setFinFormB1s(List<FinFormB1> finFormB1s) {
		this.finFormB1s = finFormB1s;
	}

	public FinFormB1 addFinFormB1(FinFormB1 finFormB1) {
		getFinFormB1s().add(finFormB1);
		finFormB1.setLnkMainAttach(this);

		return finFormB1;
	}

	public FinFormB1 removeFinFormB1(FinFormB1 finFormB1) {
		getFinFormB1s().remove(finFormB1);
		finFormB1.setLnkMainAttach(null);

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
		finFormB12.setLnkMainAttach(this);

		return finFormB12;
	}

	public FinFormB12 removeFinFormB12(FinFormB12 finFormB12) {
		getFinFormB12s().remove(finFormB12);
		finFormB12.setLnkMainAttach(null);

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
		finFormB21.setLnkMainAttach(this);

		return finFormB21;
	}

	public FinFormB21 removeFinFormB21(FinFormB21 finFormB21) {
		getFinFormB21s().remove(finFormB21);
		finFormB21.setLnkMainAttach(null);

		return finFormB21;
	}

}