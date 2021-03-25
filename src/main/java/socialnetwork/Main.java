package socialnetwork;

//import socialnetwork.components.Graph;


public class Main {

    public static void main(String[] args) {
        MainFX.main(args);
    }
//    public static void main(String[] args) {
//        String usersFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
//        String friendshipsFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendships");
//        String messageFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.message");
//        String requestFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.request");
//
//        //String fileName="data/users.csv";
//        Repository<Long,Utilizator> userFileRepository = new UtilizatorFile(usersFile
//                , new UtilizatorValidator());
//
//        Repository<Tuple<Long,Long>, Prietenie> friendshipsFileRepository = new PrietenieFile(friendshipsFile
//                , new PrietenieValidator());
//
//        Repository<Long, Mesaj> messageFileRepository= new MesajFile(messageFile,new MesajValidator());
//
//        Repository<Tuple<Long,Long>, Cerere> requestFileRepository = new CerereFile(requestFile
//                , new CerereValidator());
//
//
//
//
//
//
//        PrietenieService prietenieService=new PrietenieService(friendshipsFileRepository,userFileRepository);
//        UtilizatorService utilizatorService=new UtilizatorService(userFileRepository);
//        MesajService mesajService=new MesajService(messageFileRepository,
//                friendshipsFileRepository,userFileRepository);
//        CerereService cerereService=new CerereService(requestFileRepository,
//                friendshipsFileRepository,userFileRepository);
//
//
//
//        UI ui=new UI(utilizatorService,prietenieService,mesajService,cerereService);
//        ui.run();
//
//
//    }
}


