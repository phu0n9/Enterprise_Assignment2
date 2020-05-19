package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Category;
import model.Product;
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

class ProductControllerTest {

    @DisplayName("Add product")
    @Test
    void addProduct() {
        try{
            URL url = new URL(TestConfig.URL+"product");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\product.json");
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

    @DisplayName("Update Product")
    @Test
    void updateProduct() {
        try{
            URL url = new URL(TestConfig.URL+"product");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "\t\"id\":3,\n" +
                    "\t\"name\": \"new name\",\n" +
                    "    \"model\": \"Air\",\n" +
                    "    \"brand\": \"Nike\",\n" +
                    "    \"company\": \"Nike Inc.\",\n" +
                    "    \"description\": \"Its a shoes\",\n" +
                    "    \"price\": 300,\n" +
                    "    \"category\": {\"id\": 2}\n" +
                    "}";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run Add Product")
    @Test
    void testUpdateName() throws IOException {
        int limit = 10,start = 0;
        URL url = new URL(TestConfig.URL + "product?limit="+limit+"&start="+start);
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

    @DisplayName("Delete Product")
    @Test
    void deleteProduct() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"product/"+id);
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

    @DisplayName("Search Product")
    @Test
    void pagingSearch() {
        try {
            int limit = 2,start = 0;
            URL url = new URL(TestConfig.URL + "product?limit="+limit+"&start="+start);
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
            List<Product> products = gson.fromJson(json, new TypeToken<List<Product>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            HashMap<String,String> list2 = new HashMap<>();
            List<String> strings = new ArrayList<>();
            strings.add("{Brand=Apple, Company=Apple Inc., Description=Its an iphone, Price=800, Model=6S Plus, Category_ID=1, Name=Iphone 6S}");
            strings.add("{Brand=VinFast, Company=VinGroup Inc., Description=Its a car, Price=3000, Model=New model, Category_ID=2, Name=VinFast Car}");

            list1.put("Name",products.get(0).getName());
            list1.put("Model",products.get(0).getModel());
            list1.put("Brand",products.get(0).getBrand());
            list1.put("Company",products.get(0).getCompany());
            list1.put("Description",products.get(0).getDescription());
            list1.put("Price",Integer.toString(products.get(0).getPrice()));
            list1.put("Category_ID",Integer.toString(products.get(0).getCategory().getId()));

            list2.put("Name",products.get(1).getName());
            list2.put("Model",products.get(1).getModel());
            list2.put("Brand",products.get(1).getBrand());
            list2.put("Company",products.get(1).getCompany());
            list2.put("Description",products.get(1).getDescription());
            list2.put("Price",Integer.toString(products.get(1).getPrice()));
            list2.put("Category_ID",Integer.toString(products.get(1).getCategory().getId()));

            stringList.add(list1);
            stringList.add(list2);

            Assert.assertEquals(strings.get(0),stringList.get(0).toString());
            Assert.assertEquals(strings.get(1),stringList.get(1).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}