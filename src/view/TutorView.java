package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import controller.TutorController;
import model.Course;
import model.Student;

public class TutorView {

	Scanner in;
	TutorController tutorController;
	
	public TutorView() throws SQLException {
		in = new Scanner(System.in);
		tutorController = new TutorController();
	}
	
	public void tutorLogin() throws SQLException {
		System.out.println("\n-----------------------------------\n");
		int tutorId;
		while (true) {
			System.out.print("\nEnter tutor id: ");
			tutorId = in.nextInt();
			System.out.print("Enter password: ");
			String password = in.next();		
			
			if (tutorController.isValidCredentials(tutorId, password)) {				
				break;
			} else {
				System.out.println("Invalid credentials!");
			}
		}
		printMenu(tutorId);
	}

	private void printMenu(int tutorId) throws SQLException {
		
		System.out.println("\n-----------------------------------\n");
		System.out.println("1 - Show my course list");
		System.out.println("2 - Show my schedule");
		System.out.println("3 - show students details");
		System.out.println("4 - change password");
		System.out.println("5 - Sign out");
		
		int choice;
		while (true) {
			System.out.print("\nEnter choice: ");
			choice = in.nextInt();
			if (choice >= 1 && choice <= 5) {
				break;
			} else {
				System.out.println("Invalid choice!");
			}
		}
		
		switch (choice) {
		
		case 1:
			showMyCourseList(tutorId);
			printMenu(tutorId);
			break;
		
		case 2:
			showMySchedule(tutorId);
			printMenu(tutorId);
			break;
			
		case 3:
			showStudents(tutorId);
			printMenu(tutorId);
			break;
			
		case 4:
			changePassword(tutorId);
			printMenu(tutorId);
			break;
			
		case 5:
			new MainMenu().printMainMenu();
			break;
			
		}
		
	}


	private void showMyCourseList(int tutorId) throws SQLException {
		System.out.println("\n--------------------------------------------------------------------------");
		System.out.printf("%-10s %-25s %-12s", "Course id", "Course name", "Semester").println();
		System.out.println("--------------------------------------------------------------------------");
		tutorController.printMyCourses(tutorId, (List<Course> courseList) -> {
			for (Course course:courseList) {
				System.out.printf("%-10s %-25s %-12s", course.getCourseId(), course.getCourseName(), course.getSemester()).println();
			}
		});		
	}

	private void showMySchedule(int tutorId) throws SQLException {
		System.out.println("--------------------------------------------------------------------------");
		System.out.printf("%-15s| %-10s %-10s %-10s %-10s %-10s", "Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday").println();;
		System.out.println("--------------------------------------------------------------------------");
		String[] time = {"09:00 - 10:00", "10:00 - 11:00", "11:30 - 12:30", "14:00 - 15:00", "15:00 - 16:00"};
		tutorController.printSchedule(tutorId, (String[][] schedule) -> {
			for (int i=0; i<5; i++) {
				System.out.printf("%-15s|   %-10s %-10s %-10s %-10s %-10s", time[i], schedule[i][0], schedule[i][1], schedule[i][2], schedule[i][3], schedule[i][4]).println();;
			}
		});
		System.out.println("--------------------------------------------------------------------------\n");
		tutorController.printMyCourseCaption(tutorId, (List<Course> courseList)-> {
			for (Course course:courseList) {
				System.out.printf("%-3s -->  %-25s", course.getCourseId(), course.getCourseName()).println();
			}
		});		
	}

	private void showStudents(int tutorId) throws SQLException {
		
		System.out.println("\nSelect course:");
		tutorController.printMyCourseCaption(tutorId, (List<Course> courseList)-> {
			for (Course course:courseList) {
				System.out.printf("%-3s -->  %-25s", course.getCourseId(), course.getCourseName()).println();
			}
		});		
		int courseId;
		System.out.print("\nEnter course id: ");
		courseId = in.nextInt();
		System.out.println("--------------------------------------------------------------------------");
		System.out.printf("%-8s %-15s %-15s %-20s", "Roll No", "Name", "Department", "Email").println();
		System.out.println("--------------------------------------------------------------------------");

		tutorController.printStudents(courseId, (List<Student> studentList) -> {
			for (Student student:studentList) {
				System.out.printf("%-8s %-15s %-15s %-20s", student.getRollNo(), student.getName(), student.getDeptName(), student.getEmail()).println();
			}
		});
		
	}
	
	private void changePassword(int tutorId) throws SQLException {
		
		System.out.println("\n-----------------------------------\n");
		System.out.print("Enter old password: ");
		String oldPassword = in.next();
		if (tutorController.isValidCredentials(tutorId, oldPassword)) {
			
			System.out.print("Enter new password: ");
			String newPassword = in.next();
			tutorController.changePassword(tutorId, newPassword);
			System.out.println("Password changed successfully!");
			
		} else {
			System.out.println("Invalid password!");
			changePassword(tutorId);
		}
		
	}

	
}
