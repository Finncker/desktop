package com.github.finncker.desktop.model.repository;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.finncker.desktop.model.interfaces.IRepository;

public abstract class AbstractRepository<T extends Serializable> implements IRepository<T> {

    private final String dataFile;

    public AbstractRepository(String dataFile) {
        this.dataFile = dataFile;

        File f = new File(dataFile);

        if (!f.exists()) {
            try {
                f.createNewFile();

                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
                    out.writeObject(new ArrayList<T>());
                }
            } catch (IOException e) {
                throw new RuntimeException("Erro ao inicializar arquivo de dados: " + dataFile, e);
            }
        }
    }

    protected abstract String getId(T entity);

    protected abstract boolean matchId(T entity, String id);

    @SuppressWarnings("unchecked")
    protected List<T> readAll() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (List<T>) in.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler lista do repositório: " + dataFile, e);
        }
    }

    protected void writeAll(List<T> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(list);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar lista no arquivo: " + dataFile, e);
        }
    }

    @Override
    public T create(T entity) {
        List<T> list = readAll();
        list.add(entity);
        writeAll(list);
        return entity;
    }

    @Override
    public T read(String id) {
        return readAll().stream()
                .filter(e -> matchId(e, id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public T update(T entity) {
        List<T> list = readAll();
        String id = getId(entity);

        for (int i = 0; i < list.size(); i++) {
            if (matchId(list.get(i), id)) {
                list.set(i, entity);
                writeAll(list);
                return entity;
            }
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        List<T> list = readAll();
        boolean removed = list.removeIf(e -> matchId(e, id));

        if (removed)
            writeAll(list);

        return removed;
    }
}
