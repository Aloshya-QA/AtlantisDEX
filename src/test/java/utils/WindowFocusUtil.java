package utils;

public class WindowFocusUtil {

    public static void focusWindowByName(String targetWindowName) {
        try {
            String command = "wmctrl -a " + targetWindowName;
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Successfully focused window containing: " + targetWindowName);
            } else {
                System.err.println("Failed to focus window containing: " + targetWindowName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
