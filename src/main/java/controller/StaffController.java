package controller;

import model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.StaffService;

import java.util.List;

@RestController
@RequestMapping(path = "/")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @RequestMapping(path = "staff", method = RequestMethod.POST)
    public List<Staff> addStaff(@RequestBody List<Staff> staff) {
        return staffService.saveStaff(staff);
    }

    @RequestMapping(path = "staff",method = RequestMethod.PUT)
    public Staff updateStaff(@RequestBody Staff staff){
        return staffService.updateStaff(staff);
    }

    @RequestMapping(path = "staff/{id}",method = RequestMethod.DELETE)
    public int deleteStaff(@PathVariable int id){
        return staffService.deleteStaff(id);
    }

    @RequestMapping(path = "staff", method = RequestMethod.GET)
    public List<Staff> pagingSearch(@RequestParam Integer limit,@RequestParam Integer start){
        return staffService.pagingStaffSearch(limit,start);
    }

    @RequestMapping(path = "staff/name={name}", method = RequestMethod.GET)
    public List<Staff> getStaffByName(@PathVariable String name,@RequestParam Integer limit, @RequestParam Integer start){
        return staffService.multipleSearchByName(name,limit,start);
    }

    @RequestMapping(path = "staff/address={address}", method = RequestMethod.GET)
    public List<Staff> getStaffByAddress(@PathVariable String address,@RequestParam Integer limit, @RequestParam Integer start){
        return staffService.multipleSearchByAddress(address,limit,start);
    }

}
