
import java.util.Scanner;



public class Menu {

    static RegexManager regexManager = new RegexManager();

    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        String date = getDateFromUser(scanner);
        String time = getTimeFromUser(scanner);
        time=date+time;
        System.out.println("사용자가 입력한 날짜와 시간은 " +RegexManager.formatDateTime(time)+"입니다.");
        System.out.println(time);
        mainMenu(scanner, time);
    }

    static void mainMenu(Scanner scanner, String time) {
        User user = new User(time);
        Seat seat = new Seat();
        CsvManager csvManager = new CsvManager();
        csvManager.initSeatCsv(seat.SEAT_CAPACITY);
        while (true) {
            System.out.println("Konkuk library");
            System.out.println("---------------------------");
            System.out.println("1) 회원가입");
            System.out.println("2) 사용자 로그인");
            System.out.println("3) 관리자 로그인");
            System.out.println("4) 종료");
            System.out.print("메뉴 번호를 입력하세요 >>");

            String input = scanner.nextLine();


            if (regexManager.checkFourMenu(input)) {
                int menuNumber = Integer.parseInt(input);
                switch (menuNumber) {
                    case 1:
                        System.out.println("회원가입 메뉴로 이동합니다.");
                        user.register();
                        break;
                    case 2:
                        System.out.println("로그인 메뉴로 이동합니다.");
                        user.user_Login(time);
                        break;
                    case 3:
                        System.out.println("관리자 로그인 메뉴로 이동합니다.");
                        user.admin_Login(time);
                        break;
                    case 4:
                        System.out.println("프로그램을 종료합니다.\n");
                        return;
                }
            }


        }
    }


    private static String getDateFromUser(Scanner scanner) {
        String date;
        do {
            System.out.println("오늘의 날짜를 YYYYMMDD 형식으로 입력해주세요.(예: 20231031)");
            System.out.print(">> ");
            date = scanner.nextLine().trim();


        } while (!regexManager.checkDate(date));
        return date;
    }

    private static String getTimeFromUser(Scanner scanner) {
        String time;
        do {
            System.out.println("현재 시간을 HHMM 형식으로 입력해주세요.(예: 21시 14분 -> 2114)");
            System.out.print(">> ");
            time = scanner.nextLine().trim();

        } while (!regexManager.checkTime(time));
        return time;
    }



}
