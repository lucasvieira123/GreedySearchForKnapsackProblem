package custom;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReaderFileWithMultDimension implements ReaderFile {
    private int numberOfKnapsack;
    private int numberOfElements;
    private float[] capacityKnapsackArray;
    private float[] valuesElementsArray;
    private float[][] weightsMatrix;
    private Map<Integer, Float> relativeCostsMap = new HashMap<>();
    private boolean weightDividedValueIsSelected = false;
    private File selectedFile;

    public ReaderFileWithMultDimension(File selectedFile, boolean weightDividedValueIsSelected) {
        this.selectedFile = selectedFile;
        this.weightDividedValueIsSelected = weightDividedValueIsSelected;


    }

    private int counterOfRowAlreadyRead = 0;
    private int counterOfCollumnAlreadyRead = 0;


    //get informations about Knapsack
    @Override
    public void buildAllConfigurations() {

        BufferedReader br = ReaderHelp.tryGetBufferedReader(selectedFile);

        if (br == null) return;

        String lineWithNumberOfKnapsackAndNumberOfElements = ReaderHelp.tryReadLine(br);

        setNumberOfKnapsackAndNumberOfElements(lineWithNumberOfKnapsackAndNumberOfElements);

        valuesElementsArray = new float[numberOfElements];
        capacityKnapsackArray = new float[numberOfKnapsack];
        valuesElementsArray = new float[numberOfElements];
        weightsMatrix = new float[numberOfKnapsack][numberOfElements];


        String lineWithValuesAboutElements = ReaderHelp.tryReadLine(br);

        while (lineWithValuesAboutElements != null && !lineWithValuesAboutElements.equals("")) {
                readLineWithValuesAboutElementsAndSaveInArray(lineWithValuesAboutElements);
                counterOfRowAlreadyRead++;

            if (!existValuesOfElementsToRead()) {
                break;
            }


            lineWithValuesAboutElements = ReaderHelp.tryReadLine(br);

        }

        String lineWithWeightsOfSnapsack = ReaderHelp.tryReadLine(br);

        setWeightsOfSnapsack(lineWithWeightsOfSnapsack);

        String lineWithWeightsOfElements =  ReaderHelp.tryReadLine(br);

        counterOfRowAlreadyRead = 0;
        counterOfCollumnAlreadyRead = 0;

        while (lineWithWeightsOfElements != null && !lineWithWeightsOfElements.equals("")) {

            if(existWeightsOfElementsToReadInSameLine()){
                readLineWithWeightsSaveMatrix(lineWithWeightsOfElements);

            }else {
                counterOfRowAlreadyRead++;
                counterOfCollumnAlreadyRead = 0;
                continue;
            }



            lineWithWeightsOfElements =  ReaderHelp.tryReadLine(br);


        }




    }


    private boolean existWeightsOfElementsToReadInSameLine() {
        return counterOfCollumnAlreadyRead < numberOfElements ;
    }

    private void readLineWithWeightsSaveMatrix(String elements) {
        String[] weightsOfElementsStrings = elements.
                trim().split("\\s+");

        for(int i = 0; i < weightsOfElementsStrings.length ; i++){
            weightsMatrix[counterOfRowAlreadyRead][counterOfCollumnAlreadyRead+i] = Float.valueOf(weightsOfElementsStrings[i]);
            calculeRelativeCostAndSave(Float.valueOf(weightsOfElementsStrings[i]),counterOfCollumnAlreadyRead+i);

        }

        counterOfCollumnAlreadyRead = counterOfCollumnAlreadyRead + weightsOfElementsStrings.length;
    }

    private int indexMap = 0;
    private void calculeRelativeCostAndSave(Float currentWeight, int i) {
        float relativeCost, currentValue;
        currentValue = valuesElementsArray[i];

        if(currentValue == 0 || currentWeight ==0){
            relativeCostsMap.put(indexMap,  0.f);
            indexMap++;
            return;
        }

        if(weightDividedValueIsSelected){
            relativeCost = currentWeight/currentValue;

        }else {

            relativeCost = currentValue/currentWeight;
        }

        relativeCostsMap.put(indexMap,relativeCost);
        indexMap++;






    }


    private void setWeightsOfSnapsack(String lineWithWeightsOfSnapsack) {

        String[] weightsOfSnapsackStrings = lineWithWeightsOfSnapsack.
                trim().split("\\s+");

        for(int i = 0; i< weightsOfSnapsackStrings.length ; i++){
            capacityKnapsackArray[i] = Float.valueOf(weightsOfSnapsackStrings[i]);
        }
    }

    private void readLineWithValuesAboutElementsAndSaveInArray(String elements) {
        String[] valuesAboutElementsStrings = elements.
                trim().split("\\s+");
        for (int i = 0; i < valuesAboutElementsStrings.length; i++) {

            valuesElementsArray[i+counterOfCollumnAlreadyRead] = Float.valueOf(valuesAboutElementsStrings[i]);
        }

        counterOfCollumnAlreadyRead = counterOfCollumnAlreadyRead + valuesAboutElementsStrings.length;
    }

    public void setNumberOfKnapsackAndNumberOfElements(String numberOfKnapsackAndNumberOfElements) {
        String[] numberOfKnapsackAndNumberOfElementsInStrings = numberOfKnapsackAndNumberOfElements.
                trim().split("\\s+");

        numberOfKnapsack = Integer.valueOf(numberOfKnapsackAndNumberOfElementsInStrings[0]);
        numberOfElements = Integer.valueOf(numberOfKnapsackAndNumberOfElementsInStrings[1]);


    }

    private boolean existValuesOfElementsToRead() {
        return numberOfElements > counterOfCollumnAlreadyRead;
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
        return numberOfKnapsack;
    }
}
