package com.github.multidestroy.api.data.structures;

import com.github.multidestroy.api.ActionCode;
import com.github.multidestroy.api.data.structures.ActionsConsumers;
import com.github.multidestroy.exceptions.EmptyActionCodeEnumException;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionsConsumersTest {

    private ActionsConsumers actionsConsumers;

    @BeforeAll
    public static void checkIfActionCodeHasValues() {
        if (ActionCode.values().length == 0)
            throw new EmptyActionCodeEnumException();
    }

    @BeforeEach
    public void prepareToTest() {
        this.actionsConsumers = new ActionsConsumers();
    }

    @Test
    @DisplayName("Test if every ActionCode while creating, has a mapping in ActionsConsumers as an empty list.")
    public void testIfEveryActionCodeHasEmptyList() {
        var actionCodes = Arrays.stream(ActionCode.values());

        var numberOfNonEmptyLists = actionCodes.map(this.actionsConsumers::getConsumers)
                .filter(Predicate.not(List::isEmpty))
                .count();

        Assertions.assertEquals(numberOfNonEmptyLists, 0);
    }

    @Test
    @DisplayName("Test if every returning list is unmodifiable.")
    public void testIfReturningListsAreNonModifiable() {
        var actionCodes = Arrays.stream(ActionCode.values());

        var numberOfModifiableLists = actionCodes.map(this.actionsConsumers::getConsumers)
                .filter(list -> List.of(1).getClass().isInstance(list)) //List.of() returns unmodifiable list
                .count();

        Assertions.assertEquals(numberOfModifiableLists, 0);
    }

    @Test
    public void testExpandConsumersList() {
        var exampleActionCode = ActionCode.values()[0];
        Consumer<Object> consumer = System.out::println;
        this.actionsConsumers.addConsumer(exampleActionCode, consumer);

        Assertions.assertEquals(consumer, this.actionsConsumers.getConsumers(exampleActionCode).get(0));
    }
}
