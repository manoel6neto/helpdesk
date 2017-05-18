package com.mycompany.maven.model.comparators;

import com.mycompany.maven.model.Role;
import java.util.Comparator;

/**
 *
 * @author Manoel
 */
public class RoleComparator implements Comparator<Role> {

    @Override
    public int compare(Role o1, Role o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
