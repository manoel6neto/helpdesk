package com.mycompany.maven.jsf;

import com.mycompany.maven.ejb.ImovelBean;
import com.mycompany.maven.model.Imovel;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Manoel
 */
@ManagedBean
@ViewScoped
public abstract class ImovelController implements Serializable {

    @EJB
    private Imovel imovel;

    private ImovelBean imovelBean;

}
