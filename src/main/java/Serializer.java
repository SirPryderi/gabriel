import filters.ProcessCommandContainsFilter;
import filters.ProcessFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 27/05/2017.
 *
 * @author Vittorio
 */
public class Serializer {
    private static final File path = new File(System.getProperty("user.home") + "/.gabriel/");
    private static final File whitelistFile = new File(path, "whitelist.bin");

    private Serializer() {
    }

    public static void serialize(Collection<ProcessFilter> filters) {
        if (!whitelistFile.exists())
            try {
                if (path.exists() || path.mkdirs())
                    if (!whitelistFile.createNewFile())
                        throw new IOException("Could not create serialisation file.");
                    else
                        System.out.println("Created file " + whitelistFile.getAbsolutePath());
                else
                    throw new IOException("Could not create serialisation folder.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(whitelistFile))) {
            outputStream.writeObject(new ArrayList<>(filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Collection<ProcessFilter> deserialize() {
        if (whitelistFile.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(whitelistFile))) {
                //noinspection unchecked
                return (ArrayList<ProcessFilter>) inputStream.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

            throw new RuntimeException("If you're seeing this, you have a problem, mate.");
        } else {
            ArrayList<ProcessFilter> processFilters = new ArrayList<>();

            processFilters.add(new ProcessCommandContainsFilter("\\AppData\\Local\\Temp\\wmi4java"));
            //processFilters.add(new ProcessCommandContainsFilter("gabriel"));

            return processFilters;
        }
    }
}
