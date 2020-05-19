package controller;

import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.CustomerService;

import java.util.List;

@RestController
@RequestMapping(path = "/")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(path = "customer", method = RequestMethod.POST)
    public List<Customer> addCustomer(@RequestBody List<Customer> customer) {
        return customerService.saveCustomer(customer);
    }

    @RequestMapping(path = "customer", method = RequestMethod.PUT)
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @RequestMapping(path = "customer/{id}",method = RequestMethod.DELETE)
    public int deleteCustomer(@PathVariable int id){
        return customerService.deleteCustomer(id);
    }

    @RequestMapping(path = "customer", method = RequestMethod.GET)
    public List<Customer> pagingSearch(@RequestParam Integer limit, @RequestParam Integer start){
        return customerService.pagingCustomerSearch(limit,start);
    }

    @RequestMapping(path = "customer/name={name}", method = RequestMethod.GET)
    public List<Customer> pagingSearchByName(@PathVariable String name,@RequestParam Integer limit, @RequestParam Integer start){
        return customerService.multipleSearchByName(name,limit,start);
    }

    @RequestMapping(path = "customer/address={address}", method = RequestMethod.GET)
    public List<Customer> pagingSearchByAddress(@PathVariable String address,@RequestParam Integer limit, @RequestParam Integer start){
        return customerService.multipleSearchByAddress(address,limit,start);
    }

    @RequestMapping(path = "customer/phone={phone}", method = RequestMethod.GET)
    public List<Customer> pagingSearchByPhone(@PathVariable int phone,@RequestParam Integer limit, @RequestParam Integer start){
        return customerService.multipleSearchByPhone(phone,limit,start);
    }

}
