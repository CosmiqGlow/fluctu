package de.cosmiqglow.fluctus.state;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a State that can hold multiple ordered states inside of itself.
 * This class is used by {@link StatesSeries} and its subclasses.
 */
public abstract class StatesContainer extends State implements Iterable<State> {

    protected final List<State> states = new LinkedList<>();

    /**
     * Append a single state at the end of the ordered list of states.
     * @param state - not null state
     */
    public void add(State state) {
        Validate.notNull(state);
        states.add(state);
    }

    /**
     * Append multiple states at the of the ordered list of states.
     * @param states - not empty and not null list of states.
     */
    public void addAll(Collection<State> states) {
        Validate.notNull(states);
        Validate.notEmpty(states);
        this.states.addAll(states);
    }

    /**
     * Freeze all states simultaneously.
     * This calls {@link State#freeze()}.
     */
    public void freeze() {
        states.forEach(State::freeze);
    }

    /**
     * Unfreeze all states simultaneously.
     * This calls {@link State#unfreeze()}.
     */
    public void unfreeze() {
        states.forEach(State::unfreeze);
    }

    public void reset() {
        states.forEach(State::reset);
    }

    /**
     * Retrieve an Iterator.
     * @return - Iterator containing this StateHolder's states.
     */
    @Override
    @NotNull
    public Iterator<State> iterator() {
        return states.iterator();
    }

}
