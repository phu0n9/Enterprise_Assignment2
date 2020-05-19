package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.InventoryService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(path = "inventory/period",method = RequestMethod.GET)
    public List<Map<String,String>> searchRevenue(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date sd, @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date ed){
        return inventoryService.searchAllInventoryByDate(sd,ed);
    }
}
