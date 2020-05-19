package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.DeliveryNote;
import model.SaleInvoice;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class DeliveryControllerTest {

    @DisplayName("Add delivery note")
    @Test
    void addDeliveryNote() {
        try{
            URL url = new URL(TestConfig.URL+"delivery");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\delivery.json");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            try (OutputStream os = httpURLConnection.getOutputStream()){
                byte[] input = sb.toString().getBytes(StandardCharsets.UTF_8);
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

    @DisplayName("Add Delivery Note Details")
    @Test
    void addDeliveryNoteDetails() {
        try{
            URL url = new URL(TestConfig.URL+"delivery/details");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\deliveryNote.json");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            try (OutputStream os = httpURLConnection.getOutputStream()){
                byte[] input = sb.toString().getBytes(StandardCharsets.UTF_8);
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

    @DisplayName("Update Delivery Note")
    @Test
    void updateDeliveryNote() {
        try{
            URL url = new URL(TestConfig.URL+"delivery");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "\t\"id\":3,\n" +
                    "    \"staff\": {\"id\": 2},\n" +
                    "    \"date\": \"2020-03-17\"\n" +
                    "}";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run Add Delivery")
    @Test
    void testUpdateName() throws IOException {
        int id = 3;
        URL url = new URL(TestConfig.URL + "delivery/"+id);
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
        DeliveryNote deliveryNote = gson.fromJson(json, new TypeToken<DeliveryNote>(){}.getType());
        Assert.assertEquals("2020-03-17",deliveryNote.getDate().toString());
    }

    @DisplayName("Delete Delivery Note")
    @Test
    void deleteDeliveryNote() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"delivery/"+id);
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

    @DisplayName("Paging by Date")
    @Test
    void pagingSearchDeliveryNoteByDate() {
        try {
            int limit = 5,start = 0;
            URL url = new URL(TestConfig.URL +"delivery/date?date=20200316&limit="+limit+"&start="+start);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while((line = br.readLine()) !=null){
                stringBuilder.append(line);
            }
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            String json = stringBuilder.toString();
            List<DeliveryNote> deliveryNotes = gson.fromJson(json, new TypeToken<List<DeliveryNote>>(){}.getType());
            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();

            list1.put("Date",deliveryNotes.get(0).getDate().toString());
            list1.put("Staff",Integer.toString(deliveryNotes.get(0).getStaff().getId()));

            stringList.add(list1);

            List<String> str = new ArrayList<>();
            str.add("{Staff=1, Date=2020-03-16}");
            Assert.assertEquals(str.get(0),stringList.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @DisplayName("Search by Period")
    @Test
    void searchByPeriodOfDate() {
        try {
            int limit = 5,start = 0;
            URL url = new URL(TestConfig.URL +"delivery/period?startDate=20200316&endDate=20200317&limit="+limit+"&start="+start);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while((line = br.readLine()) !=null){
                stringBuilder.append(line);
            }
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            String json = stringBuilder.toString();
            List<DeliveryNote> deliveryNotes = gson.fromJson(json, new TypeToken<List<DeliveryNote>>(){}.getType());
            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            HashMap<String,String> list2 = new HashMap<>();

            list1.put("Date",deliveryNotes.get(0).getDate().toString());
            list1.put("Staff",Integer.toString(deliveryNotes.get(0).getStaff().getId()));
            list2.put("Date",deliveryNotes.get(1).getDate().toString());
            list2.put("Staff",Integer.toString(deliveryNotes.get(1).getStaff().getId()));
            stringList.add(list1);
            stringList.add(list2);

            List<String> str = new ArrayList<>();
            str.add("{Staff=1, Date=2020-03-16}");
            str.add("{Staff=4, Date=2020-03-17}");

            Assert.assertEquals(str.get(0),stringList.get(0).toString());
            Assert.assertEquals(str.get(1),stringList.get(1).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}