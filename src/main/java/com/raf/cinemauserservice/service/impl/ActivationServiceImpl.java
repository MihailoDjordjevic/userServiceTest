package com.raf.cinemauserservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raf.cinemauserservice.domain.NonActivatedUser;
import com.raf.cinemauserservice.domain.User;
import com.raf.cinemauserservice.dto.UserCreateDto;
import com.raf.cinemauserservice.dto.UserDto;
import com.raf.cinemauserservice.exception.AccesDeniedException;
import com.raf.cinemauserservice.listener.MessageHelper;
import com.raf.cinemauserservice.mapper.UserMapper;
import com.raf.cinemauserservice.repository.NonActivatedUsersRepository;
import com.raf.cinemauserservice.repository.UserRepository;
import com.raf.cinemauserservice.service.ActivationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ActivationServiceImpl implements ActivationService {

    private NonActivatedUsersRepository nonActivatedUsersRepository;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private JmsTemplate jmsTemplate;
    private String sendRegistrationMail;
    private MessageHelper messageHelper;

    public ActivationServiceImpl(NonActivatedUsersRepository nonActivatedUsersRepository, UserRepository userRepository, UserMapper userMapper, JmsTemplate jmsTemplate,MessageHelper messageHelper, @Value("${destination.sendRegistrationMail}") String sendRegistrationMail) {
        this.nonActivatedUsersRepository = nonActivatedUsersRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jmsTemplate = jmsTemplate;
        this.sendRegistrationMail = sendRegistrationMail;
        this.messageHelper = messageHelper;
    }

    @Override
    public UserCreateDto addNonActivatedUser(UserCreateDto userCreateDto) {
        NonActivatedUser nonActivatedUser = userMapper.userCreateDtoToNonActivedUser(userCreateDto);
        nonActivatedUser = nonActivatedUsersRepository.save(nonActivatedUser);
        jmsTemplate.convertAndSend(sendRegistrationMail, messageHelper.createTextMessage(nonActivatedUser));
        return userCreateDto;
    }

    @Override
    public void removeFromUnactivated(Long id) {
        nonActivatedUsersRepository.deleteById(id);
    }

    @Override
    public UserDto add(Long id, String username) {

        Optional<NonActivatedUser> nonActivatedUser = nonActivatedUsersRepository.findById(id);

        UserCreateDto userCreateDto;
        User user;

        if (nonActivatedUser.isPresent()){
            userCreateDto = userMapper.nonActivedUserToUserCreateDto(nonActivatedUser.get());
            user = userMapper.userCreateDtoToUser(userCreateDto);
            userRepository.save(user);

            removeFromUnactivated(id);

            return userMapper.userToUserDto(user);
        } else throw new AccesDeniedException("Wrong link");
    }
}
