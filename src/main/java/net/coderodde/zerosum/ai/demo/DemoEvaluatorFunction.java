package net.coderodde.zerosum.ai.demo;

import java.util.Random;
import net.coderodde.zerosum.ai.EvaluatorFunction;

/**
 * This class implements a demonstrative evaluator function.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jun 15, 2019)
 */
public final class DemoEvaluatorFunction 
        implements EvaluatorFunction<DemoState> {

    @Override
    public double evaluate(DemoState state) {
        return state.getValue();
    }
}
