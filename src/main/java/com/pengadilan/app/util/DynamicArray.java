package com.pengadilan.app.util;
public class DynamicArray<T> implements StrukturData<T> {
    private Object[] data;
    private int size;
    public DynamicArray() {
        this.data = new Object[10];
        this.size = 0;
    }
    public DynamicArray(int capacity) {
        this.data = new Object[Math.max(capacity, 10)];
        this.size = 0;
    }
    @Override
    public void add(T value) {
        ensureCapacity(size + 1);
        data[size++] = value;
    }
    public void insert(int position, T value) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > position; i--) {
            data[i] = data[i - 1];
        }
        data[position] = value;
        size++;
    }
    @Override
    @SuppressWarnings("unchecked")
    public T get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + size);
        }
        return (T) data[position];
    }
    public void set(int position, T value) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + size);
        }
        data[position] = value;
    }
    @Override
    @SuppressWarnings("unchecked")
    public T remove(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + size);
            T removed = (T) data[position];
            for (int i = position; i < size - 1; i++) {
                data[i] = data[i + 1];
            }
            data[size - 1] = null;
            size--;
            return removed;
        }
        @Override
        public int size() {
            return size;
        }
        @Override
        public boolean isEmpty() {
            return size == 0;
        }
        @Override
        public boolean contains(T value) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null ? value == null : data[i].
                        equals(value)) {
                    return true;
                }
            }
            return false;
        }
        @Override
        public void clear() {
            for (int i = 0; i < size; i++) {
                data[i] = null;
            }
            size = 0
        }

        @Override
        public boolean validateInvariant() {
            if (data == null || size < 0 || size > data.lengt
            h) {
                return false;
            }
            for (int i = size; i < data.length; i++) {
                if (data[i] != null) return false;
            }
            return true;
        }
        private void ensureCapacity(int minCapacity) {
            if (minCapacity > data.length) {
                int newCapacity = Math.max(data.length * 2, mi
                        nCapacity);
                Object[] newData = new Object[newCapacity];
                System.arraycopy(data, 0, newData, 0, size);
                data = newData;
            }
        }
    }