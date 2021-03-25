package socialnetwork.repository.file;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class PrietenieFile extends AbstractFileRepository<Tuple<Long, Long>, Prietenie> {


    public PrietenieFile(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
    }

    @Override
    public Prietenie extractEntity(List<String> attributes) {
        Long id1=Long.parseLong(attributes.get(0));
        Long id2=Long.parseLong(attributes.get(1));
        LocalDateTime date =LocalDateTime.parse(attributes.get(2));
        Prietenie prietenie = new Prietenie(new Tuple<>(id1,id2));
        prietenie.setDate(date);
        return prietenie;
        //return null;
    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getDate();
    }
}
