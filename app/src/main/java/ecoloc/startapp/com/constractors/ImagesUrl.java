package ecoloc.startapp.com.constractors;

import java.io.Serializable;

/**
 * Created by ghazi on 28/05/18.
 */

public class ImagesUrl extends Ads implements Serializable {
    public String downloadUrl;

    public ImagesUrl() {
    }

    public ImagesUrl(String dUrl) {
        this.downloadUrl = dUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
