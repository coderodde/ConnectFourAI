package net.coderodde.zerosum.ai.demo;

import java.util.ArrayList;
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
    
    private static final double MAXIMIZING_PLAYER_VICTORY_CUT_OFF = 1.5;
    private static final double MINIMIZING_PLAYER_VICTORY_CUT_OFF = -1.5;
    private static final int MINIMUM_CHILDREN = 2;
    private static final int MAXIMUM_CHILDREN = 4;
    
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        
        System.out.println("seed = " + seed);
        
        GameEngine<DemoState, DemoPlayerColor> gameEngine = 
                new MinimaxGameEngine<>(new DemoEvaluatorFunction(), 6);
        
        DemoState currentState = 
                new DemoState(random, 
                              DemoPlayerColor.MAXIMIZING_PLAYER,
                              MINIMIZING_PLAYER_VICTORY_CUT_OFF,
                              MAXIMIZING_PLAYER_VICTORY_CUT_OFF,
                              MINIMUM_CHILDREN,
                              MAXIMUM_CHILDREN);
        
        List<DemoState> playedStates = new ArrayList<>();
        DemoPlayerColor demoPlayerColor = DemoPlayerColor.MAXIMIZING_PLAYER;
        playedStates.add(currentState);
        
        long startTime = System.currentTimeMillis();
        
        do {
            currentState = gameEngine.makePly(currentState,
                                              DemoPlayerColor.MINIMIZING_PLAYER, 
                                              DemoPlayerColor.MAXIMIZING_PLAYER, 
                                              demoPlayerColor);
            
            playedStates.add(currentState);
            
            demoPlayerColor = 
                    demoPlayerColor == DemoPlayerColor.MAXIMIZING_PLAYER ? 
                DemoPlayerColor.MINIMIZING_PLAYER :
                DemoPlayerColor.MAXIMIZING_PLAYER;
        } while (!currentState.isTerminal());
        
        long endTime = System.currentTimeMillis();
        
        for (DemoState state : playedStates) {
            System.out.println(state);
        }
        
        System.out.println(playedStates.size() + " plies in " +
                           (endTime - startTime) + " milliseconds.");
    }
}
