import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyHashTableTest {

    // ---------- Helpers ----------
    private static void assertPresent(MyHashTable<String> ht, String s) {
        assertNotNull(ht.contains(s), "Expected present: " + s);
        assertEquals(s, ht.contains(s).item, "Node should store the same item");
    }

    private static void assertAbsent(MyHashTable<String> ht, String s) {
        assertNull(ht.contains(s), "Expected absent: " + s);
    }

    // ---------- Tests ----------

    @Test
    void testInitiallyEmpty() {
        MyHashTable<String> ht = new MyHashTable<>();
        assertTrue(ht.isEmpty());
        assertEquals(0, ht.size());
        assertAbsent(ht, "Apple");
        assertFalse(ht.remove("Apple"));
    }

    @Test
    void testAddAndContainsBasic() {
        MyHashTable<String> ht = new MyHashTable<>();

        ht.add("Apple");
        ht.add("Banana");
        ht.add("Cherry");

        assertEquals(3, ht.size());
        assertFalse(ht.isEmpty());

        assertPresent(ht, "Apple");
        assertPresent(ht, "Banana");
        assertPresent(ht, "Cherry");
        assertAbsent(ht, "Dragonfruit");
    }

    @Test
    void testAddDuplicateDoesNotIncreaseSize() {
        MyHashTable<String> ht = new MyHashTable<>();

        ht.add("Apple");
        assertEquals(1, ht.size());

        ht.add("Apple"); // duplicate
        assertEquals(1, ht.size(), "Duplicate insert should not change hash table size");

        // still present
        assertPresent(ht, "Apple");
    }

    @Test
    void testRemoveBasic() {
        MyHashTable<String> ht = new MyHashTable<>();

        ht.add("Apple");
        ht.add("Banana");
        ht.add("Cherry");

        assertTrue(ht.remove("Banana"));
        assertEquals(2, ht.size());
        assertAbsent(ht, "Banana");

        // removing again should fail
        assertFalse(ht.remove("Banana"));
        assertEquals(2, ht.size());
    }

    @Test
    void testRemoveOnEmpty() {
        MyHashTable<String> ht = new MyHashTable<>();
        assertFalse(ht.remove("Anything"));
        assertEquals(0, ht.size());
    }

    @Test
    void testClear() {
        MyHashTable<String> ht = new MyHashTable<>();
        ht.add("Apple");
        ht.add("Banana");
        ht.add("Cherry");

        assertEquals(3, ht.size());
        ht.clear();

        assertEquals(0, ht.size());
        assertTrue(ht.isEmpty());
        assertAbsent(ht, "Apple");
        assertAbsent(ht, "Banana");
        assertAbsent(ht, "Cherry");

        // should be usable after clear
        ht.add("Durian");
        assertEquals(1, ht.size());
        assertPresent(ht, "Durian");
    }

    @Test
    void testNullArgumentsThrow() {
        MyHashTable<String> ht = new MyHashTable<>();

        assertThrows(IllegalArgumentException.class, () -> ht.add(null));
        assertThrows(IllegalArgumentException.class, () -> ht.contains(null));
        assertThrows(IllegalArgumentException.class, () -> ht.remove(null));
    }

    @Test
    void testCollisionHeavyCapacityOne() {
        // Forces everything into the same bucket => stresses your MyTree bucket logic
        MyHashTable<String> ht = new MyHashTable<>(1);

        String[] items = {"Apple", "Banana", "Cherry", "Durian", "Elderberry", "Fig", "Grape"};
        for (String s : items) ht.add(s);

        assertEquals(items.length, ht.size());
        for (String s : items) assertPresent(ht, s);

        // duplicates shouldn't change size even under collisions
        ht.add("Apple");
        ht.add("Banana");
        assertEquals(items.length, ht.size());

        // remove a few
        assertTrue(ht.remove("Cherry"));
        assertTrue(ht.remove("Fig"));
        assertEquals(items.length - 2, ht.size());
        assertAbsent(ht, "Cherry");
        assertAbsent(ht, "Fig");

        // remove non-existent
        assertFalse(ht.remove("NotThere"));
        assertEquals(items.length - 2, ht.size());
    }

    @Test
    void testMixedOperationsRegression() {
        MyHashTable<String> ht = new MyHashTable<>(5); // small to encourage some collisions

        assertEquals(0, ht.size());

        ht.add("A");
        ht.add("B");
        ht.add("C");
        ht.add("D");
        assertEquals(4, ht.size());

        // duplicate
        ht.add("C");
        assertEquals(4, ht.size());

        // remove middle
        assertTrue(ht.remove("B"));
        assertEquals(3, ht.size());
        assertAbsent(ht, "B");

        // re-add removed
        ht.add("B");
        assertEquals(4, ht.size());
        assertPresent(ht, "B");

        // remove all
        assertTrue(ht.remove("A"));
        assertTrue(ht.remove("B"));
        assertTrue(ht.remove("C"));
        assertTrue(ht.remove("D"));
        assertEquals(0, ht.size());
        assertTrue(ht.isEmpty());

        // removing again should fail
        assertFalse(ht.remove("A"));
        assertFalse(ht.remove("B"));
        assertFalse(ht.remove("C"));
        assertFalse(ht.remove("D"));
        assertEquals(0, ht.size());
    }
}