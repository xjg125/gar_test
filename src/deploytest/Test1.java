package deploytest;

import java.util.Arrays;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.deployment.uri.UriDeploymentSpi;

public class Test1 {

	public static void main(String[] args) {


		Ignition.setClientMode(true);

		IgniteConfiguration cfg = new IgniteConfiguration();
		UriDeploymentSpi deploymentSpi = new UriDeploymentSpi();
		
		deploymentSpi.setUriList(Arrays.asList("http://freq=5000@127.0.0.1/deptest/deployment.html"));
		cfg.setDeploymentSpi(deploymentSpi);
		Ignite ignite = Ignition.start(cfg);
		
		ignite.compute().execute("MapExampleCharacterCountTask", "Hello Ignite Enabled World!");

		ignite.close();

	}

}
