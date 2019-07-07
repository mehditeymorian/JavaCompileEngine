package jce.processing;


import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
class ProcessInfo {
    @Getter private Map<Integer,Process> process;
    @Getter private String log;
    @Getter private String[] commands;
}
