package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the FS_LUT_FORM_TYPE database table.
 * 
 */
@Entity
@Table(name="FS_LUT_FORM_TYPE")
@NamedQuery(name="FsLutFormType.findAll", query="SELECT f FROM FsLutFormType f")
public class FsLutFormType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FS_LUT_FORM_TYPE_SEQ", sequenceName="FS_LUT_FORM_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FS_LUT_FORM_TYPE_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String formname;

	public FsLutFormType() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFormname() {
		return this.formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

}