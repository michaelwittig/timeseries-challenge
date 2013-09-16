package de.cinovo.timeseries.benchmark.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class CreateChaoticDataset {
	
	public static void main(final String[] args) throws IOException {
		try (final BufferedWriter br = new BufferedWriter(new FileWriter("/tmp/chaotic.data"))) {
			long time = 1378591663329l;
			for (int i = 0; i < 500000; i++) {
				final float value = Math.round(Math.random() * 1000000.0f * 100.0f) / 100.0f;
				br.write(time + "," + value + "\n");
				time += (long) (Math.random() * 1000l) + 1l;
			}
		}
	}
}
