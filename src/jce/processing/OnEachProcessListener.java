package jce.processing;

public interface OnEachProcessListener {

    void command(String command, int index);

    void processResult(String result, int index);
}
