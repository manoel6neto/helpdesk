package com.mycompany.maven.model.comparators;

import com.mycompany.maven.model.User;
import java.util.Comparator;

/**
 *
 * @author Manoel
 */
public class UserComparator implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
