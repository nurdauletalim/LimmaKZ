package kz.reself.limma.gatewaycrm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_CONTENT_MANAGER("ROLE_CONTENT_MANAGER");

    private final String name;

    public String getName() {
        return name;
    }
}
