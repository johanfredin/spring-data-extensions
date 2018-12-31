/*
 * Copyright 2018 Johan Fredin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.johanfredin.springdataextensions.util;

import com.github.johanfredin.springdataextensions.domain.ChangeDateHolder;

import java.util.Comparator;

public interface DateComparator extends Comparator<ChangeDateHolder> {

    @Override
    default int compare(ChangeDateHolder o1, ChangeDateHolder o2) {
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
