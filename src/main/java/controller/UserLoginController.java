package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.CerereService;
import socialnetwork.service.MesajService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;

import java.io.IOException;

//import java.awt.*;

public class UserLoginController {

    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private CerereService cerereService;
    private MesajService mesajService;
    ObservableList<Utilizator> modelGrade = FXCollections.observableArrayList();
    private Utilizator utilizator;

    @FXML
    TextField textFieldID;



    public void setService(UtilizatorService serviceU, PrietenieService serviceP,CerereService serviceC,MesajService serviceM) {
        this.utilizatorService=serviceU;
        this.prietenieService=serviceP;
        this.cerereService=serviceC;
        this.mesajService=serviceM;


    }


    public void handleLogin(ActionEvent actionEvent) {
        try{

            Long idLog=Long.parseLong(textFieldID.getText());
            Utilizator utilizator=utilizatorService.getOne(idLog);
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"Welcome","You are now logged in");
            this.utilizator=utilizator;

            showPage();
        }catch (IOException ex){
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            MessageAlert.showErrorMessage(null,ex.getMessage());
        }


    }



    public void showPage() throws IOException {
        Stage primaryStage=new Stage();
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/userPageView.fxml"));
        AnchorPane root=loader.load();

        UserPageController userPageController=loader.getController();
        userPageController.setUtilizator(utilizator);
        userPageController.setStage(primaryStage);

        userPageController.setService(this.utilizatorService,this.prietenieService,this.cerereService,this.mesajService);
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setTitle(utilizator.getFirstName()+" "+utilizator.getLastName());
        primaryStage.show();



    }

}
