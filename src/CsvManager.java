import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.newBufferedReader;

public class CsvManager {

    final String userCsvFileName = "src/userData.csv";
    final String seatCsvFileName = "src/seatData.csv";

    public void writeUserCsv( ArrayList<User> users) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(userCsvFileName));
            for (User u : users) {
                writer.write(u.getUserId() + "," + u.getUserPassword() + "," + u.getUserName() + "," + u.getUserPhoneNum() + "," + u.getUsingSeatNum() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> readUserCsv() {
        BufferedReader br;
        ArrayList<User> users = new ArrayList<>();
        try (FileReader fileReader = new FileReader(userCsvFileName)) {
            br = newBufferedReader(Paths.get(userCsvFileName));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                String[] array = line.split(",");
                users.add(new User(array[0], array[1], array[2], array[3],Integer.parseInt((array[4]))));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("회원 정보 파일이 없습니다.\n프로그램을 종료합니다.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("회원 정보 파일이 없습니다.\n프로그램을 종료합니다.");
            System.exit(0);
        }
        return users;
    }

    public List<Seat> readSeatCsv() {
        List<Seat> seats = new ArrayList<>();
        Path path = Paths.get(seatCsvFileName);
        try (BufferedReader br = newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int seatNum = Integer.parseInt(data[0]);
                boolean using = data[1].equals("1");
                String startTime = data[2];
                String endTime = data[3];
                seats.add(new Seat(seatNum, using, startTime, endTime));
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다.");
        }
        return seats;
    }

    public int getCurrentSeatCount() {
        int currentSeatCount = 0;
        List<Seat> seats = readSeatCsv();
        for (Seat seat : seats) {
            if (seat.getUsing()) {
                currentSeatCount++;
            }
        }
        return currentSeatCount;
    }
    public void updateUserCsv(User user) {
        Path path = Paths.get(userCsvFileName);
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                String phoneNumber = data[3];
                if (phoneNumber.equals(user.getUserPhoneNum())) {
                    lines.set(i, user.getUserId() + "," + user.getUserPassword() + "," + user.getUserName() + "," + user.getUserPhoneNum() + "," + user.getUsingSeatNum());
                    break;
                }
            }
            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("파일을 쓰는 중 오류가 발생했습니다.");
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다.");
        }
    }

    public void updateSeatInCsv(Seat seat) {
        Path path = Paths.get(seatCsvFileName);
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                int currentSeatNum = Integer.parseInt(data[0]);
                if (currentSeatNum == seat.seatNum) {
                    String usingStatus = seat.using ? "1" : "0";
                    String updatedLine = seat.seatNum + "," + usingStatus + "," + seat.StartTime.toString() + "," + seat.getEndTime().toString();
                    lines.set(i, updatedLine);
                    break;
                }
            }
            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("파일을 쓰는 중 오류가 발생했습니다.");
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다.");
        }
    }


    public void initSeatCsv(int SEAT_CAPACITY) {
        Path path = Paths.get(seatCsvFileName);
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (int i = 0; i < SEAT_CAPACITY; i++) {
                String line = i + 1 + ",0,00:00,00:00";
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("파일을 쓰는 중 오류가 발생했습니다.");
        }
    }

    public void expandSeatCapacity(int currentCapacity,int newCapacity) {
        List<Seat> seats = readSeatCsv();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(seatCsvFileName))) {
            for (Seat seat : seats) {
                writer.write(seat.getSeatNum() + "," + (seat.getUsing() ? "1" : "0") + "," + seat.getStartTime() + "," + seat.getEndTime());
                writer.newLine();
            }
            // 새로운 좌석 추가
            for (int i = currentCapacity + 1; i <= newCapacity; i++) {
                writer.write(i + ",0,000000000000,000000000000");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reduceSeatCapacity(int newCapacity) {
        List<Seat> seats = readSeatCsv();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(seatCsvFileName))) {
            for (int i = 0; i < newCapacity; i++) {
                Seat seat =seats.get(i);
                writer.write(seat.getSeatNum() + "," + (seat.getUsing() ? "1" : "0") + "," + seat.getStartTime() + "," + seat.getEndTime());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
