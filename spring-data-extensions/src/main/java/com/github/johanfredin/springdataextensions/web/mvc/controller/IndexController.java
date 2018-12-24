package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(Page.INDEX)
public class IndexController extends ControllerBase<MemberService> {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String index(@PathVariable long memberId, Model model) {
        Member member = getMainService().getMemberWithAllChildren(memberId);
        model.addAttribute("member", member);
        return Page.INDEX_FW;
    }

    @Override
    public MemberService getMainService() {
        return this.memberService;
    }

    @Override
    public void setMainService(MemberService mainService) {
        this.memberService = mainService;
    }

}
