
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Seat {
    final int SEAT_CAPACITY = 100;
    int seatNum; //좌석 번호
    Boolean using = true; // 해당 좌석을 사용중인지 여부
    String StartTime;
    String EndTime;

    static RegexManager regexManager = new RegexManager();
    CsvManager csvManager = new CsvManager();

    public Seat(int seatNum, Boolean using, String StartTime, String EndTime) {
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


    public void resetTime() {
        this.StartTime = "000000000000";
        this.EndTime = "000000000000";
    }



    public void setTime(String time) {
        // Define the input and output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        // Parse the input time to LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);

        // Add 5 hours to the parsed time
        LocalDateTime updatedDateTime = dateTime.plusHours(5);

        // Format the updated time back to the string format

        this.StartTime = time;
        this.EndTime = updatedDateTime.format(formatter);
    }

    public void extensionTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        LocalDateTime updatedDateTime = dateTime.plusHours(5);

        this.EndTime = updatedDateTime.format(formatter);
    }


    //시작시간 초기파일에서 받을때 00:00
    public String getStartTime() {
        if (StartTime == null) {
            System.out.println("빈 좌석 입니다");
        }
        return StartTime;
    }


    // 시작시간 초기파일에서 받을때 00:00
    public String getEndTime() {
        if (EndTime == null) {
            System.out.println("빈 좌석 입니다.");
        }
        return EndTime;
    }


    public void reservation_Menu(String time) {
        Scanner sc = new Scanner(System.in);

        csvManager.readSeatCsv();
        while (true) {
            System.out.println("<예약 메뉴>");
            System.out.println("---------------");
            System.out.println("1) 좌석 예약");
            System.out.println("2) 좌석 확인");
            System.out.println("3) 좌석 퇴실");
            System.out.println("4) 좌석 연장");
            System.out.println("5) 로그아웃");
            System.out.println("---------------");

            System.out.println("메뉴 번호를 입력하세요.");
            System.out.print(">>");
            String choice = sc.nextLine().trim();
            if (regexManager.checkFiveMenu(choice)) {
                switch (choice) {
                    case "1":
                        if (user.getUsingSeatNum() == 0) {
                            apply_Reservation(time);
                        } else {
                            System.out.println("이미 이용 중인 좌석이 있습니다.");
                        }
                        break;
                    case "2":
                        check_Seat();
                        break;
                    case "3":
                        out_Seat(time);
                        break;
                    case "4":
                        extension_Seat(time);
                        break;
                    case "5":
                        System.out.println("메뉴로 돌아갑니다.");
                        return;
                    default:
                        System.out.println("1~3 사이 숫자를 입력하세요");
                }
            }
        }
    }


    public void apply_Reservation(String time) {
        int selectSeatNum;
        List<Seat> seats = csvManager.readSeatCsv();
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
                        seat.setTime(time);
                        user.setStartTime(seat.getStartTime());
                        user.setEndTime(seat.getEndTime());
                        csvManager.updateSeatInCsv(seat);
                        csvManager.updateUserCsv(user);
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





    public void printSeat() {
        List<Seat> seats = csvManager.readSeatCsv();
        System.out.println("----- 좌석의 사용여부를 출력합니다 -----");
        for (int i = 0; i < seats.size(); i++) {
            if (i % 10 == 0 && i != 0) {
                System.out.println();
            }
            Seat seat = seats.get(i);
            String formattedSeatNum = String.format("%02d", seat.seatNum);
            System.out.print(formattedSeatNum);
            if ((i % 10 != 9) && (i != seats.size() - 1)) System.out.print(",");

            if ((i % 10 == 9) || (i == seats.size() - 1)) {
                System.out.println();
                int start = (i / 10) * 10;
                for (int j = start; j <= i; j++) {
                    seat = seats.get(j);
                    String usingStatus = (seat.using ? " O" : " X");
                    System.out.print(usingStatus);
                    if (j < i) System.out.print(",");
                }
                System.out.println();
            }
        }
    }

    public void seatout() {
        int selectSeatNum;
        List<Seat> seats = csvManager.readSeatCsv();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("퇴실 시키고자하는 좌석을 입력해주세요. q 입력 시 예약 메뉴로 돌아갑니다.");
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

                        seat.setUsing(false);
                        System.out.println("해당 좌석은 이미 사용중입니다");
                        System.out.println("다른 좌석을 입력해주세요");
                        break;
                        //1차 기획서랑 다르게 메인화면으로 안가고 다시입력받을수있도록함
                    } else {
                        System.out.println("좌석 예약에 성공했습니다.");

                        user.setUsingSeatNum(selectSeatNum); //유저정보 업데이트
                        seat.setUsing(true);
                        seat.setTime();
                        user.setStartTime(seat.getStartTime());
                        user.setEndTime(seat.getEndTime());
                        csvManager.updateSeatInCsv(seat);
                        csvManager.updateUserCsv(user);
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

    public void seatsetting() {

    }






    public void check_Seat() {
        csvManager.readSeatCsv();
        Scanner sc = new Scanner(System.in);
        if (user.getUsingSeatNum() == 0) {
            System.out.println(user.getUserName() + "님, 사용중인 좌석이 없습니다");
            System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
            sc.nextLine();
        } else {
            System.out.println(user.getUserName() + "님의 현재 사용중인 좌석 정보");
            System.out.println("------------------");
            System.out.println("좌석 번호: " + user.getUsingSeatNum());
            System.out.println("좌석 이용 시작 시간: " + RegexManager.formatDateTime(user.getStartTime()));
            System.out.println("좌석 이용 종료 시간: " + RegexManager.formatDateTime(user.getEndTime()));
        }
    }

    public void out_Seat(String time) {
        List<Seat> seats = csvManager.readSeatCsv();
        csvManager.readSeatCsv();
        Scanner sc = new Scanner(System.in);
        if (user.getUsingSeatNum() == 0) {
            System.out.println(user.getUserName() + "님, 사용중인 좌석이 없습니다");


        } else {
            for (Seat seat : seats) {
                if (user.getUsingSeatNum() == seat.getSeatNum()) {

                    user.setUsingSeatNum(0); //유저정보 업데이트
                    seat.setUsing(false);
                    seat.resetTime();
                    user.setStartTime(seat.getStartTime());
                    user.setEndTime(seat.getEndTime());
                    csvManager.updateSeatInCsv(seat);
                    csvManager.updateUserCsv(user);

                    System.out.println("좌석 퇴실에 성공했습니다.");
                    return;
                }
            }
        }
        System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
        sc.nextLine();
    }

    public void extension_Seat(String time) {

        if (user.getUsingSeatNum() == 0) {
            System.out.println(user.getUserName() + "님, 사용중인 좌석이 없습니다");
        }else {
            List<Seat> seats = csvManager.readSeatCsv();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

            LocalDateTime endTime = LocalDateTime.parse(user.getEndTime(), formatter);
            LocalDateTime requestTime = LocalDateTime.parse(time, formatter);

            long minutesDifference = ChronoUnit.MINUTES.between(requestTime, endTime);
            if (minutesDifference <= 30) {
                for (Seat seat : seats) {
                    if (user.getUsingSeatNum() == seat.getSeatNum()) {
                        seat.extensionTime(time);
                        user.setEndTime(seat.getEndTime());
                        csvManager.updateSeatInCsv(seat);
                        csvManager.updateUserCsv(user);
                        System.out.println("좌석 연장에 성공했습니다.");
                        System.out.println("------------------");
                        System.out.println("좌석 번호: " + user.getUsingSeatNum());
                        System.out.println("좌석 이용 시작 시간: " + RegexManager.formatDateTime(user.getStartTime()));
                        System.out.println("좌석 이용 종료 시간: " + RegexManager.formatDateTime(user.getEndTime()));
                        return;
                    }
                }
            } else {
                System.out.println("좌석 연장은 이용 종료 30분 전부터 가능합니다.");
            }
        }


        System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
    }


    public void admin_Menu() {

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("<관리자 메뉴>");
            System.out.println("---------------");
            System.out.println("1) 좌석 현황 확인 및 좌석 강제퇴실");
            System.out.println("2) 좌석 수 조정");
            System.out.println("3) 로그아웃");
            System.out.println("---------------");
            try {
                System.out.println("메뉴 번호를 입력하세요.");
                System.out.print(">>");
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1":
                        printSeat();
                        seatout();
                        break;
                    case "2":
                        seatsetting();
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





}