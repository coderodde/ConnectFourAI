package net.coderodde.zerosum.ai.impl;

import net.coderodde.zerosum.ai.AbstractGameEngine;
import net.coderodde.zerosum.ai.AbstractState;
import net.coderodde.zerosum.ai.EvaluatorFunction;

/**
 * This class implements the 
 * <a href="https://en.wikipedia.org/wiki/Principal_variation_search">
 * principal variation search</a> algorithm for game tree search.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 13, 2019)
 * @since 1.6 (Sep 13, 2019)
 */
public final class PrincipalVariationSearchGameEngine 
        <S extends AbstractState<S, P>,
         P extends Enum<P>> 
           extends AbstractGameEngine<S, P> {

    public PrincipalVariationSearchGameEngine(
            EvaluatorFunction<S> evaluatorFunction,
            int depth) {
        super(evaluatorFunction, depth, Integer.MAX_VALUE);
    }
    
    @Override
    public S makePly(S state, 
                     P minimizingPlayer, 
                     P maximizingPlayer, 
                     P initialPlayer) {
        state.setDepth(depth);
        
        return makePlyImplTopmost(state,
                                  depth,
                                  Double.NEGATIVE_INFINITY,
                                  Double.POSITIVE_INFINITY,
                                  initialPlayer == minimizingPlayer ? -1 : 1);
    }
    
    /**
     * Performs the search directly under the root node denoted by 
     * {@code state].
     * 
     * @param state the root state of the game tree to search.
     * @param depth the total depth of the search.
     * @param alpha the alpha cutoff value.
     * @param beta  the beta cutoff value.
     * @param color the color. -1 for minimizing player, +1 for maximizing
     *              player.
     * @return the game board after optimal move from {@code state}.
     */
    private S makePlyImplTopmost(S state,
                                 int depth,
                                 double alpha,
                                 double beta,
                                 int color) {
        boolean firstChild = true;
        S bestState = null;
        double tentativeScore = color == -1 ?
                                Double.POSITIVE_INFINITY :
                                Double.NEGATIVE_INFINITY;
        
        for (S child : state.children()) {
            double score;
            
            if (firstChild) {
                firstChild = false;
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -beta, 
                                     -alpha,
                                     -color);
                bestState = child;
                tentativeScore = score;
            } else {
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -alpha - 1.0, 
                                     -alpha,
                                     -color);
                
                if (color == -1) {
                    if (tentativeScore > score) {
                        tentativeScore = score;
                        bestState = child;
                    }
                } else {
                    if (tentativeScore < score) {
                        tentativeScore = score;
                        bestState = child;
                    }
                }
                
                if (alpha < score && score < beta) {
                    score = -makePlyImpl(child, 
                                         depth - 1,
                                         -beta,
                                         -score,
                                         -color);
                    
                    if (color == -1) {
                        if (tentativeScore > score) {
                            tentativeScore = score;
                            bestState = child;
                        }
                    } else {
                        if (tentativeScore < score) {
                            tentativeScore = score;
                            bestState = child;
                        }
                    }
                }
            }
            
            if (alpha < score) {
                alpha = score;
            }
            
            if (alpha >= beta) {
                break;
            }
        }
        
        return bestState;
    }
    
    private double makePlyImpl(S state,
                               int depth,
                               double alpha,
                               double beta,
                               int color) {
        if (state.getDepth() == 0 
                || state.checkVictory() != null
                || state.isTerminal()) {
            return color * evaluatorFunction.evaluate(state);
        }
        
        boolean firstChild = true;
        
        for (S child : state.children()) {
            double score;
            
            if (firstChild) {
                firstChild = false;
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -beta, 
                                     -alpha,
                                     -color);
            } else {
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -alpha - 1.0, 
                                     -alpha,
                                     -color);
                
                if (alpha < score && score < beta) {
                    score = -makePlyImpl(child, 
                                         depth - 1,
                                         -beta,
                                         -score,
                                         -color);
                }
            }
            
            alpha = Math.max(alpha, score);
            
            if (alpha >= beta) {
                break;
            }
        }
        
        return alpha;
    }
}
