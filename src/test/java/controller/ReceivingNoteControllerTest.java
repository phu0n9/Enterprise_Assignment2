package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.ReceivingNote;
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

import static org.junit.jupiter.api.Assertions.*;

class ReceivingNoteControllerTest {

    @DisplayName("Add Receiving Note")
    @Test
    void addReceivingNote() {
        try{
            URL url = new URL(TestConfig.URL+"receiving");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\receiving.json");
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

    @DisplayName("Update Receiving Note")
    @Test
    void updateReceivingNote() {
        try{
            URL url = new URL(TestConfig.URL+"receiving");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "    \"id\": 3,\n" +
                    "    \"staff\": {\"id\": 2},\n" +
                    "    \"order\": {\"id\": 1},\n" +
                    "    \"receivingDate\": \"2020-03-24\"\n" +
                    "  }";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run Add Receiving Note")
    @Test
    void testUpdateName() throws IOException {
        int id = 3;
        URL url = new URL(TestConfig.URL + "receiving/"+id);
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
        ReceivingNote receivingNote = gson.fromJson(json, new TypeToken<ReceivingNote>(){}.getType());
        Assert.assertEquals("2020-03-24",receivingNote.getReceivingDate().toString());
    }


    @DisplayName("Delete a receiving note")
    @Test
    void deleteReceivingNote() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"receiving/"+id);
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

    @DisplayName("Add Receiving Details")
    @Test
    void addReceivingDetails() {
        try{
            URL url = new URL(TestConfig.URL+"receiving/details");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\receivingNote.json");
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


    @DisplayName("Search by Date")
    @Test
    void pagingSearchReceivingNoteByDate() throws IOException {
        int limit = 5,start = 0;
        URL url = new URL(TestConfig.URL + "receiving/date?date=20200320&limit="+limit+"&start="+start);
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
        List<ReceivingNote> receivingNotes = gson.fromJson(json, new TypeToken<List<ReceivingNote>>(){}.getType());

        List<HashMap<String,String>> stringList = new ArrayList<>();
        HashMap<String,String> list1 = new HashMap<>();

        List<String> strings = new ArrayList<>();
        strings.add("{Provider Name=Shoppe Corp, Order ID=4, Staff ID=2, Receiving Date=2020-03-20, Staff Name=Kimmy Schmidt}");

        list1.put("Order ID",Integer.toString(receivingNotes.get(0).getOrder().getId()));
        list1.put("Provider Name",receivingNotes.get(0).getOrder().getProvider().getName());
        list1.put("Staff ID",Integer.toString(receivingNotes.get(0).getStaff().getId()));
        list1.put("Staff Name",receivingNotes.get(0).getStaff().getName());
        list1.put("Receiving Date",receivingNotes.get(0).getReceivingDate().toString());
        stringList.add(list1);

        Assert.assertEquals(strings.get(0),stringList.get(0).toString());
    }


    @DisplayName("Search By Period")
    @Test
    void searchByPeriodOfDate() throws IOException {
        int limit = 5,start = 0;
        URL url = new URL(TestConfig.URL + "receiving/period?startDate=20200320&endDate=20200323&limit="+limit+"&start="+start);
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
        List<ReceivingNote> receivingNotes = gson.fromJson(json, new TypeToken<List<ReceivingNote>>(){}.getType());

        List<HashMap<String,String>> stringList = new ArrayList<>();
        HashMap<String,String> list1 = new HashMap<>();
        HashMap<String,String> list2 = new HashMap<>();
        HashMap<String,String> list3 = new HashMap<>();


        List<String> strings = new ArrayList<>();
        strings.add("{Provider Name=Shoppe Corp, Order ID=4, Staff ID=2, Receiving Date=2020-03-20, Staff Name=Kimmy Schmidt}");
        strings.add("{Provider Name=Shoppe Corp, Order ID=2, Staff ID=1, Receiving Date=2020-03-21, Staff Name=Hannah Montana}");
        strings.add("{Provider Name=Shoppe Corp, Order ID=4, Staff ID=1, Receiving Date=2020-03-23, Staff Name=Hannah Montana}");

        list1.put("Order ID",Integer.toString(receivingNotes.get(0).getOrder().getId()));
        list1.put("Provider Name",receivingNotes.get(0).getOrder().getProvider().getName());
        list1.put("Staff ID",Integer.toString(receivingNotes.get(0).getStaff().getId()));
        list1.put("Staff Name",receivingNotes.get(0).getStaff().getName());
        list1.put("Receiving Date",receivingNotes.get(0).getReceivingDate().toString());

        list2.put("Order ID",Integer.toString(receivingNotes.get(1).getOrder().getId()));
        list2.put("Provider Name",receivingNotes.get(1).getOrder().getProvider().getName());
        list2.put("Staff ID",Integer.toString(receivingNotes.get(1).getStaff().getId()));
        list2.put("Staff Name",receivingNotes.get(1).getStaff().getName());
        list2.put("Receiving Date",receivingNotes.get(1).getReceivingDate().toString());

        list3.put("Order ID",Integer.toString(receivingNotes.get(2).getOrder().getId()));
        list3.put("Provider Name",receivingNotes.get(2).getOrder().getProvider().getName());
        list3.put("Staff ID",Integer.toString(receivingNotes.get(2).getStaff().getId()));
        list3.put("Staff Name",receivingNotes.get(2).getStaff().getName());
        list3.put("Receiving Date",receivingNotes.get(2).getReceivingDate().toString());

        stringList.add(list1);
        stringList.add(list2);
        stringList.add(list3);

        Assert.assertEquals(strings.get(0),stringList.get(0).toString());
        Assert.assertEquals(strings.get(1),stringList.get(1).toString());
        Assert.assertEquals(strings.get(2),stringList.get(2).toString());

    }
}