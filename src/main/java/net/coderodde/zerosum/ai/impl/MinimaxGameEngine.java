package net.coderodde.zerosum.ai.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.coderodde.zerosum.ai.EvaluatorFunction;
import net.coderodde.zerosum.ai.GameEngine;
import net.coderodde.zerosum.ai.State;

/**
 * This class implements the 
 * <a href="https://en.wikipedia.org/wiki/Minimax">Minimax</a> algorithm for 
 * zero-sum two-player games.
 * 
 * @param <S> the game state type.
 * @param <P> the player color type.
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 26, 2019)
 */
public final class MinimaxGameEngine<S extends State<S>, P extends Enum<P>> 
        extends GameEngine<S, P> {

    /**
     * Stores the terminal node or a node at the depth zero with the best value
     * so far.
     */
    private S bestTerminalState;
    
    /**
     * Stores the value of {@code bestTerminalState}.
     */
    private double bestTerminalStateValue;
    
    /**
     * Indicates whether we are computing a next ply for the minimizing player 
     * or not. If not, we are computing a next ply for the maximizing player.
     */
    private boolean makingPlyForMinimizingPlayer;
    
    /**
     * Maps each visited state to its parent state.
     */
    private final Map<S, S> parents = new HashMap<>();
    
    /**
     * Constructs this minimax game engine.
     * @param evaluatorFunction the evaluator function.
     * @param depth the search depth.
     */
    public MinimaxGameEngine(EvaluatorFunction<S> evaluatorFunction,
                             int depth) {
        super(evaluatorFunction, depth, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public S makePly(S state, 
                     P minimizingPlayer,
                     P maximizingPlayer,
                     P initialPlayer) {
        bestTerminalStateValue = initialPlayer == minimizingPlayer ?
                Double.POSITIVE_INFINITY :
                Double.NEGATIVE_INFINITY;
        
        makingPlyForMinimizingPlayer = initialPlayer == minimizingPlayer;
        
        makePlyImpl(state,
                    depth,
                    minimizingPlayer,
                    maximizingPlayer,
                    initialPlayer);
        
        S returnState = inferBestState();
        parents.clear();
        bestTerminalState = null;
        return returnState;
    }
    
    private S inferBestState() {
        List<S> statePath = new ArrayList<>();
        S s = bestTerminalState;
        
        while (s != null) {
            statePath.add(s);
            s = parents.get(s);
        }
        
        // Return the second upmost state:
        return statePath.get(statePath.size() - 2);
    }
     
    private double makePlyImpl(S state,
                               int depth,
                               P minimizingPlayer,
                               P maximizingPlayer,
                               P initialPlayer) {
        if (depth == 0 || state.isTerminal()) {
            double value = evaluatorFunction.evaluate(state);
            
            if (makingPlyForMinimizingPlayer) {
                if (bestTerminalStateValue > value) {
                    bestTerminalStateValue = value;
                    bestTerminalState = state;
                }
            } else {
                if (bestTerminalStateValue < value) {
                    bestTerminalStateValue = value;
                    bestTerminalState = state;
                }
            }
            
            return value;
        }
        
        if (initialPlayer == maximizingPlayer) {
            double value = Double.NEGATIVE_INFINITY;
            
            for (S child : state.children()) {
                value = Math.max(
                        value, 
                        makePlyImpl(child, 
                                    depth - 1, 
                                    minimizingPlayer, 
                                    maximizingPlayer, 
                                    minimizingPlayer));
                
                parents.put(child, state);
            }
            
            return value;
        } else {
            double value = Double.POSITIVE_INFINITY;
            
            for (S child : state.children()) {
                value = Math.min(
                        value,
                        makePlyImpl(child, 
                                    depth - 1, 
                                    minimizingPlayer, 
                                    maximizingPlayer, 
                                    maximizingPlayer));
                
                parents.put(child, state);
            }
            
            return value;
        }
    }
}
