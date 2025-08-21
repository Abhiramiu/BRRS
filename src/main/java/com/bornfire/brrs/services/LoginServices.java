package com.bornfire.brrs.services;

import java.io.File;


import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.bornfire.brrs.config.PasswordEncryption;
import com.bornfire.brrs.entities.BrrsSession;
import com.bornfire.brrs.entities.UserProfile;
import com.bornfire.brrs.entities.UserProfileRep;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
@ConfigurationProperties("output")
@Transactional
public class LoginServices {

	private static final Logger logger = LoggerFactory.getLogger(LoginServices.class);

	@Autowired
	UserProfileRep userProfileRep;


	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DataSource srcdataSource;

	@NotNull
	private String exportpath;

	@Value("${default.password}")
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getExportpath() {
		return exportpath;
	}

	public void setExportpath(String exportpath) {
		this.exportpath = exportpath;
	}

	/*
	 * Getting 3 inputs -
	 * 
	 * UserProfile Object, Formmode - Valid values : add, edit, inputuser - user who
	 * edited the data
	 * 
	 * if formmode is add - Get password from application.properties create the user
	 * 
	 * if formmode is edit - Get password from database for that user and use other
	 * fields came from front end.
	 * 
	 * 
	 */

	public String addUser(UserProfile userProfile, String formmode, String inputUser) {

		String msg = "";

		try {

			if (formmode.equals("add")) {

				UserProfile up = userProfile;
				try {
					String encryptedPassword = PasswordEncryption.getEncryptedPassword(this.password);

					if (up.getLogin_status().equals("Active")) {
						up.setUser_locked_flg("N");
					} else {
						up.setUser_locked_flg("Y");
					}

					if (up.getUser_status().equals("Active")) {
						up.setDisable_flg("N");
					} else {
						up.setDisable_flg("Y");
					}

					up.setEntity_flg("N");
					up.setEntry_time(new Date());
					up.setEntry_user(inputUser);
					
					up.setLogin_flg("N");//To prompt the user for changing the password at first login
					up.setNo_of_attmp(0);
					up.setPassword(encryptedPassword);

				} catch (Exception e) {
					e.printStackTrace();
				}

				
				userProfileRep.save(up);

				msg = "User Created Successfully";

			}
			//When the user data modifed and submitted.
			else {

				Optional<UserProfile> up = userProfileRep.findById(userProfile.getUserid());

				if (up.isPresent()) {

					userProfile.setPassword(up.get().getPassword());

					if (userProfile.getLogin_status().equals("Active")) {
						userProfile.setUser_locked_flg("N");
					} else {
						userProfile.setUser_locked_flg("Y");
					}

					if (userProfile.getUser_status().equals("Active")) {
						userProfile.setDisable_flg("N");
					} else {
						userProfile.setDisable_flg("Y");
					}

					userProfile.setNo_of_attmp(0);
					userProfile.setEntity_flg("N");
					userProfile.setModify_user(inputUser);
					userProfile.setModify_time(new Date());

					userProfileRep.save(userProfile);
				}

				msg = "User Edited Successfully";

			}
		} catch (Exception e) {
			msg = "Error Occured. Please contact Administrator";
			e.printStackTrace();
			logger.info(e.getMessage());
		}

		return msg;
	}

	
	public Iterable<UserProfile> getUsersList() {


		Iterable<UserProfile> users = userProfileRep.findAll();
		
		return users;

	}

	public UserProfile getUser(String id) {
		logger.info(id);
		if (userProfileRep.existsById(id)) {
			UserProfile up = userProfileRep.findById(id).get();
			logger.info(up.getEntity_flg());
			return up;
		} else {
			UserProfile UserProfile = new UserProfile();
			UserProfile.setLogin_low("09:00");
			UserProfile.setLogin_high("19:00");
			return  UserProfile;
		}

	};

public List<UserProfile> getFinUsersList() {
		
		Session hs = sessionFactory.getCurrentSession();
		return hs.createQuery("from UserProfile ", UserProfile.class).getResultList();		

	}
	
	
public String addUser(UserProfile userProfile, String formmode, String inputUser, String username, String mob, String role) {
    String msg = "";

    try {
        if ("add".equalsIgnoreCase(formmode)) {
            UserProfile up = userProfile;

            System.out.println("password is : " + up.getPassword());

            // Encrypt password
            String encryptedPassword = PasswordEncryption.getEncryptedPassword(up.getPassword());

            // Login and User Status Flags
            up.setUser_locked_flg("Active".equalsIgnoreCase(up.getLogin_status()) ? "N" : "Y");
            up.setDisable_flg("Active".equalsIgnoreCase(up.getUser_status()) ? "N" : "Y");

            up.setEntity_flg("N");
            up.setEntry_time(new Date());
            up.setEntry_user(inputUser);
            up.setLogin_flg("N");
            up.setNo_of_attmp(0);
            up.setLog_in_count("0");
            up.setEmp_name(up.getUsername());

            // Password expiry date
            if (up.getPass_exp_days() != null && !up.getPass_exp_days().trim().isEmpty()) {
                String localdateval = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                LocalDate date = LocalDate.parse(localdateval);
                BigDecimal passexpdays = new BigDecimal(up.getPass_exp_days());
                LocalDate date2 = date.plusDays(passexpdays.intValue());
                up.setPass_exp_date(new SimpleDateFormat("yyyy-MM-dd").parse(date2.toString()));
            } else {
                // Default expiry period: 90 days (optional)
                String localdateval = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                LocalDate date = LocalDate.parse(localdateval);
                LocalDate date2 = date.plusDays(90);
                up.setPass_exp_date(new SimpleDateFormat("yyyy-MM-dd").parse(date2.toString()));
            }

            // Final password set
            up.setPassword(encryptedPassword);

            // Save the user
            userProfileRep.save(up);

            // Optional: Audit logging
            if (mob != null && !mob.trim().isEmpty()) {
                try {
                    BigDecimal mobile = new BigDecimal(mob); // only if needed
                } catch (Exception e) {
                    System.out.println("Invalid mobile number for audit");
                }
            }

            // Example audit log ID fetch
            try {
                Session hs = sessionFactory.getCurrentSession();
                BigDecimal srlno = (BigDecimal) hs
                    .createNativeQuery("SELECT USER_AUDIT_SRL_NO.NEXTVAL AS SRL_NO FROM DUAL")
                    .getSingleResult();
                System.out.println("Audit SRL_NO: " + srlno);
                // you can insert audit data here
            } catch (Exception e) {
                System.out.println("Audit logging failed: " + e.getMessage());
            }

            msg = "User Created Successfully";

        } else {
            // Form mode is not add, update flow
            Optional<UserProfile> upOptional = userProfileRep.findById(userProfile.getUserid());

            if (upOptional.isPresent()) {
                UserProfile up = upOptional.get();

                userProfile.setPassword(up.getPassword());

                userProfile.setUser_locked_flg("Active".equalsIgnoreCase(userProfile.getLogin_status()) ? "N" : "Y");
                userProfile.setDisable_flg("Active".equalsIgnoreCase(userProfile.getUser_status()) ? "N" : "Y");

                // Handle pass_exp_days and pass_exp_date
                if (userProfile.getPass_exp_days() != null &&
                    userProfile.getPass_exp_days().equals(up.getPass_exp_days())) {
                    userProfile.setPass_exp_date(up.getPass_exp_date());
                } else {
                    if (userProfile.getPass_exp_days() != null && !userProfile.getPass_exp_days().trim().isEmpty()) {
                        String localdateval = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        LocalDate date = LocalDate.parse(localdateval);
                        BigDecimal passexpdays = new BigDecimal(userProfile.getPass_exp_days());
                        LocalDate date2 = date.plusDays(passexpdays.intValue());
                        userProfile.setPass_exp_date(new SimpleDateFormat("yyyy-MM-dd").parse(date2.toString()));
                    }
                }

                userProfile.setLog_in_count(up.getLog_in_count() != null ? up.getLog_in_count() : "1");
                userProfile.setEntry_user(up.getEntry_user());
                userProfile.setEntry_time(up.getEntry_time());
                userProfile.setNo_of_attmp(0);
                userProfile.setEntity_flg("N");
                userProfile.setModify_user(inputUser);
                userProfile.setModify_time(new Date());

                userProfileRep.save(userProfile);
                msg = "User Edited Successfully";
            } else {
                msg = "User Not found to edit";
            }
        }

    } catch (Exception e) {
        msg = "Error Occurred. Please contact Administrator";
        e.printStackTrace();
        logger.info(e.getMessage());
    }

    return msg;
}


	
	public String verifyUser(UserProfile userProfile, String inputUser) {
		String msg = "";

		Optional<UserProfile> up = userProfileRep.findById(userProfile.getUserid());

		try {

			if (up.isPresent()) {

				userProfile.setPassword(up.get().getPassword());

				if (userProfile.getLogin_status().equals("Active")) {
					userProfile.setUser_locked_flg("N");
				} else {
					userProfile.setUser_locked_flg("Y");
				}

				if (userProfile.getUser_status().equals("Active")) {
					userProfile.setDisable_flg("N");
				} else {
					userProfile.setDisable_flg("Y");
				}

				userProfile.setNo_of_attmp(0);
				userProfile.setEntity_flg("Y");
				userProfile.setLogin_flg("N");
				userProfile.setAuth_user(inputUser);
				userProfile.setAuth_time(new Date());

				userProfileRep.save(userProfile);
			}

			msg = "User Verified Successfully";
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			msg = "Error Occured. Please contact Administrator";
		}

		return msg;
	}

	public String passwordReset(UserProfile userprofile, String userid) {

		String msg = "";

		try {
			String encryptedPassword = PasswordEncryption.getEncryptedPassword(this.password);

			Optional<UserProfile> up = userProfileRep.findById(userprofile.getUserid());

			if (up.isPresent()) {
				UserProfile user = up.get();
				user.setPassword(encryptedPassword);
				user.setNo_of_attmp(0);
				user.setLogin_flg("N");
				user.setUser_locked_flg("N");
				userProfileRep.save(user);
			}

			msg = "Password Resetted Successfully";

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

			e.printStackTrace();

			msg = "Error Occured. Please contact Administrator";
		}

		return msg;
	}

	/*
	 * Getting LoginFlg -
	 * 
	 * If loginFlg = 'N' - User should be prompted to change password. else thats
	 * not required.
	 * 
	 * Loginflg ='N' will be updated at the time of new user creation and at the
	 * time of password reset by admin.
	 * 
	 */
	public String checkPasswordChangeReq(String userid) {

		Optional<UserProfile> up = userProfileRep.findById(userid);
		String loginflg = up.get().getLogin_flg();

		return loginflg;
	}

	public int checkAcctexpirty(String userid) {

		Optional<UserProfile> up = userProfileRep.findById(userid);
		Date expDate = up.get().getAcc_exp_date();
		Date currDate = new Date();

		DateTime dt1 = new DateTime(currDate);
		DateTime dt2 = new DateTime(expDate);

		int remaindays = Days.daysBetween(dt1, dt2).getDays();

		logger.info("Account Expired in:" + remaindays);
		return remaindays;
	}

	public int checkpassexpirty(String userid) {

		Optional<UserProfile> up = userProfileRep.findById(userid);
		Date expDate = up.get().getPass_exp_date();
		Date currDate = new Date();

		DateTime dt1 = new DateTime(currDate);
		DateTime dt2 = new DateTime(expDate);

		int remaindays = Days.daysBetween(dt1, dt2).getDays();

		logger.info("Password Expired in:" + remaindays);
		return remaindays;
	}

	public String changePassword(String oldpass, String newpass, String userid) {
		String msg = "";

		Optional<UserProfile> up = userProfileRep.findById(userid);

		try {
			if (up.isPresent()) {
				UserProfile user = up.get();
				if (PasswordEncryption.validatePassword(oldpass, user.getPassword())) {
					
					if (!PasswordEncryption.validatePassword(newpass, user.getPassword())) {
						
						String encryptedPassword = PasswordEncryption.getEncryptedPassword(newpass);
						user.setPassword(encryptedPassword);
						user.setLogin_flg("Y");
						
						LocalDateTime localDateTime = user.getPass_exp_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
						user.setPass_exp_date(Date.from(localDateTime.plusDays(365).atZone(ZoneId.systemDefault()).toInstant()));
						
						userProfileRep.save(user);
						msg = "Password Changed Successfully";
						
					}else {
						
						msg = "New password cannot be Same as Old password";
					}
					
					
				} else {
					msg = "Incorrect Old Password!";
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			msg = "Error Occured. Please contact Administrator";
		}
		logger.info(msg);
		return msg;
	};

	public void SessionLogging(String menuname, String menuid, String sessionid, String userid, String ip,
			String status) {
		Session hs = sessionFactory.getCurrentSession();

		try {

			if (menuname.equals("LOGOUT")) {

				hs.createQuery("update XBRLSession set status='IN-ACTIVE' where session_id = ?1")
						.setParameter(1, sessionid).executeUpdate();

			} else {
				
				hs.save(new BrrsSession(menuname, menuid, sessionid, userid, ip, new Date(), status));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String deleteuser(String userid) {
		String msg = "";
		
		try {
			Optional<UserProfile> user = userProfileRep.findById(userid);
			if(user.isPresent()) {
				userProfileRep.deleteById(userid);
				msg = "User Id Rejected";
				
			}else {
				msg = "Invalid Data";
			}
			
		}catch (Exception e) {
			msg = "Please contact Administrator";
			// TODO: handle exception
		}
		return msg;
	}
	

	public File getUserLogFile(Date fromdate, Date todate) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

		String path = exportpath;
		String fileName = "USER_LOGS_"+dateFormat.format(new Date())+".xlsx";
		File outputFile;

		File jasperFile;
		
		File folders = new File(path);
		if (!folders.exists()) {
			folders.mkdirs();
		}
		
		
		try {
			jasperFile = ResourceUtils.getFile("classpath:static/jasper/USER_LOGS/UserLogs.jasper");
			JasperReport jr = (JasperReport) JRLoader.loadObject(jasperFile);
			HashMap<String, Object> map = new HashMap<String, Object>();

			logger.info("Assigning Parameters for Jasper");
			map.put("FromDate", dateFormat.format(fromdate));
			map.put("ToDate", dateFormat.format(todate));
			
			
			
			path = path + "/" + fileName;
			JasperPrint jp = JasperFillManager.fillReport(jr, map, srcdataSource.getConnection());
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jp));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));
			exporter.exportReport();
			logger.info("Excel File exported");
			
		} catch (FileNotFoundException|JRException|SQLException e) {
			
			e.printStackTrace();
		}


		outputFile = new File(path);	

		
	return outputFile;
	}

	public List<BrrsSession> getUserLog(Date fromdate, Date todate) {
		
		
		Session hs = sessionFactory.getCurrentSession();
		
		List<BrrsSession> ls = hs.createQuery("from XBRLSession where trunc(entry_time,'DD') between ?1 and ?2 and menu in ('LOGIN','LOGOUT') order by entry_time desc ", BrrsSession.class)
		.setParameter(1, fromdate)
		.setParameter(2, todate)
		.getResultList();
		
		
		return ls;
	}
	
	

}
