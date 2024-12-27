package com.backend.util;

import java.util.List;
import java.util.Objects;

public enum PermissionType {
    READ(1),
    WRITE(2),
    FULL(4),
    SHARE(8);
    private final int value;

    PermissionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // 특정 권한 포함 여부 확인
    public static boolean hasPermission(int permissions, PermissionType type) {
        return (permissions & type.getValue()) == type.getValue();
    }

    // 권한 추가
    public static int addPermission(int permissions, PermissionType type) {
        return permissions | type.getValue();
    }

    // 권한 제거
    public static int removePermission(int permissions, PermissionType type) {
        return permissions & ~type.getValue();
    }

    public static int combinePermissions(List<PermissionType> permissionTypes) {
        if (permissionTypes == null || permissionTypes.isEmpty()) {
            throw new IllegalArgumentException("PermissionTypes list cannot be null or empty");
        }

        return permissionTypes.stream()
                .filter(Objects::nonNull) // null 값 필터링
                .mapToInt(PermissionType::getValue)
                .reduce(0, (a, b) -> a | b); // 비트 OR 연산
    }


    // 문자열을 PermissionType으로 변환
    public static PermissionType fromString(String name) {
        for (PermissionType type : PermissionType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PermissionType: " + name);
    }
}
