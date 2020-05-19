package service;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class InventoryService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReceivingService receivingService;

    @Autowired
    private DeliveryService deliveryService;

    public List<Map<String,String>> searchAllInventoryByDate(Date startDate,Date endDate){
        List<Map<String, String>> stats = new ArrayList<>();
        List<Integer> productList = productService.getProductById();
        int received,delivered;
        String pName;
        for (int id:productList){
            Query receivedInventory = receivingService.searchReceivedSumQuantityByPeriod(startDate,endDate,id);
            if(receivedInventory.uniqueResult() == null){
                received = 0;
            }
            else received = Integer.parseInt(receivedInventory.uniqueResult().toString());
            Query deliveredInventory = deliveryService.searchDeliveredSumQuantityByPeriod(startDate,endDate,id);
            if(deliveredInventory.uniqueResult() == null){
                delivered = 0;
            }
            else delivered = Integer.parseInt(deliveredInventory.uniqueResult().toString());
            Query productName = productService.getProductByName(id);
            if(productName.uniqueResult() == null){
                pName = "";
            }
            else pName = productName.uniqueResult().toString();
            Map<String,String> inventory = new HashMap<>();
            inventory.put("ProductID",Integer.toString(id));
            inventory.put("ProductName",pName);
            inventory.put("Received",Integer.toString(received));
            inventory.put("Delivered",Integer.toString(delivered));
            inventory.put("Balance",Integer.toString(received-delivered));
            stats.add(inventory);
        }
        return stats;
    }
}
