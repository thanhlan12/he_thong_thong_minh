package com.example.he_thong_thong_minh.service.impl;


import com.example.he_thong_thong_minh.entity.member;
import com.example.he_thong_thong_minh.repository.MemberRepository;
import com.example.he_thong_thong_minh.service.memberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class memberServiceImpl implements memberService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void save(member mem) {
        mem.setPassword(passwordEncoder.encode(mem.getPassword()));
        memberRepository.save(mem);
    }

    @Override
    public member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public List<member> findByIdCard(String idCard) {
        return memberRepository.findByIdCard(idCard);
    }

//    @Override
//    public List<member> getMemberByIdCard(String idCard) {
//        return Collections.singletonList(memberRepository.findByIdCardNumber(idCard));
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        member mem = memberRepository.findByUsername(username);
        if (mem == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                mem.getUsername(),
                mem.getPassword(),
                new ArrayList<>()
        );
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

