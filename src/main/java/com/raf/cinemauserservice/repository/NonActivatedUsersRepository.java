package com.raf.cinemauserservice.repository;

import com.raf.cinemauserservice.domain.NonActivatedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NonActivatedUsersRepository extends JpaRepository<NonActivatedUser, Long> {

    @Override
    Optional<NonActivatedUser> findById(Long aLong);
}
