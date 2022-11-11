package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Course;
import model.Student;

public class TutorController {

private Statement statement;
	
	public TutorController() throws SQLException {
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseManagementDB", "root", "password");
		statement = connection.createStatement();
		
	}

	public boolean isValidCredentials(int tutorId, String password) {
		try {			
			String query = "SELECT password FROM tutor WHERE (id = "+ tutorId +")";
			ResultSet result = statement.executeQuery(query);
			if (result.next()) {
				if (password.equals(result.getString(1))) {
					return true;
				}
			}			
		} catch (SQLException e) {
			return false;
		}
		return false;
	}
	
	public boolean addCourse(int tutorId, int courseId) throws SQLException {
		
		List<Integer> courseList = new ArrayList<>();
		String query = "SELECT course FROM tutortocourse WHERE (tutor = "+tutorId+")";
		ResultSet r = statement.executeQuery(query);
		while (r.next()) {
			courseList.add(r.getInt(1));
		}
		
		query = "SELECT * FROM schedule WHERE (course = "+courseId+")";
		ResultSet result = statement.executeQuery(query);
		int[] currCourse = new int[5];
		while (result.next()) {
			for (int col=0; col<5; col++) {
				currCourse[col] = result.getInt(col+3);
			}
		}
		
		for (int course:courseList) {
			query = "SELECT * FROM schedule WHERE (course = "+course+")";
			result = statement.executeQuery(query);
			while (result.next()) {
				for (int col=0; col<5; col++) {
					if(result.getInt(col+3) != 0) {
						if (currCourse[col] == result.getInt(col+3)) {
							System.out.println("Can't  fit this course into tutor's schedule!");
							return false;
						}
					}
				}
			}
		}
		
		query = "INSERT INTO tutortocourse (tutor, course) VALUES ("+ tutorId + "," + courseId + ");";
		statement.executeUpdate(query);		
				
		return true;
		
	}

	public void printMyCourses(int tutorId, TutuorControllerListener<Course> listener) throws SQLException {
		
		String query = "SELECT course.id, courseName, course.semester FROM tutortocourse "
				+ "INNER JOIN course ON tutortocourse.course = course.id "
				+ "WHERE (tutor = "+ tutorId +")";
		ResultSet result = statement.executeQuery(query);
		
		List<Course> courseList = new ArrayList<>();
		
		while (result.next()) {
			Course course = new Course();
			course.setCourseId(result.getInt(1));
			course.setCourseName(result.getString(2));
			course.setSemester(result.getInt(3));
			courseList.add(course);						
		}
		
		listener.print(courseList);
		
	}

	public void printSchedule(int tutorId, SchedulePrinter<String[][]> listner) throws SQLException {
		
		String[][] schedule = prepareSchedule(tutorId);
		
		listner.print(schedule);
		
	}
	
	private String[][] prepareSchedule(int tutorId) throws SQLException{
		String[][] schedule = new String[5][5];
		for (String[] str:schedule) {
			Arrays.fill(str, "");
		}
		
		List<Integer> courseList = new ArrayList<>();
		String query = "SELECT course FROM tutortocourse WHERE (tutor = "+tutorId+")";
		ResultSet r = statement.executeQuery(query);
		while (r.next()) {
			courseList.add(r.getInt(1));
		}
		
		for (int course:courseList) {
			query = "SELECT * FROM schedule WHERE (course = "+course+")";
			ResultSet result = statement.executeQuery(query);
			while (result.next()) {
				for (int col=0; col<5; col++) {
					if(result.getInt(col+3) != 0) {
						schedule[result.getInt(col+3)-1][col] = result.getString(2);						
					}
				}
			}
		}
		
		return schedule;
	}

	public void printMyCourseCaption(int tutorId, TutuorControllerListener<Course> listener) throws SQLException {
		
		String query = "SELECT course.id, courseName FROM tutortocourse "
				+ "INNER JOIN course ON tutortocourse.course = course.id "
				+ "WHERE (tutor = "+ tutorId +")";
		ResultSet result = statement.executeQuery(query);
		
		List<Course> courseList = new ArrayList<>();
		
		while (result.next()) {
			Course course = new Course();
			course.setCourseId(result.getInt(1));
			course.setCourseName(result.getString(2));
			courseList.add(course);			
		}
		
		listener.print(courseList);
		
	}

	public void printStudents(int courseId, TutuorControllerListener<Student> listener) throws SQLException {
		
		String query = "SELECT student.rollno, student.name, department.deptName, student.email FROM studenttocourse "
							+ "INNER JOIN student ON studenttocourse.student = student.rollno "
							+ "INNER JOIN department ON student.department = department.id "
							+ "WHERE (studenttocourse.course = "+courseId+")";
		ResultSet result = statement.executeQuery(query);
		
		List<Student> studentList = new ArrayList<>();
		
		while (result.next()) {
			Student student = new Student();
			student.setRollNo(result.getShort(1));
			student.setName(result.getString(2));
			student.setDeptName(result.getString(3));
			student.setEmail(result.getString(4));
			studentList.add(student);						
		}
		
		listener.print(studentList);
		
	}

	public void changePassword(int tutorId, String newPassword) throws SQLException {
		
		String query = "UPDATE tutor SET password = \""+ newPassword +"\" WHERE (id = "+ tutorId +");";
		statement.executeUpdate(query);
		
	}



}
