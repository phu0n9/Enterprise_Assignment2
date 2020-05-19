package service;

import model.Provider;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ProviderService {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Provider> saveProvider(List<Provider> provider){
        for(Provider provider1: provider){
            sessionFactory.getCurrentSession().save(provider1);
        }
        return provider;
    }

    public Provider updateProvider(Provider provider){
        sessionFactory.getCurrentSession().update(provider);
        return provider;
    }

    public Provider getProvider(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Provider where id=:id");
        query.setInteger("id", id);
        return (Provider) query.uniqueResult();
    }

    public int deleteProvider(int id){
        Provider provider = getProvider(id);
        this.sessionFactory.getCurrentSession().delete(provider);
        return id;
    }

    public List<Provider> pagingProviderSearch(int i, int j){
        Query query = sessionFactory.getCurrentSession().createQuery("from Provider order by id").setMaxResults(i).setFirstResult(j);
        return query.list();
    }
}
