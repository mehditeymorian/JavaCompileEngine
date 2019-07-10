package jce.processing;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import java.util.Map;

import static jce.processing.TimerProcessorState.*;

@RequiredArgsConstructor
public class TimerProcessor implements OnFinishListener {

    private Thread timer;
    private BasicProcessor processor;
    private OnFinishListener onFinishListener;

    @NonNull @Getter private String[] commands;
    @NonNull private int timeExceedInMillis;

    @Getter
    private TimerProcessorState processorState = NOT_FINISHED; // default value -- none of the options

    public void start(){
        onFinishListener = this;
        timer = getTimer();
        processor = getProcessor();

        timer.start();
        processor.start();

        synchronized (this){
            try { wait(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        } // stop current thread to wait for TimerProcess to finishing The Process or Timer to exceed the time
    }

    @Override
    public void OnFinish(TimerProcessorState state) {
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

    private Thread getTimer(){
        return new Thread(() -> {
            // Timer Runnable
            try { Thread.sleep(timeExceedInMillis); }
            catch (InterruptedException ignored) {/* No Action Needed */ }
            if (processorState.taskNotFinishedEarly()) onFinishListener.OnFinish(TIME_EXCEEDED);

        });
    }

    private BasicProcessor getProcessor(){
        return new BasicProcessor(commands, () -> {
            // After Process
            if (processorState.timeNotExceeded()) onFinishListener.OnFinish(TASK_FINISHED_EARLY);
        });
    }

    public Map<Integer,String> getProcessList(){
        return processor.getProcessList();
    }

    public String getLog(){
        return processor.getLog();
    }

}
