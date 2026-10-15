
package ua.khnu.kpp;

public final class Counter {

    private int value;

    public Counter(int initial) {
        if (initial < 0) {
            throw new IllegalArgumentException("negative");
        }
        value = initial;
    }

    public void increment() {
        if (value == Integer.MAX_VALUE) {
            throw new IllegalStateException("limit reached");
        }
        value++;
    }

    public int value() {
        return value;
    }

    public static void main(String[] args) {
        Counter counter = new Counter(2);
        counter.increment();
        System.out.println(counter.value());
    }
}
