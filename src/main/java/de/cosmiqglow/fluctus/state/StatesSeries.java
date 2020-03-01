package de.cosmiqglow.fluctus.state;

import org.apache.commons.lang.Validate;

/**
 * StateSeries is a StateHolder, that can organize and execute a series of states.
 */
public class StatesSeries extends StatesContainer {

    private int current = 0;
    private boolean skipping = false;
    private State currentState = null;

    /**
     * Insert a state directly after the current state.
     * This will not automatically skip to the newly added state.
     * @param state - not null state
     */
    public void addNext(State state) {
        Validate.notNull(state);
        states.add(current + 1, state);
    }

    /**
     * Skip the current state.
     * This will be done on the next update tick.
     * Please use this only for "/start" and development purposes.
     */
    public void skip() {
        skipping = true;
    }

    @Override
    protected void onStart() {
        if (states.isEmpty()) {
            end();
        }

        currentState = states.get(current);
        currentState.start();
    }

    @Override
    protected void onUpdate() {
        currentState.update();

        if ((currentState.isReadyToEnd() && !currentState.isFrozen()) || skipping) {
            if (skipping) {
                skipping = false;
            }

            currentState.end();
            ++current;
            if (current >= states.size()) {
                end();
                return;
            }

            currentState = states.get(current);
            currentState.start();
        }
    }

    @Override
    protected void onEnd() {
        if (current < states.size()) {
            currentState.end();
        }
    }

    /*
    Determines whether we looped through all states and the current (last) state is ready to end.
     */
    @Override
    protected boolean isReadyToEnd() {
        return (current == states.size() - 1 && currentState.isReadyToEnd());
    }
}
