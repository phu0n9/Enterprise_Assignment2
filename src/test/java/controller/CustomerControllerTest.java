package controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Customer;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Config.TestConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


class CustomerControllerTest {


    @DisplayName("Add customer using POST method")
    @Test
    void addCustomer() throws Exception{
        try{
            URL url = new URL(TestConfig.URL+"customer");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);

            InputStream is = new FileInputStream("src\\test\\java\\json\\customer.json");
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


    @DisplayName("Update customer information by ID using PUT method")
    @Test
    void updateCustomer() {
        try{
            URL url = new URL(TestConfig.URL+"customer");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setRequestProperty("Content-Type","application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            String inputString = "{\n" +
                    "    \"id\": 3,\n" +
                    "    \"name\": \"new name\",\n" +
                    "    \"address\": \"Dog\",\n" +
                    "    \"phone\": 342,\n" +
                    "    \"fax\": 2323,\n" +
                    "    \"email\": \"@snapchat.com\",\n" +
                    "    \"contactPerson\": \"Seymour Butts\"\n" +
                    "  }";
            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(inputString);
            out.close();
            httpURLConnection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @DisplayName("Run this after run Add Customer")
    @Test
    void testUpdateName() throws IOException {
        int limit = 5,start = 0;
        URL url = new URL(TestConfig.URL + "customer?limit="+limit+"&start="+start);
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
        List<Customer> customer = gson.fromJson(json, new TypeToken<List<Customer>>(){}.getType());
        Assert.assertEquals("new name",customer.get(2).getName());
    }


    @DisplayName("Delete customer by ID using DELETE method")
    @Test
    void deleteCustomer() {
        try {
            int id = 3;

            URL url = new URL(TestConfig.URL+"customer/"+id);
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
            URL url = new URL(TestConfig.URL + "customer?limit="+limit+"&start="+start);
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
            List<Customer> customer = gson.fromJson(json, new TypeToken<List<Customer>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            HashMap<String,String> list2 = new HashMap<>();
            List<String> strings = new ArrayList<>();
            strings.add("{Contact Person=Mike Scott, Email=@facebook.com, Address=Vietnam street, Phone=342, Fax=937, Name=Michael Johnson}");
            strings.add("{Contact Person=Taylor Smith, Email=@twitter.com, Address=Cat street, Phone=984, Fax=93802, Name=Taylor Smith}");

            list1.put("Name",customer.get(0).getName());
            list1.put("Address",customer.get(0).getAddress());
            list1.put("Phone",Integer.toString(customer.get(0).getPhone()));
            list1.put("Fax",Integer.toString(customer.get(0).getFax()));
            list1.put("Email",customer.get(0).getEmail());
            list1.put("Contact Person",customer.get(0).getContactPerson());
            list2.put("Name",customer.get(1).getName());
            list2.put("Address",customer.get(1).getAddress());
            list2.put("Phone",Integer.toString(customer.get(1).getPhone()));
            list2.put("Fax",Integer.toString(customer.get(1).getFax()));
            list2.put("Email",customer.get(1).getEmail());
            list2.put("Contact Person",customer.get(1).getContactPerson());
            stringList.add(list1);
            stringList.add(list2);

            Assert.assertEquals(strings.get(0),stringList.get(0).toString());
            Assert.assertEquals(strings.get(1),stringList.get(1).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Searching by Name")
    @Test
    void pagingSearchByName() {
        try {
            int limit = 5,start = 0;
            String name = "Heloise";
            URL url = new URL(TestConfig.URL +"customer/name="+name+"?limit="+limit+"&start="+start);
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
            List<Customer> customer = gson.fromJson(json, new TypeToken<List<Customer>>(){}.getType());
            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            list1.put("Name",customer.get(0).getName());
            list1.put("Address",customer.get(0).getAddress());
            list1.put("Phone",Integer.toString(customer.get(0).getPhone()));
            list1.put("Fax",Integer.toString(customer.get(0).getFax()));
            list1.put("Email",customer.get(0).getEmail());
            list1.put("Contact Person",customer.get(0).getContactPerson());
            stringList.add(list1);
            String str = "{Contact Person=Marianne, Email=@film.com, Address=Portrait, Phone=3232, Fax=32322, Name=Heloise}";
            Assert.assertEquals(str,stringList.get(0).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Search By Address")
    @Test
    void pagingSearchByAddress() {
        try {
            int limit = 5,start = 0;
            String address = "Portrait";
            URL url = new URL(TestConfig.URL +"customer/address="+address+"?limit="+limit+"&start="+start);
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
            List<Customer> customer = gson.fromJson(json, new TypeToken<List<Customer>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            list1.put("Name",customer.get(0).getName());
            list1.put("Address",customer.get(0).getAddress());
            list1.put("Phone",Integer.toString(customer.get(0).getPhone()));
            list1.put("Fax",Integer.toString(customer.get(0).getFax()));
            list1.put("Email",customer.get(0).getEmail());
            list1.put("Contact Person",customer.get(0).getContactPerson());
            stringList.add(list1);
            String str = "{Contact Person=Marianne, Email=@film.com, Address=Portrait, Phone=3232, Fax=32322, Name=Heloise}";
            Assert.assertEquals(str,stringList.get(0).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Search by phone")
    @Test
    void pagingSearchByPhone() {
        try {
            int limit = 5,start = 0;
            String phone = "342";
            URL url = new URL(TestConfig.URL +"customer/phone="+phone+"?limit="+limit+"&start="+start);
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
            List<Customer> customer = gson.fromJson(json, new TypeToken<List<Customer>>(){}.getType());

            List<HashMap<String,String>> stringList = new ArrayList<>();
            HashMap<String,String> list1 = new HashMap<>();
            list1.put("Name",customer.get(0).getName());
            list1.put("Address",customer.get(0).getAddress());
            list1.put("Phone",Integer.toString(customer.get(0).getPhone()));
            list1.put("Fax",Integer.toString(customer.get(0).getFax()));
            list1.put("Email",customer.get(0).getEmail());
            list1.put("Contact Person",customer.get(0).getContactPerson());
            stringList.add(list1);

            String str = "{Contact Person=Mike Scott, Email=@facebook.com, Address=Vietnam street, Phone=342, Fax=937, Name=Michael Johnson}";
            Assert.assertEquals(str,stringList.get(0).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}