package com.service.assignment.wallet.repository;

import com.service.assignment.wallet.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Customer, Long> {
}
