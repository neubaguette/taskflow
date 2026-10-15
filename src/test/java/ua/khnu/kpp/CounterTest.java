
package ua.khnu.kpp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CounterTest {

    @Test
    void counterIncrement() {
        Counter counter = new Counter(2);

        counter.increment();

        assertEquals(3, counter.value());
    }

    @Test
    void counterOverflowProtection() {
        Counter counter = new Counter(Integer.MAX_VALUE);

        assertThrows(IllegalStateException.class,
                counter::increment);

        assertEquals(Integer.MAX_VALUE, counter.value());

        assertThrows(IllegalArgumentException.class,
                () -> new Counter(-1));
    }
}
