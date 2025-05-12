interface ClaimDto {
  professionId: number;
  amount: number;
  areaId: number;
}

interface OfferDto {
  vacancyOfferDtos: Array<{
    id: number;
    areaId: number;
    professionId: number;
    packageVolume: number;
    vacancyType: string;
    publicationPeriod: number;
    pricePerOne: number;
    pricePerPackage: number;
  }>;
  resumesAccessOfferDtos: Array<{
    id: number;
    areaId: number;
    professionId: number;
    accessDuration: number;
    numberOfContacts: number;
    price: number;
  }>;
}

const API_BASE_URL = 'http://localhost:8080';

export const serviceApi = {
  async sendServiceRequest(data: ClaimDto): Promise<OfferDto> {
    const response = await fetch(`${API_BASE_URL}/service-offer`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Origin': 'http://localhost:3000'
      },
      credentials: 'include',
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error(`Ошибка при отправке запроса: ${response.statusText}`);
    }

    return response.json();
  }
}; 