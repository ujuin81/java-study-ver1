package springbook.learningtest.template;

public interface LineCallback<T> {
	T doSomthingWithLine(String line, T value);
}
