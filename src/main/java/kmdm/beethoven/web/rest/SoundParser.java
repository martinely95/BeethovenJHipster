package kmdm.beethoven.web.rest;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundParser {

	private static final int TIME_IN_MILISECONDS = 190;

	//
	// final JToggleButton[][] buttons
	public static void parse(Map<Integer, Boolean[][]> map) throws MidiUnavailableException, InterruptedException, IOException {

		System.out.println(map);
		Boolean[][] tmp = map.get(map.keySet().iterator().next());

		Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		MidiChannel[] channels = synthesizer.getChannels();

		int index = 0;
		Map<Integer,MidiChannel> colChannels = new HashMap<>();
		System.out.println(colChannels.size());
		Map<Integer, Integer> notes = new HashMap<>();

		for(int color : map.keySet()){
			colChannels.put(color, channels[index]);
			System.out.println(colChannels);
			index++;
		}

		index = 0;
		for (int cols = 0; cols < tmp[0].length; cols++) {
			for (int row = 0; row < tmp.length; row++) {
				for(int color : colChannels.keySet())
				{
					System.out.println("Before if ::::::::" + map.get(color)[row][cols]);
					if(map.get(color)[row][cols])
					{
						colChannels.get(color).programChange(color);
						colChannels.get(color).noteOn(30+10*row, 80);
					}
				}
			}
			Thread.sleep(TIME_IN_MILISECONDS);
			notes.clear();
			index = 0;
		}
		colChannels.clear();
	}

}
