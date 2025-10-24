package projeto.salf.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

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
        return telefone.matches("\\d{10,11}");
    }

    /** 🔹 NOVO: remove acentos e espaços extras */
    public static String limparTexto(String texto) {
        if (texto == null) return "";
        texto = texto.trim();
        String nfdNormalizedString = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return nfdNormalizedString.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    /** 🔹 NOVO: formata texto para maiúsculo padrão */
    public static String padronizarMaiusculo(String texto) {
        return limparTexto(texto).toUpperCase();
    }

    /** 🔹 NOVO: formata CPF */
    public static String limparCPF(String cpf) {
        if (cpf == null) return "";
        return cpf.replaceAll("[^0-9]", "");
    }

    /** 🔹 NOVO: valida CPF */
    public static boolean validarCPF(String cpf) {
        cpf = limparCPF(cpf);
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
            int dig1 = 11 - (soma % 11);
            if (dig1 >= 10) dig1 = 0;
            if (dig1 != (cpf.charAt(9) - '0')) return false;

            soma = 0;
            for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
            int dig2 = 11 - (soma % 11);
            if (dig2 >= 10) dig2 = 0;
            return dig2 == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }

    /** 🔹 NOVO: formata string para SQL segura */
    public static String sqlSafe(String input) {
        if (input == null) return "";
        return input.replace("'", "''");
    }

    /** 🔹 NOVO: máscara para CNPJ */
    public static String formatarCNPJ(String cnpj) {
        cnpj = limparCNPJ(cnpj);
        if (cnpj.length() != 14) return cnpj;
        return String.format("%s.%s.%s/%s-%s",
                cnpj.substring(0, 2),
                cnpj.substring(2, 5),
                cnpj.substring(5, 8),
                cnpj.substring(8, 12),
                cnpj.substring(12));
    }

    /** 🔹 NOVO: máscara para telefone */
    public static String formatarTelefone(String telefone) {
        telefone = limparTelefone(telefone);
        if (telefone.length() == 10) {
            return String.format("(%s) %s-%s", telefone.substring(0, 2), telefone.substring(2, 6), telefone.substring(6));
        } else if (telefone.length() == 11) {
            return String.format("(%s) %s-%s", telefone.substring(0, 2), telefone.substring(2, 7), telefone.substring(7));
        }
        return telefone;
    }

    /** 🔹 NOVO: verifica string nula ou vazia */
    public static boolean vazio(String s) {
        return s == null || s.trim().isEmpty();
    }
}
