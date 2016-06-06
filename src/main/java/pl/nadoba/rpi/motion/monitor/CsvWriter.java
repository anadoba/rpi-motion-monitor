package pl.nadoba.rpi.motion.monitor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvWriter {

    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final char DELIMITER = ';';
    private static final Object[] FILE_HEADERS = {"event", "timestamp"};
    private static final String TIMESTAMP_FORMAT = "%1$TD %1$TT";

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withDelimiter(DELIMITER);

    private CSVPrinter csvFilePrinter;

    public CsvWriter(String filename) {
        try {
            FileWriter fileWriter = new FileWriter(filename, true);
            csvFilePrinter = new CSVPrinter(fileWriter, CSV_FORMAT);
            csvFilePrinter.printRecord(FILE_HEADERS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public void writeMovementEvent() {
        List<String> record = new ArrayList<>();
        record.add("MOVEMENT");
        record.add(String.format(TIMESTAMP_FORMAT, getCurrentTimestamp()));
        System.out.println("Exporting to CSV: " + Arrays.deepToString(record.toArray()));

        try {
            csvFilePrinter.printRecord(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
