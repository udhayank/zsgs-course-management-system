package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Course;
import model.Student;
import model.Tutor;

public class AdminController {
	
	private Statement statement;
	private Connection connection;
	
	public AdminController() throws SQLException {
		
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseManagementDB", "root", "password");
		statement = connection.createStatement();
		
	}

	public boolean isValidCredentials(String password) {
		if (password.equals("password")) {
			return true;
		}
		return false;
	}
	
	public int addCourse(Course course) throws SQLException {
		
		String query = String.format("INSERT INTO course (courseName, department, semester, credits) VALUES (\"%s\", %d, %d, %d)"
				, course.getCourseName(), course.getDepartment(), course.getSemester(), course.getCredits());
		
		PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		stmt.execute();
		
		ResultSet courseId = stmt.getGeneratedKeys();
		
		if (courseId.next()) {
			return courseId.getInt(1);
		}
		
		return -1;
		
	}

	public void printCourseList(AdminControllerListener<Course> listner) throws SQLException {
		
		String query = "SELECT course.id, courseName, deptName, semester, credits FROM course INNER JOIN department ON course.department = department.id;";
		ResultSet result = statement.executeQuery(query);
		
//		System.out.println("--> got result");
		
		List<Course> courseList = new ArrayList<>();
		
		while (result.next()) {	
			Course course = new Course();
			
			course.setCourseId(result.getInt(1));
			course.setCourseName(result.getString(2));
			course.setDeptName(result.getString(3));
			course.setSemester(result.getInt(4));
			course.setCredits(result.getInt(5));
			
			courseList.add(course);
								
		}
		
		listner.print(courseList);
		
	}
	
	public void printStudentList(AdminControllerListener<Student> listner) throws SQLException {
		String query = "SELECT name, deptName, semester, credits FROM student INNER JOIN department ON student.department = department.id;";
		ResultSet result = statement.executeQuery(query);
		
		List<Student> studentList = new ArrayList<>();
		
		while (result.next()) {
			Student student = new Student();
			student.setName(result.getString(1));
			student.setDeptName(result.getString(2));
			student.setSemester(result.getInt(3));
			student.setCredits(result.getInt(4));
			studentList.add(student);
		}
		
		listner.print(studentList);
		
	}

	public void addStudent(Student student) throws SQLException {
		
		String query = String.format("INSERT INTO student (name, semester, department, email) VALUES (\"%s\", %d, %d, \"%s\");"
							, student.getName(), student.getSemester(), student.getDepartment(), student.getEmail());
		statement.executeUpdate(query);
	}

	public void addTutor(Tutor tutor) throws SQLException {
		
		String query = String.format("INSERT INTO tutor (name, department, email) VALUES (\"%s\", %d, \"%s\");"
				, tutor.getName(), tutor.getDepartment(), tutor.getEmail());
		statement.executeUpdate(query);
		
	}

	public void printTutorList(AdminControllerListener<Tutor> listener) throws SQLException {
		
		String query = "SELECT tutor.id, name, deptName, email FROM tutor INNER JOIN department ON tutor.department = department.id;";
		ResultSet result = statement.executeQuery(query);
		
		List<Tutor> tutorList = new ArrayList<>();
		
		while (result.next()) {
			Tutor tutor = new Tutor();
			tutor.setId(result.getInt(1));
			tutor.setName(result.getString(2));
			tutor.setDeptName(result.getString(3));
			tutor.setEmail(result.getString(4));
			tutorList.add(tutor);
		}
		
		listener.print(tutorList);
		
	}

	public void addSchedule(int courseId, int[] slots) throws SQLException {
		
		String querry = String.format("INSERT INTO schedule (course, monday, tuesday, wednesday, thursday, friday) VALUES (%d, %d, %d, %d, %d, %d)"
								, courseId, slots[0], slots[1], slots[2], slots[3], slots[4]);
		
		statement.executeUpdate(querry);
		
	}

	
}
