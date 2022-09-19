package com.edu.ulab.app.storage;

public interface ProjectRepository<T> {

    T store(T item); // сохранение

    T find(long id); //поиск

    void removeItemById(long id);

    T update(T item, long id);
}