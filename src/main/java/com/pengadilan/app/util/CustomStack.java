package com.pengadilan.app.util;

public class CustomStack<T> {
    private Object[] data;
    private int size;

    public CustomStack() {
        this.data = new Object[10];
        this.size = 0;
    }

    public void push(T value) {
        if (size >= data.length) {
            resize(data.length * 2);
        }
        data[size++] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong");
        }
        T val = (T) data[--size];
        data[size] = null;
        return val;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong");
        }
        return (T) data[size - 1];
    }

    @SuppressWarnings("unchecked")
    public T getFromTop(int offset) {
        if (offset < 0 || offset >= size) {
            throw new IndexOutOfBoundsException("Offset tidak valid: " + offset);
        }
        return (T) data[size - 1 - offset];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public boolean validateInvariant() {
        if (data == null || size < 0 || size > data.length) return false;
        for (int i = size; i < data.length; i++) {
            if (data[i] != null) return false;
        }
        return true;
    }

    private void resize(int newCapacity) {
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }
}