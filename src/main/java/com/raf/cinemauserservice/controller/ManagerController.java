package com.raf.cinemauserservice.controller;

import com.raf.cinemauserservice.dto.*;
import com.raf.cinemauserservice.secutiry.CheckSecurity;
import com.raf.cinemauserservice.service.ManagerService;
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

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private UserService userService;
    private ManagerService managerService;

    public ManagerController(UserService userService, ManagerService managerService) {
        this.userService = userService;
        this.managerService = managerService;
    }

    @ApiOperation(value = "Register manager")
    @PostMapping
    public ResponseEntity<UserDto> saveManager(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        return new ResponseEntity<>(managerService.addManager(managerCreateDto), HttpStatus.CREATED);
    }


}
