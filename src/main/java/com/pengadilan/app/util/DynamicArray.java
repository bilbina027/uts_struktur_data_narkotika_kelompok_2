package com.pengadilan.app.util;

public final class DynamicArray<T> implements StrukturData<T> {
    private Object[] data;
    private int size;

    public DynamicArray() { this(10); }

    public DynamicArray(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Kapasitas minimal 1.");
        }
        data = new Object[capacity];
    }

    private void requireValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Elemen tidak boleh null.");
        }
    }

    private void checkElement(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indeks " + index + ", size " + size);
        }
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1 || newCapacity < size) {
            throw new IllegalArgumentException("Kapasitas baru tidak boleh lebih kecil dari size atau minimal 1.");
        }
        if (newCapacity == data.length) return;
        Object[] replacement = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            replacement[i] = data[i];
        }
        data = replacement;
    }

    private void grow() {
        if (size < data.length) return;
        long capacity = (long) data.length * 2;
        if (capacity > Integer.MAX_VALUE) {
            throw new IllegalStateException("Kapasitas terlalu besar.");
        }
        resize((int) capacity);
    }

    @Override
    public void add(T value) {
        requireValue(value);
        grow();
        data[size++] = value;
    }

    public void insert(int index, T value) {
        requireValue(value);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Posisi sisip tidak valid.");
        }
        grow();
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkElement(index);
        return (T) data[index];
    }

    public void set(int index, T value) {
        checkElement(index);
        requireValue(value);
        data[index] = value;
    }

    @Override
    public T remove(int index) {
        T removed = get(index);
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
        return removed;
    }

    public int search(T value) {
        if (value == null) return -1;
        for (int i = 0; i < size; i++) {
            if (value.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(T value) { return search(value) >= 0; }

    @Override
    public int size() { return size; }

    public int capacity() { return data.length; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    public DynamicArray<T> copy() {
        DynamicArray<T> copy = new DynamicArray<>(Math.max(1, size));
        for (int i = 0; i < size; i++) copy.add(get(i));
        return copy;
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