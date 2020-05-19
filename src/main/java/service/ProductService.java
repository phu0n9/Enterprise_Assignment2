package service;

import model.Category;
import model.Customer;
import model.Product;
import model.SaleInvoice;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Product> saveProduct(List<Product> product){
        for(Product product1: product){
            sessionFactory.getCurrentSession().save(product1);
        }
        return product;
    }

    public Product updateProduct(Product product){
        sessionFactory.getCurrentSession().update(product);
        return product;
    }

    public Product getProduct(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Product where id=:id");
        query.setInteger("id", id);
        return (Product) query.uniqueResult();
    }

    public int deleteProduct(int id){
        Product product = getProduct(id);
        this.sessionFactory.getCurrentSession().delete(product);
        return id;
    }

    public List<Product> pagingProductSearch(int i, int j){
        Query query = sessionFactory.getCurrentSession().createQuery("from Product order by id").setMaxResults(i).setFirstResult(j);
        return query.list();
    }

    public List<Integer> getProductById(){
        Query query = sessionFactory.getCurrentSession().createQuery("select p.id from Product p order by p.id");
        return query.list();
    }

    public Query getProductByName(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("select p.name from Product p where p.id=:id");
        query.setInteger("id",id);
        return query;
    }

}
