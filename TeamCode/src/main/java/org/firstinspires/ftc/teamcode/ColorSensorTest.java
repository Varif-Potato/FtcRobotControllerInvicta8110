package org.firstinspires.ftc.teamcode;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;


@TeleOp(name = "Test: Color Sensor", group = "Sensor")
public class ColorSensorTest extends LinearOpMode {
    final float[] hsvValues = new float[3];
    NormalizedColorSensor colorSensor;
    public void runOpMode(){

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
        waitForStart();
        while(opModeIsActive()){
            NormalizedRGBA colors = colorSensor.getNormalizedColors();
            Color.colorToHSV(colors.toColor(), hsvValues);

            if(colors.blue > colors.green && colors.blue > colors.red){
                telemetry.addData("Color of Artifact","Purple");
            }else if(colors.green > colors.red && colors.green > colors.blue){
                telemetry.addData("Color of Artifact", "Green");
            }
            telemetry.addLine();
            telemetry.addData("Red", "%.3f", colors.red);
            telemetry.addData("Green", "%.3f", colors.green);
            telemetry.addData("Blue", "%.3f", colors.blue);
            telemetry.addLine();
            telemetry.addData("Hue", "%.3f", hsvValues[0]);
            telemetry.addData("Saturation", "%.3f", hsvValues[1]);
            telemetry.addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", colors.alpha);
            telemetry.addLine();
            if(colors.blue > colors.green && colors.blue > colors.red){
                telemetry.addData("Color of Artifact","Purple");
            }else if(colors.green > colors.red && colors.green > colors.blue){
                telemetry.addData("Color of Artifact", "Green");
            }
            telemetry.update();
        }
    }
}
