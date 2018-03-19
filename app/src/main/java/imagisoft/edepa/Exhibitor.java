package imagisoft.edepa;

public class Exhibitor {

    private String completeName;

    /**
     * El título podría ser solo el país y tal vez la institución
     * Ej: Julian Salinas
     *     Instituto Tecnológico de Costa Rica
     */
    private String personalTitle;

    public String getCompleteName() {
        return completeName;
    }

    public String getPersonalTitle() {
        return personalTitle;
    }

    public Exhibitor() {
        // Requerido por firebase
    }

    public Exhibitor(String completeName, String personalTitle) {
        this.completeName = completeName;
        this.personalTitle = personalTitle;
    }

    /*
    * Es la misma persona con solo poseer el mismo nombre
    */
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
