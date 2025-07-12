package com.example.shop_mall_back.admin.order.domain;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "customer_claim")
public class ClaimManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", foreignKey = @ForeignKey(name = "fk_return_id"))
    private OrderReturn orderReturn;

    //isApproved 없애고 상태를 교환요청, 취소요청, 환불 요청, 교환승인, 취소승인, 환불승인
    //교환거부, 취소거부, 환불거부 등 9가지 상태로 바꾸기, 건호님 작업 완료되면 수정
}
