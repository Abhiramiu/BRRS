package com.bornfire.brrs.services;

import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bornfire.brrs.dto.ReportLineItemDTO;

@Service
public class ReportCodeMappingService {

	/*@Autowired
	private ReportCodeMappingRepo reportCodeMappingRepo;

	@Autowired
	private RRReportRepository rrReportRepository;

	

	public List<String> getAllReportCodes() {
		return rrReportRepository.findAllDistinctReportCodes();
	}

	public String getReportNameByCode(String rptCode) {
		return rrReportRepository.findReportDescriptionByRptCode(rptCode).orElse("N/A");
	}
	
	public BaseMappingParameter saveMapping(BaseMappingParameter mapping) {
		mapping.setEntry_time(new Date());
		mapping.setEntry_user("SYSTEM");
		mapping.setAuth_flg("N");
		mapping.setModify_flg("N");
		mapping.setDel_flg("N");

		return reportCodeMappingRepo.save(mapping);
	}
*/
	@Autowired
	private ReportLineItemService reportLineItemService;
	
	public List<ReportLineItemDTO> getReportDataByCode(String reportCode) throws Exception {
		System.out.println("ReportCodeMappingService received request for report code: " + reportCode);
		return reportLineItemService.getReportData(reportCode);
	}

	
}