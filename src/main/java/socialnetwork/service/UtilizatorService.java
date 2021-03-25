package socialnetwork.service;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exception.MyException;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UtilizatorChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class UtilizatorService implements Observable<UtilizatorChangeEvent> {
    private Repository<Long, Utilizator> repo;
    private MyException<Long,Utilizator> myException;

    public UtilizatorService(Repository<Long, Utilizator> repo) {
        this.repo = repo;
        myException=new MyException<>(repo);
    }

    public Utilizator addUtilizator(String id, String firstname,String lastname) {
        Utilizator messageTask=new Utilizator(firstname,lastname);
        Long id1= UtilizatorValidator.validLong(id);
        messageTask.setId(id1);
        myException.is(messageTask);
        Utilizator task = repo.save(messageTask);
        if(task==null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD,task));
        }
        return task;

    }
    public Utilizator deleteUtilizator(String id){
        Long id1= UtilizatorValidator.validLong(id);
        myException.existId(id1);
        Utilizator task=repo.delete(id1);
        if(task!=null){
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.DELETE,task));
        }
        return task;

    }


    public Iterable<Utilizator> getAll(){
        return repo.findAll();
    }

    public Utilizator getOne(Long id){
        myException.existId(id);
        return repo.findOne(id);
    }

    private List<Observer<UtilizatorChangeEvent>> observerList=new ArrayList<>();

    @Override
    public void addObserver(Observer<UtilizatorChangeEvent> e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer<UtilizatorChangeEvent> e) {
       // observerList.remove(e);

    }

    @Override
    public void notifyObservers(UtilizatorChangeEvent t) {
        observerList.stream()
                .forEach(x->x.update(t));
    }

    ///TO DO: add other methods
}
