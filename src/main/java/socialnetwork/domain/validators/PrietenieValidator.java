package socialnetwork.domain.validators;

import socialnetwork.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie>{

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getId().getRight()==entity.getId().getLeft()){
            throw new ValidationException("Users must be different!");

        }

    }
    public static Long validLong(String nr){
        try {
            return Long.parseLong(nr);
        }catch (NumberFormatException ex){
            throw new ValidationException(nr +"is not a valid number. ");
        }
    }
}
