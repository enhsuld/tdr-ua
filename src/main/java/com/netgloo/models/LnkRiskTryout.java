package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;



/**
 * The persistent class for the LNK_RISK_TRYOUT database table.
 * 
 */
@Entity
@Table(name="LNK_RISK_TRYOUT")
@NamedQuery(name="LnkRiskTryout.findAll", query="SELECT l FROM LnkRiskTryout l")
public class LnkRiskTryout implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_RISK_TRYOUT_SEQ", sequenceName="LNK_RISK_TRYOUT_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_RISK_TRYOUT_SEQ")
	private long id;

	private long dirid;

	private long rt2id;

	private long riskid;

	private long tryid;
	
	private long mid;
	
	private String data1;
	private String data2;
	private String data3;
	private String data4;
	private String data5;
	private String data6;
	private String data7;
	private String data8;
	private String data9;
	private String data10;
	private String data11;
	private String data12;
	private String data13;
	private String data14;
	private String data15;
	private String data16;
	private String data17;
	private String data18;
	private String data19;
	private String data20;
	private String data21;
	private String data22;	
	private Integer data23;
	private String data24;
	private Integer data25;
	private String data26;
	private String data27;
	private String data28;
	private String data29;
	private String data30;
	
	private String b44data1;
	private String b44data2;
	private String b44data3;
	private String b44data4;
	private String b44data5;
	private String b44data6;
	private String b44data7;
	private String b44data8;
	private String b44data9;
	private String b44data10;
	private String b44data11;
	private String b44data12;
	private int b44data13;
	private String b44data14;
	private String b44data15;
	private String b44data16;
	private String b44data17;
	
	private String b45data1;
	private String b45data2;
	private String b45data3;
	private String b45data4;
	private String b45data5;
	private String b45data6;
	private String b45data7;
	private String b45data8;
	private String b45data9;
	private String b45data10;
	private String b45data11;
	
	private String b46data1;
	private String b46data2;
	private String b46data3;
	private int b46data4;
	private String b46data5;
	
	private String b461data1;
	private String b461data2;
	private String b461data3;
	private String b461data4;
	
	private String b462data1;
	private String b462data2;
	private String b462data3;
	private String b462data4;
	private String b462data5;
	
	private String b463data1;
	private String b463data2;
	private String b463data3;
	private String b463data4;
	private String b463data5;
	
	private String b464data1;
	private String b464data2;
	private String b464data3;
	private String b464data4;
	private String b464data5;
	private String b464data6;
	private String b464data7;
	
	private String b465data1;
	private String b465data2;
	private String b465data3;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="mid",nullable = false,insertable=false,updatable=false)
	private MainAuditRegistration mainAuditRegistration;
	
	@ManyToOne
	@JoinColumn(name="rt2id", insertable=false,updatable=false)
	@JsonBackReference
	private LnkRiskT2 lnkRiskT2;
	
	//bi-directional many-to-one association to LutRisk
	@ManyToOne
	@JoinColumn(name="riskid", insertable=false,updatable=false)
	@JsonBackReference
	private LutRisk lutRisk;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne
	@JoinColumn(name="tryid", insertable=false,updatable=false)
	@JsonBackReference
	private LutTryout lutTryout;
	
	//bi-directional many-to-one association to LnkTryoutFile
	@OneToMany(mappedBy="lnkRiskTryout")
	@JsonManagedReference
	private List<LnkTryoutFile> lnkTryoutFiles;

	public LnkRiskTryout() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDirid() {
		return this.dirid;
	}

	public void setDirid(long dirid) {
		this.dirid = dirid;
	}

	public long getRt2id() {
		return this.rt2id;
	}

	public void setRt2id(long rt2id) {
		this.rt2id = rt2id;
	}

	public LutRisk getLutRisk() {
		return this.lutRisk;
	}

	public void setLutRisk(LutRisk lutRisk) {
		this.lutRisk = lutRisk;
	}

	public LutTryout getLutTryout() {
		return this.lutTryout;
	}

	public void setLutTryout(LutTryout lutTryout) {
		this.lutTryout = lutTryout;
	}

	public long getRiskid() {
		return riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}

	public long getTryid() {
		return tryid;
	}

	public void setTryid(long tryid) {
		this.tryid = tryid;
	}

	public LnkRiskT2 getLnkRiskT2() {
		return lnkRiskT2;
	}

	public void setLnkRiskT2(LnkRiskT2 lnkRiskT2) {
		this.lnkRiskT2 = lnkRiskT2;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
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

	public String getData9() {
		return data9;
	}

	public void setData9(String data9) {
		this.data9 = data9;
	}

	public String getData10() {
		return data10;
	}

	public void setData10(String data10) {
		this.data10 = data10;
	}

	public String getData11() {
		return data11;
	}

	public void setData11(String data11) {
		this.data11 = data11;
	}

	public String getData12() {
		return data12;
	}

	public void setData12(String data12) {
		this.data12 = data12;
	}

	public String getData13() {
		return data13;
	}

	public void setData13(String data13) {
		this.data13 = data13;
	}

	public String getData14() {
		return data14;
	}

	public void setData14(String data14) {
		this.data14 = data14;
	}

	public String getData15() {
		return data15;
	}

	public void setData15(String data15) {
		this.data15 = data15;
	}

	public String getData16() {
		return data16;
	}

	public void setData16(String data16) {
		this.data16 = data16;
	}

	public String getData17() {
		return data17;
	}

	public void setData17(String data17) {
		this.data17 = data17;
	}

	public String getData18() {
		return data18;
	}

	public void setData18(String data18) {
		this.data18 = data18;
	}

	public String getData19() {
		return data19;
	}

	public void setData19(String data19) {
		this.data19 = data19;
	}

	public String getData20() {
		return data20;
	}

	public void setData20(String data20) {
		this.data20 = data20;
	}

	public String getData21() {
		return data21;
	}

	public void setData21(String data21) {
		this.data21 = data21;
	}

	public String getData22() {
		return data22;
	}

	public void setData22(String data22) {
		this.data22 = data22;
	}
	
	public Integer getData23() {
		return data23;
	}

	public void setData23(Integer data23) {
		this.data23 = data23;
	}

	public String getData24() {
		return data24;
	}

	public void setData24(String data24) {
		this.data24 = data24;
	}

	public Integer getData25() {
		return data25;
	}

	public void setData25(Integer data25) {
		this.data25 = data25;
	}

	public String getData26() {
		return data26;
	}

	public void setData26(String data26) {
		this.data26 = data26;
	}

	public String getData27() {
		return data27;
	}

	public void setData27(String data27) {
		this.data27 = data27;
	}

	public String getData28() {
		return data28;
	}

	public void setData28(String data28) {
		this.data28 = data28;
	}

	public String getData29() {
		return data29;
	}

	public void setData29(String data29) {
		this.data29 = data29;
	}

	public String getData30() {
		return data30;
	}

	public void setData30(String data30) {
		this.data30 = data30;
	}
	
	public String getB44data1() {
		return b44data1;
	}

	public void setB44data1(String b44data1) {
		this.b44data1 = b44data1;
	}

	public String getB44data2() {
		return b44data2;
	}

	public void setB44data2(String b44data2) {
		this.b44data2 = b44data2;
	}

	public String getB44data3() {
		return b44data3;
	}

	public void setB44data3(String b44data3) {
		this.b44data3 = b44data3;
	}

	public String getB44data4() {
		return b44data4;
	}

	public void setB44data4(String b44data4) {
		this.b44data4 = b44data4;
	}

	public String getB44data5() {
		return b44data5;
	}

	public void setB44data5(String b44data5) {
		this.b44data5 = b44data5;
	}

	public String getB44data6() {
		return b44data6;
	}

	public void setB44data6(String b44data6) {
		this.b44data6 = b44data6;
	}

	public String getB44data7() {
		return b44data7;
	}

	public void setB44data7(String b44data7) {
		this.b44data7 = b44data7;
	}

	public String getB44data8() {
		return b44data8;
	}

	public void setB44data8(String b44data8) {
		this.b44data8 = b44data8;
	}

	public String getB44data9() {
		return b44data9;
	}

	public void setB44data9(String b44data9) {
		this.b44data9 = b44data9;
	}

	public String getB44data10() {
		return b44data10;
	}

	public void setB44data10(String b44data10) {
		this.b44data10 = b44data10;
	}

	public String getB44data11() {
		return b44data11;
	}

	public void setB44data11(String b44data11) {
		this.b44data11 = b44data11;
	}

	public String getB44data12() {
		return b44data12;
	}

	public void setB44data12(String b44data12) {
		this.b44data12 = b44data12;
	}

	public int getB44data13() {
		return b44data13;
	}

	public void setB44data13(int b44data13) {
		this.b44data13 = b44data13;
	}

	public String getB44data14() {
		return b44data14;
	}

	public void setB44data14(String b44data14) {
		this.b44data14 = b44data14;
	}

	public String getB44data15() {
		return b44data15;
	}

	public void setB44data15(String b44data15) {
		this.b44data15 = b44data15;
	}

	public String getB44data16() {
		return b44data16;
	}

	public void setB44data16(String b44data16) {
		this.b44data16 = b44data16;
	}

	public String getB44data17() {
		return b44data17;
	}

	public void setB44data17(String b44data17) {
		this.b44data17 = b44data17;
	}
	
	public String getB45data1() {
		return b45data1;
	}

	public void setB45data1(String b45data1) {
		this.b45data1 = b45data1;
	}

	public String getB45data2() {
		return b45data2;
	}

	public void setB45data2(String b45data2) {
		this.b45data2 = b45data2;
	}

	public String getB45data3() {
		return b45data3;
	}

	public void setB45data3(String b45data3) {
		this.b45data3 = b45data3;
	}

	public String getB45data4() {
		return b45data4;
	}

	public void setB45data4(String b45data4) {
		this.b45data4 = b45data4;
	}

	public String getB45data5() {
		return b45data5;
	}

	public void setB45data5(String b45data5) {
		this.b45data5 = b45data5;
	}

	public String getB45data6() {
		return b45data6;
	}

	public void setB45data6(String b45data6) {
		this.b45data6 = b45data6;
	}

	public String getB45data7() {
		return b45data7;
	}

	public void setB45data7(String b45data7) {
		this.b45data7 = b45data7;
	}

	public String getB45data8() {
		return b45data8;
	}

	public void setB45data8(String b45data8) {
		this.b45data8 = b45data8;
	}

	public String getB45data9() {
		return b45data9;
	}

	public void setB45data9(String b45data9) {
		this.b45data9 = b45data9;
	}

	public String getB45data10() {
		return b45data10;
	}

	public void setB45data10(String b45data10) {
		this.b45data10 = b45data10;
	}

	public String getB45data11() {
		return b45data11;
	}

	public void setB45data11(String b45data11) {
		this.b45data11 = b45data11;
	}
	
	public String getB46data1() {
		return b46data1;
	}

	public void setB46data1(String b46data1) {
		this.b46data1 = b46data1;
	}

	public String getB46data2() {
		return b46data2;
	}

	public void setB46data2(String b46data2) {
		this.b46data2 = b46data2;
	}

	public String getB46data3() {
		return b46data3;
	}

	public void setB46data3(String b46data3) {
		this.b46data3 = b46data3;
	}

	public int getB46data4() {
		return b46data4;
	}

	public void setB46data4(int b46data4) {
		this.b46data4 = b46data4;
	}

	public String getB46data5() {
		return b46data5;
	}

	public void setB46data5(String b46data5) {
		this.b46data5 = b46data5;
	}

	public String getB461data1() {
		return b461data1;
	}

	public void setB461data1(String b461data1) {
		this.b461data1 = b461data1;
	}

	public String getB461data2() {
		return b461data2;
	}

	public void setB461data2(String b461data2) {
		this.b461data2 = b461data2;
	}

	public String getB461data3() {
		return b461data3;
	}

	public void setB461data3(String b461data3) {
		this.b461data3 = b461data3;
	}

	public String getB461data4() {
		return b461data4;
	}

	public void setB461data4(String b461data4) {
		this.b461data4 = b461data4;
	}

	public String getB462data1() {
		return b462data1;
	}

	public void setB462data1(String b462data1) {
		this.b462data1 = b462data1;
	}

	public String getB462data2() {
		return b462data2;
	}

	public void setB462data2(String b462data2) {
		this.b462data2 = b462data2;
	}

	public String getB462data3() {
		return b462data3;
	}

	public void setB462data3(String b462data3) {
		this.b462data3 = b462data3;
	}

	public String getB462data4() {
		return b462data4;
	}

	public void setB462data4(String b462data4) {
		this.b462data4 = b462data4;
	}

	public String getB462data5() {
		return b462data5;
	}

	public void setB462data5(String b462data5) {
		this.b462data5 = b462data5;
	}

	public String getB463data1() {
		return b463data1;
	}

	public void setB463data1(String b463data1) {
		this.b463data1 = b463data1;
	}

	public String getB463data2() {
		return b463data2;
	}

	public void setB463data2(String b463data2) {
		this.b463data2 = b463data2;
	}

	public String getB463data3() {
		return b463data3;
	}

	public void setB463data3(String b463data3) {
		this.b463data3 = b463data3;
	}

	public String getB463data4() {
		return b463data4;
	}

	public void setB463data4(String b463data4) {
		this.b463data4 = b463data4;
	}

	public String getB463data5() {
		return b463data5;
	}

	public void setB463data5(String b463data5) {
		this.b463data5 = b463data5;
	}

	public String getB464data1() {
		return b464data1;
	}

	public void setB464data1(String b464data1) {
		this.b464data1 = b464data1;
	}

	public String getB464data2() {
		return b464data2;
	}

	public void setB464data2(String b464data2) {
		this.b464data2 = b464data2;
	}

	public String getB464data3() {
		return b464data3;
	}

	public void setB464data3(String b464data3) {
		this.b464data3 = b464data3;
	}

	public String getB464data4() {
		return b464data4;
	}

	public void setB464data4(String b464data4) {
		this.b464data4 = b464data4;
	}

	public String getB464data5() {
		return b464data5;
	}

	public void setB464data5(String b464data5) {
		this.b464data5 = b464data5;
	}

	public String getB464data6() {
		return b464data6;
	}

	public void setB464data6(String b464data6) {
		this.b464data6 = b464data6;
	}

	public String getB464data7() {
		return b464data7;
	}

	public void setB464data7(String b464data7) {
		this.b464data7 = b464data7;
	}

	public String getB465data1() {
		return b465data1;
	}

	public void setB465data1(String b465data1) {
		this.b465data1 = b465data1;
	}

	public String getB465data2() {
		return b465data2;
	}

	public void setB465data2(String b465data2) {
		this.b465data2 = b465data2;
	}

	public String getB465data3() {
		return b465data3;
	}

	public void setB465data3(String b465data3) {
		this.b465data3 = b465data3;
	}

	public List<LnkTryoutFile> getLnkTryoutFiles() {
		return this.lnkTryoutFiles;
	}

	public void setLnkTryoutFiles(List<LnkTryoutFile> lnkTryoutFiles) {
		this.lnkTryoutFiles = lnkTryoutFiles;
	}

	public LnkTryoutFile addLnkTryoutFile(LnkTryoutFile lnkTryoutFile) {
		getLnkTryoutFiles().add(lnkTryoutFile);
		lnkTryoutFile.setLnkRiskTryout(this);

		return lnkTryoutFile;
	}

	public LnkTryoutFile removeLnkTryoutFile(LnkTryoutFile lnkTryoutFile) {
		getLnkTryoutFiles().remove(lnkTryoutFile);
		lnkTryoutFile.setLnkRiskTryout(null);

		return lnkTryoutFile;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}
	
}