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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value="회원 정보", notes="회원 정보를 조회한다")
    @GetMapping("/user/me")
    public SingleResult<UserInfoResponse> currentUserInfo(@CurrentUser Account currentUser){
        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setUserId(currentUser.getUserId());
        userInfo.setPasssword(currentUser.getPassword());

        return  responseService.getSingleResult(userInfo);
    }
//    public SingleResult<UserInfoResponse> currentUserInfo() {
//        UserInfoResponse userInfo = new UserInfoResponse();
//
//        return responseService.getSingleResult(userInfo);
//    }

    @Data
    private class UserInfoResponse {
        private String userId;
        private String passsword;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<Account> findUserById() {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(accountRepository.findByUserId(id).orElseThrow(CUserNotFoundException::new));
    }
}
