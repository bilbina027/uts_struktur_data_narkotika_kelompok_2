package com.pengadilan.app.util;

public interface StrukturData<T> {
    void add(T value);
    T get(int index);
    T remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(T value);
    void clear();
}