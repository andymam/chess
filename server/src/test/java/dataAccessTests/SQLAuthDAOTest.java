package dataAccessTests;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import records.AuthData;

import java.util.List;

public class SQLAuthDAOTest {
  public static void main(String[] args) {
    // Initialize SQLAuthDAO
    SQLAuthDAO authDAO;
    try {
      authDAO = new SQLAuthDAO();
    } catch (DataAccessException e) {
      System.out.println("Error initializing SQLAuthDAO: " + e.getMessage());
      return;
    }

    // Test addAuth
    AuthData newAuthData = new AuthData("token123", "testuser");
    try {
      authDAO.addAuth(newAuthData);
      System.out.println("Auth data added successfully.");
    } catch (DataAccessException e) {
      System.out.println("Error adding auth data: " + e.getMessage());
    }

    // Test getAuth
    try {
      AuthData retrievedAuthData = authDAO.getAuth("token123");
      if (retrievedAuthData != null) {
        System.out.println("Auth data retrieved successfully: " + retrievedAuthData);
      } else {
        System.out.println("Auth data not found.");
      }
    } catch (DataAccessException e) {
      System.out.println("Error getting auth data: " + e.getMessage());
    }

    // Test inAuths
    try {
      boolean isInAuths = authDAO.inAuths("token123");
      System.out.println("Is token in auths: " + isInAuths);
    } catch (DataAccessException e) {
      System.out.println("Error checking auths: " + e.getMessage());
    }

    // Test deleteAuthorization
    try {
      authDAO.deleteAuthorization("token123");
      System.out.println("Authorization deleted successfully.");
    } catch (DataAccessException e) {
      System.out.println("Error deleting authorization: " + e.getMessage());
    }

    // Test clearAuths
    try {
      authDAO.clearAuths();
      System.out.println("All auths cleared successfully.");
    } catch (DataAccessException e) {
      System.out.println("Error clearing auths: " + e.getMessage());
    }
  }
}
