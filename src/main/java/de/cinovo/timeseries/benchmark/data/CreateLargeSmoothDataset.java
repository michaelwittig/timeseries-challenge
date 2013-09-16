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
public final class CreateLargeSmoothDataset {
	
	public static void main(final String[] args) throws IOException {
		try (final BufferedWriter br = new BufferedWriter(new FileWriter("/tmp/smooth.large.data"))) {
			long time = 1378591663329l;
			float value = 91.45f;
			for (int i = 0; i < 100000000; i++) {
				br.write(time + "," + value + "\n");
				time += (long) (Math.random() * 20l) + 1l;
				if (Math.random() < 0.5d) {
					value += 0.01f;
				} else {
					value -= 0.01f;
				}
				value = Math.round(value * 100.0f) / 100.0f;
			}
		}
		
	}
}
