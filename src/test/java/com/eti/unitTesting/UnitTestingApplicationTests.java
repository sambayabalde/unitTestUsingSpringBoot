package com.eti.unitTesting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
		locations = "classpath:application.properties"
)
class UnitTestingApplicationTests {

	@Test
	void contextLoads() {
	}

}
