package net.coderodde.zerosum.ai.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.coderodde.zerosum.ai.AbstractGameEngine;
import net.coderodde.zerosum.ai.impl.AlphaBetaPruningGameEngine;
import net.coderodde.zerosum.ai.impl.SortingAlphaBetaPruningGameEngine;
import net.coderodde.zerosum.ai.impl.MinimaxGameEngine;
import net.coderodde.zerosum.ai.impl.PrincipalVariationSearchGameEngine;

/**
 * This class implements a demonstration of the game-playing algorithms.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jun 15, 2019)
 */
public final class Demo {
    
    private static final double MAXIMIZING_PLAYER_VICTORY_CUT_OFF = 2.0;
    private static final double MINIMIZING_PLAYER_VICTORY_CUT_OFF = -2.0;
    private static final int MAXIMUM_DEPTH = 3;
    private static final int MINIMUM_CHILDREN = 2;
    private static final int MAXIMUM_CHILDREN = 4;
    
    private static void 
        warmup(Random random,
               AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine) {
        runGameEngine(random, 
                      gameEngine,
                      MINIMIZING_PLAYER_VICTORY_CUT_OFF,
                      MAXIMIZING_PLAYER_VICTORY_CUT_OFF,
                      MINIMUM_CHILDREN,
                      MAXIMUM_CHILDREN,
                      false);
    }
    
    private static void 
        benchmark(Random random,
                  AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine) {
        runGameEngine(random, 
                      gameEngine,
                      MINIMIZING_PLAYER_VICTORY_CUT_OFF,
                      MAXIMIZING_PLAYER_VICTORY_CUT_OFF,
                      MINIMUM_CHILDREN,
                      MAXIMUM_CHILDREN,
                      true);
    }
    
    private static void 
        runGameEngine(Random random,
                      AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine,
                      double minimizingPlayerVictoryCutOff,
                      double maximizingPlayerVictoryCutOff,
                      int minimumChildren,
                      int maximumChildren,
                      boolean print) {
        DemoState.resetCounter();
        DemoState currentState = 
                new DemoState(random, 
                              DemoPlayerColor.MAXIMIZING_PLAYER,
                              minimizingPlayerVictoryCutOff,
                              maximizingPlayerVictoryCutOff,
                              minimumChildren,
                              maximumChildren);
        
        List<DemoState> playedStates = new ArrayList<>();
        DemoPlayerColor demoPlayerColor = DemoPlayerColor.MAXIMIZING_PLAYER;
        playedStates.add(currentState);
        
        long startTime = System.nanoTime();
        
        do {
            currentState = gameEngine.makePly(currentState,
                                              DemoPlayerColor.MINIMIZING_PLAYER, 
                                              DemoPlayerColor.MAXIMIZING_PLAYER, 
                                              demoPlayerColor);      
            if (currentState == null) {
                break;
            }
            
            playedStates.add(currentState);
            
            demoPlayerColor = 
                    demoPlayerColor == DemoPlayerColor.MAXIMIZING_PLAYER ? 
                DemoPlayerColor.MINIMIZING_PLAYER :
                DemoPlayerColor.MAXIMIZING_PLAYER;
        } while (!currentState.isTerminal());
        
        long endTime = System.nanoTime();
        
        if (print) {
            for (DemoState state : playedStates) {
                System.out.println(state);
            }
            
            float microseconds = (endTime - startTime) / 1000.0f;
            
            System.out.println(playedStates.size() + " states in " +
                               microseconds + " microseconds in " + 
                                gameEngine.getClass().getSimpleName() + ".");
        }
    }
    
    public static void main(String[] args) {    
        long seed = 10L; System.currentTimeMillis();
        Random random1 = new Random(seed);
        Random random2 = new Random(seed);
        Random random3 = new Random(seed);
        Random random4 = new Random(seed);
        
        DemoEvaluatorFunction ef = new DemoEvaluatorFunction();
        AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine1;
        AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine2;
        AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine3;
        AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine4;
        
        gameEngine1 = new MinimaxGameEngine<>(ef, MAXIMUM_DEPTH);
        gameEngine2 = new AlphaBetaPruningGameEngine<>(ef, MAXIMUM_DEPTH);
        gameEngine3 = new SortingAlphaBetaPruningGameEngine<>(ef,
                                                              MAXIMUM_DEPTH);
        gameEngine4 = new PrincipalVariationSearchGameEngine<>(ef, 
                                                               MAXIMUM_DEPTH);
        System.out.println("seed = " + seed);
        
        // Warm up:
        System.out.println("Warming up...");
        warmup(random1, gameEngine1);
        warmup(random2, gameEngine2);
        warmup(random3, gameEngine3);
        warmup(random4, gameEngine4);
        System.out.println("Warmed up!");
        
        // Benchmark:
        benchmark(random1, gameEngine1);
        benchmark(random2, gameEngine2);
        benchmark(random3, gameEngine3);
        benchmark(random4, gameEngine4);
    }
}
