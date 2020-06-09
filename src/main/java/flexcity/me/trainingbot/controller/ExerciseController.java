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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public ResponseExercise createExerciseData(@RequestBody RequestExercise requestExercise) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseExercise responseExercise = new ResponseExercise();

        String userId = authentication.getName();

        Account account = accountRepository.findByUserId(userId).orElseThrow(CUserNotFoundException::new);

        Exercise exercise = exerciseRepository.save(
                Exercise.builder()
                        .kind(requestExercise.getKind())
                        .count(requestExercise.getCount())
                        .purposeCount(requestExercise.getPurposeCount())
                        .date(new Date())
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
    @ApiOperation(value = "운동 데이터 저장", notes = "현재 로그인한 대상에 대해서 데이터를 저장한다")
    @GetMapping("searchExerciseData")
    public ListResult<ResponseExerciseData> exerciseData(){
        List<ResponseExerciseData> response = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Account account = accountRepository.findByUserIdForExercise(userId).orElseThrow(CUserNotFoundException::new);
        List<Exercise> exercises = account.getExercises();

        for(Exercise exercise : exercises){
            ResponseExerciseData responseExercise = ResponseExerciseData.builder()
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
        private Long purposeCount;
        private Long count;
        private String kind;
    }
}
