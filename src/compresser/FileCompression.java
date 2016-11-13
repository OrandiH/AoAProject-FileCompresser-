package compresser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;


public class FileCompression extends Application {
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

    public void lzw(File selectedFile){
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(selectedFile);
            output = new FileOutputStream("compressed.lzw");
            LZW lzwCompression = new LZW(input,output);
            lzwCompression.Compress();

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
