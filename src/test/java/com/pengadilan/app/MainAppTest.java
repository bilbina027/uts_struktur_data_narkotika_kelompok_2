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

    /*
     * Data berikut hanya fixture pengujian.
     * Bukan dataset putusan nyata untuk pengumpulan tugas.
     */
    private Putusan fixture(int index) {
        return new Putusan(
            "TEST-" + index,
            index + "/Pid.Sus/2024/PN TEST",
            "PN Uji A", // Seluruh fixture menggunakan satu Pengadilan Negeri yang sama
            LocalDate.of(2024, 1, 1),
            "T-UJI-" + index,
            index % 2 == 0 ? "Sabu" : "Ganja",
            index + 0.25,
            "Dakwaan pengujian",
            index % 2 == 0 ? "Pasal 112 ayat (1)" : null,
            72,
            index,
            (long) index * 1000L,
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
        /*
         * DataManager menggunakan singleton.
         * Dataset utama direset sebelum setiap pengujian.
         */
        manager.replaceAll(dataset());
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
    void dynamicArrayExplicitResizeWorks() {
        DynamicArray<String> array = new DynamicArray<>(2);
        array.add("A");
        array.add("B");
        array.resize(5);
        assertEquals(5, array.capacity());
        assertEquals(2, array.size());
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertTrue(array.validateInvariant());
        // Resize ke bawah di bawah ukuran size harus ditolak
        assertThrows(IllegalArgumentException.class, () -> array.resize(1));
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
        assertEquals(50, missing.steps());
        assertNull(missing.putusan());
        DataManager.SearchResult number =
                manager.cariPerkara("49/Pid.Sus/2024/PN TEST");
        assertEquals(49, number.index());
        assertThrows(
            IllegalArgumentException.class,
            () -> manager.cariPerkara("")
        );
    }

    @Test
    void deleteFirstMiddleLastThenAddAgain() {
        assertTrue(manager.hapusPutusanByCaseId("TEST-0"));
        assertTrue(manager.hapusPutusanByCaseId("TEST-25"));
        assertTrue(manager.hapusPutusanByCaseId("TEST-49"));
        assertEquals(47, manager.size());
        DynamicArray<Putusan> remaining = manager.getDataPutusan();
        assertEquals("TEST-1", remaining.get(0).getCaseId());
        assertEquals(
            "TEST-48",
            remaining.get(remaining.size() - 1).getCaseId()
        );
        manager.tambahPutusan(fixture(0));
        manager.tambahPutusan(fixture(25));
        manager.tambahPutusan(fixture(49));
        assertEquals(50, manager.size());
        DynamicArray<Putusan> updated = manager.getDataPutusan();
        assertEquals("TEST-0", updated.get(47).getCaseId());
        assertEquals("TEST-25", updated.get(48).getCaseId());
        assertEquals("TEST-49", updated.get(49).getCaseId());
        assertTrue(manager.validateInvariant());
    }

    @Test
    void duplicateIdAndNumberAreRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> manager.tambahPutusan(fixture(0))
        );
        String[] fields = fixture(100).toFields();
        fields[1] = fixture(0).getNomorPerkara();
        Putusan duplicateNumber = Putusan.fromFields(fields);
        assertThrows(
            IllegalArgumentException.class,
            () -> manager.tambahPutusan(duplicateNumber)
        );
        assertEquals(50, manager.size());
        assertTrue(manager.validateInvariant());
    }

    @Test
    void snapshotDoesNotModifyMainStorage() {
        DynamicArray<Putusan> snapshot = manager.getDataPutusan();
        snapshot.remove(0);
        snapshot.clear();
        assertEquals(50, manager.size());
        assertEquals(
            "TEST-0",
            manager.getDataPutusan().get(0).getCaseId()
        );
    }

    @Test
    void failedReplacementPreservesExistingData() {
        DynamicArray<Putusan> invalid = new DynamicArray<>();
        invalid.add(fixture(100));
        assertThrows(
            IllegalArgumentException.class,
            () -> manager.replaceAll(invalid)
        );
        assertEquals(50, manager.size());
        assertEquals(
            "TEST-0",
            manager.getDataPutusan().get(0).getCaseId()
        );
        DynamicArray<Putusan> duplicates = dataset();
        duplicates.set(49, fixture(0));
        assertThrows(
            IllegalArgumentException.class,
            () -> manager.replaceAll(duplicates)
        );
        assertEquals(
            "TEST-49",
            manager.getDataPutusan().get(49).getCaseId()
        );
        assertTrue(manager.validateInvariant());
    }

    @Test
    void singleCourtDatasetIsAccepted() {
        DynamicArray<Putusan> records = dataset();
        manager.replaceAll(records);
        assertEquals(50, manager.size());
        for (int i = 0; i < manager.size(); i++) {
            assertEquals(
                "PN Uji A",
                manager.getDataPutusan().get(i).getPengadilan()
            );
        }
        assertTrue(manager.validateInvariant());
    }

    @Test
    void mixedCourtDatasetIsRejected() {
        DynamicArray<Putusan> records = dataset();
        String[] fields = records.get(49).toFields();
        fields[2] = "PN Uji B";
        records.set(49, Putusan.fromFields(fields));
        assertThrows(
            IllegalArgumentException.class,
            () -> manager.replaceAll(records)
        );
        assertEquals("TEST-0", manager.getDataPutusan().get(0).getCaseId());
        assertEquals("TEST-49", manager.getDataPutusan().get(49).getCaseId());
        assertEquals(50, manager.size());
        assertTrue(manager.validateInvariant());
    }

    @Test
    void combinedFilterUsesInclusiveBoundaries() {
        DataManager.FilterCriteria criteria =
                new DataManager.FilterCriteria(
                    "Sabu", 10.25, 20.25, "112", 10, 20
                );
        DynamicArray<Putusan> result = manager.filter(criteria);
        assertEquals(6, result.size());
        assertEquals("TEST-10", result.get(0).getCaseId());
        assertEquals("TEST-20", result.get(5).getCaseId());
        for (int index = 0; index < result.size(); index++) {
            assertTrue(criteria.matches(result.get(index)));
        }
        assertEquals(50, manager.size());
    }

    @Test
    void filterHandlesNullPasalSafely() {
        DynamicArray<Putusan> result = manager.filter(
                new DataManager.FilterCriteria(
                    "Ganja", null, null, "112", null, null
                )
        );
        assertTrue(result.isEmpty());
    }

    @Test
    void invalidFilterRangesAreRejected() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new DataManager.FilterCriteria("", 10.0, 5.0, null, null, null)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new DataManager.FilterCriteria("", Double.NaN, null, "", null, null)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new DataManager.FilterCriteria("", null, null, "", 20, 10)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new DataManager.FilterCriteria("", null, null, "", -1, null)
        );
    }

    @Test
    void summaryAndDistributionAreCorrect() {
        DataManager.Ringkasan summary =
                manager.hitungRingkasan();
        assertEquals(50, summary.jumlah());
        assertEquals(24.5, summary.rataVonis(), 0.000001);
        assertEquals(
            BigInteger.valueOf(1_225_000L),
            summary.totalDenda()
        );
        DynamicArray<DataManager.DistribusiJenis> distribution =
                manager.distribusiJenis();
        assertEquals(2, distribution.size());
        assertEquals("Sabu", distribution.get(0).jenis());
        assertEquals(25, distribution.get(0).jumlah());
        assertEquals("Ganja", distribution.get(1).jenis());
        assertEquals(25, distribution.get(1).jumlah());
    }

    @Test
    void insertionSortHandlesReverseOrder() {
        DynamicArray<Putusan> reversed = new DynamicArray<>();
        for (int index = 49; index >= 0; index--) {
            reversed.add(fixture(index));
        }
        manager.replaceAll(reversed);
        manager.urutkanBerdasarkanVonis();
        DynamicArray<Putusan> sorted = manager.getDataPutusan();
        for (int index = 0; index < 50; index++) {
            assertEquals(index, sorted.get(index).getVonisBulan());
        }
        assertFalse(manager.getSortTrace().isEmpty());
        assertTrue(manager.validateInvariant());
    }

    @Test
    void insertionSortIsStableForEqualValues() {
        DynamicArray<Putusan> records = dataset();
        for (int index = 0; index < records.size(); index++) {
            String[] fields = records.get(index).toFields();
            fields[10] = Integer.toString(index % 2);
            records.set(index, Putusan.fromFields(fields));
        }
        manager.replaceAll(records);
        manager.urutkanBerdasarkanVonis();
        DynamicArray<Putusan> sorted = manager.getDataPutusan();
        assertEquals("TEST-0", sorted.get(0).getCaseId());
        assertEquals("TEST-2", sorted.get(1).getCaseId());
        assertEquals("TEST-1", sorted.get(25).getCaseId());
        assertEquals("TEST-3", sorted.get(26).getCaseId());
        assertTrue(manager.validateInvariant());
    }

    @Test
    void modelRejectsInvalidNumbersAndDate() {
        String[] nanFields = fixture(0).toFields();
        nanFields[6] = "NaN";
        assertThrows(
            IllegalArgumentException.class,
            () -> Putusan.fromFields(nanFields)
        );
        String[] infiniteFields = fixture(0).toFields();
        infiniteFields[6] = "Infinity";
        assertThrows(
            IllegalArgumentException.class,
            () -> Putusan.fromFields(infiniteFields)
        );
        String[] invalidDate = fixture(0).toFields();
        invalidDate[3] = "2024-02-30";
        assertThrows(
            IllegalArgumentException.class,
            () -> Putusan.fromFields(invalidDate)
        );
        String[] negativeFine = fixture(0).toFields();
        negativeFine[11] = "-1";
        assertThrows(
            IllegalArgumentException.class,
            () -> Putusan.fromFields(negativeFine)
        );
        String[] invalidInteger = fixture(0).toFields();
        invalidInteger[10] = "2.5";
        assertThrows(
            IllegalArgumentException.class,
            () -> Putusan.fromFields(invalidInteger)
        );
    }

    @Test
    void missingOptionalFieldsBecomeNullOrZero() {
        String[] fields = fixture(0).toFields();
        fields[6] = "";
        fields[8] = "";
        fields[9] = "";
        fields[10] = "";
        fields[11] = "";
        fields[12] = "";
        Putusan record = Putusan.fromFields(fields);
        assertEquals(0.0, record.getBeratBarangBuktiGram(), 0.000001);
        assertNull(record.getPasalTerbukti());
        assertEquals(0, record.getTuntutanBulan());
        assertEquals(0, record.getVonisBulan());
        assertEquals(0L, record.getDendaRupiah());
        assertNull(record.getBarangBukti());
    }

    @Test
    void csvRoundTripPreservesSixteenFields() throws IOException {
        DynamicArray<Putusan> source = dataset();
        String[] fields = source.get(0).toFields();
        fields[12] = "Barang bukti, dengan \"kutipan\"\nbaris kedua";
        fields[13] = "Teks, berkoma";
        fields[14] = "\"Kutipan\" tambahan";
        source.set(0, Putusan.fromFields(fields));
        Path file = directory.resolve("roundtrip.csv");
        CsvService service = new CsvService();
        service.exportCsv(file.toFile(), source);
        CsvService.ImportResult imported =
                service.importCsv(file.toFile());
        assertTrue(imported.acceptable(), imported.errorText());
        assertEquals(50, imported.validRows());
        assertEquals(0, imported.invalidRows());
        for (int index = 0; index < 50; index++) {
            assertArrayEquals(
                source.get(index).toFields(),
                imported.records().get(index).toFields()
            );
        }
    }

    @Test
    void csvRejectsWrongRecordCount() throws IOException {
        DynamicArray<Putusan> records = dataset();
        records.remove(49);
        Path file = directory.resolve("wrong-count.csv");
        CsvService service = new CsvService();
        service.exportCsv(file.toFile(), records);
        CsvService.ImportResult result =
                service.importCsv(file.toFile());
        assertFalse(result.acceptable());
        assertEquals(49, result.validRows());
        assertEquals(50, manager.size());
    }

    @Test
    void csvRejectsDuplicateRecord() throws IOException {
        DynamicArray<Putusan> records = dataset();
        records.set(49, fixture(0));
        Path file = directory.resolve("duplicate.csv");
        CsvService service = new CsvService();
        service.exportCsv(file.toFile(), records);
        CsvService.ImportResult result =
                service.importCsv(file.toFile());
        assertFalse(result.acceptable());
        assertEquals(49, result.validRows());
        assertEquals(1, result.invalidRows());
        assertEquals(50, manager.size());
    }

    @Test
    void csvRejectsWrongHeader() throws IOException {
        Path file = directory.resolve("wrong-header.csv");
        Files.writeString(
            file,
            "header, salah\n",
            StandardCharsets.UTF_8
        );
        CsvService.ImportResult result =
                new CsvService().importCsv(file.toFile());
        assertFalse(result.acceptable());
        assertEquals(0, result.validRows());
    }

    @Test
    void csvParserRejectsBrokenQuotes() {
        CsvService.CsvReader reader = new CsvService.CsvReader(
            new StringReader("\"quote belum ditutup")
        );
        assertThrows(IOException.class, reader::readRecord);
    }

    @Test
    void csvParserRejectsCharactersAfterClosingQuote() {
        CsvService.CsvReader reader = new CsvService.CsvReader(
            new StringReader("\"A\"x,B")
        );
        assertThrows(IOException.class, reader::readRecord);
    }

    @Test
    void csvParserRejectsExtraColumns() {
        CsvService.CsvReader reader = new CsvService.CsvReader(
            new StringReader(
                "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17"
            )
        );
        assertThrows(IOException.class, reader::readRecord);
    }

    @Test
    void csvSupportsUtf8Bom() throws IOException {
        Path file = directory.resolve("bom.csv");
        CsvService service = new CsvService();
        service.exportCsv(file.toFile(), dataset());
        String content = Files.readString(
            file,
            StandardCharsets.UTF_8
        );
        Files.writeString(
            file,
            "\uFEFF" + content,
            StandardCharsets.UTF_8
        );
        CsvService.ImportResult result =
                service.importCsv(file.toFile());
        assertTrue(result.acceptable(), result.errorText());
    }

    @Test
    void fxmlAndCssResourcesExist() {
        String base = "/com/pengadilan/app/";
        assertNotNull(getClass().getResource(base + "fxml/MainView.fxml"));
        assertNotNull(getClass().getResource(base + "fxml/DashboardView.fxml"));
        assertNotNull(getClass().getResource(base + "fxml/DataPutusanView.fxml"));
        assertNotNull(getClass().getResource(base + "fxml/FilterView.fxml"));
        assertNotNull(getClass().getResource(base + "fxml/ImporEksporView.fxml"));
        assertNotNull(getClass().getResource(base + "fxml/RiwayatView.fxml"));
        assertNotNull(getClass().getResource(base + "css/style.css"));
    }
}