package frc.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;

public class Cameras implements Runnable, System {

    private volatile boolean exit = false;

    public Cameras() {
        initialize();
    }

    @Override
    public void initialize() {
        power = CameraPower.ON;
    }

    @Override
    public void control(Controllers controllers) {
        power(controllers.isCameraPower());
    }

    public void run() {
        UsbCamera hatchCamera = new UsbCamera("USB Camera 0", 0);
        hatchCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
        UsbCamera cargoCamera = new UsbCamera("USB Camera 1", 1);
        cargoCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);

        CvSink cvSink = CameraServer.getInstance().getVideo(hatchCamera);
        CvSource cvSource = CameraServer.getInstance().putVideo("Current View", 320, 240);

        Mat image = new Mat();
        Mat output = new Mat();

        while (!exit) {
            if(cvSink.grabFrame(image) == 0) {
                cvSource.notifyError(cvSink.getError());
                continue;
            }

            switch(SwitchSides.side) {
                case HATCH_SIDE:
                    cvSink.setSource(hatchCamera);
                    break;
                case CARGO_SIDE:
                    cvSink.setSource(cargoCamera);
                    break;
            }

            Imgproc.cvtColor(image, output, Imgproc.COLOR_BGR2GRAY);
            cvSource.putFrame(output);
        }
    }

    public enum CameraPower {
        ON, OFF
    }
    private CameraPower power;

    public void power(boolean powerToggle) {
        switch(power) {
            case ON:
                if(powerToggle) {
                    exit = true;
                    power = CameraPower.OFF;
                }
                break;
            case OFF:
                if(!powerToggle) {
                    exit = false;
                    power = CameraPower.ON;
                }
                break;
        }
    }
}