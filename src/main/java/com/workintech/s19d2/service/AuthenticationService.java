package com.workintech.s19d2.service;

import com.workintech.s19d2.entity.Member;
import com.workintech.s19d2.entity.Role;
import com.workintech.s19d2.repository.MemberRepository;
import com.workintech.s19d2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member register(String email,String password,boolean isAdmin){
        Optional<Member> optionalMember=memberRepository.findByEmail(email);
        if(optionalMember.isPresent()){
            throw new RuntimeException("User with given email already exist");
        }
        String encodedPassword=passwordEncoder.encode(password);

        List<Role> roleList=new ArrayList<>();
        if(isAdmin){
            addRoleAdmin(roleList);
            isAdmin=true;
        }else{
            addRoleUser(roleList);
            isAdmin=false;
        }


        Member member=new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);
        member.setRoles(roleList);
        member.setAdmin(isAdmin);

        return memberRepository.save(member);
    }

    private void addRoleUser(List<Role> roleList) {
        Optional<Role> roleUser=roleRepository.findByAuthority(ROLE_USER);
        if(!roleUser.isPresent()){
            Role roleUserEntity=new Role();
            roleUserEntity.setAuthority(ROLE_USER);
            roleRepository.save(roleUserEntity);
            roleList.add(roleUserEntity);
        }else{
            roleList.add(roleUser.get());
        }
    }
    private void addRoleAdmin(List<Role> roleList) {
        Optional<Role> roleAdmin=roleRepository.findByAuthority(ROLE_ADMIN);
        if(!roleAdmin.isPresent()){
            Role roleUserEntity=new Role();
            roleUserEntity.setAuthority(ROLE_ADMIN);
            roleRepository.save(roleUserEntity);
            roleList.add(roleUserEntity);
        }else{
            roleList.add(roleAdmin.get());
        }
    }
}
