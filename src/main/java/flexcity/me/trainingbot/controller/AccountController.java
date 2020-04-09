package flexcity.me.trainingbot.controller;

import flexcity.me.trainingbot.configs.JwtTokenProvider;
import flexcity.me.trainingbot.domain.Account;
import flexcity.me.trainingbot.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;

@Api(tags = {"1. User"})
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원 가입", notes = "회원을 가입을 한다.")
    @PostMapping("/signup")
    public ResponseEntity<CreateAccountResponse> signup(@ApiParam(value="회원가입 정보", required=true) @RequestBody @Valid CreateAccountRequest request) {
        Account saveAccount = accountService.saveAccount(
                Account.builder()
                        .userId(request.userId)
                        .password(request.password)
                        .name(request.name)
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build());

        return new ResponseEntity<>(new CreateAccountResponse(saveAccount.getUserId()), HttpStatus.CREATED);
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@ApiParam(value="로그인 정보", required=true) @RequestBody @Valid LoginRequest request) {
        Optional<Account> account = accountService.findOne(request.getUserId());
        Account loginAccount = account.get();

        if(!passwordEncoder.matches(request.getPassword(), loginAccount.getPassword())){
            ResponseEntity.badRequest();
        }

        return ResponseEntity.ok(jwtTokenProvider.createToken(String.valueOf(loginAccount.getId()), loginAccount.getRoles()));
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

        public CreateAccountResponse(String userId){
            this.userId = userId;
        }
    }

    @Data
    static class LoginRequest {
        private String userId;
        private String password;
    }

    @Data
    static class LoginResponse {
        private  String userId;

        public LoginResponse(String userId) { this.userId = userId;}
    }
}
