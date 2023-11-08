package com.example.he_thong_thong_minh.service.impl;

import com.example.he_thong_thong_minh.entity.Label;
import com.example.he_thong_thong_minh.repository.LabelRepository;
import com.example.he_thong_thong_minh.service.labelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class labelServiceImpl implements labelService {
    @Autowired
    private LabelRepository labelRepository;

    @Override
    public void saveLabels(List<Label> labels) {
        for (Label lb : labels) {
            System.out.println(lb.getValue());
            Label existingLabel = labelRepository.findById(lb.getId()).orElse(null);
            if (existingLabel != null) {
                existingLabel.setName(lb.getName());
                existingLabel.setValue(lb.getValue());
                labelRepository.save(existingLabel);
            }
        }
    }


    @Override
    public List<Label> getAllLabelById(Long sampleId){
        return  labelRepository.getAllLabelById(sampleId);
    }

    @Override
    public Label saveNewLabel(Label lb){
        return  labelRepository.save(lb);
    }
}
