package net.coderodde.zerosum.ai;

import java.util.Collection;
import java.util.List;

/**
 * This interface defines the API for search states.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 26, 2019)
 * @param <S> the actual state type.
 */
public interface State<S> {
    
    /**
     * Returns the next ply.
     * 
     * @return the collection of next states.
     */
    public List<S> children();
    
    /**
     * Returns {@code true} if this state is a terminal state.
     * 
     * @return a boolean indicating whether this state is terminal.
     */
    public boolean isTerminal();
}
