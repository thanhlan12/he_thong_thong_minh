package com.example.he_thong_thong_minh.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class sampleDTO {
    private Long id;
    private  String image;


    public sampleDTO(Long id,  String image) {
        this.id = id;
        this.image= image;
    }


}
