import org.jutils.jprocesses.model.ProcessInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 27/05/2017.
 *
 * @author Vittorio
 */
public class QuickWindowsScan {
    public static Collection<ProcessInfo> getProcesses() throws IOException {
        Runtime runtime = Runtime.getRuntime();

        Process exec = runtime.exec("tasklist /svc /nh /fo csv");

        StringBuilder st = new StringBuilder();

        st.append(exec.getOutputStream());

        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

        String line;

        Collection<ProcessInfo> processes = new ArrayList<>();

        while ((line = inputStreamReader.readLine()) != null) {
            System.out.println(line);

            String[] split = line.split(",");

            String name = split[0].substring(1, split[0].length() - 2);

            String pid = split[1].substring(1, split[1].length() - 2);


            processes.add(new ProcessInfo(pid, null, name, null,null, null, null, null, null, null ));
        }

        return processes;
    }

    public static void main(String args[]) throws IOException {
        getProcesses().forEach(System.out::println);
    }
}
