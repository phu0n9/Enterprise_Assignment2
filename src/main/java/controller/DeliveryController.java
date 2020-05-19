package controller;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import service.DeliveryService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/")
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(path = "delivery",method = RequestMethod.POST)
    public List<DeliveryNote> addDeliveryNote(@RequestBody List<DeliveryNote> deliveryNote){
        return deliveryService.saveDeliveryNote(deliveryNote);
    }

    @RequestMapping(path = "delivery",method = RequestMethod.PUT)
    public DeliveryNote updateDeliveryNote(@RequestBody DeliveryNote deliveryNote){
        return deliveryService.updateDeliveryNote(deliveryNote);
    }

    @RequestMapping(path = "delivery/{id}",method = RequestMethod.GET)
    public DeliveryNote getDeliveryNote(@PathVariable int id){
        return deliveryService.getDeliveryNote(id);
    }

    @RequestMapping(path = "delivery/{id}",method = RequestMethod.DELETE)
    public int deleteDeliveryNote(@PathVariable int id){
        return deliveryService.deleteDeliveryNote(id);
    }

    @RequestMapping(path = "delivery/details",method = RequestMethod.GET)
    public List<DeliveryNoteDetails> getDeliveryDetailsByNote(@RequestParam Integer id){
        return deliveryService.getDeliveryNoteDetailsByNote(id);
    }

    @RequestMapping(path = "delivery/details",method = RequestMethod.POST)
    public List<DeliveryNoteDetails> addDeliveryNoteDetails (@RequestBody List<DeliveryNoteDetails> deliveryNoteDetails){
        return deliveryService.saveDeliveryNoteDetails(deliveryNoteDetails);
    }

    @RequestMapping(path = "delivery/date", method = RequestMethod.GET)
    public List<DeliveryNote> pagingSearchDeliveryNoteByDate(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date date, @RequestParam Integer limit, @RequestParam Integer start){
        return deliveryService.multipleSearchByDate(date,limit,start);
    }

    @RequestMapping(path = "delivery/period", method = RequestMethod.GET)
    public List<DeliveryNote> searchByPeriodOfDate(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date endDate, @RequestParam Integer limit, @RequestParam Integer start){
        return deliveryService.searchByPeriodOfDate(startDate,endDate,limit,start);
    }

}
