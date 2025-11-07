package com.bornfire.brrs.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.brrs.entities.BRRS_M_PLL_Detail_Repo;
import com.bornfire.brrs.entities.BrrsCommonMappingEntity;
import com.bornfire.brrs.entities.BrrsCommonMappingRepo;
import com.bornfire.brrs.entities.M_PLL_Detail_Entity;

@Service
public class CommonMappingService {

	@Autowired
    private BrrsCommonMappingRepo commonMappingRepo;

	  @PersistenceContext
	  private EntityManager entityManager;
	  
    // List of allowed columns
    private static final List<String> allowedColumns = Arrays.asList(
            "M_PI", "M_SFINP1", "M_SFINP2", "M_LIQ", "M_SCI_E", "M_IS", "M_CA1", "M_CA2",
            "M_SRWA_12A", "M_OR1", "M_MRC", "M_IRR", "M_IRB", "M_GALOR", "M_CALOC",
            "M_I_S_CA", "M_SP", "M_DEP1", "Q_SMME_INT_INCOME", "M_LIQGAP", "M_LA1",
            "M_LA3", "M_LA4", "M_LA5", "M_PLL", "M_PD", "M_GP", "M_DEP2", "M_FAS",
            "Q_ATF", "Q_SMME_LA"
    );

    public List<String> getAllColumns() {
        return allowedColumns;
    }

    
    public List<Object[]> getMappingData(String selectedColumn) {
        if (selectedColumn == null || selectedColumn.isEmpty()) {
            return Collections.emptyList();
        }
        
        if (!allowedColumns.contains(selectedColumn)) {
            throw new IllegalArgumentException("Invalid column name: " + selectedColumn);
        }


        return commonMappingRepo.getColumnData(selectedColumn);
    }
    
    
    public Object[] getRecordBySno(String sNo) {
        String sql = "SELECT SNO, GL_CODE, GL_SUB_CODE, ACCOUNT_NO, CURRENCY FROM BRRS_COMMON_MAPPING_TABLE WHERE SNO = :sno";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("sno", sNo);
        return (Object[]) query.getSingleResult();
    }

    @Transactional
    public void deleteBySno(String sNo) {
        String sql = "DELETE FROM BRRS_COMMON_MAPPING_TABLE WHERE SNO = :sno";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("sno", sNo);
        query.executeUpdate();
    }

    public boolean updateCM(BrrsCommonMappingEntity CMData) {
        try {
        	BrrsCommonMappingEntity existing = commonMappingRepo.getdatabyAcctNo(CMData.getACCOUNT_NO());
            
            System.out.println("came to services");
            if (existing != null) {
                
            	existing.setM_PI(CMData.getM_PI());
            	existing.setM_SFINP1(CMData.getM_SFINP1());
            	existing.setM_SFINP2(CMData.getM_SFINP2());
            	existing.setM_LIQ(CMData.getM_LIQ());
            	existing.setM_SCI_E(CMData.getM_SCI_E());
            	existing.setM_IS(CMData.getM_IS());
            	existing.setM_CA1(CMData.getM_CA1());
            	existing.setM_CA2(CMData.getM_CA2());
            	existing.setM_SRWA_12A(CMData.getM_SRWA_12A());
            	existing.setM_OR1(CMData.getM_OR1());
            	existing.setM_MRC(CMData.getM_MRC());
            	existing.setM_IRR(CMData.getM_IRR());
            	existing.setM_IRB(CMData.getM_IRB());
            	existing.setM_GALOR(CMData.getM_GALOR());
            	existing.setM_CALOC(CMData.getM_CALOC());
            	existing.setM_I_S_CA(CMData.getM_I_S_CA());
            	existing.setM_SP(CMData.getM_SP());
            	existing.setM_DEP1(CMData.getM_DEP1());
            	existing.setQ_SMME_INT_INCOME(CMData.getQ_SMME_INT_INCOME());
            	existing.setM_LIQGAP(CMData.getM_LIQGAP());
            	existing.setM_LA1(CMData.getM_LA1());
            	existing.setM_LA3(CMData.getM_LA3());
            	existing.setM_LA4(CMData.getM_LA4());
            	existing.setM_LA5(CMData.getM_LA5());
            	existing.setM_PLL(CMData.getM_PLL());
            	existing.setM_PD(CMData.getM_PD());
            	existing.setM_GP(CMData.getM_GP());
            	existing.setM_DEP2(CMData.getM_DEP2());
            	existing.setM_FAS(CMData.getM_FAS());
            	existing.setQ_ATF(CMData.getQ_ATF());
            	existing.setQ_SMME_LA(CMData.getQ_SMME_LA());

            	existing.setMODIFY_FLG("Y");
            	existing.setDEL_FLG("N");

            	existing.setENTRY_TIME(CMData.getENTRY_TIME());
            	existing.setMODIFY_TIME(CMData.getMODIFY_TIME());
            	existing.setVERIFY_TIME(CMData.getVERIFY_TIME());

                
            	commonMappingRepo.save(existing);
                
                return true;
            } else {
                System.out.println("Record not found for Account No: " + CMData.getACCOUNT_NO());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
