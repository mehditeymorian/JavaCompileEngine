package jce.processing;

/**
 * <p>
 * An Interface associated with {@link TimerProcessor} that invoke {@link #OnFinish(ProcessorState)}
 * whenever one of BasicProcessor or Timer finished earlier.
 * </p>
 */
public interface OnFinishListener {

    /**
     * @param state state of process e.g. {@link ProcessorState#TASK_FINISHED_EARLY}. see {@link ProcessorState} for more states.
     */
    void OnFinish(ProcessorState state);
}
