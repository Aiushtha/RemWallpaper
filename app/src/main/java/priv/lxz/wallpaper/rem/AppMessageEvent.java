package priv.lxz.wallpaper.rem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Lin on 16/8/25.
 */

public class AppMessageEvent implements Serializable {

    private static final String TAG = "PostEvent";
    private int taskID;
    private String content;
    private Object obj;
    private Objects[] objects;
    private HashMap<String,Objects> map=new HashMap();

    public AppMessageEvent(int taskID) {
        this.taskID=taskID;
    }

    public AppMessageEvent(int taskID,Object obj) {
        this.taskID=taskID;
        this.obj=obj;
    }

    public AppMessageEvent(int taskID,Objects[] objs) {
        this.taskID=taskID;
        this.objects=objs;
    }

    public AppMessageEvent(int taskID, HashMap<String,Objects> map) {
        this.taskID=taskID;
        this.map=map;
    }


    public String getContent() {
        return content;
    }

    public static AppMessageEvent getInstance(int taskID) {
        return new AppMessageEvent(taskID);
    }

    public AppMessageEvent setContent(String content) {
        this.content = content;
        return this;
    }

    public HashMap<String,Objects> getMap() {
        return map;
    }

    public AppMessageEvent setMap(HashMap<String,Objects> map) {
        this.map = map;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public AppMessageEvent setObj(Serializable obj) {
        this.obj = obj;
        return this;
    }

    public int getTaskID() {
        return taskID;
    }

    public AppMessageEvent setTaskID(int taskID) {
        this.taskID = taskID;
        return this;
    }

    public static String getTAG() {
        return TAG;
    }

    public AppMessageEvent setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    public Objects[] getObjects() {
        return objects;
    }

    public AppMessageEvent setObjects(Objects[] objects) {
        this.objects = objects;
        return this;
    }
}
