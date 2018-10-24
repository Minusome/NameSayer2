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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static namesayer.persist.Config.SAVED_ASSESSMENT_SESSIONS;
import static namesayer.persist.Config.SAVED_PRACTISE_SESSIONS;

/**
 * Singleton responsible for all file storage operations (loading and saving) surrounding Sessions
 */

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

    /**
     * On construction, load the Practice and Assessment Sessions
     */
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

    /**
     * Loads the session object using input streams
     * @param type Practice or Assessment
     * @param path File path of the Session
     */
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

    /**
     * Saves Practice Sessions because the object graph is serializable
     * @param session Practice Session
     */
    public void saveSession(PractiseSession session) {
        Path saveFile = SAVED_PRACTISE_SESSIONS.resolve(session.getId() + "ser");
        savedPractiseSessions.put(session, saveFile);
        saveFile(saveFile, session);
    }

    /**
     * Saves Assessment Sessions because the object graph is serializable
     * @param session Assessment Session
     */
    public void saveSession(AssessmentSession session) {
        Path saveFile = SAVED_ASSESSMENT_SESSIONS.resolve(session.getId() + "ser");
        savedAssessmentSessions.put(session, saveFile);
        saveFile(saveFile, session);
    }

    /**
     * On a new thread, saves the session to file using output streams
     *
     * @param location The File to save to
     * @param session Practice or Assessment session to save
     */
    private void saveFile(Path location, Session session) {
        new Thread(() -> {
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
        }).start();
    }

    /**
     * Delete a session no longer needed
     * @param path The file path of the session
     */
    private void deleteFile(Path path) {
        try {
            if (path != null) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes Assessment Session from memory
     * @param session Assessment Session
     */
    public void removeSession(AssessmentSession session) {
        session.removeFiles();
        if (savedAssessmentSessions.containsKey(session)) {
            deleteFile(savedAssessmentSessions.get(session));
            savedAssessmentSessions.remove(session);
        }


    }

    /**
     * Removes Pracice Session from memory
     * @param session Practice Session
     */
    public void removeSession(PractiseSession session) {
        session.removeFiles();
        if (savedPractiseSessions.containsKey(session)) {
            deleteFile(savedPractiseSessions.get(session));
            savedPractiseSessions.remove(session);
        }
    }


    public List<PractiseSession> getSavedPractiseSessions() {
        return new ArrayList<>(savedPractiseSessions.keySet());
    }

    public List<AssessmentSession> getSavedAssessmentSessions() {
        return new ArrayList<>(savedAssessmentSessions.keySet());
    }
}
