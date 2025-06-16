package com.example.backend.query;

import com.example.backend.DTO.OfferDto;
import com.example.backend.enums.OfferChildCodeEnum;
import com.example.backend.models.Offer;
import com.example.backend.services.PriceProfroleService;
import com.example.backend.services.PriceRegionService;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

  private final PriceRegionService priceRegionService;
  private final PriceProfroleService priceProfroleService;

  public OfferMapper(
      PriceRegionService priceRegionService,
      PriceProfroleService priceProfroleService
  ) {
    this.priceRegionService = priceRegionService;
    this.priceProfroleService = priceProfroleService;
  }

  public OfferDto toDto(Offer offer) {
    String code = offer.getCode();
    String period = offer.getPeriod().toString();
    String region = priceRegionService.getRegionNameById(offer.getRegionId());
    String profrole = priceProfroleService.getProfroleNameById(offer.getProfroleGroupId().longValue());
    String price = Double.toString(offer.getPriceAll() / 100.0);
    String label = "Оптимальный";
    return switch (code) {
      case "DI" ->
        // доступ к базе резюме
          new OfferDto(
              label,
              "resume_access",
              "Доступ к базе резюме",
              period,
              region,
              profrole,
              price,
              null,
              null,
              offer.getChildCount1().toString(),
              offer.getChildCount2().toString()
          );
      case "VPPL" -> {
        // публикация вакансий
        String vacancyLabel = OfferChildCodeEnum.getLabelByCode(offer.getChildCode1());
        yield new OfferDto(
            label,
            "vacancy",
            "Публикация вакансий",
            period,
            region,
            profrole,
            price,
            vacancyLabel,
            offer.getChildCount1().toString(),
            null,
            null
        );
      }
      case "CIV+VPPL" -> {
        // смешанный
        String mixedLabel = OfferChildCodeEnum.getLabelByCode(offer.getChildCode2());
        yield new OfferDto(
            label,
            "mixed",
            "Доступ к базе резюме + публикация вакансий",
            period,
            region,
            profrole,
            price,
            mixedLabel,
            offer.getChildCount2().toString(),
            offer.getChildCount1().toString(),
            offer.getChildCount3().toString()
        );
      }
      default -> throw new IllegalArgumentException("Unknown offer code: " + code);
    };
  }
}
