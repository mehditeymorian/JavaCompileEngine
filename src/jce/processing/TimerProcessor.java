package jce.processing;


import lombok.Getter;
import lombok.NonNull;

import static jce.processing.ProcessorState.*;

public class TimerProcessor implements OnFinishListener {

    private Thread timer;
    private BasicProcessor processor;
    private OnFinishListener onFinishListener;

    @NonNull @Getter private String[] commands;
    @NonNull private int timeExceedInMillis;

    @Getter
    private ProcessorState processorState = NOT_FINISHED; // default value -- none of the options

    public TimerProcessor(@NonNull String[] commands, @NonNull int timeExceedInMillis) {
        this.commands = commands;
        this.timeExceedInMillis = timeExceedInMillis;
        timer = buildTimer();
        processor = buildProcessor();
    }

    public void start(){
        onFinishListener = this;

        timer.start();
        processor.start();

        synchronized (this){
            try { wait(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        } // stop current thread to wait for TimerProcess to finishing The Process or Timer to exceed the time
    }

    @Override
    public void OnFinish(ProcessorState state) {
        processorState = state;
        switch (state){
            case TIME_EXCEEDED:// Time Exceeded
                processor.interrupt();
                break;

            case TASK_FINISHED_EARLY:// Process Finished Before Time Exceeded
                timer.interrupt();
                break;
        }

        synchronized (this){ notify(); } // notify current thread that process is finished
    }

    private Thread buildTimer(){
        return new Thread(() -> {
            // Timer Runnable
            try { Thread.sleep(timeExceedInMillis); }
            catch (InterruptedException ignored) {/* No Action Needed */ }
            if (processorState.taskNotFinishedEarly()) onFinishListener.OnFinish(TIME_EXCEEDED);

        });
    }

    private BasicProcessor buildProcessor(){
        return new BasicProcessor(commands, () -> {
            // After Process
            if (processorState.timeNotExceeded()) onFinishListener.OnFinish(TASK_FINISHED_EARLY);
        });
    }

    public BasicProcessor getProcessor(){
        return processor;
    }

}
