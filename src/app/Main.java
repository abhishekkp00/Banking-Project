package app;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome to the Application");
        System.out.println("-------------------------------------------------");
        boolean running = true;

        while(running) {
            System.out.println("""
                    1) Open Account
                    2) Deposit
                    3) Withdraw
                    4) Transfer
                    5) Account Statement
                    6) List Account
                    7) Search Accounts by Customer Name
                    0) Exit
                    """);
            System.out.println("----------------------------------------");
            System.out.println("Enter your choice: ");
            String choice = sc.nextLine().trim();
            System.out.println("You have selected: " + choice);

            switch(choice) {
                case "0" : running = false;
                break;
            }
        }
    }
}
