package com.netgloo.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sql.DataSource;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.netgloo.models.FinAbw;
import com.netgloo.models.FinAsset;
import com.netgloo.models.FinBudget;
import com.netgloo.models.FinCbw;
import com.netgloo.models.FinCt1a;
import com.netgloo.models.FinCt2a;
import com.netgloo.models.FinCt3a;
import com.netgloo.models.FinCt4a;
import com.netgloo.models.FinCtt1;
import com.netgloo.models.FinCtt2;
import com.netgloo.models.FinCtt3;
import com.netgloo.models.FinCtt4;
import com.netgloo.models.FinCtt5;
import com.netgloo.models.FinCtt6;
import com.netgloo.models.FinCtt7;
import com.netgloo.models.FinCtt8;
import com.netgloo.models.FinCtt9;
import com.netgloo.models.FinFormB1;
import com.netgloo.models.FinFormB12;
import com.netgloo.models.FinFormB21;
import com.netgloo.models.FinInventory;
import com.netgloo.models.FinJournal;
import com.netgloo.models.FinNt2;
import com.netgloo.models.FinPayroll;
import com.netgloo.models.FinTgt1;
import com.netgloo.models.FinTgt1a;
import com.netgloo.models.FinTrialBalance;
import com.netgloo.models.FsForm241;
import com.netgloo.models.FsForm242;
import com.netgloo.models.FsForm243;
import com.netgloo.models.LnkAuditOrganization;
import com.netgloo.models.LutFormB1;
import com.netgloo.models.LutUser;
import com.netgloo.models.MainAuditRegistration;
import com.netgloo.models.StsCheckVariable;
@Repository
@Transactional
public class UserDaoImpl  extends JdbcDaoSupport implements UserDao {
	
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SessionFactory sessionFactory;	
	
	@Autowired 
	DataSource dataSource;
	
	@PostConstruct
	private void initialize(){
		setDataSource(dataSource);
	}
	
	public LutUser findByUserName(String username) {
		List<LutUser> userList = new ArrayList<LutUser>();
		Query query = sessionFactory.getCurrentSession().createQuery("from LutUser u where u.username = :username");
		query.setParameter("username", username);
		userList = query.list();
		if (userList.size() > 0)
			return userList.get(0);
		else
			return null;	
	}
	
	@Override
	public void inserBatch(final List<?> dt, String model) {

		if(model.equals("gLutFormB1")){		
			String sql = "INSERT INTO LUT_FORM_B1 " + "(PLANID,GENCODE,ORGCODE,STEPID,CYEAR,ORDERID,DATA1) VALUES (?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					LutFormB1 item = (LutFormB1) dt.get(i);
					ps.setLong(1, item.getPlanid());
					ps.setString(2, item.getGencode());
					ps.setString(3, item.getOrgcode());
					ps.setLong(4, item.getStepid());
					ps.setString(5, item.getCyear());
					ps.setLong(6, item.getOrderid());
					ps.setString(7, item.getData1());
					
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("FinFormB21")){		
			String sql = "INSERT INTO FIN_FORM_B2_1 " + "(ORGCODE,STEPID,CYEAR,PLANID,ORGCATID, FILELNKID,USERID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,PART) VALUES (?,?,?, ?,?,?,?, ?, ?,?, ?, ?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinFormB21 item = (FinFormB21) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setLong(6, item.getFilelnkid());
					ps.setLong(7, item.getUserid());
					ps.setString(8, item.getData1());
					ps.setString(9, item.getData2());
					ps.setString(10, item.getData3());
					ps.setString(11, item.getData4());
					ps.setString(12, item.getData5());
					ps.setString(13, item.getData6());
					ps.setLong(14, item.getPart());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("FinFormB12")){		
			String sql = "INSERT INTO FIN_FORM_B1_2 " + "(ID,ORGCODE,STEPID,CYEAR,PLANID,ORGCATID, FILELNKID,USERID,DATA1,DATA2) VALUES (FIN_FORM_B1_2_SEQ.nextval,?,?,?, ?, ?,?, ?, ?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinFormB12 item = (FinFormB12) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setLong(6, item.getFilelnkid());
					ps.setLong(7, item.getUserid());
					ps.setString(8, item.getData1());
					ps.setString(9, item.getData2());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("FinFormB1")){		
			String sql = "INSERT INTO FIN_FORM_B1 " + "(iD,ORGCODE,STEPID,CYEAR,PLANID,ORGCATID, FILELNKID,USERID,DATA1,DATA2,DATA3,DATA4,DATA5) VALUES (FIN_FORM_B1_SEQ.nextval,?, ?,?, ?, ?,?, ?, ?,?, ?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinFormB1 item = (FinFormB1) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setLong(6, item.getFilelnkid());
					ps.setLong(7, item.getUserid());
					ps.setString(8, item.getData1());
					ps.setString(9, item.getData2());
					ps.setString(10, item.getData3());
					ps.setString(11, item.getData4());
					ps.setString(12, item.getData5());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("11.CTT6")){		
			String sql = "INSERT INTO FIN_CTT6 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_CTT6_SEQ.nextval,?, ?,?, ?, ?,?, ?, ?,?, ?, ?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt6 item = (FinCtt6) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("23.TRIAL BALANCE")){
			String sql = "INSERT INTO FIN_TRIAL_BALANCE " + "(ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinTrialBalance item = (FinTrialBalance) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("25.CBWS")){
			String sql = "INSERT INTO FIN_CBWS " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8) VALUES (FIN_CBWS_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCbw item = (FinCbw) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("24.ABWS")){
			String sql = "INSERT INTO FIN_ABWS " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_ABWS_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinAbw item = (FinAbw) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("21.TGT1A")){
			String sql = "INSERT INTO FIN_TGT1A " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_TGT1A_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinTgt1a item = (FinTgt1a) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("6.Payroll")){
			String sql = "INSERT INTO FIN_PAYROLL " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15,DATA16) VALUES (FIN_PAYROLL_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinPayroll item = (FinPayroll) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
					ps.setString(21, item.getData16());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("5.Inventory")){
			String sql = "INSERT INTO FIN_INVENTORY " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12) VALUES (FIN_INVENTORY_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinInventory item = (FinInventory) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("4.Assets")){
			String sql = "INSERT INTO FIN_ASSETS " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15,DATA16,DATA17,DATA18"
					+ ",DATA19,DATA20,DATA21,DATA22,DATA23,DATA24,DATA25,DATA26,DATA27,DATA28,DATA29,DATA30,DATA31,DATA32,DATA33,DATA34,DATA35,DATA36,DATA37,DATA38,DATA39) VALUES (FIN_ASSETS_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinAsset item = (FinAsset) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
					ps.setString(21, item.getData16());
					ps.setString(22, item.getData17());
					ps.setString(23, item.getData18());
					ps.setString(24, item.getData19());
					ps.setString(25, item.getData20());
					ps.setString(26, item.getData21());
					ps.setString(27, item.getData22());
					ps.setString(28, item.getData23());
					ps.setString(29, item.getData24());
					ps.setString(30, item.getData25());
					ps.setString(31, item.getData26());
					ps.setString(32, item.getData27());
					ps.setString(33, item.getData28());
					ps.setString(34, item.getData29());
					ps.setString(35, item.getData30());
					ps.setString(36, item.getData31());
					ps.setString(37, item.getData32());
					ps.setString(38, item.getData33());
					ps.setString(39, item.getData34());
					ps.setString(40, item.getData35());
					ps.setString(41, item.getData36());
					ps.setString(42, item.getData37());
					ps.setString(43, item.getData38());
					ps.setString(44, item.getData39());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("3.Journal")){
			String sql = "INSERT INTO FIN_JOURNAL " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15,DATA16,DATA17,DATA18"
					+ ",DATA19,DATA20,DATA21) VALUES (FIN_JOURNAL_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinJournal item = (FinJournal) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
					ps.setString(21, item.getData16());
					ps.setString(22, item.getData17());
					ps.setString(23, item.getData18());
					ps.setString(24, item.getData19());
					ps.setString(25, item.getData20());
					ps.setString(26, item.getData21());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("19.Budget")){
			String sql = "INSERT INTO FIN_BUDGET " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15,DATA16,DATA17) VALUES (FIN_BUDGET_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinBudget item = (FinBudget) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
					ps.setString(21, item.getData16());
					ps.setString(22, item.getData17());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("13.CTT8")){
			String sql = "INSERT INTO FIN_CTT8 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_CTT8_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt8 item = (FinCtt8) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("14.CTT9")){
			String sql = "INSERT INTO FIN_CTT9 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_CTT9_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt9 item = (FinCtt9) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("12.CTT7")){
			String sql = "INSERT INTO FIN_CTT7 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15,DATA16) VALUES (FIN_CTT7_SEQ.nextval,?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt7 item = (FinCtt7) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
					ps.setString(21, item.getData16());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("7.CTT2")){
			String sql = "INSERT INTO FIN_CTT2 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_CTT2_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt2 item = (FinCtt2) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
				}

				@Override
				public int getBatchSize() {
					
					return dt.size();
				}
			});
		}
		else if(model.equals("10.CTT5")){
			String sql = "INSERT INTO FIN_CTT5 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15,DATA16,DATA17) VALUES (FIN_CTT5_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt5 item = (FinCtt5) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
					ps.setString(21, item.getData16());
					ps.setString(22, item.getData17());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("9.CTT4")){
			String sql = "INSERT INTO FIN_CTT4 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_CTT4_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt4 item = (FinCtt4) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("8.CTT3")){
			String sql = "INSERT INTO FIN_CTT3 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15,DATA16,DATA17) VALUES (FIN_CTT3_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt3 item = (FinCtt3) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
					ps.setString(21, item.getData16());
					ps.setString(22, item.getData17());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("6.CTT1")){
			String sql = "INSERT INTO FIN_CTT1 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,DATA11,DATA12,DATA13,DATA14,DATA15) VALUES (FIN_CTT1_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCtt1 item = (FinCtt1) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
					ps.setString(13, item.getData8());
					ps.setString(14, item.getData9());
					ps.setString(15, item.getData10());
					ps.setString(16, item.getData11());
					ps.setString(17, item.getData12());
					ps.setString(18, item.getData13());
					ps.setString(19, item.getData14());
					ps.setString(20, item.getData15());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("22.NT2")){
			String sql = "INSERT INTO FIN_NT2 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4) VALUES (FIN_NT2_SEQ.nextval,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinNt2 item = (FinNt2) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("20.TGT1")){
			String sql = "INSERT INTO FIN_TGT1 " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6) VALUES (FIN_TGT1_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinTgt1 item = (FinTgt1) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("5.CT4A")){
			String sql = "INSERT INTO FIN_CT4A " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7) VALUES (FIN_ST4A_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCt4a item = (FinCt4a) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
					ps.setString(10, item.getData5());
					ps.setString(11, item.getData6());
					ps.setString(12, item.getData7());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("4.CT3A")){
			String sql = "INSERT INTO FIN_CT3A " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4) VALUES (FIN_ST3A_SEQ.nextval,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCt3a item = (FinCt3a) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("st2a")){
			System.out.println("2a");
			String sql = "INSERT INTO FIN_CT2A " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4) VALUES (FIN_ST2A_SEQ.nextval,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCt2a item = (FinCt2a) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("2.CT1A")){
			String sql = "INSERT INTO FIN_CT1A " + "(ID,ORGCODE, STEPID,CYEAR,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4) VALUES (FIN_ST1A_SEQ.nextval,?,?,?,?,?,?,?,?,?)";
			
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FinCt1a item = (FinCt1a) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setString(3, item.getCyear());
					ps.setLong(4, item.getPlanid());
					ps.setLong(5, item.getOrgcatid());
					ps.setString(6, item.getData1());
					ps.setString(7, item.getData2());
					ps.setString(8, item.getData3());
					ps.setString(9, item.getData4());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("tulgalt")){
			String sql = "INSERT INTO STS_CHECK_VARIABLES " + "(ID,ORGCODE, STEPID,PLANID,ORGCATID,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10,VALID) VALUES (STS_CHECK_VARIABLES_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					StsCheckVariable item = (StsCheckVariable) dt.get(i);
					ps.setString(1, item.getOrgcode());
					ps.setLong(2, item.getStepid());
					ps.setLong(3, item.getPlanid());
					ps.setLong(4, item.getOrgcatid());
					ps.setString(5, item.getData1());
					ps.setString(6, item.getData2());
					ps.setLong(7, item.getData3());
					ps.setString(8, item.getData4());
					ps.setString(9, item.getData5());
					ps.setLong(10, item.getData6());
					ps.setString(11, item.getData7());
					ps.setString(12, item.getData8());
					ps.setLong(13, item.getData9());
					ps.setLong(14, item.getData10());
					ps.setLong(15, item.getValid());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("quata")){
			String sql = "INSERT INTO LNK_AUDIT_ORGANIZATION " + "(ayear, orgcode,orgname,orgtype,reasonid,quataid,preid,auditorgid,distribution) VALUES (?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					 LnkAuditOrganization lk = (LnkAuditOrganization) dt.get(i);
					 ps.setInt(1, lk.getAyear());
					 ps.setString(2, lk.getOrgcode());
					 ps.setString(3, lk.getOrgname());
					 ps.setInt(4, lk.getOrgtype());
					 ps.setLong(5, 1);
					 ps.setLong(6, lk.getQuataid());
					 ps.setLong(7, lk.getPreid());
					 ps.setLong(8, lk.getAuditorgid());
					 ps.setLong(9, 0);		
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("upquata")){
			String sql = "INSERT INTO MAIN_AUDIT_REGISTRATION " + "(orgname, orgcode,startdate,enddate,audityear,orgtype,autype,stepid,depid,gencode,isactive,orgid,isenabled) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					MainAuditRegistration lk = (MainAuditRegistration) dt.get(i);
					 					 
					 ps.setString(1, lk.getOrgname());
					 ps.setString(2, lk.getOrgcode());
					 ps.setString(3, lk.getStartdate());
					 ps.setString(4, lk.getEnddate());
					 ps.setString(5, lk.getAudityear());
					 ps.setLong(6, lk.getOrgtype());
					 ps.setLong(7, lk.getAutype());
					 ps.setLong(8, lk.getStepid());
					 ps.setLong(9, lk.getDepid());			
					 ps.setString(10, lk.getGencode());						
					 ps.setLong(11, 0);	
					 ps.setLong(12, lk.getOrgid());	
					 ps.setLong(13, 1);	
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("fs241")){
			String sql = "INSERT INTO FS_FORM_241 " + "(ACC_CODE, ACC_NAME,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,PLANID, ORDERID) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FsForm241 lk = (FsForm241) dt.get(i);				 
					 ps.setString(1, lk.getAccCode());
					 ps.setString(2, lk.getAccName());
					 ps.setString(3, String.valueOf(lk.getData1()));
					 ps.setString(4, String.valueOf(lk.getData2()));
					 ps.setString(5, String.valueOf(lk.getData3()));
					 ps.setString(6, String.valueOf(lk.getData4()));
					 ps.setString(7, String.valueOf(lk.getData5()));
					 ps.setString(8, String.valueOf(lk.getData6()));
					 ps.setString(9, String.valueOf(lk.getData7()));
					 ps.setLong(10, lk.getPlanid());
					 ps.setLong(11, lk.getOrderid());
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		else if(model.equals("fs242")){
			String sql = "INSERT INTO FS_FORM_242 " + "(ACC_CODE, ACC_NAME,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,PLANID, ORDERID, DATA8, DATA9, DATA10) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FsForm242 lk = (FsForm242) dt.get(i);			 
					 ps.setString(1, lk.getAccCode());
					 ps.setString(2, lk.getAccName());
					 ps.setString(3, String.valueOf(lk.getData1()));
					 ps.setString(4, String.valueOf(lk.getData2()));
					 ps.setString(5, String.valueOf(lk.getData3()));
					 ps.setString(6, String.valueOf(lk.getData4()));
					 ps.setString(7, String.valueOf(lk.getData5()));
					 ps.setString(8, String.valueOf(lk.getData6()));
					 ps.setString(9, String.valueOf(lk.getData7()));
					 ps.setLong(10, lk.getPlanid());
					 ps.setLong(11, lk.getOrderid());
					 ps.setString(12, String.valueOf(lk.getData8()));
					 ps.setString(13, String.valueOf(lk.getData9()));
					 ps.setString(14, String.valueOf(lk.getData10()));
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
		
		else if(model.equals("fs243")){
			String sql = "INSERT INTO FS_FORM_243 " + "(ACC_CODE, ACC_NAME,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,PLANID, ORDERID, DATA8, DATA9, DATA10) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException,NullPointerException {
					FsForm243 lk = (FsForm243) dt.get(i);			 
					 ps.setString(1, lk.getAccCode());
					 ps.setString(2, lk.getAccName());
					 ps.setString(3, String.valueOf(lk.getData1()));
					 ps.setString(4, String.valueOf(lk.getData2()));
					 ps.setString(5, String.valueOf(lk.getData3()));
					 ps.setString(6, String.valueOf(lk.getData4()));
					 ps.setString(7, String.valueOf(lk.getData5()));
					 ps.setString(8, String.valueOf(lk.getData6()));
					 ps.setString(9, String.valueOf(lk.getData7()));
					 ps.setLong(10, lk.getPlanid());
					 ps.setLong(11, lk.getOrderid());
					 ps.setString(12, String.valueOf(lk.getData8()));
					 ps.setString(13, String.valueOf(lk.getData9()));
					 ps.setString(14, String.valueOf(lk.getData10()));
				}

				@Override
				public int getBatchSize() {
					return dt.size();
				}
			});
		}
	}


	@Override
	public Object saveOrUpdate(Object obj) {
		try{			
			sessionFactory.getCurrentSession().update(obj);
			return true;
		}
		catch (ConstraintViolationException err){
			err.printStackTrace();			
			return false;
		}
		
	}
	
	@Override
	public Object findAll(String domain, String whereclause) {
		Query query=null;
		if(whereclause!=null){			
			query = sessionFactory.getCurrentSession().createQuery(whereclause);			
		}
		else{
			query = sessionFactory.getCurrentSession().createQuery("from "+domain+" objlist  order by objlist.id desc ");
		}
		
		List<Object> robj = query.list();
		query = null;
		return robj;
		
	}
	
	@Override
	public Object findById(String domain,long id, String whereclause) {
		Query query=null;
		query=sessionFactory.getCurrentSession().createQuery("from "+domain+" t where t.id=:id");
		query.setParameter("id", id);
		Object robj = query.list().get(0);		
		return robj;
		
	}
	
	@Override
	public void deleteById(String domain,long obj_id, String whereclause) {
		Query query=null;
		if(whereclause!=null){			
			query= sessionFactory.getCurrentSession().createQuery("delete from "+domain+"  t where t."+whereclause+"=:obj_id");
			query.setParameter("obj_id", obj_id);
			
		}
		else{
			query= sessionFactory.getCurrentSession().createQuery("delete from "+domain+"  t where t.id=:obj_id");
    		query.setParameter("obj_id", obj_id);
		}
		//List list = query.list();
		int qresult = query.executeUpdate();
		//return qresult;
		
	}
		
	
	@Override
	public Object getHQLResult(String hqlString,String returnType){
		Query query = sessionFactory.getCurrentSession().createQuery(hqlString);
		System.out.println(query);
		
		if("list".equals(returnType)){
			return  query.list();
		}
		else if("current".equals(returnType)){
			if(query.list().size()>0){
				if(query.list().size()==1){
					return query.list().get(0);
				}
				else {
					return null;
				}
			}
			
		}
		else if("count".equals(returnType)){
			int resultInt =  ((Long)query.uniqueResult()).intValue();
			return resultInt;
		}
		
		else {
			return null;
		}
		
		return null;
	}

	public List<Object> kendojson(String request,String tablename){

		try{			
			
		//	Gson gson = new Gson ();	    
			int skip=0;
			int take=2;
			int page=0;			
			String field="";
			String order="";
			String dir="";
			JSONArray sort=null;
			String group="";
			JSONObject filter=null;
			String org="";
			String custom="";
			String nat="";
			String flquery="";
			String isspecial="";
			System.out.println("sss"+request+tablename);
			JSONObject req= new JSONObject(request);				
			skip=req.getInt("skip");
			
			page=req.getInt("page");
			if(req.has("sort")){
				sort=req.getJSONArray("sort");
			}
			if(req.has("take")){
				take=req.getInt("take");
			}
			if(req.has("group")){
				group=req.getString("group");
			}
			if(req.has("filter")){
				
				if(!req.isNull("filter")){
					filter=req.getJSONObject("filter");	
				}
						
			}
		
			if(req.has("custom")){
				custom=req.getString("custom");
			}
			if(req.has("field")){
				field=req.getString("field");
			}
			if(req.has("dir")){
				dir=req.getString("dir");
			}
			if(custom.length()>0){
				flquery=custom;
			}
			if(req.has("native")){
				nat=req.getString("native");
			}
			if(req.has("isspecial")){
				isspecial=req.getString("isspecial");
			}
			
			String multiOrde="";
			
			if(sort!=null){
				JSONArray arr= sort;
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						field="";	
					}
					else{
						field=srt.getString("field");
						multiOrde=multiOrde+ " "+ field;
						
					}
					if(srt.isNull("dir")){
						dir="";
					}
					else{
						dir=srt.getString("dir");
						multiOrde=multiOrde + " " +dir + ",";
					}
				}
				
			}
			if(multiOrde.length()>0){
				System.out.println("$$$$ "+multiOrde.substring(0, multiOrde.length()-1));
			}
			
			
			String groupfield="";
			String groupdir="";
			if(group.length()>0){
				JSONArray arr= new JSONArray(group);
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						groupfield="";	
					}
					else{
						groupfield=srt.getString("field");	
					}
					if(srt.isNull("dir")){
						groupdir="";
					}
					else{
						groupdir=srt.getString("dir");
					}
				}
				
			}
			String filterfield="";
			String operator="";
			String value="";
			
			if(filter!=null){
				
				JSONObject fltr= filter;		
				
				String logic=fltr.getString("logic");
				
				JSONArray arr= fltr.getJSONArray("filters");
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						filterfield="";	
					}
					else{
						filterfield=srt.getString("field");	
					}
					if(srt.isNull("operator")){
						operator="";
					}
					else{
						operator=srt.getString("operator");
					}
					if(srt.isNull("value")){
						value="";
					}
					else{
						value=String.valueOf(srt.get("value")).toLowerCase();
					}
					if(i>0){
						
						switch(operator){
							case "startswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '"+value+"%'"; break;
							case "contains":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
							case "doesnotcontain":flquery=flquery+  " "+logic+" lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
							case "endswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"'"; break;
							case "neq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") != '"+value+"'"; break;
							case "eq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") = '"+value+"'"; break;
						}						
					}
					else{
						if(custom.length()>0){
							System.out.println("enduu");
							if(custom.contains("orgid1")){
								
								System.out.println("enduu 111");
								switch(operator){
									case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
								}								
							}
							else{
								
								System.out.println("enduu 222 "); 
								switch(operator){
									case "startswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" "+custom+" and lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" "+custom+" and lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" "+custom+" and lower("+filterfield+ ") = '"+value+"'"; break;
								}
							}							
						}
						else{
							switch(operator){
								case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
								case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
								case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
								case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
								case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
								case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
							}
						}
						
					}
					
				}
			
			}
			
			if(groupfield.isEmpty()){
				group="";
			}
			else{
				group="group by "+groupfield+" "+groupdir+"";
			}
			
			if(field.isEmpty()){
				order="order by t.id desc";
			}
			else{
				order="order by "+multiOrde.substring(0,multiOrde.length()-1)+"";
			}
					
			
			String query="from "+tablename+" t  "+flquery+"  "+group+" "+order+"";
			
			System.out.println("query " +query);
			
			if(isspecial.isEmpty()){
				Query hql = sessionFactory.getCurrentSession().createQuery(query);
				hql.setFirstResult(skip);
				hql.setMaxResults(take);
				List<Object> rlist = hql.list();
				  //List list = hql.list();
				//sessionFactory.getCurrentSession().flush();
				
				return rlist;
			}
			else {
				Query  nquery= sessionFactory.getCurrentSession().createSQLQuery(isspecial);
				List<Object> nlist=nquery.list();
				
				return nlist;
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public Object getNativeSQLResult(String queryStr, String type){
				
		try{
			if("insert".equals(type)){
				Query query=sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				int numberOfRowsAffected = query.executeUpdate();
				try{
					return true;
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			else if("list".equals(type)){
				System.out.println("qqq"+queryStr);
				Query query=sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				try{
					return query.list();
				}
				catch(Exception e){
					return null;
				}
				
			}
			else if("delete".equals(type)){
				Query query=sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				int numberOfRowsAffected = query.executeUpdate();
				try{
					return true;
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			else {
				return null;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public Object PeaceCrud(Object obj, String domainname, String method, Long obj_id, int page_val, int maxresult,
			String whereclause) {
		try{
    		Query query=null;
    		if("save".equals(method)){
    			try{
    				sessionFactory.getCurrentSession().saveOrUpdate(obj);
    				System.out.println(query);
    				return true;
    			}
    			catch (ConstraintViolationException aldaa){
    				aldaa.printStackTrace();
    				
    				return false;
    			}
    			
        	}
    		else if("update".equals(method)){
    			try{
    				
    				sessionFactory.getCurrentSession().update(obj);
    				System.out.println(query);
    				//sessionFactory.getCurrentSession().flush();
					//session.getCurrentSession().clear();
					
					
    				return true;
    			}
    			catch (ConstraintViolationException aldaa){
    				System.out.println("ooou laitai");
    				aldaa.printStackTrace();
    				
    				return false;
    			}
    		}
        	else if ("delete".equals(method)){
        		
    			if(whereclause!=null){
        			
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname+"  dname where dname."+whereclause+"=:obj_id");
        			query.setParameter("obj_id", obj_id);
        			System.out.println(query);
        		}
        		else{
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname+"  dname where dname.id=:obj_id");
            		query.setParameter("obj_id", obj_id);
            		System.out.println(query);
        		}
    			//List list = query.list();
        		int qresult = query.executeUpdate();
        	}
        	else if ("multidelete".equals(method)){
        		
    			if(whereclause!=null){        			
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname + " " + whereclause);
        		}
        		else{
        			query= sessionFactory.getCurrentSession().createQuery("delete from "+domainname+"  dname where dname.id=:obj_id");
            		query.setParameter("obj_id", obj_id);
        		}
        		int qresult = query.executeUpdate();
        	}
        	else if("list".equals(method)){
        		if(whereclause!=null){
        			
        			query = sessionFactory.getCurrentSession().createQuery(whereclause);
        			System.out.println(query);
        		}
        		else{
        			query = sessionFactory.getCurrentSession().createQuery("from "+domainname+" objlist  order by objlist.id desc ");
        			System.out.println(query);
        		}
        		int pval = page_val-1;
        		query.setFirstResult(maxresult*pval);
        		query.setMaxResults(maxresult);
        		
        		List<Object> robj = query.list();
        		query = null;
        	//	sessionFactory.getCurrentSession().flush();
        		//session.getCurrentSession().clear();
        		return robj;
        	}
        	else if("current".equals(method)){
        		query=sessionFactory.getCurrentSession().createQuery("from "+domainname+" t where t.id=:obj_id");
        		query.setParameter("obj_id", obj_id);
        		Object robj = query.list().get(0);
        		System.out.println(query);
        		return robj;
        		
        	}
        	else if("calculatepage".equals(method)){
        		if(whereclause==null){
        			int resultInt =  ((Long)sessionFactory.getCurrentSession().createQuery("select count(*) from "+domainname+"").uniqueResult()).intValue();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		
            		sessionFactory.getCurrentSession().flush();
            		if(resultInt%maxresult==0){
            			return resultInt/maxresult;
            		}
            		else{
            			return resultInt/maxresult+1;
            		}
        		}
        		else{
        			int resultInt =  sessionFactory.getCurrentSession().createQuery(whereclause).list().size();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		query=null;
            		sessionFactory.getCurrentSession().flush();
            		if(resultInt%maxresult==0){
            			return resultInt/maxresult;
            		}
            		else{
            			return resultInt/maxresult+1;
            		}
        		}	
        	}
        	else if ("countrecord".equalsIgnoreCase(method)){
        		if(whereclause==null){
        			long resultInt =  ((Long)sessionFactory.getCurrentSession().createQuery("select count(*) from "+domainname+"").uniqueResult()).intValue();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		query=null;
            		sessionFactory.getCurrentSession().flush();
            		return resultInt;
        		}
        		else{
        			long resultInt =  ((Long) sessionFactory.getCurrentSession().createQuery(whereclause).uniqueResult()).intValue();
            		System.out.println("ene bol niit bichlegiin too "+resultInt);
            		
            		sessionFactory.getCurrentSession().flush();
            		//session.getCurrentSession().clear();
            		return resultInt;
        		}
        	}
    		
    		return 1;		
    	}
    	catch (Exception e){
    		e.printStackTrace();
    		return 0;
    	}
	}


	@Override
	public int resulsetcount(String request,String tablename) {
		try{			
		//	Gson gson = new Gson ();	    
			
			String field="";
			String order="";
			String dir="";
			String sort="";
			String group="";
			JSONObject filter=null;
			String org="";
			String custom="";
			String isspecial="";
			System.out.println("req "+request);
			System.out.println("req "+tablename);
			JSONObject req= new JSONObject(request);				
			
			if(req.has("filter")){
				if(!req.isNull("filter")){
					filter=req.getJSONObject("filter");	
				}
			}
		
			if(req.has("custom")){
				custom=req.getString("custom");
			}
			System.out.println("req "+request);
			System.out.println("group "+group);
			System.out.println("sort "+sort);
			System.out.println("filter "+filter);
				
			String filterfield="";
			String operator="";
			String value="";
			String flquery="";
			if(custom.length()>0){
				flquery=custom;
			}
			if(req.has("isspecial")){
				isspecial=req.getString("isspecial");
			}
			if(filter!=null){
				
				JSONObject fltr= filter;		
				
				String logic=fltr.getString("logic");
				//String filters=fltr.getString("filters");
			
				JSONArray arr= fltr.getJSONArray("filters");
				for(int i=0; i<arr.length();i++){
					String str=arr.get(i).toString();
					JSONObject srt= new JSONObject(str);
					if(srt.isNull("field")){
						filterfield="";	
					}
					else{
						filterfield=srt.getString("field");	
					}
					if(srt.isNull("operator")){
						operator="";
					}
					else{
						operator=srt.getString("operator");
					}
					if(srt.isNull("value")){
						value="";
					}
					else{						
						value=String.valueOf(srt.get("value")).toLowerCase();
					}
					if(i>0){
						
						switch(operator){
							case "startswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '"+value+"%'"; break;
							case "contains":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
							case "doesnotcontain":flquery=flquery+  " "+logic+" lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
							case "endswith":flquery=flquery+  " "+logic+" lower("+filterfield+ ") LIKE '%"+value+"'"; break;
							case "neq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") != '"+value+"'"; break;
							case "eq":flquery=flquery+  " "+logic+" lower("+filterfield+ ") = '"+value+"'"; break;
						}						
					}
					else{
						if(custom.length()>0){
							if(custom.contains("orgid")){
								switch(operator){
									case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
								}								
							}
							else{
								switch(operator){
									case "startswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '"+value+"%'"; break;
									case "contains":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
									case "doesnotcontain":flquery=" "+custom+" and lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
									case "endswith":flquery=" "+custom+" and lower("+filterfield+ ") LIKE '%"+value+"'"; break;
									case "neq":flquery=" "+custom+" and lower("+filterfield+ ") != '"+value+"'"; break;
									case "eq":flquery=" "+custom+" and lower("+filterfield+ ") = '"+value+"'"; break;
								}
							}							
						}
						else{
							switch(operator){
								case "startswith":flquery=" Where lower("+filterfield+ ") LIKE '"+value+"%'"; break;
								case "contains":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"%'"; break;
								case "doesnotcontain":flquery=" Where lower("+filterfield+ ") NOT LIKE '%"+value+"%'"; break;
								case "endswith":flquery=" Where lower("+filterfield+ ") LIKE '%"+value+"'"; break;
								case "neq":flquery=" Where lower("+filterfield+ ") != '"+value+"'"; break;
								case "eq":flquery=" Where lower("+filterfield+ ") = '"+value+"'"; break;
							}
						}
						
						
						
						System.out.println("end bn "+flquery);
					}
					
				}
				
			}

			
			String query="select count(*) from "+tablename+" t "+flquery+" ";
			
			System.out.println("query" +query);

			System.out.println("query " +query);
			if(isspecial.isEmpty()){
				Query hql = sessionFactory.getCurrentSession().createQuery(query);
		    	int count = Integer.parseInt(hql.list().get(0).toString());			
		   // 	sessionFactory.getCurrentSession().flush();
				return count;
			}
			else {
				Query  nquery= sessionFactory.getCurrentSession().createSQLQuery(isspecial);
				List<Object> nlist=nquery.list();
				
				return nlist.size();
			}			
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<?> jData(Integer pageSize, Integer skip, String sortColumn, String sortColumnDir, String searchStr,
			String domain) {
		
	
		
		String order="order by "+sortColumn+" "+sortColumnDir+"";
		String query="";
		if(domain==null){
			query=searchStr;
		}
		else{
			query="from "+domain+"  "+searchStr+"  "+order+"";
		}
		
		Query hql = sessionFactory.getCurrentSession().createQuery(query);
		hql.setFirstResult(skip);
		hql.setMaxResults(pageSize);
		List<Object> rlist = hql.list();
		
		return rlist;
	}
	  
}