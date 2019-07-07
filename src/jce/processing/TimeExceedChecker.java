package jce.processing;

public final class TimeExceedChecker {

    private OnProcessFinish onProcessFinish;

    private int limit;

    private String[] commands;

    private Processor processor;

    public TimeExceedChecker(Processor processor, String[] commands, int limit, OnProcessFinish onProcessFinish) {
        this.processor = processor;
        this.limit = limit;
        this.commands = commands;
        this.onProcessFinish = onProcessFinish;
    }

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
