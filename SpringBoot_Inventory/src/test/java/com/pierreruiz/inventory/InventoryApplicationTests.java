package com.pierreruiz.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"INVENTORY_READER_PASSWORD=test-reader-password",
		"INVENTORY_ADMIN_PASSWORD=test-admin-password",
		"spring.datasource.url=jdbc:h2:mem:application-tests",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"server.ssl.enabled=false"
})
class InventoryApplicationTests {

	@Test
	void contextLoads() {
	}

}
