package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Cerere;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.CerereService;
import socialnetwork.service.MesajService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendRequestController implements Observer<UtilizatorChangeEvent> {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private CerereService cerereService;
    private MesajService mesajService;
    ObservableList<Utilizator> modelGrade = FXCollections.observableArrayList();
    private Utilizator utilizator;
    Stage stage;

    @FXML
    TableView<Utilizator> tableViewR;
    @FXML
    TableColumn<Utilizator,String> tableColumnNumeR;
    @FXML
    TableColumn<Utilizator,String> tableColumnPrenumeR;
    @FXML
    TextField textFieldStatus;
    @FXML
    TextField textFieldData;




    public void setStageR(Stage stage) {
        this.stage = stage;
    }

    public void setUtilizatorR(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    @FXML
    public void initialize() {
        // TODO
        tableColumnNumeR.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnPrenumeR.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));

        tableViewR.setItems(modelGrade);

    }


    private void initModel(){
        List<Utilizator> utilizatori=cerereService.cereriUtilizator1(utilizator.getId().toString());
        modelGrade.setAll(utilizatori);

    }

    @Override
    public void update(UtilizatorChangeEvent utilizatorChangeEvent) {
        initModel();

    }

    public void setServiceR(UtilizatorService serviceU, PrietenieService serviceP,CerereService serviceC,MesajService serviceM) {
        this.utilizatorService=serviceU;
        this.prietenieService=serviceP;
        this.cerereService=serviceC;
        this.mesajService=serviceM;
        cerereService.addObserver(this);
        prietenieService.addObserver(this);
        mesajService.addObserver(this);


        initModel();

    }



    public void handleStatus(ActionEvent actionEvent) {
        Utilizator utilizatorR=tableViewR.getSelectionModel().getSelectedItem();
        if(utilizatorR!=null) {
            Cerere cerere = cerereService.getOne(utilizatorR.getId(), utilizator.getId());
            textFieldStatus.setText(cerere.getStatus());
            textFieldData.setText(cerere.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
        }
        else{
            MessageAlert.showErrorMessage(null,"You haven`t selected anything");
        }
    }

    public void handleAccept(ActionEvent actionEvent) {
        Utilizator utilizatorR=tableViewR.getSelectionModel().getSelectedItem();
        if(utilizatorR!=null) {
            try {
                cerereService.accepta_respinge(utilizatorR.getId().toString(), utilizator.getId().toString(), "approved");
                prietenieService.notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD,utilizator));
                mesajService.notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD,utilizator));
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "", "You`re friends with " + utilizatorR.getFirstName()
                        + " " + utilizatorR.getLastName() + " now");

            }catch (Exception ex){
                MessageAlert.showErrorMessage(null,ex.getMessage());
            }
        }
        else{
            MessageAlert.showErrorMessage(null,"You haven`t selected anything");
        }
    }

    public void handleReject(ActionEvent actionEvent) {
        Utilizator utilizatorR=tableViewR.getSelectionModel().getSelectedItem();
        if(utilizatorR!=null) {
            try {
                cerereService.accepta_respinge(utilizatorR.getId().toString(), utilizator.getId().toString(), "rejected");
                //initModel();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "", "Request rejected");
            }catch (Exception ex) {
                MessageAlert.showErrorMessage(null, ex.getMessage());
            }
        }
        else{
            MessageAlert.showErrorMessage(null,"You haven`t selected anything");
        }
    }



}
