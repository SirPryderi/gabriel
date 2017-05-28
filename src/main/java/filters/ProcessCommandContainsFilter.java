package filters;

import org.jutils.jprocesses.model.ProcessInfo;

/**
 * Created on 27/05/2017.
 *
 * @author Vittorio
 */
public class ProcessCommandContainsFilter implements ProcessFilter {
    private final String pattern;

    public ProcessCommandContainsFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean check(ProcessInfo process) {
        return process.getCommand().toLowerCase().contains(pattern.toLowerCase());
    }
}
