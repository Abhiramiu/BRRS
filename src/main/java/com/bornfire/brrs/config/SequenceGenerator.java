package com.bornfire.brrs.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bornfire.brrs.entities.AuditServicesRep;

@Component
public class SequenceGenerator {
	private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int DOC_SEQ_ID = 9;
	private static final String NUM_LIST= "0123456789";


	public int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(NUM_LIST.length());

		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}
	

	

	public String generateSeq() {

		StringBuffer randStr = new StringBuffer();
		
		
		for (int i = 0; i < DOC_SEQ_ID; i++) {
			int number = getRandomNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	@Autowired
	private AuditServicesRep auditServicesRep;
	
	public String generateRequestUUId() {

		StringBuffer randStr = new StringBuffer();
		randStr.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		randStr.append("_");
		
		Long request_UUID=auditServicesRep.getAuditRefUUID();
		
		randStr.append(String.format("%05d", request_UUID));
		
		return randStr.toString();
	}
	
}