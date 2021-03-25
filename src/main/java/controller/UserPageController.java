package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.CerereService;
import socialnetwork.service.MesajService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.List;


public class UserPageController implements Observer<UtilizatorChangeEvent> {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private CerereService cerereService;
    private MesajService mesajService;
    ObservableList<Utilizator> modelGrade = FXCollections.observableArrayList();
    private Utilizator utilizator;
    Stage stage;


    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator,String> tableColumnNume;
    @FXML
    TableColumn<Utilizator,String> tableColumnPrenume;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public void setService(UtilizatorService serviceU, PrietenieService serviceP,CerereService serviceC,MesajService serviceM) {
        this.utilizatorService=serviceU;
        this.prietenieService=serviceP;
        this.cerereService=serviceC;
        this.mesajService=serviceM;
        prietenieService.addObserver(this);
        mesajService.addObserver(this);
        initModel();

    }
    @FXML
    public void initialize() {
        // TODO
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnPrenume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableView.setItems(modelGrade);
    }


    private void initModel(){
        List<Utilizator> utilizatori=prietenieService.prieteniUtilizator1(utilizator.getId().toString());
        modelGrade.setAll(utilizatori);
    }
    @Override
    public void update(UtilizatorChangeEvent prietenieChangeEvent) {
        initModel();
    }


    public void handleLogout(ActionEvent actionEvent) {
        stage.close();
    }

    public void handleAdd(ActionEvent actionEvent) {
        try {
            Stage addStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/addFriendView.fxml"));
            AnchorPane root = loader.load();

            AddFriendController addFriendController=loader.getController();
            addFriendController.setUtilizatorA(utilizator);
            addFriendController.setStageA(addStage);

            addFriendController.setServiceA(utilizatorService,prietenieService,cerereService);


            addStage.setScene(new Scene(root, 400, 400));
            addStage.setTitle("Add new friend");
            addStage.show();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public void handleDelete(ActionEvent actionEvent) {
        Utilizator utilizatorD=tableView.getSelectionModel().getSelectedItem();
        if(utilizatorD!=null){
            prietenieService.deletePrietenie(utilizator.getId().toString(),utilizatorD.getId().toString());
            prietenieService.deletePrietenie(utilizatorD.getId().toString(),utilizator.getId().toString());
            cerereService.deleteCerere(utilizator.getId().toString(),utilizatorD.getId().toString());
            cerereService.deleteCerere(utilizatorD.getId().toString(),utilizator.getId().toString());
            mesajService.notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD,utilizator));

        }
        else {
            MessageAlert.showErrorMessage(null,"You haven`t selected anything");

        }

    }

    public void handleFriendRequest(ActionEvent actionEvent) {
        try {
            Stage requestStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/friendRequestView.fxml"));
            AnchorPane root = loader.load();

            FriendRequestController friendRequestController=loader.getController();
            friendRequestController.setUtilizatorR(utilizator);
            friendRequestController.setStageR(requestStage);
            friendRequestController.setServiceR(utilizatorService,prietenieService,cerereService,mesajService);


            requestStage.setScene(new Scene(root, 400, 400));
            requestStage.setTitle("Friend request");
            requestStage.show();

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void handleRefresh(ActionEvent actionEvent) {
        initModel();
    }


    public void handleMyRequest(ActionEvent actionEvent) {
        try {
            Stage requestStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/myRequestView.fxml"));
            AnchorPane root = loader.load();

            MyRequestController myRequestController=loader.getController();
            myRequestController.setUtilizatorR(utilizator);
            myRequestController.setStageR(requestStage);
            myRequestController.setServiceR(utilizatorService,prietenieService,cerereService);


            requestStage.setScene(new Scene(root, 400, 400));
            requestStage.setTitle("My request");
            requestStage.show();

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void handleChat(ActionEvent actionEvent) {
        try {
            Stage requestStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/chatPageView.fxml"));
            AnchorPane root = loader.load();

            MesajeController mesajController=loader.getController();
            mesajController.setUtilizatorR(utilizator);
            mesajController.setStageR(requestStage);
            mesajController.setServiceR(utilizatorService,prietenieService,mesajService);


            requestStage.setScene(new Scene(root, 500, 400));
            requestStage.setTitle("Chat");
            requestStage.show();

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
