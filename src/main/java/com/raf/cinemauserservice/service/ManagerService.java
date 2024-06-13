package com.raf.cinemauserservice.service;

import com.raf.cinemauserservice.dto.ManagerCreateDto;
import com.raf.cinemauserservice.dto.UserCreateDto;
import com.raf.cinemauserservice.dto.UserDto;

public interface ManagerService {

    UserDto addManager(ManagerCreateDto managerData);

}
