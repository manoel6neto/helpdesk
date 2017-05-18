package com.mycompany.maven.servlet;

import com.mycompany.maven.ejb.UserBean;
import com.mycompany.maven.model.User;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Manoel
 */
@WebFilter(filterName = "PasswordFilter", urlPatterns = {"/changePassword/*"})
public class ChangePasswordFilter implements Filter {

    public static final String CHANGE_PASSWORD_USER = "changePasswordUser";
    @EJB
    private UserBean userBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String email = req.getParameter("e");
        String token = req.getParameter("t");

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            email = new String(decoder.decodeBuffer(email));
        } catch (IOException ex) {
        }

        User u = userBean.findByEmailAndPassword(email, token);
        if (u != null) {
            req.getSession().setAttribute(CHANGE_PASSWORD_USER, u);
            resp.sendRedirect(req.getServletContext().getContextPath() + "/user/newPasswordForm.xhtml");
        } else {
            RequestDispatcher rd = req.getRequestDispatcher("/teste.xhtml");
            rd.forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
