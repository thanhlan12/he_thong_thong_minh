package com.example.he_thong_thong_minh.repository;

import com.example.he_thong_thong_minh.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    @Query("SELECT l FROM Label l JOIN l.samples s WHERE s.id = :sampleId")
    List<Label> getAllLabelById(@Param("sampleId") Long sampleId);


}
