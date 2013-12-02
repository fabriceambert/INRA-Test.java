package robot;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static robot.Direction.EAST;
import static robot.Direction.NORTH;
import static robot.Direction.WEST;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class RobotUnitTest {

    @Test
    public void testLand() throws UnlandedRobotException {
        //---DEFINE---
        Robot robot = new Robot();
        //---WHEN---
        robot.land(new Coordinates(3,0));
        //---THEN---
        Assert.assertEquals(NORTH, robot.getDirection());
        Assert.assertEquals(3, robot.getXposition());
        Assert.assertEquals(0, robot.getYposition());
    }

    // tester l'apparition d'une exception, l'annotation @Test intègre expected suivi de la classe de l'exception attendue
    // Attention : il est parfois nécessaire de s'assurer que l'exception n'apparaît pas avant la dernière instruction du test
    @Test (expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeMoveForward() throws Exception {
        Robot robot = new Robot();
        robot.moveForward();
    }

    @Test
    public void testMoveForward() throws Exception {
        //---DEFINE---
        Robot robot = new Robot();
        robot.land(new Coordinates(5, 5));
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        //---WHEN---
        robot.moveForward();
        //---THEN---
        // TODO : complétez ce test
    }

    @Test
    public void testMoveBackward() throws Exception {
        //---DEFINE---
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        //---WHEN---
        // TODO : complétez ce test
        //---THEN---
        Assert.assertEquals(currentXposition, robot.getXposition());
        Assert.assertEquals(currentYposition+1, robot.getYposition());
    }

    @Test
    public void testTurnLeft() throws Exception {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnLeft();
        Assert.assertEquals(WEST, robot.getDirection());
    }

    @Test
    public void testTurnRight() throws Exception {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnRight();
        Assert.assertEquals(EAST, robot.getDirection());
    }

    @Test (expected = UndefinedRoadbookException.class)
    public void testLetsGoWithoutRoadbook() throws Exception {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.letsGo();
    }

    @Test
    public void testFollowInstruction() throws Exception {
        Robot robot = new Robot();
        robot.land(new Coordinates(5, 7));
        robot.setRoadBook(new RoadBook(Arrays.asList(Instruction.FORWARD, Instruction.FORWARD, Instruction.TURNLEFT, Instruction.FORWARD)));
        robot.letsGo();
        Assert.assertEquals(4, robot.getXposition());
        Assert.assertEquals(5, robot.getYposition());
    }

    @Test (expected = UnlandedRobotException.class)
    public void testComputeRoadToWithUnlandedRobot() throws Exception {
        Robot robot = new Robot();
        robot.computeRoadTo(new Coordinates(3, 5));
    }

    @Test
    public void testComputeRoadTo() throws UnlandedRobotException {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.computeRoadTo(new Coordinates(7, 5));


    }

    @Test
    public void testLetsTo() throws Exception {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.computeRoadTo(new Coordinates(0, -6));
        robot.letsGo();
        Assert.assertEquals(0, robot.getXposition());
        Assert.assertEquals(-6, robot.getYposition());
    }
}
