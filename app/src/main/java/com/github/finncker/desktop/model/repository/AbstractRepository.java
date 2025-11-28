package com.github.finncker.desktop.model.repository;

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
    protected File file;

    public AbstractRepository(String filename) {
        this.file = new File(filename);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            // OBS: [MUDAR] Criar uma exceção personalizada aqui
            System.out.println("Erro ao criar o arquivo.");
        }
    }

    protected abstract String getId(T entity);

    protected abstract boolean matchId(T entity, String id);

    protected List<T> readAll() {
        List<T> list = new ArrayList<>();
        ObjectInputStream ois = null;

        try (FileInputStream fis = new FileInputStream(file)) {
            while (fis.available() > 0) {
                ois = new ObjectInputStream(fis);
                T obj = (T) ois.readObject();
                list.add(obj);
            }
        } catch (Exception e) {
            // OBS: [MUDAR] Criar uma exceção personalizada aqui
            System.out.println("Erro ao ler objetos: " + e.getMessage());
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                // OBS: [MUDAR] Criar uma exceção personalizada aqui
                System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
            }
        }

        return list;
    }

    protected void writeAll(List<T> list) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (T obj : list) {
                oos.writeObject(obj);
            }

            oos.close();
        } catch (Exception e) {
            // OBS: [MUDAR] Criar uma exceção personalizada aqui
            System.out.println("Erro ao atualizar arquivo: " + e.getMessage());
        }
    }

    @Override
    public T create(T entity) {
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(entity);
            oos.close();
        } catch (Exception e) {
            // OBS: [MUDAR] Criar uma exceção personalizada aqui
            System.out.println("Erro ao criar entidade: " + e.getMessage());
        }

        return entity;
    }

    @Override
    public T read(String id) {
        List<T> list = readAll();

        for (T entity : list) {
            if (matchId(entity, id)) {
                return entity;
            }
        }

        return null;
    }

    @Override
    public T update(T updateEntity) {
        List<T> list = readAll();
        String id = getId(updateEntity);

        for (int i = 0; i < list.size(); i++) {
            if (matchId(list.get(i), id)) {
                list.set(i, updateEntity);
                writeAll(list);
                return updateEntity;
            }
        }

        return null;
    }

    @Override
    public boolean delete(String id) {
        List<T> list = readAll();
        
        boolean removed = list.removeIf(e -> matchId(e, id));

        if (removed) {
            writeAll(list);
        }

        return removed;
    }

}
