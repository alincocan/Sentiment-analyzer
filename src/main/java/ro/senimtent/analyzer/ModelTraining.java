package ro.senimtent.analyzer;

import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ModelTraining implements ModelTrainingBuilder {

    private File[] positiveFiles = null;
    private File[] negativeFiles = null;
    private Model model = new Model();
    
    public void prepareData() throws Exception {
        File positiveDir = new File(Paths.TRAINING_POSITIVE_FILES);
        File negativeDir = new File(Paths.TRAINING_NEGATIVE_FILES);

        positiveFiles = positiveDir.listFiles();
        negativeFiles = negativeDir.listFiles();

    }

    public void trainModel() throws Exception {

        System.out.println("Se citesc fisierele ..... ");

        for(File f: positiveFiles) {
            String fileText = new Scanner(f).useDelimiter("\\A").next();
            double[] value = new double[model.getData().numAttributes()];
            value[0] = model.getData().attribute(0).addStringValue(fileText);
            value[1] = 1;
            model.getData().add(new DenseInstance(1.0, value));
            model.getData().setClassIndex(1);
        }

        for(File f: negativeFiles) {
            String fileText = new Scanner(f).useDelimiter("\\A").next();
            double[] value = new double[model.getData().numAttributes()];
            value[0] = model.getData().attribute(0).addStringValue(fileText);
            value[1] = 0;
            model.getData().add(new DenseInstance(1.0, value));
            model.getData().setClassIndex(1);
        }

        model.getData().setClassIndex(1);
        StringToWordVector filter = new StringToWordVector();
        filter.setAttributeIndices("first");
        filter.setInputFormat(model.getData());
        filter.setStemmer(new LovinsStemmer());
        filter.setStopwordsHandler(new Rainbow());
        filter.setTokenizer(new WordTokenizer());
        filter.setLowerCaseTokens(true);

        model.getFilteredClassifier().setFilter(filter);
        model.getFilteredClassifier().setClassifier(new NaiveBayesMultinomial());
        model.build();
        model.write();
    }


    public void train() throws Exception {
        this.prepareData();
        this.trainModel();
    }

    public void runAllTestFiles() throws Exception {
        model.loadModel();
        File testPosDir = new File(Paths.TEST_POSITIVE_FILES);
        File testNegDir = new File(Paths.TEST_NEGATIVE_FILES);
        File[] testPosFiles = testPosDir.listFiles();
        File[] testNegFiles = testNegDir.listFiles();
        int positive = 0;
        int negative = 0;


        for(File file: testPosFiles) {
            String text = new Scanner(file).useDelimiter("\\A").next();
            double result = model.classifyText(text);
            if(result == 1) positive++;
            else if(result == 0) negative ++;
        }



        System.out.println("Positive files -> " + positive);
        System.out.println("Negative files -> " + negative);

        for(File file: testNegFiles) {
            String text = new Scanner(file).useDelimiter("\\A").next();
            double result = model.classifyText(text);
            if(result == 1) positive++;
            else if(result == 0) negative ++;
        }

        System.out.println("Positive files -> " + positive);
        System.out.println("Negative files -> " + negative);

    }

    public void runASpecificText(String text) throws Exception {
        model.loadModel();
         double result = model.classifyText(text);
         if(result == 1) {
             System.out.println("This text is classified as positive");
         } else {
             System.out.println("This text is classified as negative");
        }
    }
}
