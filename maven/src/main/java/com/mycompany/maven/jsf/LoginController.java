package com.mycompany.maven.jsf;

import com.mycompany.maven.ejb.UserBean;
import com.mycompany.maven.model.User;
import com.mycompany.maven.servlet.SecurityFilter;
import com.mycompany.maven.util.Criptografia;
import com.mycompany.maven.util.JsfUtil;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thadeu
 */
@ManagedBean
@ViewScoped
public class LoginController extends BaseController {

    @EJB
    private UserBean userBean;

    private String email;

    private String password;

    public String login() {
        try {
            userBean.clearCache();
            User user = userBean.findByEmailAndPassword(email, Criptografia.criptografar(password));

            if (user != null) {
                if (!user.isActive()) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/bundle").getString("login.inactive"));
                    return "login?faces-redirect=true&userInactive=true";
                }

                user.setLastAccess(new Date());
                userBean.edit(user);

                JsfUtil.setSessionAttribute(SecurityFilter.USER_KEY, user);

                return "index?faces-redirect=true";
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/bundle").getString("login.invalid"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/bundle").getString("login.invalid"));
            return null;
        }
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(SecurityFilter.USER_KEY, null);
        session.invalidate();

        return "/login?faces-redirect=true";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
