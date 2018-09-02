package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SOURCE_DATA database table.
 * 
 */
@Entity
@Table(name="SOURCE_DATA")
@NamedQuery(name="SourceData.findAll", query="SELECT s FROM SourceData s")
public class SourceData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SOURCE_DATA_SEQ", sequenceName="SOURCE_DATA_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="SOURCE_DATA_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name="\"COLUMN\"")
	private long column;

	private String formname;

	private String text;

	@Column(name="\"VALUE\"")
	private long value;

	public SourceData() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getColumn() {
		return this.column;
	}

	public void setColumn(long column) {
		this.column = column;
	}

	public String getFormname() {
		return this.formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getValue() {
		return this.value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}