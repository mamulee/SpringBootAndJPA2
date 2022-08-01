package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // Entity를 직접 노출 시 다양한 문제점이 생긴다. (필기 참고 "회원 수정 API - 조회 V1)
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(/*collect.size(),*/ collect);
    }

    // Json Array로 응답이 나간다면 유연성이 떨어지기 때문에 Result 객체로 한 번 감싸준다.
    @Data
    @AllArgsConstructor
    static class Result<T> {
        // private int count; 이런 것도 추가 가능
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

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

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
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
