import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CsvManager {
    public void writeCsv(String filename , ArrayList<User> users) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (User u : users) {
                writer.write(u.getUserId() + "," + u.getUserPassword() + "," + u.getUserName() + "," + u.getUserPhoneNum() + "," + u.getUsingSeatNum() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readCsv(String filename) {
        BufferedReader br;

        try (FileReader fileReader = new FileReader(filename)) {
            br = Files.newBufferedReader(Paths.get(filename));
            String line = "";

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                String[] array = line.split(",");
                User user = new User(array[0], array[1], array[2], array[3],Integer.parseInt((array[4])));
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("회원 정보 파일이 없습니다.\n프로그램을 종료합니다.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("회원 정보 파일이 없습니다.\n프로그램을 종료합니다.");
            System.exit(0);
        }
    }
}
