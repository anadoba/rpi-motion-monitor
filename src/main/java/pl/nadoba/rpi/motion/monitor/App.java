package pl.nadoba.rpi.motion.monitor;

public class App {


    public static void main(String[] args) throws InterruptedException {

        if (args.length != 1)
            throw new RuntimeException("There should be only 1 argument - CSV filename to print the results");

        String outputFile = args[0];

        MotionMonitor monitor = new MotionMonitor(outputFile);
        monitor.loop();
    }


}
