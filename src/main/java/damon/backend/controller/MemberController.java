package damon.backend.controller;

import damon.backend.model.Member;
import damon.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("member/add/{name}")
    public String addMember(@PathVariable("name") String name) {
        Member member = new Member(name);
        memberRepository.save(member);
        return member.getName();
    }

    @GetMapping("/members")
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }
}
