package controller;

@FunctionalInterface
public interface SchedulePrinter<T> {

	void print(T t);
	
}
