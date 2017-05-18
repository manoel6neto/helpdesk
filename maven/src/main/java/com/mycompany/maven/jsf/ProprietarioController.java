package com.mycompany.maven.jsf;

import com.mycompany.maven.ejb.ProprietarioBean;
import com.mycompany.maven.model.Proprietario;
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
public abstract class ProprietarioController implements Serializable {

    @EJB
    private Proprietario proprietario;

    private ProprietarioBean proprietarioBean;

}
