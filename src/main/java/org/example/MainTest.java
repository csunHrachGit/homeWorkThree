package org.example;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testEdgeIndexCases() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals("A", list.get(0)); // First element
        assertEquals("C", list.get(list.size() - 1)); // Last element
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(list.size())); // Out of bounds
    }

    @Test
    void testMultipleResizes() {
        CSArrayList<Integer> list = new CSArrayList<>();
        int numItems = 10_000;

        for (int i = 0; i < numItems; i++) {
            list.add(i);
        }

        assertEquals(numItems, list.size());
        for (int i = 0; i < numItems; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    void testSearchesWithDuplicatesAndNulls() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add("A");
        list.add("B");
        list.add(null);
        list.add("B");
        list.add("C");

        assertEquals(1, list.indexOf("B"));
        assertEquals(3, list.lastIndexOf("B"));
        assertEquals(2, list.indexOf(null));
        assertEquals(-1, list.indexOf("D"));
    }

    @Test
    void testRemoveObjectBehavior() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        assertTrue(list.remove("B")); // Remove existing element
        assertEquals(2, list.size());
        assertEquals(-1, list.indexOf("B"));

        assertFalse(list.remove("D")); // Remove non-existing element
        assertEquals(2, list.size());
    }

    @Test
    void testFailFastIterator() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> iterator = list.iterator();
        list.add("D");

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }
}
