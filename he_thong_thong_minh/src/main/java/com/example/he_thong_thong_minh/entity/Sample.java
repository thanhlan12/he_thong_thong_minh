package com.example.he_thong_thong_minh.entity;


import com.example.he_thong_thong_minh.dto.sampleDTO;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Sample implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String image;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private member Member;

    @ManyToMany
    @JoinTable(name = "sample_label",
    joinColumns = @JoinColumn(name = "sample_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<Label> lables;




    public Sample() {
    }

    public sampleDTO toDTO() {
        return new sampleDTO(id,image);
    }
}
