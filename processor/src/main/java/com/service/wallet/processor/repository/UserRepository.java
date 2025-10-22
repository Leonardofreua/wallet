package com.service.wallet.processor.repository;

import com.service.wallet.processor.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Customer, Long> {
}
