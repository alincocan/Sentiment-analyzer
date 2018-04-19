package ro.senimtent.analyzer;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;

import java.util.ArrayList;

public class Model {

    public ArrayList<String> sentiment = new ArrayList<String>();
    public ArrayList<Attribute> attributes = new ArrayList<Attribute>(2);
    private Instances data;
    private FilteredClassifier filteredClassifier;

    public Model() {

        sentiment.add("NEG");
        sentiment.add("POS");

        attributes.add(new Attribute("content", (ArrayList<String>)null));
        attributes.add(new Attribute("sentiment", sentiment));

        data = new Instances("Instances", attributes,0);
        filteredClassifier = new FilteredClassifier();
    }

    public void loadModel() throws Exception {
        FilteredClassifier classifier = (FilteredClassifier) weka.core.SerializationHelper.read(Paths.TRAINED_MODEL);
        this.filteredClassifier = classifier;

        Instances data = (Instances) weka.core.SerializationHelper.read(Paths.DATA);
        this.data = data;
    }

    public Instances getData() {
        return data;
    }

    public void setData(Instances data) {
        this.data = data;
    }

    public FilteredClassifier getFilteredClassifier() {
        return filteredClassifier;
    }

    public void setFilteredClassifier(FilteredClassifier filteredClassifier) {
        this.filteredClassifier = filteredClassifier;
    }

    public void build() throws Exception {
        filteredClassifier.buildClassifier(data);
    }

    public void write() throws Exception {
        SerializationHelper.write(Paths.TRAINED_MODEL, filteredClassifier);
        SerializationHelper.write(Paths.DATA, data);
    }


    public double classifyText(String text) throws Exception {
        double[] value = new double[2];

        value[0] = data.attribute(0).addStringValue(text);

        Instance for_classification = new DenseInstance(1.0, value);
        data.setClassIndex(1);
        for_classification.setDataset(data);

        return  filteredClassifier.classifyInstance(for_classification);

    }
}
