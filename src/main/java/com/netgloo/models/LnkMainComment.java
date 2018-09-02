package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_MAIN_COMMENT database table.
 * 
 */
@Entity
@Table(name="LNK_MAIN_COMMENT")
@NamedQuery(name="LnkMainComment.findAll", query="SELECT l FROM LnkMainComment l")
public class LnkMainComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_MAIN_COMMENT_SEQ", sequenceName="LNK_MAIN_COMMENT_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_MAIN_COMMENT_SEQ")
	@Column(name = "id", unique = true, nullable = false)	
	
	private long id;

	@Lob
	private String note;

	private String notedate;
	
	private String username;

	private long parentid;
	private long positionid;

	private long wid;
	private long mid;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="mid", insertable=false, updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public LnkMainComment() {
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

	public long getParentid() {
		return this.parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}

	public long getWid() {
		return this.wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}

	public long getPositionid() {
		return positionid;
	}

	public void setPositionid(long positionid) {
		this.positionid = positionid;
	}

}