package com.example.he_thong_thong_minh.entity;

import com.example.he_thong_thong_minh.dto.labelDTO;
import com.example.he_thong_thong_minh.dto.sampleDTO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Label implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String value;

    public Label(Long id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Label() {
    }

    @ManyToMany
    @JoinTable(
            name = "sample_label",
            joinColumns = @JoinColumn(name = "label_id"),
            inverseJoinColumns = @JoinColumn(name = "sample_id"))
    private List<Sample> samples;

    public labelDTO toDTO() {
        return new labelDTO(name, value);
    }

}

