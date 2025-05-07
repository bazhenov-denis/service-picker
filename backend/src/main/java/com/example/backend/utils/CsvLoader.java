package com.example.backend.utils;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CsvLoader {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostConstruct
  public void loadCsvData() {
    String csvFilePath = "/app/src/main/resources/db/migration/offers.csv";
    try (CSVReader reader = new CSVReaderBuilder(new FileReader(Paths.get(csvFilePath).toFile()))
        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
        .build()) {
      List<String[]> records = reader.readAll();
      records.remove(0);

      String insertQuery = "INSERT INTO offers (product_id, tariff, code, child_code_1, child_count_1, child_code_2, child_count_2, child_code_3, child_count_3, child_code_4, child_count_4, period, region_id, profrole_group_id, price_all, currency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      for (String[] record : records) {
        if (record.length < 16) {
          System.err.println("Invalid CSV row: " + String.join(";", record));
          continue;
        }

        try {
          Long productId = parseLong(record[0]);
          String tariff = record[1];
          String code = record[2];
          String childCode1 = record[3];
          Integer childCount1 = parseInt(record[4]);
          String childCode2 = record[5];
          Integer childCount2 = parseInt(record[6]);
          String childCode3 = record[7];
          Integer childCount3 = parseInt(record[8]);
          String childCode4 = record[9];
          Integer childCount4 = parseInt(record[10]);
          Integer period = parseInt(record[11]);
          Integer regionId = parseInt(record[12]);
          Integer profroleGroupId = parseInt(record[13]);
          Double priceAll = parseDouble(record[14]);
          String currency = record[15];

          jdbcTemplate.update(insertQuery,
              productId, tariff, code,
              childCode1, childCount1,
              childCode2, childCount2,
              childCode3, childCount3,
              childCode4, childCount4,
              period, regionId,
              profroleGroupId, priceAll, currency
          );
        } catch (Exception e) {
          System.err.println("Error parsing row: " + String.join(";", record));
          e.printStackTrace();
        }
      }
      System.out.println("CSV Data Loaded Successfully.");
    } catch (Exception e) {
      System.err.println("Error loading CSV: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private Long parseLong(String value) {
    return value == null || value.isEmpty() ? null : Long.parseLong(value.trim());
  }

  private Integer parseInt(String value) {
    return value == null || value.isEmpty() ? null : Integer.parseInt(value.trim());
  }

  private Double parseDouble(String value) {
    return value == null || value.isEmpty() ? null : Double.parseDouble(value.trim());
  }
}
