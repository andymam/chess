package server.results;

import records.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games, String message) { }
