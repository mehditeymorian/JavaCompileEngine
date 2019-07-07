package jce.processing;


import java.io.IOException;

/**
 * Built-in class for processing Commands with {@link Runtime}
 */
public class Processor extends Thread{


    /**
     * get {@link Runtime} from static method {@link Runtime#getRuntime()}
     * use this object to process commands
     */
    private Runtime runtime = Runtime.getRuntime();

    private Process lastProcess = null;

    private StringBuilder logBuilder;

    private String[] commands;

    private int processState = ProcessState.RUNNING;


    public Processor(String[] commands) {
        super();
        this.commands = commands;
        logBuilder = new StringBuilder();
    }

    @Override
    public void run() {
        super.run();

        for (String command : commands) {
            if (isTimeExceeded()) break;
            appendLog(command);
            if (!executeCommand(command)){
                lastProcess = null;
                break;
            }
        }

        if (!isTimeExceeded()) setProcessState(ProcessState.FINISHED_BEFORE_EXCEED);
    }

    private boolean executeCommand(String command){
        try {
            lastProcess = runtime.exec(command);
            lastProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            appendLog(e.getMessage());
            return false;
        }

        return true;
    }

    private void appendLog(String log){
        logBuilder.append(log).append("\n");
    }

    final String getLog() {
        return logBuilder.toString();
    }


    ///////////////////////// SETTER GETTER METHODS //////////////////////////////


    final Process getLastProcess(){
        return lastProcess;
    }

     private int getProcessState() {
        return processState;
    }

      final void setProcessState(int processState) {
        this.processState = processState;
    }

     final boolean isRunning(){
        return getProcessState() == ProcessState.RUNNING;
    }

     final boolean isFinishedBeforeExceed(){
        return getProcessState() == ProcessState.FINISHED_BEFORE_EXCEED;
    }

     private boolean isTimeExceeded(){
        return getProcessState() == ProcessState.TIME_EXCEEDED;
    }

}
