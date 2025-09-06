package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Robot: IntakeOpMode", group = "LinearOpMode")

public class RobotIntakeOpMode extends LinearOpMode {

    private ElapsedTime timer = new ElapsedTime();
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;
    private DcMotor outtakeMotor = null;


    boolean IntakeToggle = false;
    boolean OuttakeToggle = false;
    boolean DpadUpToggle = false;
    boolean DpadDownToggle = false;
    boolean lastOutput = false;
    double motorPower = 0.5;
    @Override
    public void runOpMode() {

        motorLeft = hardwareMap.get(DcMotor.class, "IntakeMotorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "IntakeMotorRight");
        outtakeMotor = hardwareMap.get(DcMotor.class, "OuttakeMotor");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.FORWARD);
        outtakeMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {

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
        }
    }
}
