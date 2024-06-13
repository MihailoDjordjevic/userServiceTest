package com.raf.cinemauserservice.service;


import com.raf.cinemauserservice.client.reservationservice.dto.DisplayTermDto;
import com.raf.cinemauserservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

   // DiscountDto findDiscount(Long id);

    UserDto add(UserCreateDto userCreateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    UserDto ban(BanRequest username);

    UserDto unban(BanRequest username);

    UserDto editUser(EditUserDto editUserDto, String token);

    List<DisplayTermDto> findAllTerms( );

    DisplayTermDto findTermById(Long id);

  //  void incrementReservationCount(IncrementReservationCountDto incrementReservationCountDto);
}
