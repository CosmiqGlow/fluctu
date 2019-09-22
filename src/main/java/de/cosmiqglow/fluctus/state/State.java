package de.cosmiqglow.fluctus.state;

/**
 * A State represents a condition in which a system is located.
 * This State is used as a base-class for different forms of states.
 * <p>This implementation is currently NOT thread-safe, due to rare use-cases.</p>
 */
public abstract class State {

    private boolean started = false;
    private boolean updating = false;
    private boolean ended = false;
    private boolean frozen = false;
    //TODO: Add lock

    /**
     * Start the State.
     * <p>Internally the state will be marked as started and calls {@link State#onStart()}
     * which should be used for initialization purpose.</p>
     */
    public void start() {
        if (started || ended) {
            return;
        }

        started = true;
        onStart();
    }

    /**
     * Finishes the state, if it has been started and not ended yet.
     * Call this to end the state and {@link State#onEnd()}.
     */
    public void end() {
        if (!started || ended) {
            return;
        }

        ended = true;
        onEnd();
    }

    /**
     * Updates the state and checks if it is ready to end by calling {@link State#isReadyToEnd()}.
     * Furthermore it calls {@link State#onUpdate()}.
     * If a state is freezed, this will never succeed to end the state automatically.
     * You can implement your own scheduling to provide consistent updating.
     */
    public void update() {
        if (!started || ended || updating) {
            return;
        }
        updating = true;

        if (isReadyToEnd() && !frozen) {
            end();
            return;
        }

        onUpdate();
        updating = false;
    }

    /**
     * Freezes the state and wont let it end by update ticks..
     * You can only end a freezed state by calling {@link State#end()}.
     * Opposite function: {@link State#unfreeze()}
     */
    public void freeze() {
        frozen = true;
    }

    /**
     * Unfreezes the state and lets it end by update ticks.
     * Opposite function: {@link State#freeze()}
     */
    public void unfreeze() {
        frozen = false;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean hasEnded() {
        return ended;
    }

    public boolean hasStarted() {
        return started;
    }

    public void reset() {
        if(started && !ended) {
            throw new IllegalStateException("State has not finished yet!");
        }
        started = false;
        ended = false;
        frozen = false;
    }

    /**
     * Provides room to initialize the state.
     * This will be once called, when the state is started by {@link State#start()}.
     */
    protected abstract void onStart();

    /**
     * Provides possibility to update the state.
     * Will be called, when the state is updated by {@link State#update()}.
     * Please make sure that if you dont provide scheduling for the state, this won't work by itself.
     */
    protected abstract void onUpdate();

    /**
     * Provides room for cleaning up the state.
     * This will be once called, when the state is ended by {@link State#end()}.
     */
    protected abstract void onEnd();

    /**
     * Default implementation will consider a state ready to end, if the {@link State#end()} method has been called.
     * Use this method to add ending conditions to this state or phase.
     * @return boolean - Whether the State is ready to end and can be ended or ist not ready to end yet.
     */
    protected boolean isReadyToEnd() {
        return ended;
    }
}
