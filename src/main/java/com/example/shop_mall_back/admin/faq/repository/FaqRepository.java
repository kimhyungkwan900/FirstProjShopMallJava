package com.example.shop_mall_back.admin.faq.repository;

import com.example.shop_mall_back.admin.faq.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {



}
