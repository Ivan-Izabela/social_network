package socialnetwork.domain.validators;

import socialnetwork.domain.Cerere;

public class CerereValidator implements Validator<Cerere> {
    @Override
    public void validate(Cerere entity) throws ValidationException {
        if(entity.getId().getRight()==entity.getId().getLeft()) {
            throw new ValidationException("Users must be different!");
        }
    }

    public static Long validLong(String nr) {
        try {
            return Long.parseLong(nr);
        } catch (NumberFormatException ex) {
            throw new ValidationException(nr + "is not a valid number. ");
        }
    }
}
