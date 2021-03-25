package socialnetwork.service;

import socialnetwork.domain.Mesaj;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.MesajValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exception.MyException;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MesajService implements Observable<UtilizatorChangeEvent> {
    private  Repository<Long, Mesaj> repo;
    private Repository<Tuple<Long,Long>, Prietenie> repoP;
    private Repository<Long, Utilizator> repoU;
    private MyException<Tuple<Long,Long>, Prietenie> myExceptionP;
    private MyException<Long,Utilizator> myExceptionU;
    private MyException<Long,Mesaj> myExceptionM;

    public MesajService(Repository<Long, Mesaj> repo, Repository<Tuple<Long, Long>, Prietenie> repoP, Repository<Long, Utilizator> repoU) {
        this.repo = repo;
        this.repoP = repoP;
        this.repoU = repoU;
        myExceptionP=new MyException<>(repoP);
        myExceptionU=new MyException<>(repoU);
        myExceptionM=new MyException<>(repo);
    }
    public List<Mesaj> getAll(){
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Mesaj trimiteMesaj(String idMsg,String from, List<String> to, String mesaj){
        Long id = MesajValidator.validLong(idMsg);
        Long idFrom =MesajValidator.validLong(from);

        myExceptionU.existId(idFrom);
        List<Long> list= new ArrayList<Long>();
        to.forEach(x->myExceptionU.existId(MesajValidator.validLong(x)));
        to.forEach(x->list.add(Long.parseLong(x)));

        Mesaj mesaj1=new Mesaj(idFrom,list,mesaj);
        mesaj1.setId(id);
        Mesaj task =repo.save(mesaj1);
        if(task==null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD,repoU.findOne(id)));
        }
        return task;

    }

    public Mesaj deleteMessage(String idStr){
        Long id = MesajValidator.validLong(idStr);
        myExceptionM.existId(id);
        Mesaj mesaj=repo.findOne(id);
        Mesaj task=repo.delete(id);
        if(task==null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.DELETE,repoU.findOne(mesaj.getFrom())));
        }
        return task;
    }

    public void conversatie(String id1Str, String id2Str){
        Long id1 =MesajValidator.validLong(id1Str);
        Long id2 =MesajValidator.validLong(id2Str);
        myExceptionU.existId(id1);
        myExceptionU.existId(id2);

        List<Mesaj> mesaje = getAll();

        List<Mesaj>  mesaje1= mesaje.stream()
                .filter(x->x.getFrom()==id1)
                .filter(x->x.getTo().contains(id2))
                .collect(Collectors.toList());

        List<Mesaj> mesaje2 =mesaje.stream()
                .filter(x->x.getFrom()==id2)
                .filter(x->x.getTo().contains(id1))
                .collect(Collectors.toList());

        mesaje2.forEach(x->mesaje1.add(x));

        mesaje1.stream()
                .sorted((x,y)->x.getDate().compareTo(y.getDate()))
                .forEach(x-> System.out.println(x.getMessage()));

    }

    private List<Observer<UtilizatorChangeEvent>> observerList=new ArrayList<>();

    @Override
    public void addObserver(Observer<UtilizatorChangeEvent> e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer<UtilizatorChangeEvent> e) {
        observerList.remove(e);

    }

    @Override
    public void notifyObservers(UtilizatorChangeEvent t) {
        observerList.stream()
                .forEach(x->x.update(t));
    }

    public List<String> conversatie1(String id1Str, String id2Str){
        Long id1 =MesajValidator.validLong(id1Str);
        Long id2 =MesajValidator.validLong(id2Str);
        myExceptionU.existId(id1);
        myExceptionU.existId(id2);

        List<Mesaj> mesaje = getAll();
        List<String> conversatie=new ArrayList<>();

        List<Mesaj>  mesaje1= mesaje.stream()
                .filter(x->x.getFrom()==id1)
                .filter(x->x.getTo().contains(id2))
                .collect(Collectors.toList());

        List<Mesaj> mesaje2 =mesaje.stream()
                .filter(x->x.getFrom()==id2)
                .filter(x->x.getTo().contains(id1))
                .collect(Collectors.toList());

        mesaje2.forEach(x->mesaje1.add(x));

        mesaje1.stream()
                .sorted((x,y)->x.getDate().compareTo(y.getDate()))
                .forEach(x-> conversatie.add(repoU.findOne(x.getFrom()).getFirstName()+" "+repoU.findOne(x.getFrom()).getLastName()
                +": "+x.getMessage()));

        return conversatie;

    }

    public Long getLastId(){
        List<Mesaj> mesaje=getAll();
        Long last=0L;
        for(Mesaj msg:mesaje){
            last++;
        }
        return last+1;
    }


}
