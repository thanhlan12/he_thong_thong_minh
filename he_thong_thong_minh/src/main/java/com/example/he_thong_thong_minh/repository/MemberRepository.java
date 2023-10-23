package com.example.he_thong_thong_minh.repository;

import com.example.he_thong_thong_minh.entity.member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<member, Integer> {
    member findByUsername(String username);
    List<member> findByIdCard(String idCardNumber);
}
