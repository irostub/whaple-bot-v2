package com.irostub.standard.bot.checkio;

import com.irostub.domain.entity.Account;
import com.irostub.domain.repository.AccountRepository;
import com.irostub.standard.bot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckIoService {
    private final AccountRepository accountRepository;

    @Transactional
    public Account switchCheckIoService(Long accountId){
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(NotFoundException::new);
        account.switchCheckIo();
        return account;
    }

    public List<Account> findScheduledAccount() {
        return accountRepository.findByCheckIoAndUserChatIdIsNotNull(true);
    }
}
