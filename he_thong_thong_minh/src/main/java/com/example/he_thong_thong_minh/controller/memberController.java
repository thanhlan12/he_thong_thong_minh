package com.example.he_thong_thong_minh.controller;

import com.example.he_thong_thong_minh.dto.sampleDTO;
import com.example.he_thong_thong_minh.dto.labelDTO;
import com.example.he_thong_thong_minh.entity.Label;
import com.example.he_thong_thong_minh.entity.Sample;
import com.example.he_thong_thong_minh.entity.member;
import com.example.he_thong_thong_minh.repository.LabelRepository;
import com.example.he_thong_thong_minh.service.labelService;
import com.example.he_thong_thong_minh.service.memberService;
import com.example.he_thong_thong_minh.service.sampleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.util.*;


import org.springframework.web.bind.annotation.PostMapping;

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

    @Autowired
    private labelService labServ;

    @Autowired
    private LabelRepository labelRepository;



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

        return ResponseEntity.ok().body(file);
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
    public String registerMember(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("idCard") String idCard,
                                 @RequestParam("name") String name){
        member memb = new member();
        memb.setUsername(username);
        memb.setPassword(password);
        memb.setIdCard(idCard);
        memb.setName(name);
        mem.save(memb);
        return "redirect:/samples";
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


    @GetMapping("/deleteSample/{id}")
    public String deleteSample(@PathVariable("id") Long id) {
        sampleServ.deleteSample(id);
        return "redirect:/samples";
    }


    @PostMapping("/images/AdminUpload")
    public String AdminUploadImage(Model model, @RequestParam("file") MultipartFile file,
                                   @RequestParam("idCardNumber") String idCardNumber,
                                   HttpServletRequest request) {
        String message = "";

        try {
            sampleServ.save(file);
            Sample sp = new Sample();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(memberController.class, "getImage", file.getOriginalFilename()).build().toString();
            List<member> Mem = membs.findByIdCard(idCardNumber);
            sp.setMember(Mem.get(0));
            sp.setImage(url);
            sampleServ.saveSample(sp);
            message = "Uploaded the image successfully: " + file.getOriginalFilename();
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            model.addAttribute("message", message);
        }

        return "AdminUpload";
    }

    @GetMapping("/addSampleAdmin")
    public String addSampleAdmin() {
        return "AdminUpload";
    }


    @GetMapping("/editSample/{id}")
    public String viewSample(@PathVariable("id") Long id, Model model) {
        List<Label> labelDTOs = labelRepository.getAllLabelById(id);
//        List<labelDTO> labelDTOs = new ArrayList<>();
//        for (Label l : labels) {
//            System.out.println(l.getValue());
//            labelDTOs.add(l.toDTO());
//        }
        model.addAttribute("labels", labelDTOs);
        return "detailView";
    }


//    @PostMapping("/saveChange")
//    public String saveChange(@ModelAttribute("labels") List<Label> labels) {
//        for (Label l : labels) {
//            System.out.println(l.getName()+"//////////////////--------------------------////////////////////////"+ l.getValue());
//            labServ.saveNewLabel(l);
//        }
//        return "redirect:/samples";
//    }




//    @PostMapping("/saveChange")
//    public String saveLabels(@ModelAttribute("labels") List<Label> labels) {
//        System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////");
//        // Lặp qua danh sách các nhãn và lưu từng nhãn
//        for (Label label : labels) {
//            System.out.println(label.getName()+"///////////////////////////////////////////////////////////");
//            System.out.println(label.getValue()+"///////////////////////////////////////////////////////////");
//            System.out.println(label.getId()+"///////////////////////////////////////////////////////////");
//            System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////");
//            // Giả sử bạn có một phương thức dịch vụ để lưu nhãn
//            labelRepository.save(label);
//        }
//
//        // Chuyển hướng đến trang phù hợp sau khi lưu
//        return "redirect:/samples";
//    }


    @PostMapping("/saveChange")
    public String saveChange(@RequestParam(name = "id")List<String> ids,@RequestParam(name = "name")List<String> names,@RequestParam(name = "value")List<String> values) {
        log.info("Ids: {}",ids);
        log.info("name: {}",names);
        log.info("value: {}",values);

        for (int i=0;i< ids.size();i++){
            Label existingLabel = labelRepository.findById(Long.parseLong(ids.get(i))).orElse(null);
            if (existingLabel != null) {
                existingLabel.setName(names.get(i));
                existingLabel.setValue(values.get(i));
                labelRepository.save(existingLabel);
            }
        }
        return "redirect:/samples";
//        for (String key : requestParams.keySet()) {
//            System.out.println(key);
//            if (key.startsWith("name")) {
//                System.out.println("00000000000000000000000000000000000000000000000000000000000000000000000000000000000");
//                Long id = Long.parseLong(key.replace("name", ""));
//                String name = requestParams.get(key);
//                String value = requestParams.get(key.replace("name", "value"));
//                System.out.println(name+"//////////////////////////////////////////////-----------////////////////"+ value);
//                Label existingLabel = labelRepository.findById(id).orElse(null);
//                if (existingLabel != null) {
//                    existingLabel.setName(name);
//                    existingLabel.setValue(value);
//                    labelRepository.save(existingLabel);
//                }
//                return "redirect:/samples";
//            }
//        }
//        return "redirect:/samples";
    }





}
