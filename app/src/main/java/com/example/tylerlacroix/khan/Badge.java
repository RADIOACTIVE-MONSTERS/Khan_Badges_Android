package com.example.tylerlacroix.khan;

import java.io.Serializable;

/**
 * Created by Tyler on 15-12-27.
 */
public class Badge implements Comparable, Serializable {
    String icon;
    String image;
    String descript;
    String description_extended;
    int category;
    int points;


    public int compareTo(Object obj) {
        Badge second = (Badge)obj;
        if ((category == second.category) && (points + second.points > 0)) {
            if (points == 0)
                return 1;
            else if (second.points == 0)
                return -1;
            return points - second.points;
        }
        return category - second.category;
    }
}
