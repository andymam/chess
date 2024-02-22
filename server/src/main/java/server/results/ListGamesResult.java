package server.results;

public record ListGamesResult(int gameID, String whiteUsername, String blackUsername, String gameName, String message) { }
