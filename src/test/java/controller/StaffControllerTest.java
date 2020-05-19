package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Staff;
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

class StaffControllerTest {

    @DisplayName("Add staff")
    @Test
    void addStaff() {
        try{
            URL url = new URL(TestConfig.URL+"staff");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\staff.json");
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

    @DisplayName("Update staff")
    @Test
    void updateStaff() {
        try{
            URL url = new URL(TestConfig.URL+"staff");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "    \"id\": 3,\n" +
                    "    \"name\": \"new name\",\n" +
                    "    \"address\": \"Dog\",\n" +
                    "    \"phone\": 772,\n" +
                    "    \"email\": \"@snapchat.com\"\n" +
                    "}";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run Add Staff")
    @Test
    void testUpdateName() throws IOException {
        int limit = 5,start = 0;
        URL url = new URL(TestConfig.URL + "staff?limit="+limit+"&start="+start);
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
        Gson gson = new Gson();
        String json = stringBuilder.toString();
        List<Staff> staff = gson.fromJson(json, new TypeToken<List<Staff>>(){}.getType());
        Assert.assertEquals("new name",staff.get(2).getName());
    }


    @DisplayName("Delete Staff by Id")
    @Test
    void deleteStaff() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"staff/"+id);
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

    @DisplayName("Search in general")
    @Test
    void pagingSearch() {
        try {
            int limit = 2,start = 0;
            URL url = new URL(TestConfig.URL + "staff?limit="+limit+"&start="+start);
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
            Gson gson = new Gson();
            String json = stringBuilder.toString();
            List<Staff> staff = gson.fromJson(json, new TypeToken<List<Staff>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            HashMap<String,String> list2 = new HashMap<>();
            List<String> strings = new ArrayList<>();
            strings.add("{Email=@facebook.com, Address=Hollywood street, Phone=984, Name=Hannah Montana}");
            strings.add("{Email=@twitter.com, Address=Haha street, Phone=326, Name=Kimmy Schmidt}");

            list1.put("Name",staff.get(0).getName());
            list1.put("Address",staff.get(0).getAddress());
            list1.put("Phone",Integer.toString(staff.get(0).getPhone()));
            list1.put("Email",staff.get(0).getEmail());
            list2.put("Name",staff.get(1).getName());
            list2.put("Address",staff.get(1).getAddress());
            list2.put("Phone",Integer.toString(staff.get(1).getPhone()));
            list2.put("Email",staff.get(1).getEmail());
            stringList.add(list1);
            stringList.add(list2);

            Assert.assertEquals(strings.get(0),stringList.get(0).toString());
            Assert.assertEquals(strings.get(1),stringList.get(1).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Search by Name,run this after run add Staff(Run this again after add)")
    @Test
    void getStaffByName() {
        try {
            int limit = 5,start = 0;
            String name = "Marianne";
            URL url = new URL(TestConfig.URL +"staff/name="+name+"?limit="+limit+"&start="+start);
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
            Gson gson = new Gson();
            String json = stringBuilder.toString();
            List<Staff> staff = gson.fromJson(json, new TypeToken<List<Staff>>(){}.getType());
            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            list1.put("Name",staff.get(0).getName());
            list1.put("Address",staff.get(0).getAddress());
            list1.put("Phone",Integer.toString(staff.get(0).getPhone()));
            list1.put("Email",staff.get(0).getEmail());
            stringList.add(list1);
            String str = "{Email=@film.com, Address=Portrait, Phone=3232, Name=Marianne}";
            Assert.assertEquals(str,stringList.get(0).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Search By Address")
    @Test
    void getStaffByAddress() {
        try {
            int limit = 5,start = 0;
            String address = "Portrait";
            URL url = new URL(TestConfig.URL +"staff/address="+address+"?limit="+limit+"&start="+start);
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
            Gson gson = new Gson();
            String json = stringBuilder.toString();
            List<Staff> staff = gson.fromJson(json, new TypeToken<List<Staff>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            list1.put("Name",staff.get(0).getName());
            list1.put("Address",staff.get(0).getAddress());
            list1.put("Phone",Integer.toString(staff.get(0).getPhone()));
            list1.put("Email",staff.get(0).getEmail());
            stringList.add(list1);
            String str = "{Email=@film.com, Address=Portrait, Phone=3232, Name=Marianne}";
            Assert.assertEquals(str,stringList.get(0).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}