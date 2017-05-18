package com.mycompany.maven.jsf;

import com.mycompany.maven.model.Role;
import com.mycompany.maven.model.User;
import com.mycompany.maven.servlet.SecurityFilter;
import com.mycompany.maven.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Thadeu
 */
public abstract class BaseController implements Serializable {

    protected void putFlash(String name, Object obj) {
        JsfUtil.putFlash(name, obj);
    }

    protected Object getFlash(String name) {
        return JsfUtil.getFlash(name);
    }

    public User getSessionUser() {
        return (User) JsfUtil.getSessionAttribute(SecurityFilter.USER_KEY);
    }

    public boolean isUserInRole(String roleName) {
        User tempUser = getSessionUser();
        if (tempUser != null) {
            if (tempUser.getRole().getName().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

}
