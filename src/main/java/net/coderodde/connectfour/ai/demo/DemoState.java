package net.coderodde.connectfour.ai.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import net.coderodde.connectfour.ai.State;

/**
 * This class implements a demonstrative game state.
 * 
 * @author Rodion "rodde" Efremov 
 * @version 1.6 (Jun 15, 2019)
 */
public final class DemoState implements State<DemoState> {

    private final Random random;
    private final int minimumChildren;
    private final int maximumChildren;
    
    public DemoState(Random random, 
                     int minimumChildren,
                     int maximumChildren) {
        this.random = random;
        this.minimumChildren = minimumChildren;
        this.maximumChildren = maximumChildren;
    }
    
    @Override
    public Collection<DemoState> children() {
        Collection<DemoState> children = new ArrayList<>();
        
        int numberOfChildren = minimumChildren + 
                random.nextInt(maximumChildren - minimumChildren + 1);
        
        for (int i = 0; i < numberOfChildren; i++) {
            children.add(
                    new DemoState(
                            random,
                            minimumChildren, 
                            maximumChildren));
        }
        
        return children;
    }
    
    public boolean isTerminal() {
        return random.nextGaussian() > 1.5;
    }
}
