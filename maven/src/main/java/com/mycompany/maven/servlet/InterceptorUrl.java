package com.mycompany.maven.servlet;

/**
 *
 * @author Manoel
 */
public class InterceptorUrl {

    private String urlPattern;

    private String[] rolesAllowed;

    public InterceptorUrl(String urlPattern) {
        this(urlPattern, null);
    }

    public InterceptorUrl(String urlPattern, String[] rolesAllowed) {
        if (urlPattern.charAt(urlPattern.length() - 1) == '*') {

            urlPattern = urlPattern.substring(0, urlPattern.length() - 1);

            if (urlPattern.charAt(urlPattern.length() - 1) == '/') {
                urlPattern = urlPattern.substring(0, urlPattern.length() - 1);
                urlPattern = "^(" + urlPattern + ")(/[^/]+)*/?$";
            } else {
                urlPattern = "^(" + urlPattern + ")([^/])*$";
            }

        } else {
            urlPattern = "^(" + urlPattern + ")/?$";
        }
        this.urlPattern = urlPattern;
        this.rolesAllowed = rolesAllowed;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String[] getRolesAllowed() {
        return rolesAllowed;
    }

    public void setRolesAllowed(String[] rolesAllowed) {
        this.rolesAllowed = rolesAllowed;
    }
}
