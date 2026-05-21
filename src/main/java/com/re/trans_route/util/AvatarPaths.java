package com.re.trans_route.util;

public final class AvatarPaths {
    private AvatarPaths() {
    }

    public static String forRoleSlug(String roleSlug) {
        if (roleSlug == null) {
            return "/assets/avatars/default-passenger.svg";
        }
        return switch (roleSlug.toUpperCase()) {
            case "ADMIN" -> "/assets/avatars/default-admin.svg";
            case "STAFF" -> "/assets/avatars/default-staff.svg";
            default -> "/assets/avatars/default-passenger.svg";
        };
    }

    public static String roleLabel(String roleSlug) {
        if (roleSlug == null) {
            return "User";
        }
        return switch (roleSlug.toUpperCase()) {
            case "ADMIN" -> "Administrator";
            case "STAFF" -> "Staff";
            case "PASSENGER" -> "Passenger";
            default -> roleSlug;
        };
    }
}
