package view;

import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {
	
	Scanner in;
	
	public MainMenu() {
		in = new Scanner(System.in);
	}
	
	public void printMainMenu() throws SQLException {
		
		System.out.println("1 - Admin login");
		System.out.println("2 - Student login");
		System.out.println("3 - Tutor login");
		
		int choice = 0;
		while (true) {
			System.out.print("\nEnter your choice: ");
			choice = in.nextInt();
			if (choice >= 1 && choice <= 3) {
				break;
			} else {
				System.out.println("Invalid Choice!");
			}			
		}
		
		switch (choice) {
		case 1:
			AdminView adminView = new AdminView();
			adminView.adminLogin();
			break;

		case 2:
			StudentView studentView = new StudentView();
			studentView.studentLogin();
			break;
			
		case 3:
			TutorView tutorView = new TutorView();
			tutorView.tutorLogin();
			break;
			
		}
		
	}
	
	public void init() {
		System.out.println("-----------------------------------");
		System.out.println("Welcome to Course Management System");
		System.out.println("-----------------------------------\n");
		
		printFeatures();
		
		try {
			printMainMenu();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void printFeatures() {
		
		System.out.println("Features:");
		System.out.println("\t--> Store and manage information about courses offered, students and tutors.");
		System.out.println("\t--> Manage mapping of course with students and tutor.");
		System.out.println("\t--> Admin can add new course, new student, new tutor");
		System.out.println("\t--> Admin can add assign tutor to cource");
		System.out.println("\t--> Students can enroll in the interested courses and a schedule is generated");
		System.out.println("\t--> Tutor can view student list and class schedule");
		System.out.println("\t--> Any clash in the schedule is handled and not allowed");
		System.out.println("\n-----------------------------------\n");
		
	}

	public static void main(String[] args) {
		
		MainMenu menu = new MainMenu();
		menu.init();
		
	}
	
}
