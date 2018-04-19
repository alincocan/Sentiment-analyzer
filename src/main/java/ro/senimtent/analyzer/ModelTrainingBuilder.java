package ro.senimtent.analyzer;

public interface ModelTrainingBuilder  {
    void train() throws Exception;
    void runAllTestFiles() throws Exception;
    void runASpecificText(String text) throws Exception;
}
