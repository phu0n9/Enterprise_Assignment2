package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Customer;
import model.SaleInvoice;
import model.SaleInvoiceDetails;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaleInvoiceControllerTest {

    @DisplayName("Add Sale Invoice")
    @Test
    void addSaleInvoice() {
        try{
            URL url = new URL(TestConfig.URL+"sale");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\sale.json");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String str = sb.toString();

            try (OutputStream os = httpURLConnection.getOutputStream()){
                byte[] input = str.getBytes(StandardCharsets.UTF_8);
                os.write(input,0,input.length);
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                    System.out.println(response.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Update sale invoice")
    @Test
    void updateSaleInvoice() {
        try{
            URL url = new URL(TestConfig.URL+"sale");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "    \"id\": 3,\n" +
                    "    \"invoiceDate\": \"2020-03-16\",\n" +
                    "    \"staff\": {\n" +
                    "      \"id\": 4\n" +
                    "    },\n" +
                    "    \"customer\": {\n" +
                    "      \"id\": 1\n" +
                    "    },\n" +
                    "    \"deliveryNote\": {\"id\": 1},\n" +
                    "    \"saleInvoiceDetails\": [\n" +
                    "      {\n" +
                    "        \"product\": {\"id\": 4}\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run Add Sale Invoice")
    @Test
    void testUpdateName() throws IOException {
        int id = 3;
        URL url = new URL(TestConfig.URL + "sale/"+id);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) !=null){
            stringBuilder.append(line);
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = stringBuilder.toString();
        SaleInvoice saleInvoice = gson.fromJson(json, new TypeToken<SaleInvoice>(){}.getType());
        Assert.assertEquals("2020-03-16",saleInvoice.getInvoiceDate().toString());
    }

    @DisplayName("Delete a sale invoice")
    @Test
    void deleteSaleInvoice() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"sale/"+id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Accept","application/json");
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.setRequestMethod("DELETE");

            System.out.println("Response code: " + httpURLConnection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                Assert.assertEquals(Integer.toString(id),line);
            }
            br.close();
            httpURLConnection.disconnect();

        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @DisplayName("Search Sale invoice By a Date")
    @Test
    void multipleSearchByDate() throws IOException {
        int limit = 5,start = 0;
        URL url = new URL(TestConfig.URL + "sale/date?date=20200315&limit="+limit+"&start="+start);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) !=null){
            stringBuilder.append(line);
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = stringBuilder.toString();
        List<SaleInvoice> saleInvoice = gson.fromJson(json, new TypeToken<List<SaleInvoice>>(){}.getType());

        List<HashMap<String,String>> stringList = new ArrayList<>();
        HashMap<String,String> list1 = new HashMap<>();

        List<String> strings = new ArrayList<>();
        strings.add("{Customer ID=2, Invoice Date=2020-03-15, Total Value=15000, Customer Name=Taylor Smith, Staff ID=1, Staff Name=Hannah Montana}");
        list1.put("Customer ID",Integer.toString(saleInvoice.get(0).getCustomer().getId()));
        list1.put("Customer Name",saleInvoice.get(0).getCustomer().getName());
        list1.put("Staff ID",Integer.toString(saleInvoice.get(0).getStaff().getId()));
        list1.put("Staff Name",saleInvoice.get(0).getStaff().getName());
        list1.put("Invoice Date",saleInvoice.get(0).getInvoiceDate().toString());
        list1.put("Total Value",Integer.toString(saleInvoice.get(0).getTotalValue()));
        stringList.add(list1);

        Assert.assertEquals(strings.get(0),stringList.get(0).toString());
    }

    @DisplayName("Search By Period of Date")
    @Test
    void searchByPeriodOfDate() throws IOException {
        int limit = 5,start = 0;
        URL url = new URL(TestConfig.URL + "sale/period?startDate=20200313&endDate=20200315&limit="+limit+"&start="+start);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) !=null){
            stringBuilder.append(line);
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = stringBuilder.toString();
        List<SaleInvoice> saleInvoice = gson.fromJson(json, new TypeToken<List<SaleInvoice>>(){}.getType());

        List<HashMap<String,String>> stringList = new ArrayList<>();
        HashMap<String,String> list1 = new HashMap<>();
        HashMap<String,String> list2 = new HashMap<>();
        HashMap<String,String> list3 = new HashMap<>();


        List<String> strings = new ArrayList<>();
        strings.add("{Customer ID=2, Invoice Date=2020-03-15, Total Value=15000, Customer Name=Taylor Smith, Staff ID=1, Staff Name=Hannah Montana}");
        strings.add("{Customer ID=1, Invoice Date=2020-03-13, Total Value=3900, Customer Name=Michael Johnson, Staff ID=2, Staff Name=Kimmy Schmidt}");
        strings.add("{Customer ID=1, Invoice Date=2020-03-14, Total Value=15000, Customer Name=Michael Johnson, Staff ID=2, Staff Name=Kimmy Schmidt}");
        list1.put("Customer ID",Integer.toString(saleInvoice.get(0).getCustomer().getId()));
        list1.put("Customer Name",saleInvoice.get(0).getCustomer().getName());
        list1.put("Staff ID",Integer.toString(saleInvoice.get(0).getStaff().getId()));
        list1.put("Staff Name",saleInvoice.get(0).getStaff().getName());
        list1.put("Invoice Date",saleInvoice.get(0).getInvoiceDate().toString());
        list1.put("Total Value",Integer.toString(saleInvoice.get(0).getTotalValue()));
        list2.put("Customer ID",Integer.toString(saleInvoice.get(1).getCustomer().getId()));
        list2.put("Customer Name",saleInvoice.get(1).getCustomer().getName());
        list2.put("Staff ID",Integer.toString(saleInvoice.get(1).getStaff().getId()));
        list2.put("Staff Name",saleInvoice.get(1).getStaff().getName());
        list2.put("Invoice Date",saleInvoice.get(1).getInvoiceDate().toString());
        list2.put("Total Value",Integer.toString(saleInvoice.get(1).getTotalValue()));
        list3.put("Customer ID",Integer.toString(saleInvoice.get(2).getCustomer().getId()));
        list3.put("Customer Name",saleInvoice.get(2).getCustomer().getName());
        list3.put("Staff ID",Integer.toString(saleInvoice.get(2).getStaff().getId()));
        list3.put("Staff Name",saleInvoice.get(2).getStaff().getName());
        list3.put("Invoice Date",saleInvoice.get(2).getInvoiceDate().toString());
        list3.put("Total Value",Integer.toString(saleInvoice.get(2).getTotalValue()));
        stringList.add(list1);
        stringList.add(list2);
        stringList.add(list3);

        Assert.assertEquals(strings.get(0),stringList.get(0).toString());
        Assert.assertEquals(strings.get(1),stringList.get(1).toString());
        Assert.assertEquals(strings.get(2),stringList.get(2).toString());

    }

    @DisplayName("Search By Period of Date with Staff")
    @Test
    void searchStaffByPeriodOfDate() throws IOException {
        int limit = 5,start = 0,staffID = 2;
        URL url = new URL(TestConfig.URL + "sale/period/sid="+staffID+"?startDate=20200313&endDate=20200315&limit="+limit+"&start="+start);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) !=null){
            stringBuilder.append(line);
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = stringBuilder.toString();
        List<SaleInvoice> saleInvoice = gson.fromJson(json, new TypeToken<List<SaleInvoice>>(){}.getType());

        List<HashMap<String,String>> stringList = new ArrayList<>();
        HashMap<String,String> list1 = new HashMap<>();
        HashMap<String,String> list2 = new HashMap<>();


        List<String> strings = new ArrayList<>();
        strings.add("{Customer ID=1, Invoice Date=2020-03-13, Total Value=3900, Customer Name=Michael Johnson, Staff ID=2, Staff Name=Kimmy Schmidt}");
        strings.add("{Customer ID=1, Invoice Date=2020-03-14, Total Value=15000, Customer Name=Michael Johnson, Staff ID=2, Staff Name=Kimmy Schmidt}");

        list1.put("Customer ID",Integer.toString(saleInvoice.get(0).getCustomer().getId()));
        list1.put("Customer Name",saleInvoice.get(0).getCustomer().getName());
        list1.put("Staff ID",Integer.toString(saleInvoice.get(0).getStaff().getId()));
        list1.put("Staff Name",saleInvoice.get(0).getStaff().getName());
        list1.put("Invoice Date",saleInvoice.get(0).getInvoiceDate().toString());
        list1.put("Total Value",Integer.toString(saleInvoice.get(0).getTotalValue()));

        list2.put("Customer ID",Integer.toString(saleInvoice.get(1).getCustomer().getId()));
        list2.put("Customer Name",saleInvoice.get(1).getCustomer().getName());
        list2.put("Staff ID",Integer.toString(saleInvoice.get(1).getStaff().getId()));
        list2.put("Staff Name",saleInvoice.get(1).getStaff().getName());
        list2.put("Invoice Date",saleInvoice.get(1).getInvoiceDate().toString());
        list2.put("Total Value",Integer.toString(saleInvoice.get(1).getTotalValue()));

        stringList.add(list1);
        stringList.add(list2);
        Assert.assertEquals(strings.get(0),stringList.get(0).toString());
        Assert.assertEquals(strings.get(1),stringList.get(1).toString());
    }

    @DisplayName("Search By Period Of Date with Customer ID")
    @Test
    void searchCustomerByPeriodOfDate() throws IOException {
        int limit = 10,start = 0,customerID = 1;
        URL url = new URL(TestConfig.URL + "sale/period/cid="+customerID+"?startDate=20200313&endDate=20200315&limit="+limit+"&start="+start);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) !=null){
            stringBuilder.append(line);
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = stringBuilder.toString();
        List<SaleInvoice> saleInvoice = gson.fromJson(json, new TypeToken<List<SaleInvoice>>(){}.getType());

        List<HashMap<String,String>> stringList = new ArrayList<>();
        HashMap<String,String> list1 = new HashMap<>();
        HashMap<String,String> list2 = new HashMap<>();

        List<String> strings = new ArrayList<>();
        strings.add("{Customer ID=1, Invoice Date=2020-03-13, Total Value=3900, Customer Name=Michael Johnson, Staff ID=2, Staff Name=Kimmy Schmidt}");
        strings.add("{Customer ID=1, Invoice Date=2020-03-14, Total Value=15000, Customer Name=Michael Johnson, Staff ID=2, Staff Name=Kimmy Schmidt}");

        list1.put("Customer ID",Integer.toString(saleInvoice.get(0).getCustomer().getId()));
        list1.put("Customer Name",saleInvoice.get(0).getCustomer().getName());
        list1.put("Staff ID",Integer.toString(saleInvoice.get(0).getStaff().getId()));
        list1.put("Staff Name",saleInvoice.get(0).getStaff().getName());
        list1.put("Invoice Date",saleInvoice.get(0).getInvoiceDate().toString());
        list1.put("Total Value",Integer.toString(saleInvoice.get(0).getTotalValue()));

        list2.put("Customer ID",Integer.toString(saleInvoice.get(1).getCustomer().getId()));
        list2.put("Customer Name",saleInvoice.get(1).getCustomer().getName());
        list2.put("Staff ID",Integer.toString(saleInvoice.get(1).getStaff().getId()));
        list2.put("Staff Name",saleInvoice.get(1).getStaff().getName());
        list2.put("Invoice Date",saleInvoice.get(1).getInvoiceDate().toString());
        list2.put("Total Value",Integer.toString(saleInvoice.get(1).getTotalValue()));

        stringList.add(list1);
        stringList.add(list2);

        Assert.assertEquals(strings.get(0),stringList.get(0).toString());
        Assert.assertEquals(strings.get(1),stringList.get(1).toString());
    }

    @DisplayName("Search By revenue")
    @Test
    void searchRevenue() throws IOException {
        int limit = 10,start = 0,customerID = 1,staffID=2;
        URL url = new URL(TestConfig.URL + "sale/id="+customerID+"/id="+staffID+"/period?startDate=20200313&endDate=20200315&limit="+limit+"&start="+start);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) !=null){
            stringBuilder.append(line);
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = stringBuilder.toString();
        List<Map<String,String>> mapList = gson.fromJson(json,new TypeToken<List<Map<String,String>>>(){}.getType());

        for(Map<String,String> map: mapList){
            System.out.println(map);
        }
    }
}