package coding.mentor;
import java.util.Objects;
import java.util.Scanner;
import coding.mentor.db.Database;
import coding.mentor.service.*;
import coding.mentor.data.*;
public class Main {
	public static void main (String[] args) {
		
		final int LOGIN = 1;
		final int REGISTER = 2;
		
		Database.initDB();
		User user = null;
		do {
			do {
				switch(mainMenu()){
					case LOGIN:
						user = login();
						break;
					case REGISTER:
						register();
						break;
					default:
						System.out.println("Invalid input. Please try again");
				}
				if (!Objects.isNull(user)) {
					user = courseList(user);
				}
			}while(Objects.isNull(user));
		}while (true);
	}
	
	public static User courseList(User user) {
		
		final int LOGOUT = -1;
		final int SHOW_USER_COURSE = 0;
		
		Scanner scan = new Scanner (System.in);
		do {
			System.out.println("--------------------------------------------------");
			System.out.println("-1. Logout");
			System.out.println("0. Your courses");
			CourseService.showAllCourses();
			System.out.print("Your selection: ");
			int selection = scan.nextInt();
			switch (selection) {
				case (LOGOUT):
					return (null);
				case (SHOW_USER_COURSE):
					yourCourse(user);
					break;
				default:
					showCourseDetails(user,Database.COURSE_DB.get(selection-1));
			}
		}while(true);
		
	}
	
	public static void showCourseDetails(User user, Course course) {
		
		final int ADD_NEW_COURSE = 1;
		final int SHOW_MENTOR_DETAIL = 3;
		
		Scanner scan = new Scanner (System.in);
		

		CourseService.showCourseDetails(course);
		System.out.println("Do you want to enroll this course?");
		System.out.println("1. Register");
		System.out.println("2. No");
		System.out.println("3. View Mentor Detail");
		System.out.print("Your selection: ");
		int selection = scan.nextInt();
		if (selection == ADD_NEW_COURSE) {
			if (UserService.registerNewCourse(user, course)) {
				System.out.println("Your new course has been added.");
				return;
			}
			System.out.println("Something went wrong. Please try again.2");
		}
		if (selection==SHOW_MENTOR_DETAIL) {
			CourseService.showMentorByCourse(course);
		}

	}
	
	public static void yourCourse(User user) {
		Scanner scan = new Scanner (System.in);
		
		System.out.println("--------------------------------------------------");
		UserService.showRegisterCoursesByUser(user);
		
	}
	
	public static void register() {
		Scanner scan = new Scanner (System.in);
		
		System.out.println("--------------------------------------------------");
		System.out.println("Please enter your register information.");
		System.out.print("UserID: ");
		int id = scan.nextInt();
		System.out.print("Password: ");
		String password = scan.next();
		System.out.print("Name: ");
		String name = scan.next();
		
		if (UserService.registerNewUser(id,password,name) == true) {
			System.out.println("Successfully registered.");
		}
		
		if (UserService.registerNewUser(id,password,name) == false) {
			System.out.println("User ID has been used. Please try again with another.");
		}
	}
	
	public static User login() {
		
		final int USERID_OR_PASSWORD_HAS_BEEN_WRONG= -1;
		final int ACCOUNT_HAS_BEEN_LOCKED = -2;
		
		Scanner scan = new Scanner (System.in);

		System.out.println("--------------------------------------------------");
		System.out.println("Please enter your userID and password.");
		System.out.print("UserID: ");
		int id = scan.nextInt();
		System.out.print("Password: ");
		String password = scan.next();
		
		int index = UserService.login(id, password);
		if (index == USERID_OR_PASSWORD_HAS_BEEN_WRONG) {
			System.out.println("Your login has been failed. Please try agin");
			return (null);
		}
		if (index == ACCOUNT_HAS_BEEN_LOCKED) {
			System.out.println("Your account has been locked.");
			return (null);
		}
		if (index >=0) {
			return (Database.USER_DB.get(index));
		}
		return null;
		
	}
	
	public static int mainMenu() {
		Scanner scan = new Scanner (System.in);
		System.out.println("Welcome to Coding Mentor.");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.print("Your selection: ");
		return(scan.nextInt());
	}
}
