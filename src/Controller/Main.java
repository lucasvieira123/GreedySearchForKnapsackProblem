package Controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Main extends Application {

    private Stage primaryStage;
    private AnchorPane mainLayout;

    @FXML
    private TextField pathFileTxtFild;

    @FXML
    private TextField amoutTxtField;

    @FXML
    private RadioButton valueDividedWeightRadioBt;

    @FXML
    private RadioButton weightDividedValueRadioBt;
    
    @FXML
    private Button startBtn;

    @FXML
    private Button chooseBtn;



    FileChooser fileChooser = new FileChooser();
    private File chosenFile;
    private boolean weightDividedValueIsSelected = true;

    private Float [] valuesArray;
    private Float [] weightsArray;
    private Map<Integer,Float> relativeCostsMap;

    private float fullWeight;
    private int countValues;
    private int countWeights;
    private int countRelativeCosts;


    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Greedy Search");
        configureFileChooser();
        initRootLayout();

    }

    private void configureFileChooser() {
        fileChooser.setTitle("Choose text file with settings (KPn.txt)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));

    }

    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/View/MainLayout.fxml"));
        try {
            mainLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @FXML
    private void initialize() {

        weightDividedValueRadioBt.setSelected(true);

        weightDividedValueRadioBt.setOnAction(event -> {
            valueDividedWeightRadioBt.setSelected(false);
            weightDividedValueIsSelected = true;
        });

        valueDividedWeightRadioBt.setOnAction(event -> {
            weightDividedValueRadioBt.setSelected(false);
            weightDividedValueIsSelected = false;
        });

        chooseBtn.setOnAction(event -> {
             chosenFile = fileChooser.showOpenDialog(primaryStage);
            pathFileTxtFild.setText(chosenFile.getPath());

        });
        
        startBtn.setOnAction(event -> {
           if(checkExistFile()){
               startReadFileAndAlgorithmGreedySearch();
               
           }else {
               //mgs erro
           }
            
           
        });




    }

    private void startReadFileAndAlgorithmGreedySearch() {
       readFileAndSaveValuesSaveWeightsAndRelativeCostAndCountOfValuesAndWithKnapsackCapacity();
        float amount = startAlgorithmGreedySearch();

        amoutTxtField.setText("");
        amoutTxtField.setText(String.valueOf(amount));


    }

    private Float startAlgorithmGreedySearch() {
        ArrayList<Map.Entry<Integer,Float>> ordernedList;

        boolean ascendingOrder = false; // ordem crescente

        if(weightDividedValueIsSelected){
            ascendingOrder = true;
        }

        if(ascendingOrder){
            ordernedList = (ArrayList<Map.Entry<Integer, Float>>) buildAscendingOrderList();
        }else {
            ordernedList = (ArrayList<Map.Entry<Integer, Float>>) buildReverseOrderList();
        }

        float weightPlaced = 0;
        float amount = 0;
        int currentKey;




        for (Map.Entry<Integer, Float> entry : ordernedList){
          currentKey = entry.getKey();

          if(willExceedMaximumCapacity(currentKey, weightPlaced)){
              return amount;
          }



          amount += getCurrentValue(currentKey);
            weightPlaced += getCurrentWeight(currentKey);

        }

        return amount;


    }

    private boolean willExceedMaximumCapacity(int currentKey, float weightPlaced) {
        return getCurrentWeight(currentKey)+weightPlaced> fullWeight;

    }

    private float getCurrentValue(int currentKey) {
        return valuesArray[currentKey];
    }

    private float getCurrentWeight(int currentKey) {
        return weightsArray[currentKey];
    }


    private List<Map.Entry<Integer,Float>> buildReverseOrderList() {
        List<Map.Entry<Integer,Float>> sortedEntries = new ArrayList<>(relativeCostsMap.entrySet());

        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return sortedEntries;

    }

    private  List<Map.Entry<Integer,Float>> buildAscendingOrderList() {
        List<Map.Entry<Integer,Float>> sortedEntries = new ArrayList<>(relativeCostsMap.entrySet());

         sortedEntries.sort(Comparator.comparing(Map.Entry::getValue));

         return  sortedEntries;
    }


    private void readFileAndSaveValuesSaveWeightsAndRelativeCostAndCountOfValuesAndWithKnapsackCapacity() {
        BufferedReader br = tryGetBufferedReader();
        final int COUNT_LINES_WITH_COUNT_OF_VALUES_AND_FULL_WEIGHT = 2;
        int countLines = 0;

        if(br == null) return;

        String line = tryReadLine(br);

       // if(fistLine == null) return;

       // int [] countsValuesAndFullWeight = getValueAndWeightFrom(fistLine);

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

            line = tryReadLine(br);
        }

    }



    private void readLineWithCountOfValues(String line) {
        countValues = Integer.valueOf(line);
        countWeights = countValues;
        countRelativeCosts = countValues;
    }

    private void readLineWithKnapsackCapacityAndCreateArrays(String line) {
        fullWeight = Integer.valueOf(line);
        createArrays();
    }

    private void createArrays() {
        valuesArray = new Float[countValues];
        weightsArray = new Float[countValues];
        relativeCostsMap = new HashMap<>();
    }

    /*private void buildAscendingOrderList() {

        relativeCostsMap = new TreeMap<>();

    }

    private void buildReverseOrderList() {
        relativeCostsMap = new TreeMap<>(Comparator.reverseOrder());
    }*/




    private void readLineWithValueAndWeightAndCalculateRelativeCostAndSaveInArrays(String line, int countLines) {
        final int COUNT_OF_HEAD_LINES = 2;

        int index = countLines - COUNT_OF_HEAD_LINES -1; //( -1 to force init zero)

        float [] valuesAndWeightsFloatArray = getValueAndWeightFrom(line);

        float value = valuesAndWeightsFloatArray[0];
        float weight = valuesAndWeightsFloatArray[1];
        float relativeCost = calculeteRelativeCost(value,weight);

        valuesArray[index] = value;
        weightsArray[index] = weight;
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

    private BufferedReader tryGetBufferedReader() {
        try {
            return new BufferedReader(new FileReader(chosenFile));
        } catch (FileNotFoundException e) {
            //print msg de erro
            e.printStackTrace();
        }

        return null;
    }

    private String tryReadLine(BufferedReader br) {

        try {
            return br.readLine();
        } catch (IOException e) {
            //print msg de erro
            e.printStackTrace();
        }
        return null;
    }


    private boolean checkExistFile() {
        return chosenFile != null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
