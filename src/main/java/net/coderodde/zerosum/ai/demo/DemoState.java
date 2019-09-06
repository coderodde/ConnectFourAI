package net.coderodde.zerosum.ai.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.coderodde.zerosum.ai.AbstractState;

/**
 * This class implements a demonstrative game state.
 * 
 * @author Rodion "rodde" Efremov 
 * @version 1.6 (Jun 15, 2019)
 */
public final class DemoState extends AbstractState<DemoState, DemoPlayerColor> {

    /**
     * Holds the child nodes of this node.
     */
    private List<DemoState> children;
    
    /**
     * Used for generating children nodes.
     */
    private final Random random;
    
    /**
     * The player this state belongs to.
     */
    private final DemoPlayerColor playerColor;
    
    /**
     * The minimum number of children.
     */
    private final int minimumChildren;
    
    /**
     * The maximum number of children.
     */
    private final int maximumChildren;
    
    /**
     * The value of this state.
     */
    private final double value;
    
    /**
     * The highest value producing the victory of the minimizing player.
     */
    private final double minimizingPlayerVictoryCutOff;
    
    /**
     * The lowest value producing the victory of the maximizing player.
     */
    private final double maximizingPlayerVictoryCutOff;
    
    /**
     * The ID of this node.
     */
    private final int stateId;
    
    /**
     * Counts the total number of nodes created and is used for generating the
     * node IDs.
     */
    private static int stateIdCounter = 0;
    
    /**
     * Constructs a new demo node.
     * @param random the random number generator.
     * @param playerColor the player color.
     * @param minimizingPlayerVictoryCutOff the highest value producing the 
     *                                      victory for the minimizing player.
     * @param maximizingPlayerVictoryCutOff the lowest value producing the 
     *                                      victory for the maximizing player.
     * @param minimumChildren the minimum number of children of this node.
     * @param maximumChildren the maximum number of children of this node.
     */
    public DemoState(Random random, 
                     DemoPlayerColor playerColor,
                     double minimizingPlayerVictoryCutOff,
                     double maximizingPlayerVictoryCutOff,
                     int minimumChildren,
                     int maximumChildren) {
        this.random = random;
        this.playerColor = playerColor;
        this.minimizingPlayerVictoryCutOff = minimizingPlayerVictoryCutOff;
        this.maximizingPlayerVictoryCutOff = maximizingPlayerVictoryCutOff;
        this.minimumChildren = minimumChildren;
        this.maximumChildren = maximumChildren;
        this.value = random.nextGaussian();
        this.stateId = DemoState.stateIdCounter++;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<DemoState> children() {
        if (children != null) {
            return children;
        }
        
        int numberOfChildren = minimumChildren + 
                random.nextInt(maximumChildren - minimumChildren + 1);
        
        this.children = new ArrayList<>(numberOfChildren);
        DemoPlayerColor nextPlayerColor =
                playerColor == DemoPlayerColor.MAXIMIZING_PLAYER ? 
                DemoPlayerColor.MINIMIZING_PLAYER : 
                DemoPlayerColor.MAXIMIZING_PLAYER;
        
        for (int i = 0; i < numberOfChildren; i++) {
            DemoState child = new DemoState(random,
                                            nextPlayerColor,
                                            minimizingPlayerVictoryCutOff,
                                            maximizingPlayerVictoryCutOff,
                                            minimumChildren, 
                                            maximumChildren);
            
            child.setDepth(this.getDepth() - 1);
            children.add(child);
        }
        
        return children;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "[Node " + stateId + 
               ", value = " + value + 
               ", player = " + playerColor.name() + "]"; 
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTerminal() {
        switch (playerColor) {
            case MAXIMIZING_PLAYER:
                return value > maximizingPlayerVictoryCutOff;
                
            case MINIMIZING_PLAYER:
                return value <  minimizingPlayerVictoryCutOff;
                
            default:
                throw new IllegalStateException(
                        "Unknown player color: " + playerColor + ".");
        }
    }
    
    @Override
    public int hashCode() {
        return stateId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        
        if (o == null) {
            return false;
        }
        
        DemoState other = (DemoState) o;
        return stateId == other.stateId;
    }
    
    /**
     * Returns the value of this state.
     * 
     * @return the value of this state.
     */
    public double getValue() {
        return value;
    }
    
    /**
     * Resets the ID counter.
     */
    public static void resetCounter() {
        DemoState.stateIdCounter = 0;
    }

    @Override
    public DemoPlayerColor checkVictory() {
        if (value < minimizingPlayerVictoryCutOff) {
            return DemoPlayerColor.MINIMIZING_PLAYER;
        }
        
        if (value > maximizingPlayerVictoryCutOff) {
            return DemoPlayerColor.MAXIMIZING_PLAYER;
        }
        
        return null;
    }
}
