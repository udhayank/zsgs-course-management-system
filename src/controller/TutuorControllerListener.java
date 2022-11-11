package controller;

import java.util.List;

@FunctionalInterface
public interface TutuorControllerListener<T> {

	void print(List<T> t);
	
}
