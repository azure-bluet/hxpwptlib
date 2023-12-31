package hxpwpt.utils;

public interface QuadFunction <A, B, C, D, R> {
    public R apply (A a, B b, C c, D d);
}
