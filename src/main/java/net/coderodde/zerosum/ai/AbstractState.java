package net.coderodde.zerosum.ai;

import java.util.List;

/**
 * This interface defines the API for search states.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 26, 2019)
 * @param <S> the actual state type.
 */
public abstract class AbstractState<S extends AbstractState<S, P>,
                                    P extends Enum<P>> {
    
    private int depth;
    
    /**
     * Returns the next ply.
     * 
     * @return the collection of next states.
     */
    public abstract List<S> children();
    
    /**
     * Returns {@code true} if this state is a terminal state.
     * 
     * @return a boolean indicating whether this state is terminal.
     */
    public abstract boolean isTerminal();
    
    /**
     * Checks whether this state represents a victory of a player.
     * 
     * @return the winning player or {@code null} if there is no such.
     */
    public abstract P checkVictory();
    
    public int getDepth() {
        return depth;
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
}
