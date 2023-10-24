package com.example.he_thong_thong_minh.repository;


import com.example.he_thong_thong_minh.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
   // List<Sample> findByMember_Id(Long memberId);

    @Query("SELECT e FROM Sample e WHERE e.Member.idCard = :memberIdCard")
    List<Sample> findByMember_IdCard(@Param("memberIdCard") String memberIdCard);

    Sample getSampleById(Long id);
}




