package com.bornfire.brrs.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bornfire.brrs.entities.UserProfile;
import com.bornfire.brrs.entities.UserProfileRep;

@Service
public class LoginCompletionService {

    @Autowired
    private UserProfileRep userProfileRep;

    @Autowired
    private LoginServices loginServices;

    @Autowired
    private AuditService auditService;

    private static final int SESSION_TIMEOUT_IN_SECONDS = 650000;

    public void completeLogin(HttpServletRequest request, String userId) {
        UserProfile user = userProfileRep.findById(userId).get();
        user.setNo_of_attmp(0);
        user.setUser_locked_flg("N");
        userProfileRep.save(user);

        loginServices.SessionLogging("LOGIN", "M1", request.getSession().getId(),
                user.getUserid(), request.getRemoteAddr(), "ACTIVE");

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

        auditService.createBusinessAudit(user.getUserid(), "Login", null, null,
                "XBRL_USER_PROFILE_TABLE");
    }
}