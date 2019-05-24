package io.github.oblarg.logexample;/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.oblarg.logexample.commands.LoggedCommand;
import io.github.oblarg.logexample.subsystems.LoggedSubsystem;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    //These fields are loggable, and so Logger will automatically add them as tabs to shuffleboard.  Their own Loggable fields,
    //in turn, will be added as layouts within those tabs, and their children will become sub-layouts, etc.
    public static final LoggedSubsystem subsystem = new LoggedSubsystem();

    private LoggedCommand command5Seconds = new LoggedCommand(5);

    private LoggedCommand command10Seconds = new LoggedCommand(10);

    @Log(methodName = "getFoo", tabName = "FooTab")
    @Log(methodName = "getFoo")
    private UnloggedComponent methodTest = new UnloggedComponent();

    @Log
    private static DifferentialDrive drive = new DifferentialDrive(new Victor(1), new Victor(2));

    //This is loggable, but will not be logged due to the exclude annotation.
    @Log.Exclude
    private LoggedCommand commandExcluded = new LoggedCommand(11);

    private final SendableChooser<Command> m_chooser = new SendableChooser<>();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        m_chooser.setDefaultOption("5 Seconds", command5Seconds);
        m_chooser.addOption("10 Seconds", command10Seconds);
        SmartDashboard.putData("Auto choices", m_chooser);

        //Configures logging.  Passing "this" specifies the runtime instance of Robot.java as object whose loggable fields
        //will be make up the shuffleboard tabs.
        //Logger.configureLoggingNTOnly(this, "Robot");
        Logger.configureLoggingAndConfig(this, false);

        Scheduler.getInstance().add(command5Seconds);


    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        //Updates all of the NT entries.  Necessary to ensure the values sent to shuffleboard change as they
        //change in code.
        Logger.updateEntries();
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to
     * the switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        Command autoCommand = m_chooser.getSelected();
        System.out.println("Auto selected: " + autoCommand);

        autoCommand.start();
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
