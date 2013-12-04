package robot;

import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static robot.Direction.*;
import static robot.Instruction.*;
import static robot.RoadBookCalculator.calculateRoadBook;

public class Robot {

    private Coordinates position;
    private Direction direction;
    private boolean isLanded;
    private RoadBook roadBook;
    private final double energyConsumption; // energie consommée pour la réalisation d'une action dans les conditions idéales
    private LandSensor landSensor;
    public final BlackBox blackBox;
    private Battery cells;

    public Robot(double energyConsumption, Battery cells) {
        isLanded = false;
        this.energyConsumption = energyConsumption;
        this.cells = cells;
        blackBox = new BlackBox();
    }

    public void land(Coordinates landPosition, LandSensor sensor) {
        position = landPosition;
        direction = NORTH;
        isLanded = true;
        landSensor = sensor;
        cells.setUp();
        blackBox.addCheckPoint(position, direction, true);
    }

    public int getXposition() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        return position.getX();
    }

    public int getYposition() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        return position.getY();
    }

    public Direction getDirection() throws UnlandedRobotException {
        if (!isLanded) throw new UnlandedRobotException();
        return direction;
    }

    public void moveForward() throws UnlandedRobotException, InsufficientChargeException, LandSensorDefaillance, InaccessibleCoordinate {
        if (!isLanded) throw new UnlandedRobotException();
        moveTo(MapTools.nextForwardPosition(position, direction));
    }

    public void moveBackward() throws UnlandedRobotException, InsufficientChargeException, LandSensorDefaillance, InaccessibleCoordinate {
        if (!isLanded) throw new UnlandedRobotException();
        moveTo(MapTools.nextBackwardPosition(position, direction));
    }

    private void moveTo(Coordinates nextPosition) throws InsufficientChargeException, LandSensorDefaillance, InaccessibleCoordinate {
        double neededEnergy;
        neededEnergy = landSensor.getPointToPointEnergyCoefficient(position, nextPosition) * energyConsumption;
        if (!cells.canDeliver(neededEnergy)) throw new InsufficientChargeException();
        cells.use(neededEnergy);
        position = nextPosition;
        blackBox.addCheckPoint(position, direction, true);
    }

    public void turnLeft() throws UnlandedRobotException, InsufficientChargeException {
        turnTo(MapTools.counterclockwise(direction));
    }

    public void turnRight() throws UnlandedRobotException, InsufficientChargeException {
        turnTo(MapTools.clockwise(direction));
    }

    private void turnTo(Direction newDirection) throws UnlandedRobotException, InsufficientChargeException {
        if (!isLanded) throw new UnlandedRobotException();
        if (!cells.canDeliver(energyConsumption)) throw new InsufficientChargeException();
        cells.use(energyConsumption);
        direction = newDirection;
        blackBox.addCheckPoint(position, direction, true);
    }

    public void setRoadBook(RoadBook roadBook) {
        this.roadBook = roadBook;
    }

    public List<CheckPoint> letsGo() throws UnlandedRobotException, UndefinedRoadbookException, InsufficientChargeException, LandSensorDefaillance, InaccessibleCoordinate {
        if (roadBook == null) throw new UndefinedRoadbookException();
        List<CheckPoint> mouchard = new ArrayList<CheckPoint>();
        while (roadBook.hasInstruction()) {
            Instruction nextInstruction = roadBook.next();
            if (nextInstruction == FORWARD) moveForward();
            else if (nextInstruction == BACKWARD) moveBackward();
            else if (nextInstruction == TURNLEFT) turnLeft();
            else if (nextInstruction == TURNRIGHT) turnRight();
            CheckPoint checkPoint = new CheckPoint(position, direction, true);
            mouchard.add(checkPoint);
            blackBox.addCheckPoint(checkPoint);
        }
        return mouchard;
    }

    public void computeRoadTo(Coordinates destination) throws UnlandedRobotException, LandSensorDefaillance, UndefinedRoadbookException {
        if (!isLanded) throw new UnlandedRobotException();
        setRoadBook(calculateRoadBook(landSensor, direction, position, destination, new ArrayList<Instruction>()));
    }

}
