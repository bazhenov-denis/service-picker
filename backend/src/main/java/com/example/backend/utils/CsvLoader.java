package com.example.backend.utils;

import com.example.backend.DAO.OfferDaoImpl;
import com.example.backend.models.Offer;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CsvLoader {

  @Autowired
  private OfferDaoImpl offerDao;

  @PostConstruct
  public void loadCsvData() {
    loadOffersData();
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

      for (String[] record : records) {
        if (record.length < 16) continue;

        Offer offer = new Offer();
        offer.setProductId(parseLong(record[0]));
        offer.setTariff(record[1]);
        offer.setCode(record[2]);
        offer.setChildCode1(record[3]);
        offer.setChildCount1(parseInt(record[4]));
        offer.setChildCode2(record[5]);
        offer.setChildCount2(parseInt(record[6]));
        offer.setChildCode3(record[7]);
        offer.setChildCount3(parseInt(record[8]));
        offer.setChildCode4(record[9]);
        offer.setChildCount4(parseInt(record[10]));
        offer.setPeriod(parseInt(record[11]));
        offer.setRegionId(parseLong(record[12]));
        offer.setProfroleGroupId(parseInt(record[13]));
        offer.setPriceAll(parseDouble(record[14]));
        offer.setCurrency(record[15]);
        offerDao.save(offer);
      }
    } catch (Exception e) {
      System.err.println("Error loading offers CSV: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private boolean isHeader(String[] record, String firstColumnName) {
    // simple check: if first cell equals expected column name
    return record[0].contains(firstColumnName);
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
