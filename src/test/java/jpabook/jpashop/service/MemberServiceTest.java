package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
//    @Rollback(false)
    void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("이다현");
        member.setAddress(new Address("서울", "송파구", "177894"));

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("이다현");

        Member member2 = new Member();
        member2.setName("이다현");

        //when
        memberService.join(member1);
        //then
        assertThrows(IllegalStateException.class, () ->
                memberService.join(member2));

    }
}