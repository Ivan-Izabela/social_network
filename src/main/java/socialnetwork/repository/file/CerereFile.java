package socialnetwork.repository.file;

import socialnetwork.domain.Cerere;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class CerereFile extends AbstractFileRepository<Tuple<Long, Long>, Cerere>{
    public CerereFile(String fileName, Validator<Cerere> validator) {
        super(fileName, validator);
    }

    @Override
    public Cerere extractEntity(List<String> attributes) {
        Long id1=Long.parseLong(attributes.get(0));
        Long id2=Long.parseLong(attributes.get(1));
        LocalDateTime date =LocalDateTime.parse(attributes.get(3));
        Cerere cerere = new Cerere(new Tuple<>(id1,id2));
        cerere.setStatus(attributes.get(2));
        cerere.setDate(date);
        return cerere;
    }

    @Override
    protected String createEntityAsString(Cerere entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getStatus()+";"+entity.getDate();
    }
}
