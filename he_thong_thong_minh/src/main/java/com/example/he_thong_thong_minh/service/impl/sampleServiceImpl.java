package com.example.he_thong_thong_minh.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import com.example.he_thong_thong_minh.entity.Sample;
import com.example.he_thong_thong_minh.repository.SampleRepository;
import com.example.he_thong_thong_minh.service.sampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;

@Service
public class sampleServiceImpl implements sampleService {

    @Autowired
    private SampleRepository sampleRepo;

    @Autowired
    public sampleServiceImpl(SampleRepository sampleRepository) {
        this.sampleRepo = sampleRepository;
    }
    @Override
    public List<Sample> findByMember_IdCard(String memberIdCard) {
        return sampleRepo.findByMember_IdCard(memberIdCard);
    }


    @Override
    public List<Sample> getAllSample() {
        return sampleRepo.findAll();
    }



    @Override
    public Sample saveSample(Sample sample) {
        return sampleRepo.save(sample);
    }

    @Override
    public Sample updateSample(Sample sample) {
        return sampleRepo.save(sample);
    }

    @Override
    public void deleteSample(Long id) {
        sampleRepo.deleteById(id);
    }


    private final Path root = Paths.get("./uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
