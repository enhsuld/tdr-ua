package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.netgloo.models.LnkTryoutConfMethod;


/**
 * The persistent class for the LUT_CONFIRMATION_METHOD database table.
 * 
 */
@Entity
@Table(name="LUT_CONFIRMATION_METHOD")
@NamedQuery(name="LutConfirmationMethod.findAll", query="SELECT l FROM LutConfirmationMethod l")
public class LutConfirmationMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_CONFIRMATION_METHOD_SEQ", sequenceName="LUT_CONFIRMATION_METHOD_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_CONFIRMATION_METHOD_SEQ")
	private long id;

	private String name;

	//bi-directional many-to-one association to LnkTryoutConfMethod
		@OneToMany(mappedBy="lutConfirmationMethod")
		private List<LnkTryoutConfMethod> lnkTryoutConfMethods;

		public LutConfirmationMethod() {
		}

		public long getId() {
			return this.id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<LnkTryoutConfMethod> getLnkTryoutConfMethods() {
			return this.lnkTryoutConfMethods;
		}

		public void setLnkTryoutConfMethods(List<LnkTryoutConfMethod> lnkTryoutConfMethods) {
			this.lnkTryoutConfMethods = lnkTryoutConfMethods;
		}

		public LnkTryoutConfMethod addLnkTryoutConfMethod(LnkTryoutConfMethod lnkTryoutConfMethod) {
			getLnkTryoutConfMethods().add(lnkTryoutConfMethod);
			lnkTryoutConfMethod.setLutConfirmationMethod(this);

			return lnkTryoutConfMethod;
		}

		public LnkTryoutConfMethod removeLnkTryoutConfMethod(LnkTryoutConfMethod lnkTryoutConfMethod) {
			getLnkTryoutConfMethods().remove(lnkTryoutConfMethod);
			lnkTryoutConfMethod.setLutConfirmationMethod(null);

			return lnkTryoutConfMethod;
		}

}