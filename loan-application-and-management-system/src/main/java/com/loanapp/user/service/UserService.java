
package com.loanapp.user.service;

import java.util.List;

import com.loanapp.user.dto.RegisterUserRequestDto;
import com.loanapp.user.dto.UserResponseDto;
import com.loanapp.user.exception.UserAlreadyExistsException;
import com.loanapp.user.exception.UserNotFoundException;

public interface UserService {

    UserResponseDto registerUser(RegisterUserRequestDto request) throws UserAlreadyExistsException;

    UserResponseDto getUserById(Long userId) throws UserNotFoundException;

	List<UserResponseDto> getAllUsers();

	void deleteUser(Long userId) throws UserNotFoundException;
}