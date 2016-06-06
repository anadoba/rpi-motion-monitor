package pl.nadoba.rpi.motion.monitor;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

import java.util.concurrent.Callable;

public class MotionMonitor {

    private final static int CHECK_DURATION = 2000;

    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput ledActive = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "ACTIVE", PinState.LOW);
    private final GpioPinDigitalOutput ledCapture = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06 , "CAPTURE", PinState.LOW);
    private final GpioPinDigitalInput motionSensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "MOTION");
    private final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, "BUTTON");

    private CsvWriter csvWriter;

    public MotionMonitor(String outputFile) {
        csvWriter = new CsvWriter(outputFile);
        init();
    }

    private void init() {
        ledActive.setShutdownOptions(true, PinState.LOW);
        button.addTrigger(new GpioSyncStateTrigger(ledActive));

        ledCapture.setShutdownOptions(true, PinState.LOW);
        motionSensor.addTrigger(new GpioSyncStateTrigger(ledCapture));

        motionSensor.addTrigger(new GpioCallbackTrigger(new Callable<Void>() {
            public Void call() {

                if (ledActive.isHigh()) {
                    csvWriter.writeMovementEvent();
                }

                return null;
            }
        }));
    }

    public void loop() throws InterruptedException {
        for (; ; ) {
            Thread.sleep(CHECK_DURATION);
        }
    }


}
