package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.CerereService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.util.List;

public class MyRequestController implements Observer<UtilizatorChangeEvent> {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private CerereService cerereService;
    ObservableList<Utilizator> modelGrade = FXCollections.observableArrayList();
    private Utilizator utilizator;
    Stage stage;

    @FXML
    TableView<Utilizator> tableViewMyRequest;
    @FXML
    TableColumn<Utilizator,String> tableColumnNumeMyRequest;
    @FXML
    TableColumn<Utilizator,String> tableColumnPrenumeMyRequest;





    public void setStageR(Stage stage) {
        this.stage = stage;
    }

    public void setUtilizatorR(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    @FXML
    public void initialize() {
        // TODO
        tableColumnNumeMyRequest.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnPrenumeMyRequest.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));

        tableViewMyRequest.setItems(modelGrade);

    }


    private void initModel(){
        List<Utilizator> utilizatori=cerereService.cereriUtilizator2(utilizator.getId().toString());
        modelGrade.setAll(utilizatori);
    }

    @Override
    public void update(UtilizatorChangeEvent cerereChangeEvent) {
        initModel();

    }

    public void setServiceR(UtilizatorService serviceU, PrietenieService serviceP,CerereService serviceC) {
        this.utilizatorService=serviceU;
        this.prietenieService=serviceP;
        this.cerereService=serviceC;
        cerereService.addObserver(this);

        initModel();

    }

    public void handleDeleteRequest(ActionEvent actionEvent) {
        Utilizator utilizatorD=tableViewMyRequest.getSelectionModel().getSelectedItem();
        if(utilizatorD!=null){
            cerereService.deleteCerere(utilizator.getId().toString(),utilizatorD.getId().toString());
        }
        else {
            MessageAlert.showErrorMessage(null,"You haven`t selected anything");

        }
    }
}
