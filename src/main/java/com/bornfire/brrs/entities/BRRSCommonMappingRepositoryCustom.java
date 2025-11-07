package com.bornfire.brrs.entities;

import java.util.List;

public interface BRRSCommonMappingRepositoryCustom {
	List<Object[]> getColumnData(String selectedColumn);
}

