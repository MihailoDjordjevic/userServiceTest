package com.raf.cinemauserservice.service;

import com.raf.cinemauserservice.dto.UserCreateDto;
import com.raf.cinemauserservice.dto.UserDto;

public interface ActivationService {

    UserCreateDto addNonActivatedUser(UserCreateDto userCreateDto);

    void removeFromUnactivated(Long id);

    UserDto add(Long id, String username);

}
