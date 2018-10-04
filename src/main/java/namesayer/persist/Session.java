package namesayer.persist;

import namesayer.model.CompleteName;

import java.util.LinkedList;
import java.util.List;

public abstract class Session {

    private List<CompleteName> namesList = new LinkedList<>();
    private int currentIndex = 0;
    protected CompleteName currentName;


    public void add(CompleteName completeName) {
        namesList.add(completeName);
    }

    public CompleteName getCurrentName() {
        currentName = namesList.get(currentIndex);
        return currentName;
    }

    public CompleteName next() {
        currentName = namesList.get(++currentIndex);
        return currentName;
    }

    public CompleteName prev() {
        currentName = namesList.get(--currentIndex);
        return currentName;
    }

    public boolean hasNext() {
        return !(currentIndex == namesList.size() - 1);
    }

    public boolean hasPrev() {
        return !(currentIndex == 0);
    }

    public void playExemplar() {
        currentName.getExemplar().playAudio();
    }


}
