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

    private static final double AMPLIFY = 10.0;
    
    private final Random random;
    
    public DemoEvaluatorFunction(Random random) {
        this.random = random;
    }
    
    @Override
    public double evaluate(DemoState state) {
        return random.nextGaussian() * AMPLIFY;
    }
}
