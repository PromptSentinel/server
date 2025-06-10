package com.example.promptsentinel.domain.csv.service;


import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.csv.dao.CsvDataRepository;
import com.example.promptsentinel.domain.model.dto.LLMResponse;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsvService {
    private final CsvDataRepository csvDataRepository;

    public void generateQACSV(List<LLMResponse> llmResponseList, String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            // CSV 헤더
            writer.append("index,strategy,prompt,response\n");

            // 데이터 생성 및 작성
            for (int i = 0; i < llmResponseList.size(); i++) {
                LLMResponse llmResponse = llmResponseList.get(i);

                writer.append(String.valueOf(i + 1)).append(","); // 인덱스 번호
                writer.append("\"").append(escapeCsvValue(llmResponse.getStrategy())).append("\",");
                writer.append("\"").append(escapeCsvValue(llmResponse.getLlmRequest())).append("\","); // prompt 또는 question
                writer.append("\"").append(escapeCsvValue(llmResponse.getLlmResponse())).append("\"");
                writer.append("\n");
            }
        }

        System.out.println("CSV 파일이 생성되었습니다: " + fileName);
    }


    public List<CsvData> readCSVFile(String fileName) throws IOException {
        List<CsvData> csvDataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                CsvData csvData = parseCsvLine(line);
                if (csvData != null) {
                    csvDataList.add(csvData);
                }
            }
        }

        System.out.println("CSV에서 " + csvDataList.size() + "개의 데이터를 읽었습니다.");
        return csvDataList;
    }
    /**
     * DB에 저장
     */

    public void saveToDatabase(List<CsvData> csvDataList) {
        List<CsvData> entities = new ArrayList<>();

        for (CsvData csvData : csvDataList) {
            CsvData entity = new CsvData();
            entity.setStrategy(csvData.getStrategy());
            entity.setQuestion(csvData.getQuestion());
            entity.setResponse(csvData.getResponse());
            entity.setRoBERTaLabel(csvData.getRoBERTaLabel());
            entity.setDeBERTaLabel(csvData.getDeBERTaLabel());
            entity.setBARTLabel(csvData.getBARTLabel());
            entity.setELECTRALabel(csvData.getELECTRALabel());
            entities.add(entity);
        }

        csvDataRepository.saveAll(entities);
        System.out.println(entities.size() + "개의 데이터가 DB에 저장되었습니다.");
    }

    private CsvData parseCsvLine(String line) {
        try {
            List<String> fields = new ArrayList<>();
            StringBuilder currentField = new StringBuilder();
            boolean inQuotes = false;
            boolean quoteStarted = false;

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c == '"') {
                    if (!quoteStarted) {
                        inQuotes = true;
                        quoteStarted = true;
                    } else if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        // 연속된 따옴표는 하나의 따옴표로 처리
                        currentField.append('"');
                        i++; // 다음 따옴표 스킵
                    } else {
                        inQuotes = false;
                    }
                } else if (c == ',' && !inQuotes) {
                    fields.add(currentField.toString());
                    currentField = new StringBuilder();
                    quoteStarted = false;
                } else {
                    currentField.append(c);
                }
            }

            // 마지막 필드 추가
            fields.add(currentField.toString());
            log.info("fields : " + fields.toString());

            // CSV 읽기용: strategy, prompt, response, RoBERTaLabel, DeBERTaLabel, BARTLabel, ELECTRALabel
            if (fields.size() >= 7) {
                CsvData csvData = new CsvData();
                csvData.setStrategy(fields.get(2));
                csvData.setQuestion(fields.get(4));
                csvData.setResponse(fields.get(5));

                // 라벨 필드들 파싱 (숫자로 변환)
                try {
                    csvData.setRoBERTaLabel(extractLabelNumber(fields.get(6).trim()));
                    csvData.setDeBERTaLabel(extractLabelNumber(fields.get(7).trim()));
                    csvData.setBARTLabel(extractLabelNumber(fields.get(8).trim()));
                    csvData.setELECTRALabel(extractLabelNumber(fields.get(9).trim()));
                } catch (NumberFormatException e) {
                    System.err.println("라벨 값 파싱 오류: " + line);
                    // 기본값 설정
                    csvData.setRoBERTaLabel(0);
                    csvData.setDeBERTaLabel(0);
                    csvData.setBARTLabel(0);
                    csvData.setELECTRALabel(0);
                }

                return csvData;
            }

        } catch (Exception e) {
            System.err.println("라인 파싱 오류: " + line);
            e.printStackTrace();
        }

        return null;
    }

    private int extractLabelNumber(String labelStr) {
        String numberStr = labelStr.replaceAll("[^0-9]", "");
        if (numberStr.isEmpty()) {
            // 숫자가 없으면 기본값 0 반환하거나 예외 처리 가능
            return 0;
        }
        return Integer.parseInt(numberStr);
    }

    /**
     * CSV 값 이스케이프 처리
     */
    private String escapeCsvValue(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

}
