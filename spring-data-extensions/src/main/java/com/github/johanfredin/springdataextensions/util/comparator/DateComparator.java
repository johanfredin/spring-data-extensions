package com.github.johanfredin.springdataextensions.util.comparator;

import java.util.Comparator;

public interface DateComparator extends Comparator<AbstractEntity> {

    @Override
    int compare(AbstractEntity o1, AbstractEntity o2);

}
