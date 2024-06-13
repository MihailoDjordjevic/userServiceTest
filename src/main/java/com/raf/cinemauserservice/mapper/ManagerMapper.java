package com.raf.cinemauserservice.mapper;

import com.raf.cinemauserservice.domain.Manager;
import com.raf.cinemauserservice.domain.User;
import com.raf.cinemauserservice.dto.ManagerCreateDto;
import com.raf.cinemauserservice.dto.UserCreateDto;
import com.raf.cinemauserservice.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {

    private RoleRepository roleRepository;

    public ManagerMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Manager managerCreateDtoToManager(ManagerCreateDto managerCreateDto, User user){


        user.setRole(roleRepository.findRoleByName("ROLE_GYM_MANAGER").get());

        Manager manager = new Manager();

        manager.setEmploymentDate(managerCreateDto.getEmploymentDate());
        manager.setManagedGym(managerCreateDto.getManagedGym());
        manager.setUser(user);


        return manager;
    }

    public UserCreateDto managerCreateDtoToUserCreateDto(ManagerCreateDto managerCreateDto){

        UserCreateDto userCreateDto = new UserCreateDto();

        userCreateDto.setEmail(managerCreateDto.getEmail());
        userCreateDto.setFirstName(managerCreateDto.getFirstName());
        userCreateDto.setLastName(managerCreateDto.getLastName());
        userCreateDto.setUsername(managerCreateDto.getUsername());
        userCreateDto.setPassword(managerCreateDto.getPassword());

        return userCreateDto;
    }

}
