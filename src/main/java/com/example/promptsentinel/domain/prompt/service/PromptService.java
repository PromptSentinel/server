package com.example.promptsentinel.domain.prompt.service;

import com.example.promptsentinel.domain.prompt.dao.PromptRepository;
import com.example.promptsentinel.domain.prompt.dto.PromptListRequest;
import com.example.promptsentinel.domain.prompt.entity.Prompt;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptService {
    private final PromptRepository promptRepository;

    public List<Prompt> getQuestion(){
        return promptRepository.findAll();
    }

    public List<Prompt> uploadPrompt(PromptListRequest promptListRequest) {
        return promptListRequest.getPromptRequeatList().stream()
                .map(promptRequeat -> promptRepository.save(
                        Prompt.builder()
                                .scenarioName(promptRequeat.getScenarioName())
                                .strategy(promptRequeat.getStrategy())
                                .question(promptRequeat.getQuestion())
                                .generatedPrompt(promptRequeat.getGeneratedPrompt())
                                .build()))
                .collect(Collectors.toList());
    }

    public void importPromptsFromMultipartFile(MultipartFile file) {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = csvReader.readAll();
            boolean isFirst = true;
            for (String[] columns : records) {
                if (isFirst) {
                    isFirst = false;
                    continue;  // 헤더 스킵
                }

                log.info("columns[1].trim() "+ columns[1].trim());
                log.info("columns[2].trim() "+ columns[2].trim());
                log.info("columns[2].trim() "+ columns[3].trim());
                log.info("columns[2].trim() "+ columns[4].trim());
                Prompt prompt = Prompt.builder()
                        .scenarioName(columns[1].trim())
                        .strategy(columns[2].trim())
                        .question(columns[3].trim())
                        .generatedPrompt(columns[4].trim())
                        .build();
                promptRepository.save(prompt);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException("CSV 파싱 실패", e);
        }
    }
}
