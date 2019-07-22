package main.java.jce.processing;

/**
 * <p>
 * An Interface associated with {@link TimerProcessor} that invoke {@link #onFinish(ProcessorState)}
 * whenever one of BasicProcessor or Timer finished earlier.
 * </p>
 */
public interface OnFinishListener {

    /**
     * @param state state of process e.g. {@link ProcessorState#TASK_FINISHED_EARLY}. see {@link ProcessorState} for more states.
     */
    void onFinish(ProcessorState state);
}
