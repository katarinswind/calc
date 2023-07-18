package com.rinsvind;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args)  throws IOException {
        System.out.print("Calculator:\nвведите Ваше выражение и нажмите \"Enter\" или нажмите \"q\" to exit\n");
        while (true) {
            System.out.print("> ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String readLine = reader.readLine();

            if (readLine.equals("q") || readLine.equals("Q")){
                break;
            }
            try {
                String result = calc(readLine);
                System.out.println(result);
            }catch (RuntimeException e){
                System.out.println(e.getMessage());
                System.exit(1);
            }

            // echo
//            System.out.println(readLine);
        }
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }

    private static boolean isRomanianNumber(String str){
        return str != null && str.matches("[IiVvXx.]+");
    }

    private static int RomanianNumber2int(String str){
        char seq[] = str.toCharArray();
        int index = 0;
        int result = 0;
        while (index < seq.length) {
            char s = seq[index];
            if (s == 'i' || s == 'I') {
                if ((index + 1) < seq.length){
                    if (seq[index + 1] == 'v' || seq[index + 1] == 'V') {
                        result += 4;
                        index += 2;
                        continue;
                    } else{
                        if (seq[index + 1] == 'x' || seq[index + 1] == 'X') {
                            result += 9;
                            index += 2;
                            continue;
                        }
                    }
                }

                result += 1;
            } else {
                if (s == 'v' || s == 'V'){
                    result += 5;
                } else {
                    if (s == 'x' || s == 'X') {
                        result += 10;
                    }
                }
            }
            index ++;
        }
        return result;
    }

    public static int token2Int (String token){
        int arg = 0;
        if (token != null && token.length() != 0) {
            if (isNumeric(token)){
                // содержит десятичные цифлы
                arg = Integer.parseInt(token);
            } else {
                if (isRomanianNumber(token)) {

                    arg = RomanianNumber2int(token);
                }
            }
        }
        return arg;
    }

    public static void check(String token) {
        if (!isRomanianNumber(token) && romanianDigit){
            throw new IllegalArgumentException("используются римские и десятичные цифры в одном выражении");
        }

        romanianDigit = isRomanianNumber(token);
    }

    static private boolean romanianDigit = false;

    public static String calc(String input) {
        String retValue = "";
        int result = -1;
        String token = "";
        input += "\t";
        char op = ' ';

        for (int ii = 0; ii < input.length(); ii++) {
            char s = input.charAt(ii);
            if (s == ' ')
                continue;
            if (s == '+' || s == '-' || s == '*' || s == '/' || s == '\t'){
                // new token

                if (token.length() != 0) {
                    try {
                        check(token);
                    }catch (RuntimeException e){
                        throw e;
                    }
                    int arg = token2Int(token);
                    if (arg > 10){
                        throw new IllegalArgumentException("введено число больше 10");
                    }

                    if (result == -1) {
                        result = arg;
                    } else {
                        switch (op) {
                            case '+': result += arg; break;
                            case '-': result -= arg; break;
                            case '*': result *= arg; break;
                            case '/': result /= arg; break;
                            default:break;
                        }
                    }
                    op = s;
                    token = "";
                } else {
                    // два или болле знака операции подряд
                    throw new IllegalArgumentException("два или болле знака операции подряд");
                }
            } else {
                token += s;
            }
        }

        retValue = String.valueOf(result);

        return retValue;
    }
}
