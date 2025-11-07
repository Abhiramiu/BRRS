package com.bornfire.brrs.entities;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class BRRSCommonMappingRepositoryCustomImpl implements BRRSCommonMappingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager; // JPA’s direct database handler

    @Override
    public List<Object[]> getColumnData(String selectedColumn) {
        // ✅ Validate again at DB layer just in case
        if (selectedColumn == null || selectedColumn.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // ✅ Dynamically build query filtering non-null column values
        String sql = "SELECT SNO, GL_CODE, GL_SUB_CODE, ACCOUNT_NO, CURRENCY, "
                   + selectedColumn + " AS SELECTED_VALUE "
                   + "FROM BRRS_COMMON_MAPPING_TABLE "
                   + "WHERE " + selectedColumn + " IS NOT NULL";

        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }
}
