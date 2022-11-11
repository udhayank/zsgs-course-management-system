package controller;

import java.util.List;

@FunctionalInterface
public interface StudentContorllerListener<T> {
	
	void print(List<T> courseList);
	
}
