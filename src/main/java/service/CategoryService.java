package service;

import model.Category;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class CategoryService {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Category> saveCategory(List<Category> category){
        for(Category category1: category){
            sessionFactory.getCurrentSession().save(category1);
        }
        return category;
    }

    public Category updateCategory(Category category){
        sessionFactory.getCurrentSession().update(category);
        return category;
    }

    public Category getCategory(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Category where id=:id");
        query.setInteger("id", id);
        return (Category) query.uniqueResult();
    }

    public int deleteCategory(int id){
        Category category = getCategory(id);
        this.sessionFactory.getCurrentSession().delete(category);
        return id;
    }

    public List<Category> pagingCategorySearch(int i, int j){
        Query query = sessionFactory.getCurrentSession().createQuery("from Category order by id").setMaxResults(i).setFirstResult(j);
        return query.list();
    }
}
