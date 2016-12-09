import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.jetbrains.annotations.Nullable;

public class MapExampleCharacterCountTask extends ComputeTaskAdapter<String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> nodes, String arg) {
		Map<ComputeJob, ClusterNode> map = new HashMap<>();

		Iterator<ClusterNode> it = nodes.iterator();

		for (final String word : arg.split(" ")) {
			// If we used all nodes, restart the iterator.
			if (!it.hasNext())
				it = nodes.iterator();

			ClusterNode node = it.next();

			map.put(new ComputeJobAdapter() {
				@Nullable
				@Override
				public Object execute() {
					System.out.println();
					System.out.println(">>> Printing '" + word + "' on this node from ignite job.");

					// Return number of letters in the word.
					return word.length();
				}
			}, node);
		}

		return map;
	}

	/** {@inheritDoc} */
	@Nullable
	@Override
	public Integer reduce(List<ComputeJobResult> results) {
		int sum = 0;

		for (ComputeJobResult res : results)
			sum += res.<Integer> getData();

		return sum;
	}
}
