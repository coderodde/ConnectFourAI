package net.coderodde.connectfour.ai;

import java.util.Collection;

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
    public Collection<S> children();
}
