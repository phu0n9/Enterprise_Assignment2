package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Category;
import model.Customer;
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

class CategoryControllerTest {

    @DisplayName("Add category")
    @Test
    void addCategory() {
        try{
            URL url = new URL(TestConfig.URL+"category");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\category.json");
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

    @DisplayName("Update category")
    @Test
    void updateCategory() {
        try{
            URL url = new URL(TestConfig.URL+"category");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "    \"id\": 3,\n" +
                    "    \"name\": \"new name\"\n" +
                    "}";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run add category")
    @Test
    void testUpdateName() throws IOException {
        int limit = 5,start = 0;
        URL url = new URL(TestConfig.URL + "category?limit="+limit+"&start="+start);
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
        List<Category> categories = gson.fromJson(json, new TypeToken<List<Category>>(){}.getType());
        Assert.assertEquals("new name",categories.get(2).getName());
    }

    @DisplayName("Delete a category")
    @Test
    void deleteCategory() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"category/"+id);
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

    @DisplayName("Search category")
    @Test
    void pagingSearch() {
        try {
            int limit = 2,start = 0;
            URL url = new URL(TestConfig.URL + "category?limit="+limit+"&start="+start);
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
            List<Category> categories = gson.fromJson(json, new TypeToken<List<Category>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            HashMap<String,String> list2 = new HashMap<>();
            List<String> strings = new ArrayList<>();
            strings.add("{Name=Smart phone}");
            strings.add("{Name=Car}");

            list1.put("Name",categories.get(0).getName());
            list2.put("Name",categories.get(1).getName());
            stringList.add(list1);
            stringList.add(list2);

            Assert.assertEquals(strings.get(0),stringList.get(0).toString());
            Assert.assertEquals(strings.get(1),stringList.get(1).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}