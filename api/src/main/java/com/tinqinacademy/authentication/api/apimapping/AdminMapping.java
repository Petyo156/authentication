package com.tinqinacademy.authentication.api.apimapping;

import java.util.List;

public class AdminMapping {
    private static final List<String> ADMIN_PATHS = List.of(
            RestApiMappingAuthentication.POST_DEMOTE_PATH,
            RestApiMappingAuthentication.POST_PROMOTE_PATH
    );

    public static List<String> getPaths() {
        return ADMIN_PATHS;
    }
}