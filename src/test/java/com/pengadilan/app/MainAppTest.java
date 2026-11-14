package com.pengadilan.app;

import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.service.CsvService;
import com.pengadilan.app.service.DataManager;
import com.pengadilan.app.util.CustomStack;
import com.pengadilan.app.util.DynamicArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class MainAppTest {
    @TempDir
    Path directory;
    private final DataManager manager = DataManager.getInstance();

    private Putusan fixture(int index) {
        return new Putusan(
                "TEST-" + index,
                index + "/Pid.Sus/2024/PN TEST",
                index % 2 == 0 ? "PN Uji A" : "PN Uji B",
                LocalDate.of(2024, 1, 1),
                "T-UJI-" + index,
                index % 2 == 0 ? "Sabu" : "Ganja",
                index + 0.25,
                "Dakwaan pengujian",
                index % 2 == 0 ? "Pasal 112 ayat (1)" : null,
                72,
                index,
                index * 1000L,
                "Barang bukti pengujian",
                null,
                null,
                "FIXTURE TEST; bukan sumber putusan nyata"
        );
    }

    private DynamicArray<Putusan> dataset() {
        DynamicArray<Putusan> records = new DynamicArray<>();
        for (int index = 0; index < 50; index++) {
            records.add(fixture(index));
        }
        return records;
    }

    @BeforeEach
    void resetData() {
        manager.replaceAll(dataset(), "PN Uji");
    }

    @Test
    void dynamicArrayOperationsAndInvariant() {
        DynamicArray<String> array = new DynamicArray<>(1);
        array.add("A");
        array.add("C");
        array.insert(1, "B");
        assertEquals(3, array.size());
        assertTrue(array.capacity() >= 3);
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
        array.set(2, "D");
        assertEquals(2, array.search("D"));
        assertTrue(array.contains("B"));
        assertEquals(-1, array.search("TIDAK-ADA"));
        assertEquals("B", array.remove(1));
        assertEquals("D", array.get(1));
        assertEquals(2, array.size());
        assertTrue(array.validateInvariant());
        array.clear();
        assertEquals(0, array.size());
        assertTrue(array.isEmpty());
        assertTrue(array.validateInvariant());
    }

    @Test
    void dynamicArrayRejectsInvalidOperations() {
        DynamicArray<String> array = new DynamicArray<>();
        assertThrows(
                IllegalArgumentException.class,
                () -> new DynamicArray<String>(0)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.get(0)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.remove(-1)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.insert(1, "A")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> array.add(null)
        );
        array.insert(0, "A");
        array.insert(array.size(), "B");
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> array.set(2, "C")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> array.set(0, null)
        );
        assertTrue(array.validateInvariant());
    }

    @Test
    void stackPreservesOrderDuringSearchAndRestore() {
        CustomStack<String> stack = new CustomStack<>(1);
        stack.push("A");
        stack.push("B");
        stack.push("C");
        assertEquals(3, stack.size());
        assertEquals("C", stack.peek());
        assertTrue(stack.searchPreserve("B"));
        assertFalse(stack.searchPreserve("TIDAK-ADA"));
        assertEquals(3, stack.size());
        assertEquals("C", stack.getFromTop(0));
        assertEquals("B", stack.getFromTop(1));
        assertEquals("A", stack.getFromTop(2));
        CustomStack<String> temporary = new CustomStack<>();
        while (!stack.isEmpty()) {
            temporary.push(stack.pop());
        }
        stack.restore(temporary);
        assertTrue(temporary.isEmpty());
        assertEquals("C", stack.pop());
        assertEquals("B", stack.pop());
        assertEquals("A", stack.pop());
        assertTrue(stack.isEmpty());
        assertTrue(stack.validateInvariant());
        assertTrue(temporary.validateInvariant());
    }

    @Test
    void stackRejectsInvalidOperations() {
        CustomStack<String> stack = new CustomStack<>();
        assertThrows(
                IllegalStateException.class,
                stack::pop
        );
        assertThrows(
                IllegalStateException.class,
                stack::peek
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> stack.push(null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> stack.restore(stack)
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> stack.getFromTop(0)
        );
        assertTrue(stack.validateInvariant());
    }

    @Test
    void searchReportsCorrectIndicesAndSteps() {
        DataManager.SearchResult first =
                manager.cariPerkara("TEST-0");
        assertTrue(first.found());
        assertEquals(0, first.index());
        assertEquals(1, first.steps());
        DataManager.SearchResult last =
                manager.cariPerkara("TEST-49");
        assertTrue(last.found());
        assertEquals(49, last.index());
        assertEquals(50, last.steps());
        DataManager.SearchResult missing =
                manager.cariPerkara("TIDAK-ADA");
        assertFalse(missing.found());
        assertEquals(-1, missing.index());