package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.MesajService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MesajeController implements Observer<UtilizatorChangeEvent> {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private MesajService mesajService;
    ObservableList<Utilizator> modelGrade = FXCollections.observableArrayList();
    private Utilizator utilizator;
    Stage stage;

    @FXML
    TableView<Utilizator> tableViewMesaje;
    @FXML
    TableColumn<Utilizator,String> tableColumnNumeMesaj;
    @FXML
    TableColumn<Utilizator,String> tableColumnPrenumeMesaj;
    @FXML
    TextArea textAreaChat;
    @FXML
    TextField textFieldUser;
    @FXML
    TextField textFieldMesaj;






    public void setStageR(Stage stage) {
        this.stage = stage;
    }

    public void setUtilizatorR(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    @FXML
    public void initialize() {
        // TODO
        tableColumnNumeMesaj.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnPrenumeMesaj.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));

        tableViewMesaje.setItems(modelGrade);

    }


    private void initModel(){
        List<Utilizator> utilizatori=prietenieService.prieteniUtilizator1(utilizator.getId().toString());
        modelGrade.setAll(utilizatori);
    }

    @Override
    public void update(UtilizatorChangeEvent cerereChangeEvent) {
        initModel();
        initConversation();

    }

    public void setServiceR(UtilizatorService serviceU, PrietenieService serviceP,MesajService serviceM) {
        this.utilizatorService=serviceU;
        this.prietenieService=serviceP;
        this.mesajService=serviceM;
        mesajService.addObserver(this);

        initModel();

    }

    public void initConversation(){
        Utilizator utilizatorM=tableViewMesaje.getSelectionModel().getSelectedItem();
        if(utilizatorM!=null){
            textFieldUser.setText(utilizatorM.getFirstName()+" "+utilizatorM.getLastName());
            textFieldUser.setEditable(false);
            List<String> list=mesajService.conversatie1(utilizator.getId().toString(),utilizatorM.getId().toString());
            String string=list.stream()
                    .reduce("",(a,b) -> a + "\n" + b);
            textAreaChat.setText(string);
            textAreaChat.setEditable(false);

        }

    }

    public void handleShowConversation(ActionEvent actionEvent) {
        //Utilizator utilizatorM=tableViewMesaje.getSelectionModel().getSelectedItem();
        initConversation();
    }

    public void handleSendMessage(ActionEvent actionEvent) {

        try{
            String mesaj=textFieldMesaj.getText();
            Utilizator utilizatorM=tableViewMesaje.getSelectionModel().getSelectedItem();
            List<String> list=new ArrayList<>();
            list.add(utilizatorM.getId().toString());
            mesajService.trimiteMesaj(mesajService.getLastId().toString(),utilizator.getId().toString(),list,mesaj);
            textFieldMesaj.setText("");
        }catch (ValidationException ex){

            MessageAlert.showErrorMessage(null,"Please write a message!");
        }catch (NullPointerException ex){
            MessageAlert.showErrorMessage(null,"Please select someone!");
        }
    }
}
