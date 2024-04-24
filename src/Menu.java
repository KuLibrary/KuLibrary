import java.util.Scanner;

public class Menu {
    public static void showMenu() {

        String date = "";
        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("오늘의 날짜를 YYYYMMDD 형식으로 입력해주세요.(예: 20231031)");
            System.out.print(">>");
            date = scanner.nextLine();
            date = date.trim();
            if (date.length() != 8) {
                System.out.println("날짜는 YYYYMMDD 형식으로 입력해야합니다.");
                continue;
            } else if (!date.matches("\\d+")) { // date가 숫자가 아닐 시
                System.out.println("날짜는 YYYYMMDD 형식의 숫자로 입력해야합니다.");
                continue;
            } else if (!check_Date(date)) {
                System.out.println("달력에 존재하는 날짜를 입력해주세요");
                continue;
            } else {
                break;
            }
        }


        while(true){
            User user = new User(date);
            System.out.println("Konkuk library");
            System.out.println("---------------------------");
            System.out.println("1) 회원가입");
            System.out.println("2) 사용자 로그인");
            System.out.println("3) 관리자 로그인");
            System.out.println("4) 종료");
            System.out.print("메뉴 번호를 입력하세요 >>");

            int menuNumber = scanner.nextInt();

            // Process the menu selection
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

    public static boolean check_Date(String date) { //YYYYMMDD 형식 확인
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));

        boolean ret = false;

        if (year >= 0 && year <= 9999) {  //년도가 0000년 부터 9999년 사이인 경우
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (day >= 1 && day <= 31)    //31일 까지 있는 달에 대한 처리
                    ret = true;
                else
                    ret = false;              //1에서 31 사이가 아닌 경우
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day >= 1 && day <= 30)    //30일까지 있는 달에 대한 처리
                    ret = true;
                else {
                    ret = false;              //1에서 30일 사이가 아닌 경우
                }
            } else if (month == 2) {
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {    //윤년인 경우
                    if (day >= 1 && day <= 29)
                        ret = true;
                    else
                        ret = false;       //1에서 29일 사이가 아닌 경우
                } else {
                    if (day >= 1 && day <= 28)
                        ret = true;
                    else
                        ret = false;       //1에서 28일 사이가 아닌 경우
                }
            } else {
                ret = false;      //월이 1에서 12가 아닌 경우
            }
        } else {
            ret = false;    //년도가 0000년에서 9999년 사이가 아닌 경우
        }
        return ret;
    }
}