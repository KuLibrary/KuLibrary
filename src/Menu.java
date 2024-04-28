import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Menu {
    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        String date = getDateFromUser(scanner);
        mainMenu(scanner, date);
    }

    private static String getDateFromUser(Scanner scanner) {
        String date;
        while (true) {
            System.out.println("오늘의 날짜를 YYYYMMDD 형식으로 입력해주세요.(예: 20231031)");
            System.out.print(">>");
            date = scanner.nextLine().trim();
            if (date.length() != 8) {
                System.out.println("날짜는 YYYYMMDD 형식으로 입력해야합니다.");
            } else if (!date.matches("\\d+")) {
                System.out.println("날짜는 YYYYMMDD 형식의 숫자로 입력해야합니다.");
            } else if (!checkDate(date)) {
                System.out.println("달력에 존재하는 날짜를 입력해주세요");
            } else {
                break;
            }
        }
        return date;
    }

    static void mainMenu(Scanner scanner, String date){
        User user = new User(date);
        Seat seat = new Seat();
        seat.initSeatCsv(seat.SEAT_CAPACITY);
        while(true){
            System.out.println("Konkuk library");
            System.out.println("---------------------------");
            System.out.println("1) 회원가입");
            System.out.println("2) 사용자 로그인");
            System.out.println("3) 관리자 로그인");
            System.out.println("4) 종료");
            System.out.print("메뉴 번호를 입력하세요 >>");

            String input = scanner.nextLine();
            int menuNumber;
            try {
                menuNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("*입력을 확인해주세요* (1~4 사이의 숫자를 입력해주세요.)");
                continue;
            }

            switch (menuNumber) {
                case 1:
                    System.out.println("회원가입 메뉴로 이동합니다.");
                    user.register();
                    break;
                case 2:
                    System.out.println("로그인 메뉴로 이동합니다.");
                    user.user_Login();
                    break;
                case 3:
                    System.out.println("관리자 로그인 메뉴로 이동합니다.");
                    user.admin_Login();
                    break;
                case 4:
                    System.out.println("프로그램을 종료합니다.\n");
                    return;
                default:
                    System.out.println("*입력을 확인해주세요* (1~4 사이의 숫자를 입력해주세요.)");
            }
        }
    }

    public static boolean checkDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
