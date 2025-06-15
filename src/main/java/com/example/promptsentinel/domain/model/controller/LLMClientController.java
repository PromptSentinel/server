package com.example.promptsentinel.domain.model.controller;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.csv.service.CsvService;
import com.example.promptsentinel.domain.evalutation.service.EvaluationService;
import com.example.promptsentinel.domain.fastAPI.dto.FastApiResponse;
import com.example.promptsentinel.domain.fastAPI.service.FastApiService;
import com.example.promptsentinel.domain.model.dto.LLMModelRequest;
import com.example.promptsentinel.domain.model.dto.LLMResponse;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import com.example.promptsentinel.domain.model.service.LLMClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class LLMClientController {

    private final LLMClientService llmClientService;
    private final FastApiService fastApiService;
    private final EvaluationService evaluationService;
    private final CsvService csvService;



    //추후 response 변경
    @PostMapping("/process")
    public ResponseEntity<?> evaluatePromptAgainstLLM(@AuthenticationPrincipal Long memberId, @RequestBody LLMModelRequest request) {

        LLMModel llmModel = llmClientService.saveModel(memberId, request);
        List<LLMResponse> responseList  = llmClientService.processMultiplePromptEntities(memberId, llmModel);

        try {
            String fileName = "output_" + request.getModelName() + ".csv";
            csvService.generateQACSV(responseList, fileName);

            // 2. FastAPI로 분류 요청
            ResponseEntity<byte[]> fastApiResponse = fastApiService.sendCsvToFastApi(fileName);

            // 3. 분류된 결과 저장
            String classifiedFileName =  "classified_" + request.getModelName() + "_" + System.currentTimeMillis() + ".csv";
            try (FileOutputStream fos = new FileOutputStream(classifiedFileName)) {
                fos.write(fastApiResponse.getBody());
            }

            // 4. 분류된 CSV를 DB에 저장
            List<CsvData> csvDataList = csvService.readCSVFile(classifiedFileName);

           // csvService.saveToDatabase(csvDataList);



            return ResponseEntity.ok(evaluationService.saveEvaluation(llmModel, csvDataList));




        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CSV 파일 생성 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FastApiResponse.error("FastAPI 호출 중 오류가 발생했습니다: " + e.getMessage()));
        }


    }



    @PostMapping("/evaluate")
    public ResponseEntity<List<LLMResponse>> evaluatePromptsAgainstLLM(@AuthenticationPrincipal Long memberId, @RequestBody LLMModelRequest request) {

        List<LLMResponse> responseList  = llmClientService.evaluatePromptListAgainstLLM(memberId, request);

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


}