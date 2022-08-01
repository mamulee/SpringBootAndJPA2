package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    // @Autowired
    private final MemberRepository memberRepository;


    // 생성자 Injection
    // @Autowired 생성자가 하나일 경우 자동으로 @Autowired 를 해준다
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    // 위 생성자는 @RequiredArgsConstructor을 통해 자동으로 생성됨.

    /*
    Setter Injection
    단점 : Run Time 시점에 중도 변경된 경우 안 됨
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
     */

    /**
     * 회원 가입
     */
    @Transactional //(readOnly = false)
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 동시에 가입할 경우 중복 확인이 안 돼서 DB 쪽에서 UNIQUE 제약 조건을 추가로 거는 게 좋다.
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    // 회원 전체 조회
    //@Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    //@Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
