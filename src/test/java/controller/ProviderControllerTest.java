package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Provider;
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

class ProviderControllerTest {

    @Test
    @DisplayName("Add Provider")
    void addProvider() {
        try{
            URL url = new URL(TestConfig.URL+"provider");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\provider.json");
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

    @DisplayName("Update Provider")
    @Test
    void updateProvider() {
        try{
            URL url = new URL(TestConfig.URL+"provider");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "\t\"id\":3,\n" +
                    "    \"name\": \"new name\",\n" +
                    "    \"address\": \"amazon street\",\n" +
                    "    \"phone\": 736,\n" +
                    "    \"fax\": 1874,\n" +
                    "    \"email\": \"@amazon.vn\",\n" +
                    "    \"contactPerson\": \"Khanh Nguyen\"\n" +
                    "}";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run add provider")
    @Test
    void testUpdateName() throws IOException {
        int limit = 10,start = 0;
        URL url = new URL(TestConfig.URL + "provider?limit="+limit+"&start="+start);
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
        List<Provider> providers = gson.fromJson(json, new TypeToken<List<Provider>>(){}.getType());
        Assert.assertEquals("new name",providers.get(2).getName());
    }

    @DisplayName("Delete Provider")
    @Test
    void deleteProvider() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"provider/"+id);
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

    @DisplayName("Search Provider")
    @Test
    void pagingSearch() {
        try {
            int limit = 10,start = 0;
            URL url = new URL(TestConfig.URL + "provider?limit="+limit+"&start="+start);
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
            List<Provider> providers = gson.fromJson(json, new TypeToken<List<Provider>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            HashMap<String,String> list2 = new HashMap<>();
            List<String> strings = new ArrayList<>();
            strings.add("{Contact Person=Han Nguyen, Email=@tiki.vn, Address=tiki street, Phone=3498, Fax=9973, Name=Tiki Corp}");
            strings.add("{Contact Person=Thanh Nguyen, Email=@lazada.vn, Address=lazada street, Phone=9372, Fax=239, Name=Lazada Corp}");

            list1.put("Name",providers.get(0).getName());
            list1.put("Address",providers.get(0).getAddress());
            list1.put("Phone",Integer.toString(providers.get(0).getPhone()));
            list1.put("Fax",Integer.toString(providers.get(0).getFax()));
            list1.put("Email",providers.get(0).getEmail());
            list1.put("Contact Person",providers.get(0).getContactPerson());
            list2.put("Name",providers.get(1).getName());
            list2.put("Address",providers.get(1).getAddress());
            list2.put("Phone",Integer.toString(providers.get(1).getPhone()));
            list2.put("Fax",Integer.toString(providers.get(1).getFax()));
            list2.put("Email",providers.get(1).getEmail());
            list2.put("Contact Person",providers.get(1).getContactPerson());
            stringList.add(list1);
            stringList.add(list2);

            Assert.assertEquals(strings.get(0),stringList.get(0).toString());
            Assert.assertEquals(strings.get(1),stringList.get(1).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}