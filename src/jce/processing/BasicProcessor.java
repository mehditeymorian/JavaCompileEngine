package jce.processing;

import lombok.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * Built-in class for processing Commands with {@link Runtime}
 */


@RequiredArgsConstructor
 public class BasicProcessor extends Thread {


    /**
     * get {@link Runtime} from static method {@link Runtime#getRuntime()}
     * use this object to invokeProcess commands
     */
    private Runtime runtime = Runtime.getRuntime();

    @Getter
    private Map<Integer, String> processList = new HashMap<>();

    @Getter
    private String log = "";

    @NonNull @Getter
    private String[] commands;

    @Setter
    private Runnable afterProcess;

    public BasicProcessor(@NonNull String[] commands, Runnable afterProcess) {
        this.commands = commands;
        this.afterProcess = afterProcess;
    }

    @Override
    public void run() {
        super.run();
        //noinspection ResultOfMethodCallIgnored
        IntStream.range(0,commands.length).allMatch( index -> {
                    String command = commands[index];
                    log(command, index);
                    try { invokeProcess(command, index); }
                    catch (IOException e) { log(e.getMessage(), index); }
                    catch (InterruptedException e) { return false; /* Process at this index failed */ }
                    return true; // Process successfully done
                });

        if (afterProcess != null) afterProcess.run();
    }

    private void invokeProcess(String command, int index) throws InterruptedException, IOException {
        Process process = runtime.exec(command);
        process.waitFor();
        processList.put(index, inputStreamToStr(process.getInputStream()));
    }

    private void log(String newLog, int number){
        this.log = this.log.concat(String.format("%d) %s",number,newLog)).concat("\n");
    }

    private String inputStreamToStr(InputStream inputStream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
        String result ="";
        String str;
        while ( (str = input.readLine()) != null) result = result.concat(str);
        return result;
    }

}
