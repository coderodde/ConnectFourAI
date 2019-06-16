package net.coderodde.zerosum.ai.impl;

import java.util.ArrayList;
import java.util.Collections;
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
     * so far, which belongs to the maximizing player moves.
     */
    private S bestTerminalMaximizingState;
    
    /**
     * Stores the value of {@code bestTerminalMaximizingState}.
     */
    private double bestTerminalMaximizingStateValue;

    /**
     * Stores the terminal node or a node at the depth zero with the best value
     * so far, which belongs to the minimizing player moves.
     */
    private S bestTerminalMinimizingState;
    
    /**
     * Stores the value of {@code bestTerminalMinimizingState}.
     */
    private double bestTerminalMinimizingStateValue;
    
    /**
     * Indicates whether we are computing a next ply for the minimizing player 
     * or not. If not, we are computing a next ply for the maximizing player.
     */
    private boolean makingPlyForMinimizingPlayer;
    
    /**
     * Holds the player color for which we are computing the next move (ply).
     */
    private P targetPlayer;
    
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
        // Reset the best known values:
        bestTerminalMaximizingStateValue = Double.NEGATIVE_INFINITY;
        bestTerminalMinimizingStateValue = Double.POSITIVE_INFINITY;
        targetPlayer = initialPlayer;
        makingPlyForMinimizingPlayer = initialPlayer != minimizingPlayer;
        
        // Do the game tree search:
        makePlyImpl(state,
                    depth,
                    minimizingPlayer,
                    maximizingPlayer,
                    initialPlayer);
        
        // Find the next game state starting from 'state':
        S returnState =
                inferBestState(
                        initialPlayer == minimizingPlayer ? 
                                bestTerminalMinimizingState : 
                                bestTerminalMaximizingState);
        
        // Release the resources:
        parents.clear();
        bestTerminalMaximizingState = null;
        bestTerminalMinimizingState = null;
        // We are done with a single move:
        return returnState;
    }
    
    private S inferBestState(S bestTerminalState) {
        List<S> statePath = new ArrayList<>();
        S state = bestTerminalState;
        
        while (state != null) {
            statePath.add(state);
            state = parents.get(state);
        }
        
        // Return the second upmost state:
        Collections.<S>reverse(statePath);
        return statePath.get(1);
    }
     
    /**
     * Performs a single step down the game tree branch.
     * 
     * @param state the starting state.
     * @param depth the maximum depth of the game tree.
     * @param minimizingPlayer the minimizing player.
     * @param maximizingPlayer the maximizing player.
     * @param currentPlayer the current player.
     * @return the value of the best ply.
     */
    private double makePlyImpl(S state,
                               int depth,
                               P minimizingPlayer,
                               P maximizingPlayer,
                               P currentPlayer) {
        if (depth == 0 || state.isTerminal()) {
            double value = evaluatorFunction.evaluate(state);
            
            if (!makingPlyForMinimizingPlayer) {
                if (bestTerminalMinimizingStateValue > value) {
                    bestTerminalMinimizingStateValue = value;
                    bestTerminalMinimizingState = state;
                }
            } else {
                if (bestTerminalMaximizingStateValue < value) {
                    bestTerminalMaximizingStateValue = value;
                    bestTerminalMaximizingState = state;
                }
            }
//            if (currentPlayer == minimizingPlayer) {
//                if (bestTerminalMinimizingStateValue > value) {
//                    bestTerminalMinimizingStateValue = value;
//                    bestTerminalMinimizingState = state;
//                }
//            } else {
//                if (bestTerminalMaximizingStateValue < value) {
//                    bestTerminalMaximizingStateValue = value;
//                    bestTerminalMaximizingState = state;
//                }
//            }
            
            return value;
        }
        
        if (currentPlayer == maximizingPlayer) {
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
            // Here, 'initialPlayer == minimizingPlayer'.
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
