package com.bornfire.brrs.services;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		
			if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_TBS_Archival_Summary_Entity> T1Master = new ArrayList<M_TBS_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = BRRS_M_TBS_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else{
		
		 
		List<M_TBS_Summary_Entity> T1Master = new ArrayList<M_TBS_Summary_Entity>();
		try {
			Date d1 = dateformat.parse(todate);
			
			 T1Master=BRRS_M_TBS_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mv.addObject("reportsummary", T1Master);
}
		mv.setViewName("BRRS/M_TBS");		
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
}
	public List<Object> getM_TBSArchival() {
		List<Object> M_TBSArchivallist = new ArrayList<>();
		try {
			M_TBSArchivallist = BRRS_M_TBS_Archival_Summary_Repo.getM_TBSarchival();
			System.out.println("countser" + M_TBSArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_TBS Archival data: " + e.getMessage());
			e.printStackTrace();
		}
		return M_TBSArchivallist;
	}
	public void updateReport(M_TBS_Summary_Entity Entity) {
	    System.out.println("Report Date: " + Entity.getREPORT_DATE());

	    M_TBS_Summary_Entity existing = BRRS_M_TBS_Summary_Repo.findById(Entity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + Entity.getREPORT_DATE()));
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
}