package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_TBS_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Summary_Repo;
import com.bornfire.brrs.entities.M_CA7_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Summary_Entity;

@Component
@Service
public class BRRS_M_TBS_ReportService {


	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_TBS_ReportService.class);


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_M_TBS_Summary_Repo BRRS_M_TBS_Summary_Repo;
	

	@Autowired
	BRRS_M_TBS_Archival_Summary_Repo BRRS_M_TBS_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	public ModelAndView getM_TBSView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable,String type, String version) {
System.out.println("Entered service method M_TBS......................");
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	
		try {
			Date d1 = dateformat.parse(todate);

	 // ---------- CASE 1: ARCHIVAL ----------
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            List<M_TBS_Archival_Summary_Entity> T1Master = 
            		BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }
        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_TBS_Archival_Summary_Entity> T1Master =
            		BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_TBS_Summary_Entity> T1Master = 
            		BRRS_M_TBS_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_TBS");		
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
}
	
	public void updateReport(M_TBS_Summary_Entity Entity) {
	    System.out.println("Report Date: " + Entity.getReportDate());

	    M_TBS_Summary_Entity existing = BRRS_M_TBS_Summary_Repo.findById(Entity.getReportDate())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + Entity.getReportDate()));
	    //  R11 =SUM(C12:C16)+C21

	    try {
	        int[] rowNumbers = {12, 13, 14, 15,16,21};
	        String[] fields = {
	            "NV_LONG",
	            "NV_SHORT",
	            "FV_LONG",
	            "FV_SHORT",
	            "QFHA"
	        };

	        for (int row : rowNumbers) {
	            String prefix = "R" + row + "_";
	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing field
	                    continue;
	                }
	            }
	        }

	        for (String field : fields) {
	            String getterName = "getR11_" + field;
	            String setterName = "setR11_" + field;
	            try {
	                Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                Object newValue = getter.invoke(Entity);
	                setter.invoke(existing, newValue);
	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    
	// R16 =SUM(C17:C20)
        try {
           
            for (int i = 17; i <= 20; i++) {
                String prefix = "R" + i + "_";
                String[] fields = {
                		"NV_LONG",
        	            "NV_SHORT",
        	            "FV_LONG",
        	            "FV_SHORT",
        	            "QFHA"
                };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                        Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                        Object newValue = getter.invoke(Entity);
                        setter.invoke(existing, newValue);

                    } catch (NoSuchMethodException e) {
                       
                        continue;
                    }
                }
            }

            String[] totalFields1 = {
            		"NV_LONG",
    	            "NV_SHORT",
    	            "FV_LONG",
    	            "FV_SHORT",
    	            "QFHA"
            };

            for (String field : totalFields1) {
                String getterName = "getR16_" + field;
                String setterName = "setR16_" + field;

                try {
                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(Entity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating R29 fields (54,55,56 and total)", e);
        }
        
        //R22=SUM(C23:C27)+C33
        

	    try {
	        int[] rowNumbers = {23,24, 25, 26,27,33};
	        String[] fields = {
	        		"NV_LONG",
		            "NV_SHORT",
		            "FV_LONG",
		            "FV_SHORT",
		            "QFHA"
	        };

	        for (int row : rowNumbers) {
	            String prefix = "R" + row + "_";
	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing field
	                    continue;
	                }
	            }
	        }

	        for (String field : fields) {
	            String getterName = "getR22_" + field;
	            String setterName = "setR22_" + field;
	            try {
	                Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                Object newValue = getter.invoke(Entity);
	                setter.invoke(existing, newValue);
	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    //R27=SUM(C28:C32)
	    try {
	           
            for (int i = 28; i <= 32; i++) {
                String prefix = "R" + i + "_";
                String[] fields = {
                		"NV_LONG",
        	            "NV_SHORT",
        	            "FV_LONG",
        	            "FV_SHORT",
        	            "QFHA"
                };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                        Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                        Object newValue = getter.invoke(Entity);
                        setter.invoke(existing, newValue);

                    } catch (NoSuchMethodException e) {
                       
                        continue;
                    }
                }
            }

            String[] totalFields1 = {
            		"NV_LONG",
    	            "NV_SHORT",
    	            "FV_LONG",
    	            "FV_SHORT",
    	            "QFHA"
            };

            for (String field : totalFields1) {
                String getterName = "getR27_" + field;
                String setterName = "setR27_" + field;

                try {
                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(Entity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating R29 fields (54,55,56 and total)", e);
        }
	    
	    //R34=C35+C36+C40
	    try {
	        int[] rowNumbers = {35,36,40};
	        String[] fields = {
	        		"NV_LONG",
		            "NV_SHORT",
		            "FV_LONG",
		            "FV_SHORT",
		            "QFHA"
	        };

	        for (int row : rowNumbers) {
	            String prefix = "R" + row + "_";
	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing field
	                    continue;
	                }
	            }
	        }

	        for (String field : fields) {
	            String getterName = "getR34_" + field;
	            String setterName = "setR34_" + field;
	            try {
	                Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                Object newValue = getter.invoke(Entity);
	                setter.invoke(existing, newValue);
	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    //R36=SUM(C37:C39)
	    try {
	           
            for (int i = 37; i <= 39; i++) {
                String prefix = "R" + i + "_";
                String[] fields = {
                		"NV_LONG",
        	            "NV_SHORT",
        	            "FV_LONG",
        	            "FV_SHORT",
        	            "QFHA"
                };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                        Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                        Object newValue = getter.invoke(Entity);
                        setter.invoke(existing, newValue);

                    } catch (NoSuchMethodException e) {
                       
                        continue;
                    }
                }
            }

            String[] totalFields1 = {
            		"NV_LONG",
    	            "NV_SHORT",
    	            "FV_LONG",
    	            "FV_SHORT",
    	            "QFHA"
            };

            for (String field : totalFields1) {
                String getterName = "getR36_" + field;
                String setterName = "setR36_" + field;

                try {
                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(Entity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating  total)", e);
        }
	    //R41=C42+C43+C44+C49
	    try {
	        int[] rowNumbers = {42,43,44,49};
	        String[] fields = {
	        		"NV_LONG",
		            "NV_SHORT",
		            "FV_LONG",
		            "FV_SHORT",
		            "QFHA"
	        };

	        for (int row : rowNumbers) {
	            String prefix = "R" + row + "_";
	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing field
	                    continue;
	                }
	            }
	        }

	        for (String field : fields) {
	            String getterName = "getR41_" + field;
	            String setterName = "setR41_" + field;
	            try {
	                Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                Object newValue = getter.invoke(Entity);
	                setter.invoke(existing, newValue);
	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    //R44=SUM(C45:C48)
	    
	    try {
	           
            for (int i = 45; i <= 48; i++) {
                String prefix = "R" + i + "_";
                String[] fields = {
                		"NV_LONG",
        	            "NV_SHORT",
        	            "FV_LONG",
        	            "FV_SHORT",
        	            "QFHA"
                };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                        Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                        Object newValue = getter.invoke(Entity);
                        setter.invoke(existing, newValue);

                    } catch (NoSuchMethodException e) {
                       
                        continue;
                    }
                }
            }

            String[] totalFields1 = {
            		"NV_LONG",
    	            "NV_SHORT",
    	            "FV_LONG",
    	            "FV_SHORT",
    	            "QFHA"
            };

            for (String field : totalFields1) {
                String getterName = "getR44_" + field;
                String setterName = "setR44_" + field;

                try {
                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(Entity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating  total)", e);
        }
	    //R50=SUM(C51:C54)
	    
	    try {
	           
            for (int i = 51; i <= 54; i++) {
                String prefix = "R" + i + "_";
                String[] fields = {
                		"NV_LONG",
        	            "NV_SHORT",
        	            "FV_LONG",
        	            "FV_SHORT",
        	            "QFHA"
                };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                        Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                        Object newValue = getter.invoke(Entity);
                        setter.invoke(existing, newValue);

                    } catch (NoSuchMethodException e) {
                       
                        continue;
                    }
                }
            }

            String[] totalFields1 = {
            		"NV_LONG",
    	            "NV_SHORT",
    	            "FV_LONG",
    	            "FV_SHORT",
    	            "QFHA"
            };

            for (String field : totalFields1) {
                String getterName = "getR50_" + field;
                String setterName = "setR50_" + field;

                try {
                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

                    Object newValue = getter.invoke(Entity);
                    setter.invoke(existing, newValue);

                } catch (NoSuchMethodException e) {
                    continue;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating  total)", e);
        }
	    //R55=C50+C41+C34+C22+C11
	    try {
	        int[] rowNumbers = {50,41,34,22,11};
	        String[] fields = {
	        		"NV_LONG",
		            "NV_SHORT",
		            "FV_LONG",
		            "FV_SHORT",
		            "QFHA"
	        };

	        for (int row : rowNumbers) {
	            String prefix = "R" + row + "_";
	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(Entity);
	                    setter.invoke(existing, newValue);
	                } catch (NoSuchMethodException e) {
	                    // Skip missing field
	                    continue;
	                }
	            }
	        }

	        for (String field : fields) {
	            String getterName = "getR55_" + field;
	            String setterName = "setR55_" + field;
	            try {
	                Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);
	                Method setter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                Object newValue = getter.invoke(Entity);
	                setter.invoke(existing, newValue);
	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    
	    
	        BRRS_M_TBS_Summary_Repo.save(existing);
	}


	/// RESUB VIEW
	public List<Object[]> getM_TBSResub() {
	List<Object[]> resubList = new ArrayList<>();
	try {
	List<M_TBS_Archival_Summary_Entity> latestArchivalList = 
			BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListWithVersionAll();

	if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	for (M_TBS_Archival_Summary_Entity entity : latestArchivalList) {
	Object[] row = new Object[] {
	entity.getReportDate(),
	entity.getReportVersion()
	};
	resubList.add(row);
	}
	System.out.println("Fetched " + resubList.size() + " record(s)");
	} else {
	System.out.println("No archival data found.");
	}
	} catch (Exception e) {
	System.err.println("Error fetching M_TBS Resub data: " + e.getMessage());
	e.printStackTrace();
	}
	return resubList;
	}

	
	//Archival View
	public List<Object[]> getM_TBSArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_TBS_Archival_Summary_Entity> repoData = BRRS_M_TBS_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_TBS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReportDate(), 
							entity.getReportVersion() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_TBS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_TBS Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}		
	// Resubmit the values , latest version and Resub Date
			public void updateReportReSub(M_TBS_Summary_Entity updatedEntity) {
				System.out.println("Came to Resub Service");
				System.out.println("Report Date: " + updatedEntity.getReportDate());

				Date reportDate = updatedEntity.getReportDate();
				int newVersion = 1;

				try {
					// Fetch the latest archival version for this report date
					Optional<M_TBS_Archival_Summary_Entity> latestArchivalOpt = BRRS_M_TBS_Archival_Summary_Repo
							.getLatestArchivalVersionByDate(reportDate);

					// Determine next version number
					if (latestArchivalOpt.isPresent()) {
						M_TBS_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
						try {
							newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
						} catch (NumberFormatException e) {
							System.err.println("Invalid version format. Defaulting to version 1");
							newVersion = 1;
						}
					} else {
						System.out.println("No previous archival found for date: " + reportDate);
					}

					// Prevent duplicate version number
					boolean exists = BRRS_M_TBS_Archival_Summary_Repo
							.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
							.isPresent();

					if (exists) {
						throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
					}

					// Copy summary entity to archival entity
					M_TBS_Archival_Summary_Entity archivalEntity = new M_TBS_Archival_Summary_Entity();
					org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

					archivalEntity.setReportDate(reportDate);
					archivalEntity.setReportVersion(String.valueOf(newVersion));
					archivalEntity.setReportResubDate(new Date());

					System.out.println("Saving new archival version: " + newVersion);

					// Save new version to repository
					BRRS_M_TBS_Archival_Summary_Repo.save(archivalEntity);

					System.out.println(" Saved archival version successfully: " + newVersion);

				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Error while creating archival resubmission record", e);
				}
			}


			public byte[] getM_TBSExcel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype,String type,String version) throws Exception {
				logger.info("Service: Starting Excel generation process in memory.");


logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
Date reportDate = dateformat.parse(todate);
				System.out.println(type);
				System.out.println(version);
				if (type.equals("ARCHIVAL") & version != null) {
					byte[] ARCHIVALreport = getExcelM_TBSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
							version);
					return ARCHIVALreport;
				}


				// RESUB check
				else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
				logger.info("Service: Generating RESUB report for version {}", version);


				List<M_TBS_Archival_Summary_Entity> T1Master =
						BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);


				
				// Generate Excel for RESUB
				return BRRS_M_tbsResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				}
				
				List<M_TBS_Summary_Entity> dataList =BRRS_M_TBS_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_TBS report. Returning empty result.");
					return new byte[0];
				}

				String templateDir = env.getProperty("output.exportpathtemp");
				String templateFileName = filename;
				System.out.println(filename);
				Path templatePath = Paths.get(templateDir, templateFileName);
				System.out.println(templatePath);
				
				logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

				if (!Files.exists(templatePath)) {
					// This specific exception will be caught by the controller.
					throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
				}
				if (!Files.isReadable(templatePath)) {
					// A specific exception for permission errors.
					throw new SecurityException(
							"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
				}

				// This try-with-resources block is perfect. It guarantees all resources are
				// closed automatically.
				try (InputStream templateInputStream = Files.newInputStream(templatePath);
						Workbook workbook = WorkbookFactory.create(templateInputStream);
						ByteArrayOutputStream out = new ByteArrayOutputStream()) {

					Sheet sheet = workbook.getSheetAt(0);

					// --- Style Definitions ---
					CreationHelper createHelper = workbook.getCreationHelper();

					CellStyle dateStyle = workbook.createCellStyle();
					dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
					dateStyle.setBorderBottom(BorderStyle.THIN);
					dateStyle.setBorderTop(BorderStyle.THIN);
					dateStyle.setBorderLeft(BorderStyle.THIN);
					dateStyle.setBorderRight(BorderStyle.THIN);

					CellStyle textStyle = workbook.createCellStyle();
					textStyle.setBorderBottom(BorderStyle.THIN);
					textStyle.setBorderTop(BorderStyle.THIN);
					textStyle.setBorderLeft(BorderStyle.THIN);
					textStyle.setBorderRight(BorderStyle.THIN);
					
					// Create the font
					Font font = workbook.createFont();
					font.setFontHeightInPoints((short)8); // size 8
					font.setFontName("Arial");    

					CellStyle numberStyle = workbook.createCellStyle();
					//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
					numberStyle.setBorderBottom(BorderStyle.THIN);
					numberStyle.setBorderTop(BorderStyle.THIN);
					numberStyle.setBorderLeft(BorderStyle.THIN);
					numberStyle.setBorderRight(BorderStyle.THIN);
					numberStyle.setFont(font);
					// --- End of Style Definitions ---

					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
					int startRow = 10;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_TBS_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber="+startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
/*
 * 	private BigDecimal R11_NV_LONG;
			private BigDecimal R11_NV_SHORT;
			private BigDecimal R11_FV_LONG;
			private BigDecimal R11_FV_SHORT;
			private BigDecimal R11_QFHA;
 */
							//row11
							// Column C 
							//row=sheet.getRow(11);

							Cell cell1 = row.getCell(2);
							if (record.getR11_NV_LONG() != null) {
								cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR11_NV_SHORT() != null) {
								cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

						    cell1 = row.getCell(4);
							if (record.getR11_FV_LONG() != null) {
								cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR11_FV_SHORT() != null) {
								cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR11_QFHA() != null) {
								cell1.setCellValue(record.getR11_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
//------ROW11---------						
							row=sheet.getRow(11);
						    cell1 = row.getCell(2);
							if (record.getR12_NV_LONG() != null) {
								cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR12_NV_SHORT() != null) {
								cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR12_FV_LONG() != null) {
								cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR12_FV_SHORT() != null) {
								cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR12_QFHA() != null) {
								cell1.setCellValue(record.getR12_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							//------ROW12---------						
							row=sheet.getRow(12);
						    cell1 = row.getCell(2);
							if (record.getR13_NV_LONG() != null) {
								cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR13_NV_SHORT() != null) {
								cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR13_FV_LONG() != null) {
								cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR13_FV_SHORT() != null) {
								cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR13_QFHA() != null) {
								cell1.setCellValue(record.getR13_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	

							//------ROW13---------
							row = sheet.getRow(13);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR14_NV_LONG() != null) {
							    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR14_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR14_FV_LONG() != null) {
							    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR14_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR14_QFHA() != null) {
							    cell1.setCellValue(record.getR14_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW14---------
							row = sheet.getRow(14);

							cell1 = row.getCell(2);
							if (record.getR15_NV_LONG() != null) {
							    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR15_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR15_FV_LONG() != null) {
							    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR15_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR15_QFHA() != null) {
							    cell1.setCellValue(record.getR15_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW15---------
							row = sheet.getRow(15);

							cell1 = row.getCell(2);
							if (record.getR16_NV_LONG() != null) {
							    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR16_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR16_FV_LONG() != null) {
							    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR16_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR16_QFHA() != null) {
							    cell1.setCellValue(record.getR16_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW16---------
							row = sheet.getRow(16);

							cell1 = row.getCell(2);
							if (record.getR17_NV_LONG() != null) {
							    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR17_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR17_FV_LONG() != null) {
							    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR17_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR17_QFHA() != null) {
							    cell1.setCellValue(record.getR17_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW17---------
							row = sheet.getRow(17);

							cell1 = row.getCell(2);
							if (record.getR18_NV_LONG() != null) {
							    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR18_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR18_FV_LONG() != null) {
							    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR18_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR18_QFHA() != null) {
							    cell1.setCellValue(record.getR18_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW18---------
							row = sheet.getRow(18);

							cell1 = row.getCell(2);
							if (record.getR19_NV_LONG() != null) {
							    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR19_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR19_FV_LONG() != null) {
							    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR19_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR19_QFHA() != null) {
							    cell1.setCellValue(record.getR19_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW19---------
							row = sheet.getRow(19);

							cell1 = row.getCell(2);
							if (record.getR20_NV_LONG() != null) {
							    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR20_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR20_FV_LONG() != null) {
							    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR20_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR20_QFHA() != null) {
							    cell1.setCellValue(record.getR20_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW20---------
							row = sheet.getRow(20);

							cell1 = row.getCell(2);
							if (record.getR21_NV_LONG() != null) {
							    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR21_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR21_FV_LONG() != null) {
							    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR21_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR21_QFHA() != null) {
							    cell1.setCellValue(record.getR21_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW21---------
							row = sheet.getRow(21);

							cell1 = row.getCell(2);
							if (record.getR22_NV_LONG() != null) {
							    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR22_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR22_FV_LONG() != null) {
							    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR22_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR22_QFHA() != null) {
							    cell1.setCellValue(record.getR22_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW22---------
							row = sheet.getRow(22);

							cell1 = row.getCell(2);
							if (record.getR23_NV_LONG() != null) {
							    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR23_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR23_FV_LONG() != null) {
							    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR23_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR23_QFHA() != null) {
							    cell1.setCellValue(record.getR23_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW23---------
							row = sheet.getRow(23);

							cell1 = row.getCell(2);
							if (record.getR24_NV_LONG() != null) {
							    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR24_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR24_FV_LONG() != null) {
							    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR24_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR24_QFHA() != null) {
							    cell1.setCellValue(record.getR24_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW24---------
							row = sheet.getRow(24);

							cell1 = row.getCell(2);
							if (record.getR25_NV_LONG() != null) {
							    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR25_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR25_FV_LONG() != null) {
							    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR25_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR25_QFHA() != null) {
							    cell1.setCellValue(record.getR25_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW25---------
							row = sheet.getRow(25);

							cell1 = row.getCell(2);
							if (record.getR26_NV_LONG() != null) {
							    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR26_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR26_FV_LONG() != null) {
							    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR26_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR26_QFHA() != null) {
							    cell1.setCellValue(record.getR26_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW26---------
							row = sheet.getRow(26);

							cell1 = row.getCell(2);
							if (record.getR27_NV_LONG() != null) {
							    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR27_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR27_FV_LONG() != null) {
							    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR27_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR27_QFHA() != null) {
							    cell1.setCellValue(record.getR27_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW27---------
							row = sheet.getRow(27);

							cell1 = row.getCell(2);
							if (record.getR28_NV_LONG() != null) {
							    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR28_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR28_FV_LONG() != null) {
							    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR28_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR28_QFHA() != null) {
							    cell1.setCellValue(record.getR28_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW28---------
							row = sheet.getRow(28);

							cell1 = row.getCell(2);
							if (record.getR29_NV_LONG() != null) {
							    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR29_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR29_FV_LONG() != null) {
							    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR29_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR29_QFHA() != null) {
							    cell1.setCellValue(record.getR29_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW29---------
							row = sheet.getRow(29);

							cell1 = row.getCell(2);
							if (record.getR30_NV_LONG() != null) {
							    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR30_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR30_FV_LONG() != null) {
							    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR30_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR30_QFHA() != null) {
							    cell1.setCellValue(record.getR30_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW30--------- R31
							row = sheet.getRow(30);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR31_NV_LONG() != null) {
							    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR31_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR31_FV_LONG() != null) {
							    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR31_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR31_QFHA() != null) {
							    cell1.setCellValue(record.getR31_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW31--------- R32
							row = sheet.getRow(31);

							cell1 = row.getCell(2);
							if (record.getR32_NV_LONG() != null) {
							    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR32_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR32_FV_LONG() != null) {
							    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR32_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR32_QFHA() != null) {
							    cell1.setCellValue(record.getR32_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW32--------- R33
							row = sheet.getRow(32);

							cell1 = row.getCell(2);
							if (record.getR33_NV_LONG() != null) {
							    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR33_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR33_FV_LONG() != null) {
							    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR33_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR33_QFHA() != null) {
							    cell1.setCellValue(record.getR33_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW33--------- R34
							row = sheet.getRow(33);

							cell1 = row.getCell(2);
							if (record.getR34_NV_LONG() != null) {
							    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR34_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR34_FV_LONG() != null) {
							    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR34_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR34_QFHA() != null) {
							    cell1.setCellValue(record.getR34_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW34--------- R35
							row = sheet.getRow(34);

							cell1 = row.getCell(2);
							if (record.getR35_NV_LONG() != null) {
							    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR35_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR35_FV_LONG() != null) {
							    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR35_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR35_QFHA() != null) {
							    cell1.setCellValue(record.getR35_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW35--------- R36
							row = sheet.getRow(35);

							cell1 = row.getCell(2);
							if (record.getR36_NV_LONG() != null) {
							    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR36_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR36_FV_LONG() != null) {
							    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR36_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR36_QFHA() != null) {
							    cell1.setCellValue(record.getR36_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW36--------- R37
							row = sheet.getRow(36);

							cell1 = row.getCell(2);
							if (record.getR37_NV_LONG() != null) {
							    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR37_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR37_FV_LONG() != null) {
							    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR37_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR37_QFHA() != null) {
							    cell1.setCellValue(record.getR37_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW37--------- R38
							row = sheet.getRow(37);

							cell1 = row.getCell(2);
							if (record.getR38_NV_LONG() != null) {
							    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR38_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR38_FV_LONG() != null) {
							    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR38_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR38_QFHA() != null) {
							    cell1.setCellValue(record.getR38_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW38--------- R39
							row = sheet.getRow(38);

							cell1 = row.getCell(2);
							if (record.getR39_NV_LONG() != null) {
							    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR39_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR39_FV_LONG() != null) {
							    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR39_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR39_QFHA() != null) {
							    cell1.setCellValue(record.getR39_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW39--------- R40
							row = sheet.getRow(39);

							cell1 = row.getCell(2);
							if (record.getR40_NV_LONG() != null) {
							    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR40_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR40_FV_LONG() != null) {
							    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR40_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR40_QFHA() != null) {
							    cell1.setCellValue(record.getR40_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							//------ROW40--------- R41
							row = sheet.getRow(40);

							cell1 = row.getCell(2);
							if (record.getR41_NV_LONG() != null) {
							    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR41_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR41_FV_LONG() != null) {
							    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR41_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR41_QFHA() != null) {
							    cell1.setCellValue(record.getR41_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW41--------- R42
							row = sheet.getRow(41);

							cell1 = row.getCell(2);
							if (record.getR42_NV_LONG() != null) {
							    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR42_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR42_FV_LONG() != null) {
							    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR42_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR42_QFHA() != null) {
							    cell1.setCellValue(record.getR42_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW42--------- R43
							row = sheet.getRow(42);

							cell1 = row.getCell(2);
							if (record.getR43_NV_LONG() != null) {
							    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR43_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR43_FV_LONG() != null) {
							    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR43_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR43_QFHA() != null) {
							    cell1.setCellValue(record.getR43_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW43--------- R44
							row = sheet.getRow(43);

							cell1 = row.getCell(2);
							if (record.getR44_NV_LONG() != null) {
							    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR44_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR44_FV_LONG() != null) {
							    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR44_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR44_QFHA() != null) {
							    cell1.setCellValue(record.getR44_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW44--------- R45
							row = sheet.getRow(44);

							cell1 = row.getCell(2);
							if (record.getR45_NV_LONG() != null) {
							    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR45_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR45_FV_LONG() != null) {
							    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR45_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR45_QFHA() != null) {
							    cell1.setCellValue(record.getR45_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW45--------- R46
							row = sheet.getRow(45);

							cell1 = row.getCell(2);
							if (record.getR46_NV_LONG() != null) {
							    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR46_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR46_FV_LONG() != null) {
							    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR46_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR46_QFHA() != null) {
							    cell1.setCellValue(record.getR46_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW46--------- R47
							row = sheet.getRow(46);

							cell1 = row.getCell(2);
							if (record.getR47_NV_LONG() != null) {
							    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR47_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR47_FV_LONG() != null) {
							    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR47_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR47_QFHA() != null) {
							    cell1.setCellValue(record.getR47_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW47--------- R48
							row = sheet.getRow(47);

							cell1 = row.getCell(2);
							if (record.getR48_NV_LONG() != null) {
							    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR48_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR48_FV_LONG() != null) {
							    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR48_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR48_QFHA() != null) {
							    cell1.setCellValue(record.getR48_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW48--------- R49
							row = sheet.getRow(48);

							cell1 = row.getCell(2);
							if (record.getR49_NV_LONG() != null) {
							    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR49_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR49_FV_LONG() != null) {
							    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR49_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR49_QFHA() != null) {
							    cell1.setCellValue(record.getR49_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW49--------- R50
							row = sheet.getRow(49);

							cell1 = row.getCell(2);
							if (record.getR50_NV_LONG() != null) {
							    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR50_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR50_FV_LONG() != null) {
							    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR50_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR50_QFHA() != null) {
							    cell1.setCellValue(record.getR50_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							//------ROW50--------- R51
							row = sheet.getRow(50);

							cell1 = row.getCell(2);
							if (record.getR51_NV_LONG() != null) {
							    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR51_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR51_FV_LONG() != null) {
							    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR51_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR51_QFHA() != null) {
							    cell1.setCellValue(record.getR51_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							
							
							//------ROW51--------- R52
							row = sheet.getRow(51);

							cell1 = row.getCell(2);
							if (record.getR52_NV_LONG() != null) {
							    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR52_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR52_FV_LONG() != null) {
							    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR52_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR52_QFHA() != null) {
							    cell1.setCellValue(record.getR52_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW52--------- R53
							row = sheet.getRow(52);

							cell1 = row.getCell(2);
							if (record.getR53_NV_LONG() != null) {
							    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR53_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR53_FV_LONG() != null) {
							    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR53_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR53_QFHA() != null) {
							    cell1.setCellValue(record.getR53_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW53--------- R54
							row = sheet.getRow(53);

							cell1 = row.getCell(2);
							if (record.getR54_NV_LONG() != null) {
							    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR54_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR54_FV_LONG() != null) {
							    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR54_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR54_QFHA() != null) {
							    cell1.setCellValue(record.getR54_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW54--------- R55
							row = sheet.getRow(54);

							cell1 = row.getCell(2);
							if (record.getR55_NV_LONG() != null) {
							    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR55_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR55_FV_LONG() != null) {
							    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR55_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR55_QFHA() != null) {
							    cell1.setCellValue(record.getR55_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

}
	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
} else {
	
}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

return out.toByteArray();
}


}
			public byte[] getExcelM_TBSARCHIVAL(String filename,String reportId, String fromdate, String todate, String currency, String dtltype,String type,String version) throws Exception {
				logger.info("Service: Starting Excel generation process in memory.");
				System.out.println(type);
				System.out.println(version);
				if (type.equals("ARCHIVAL") & version != null) {
					
				}
				/*
				 * if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            List<M_TBS_Archival_Summary_Entity> T1Master = 
            		BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
				 */
				List<M_TBS_Archival_Summary_Entity> dataList =BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),version) ;

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_TBS report. Returning empty result.");
					return new byte[0];
				}

				String templateDir = env.getProperty("output.exportpathtemp");
				String templateFileName = filename;
				System.out.println(filename);
				Path templatePath = Paths.get(templateDir, templateFileName);
				System.out.println(templatePath);
				
				logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

				if (!Files.exists(templatePath)) {
					// This specific exception will be caught by the controller.
					throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
				}
				if (!Files.isReadable(templatePath)) {
					// A specific exception for permission errors.
					throw new SecurityException(
							"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
				}

				// This try-with-resources block is perfect. It guarantees all resources are
				// closed automatically.
				try (InputStream templateInputStream = Files.newInputStream(templatePath);
						Workbook workbook = WorkbookFactory.create(templateInputStream);
						ByteArrayOutputStream out = new ByteArrayOutputStream()) {

					Sheet sheet = workbook.getSheetAt(0);

					// --- Style Definitions ---
					CreationHelper createHelper = workbook.getCreationHelper();

					CellStyle dateStyle = workbook.createCellStyle();
					dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
					dateStyle.setBorderBottom(BorderStyle.THIN);
					dateStyle.setBorderTop(BorderStyle.THIN);
					dateStyle.setBorderLeft(BorderStyle.THIN);
					dateStyle.setBorderRight(BorderStyle.THIN);

					CellStyle textStyle = workbook.createCellStyle();
					textStyle.setBorderBottom(BorderStyle.THIN);
					textStyle.setBorderTop(BorderStyle.THIN);
					textStyle.setBorderLeft(BorderStyle.THIN);
					textStyle.setBorderRight(BorderStyle.THIN);
					
					// Create the font
					Font font = workbook.createFont();
					font.setFontHeightInPoints((short)8); // size 8
					font.setFontName("Arial");    

					CellStyle numberStyle = workbook.createCellStyle();
					//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
					numberStyle.setBorderBottom(BorderStyle.THIN);
					numberStyle.setBorderTop(BorderStyle.THIN);
					numberStyle.setBorderLeft(BorderStyle.THIN);
					numberStyle.setBorderRight(BorderStyle.THIN);
					numberStyle.setFont(font);
					// --- End of Style Definitions ---

					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
					int startRow = 10;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_TBS_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber="+startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
/*
 * 	private BigDecimal R11_NV_LONG;
			private BigDecimal R11_NV_SHORT;
			private BigDecimal R11_FV_LONG;
			private BigDecimal R11_FV_SHORT;
			private BigDecimal R11_QFHA;
 */
							//row11
							// Column C 
							//row=sheet.getRow(11);

							Cell cell1 = row.getCell(2);
							if (record.getR11_NV_LONG() != null) {
								cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR11_NV_SHORT() != null) {
								cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

						    cell1 = row.getCell(4);
							if (record.getR11_FV_LONG() != null) {
								cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR11_FV_SHORT() != null) {
								cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR11_QFHA() != null) {
								cell1.setCellValue(record.getR11_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
//------ROW11---------						
							row=sheet.getRow(11);
						    cell1 = row.getCell(2);
							if (record.getR12_NV_LONG() != null) {
								cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR12_NV_SHORT() != null) {
								cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR12_FV_LONG() != null) {
								cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR12_FV_SHORT() != null) {
								cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR12_QFHA() != null) {
								cell1.setCellValue(record.getR12_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							//------ROW12---------						
							row=sheet.getRow(12);
						    cell1 = row.getCell(2);
							if (record.getR13_NV_LONG() != null) {
								cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR13_NV_SHORT() != null) {
								cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR13_FV_LONG() != null) {
								cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR13_FV_SHORT() != null) {
								cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR13_QFHA() != null) {
								cell1.setCellValue(record.getR13_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	

							//------ROW13---------
							row = sheet.getRow(13);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR14_NV_LONG() != null) {
							    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR14_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR14_FV_LONG() != null) {
							    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR14_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR14_QFHA() != null) {
							    cell1.setCellValue(record.getR14_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW14---------
							row = sheet.getRow(14);

							cell1 = row.getCell(2);
							if (record.getR15_NV_LONG() != null) {
							    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR15_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR15_FV_LONG() != null) {
							    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR15_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR15_QFHA() != null) {
							    cell1.setCellValue(record.getR15_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW15---------
							row = sheet.getRow(15);

							cell1 = row.getCell(2);
							if (record.getR16_NV_LONG() != null) {
							    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR16_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR16_FV_LONG() != null) {
							    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR16_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR16_QFHA() != null) {
							    cell1.setCellValue(record.getR16_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW16---------
							row = sheet.getRow(16);

							cell1 = row.getCell(2);
							if (record.getR17_NV_LONG() != null) {
							    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR17_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR17_FV_LONG() != null) {
							    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR17_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR17_QFHA() != null) {
							    cell1.setCellValue(record.getR17_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW17---------
							row = sheet.getRow(17);

							cell1 = row.getCell(2);
							if (record.getR18_NV_LONG() != null) {
							    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR18_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR18_FV_LONG() != null) {
							    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR18_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR18_QFHA() != null) {
							    cell1.setCellValue(record.getR18_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW18---------
							row = sheet.getRow(18);

							cell1 = row.getCell(2);
							if (record.getR19_NV_LONG() != null) {
							    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR19_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR19_FV_LONG() != null) {
							    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR19_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR19_QFHA() != null) {
							    cell1.setCellValue(record.getR19_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW19---------
							row = sheet.getRow(19);

							cell1 = row.getCell(2);
							if (record.getR20_NV_LONG() != null) {
							    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR20_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR20_FV_LONG() != null) {
							    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR20_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR20_QFHA() != null) {
							    cell1.setCellValue(record.getR20_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW20---------
							row = sheet.getRow(20);

							cell1 = row.getCell(2);
							if (record.getR21_NV_LONG() != null) {
							    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR21_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR21_FV_LONG() != null) {
							    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR21_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR21_QFHA() != null) {
							    cell1.setCellValue(record.getR21_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW21---------
							row = sheet.getRow(21);

							cell1 = row.getCell(2);
							if (record.getR22_NV_LONG() != null) {
							    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR22_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR22_FV_LONG() != null) {
							    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR22_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR22_QFHA() != null) {
							    cell1.setCellValue(record.getR22_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW22---------
							row = sheet.getRow(22);

							cell1 = row.getCell(2);
							if (record.getR23_NV_LONG() != null) {
							    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR23_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR23_FV_LONG() != null) {
							    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR23_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR23_QFHA() != null) {
							    cell1.setCellValue(record.getR23_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW23---------
							row = sheet.getRow(23);

							cell1 = row.getCell(2);
							if (record.getR24_NV_LONG() != null) {
							    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR24_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR24_FV_LONG() != null) {
							    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR24_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR24_QFHA() != null) {
							    cell1.setCellValue(record.getR24_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW24---------
							row = sheet.getRow(24);

							cell1 = row.getCell(2);
							if (record.getR25_NV_LONG() != null) {
							    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR25_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR25_FV_LONG() != null) {
							    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR25_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR25_QFHA() != null) {
							    cell1.setCellValue(record.getR25_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW25---------
							row = sheet.getRow(25);

							cell1 = row.getCell(2);
							if (record.getR26_NV_LONG() != null) {
							    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR26_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR26_FV_LONG() != null) {
							    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR26_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR26_QFHA() != null) {
							    cell1.setCellValue(record.getR26_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW26---------
							row = sheet.getRow(26);

							cell1 = row.getCell(2);
							if (record.getR27_NV_LONG() != null) {
							    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR27_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR27_FV_LONG() != null) {
							    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR27_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR27_QFHA() != null) {
							    cell1.setCellValue(record.getR27_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW27---------
							row = sheet.getRow(27);

							cell1 = row.getCell(2);
							if (record.getR28_NV_LONG() != null) {
							    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR28_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR28_FV_LONG() != null) {
							    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR28_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR28_QFHA() != null) {
							    cell1.setCellValue(record.getR28_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW28---------
							row = sheet.getRow(28);

							cell1 = row.getCell(2);
							if (record.getR29_NV_LONG() != null) {
							    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR29_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR29_FV_LONG() != null) {
							    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR29_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR29_QFHA() != null) {
							    cell1.setCellValue(record.getR29_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW29---------
							row = sheet.getRow(29);

							cell1 = row.getCell(2);
							if (record.getR30_NV_LONG() != null) {
							    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR30_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR30_FV_LONG() != null) {
							    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR30_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR30_QFHA() != null) {
							    cell1.setCellValue(record.getR30_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW30--------- R31
							row = sheet.getRow(30);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR31_NV_LONG() != null) {
							    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR31_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR31_FV_LONG() != null) {
							    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR31_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR31_QFHA() != null) {
							    cell1.setCellValue(record.getR31_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW31--------- R32
							row = sheet.getRow(31);

							cell1 = row.getCell(2);
							if (record.getR32_NV_LONG() != null) {
							    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR32_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR32_FV_LONG() != null) {
							    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR32_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR32_QFHA() != null) {
							    cell1.setCellValue(record.getR32_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW32--------- R33
							row = sheet.getRow(32);

							cell1 = row.getCell(2);
							if (record.getR33_NV_LONG() != null) {
							    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR33_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR33_FV_LONG() != null) {
							    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR33_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR33_QFHA() != null) {
							    cell1.setCellValue(record.getR33_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW33--------- R34
							row = sheet.getRow(33);

							cell1 = row.getCell(2);
							if (record.getR34_NV_LONG() != null) {
							    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR34_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR34_FV_LONG() != null) {
							    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR34_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR34_QFHA() != null) {
							    cell1.setCellValue(record.getR34_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW34--------- R35
							row = sheet.getRow(34);

							cell1 = row.getCell(2);
							if (record.getR35_NV_LONG() != null) {
							    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR35_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR35_FV_LONG() != null) {
							    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR35_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR35_QFHA() != null) {
							    cell1.setCellValue(record.getR35_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW35--------- R36
							row = sheet.getRow(35);

							cell1 = row.getCell(2);
							if (record.getR36_NV_LONG() != null) {
							    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR36_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR36_FV_LONG() != null) {
							    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR36_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR36_QFHA() != null) {
							    cell1.setCellValue(record.getR36_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW36--------- R37
							row = sheet.getRow(36);

							cell1 = row.getCell(2);
							if (record.getR37_NV_LONG() != null) {
							    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR37_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR37_FV_LONG() != null) {
							    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR37_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR37_QFHA() != null) {
							    cell1.setCellValue(record.getR37_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW37--------- R38
							row = sheet.getRow(37);

							cell1 = row.getCell(2);
							if (record.getR38_NV_LONG() != null) {
							    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR38_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR38_FV_LONG() != null) {
							    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR38_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR38_QFHA() != null) {
							    cell1.setCellValue(record.getR38_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW38--------- R39
							row = sheet.getRow(38);

							cell1 = row.getCell(2);
							if (record.getR39_NV_LONG() != null) {
							    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR39_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR39_FV_LONG() != null) {
							    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR39_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR39_QFHA() != null) {
							    cell1.setCellValue(record.getR39_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW39--------- R40
							row = sheet.getRow(39);

							cell1 = row.getCell(2);
							if (record.getR40_NV_LONG() != null) {
							    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR40_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR40_FV_LONG() != null) {
							    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR40_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR40_QFHA() != null) {
							    cell1.setCellValue(record.getR40_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							//------ROW40--------- R41
							row = sheet.getRow(40);

							cell1 = row.getCell(2);
							if (record.getR41_NV_LONG() != null) {
							    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR41_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR41_FV_LONG() != null) {
							    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR41_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR41_QFHA() != null) {
							    cell1.setCellValue(record.getR41_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW41--------- R42
							row = sheet.getRow(41);

							cell1 = row.getCell(2);
							if (record.getR42_NV_LONG() != null) {
							    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR42_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR42_FV_LONG() != null) {
							    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR42_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR42_QFHA() != null) {
							    cell1.setCellValue(record.getR42_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW42--------- R43
							row = sheet.getRow(42);

							cell1 = row.getCell(2);
							if (record.getR43_NV_LONG() != null) {
							    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR43_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR43_FV_LONG() != null) {
							    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR43_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR43_QFHA() != null) {
							    cell1.setCellValue(record.getR43_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW43--------- R44
							row = sheet.getRow(43);

							cell1 = row.getCell(2);
							if (record.getR44_NV_LONG() != null) {
							    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR44_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR44_FV_LONG() != null) {
							    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR44_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR44_QFHA() != null) {
							    cell1.setCellValue(record.getR44_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW44--------- R45
							row = sheet.getRow(44);

							cell1 = row.getCell(2);
							if (record.getR45_NV_LONG() != null) {
							    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR45_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR45_FV_LONG() != null) {
							    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR45_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR45_QFHA() != null) {
							    cell1.setCellValue(record.getR45_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW45--------- R46
							row = sheet.getRow(45);

							cell1 = row.getCell(2);
							if (record.getR46_NV_LONG() != null) {
							    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR46_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR46_FV_LONG() != null) {
							    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR46_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR46_QFHA() != null) {
							    cell1.setCellValue(record.getR46_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW46--------- R47
							row = sheet.getRow(46);

							cell1 = row.getCell(2);
							if (record.getR47_NV_LONG() != null) {
							    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR47_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR47_FV_LONG() != null) {
							    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR47_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR47_QFHA() != null) {
							    cell1.setCellValue(record.getR47_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW47--------- R48
							row = sheet.getRow(47);

							cell1 = row.getCell(2);
							if (record.getR48_NV_LONG() != null) {
							    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR48_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR48_FV_LONG() != null) {
							    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR48_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR48_QFHA() != null) {
							    cell1.setCellValue(record.getR48_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW48--------- R49
							row = sheet.getRow(48);

							cell1 = row.getCell(2);
							if (record.getR49_NV_LONG() != null) {
							    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR49_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR49_FV_LONG() != null) {
							    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR49_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR49_QFHA() != null) {
							    cell1.setCellValue(record.getR49_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW49--------- R50
							row = sheet.getRow(49);

							cell1 = row.getCell(2);
							if (record.getR50_NV_LONG() != null) {
							    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR50_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR50_FV_LONG() != null) {
							    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR50_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR50_QFHA() != null) {
							    cell1.setCellValue(record.getR50_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							//------ROW50--------- R51
							row = sheet.getRow(50);

							cell1 = row.getCell(2);
							if (record.getR51_NV_LONG() != null) {
							    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR51_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR51_FV_LONG() != null) {
							    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR51_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR51_QFHA() != null) {
							    cell1.setCellValue(record.getR51_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							
							
							//------ROW51--------- R52
							row = sheet.getRow(51);

							cell1 = row.getCell(2);
							if (record.getR52_NV_LONG() != null) {
							    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR52_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR52_FV_LONG() != null) {
							    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR52_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR52_QFHA() != null) {
							    cell1.setCellValue(record.getR52_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW52--------- R53
							row = sheet.getRow(52);

							cell1 = row.getCell(2);
							if (record.getR53_NV_LONG() != null) {
							    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR53_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR53_FV_LONG() != null) {
							    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR53_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR53_QFHA() != null) {
							    cell1.setCellValue(record.getR53_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW53--------- R54
							row = sheet.getRow(53);

							cell1 = row.getCell(2);
							if (record.getR54_NV_LONG() != null) {
							    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR54_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR54_FV_LONG() != null) {
							    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR54_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR54_QFHA() != null) {
							    cell1.setCellValue(record.getR54_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW54--------- R55
							row = sheet.getRow(54);

							cell1 = row.getCell(2);
							if (record.getR55_NV_LONG() != null) {
							    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR55_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR55_FV_LONG() != null) {
							    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR55_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR55_QFHA() != null) {
							    cell1.setCellValue(record.getR55_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

}
	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
} else {
	
}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

return out.toByteArray();
}


}
			/// Downloaded for Archival & Resub
			public byte[] BRRS_M_tbsResubExcel(String filename, String reportId, String fromdate,
		        String todate, String currency, String dtltype,
		        String type, String version) throws Exception {

		    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		    if (type.equals("RESUB") & version != null) {
		       
		    }

				List<M_TBS_Archival_Summary_Entity> dataList =BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),version) ;

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_TBS report. Returning empty result.");
					return new byte[0];
				}

				String templateDir = env.getProperty("output.exportpathtemp");
				String templateFileName = filename;
				System.out.println(filename);
				Path templatePath = Paths.get(templateDir, templateFileName);
				System.out.println(templatePath);
				
				logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

				if (!Files.exists(templatePath)) {
					// This specific exception will be caught by the controller.
					throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
				}
				if (!Files.isReadable(templatePath)) {
					// A specific exception for permission errors.
					throw new SecurityException(
							"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
				}

				// This try-with-resources block is perfect. It guarantees all resources are
				// closed automatically.
				try (InputStream templateInputStream = Files.newInputStream(templatePath);
						Workbook workbook = WorkbookFactory.create(templateInputStream);
						ByteArrayOutputStream out = new ByteArrayOutputStream()) {

					Sheet sheet = workbook.getSheetAt(0);

					// --- Style Definitions ---
					CreationHelper createHelper = workbook.getCreationHelper();

					CellStyle dateStyle = workbook.createCellStyle();
					dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
					dateStyle.setBorderBottom(BorderStyle.THIN);
					dateStyle.setBorderTop(BorderStyle.THIN);
					dateStyle.setBorderLeft(BorderStyle.THIN);
					dateStyle.setBorderRight(BorderStyle.THIN);

					CellStyle textStyle = workbook.createCellStyle();
					textStyle.setBorderBottom(BorderStyle.THIN);
					textStyle.setBorderTop(BorderStyle.THIN);
					textStyle.setBorderLeft(BorderStyle.THIN);
					textStyle.setBorderRight(BorderStyle.THIN);
					
					// Create the font
					Font font = workbook.createFont();
					font.setFontHeightInPoints((short)8); // size 8
					font.setFontName("Arial");    

					CellStyle numberStyle = workbook.createCellStyle();
					//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
					numberStyle.setBorderBottom(BorderStyle.THIN);
					numberStyle.setBorderTop(BorderStyle.THIN);
					numberStyle.setBorderLeft(BorderStyle.THIN);
					numberStyle.setBorderRight(BorderStyle.THIN);
					numberStyle.setFont(font);
					// --- End of Style Definitions ---

					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
					int startRow = 10;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_TBS_Archival_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber="+startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
/*
 * 	private BigDecimal R11_NV_LONG;
			private BigDecimal R11_NV_SHORT;
			private BigDecimal R11_FV_LONG;
			private BigDecimal R11_FV_SHORT;
			private BigDecimal R11_QFHA;
 */
							//row11
							// Column C 
							//row=sheet.getRow(11);

							Cell cell1 = row.getCell(2);
							if (record.getR11_NV_LONG() != null) {
								cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR11_NV_SHORT() != null) {
								cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

						    cell1 = row.getCell(4);
							if (record.getR11_FV_LONG() != null) {
								cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR11_FV_SHORT() != null) {
								cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR11_QFHA() != null) {
								cell1.setCellValue(record.getR11_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
//------ROW11---------						
							row=sheet.getRow(11);
						    cell1 = row.getCell(2);
							if (record.getR12_NV_LONG() != null) {
								cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR12_NV_SHORT() != null) {
								cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR12_FV_LONG() != null) {
								cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR12_FV_SHORT() != null) {
								cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR12_QFHA() != null) {
								cell1.setCellValue(record.getR12_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							//------ROW12---------						
							row=sheet.getRow(12);
						    cell1 = row.getCell(2);
							if (record.getR13_NV_LONG() != null) {
								cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR13_NV_SHORT() != null) {
								cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR13_FV_LONG() != null) {
								cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR13_FV_SHORT() != null) {
								cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR13_QFHA() != null) {
								cell1.setCellValue(record.getR13_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	

							//------ROW13---------
							row = sheet.getRow(13);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR14_NV_LONG() != null) {
							    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR14_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR14_FV_LONG() != null) {
							    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR14_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR14_QFHA() != null) {
							    cell1.setCellValue(record.getR14_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW14---------
							row = sheet.getRow(14);

							cell1 = row.getCell(2);
							if (record.getR15_NV_LONG() != null) {
							    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR15_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR15_FV_LONG() != null) {
							    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR15_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR15_QFHA() != null) {
							    cell1.setCellValue(record.getR15_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW15---------
							row = sheet.getRow(15);

							cell1 = row.getCell(2);
							if (record.getR16_NV_LONG() != null) {
							    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR16_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR16_FV_LONG() != null) {
							    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR16_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR16_QFHA() != null) {
							    cell1.setCellValue(record.getR16_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW16---------
							row = sheet.getRow(16);

							cell1 = row.getCell(2);
							if (record.getR17_NV_LONG() != null) {
							    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR17_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR17_FV_LONG() != null) {
							    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR17_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR17_QFHA() != null) {
							    cell1.setCellValue(record.getR17_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW17---------
							row = sheet.getRow(17);

							cell1 = row.getCell(2);
							if (record.getR18_NV_LONG() != null) {
							    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR18_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR18_FV_LONG() != null) {
							    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR18_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR18_QFHA() != null) {
							    cell1.setCellValue(record.getR18_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW18---------
							row = sheet.getRow(18);

							cell1 = row.getCell(2);
							if (record.getR19_NV_LONG() != null) {
							    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR19_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR19_FV_LONG() != null) {
							    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR19_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR19_QFHA() != null) {
							    cell1.setCellValue(record.getR19_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW19---------
							row = sheet.getRow(19);

							cell1 = row.getCell(2);
							if (record.getR20_NV_LONG() != null) {
							    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR20_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR20_FV_LONG() != null) {
							    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR20_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR20_QFHA() != null) {
							    cell1.setCellValue(record.getR20_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW20---------
							row = sheet.getRow(20);

							cell1 = row.getCell(2);
							if (record.getR21_NV_LONG() != null) {
							    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR21_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR21_FV_LONG() != null) {
							    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR21_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR21_QFHA() != null) {
							    cell1.setCellValue(record.getR21_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW21---------
							row = sheet.getRow(21);

							cell1 = row.getCell(2);
							if (record.getR22_NV_LONG() != null) {
							    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR22_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR22_FV_LONG() != null) {
							    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR22_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR22_QFHA() != null) {
							    cell1.setCellValue(record.getR22_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW22---------
							row = sheet.getRow(22);

							cell1 = row.getCell(2);
							if (record.getR23_NV_LONG() != null) {
							    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR23_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR23_FV_LONG() != null) {
							    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR23_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR23_QFHA() != null) {
							    cell1.setCellValue(record.getR23_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW23---------
							row = sheet.getRow(23);

							cell1 = row.getCell(2);
							if (record.getR24_NV_LONG() != null) {
							    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR24_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR24_FV_LONG() != null) {
							    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR24_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR24_QFHA() != null) {
							    cell1.setCellValue(record.getR24_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW24---------
							row = sheet.getRow(24);

							cell1 = row.getCell(2);
							if (record.getR25_NV_LONG() != null) {
							    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR25_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR25_FV_LONG() != null) {
							    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR25_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR25_QFHA() != null) {
							    cell1.setCellValue(record.getR25_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW25---------
							row = sheet.getRow(25);

							cell1 = row.getCell(2);
							if (record.getR26_NV_LONG() != null) {
							    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR26_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR26_FV_LONG() != null) {
							    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR26_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR26_QFHA() != null) {
							    cell1.setCellValue(record.getR26_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW26---------
							row = sheet.getRow(26);

							cell1 = row.getCell(2);
							if (record.getR27_NV_LONG() != null) {
							    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR27_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR27_FV_LONG() != null) {
							    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR27_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR27_QFHA() != null) {
							    cell1.setCellValue(record.getR27_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW27---------
							row = sheet.getRow(27);

							cell1 = row.getCell(2);
							if (record.getR28_NV_LONG() != null) {
							    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR28_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR28_FV_LONG() != null) {
							    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR28_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR28_QFHA() != null) {
							    cell1.setCellValue(record.getR28_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW28---------
							row = sheet.getRow(28);

							cell1 = row.getCell(2);
							if (record.getR29_NV_LONG() != null) {
							    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR29_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR29_FV_LONG() != null) {
							    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR29_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR29_QFHA() != null) {
							    cell1.setCellValue(record.getR29_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW29---------
							row = sheet.getRow(29);

							cell1 = row.getCell(2);
							if (record.getR30_NV_LONG() != null) {
							    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR30_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR30_FV_LONG() != null) {
							    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR30_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR30_QFHA() != null) {
							    cell1.setCellValue(record.getR30_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW30--------- R31
							row = sheet.getRow(30);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR31_NV_LONG() != null) {
							    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR31_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR31_FV_LONG() != null) {
							    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR31_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR31_QFHA() != null) {
							    cell1.setCellValue(record.getR31_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW31--------- R32
							row = sheet.getRow(31);

							cell1 = row.getCell(2);
							if (record.getR32_NV_LONG() != null) {
							    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR32_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR32_FV_LONG() != null) {
							    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR32_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR32_QFHA() != null) {
							    cell1.setCellValue(record.getR32_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW32--------- R33
							row = sheet.getRow(32);

							cell1 = row.getCell(2);
							if (record.getR33_NV_LONG() != null) {
							    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR33_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR33_FV_LONG() != null) {
							    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR33_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR33_QFHA() != null) {
							    cell1.setCellValue(record.getR33_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW33--------- R34
							row = sheet.getRow(33);

							cell1 = row.getCell(2);
							if (record.getR34_NV_LONG() != null) {
							    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR34_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR34_FV_LONG() != null) {
							    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR34_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR34_QFHA() != null) {
							    cell1.setCellValue(record.getR34_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW34--------- R35
							row = sheet.getRow(34);

							cell1 = row.getCell(2);
							if (record.getR35_NV_LONG() != null) {
							    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR35_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR35_FV_LONG() != null) {
							    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR35_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR35_QFHA() != null) {
							    cell1.setCellValue(record.getR35_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW35--------- R36
							row = sheet.getRow(35);

							cell1 = row.getCell(2);
							if (record.getR36_NV_LONG() != null) {
							    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR36_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR36_FV_LONG() != null) {
							    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR36_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR36_QFHA() != null) {
							    cell1.setCellValue(record.getR36_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW36--------- R37
							row = sheet.getRow(36);

							cell1 = row.getCell(2);
							if (record.getR37_NV_LONG() != null) {
							    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR37_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR37_FV_LONG() != null) {
							    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR37_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR37_QFHA() != null) {
							    cell1.setCellValue(record.getR37_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW37--------- R38
							row = sheet.getRow(37);

							cell1 = row.getCell(2);
							if (record.getR38_NV_LONG() != null) {
							    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR38_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR38_FV_LONG() != null) {
							    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR38_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR38_QFHA() != null) {
							    cell1.setCellValue(record.getR38_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW38--------- R39
							row = sheet.getRow(38);

							cell1 = row.getCell(2);
							if (record.getR39_NV_LONG() != null) {
							    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR39_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR39_FV_LONG() != null) {
							    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR39_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR39_QFHA() != null) {
							    cell1.setCellValue(record.getR39_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW39--------- R40
							row = sheet.getRow(39);

							cell1 = row.getCell(2);
							if (record.getR40_NV_LONG() != null) {
							    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR40_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR40_FV_LONG() != null) {
							    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR40_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR40_QFHA() != null) {
							    cell1.setCellValue(record.getR40_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							//------ROW40--------- R41
							row = sheet.getRow(40);

							cell1 = row.getCell(2);
							if (record.getR41_NV_LONG() != null) {
							    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR41_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR41_FV_LONG() != null) {
							    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR41_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR41_QFHA() != null) {
							    cell1.setCellValue(record.getR41_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW41--------- R42
							row = sheet.getRow(41);

							cell1 = row.getCell(2);
							if (record.getR42_NV_LONG() != null) {
							    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR42_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR42_FV_LONG() != null) {
							    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR42_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR42_QFHA() != null) {
							    cell1.setCellValue(record.getR42_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW42--------- R43
							row = sheet.getRow(42);

							cell1 = row.getCell(2);
							if (record.getR43_NV_LONG() != null) {
							    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR43_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR43_FV_LONG() != null) {
							    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR43_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR43_QFHA() != null) {
							    cell1.setCellValue(record.getR43_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW43--------- R44
							row = sheet.getRow(43);

							cell1 = row.getCell(2);
							if (record.getR44_NV_LONG() != null) {
							    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR44_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR44_FV_LONG() != null) {
							    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR44_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR44_QFHA() != null) {
							    cell1.setCellValue(record.getR44_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW44--------- R45
							row = sheet.getRow(44);

							cell1 = row.getCell(2);
							if (record.getR45_NV_LONG() != null) {
							    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR45_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR45_FV_LONG() != null) {
							    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR45_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR45_QFHA() != null) {
							    cell1.setCellValue(record.getR45_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW45--------- R46
							row = sheet.getRow(45);

							cell1 = row.getCell(2);
							if (record.getR46_NV_LONG() != null) {
							    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR46_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR46_FV_LONG() != null) {
							    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR46_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR46_QFHA() != null) {
							    cell1.setCellValue(record.getR46_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW46--------- R47
							row = sheet.getRow(46);

							cell1 = row.getCell(2);
							if (record.getR47_NV_LONG() != null) {
							    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR47_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR47_FV_LONG() != null) {
							    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR47_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR47_QFHA() != null) {
							    cell1.setCellValue(record.getR47_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW47--------- R48
							row = sheet.getRow(47);

							cell1 = row.getCell(2);
							if (record.getR48_NV_LONG() != null) {
							    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR48_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR48_FV_LONG() != null) {
							    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR48_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR48_QFHA() != null) {
							    cell1.setCellValue(record.getR48_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW48--------- R49
							row = sheet.getRow(48);

							cell1 = row.getCell(2);
							if (record.getR49_NV_LONG() != null) {
							    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR49_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR49_FV_LONG() != null) {
							    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR49_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR49_QFHA() != null) {
							    cell1.setCellValue(record.getR49_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW49--------- R50
							row = sheet.getRow(49);

							cell1 = row.getCell(2);
							if (record.getR50_NV_LONG() != null) {
							    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR50_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR50_FV_LONG() != null) {
							    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR50_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR50_QFHA() != null) {
							    cell1.setCellValue(record.getR50_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							//------ROW50--------- R51
							row = sheet.getRow(50);

							cell1 = row.getCell(2);
							if (record.getR51_NV_LONG() != null) {
							    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR51_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR51_FV_LONG() != null) {
							    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR51_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR51_QFHA() != null) {
							    cell1.setCellValue(record.getR51_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							
							
							//------ROW51--------- R52
							row = sheet.getRow(51);

							cell1 = row.getCell(2);
							if (record.getR52_NV_LONG() != null) {
							    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR52_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR52_FV_LONG() != null) {
							    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR52_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR52_QFHA() != null) {
							    cell1.setCellValue(record.getR52_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW52--------- R53
							row = sheet.getRow(52);

							cell1 = row.getCell(2);
							if (record.getR53_NV_LONG() != null) {
							    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR53_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR53_FV_LONG() != null) {
							    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR53_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR53_QFHA() != null) {
							    cell1.setCellValue(record.getR53_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW53--------- R54
							row = sheet.getRow(53);

							cell1 = row.getCell(2);
							if (record.getR54_NV_LONG() != null) {
							    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR54_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR54_FV_LONG() != null) {
							    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR54_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR54_QFHA() != null) {
							    cell1.setCellValue(record.getR54_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW54--------- R55
							row = sheet.getRow(54);

							cell1 = row.getCell(2);
							if (record.getR55_NV_LONG() != null) {
							    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR55_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR55_FV_LONG() != null) {
							    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR55_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR55_QFHA() != null) {
							    cell1.setCellValue(record.getR55_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

}
	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
} else {
	
}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

return out.toByteArray();
}


}
	
}