package kz.reself.limma.gatewaycrm.service;

import kz.reself.limma.gatewaycrm.model.Account;
import kz.reself.limma.gatewaycrm.model.LoginRequest;
import kz.reself.limma.gatewaycrm.model.LoginResponse;
import kz.reself.limma.gatewaycrm.repository.AccountRepository;
import kz.reself.limma.gatewaycrm.security.CustomEncoder;
import kz.reself.limma.gatewaycrm.util.JWTUtil;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomEncoder passwordEncoder;

    @Autowired
    private AccountRepository repository;

    public Mono<Account> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Mono<Account> findById(Long id) {
        return repository.getById(id);
    }

    public Mono<Account> addUpdateUser(Account account) {
        return repository.save(account);
    }

    public ResponseEntity<?> login(LoginRequest request) {
        Account account = findByUsername(request.getUsername()).toProcessor().block();
        if (account != null) {
            boolean checkPassword = account.getPassword().equals(passwordEncoder.encode(request.getPassword()));
            boolean isEmpData = account.getRoles().contains("ROLE_USER");
            if (checkPassword && isEmpData) {
                return ResponseEntity.ok(buildWithLanguage(account));
            } else if (checkPassword) {
                return ResponseEntity.ok(buildWithoutLanguage(account));
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    private PageRequest createPageRequest(Map<String, String> params) {
        int page = 0;
        int size = 5;
        Sort sort = Sort.by("id");
        if (params.containsKey("page"))
            page = Integer.parseInt(params.get("page"));
        if (params.containsKey("size"))
            size = Integer.parseInt(params.get("size"));
        if (params.containsKey("sortBy"))
            sort = Sort.by(params.get("sortBy"));
        if (params.containsKey("sortDirection")) {
            if (params.get("sortDirection").equals("asc")) {
                sort.ascending();
            } else {
                sort.descending();
            }
        }

        return PageRequest.of(page, size, sort);
    }

    private LoginResponse buildWithLanguage(Account employee) {
        return LoginResponse.builder()
                .token(jwtUtil.generateToken(employee))
                .id(employee.getId())
                .username(employee.getUsername())
                .password(employee.getPassword())
                .roles(employee.getRoles())
                .build();
    }

    private LoginResponse buildWithoutLanguage(Account employee) {
        return LoginResponse.builder()
                .token(jwtUtil.generateToken(employee))
                .id(employee.getId())
                .username(employee.getUsername())
                .password(employee.getPassword())
                .roles(employee.getRoles())
                .build();
    }
}
