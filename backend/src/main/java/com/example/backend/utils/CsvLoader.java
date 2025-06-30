package com.example.backend.utils;

import com.example.backend.DAO.OfferDao;
import com.example.backend.models.Offer;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CsvLoader {

  private final OfferDao offerDao;
  private final Logger log = LoggerFactory.getLogger(CsvLoader.class);

  public CsvLoader(OfferDao offerDao) {
    this.offerDao = offerDao;
  }

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
      if (!records.isEmpty() && isHeader(records.get(0))) {
        records.remove(0);
      }
      for (String[] record : records) {
        if (record.length < 16) continue;
        Offer offer = mapToOffer(record);
        offerDao.save(offer);
      }
    } catch (Exception e) {
      log.error("Error loading offers CSV: {}", e.getMessage());
    }
  }

  private boolean isHeader(String[] record) {
    return record[0].contains("product_id");
  }


  private Offer mapToOffer(String[] r) {
    Offer o = new Offer();
    o.setProductId        (parse(r[0], Long::parseLong));
    o.setTariff           (r[1].trim());
    o.setCode             (r[2].trim());
    o.setChildCode1       (r[3].trim());
    o.setChildCount1      (parse(r[4], Integer::parseInt));
    o.setChildCode2       (r[5].trim());
    o.setChildCount2      (parse(r[6], Integer::parseInt));
    o.setChildCode3       (r[7].trim());
    o.setChildCount3      (parse(r[8], Integer::parseInt));
    o.setChildCode4       (r[9].trim());
    o.setChildCount4      (parse(r[10], Integer::parseInt));
    o.setPeriod           (parse(r[11], Integer::parseInt));
    o.setRegionId         (parse(r[12], Long::parseLong));
    o.setProfroleGroupId  (parse(r[13], Integer::parseInt));
    o.setPriceAll         (parse(r[14], Double::parseDouble));
    o.setCurrency         (r[15].trim());
    return o;
  }

  private <T> T parse(String s, Function<String,T> fn) {
    if (s == null) return null;
    String t = s.replace("\uFEFF","").trim();
    if (t.isEmpty()) return null;
    try { return fn.apply(t); }
    catch (Exception ex) {
      log.warn("Не распарсить '{}' → {}", s, ex.getMessage());
      return null;
    }
  }
}
