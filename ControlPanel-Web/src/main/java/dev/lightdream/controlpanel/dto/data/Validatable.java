package dev.lightdream.controlpanel.dto.data;

import dev.lightdream.controlpanel.dto.data.annotation.Validate;
import dev.lightdream.logger.Logger;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Validatable {

    private final List<Field> invalid = new ArrayList<>();

    @SneakyThrows
    public boolean validate() {
        boolean valid = true;

        for (Field field : getClass().getFields()) {
            if (!isValid(field)) {
                valid = false;
                invalid.add(field);
            }
        }
        return valid;
    }

    public List<String> getInvalidFields() {
        if (invalid.isEmpty()) {
            validate();
        }

        List<String> invalidFields = new ArrayList<>();
        for (Field field : invalid) {
            invalidFields.add(field.getName());
        }
        return invalidFields;
    }

    public boolean isValid(Field field) {
        if (field.isAnnotationPresent(Validate.class)) {

            Validate validateAnnotation = field.getAnnotation(Validate.class);
            String validateMethod = validateAnnotation.validateMethod();

            if (field.getType().equals(String.class)) {
                if (!validateString(field, validateAnnotation.emptyAllowed())) {
                    return false;
                }
            }

            if (validateMethod.equals("")) {
                return true;
            }

            Method method;
            try {
                method = getClass().getMethod(validateMethod);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Logger.error("Returning false in validate function. See the error above");
                return false;
            }

            Object resultObj;
            try {
                resultObj = method.invoke(this);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Logger.error("Returning false in validate function. See the error above");
                return false;
            }
            if (!(resultObj instanceof Boolean)) {
                Logger.error("Validate method must return boolean");
                return false;
            }

            return (Boolean) resultObj;
        }
        return true;
    }

    private boolean _validateString(String name, boolean emptyAllowed) {
        if(!emptyAllowed) {
            return name != null && !name.isEmpty();
        }
        return name != null;
    }

    @SneakyThrows
    public boolean validateString(Field field, boolean emptyAllowed) {
        if (!field.getType().equals(String.class)) {
            return false;
        }

        return _validateString((String) field.get(this), emptyAllowed);
    }

}
