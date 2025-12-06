package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "RevolverTest", group = "LinearOpMode")
public class RevolverTest extends LinearOpMode{
    private DcMotor revolverMotor = null;
    int revolverTicks = 538;
    int revolverTarget = 0;
    boolean revolverInput;
    boolean revolverToggle;
    public void setRevolverPosition (){
        if(revolverTarget == 0){
            revolverTarget = 538;
        } else {
            revolverTarget -= 10;
        }
        revolverMotor.setTargetPosition(revolverTarget);
        revolverMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        revolverMotor.setPower(-0.3);
    }


    public void runOpMode() {
        revolverMotor = hardwareMap.get(DcMotor.class, "RevolverMotor");
        revolverMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        revolverMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.x && !revolverInput) {
                revolverToggle = !revolverToggle;

                if (revolverToggle) {
                    setRevolverPosition();
                }
            }
            revolverInput = gamepad1.x;
            telemetry.addData("Revolver Tick: ", revolverTarget);

        }
    }
}
