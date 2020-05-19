package controller;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import service.SaleService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
public class SaleInvoiceController {
    @Autowired
    private SaleService saleService;

    @RequestMapping(path = "sale",method = RequestMethod.POST)
    public List<SaleInvoice> addSaleInvoice(@RequestBody List<SaleInvoice> saleInvoice){
        return saleService.saveSaleInvoice(saleInvoice);
    }

    @RequestMapping(path = "sale",method = RequestMethod.PUT)
    public SaleInvoice updateSaleInvoice(@RequestBody SaleInvoice saleInvoice){
        return saleService.updateSaleInvoice(saleInvoice);
    }

    @RequestMapping(path = "sale/{id}",method = RequestMethod.GET)
    public SaleInvoice getSaleInvoice(@PathVariable int id){
        return saleService.getSaleInvoice(id);
    }

    @RequestMapping(path = "sale/{id}",method = RequestMethod.DELETE)
    public int deleteSaleInvoice(@PathVariable int id){
        return saleService.deleteSaleInvoice(id);
    }


    @RequestMapping(path = "sale/date", method = RequestMethod.GET)
    public List<SaleInvoice> multipleSearchByDate(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date date, @RequestParam Integer limit, @RequestParam Integer start){
        return saleService.multipleSearchByDate(date,limit,start);
    }

    @RequestMapping(path = "sale/period", method = RequestMethod.GET)
    public List<SaleInvoice> searchByPeriodOfDate(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date endDate, @RequestParam Integer limit, @RequestParam Integer start){
        return saleService.searchByPeriodOfDate(startDate,endDate,limit,start);
    }

    @RequestMapping(path = "sale/period/sid={staffId}", method = RequestMethod.GET)
    public List<SaleInvoice> searchStaffByPeriodOfDate(@PathVariable Integer staffId,@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date endDate, @RequestParam Integer limit, @RequestParam Integer start){
        return saleService.searchStaffByPeriodOfDate(staffId,startDate,endDate,limit,start);
    }

    @RequestMapping(path = "sale/period/cid={customerId}", method = RequestMethod.GET)
    public List<SaleInvoice> searchCustomerByPeriodOfDate(@PathVariable Integer customerId,@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date endDate, @RequestParam Integer limit, @RequestParam Integer start){
        return saleService.searchCustomerByPeriodOfDate(customerId,startDate,endDate,limit,start);
    }

    @RequestMapping(path = "sale/id={customerId}/id={staffId}/period",method = RequestMethod.GET)
    public List<Map<String, String>> searchRevenue(@PathVariable Integer customerId, @PathVariable Integer staffId, @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date endDate){
        return saleService.revenueByDateByStaffAndCustomer(customerId,staffId,startDate,endDate);
    }

}
