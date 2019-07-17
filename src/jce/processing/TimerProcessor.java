package jce.processing;


import lombok.Getter;
import lombok.NonNull;

import static jce.processing.ProcessorState.*;

/**
 * <p><TimerProcessor is pretty much the same as {@link BasicProcessor}
 * with a timer that provide the class a TimeExceed Feature./p>
 * Note: This program is Written with Lombok.
 * @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
public class TimerProcessor implements OnFinishListener {

    private Thread timer;

    private BasicProcessor processor;

    private OnFinishListener onFinishListener;

    @NonNull @Getter private String[] commands;

    /**
     * The Duration that expected to BasicProcessor be finished
     */
    private int exceedTimeInMillis;

    /**
     * state of process.
     * NOT_FINISHED is the default.
     */
    @Getter
    private ProcessorState processorState = NOT_FINISHED;


    public TimerProcessor(@NonNull String[] commands, int exceedTimeInMillis) {
        this.commands = commands;
        this.exceedTimeInMillis = exceedTimeInMillis;
        timer = buildTimer();
        processor = buildProcessor();
    }


    public void start() throws InterruptedException {
        onFinishListener = this;

        timer.start();
        processor.start();

        // stop current thread to wait for one of the BasicProcessor or Timer finish sooner.
        synchronized (this){
             wait();
        }
    }

    /**
     * @param state state of process e.g. {@link ProcessorState#TASK_FINISHED_EARLY}. see {@link ProcessorState} for more states.
     *              whenever BasicProcessor or Timer be finished, call onFinish after.
     *              if BasicProcessor finish first, call the onFinish with parameter of {@link ProcessorState#TASK_FINISHED_EARLY}.
     *              if Timer finish first, call the onFinish with parameter of {@link ProcessorState#TIME_EXCEEDED}.
     *              after one of the finished, finally, Notify TimerProcessor to continue.
     */
    @Override
    public void onFinish(ProcessorState state) {
        processorState = state;
        switch (state){
            case TIME_EXCEEDED:// Time Exceeded
                processor.interrupt();
                break;

            case TASK_FINISHED_EARLY:// Process Finished Before Time Exceeded
                timer.interrupt();
                break;
                default:
        }

        synchronized (this){ notify(); } // notify current thread that process is finished
    }

    /**
     * @return A Thread that sleep for {@link #exceedTimeInMillis} amount of time.
     */
    private Thread buildTimer(){
        return new Thread(() -> {
            // Timer Runnable
            try { Thread.sleep(exceedTimeInMillis); }
            catch (InterruptedException ignored) {/* No Action Needed */ }
            if (processorState.taskNotFinishedEarly()) onFinishListener.onFinish(TIME_EXCEEDED);

        });
    }

    /**
     * @return A BasicProcessor with a AfterProcess Runnable that call onFinish method after process finished.
     */
    private BasicProcessor buildProcessor(){
        return new BasicProcessor(commands, () -> {
            // After Process
            if (processorState.timeNotExceeded()) onFinishListener.onFinish(TASK_FINISHED_EARLY);
        });
    }

    public BasicProcessor getProcessor(){
        return processor;
    }

}
