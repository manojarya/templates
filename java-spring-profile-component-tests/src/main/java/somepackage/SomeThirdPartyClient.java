package somepackage;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Third party client.
 */
@Component
public class SomeThirdPartyClient {

	private static final Logger logger = LoggerFactory.getLogger(SomeThirdPartyClient.class);

	private final Map<String, Object> data;

	@Autowired
	public SomeThirdPartyClient() {
		this.data = new HashMap<>();
		this.data.put("key1", "val1");
		this.data.put("key2", "val2");
	}
	/**
	 * Fetches data for param.
	 *
	 * @param param
	 * @return
	 */
	public Map<String, Object> getData(final String param) {
		logger.info("getting data for param {}", param);
		return this.data;
	}

}
