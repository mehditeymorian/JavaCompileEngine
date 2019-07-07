package jce.processing;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class TimeExceedChecker {

    private Processor processor;
    private int limitTime;
    private OnProcessFinish onProcessFinish;

    public void start(){
        long start = System.currentTimeMillis();
        processor.start();
        startCounting(start);
        onProcessFinish.onFinish(getProcessInfo());
    }

    private void startCounting(long start){
        while (!processor.isFinishedBeforeExceed()){
            if (isTimeExceeded(limitTime,start)){
                processor.setProcessState(ProcessState.TIME_EXCEEDED);
                break;
            }
        }
    }

    private ProcessInfo getProcessInfo(){
        return ProcessInfo.builder()
                .process(processor.getProcessList())
                .log(processor.getLog())
                .commands(processor.getCommands())
                .build();
    }

    private boolean isTimeExceeded(int limit, long start){
        return System.currentTimeMillis() - start > limit;
    }

}
