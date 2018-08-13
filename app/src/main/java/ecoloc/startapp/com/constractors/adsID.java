package ecoloc.startapp.com.constractors;

import android.support.annotation.NonNull;

/**
 * Created by ghazi on 22/05/18.
 */

public class adsID {
    public String adsID;
    public <T extends adsID> T withId(@NonNull final String id){
        this.adsID = id;
        return (T)this;
    }
}
