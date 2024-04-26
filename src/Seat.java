import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class Seat {
    final int SEAT_CAPACITY =100;
    int seatNum; //좌석 번호
    Boolean using = true; // 해당 좌석을 사용중인지 여번
    LocalTime StartTime;
    LocalTime EndTime;

    List<Seat> seatList = new ArrayList<>();
    public Seat(int seatNum, Boolean using, LocalTime StartTime, LocalTime EndTime) {
        this.seatNum = seatNum;
        this.using = using;
        this.StartTime = getStartTime();
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

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public void setUsing(Boolean using) {
        this.using = using;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public LocalTime getEndTime() { //StartTime + 5시간으로 설정할 수 있도록
        LocalTime now = LocalTime.now();
        return now.plusHours(5);
    }

    public boolean displayStatus(){
        return using ? true : false;
    }

    ArrayList<Seat> seats = new ArrayList<Seat>(); //좌석정보만 담겨있는 리스트
    ArrayList<Seat> userseats = new ArrayList<Seat>(); //
    static String filename = "src/KuLibrary1/seatData.csv";


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
        String choose;
        int myroomNum = 0;
        int personNum;
        int selectSeatNum;
        LocalTime StartTime, EndTime;

        Scanner sc = new Scanner(System.in);
        printSeat();
        while (true) {
            System.out.println("사용하고자하는 좌석을 입력해주세요. q 입력 시 예약 메뉴로 돌아갑니다.");
            System.out.print(">> ");
            String input = sc.nextLine().trim();
            if (input.equals("q")) {
                System.out.println();
                return;
            }
            else if (!input.matches("\\d+")) {
                System.out.println("숫자만 입력해주세요.");
                continue;
            }
            selectSeatNum = Integer.parseInt(input);
            if (seatNum <= 0 || seatNum >= SEAT_CAPACITY) {
                System.out.println("해당 좌석은 존재하지 않습니다.");
                continue;
            }
            for (int i = 0; i < SEAT_CAPACITY; i++) {
                if (seatNum == seats.get(i).getSeatNum()) {
                    if(seats.get(i).getUsing()) {
                        System.out.println("해당 좌석은 이미 사용중입니다");
                        System.out.println("다른 좌석을 입력해주세요");
                        break;
                        //1차 기획서랑 다르게 메인화면으로 안가고 다시입력받을수있도록함
                    }
                    else{
                        System.out.println("좌석 예약에 성공했습니다.");
                        seatList.add(new Seat(selectSeatNum,true,getStartTime(),getEndTime()));


                        toCsv(seats);
                        break;
                    }
                }
            }
        }

    }

    public void check_Seat() {
        boolean reserve = false;
        while (true) {
            ArrayList<Seat> tmprooms = new ArrayList<>();
            System.out.println("---------------");
            System.out.println("좌석 내역 출력");
            //사용하고 있는 좌석 번호와 시작 시간, 종료 시간 출력. 사용 안하고 있다면 사용 중인 정보가 없습니다. 출력.

        }
    }


    public void printSeat(){ //동희가 적은 부분
        ArrayList<Seat> seats=new ArrayList<>();
        System.out.println("잔여 좌석입니다.");
        Iterator<Seat> iterator=seats.iterator();
        if(iterator.hasNext()){
            Seat seat=iterator.next();
            if(seat.isReserved());
            System.out.print(seat.getSeatNumber()+" ");
        }

        return;
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


    public void admin_Menu() {

        Scanner sc = new Scanner(System.in);
        fromCsv();
        while (true) {
            System.out.println("<어드민 메뉴>");
            System.out.println("---------------");
            System.out.println("1) 좌석 예약");
            System.out.println("2) 로그아웃");
            System.out.println("---------------");
            try {
                System.out.println("메뉴 번호를 입력하세요.");
                System.out.print(">>");
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1":
                        printSeat();;
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


}