package jce.processing;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TimerProcessorState {
    NOT_FINISHED(-1),
    TIME_EXCEEDED(0),
    TASK_FINISHED_EARLY(1);

    @NonNull
    @Getter
    private int state;

     public boolean taskNotFinishedEarly(){
        return this.getState() != TASK_FINISHED_EARLY.getState();
    }

     public boolean timeNotExceeded(){
        return this.getState() != TIME_EXCEEDED.getState();
    }

}
