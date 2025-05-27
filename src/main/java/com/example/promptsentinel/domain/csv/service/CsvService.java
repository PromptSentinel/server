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
    private final Random random = new Random();

    // 샘플 질문들
    private static final String[] SAMPLE_QUESTIONS = {
            "서울에서 가족과 함께 갈 만한 관광지는 어디인가요?",
            "부산 여행 3박 4일 코스 추천해주세요.",
            "제주도에서 렌터카 없이도 갈 수 있는 곳은?",
            "경주에서 역사 유적지 투어하려면 어떻게 해야 하나요?",
            "전주 한옥마을에서 먹을 만한 음식 추천해주세요.",
            "강릉에서 바다 뷰 좋은 카페 알려주세요.",
            "인천 차이나타운 맛집 순서대로 알려주세요.",
            "대구에서 쇼핑하기 좋은 곳은 어디인가요?",
            "광주에서 문화 예술 체험할 수 있는 곳은?",
            "춘천에서 닭갈비 맛집 베스트 5는?"
    };

    private static final String[] SAMPLE_RESPONSES = {
            "서울에서 가족 여행지로는 경복궁, 창덕궁, 남산타워, 한강공원, 롯데월드가 추천됩니다. 특히 경복궁은 교육적 가치가 높고, 한강공원은 피크닉하기 좋습니다.",
            "부산 3박4일 코스: 1일차 해운대-광안리, 2일차 감천문화마을-태종대, 3일차 부산타워-자갈치시장, 4일차 온천-쇼핑. 각 일정마다 맛집과 카페도 함께 즐기세요.",
            "제주도 대중교통 이용 코스: 제주시 관광지(용두암, 한라수목원), 성산일출봉(시외버스), 우도(도보), 섭지코지(버스) 순으로 이동 가능합니다.",
            "경주 역사투어는 불국사→석굴암→첨성대→대릉원→안압지 순서로 추천합니다. 하루 코스로 가능하며, 각 유적지마다 역사 해설을 들으시면 더욱 의미있습니다.",
            "전주 한옥마을 음식: 비빔밥, 콩나물국밥, 전주식 한정식, 초코파이, 떡갈비를 꼭 드셔보세요. 특히 한옥마을 내 전통찻집에서의 전통차도 추천합니다.",
            "강릉 바다뷰 카페 추천: 안목해변 카페거리, 정동진 썬크루즈, 주문진 등대카페, 경포대 해변 근처 카페들이 유명합니다. 일출이나 일몰 시간에 가시면 더욱 좋습니다.",
            "인천 차이나타운 맛집 코스: 공화춘(짜장면) → 신승반점(탕수육) → 월미도 씨사이드파크 → 화교중산학교 구경 → 자유공원 산책 순서로 추천합니다.",
            "대구 쇼핑 명소: 동성로, 김광석다시그리기길, 서문시장, 약령시한방문화축제장, 이월드몰이 있습니다. 각각 다른 매력의 쇼핑과 문화를 경험할 수 있습니다.",
            "광주 문화예술 체험: 광주비엔날레관, 국립아시아문화전당, 예술의거리, 무등산국립공원, 518민주광장에서 다양한 문화와 역사를 체험하실 수 있습니다.",
            "춘천 닭갈비 맛집 TOP5: 1.명동닭갈비, 2.춘천집, 3.유포리닭갈비, 4.옛날통나무집, 5.마루 닭갈비. 각각 고유한 양념과 맛의 특징이 있습니다."
    };

    private static final String[] LABELS = {
            "TOURIST_ATTRACTION", "TRAVEL_COURSE", "TRANSPORTATION", "HISTORICAL_SITE", "FOOD_RECOMMENDATION",
            "CAFE_RECOMMENDATION", "RESTAURANT_GUIDE", "SHOPPING", "CULTURAL_EXPERIENCE", "FOOD_RANKING"
    };

    /**
     * CSV 파일 생성
     */
    public void generateQACSV(String fileName, int recordCount) throws IOException {
        try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            // CSV 헤더
            writer.append("question,response,label\n");

            // 데이터 생성 및 작성
            for (int i = 0; i < recordCount; i++) {
                CsvData csvData = generateRandomPromtData(i);

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
            entity.setLabel(csvData.getLabel());
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
     * 랜덤 QA 데이터 생성
     */
    private CsvData generateRandomPromtData(int index) {
        int questionIndex = random.nextInt(SAMPLE_QUESTIONS.length);

        CsvData csvData = new CsvData();
        csvData.setQuestion(SAMPLE_QUESTIONS[questionIndex] + " (케이스 " + (index + 1) + ")");
        csvData.setResponse(SAMPLE_RESPONSES[questionIndex]);
        csvData.setLabel(LABELS[questionIndex]);

        return csvData;
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
                return new CsvData(fields.get(0), fields.get(1), fields.get(2));
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
