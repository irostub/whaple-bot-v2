package com.irostub.domain.repository;

import com.irostub.domain.entity.market.WebAppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebAppUserRepository extends JpaRepository<WebAppUser, Long> {
}
