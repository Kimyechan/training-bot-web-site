package flexcity.me.trainingbot.controller;

import flexcity.me.trainingbot.advice.exception.CEmailSigninFailedException;
import flexcity.me.trainingbot.advice.exception.CUserNotFoundException;
import flexcity.me.trainingbot.configs.JwtTokenProvider;
import flexcity.me.trainingbot.domain.Account;
import flexcity.me.trainingbot.model.reponse.SingleResult;
import flexcity.me.trainingbot.repository.AccountRepository;
import flexcity.me.trainingbot.service.AccountService;
import flexcity.me.trainingbot.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.Collections;

@Api(tags = {"1. Account"})
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ResponseService responseService;

    @Autowired
    AuthenticationManager authenticationManager;

    @ApiOperation(value = "회원 가입", notes = "회원을 가입을 한다.")
    @PostMapping("/signup")
    public ResponseEntity<SingleResult<CreateAccountResponse>> signup(@ApiParam(value = "회원가입 정보", required = true) @RequestBody @Valid CreateAccountRequest request) {
        Account saveAccount = accountService.saveAccount(
                Account.builder()
                        .userId(request.userId)
                        .password(request.password)
                        .name(request.name)
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build());

        return new ResponseEntity<>(responseService.getSingleResult(new CreateAccountResponse(saveAccount.getUserId())), HttpStatus.CREATED);
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@ApiParam(value = "로그인 정보", required = true) @RequestBody @Valid LoginRequest request) {
        Account account = accountService.findOne(request.getUserId()).orElseThrow(CUserNotFoundException::new);
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword()))
            throw new CEmailSigninFailedException();

        String token = jwtTokenProvider.createToken(String.valueOf(account.getId()), account.getRoles());
//        return ResponseEntity.ok(responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(account.getId()), account.getRoles())));
//        return ResponseEntity.ok(jwtTokenProvider.createToken(String.valueOf(account.getId()), account.getRoles()));
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @Data
    public class JwtAuthenticationResponse {
        private String accessToken;
        private String tokenType = "Bearer";

        public JwtAuthenticationResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @Data
    static class CreateAccountRequest {
        private String userId;
        private String password;
        private String name;
        private String phoneNumber;
    }

    @Data
    static class CreateAccountResponse {
        private String userId;

        public CreateAccountResponse(String userId) {
            this.userId = userId;
        }
    }

    @Data
    static class LoginRequest {
        private String userId;
        private String password;
    }

}
