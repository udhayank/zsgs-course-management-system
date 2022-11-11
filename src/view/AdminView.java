package view;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import controller.AdminController;
import controller.AdminControllerListener;
import controller.DataBaseController;
import controller.TutorController;
import model.Course;
import model.Student;
import model.Tutor;

public class AdminView {
	
	Scanner in;
	AdminController adminController;
	TutorController tutorController;
	DataBaseController db;
	
	public AdminView() {
		in = new Scanner(System.in);
		try {
			adminController = new AdminController();
			tutorController = new TutorController();
			db = new DataBaseController();
		} catch (SQLException e) {
			System.out.println("Can't connect to database!");
		}
		
	}
	
	public void adminLogin() {
		System.out.println("\n-----------------------------------\n");
		System.out.println("Hello admin!");
		while (true) {
			System.out.print("\nEnter admin password: ");
			String password = in.next();		
			
			if (adminController.isValidCredentials(password)) {				
				break;
			} else {
				System.out.println("Invalid credentials!");
			}
		}
		printMenu();
	}

	public void printMenu() {
		System.out.println("\n-----------------------------------\n");
		System.out.println("1 - view course list");
		System.out.println("2 - add course");
		System.out.println("3 - view student list");
		System.out.println("4 - add student");
		System.out.println("5 - view tutor list");
		System.out.println("6 - add tutor");
		System.out.println("7 - assign tutor to course");
		System.out.println("8 - sign out");
		
		int choice = 0;
		while(true) {
			System.out.println("\nEnter your choice: ");
			choice = in.nextInt();
			if (choice >= 1 && choice <= 8) {
				break;
			} else {
				System.out.println("Invalid choice!");
			}
		}
		
		switch (choice) {
		case 1:
			printCourseList();
			printMenu();
			break;
			
		case 2:
			addCourse();
			printMenu();
			break;
			
		case 3:
			printStudentList();
			printMenu();
			break;
			
		case 4:
			addStudent();
			printMenu();
			break;
			
		case 5:
			printTutorList();
			printMenu();
			break;
			
		case 6:
			addTutor();
			printMenu();
			break;
			
		case 7:
			assignTutor();
			printMenu();
			break;
			
		case 8:
			System.out.println("\n-----------------------------------\n");
			try {
				new MainMenu().printMainMenu();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		}
	}

	public void printCourseList() {
		System.out.println("\n----------------------------------------------------------------------------");
		System.out.printf("%-5s %-25s %-15s %-12s %-10s", "Id", "Course Name", "Department", "Semester", "Credits").println();
		System.out.println("----------------------------------------------------------------------------");
		
		// This has been implemented as lambda function below 
//		AdminControllerListener<Course> listener = new AdminControllerListener<Course>() {
//			
//			@Override
//			public void print(List<Course> result) {
//				
//				for(Course course:result) {
//					System.out.printf("%-5s %-25s %-15s %-12s %-10s", course.getCourseId(), course.getCourseName(), course.getDeptName(), course.getSemester(), course.getCredits()).println();
//				}
//				
//			}
//		};
		
		try {
			adminController.printCourseList((List<Course> result) -> {
				for(Course course:result) {
					System.out.printf("%-5s %-25s %-15s %-12s %-10s", course.getCourseId(), course.getCourseName(), course.getDeptName(), course.getSemester(), course.getCredits()).println();
				}
			});
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Can't fetch course details!");
		}
	}

	public void addCourse() {
		
		String courseName = inputCourseName();
		int department;
		try {
			department = inputDepartment();			
		} catch (SQLException e) {
			System.err.println("Can't fetch department information!");
			return;
		}
		int semester = inputSemester();
		int credits = inputCredits();
		
		Course course = new Course();
		course.setCourseName(courseName);
		course.setDepartment(department);
		course.setSemester(semester);
		course.setCredits(credits);
		
		try {
			int courseId = adminController.addCourse(course);		
			addCourseSchedule(courseId);
		} catch (SQLException e) {
			System.err.println("Can't add course!");
		}
				
	}
		
	private void printStudentList() {		
		System.out.println("\n----------------------------------------------------------------------------");
		System.out.printf("%-25s %-15s %-12s %-10s", "Name", "Department", "Semester", "Credits").println();
		System.out.println("----------------------------------------------------------------------------");
		try {
			adminController.printStudentList((List<Student> studentList) -> {
				for (Student student:studentList) {
					System.out.printf("%-25s %-15s %-12s %-10s", student.getName(), student.getDeptName(), student.getSemester(), student.getCredits()).println();
				}					
			});
		} catch (SQLException e) {
			System.err.println("Can't fetch students list!");
		}
	}
	
	public void addStudent() {
		String studentName = inputStudentName();
		int semester = inputSemester();
		int department;
		try {
			department = inputDepartment();			
		} catch (SQLException e) {
			System.err.println("Can't fetch department information!");
			return;
		}
		String email = inputEmail();
		
		Student student = new Student();
		student.setName(studentName);
		student.setSemester(semester);
		student.setDepartment(department);
		student.setEmail(email);
		
		try {
			adminController.addStudent(student);
		} catch (SQLException e) {
			System.err.println("Can't add student details!");
		}
	}
	
	private void printTutorList() {
		System.out.println("\n----------------------------------------------------------------------------");
		System.out.printf("%-10s %-15s %-12s %-15s","Tutor ID", "Tutor Name", "Department", "Email").println();
		System.out.println("----------------------------------------------------------------------------");
		try {
			adminController.printTutorList((List<Tutor> tutorList) -> {
				for (Tutor tutor:tutorList) {
					System.out.printf("%-10s %-15s %-12s %-15s", tutor.getId(),  tutor.getName(), tutor.getDeptName(), tutor.getEmail()).println();
				}
			});
		} catch (SQLException e) {
			System.err.println("Can't fetch tutor list!");
		}
	}
	
	private void addTutor() {
		String name = inputTutorName();
		int department;
		try {
			department = inputDepartment();			
		} catch (SQLException e) {
			System.err.println("Can't fetch department information!");
			return;
		}
		String email = inputEmail();
		
		Tutor tutor = new Tutor();
		tutor.setName(name);
		tutor.setEmail(email);
		tutor.setDepartment(department);
		
		try {
			adminController.addTutor(tutor);
		} catch (SQLException e) {
			System.err.println("Can't add tutor details!");
		}
	}
	
	private void assignTutor() {
		System.out.println("\nChoose tutor:");	
		printTutorList();
		System.out.print("\nEnter tutor id: ");
		int tutorId = in.nextInt();
		System.out.println("\nChoose course:");
		printCourseList();
		System.out.print("\nEnter course id: ");
		int courseId = in.nextInt();
		
		try {
			if(tutorController.addCourse(tutorId, courseId)) {
				System.out.println("Tutor assigned successfully!");
			}
		} catch (SQLException e) {
			System.err.println("Can't assign tutor!");
		}
		
	}

	private String inputEmail() {
		String email;
		System.out.print("Enter email: ");
		in.nextLine();
		email = in.nextLine();
		return email;
	}

	private String inputTutorName() {
		System.out.println("\n-----------------------------------\n");
		System.out.print("Enter tutor name: ");
		in.nextLine();
		return in.nextLine();
	}
	
	private String inputStudentName() {
		System.out.println("\n-----------------------------------\n");
		System.out.print("Enter student name: ");
		in.nextLine();
		return in.nextLine();
	}

	private String inputCourseName() {
		System.out.println("\n-----------------------------------\n");
		System.out.print("Enter course name: ");
		in.nextLine();
		return in.nextLine();
	}
	
	private int inputDepartment() throws SQLException {
		int department;
		System.out.println("\nDepartments:");
		db.printDeptList();
		while (true) {
			System.out.print("\nChoose department: ");
			department = in.nextInt();
			if  (department >= 1 && department <= db.getNoOfDepartment()) {
				break;
			}
		}
		return department;
	}
	
	private int inputSemester() {
		int semester;
		System.out.println("\nChoose semester: ");
		System.out.println("1 - odd");
		System.out.println("2 - even");
		while (true) {
			System.out.print("Enter choise: ");
			semester = in.nextInt();
			if  (semester >= 1 && semester <= 2) {
				break;
			}
		}
		return semester;
	}
	
	private int inputCredits() {
		int credits = 0;
		while (true) {
			System.out.print("\nEnter credits (1-10): ");
			credits = in.nextInt();
			if (credits >= 1 && credits <= 10) {
				break;
			} else {
				System.out.println("Invalid credits!");
			}
		}
		return credits;
	}

	private void addCourseSchedule(int courseId) {
		
		System.out.println("\nAdd schedule of the created course:\n");
		printSlotInfo();
		System.out.println("Enter slot for each day of week (0 for no slot):");
		
		int[] slots = new int[5];
		HashMap<Integer, String> days = new HashMap<>();
		days.put(0, "monday");
		days.put(1, "tuesday");
		days.put(2, "wednesday");
		days.put(3, "thursday");
		days.put(4, "friday");
		
		for (int i=0; i<slots.length; i++) {
			while (true) {
				System.out.print(days.get(i) + " :");
				slots[i] = in.nextInt();
				if (slots[i] >= 0 && slots[i] <= 5) {
					break;
				} else {
					System.out.println("Invalid slot!");
				}
			}
		}
		
		try {
			adminController.addSchedule(courseId, slots);
			System.out.println("\nSchedule added successfully!");
		} catch (SQLException e) {
			System.err.println("Can't add schedule!");
		}
		
		
	}

	private void printSlotInfo() {
		System.out.println("1 -> 9:00 - 10:00");		
		System.out.println("2 -> 10:00 - 11:00");		
		System.out.println("3 -> 11:30 - 12:30");		
		System.out.println("4 -> 14:00 - 15:00");		
		System.out.println("5 -> 15:00 - 16:00");		
	}

	
//	private LocalTime inputStartTime() {
//		LocalTime startTime;
//		System.out.print("Enter start time: ");
//		String time = in.next();
//		startTime = LocalTime.of( Integer.valueOf(time.split(":")[0]) , Integer.valueOf(time.split(":")[1]) );
//		return startTime;
//	}
//	
//	private LocalTime inputEndTime() {
//		LocalTime endTime;
//		System.out.print("Enter end time: ");
//		String time = in.next();
//		endTime = LocalTime.of( Integer.valueOf(time.split(":")[0]) , Integer.valueOf(time.split(":")[1]) );
//		return endTime;
//	}
	
}
