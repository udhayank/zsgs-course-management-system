package controller;

import java.util.List;

@FunctionalInterface
public interface AdminControllerListener<T> {

	 void print(List<T> result);


}
