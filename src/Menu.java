import java.util.Scanner;

public class Menu {
    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);


        while(true){
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
                    System.out.println("You selected Menu 1");
                    User user = new User();
                    user.register();
                    break;
                case 2:
                    System.out.println("You selected Menu 2");

                    break;
                case 3:
                    System.out.println("You selected Menu 3");
                    // TODO: 관리자 로그인 구현
                    break;
                case 4:
                    System.out.println("You selected Menu 4");
                    // TODO: 종료 메뉴 구현
                    break;
                default:
                    System.out.println("*입력을 확인해주세요* (1~4 사이의 숫자를 입력해주세요.)");
            }
        }
    }
}