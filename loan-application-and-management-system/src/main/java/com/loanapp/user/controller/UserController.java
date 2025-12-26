package com.loanapp.user.controller;

import com.loanapp.user.dto.RegisterUserRequestDto;
import com.loanapp.user.dto.UserResponseDto;
import com.loanapp.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @RequestBody RegisterUserRequestDto request) {

        return new ResponseEntity<>(
                userService.registerUser(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }
}
