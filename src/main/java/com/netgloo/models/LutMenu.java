package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_MENU database table.
 * 
 */
@Entity
@Table(name="LUT_MENU")
@NamedQuery(name="LutMenu.findAll", query="SELECT l FROM LutMenu l")
public class LutMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private int isactive;

	private String menuname;

	private int orderid;
	
	private String parentid;

	private String stateurl;

	private String uicon;

	//bi-directional many-to-one association to LnkMenurole
	@OneToMany(mappedBy="lutMenu")
	@OrderBy("orderid")
	private List<LnkMenurole> lnkMenuroles;
		
	//bi-directional many-to-one association to LutMenu
	@ManyToOne(fetch = FetchType.LAZY)
	@OrderBy("orderid")
	@JoinColumn(name = "parentid",referencedColumnName="id",nullable = false,insertable=false,updatable=false)	
	private LutMenu lutMenu;

	//bi-directional many-to-one association to LutMenu
	@OneToMany(mappedBy="lutMenu")
	@OrderBy("orderid")
	private List<LutMenu> lutMenus;

	public LutMenu() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIsactive() {
		return this.isactive;
	}

	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}

	public String getMenuname() {
		return this.menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	
	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public int getOrderid() {
		return this.orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getStateurl() {
		return this.stateurl;
	}

	public void setStateurl(String stateurl) {
		this.stateurl = stateurl;
	}

	public String getUicon() {
		return this.uicon;
	}

	public void setUicon(String uicon) {
		this.uicon = uicon;
	}

	public LutMenu getLutMenu() {
		return this.lutMenu;
	}

	public void setLutMenu(LutMenu lutMenu) {
		this.lutMenu = lutMenu;
	}

	public List<LutMenu> getLutMenus() {
		return this.lutMenus;
	}

	public void setLutMenus(List<LutMenu> lutMenus) {
		this.lutMenus = lutMenus;
	}

	public LutMenu addLutMenus(LutMenu lutMenus) {
		getLutMenus().add(lutMenus);
		lutMenus.setLutMenu(this);

		return lutMenus;
	}

	public LutMenu removeLutMenus(LutMenu lutMenus) {
		getLutMenus().remove(lutMenus);
		lutMenus.setLutMenu(null);

		return lutMenus;
	}
	
	public List<LnkMenurole> getLnkMenuroles() {
		return this.lnkMenuroles;
	}

	public void setLnkMenuroles(List<LnkMenurole> lnkMenuroles) {
		this.lnkMenuroles = lnkMenuroles;
	}

	public LnkMenurole addLnkMenurole(LnkMenurole lnkMenurole) {
		getLnkMenuroles().add(lnkMenurole);
		lnkMenurole.setLutMenu(this);

		return lnkMenurole;
	}

	public LnkMenurole removeLnkMenurole(LnkMenurole lnkMenurole) {
		getLnkMenuroles().remove(lnkMenurole);
		lnkMenurole.setLutMenu(null);

		return lnkMenurole;
	}

}