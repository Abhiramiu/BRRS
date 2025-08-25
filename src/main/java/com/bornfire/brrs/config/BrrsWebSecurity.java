package com.bornfire.brrs.config;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.brrs.entities.UserProfile;
import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.AuditService;
import com.bornfire.brrs.services.LoginServices;

@Configuration
@EnableWebSecurity
public class BrrsWebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	LoginServices loginServices;

	@Autowired 
	AuditService auditService;
	
	private final Integer SESSION_TIMEOUT_IN_SECONDS = 650000;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/webjars/**", "/images/**", "/login*", "/freezeColumn/**","favicon.ico").permitAll()
				.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
				.failureHandler(xbrlAuthFailHandle()).successHandler(xbrlAuthSuccessHandle())
				.usernameParameter("userid").and().logout().permitAll().and()
				.logout().logoutSuccessHandler(xbrlLogoutSuccessHandler()).permitAll()
				.and().sessionManagement().maximumSessions(1)
				.maxSessionsPreventsLogin(false);
				

		http.csrf().disable();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean

	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider ap = new DaoAuthenticationProvider() {

			@Override
			@Transactional
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String userid = authentication.getName();
				String password = authentication.getCredentials().toString();

				Optional<UserProfile> up = userProfileRep.findById(userid);

				try {

					if (up.isPresent()) {
						UserProfile usr = up.get();

						if (!usr.isAccountNonExpired()) {

							throw new AccountExpiredException("Account Expired");

						} else if (!usr.isCredentialsNonExpired()) {

							throw new CredentialsExpiredException("Credentials Expired");

						} else if (!usr.isAccountNonLocked()) {

							throw new LockedException("Account Locked");

						} else if (!usr.isEnabled()) {

							throw new DisabledException("Account Disabled");

						} else if (!usr.isLoginAllowed()) {

							throw new LockedException("Login Not Allowed");

						} else if (!PasswordEncryption.validatePassword(password, usr.getPassword())) {

							logger.info("Passing Userid :" + userid);

							Session hs = sessionFactory.getCurrentSession();
							Transaction tr = hs.getTransaction();
							hs.createQuery(
									"update UserProfile a set a.no_of_attmp=nvl(a.no_of_attmp,0)+1, a.user_locked_flg=decode(nvl(a.no_of_attmp,0)+1,'3','Y','N'), a.login_status=decode(nvl(a.no_of_attmp,0)+1,'3','Inactive','Active') where userid=?1")
									.setParameter(1, userid).executeUpdate();
							tr.commit();
							hs.close();
							throw new BadCredentialsException("Authentication failed");

						} else {
							
							return new UsernamePasswordAuthenticationToken(userid, password, Collections.emptyList());

						}

					} else {

						throw new UsernameNotFoundException("Invalid User Name");
					}

				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					e.printStackTrace();
					authentication.setAuthenticated(false);
				}
				return authentication;

			}

			@Override
			public boolean supports(Class<?> aClass) {
				return aClass.equals(UsernamePasswordAuthenticationToken.class);
			}

		};

		ap.setHideUserNotFoundExceptions(false);
		ap.setUserDetailsService(userDetailsService());

		return ap;
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {

		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

				Optional<UserProfile> up = userProfileRep.findByusername(username);

				if (up.isPresent()) {
					return up.get();
				} else {
					return new UserProfile();
				}

			}

		};
	}

	@Bean
	public AuthenticationFailureHandler xbrlAuthFailHandle() {
		return new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {

				response.setStatus(HttpStatus.UNAUTHORIZED.value());
	//			logger.info(exception.getMessage());
				response.sendRedirect("login?error=" + exception.getMessage());

			}

		};

	}

	@Bean
	public AuthenticationSuccessHandler xbrlAuthSuccessHandle() {
		return new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				
				Optional<UserProfile> up = userProfileRep.findById(authentication.getName());
				UserProfile user = up.get();
				user.setNo_of_attmp(0);
				user.setUser_locked_flg("N");
				userProfileRep.save(user);
				
				loginServices.SessionLogging("LOGIN","M1",request.getSession().getId(),user.getUserid(),request.getRemoteAddr(),
						"ACTIVE");
			
				request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
				request.getSession().setAttribute("USERID", user.getUserid());
				request.getSession().setAttribute("USERNAME", user.getUsername());
				request.getSession().setAttribute("ROLEID", user.getRole_id());
				request.getSession().setAttribute("DOMAINID", user.getDomain_id());
				request.getSession().setAttribute("PERMISSIONS", user.getPermissions());
				request.getSession().setAttribute("WORKCLASS", user.getWork_class());
				request.getSession().setAttribute("MOBILENUMBER", user.getMob_number());
				request.getSession().setAttribute("ROLEDESC", user.getRole_desc());
				request.getSession().setAttribute("ACCESSCODE", user.getAcct_access_code());
				request.getSession().setAttribute("BRANCHCODE", user.getBranch_code());
				request.getSession().setAttribute("BRANCHNAME", user.getBranch_name());
				
				auditService.createBusinessAudit(user.getUserid() , "Login",null , null, "XBRL_USER_PROFILE_TABLE");				
				
				response.sendRedirect("Dashboard");
			}

		};

	}
	
	@Bean
	public LogoutSuccessHandler xbrlLogoutSuccessHandler() {
		
		return new LogoutSuccessHandler() {

			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				
				
				response.sendRedirect("login?logout");
			
			}
		
		
	};
}
}