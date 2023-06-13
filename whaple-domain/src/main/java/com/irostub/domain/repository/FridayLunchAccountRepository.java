package com.irostub.domain.repository;

import com.irostub.domain.entity.Account;
import com.irostub.domain.entity.FridayLunchAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FridayLunchAccountRepository extends JpaRepository<FridayLunchAccount, Long> {
    List<FridayLunchAccount> findByAccount(Account account);
}
