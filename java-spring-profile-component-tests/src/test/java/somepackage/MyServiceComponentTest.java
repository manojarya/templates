

package somepackage;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Service component test.
 *
 * Change @ActiveProfiles which is by default, can be set in IDE explicitely to
 * "prod" to invoke prod mode.
 *
 * <mvn clean install> will run tests in default mode scan class with profile.
 * <mvn clean install -Dspring.profiles.active=prod> will run tests in prod mode
 *
 */
@ContextConfiguration("classpath:test-context.xml")
// @ActiveProfiles("prod") to run profile from IDE
public class MyServiceComponentTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private MyService myService;

	@Test
	public void given_when_then() {

		final Map<String, Object> data = this.myService.someFunction("p1");

		final Map<String, Object> expected = new HashMap<>();
		expected.put("key1", "val1");
		expected.put("key2", "val2");

		Assert.assertEquals(data, expected);

	}

}
