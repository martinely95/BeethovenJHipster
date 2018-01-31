package kmdm.beethoven.web.rest;

import com.codahale.metrics.annotation.Timed;
import kmdm.beethoven.rmi.Converter;
import kmdm.beethoven.service.MelodyEntityService;
import kmdm.beethoven.service.UserService;
import kmdm.beethoven.service.dto.MelodyEntityDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

//import kmdm.beethoven.rmi.MidiResponse;

/**
 * REST controller for managing MelodyEntity.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private static final String ENTITY_NAME = "melodyEntity";

    private final MelodyEntityService melodyEntityService;

    private static String DOWNLOADS_FOLDER = ".\\src\\main\\downloads\\";

    @Inject
    UserService userService;



    public PlayerResource(MelodyEntityService melodyEntityService) {
        this.melodyEntityService = melodyEntityService;
    }

    @PostMapping("/beathoven/post")
    @Timed
    public void createMelodyEntity(@RequestBody String json) throws URISyntaxException, JSONException {

        try {
            Map<Integer, Boolean[][]> map = parse(json);
            SoundParser.parse(map);
        } catch (MidiUnavailableException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @PostMapping("/beathoven/saveInDB")
    @Timed
    public void persistMelodyEntity(@RequestBody String json) throws Exception {
        MelodyEntityDTO melodyEntity = new MelodyEntityDTO();
        melodyEntity.setName(ENTITY_NAME);
        melodyEntity.setContent(json);
        melodyEntity.setProfileId(userService.getCurrentlyLoggedInProfile().getId());
        melodyEntity.setCreatedDateTime(ZonedDateTime.now(ZoneId.systemDefault()));

        melodyEntityService.save(melodyEntity);
    }


    @GetMapping(value = "/beathoven/download{id}", produces="audio/midi")
    @Timed
    public void downloadMidiFile(@PathVariable Long id, HttpServletResponse response) throws URISyntaxException, JSONException {

        MelodyEntityDTO melodyEntityDTO = melodyEntityService.findOne(id);
        String json = melodyEntityDTO.getContent();

        String host = "localhost";

        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            System.out.println("==== JVM :" + jvmName);

            System.out.println("locating RMI Registry");
            Registry registry = LocateRegistry.getRegistry(host);

            System.out.println("looking up object: " + Converter.NAME);
            Converter checker = (Converter) registry.lookup(Converter.NAME);
            System.out.println("***********************************************");
            System.out.println("* invoking the remote method with a parameter *");
            System.out.println("***********************************************");

            String path = melodyEntityDTO.getPath();
            String midiFileName = null;
            String filePath = null;
            File dirToFile = new File(DOWNLOADS_FOLDER);
            if (!dirToFile.exists()) {
                dirToFile.mkdir();
            }

            if (path == null || (path!= null && path.length() == 0)) {
                midiFileName = checker.convertJsonToSequence(json);

                filePath = dirToFile.getCanonicalPath() + "\\" + midiFileName;

                System.out.println(dirToFile + "\\" + midiFileName);
                melodyEntityDTO.setPath(dirToFile + "\\" + midiFileName);
                melodyEntityService.save(melodyEntityDTO);
            } else {
                String[] folders = melodyEntityDTO.getPath().split("\\\\");
                midiFileName = folders[folders.length - 1];
                filePath  = dirToFile.getCanonicalPath() + "\\" + midiFileName;
            }

            File midiFile = new File(filePath);

            try (OutputStream out = response.getOutputStream()) {
                Path path2 = midiFile.toPath();
                Files.copy(path2, out);
                out.flush();

                String mimeType = URLConnection.guessContentTypeFromName(midiFile.getName());
                String contentDisposition = String.format("attachment; filename=%s", midiFile.getName());
                int fileSize = Long.valueOf(midiFile.length()).intValue();

                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", contentDisposition);
                response.setContentLength(fileSize);
            } catch (IOException e) {
                // handle exception
            }

            System.out.println("Playing.....");
            Sequencer sequencer = null;
            Sequence sequence = null;
            try {
                sequencer = MidiSystem.getSequencer();
                sequence = MidiSystem.getSequence(midiFile);
                sequencer.setSequence(sequence);
                sequencer.open();
                sequencer.start();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(true) {
                if(sequencer.isRunning()) {
                    try {
                        Thread.sleep(1000); // Check every second
                    } catch(InterruptedException ignore) {
                        break;
                    }
                } else {
                    break;
                }
            }

            sequencer.stop();
            sequencer.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * GET  /profiles : get all the profiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of profiles in body
     */
    @GetMapping("/beathoven/get")
    @Timed
    public String getAllProfiles() {
        log.debug("Test REST GET request");
        return "yo man, you're good";
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
                    System.out.println(ja2.get(k));
                    boolMatr[j][k] = (Boolean) ja2.get(k);
                }
                map.put(instrumentColor.get(objName), boolMatr);
            }
        }
        return map;
    }
}
