package com.irostub.whaple.bot.fridaylunch;

import com.irostub.whaple.bot.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FridayLunchAccountRepository extends JpaRepository<FridayLunchAccount, Long> {
    List<FridayLunchAccount> findByAccount(Account account);
}
