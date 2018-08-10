package edepa.model;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase para normalizar los strings antes de realizar
 * una búsqueda. Primero remueve acentos y luego coloca
 * cada letra en minúscula
 */
public class Searcher {

    /**
     * Están en el mismo orden que se deben reemplazar
     * al momento de realizar la normalización
     */
    private static final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
    private static final String REPLACEMENT = "AaEeIiOoUuNnUu";

    /**
     * Patrón que divide un texto en varias palabras
     */
    private final static
    Pattern SPLIT_PATTERN = Pattern.compile("\\s+");

    /**
     * Patrón para tomar el nombre de pila de una persona
     * suponiendo que dicho nombre se encuentra al inicio
     */
    private final static
    Pattern FIRST_NAME_PATTERN = Pattern.compile("([\\w\\.@]+)");

    /**
     * Remueven tildes y cambia todos a minúsculas
     * @param str: String sin normalizar
     * @return String sin tildes y en minúsculas
     */
    public static String normalize(String str){
        return removeAccents(str).toLowerCase();
    }

    /**
     * Se sustituyen todas las letras con tildes
     * con alguna letra del ingles \
     * Usada por {@link #normalize(String)}
     * @param str con tildes u otros caracteres
     * @return String sin tildes y en minúsculas
     */
    private static String removeAccents(String str) {

        if (str == null) return null;
        char[] array = str.toCharArray();

        for (int index = 0; index < array.length; index++) {
            int pos = ORIGINAL.indexOf(array[index]);
            if (pos > -1) array[index] = REPLACEMENT.charAt(pos);
        }

        return new String(array);

    }

    /**
     * Utiliza el patrón {@link #FIRST_NAME_PATTERN} para
     * encontrar el nombre de pila de la persona
     * @param completeName Nombre completo de la persona,
     *                     por ejemplo: Julian Salinas Rojas
     * @return Primer término encontrado en el nombre completo
     */
    public static String findFirstName(String completeName){
        Matcher m = FIRST_NAME_PATTERN.matcher(completeName);
        return m.find() ? m.group(0) : null;
    }

    /**
     * A partir de una consulta se crea una expresión regular
     * para realizar una búsqueda sobre un texto
     * @param query: String de la consulta
     * @return Patrón para realizar la búsqueda
     */
    private static Pattern createPatternFromQuery(String query){
        Matcher m = SPLIT_PATTERN.matcher(query);
        return Pattern.compile(
                m.replaceAll("|"),
                Pattern.CASE_INSENSITIVE);
    }

    /**
     * A partir de una consulta se crear una expresión regular
     * utilizado {@link #createPatternFromQuery(String)}.
     * A partir de esto se realiza una búsqueda sobre el texto
     * a consultar. Retorna una lista de MatchResults de los que
     * se puede extraer los indices del texto donde se encontraron
     * las concidencias
     *
     * @param query: Texto de la consulta
     * @param text: Texto donde realizar la consulta
     * @return Lista de resultados donde se pueden extraer los indices
     * de cada una de las concidencias
     */
    public static ArrayList<MatchResult> findAll(String query, String text){
        ArrayList<MatchResult> results = new ArrayList<>();
        Pattern p = Searcher.createPatternFromQuery(query);
        Matcher m = p.matcher(text);
        while(m.find()) results.add(m.toMatchResult());
        return results;
    }


}
