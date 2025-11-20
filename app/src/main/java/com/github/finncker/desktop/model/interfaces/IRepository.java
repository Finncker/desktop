package com.github.finncker.desktop.model.interfaces;

public interface IRepository<T> {
    public T create(T entity);

    public T read(String id);

    public T update(T entity);

    public T delete(T entity);
}
