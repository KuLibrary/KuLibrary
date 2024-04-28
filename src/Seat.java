import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;

public class Seat {
    final int SEAT_CAPACITY = 100;
    int seatNum; //좌석 번호
    Boolean using = true; // 해당 좌석을 사용중인지 여부
    LocalTime StartTime;
    LocalTime EndTime;


    public Seat(int seatNum, Boolean using, LocalTime StartTime, LocalTime EndTime) {
        this.seatNum = seatNum;
        this.using = using;
        this.StartTime = StartTime;
        this.EndTime = EndTime; // 수정: 생성자에서 EndTime을 바로 설정
    }

    User user = new User();

    public Seat(User user) {
        this.user = user;
    }

    public Seat() {
    }

    public int getSeatNum() {
        return seatNum;
    }

    public Boolean getUsing() {
        return using;
    }


    public void setUsing(Boolean using) {
        this.using = using;
    }

    public void setTime() {
        this.StartTime = LocalTime.now();
        this.EndTime = LocalTime.now().plusHours(5);
    }

    //시작시간 초기파일에서 받을때 00:00
    public LocalTime getStartTime() {
        if (StartTime == null) {
            return LocalTime.MIN; // 수정: null 대신 LocalTime.MIN 반환
        }
        return StartTime;
    }


    // 시작시간 초기파일에서 받을때 00:00
    public LocalTime getEndTime() {
        if (EndTime == null) {
            return LocalTime.MIN; // 수정: null 대신 LocalTime.MIN 반환
        }
        return EndTime;
    }


    ArrayList<Seat> seats = new ArrayList<Seat>(); //좌석정보만 담겨있는 리스트
    static String filename = "src/seatData.csv";


    public void reservation_Menu() {
        Scanner sc = new Scanner(System.in);

        fromCsv();
        while (true) {
            System.out.println("<예약 메뉴>");
            System.out.println("---------------");
            System.out.println("1) 좌석 예약");
            System.out.println("2) 좌석 확인");
            System.out.println("3) 로그아웃");
            System.out.println("---------------");
            try {
                System.out.println("메뉴 번호를 입력하세요.");
                System.out.print(">>");
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1":
                        if (user.getUsingSeatNum()==0) {
                            apply_Reservation();
                        }else {
                            System.out.println("이미 이용 중인 좌석이 있습니다.");
                        }
                        break;
                    case "2":
                        check_Seat();
                        break;
                    case "3":
                        System.out.println("메뉴로 돌아갑니다.");
                        return;
                    default:
                        System.out.println("1~3 사이 숫자를 입력하세요");
                }
            } catch (NumberFormatException E) {
                System.out.println("올바른 형식으로 입력하세요!");
            }
        }
    }


    public void apply_Reservation() {
        int selectSeatNum;
        List<Seat> seats = fromCsv();
        printSeat();
        Scanner sc = new Scanner(System.in);
        while (true) {

            System.out.println("사용하고자하는 좌석을 입력해주세요. q 입력 시 예약 메뉴로 돌아갑니다.");
            System.out.print(">> ");
            String input = sc.nextLine().trim();
            if (input.equals("q")) {
                System.out.println();
                return;
            } else if (!input.matches("\\d+")) {
                System.out.println("숫자만 입력해주세요.");
                continue;
            }
            selectSeatNum = Integer.parseInt(input);
            if (selectSeatNum <= 0 || selectSeatNum > SEAT_CAPACITY) {
                System.out.println("해당 좌석은 존재하지 않습니다.");
            }
            for (Seat seat : seats) {
                if (selectSeatNum == seat.getSeatNum()) {
                    if (seat.getUsing()) {
                        System.out.println("해당 좌석은 이미 사용중입니다");
                        System.out.println("다른 좌석을 입력해주세요");
                        break;
                        //1차 기획서랑 다르게 메인화면으로 안가고 다시입력받을수있도록함
                    } else {
                        System.out.println("좌석 예약에 성공했습니다.");

                        user.setUsingSeatNum(selectSeatNum); //유저정보 업데이트

                        seat.setUsing(true);
                        seat.setTime();
                        updateSeatInCsv(seat);
                        updateUserCsv(user);
                        System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
                        sc.nextLine();
                        return;
                    }
                }
            }

            System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
            sc.nextLine();
            return;
        }
    }


    public void updateSeatInCsv(Seat seat) {
        Path path = Paths.get(filename);
        List<String> lines = new ArrayList<>();
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




    public void printSeat() {
        List<Seat> seats = fromCsv();
        System.out.println("----- 좌석의 사용여부를 출력합니다 -----");
        for (int i = 0; i < seats.size(); i++) {
            // 좌석 번호를 출력합니다. 10개마다 줄바꿈을 합니다.
            if (i % 10 == 0 && i != 0) {
                System.out.println(); // 번호 출력을 마친 후 줄바꿈
            }
            Seat seat = seats.get(i);
            // 번호가 한 자리 수일 경우 앞에 0을 붙입니다.
            String formattedSeatNum = String.format("%02d", seat.seatNum);
            System.out.print(formattedSeatNum);
            if ((i % 10 != 9) && (i != seats.size() - 1)) System.out.print(","); // 줄의 마지막 항목 제외하고 콤마를 찍습니다.

            // 10개마다 또는 마지막 좌석에서 사용 여부를 출력합니다.
            if ((i % 10 == 9) || (i == seats.size() - 1)) {
                System.out.println(); // 줄바꿈
                int start = (i / 10) * 10; // 이번에 출력할 사용 여부 시작 인덱스
                for (int j = start; j <= i; j++) { // 시작 인덱스부터 현재 인덱스까지 사용 여부 출력
                    seat = seats.get(j);
                    String usingStatus = (seat.using ? " O" : " X"); // 공백 추가
                    System.out.print(usingStatus);
                    if (j < i) System.out.print(","); // 마지막 항목 제외하고 콤마를 찍습니다.
                }
                System.out.println(); // 사용 여부 출력 후 줄바꿈
            }
        }
    }






    public static List<Seat> fromCsv() {
        List<Seat> seats = new ArrayList<>();
        Path path = Paths.get(filename);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int seatNum = Integer.parseInt(data[0]);
                boolean using = data[1].equals("1");
                LocalTime startTime = LocalTime.parse(data[2]);
                LocalTime endTime = LocalTime.parse(data[3]);
                seats.add(new Seat(seatNum, using, startTime, endTime));
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다.");
        }
        return seats;
    }

    public void check_Seat() {
        fromCsv();
        Scanner sc = new Scanner(System.in);
        if (user.getUsingSeatNum() == 0) {
            System.out.println(user.getUserName() + "님, 사용중인 좌석이 없습니다");
            System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
            sc.nextLine();
            return;
        } else {
            System.out.println(user.getUserName() + "님의 현재 사용중인 좌석 정보");
            System.out.println("------------------");
            System.out.println("좌석 번호: " + user.getUsingSeatNum());
            System.out.println("좌석 이용 시작 시간: " + user.getStartTime());
            System.out.println("좌석 이용 종료 시간: " + user.getEndTime());

        }
    }


    public static void toCsv(List<Seat> seats) {
        Path path = Paths.get(filename);
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Seat seat : seats) {
                String line = seat.seatNum + "," + seat.using + "," + seat.StartTime + "," + seat.EndTime;
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("파일을 쓰는 중 오류가 발생했습니다.");
        }
    }

    public void admin_Menu() {

        Scanner sc = new Scanner(System.in);
        List<Seat> seats = fromCsv();
        while (true) {
            System.out.println("<어드민 메뉴>");
            System.out.println("---------------");
            System.out.println("1) 좌석 현황");
            System.out.println("2) 로그아웃");
            System.out.println("---------------");
            try {
                System.out.println("메뉴 번호를 입력하세요.");
                System.out.print(">>");
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1":
                        printSeat();
                        break;
                    case "2":
                        System.out.println("메뉴로 돌아갑니다.");
                        return;
                    default:
                        System.out.println("1~2 사이 숫자를 입력하세요");
                }
            } catch (NumberFormatException E) {
                System.out.println("올바른 형식으로 입력하세요!");
            }
        }
    }

    public void initSeatCsv(int SEAT_CAPACITY) {
        Path path = Paths.get(filename);
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

    public void updateUserCsv(User user) {
        Path path = Paths.get("src/userData.csv");
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                String phoneNumber = data[3]; // Assuming phone number is stored at index 3
                if (phoneNumber.equals(user.getUserPhoneNum())) {
                    // Update the user's information
                    lines.set(i, user.getUserId() + "," + user.getUserPassword() + "," + user.getUserName() + "," + user.getUserPhoneNum() + "," + user.getUsingSeatNum());
                    break;
                }
            }
            // Write the updated lines back to the CSV file
            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to the file.");
            }
        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
    }

}