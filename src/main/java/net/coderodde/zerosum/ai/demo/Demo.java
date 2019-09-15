package net.coderodde.zerosum.ai.demo;

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
    private static final int MAXIMUM_DEPTH = 2;
    private static final int MINIMUM_CHILDREN = 1;
    private static final int MAXIMUM_CHILDREN = 2;
//    private static final int MAXIMUM_DEPTH = 2;
//    private static final int MINIMUM_CHILDREN = 3;
//    private static final int MAXIMUM_CHILDREN = 6;
    
    private static void 
        warmup(AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine,
               DemoState root) {
        runGameEngine(gameEngine, root, false);
    }
    
    private static DemoState
         benchmark(AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine,
                   DemoState root) {
        return runGameEngine(gameEngine, root, true);
    }
        
    private static DemoState runGameEngine(
            AbstractGameEngine<DemoState, DemoPlayerColor> gameEngine,
            DemoState root,
            boolean printMessages) {
        gameEngine.setDepth(MAXIMUM_DEPTH);
        root.setDepth(MAXIMUM_DEPTH);
        long startTime = System.nanoTime();
        
        DemoState ret = gameEngine.makePly(root, 
                                           DemoPlayerColor.MINIMIZING_PLAYER, 
                                           DemoPlayerColor.MAXIMIZING_PLAYER, 
                                           DemoPlayerColor.MINIMIZING_PLAYER);
        
        long endTime = System.nanoTime();
        
        if (printMessages) {
            System.out.println(gameEngine.getClass().getSimpleName() + 
                               " in " + (endTime - startTime) / 1000.0f + 
                               " microseconds.");
        }
        
        return ret;
    }
    
    public static void main(String[] args) {    
        long seed = 1568550404808L; System.currentTimeMillis();
        System.out.println("Seed = " + seed);
        Random random1 = new Random(seed);
        
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
        System.out.println("Building the test tree...");
        DemoState root = createGameTreeRootState(random1);
        
        // Warm up:
        System.out.println("Warming up...");
        warmup(gameEngine1, root);
        warmup(gameEngine2, root);
        warmup(gameEngine3, root);
        warmup(gameEngine4, root);
        System.out.println("Warmed up!");
        
        // Benchmark:
        System.out.println("Benchmarking...");
        DemoState result1 = benchmark(gameEngine1, root);
        DemoState result2 = benchmark(gameEngine2, root);
        DemoState result3 = benchmark(gameEngine3, root);
        DemoState result4 = benchmark(gameEngine4, root);
        
        System.out.println("Result nodes:");
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(result4);
        
        
    }
    
    private static DemoState createGameTreeRootState(Random random) {
        return createGameTreeRootState(MAXIMUM_DEPTH,
                                       MINIMUM_CHILDREN,
                                       MAXIMUM_CHILDREN, 
                                       MINIMIZING_PLAYER_VICTORY_CUT_OFF, 
                                       MAXIMIZING_PLAYER_VICTORY_CUT_OFF, 
                                       random);
    }
    
    private static DemoState createGameTreeRootState(int depth,
                                                     int minimumChildren,
                                                     int maximumChildren,
                                                     double minimizingPlayerVictoryCutOff,
                                                     double maximizingPlayerVictoryCutoff,
                                                     Random random) {
        DemoState root = new DemoState(random,
                                       DemoPlayerColor.MAXIMIZING_PLAYER,
                                       minimizingPlayerVictoryCutOff,
                                       maximizingPlayerVictoryCutoff,
                                       minimumChildren,
                                       maximumChildren);
        root.setDepth(depth);
        createGameTree(root, MAXIMUM_DEPTH);
        return root;
    }
    
    private static void createGameTree(DemoState state, int depth) {
        if (depth == 0 || state.checkVictory() != null) {
            return;
        }
        
        for (DemoState child : state.children()) {
            createGameTree(child, depth - 1);
        }
    }
    
    
}
