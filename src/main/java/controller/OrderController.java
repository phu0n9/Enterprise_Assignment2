package controller;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import service.OrderService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(path = "order",method = RequestMethod.POST)
    public List<Order> addOrder(@RequestBody List<Order> order){
        return orderService.saveOrder(order);
    }

    @RequestMapping(path = "order",method = RequestMethod.PUT)
    public Order updateOrder(@RequestBody Order order){
        return orderService.updateOrder(order);
    }

    @RequestMapping(path = "order/{id}",method = RequestMethod.GET)
    public Order getOrder(@PathVariable int id){
        return orderService.getOrder(id);
    }

    @RequestMapping(path = "order/{id}",method = RequestMethod.DELETE)
    public int deleteOrder(@PathVariable int id){
        return orderService.deleteOrder(id);
    }

//    @RequestMapping(path = "order/details",method = RequestMethod.GET)
//    public List<OrderDetails> getOrderDetailsByOrder(@RequestParam Integer id){
//        return orderService.getOrderDetailsByOrder(id);
//    }

    @RequestMapping(path = "order/details",method = RequestMethod.POST)
    public List<OrderDetails> addOrderDetails (@RequestBody List<OrderDetails> orderDetails){
        return orderService.saveOrderDetails(orderDetails);
    }

    @RequestMapping(path = "order/date", method = RequestMethod.GET)
    public List<Order> pagingSearchOrderByDate(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date date, @RequestParam Integer limit, @RequestParam Integer start){
        return orderService.multipleSearchByDate(date,limit,start);
    }

}
