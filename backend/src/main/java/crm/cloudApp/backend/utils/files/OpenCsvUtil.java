package crm.cloudApp.backend.utils.files;

import com.opencsv.CSVReader;
import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

@UtilityClass
public class OpenCsvUtil {

  public static final List<String[]> csvInList(String path) throws IOException {
    List<String[]> records;
    try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
      records = csvReader.readAll();
    }
    return records;
  }

  public static final List<String[]> csvInList(byte[] data) throws IOException {
    Reader reader = new StringReader(new String((data)));
    List<String[]> records;
    try (CSVReader csvReader = new CSVReader(reader)) {
      records = csvReader.readAll();
    }
    return records;
  }

}
