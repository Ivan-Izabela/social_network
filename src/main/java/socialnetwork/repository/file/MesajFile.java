package socialnetwork.repository.file;

import socialnetwork.domain.Mesaj;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MesajFile extends AbstractFileRepository<Long, Mesaj>{
    public MesajFile(String fileName, Validator<Mesaj> validator) {
        super(fileName, validator);
    }

    @Override
    public Mesaj extractEntity(List<String> attributes) {
        Long id=Long.parseLong(attributes.get(0));
        Long idFrom=Long.parseLong(attributes.get(1));
        List<Long> list =new  ArrayList<Long>();
        for(int i=0;i<attributes.get(2).length()-1;i++){
            if(i%2==1){
                list.add(Long.parseLong(String.valueOf(attributes.get(2).charAt(i))));

            }
        }
        LocalDateTime date =LocalDateTime.parse(attributes.get(4));
        Mesaj mesaj= new Mesaj(idFrom,list,attributes.get(3));
        mesaj.setId(id);
        mesaj.setDate(date);
        return mesaj;
    }

    @Override
    protected String createEntityAsString(Mesaj entity) {

        return entity.getId()+";"+entity.getFrom()+";"+entity.getTo()+";"+entity.getMessage()+";"+entity.getDate();
    }
}
