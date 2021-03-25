package socialnetwork.domain.validators;

import socialnetwork.domain.Mesaj;

public class MesajValidator implements Validator<Mesaj> {
    @Override
    public void validate(Mesaj entity) throws ValidationException {
        if(entity.getId()<0) throw  new ValidationException("Id  can`t be negativ");
        if(entity.getFrom()<0) throw  new ValidationException("Id can`t be negativ");
        for(Long x:entity.getTo()){
            if(x<0) throw  new ValidationException("Id can`t be negativ");
        }
        if(entity.getMessage().length()<=1) throw new ValidationException("Mesagge can`t be null");
    }
    public static Long validLong(String nr) {
        try {
            return Long.parseLong(nr);
        } catch (NumberFormatException ex) {
            throw new ValidationException(nr + "is not a valid number. ");
        }
    }
}
