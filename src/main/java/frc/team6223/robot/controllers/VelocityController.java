package frc.team6223.robot.controllers;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team6223.arsenalFramework.drive.ArsenalDrive;
import frc.team6223.arsenalFramework.drive.ControllerInput;
import frc.team6223.arsenalFramework.drive.DriveControllerOutput;
import frc.team6223.arsenalFramework.drive.MovementControllerCommand;
import frc.team6223.arsenalFramework.hardware.motor.MotorControlMode;
import frc.team6223.arsenalFramework.software.NetworkTableUtilities;
import frc.team6223.arsenalFramework.software.pid.PIDFConstants;
import frc.team6223.arsenalFramework.software.pid.PIDFController;
import frc.team6223.arsenalFramework.software.units.Distance;
import frc.team6223.arsenalFramework.software.units.DistanceUnits;
import frc.team6223.arsenalFramework.software.units.TimeUnits;


public class VelocityController extends MovementControllerCommand {

    private final double velocityTarget;
    private final PIDFConstants pidfConstants;
    private final PIDFController pidfController;

    public VelocityController(ArsenalDrive drive, double velocityTarget) {
        super(drive);
        this.velocityTarget = velocityTarget;
        pidfConstants = new PIDFConstants(1.0, 0.0, 1.0, 1.0);
        pidfController = new PIDFController(pidfConstants, velocityTarget);
    }

    public double getVelocityTarget() {
        return velocityTarget;
    }

    public PIDFController getPidfController() {
        return pidfController;
    }

    @Override
    public DriveControllerOutput calculateMotorOutput(ControllerInput controllerInput) {
        double motorVal = pidfController.runController(controllerInput.getLeftDriveVelocity().rescaleScalar(DistanceUnits.METERS, TimeUnits.SECONDS));
        // todo: separate left and right rates
        return new DriveControllerOutput(MotorControlMode.PIDVelocity, motorVal, motorVal);
    }

    @Override
    public void startController(Distance leftInitial, Distance rightInitial) {

    }

    @Override
    public void stopController() {

    }

    @Override
    protected void interruptedController() {

    }

    @Override
    public boolean isFinished() {
        return pidfController.isFinished();
    }

    @Override
    public void dashboardPeriodic(NetworkTable table) {
        NetworkTableUtilities.putItemInTable(table, "Current Controller", "VelocityController");
        NetworkTableUtilities.putItemInTable(table, "Velocity Target", velocityTarget);
        NetworkTableUtilities.putItemInTable(table, "Current Controller kP", pidfConstants.getkP());
        NetworkTableUtilities.putItemInTable(table, "Current Controller kI", pidfConstants.getkI());
        NetworkTableUtilities.putItemInTable(table, "Current Controller kD", pidfConstants.getkD());
        NetworkTableUtilities.putItemInTable(table, "Current Controller kF", pidfConstants.getkF());
    }
}
