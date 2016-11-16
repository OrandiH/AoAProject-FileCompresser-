package compresser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;


public class FileCompression extends Application {
    @FXML
    private ProgressBar progressBar;

   private Stage primaryStage = new Stage();
    File file = null;

    File selectedFile = new  File("selectedFile.txt");
    public static void main(String[] args) {
        launch(args);    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("LZW File Compression Wizard");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public void on_click(){
        Window stage = null;
        FileChooser fileChooser;
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File to Compress or Decompress");
        selectedFile = fileChooser.showOpenDialog(stage);
        lzw(selectedFile);
}
    public void updateProgressBar(int num1, int num2){
        progressBar = new ProgressBar();
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                double num1 =1;
                double num2 =0;
                progressBar.setProgress(num1/num2);
                //primaryStage.show();
            }
        });
    }

    public void lzw(File selectedFile){
        FileInputStream input = null;
        FileOutputStream output = null;


        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("LZW files (*.lzw)", "*.lzw");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(primaryStage);
            input = new FileInputStream(selectedFile);
            output = new FileOutputStream(file);
            LZW lzwCompression = new LZW(input,output);
            lzwCompression.Compress();
            updateProgressBar(1,0);
            try
            {
                input.close();
                output.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("File compressed. ");
    }
}
