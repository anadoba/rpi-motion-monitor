package pl.nadoba.rpi.motion.monitor;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;

public class MotionMonitor {

    private final static int CHECK_DURATION = 2500;

    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput ledActive = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "ACTIVE", PinState.LOW);
    private final GpioPinDigitalOutput ledCapture = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06 , "CAPTURE", PinState.LOW);
    private final GpioPinDigitalInput motionSensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "MOTION");
    private final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, "BUTTON");

    private CsvWriter csvWriter;

    private boolean isMonitoring = false;

    public MotionMonitor(String outputFile) {
        csvWriter = new CsvWriter(outputFile);
        init();
    }

    private void init() {
        ledActive.setShutdownOptions(true, PinState.LOW);
        button.addTrigger(new GpioCallbackTrigger(() -> {
            isMonitoring = !isMonitoring;
            ledActive.setState(isMonitoring);
            return null;
        }));

        ledCapture.setShutdownOptions(true, PinState.LOW);

        motionSensor.addTrigger(new GpioCallbackTrigger(() -> {

            if (isMonitoring && motionSensor.isHigh()) {
                ledCapture.setState(PinState.HIGH);
                csvWriter.writeMovementEvent();
            }

            ledCapture.setState(PinState.LOW);

            return null;
        }));
    }

    public void loop() throws InterruptedException {
        for (; ; ) {
            Thread.sleep(CHECK_DURATION);
        }
    }


}
