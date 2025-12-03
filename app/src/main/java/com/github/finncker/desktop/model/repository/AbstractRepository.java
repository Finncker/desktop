package com.github.finncker.desktop.model.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRepository {

    private final String FILENAME = "database.ser";
    private static User dataState;

    public AbstractRepository() {
        loadState();
    }

    private void loadState() {
        if (dataState == null) {
            try (FileInputStream fis = new FileInputStream(FILENAME);
                    ObjectInputStream ois = new ObjectInputStream(fis);) {
                dataState = (User) ois.readObject();
            } catch (FileNotFoundException fnfe) {
                log.warn("Arquivo {} não encontrado.", FILENAME);
            } catch (IOException ioe) {
                log.warn("Erro de IO: {}.", ioe);
            } catch (ClassNotFoundException cnfe) {
                log.warn("Classe User não encontrada em {}.", FILENAME);
            }
        }
    }

    private void writeState() {
        if (dataState != null) {
            try (FileOutputStream fos = new FileOutputStream(FILENAME);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);) {
                oos.writeObject(dataState);
            } catch (FileNotFoundException fnfe) {
                log.warn("Arquivo {} não encontrado.", FILENAME);
            } catch (IOException ioe) {
                log.warn("Erro de IO: {}.", ioe);
            }
        }
    }

    protected User getUser() throws UserNotFoundException {
        if (dataState == null) {
            throw new UserNotFoundException();
        }

        return dataState;
    }

    protected void setUser(User user) {
        dataState = user;
        writeState();
    }
}