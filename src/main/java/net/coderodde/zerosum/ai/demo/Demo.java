package net.coderodde.zerosum.ai.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.coderodde.zerosum.ai.GameEngine;
import net.coderodde.zerosum.ai.impl.MinimaxGameEngine;

/**
 * This class implements a demonstration of the game-playing algorithms.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jun 15, 2019)
 */
public final class Demo {
    
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        
        System.out.println("seed = " + seed);
        
        GameEngine<DemoState, DemoPlayerColor> gameEngine = 
                new MinimaxGameEngine<>(new DemoEvaluatorFunction(random), 3);
        
        DemoState currentState = new DemoState(random, 1, 5);
        List<DemoState> playedStates = 
                new ArrayList<>(Arrays.asList(currentState));
        
        DemoPlayerColor demoPlayerColor = DemoPlayerColor.WHITE;
        
        do {
            playedStates.add(currentState);
            currentState = gameEngine.makePly(currentState,
                                              DemoPlayerColor.WHITE, 
                                              DemoPlayerColor.BLACK, 
                                              demoPlayerColor);
            
            demoPlayerColor = demoPlayerColor == DemoPlayerColor.WHITE ? 
                DemoPlayerColor.BLACK :
                DemoPlayerColor.WHITE;
        } while (!currentState.isTerminal());
        
        System.out.println("Plies: " + playedStates.size());
    }
}
