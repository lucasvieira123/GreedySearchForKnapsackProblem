package custom;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReaderFileWithOneDimension implements ReaderFile {

    private int numberOfElements;
    private float[] capacityKnapsackArray = new float[1];
    private float[] valuesElementsArray;
    private float[][] weightsMatrix;
    private Map<Integer, Float> relativeCostsMap = new HashMap<>();
    private boolean weightDividedValueIsSelected = false;
    private File selectedFile;

    public ReaderFileWithOneDimension(File selectedFile, boolean weightDividedValueIsSelected ) {
        this.selectedFile = selectedFile;
        this.weightDividedValueIsSelected = weightDividedValueIsSelected;
    }

    @Override
    public void buildAllConfigurations() {
        BufferedReader br = ReaderHelp.tryGetBufferedReader(selectedFile);

        final int COUNT_LINES_WITH_COUNT_OF_VALUES_AND_FULL_WEIGHT = 2;
        int countLines = 0;

        if(br == null) return;

        String line = ReaderHelp.tryReadLine(br);

        while (line!= null && !line.equals("")){
            countLines ++;

            if(countLines <= COUNT_LINES_WITH_COUNT_OF_VALUES_AND_FULL_WEIGHT){
                if(countLines == 1){
                    readLineWithCountOfValues(line);
                }

                if(countLines == 2){
                    readLineWithKnapsackCapacityAndCreateArrays(line);
                }

            }else {
                readLineWithValueAndWeightAndCalculateRelativeCostAndSaveInArrays(line,countLines);
            }

            line = ReaderHelp.tryReadLine(br);
        }
    }



    private void readLineWithCountOfValues(String line) {
        numberOfElements = Integer.valueOf(line);
    }

    private void readLineWithKnapsackCapacityAndCreateArrays(String line) {
        capacityKnapsackArray[0] = Integer.valueOf(line);
        createArrays();
    }

    private void createArrays() {

        valuesElementsArray = new float[numberOfElements];
        weightsMatrix = new  float [1][numberOfElements];



    }


    private void readLineWithValueAndWeightAndCalculateRelativeCostAndSaveInArrays(String line, int countLines) {
        final int COUNT_OF_HEAD_LINES = 2;

        int index = countLines - COUNT_OF_HEAD_LINES -1; //( -1 to force init zero)

        float [] valuesAndWeightsFloatArray = getValueAndWeightFrom(line);

        float value = valuesAndWeightsFloatArray[0];
        float weight = valuesAndWeightsFloatArray[1];
        float relativeCost = calculeteRelativeCost(value,weight);

        valuesElementsArray[index] = value;
        weightsMatrix[0][index] =weight;
        relativeCostsMap.put(index,relativeCost);

    }

    private float calculeteRelativeCost(float value, float weight) {
        if(weightDividedValueIsSelected){
            return (weight/value);
        }else {
            return (value/weight);
        }

    }


    private float[] getValueAndWeightFrom(String line) {
        String [] valuesAndWeightString =  line.trim().split("\\s+");

        float [] valuesAndWeightsFloatArray = new float[2];

        valuesAndWeightsFloatArray [0] = Float.valueOf(valuesAndWeightString[0]);
        valuesAndWeightsFloatArray [1] = Float.valueOf(valuesAndWeightString[1]);

        return valuesAndWeightsFloatArray;

    }

    @Override
    public Map<Integer, Float> getRelativeCostsMap() {
        return relativeCostsMap;
    }

    @Override
    public float[] getCapacityKnapsackArray() {
        return capacityKnapsackArray;
    }

    @Override
    public float[][] getWeightsMatrix() {
        return weightsMatrix;
    }

    @Override
    public float[] getValuesElementsArray() {
        return valuesElementsArray;
    }

    @Override
    public int getNumberOfElements() {
        return numberOfElements;
    }

    @Override
    public int getNumberOfKnapsack() {
        return 1;
    }

}
