package net.coderodde.zerosum.ai.impl;

import net.coderodde.zerosum.ai.EvaluatorFunction;
import net.coderodde.zerosum.ai.AbstractGameEngine;
import net.coderodde.zerosum.ai.AbstractState;

/**
 * This class implements the 
 * <a href="https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning">
 * Alpha-beta pruning</a> algorithm for zero-sum two-player games.
 * 
 * @param <S> the game state type.
 * @param <P> the player color type.
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 26, 2019)
 * @version 1.61 (Sep 12, 2019)
 * @since 1.6 (May 26, 2019)
 */
public final class AlphaBetaPruningGameEngine<S extends AbstractState<S, P>, 
                                              P extends Enum<P>> 
extends AbstractGameEngine<S, P> {

    /**
     * Constructs this minimax game engine.
     * @param evaluatorFunction the evaluator function.
     * @param depth the search depth.
     */
    public AlphaBetaPruningGameEngine(EvaluatorFunction<S> evaluatorFunction,
                                      int depth) {
        super(evaluatorFunction, depth, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    public S makePly(S state,
                     P minimizingPlayer,
                     P maximizingPlayer,
                     P initialPlayer) {
        state.setDepth(depth);
        
        // Do the game tree search with Alpha-beta pruning:
        return makePlyImplTopmost(state,
                                  depth,
                                  -Double.NEGATIVE_INFINITY,
                                   Double.POSITIVE_INFINITY,
                                  minimizingPlayer,
                                  maximizingPlayer,
                                  initialPlayer);
    }
    
    /**
     * Pefrorms the topmost search of a game tree.
     * 
     * @param state            the state to start the search from.
     * @param depth            the depth of the tree to search.
     * @param alpha            the alpha cut-off value.
     * @param beta             the beta cut-off value.
     * @param minimizingPlayer the minimizing player color.
     * @param maximizingPlayer the maximizing player color.
     * @param currentPlayer    the current player color.
     * @return 
     */
    private S makePlyImplTopmost(S state,
                                 int depth,
                                 double alpha,
                                 double beta,
                                 P minimizingPlayer,
                                 P maximizingPlayer,
                                 P currentPlayer) {
        S bestState = null;
        
        if (currentPlayer == maximizingPlayer) {
            double tentativeValue = Double.NEGATIVE_INFINITY;
            
            for (S childState : state.children()) {
                double value = makePlyImpl(childState,
                                           depth - 1,
                                           alpha,
                                           beta,
                                           minimizingPlayer,
                                           maximizingPlayer,
                                           minimizingPlayer);
                
                if (tentativeValue < value) {
                    tentativeValue = value;
                    bestState = childState;
                }
                
                alpha = Math.max(alpha, tentativeValue);
                
                if (alpha >= beta) {
                    return bestState;
                }
            }
        } else {
            // Here, 'initialPlayer == minimizingPlayer'.
            double tentativeValue = Double.POSITIVE_INFINITY;
            
            for (S childState : state.children()) {
                double value = makePlyImpl(childState,
                                           depth - 1,
                                           alpha,
                                           beta,
                                           minimizingPlayer,
                                           maximizingPlayer,
                                           minimizingPlayer);
                
                if (tentativeValue > value) {
                    tentativeValue = value;
                    bestState = childState;
                }
                
                beta = Math.min(beta, tentativeValue);
                
                if (alpha >= beta) {
                    return bestState;
                }
            }
        }
            
        return bestState;
    }
    
    /**
     * Performs a single step down the game tree.
     * 
     * @param state            the starting state.
     * @param depth            the maximum depth of the game tree.
     * @param alpha            the alpha cut-off.
     * @param beta             the beta cut-off.
     * @param minimizingPlayer the minimizing player.
     * @param maximizingPlayer the maximizing player.
     * @param currentPlayer    the current player.
     * 
     * @return the value of the best ply.
     */
    private double makePlyImpl(S state,
                               int depth,
                               double alpha,
                               double beta,
                               P minimizingPlayer,
                               P maximizingPlayer,
                               P currentPlayer) {
        if (state.getDepth() == 0 
                || state.checkVictory() != null
                || state.isTerminal()) {
            return evaluatorFunction.evaluate(state);
        }
        
        if (currentPlayer == maximizingPlayer) {
            double tentativeValue = Double.NEGATIVE_INFINITY;
            
            for (S child : state.children()) {
                double value = makePlyImpl(child,
                                           depth - 1,
                                           alpha, 
                                           beta,
                                           minimizingPlayer,
                                           maximizingPlayer,
                                           minimizingPlayer);
                
                if (tentativeValue < value) {
                    tentativeValue = value;
                }
                
                alpha = Math.max(alpha, tentativeValue);
                
                if (alpha >= beta) {
                    break;
                }
            }
            
            return tentativeValue;
        } else {
            // Here, 'initialPlayer == minimizingPlayer'.
            double tentativeValue = Double.POSITIVE_INFINITY;
            
            for (S child : state.children()) {
                double value = makePlyImpl(child,
                                           depth - 1,
                                           alpha,
                                           beta,
                                           minimizingPlayer,
                                           maximizingPlayer,
                                           minimizingPlayer);
                
                if (tentativeValue > value) {
                    tentativeValue = value;
                }
                
                beta = Math.min(beta, tentativeValue);
                
                if (alpha >= beta) {
                    break;
                }
            }
            
            return tentativeValue;
        }
    }
}
