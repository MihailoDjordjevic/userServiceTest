package com.raf.cinemauserservice.service.impl;

import com.raf.cinemauserservice.domain.Manager;
import com.raf.cinemauserservice.domain.User;
import com.raf.cinemauserservice.dto.ManagerCreateDto;
import com.raf.cinemauserservice.dto.UserDto;
import com.raf.cinemauserservice.mapper.ManagerMapper;
import com.raf.cinemauserservice.mapper.UserMapper;
import com.raf.cinemauserservice.repository.ManagerRepository;
import com.raf.cinemauserservice.repository.UserRepository;
import com.raf.cinemauserservice.service.ManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private UserRepository userRepository;
    private ManagerRepository managerRepository;
    private UserMapper userMapper;
    private ManagerMapper managerMapper;

    public ManagerServiceImpl(UserRepository userRepository, ManagerRepository managerRepository, UserMapper userMapper, ManagerMapper managerMapper) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.userMapper = userMapper;
        this.managerMapper = managerMapper;
    }

    @Override
    public UserDto addManager(ManagerCreateDto managerCreateDto) {

        User user = userMapper.userCreateDtoToUser(managerMapper.managerCreateDtoToUserCreateDto(managerCreateDto));

        Manager manager = managerMapper.managerCreateDtoToManager(managerCreateDto, user);

        managerRepository.save(manager);
        userRepository.save(user);

        return userMapper.userToUserDto(user);
    }
}
