package com.bornfire.brrs.services;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
}