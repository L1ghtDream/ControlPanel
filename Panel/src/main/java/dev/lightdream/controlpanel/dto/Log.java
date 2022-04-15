package dev.lightdream.controlpanel.dto;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Log {

    public int maxSize = 100;
    public List<String> logs = new ArrayList<>();

    public Log(List<String> logs) {
        this.logs = logs;
    }

    @SuppressWarnings("unused")
    public Log(String log) {
        this.logs.add(log);
    }

    public void addLog(Log log) {
        this.logs.addAll(log.logs);
        while (this.logs.size() > maxSize) {
            this.logs.remove(0);
        }
    }
}
