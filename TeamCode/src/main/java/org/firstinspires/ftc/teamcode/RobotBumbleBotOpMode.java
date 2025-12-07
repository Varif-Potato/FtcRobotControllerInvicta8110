package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import android.graphics.Color;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;


@TeleOp(name = "Robot: BumbleBotOpModeV2", group = "LinearOpMode")

public class RobotBumbleBotOpMode extends LinearOpMode {
    // DriveTrain Variables and Function
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    public void DriveTrain(){
        double max;


        double axial   = gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral =  gamepad1.left_stick_x;
        double yaw     =  gamepad1.right_stick_x;

        double frontLeftPower  = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower   = axial - lateral + yaw;
        double backRightPower  = axial + lateral - yaw;

        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }
        frontLeftDrive.setPower(frontLeftPower * 0.5);
        frontRightDrive.setPower(frontRightPower * 0.5);
        backLeftDrive.setPower(backLeftPower * 0.5);
        backRightDrive.setPower(backRightPower * 0.5);
    }


    // Ramp Servo Variables and Function
    boolean ServoToggle = false;
    double servoPosition = 1.0;
    private Servo servoSort;
    public void servoTrigger(){
        if(gamepad1.y && !ServoToggle) {
            servoPosition -= 1;  // move by ~120 degrees

            // Keep position within range 0.0–1.0
            if (servoPosition < 0) {
                servoPosition += 2; // wrap around if past max
            }

            servoSort.setPosition(servoPosition);
        }
        ServoToggle = gamepad1.y;
    }
    // Intake Variables and Functions
    boolean IntakeToggle = false;
    private DcMotor motorLeft = null;

    private DcMotor motorRight = null;
    private ElapsedTime timer = new ElapsedTime();
    boolean OuttakeToggle = false;
//    boolean RevolverToggle = false;
    public void IntakeTrigger(){
        if (gamepad1.a && !IntakeToggle) {
            IntakeToggle = true;
//            RevolverToggle = true;
            timer.reset();
        }

        if (IntakeToggle) {
            motorLeft.setPower(1.0);
            motorRight.setPower(1.0);
//            determineColor();

//            if (timer.seconds() >= 0.05 && RevolverToggle) {
//                setRevolverPosition(3);
//                RevolverToggle = false;
//            }

            if (timer.seconds() >= 5) {
                motorLeft.setPower(0.0);
                motorRight.setPower(0.0);
                IntakeToggle = false;
            }
        }
    }
    // Outtake Variables and Functions
    boolean DpadUpToggle = false;
    boolean DpadDownToggle = false;
    private DcMotor outtakeMotor = null;

    public void outtakeSpeedControl(){
        if(gamepad1.dpad_up && !DpadUpToggle){
            if (motorPower >= 1.0){
                motorPower = 1.0;

            } else {
                motorPower += 0.1;
            }
        }
        DpadUpToggle = gamepad1.dpad_up;
        if(gamepad1.dpad_down && !DpadDownToggle){
            if(motorPower <= 0.0){
                motorPower = 0.0;

            } else {
                motorPower -= 0.1;

            }
        }
        DpadDownToggle = gamepad1.dpad_down;
    }
    double motorPower = 1.0;
    boolean lastOutput = false;
    public void outtakeTrigger(){
        if (gamepad1.b && !lastOutput) {
            OuttakeToggle = !OuttakeToggle;
        }
        lastOutput = gamepad1.b;
        if (OuttakeToggle) {
            outtakeMotor.setPower(motorPower);
        } else {
            outtakeMotor.setPower(0.0);
        }
    }


    // Revolver Variables and Function
    private DcMotor revolverMotor = null;
    int revolverTicks = 538;
    int revolverTarget = 0;
    boolean revolverInput;
    boolean revolverToggle;
    public void setRevolverPosition (int positions){

        revolverTarget += revolverTicks/positions;
        revolverMotor.setTargetPosition(revolverTarget);
        revolverMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        revolverMotor.setPower(-1);
    }
    public void revolverTrigger(){
        if (gamepad1.x && !revolverInput) {
            setRevolverPosition(3);
        }
        revolverInput = gamepad1.x;
    }
    // Color sensor variables and functions
    final float[] hsvValues = new float[3];
    String colorName = "";
//    NormalizedColorSensor colorSensor;

//    public void determineColor(){
//        NormalizedRGBA colors = colorSensor.getNormalizedColors();
//        Color.colorToHSV(colors.toColor(), hsvValues);
//
//        if(colors.blue > colors.green && colors.blue > colors.red){
//            colorName = "Purple";
//
//
//        }else if(colors.green > colors.red && colors.green > colors.blue){
//            colorName = "Green";
//
//        }
//    }




    @Override
    public void runOpMode() {

        // Initialization of DriveTrain Motors
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        // Initialization of Intake and Outtake Motors
        motorLeft = hardwareMap.get(DcMotor.class, "IntakeMotorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "IntakeMotorRight");
        outtakeMotor = hardwareMap.get(DcMotor.class, "OuttakeMotor");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.FORWARD);
        outtakeMotor.setDirection(DcMotor.Direction.REVERSE);
        // Initialization of Servo
        servoSort = hardwareMap.get(Servo.class, "SortingServo");
        servoSort.setDirection(Servo.Direction.FORWARD);
        servoSort.scaleRange(0.0, 0.4);
        servoSort.setPosition(servoPosition);
        // Initialization of Revolver Motor
        revolverMotor = hardwareMap.get(DcMotor.class, "RevolverMotor");
        revolverMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        revolverMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Initialization for Color Sensor
//        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");

        timer.reset();
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();


        waitForStart();
        while (opModeIsActive()) {


            DriveTrain();
            IntakeTrigger();
            revolverTrigger();
            servoTrigger();
            outtakeSpeedControl();
            outtakeTrigger();






            telemetry.addData("Toggle", IntakeToggle ? "ON" : "OFF");
            telemetry.addData("Toggle Timer", timer.seconds());
            telemetry.addData("Outtake Power", motorPower);
            telemetry.addData("Servo Position", servoPosition);
            telemetry.addData("Revolver Tick", revolverTarget);
            telemetry.addData("Color of Artifact", "Green");
            telemetry.update();
        }

    }
}


