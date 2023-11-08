package com.example.he_thong_thong_minh.service;

import com.example.he_thong_thong_minh.entity.Label;

import java.util.List;

public interface labelService {

    void saveLabels(List<Label> labels);

    List<Label> getAllLabelById(Long sampleId);

    Label saveNewLabel(Label lb);
}
