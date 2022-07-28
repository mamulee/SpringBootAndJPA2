package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepositoryEx {

    @PersistenceContext
    private EntityManager em;

    public Long save(MemberEx member){
        em.persist(member);
        return member.getId();
    }

    public MemberEx find(Long id){
        return em.find(MemberEx.class, id);
    }
}
