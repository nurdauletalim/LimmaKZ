package kz.reself.limma.account.repository;

import kz.reself.limma.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account getById(Long id);

    Account getAccountByUsername(String username);
    List<Account> findAllByOrganizationId(Long id);
    void deleteById(Integer id);
}
