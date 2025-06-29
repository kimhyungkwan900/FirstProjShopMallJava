package com.example.shop_mall_back.common.domain;

import com.example.shop_mall_back.common.constant.Age;
import com.example.shop_mall_back.common.constant.Gender;
import com.example.shop_mall_back.common.constant.Grade;
import com.example.shop_mall_back.common.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "member_profile")
@Builder
@Getter
@ToString(exclude = {"nickname", "profileImgUrl", "delivAddress", "name", "member"})
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)                   //1 : 1 관계 명확히 선언
    @JoinColumn(name = "member_id", nullable = false)   //외래키 설정
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_admin", length = 10, nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column( length = 10, nullable = false)
    private Grade grade;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(name = "is_membership", nullable = false)
    private boolean isMembership;

    @Column(unique = true, length = 20)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10, nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Age age;

    @Column(name = "deliv_address")
    private String delivAddress;

    public void updateProfile(String nickname, String profileImgUrl, String address) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.delivAddress = address;
    }
}
