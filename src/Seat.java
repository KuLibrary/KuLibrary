
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Seat {
    int SEAT_CAPACITY;
    int seatNum; //좌석 번호
    Boolean using = true; // 해당 좌석을 사용중인지 여부
    String StartTime;
    String EndTime;
    private static Seat instance;

    static RegexManager regexManager = new RegexManager();
    CsvManager csvManager = new CsvManager();

    public Seat(int seatNum, Boolean using, String StartTime, String EndTime) {
        this.seatNum = seatNum;
        this.using = using;
        this.StartTime = StartTime;
        this.EndTime = EndTime; // 수정: 생성자에서 EndTime을 바로 설정
    }
    public static Seat getInstance() {
        if (instance == null) {
            instance = new Seat(30); //좌석 수 기본값
        }
        return instance;
    }
    User user = new User();
    ArrayList<User> users = new ArrayList<>(); //유저 리스트
    public Seat(int SEAT_CAPACITY) {
        this.SEAT_CAPACITY = 30;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Seat() {
        this.SEAT_CAPACITY = 30;
        csvManager.writeSeatCsv(SEAT_CAPACITY);
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
    public void admin_out_Seat(String time) {
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
                        for (User user : users) {
                            if(user.getUsingSeatNum()==seat.getSeatNum()) {
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
                    } else {
                        System.out.println("해당 좌석은 사용중이지 않습니다.");
                        break;
                    }
                }
            }
            System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
            sc.nextLine();
            return;
        }


    }

    public void seat_setting() {
        int selectnum;
        //int currentseatnum=0; //현재 사용하고 있는 좌석수
        int maxseatnum =0; //사용자가 앉아있는 좌석수중에 제일 큰 값
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("조정하고자 하는 최대 좌석 수를 입력해주세요(30이하). q 입력 시 예약 메뉴로 돌아갑니다.");
            System.out.print(">> ");
            String input = sc.nextLine().trim();
            if (input.equals("q")) {
                System.out.println();
                return;
            } else if (!input.matches("\\d+")) {
                System.out.println("숫자만 입력해주세요.");
                continue;
            }
            selectnum = Integer.parseInt(input);
            if (selectnum > 30) {
                System.out.println("최대 좌석수는 30입니다.");
            }
            for (User user : users) {
                if(user.getUsingSeatNum()!=0) {
                    //currentseatnum++;
                    if(maxseatnum<user.getUsingSeatNum())
                        maxseatnum=user.getUsingSeatNum();
                }
            }
            if(selectnum<maxseatnum){
                System.out.println("조정하려는 최대 좌석 수보다 더 높은 좌석번호를 사용자가 사용중입니다.");
                System.out.println(maxseatnum+" 보다 더 큰 수를 입력해주세요");}
            else{
                if(selectnum > SEAT_CAPACITY) //27 ->> 29 좌석 늘릴경우 좌석 더 그리기
                    csvManager.expandSeatCapacity(SEAT_CAPACITY,selectnum);
                else if(selectnum < SEAT_CAPACITY) // 29-> 27 좌석 줄일경우 줄인만큼 치우기
                    csvManager.reduceSeatCapacity(selectnum);
                SEAT_CAPACITY=selectnum;
                System.out.println("최대 좌석 수를 조정하였습니다.");
                System.out.println("현재 최대 좌석수는 "+SEAT_CAPACITY+" 입니다.");
                return;
            }
            System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
            sc.nextLine();
            return;
        }
    }






    public void check_Seat() {
        csvManager.readSeatCsv();
        List<Seat> seats = csvManager.readSeatCsv();
        Scanner sc = new Scanner(System.in);
        if (user.getUsingSeatNum() == 0) {
            System.out.println(user.getUserName() + "님, 사용중인 좌석이 없습니다");
            System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
            sc.nextLine();
        } else {
            System.out.println(user.getUserName() + "님의 현재 사용중인 좌석 정보");
            System.out.println("------------------");
            System.out.println("좌석 번호: " + user.getUsingSeatNum());
            for (Seat seat : seats) {
                if (user.getUsingSeatNum() == seat.getSeatNum()) {
                    System.out.println("좌석 이용 시작 시간: " + RegexManager.formatDateTime(seat.getStartTime()));
                    System.out.println("좌석 이용 종료 시간: " + RegexManager.formatDateTime(seat.getEndTime()));
                    break;
                }
            }
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
        int currentSeatCount=csvManager.getCurrentSeatCount();
        System.out.println(currentSeatCount);
        System.out.println(SEAT_CAPACITY);
        System.out.println(SEAT_CAPACITY * 0.8);
        if (user.getUsingSeatNum() == 0) {
            System.out.println(user.getUserName() + "님, 사용중인 좌석이 없습니다");
        }else {
            List<Seat> seats = csvManager.readSeatCsv();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDateTime endTime = LocalDateTime.parse(time, formatter);
            LocalDateTime requestTime = LocalDateTime.parse(time, formatter);

            long minutesDifference = ChronoUnit.MINUTES.between(requestTime, endTime);

            if (currentSeatCount >= (SEAT_CAPACITY * 0.8)) {
                System.out.println("좌석 연장이 불가능합니다.");
            }else {
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
                            System.out.println("좌석 이용 시작 시간: " + RegexManager.formatDateTime(seat.getStartTime()));
                            System.out.println("좌석 이용 종료 시간: " + RegexManager.formatDateTime(seat.getEndTime()));
                            return;
                        }
                    }
                } else {
                    System.out.println("좌석 연장은 이용 종료 30분 전부터 가능합니다.");
                }
            }
        }


        System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
    }


    public void admin_Menu(String time) {

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
                        admin_out_Seat(time);
                        break;
                    case "2":
                        seat_setting();
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