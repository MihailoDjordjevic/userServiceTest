package com.raf.cinemauserservice.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raf.cinemauserservice.client.reservationservice.dto.DisplayTermDto;
import com.raf.cinemauserservice.domain.BannedUsers;
import com.raf.cinemauserservice.domain.User;
import com.raf.cinemauserservice.domain.UserStatus;
import com.raf.cinemauserservice.dto.*;
import com.raf.cinemauserservice.exception.AccesDeniedException;
import com.raf.cinemauserservice.exception.NotFoundException;
import com.raf.cinemauserservice.mapper.UserMapper;
import com.raf.cinemauserservice.repository.BannedUsersRepository;
import com.raf.cinemauserservice.repository.NonActivatedUsersRepository;
import com.raf.cinemauserservice.repository.UserRepository;
import com.raf.cinemauserservice.repository.UserStatusRepository;
import com.raf.cinemauserservice.secutiry.CheckSecurity;
import com.raf.cinemauserservice.secutiry.service.TokenService;
import com.raf.cinemauserservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private UserMapper userMapper;
    private BannedUsersRepository bannedUsersRepository;
    private NonActivatedUsersRepository nonActivatedUsersRepository;
    private RestTemplate reservationServiceRestTemplate;

    public UserServiceImpl(TokenService tokenService, UserRepository userRepository, UserStatusRepository userStatusRepository, UserMapper userMapper, BannedUsersRepository bannedUsersRepository, NonActivatedUsersRepository nonActivatedUsersRepository, RestTemplate reservationServiceRestTemplate) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.userMapper = userMapper;
        this.bannedUsersRepository = bannedUsersRepository;
        this.nonActivatedUsersRepository = nonActivatedUsersRepository;
        this.reservationServiceRestTemplate = reservationServiceRestTemplate;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

 //   @Override
//    public DiscountDto findDiscount(Long id) {
//        User user = userRepository
//                .findById(id)
//                .orElseThrow(() -> new NotFoundException(String
//                        .format("User with id: %d not found.", id)));
//        List<UserStatus> userStatusList = userStatusRepository.findAll();
//        //get discount
//        Integer discount = userStatusList.stream()
//                .filter(userStatus -> userStatus.getMaxNumberOfReservations() >= user.getNumberOfReservations()
//                        && userStatus.getMinNumberOfReservations() <= user.getNumberOfReservations())
//                .findAny()
//                .get()
//                .getDiscount();
//        return new DiscountDto(discount);
//    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {

        //Try to find active user for specified credentials
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));


        //check if user is banned
        if (bannedUsersRepository.findById(user.getId()).isPresent())
            throw new AccesDeniedException("Hello " + user.getUsername() + " your account has been banned, please contact support for more details");

        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public UserDto ban(BanRequest banRequest) {

        String username = banRequest.getUsername();

        List<User> users = userRepository.findByUsername(username);

        if (users.size() == 0) throw new NotFoundException("User with username " + username + "not found");
        if (users.size() > 1) throw new NotFoundException("More than one user with username " + username);

        User user = users.get(0);
        BannedUsers baneBannedUser = new BannedUsers();
        baneBannedUser.setBannedId(user.getId());

        bannedUsersRepository.save(baneBannedUser);

        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto unban(BanRequest banRequest) {

        String username = banRequest.getUsername();

        List<User> users = userRepository.findByUsername(username);

        if (users.size() == 0) throw new NotFoundException("User with username " + username + "not found");
        if (users.size() > 1) throw new NotFoundException("More than one user with username " + username);

        User user = users.get(0);

        Optional<BannedUsers> bannedUser = bannedUsersRepository.findById(user.getId());
        if(bannedUser.isPresent())
            bannedUsersRepository.delete(bannedUser.get());
        else throw new NotFoundException("User " + username + " is not banned, no need to unban");

        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto editUser(EditUserDto editUserDto, String token) {
        Long id = getUserIdFromToken(token);

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            userMapper.userEditDtoToUser(editUserDto, user.get());
            userRepository.save(user.get());
        } else throw new NotFoundException("Bad request or invalid token");
        return userMapper.userToUserDto(user.get());
    }

    @Override
    public List<DisplayTermDto> findAllTerms() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8082/api"));

        ParameterizedTypeReference<List<DisplayTermDto>> list = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<DisplayTermDto>> response = null;

        try {
            response = restTemplate.exchange("/term", HttpMethod.GET, null, list);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new NotFoundException(String.format("Term with id: %d not found.", 5));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<DisplayTermDto> displayTermDtos;

//        try {
//            displayTermDtos = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, DisplayTermDto.class));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        return response.getBody();
    }

    @Override
    public DisplayTermDto findTermById(Long id) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8082/api"));

        ResponseEntity<DisplayTermDto> response = null;

                try {
                    response = restTemplate.exchange("/term/" + id, HttpMethod.GET, null, DisplayTermDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new NotFoundException(String.format("Term with id: %d not found.", id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.getBody();
    }

    private Long getUserIdFromToken(String token){
        Long id;

        String jwtPart = token.split(" ")[1];

        Claims claims = tokenService.parseToken(jwtPart);
        //If fails return UNAUTHORIZED response
        if (claims == null) {
            throw new AccesDeniedException("You do not have the right to make desired changes or bad request");
        }
        id = Long.valueOf(claims.get("id", Integer.class));

        return id;
    }
}
