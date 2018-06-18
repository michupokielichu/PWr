package com.myo.starter;

import java.util.Arrays;
import java.util.stream.Stream;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.FirmwareVersion;
import com.thalmic.myo.Myo;

public class EmgDataCollector extends AbstractDeviceListener {
	private byte[] emgSamples;

	public EmgDataCollector() {
	}

	@Override
	public void onPair(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
		if (emgSamples != null) {
			for (int i = 0; i < emgSamples.length; i++) {
				emgSamples[i] = 0;
			}
		}
	}

	@Override
	public void onEmgData(Myo myo, long timestamp, byte[] emg) {
		this.emgSamples = emg;
	}

	@Override
	public String toString() {
		return Arrays.toString(emgSamples);
	}

	public double[] toArray() {
		String data = toString();
		data = data.substring(1);
		data = data.substring(0, data.length() - 1);
		
		return Stream.of(data.split(",")) 
                .mapToDouble (Double::parseDouble)
                .toArray();
	}
}
