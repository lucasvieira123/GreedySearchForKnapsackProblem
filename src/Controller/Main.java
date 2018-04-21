package Controller;

import custom.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    @FXML
    private CheckBox multDimencheckBox;



    FileChooser fileChooser = new FileChooser();
    private File selectedFile;
    private boolean weightDividedValueIsSelected = true;

    private float[] valuesArray;
    private float[][] weightsMatrix;
    private Map<Integer,Float> relativeCostsMap;

    private float [] fullWeights;
    private int countValues;
    private int countWeights;
    private int countRelativeCosts;
    private boolean isMultipleKnapsack = true;


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
             selectedFile = fileChooser.showOpenDialog(primaryStage);
            pathFileTxtFild.setText(selectedFile.getPath());

        });
        
        startBtn.setOnAction(event -> {
            isMultipleKnapsack = multDimencheckBox.isSelected();

           if(checkExistFile()){
               startReadFileAndAlgorithmGreedySearch();
               
           }else {
               //mgs erro
           }
            
           
        });




    }

    ReaderFile readerFile;

    private void startReadFileAndAlgorithmGreedySearch() {
        //unique mult dimension
        if(isMultipleKnapsack){
            readerFile = new ReaderFileWithMultDimension(selectedFile, weightDividedValueIsSelected);
            readerFile.buildAllConfigurations();

            relativeCostsMap = readerFile.getRelativeCostsMap();
            valuesArray = readerFile.getValuesElementsArray();
            weightsMatrix = readerFile.getWeightsMatrix();
            fullWeights = readerFile.getCapacityKnapsackArray();

        }else {
            //unique dimension
            readerFile = new ReaderFileWithOneDimension(selectedFile,
                    weightDividedValueIsSelected);
            readerFile.buildAllConfigurations();
            relativeCostsMap = readerFile.getRelativeCostsMap();
            valuesArray = readerFile.getValuesElementsArray();
            weightsMatrix = readerFile.getWeightsMatrix();
            fullWeights = readerFile.getCapacityKnapsackArray();
        }


        float amount = startAlgorithmGreedySearch();

        amoutTxtField.setText("");
        amoutTxtField.setText(String.valueOf(amount));


    }

    private Float startAlgorithmGreedySearch() {
        boolean ascendingOrder = false; // ordem decrescente

        if(weightDividedValueIsSelected){
            ascendingOrder = true;
        }

        ArrayList<Map.Entry<Integer,Float>> ordernedList = new SortHelp(ascendingOrder, relativeCostsMap)
                .getOrdenedList();


        float weightPlaced = 0;
        float amount = 0;
        int currentKey;
        int indexSnapsack = 0;


        for (Map.Entry<Integer, Float> entry : ordernedList){
          currentKey = entry.getKey();

          if(willExceedMaximumCapacity(currentKey, weightPlaced, indexSnapsack)){

              if(existSnapsackYet(indexSnapsack)){
                  indexSnapsack++;
                  weightPlaced=0;

              }else {
                  return amount;
              }

          }

          amount += getCurrentValue(currentKey);
          weightPlaced += getCurrentWeight(currentKey);

        }

        return amount;


    }

    private boolean existSnapsackYet(int snapsack) {
      int indexSnapsack = readerFile.getNumberOfKnapsack();
        return indexSnapsack > snapsack+1;
    }

    private boolean willExceedMaximumCapacity(int currentKey, float weightPlaced, int indexSnapsack) {
        return getCurrentWeight(currentKey)+weightPlaced > fullWeights[indexSnapsack];

    }

    private float getCurrentValue(int currentKey) {
        int currentCollumn = currentKey % readerFile.getNumberOfElements();
        return valuesArray[currentCollumn];
    }

    private float getCurrentWeight(int currentKey) {
        int currentLine = currentKey / readerFile.getNumberOfElements();
        int currentCollumn = currentKey % readerFile.getNumberOfElements();
        return weightsMatrix[currentLine][ currentCollumn];
    }



    private boolean checkExistFile() {
        return selectedFile != null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
