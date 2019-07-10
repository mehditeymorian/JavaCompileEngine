package jce.processing;

public interface OnEachProcessListener {

    void log(String log, int index);

    void processResult(String result, int index);
}
