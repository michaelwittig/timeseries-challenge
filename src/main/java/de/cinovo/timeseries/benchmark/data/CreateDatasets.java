package de.cinovo.timeseries.benchmark.data;

import java.io.IOException;

/**
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class CreateDatasets {
	
	public static void main(final String[] args) throws IOException {
		CreateChaoticDataset.main(args);
		CreateSmoothDataset.main(args);
		CreateLargeChaoticDataset.main(args);
		CreateLargeSmoothDataset.main(args);
	}
	
}
