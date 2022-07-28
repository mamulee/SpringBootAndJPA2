package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepositoryEx memberRepository;

    @Test
    @Transactional // Test에 있으면 정상 동작 후 바로 Roll back
    @Rollback(false)
    public void $NAMES() throws Exception {
        //given
        MemberEx member = new MemberEx();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        MemberEx findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        System.out.println("findeMember == member : " + (findMember == member));
        // 같은 영속속 컨텍스트에서 아이디 값(식별자)이 같으면 같은 엔티티로 인식.
    }
}