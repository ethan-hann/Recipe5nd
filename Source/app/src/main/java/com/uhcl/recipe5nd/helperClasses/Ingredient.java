/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Mark Odom
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.uhcl.recipe5nd.helperClasses;

public class Ingredient
{
    private String name = "";
    private PrimaryTag primaryTag;
    private String optionalTag = "";

    public Ingredient(String name, PrimaryTag pTag, String oTag) {
        this.name = name;
        this.primaryTag = pTag;
        this.optionalTag = oTag;
    }

    public Ingredient(String name, PrimaryTag pTag) {
        this.name = name;
        this.primaryTag = pTag;
    }

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrimaryTag getPrimaryTag() {
        return primaryTag;
    }

    public void setPrimaryTag(PrimaryTag primaryTag) {
        this.primaryTag = primaryTag;
    }

    public String getOptionalTag() {
        return optionalTag;
    }

    public void setOptionalTag(String optionalTag) {
        this.optionalTag = optionalTag;
    }
}
