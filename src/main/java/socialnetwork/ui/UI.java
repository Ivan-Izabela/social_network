package socialnetwork.ui;

import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.CerereService;
import socialnetwork.service.MesajService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private MesajService mesajService;
    private CerereService cerereService;

    public UI(UtilizatorService utilizatorService, PrietenieService prietenieService, MesajService mesajService,CerereService cerereService) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.mesajService=mesajService;
        this.cerereService=cerereService;
    }

    public void run(){
        Scanner in= new Scanner(System.in);
        while(true){
            System.out.println("Press:");
            System.out.println("0 exit");
            System.out.println("1 to print all users");
            System.out.println("2 to add user");
            System.out.println("3 to delete user");
            System.out.println("4 to print all friendships");
            System.out.println("5 to add friendship");
            System.out.println("6 to delete friendship");
            System.out.println("7 to print friendships for an user");
            System.out.println("8 to print friendships for an user/month ");
            System.out.println("9 to print all messages");
            System.out.println("10 to send a message");
            System.out.println("11 to delete a message");
            System.out.println("12 to pint conversation");
            System.out.println("13 to pint all requests");
            System.out.println("14 to send a request");
            System.out.println("15 to delete a request");
            System.out.println("16 to accept/reject a request");



            String option =in.next();
            switch (option){
                case "0": return;
                case "1":
                    utilizatorService.getAll().forEach(System.out::println);
                    break;
                case "2":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("ID: ");
                        String id =in.next();
                        System.out.println("First name: ");
                        String firstname =in.next();
                        System.out.println("Last name: ");
                        String lastname =in.next();

                        utilizatorService.addUtilizator(id,firstname,lastname);
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (IllegalArgumentException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "3":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("ID: ");
                        String id =in.next();
                        utilizatorService.deleteUtilizator(id);
                        utilizatorService.getAll().forEach(x-> prietenieService.deletePrietenie(id,x.getId().toString()));
                        utilizatorService.getAll().forEach(x-> prietenieService.deletePrietenie(x.getId().toString(),id));
                        mesajService.getAll().stream()
                                .filter(x->x.getFrom()==Long.parseLong(id))
                                .forEach(x->mesajService.deleteMessage(x.getId().toString()));
                        cerereService.getAll().stream()
                                .filter(x->x.getId().getLeft()==Long.parseLong(id))
                                .forEach(x->cerereService.deleteCerere(id,x.getId().getRight().toString()));

                        cerereService.getAll().stream()
                                .filter(x->x.getId().getRight()==Long.parseLong(id))
                                .forEach(x->cerereService.deleteCerere(x.getId().getLeft().toString(),id));

                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "4":
                    prietenieService.getAll().forEach(System.out::println);
                    //prietenieService.getAll().forEach(x-> System.out.println(x.getDate()));
                    break;
                case "5":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("Id1: ");
                        String id1 =in.next();
                        System.out.println("Id2: ");
                        String id2 =in.next();
                        prietenieService.addPrietenie(id1,id2);
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "6":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("Id1: ");
                        String id1 =in.next();
                        System.out.println("Id2: ");
                        String id2 =in.next();
                        prietenieService.deletePrietenie(id1,id2);

                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "7":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("ID: ");
                        String id =in.next();
                        prietenieService.prieteniUtilizator(id);
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "8":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("ID: ");
                        String id =in.next();
                        System.out.println("Month: ");
                        String month=in.next();
                        prietenieService.prieteniUtilizatorLuna(id,month);
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "9":
                    mesajService.getAll().forEach(System.out::println);
                    break;
                case "10":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("ID: ");
                        String id =in.next();
                        System.out.println("Id (log in): ");
                        String idFrom=in.next();
                        System.out.println("Sent message to: ");
                        mesajService.getAll().stream()
                                .filter(x->x.getFrom()==Long.parseLong(idFrom))
                                .forEach(x-> System.out.println(x.getTo()+" "+x.getMessage()+ " "+ x.getDate()));

                        System.out.println("Recive message from: ");
                        mesajService.getAll().stream()
                                .filter(x->x.getTo().contains(Long.parseLong(idFrom)))
                                .forEach(x-> System.out.println(x.getFrom()+" "+x.getMessage()+ " "+ x.getDate()));

                        List<String> list=new ArrayList<String>();
                        System.out.println("Send to id: (press 0 to stop) ");
                        String idTo=in.next();
                        while(!idTo.equals("0")){
                            list.add(idTo);
                            System.out.println("Send to id: (press 0 to stop) ");
                            idTo=in.next();


                        }
                        System.out.println("Message: ");
                        String message=in.next();
                        mesajService.trimiteMesaj(id,idFrom,list,message);
                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "11":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("ID: ");
                        String id =in.next();
                        mesajService.deleteMessage(id);


                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "12":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("Id user 1: ");
                        String id1 =in.next();
                        System.out.println("Id user 2: ");
                        String id2 =in.next();
                        mesajService.conversatie(id1,id2);
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "13":
                    cerereService.getAll().forEach(System.out::println);
                    break;
                case "14":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("Id from: ");
                        String id1 =in.next();
                        System.out.println("Id to: ");
                        String id2 =in.next();
                        cerereService.trimiteCerere(id1,id2);
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "15":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("Id from: ");
                        String id1 =in.next();
                        System.out.println("Id to: ");
                        String id2 =in.next();
                        cerereService.deleteCerere(id1,id2);
                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "16":
                    try{
                        System.out.println("Enter the data: ");
                        System.out.println("Id (log in): ");
                        String id1 =in.next();

                        System.out.println("Request: ");
                        cerereService.cereriUtilizator(id1);

                        System.out.println("Id (accept/reject): ");
                        String id2 =in.next();
                        System.out.println("Accept?(yes/no)");
                        String rasp =in.next();
                        String status="rejected";
                        if(rasp.equals("yes")){
                            status="approved";
                        }


                        cerereService.accepta_respinge(id2,id1,status);

                    }catch (ValidationException ex){
                        System.out.println(ex.getMessage());
                    }catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;



                default:
                    System.out.println("Invalid option");

            }
        }

    }
}
