package com.mycompany.maven.ejb;

import com.mycompany.maven.dao.DAO;
import com.mycompany.maven.model.Role;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Manoel
 */
@Stateless
public class RoleBean extends DAO<Role> {
    public List<Role> findAll(boolean includeAdmin) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Role> query = criteriaBuilder.createQuery(Role.class);
        Root<Role> from = query.from(Role.class);
        
        query.select(from);
        
        if (!includeAdmin) {
            Predicate equalAdmin = criteriaBuilder.equal(from.get("id"), 1);
            query.where(criteriaBuilder.not(equalAdmin));
        }
        
        return em.createQuery(query).getResultList();
    }
}

