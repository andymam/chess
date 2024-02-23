package server.requests;

import records.AuthData;

public record LogoutRequest(AuthData authtoken) { }
