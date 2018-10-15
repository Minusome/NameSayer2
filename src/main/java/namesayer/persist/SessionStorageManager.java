package namesayer.persist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import namesayer.session.Session;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SessionStorageManager {

    private static SessionStorageManager instance;

    public static SessionStorageManager getInstance() {
        if (instance == null) {
            instance = new SessionStorageManager();
        }
        return instance;
    }

    public void save(Session session) {
        try (Writer writer = new FileWriter("Output.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(session, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
