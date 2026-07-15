package com.coding.spring_boot_user_authentication.repository;

import com.coding.spring_boot_user_authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}