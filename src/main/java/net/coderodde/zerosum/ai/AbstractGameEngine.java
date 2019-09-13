package net.coderodde.zerosum.ai;

/**
 * This abstract class defines the API for game-playing AI algorithms such as 
 * Minimax, Alpha-beta pruning, and so on.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 26, 2019)
 * @param <S> the board state type.
 * @param <P> the player color type.
 */
public abstract class AbstractGameEngine<
    S extends AbstractState<S, P>,
    P extends Enum<P>
> {

    /**
     * The minimum depth of the game tree to traverse.
     */
    private static final int MINIMUM_DEPTH = 1;

    /**
     * The depth, after reaching which, the search spawns isolated tasks for a 
     * thread pool to process.
     */
    private static final int MINIMUM_PARALLEL_DEPTH = 1;

    /**
     * The state evaluator function.
     */
    protected EvaluatorFunction<S> evaluatorFunction;

    /**
     * The maximum depth of the game tree to construct.
     */
    protected int depth;

    /**
     * The depth after which to switch to parallel computation.
     */
    protected int parallelDepth;

    /**
     * Constructs this game engine with given parameters. Note that if 
     * {@code parallelDepth > depth}, the entire computation will be run in this
     * thread without spawning 
     * @param evaluatorFunction
     * @param depth
     * @param parallelDepth 
     */
    public AbstractGameEngine(EvaluatorFunction<S> evaluatorFunction,
                      int depth,
                      int parallelDepth) {
        setEvaluatorFunction(evaluatorFunction);
        setDepth(depth);
        setParallelDepth(parallelDepth);
    }

    public EvaluatorFunction<S> getEvaluatorFunction() {
        return evaluatorFunction;
    }

    public int getDepth() {
        return depth;
    }

    public int getParallelDepth() {
        return parallelDepth;
    }

    public void setEvaluatorFunction(EvaluatorFunction<S> evaluatorFunction) {
        this.evaluatorFunction = evaluatorFunction;
    }

    public void setDepth(int depth) {
        this.depth = checkDepth(depth);
    }

    public void setParallelDepth(int parallelDepth) {
        this.parallelDepth = checkParallelDepth(parallelDepth);
    }

    /**
     * Computes and makes a single move. 
     * @param state the source game state.
     * @param minimizingPlayer the player that seeks to minimize the score.
     * @param maximizingPlayer the player that seeks to maximize the score.
     * @param initialPlayer the initial player. Must be either 
     * {@code minimizingPlayer} or {@code maximizingPlayer}. The ply is computed
     * for this specific player.
     * @return the next game state.
     */
    public abstract S makePly(S state, 
                              P minimizingPlayer,
                              P maximizingPlayer,
                              P initialPlayer);

    /**
     * Validates the depth candidate.
     * @param depthCandidate the depth candidate to validate.
     * @return the depth candidate if valid.
     */
    private int checkDepth(int depthCandidate) {
        if (depthCandidate < MINIMUM_DEPTH) {
            throw new IllegalArgumentException(
                    "The requested depth (" + depthCandidate + ") is too " + 
                    "small. Must be at least " + MINIMUM_DEPTH + ".");
        }

        return depthCandidate;
    }

    /**
     * Validates the parallel depth candidate.
     * @param parallelDepthCandidate the parallel depth candidate to validate.
     * @return the parallel depth candidate.
     */
    private int checkParallelDepth(int parallelDepthCandidate) {
        if (parallelDepthCandidate < MINIMUM_PARALLEL_DEPTH) {
            throw new IllegalArgumentException(
                    "The requested parallel depth (" + parallelDepthCandidate +
                    ") is too small. Must be at least " +
                    MINIMUM_PARALLEL_DEPTH + ".");
        }

        return parallelDepthCandidate;
    }
}
