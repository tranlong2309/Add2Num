package com.add2num.core;

import java.util.Random;

public class Benchmark {
    
    public static void main(String[] args) {
        int length = 10000000;
        StringBuilder sb1 = new StringBuilder(length);
        StringBuilder sb2 = new StringBuilder(length);
        Random rand = new Random(42);
        for (int i = 0; i < length; i++) {
            sb1.append(rand.nextInt(10));
            sb2.append(rand.nextInt(10));
        }
        String s1 = sb1.toString();
        String s2 = sb2.toString();

        System.out.println("Warming up JVM...");
        for (int i = 0; i < 50; i++) {
            sumV1_StringBuilder(s1, s2);
            sumV2_CharArray_Modulo(s1, s2);
            sumV4_LoopSplitting(s1, s2);
            sumV5_Extreme_Unrolling(s1, s2);
        }

        System.out.println("Starting Benchmark (10,000,000 digits)...");

        long t1 = System.nanoTime();
        for (int i = 0; i < 100; i++) sumV1_StringBuilder(s1, s2);
        long t2 = System.nanoTime();
        System.out.println("V1 (StringBuilder + reverse): " + (t2 - t1) / 1000000 + " ms");

        long t3 = System.nanoTime();
        for (int i = 0; i < 100; i++) sumV2_CharArray_Modulo(s1, s2);
        long t4 = System.nanoTime();
        System.out.println("V2 (char[] + modulo %): " + (t4 - t3) / 1000000 + " ms");

        long t7 = System.nanoTime();
        for (int i = 0; i < 100; i++) sumV4_LoopSplitting(s1, s2);
        long t8 = System.nanoTime();
        System.out.println("V4 (Loop Splitting): " + (t8 - t7) / 1000000 + " ms");

        long t9 = System.nanoTime();
        for (int i = 0; i < 100; i++) sumV5_Extreme_Unrolling(s1, s2);
        long t10 = System.nanoTime();
        System.out.println("V5 (Loop Splitting + Loop Unrolling x4): " + (t10 - t9) / 1000000 + " ms");
    }

    public static String sumV1_StringBuilder(String stn1, String stn2) {
        int i = stn1.length() - 1, j = stn2.length() - 1, carry = 0;
        StringBuilder rawDigits = new StringBuilder();
        int digit1, digit2, total, digitWritten, carryOut;
        while (i >= 0 || j >= 0 || carry > 0) {
            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;
            total = digit1 + digit2 + carry;
            rawDigits.append(total % 10);
            carry = total / 10;
            i--; j--;
        }
        return rawDigits.reverse().toString();
    }

    public static String sumV2_CharArray_Modulo(String stn1, String stn2) {
        int len1 = stn1.length(), len2 = stn2.length();
        int maxLen = Math.max(len1, len2);
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen, carry = 0;
        int i, j, digit1, digit2, total;
        for (int k = 0; (k < maxLen) || (carry > 0); k++) {
            i = len1 - 1 - k;
            j = len2 - 1 - k;
            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;
            total = digit1 + digit2 + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }
        return new String(resultChars, writePos + 1, maxLen - writePos);
    }

    public static String sumV4_LoopSplitting(String stn1, String stn2) {
        int len1 = stn1.length(), len2 = stn2.length();
        int maxLen = Math.max(len1, len2);
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen, carry = 0, i = len1 - 1, j = len2 - 1;
        int total;

        while (i >= 0 && j >= 0) {
            total = (stn1.charAt(i--) - '0') + (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }
        while (i >= 0) {
            total = (stn1.charAt(i--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }
        while (j >= 0) {
            total = (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }
        if (carry > 0) resultChars[writePos--] = (char) (carry + '0');
        return new String(resultChars, writePos + 1, maxLen - writePos);
    }

    public static String sumV5_Extreme_Unrolling(String stn1, String stn2) {
        int len1 = stn1.length(), len2 = stn2.length();
        int maxLen = Math.max(len1, len2);
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen, carry = 0, i = len1 - 1, j = len2 - 1;
        int total;

        // Unroll phase 1 by 4 steps
        while (i >= 3 && j >= 3) {
            total = (stn1.charAt(i--) - '0') + (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn1.charAt(i--) - '0') + (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn1.charAt(i--) - '0') + (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn1.charAt(i--) - '0') + (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }
        // Leftovers from phase 1
        while (i >= 0 && j >= 0) {
            total = (stn1.charAt(i--) - '0') + (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }

        // Unroll phase 2 by 4 steps
        while (i >= 3) {
            total = (stn1.charAt(i--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn1.charAt(i--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn1.charAt(i--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn1.charAt(i--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }
        while (i >= 0) {
            total = (stn1.charAt(i--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }

        // Unroll phase 3 by 4 steps
        while (j >= 3) {
            total = (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;

            total = (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }
        while (j >= 0) {
            total = (stn2.charAt(j--) - '0') + carry;
            resultChars[writePos--] = (char) ((total % 10) + '0');
            carry = total / 10;
        }

        if (carry > 0) resultChars[writePos--] = (char) (carry + '0');
        return new String(resultChars, writePos + 1, maxLen - writePos);
    }
}
