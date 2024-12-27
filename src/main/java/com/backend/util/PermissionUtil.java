package com.backend.util;

public class PermissionUtil {

    // 권한 추가
    public static int addPermission(int existingPermissions, PermissionType permission) {
        return existingPermissions | permission.getValue();
    }

    // 권한 제거
    public static int removePermission(int existingPermissions, PermissionType permission) {
        return existingPermissions & ~permission.getValue();
    }

    // 권한 확인 (단일)
    public static boolean hasPermission(int permissions, PermissionType permission) {
        return (permissions & permission.getValue()) != 0;
    }

    // 모든 권한 만족 여부
    public static boolean hasAllPermissions(int permissions, PermissionType... requiredPermissions) {
        for (PermissionType permission : requiredPermissions) {
            if ((permissions & permission.getValue()) == 0) {
                return false;
            }
        }
        return true;
    }

    // 하나 이상의 권한 만족 여부
    public static boolean hasAnyPermission(int permissions, PermissionType... requiredPermissions) {
        for (PermissionType permission : requiredPermissions) {
            if ((permissions & permission.getValue()) != 0) {
                return true;
            }
        }
        return false;
    }
}
