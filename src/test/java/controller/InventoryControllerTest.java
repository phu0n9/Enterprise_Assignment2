package controller;

import Config.TestConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryControllerTest {

    @DisplayName("Search Items by Revenue")
    @Test
    void searchRevenue() throws IOException {
        int limit = 10,start = 0;
        URL url = new URL(TestConfig.URL + "inventory/period?sd=20200301&ed=20200331&limit="+limit+"&start="+start);
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

        for (Map<String,String> map: mapList){
            System.out.println(map);
        }

    }
}