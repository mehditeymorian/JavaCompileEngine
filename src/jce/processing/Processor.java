package jce.processing;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.*;

/**
 * Built-in class for processing Commands with {@link Runtime}
 */
public class Processor extends Thread{


    /**
     * get {@link Runtime} from static method {@link Runtime#getRuntime()}
     * use this object to process commands
     */
    private Runtime runtime = Runtime.getRuntime();

    @Getter private Map<Integer,Process> processList;

    @Getter private String log;

    private String[] commands;

    @Getter @Setter(value = AccessLevel.PACKAGE)
    private int processState = ProcessState.RUNNING;


    public Processor(String[] commands) {
        super();
        this.commands = commands;
        log = "";
        processList = new HashMap<>();
    }

    @Override
    public void run() {
        super.run();
        for (int index = 0 ; index < commands.length; index++) {
            String command = commands[index];
            if (isTimeExceeded()) break;
            appendLog(numericalText(command,index+1));
            executeCommand(command,index+1);
        }
        if (!isTimeExceeded()) setProcessState(ProcessState.FINISHED_BEFORE_EXCEED);
    }

    private void executeCommand(String command, int commandIssue){
        try {
            Process process = runtime.exec(command);
            process.waitFor();
            processList.put(commandIssue, process);
        } catch (IOException | InterruptedException e) {
            appendLog(numericalText(e.getMessage(),commandIssue));
        }
    }

    private void appendLog(String log){
        this.log = log.concat(log).concat("\n");
    }

    private String numericalText(String text, int number){
        return String.format("%d) %s",number,text);
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
