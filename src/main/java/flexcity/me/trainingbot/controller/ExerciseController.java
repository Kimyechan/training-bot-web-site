package flexcity.me.trainingbot.controller;

import flexcity.me.trainingbot.advice.exception.CUserNotFoundException;
import flexcity.me.trainingbot.domain.Account;
import flexcity.me.trainingbot.domain.Exercise;
import flexcity.me.trainingbot.model.reponse.ListResult;
import flexcity.me.trainingbot.repository.AccountRepository;
import flexcity.me.trainingbot.repository.ExerciseRepository;
import flexcity.me.trainingbot.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = {"1. Exercise"})
@RestController
@RequestMapping("/api")
public class ExerciseController {

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "운동 데이터 저장", notes = "현재 로그인한 대상에 대해서 데이터를 저장한다")
    @PostMapping("/saveExercise")
    public ResponseExercise createExerciseData(Authentication authentication, @RequestBody RequestExercise requestExercise) {
        ResponseExercise responseExercise = new ResponseExercise();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        Account account = accountRepository.findByUserId(userId).orElseThrow(CUserNotFoundException::new);

        Exercise exercise = exerciseRepository.save(
                Exercise.builder()
                        .kind(requestExercise.getKind())
                        .count(requestExercise.getCount())
                        .purposeCount(requestExercise.getPurposeCount())
                        .date(LocalDate.now())
                        .account(account)
                        .build()
        );

        responseExercise.setExerciseId(exercise.getExerciseId());

        return responseExercise;
    }

    @Data
    static class RequestExercise{
        private Long purposeCount;
        private Long count;
        private String kind;
    }
    @Data
    static class ResponseExercise {
        private Long exerciseId;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "운동 목표 달성률", notes = "운동 목표를 달성하는 비율을 나타내기위해 데이터를 가져온다")
    @GetMapping("/searchExerciseData")
    public ListResult<ResponseExerciseData> exerciseData(Authentication authentication){
        List<ResponseExerciseData> response = new ArrayList<>();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        Optional<Account> account = accountRepository.findByUserIdForExercise(userId);
        Account accountNew;
        List<Exercise> exercises;

        if(account.isEmpty()){
            accountNew = accountRepository.findByUserId(userId).orElseThrow(CUserNotFoundException::new);
            exercises = accountNew.getExercises();
        } else {
            accountNew = account.get();
            exercises = accountNew.getExercises();
        }

        for(Exercise exercise : exercises){
            ResponseExerciseData responseExercise = ResponseExerciseData.builder()
                    .date(exercise.getDate())
                    .purposeCount(exercise.getPurposeCount())
                    .count(exercise.getCount())
                    .kind(exercise.getKind())
                    .build();

            response.add(responseExercise);
        }

        return responseService.getListResult(response);
    }

    @Data
    @Builder
    static class ResponseExerciseData{
        private LocalDate date;
        private Long purposeCount;
        private Long count;
        private String kind;
    }
}
