# customer-content
This project is built on JAVA 21 and Spring Boot.
To Run this project first clone the repo. Then install docker in the local machine. after that go to the file location of the project and execute the docker command => docker compose up -d ) to create the server.
After that go to PGADMIN using which will be running on 5050 port(you can change it from the docker-compsoe.yml file) and create a database name consumer. after that use an IDE to run the spring boot application.
For Charge Config data use this JSON and this end point for bulk save. End Point : http://localhost:8080/api/v1/charge-config/save-bulk-keyword
JSON : [
    {
        "chargeCode": "BL914679",
        "operator": "BANGLALINK"
      },
      {
        "chargeCode": "GP714363",
        "operator": "GRAMEENPHONE"
      },
      {
        "chargeCode": "RO874556",
        "operator": "ROBI"
      },
      {
        "chargeCode": "TL512343",
        "operator": "TELETALK"
      },
      {
        "chargeCode": "AL621124",
        "operator": "AIRTEL"
      }
]
For Keyword details use this end point and JSON For bulk Save: End point: http://localhost:8080/api/v1/keyword-details/save-bulk-keyword
JOSN :
 [
    {
      "keyword": "APPLE"
    },
    {
      "keyword": "GOOGLE"
    },
    {
      "keyword": "MICROSOFT"
    },
    {
      "keyword": "FACEBOOK"
    },
    {
      "keyword": "NETFLIX"
    },
    {
      "keyword": "AMAZON"
    },
    {
      "keyword": "NVIDIA"
    },
    {
      "keyword": "INTEL"
    },
    {
      "keyword": "AMD"
    },
    {
      "keyword": "IBM"
    }
  ]

Use this end point to execute total process. end point : localhost:8080/api/v1/content-retrieval/get

