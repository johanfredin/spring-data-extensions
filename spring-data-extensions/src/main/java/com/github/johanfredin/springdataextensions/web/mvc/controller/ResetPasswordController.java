package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.ResetMemberPasswordBean;
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
import static com.github.johanfredin.springdataextensions.web.MvcUtils.*;

@Controller
@RequestMapping(Page.RESET_PASSWORD)
public class ResetPasswordController extends ControllerBase<MemberService> {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String index(ResetMemberPasswordBean resetMemberPasswordBean) {
        return Page.RESET_PASSWORD_FW;
    }

    @PostMapping
    public String handleSubmit(@Valid ResetMemberPasswordBean resetMemberPasswordBean, BindingResult bindingResult, Model model) {
        String password = resetMemberPasswordBean.getPassword();
        String repeatPassword = resetMemberPasswordBean.getRepeatPassword();

        boolean isRepeatPasswordCorrect = !isRepeatPasswordIncorrect(password, repeatPassword);

        if (isAnyErrors(isRepeatPasswordCorrect, !bindingResult.hasErrors())) {
            // Check regular validation errors
            if (bindingResult.hasErrors()) {
                return Page.RESET_PASSWORD_FW;
            }

            // Check that the new password entered twice is correct
            if (!isRepeatPasswordCorrect) {
                model.addAttribute(NEW_PW_INCORRECT, true).addAttribute(NEW_PW_INCORRECT_MSG_KEY, NEW_PW_INCORRECT_MSG_VAL);
            }
            return Page.RESET_PASSWORD_FW;
        }

        // Find a member in db with that username and update it
        Member member = getMainService().findOneByUserName(resetMemberPasswordBean.getUserName());
        if (member != null) {

            // Update the member if all good
            member.setPassword(password);
            getMainService().save(member);
            return redirectTo("index", member);

        }

        // Tell the user that the member was not found with that username
        model.addAttribute(USR_NAME_EXISTS, true);
        model.addAttribute(USR_NAME_EXISTS_MSG_KEY, USR_NAME_EXISTS_MSG_VAL);
        return Page.RESET_PASSWORD_FW;
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
