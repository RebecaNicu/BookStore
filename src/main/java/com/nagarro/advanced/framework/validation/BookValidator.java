package com.nagarro.advanced.framework.validation;

import com.nagarro.advanced.framework.persistence.entity.Book;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookValidator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "title", "title.empty");
        ValidationUtils.rejectIfEmpty(errors, "author", "author.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "isbn.required");
        Book book = (Book) object;
        if (book.getPrice().compareTo(BigDecimal.ZERO) < 0){
            errors.rejectValue("price", "price.negativevalue");
        }
    }

    public List<String> getErrorMessage(Object object) {
        DataBinder dataBinder = new DataBinder(object);
        dataBinder.addValidators(new BookValidator());
        dataBinder.validate();

        List<String> errorMessages = new ArrayList<>();
        if (dataBinder.getBindingResult().hasErrors()) {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("ValidationMessages");
            messageSource.getMessage("object.invalid", null, Locale.US);

            dataBinder.getBindingResult().getAllErrors()
                    .forEach(e -> errorMessages.add(messageSource.getMessage(e, Locale.US)));
        }
        return errorMessages;
    }
}
