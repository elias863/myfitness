package com.myrecovery.fit.repository;

import com.myrecovery.fit.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email);   // use email if that is the correct field for login
}

