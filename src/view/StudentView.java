package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import controller.DataBaseController;
import controller.StudentController;
import model.Course;

public class StudentView {

	Scanner in;
	StudentController studentController;
	DataBaseController db;
	
	public StudentView() throws SQLException {
		in = new Scanner(System.in);
		studentController = new StudentController();
		db = new DataBaseController();
	}
	
	public void studentLogin() throws SQLException {
		System.out.println("\n-----------------------------------\n");
		int rollNo;
		while (true) {
			System.out.print("\nEnter roll number: ");
			rollNo = in.nextInt();
			System.out.print("Enter password: ");
			String password = in.next();		
			
			if (studentController.isValidCredentials(rollNo, password)) {				
				break;
			} else {
				System.out.println("Invalid credentials!");
			}
		}
		printMenu(rollNo);
	}

	public void printMenu(int rollNo) throws SQLException {
		
		System.out.println("\n-----------------------------------\n");
		System.out.println("1 - Enroll course");
		System.out.println("2 - Show my course list");
		System.out.println("3 - Exit course");
		System.out.println("4 - Show my schedule");
		System.out.println("5 - change password");
		System.out.println("6 - Sign out");
		
		int choice;
		while (true) {
			System.out.print("\nEnter choice: ");
			choice = in.nextInt();
			if (choice >= 1 && choice <= 6) {
				break;
			} else {
				System.out.println("Invalid choice!");
			}
		}
		
		switch (choice) {
		case 1:
			enrollCourse(rollNo);
			printMenu(rollNo);
			break;
			
		case 2:
			showMyCourseList(rollNo);
			printMenu(rollNo);
			break;
			
		case 3:
			showMyCourseList(rollNo);
			exitCourse(rollNo);
			printMenu(rollNo);
			break;
			
		case 4:
			printMySchedule(rollNo);
			printMenu(rollNo);
			break;
		
		case 5:
			changePassword(rollNo);
			printMenu(rollNo);
			break;
			
		case 6:
			new MainMenu().printMainMenu();
			break;			
		}
		
	}
	
	
	private void enrollCourse(int rollNo) throws SQLException {
		
		System.out.println("\n-----------------------------------\n");
		System.out.println("Choose department:");
		db.printDeptList();
		
		int choice;
		while (true) {
			System.out.println("\nEnter choice:");
			choice = in.nextInt();
			if (choice >= 1 && choice <= db.getNoOfDepartment()) {
				break;
			} else {
				System.out.println("Invalid choice!");
			}
		}
		
		chooseCourse(rollNo, choice);
		
	}
	
	private void showMyCourseList(int rollNo) throws SQLException {
		
		System.out.println("--------------------------------------------------------------------------");
		System.out.printf("%-10s %-25s %-12s %-10s", "Course id", "Course name", "Semester", "Credits").println();
		System.out.println("--------------------------------------------------------------------------");
		studentController.printMyCourses(rollNo, (List<Course> courseList) -> {
			for (Course course:courseList) {
				System.out.printf("%-10s %-25s %-12s %-10s", course.getCourseId(), course.getCourseName(), course.getSemester(), course.getCredits()).println();;
			}
		});
				
	}
	
	private void exitCourse(int rollNo) throws SQLException {
		
		System.out.print("\nEnter course id: ");
		int courseId = in.nextInt();
		studentController.deleteCourse(rollNo, courseId);
		
	}
	
	private void chooseCourse(int rollNo, int department) throws SQLException {
		
		System.out.println("\nAvailable courses:");
		
		System.out.printf("%-10s %-25s %-12s %-10s", "Course id", "Course name", "Semester", "Credits").println();
		db.printDeptCourse(department);
		
		System.out.print("\nChoose course: ");
		int choice = in.nextInt();
		
		if(studentController.addCourse(rollNo, choice)) {
			System.out.println("Course added successfully!");			
		}
		
	}

	public void printMySchedule(int rollNo) throws SQLException {
		
		System.out.println("--------------------------------------------------------------------------");
		System.out.printf("%-15s| %-10s %-10s %-10s %-10s %-10s", "Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday").println();;
		System.out.println("--------------------------------------------------------------------------");
		String[] time = {"09:00 - 10:00", "10:00 - 11:00", "11:30 - 12:30", "14:00 - 15:00", "15:00 - 16:00"};
		studentController.printSchedule(rollNo, (String[][] schedule) -> {
			for (int i=0; i<5; i++) {
				System.out.printf("%-15s|   %-10s %-10s %-10s %-10s %-10s", time[i], schedule[i][0], schedule[i][1], schedule[i][2], schedule[i][3], schedule[i][4]).println();;
			}
		});
		System.out.println("--------------------------------------------------------------------------\n");
		studentController.printMyCourseCaption(rollNo, (List<Course> courseList) -> {
			for (Course course:courseList) {
				System.out.printf("%-3s -->  %-25s", course.getCourseId(), course.getCourseName()).println();
			}
		});
		
		
	}
	
	private void changePassword(int rollNo) throws SQLException {
		
		System.out.println("\n-----------------------------------\n");
		System.out.print("Enter old password: ");
		String oldPassword = in.next();
		if (studentController.isValidCredentials(rollNo, oldPassword)) {
			
			System.out.print("Enter new password: ");
			String newPassword = in.next();
			studentController.changePassword(rollNo, newPassword);
			System.out.println("Password changed successfully!");
			
		} else {
			System.out.println("Invalid password!");
			changePassword(rollNo);
		}
		
	}
	
}
