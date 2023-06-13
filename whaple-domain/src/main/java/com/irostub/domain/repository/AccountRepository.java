package com.irostub.domain.repository;

import com.irostub.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountId(Long accountId);
    List<Account> findByCheckIoAndUserChatIdIsNotNull(boolean checkIo);
}
