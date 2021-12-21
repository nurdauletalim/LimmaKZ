package kz.reself.limma.gatewaycrm.controller;

import kz.reself.limma.gatewaycrm.model.Account;
import kz.reself.limma.gatewaycrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/crm/api")
public class UserResource {

    @Autowired
    private UserService userService;

    /**
     * Endpoint for all user
     *
     * @param principal
     * @return
     */
    @RequestMapping(value = "/public/v1/user", method = RequestMethod.GET)
    public Mono<ResponseEntity<?>> publicUser(Principal principal) {
        return Mono.just(ResponseEntity.ok(principal.toString()));
    }

    @RequestMapping(value = "account/read", method = RequestMethod.GET)
    public Mono<ResponseEntity<?>> getAccountByUsername(@RequestParam String username) {
        return Mono.just(ResponseEntity.ok(userService.findByUsername(username)));
    }

    /**
     * Authorized endpoint for 'USER' type user
     *
     * @param principal
     * @return
     */
    @RequestMapping(value = "/private/v1/resource/user", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<?>> user(Principal principal) {
        return Mono.just(ResponseEntity.ok(principal.toString()));
    }

    @PutMapping("/private/v1/user/{id}")
    public Mono<ResponseEntity<?>> updateUser(@PathVariable("id") Long id, @RequestBody Account user) {
        try {
            if (userService.addUpdateUser(user) != null) {
                return Mono.just(ResponseEntity.ok(userService.addUpdateUser(user)));
            } else {
                return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            System.out.println(e);
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/public/v1/user/{id}")
    public Mono<Account> getById(@PathVariable("id") Long id) {
        try {
            return userService.findById(id);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

//    @GetMapping("/public/v1/employees/stream/{paginationCount}")
//    public Flux<EmpData> getAllEmployeesByPaginationForStream(
//            @PathVariable("paginationCount") Integer paginationCount) {
//        try {
//            return userService.getAllEmployeesByPaginationForStream(paginationCount);
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//    }
}
