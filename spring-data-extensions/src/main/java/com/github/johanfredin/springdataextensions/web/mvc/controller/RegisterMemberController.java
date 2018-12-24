package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.RegisterMemberBean;
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
@RequestMapping(Page.REGISTER_MEMBER)
public class RegisterMemberController extends ControllerBase<MemberService> implements ControllerExtensions<Member, RegisterMemberBean> {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String index(RegisterMemberBean registerMemberBean, Model model) {
        populateModel(model, new RegisterMemberBean(new Member()));
        return Page.REGISTER_MEMBER_FW;
    }

    @PostMapping
    public String handleSubmit(@Valid RegisterMemberBean registerMemberBean, Model model, BindingResult bindingResult) {
        Member member = registerMemberBean.getMember();
        boolean isRepeatPasswordCorrect = !isRepeatPasswordIncorrect(member.getPassword(), registerMemberBean.getComparePassword());

        if (isAnyErrors(isRepeatPasswordCorrect, !bindingResult.hasErrors())) {
            if (!isRepeatPasswordCorrect) {

                log.error("Passwords: " + member.getPassword() + " and " + registerMemberBean.getComparePassword() + " does not match");
                model.addAttribute(NEW_PW_INCORRECT, true).addAttribute(NEW_PW_INCORRECT_MSG_KEY, NEW_PW_INCORRECT_MSG_VAL);
            }
            return Page.REGISTER_MEMBER_FW;
        }

        // Pass in 0 as id since member is not yet created (e.g we check entire member db)
        boolean errUsername = !getMainService().isNoOtherEntityWithUserName(0, member.getUserName());
        boolean errEmail = !getMainService().isNoOtherEntityWithEmail(0, member.getEmail());

        if (errUsername || errEmail) {
            if (errUsername) {
                model.addAttribute(USR_NAME_EXISTS, true).addAttribute(USR_NAME_EXISTS_MSG_KEY, USR_NAME_EXISTS_MSG_VAL);
            }
            if (errEmail) {
                model.addAttribute(EMAIL_EXISTS, true).addAttribute(EMAIL_EXISTS_MSG_KEY, getEmailMessage(member));
            }
            return Page.REGISTER_MEMBER_FW;
        }

        getMainService().save(member);
        return redirectTo("index", member);
    }

    @Override
    public MemberService getMainService() {
        return memberService;
    }

    @Override
    public void setMainService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public String toString() {
        return "RegisterMemberController [memberService=" + memberService + "]";
    }

    @Override
    public void populateModel(Model model, RegisterMemberBean bean) {
        model.addAttribute("regiterMemberBean", bean);
    }

    @Override
    public Member getPersistedEntity(long id) {
        return null;
    }


}
