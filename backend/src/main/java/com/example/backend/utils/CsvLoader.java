package com.example.backend.utils;

import com.example.backend.models.Offer;
import com.example.backend.models.ProfrolesMapping;
import com.example.backend.models.RegionAreaMapping;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CsvLoader {

  //@Autowired
  //private JdbcTemplate jdbcTemplate;

  @PostConstruct
  public void loadCsvData() {
    loadOffersData();
    loadProfrolesMappingData();
    loadAreaMappingData();
  }

  private void loadOffersData() {
    String csvFilePath = "/app/src/main/resources/db/migration/data/offers.csv";
    try (CSVReader reader = new CSVReaderBuilder(new FileReader(Paths.get(csvFilePath).toFile()))
        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
        .build()) {
      List<String[]> records = reader.readAll();
      // remove header if present
      if (!records.isEmpty() && isHeader(records.get(0), "product_id")) {
        records.remove(0);
      }

      Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();
//      Query query = session.createQuery(
//          "INSERT INTO offers (product_id, tariff, code, " +
//              "child_code_1, child_count_1, child_code_2, child_count_2, child_code_3, child_count_3, child_code_4, child_count_4, " +
//              "period, region_id, profrole_group_id, price_all, currency) " +
//              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
//      );
//      String insertQuery = "INSERT INTO offers (product_id, tariff, code, child_code_1, child_count_1, child_code_2, child_count_2, child_code_3, child_count_3, child_code_4, child_count_4, period, region_id, profrole_group_id, price_all, currency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      for (String[] record : records) {
        if (record.length < 16) continue;

        Offer offer = new Offer(
            parseLong(record[0]), record[1], record[2],
            record[3], parseInt(record[4]),
            record[5], parseInt(record[6]),
            record[7], parseInt(record[8]),
            record[9], parseInt(record[10]),
            parseInt(record[11]), parseLong(record[12]),
            parseInt(record[13]), parseDouble(record[14]), record[15]
            );
        session.persist(offer);

//        jdbcTemplate.update(insertQuery,
//            parseLong(record[0]), record[1], record[2],
//            record[3], parseInt(record[4]),
//            record[5], parseInt(record[6]),
//            record[7], parseInt(record[8]),
//            record[9], parseInt(record[10]),
//            parseInt(record[11]), parseInt(record[12]),
//            parseInt(record[13]), parseDouble(record[14]), record[15]
//        );
      }
      tx.commit();
      session.close();
    } catch (Exception e) {
      System.err.println("Error loading offers CSV: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void loadProfrolesMappingData() {
    String csvFilePath = "/app/src/main/resources/db/migration/data/mapping_profroles.csv";
    try (CSVReader reader = new CSVReaderBuilder(new FileReader(Paths.get(csvFilePath).toFile()))
        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
        .build()) {
      List<String[]> records = reader.readAll();
      // remove header if present
      if (!records.isEmpty() && records.get(0).length >= 2 && isHeader(records.get(0), "price_profrole_group_id")) {
        records.remove(0);
      }

      //String insertQuery = "INSERT INTO profroles_mapping (price_profrole_group_id, professional_role_id) VALUES (?, ?)";
      Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();

      for (String[] record : records) {
        if (record.length < 2) continue;

        ProfrolesMapping profrolesMapping = new ProfrolesMapping(parseInt(record[0]), parseInt(record[1]));
        session.persist(profrolesMapping);
//        jdbcTemplate.update(insertQuery,
//            parseLong(record[0]), // use Long
//            parseLong(record[1])
//        );
      }

      tx.commit();
      session.close();
    } catch (Exception e) {
      System.err.println("Error loading profroles mapping CSV: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void loadAreaMappingData() {
    String csvFilePath = "/app/src/main/resources/db/migration/data/mapping_area.csv";
    try (CSVReader reader = new CSVReaderBuilder(new FileReader(Paths.get(csvFilePath).toFile()))
        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
        .build()) {
      List<String[]> records = reader.readAll();
      // remove header if present
      if (!records.isEmpty() && records.get(0).length >= 2 && isHeader(records.get(0), "price_region_id")) {
        records.remove(0);
      }

//      String insertQuery = "INSERT INTO region_area_mapping (price_region_id, area_id) VALUES (?, ?)";
      Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();

      for (String[] record : records) {
        if (record.length < 2) continue;

        RegionAreaMapping regionAreaMapping = new RegionAreaMapping(parseLong(record[0]), parseInt(record[1]));
        session.persist(regionAreaMapping);
//        jdbcTemplate.update(insertQuery,
//            parseLong(record[0]),
//            parseLong(record[1])
//        );
      }

      tx.commit();
      session.close();
    } catch (Exception e) {
      System.err.println("Error loading area mapping CSV: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private boolean isHeader(String[] record, String firstColumnName) {
    // simple check: if first cell equals expected column name
    return record[0].trim().equalsIgnoreCase(firstColumnName);
  }

  private Long parseLong(String value) {
    if (value == null) return null;
    // remove BOM and whitespace
    String cleaned = value.replace("\uFEFF", "").trim().replaceAll("\\s+", "");
    try {
      return cleaned.isEmpty() ? null : Long.parseLong(cleaned);
    } catch (NumberFormatException e) {
      System.err.println("Error parsing Long: '" + value + "'");
      return null;
    }
  }

  private Integer parseInt(String value) {
    if (value == null) return null;
    String cleaned = value.replace("\uFEFF", "").trim().replaceAll("\\s+", "");
    try {
      return cleaned.isEmpty() ? null : Integer.parseInt(cleaned);
    } catch (NumberFormatException e) {
      System.err.println("Error parsing Integer: '" + value + "'");
      return null;
    }
  }

  private Double parseDouble(String value) {
    if (value == null) return null;
    String cleaned = value.replace("\uFEFF", "").trim().replaceAll("\\s+", "");
    try {
      return cleaned.isEmpty() ? null : Double.parseDouble(cleaned);
    } catch (NumberFormatException e) {
      System.err.println("Error parsing Double: '" + value + "'");
      return null;
    }
  }
}
