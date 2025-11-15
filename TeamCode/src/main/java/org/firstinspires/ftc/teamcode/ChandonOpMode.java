package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name = "ChandonOpMode", group = "LinearOpMode")
public class ChandonOpMode extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private ElapsedTime timer = new ElapsedTime();
    private DcMotor motorLeft = null;
    private Servo servoSort;
    private DcMotor motorRight = null;
    private DcMotor outtakeMotor = null;

    boolean ServoToggle = false;
    double servoPosition;
    boolean IntakeToggle = false;
    boolean OuttakeToggle = false;
    boolean DpadUpToggle = false;
    boolean DpadDownToggle = false;
    boolean lastOutput = false;
    double motorPower = 0.5;
    @Override
    public void runOpMode()  {

        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");
        motorLeft = hardwareMap.get(DcMotor.class, "IntakeMotorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "IntakeMotorRight");
        outtakeMotor = hardwareMap.get(DcMotor.class, "OuttakeMotor");
        servoSort = hardwareMap.get(Servo.class, "SortingServo");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.FORWARD);
        outtakeMotor.setDirection(DcMotor.Direction.REVERSE);
        servoSort.setDirection(Servo.Direction.FORWARD);
        servoSort.scaleRange(0.0, 1.0);

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            double max;

            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
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
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);


            if(gamepad1.x && !ServoToggle) {
                servoPosition += 0.67;  // move by ~120 degrees

                // Keep position within range 0.0–1.0
                if (servoPosition > 1.0) {
                    servoPosition -= 1.0; // wrap around if past max
                }

                servoSort.setPosition(servoPosition);
            }
            ServoToggle = gamepad1.x;
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
            if (gamepad1.a && !IntakeToggle) {
                IntakeToggle = true;
                timer.reset();
            }

            if (IntakeToggle) {
                motorLeft.setPower(1.0);
                motorRight.setPower(1.0);

                if (timer.seconds() >= 5) {
                    motorLeft.setPower(0.0);
                    motorRight.setPower(0.0);
                    IntakeToggle = false;
                }
            }

            if (gamepad1.b && !lastOutput) {
                OuttakeToggle = !OuttakeToggle;
            }
            lastOutput = gamepad1.b;
            if (OuttakeToggle) {
                outtakeMotor.setPower(motorPower);
            } else {
                outtakeMotor.setPower(0.0);
            }

            telemetry.addData("Toggle", IntakeToggle ? "ON" : "OFF");
            telemetry.addData("Toggle Timer", timer.seconds());
            telemetry.update();
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            telemetry.update();
        }
    
    }
}
