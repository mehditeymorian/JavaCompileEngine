package main.java.jce.processing;

import lombok.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * The BasicProcessor program implements a set of methods to process commands with help of {@link Runtime}.
 * This class extends from {@link Thread} so as it run on a different thread or simply it run in background
 * Note: This program is Written with Lombok. @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@RequiredArgsConstructor
 public class BasicProcessor extends Thread {

    private Runtime runtime = Runtime.getRuntime();

    /**
     * processList is a HashMap with numeral keys and String values that store result of {@link Process#getInputStream()} as String.
     * keys are commands indices
     */
    @Getter
    private Map<Integer, String> processList = new HashMap<>();


    /**
     * Commands that will be processed by {@link Runtime}
     */
    @NonNull @Getter
    private String[] commands;

    /**
     * An action that need to be done after all commands processed
     */
    @Setter
    private Runnable afterProcess;

    /**
     * An Interface that provide two method {@link OnEachProcessListener#processResult(String, int)}
     *  {@link OnEachProcessListener#command(String, int)} for realtime feedback
     */
    @Setter
    private OnEachProcessListener onEachProcessListener;

    /**
     * State values used in {@link #realtimeFeedback(int, String, int)} to write shorter code
     */
    private static final int COMMAND = 0;
    private static final int RESULT = 1;

    /**
     * Duration of the process of whole commands
     */
    @Getter
    private long duration;

    @SuppressWarnings("WeakerAccess")
    public BasicProcessor(@NonNull String[] commands, Runnable afterProcess) {
        this.commands = commands;
        this.afterProcess = afterProcess;
    }

    @Override
    public void run() {
        super.run();
        duration = System.currentTimeMillis(); // start time of process

        // used IntStream to Iterate through commands and process them.
        // while processing command if BasicProcessor become Interrupted, loop break and process will be finished
        //noinspection ResultOfMethodCallIgnored
        IntStream.range(0,commands.length).allMatch( index -> {
                    String command = commands[index];
                    realtimeFeedback(COMMAND, command, index);
                    return invokeProcess(command, index);

                });

        calculateDuration();
        invokeAfterProcess();

    }

    /**
     * @param command current command that is gonna processed
     * @param index Current index. in other words, index of command parameter
     * @return return false if process interrupted otherwise return true
     */
    private boolean invokeProcess(String command, int index){
        try {
            invokeProcessBody(command, index);

        } catch (IOException e) {
            processList.put(index, e.getMessage());
            realtimeFeedback(RESULT, e.getMessage(), index);

        } catch (InterruptedException e) {
            processList.put(index, e.getMessage());
            realtimeFeedback(RESULT, e.getMessage(), index);
            return false; /* Process at this index failed */
        }

        return true; // Process successfully done
    }

    private void invokeProcessBody(String command, int index) throws InterruptedException, IOException {
        Process process = runtime.exec(command); // execute command
        process.waitFor(); // wait for process to finish

        String processResult = processResult(process);
        processList.put(index, processResult);
        realtimeFeedback(RESULT, processResult, index);

    }

    /**
     * @param type is {@link #COMMAND} or {@link #RESULT}
     * @param value value of feedback base on the type
     * @param index Current index. in other words, index of current process
     */
    private void realtimeFeedback(int type, String value, int index){
        if (onEachProcessListener == null) return;

        switch (type){
            case COMMAND:
                onEachProcessListener.command(value, index);
                break;
            case RESULT:
                onEachProcessListener.processResult(value, index);
                break;
                default:
        }
    }

    private void calculateDuration(){
        duration = System.currentTimeMillis() - duration;
    }

    private void invokeAfterProcess(){
        if (afterProcess != null) afterProcess.run();
    }

    /**
     * @param inputStream is {@link Process#getInputStream()} that is the result of process
     * @return converted of Process result
     */
    private String inputStreamToStr(InputStream inputStream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
        String result ="";
        String line;
        while ( (line = input.readLine()) != null) result = result.concat(line).concat("\n");
        return result;
    }

    /**
     * @return  Result of process. if there wasn't any errors, return inputStream otherwise return errorStream as String.
     */
    private String processResult(Process process) throws IOException {
        String input = inputStreamToStr(process.getInputStream()); // convert process result to String
        return input.trim().isEmpty() ? inputStreamToStr(process.getErrorStream()) : input;
    }

}
