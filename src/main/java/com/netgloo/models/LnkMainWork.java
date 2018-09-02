package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_MAIN_WORK database table.
 * 
 */
@Entity
@Table(name="LNK_MAIN_WORK")
@NamedQuery(name="LnkMainWork.findAll", query="SELECT l FROM LnkMainWork l")
public class LnkMainWork implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_MAIN_WORK_SEQ", sequenceName="LNK_MAIN_WORK_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_MAIN_WORK_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Lob
	private String note;

	private String notedate;

	private long stepid;

	private long wid;
	
	private long mid;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="mid", insertable=false, updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public LnkMainWork() {
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNotedate() {
		return this.notedate;
	}

	public void setNotedate(String notedate) {
		this.notedate = notedate;
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

}