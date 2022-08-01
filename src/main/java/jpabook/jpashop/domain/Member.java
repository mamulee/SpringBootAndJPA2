package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded //@Embeddable 둘 중 하나만 있으면 된다.
    private Address address;

    // @JsonIgnore // Member 조회 시 제외시키고 싶을 때, 하지만 Entity에 화면과 관련한 기능이 추가되어 버린다.
    // 양방향 의존관계가 걸리면서 API 수정 시 곤란해진다.
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
