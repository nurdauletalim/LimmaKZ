package kz.reself.limma.account.service;

import kz.reself.limma.account.model.Account;
import kz.reself.limma.account.model.dto.AccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountService {
    Page<Account> getAccounts(Pageable pageable);

    List<Account> getAccountsIterable();

    Account getAccountById(Integer id);

    Account getAccountByUsername(String username);

    Account addAccount(AccountDTO account);

    List<Account> getAllByOrganizationId(Long organizationId);

    void updateAccount(Account account);

    Account updateUsername(Integer id, String username);

    Account updatePassword(Integer id, String oldPass, String newPass);

    void deleteAccount(Account account);

    void deleteAccountById(Integer id);

    boolean verifyAccountPassword(String username, String password);
}

