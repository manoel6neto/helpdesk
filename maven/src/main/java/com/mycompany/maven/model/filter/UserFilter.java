package com.mycompany.maven.model.filter;

import java.io.Serializable;

/**
 *
 * @author Manoel
 */
public class UserFilter implements Serializable {

    private String name;

    private String email;

    private Integer id;

    private Integer roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}
