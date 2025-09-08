package com.bornfire.brrs.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.brrs.controllers.NavigationController;
import com.bornfire.brrs.entities.RRReport;
import com.bornfire.brrs.entities.RRReportMast;
import com.bornfire.brrs.entities.RRReportMastRepo;
import com.bornfire.brrs.entities.RRReportRepo;
import com.bornfire.brrs.entities.ReportsMaster;
import com.bornfire.brrs.entities.ReportsMasterRep;

@Service
@Transactional
@ConfigurationProperties("output")
public class ReportServices {
	
	@Autowired
	ReportsMasterRep ReportsMasterRep;
	
	@Autowired
	RRReportRepo rrReportlist;
	
	@Autowired
	RRReportMastRepo rrReportMastlist;
	
	private static final Logger logger = LoggerFactory.getLogger(NavigationController.class);
	
	
	/*public Iterable<RRReport> getReportsMaster() {
		logger.info("Getting Report Master");

		return rrReportlist.findAll();

	}
*/
	public Iterable<RRReportMast> getReportsMaster() {
		logger.info("Getting Report Master");

		return rrReportMastlist.findAll();

	}
	
	public String updateValidity(String rptCode, String valid, String userid) {

		String msg = "";
		try {
			rrReportlist.updateValidity(rptCode, valid, userid);
			System.out.println("Success");
			msg = "success";
		} catch (Exception e) {
			msg = "Error Occured. Please contact Administrator";
			e.printStackTrace();
		}

		return msg;

	}
}
