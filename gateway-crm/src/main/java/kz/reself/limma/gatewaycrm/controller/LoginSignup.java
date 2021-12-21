package kz.reself.limma.gatewaycrm.controller;

import kz.reself.limma.gatewaycrm.model.Account;
import kz.reself.limma.gatewaycrm.model.LoginRequest;
import kz.reself.limma.gatewaycrm.security.CustomEncoder;
import kz.reself.limma.gatewaycrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/crm/api")
//@Slf4j
//@Validated
public class LoginSignup {

    @Autowired
    private CustomEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    /**
     * Login request endpoint
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    /**
     * Signup endpoint
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> createPerson(@RequestBody Account user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Mono.just(ResponseEntity.ok(userService.addUpdateUser(user)));
    }
}
