package imagisoft.miscellaneous;

/**
 * Clase para normalizar los strings antes de realizar una búsqueda
 */
public class SearchNormalizer {

    /**
     * Están en el mismo orden que se deb reemplazar
     */
    private static final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
    private static final String REPLACEMENT = "AaEeIiOoUuNnUu";

    /**
     * Remueven tildes y cambia todos a minúsculas
     */
    public static String normalize(String str){
        return removeAccents(str).toLowerCase();
    }

    /**
     * Se sustituyen todas las letras con tildes por ejemplo
     */
    private static String removeAccents(String str) {

        if (str == null)
            return null;

        char[] array = str.toCharArray();
        for (int index = 0; index < array.length; index++) {

            int pos = ORIGINAL.indexOf(array[index]);
            if (pos > -1)
                array[index] = REPLACEMENT.charAt(pos);

        }

        return new String(array);

    }

}
