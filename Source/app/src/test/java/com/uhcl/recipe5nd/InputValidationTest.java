package com.uhcl.recipe5nd;

import com.uhcl.recipe5nd.helperClasses.Helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class InputValidationTest
{
    String validName = "Chicken";
    String inValidName = "Chis12Ks/*";

    @Test
    public void testIngredientName()
    {
        assertTrue(Helper.validateInput(validName));
        assertFalse(Helper.validateInput(inValidName));
    }
}
