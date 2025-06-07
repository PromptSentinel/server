package com.example.promptsentinel.domain.csv.service;


import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.csv.dao.CsvDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CsvService {
    private final CsvDataRepository csvDataRepository;


    /**
     * CSV 파일 생성
     */
    public void generateQACSV(CsvData csvData, String fileName, int recordCount) throws IOException {
        try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            // CSV 헤더
            writer.append("question,response,label\n");

            // 데이터 생성 및 작성
            for (int i = 0; i < recordCount; i++) {

                writer.append("\"").append(escapeCsvValue(csvData.getQuestion())).append("\",");
                writer.append("\"").append(escapeCsvValue(csvData.getResponse())).append("\",");
                writer.append(csvData.getLabel()).append("\n");
            }
        }

        System.out.println("CSV 파일이 생성되었습니다: " + fileName);
    }

    /**
     * CSV 파일에서 데이터 읽기
     */
    public List<CsvData> readCSVFile(String fileName) throws IOException {
        List<CsvData> qaDataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // 헤더 스킵
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                CsvData qaData = parseCsvLine(line);
                if (qaData != null) {
                    qaDataList.add(qaData);
                }
            }
        }

        System.out.println("CSV에서 " + qaDataList.size() + "개의 데이터를 읽었습니다.");
        return qaDataList;
    }

    /**
     * DB에 저장
     */
    public void saveToDatabase(List<CsvData> csvDataList) {
        List<CsvData> entities = new ArrayList<>();

        for (CsvData csvData : csvDataList) {
            CsvData entity = new CsvData();
            entity.setQuestion(csvData.getQuestion());
            entity.setResponse(csvData.getResponse());
            //entity.setLabel(csvData.getLabel());
            entities.add(entity);
        }

        csvDataRepository.saveAll(entities);
        System.out.println(entities.size() + "개의 데이터가 DB에 저장되었습니다.");
    }

    /**
     * CSV 파일에서 직접 DB로 저장하는 통합 메서드
     */
    public void importCSVToDatabase(String fileName) throws IOException {
        List<CsvData> csvDataList = readCSVFile(fileName);
        saveToDatabase(csvDataList);
    }


    /**
     * CSV 라인 파싱
     */
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

            if (fields.size() >= 3) {
               // return new CsvData(fields.get(0), fields.get(1), fields.get(2));
            }

        } catch (Exception e) {
            System.err.println("라인 파싱 오류: " + line);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * CSV 값 이스케이프 처리
     */
    private String escapeCsvValue(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

}
