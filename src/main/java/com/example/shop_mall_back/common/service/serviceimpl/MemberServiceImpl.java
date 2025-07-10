package com.example.shop_mall_back.common.service.serviceimpl;

import com.example.shop_mall_back.common.constant.Age;
import com.example.shop_mall_back.common.constant.Gender;
import com.example.shop_mall_back.common.constant.Grade;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.dto.MemberDTO;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.dto.PasswordChangeDTO;
import com.example.shop_mall_back.common.repository.MemberProfileRepository;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //<editor-fold desc="회원가입">
    @Override
    public Long signUp(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {

        // 중복 가입 검사
        validateDuplicateMember(memberFormDTO.getEmail());

        // 멤버 객체 생성
        Member member = Member.create(memberFormDTO.getUserId(), memberFormDTO.getUserPassword(), memberFormDTO.getEmail(), memberFormDTO.getPhoneNumber(), passwordEncoder);

        // 멤버 기본 프로필 생성
        MemberProfile memberProfile = MemberProfile.builder()
                .member(member)
                .name("신규회원")
                .role(Role.MEMBER)
                .grade(Grade.NORMAL)
                .gender(Gender.UNKNOWN)
                .age(Age.UNKNOWN)
                .nickname(null)
                .isMembership(false)
                .profileImgUrl(null)
                .delivAddress(null)
                .build();

        // 저장 / member 의 경우 생략가능
        memberRepository.save(member);
        memberProfileRepository.save(memberProfile);

        log.info("member 저장 완료 - ID: {}", member.getId());
        log.info("memberProfile 저장 완료 - ID: {}", memberProfile.getId());

        // 저장된 member 객체 id 반환
        return member.getId();
    }
//</editor-fold>

//    <editor-fold desc="회원 정보 검색">
    @Override
    public Optional<Member> findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    @Override
    public Member findByEmail(String email) {

        Member member = memberRepository.findByEmail(email).orElse(null);

        return member;
    }

    @Override
    public Member findByIdOrThrow(Long id) {
        return memberRepository.findById(id).orElseThrow(()->new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
    }

    @Override
    public MemberDTO getMemberDTOByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        return entityToDTOMember(member);
    }

    @Override
    public Role getRoleByMember(Member member) {
        MemberProfile profile = memberProfileRepository.findByMemberId(member.getId());

        if (profile == null) {
            throw new IllegalStateException("사용자 프로필이 존재하지 않습니다.");
        }

        return profile.getRole();
    }
//</editor-fold>

//    <editor-fold desc="비밀번호 재설정">
    @Override
    public void passWordUpdate(PasswordChangeDTO passwordChangeDTO, PasswordEncoder passwordEncoder) {
        // Update 할 member 객체 검색
        Member member = findByIdOrThrow(passwordChangeDTO.getId());

        // 새로 입력받은 password 를 newPass 에 저장
        String newPass = passwordEncoder.encode(passwordChangeDTO.getUserPassword());

        // password 변경
        member.changePassword(newPass);

        // 변경사항 저장
        memberRepository.save(member);
    }
//    </editor-fold>

//<editor-fold desc="계정 활성화 비활성화">
// 맴버의 가입과 탈퇴 boolean 값으로 관리
    @Override
    public void deActivateMember(Long id) {
        Member member = findByIdOrThrow(id);

        member.deactivateMember();
    }

    @Override
    public void activateMember(Long id) {
        Member member = findByIdOrThrow(id);

        member.activateMember();
    }
//</editor-fold>

//    <editor-fold desc="기타 편의성 메서드">
    // 중복 검사
    private void validateDuplicateMember(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public Member authenticate(String userId, String rawPassword) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("ID 혹은 비밀번호를 다시 확인해주세요"));

        if (!passwordEncoder.matches(rawPassword, member.getUserPassword())) {
            throw new IllegalArgumentException("ID 혹은 비밀번호를 다시 확인해주세요");
        }

        return member;
    }
//    </editor-fold>

}
