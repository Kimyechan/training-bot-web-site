package flexcity.me.trainingbot.controller;

import flexcity.me.trainingbot.advice.exception.CUserNotFoundException;
import flexcity.me.trainingbot.domain.Account;
import flexcity.me.trainingbot.domain.Exercise;
import flexcity.me.trainingbot.repository.AccountRepository;
import flexcity.me.trainingbot.repository.ExerciseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@Api(tags = {"1. Exercise"})
@RestController
@RequestMapping("/api")
public class ExerciseController {

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    AccountRepository accountRepository;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "운동 데이터 저장", notes = "현재 로그인한 대상에 대해서 데이터를 저장한다")
    @PostMapping("/exercise/{kind}")
    public ResponseExercise createExerciseData(@PathVariable("kind") String kind, @RequestBody String count) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseExercise responseExercise = new ResponseExercise();

        String userId = authentication.getName();

        Account account = accountRepository.findByUserId(userId).orElseThrow(CUserNotFoundException::new);

        Exercise exercise = exerciseRepository.save(
                Exercise.builder()
                        .kind(kind)
                        .count(Long.valueOf(count))
                        .date(new Date())
                        .account(account)
                        .build()
        );

        responseExercise.setExerciseId(exercise.getExerciseId());

        return responseExercise;
    }

    @Data
    static class ResponseExercise {
        private Long exerciseId;
    }
}
