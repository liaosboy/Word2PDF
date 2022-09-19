package word2pdf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class HomePage {
    private JPanel mainPanel;
    private JButton inputBtn;
    private JButton saveBtn;
    private JTextField inputPath;
    private JTextField savePath;
    private JButton insertBtn;
    private JButton deleteBtn;
    private JButton deleteAllBtn;
    private JList<String> inputList;
    private JList<String> processList;
    private JButton insertAllBtn;
    private JButton processBtn;
    private JProgressBar progressBar1;

    DataHandler dataHandler;
    ConvertPDF convertPDF;

    public HomePage() {
        this.dataHandler = new DataHandler(this);
        this.convertPDF = new ConvertPDF(this);

        inputBtn.addActionListener(this::chooseFolderAction);
        saveBtn.addActionListener(this::chooseFolderAction);
        insertBtn.addActionListener(this::insert2Process);
        deleteBtn.addActionListener(this::removeProcess);
        insertAllBtn.addActionListener(this::insertAll);
        deleteAllBtn.addActionListener(this::removeAll);
        processBtn.addActionListener(this::processExec);
    }
    public void processExec(ActionEvent e){
        this.setAllBtnEnable(false);
        ArrayList<String> pList = this.dataHandler.getProcessList();
        ConvertPDF convertPDF = this.convertPDF;
        Runnable r = () -> convertPDF.batchConvert(pList);

        new Thread(r).start();
    }

    public void insert2Process(ActionEvent e){
        List<String> files = this.inputList.getSelectedValuesList();
        for(String item : files){
            this.dataHandler.insert2Process(item);
        }
    }

    public void removeProcess(ActionEvent e){
        List<String> files = this.processList.getSelectedValuesList();
        for(String item:files){
            this.dataHandler.removeProcess(item);
        }

    }

    public void insertAll(ActionEvent e){
        this.dataHandler.insertAll();
    }

    public void removeAll(ActionEvent e){
        this.dataHandler.removeAll();
    }

    public void updateList(ArrayList<String> inputItems, ArrayList<String> processItems){
        this.inputList.setListData(inputItems.toArray(String[]::new));
        this.processList.setListData(processItems.toArray(String[]::new));
    }
    public void chooseFolderAction(ActionEvent e) {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if(!inputPath.getText().isEmpty()){
            chooser.setCurrentDirectory(new java.io.File(this.inputPath.getText()));
        }

        int option = chooser.showOpenDialog(SwingUtilities.getWindowAncestor(mainPanel));

        if (option == JFileChooser.APPROVE_OPTION) {
            String choosePath = chooser.getSelectedFile().toString();
            if(e.getActionCommand().equals("讀取資料夾")){
                this.dataHandler.clearAllFiles();
                inputPath.setText(choosePath);
                this.dataHandler.readFiles(choosePath);
                this.convertPDF.setInputPath(choosePath);
                if(savePath.getText().isEmpty()){
                    savePath.setText(choosePath);
                    this.convertPDF.setSavePath(choosePath);
                }
            }else {
                savePath.setText(choosePath);
                this.convertPDF.setSavePath(choosePath);
            }
        }
    }

    public void setInputListItem(ArrayList<String> items){
        this.inputList.setListData(items.toArray(String[]::new));
    }

    public void setProcess(int v){
        this.progressBar1.setValue(v);
    }

    public void setProgressBarText(String info){
        this.progressBar1.setString(info);
    }

    public void setAllBtnEnable(Boolean bool){
        this.processBtn.setEnabled(bool);
        this.insertAllBtn.setEnabled(bool);
        this.insertBtn.setEnabled(bool);
        this.deleteAllBtn.setEnabled(bool);
        this.deleteBtn.setEnabled(bool);
    }

    public void showMessage(String msg){
        JOptionPane.showMessageDialog(null, msg, "Word To PDF", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }

        JFrame jframe = new JFrame("HomePage");
        jframe.setContentPane(new HomePage().mainPanel);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setVisible(true);
    }
}
