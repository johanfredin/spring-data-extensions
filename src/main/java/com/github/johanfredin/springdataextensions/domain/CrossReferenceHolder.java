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
package com.github.johanfredin.springdataextensions.domain;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Our entities have {@link OneToOne}, {@link ManyToOne}, {@link OneToMany} relations
 * this method should make sure that relations are set both ways.<br>
 * <b>Example</b>:<br>
 * class Entity1 has an instance of Class Entity2 and they have a {@link OneToOne} relation<br>
 * Entity1 must then both set the Entity2 instance the usual way<br>
 * and assign itself to Entity2 so that:<br><br>
 * <code>public void setCrossRelations() {<br>
 * this.getEntity2().setEntity1(this);<br>
 * }<br>
 * Many-To-Many relations will most likely not be possible with this (not in a good way at least)
 */
public interface CrossReferenceHolder {

    /**
     * Decide how to backwards update our references.
     */
    void setCrossRelations();

}
