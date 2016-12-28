package org.moskito.javaagent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import net.anotheria.moskito.webui.embedded.StartMoSKitoInspectBackendForRemote;
import net.anotheria.util.StringUtils;
import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;
import org.distributeme.core.RMIRegistryUtil;
import org.distributeme.core.conventions.SystemProperties;
import org.moskito.controlagent.endpoints.rmi.RMIEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main moskito-agent-light entry-point.
 */
public class LightTransformationAgent implements ClassFileTransformer {
	/**
	 * Backend port default value.
	 */
	private static final int MOSKITO_BACKEND_PORT_DEFAULT_VALUE = -1;


	/**
	 * Logging util.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LightTransformationAgent.class);
	/**
	 * {@link ClassPreProcessorAgentAdapter} instance.
	 */
	private final ClassPreProcessorAgentAdapter classPreProcessorAgentAdapter = new ClassPreProcessorAgentAdapter();

	/**
	 * JVM hook to statically load the javaagent at startup.
	 * <p/>
	 * After the Java Virtual Machine (JVM) has initialized, the premain method
	 * will be called. Then the real application main method will be called.
	 *
	 * @param args
	 * 		arguments
	 * @param inst
	 * 		{@link Instrumentation}
	 */
	public static void premain(String args, Instrumentation inst) {
		LOG.info("premain method invoked with args: {} and inst: {}", args, inst);
		final ClassFileTransformer aspectTransformationAgent = new LightTransformationAgent();
		inst.addTransformer(aspectTransformationAgent);
		startMoskitoBackend();
	}


	/**
	 * JVM hook to dynamically load javaagent at runtime.
	 * <p/>
	 * The agent class may have an agentmain method for use when the agent is
	 * started after VM startup.
	 *
	 * @param args
	 * 		arguments
	 * @param inst
	 * 		{@link Instrumentation} instance
	 * @throws Exception
	 * 		in scope of error
	 */
	public static void agentmain(String args, Instrumentation inst) throws Exception {
		LOG.info("agentmain method invoked with args: {} and inst: {}", args, inst);
		inst.addTransformer(new LightTransformationAgent());
		startMoskitoBackend();
	}

	/**
	 * Perform moskito-backend start, in case if enabled by configuration!.
	 */
	private static void startMoskitoBackend() {
		try {
			int moskitoBackendPort = getBackendPort();
			LOG.info("Starting Moskito-backend on using " + moskitoBackendPort + " port! !");
			StartMoSKitoInspectBackendForRemote.startMoSKitoInspectBackend();
			RMIEndpoint.startRMIEndpoint();
			LOG.info("Started Moskito-backend on " + RMIRegistryUtil.getRmiRegistryPort() + " port!");
		} catch (final Throwable mise) {
			LOG.error("Failed to start moskitoInspect backend. [" + mise.getMessage() + "]", mise);
		}
	}

	/**
	 * Fetch backend port.
	 *
	 * @return backend port property
	 */
	private static int getBackendPort() {
		try {
			if (StringUtils.isEmpty(SystemProperties.LOCAL_RMI_REGISTRY_PORT.get()))
				return MOSKITO_BACKEND_PORT_DEFAULT_VALUE;
			return SystemProperties.LOCAL_RMI_REGISTRY_PORT.getAsInt();
		} catch (final NumberFormatException e) {
			return MOSKITO_BACKEND_PORT_DEFAULT_VALUE;
		}
	}

	@Override
	public byte[] transform(final ClassLoader loader, final String className, Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
							final byte[] classfileBuffer) throws IllegalClassFormatException {
		//perform no transformations in light scope!
		return classfileBuffer;
	}


}
