//package com.loanapp.controller;
//
//import com.loanapp.dto.*;
//import com.loanapp.security.service.CustomUserDetailsService;
//import com.loanapp.security.util.JwtUtil;
//import org.springframework.security.authentication.*;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final JwtUtil jwtUtil;
//    private final CustomUserDetailsService userDetailsService;
//
//    public AuthController(AuthenticationManager authenticationManager,
//                          JwtUtil jwtUtil,
//                          CustomUserDetailsService userDetailsService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest request) {
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword())
//        );
//
//        var userDetails =
//                userDetailsService.loadUserByUsername(request.getUsername());
//
//        return new LoginResponse(jwtUtil.generateToken(userDetails));
//    }
//}