package com.raf.cinemauserservice.mapper;

import com.raf.cinemauserservice.domain.NonActivatedUser;
import com.raf.cinemauserservice.domain.User;
import com.raf.cinemauserservice.dto.EditUserDto;
import com.raf.cinemauserservice.dto.UserCreateDto;
import com.raf.cinemauserservice.dto.UserDto;
import com.raf.cinemauserservice.repository.RoleRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }

    public User userCreateDtoToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setRole(roleRepository.findRoleByName("ROLE_USER").get());
        return user;
    }

    public User userEditDtoToUser(EditUserDto editUserDto,  User user) {
        if(editUserDto.getEmail() != null)
            user.setEmail(editUserDto.getEmail());
        if(!editUserDto.getFirstName().isEmpty())
            user.setFirstName(editUserDto.getFirstName());
        if(!editUserDto.getLastName().isEmpty())
            user.setLastName(editUserDto.getLastName());
        if(editUserDto.getBirthday() != null)
            user.setBirthday(editUserDto.getBirthday());
        return user;
    }

    public NonActivatedUser userCreateDtoToNonActivedUser(UserCreateDto userCreateDto) {
        NonActivatedUser nonActivatedUser = new NonActivatedUser();

        nonActivatedUser.setEmail(userCreateDto.getEmail());
        nonActivatedUser.setFirstName(userCreateDto.getFirstName());
        nonActivatedUser.setLastName(userCreateDto.getLastName());
        nonActivatedUser.setUsername(userCreateDto.getUsername());
        nonActivatedUser.setPassword(userCreateDto.getPassword());
        return nonActivatedUser;
    }

    public UserCreateDto nonActivedUserToUserCreateDto(NonActivatedUser nonActivatedUser) {
        UserCreateDto userCreateDto = new UserCreateDto();

        userCreateDto.setEmail(nonActivatedUser.getEmail());
        userCreateDto.setFirstName(nonActivatedUser.getFirstName());
        userCreateDto.setLastName(nonActivatedUser.getLastName());
        userCreateDto.setUsername(nonActivatedUser.getUsername());
        userCreateDto.setPassword(nonActivatedUser.getPassword());
        return userCreateDto;
    }
}
