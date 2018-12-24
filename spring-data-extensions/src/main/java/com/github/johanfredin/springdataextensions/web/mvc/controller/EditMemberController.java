package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.EditMemberBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static com.github.johanfredin.springdataextensions.web.MvcErrors.*;

@Controller
@RequestMapping(Page.EDIT_MEMBER)
public class EditMemberController extends ControllerBase<MemberService> implements ControllerExtensions<Member, EditMemberBean> {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String index(@PathVariable long memberId, Model model) {
        Member member = getPersistedEntity(memberId);
        populateModel(model, new EditMemberBean(member));
        return Page.EDIT_MEMBER_FW;
    }

    @Override
    @PostMapping
    public String handleSubmit(@Valid EditMemberBean bean, Model model, BindingResult bindingResult) {

        // First check regular validation
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.info("Error " + error.getCode() + " " + error.getDefaultMessage() + " " + error.getObjectName());
            }
            populateModel(model, bean);
            return Page.EDIT_MEMBER_FW;
        }

        Member member = bean.getEntity();


        // Now ensure new data (if any) is unique
        boolean isNoOtherEntityWithEmail = getMainService().isNoOtherEntityWithEmail(member.getId(), member.getEmail());
        boolean isNoOtherEntityWithUserName = getMainService().isNoOtherEntityWithUserName(member.getId(), member.getUserName());

        boolean isNewPasswordCorrect = true;
        if (bean.isPasswordAltered()) {
            isNewPasswordCorrect = bean.isRepeatPasswordCorrect(member.getPassword());
        }

        if (MvcUtils.isAnyErrors(isNoOtherEntityWithEmail, isNoOtherEntityWithUserName, isNewPasswordCorrect)) {
            if (!isNoOtherEntityWithEmail) {
                model.addAttribute(EMAIL_EXISTS, true).addAttribute(EMAIL_EXISTS_MSG_KEY, getEmailMessage(member));
            }
            if (!isNoOtherEntityWithUserName) {
                model.addAttribute(USR_NAME_EXISTS, true).addAttribute(USR_NAME_EXISTS_MSG_KEY, USR_NAME_EXISTS_MSG_VAL);
            }
            if (!isNewPasswordCorrect) {
                model.addAttribute(NEW_PW_INCORRECT, true).addAttribute(NEW_PW_INCORRECT_MSG_KEY, NEW_PW_INCORRECT_MSG_VAL);
            }
            return Page.EDIT_MEMBER_FW;
        }


        // No errors found, now update the member
        if (isNewPasswordCorrect && bean.isPasswordAltered()) {
            member.setPassword(bean.getNewPassword());
        }

        getMainService().save(member, true);
        return MvcUtils.redirectTo(Page.INDEX_FW, member);
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
    public Member getPersistedEntity(long id) {
        return getMainService().getMemberWithAllChildren(id);
    }

    @Override
    public void populateModel(Model model, EditMemberBean bean) {
        model.addAttribute("editMemberBean", bean);
    }

}
