package controller;

import model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.CategoryService;

import java.util.List;

@RequestMapping(path = "/")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(path = "category", method = RequestMethod.POST)
    public List<Category> addCategory(@RequestBody List<Category> categories) {
        return categoryService.saveCategory(categories);
    }

    @RequestMapping(path = "category", method = RequestMethod.PUT)
    public Category updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @RequestMapping(path = "category/{id}",method = RequestMethod.DELETE)
    public int deleteCategory(@PathVariable int id){
        return categoryService.deleteCategory(id);
    }

    @RequestMapping(path = "category", method = RequestMethod.GET)
    public List<Category> pagingSearch(@RequestParam Integer limit, @RequestParam Integer start){
        return categoryService.pagingCategorySearch(limit,start);
    }
}
