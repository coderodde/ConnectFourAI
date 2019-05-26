package net.coderodde.connectfour.ai;

/**
 * This interface defines the API for evaluation functions.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 26, 2019)
 * @param <S> the state type.
 */
public interface EvaluatorFunction<S> {
    
    /**
     * Evaluates the given state and returns the result.
     * @param state the state to evaluate.
     * @return the evaluation score.
     */
    public double evaluate(S state);
}
