package com.example.shop_mall_back.common.domain.member;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "member_address")
@Builder
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class MemberAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @Column(length = 20, nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    @Lob    //LongObject 사용시 MySQL 은 TEXT 로 데이터를 받음
    private String note;

    @Column(name = "is_default")
    private boolean isDefault;

    public static MemberAddress addAddress(Member member, String zipcode, String address, String addressDetail, boolean isDefault, String note) {
        return MemberAddress.builder()
                .member(member)
                .zipcode(zipcode)
                .address(address)
                .addressDetail(addressDetail)
                .note(note)
                .isDefault(isDefault)
                .build();
    }

    public void updateAddress(String zipcode, String address, String detail, boolean isDefault, String note) {
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = detail;
        this.note = note;

        if (isDefault) {
            markAsDefault();
        } else {
            unmarkAsDefault();
        }
    }

    public void markAsDefault() {
        if (!this.isDefault) {
            this.isDefault = true;
        }
    }

    public void unmarkAsDefault() {
        if (this.isDefault) {
            this.isDefault = false;
        }
    }
}
