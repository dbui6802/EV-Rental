package com.webserver.evrentalsystem.exception.store;

import com.webserver.evrentalsystem.entity.Error;
import com.webserver.evrentalsystem.entity.ErrorStatus;
import com.webserver.evrentalsystem.repository.ErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorSaver {

    @Autowired
    private ErrorRepository errorRepository;

    public void saveErrorToDatabase(Exception e) {
        try {
            String message = e.getMessage();
            if (message == null) {
                return;
            }

            Error error = errorRepository.findByErrorMessage(message);

            if (error == null) {
                // create new
                Error newError = new Error();
                newError.setCount(1);
                newError.setMessage(message);
                newError.setStatus(ErrorStatus.OPENED);
                newError.setLastTimeOccurred(System.currentTimeMillis());
                newError.setCreatedAt(System.currentTimeMillis());
                errorRepository.save(newError);
            } else {
                if (error.getStatus() == ErrorStatus.CLOSED) {
                    error.setStatus(ErrorStatus.REOPENED);
                }
                error.setCount(error.getCount() + 1);
                error.setLastTimeOccurred(System.currentTimeMillis());
                errorRepository.save(error);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
