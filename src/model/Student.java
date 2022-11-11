package model;

import java.util.List;

public class Student {

	private int rollNo;
	private String name;
	private int semester;
	private int department;
	private String deptName;
	private String email;
	private int credits;
	private List<Course> currentCourseList;
	private List<Course> completedCourseList;
	
	public Student() {
		
	}
	
	public Student(int rollNo,String name) {
		this.name = name;
	}
	
	public int getRollNo() {
		return rollNo;
	}
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	public int getDepartment() {
		return department;
	}
	public void setDepartment(int department) {
		this.department = department;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public List<Course> getCurrentCourseList() {
		return currentCourseList;
	}
	public void setCurrentCourseList(List<Course> currentCourseList) {
		this.currentCourseList = currentCourseList;
	}
	public void addCurrentCourseList(Course currentCourse) {
		this.currentCourseList.add(currentCourse);
	}
	public List<Course> getCompletedCourseList() {
		return completedCourseList;
	}
	public void setCompletedCourseList(List<Course> completedCourseList) {
		this.completedCourseList = completedCourseList;
	}
	public void addCompletedCourseList(Course completedCourse) {
		this.completedCourseList.add(completedCourse);
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
	
	
}
