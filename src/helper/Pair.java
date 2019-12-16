package helper;

public class Pair<T, U> {
    public T a;
    public U b;

    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "{" +
            a +
            " " + b +
            '}';
    }
}