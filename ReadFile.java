import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class ReadFile{
    private ArrayList<Double> data;
    private Scanner x, y;
    private String fileName;


    public void openFile(){
        try {
            x = new Scanner (new File(fileName));
        } catch (Exception e) {
            System.out.println("Wrong File Name");
        }
    }

    public void fileName(){     
        y = new Scanner(System.in);
        System.out.println("Type a valid file path that points to the file containing the boxes data: ");
        fileName = y.nextLine(); 
    }

    public void readFile(){
        data = new ArrayList<Double>();
        while(x.hasNext()){
            double a = x.nextDouble();
            data.add(a);
        }
    }

    public void closeFile(){
        x.close();
    }

    public ArrayList<Double> dataList(){
        fileName();
        openFile();
        readFile();
        closeFile();
        return data;
    }
}