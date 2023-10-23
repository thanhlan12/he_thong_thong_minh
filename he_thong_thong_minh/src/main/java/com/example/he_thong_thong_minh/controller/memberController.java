package com.example.he_thong_thong_minh.controller;

import com.example.he_thong_thong_minh.dto.sampleDTO;
import com.example.he_thong_thong_minh.entity.Sample;
import com.example.he_thong_thong_minh.entity.member;
import com.example.he_thong_thong_minh.service.memberService;
import com.example.he_thong_thong_minh.service.sampleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("")
@Log4j2
public class memberController {
    @Autowired
    private sampleService sampleServ;

    @Autowired
    private memberService membs;



    @GetMapping("/samples")
    public String listSamples(Model model) {

        List<sampleDTO> sampleDTOs = new ArrayList<>();
        List<Sample> samples = sampleServ.getAllSample();
        for (Sample s : samples) {
            sampleDTOs.add(s.toDTO());
        }
        model.addAttribute("samples", sampleDTOs);
        return "sampleManager";
    }

    @GetMapping("/images/new")
    public String newImage(Model model) {
        return "upload_form";
    }

    @PostMapping("/images/upload")
    public String uploadImage(Model model, @RequestParam("file") MultipartFile file,   HttpServletRequest request) {
        String message = "";

        try {
            sampleServ.save(file);
            Sample sp = new Sample();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(memberController.class, "getImage", file.getOriginalFilename()).build().toString();





            String jwtCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("jwt"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);


            member Mem = membs.getMemberByUsername(jwtCookie);
            sp.setMember(Mem);
            sp.setImage(url);
            sampleServ.saveSample(sp);
            message = "Uploaded the image successfully: " + file.getOriginalFilename();
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            model.addAttribute("message", message);
        }

        return "upload_form";
    }


    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = sampleServ.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @Autowired
    private memberService mem;

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerMember(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam("idCard") String idCard,
                                            @RequestParam("name") String name){
        member memb = new member();
        memb.setUsername(username);
        memb.setPassword(password);
        memb.setIdCard(idCard);
        memb.setName(name);
        mem.save(memb);
        return ResponseEntity.ok("Member registered successfully");
    }






    @PostMapping("/af_login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse response) {
        // Kiểm tra thông tin đăng nhập
        UserDetails userDetails = membs.loadUserByUsername(username);
        if (membs.checkPassword(password, userDetails.getPassword())) {
            // Tạo JWT token từ id của thành viên
            String jwt = userDetails.getUsername();

            // Lưu JWT vào cookie
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Chuyển hướng đến trang manager.html
            return "redirect:/samples";
        } else {
            return "redirect:/login.html?error";
        }
    }

    @PostMapping("/searchMemByIdcard")
    public String searchMemberByIdCard(@RequestParam("idCardNumber") String idCardNumber, Model model) {
        List<member> members = membs.findByIdCard(idCardNumber);

        model.addAttribute("members", members);

        return "member";
    }

    @PostMapping("/searchSampleByIdCard")
    public String searchSampleByIdCard(@RequestParam("idCardNumber") String memberIdCard, Model model) {
        List<Sample> Sps = sampleServ.findByMember_IdCard(memberIdCard);
        List<sampleDTO> sampleDTOs = new ArrayList<>();
        for (Sample s : Sps) {
            System.out.println(s.getImage());
            sampleDTOs.add(s.toDTO());
        }

        model.addAttribute("samples", sampleDTOs);
        return "sampleManager";


    }





}
