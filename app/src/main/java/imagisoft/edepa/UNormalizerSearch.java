package imagisoft.edepa;

public class UNormalizerSearch {

    private static final String ORIGINAL
            = "ÁáÉéÍíÓóÚúÑñÜü";
    private static final String REPLACEMENT
            = "AaEeIiOoUuNnUu";

    public static String removeAccents(String str) {

        if (str == null) {
            return null;
        }

        char[] array = str.toCharArray();
        for (int index = 0; index < array.length; index++) {

            int pos = ORIGINAL.indexOf(array[index]);
            if (pos > -1) {
                array[index] = REPLACEMENT.charAt(pos);
            }

        }

        return new String(array);

    }

}
