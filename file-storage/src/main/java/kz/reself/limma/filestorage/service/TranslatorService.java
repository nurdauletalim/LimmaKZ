package kz.reself.limma.filestorage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
@Log
@RequiredArgsConstructor
public class TranslatorService {

    private final ResourceBundleMessageSource messageSource;

    public String toLocale(String title, String [] args) {
        var noTranslation = "NO_TRANSLATION";
        try {
            return messageSource.getMessage(title, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException ex) {
            log.info(String.format("No translation provided for %s", title));
        }
        return noTranslation;
    }
}
