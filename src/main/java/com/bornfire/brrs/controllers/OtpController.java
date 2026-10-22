package com.bornfire.brrs.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.LoginCompletionService;
import com.bornfire.brrs.services.OtpService;

@Controller
public class OtpController {

	
	@Autowired
    private OtpService otpService;

    @Autowired
    private LoginCompletionService loginCompletionService;

    @Autowired
    private UserProfileRep userProfileRep;

    @GetMapping("/otp")
    public String otpPage(HttpSession session) {
        if (session.getAttribute("PRE_AUTH") == null) return "redirect:/login";
        return "otp";
    }

    @PostMapping("/otp/verify")
    public String verify(@RequestParam String otp, HttpSession session,
                         HttpServletRequest req, Model model) {
        Authentication pre = (Authentication) session.getAttribute("PRE_AUTH");
        if (pre == null) return "redirect:/login";

        if (otpService.verify(pre.getName(), otp)) {
            session.removeAttribute("PRE_AUTH");

            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(pre);
            SecurityContextHolder.setContext(ctx);
            session.setAttribute("SPRING_SECURITY_CONTEXT", ctx);

            loginCompletionService.completeLogin(req, pre.getName());  // session attributes + audit
            return "redirect:/Dashboard";
        }
        model.addAttribute("error", "Invalid or expired OTP");
        return "otp";
    }

    @PostMapping("/otp/resend")
    public String resend(HttpSession session) {
        Authentication pre = (Authentication) session.getAttribute("PRE_AUTH");
        if (pre == null) return "redirect:/login";

        String userId = pre.getName();
        String email = userProfileRep.findById(userId).get().getEmail_id();
        otpService.sendOtp(userId, email);
        return "redirect:/otp";
    }

}
