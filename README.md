
# Assignment 2 - Enterprise Application Development 


RMIT University Vietnam Course: EEET2580 Enterprise Application Development </br>

Semester: 2020A </br>

Assessment: Assignment 2 -Back-end Website Development- API development</br>

Name: Nguyen Huynh Anh Phuong (s3695662)


## Introduction

This is a set of API of an Ecommerce B2B websites like Alibaba or B2C websites like Tiki
  
## API Features
All the APIs share 4 main activities:
* **Add new items**
* **Update items with respective ID**
* **Delete items with respective ID**
* **Search for items**

Moreover, there are some more APIs that support some following functions:
* In **Order**, **Sale Invoice**, **Receiving Notes** and **DeliveryNote**, users can search by **Date**
* Revenue can be searched in **Sale Invoice**
* **Inventory** of all products can be determined with numbers  

 
## Installation

* Install **Jetty**.
* Set up account for **PostgreSQL** with the same account and password in the class **AppConfig.java**.
* Open the **IntelliJ** and right click to the package Assignment 2 and chose **Terminal**.
* Type **```mvn jetty:run```** in the terminal

## Known bugs

All the bugs are handled successfully.

However, the order for running the test functions are:

* **CategoryControllerTest**
* **CustomerControllerTest**
* **StaffControllerTest**
* **ProductControllerTest**
* **ProviderControllerTest**
* **DeliveryControllerTest**
* **ReceivingNoteControllerTest**
* **SaleInvoiceControllerTest**

**```Attention:```** Should run delete method after other methods have been tested due to the test data in the Json files that I have inserted, so it would notify error because there are some mismatch between the inserted data and the test data

## Acknowledgement

* Mr Thanh's slides and answer from Team
* StackOverflow

