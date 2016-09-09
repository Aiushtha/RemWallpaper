package share;

import android.content.Context;

/**
 * Created by Lin on 16/8/25.
 */

public class DataShare extends AndroidShare {
    public static DataShare instance;

    public static DataShare getInstance(Context context) {
        return instance = (instance == null ? new DataShare(context) : instance);
    }

    private DataShare(Context context) {
        super(context, "save");
    }

}
