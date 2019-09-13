package net.coderodde.zerosum.ai.impl;

import java.util.List;
import net.coderodde.zerosum.ai.AbstractGameEngine;
import net.coderodde.zerosum.ai.AbstractState;
import net.coderodde.zerosum.ai.EvaluatorFunction;

/**
 * This class implements the <a href="https://en.wikipedia.org/wiki/Principal_variation_search">Negascout</a> algorithm for game tree
 * search.
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
    public S makePly(S state, P minimizingPlayer, P maximizingPlayer, P initialPlayer) {
        state.setDepth(depth);
        
        return makePlyImplTopmost(state,
                                  depth,
                                  Double.NEGATIVE_INFINITY,
                                  Double.POSITIVE_INFINITY,
                                  minimizingPlayer,
                                  maximizingPlayer,
                                  initialPlayer);
    }
    
    private S makePlyImplTopmost(S state,
                                 int depth,
                                 double alpha,
                                 double beta,
                                 P minimizingPlayer,
                                 P maximizingPlayer,
                                 P currentPlayer) {
        List<S> children = state.children();
        boolean firstChild = true;
        S bestState = null;
        
        for (S child : children) {
            double score;
            
            if (firstChild) {
                firstChild = false;
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -beta, 
                                     -alpha,
                                     minimizingPlayer,
                                     maximizingPlayer,
                                     currentPlayer == maximizingPlayer ? 
                                             minimizingPlayer : 
                                             maximizingPlayer);
            } else {
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -alpha - 1.0, 
                                     -alpha,
                                     minimizingPlayer,
                                     maximizingPlayer,
                                     currentPlayer == maximizingPlayer ?
                                             minimizingPlayer : 
                                             maximizingPlayer);
                
                if (alpha < score && score < beta) {
                    score = -makePlyImpl(child, 
                                         depth - 1,
                                         -beta,
                                         -score,
                                         minimizingPlayer,
                                         maximizingPlayer,
                                         currentPlayer == maximizingPlayer ? 
                                                 minimizingPlayer : 
                                                 maximizingPlayer);
                }
            }
            
            if (alpha < score) {
                alpha = score;
                bestState = child;
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
                               P minimizingPlayer,
                               P maximizingPlayer,
                               P currentPlayer) {
        if (state.getDepth() == 0 
                || state.checkVictory() != null
                || state.isTerminal()) {
            return evaluatorFunction.evaluate(state);
        }
        
        List<S> children = state.children();
        boolean firstChild = true;
        
        for (S child : children) {
            double score;
            
            if (firstChild) {
                firstChild = false;
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -beta, 
                                     -alpha,
                                     minimizingPlayer,
                                     maximizingPlayer,
                                     currentPlayer == maximizingPlayer ? 
                                             minimizingPlayer : 
                                             maximizingPlayer);
            } else {
                score = -makePlyImpl(child, 
                                     depth - 1, 
                                     -alpha - 1.0, 
                                     -alpha,
                                     minimizingPlayer,
                                     maximizingPlayer,
                                     currentPlayer == maximizingPlayer ?
                                             minimizingPlayer : 
                                             maximizingPlayer);
                
                if (alpha < score && score < beta) {
                    score = -makePlyImpl(child, 
                                         depth - 1,
                                         -beta,
                                         -score,
                                         minimizingPlayer,
                                         maximizingPlayer,
                                         currentPlayer == maximizingPlayer ? 
                                                 minimizingPlayer : 
                                                 maximizingPlayer);
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
