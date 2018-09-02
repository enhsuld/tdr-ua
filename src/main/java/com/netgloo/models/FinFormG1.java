package com.netgloo.models;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.netgloo.models.MainAuditRegistration;

import java.math.BigDecimal;


/**
 * The persistent class for the FIN_CTT3 database table.
 * 
 */
@Entity
@Table(name="FIN_FORM_G1")
@NamedQuery(name="FinFormG1.findAll", query="SELECT f FROM FinFormG1 f")
public class FinFormG1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FIN_FORM_G1_SEQ", sequenceName="FIN_FORM_G1_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FIN_FORM_G1_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name="ROW1_1")
	private String row1_1;
	
	@Column(name="ROW1_2")
	private String row1_2;
	
	@Column(name="ROW2_1")
	private String row2_1;
	
	@Column(name="ROW2_2")
	private String row2_2;
	
	@Column(name="ROW3_1")
	private String row3_1;
	
	@Column(name="ROW3_2")
	private String row3_2;
	
	@Column(name="ROW4_1")
	private String row4_1;
	
	@Column(name="ROW4_2")
	private String row4_2;
	
	@Column(name="ROW5_1")
	private String row5_1;
	
	@Column(name="ROW5_2")
	private String row5_2;
	
	@Column(name="ROW6_1")
	private String row6_1;
	
	@Column(name="ROW6_2")
	private String row6_2;
	
	@Column(name="ROW7_1")
	private String row7_1;
	
	@Column(name="ROW7_2")
	private String row7_2;
	
	@Column(name="ROW8_1")
	private String row8_1;
	
	@Column(name="ROW8_2")
	private String row8_2;
	
	@Column(name="ROW9_1")
	private String row9_1;
	
	@Column(name="ROW9_2")
	private String row9_2;
	
	@Column(name="ROW10_1")
	private String row10_1;
	
	@Column(name="ROW10_2")
	private String row10_2;
	
	@Column(name="ROW11_1")
	private String row11_1;
	
	@Column(name="ROW11_2")
	private String row11_2;
	
	@Column(name="ROW12_1")
	private String row12_1;
	
	@Column(name="ROW12_2")
	private String row12_2;
	
	@Column(name="ROW13_1")
	private String row13_1;
	
	@Column(name="ROW13_2")
	private String row13_2;
	
	@Column(name="ROW14_1")
	private String row14_1;
	
	@Column(name="ROW14_2")
	private String row14_2;
	
	@Column(name="ROW15_1")
	private String row15_1;
	
	@Column(name="ROW15_2")
	private String row15_2;
	
	@Column(name="ROW16_1")
	private String row16_1;
	
	@Column(name="ROW16_2")
	private String row16_2;
	
	@Column(name="ROW17_1")
	private String row17_1;
	
	@Column(name="ROW17_2")
	private String row17_2;
	
	@Column(name="ROW18_1")
	private String row18_1;
	
	@Column(name="ROW18_2")
	private String row18_2;
	
	@Column(name="ROW19_1")
	private String row19_1;
	
	@Column(name="ROW19_2")
	private String row19_2;
	
	@Column(name="ROW20_1")
	private String row20_1;
	
	@Column(name="ROW20_2")
	private String row20_2;
	
	@Column(name="ROW21_1")
	private String row21_1;
	
	@Column(name="ROW21_2")
	private String row21_2;
	
	@Column(name="ROW22_1")
	private String row22_1;
	
	@Column(name="ROW22_2")
	private String row22_2;
	
	@Column(name="ROW23_1")
	private String row23_1;
	
	@Column(name="ROW23_2")
	private String row23_2;
	
	@Column(name="ROW24_1")
	private String row24_1;
	
	@Column(name="ROW24_2")
	private String row24_2;
	
	@Column(name="ROW25_1")
	private String row25_1;
	
	@Column(name="ROW25_2")
	private String row25_2;
	
	@Column(name="ROW26_1")
	private String row26_1;
	
	@Column(name="ROW26_2")
	private String row26_2;

	private long mid;
	
	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="mid",nullable = false,insertable=false,updatable=false)
	@JsonBackReference
	private MainAuditRegistration mainAuditRegistration;

	public FinFormG1() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}

	public String getRow1_1() {
		return row1_1;
	}

	public void setRow1_1(String row1_1) {
		this.row1_1 = row1_1;
	}

	public String getRow1_2() {
		return row1_2;
	}

	public void setRow1_2(String row1_2) {
		this.row1_2 = row1_2;
	}

	public String getRow2_1() {
		return row2_1;
	}

	public void setRow2_1(String row2_1) {
		this.row2_1 = row2_1;
	}

	public String getRow2_2() {
		return row2_2;
	}

	public void setRow2_2(String row2_2) {
		this.row2_2 = row2_2;
	}

	public String getRow3_1() {
		return row3_1;
	}

	public void setRow3_1(String row3_1) {
		this.row3_1 = row3_1;
	}

	public String getRow3_2() {
		return row3_2;
	}

	public void setRow3_2(String row3_2) {
		this.row3_2 = row3_2;
	}

	public String getRow4_1() {
		return row4_1;
	}

	public void setRow4_1(String row4_1) {
		this.row4_1 = row4_1;
	}

	public String getRow4_2() {
		return row4_2;
	}

	public void setRow4_2(String row4_2) {
		this.row4_2 = row4_2;
	}

	public String getRow5_1() {
		return row5_1;
	}

	public void setRow5_1(String row5_1) {
		this.row5_1 = row5_1;
	}

	public String getRow5_2() {
		return row5_2;
	}

	public void setRow5_2(String row5_2) {
		this.row5_2 = row5_2;
	}

	public String getRow6_1() {
		return row6_1;
	}

	public void setRow6_1(String row6_1) {
		this.row6_1 = row6_1;
	}

	public String getRow6_2() {
		return row6_2;
	}

	public void setRow6_2(String row6_2) {
		this.row6_2 = row6_2;
	}

	public String getRow7_1() {
		return row7_1;
	}

	public void setRow7_1(String row7_1) {
		this.row7_1 = row7_1;
	}

	public String getRow7_2() {
		return row7_2;
	}

	public void setRow7_2(String row7_2) {
		this.row7_2 = row7_2;
	}

	public String getRow8_1() {
		return row8_1;
	}

	public void setRow8_1(String row8_1) {
		this.row8_1 = row8_1;
	}

	public String getRow8_2() {
		return row8_2;
	}

	public void setRow8_2(String row8_2) {
		this.row8_2 = row8_2;
	}

	public String getRow9_1() {
		return row9_1;
	}

	public void setRow9_1(String row9_1) {
		this.row9_1 = row9_1;
	}

	public String getRow9_2() {
		return row9_2;
	}

	public void setRow9_2(String row9_2) {
		this.row9_2 = row9_2;
	}

	public String getRow10_1() {
		return row10_1;
	}

	public void setRow10_1(String row10_1) {
		this.row10_1 = row10_1;
	}

	public String getRow10_2() {
		return row10_2;
	}

	public void setRow10_2(String row10_2) {
		this.row10_2 = row10_2;
	}

	public String getRow11_1() {
		return row11_1;
	}

	public void setRow11_1(String row11_1) {
		this.row11_1 = row11_1;
	}

	public String getRow11_2() {
		return row11_2;
	}

	public void setRow11_2(String row11_2) {
		this.row11_2 = row11_2;
	}

	public String getRow12_1() {
		return row12_1;
	}

	public void setRow12_1(String row12_1) {
		this.row12_1 = row12_1;
	}

	public String getRow12_2() {
		return row12_2;
	}

	public void setRow12_2(String row12_2) {
		this.row12_2 = row12_2;
	}

	public String getRow13_1() {
		return row13_1;
	}

	public void setRow13_1(String row13_1) {
		this.row13_1 = row13_1;
	}

	public String getRow13_2() {
		return row13_2;
	}

	public void setRow13_2(String row13_2) {
		this.row13_2 = row13_2;
	}

	public String getRow14_1() {
		return row14_1;
	}

	public void setRow14_1(String row14_1) {
		this.row14_1 = row14_1;
	}

	public String getRow14_2() {
		return row14_2;
	}

	public void setRow14_2(String row14_2) {
		this.row14_2 = row14_2;
	}

	public String getRow15_1() {
		return row15_1;
	}

	public void setRow15_1(String row15_1) {
		this.row15_1 = row15_1;
	}

	public String getRow15_2() {
		return row15_2;
	}

	public void setRow15_2(String row15_2) {
		this.row15_2 = row15_2;
	}

	public String getRow16_1() {
		return row16_1;
	}

	public void setRow16_1(String row16_1) {
		this.row16_1 = row16_1;
	}

	public String getRow16_2() {
		return row16_2;
	}

	public void setRow16_2(String row16_2) {
		this.row16_2 = row16_2;
	}

	public String getRow17_1() {
		return row17_1;
	}

	public void setRow17_1(String row17_1) {
		this.row17_1 = row17_1;
	}

	public String getRow17_2() {
		return row17_2;
	}

	public void setRow17_2(String row17_2) {
		this.row17_2 = row17_2;
	}

	public String getRow18_1() {
		return row18_1;
	}

	public void setRow18_1(String row18_1) {
		this.row18_1 = row18_1;
	}

	public String getRow18_2() {
		return row18_2;
	}

	public void setRow18_2(String row18_2) {
		this.row18_2 = row18_2;
	}

	public String getRow19_1() {
		return row19_1;
	}

	public void setRow19_1(String row19_1) {
		this.row19_1 = row19_1;
	}

	public String getRow19_2() {
		return row19_2;
	}

	public void setRow19_2(String row19_2) {
		this.row19_2 = row19_2;
	}

	public String getRow20_1() {
		return row20_1;
	}

	public void setRow20_1(String row20_1) {
		this.row20_1 = row20_1;
	}

	public String getRow20_2() {
		return row20_2;
	}

	public void setRow20_2(String row20_2) {
		this.row20_2 = row20_2;
	}

	public String getRow21_1() {
		return row21_1;
	}

	public void setRow21_1(String row21_1) {
		this.row21_1 = row21_1;
	}

	public String getRow21_2() {
		return row21_2;
	}

	public void setRow21_2(String row21_2) {
		this.row21_2 = row21_2;
	}

	public String getRow22_1() {
		return row22_1;
	}

	public void setRow22_1(String row22_1) {
		this.row22_1 = row22_1;
	}

	public String getRow22_2() {
		return row22_2;
	}

	public void setRow22_2(String row22_2) {
		this.row22_2 = row22_2;
	}

	public String getRow23_1() {
		return row23_1;
	}

	public void setRow23_1(String row23_1) {
		this.row23_1 = row23_1;
	}

	public String getRow23_2() {
		return row23_2;
	}

	public void setRow23_2(String row23_2) {
		this.row23_2 = row23_2;
	}

	public String getRow24_1() {
		return row24_1;
	}

	public void setRow24_1(String row24_1) {
		this.row24_1 = row24_1;
	}

	public String getRow24_2() {
		return row24_2;
	}

	public void setRow24_2(String row24_2) {
		this.row24_2 = row24_2;
	}

	public String getRow25_1() {
		return row25_1;
	}

	public void setRow25_1(String row25_1) {
		this.row25_1 = row25_1;
	}

	public String getRow25_2() {
		return row25_2;
	}

	public void setRow25_2(String row25_2) {
		this.row25_2 = row25_2;
	}

	public String getRow26_1() {
		return row26_1;
	}

	public void setRow26_1(String row26_1) {
		this.row26_1 = row26_1;
	}

	public String getRow26_2() {
		return row26_2;
	}

	public void setRow26_2(String row26_2) {
		this.row26_2 = row26_2;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}
	
	public void setField(String aFieldName, Object aValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field aField = getClass().getDeclaredField(aFieldName);
		aField.set(this, aValue);
    }
}