package com.irostub.whaple.bot.account;

import com.irostub.whaple.bot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public Account accountSaveIfNotExists(User user){
        Long userId = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String userName = user.getUserName();

        Account account = accountRepository.findByAccountId(userId)
                .orElseGet(() -> Account.create(userId, firstName, lastName, userName));
        return accountRepository.save(account);
    }

    public Account findByIdWithNull(Long userId) {
        return accountRepository.findByAccountId(userId).orElse(null);
    }

    public Account findById(Long userId){
        return accountRepository.findByAccountId(userId).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Account accountSaveWithChatId(Long chatId, User user) {
        Long userId = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String userName = user.getUserName();

        Account account = accountRepository.findByAccountId(userId)
                .orElseGet(() -> Account.create(userId, firstName, lastName, userName, chatId));
        account.updateUserChatId(chatId);
        return accountRepository.save(account);
    }
}
