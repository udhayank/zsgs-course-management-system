package controller;

import java.sql.SQLException;

import view.StudentView;

public class Test {

	public static void main(String[] args) throws SQLException {
		
		new StudentView().printMySchedule(1);

	}

}
