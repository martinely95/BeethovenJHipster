package kmdm.beethoven.web.rest;

import com.codahale.metrics.annotation.Timed;
import kmdm.beethoven.service.MelodyEntityService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing MelodyEntity.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private static final String ENTITY_NAME = "melodyEntity";

    private final MelodyEntityService melodyEntityService;

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
        instrumentColor.put("green", 127);
        JSONObject obj = new JSONObject(json);
        String objName = null;
        Map<Integer, Boolean[][]> map = new HashMap<>();

        Boolean[][] boolMatr = new Boolean[6][15];

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
