package controller;

import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ProductService;

import java.util.List;

@RequestMapping(path = "/")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(path = "product", method = RequestMethod.POST)
    public List<Product> addProduct(@RequestBody List<Product> products) {
        return productService.saveProduct(products);
    }

    @RequestMapping(path = "product", method = RequestMethod.PUT)
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @RequestMapping(path = "product/{id}",method = RequestMethod.DELETE)
    public int deleteProduct(@PathVariable int id){
        return productService.deleteProduct(id);
    }

    @RequestMapping(path = "product", method = RequestMethod.GET)
    public List<Product> pagingSearch(@RequestParam Integer limit, @RequestParam Integer start){
        return productService.pagingProductSearch(limit,start);
    }
}
