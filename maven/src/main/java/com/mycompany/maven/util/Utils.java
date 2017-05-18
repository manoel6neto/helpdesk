package com.mycompany.maven.util;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Manoel
 */
public class Utils {

    public static final String SYSTEM_URL = "http://localhost:8080/maven/";

    public static final String SYSTEM_NAME = "CAMPO MAGRO - CADASTRO IMOBILIÁRIO";

    public static String onlyDigits(String value, boolean dropLeftZeros) {
        String replaceAll = value.replaceAll("[^\\d]", "");

        if (dropLeftZeros) {
            while (replaceAll.length() > 0 && replaceAll.charAt(0) == '0') {
                replaceAll = replaceAll.substring(1);
            }
        }

        return replaceAll;
    }

    public static boolean validateCPF(String strCpf, Boolean allowEmpty) { // XXX.XXX.XXX-XX
        if (strCpf == null) {
            strCpf = "";
        }
        if (strCpf.length() == 0 && allowEmpty) {
            return true;
        }
        if (strCpf.length() > 0) {
            try {
                int d1, d2;
                int digit1, digito2, rest;
                int digitCPF;
                String nDigResult;
                strCpf = onlyDigits(strCpf, false);

                if (strCpf.equals("00000000000")
                        || strCpf.equals("11111111111")
                        || strCpf.equals("22222222222")
                        || strCpf.equals("33333333333")
                        || strCpf.equals("44444444444")
                        || strCpf.equals("55555555555")
                        || strCpf.equals("66666666666")
                        || strCpf.equals("77777777777")
                        || strCpf.equals("88888888888")
                        || strCpf.equals("99999999999")) {
                    return false;
                }

                d1 = d2 = 0;
                digit1 = digito2 = rest = 0;

                for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {
                    digitCPF = Integer.parseInt(strCpf.substring(nCount - 1, nCount));
                    d1 = d1 + (11 - nCount) * digitCPF;
                    d2 = d2 + (12 - nCount) * digitCPF;
                }
                rest = (d1 % 11);
                if (rest < 2) {
                    digit1 = 0;
                } else {
                    digit1 = 11 - rest;
                }
                d2 += 2 * digit1;
                rest = (d2 % 11);
                if (rest < 2) {
                    digito2 = 0;
                } else {
                    digito2 = 11 - rest;
                }
                String nDigVerific = strCpf.substring(strCpf.length() - 2,
                        strCpf.length());
                nDigResult = String.valueOf(digit1) + String.valueOf(digito2);
                return nDigVerific.equals(nDigResult);
            } catch (NumberFormatException e) {
                System.err.println("Erro !" + e);
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validateCNPJ(String cnpj) {
        int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        cnpj = onlyDigits(cnpj, false);

        if ((cnpj == null) || (cnpj.length() != 14)) {
            return false;
        }

        Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public static String getGreeting() {
        int hour = Calendar.HOUR_OF_DAY;
        if (hour >= 6 && hour < 12) {
            return "Bom dia";
        }
        if (hour >= 12 && hour < 18) {
            return "Boa tarde";
        }
        return "Boa noite";
    }

    public static String removeAcentosEEspacos(String str) {
        if (str == null) {
            return null;
        }
        if (str.equals("")) {
            return "";
        }
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replace(" ", "");
        str = str.replaceAll("[^\\p {ASCII}]", "");
        str = str.toLowerCase();
        return str;
    }

    public static String randomPassword() {

        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890/[]".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();

    }

    public static String[] decodeAuth(String auth) {
        //Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
        auth = auth.replaceFirst("[B|b]asic ", "");

        //Decode the Base64 into byte[]
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

        //If the decode fails in any case
        if (decodedBytes == null || decodedBytes.length == 0) {
            return null;
        }

        //Now we can convert the byte[] into a splitted array :
        //  - the first one is login,
        //  - the second one password
        return new String(decodedBytes).split(":", 2);
    }

}
