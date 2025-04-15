<h3>POST /service-offer</h3>
<p>Отправляет данные формы с айди региона, профессии и кол-вом работников для подбора пакета услуг.</p>
<p>
IN:
{
    "professionId": 5,
    "amount": 3,
    "areaId": 6
}
</p>
<p>
OUT:
{
    "vacancyOffers": [
        {
            "areaId": 6,
            "professionId": 5,
            "volumeOfPublicationPackage": 50,
            "vacancyType": "Regular",
            "publicationPeriod": 30,
            "pricePerOne": 100.0,
            "pricePerPackage": 5000.0
        }
    ],
    "resumesAccessOffers": []
}
</p>
<p>или</p>
<p>
{
    "vacancyOffers": [],
    "resumesAccessOffers": [
        {
            "areaId": 6,
            "professionId": 5,
            "accessDuration": 30,
            "numberOfContacts": 100,
            "price": 4500.0
        }
    ]
}
</p>