package com.mycompany.maven.ejb;

import com.mycompany.maven.dao.DAO;
import com.mycompany.maven.model.Imovel;
import javax.ejb.Stateless;

/**
 *
 * @author Manoel
 */
@Stateless
public class ImovelBean extends DAO<Imovel> {
    
}
