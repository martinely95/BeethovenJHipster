package kmdm.beethoven.rmi.server.impl;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.*;

import kmdm.beethoven.rmi.Converter;
import kmdm.beethoven.rmi.MidiResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConverterImpl extends UnicastRemoteObject implements Converter, Serializable{

    private static final int RESOLUTION = 40;
    private static final int TIME_IN_MILISECONDS = 190;
    private static final int VELOCITY = 80;
    private static String DOWNLOADS_FOLDER = ".\\src\\main\\downloads\\";

    private Track track = null;
    private Sequence sequence = null;
    private int currentTicks;

	private static final long serialVersionUID = 1L;

	public ConverterImpl() throws RemoteException {
		super();
	}

	@Override
	public String convertJsonToSequenceTest(String json) throws RemoteException {

		return "Rmi working";
	}

    @Override
    public String convertJsonToSequence(String json) throws RemoteException {
	    System.out.println("======= convertJsonToSequence ============");

        try {
            Map<Integer, Boolean[][]> map = parse(json);
            parse(map);

            File dirToFile = new File(DOWNLOADS_FOLDER);
            String midiFileName = System.currentTimeMillis()+ ".mid";
            String fileName = dirToFile.getCanonicalPath() + "\\" + midiFileName;

            File file = new File(fileName);
            MidiSystem.write(sequence, 0, file);

            return midiFileName;

        } catch (MidiUnavailableException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, Boolean[][]> parse(String json) throws IOException, JSONException {

        Map<String,Integer> instrumentColor = new HashMap<>();
        instrumentColor.put("black", 32);
        instrumentColor.put("red", 55);
        instrumentColor.put("orange", 107);
        instrumentColor.put("yellow", 113);
        instrumentColor.put("blue", 118);
        instrumentColor.put("purple", 5);
        instrumentColor.put("brown", 128);
        instrumentColor.put("darkgray", 80);
        instrumentColor.put("green", 127);
        JSONObject obj = new JSONObject(json);
        String objName = null;
        Map<Integer, Boolean[][]> map = new HashMap<>();

        Boolean[][] boolMatr = new Boolean[9][15];

        for (int i = 0; i < obj.names().length(); i++) {
            objName = obj.names().getString(i);

            JSONArray ja = (JSONArray) obj.get(objName);
            for (int j = 0; j < ja.length(); j++) {
                JSONArray ja2 = (JSONArray) ja.get(j);

                for (int k = 0; k < ja2.length(); k++) {
                    //System.out.println(ja2.get(k));
                    boolMatr[j][k] = (Boolean) ja2.get(k);
                }
                map.put(instrumentColor.get(objName), boolMatr);
            }
        }
        return map;
    }

    public void parse(Map<Integer, Boolean[][]> map) throws MidiUnavailableException, InterruptedException, IOException {
        //System.out.println(map);
        Boolean[][] tmp = map.get(map.keySet().iterator().next());

        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        MidiChannel[] channels = synthesizer.getChannels();

        int index = 0;
        Map<Integer,MidiChannel> colChannels = new HashMap<>();
        //System.out.println(colChannels.size());
        Map<Integer, Integer> notes = new HashMap<>();

        for(int color : map.keySet()){
            colChannels.put(color, channels[index]);
            //System.out.println(colChannels);
            index++;
        }

        try {
            sequence = new Sequence(Sequence.SMPTE_30, RESOLUTION);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        track = sequence.createTrack();
        currentTicks = 0;

        for (int cols = 0; cols < tmp[0].length; cols++) {
            for (int row = 0; row < tmp.length; row++) {
                for(int color : colChannels.keySet()) {
                    if(map.get(color)[row][cols]) {
                        addNote(30+10*row, 50, 0);
                    } else {
                        addSilence( 10, 0);
                    }
                }
            }
            Thread.sleep(TIME_IN_MILISECONDS);
            notes.clear();
        }
        colChannels.clear();
    }

    public void addNote(int midiKey, int numberOfTicks, int channel) {
        track.add(createNoteEvent(ShortMessage.NOTE_ON, midiKey, VELOCITY, currentTicks, channel));
        currentTicks = numberOfTicks + currentTicks;
        track.add(createNoteEvent(ShortMessage.NOTE_OFF, midiKey, 0, currentTicks, channel));
    }

    public void addSilence(final int numberOfTicks, int channel) {
        //I do not know how to add silence => velocity zero note events
        track.add(createNoteEvent(ShortMessage.NOTE_ON, 69, 0, currentTicks, channel));
        currentTicks = numberOfTicks + currentTicks;
        track.add(createNoteEvent(ShortMessage.NOTE_OFF, 69, 0, currentTicks, channel));
    }

    private static MidiEvent createNoteEvent(int nCommand, int nKey, int nVelocity, long lTick, int channel) {
        final ShortMessage message = new ShortMessage();
        try {
            message.setMessage(nCommand, channel, nKey, nVelocity);
        } catch (final InvalidMidiDataException e) {
            e.printStackTrace();
            // Will this exception ever occur?
        }
        return new MidiEvent(message, lTick);
    }

}
