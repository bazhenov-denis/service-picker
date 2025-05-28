export interface ClaimDto {
  professionId: number;
  amount: number;
  areaId: number;
}

export interface OfferItemDto {
  label: string;
  type: string;
  title: string;
  period: string;
  region: string;
  profroleGroup: string;
  price: string;
  vacancyType: string | null;
  vacancyCount: string | null;
  civCount: string | null;
  apiLimitedCount: string | null;
}

export interface OfferDto {
  items: OfferItemDto[];
}
