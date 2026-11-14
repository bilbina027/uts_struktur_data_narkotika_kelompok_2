package com.pengadilan.app.util;

public final class CustomStack<T> {
    private Object[] data;
    private int size;

    public CustomStack() {
        this(10);
    }

    public CustomStack(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Kapasitas minimal 1.");
        }
        data = new Object[capacity];
    }

    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Elemen tidak boleh null.");
        }
        if (size == data.length) {
            long capacity = (long) data.length * 2;
            if (capacity > Integer.MAX_VALUE - 8L) {
                throw new IllegalStateException("Kapasitas terlalu besar.");
            }
            Object[] replacement = new Object[(int) capacity];
            for (int i = 0; i < size; i++) replacement[i] = data[i];
            data = replacement;
        }
        data[size++] = value;
    }

    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Stack kosong.");
        return getFromTop(0);
    }

    public T pop() {
        T result = peek();
        data[--size] = null;
        return result;
    }

    @SuppressWarnings("unchecked")
    public T getFromTop(int offset) {
        if (offset < 0 || offset >= size) {
            throw new IndexOutOfBoundsException("Offset tidak valid.");
        }
        return (T) data[size - 1 - offset];
    }

    public boolean searchPreserve(T value) {
        if (value == null) return false;
        for (int i = size - 1; i >= 0; i--) {
            if (value.equals(data[i]))
                return true;
        }
        return false;
    }

    public void restore(CustomStack<T> temporary) {
        if (temporary == null || temporary == this) {
            throw new IllegalArgumentException(
                    "Stack sementara harus berbeda dan tidak null.");
        }
        while (!temporary.isEmpty()) push(temporary.pop());
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        while (!isEmpty()) pop();
    }

    public boolean validateInvariant() {
        if (data == null || size < 0 || size > data.length) return false;
        for (int i = 0; i < size; i++) {
            if (data[i] == null) return false;
        }
        for (int i = size; i < data.length; i++) {
            if (data[i] != null) return false;
        }
        return true;
    }
}