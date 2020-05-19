package flexcity.me.trainingbot.controller;

import flexcity.me.trainingbot.advice.exception.CUserNotFoundException;
import flexcity.me.trainingbot.common.CurrentUser;
import flexcity.me.trainingbot.domain.Account;
import flexcity.me.trainingbot.model.reponse.SingleResult;
import flexcity.me.trainingbot.repository.AccountRepository;
import flexcity.me.trainingbot.service.ResponseService;
import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"2. User"})
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    ResponseService responseService;

    @Autowired
    AccountRepository accountRepository;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<Account> findUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return responseService.getSingleResult(accountRepository.findByUserId(userId).orElseThrow(CUserNotFoundException::new));
    }
}
