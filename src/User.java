
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private String userId; //사용자 아이디 (학번)
    private String userPassword; // 사용자 비밀번호
    private String userName; // 사용자 이름
    private String userPhoneNum; // 사용자 비밀번호

    private int usingSeatNum=0; // 이용중인 좌석 (사용중이 아니라면 0)
    private int timeSum=0; // 누적 이용 시간부
    private String startTime; // 사용 시작 시간
    private String endTime; // 사용 종료 예정 시간
    //0000 ~ 2359
    ArrayList<User> users = new ArrayList<>();
    static String admin_id = "admin";

    CsvManager csvManager = new CsvManager();

    public User(String userId, String userPassword, String userName, String userPhoneNum, int usingSeatNum) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.usingSeatNum = usingSeatNum;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoneNum() { return userPhoneNum; }

    public int getUsingSeatNum() {
        return usingSeatNum;
    }

    public int getTimeSum() {
        return timeSum;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public void setUsingSeatNum(int usingSeatNum) {
        this.usingSeatNum = usingSeatNum;
    }

    public void setTimeSum(int timeSum) {
        this.timeSum =timeSum+5;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public User() {
        csvManager.readUserCsv();
    }

    public User(String time) {
        csvManager.readUserCsv();
        for (int i = 0; i < users.size(); i++) {
            int nowDate = Integer.parseInt(time);
        }
        csvManager.writeUserCsv(users);
    }

    public void register() {
        String name, id, PhoneNum, Password;
        Scanner sc = new Scanner(System.in);
        System.out.println("<회원 가입>");
        System.out.println("메인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        while (true) {
            System.out.print("이 름 : ");
            name = sc.nextLine();
            name = name.trim();
            if (name.contentEquals("q")) {
                return;
            }
            if (name.isEmpty() || name.length() > 15) {
                System.out.println("이름의 길이는 1이상 15이하여야 합니다!");
                continue;
            }
            if (!name.matches("^[가-힣]+$")) {
                if (name.matches(".*[a-zA-Z]+.*")) {
                    System.out.println("영어를 포함한 이름은 입력할 수 없습니다.");
                } else if (name.contains(" ") || name.contains("\t")) {
                    System.out.println("공백은 입력할 수 없습니다.");
                } else if (name.matches(".*\\d+.*")) {
                    System.out.println("숫자는 입력할 수 없습니다.");

                } else {
                    System.out.println("올바른 형식을 입력하세요!");
                }
            } else {
                break;
            }

        }

        while (true) {
            System.out.print("아이디 : ");
            id = sc.nextLine();
            id = id.trim();
            if (id.contentEquals("q")) {
                return;
            }
            if (id.length() < 4 || id.length() > 10) {
                System.out.println("4~10자 영문, 숫자를 사용하세요.");
                continue;
            }
            if (id.contains(" ")) {
                System.out.println("공백은 포함될 수 없습니다.");
                continue;
            }
            if (!id.matches("[a-zA-Z0-9]*")) {
                System.out.println("영어와 숫자만 사용해주세요.");
                continue;
            }
            if (id.equals(User.admin_id)) {
                System.out.println("관리자 아이디는 사용할 수 없습니다.");
                continue;
            }

            break;
        }

        while (true) {
            System.out.print("전화번호 : ");
            PhoneNum = sc.nextLine();
            PhoneNum = PhoneNum.trim();
            if (PhoneNum.contentEquals("q")) {
                return;
            }
            if (PhoneNum.isEmpty() || PhoneNum.length() > 15) {
                System.out.println("1~15자의 숫자만 사용하세요.");
                continue;
            }
            if (PhoneNum.contains(" ")) {
                System.out.println("공백은 포함될 수 없습니다.");
                continue;
            }
            if (!PhoneNum.matches("[0-9]*")) {
                System.out.println("숫자만 사용해주세요.");
                continue;
            }

            break;
        }
        while (true) {
            System.out.print("비밀번호 : ");
            Password = sc.nextLine();
            Password = Password.trim();
            if (Password.contentEquals("q")) {
                return;
            }
            if (Password.length() < 8 || Password.length() > 16) {
                System.out.println("8~16자의 영어, 숫자, 특수문자를 사용하세요.");
                continue;
            }
            if (Password.contains(" ")) {
                System.out.println("공백은 포함될 수 없습니다.");
                continue;
            }
            if (!Password.matches("[a-zA-Z0-9!@#$%^&*()_+=~-₩]*")) {
                System.out.println("8~16자의 영어, 숫자, 특수문자를 사용하세요.");
                continue;
            }
            break;
        }
        if (!isUniquePhoneNum(PhoneNum)) {
            System.out.println("이미 등록된 전화번호, 아이디입니다.");
            System.out.println("아무 키를 누르면 메인 메뉴로 돌아갑니다.");
            sc.nextLine();
            return;
        }
        if (!isUniqueID(id)) {
            System.out.println("이미 등록된 아이디입니다.");
            System.out.println("아무 키를 누르면 메인 메뉴로 돌아갑니다.");
            sc.nextLine();
            return;
        }
        if (!isUniquePhoneNum(PhoneNum)) {
            System.out.println("이미 등록된 전화번호입니다.");
            System.out.println("아무 키를 누르면 메인 메뉴로 돌아갑니다.");
            sc.nextLine();
            return;
        }
        User newuser = new User(id, Password, name, PhoneNum, usingSeatNum);
        System.out.println("회원가입에 성공하였습니다.\n");
        users.add(newuser);
        csvManager.writeUserCsv(users);
        System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
        sc.nextLine();
    }

    private boolean isUniquePhoneNum(String PhoneNum) {
        boolean flag=true;
        for (User user : users) {
            if (user.getUserPhoneNum().equals(PhoneNum)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private boolean isUniqueID(String id) {
        boolean flag=true;
        for (User user : users) {
            if (user.getUserId().equals(id)) {
                flag= false;
                break;
            }
        }
        return flag;
    }

    public void user_Login(String time) {
        Scanner sc = new Scanner(System.in);
        csvManager.readUserCsv();
        String uid;
        String upwd;
        System.out.println("사용자 로그인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        while (true) {
            System.out.print("아이디 : ");
            uid = sc.nextLine();
            uid = uid.trim();
            if (uid.equals("q")) {
                return;
            }
            System.out.print("비밀번호 : ");
            upwd = sc.nextLine();
            upwd = upwd.trim();
            if (upwd.equals("q")) {
                return;
            }
            for (User u : users) {       //회원 정보 일치 체크!
                if (u.getUserId().equals(uid))
                    if (u.getUserPassword().equals(upwd)) {
                        System.out.println("로그인 성공!");
                        System.out.println("아무 키를 누르면 예약 메뉴로 돌아갑니다.");
                        sc.nextLine();
                        Seat seat = new Seat(u);
                        seat.reservation_Menu(time); //seat menu
                        return;
                    }
            }
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다."); //로그인 실패!
            System.out.println("사용자 로그인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        }
    }

    public void admin_Login(String time) {
        Scanner sc = new Scanner(System.in);
        String uid;
        String upwd;
        System.out.println("사용자 로그인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        while (true) {
            System.out.print("아이디 : ");
            uid = sc.nextLine();
            uid = uid.trim();
            if (uid.equals("q")) {
                return;
            }
            System.out.print("비밀번호 : ");
            upwd = sc.nextLine();
            upwd = upwd.trim();
            if (upwd.equals("q")) {
                return;
            }
            if (uid.equals("admin")) {
                if (upwd.equals("1234")) {
                    Seat seat = new Seat();
                    seat.admin_Menu();
                    return;
                }
            }
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다."); //로그인 실패!
        }
    }



}
