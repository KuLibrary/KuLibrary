import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;

public class Seat {
    final int SEAT_CAPACITY =100;
    int seatNum; //좌석 번호
    Boolean using = true; // 해당 좌석을 사용중인지 여부
    LocalTime StartTime;
    LocalTime EndTime;


    public Seat(int seatNum, Boolean using, LocalTime StartTime, LocalTime EndTime) {
        this.seatNum = seatNum;
        this.using = using;
        this.StartTime =  StartTime;
        this.EndTime = getEndTime();
    }

    User user = new User();
    public Seat(User user){ this.user = user; }

    public Seat() {}

    public int getSeatNum() {
        return seatNum;
    }

    public Boolean getUsing() {
        return using;
    }


    public void setUsing(Boolean using) {
        this.using = using;
    }

    //시작시간 초기파일에서 받을때 00:00
    public LocalTime getStartTime() {
        return StartTime;
    }


    //시작시간 초기파일에서 받을때 00:00
    public LocalTime getEndTime() {
        //시작시간 00:00이면, 종료시간도 00:00으로 초기화
        if(getStartTime().equals(LocalTime.MIDNIGHT))
            return LocalTime.MIDNIGHT;
        //아니면(좌석 배정확정되었다면) 시작시간에 5시간 추가
        return getStartTime().plusHours(5);
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
                        //TODO: 이미 좌석 사용중이면 에약페이지로 넘어가면 안됨
                        apply_Reservation();
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

//            boolean seatFound = false;
//            for (Seat seat : seats) {
//                if (selectSeatNum == seat.seatNum) {
//                    seatFound = true;
//                    if (seat.using) {
//                        System.out.println("해당 좌석은 이미 사용중입니다.");
//                        System.out.println("다른 좌석을 입력해주세요");
//                        break;
//                    } else {
//                        System.out.println("좌석 예약에 성공했습니다.");
//                        seat.setUsing(true);
//                        seat.StartTime = LocalTime.now();
//                        updateSeatInCsv(seat);

                        //TODO: 로그인 되어있는 유저의 정보도 업데이트 해줘야함 사용중인 좌석번호 update

                        for (Seat seat : seats) {
                            if (selectSeatNum == seat.getSeatNum()) {
                                if (seat.getUsing()) {
                                    System.out.println("해당 좌석은 이미 사용중입니다");
                                    System.out.println("다른 좌석을 입력해주세요");
                                    break;
                                    //1차 기획서랑 다르게 메인화면으로 안가고 다시입력받을수있도록함
                                } else {
                                    System.out.println("좌석 예약에 성공했습니다.");
                                    seats.add(new Seat(selectSeatNum, true, LocalTime.now(), LocalTime.now().plusHours(5))); //좌석정보 업데이트
                                    user.setUsingSeatNum(selectSeatNum); //유저정보 업데이트
                                    user.setTimeSum(user.getTimeSum());
                                    user.setStartTime(user.getStartTime());
                                    user.setEndTime(user.getEndTime());
                                    updateSeatInCsv(seat);
                                    user.toCsv();
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


    public void printSeat(){
        List<Seat> seats = fromCsv();
        System.out.println("------ 좌석의 사용여부를 출력합니다 ------");
        for (Seat seat : seats) {
            String usingStatus = seat.using ? "O" : "X";
            System.out.println("좌석 번호: " + seat.seatNum + ", 사용 중: " + usingStatus);
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
                boolean using = Boolean.parseBoolean(data[1]);
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
        Scanner sc = new Scanner(System.in);
        if(user.getUsingSeatNum()==0)
        {
            System.out.println(user.getUserName()+"님, 사용중인 좌석이 없습니다");
            System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
            sc.nextLine();
            return;
        }
        else{
            System.out.println(user.getUserName()+"님의 현재 사용중인 좌석 정보");
            System.out.println("------------------");
            System.out.println("좌석 번호: "+user.getUsingSeatNum());
            System.out.println("좌석 이용 시작 시간: "+user.getStartTime());
            System.out.println("좌석 이용 종료 시간: "+user.getEndTime());

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
            for (int i=0; i<SEAT_CAPACITY; i++) {
                String line = i+1 + ",0,00:00,00:00";
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("파일을 쓰는 중 오류가 발생했습니다.");
        }
    }

}