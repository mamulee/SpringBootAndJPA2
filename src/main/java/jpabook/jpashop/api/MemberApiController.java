package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // Entity를 파라미터로 받으면 나중에 Entity에 변경이 있을 시 큰 장애가 날 수 있다. (API 스펙이 변경돼서)
    // 그러므로 별도의 Obejct Class (DTO)를 생성해 대신 받는 형식을 API 만들 시 권장한다 (실무에서 절대 금지).
    // 새로 만든 객체를 통해 Entity가 외부에 노출되는 것을 방지하고, API 스펙을 한눈에 알아보기도 쉽다.
    // 예를 들어, Member 내 필드 중 address가 API를 통해 파라미터로 받아와서 채워지는지 내부에서 따로 나중에 로직을 통해 채우는지 알기 어렵지만,
    // CreateMemberRequest를 확인하면, name만 받아오는 것을 쉽게 파악할 수 있다.
    // 또, @NotEmpty를 Entity가 아닌 DTO에 적용함으로써 API 별로 같은 필드지만 조건을 다르게 정할 수 있다.
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
