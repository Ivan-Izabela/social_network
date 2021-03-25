package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        if(entity.getId()<0) throw  new ValidationException("Id can`t be negativ");
        if(entity.getFirstName().length()<=1) throw new ValidationException("First name can`t be null");
        if(entity.getLastName().length()<=1) throw new ValidationException("Last name can`t be null");
    }

    public static Long validLong(String nr){
        try {
            return Long.parseLong(nr);
        }catch (NumberFormatException ex){
            throw new ValidationException(nr +"is not a valid number. ");
        }
    }
}
