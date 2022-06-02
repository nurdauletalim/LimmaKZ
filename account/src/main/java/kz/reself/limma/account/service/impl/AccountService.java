package kz.reself.limma.account.service.impl;

import kz.reself.limma.account.model.Account;
import kz.reself.limma.account.model.dto.AccountDTO;
import kz.reself.limma.account.repository.AccountRepository;
import kz.reself.limma.account.security.CustomEncoder;
import kz.reself.limma.account.service.IAccountService;
import kz.reself.limma.account.service.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccountService implements IAccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomEncoder customEncoder;

    @Override
    public Page<Account> getAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public List<Account> getAccountsIterable() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.getById(Long.valueOf(id));
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.getAccountByUsername(username);
    }


    @Override
    public Account addAccount(AccountDTO account) {
        Account newAcc = new Account();
        newAcc.setPassword(customEncoder.encode(account.getPassword()));
        newAcc.setUsername(account.getUsername());
        newAcc.setRoles(account.getRoles());
        newAcc.setOrganizationId(account.getOrganizationId());
        return  accountRepository.save(newAcc);
    }

    @Override
    public List<Account> getAllByOrganizationId(Long organizationId) {
        return accountRepository.findAllByOrganizationId(organizationId);
    }

    @Override
    public void updateAccount(Account account){
        accountRepository.save(account);
    }

    @Override
    public Account updateUsername(Integer id, String username) {
        Account account = accountRepository.getById(Long.valueOf(id));
        account.setUsername(username);
        return accountRepository.save(account);
    }

    @Override
    public Account updatePassword(Integer id, String oldPass, String newPass) {
        Account account = accountRepository.getById(Long.valueOf(id));
        String oldPassword = PasswordUtils.generateSecurePassword(oldPass);
        if (oldPassword.equals(account.getPassword())) {
            account.setPassword(PasswordUtils.generateSecurePassword(newPass));
            return accountRepository.save(account);
        } else
            return null;
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    @Override
    public void deleteAccountById(Integer id) {
        accountRepository.deleteById(id);
    }

    @Override
    public boolean verifyAccountPassword(String username, String password) {
        Account account = accountRepository.getAccountByUsername(username);
        if (account!=null) {
            return PasswordUtils.verifyUserPassword(password, account.getPassword());
        }
        return false;
    }
}
