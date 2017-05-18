package com.mycompany.maven.jsf.converts;

import com.mycompany.maven.model.Role;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Manoel
 */
@FacesConverter(forClass = Role.class, value = "roleConverter")
public class RoleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            return (Role) component.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Role) {
            Role entity = (Role) value;
            if (entity instanceof Role && entity.getId() != null) {
                component.getAttributes().put(entity.getId().toString(), entity);
                return entity.getId().toString();
            }
        }
        return "";
    }

}
