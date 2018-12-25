package com.github.johanfredin.springdataextensions.util.comparator;

import com.github.johanfredin.springdataextensions.domain.ChangeDateHolder;

import java.util.Comparator;

public interface DateComparator extends Comparator<ChangeDateHolder> {

    @Override
    int compare(ChangeDateHolder o1, ChangeDateHolder o2);
}
