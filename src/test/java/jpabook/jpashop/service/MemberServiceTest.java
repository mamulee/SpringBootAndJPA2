package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest // 스프링 부트를 띄운 상태로 테스트, 없으면 Autowired 실패
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    // @Autowired EntityManager em;

    @Test
    // @Rollback(value = false) Transactional은 기본으로 Rollback이 되어 (Test기 때문에 반복 수행으로 DB에 남지 않는 것이 맞기 때문에) insert 문을 볼 수 없기에 false로 봄.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        // em.flush(); // 이걸 쓰면 Rollback은 하지만, insert 문을 확인할 수 있음.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // try / catch 문 생략 가능
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e){
//            return;
//        }
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다.");
    }

}