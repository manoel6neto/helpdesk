package com.mycompany.maven.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Manoel
 */
public class AbstractFilter {

    public static final String USER_KEY = "USER_KEY";

    public static final String USER_COMPANY_KEY = "USER_COMAPANY_KEY";

    public static final String USER_ROLES_KEY = "USER_ROLES_KEY";

    public AbstractFilter() {
        super();
    }

    protected void doLogin(ServletRequest request, ServletResponse response, HttpServletRequest req) throws ServletException, IOException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.sendRedirect(req.getContextPath() + "/login.xhtml");
    }

    protected void accessDenied(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.sendRedirect(req.getContextPath() + "/acesso_negado.xhtml");
    }

    protected void accessDenied(ServletRequest request, ServletResponse response, HttpServletRequest req) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/acesso_negado.xhtml");
        rd.forward(request, response);
    }
}
