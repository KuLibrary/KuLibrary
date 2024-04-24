import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class Seat {
    int seatNum; //좌석 번호
    Boolean using = true; // 해당 좌석을 사용중인지 여번

    LocalTime StartTime;

    LocalTime EndTime;

    public Seat(int seatNum, Boolean using, StartTime, EndTime;) {
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

    public void getStartTime() {
        return StartTime;
    }

    public void getEndTime() { //StartTime + 5시간으로 설정할 수 있도록
        return EndTime+5;
    }

    final String filename = "src/KuLibrary1/seatData.csv";


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
            if (!input.matches("\\d+")) {
                System.out.println("숫자만 입력해주세요.");
                continue;
            }
            seatNum = Integer.parseInt(input);
            if (seatNum <= 0 || seatNum > seat.size()) {
                System.out.println("해당 좌석은 존재하지 않습니다.");
                continue;
            }
            for (int i = 0; i < seat.size(); i++) {
                if (seatNum == getSeatNum().using()) {
                    //사용 좌석 사용 여부 확인
                }
            }
            break;
        }
        toCsv();
    }

    public void check_Seat() {
        boolean reserve = false;
        while (true) {
            ArrayList<Seat> tmprooms = new ArrayList<>();
            System.out.println("---------------");
            System.out.println("좌석 내역 출력");
            //사용 안하고 있는 좌석 번호, 예약 시간, 종료 시간 보이게

        }
    }


    public void printSeat(){
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



    public void toCsv() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            ArrayList<Seat> csvarr = new ArrayList<>();
            for(int i=0;i<seat.size();i++){
                csvarr.add(seat.get(i));
            }
            for(int i=0;i<past_seat.size();i++){
                csvarr.add(past_seat.get(i));
            }
            Comparator<Seat> roomComparator = new Comparator<Seat>() {
                @Override
                public int compare(Seat o1, Seat o2) {
                    return Integer.compare(o1.SeatNum, o2.SeatNum);
                }
            };

            Collections.sort(csvarr,roomComparator);
            for (Seat r : csvarr) {
                writer.write(r.getSeatNum() + "," + r.getUsing() + "," + r.getStartTime() + "," + r.getEndTime() +"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fromCsv() {
        BufferedReader br;

        try (FileReader fileReader = new FileReader(filename)) {
            br = Files.newBufferedReader(Paths.get(filename));
            String line = "";

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                String[] array = line.split(",");
                if(!array[3].equals("X") && !array[4].equals("X")){ //예약이 되어있는 RoomInfo 중에서
                    if(Integer.parseInt(array[4]) < Integer.parseInt(date)){ //checkout 날짜가 현재 날짜보다 작은 경우 (과거예약내역)
                        Seat room = new Seat(Integer.parseInt(array[0]), array[1], array[2], array[3]);
                        past_seat.add(room);
                        continue;
                    }
                }
                Seat room = new Seat(Integer.parseInt(array[0]), array[1], array[2], array[3]);
                seat.add(room);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
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