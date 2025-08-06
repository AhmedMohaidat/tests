package report;

import java.util.HashMap;

public class StateHelper {

    private static HashMap<String, Object> CrossStepState = new HashMap<String, Object>();
    public static void setStepState(String key, String value)
    {
        CrossStepState.put(key, value);
    }
}
