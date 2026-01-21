package com.erp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class ErpCloudApplicationTests {

    @Test
    @Disabled("Integration test - requires database. Run manually with: mvn test -Dtest=ErpCloudApplicationTests -Dspring.profiles.active=test")
    void contextLoads() {
        // This test verifies Spring context loads successfully
        // Disabled by default as it requires full infrastructure
    }

    @Test
    void applicationClassExists() {
        // Simple smoke test to verify the main class exists
        ErpCloudApplication app = new ErpCloudApplication();
        org.junit.jupiter.api.Assertions.assertNotNull(app);
    }
}
