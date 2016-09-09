package share;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class AndroidShare {

    public interface IExcute {
        void put(SharedPreferences.Editor localEditor);

        void get(SharedPreferences settings);
    }

    protected String filename;
    protected Context context;
    protected SharedPreferences settings;


    public AndroidShare(Context context,String filename) {
        super();
        this.filename = filename;
        this.context=context;
        this.settings = context.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
    }

    public  void put(String key,String value) {

        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putString(key, value);

        localEditor.commit();
    }

    public  String get(String key) {
        SharedPreferences.Editor localEditor = settings.edit();
        String result =  settings.getString(key,null);
        return result;
    }
    public  String get(String key,String defaultvalue) {
        SharedPreferences.Editor localEditor = settings.edit();
        String result =  settings.getString(key,null);

        if(result==null)
        {
            return defaultvalue;
        }
        return result;
    }

    public boolean save(Object object) {
        try {

            String key=object.getClass().getName();
            String value=new Gson().toJson(object);
            put(key,value);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public <T> T getData(Class<T> cls) {

        try {
            String content = get(cls.getName());
            return new Gson().fromJson(content,cls);
        } catch (Exception e) {

        }
        return null;

    }

    public SharedPreferences operation(Context c, String filename, IExcute excute) {
        SharedPreferences settings = c.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor localEditor = settings.edit();

        if (excute != null) {
            excute.put(localEditor);
            localEditor.commit();
            excute.get(settings);
        }
        localEditor.commit();
        return settings;
    }

    public void clear(Context c, String filename) {
        SharedPreferences settings = c.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.clear()
                .commit();
    }



}
