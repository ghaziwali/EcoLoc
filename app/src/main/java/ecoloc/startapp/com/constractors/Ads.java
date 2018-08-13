package ecoloc.startapp.com.constractors;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;


/**
 * Created by ghazi on 22/05/18.
 */

public class Ads extends adsID {
@Exclude private String id;
    private   String title, price,ville,description,trans,model,type,folder,link;
    private ArrayList<String> links;

    private String uid;



    public Ads(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public Ads(ArrayList<String> links) {
        this.links = links;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }



    public Ads() {

    }

    public Ads(String id, String title, String price, String ville, String description, String trans, String model, String type) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.ville = ville;
        this.description = description;
        this.trans = trans;
        this.model = model;
        this.type = type;
    }

    public Ads(String folder,String link) {
        this.folder = folder;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
