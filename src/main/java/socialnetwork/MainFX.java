package socialnetwork;

import controller.UserLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.CerereValidator;
import socialnetwork.domain.validators.MesajValidator;
import socialnetwork.domain.validators.PrietenieValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.CerereFile;
import socialnetwork.repository.file.MesajFile;
import socialnetwork.repository.file.PrietenieFile;
import socialnetwork.repository.file.UtilizatorFile;
import socialnetwork.service.CerereService;
import socialnetwork.service.MesajService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;

public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginView.fxml"));
        AnchorPane root=loader.load();

        UserLoginController userController=loader.getController();

        String usersFile= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        Repository<Long, Utilizator> userFileRepository = new UtilizatorFile(usersFile
                , new UtilizatorValidator());

        String friendshipsFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendships");
        Repository<Tuple<Long,Long>, Prietenie> friendshipsFileRepository = new PrietenieFile(friendshipsFile
                , new PrietenieValidator());

        String requestFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.request");
        Repository<Tuple<Long,Long>, Cerere> requestFileRepository = new CerereFile(requestFile
               , new CerereValidator());

        String messageFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.message");
        Repository<Long, Mesaj> messageFileRepository= new MesajFile(messageFile,new MesajValidator());


        userController.setService(new UtilizatorService(userFileRepository),
                new PrietenieService(friendshipsFileRepository,userFileRepository),
                new CerereService(requestFileRepository,
                friendshipsFileRepository,userFileRepository),
                new MesajService(messageFileRepository,
                friendshipsFileRepository,userFileRepository));

        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setTitle("Login");
        primaryStage.show();




    }
}
