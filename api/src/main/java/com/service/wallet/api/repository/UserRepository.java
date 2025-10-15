package com.service.wallet.api.repository;

import com.service.wallet.api.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Customer, Long> {
}
