package word2pdf;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class DataHandler {
    private ArrayList<String> inputList = new ArrayList<>();
    private ArrayList<String> processList= new ArrayList<>();

    private HomePage homePage;

    public DataHandler(HomePage homePage){
        this.homePage = homePage;
    }
    public ArrayList<String> getProcessList(){
        return this.processList;
    }

    public void clearAllFiles(){
        this.inputList.clear();
        this.processList.clear();
        this.homePage.updateList(this.inputList, this.processList);
    }
    public void readFiles(String inputPath){
        boolean emptyFolder = true;
        File folder = new File(inputPath);
        for(File file : Objects.requireNonNull(folder.listFiles())){
            String fileName = file.getName();
            if(fileName.endsWith(".docx")){
                emptyFolder = false;
                this.inputList.add(fileName);
            }
        }

        if(emptyFolder){
            System.out.println("This folder doesn't have any file.");
        }else {
            this.homePage.setInputListItem(this.inputList);
        }
    }

    public void insert2Process(String file){
        this.processList.add(file);
        this.inputList.remove(file);
        this.homePage.updateList(this.inputList, this.processList);
    }

    public void removeProcess(String file){
        this.inputList.add(file);
        this.processList.remove(file);
        this.homePage.updateList(this.inputList, this.processList);
    }

    public void insertAll(){
        this.processList.addAll(this.inputList);
        this.inputList.clear();
        this.homePage.updateList(this.inputList, this.processList);
    }

    public void removeAll(){
        this.inputList.addAll(this.processList);
        this.processList.clear();
        this.homePage.updateList(this.inputList, this.processList);
    }




}
