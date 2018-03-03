package imagisoft.edepa;

import java.util.ArrayList;

public class Exhibitor {

    private String completeName;
    private String personalTitle;

    public String getCompleteName() {
        return completeName;
    }

    public String getPersonalTitle() {
        return personalTitle;
    }

    public Exhibitor(String completeName, String personalTitle) {
        this.completeName = completeName;
        this.personalTitle = personalTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exhibitor)) return false;
        Exhibitor exhibitor = (Exhibitor) o;
        return completeName != null ?
                completeName.equals(exhibitor.completeName) :
                exhibitor.completeName == null;
    }

}
