package namesayer.persist;

import namesayer.session.AssessmentSession;
import namesayer.session.PractiseSession;
import namesayer.session.Session;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static namesayer.persist.Config.SAVED_ASSESSMENT_SESSIONS;
import static namesayer.persist.Config.SAVED_PRACTISE_SESSIONS;


public class SessionStorageManager {

    private static SessionStorageManager instance;

    private Map<PractiseSession, Path> savedPractiseSessions = new HashMap<>();
    private Map<AssessmentSession, Path> savedAssessmentSessions = new HashMap<>();

    public static SessionStorageManager getInstance() {
        if (instance == null) {
            instance = new SessionStorageManager();
        }
        return instance;
    }

    private SessionStorageManager() {
        try {
            Files.walk(SAVED_PRACTISE_SESSIONS)
                 .filter(Files::isRegularFile)
                 .forEach(path -> load(Session.SessionType.PRACTISE, path));
            Files.walk(SAVED_ASSESSMENT_SESSIONS)
                 .filter(Files::isRegularFile)
                 .forEach(path -> load(Session.SessionType.ASSESSMENT, path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void load(Session.SessionType type, Path path) {
        try {
            FileInputStream file = new FileInputStream(path.toFile());
            ObjectInputStream in = new ObjectInputStream(file);
            if (type.equals(Session.SessionType.PRACTISE)) {
                PractiseSession session = (PractiseSession) in.readObject();
                savedPractiseSessions.put(session, path);
                System.out.println("Loaded: " + session.getSessionName());
            } else {
                AssessmentSession session = (AssessmentSession) in.readObject();
                savedAssessmentSessions.put(session, path);
                System.out.println("Loaded: " + session.getSessionName());
            }
            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void savePractiseSession(PractiseSession session) {
        Path saveFile = SAVED_PRACTISE_SESSIONS.resolve(session.getId() + "ser");
        savedPractiseSessions.put(session, saveFile);
        save(saveFile, session);
    }


    public void saveAssessmentSession(AssessmentSession session) {
        Path saveFile = SAVED_ASSESSMENT_SESSIONS.resolve(session.getId() + "ser");
        savedAssessmentSessions.put(session, saveFile);
        save(saveFile, session);
    }


    private void save(Path location, Session session) {
        try {
            FileOutputStream file = new FileOutputStream(location.toFile());
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(session);
            out.flush();
            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(AssessmentSession session) {
        savedAssessmentSessions.remove(session);
        try {
            Files.deleteIfExists(savedAssessmentSessions.get(session));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(PractiseSession session) {
        savedPractiseSessions.remove(session);
        try {
            Files.deleteIfExists(savedPractiseSessions.get(session));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
