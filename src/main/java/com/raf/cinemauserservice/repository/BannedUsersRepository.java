package com.raf.cinemauserservice.repository;

import com.raf.cinemauserservice.domain.BannedUsers;
import com.raf.cinemauserservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BannedUsersRepository extends JpaRepository<BannedUsers, Long> {

    @Override
    Optional<BannedUsers> findById(Long aLong);
}
