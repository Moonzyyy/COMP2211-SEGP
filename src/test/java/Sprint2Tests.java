import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Sprint2Tests {

    @BeforeAll
    static void prep() {
        //Do not know filter class used yet
    }

    @Test
    void UserStory18Au25() {
        assertEquals(97050, 19, "Filtered for ages <24");
    }

    @Test
    void UserStory18Au34() {
        assertEquals(121984, 19, "Filtered for ages 25-34");
    }

    @Test
    void UserStory18Au44() {
        assertEquals(121774, 19, "Filtered for ages 35-44");
    }

    @Test
    void UserStory18Au54() {
        assertEquals(84324, 19, "Filtered for ages 45-54");
    }

    @Test
    void UserStory18Ao54() {
        assertEquals(60972, 19, "Filtered for ages >54");
    }

    @Test
    void UserStory18GM() {
        assertEquals(161469, 19, "Filtered for Males");
    }

    @Test
    void UserStory18GF() {
        assertEquals(324635, 19, "Filtered for Females");
    }

    @Test
    void UserStory18IL() {
        assertEquals(145948, 19, "Filtered for Low Income");
    }

    @Test
    void UserStory18IM() {
        assertEquals(243050, 19, "Filtered for Middle Income");
    }

    @Test
    void UserStory18IH() {
        assertEquals(97106, 19, "Filtered for High Income");
    }

    @Test
    void UserStory19B() {
        assertEquals(69583, 19, "Filtered for Blogs");
    }

    @Test
    void UserStory19N() {
        assertEquals(139170, 19, "Filtered for News");
    }

    @Test
    void UserStory19Sp() {
        assertEquals(139256, 19, "Filtered for Shopping");
    }

    @Test
    void UserStory19SM() {
        assertEquals(138095, 19, "Filtered for Social Media");
    }
}