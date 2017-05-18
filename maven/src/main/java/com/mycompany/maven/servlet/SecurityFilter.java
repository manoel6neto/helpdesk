package com.mycompany.maven.servlet;

import com.mycompany.maven.model.Role;
import com.mycompany.maven.model.User;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thadeu
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter extends AbstractFilter implements Filter {

    private final InterceptorUrl[] OPEN_URLS = {
        new InterceptorUrl("/javax.faces.resource/*"),
        new InterceptorUrl("/rest/*"),
        new InterceptorUrl("/home*"),
        new InterceptorUrl("/sendAll*"),
        new InterceptorUrl("/resources/css/*"),
        new InterceptorUrl("/register*"),
        new InterceptorUrl("/unregister*"),
        new InterceptorUrl("/login.xhtml*"),
        new InterceptorUrl("/upload/*"),
        new InterceptorUrl("/activate*"),
        new InterceptorUrl("/changePassword*"),
        new InterceptorUrl("/user/newPasswordForm.xhtml*"),};

    private final InterceptorUrl[] interceptors = {
        new InterceptorUrl("/admin/*", new String[]{"ADMIN"}),};

    private static final boolean DEBUG = true;

    private FilterConfig filterConfig = null;

    public boolean isRestrictedPath(HttpServletRequest req) {
        boolean restricted = true;
        String path = req.getRequestURI();

        while (path.startsWith("//")) {
            path = path.substring(1);
        }

        if (path.startsWith(req.getContextPath())) {
            path = path.replaceFirst(req.getContextPath(), "");
        }

        for (InterceptorUrl interceptor : OPEN_URLS) {
            if (path.matches(interceptor.getUrlPattern())) {
                restricted = false;
                break;
            }
        }

        return restricted;
    }

    public boolean isAuthorized(HttpServletRequest req, List<Role> roleList) {
        boolean authorized = true;

        String path = req.getRequestURI();
        if (path.startsWith(req.getContextPath())) {
            path = path.replaceFirst(req.getContextPath(), "");
        }

        InterceptorUrl matchInterceptor = null;
        for (InterceptorUrl interceptor : interceptors) {
            if (path.matches(interceptor.getUrlPattern())) {
                authorized = false;
                matchInterceptor = interceptor;
                break;
            }
        }

        if (matchInterceptor != null) {
            for (String role : matchInterceptor.getRolesAllowed()) {
                if (roleList.contains(new Role(role))) {
                    authorized = true;
                    break;
                }
            }
        }

        return authorized;
    }

    private boolean doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute(USER_KEY);
        if (user == null && isRestrictedPath(req)) {
            log("RESTRITO: " + req.getRequestURI());
            doLogin(request, response, req);
            return true;
        }

        return false;
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean redirected = doBeforeProcessing(request, response);

        Throwable problem = null;
        try {
            if (!redirected) {
                chain.doFilter(request, response);
            }
        } catch (IOException | ServletException t) {
            problem = t;
            Logger.getLogger(SecurityFilter.class.getName()).log(Level.SEVERE, "", t);
        }

        doAfterProcessing(request, response);

        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        log("SecurityFilter:Initializing filter");
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                try (PrintStream ps = new PrintStream(response.getOutputStream()); PrintWriter pw = new PrintWriter(ps)) {
                    pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                    // PENDING! Localize this for next official release
                    pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                    pw.print(stackTrace);
                    pw.print("</pre></body>\n</html>"); //NOI18N
                }
                response.getOutputStream().close();
            } catch (IOException ex) {
            }
        } else {
            try {
                try (PrintStream ps = new PrintStream(response.getOutputStream())) {
                    t.printStackTrace(ps);
                }
                response.getOutputStream().close();
            } catch (IOException ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (IOException ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        if (DEBUG) {
            filterConfig.getServletContext().log(msg);
        }
    }
}
