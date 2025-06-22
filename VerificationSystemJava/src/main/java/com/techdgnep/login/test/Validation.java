package com.techdgnep.login.test;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import com.techdgnep.login.test.RandomCodeGenerator;

public class Validation {
    private static RandomCodeGenerator generator;

    public static void main(String[] args) {
        Scanner Input = new Scanner(System.in);
        System.out.println("Enter the email : ");
        String email = Input.nextLine();
        System.out.println("Enter the password : ");
        try {
            String password = hash(Input.nextLine());
            System.out.println("Confirm Password : ");
            String password2 = hash(Input.nextLine());
            if(password.equals(password2)){
                 generator = new RandomCodeGenerator();
                 int code = generator.GenerateCode();
                System.out.println("The code "+code+" has been sent to your Email");
                System.out.println("Please Enter the code for verification");
                int x = Input.nextInt();
                if(x==code){
                    System.out.println("JWT token getting generated");
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String hash(String input) throws RuntimeException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexPass = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexPass.append('0');
            hexPass.append(hex);
        }
        return hexPass.toString();
    }
}
