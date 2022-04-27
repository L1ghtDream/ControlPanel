package dev.lightdream.controlpanel.dto.data;

import dev.lightdream.logger.Logger;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
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

    @SneakyThrows
    public boolean isValid(Field field) {
        if (field.isAnnotationPresent(Validate.class)) {
            String validateMethod = field.getAnnotation(Validate.class).validateMethod();
            Method method = getClass().getMethod(validateMethod);
            Object resultObj = method.invoke(this);
            if (!(resultObj instanceof Boolean)) {
                Logger.error("Validate method must return boolean");
                return false;
            }

            return (Boolean) resultObj;
        }
        return true;
    }

}
