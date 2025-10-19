package projetoSalf.mvc.util;

public class FormatUtils {

    public static String limparCNPJ(String cnpj) {
        if (cnpj == null) return "";
        return cnpj.replaceAll("[^0-9]", "");
    }

    public static boolean validarCNPJ(String cnpj) {
        cnpj = limparCNPJ(cnpj);

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        try {
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * pesos1[i];
            }

            int dig1 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * pesos2[i];
            }
            soma += dig1 * pesos2[12];

            int dig2 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            return dig1 == Character.getNumericValue(cnpj.charAt(12)) &&
                    dig2 == Character.getNumericValue(cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }

    public static String limparTelefone(String telefone) {
        if (telefone == null) return "";
        return telefone.replaceAll("[^0-9]", "");
    }

    public static boolean validarTelefone(String telefone) {
        telefone = limparTelefone(telefone);
        // Aceita DDD + número com 8 ou 9 dígitos
        return telefone.matches("\\d{10,11}");
    }
}
