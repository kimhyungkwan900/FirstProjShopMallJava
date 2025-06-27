package com.example.shop_mall_back.common.service;

import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.dto.MemberFormDTO;
import com.example.shop_mall_back.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

//    <editor-fold desc="회원가입">
    @Override
    public Long signUp(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {

        // 중복 가입 검사
        validateDuplicateMember(memberFormDTO.getEmail());

        // 멤버 객체 생성
        Member member = new Member();
        member.create(memberFormDTO, passwordEncoder);

        // 저장
        memberRepository.save(member);

        // 저장된 member 객체 id 반환
        return member.getId();
    }
//</editor-fold>

//    <editor-fold desc="회원 정보 검색">
    @Override
    public MemberFormDTO getMemberForm(String email) {
        // memberEmail 로 member 검색
        Member member = memberRepository.findByEmail(email);

        // 해당 email을 가진 dto 반환 (id / pw / email / 전화번호)
        MemberFormDTO dto = entityToDTO(member);
        return dto;
    }
//</editor-fold>

//    <editor-fold desc="비밀번호 재설정">
    @Override
    public void memberFormUpdate(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) {
        // Update 할 member 객체 검색
        Member member = memberRepository.findById(memberFormDTO.getId()).orElseThrow(IllegalArgumentException::new);

        // 새로 입력받은 password 를 newPass 에 저장
        String newPass = passwordEncoder.encode(memberFormDTO.getUser_password());

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
        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        member.deActivateMember();
    }

    @Override
    public void activateMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        member.activateMember();
    }
//</editor-fold>

//    <editor-fold desc="중복 가입 검사">
    private void validateDuplicateMember(String email) {
        if (memberRepository.findByEmail(email) != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
//    </editor-fold>

}
