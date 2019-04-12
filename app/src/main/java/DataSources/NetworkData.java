package DataSources;

import android.content.Context;


public class NetworkData {

    private static NetworkData instance;

    public static synchronized NetworkData getInstance(){
        if(instance == null){
            instance = new NetworkData();
        }
        return instance;
    }

    private NetworkData(){ }

}
