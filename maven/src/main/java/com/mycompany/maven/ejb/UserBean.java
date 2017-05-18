package com.mycompany.maven.ejb;

import com.mycompany.maven.dao.DAO;
import com.mycompany.maven.model.User;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author Manoel
 */
@Stateless
public class UserBean extends DAO<User> {
    public User findByEmailAndPassword(String email, String senha) {
        TypedQuery<User> namedQuery = getEntityManager().createNamedQuery("User.findByEmailAndPassword", User.class);
        namedQuery.setParameter("email", email);
        namedQuery.setParameter("password", senha);
        
        User user;
        try {
            user = namedQuery.getSingleResult();
        } catch (Exception e) {
            user = null;
        }
        return user;
    }
}
