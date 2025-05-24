export interface ClaimDto {
  professionId: number;
  amount: number;
  areaId: number;
}

export interface OfferDto {
  vacancyOffers: Array<{
    id: number;
    areaId: number;
    professionId: number;
    packageVolume: number;
    vacancyType: string;
    publicationPeriod: number;
    pricePerOne: number;
    pricePerPackage: number;
  }>;
  resumesAccessOffers: Array<{
    id: number;
    areaId: number;
    professionId: number;
    accessDuration: number;
    numberOfContacts: number;
    price: number;
  }>;
} 