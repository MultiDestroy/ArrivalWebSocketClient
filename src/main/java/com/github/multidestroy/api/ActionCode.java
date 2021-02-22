package com.github.multidestroy.api;

import com.github.multidestroy.exceptions.NoSuchActionCodeException;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ActionCode {
    TEST(1),
    TEST2(2);

    public final int ID;

    ActionCode(int ID) {
        this.ID = ID;
    }

    public static ActionCode getFromID(int ID) {
        var list = Arrays.stream(values())
                .filter(code -> code.ID == ID)
                .collect(Collectors.toList());

        if (list.size() == 0)
            throw new NoSuchActionCodeException("ActionCode with specified ID: " + ID + " does not exists!");

        return list.get(0);
    }

}
