import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Menu {

    static RegexManager regexManager = new RegexManager();

    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        String date = getDateFromUser(scanner);
        mainMenu(scanner, date);
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

    static void mainMenu(Scanner scanner, String date) {
        User user = new User(date);
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
                        user.user_Login();
                        break;
                    case 3:
                        System.out.println("관리자 로그인 메뉴로 이동합니다.");
                        user.admin_Login();
                        break;
                    case 4:
                        //csvManager.writeUserCsv();
                        System.out.println("프로그램을 종료합니다.\n");
                        return;
                }
            }


        }
    }

    static boolean CsvFileChecker() {
            String csvFilePath = "src/userData.csv";
            try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
                String line = br.readLine();
                if (line != null) {
                   return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                System.out.println("CSV 파일 읽기 오류: " + e.getMessage());
            }
       return false;
    }

}
