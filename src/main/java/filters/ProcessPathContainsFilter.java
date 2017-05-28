package filters;

import org.jutils.jprocesses.model.ProcessInfo;

/**
 * Created on 27/05/2017.
 *
 * @author Vittorio
 */
public class ProcessPathContainsFilter implements ProcessFilter {
    private final String pattern;

    public ProcessPathContainsFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean check(ProcessInfo process) {
        return ProcessFilter.commandToPath(process.getCommand()).toLowerCase().contains(pattern.toLowerCase());
    }
}
