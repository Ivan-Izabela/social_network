package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.PrietenieValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exception.MyException;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService implements Observable<UtilizatorChangeEvent> {
    private Repository<Tuple<Long,Long>, Prietenie> repo;
    private Repository<Long,Utilizator> repou;
    private MyException<Tuple<Long,Long>, Prietenie> myException;
    private MyException<Long,Utilizator> myExceptionU;


    public PrietenieService(Repository<Tuple<Long, Long>, Prietenie> repo,Repository<Long,Utilizator> repou) {

        this.repo = repo;
        this.repou=repou;
        myException=new MyException<>(repo);
        myExceptionU=new MyException<>(repou);

    }

    public List<Prietenie> getAll(){
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Prietenie addPrietenie(String id1Str,String id2Str){
        Long id1= PrietenieValidator.validLong(id1Str);
        Long id2= PrietenieValidator.validLong(id2Str);
        myExceptionU.existId(id1);
        myExceptionU.existId(id2);

        Prietenie p=new Prietenie(new Tuple<>(id1,id2));
        myException.is(p);
        Prietenie task=repo.save(p);
        if(task==null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD, repou.findOne(id1)));
        }
        return task;

    }

    public Prietenie deletePrietenie(String id1Str,String id2Str){
        Long id1= PrietenieValidator.validLong(id1Str);
        Long id2= PrietenieValidator.validLong(id2Str);

        Prietenie p= repo.delete(new Tuple<>(id1,id2));
        if(p==null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.DELETE,repou.findOne(id1)));
        }
        return p;
    }

    public void prieteniUtilizator(String idStr){
        List<Prietenie> prieteni = getAll();
        Long id= PrietenieValidator.validLong(idStr);
        myExceptionU.existId(id);

        prieteni.stream()
                .filter(x->x.getId().getLeft()==id)
                .forEach(x-> System.out.println(repou.findOne(x.getId().getRight()).getFirstName() + " " +
                        repou.findOne(x.getId().getRight()).getLastName()+ " " +
                        x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))));

        prieteni.stream()
                .filter(x->x.getId().getRight()==id)
                .forEach(x-> System.out.println(repou.findOne(x.getId().getLeft()).getFirstName() + " " +
                        repou.findOne(x.getId().getLeft()).getLastName()+ " " +
                        x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))));

        //return prieteni;
    }

    public void prieteniUtilizatorLuna(String idStr,String lunaStr){
        List<Prietenie> prieteni = getAll();
        Long id= PrietenieValidator.validLong(idStr);
        Long luna= PrietenieValidator.validLong(lunaStr);
        myExceptionU.existId(id);

        prieteni.stream()
                .filter(x->x.getId().getLeft()==id && x.getDate().getMonthValue()==luna)
                .forEach(x-> System.out.println(repou.findOne(x.getId().getRight()).getFirstName() + " " +
                        repou.findOne(x.getId().getRight()).getLastName()+ " " +
                        x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))));

        prieteni.stream()
                .filter(x->x.getId().getRight()==id && x.getDate().getMonthValue()==luna)
                .forEach(x-> System.out.println(repou.findOne(x.getId().getLeft()).getFirstName() + " " +
                        repou.findOne(x.getId().getLeft()).getLastName()+ " " +
                        x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))));

        //return prieteni;
    }

    public List<Utilizator> prieteniUtilizator1(String idStr){
        List<Prietenie> prieteni = getAll();
        Long id= PrietenieValidator.validLong(idStr);
        myExceptionU.existId(id);

        List<Utilizator> utilizator=new ArrayList<Utilizator>();

        prieteni.stream()
                .filter(x->x.getId().getLeft()==id)
                .forEach(x-> utilizator.add(repou.findOne(x.getId().getRight())));

        prieteni.stream()
                .filter(x->x.getId().getRight()==id)
                .forEach(x-> utilizator.add(repou.findOne(x.getId().getLeft())));

        return utilizator;
    }

    public List<Utilizator> nonPrieteniUtilizator(String idStr){
        List<Prietenie> prieteni = getAll();
        Long id= PrietenieValidator.validLong(idStr);
        myExceptionU.existId(id);

        List<Utilizator> utilizator=new ArrayList<Utilizator>();

        prieteni.stream()
                .filter(x->x.getId().getLeft()==id)
                .forEach(x-> utilizator.add(repou.findOne(x.getId().getRight())));

        prieteni.stream()
                .filter(x->x.getId().getRight()==id)
                .forEach(x-> utilizator.add(repou.findOne(x.getId().getLeft())));

        List<Utilizator> nonPrieteni=new ArrayList<Utilizator>();
        List<Utilizator> utilizatori=StreamSupport.stream(repou.findAll().spliterator(), false).collect(Collectors.toList());

        boolean a;
        for(Utilizator u:utilizatori){
            a=true;
           for(Utilizator j:utilizator){
               if(u.getId()==j.getId() || u.getId()==id)
                   a=false;
           }
           if(a==true){
               nonPrieteni.add(u);
           }
       }


        return nonPrieteni;
    }

    private List<Observer<UtilizatorChangeEvent>> observerList=new ArrayList<>();

    @Override
    public void addObserver(Observer<UtilizatorChangeEvent> e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer<UtilizatorChangeEvent> e) {
        //observerList.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorChangeEvent t) {
        observerList.stream()
                .forEach(x->x.update(t));
    }
}
