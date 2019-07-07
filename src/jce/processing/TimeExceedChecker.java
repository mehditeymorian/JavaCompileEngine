package jce.processing;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class TimeExceedChecker {

    private OnProcessFinish onProcessFinish;
    private String[] commands;
    private Processor processor;
    private int limit;


    public void start(){
        long start = System.currentTimeMillis();
        processor.start();
        startCounting(start);
        onProcessFinish.onFinish(getProcessInfo());
    }


    private void startCounting(long start){
        while (!processor.isFinishedBeforeExceed()){
            if (isTimeExceeded(limit,start)){
                processor.setProcessState(ProcessState.TIME_EXCEEDED);
                break;
            }
        }
    }

    private ProcessInfo getProcessInfo(){
        return ProcessInfo.builder()
                .process(processor.getLastProcess())
                .log(processor.getLog())
                .commands(commands)
                .build();
    }

    private boolean isTimeExceeded(int limit, long start){
        return System.currentTimeMillis() - start > limit;
    }

}
