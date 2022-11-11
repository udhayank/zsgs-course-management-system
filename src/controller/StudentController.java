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

public class StudentController {
	
	private Statement statement;
		
	
	public StudentController() throws SQLException {
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseManagementDB", "root", "password");
		statement = connection.createStatement();
				
	}

	public boolean isValidCredentials(int rollNo, String password) {
		
		try {
			
			String query = "SELECT password FROM student WHERE (rollNo = "+ rollNo +")";
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

	public boolean addCourse(int rollNo, int courseId) throws SQLException {
		
		List<Integer> courseList = new ArrayList<>();
		String query = "SELECT course FROM studenttocourse WHERE (student = "+rollNo+")";
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
							System.out.println("Can't  fit this course into your schedule!");
							return false;
						}
					}
				}
			}
		}
		
		query = "INSERT INTO studenttocourse (student, course) VALUES ("+ rollNo + "," + courseId + ");";
		statement.executeUpdate(query);
		
		int credits = 0;
		r = statement.executeQuery("SELECT credits from student where (rollNo = "+ rollNo +");");
		if (r.next()) {
			credits += r.getInt(1);
		}
		r = statement.executeQuery("SELECT credits FROM course WHERE (id = "+ courseId +");");
		if (r.next()) {
			credits += r.getInt(1);
		}
		
		query = "UPDATE student SET credits = "+ credits +" WHERE (rollNo = "+ rollNo +");";
		statement.executeUpdate(query);
		
		return true;
		
	}

	public void printMyCourses(int rollNo, StudentContorllerListener<Course> listener) throws SQLException {
		
		String query = "SELECT course.id, courseName, course.semester, credits FROM studenttocourse "
				+ "INNER JOIN course ON studenttocourse.course = course.id "
				+ "WHERE (student = "+ rollNo +")";
		ResultSet result = statement.executeQuery(query);
		
		List<Course> courseList = new ArrayList<>();
		
		while (result.next()) {
			Course course = new Course();
			course.setCourseId(result.getInt("course.id"));
			course.setCourseName(result.getString("courseName"));
			course.setSemester(result.getInt("course.semester"));
			course.setCredits(result.getInt("credits"));
			courseList.add(course);
		}		
		
//		return courseList;
		listener.print(courseList);		
		
	}

	public void deleteCourse(int rollNo, int courseId) throws SQLException {
		
		String query = "SELECT id FROM studenttocourse WHERE (course = "+ courseId +")";
		ResultSet r = statement.executeQuery(query);
		r.next();
		int id = r.getInt(1);
		
		query = "DELETE FROM studenttocourse WHERE (id = "+ id +")";
		statement.executeUpdate(query);
		
	}

	public void changePassword(int rollNo, String newPassword) throws SQLException {
		
		String query = "UPDATE student SET password = \""+ newPassword +"\" WHERE (rollNo = "+ rollNo +");";
		statement.executeUpdate(query);
		
	}

	public void printSchedule(int rollNo, SchedulePrinter<String[][]> listener) throws SQLException {
		
		String[][] schedule = prepareSchedule(rollNo);
		
		listener.print(schedule);
		
	}
	
	private String[][] prepareSchedule(int rollNo) throws SQLException{
		String[][] schedule = new String[5][5];
		for (String[] str:schedule) {
			Arrays.fill(str, "");
		}
		
		List<Integer> courseList = new ArrayList<>();
		String query = "SELECT course FROM studenttocourse WHERE (student = "+rollNo+")";
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

	public void printMyCourseCaption(int rollNo, StudentContorllerListener<Course> listener) throws SQLException {
		
		String query = "SELECT course.id, courseName FROM studenttocourse "
				+ "INNER JOIN course ON studenttocourse.course = course.id "
				+ "WHERE (student = "+ rollNo +")";
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
	
}


