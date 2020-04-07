package flexcity.me.trainingbot.controller;

import flexcity.me.trainingbot.domain.Account;
import flexcity.me.trainingbot.service.AccountService;
import flexcity.me.trainingbot.vo.LoginInfo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

//    @PostMapping("/login")
//    public String Login(@RequestBody @Valid LoginInfo loginInfo){
//        Optional<Account> account = accountService.findOne(loginInfo.getId());
//        Account accountData = account.get();
//
//        if(account.isEmpty()) {
//
//        } else {
//            if(accountData.getPw() == loginInfo.getPw()){
//
//            } else {
//
//            }
//        }
//        return "ok";
//    }


    @PostMapping("/signUp")
    public CreateAccountResponse SignUp(@RequestBody @Valid CreateAccountRequest request) {
        Account account = new Account();

        account.setUserId(request.getUserId());
        account.setPassword(request.getPassword() );
        account.setName(request.getName());
        account.setPhoneNumber(request.getPhoneNumber());

        Account saveAccount = accountService.saveAccount(account);

        return new CreateAccountResponse(saveAccount.getUserId());
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

}
