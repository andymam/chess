package dataAccessTests;

import dataAccess.*;
import records.UserData;

public class SQLUserDAOTest {
  public static void main(String[] args) {
    // Initialize SQLUserDAO
    SQLUserDAO userDAO;
    try {
      userDAO = new SQLUserDAO();
    } catch (DataAccessException e) {
      System.out.println("Error initializing SQLUserDAO: " + e.getMessage());
      return;
    }

    // Test addUser
    UserData newUser = new UserData("testuser", "password123", "testuser@example.com");
    try {
      userDAO.addUser(newUser);
      System.out.println("User added successfully.");
    } catch (DataAccessException e) {
      System.out.println("Error adding user: " + e.getMessage());
    }

//    // Test getUser
//    try {
//      UserData retrievedUser = userDAO.getUser("testuser");
//      if (retrievedUser != null) {
//        System.out.println("User retrieved successfully: " + retrievedUser);
//      } else {
//        System.out.println("User not found.");
//      }
//    } catch (DataAccessException e) {
//      System.out.println("Error getting user: " + e.getMessage());
//    }

//    // Test getAllUsers
//    try {
//      List<UserData> allUsers = userDAO.getAllUsers();
//      System.out.println("All users:");
//      for (UserData user : allUsers) {
//        System.out.println(user);
//      }
//    } catch (DataAccessException e) {
//      System.out.println("Error getting all users: " + e.getMessage());
//    }

    // Test clearUsers
    try {
      userDAO.clearUsers();
      System.out.println("All users cleared successfully.");
    } catch (DataAccessException e) {
      System.out.println("Error clearing users: " + e.getMessage());
    }
  }
}
