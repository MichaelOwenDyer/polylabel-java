import com.monst.polylabel.PolyLabel;
import org.junit.jupiter.api.Test;

class PolyLabelTest {

    static int[][][] water1, water2;
//    static {
//        JSONParser parser = new JSONParser();
//        try {
//            JSONArray wat1 = (JSONArray) parser.parse(new FileReader("src/test/resources/water1.json"));
//            JSONArray wat2 = (JSONArray) parser.parse(new FileReader("src/test/resources/water2.json"));
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    void main() {
        Integer[][][] test = new Integer[][][]{{{4016,1878},{4016,1864},{4029,1859},{4024,1850},{4008,1839},{4006,1863},{4016,1878},{4016,1878}}};
        PolyLabel.Result result = PolyLabel.polyLabel(test, 1.0);
        System.out.printf("\nFound center at (%f, %f)", result.getX(), result.getY());
    }



    @Test
    void main2() {
        PolyLabel.Result result = PolyLabel.polyLabel(new Integer[][][] {{{0, 0}, {10, 0}, {0, 10}}}, 0.5, true);
        System.out.printf("\nFound center at (%f, %f)", result.getX(), result.getY());
    }

    @Test
    void water1Precision1() {

    }

}