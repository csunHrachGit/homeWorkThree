package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CSArrayList Unit Tests")
class MainTest {

    private CSArrayList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CSArrayList<>();
    }

    @Test
    @DisplayName("Add and retrieve elements correctly")
    void testAddAndGet() {
        // Arrange
        list.add(10);
        list.add(20);

        // Act & Assert
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    @DisplayName("Remove element by index")
    void testRemoveByIndex() {
        list.add(1);
        list.add(2);
        list.add(3);

        int removed = list.remove(1);

        assertEquals(2, removed);
        assertEquals(2, list.size());
        assertEquals(3, list.get(1));
    }

    @Test
    @DisplayName("Remove object (by value)")
    void testRemoveByObject() {
        list.add(10);
        list.add(20);
        list.add(30);

        assertTrue(list.remove(Integer.valueOf(20)));
        assertFalse(list.remove(Integer.valueOf(50)));
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Clear empties the list")
    void testClear() {
        list.add(1);
        list.add(2);
        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @ParameterizedTest(name = "Ensure capacity test with minCapacity={0}")
    @CsvSource({
            "5", "10", "50"
    })
    void testEnsureCapacity(int minCapacity) {
        list.ensureCapacity(minCapacity);
        assertTrue(list.capacity() >= minCapacity);
    }

    @Test
    @DisplayName("Trim to size shrinks capacity")
    void testTrimToSize() {
        for (int i = 0; i < 10; i++) list.add(i);
        int before = list.capacity();
        list.trimtoSize();
        int after = list.capacity();

        assertTrue(after <= before);
        assertEquals(10, list.size());
    }

    @Test
    @DisplayName("Fail-fast iterator throws ConcurrentModificationException")
    void testFailFastIterator() {
        list.add(1);
        list.add(2);

        Iterator<Integer> it = list.iterator();
        list.add(3); // Structural modification

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    @DisplayName("IndexOutOfBoundsException for invalid index")
    void testIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    }

    @Test
    @DisplayName("Null elements handled in remove(Object)")
    void testNullRemove() {
        list.add(null);
        list.add(5);
        assertTrue(list.remove(null));
        assertFalse(list.remove(null)); // already removed
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Multiple resizes during additions")
    void testMultipleResizes() {
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        assertEquals(10000, list.size());
        assertEquals(9999, list.get(9999));
    }
}
