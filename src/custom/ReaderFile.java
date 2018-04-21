package custom;

import java.util.Map;

public  interface ReaderFile {

    void buildAllConfigurations();
    Map<Integer, Float> getRelativeCostsMap();
    float[] getCapacityKnapsackArray();
    float[][] getWeightsMatrix();
    float[] getValuesElementsArray();
    int getNumberOfElements();
    int getNumberOfKnapsack();
}
