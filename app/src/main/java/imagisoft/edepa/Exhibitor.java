package imagisoft.edepa;

public class Exhibitor {

    /**
     * Nombre donde se incluyen también apellidos de la persona
     */
    private String completeName;

    /**
     * El título podría ser solo el país y tal vez la institución
     * Ej: Julian Salinas
     *     Instituto Tecnológico de Costa Rica
     */
    private String personalTitle;

    /**
     * Getters y Setters de los atributos del expositor
     */
    public String getCompleteName() {
        return completeName;
    }

    public String getPersonalTitle() {
        return personalTitle;
    }

    /**
     * Contructor vacío requerido por firebase
     */
    public Exhibitor() {

    }

    /**
     * Constructor usado únicamente para generar pruebas
     */
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
        return completeName.equals(exhibitor.completeName);
    }

    /**
     * Función para realizar búsquedas indexadas
     */
    @Override
    public int hashCode() {
        return completeName.hashCode();
    }

}
