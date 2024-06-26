package com.raf.cinemauserservice.repository;

import com.raf.cinemauserservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsernameAndPassword(String username, String password);

    List<User> findByUsername(String username);

    Optional<User> findById(Long id);
}
