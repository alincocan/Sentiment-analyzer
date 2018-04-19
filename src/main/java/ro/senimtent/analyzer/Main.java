package ro.senimtent.analyzer;

import java.util.Scanner;

public class Main {

    static ModelTrainingBuilder modelTrainingBuilder = new ModelTraining();

    public static void main(String args[]) {
        try {
            System.out.println("1.Train the model");
            System.out.println("2.Run all tests");
            System.out.println("3.Run a specific text");

            Scanner sc = new Scanner(System.in);
            Integer input = sc.nextInt();
            switch (input) {
                case 1:
                    modelTrainingBuilder.train();
                    break;
                case 2:
                    modelTrainingBuilder.runAllTestFiles();
                case 3:
                    String inputText = sc.next();
                    modelTrainingBuilder.runASpecificText(inputText);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
