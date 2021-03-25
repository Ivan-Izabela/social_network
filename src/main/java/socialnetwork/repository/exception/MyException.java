package socialnetwork.repository.exception;

import socialnetwork.domain.Entity;
import socialnetwork.repository.Repository;

public class MyException<ID,E extends Entity<ID>> {
    private Repository<ID,E> repo;

    public MyException(Repository<ID, E> repo) {
        this.repo = repo;
    }

    public void is(E entity){
        if(repo.findOne(entity.getId())!=null) throw new Exception("Already exist");

    }
    public void existId(ID id){
        if(repo.findOne(id)==null) throw new Exception("Id doesn`n exist");
    }
}
