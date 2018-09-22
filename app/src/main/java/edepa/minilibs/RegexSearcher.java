package edepa.minilibs;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase para normalizar los strings antes de realizar
 * una búsqueda. Primero remueve acentos y luego coloca
 * cada letra en minúscula
 */
public class RegexSearcher {

    /**
     * Están en el mismo orden que se deben reemplazar
     * al momento de realizar la normalización
     */
    private static final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
    private static final String REPLACEMENT = "AaEeIiOoUuNnUu";

    /**
     * Patrónes para poder marcar en negrita o cursiva cualquier
     * texto que este encerrado entre '*' o '_' ...
     */
    public final static
    Pattern BOLD_TEXT = Pattern.compile("\\*.*\\*");

    public final static
    Pattern ITALIC_TEXT = Pattern.compile("_.*_");

    public final static
    Pattern DECORATED_TEXT = Pattern.compile(BOLD_TEXT + "|" + ITALIC_TEXT);

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
     * Patrón para obtener el nombre del archivo
     * proveniente de una Url
     */
    private final static
    Pattern FILE_FROM_URL_PATTERN = Pattern.compile("([^\\/]+)\\.([\\w\\d]*)?$");

    /**
     * Patrón para obtener el Dominio de una Url
     */
    private final static
    Pattern DOMAIN_FROM_URL_PATTERN = Pattern
            .compile("^(?:https?:\\/\\/)?(?:[^@\\n]+@)?(?:www\\.)?([^:\\/\\n?]+)");

    /**
     * Patrón para saber si una url hace referecia a
     * una imagen o a un gif
     */
    private final static
    Pattern IS_IMAGE_PATTERN = Pattern.compile(".*\\.(?:jpg|gif|png)");

    /**
     * Patrón para saber si una url hace referencia a
     * un archivo pdf
     */
    private final static
    Pattern IS_PDF_PATTERN = Pattern.compile("(.*.pdf)");

    private final static
    Pattern LATITUDE_PATTERN = Pattern.compile("\\@(-?[\\d\\.]*)");

    private final static
    Pattern LONGITUDE_PATTERN = Pattern.compile("\\@[-?\\d\\.]*\\,([-?\\d\\.]*)");

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
        if (completeName == null || completeName.isEmpty())
            return null;
        Matcher m = FIRST_NAME_PATTERN.matcher(completeName);
        return m.find() ? m.group(0) : null;
    }

    /**
     * Utiliza el patrón {@link #FILE_FROM_URL_PATTERN} para
     * encontrar el nombre de archivo desde una url
     * @param fileUrl: Url del archivo a descargar
     * @return Nombre del archivo
     */
    public static String findFilenameFromUrl(String fileUrl){
        Matcher m = FILE_FROM_URL_PATTERN.matcher(fileUrl);
        return m.find() ? m.group(1) : null;
    }

    /**
     * Utiliza el patrón {@link #DOMAIN_FROM_URL_PATTERN} para
     * encontrar el dominio de una página
     * @param url: Url de la página
     * @return Nombre del dominio de la página
     */
    public static String findDomainFromUrl(String url){
        Matcher m = DOMAIN_FROM_URL_PATTERN.matcher(url);
        return m.find() ? m.group(1) : null;
    }

    public static double findLatitude(String placeUrl){
        Matcher m = LATITUDE_PATTERN.matcher(placeUrl);
        return m.find() ? Double.valueOf(m.group(1)) : 0.0;
    }

    public static double findLongitude(String placeUrl){
        Matcher m = LONGITUDE_PATTERN.matcher(placeUrl);
        return m.find() ? Double.valueOf(m.group(1)) : 0.0;
    }

    public static boolean isDocumentFile(String fileUrl){
        Matcher m = IS_PDF_PATTERN.matcher(fileUrl);
        return m.find();
    }

    public static boolean isImageFile(String fileUrl){
        Matcher m = IS_IMAGE_PATTERN.matcher(fileUrl);
        return m.find();
    }

    public static boolean isBoldText(String text){
        Matcher m = BOLD_TEXT.matcher(text);
        return m.find();
    }

    public static String truncateText(String text, int amount){
        return  text.length() <= amount ? text :
                text.subSequence(0, amount - 1).toString() + "...";
    }

    /**
     * A partir de una consulta se crea una expresión regular
     * para realizar una búsqueda sobre un texto
     * @param query: String de la consulta
     * @return Patrón para realizar la búsqueda
     */
    private static Pattern createPatternFromQuery(String query){
        StringBuilder newQuery = new StringBuilder();
        String [] substrings = query.split(SPLIT_PATTERN.toString());
        for (String str: substrings)
            newQuery.append("(").append(str).append(")").append("|");
        newQuery.deleteCharAt(newQuery.length() - 1);
        return Pattern.compile(newQuery.toString(), Pattern.CASE_INSENSITIVE);
    }

    /**
     * A partir de una consulta se crea una expresión regular
     * utilizado {@link #createPatternFromQuery(String)}.
     * A partir de esto se realiza una búsqueda sobre el texto
     * a consultar. Retorna una lista de MatchResults de los que
     * se puede extraer los indices del texto donde se encontraron
     * las concidencias
     *
     * @param query: Texto de la consulta
     * @param text: Texto donde realizar la consulta
     * @return Lista de resultados donde se pueden extraer los indices
     * de cada una de las coincidencias
     */
    public static ArrayList<MatchResult> autoSearch(String query, String text){
        query = RegexSearcher.normalize(query);
        text = RegexSearcher.normalize(text);
        ArrayList<MatchResult> results = new ArrayList<>();
        Pattern p = RegexSearcher.createPatternFromQuery(query);
        Matcher m = p.matcher(text);
        while(m.find()) results.add(m.toMatchResult());
        return results;
    }

    /**
     * A partir de una regex se realiza una búsqueda sobre el texto
     * a consultar. Retorna una lista de MatchResults de los que
     * se puede extraer los indices del texto donde se encontraron
     * las coincidencias
     *
     * @param regex: Expresión regular
     * @param text: Texto donde realizar la consulta
     * @return Lista de resultados donde se pueden extraer los indices
     * de cada una de las concidencias
     */
    public static ArrayList<MatchResult> customSearch(String regex, String text){
        text = RegexSearcher.normalize(text);
        ArrayList<MatchResult> results = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while(m.find()) results.add(m.toMatchResult());
        return results;
    }

    public static boolean match(String query, String text){
        ArrayList<MatchResult> match = RegexSearcher.autoSearch(query, text);
        return match.size() >= 1;
    }

}
