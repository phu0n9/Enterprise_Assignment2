package controller;

import model.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ProviderService;

import java.util.List;

@RestController
@RequestMapping(path = "/")
public class ProviderController {
    @Autowired
    private ProviderService providerService;

    @RequestMapping(path = "provider", method = RequestMethod.POST)
    public List<Provider> addProvider(@RequestBody List<Provider> providers) {
        return providerService.saveProvider(providers);
    }

    @RequestMapping(path = "provider", method = RequestMethod.PUT)
    public Provider updateProvider(@RequestBody Provider provider) {
        return providerService.updateProvider(provider);
    }

    @RequestMapping(path = "provider/{id}",method = RequestMethod.DELETE)
    public int deleteProvider(@PathVariable int id){
        return providerService.deleteProvider(id);
    }

    @RequestMapping(path = "provider", method = RequestMethod.GET)
    public List<Provider> pagingSearch(@RequestParam Integer limit, @RequestParam Integer start){
        return providerService.pagingProviderSearch(limit,start);
    }
}
