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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.CerereService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.util.List;
import java.util.stream.Collectors;

public class AddFriendController implements Observer<UtilizatorChangeEvent> {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private CerereService cerereService;
    ObservableList<Utilizator> modelGrade = FXCollections.observableArrayList();
    private Utilizator utilizator;
    Stage stage;

    @FXML
    TableView<Utilizator> tableViewA;
    @FXML
    TableColumn<Utilizator,String> tableColumnNumeA;
    @FXML
    TableColumn<Utilizator,String> tableColumnPrenumeA;
    @FXML
    TextField textFieldNume;

    public void setStageA(Stage stage) {
        this.stage = stage;
    }

    public void setUtilizatorA(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    @FXML
    public void initialize() {
        // TODO
        tableColumnNumeA.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnPrenumeA.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableViewA.setItems(modelGrade);
    }


    private void initModel(){
        List<Utilizator> utilizatori=prietenieService.nonPrieteniUtilizator(utilizator.getId().toString());
        modelGrade.setAll(utilizatori);
    }
    @Override
    public void update(UtilizatorChangeEvent prietenieChangeEvent) {
        initModel();

    }

    public void setServiceA(UtilizatorService serviceU, PrietenieService serviceP,CerereService serviceC) {
        this.utilizatorService=serviceU;
        this.prietenieService=serviceP;
        this.cerereService=serviceC;
        prietenieService.addObserver(this);

        initModel();

    }

    public void handleFilndNume(KeyEvent keyEvent) {
        String nume=textFieldNume.getText();
        List<Utilizator> utilizatori=prietenieService.nonPrieteniUtilizator(utilizator.getId().toString());

        modelGrade.setAll(
                utilizatori.stream()
                .filter(x->x.getFirstName().startsWith(nume))
                .collect(Collectors.toList())
        );

    }

    public void handleSendRequest(ActionEvent actionEvent) {
        Utilizator utilizatorR=tableViewA.getSelectionModel().getSelectedItem();
        if(utilizatorR!=null){
            try {
                cerereService.trimiteCerere(utilizator.getId().toString(), utilizatorR.getId().toString());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "", "Your request has been sent");
            }catch (Exception ex){
                MessageAlert.showErrorMessage(null,ex.getMessage());
            }
        }
        else {
            MessageAlert.showErrorMessage(null,"You haven`t selected anything");

        }
    }


}
