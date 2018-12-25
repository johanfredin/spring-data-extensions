package com.github.johanfredin.springdataextensions.util.comparator;

import com.github.johanfredin.springdataextensions.domain.ChangeDateHolder;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Custom {@link Comparator} for our collections that will by default
 * organize the entities by last change date in descending order
 *
 * @author johan
 */
@Component("dateComparator")
public class DateComparatorImpl implements DateComparator {

    @Override
    public int compare(ChangeDateHolder o1, ChangeDateHolder o2) {
        try {

            String d1 = o1.getLastChangeDate();
            String d2 = o2.getLastChangeDate();

            long date1 = Long.parseLong(d1.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", ""));
            long date2 = Long.parseLong(d2.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", ""));

            if (date1 < date2) {
                return 1;
            } else if (date1 == date2) {
                return 0;
            }

            return -1;

        } catch (NumberFormatException | NullPointerException ex) {
            // 1 in this case would mean ascending order
            return 1;
        }
    }


}
