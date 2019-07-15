package jce.processing;

/**
 * A program to generate  a realtime feedback for eachProcess
 * used in {@link BasicProcessor}
 */
public interface OnEachProcessListener {

    void command(String command, int index);

    void processResult(String result, int index);
}
