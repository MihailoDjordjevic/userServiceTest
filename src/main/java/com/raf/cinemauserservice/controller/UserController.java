package com.raf.cinemauserservice.controller;

import com.raf.cinemauserservice.client.reservationservice.dto.DisplayTermDto;
import com.raf.cinemauserservice.dto.*;
import com.raf.cinemauserservice.secutiry.CheckSecurity;
import com.raf.cinemauserservice.service.ActivationService;
import com.raf.cinemauserservice.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private ActivationService activationService;

    public UserController(UserService userService, ActivationService activationService) {
        this.userService = userService;
        this.activationService = activationService;
    }

    @ApiOperation(value = "Get all users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {

        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

//    @GetMapping("/{id}/discount")
//    public ResponseEntity<DiscountDto> getDiscount(@PathVariable("id") Long id) {
//        return new ResponseEntity<>(userService.findDiscount(id), HttpStatus.OK);
//    }

    @GetMapping("/confirm/{id}/{username}")
    public ResponseEntity<UserDto> confirmRegistration(@PathVariable("id") Long id, @PathVariable("username") String username) {
        return new ResponseEntity<>(activationService.add(id, username), HttpStatus.CREATED);
    }

//    @ApiOperation(value = "Register user")
//    @PostMapping
//    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
//        return new ResponseEntity<>(userService.add(userCreateDto), HttpStatus.CREATED);
//    }

    @ApiOperation(value = "Register user")
    @PostMapping
    public ResponseEntity<UserCreateDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(activationService.addNonActivatedUser(userCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Ban")
    @PostMapping("/ban")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<UserDto> banUser(@RequestHeader("Authorization") String authorization, @RequestBody @Valid BanRequest username) {
        return new ResponseEntity<>(userService.ban(username), HttpStatus.OK);
    }

    @ApiOperation(value = "Unban")
    @PostMapping("/unban")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<UserDto> unbanUser(@RequestHeader("Authorization") String authorization, @RequestBody @Valid BanRequest username) {
        return new ResponseEntity<>(userService.unban(username), HttpStatus.OK);
    }

    @ApiOperation(value = "editUser")
    @PostMapping("/editUser")
    @CheckSecurity(roles = {"ROLE_USER"})
    public ResponseEntity<UserDto> editUser(@RequestHeader("Authorization") String authorization, @RequestBody @Valid EditUserDto editUserDto) {
        return new ResponseEntity<>(userService.editUser(editUserDto, authorization), HttpStatus.OK);
    }

    @GetMapping("/terms")
    //@CheckSecurity(roles = {"ROLE_USER"}) //TODO request header
    public ResponseEntity<List<DisplayTermDto>> findAllFreeTerms() {
        return new ResponseEntity<>(userService.findAllTerms(), HttpStatus.OK);
    }

    @GetMapping("/terms/{id}")
    //@CheckSecurity(roles = {"ROLE_USER"}) //TODO request header
    public ResponseEntity<DisplayTermDto> findTermById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findTermById(id), HttpStatus.OK);
    }

}
