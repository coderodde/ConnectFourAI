package net.coderodde.zerosum.ai.impl;

import net.coderodde.zerosum.ai.EvaluatorFunction;
import net.coderodde.zerosum.ai.AbstractGameEngine;
import net.coderodde.zerosum.ai.AbstractState;

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
public final class MinimaxGameEngine<S extends AbstractState<S, P>,
                                     P extends Enum<P>> 
        extends AbstractGameEngine<S, P> {
    
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
        state.setDepth(depth);
        
        // Do the game tree search:
        return makePlyImplTopmost(state,
                                  minimizingPlayer,
                                  maximizingPlayer,
                                  initialPlayer);
    }
    
    private S makePlyImplTopmost(S state, 
                                 P minimizingPlayer,
                                 P maximizingPlayer,
                                 P currentPlayer) {
        if (state.getDepth() == 0 || state.checkVictory() != null) {
            return state;
        }
        
        S bestState = null;
        
        if (currentPlayer == maximizingPlayer) {
            double tentativeValue = Double.NEGATIVE_INFINITY;
            
            for (S childState : state.children()) {
                double value = makePlyImpl(childState,
                                           depth - 1,
                                           minimizingPlayer,
                                           maximizingPlayer,
                                           minimizingPlayer);
                
                if (value > tentativeValue) {
                    value = tentativeValue;
                    bestState = childState;
                }
            }
        } else {
            // Here, 'initialPlayer == minimizingPlayer'.
            double tentativeValue = Double.POSITIVE_INFINITY;
            
            for (S childState : state.children()) {
                double value = makePlyImpl(childState,
                                           depth - 1,
                                           minimizingPlayer,
                                           maximizingPlayer,
                                           minimizingPlayer);
                
                if (value < tentativeValue) {
                    value = tentativeValue;
                    bestState = childState;
                }
            }
        }
            
        return bestState;
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
        if (state.getDepth() == 0 || state.checkVictory() != null) {
            return evaluatorFunction.evaluate(state);
        }
        
        if (currentPlayer == maximizingPlayer) {
            double tentativeValue = Double.NEGATIVE_INFINITY;
            
            for (S child : state.children()) {
                double value = makePlyImpl(child,
                                           depth - 1,
                                           minimizingPlayer,
                                           maximizingPlayer,
                                           minimizingPlayer);
                
                if (value > tentativeValue) {
                    value = tentativeValue;
                }
            }
            
            return tentativeValue;
        } else {
            // Here, 'initialPlayer == minimizingPlayer'.
            double tentativeValue = Double.POSITIVE_INFINITY;
            
            for (S child : state.children()) {
                double cost = makePlyImpl(child,
                                          depth - 1,
                                          minimizingPlayer,
                                          maximizingPlayer,
                                          minimizingPlayer);
                
                if (cost < tentativeValue) {
                    cost = tentativeValue;
                }
            }
            
            return tentativeValue;
        }
    }
}
