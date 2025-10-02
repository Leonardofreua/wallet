package com.service.assignment.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.assignment.wallet.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
