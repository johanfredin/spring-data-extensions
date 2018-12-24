package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.LoginMemberBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static com.github.johanfredin.springdataextensions.web.MvcErrors.*;
import static com.github.johanfredin.springdataextensions.web.MvcUtils.redirectTo;

@Controller
@RequestMapping(Page.LOGIN)
public class LoginController extends ControllerBase<MemberService> {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String index(LoginMemberBean loginMemberBean) {
        return Page.LOGIN_FW;
    }

    @PostMapping
    public String handleSubmit(@Valid LoginMemberBean loginMemberBean, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return Page.LOGIN_FW;
        }

        // Check that there is a member in db with that username
        Member member = getMainService().findOneByUserName(loginMemberBean.getUserName());
        if (member != null) {
            // Check that the password is correct for that member, if so redirect to index page
            if (member.getPassword().equals(loginMemberBean.getPassword())) {
                return redirectTo("index", member);
            }

            // If password is incorrect reload the page
            model.addAttribute(NO_USR_WITH_PW, true);
            model.addAttribute(NO_USR_WITH_PW_MSG_KEY, getNoMatchPasswordMessage(loginMemberBean.getUserName()));
            return Page.LOGIN_FW;
        }

        // If user is not found reload the page
        model.addAttribute("userNotFound", true);
        model.addAttribute("userNotFoundMsg", "No user found with that username");
        return Page.LOGIN_FW;
    }

    @Override
    public MemberService getMainService() {
        return memberService;
    }

    @Override
    public void setMainService(MemberService memberService) {
        this.memberService = memberService;
    }

}
