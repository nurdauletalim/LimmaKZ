package kz.reself.limma.gatewaycrm.repository;

import kz.reself.limma.gatewaycrm.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {
    Mono<Account> findByUsername(String email);

//    Mono<Account> findByUsernameAndDeletedAtIsNull(String email);

    Mono<Account> getById(Long id);

    Flux<Account> getAllByRoles(String roles);
    Flux<Account> getAllByRoles(String roles, Pageable pageable);
}
