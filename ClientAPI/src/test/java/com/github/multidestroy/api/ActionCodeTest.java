package com.github.multidestroy.api;

import com.github.multidestroy.exceptions.EmptyActionCodeEnumException;
import com.github.multidestroy.exceptions.NoSuchActionCodeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ActionCodeTest {

    @BeforeAll
    public static void checkIfActionCodeHasValues() {
        if (ActionCode.values().length == 0)
            throw new EmptyActionCodeEnumException();
    }

    @Test
    public void testReturnByID() {
        boolean allMatch = Arrays.stream(ActionCode.values())
                .allMatch(code -> ActionCode.getFromID(code.ID).equals(code));

        Assertions.assertTrue(allMatch);
    }

    @Test
    public void testActionCodeNotExists() {
        var notExistingID =
                Arrays.stream(ActionCode.values())
                        .mapToInt(code -> code.ID)
                        .max()
                        .getAsInt() + 1;

        Assertions.assertThrows(NoSuchActionCodeException.class, () -> ActionCode.getFromID(notExistingID));

    }
}
