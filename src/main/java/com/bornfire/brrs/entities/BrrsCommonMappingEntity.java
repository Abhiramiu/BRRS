package com.bornfire.brrs.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_COMMON_MAPPING_TABLE")
public class BrrsCommonMappingEntity {

    @Id
    private Long SNO;

    private String GL_CODE;
    private String GL_SUB_CODE;
    private String ACCOUNT_NO;
    private String CURRENCY;
    private String M_PI;
    private String M_SFINP1;
    private String M_SFINP2;
    private String M_LIQ;
    private String M_SCI_E;
    private String M_IS;
    private String M_CA1;
    private String M_CA2;
    private String M_SRWA_12A;
    private String M_OR1;
    private String M_MRC;
    private String M_IRR;
    private String M_IRB;
    private String M_GALOR;
    private String M_CALOC;
    private String M_I_S_CA;
    private String M_SP;
    private String M_DEP1;
    private String Q_SMME_INT_INCOME;
    private String M_LIQGAP;
    private String M_LA1;
    private String M_LA3;
    private String M_LA4;
    private String M_LA5;
    private String M_PLL;
    private String M_PD;
    private String M_GP;
    private String M_DEP2;
    private String M_FAS;
    private String Q_ATF;
    private String Q_SMME_LA;

    private String ENTITY_FLG;
    private String VERIFY_FLG;
    private String MODIFY_FLG;
    private String DEL_FLG;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ENTRY_TIME;

    @Temporal(TemporalType.TIMESTAMP)
    private Date MODIFY_TIME;

    @Temporal(TemporalType.TIMESTAMP)
    private Date VERIFY_TIME;
    
    
    // Getters and Setters
    public Long getSNO() {
        return SNO;
    }

    public void setSNO(Long sNO) {
        SNO = sNO;
    }

    public String getGL_CODE() {
        return GL_CODE;
    }

    public void setGL_CODE(String gL_CODE) {
        GL_CODE = gL_CODE;
    }

    public String getGL_SUB_CODE() {
        return GL_SUB_CODE;
    }

    public void setGL_SUB_CODE(String gL_SUB_CODE) {
        GL_SUB_CODE = gL_SUB_CODE;
    }

    public String getACCOUNT_NO() {
        return ACCOUNT_NO;
    }

    public void setACCOUNT_NO(String aCCOUNT_NO) {
        ACCOUNT_NO = aCCOUNT_NO;
    }

    public String getCURRENCY() {
        return CURRENCY;
    }

    public void setCURRENCY(String cURRENCY) {
        CURRENCY = cURRENCY;
    }

    public String getM_PI() {
        return M_PI;
    }

    public void setM_PI(String m_PI) {
        M_PI = m_PI;
    }

    public String getM_SFINP1() {
        return M_SFINP1;
    }

    public void setM_SFINP1(String m_SFINP1) {
        M_SFINP1 = m_SFINP1;
    }

    public String getM_SFINP2() {
        return M_SFINP2;
    }

    public void setM_SFINP2(String m_SFINP2) {
        M_SFINP2 = m_SFINP2;
    }

    public String getM_LIQ() {
        return M_LIQ;
    }

    public void setM_LIQ(String m_LIQ) {
        M_LIQ = m_LIQ;
    }

    public String getM_SCI_E() {
        return M_SCI_E;
    }

    public void setM_SCI_E(String m_SCI_E) {
        M_SCI_E = m_SCI_E;
    }

    public String getM_IS() {
        return M_IS;
    }

    public void setM_IS(String m_IS) {
        M_IS = m_IS;
    }

    public String getM_CA1() {
        return M_CA1;
    }

    public void setM_CA1(String m_CA1) {
        M_CA1 = m_CA1;
    }

    public String getM_CA2() {
        return M_CA2;
    }

    public void setM_CA2(String m_CA2) {
        M_CA2 = m_CA2;
    }

    public String getM_SRWA_12A() {
        return M_SRWA_12A;
    }

    public void setM_SRWA_12A(String m_SRWA_12A) {
        M_SRWA_12A = m_SRWA_12A;
    }

    public String getM_OR1() {
        return M_OR1;
    }

    public void setM_OR1(String m_OR1) {
        M_OR1 = m_OR1;
    }

    public String getM_MRC() {
        return M_MRC;
    }

    public void setM_MRC(String m_MRC) {
        M_MRC = m_MRC;
    }

    public String getM_IRR() {
        return M_IRR;
    }

    public void setM_IRR(String m_IRR) {
        M_IRR = m_IRR;
    }

    public String getM_IRB() {
        return M_IRB;
    }

    public void setM_IRB(String m_IRB) {
        M_IRB = m_IRB;
    }

    public String getM_GALOR() {
        return M_GALOR;
    }

    public void setM_GALOR(String m_GALOR) {
        M_GALOR = m_GALOR;
    }

    public String getM_CALOC() {
        return M_CALOC;
    }

    public void setM_CALOC(String m_CALOC) {
        M_CALOC = m_CALOC;
    }

    public String getM_I_S_CA() {
        return M_I_S_CA;
    }

    public void setM_I_S_CA(String m_I_S_CA) {
        M_I_S_CA = m_I_S_CA;
    }

    public String getM_SP() {
        return M_SP;
    }

    public void setM_SP(String m_SP) {
        M_SP = m_SP;
    }

    public String getM_DEP1() {
        return M_DEP1;
    }

    public void setM_DEP1(String m_DEP1) {
        M_DEP1 = m_DEP1;
    }

    public String getQ_SMME_INT_INCOME() {
        return Q_SMME_INT_INCOME;
    }

    public void setQ_SMME_INT_INCOME(String q_SMME_INT_INCOME) {
        Q_SMME_INT_INCOME = q_SMME_INT_INCOME;
    }

    public String getM_LIQGAP() {
        return M_LIQGAP;
    }

    public void setM_LIQGAP(String m_LIQGAP) {
        M_LIQGAP = m_LIQGAP;
    }

    public String getM_LA1() {
        return M_LA1;
    }

    public void setM_LA1(String m_LA1) {
        M_LA1 = m_LA1;
    }

    public String getM_LA3() {
        return M_LA3;
    }

    public void setM_LA3(String m_LA3) {
        M_LA3 = m_LA3;
    }

    public String getM_LA4() {
        return M_LA4;
    }

    public void setM_LA4(String m_LA4) {
        M_LA4 = m_LA4;
    }

    public String getM_LA5() {
        return M_LA5;
    }

    public void setM_LA5(String m_LA5) {
        M_LA5 = m_LA5;
    }

    public String getM_PLL() {
        return M_PLL;
    }

    public void setM_PLL(String m_PLL) {
        M_PLL = m_PLL;
    }

    public String getM_PD() {
        return M_PD;
    }

    public void setM_PD(String m_PD) {
        M_PD = m_PD;
    }

    public String getM_GP() {
        return M_GP;
    }

    public void setM_GP(String m_GP) {
        M_GP = m_GP;
    }

    public String getM_DEP2() {
        return M_DEP2;
    }

    public void setM_DEP2(String m_DEP2) {
        M_DEP2 = m_DEP2;
    }

    public String getM_FAS() {
        return M_FAS;
    }

    public void setM_FAS(String m_FAS) {
        M_FAS = m_FAS;
    }

    public String getQ_ATF() {
        return Q_ATF;
    }

    public void setQ_ATF(String q_ATF) {
        Q_ATF = q_ATF;
    }

    public String getQ_SMME_LA() {
        return Q_SMME_LA;
    }

    public void setQ_SMME_LA(String q_SMME_LA) {
        Q_SMME_LA = q_SMME_LA;
    }

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}

	public String getVERIFY_FLG() {
		return VERIFY_FLG;
	}

	public void setVERIFY_FLG(String VERIFY_FLG) {
		VERIFY_FLG = VERIFY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}

	public Date getENTRY_TIME() {
		return ENTRY_TIME;
	}

	public void setENTRY_TIME(Date eNTRY_TIME) {
		ENTRY_TIME = eNTRY_TIME;
	}

	public Date getMODIFY_TIME() {
		return MODIFY_TIME;
	}

	public void setMODIFY_TIME(Date mODIFY_TIME) {
		MODIFY_TIME = mODIFY_TIME;
	}

	public Date getVERIFY_TIME() {
		return VERIFY_TIME;
	}

	public void setVERIFY_TIME(Date vERIFY_TIME) {
		VERIFY_TIME = vERIFY_TIME;
	}
    
    
    
}
