import com.monst.polylabel.PolyLabel;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class PolyLabelTest {

    static Integer[][][] water1, water2;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray water1Json = (JSONArray) parser.parse(new FileReader("src/test/resources/water1.json"));
        JSONArray water2Json = (JSONArray) parser.parse(new FileReader("src/test/resources/water2.json"));

        water1 = (Integer[][][]) water1Json.stream().map(row -> {
            return ((JSONArray) row).stream().map(entry -> {
                return ((JSONArray) entry).stream().map(num -> Integer.parseInt("" + num)).toArray(Integer[]::new);
            }).toArray(Integer[][]::new);
        }).toArray(Integer[][][]::new);
        water2 = (Integer[][][]) water2Json.stream().map(row -> {
            return ((JSONArray) row).stream().map(entry -> {
                return ((JSONArray) entry).stream().map(num -> Integer.parseInt("" + num)).toArray(Integer[]::new);
            }).toArray(Integer[][]::new);
        }).toArray(Integer[][][]::new);
    }

    @Test
    void water1() {
        PolyLabel result = PolyLabel.polyLabel(water1, 1);
        Assertions.assertArrayEquals(new double[]{3865.85009765625, 2124.87841796875}, result.getCoordinates());
        Assertions.assertEquals(288.8493574779127, result.getDistance());
    }

    @Test
    void water1Precision50() {
        PolyLabel result = PolyLabel.polyLabel(water1, 50);
        Assertions.assertArrayEquals(new double[]{3854.296875, 2123.828125}, result.getCoordinates());
        Assertions.assertEquals(278.5795872381558, result.getDistance());
    }

    @Test
    void water2() {
        PolyLabel result = PolyLabel.polyLabel(water2);
        Assertions.assertArrayEquals(new double[]{3263.5, 3263.5}, result.getCoordinates());
        Assertions.assertEquals(960.5, result.getDistance());
    }

    @Test
    void degeneratePolygons() {
        PolyLabel result1 = PolyLabel.polyLabel(new Integer[][][]{{{0, 0}, {1, 0}, {2, 0}, {0, 0}}});
        Assertions.assertArrayEquals(new double[]{0, 0}, result1.getCoordinates());
        Assertions.assertEquals(0, result1.getDistance());

        PolyLabel result2 = PolyLabel.polyLabel(new Integer[][][]{{{0, 0}, {1, 0}, {1, 1}, {1, 0}, {0, 0}}});
        Assertions.assertArrayEquals(new double[]{0, 0}, result2.getCoordinates());
        Assertions.assertEquals(0, result2.getDistance());
    }

}
