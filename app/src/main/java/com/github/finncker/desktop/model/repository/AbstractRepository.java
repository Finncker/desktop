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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRepository<T extends Serializable> implements IRepository<T> {

    private final String dataFile;

    public AbstractRepository(String dataFile) {
        this.dataFile = dataFile;

        File f = new File(dataFile);

        if (!f.exists()) {
            try {
                log.info("Arquivo '{}' não existe. Criando novo arquivo...", dataFile);
                f.createNewFile();

                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
                    out.writeObject(new ArrayList<T>());
                }

                log.info("Arquivo '{}' inicializado com lista vazia.", dataFile);
            } catch (IOException e) {
                log.error("Erro ao inicializar arquivo '{}': {}", dataFile, e.getMessage());
                throw new RuntimeException("Erro ao inicializar arquivo de dados: " + dataFile, e);
            }
        } else {
            log.info("Repositório carregado: {}", dataFile);
        }
    }

    protected abstract String getId(T entity);

    protected abstract boolean matchId(T entity, String id);

    @SuppressWarnings("unchecked")
    protected List<T> readAll() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            List<T> list = (List<T>) in.readObject();
            
            log.info("Arquivo '{}' carregado. {} registros encontrados.", dataFile, list.size());
            return list;
        } catch (EOFException e) {
            log.warn("Arquivo '{}' está vazio. Retornando lista vazia.", dataFile);
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Erro ao ler arquivo '", dataFile, "': ", e.getMessage());
            throw new RuntimeException("Erro ao ler lista do repositório: " + dataFile, e);
        }
    }

    protected void writeAll(List<T> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(list);
            log.info("Arquivo '{}' atualizado com {} registros.", dataFile, list.size());
        } catch (Exception e) {
            log.error("Erro ao salvar arquivo '", dataFile, "': ", e.getMessage());
            throw new RuntimeException("Erro ao salvar lista no arquivo: " + dataFile, e);
        }
    }

    @Override
    public T create(T entity) {
        log.info("Criando novo registro: {}", getId(entity));

        List<T> list = readAll();
        list.add(entity);
        writeAll(list);

        return entity;
    }

    @Override
    public T read(String id) {
        log.info("Lendo registro com id = '{}'", id);

        return readAll().stream()
                .filter(e -> matchId(e, id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public T update(T entity) {
        String id = getId(entity);
 
        log.info("Atualizando registro com id = '{}'", id);
 
        List<T> list = readAll();

        for (int i = 0; i < list.size(); i++) {
            if (matchId(list.get(i), id)) {
                list.set(i, entity);
                writeAll(list);

                log.info("Registro '{}' atualizado com sucesso.", id);
                return entity;
            }
        }

        log.warn("Falha ao atualizar registro. id = '{}' não encontrado.", id);
        return null;
    }

    @Override
    public boolean delete(String id) {
        log.info("Deletando registro com id = '{}'", id);

        List<T> list = readAll();
        boolean removed = list.removeIf(e -> matchId(e, id));

        if (removed) {
            writeAll(list);
            log.info("Registro '{}' removido com sucesso.", id);
        } else {
            log.warn("Falha ao remover. id = '{}' não encontrado.", id);
        }

        return removed;
    }
}
