package uk.co.civica.microservice.util.testing.utils;

import uk.co.civica.microservice.util.testing.utils.ConditionalIgnoreRule.IgnoreCondition;

/**
 * When there is an environment variable <code>cleanTest.integration</code> set to any none zero then the cleanTest will NOT be ignored.
 * Add to the VM arguments
 * <code>-DcleanTest.integration=true</code>
 */
public class IgnoreWhenNotIntegration implements IgnoreCondition {
	public boolean shouldIgnore() {
		return !Boolean.getBoolean("cleanTest.integration");
	}
}
