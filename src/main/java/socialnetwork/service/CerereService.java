package socialnetwork.service;

import socialnetwork.domain.Cerere;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.CerereValidator;
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

public class CerereService implements Observable<UtilizatorChangeEvent> {
    private Repository<Tuple<Long,Long>, Cerere> repo;
    private Repository<Tuple<Long,Long>, Prietenie> repoP;
    private PrietenieService prietenieService;
    private Repository<Long, Utilizator> repoU;
    private MyException<Tuple<Long,Long>, Cerere> myExceptionC;
    private MyException<Tuple<Long,Long>, Prietenie> myExceptionP;
    private MyException<Long,Utilizator> myExceptionU;

    public CerereService(Repository<Tuple<Long, Long>, Cerere> repo, Repository<Tuple<Long, Long>, Prietenie> repoP, Repository<Long, Utilizator> repoU) {
        this.repo = repo;
        this.repoP = repoP;
        this.repoU = repoU;
        myExceptionP=new MyException<>(repoP);
        myExceptionU=new MyException<>(repoU);
        myExceptionC=new MyException<>(repo);
        prietenieService=new PrietenieService(repoP,repoU);
    }

    public Cerere getOne(Long id1,Long id2){
        return repo.findOne(new Tuple<>(id1,id2));
    }

    public List<Cerere> getAll(){
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Cerere trimiteCerere(String id1,String id2){
        Long idFrom= CerereValidator.validLong(id1);
        Long idTo= CerereValidator.validLong(id2);
        myExceptionU.existId(idFrom);
        myExceptionU.existId(idTo);
        Cerere cerere=new Cerere(new Tuple<>(idFrom,idTo));
        Cerere task =repo.save(cerere);
        if(task == null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD,repoU.findOne(idFrom)));
        }
        return task;
    }

    public Cerere deleteCerere(String id1,String id2){
        Long idFrom= CerereValidator.validLong(id1);
        Long idTo= CerereValidator.validLong(id2);
        Cerere cerere=new Cerere(new Tuple<>(idFrom,idTo));
        Cerere task=repo.delete(cerere.getId());
        if(task == null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.DELETE,repoU.findOne(idFrom)));
        }
        return task;


    }

    public void accepta_respinge(String id1,String id2, String status){
        Long idFrom= CerereValidator.validLong(id1);
        Long idTo= CerereValidator.validLong(id2);
        myExceptionU.existId(idFrom);
        myExceptionU.existId(idTo);

        Prietenie p=new Prietenie(new Tuple<>(idFrom,idTo));
        myExceptionP.is(p);

        Cerere cerere=repo.findOne(new Tuple<>(idFrom,idTo));
        cerere.setStatus(status);
        if(status.equals("approved")){
            prietenieService.addPrietenie(p.getId().getLeft().toString(),p.getId().getRight().toString());

        }
        notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD,repoU.findOne(idFrom)));
        repo.delete(cerere.getId());
        repo.save(cerere);
    }

    public void cereriUtilizator(String idStr){
        Long id= CerereValidator.validLong(idStr);
        myExceptionU.existId(id);
        //System.out.println("Request: ");
        getAll().stream()
                .filter(x->x.getId().getRight()==id)
                .forEach(x-> System.out.println(x.getId().getLeft()+" "+ x.getStatus()));


    }
    public List<Utilizator> cereriUtilizator1(String idStr){
        Long id= CerereValidator.validLong(idStr);
        myExceptionU.existId(id);
        //System.out.println("Request: ");
        List<Utilizator> utilizatori=new ArrayList<Utilizator>();
        getAll().stream()
                .filter(x->x.getId().getRight()==id)
                .forEach(x->utilizatori.add(repoU.findOne(x.getId().getLeft())));
        return utilizatori;


    }

    public List<Utilizator> cereriUtilizator2(String idStr){
        Long id= CerereValidator.validLong(idStr);
        myExceptionU.existId(id);
        //System.out.println("Request: ");
        List<Utilizator> utilizatori=new ArrayList<Utilizator>();
        getAll().stream()
                .filter(x->x.getId().getLeft()==id)
                .forEach(x->utilizatori.add(repoU.findOne(x.getId().getRight())));
        return utilizatori;


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
