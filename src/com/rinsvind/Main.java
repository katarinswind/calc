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
        return str != null && str.matches("[-0-9.]+");
    }

    private static boolean isDigit(char c){
        String str = "";
        str += c;
        return str != null && (str.matches("[0-9.]+") || str.matches("[IiVvXx.]+"));
    }

    private static boolean isSign(char c){{
        if (c == '-' || c == '+'){
            return true;
        }
        return false;
    }}

    private static boolean isRomanianNumber(String str){
        return str != null && str.matches("[-IiVvXx.]+");
    }

    private static int RomanianNumber2int(String str){
        char seq[] = str.toCharArray();
        int index = 0;
        int result = 0;

        if (seq[0] == '-'){
            throw new IllegalArgumentException("Отрицательные римские числа");
        }

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

    private static String Int2RomanianNumber(int arg){
        String result = "";
        if (arg <= 3) {
            switch (arg) {
                case 1: result = "i";    break;
                case 2: result = "ii";   break;
                case 3: result = "iii";  break;
            }
        }else {
            if (arg < 9){
                if (arg < 5){
                    result = "iv";
                } else {
                    result = "v";
                    for (int i = 0; i < arg - 5; i++){
                        result += "i";
                    }
                }
            } else {
                if (arg == 9)
                    result = "ix";
                else {
                    if (arg < 20) {
                        result = "x";
                        result += Int2RomanianNumber(arg - 10);
                    } else{
                        result = "xx";
                    }
                }
            }
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

//    public static void check(String token) {
//        if (!isRomanianNumber(token) && romanianDigit){
//            throw new IllegalArgumentException("используются римские и десятичные цифры в одном выражении");
//        }
//
//        romanianDigit = isRomanianNumber(token);
//    }

    static private boolean romanianDigit = false;
    static private boolean arabicDigit = false;
    static private int deep = 0;
    static private int index = 0;

    private static int __calculate__(int arg, String input) {
        deep ++;
        if (deep > 2){
            throw new IllegalArgumentException("Больше двух операндов");
        }
        int res = 0;
        String token = "";
        for (;index < input.length(); index++) {
            char s = input.charAt(index);
            if (s == ' ')
                continue;
            if (s == '+' || (s == '-' && (isDigit(input.charAt(index + 1))) && index != 0) || s == '*' || s == '/') {
                if ((!isRomanianNumber(token) && romanianDigit) || (arabicDigit && isRomanianNumber(token))){
                    throw new IllegalArgumentException("используются римские и десятичные цифры в одном выражении");
                } else {
                    arabicDigit = !isRomanianNumber(token);;
                    romanianDigit = isRomanianNumber(token);
                }
                int a = token2Int(token);

                if (a > 10){
                    throw new IllegalArgumentException("введено число больше 10");
                }
                index ++;
                switch (s) {
                    case '+':
                        res = a + __calculate__(res, input);
                        break;
                    case '-':
                        res =  a - __calculate__(res, input);
                        break;
                    case '*':
                        res = a * __calculate__(res, input);;
                        break;
                    case '/':
                        res = a / __calculate__(res, input);;
                        break;
                    default:
                        break;
                }
                token = "";
            } else {
                token += s;
            }
        }

        if (!token.isEmpty()){
            res = token2Int(token);
        }
        return res;
    }

    public static String calc(String input) {
        String retValue = "";
        int result = -1;
        arabicDigit = false;
        romanianDigit = false;
        deep = 0;
        index = 0;
        result = __calculate__(0, input);
        if (romanianDigit){
            if (result == 0){
                throw new IllegalArgumentException("Нулевой результат для римских чисел");
            }
            retValue = Int2RomanianNumber(result);
        } else {
            retValue = String.valueOf(result);
        }
        return retValue;
    }
}
