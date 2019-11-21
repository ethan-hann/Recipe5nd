package com.uhcl.recipe5nd.helperClasses;

import java.util.Comparator;
import java.util.Date;

public class SortBasedOnDate implements Comparator<ShoppingList>
{
    @Override
    public int compare(ShoppingList list1, ShoppingList list2)
    {
        Date d1 = list1.getDate();
        Date d2 = list2.getDate();
        return d2.compareTo(d1);
    }
}
