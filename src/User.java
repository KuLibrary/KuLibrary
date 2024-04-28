import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private LocalTime startTime; // 사용 시작 시간
    private LocalTime endTime; // 사용 종료 예정 시간
    //0000 ~ 2359
    ArrayList<User> users = new ArrayList<User>();
    static String admin_id = "admin";
    final String filename = "src/userData.csv";

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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
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

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    //csv = read.(askjdla.csv)  tiemSum=csv[4];


    public User() {
        fromCsv();
    }

    public User(String date) {
        fromCsv();
        for (int i = 0; i < users.size(); i++) {
            int nowDate = Integer.parseInt(date);
        }
        toCsv();
    }

    public User register() {        //회원가입
        String name, id, PhoneNum, Password;
        Scanner sc = new Scanner(System.in);
        System.out.println("<회원 가입>");
        System.out.println("메인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        while (true) {                   //**********이름 등록**********//
            System.out.print("이 름 : ");
            name = sc.nextLine();
            name = name.trim();
            if (name.contentEquals("q")) {
                return null;
            }
            if (name.length() < 1 || name.length() > 15) {
                System.out.println("이름의 길이는 1이상 15이하여야 합니다!");
                continue;
            }
            if (!name.matches("^[가-힣]+$")) {
                if (name.matches(".*[a-zA-Z]+.*")) {
                    System.out.println("영어를 포함한 이름은 입력할 수 없습니다.");
                    continue;
                } else if (name.contains(" ") || name.contains("\t")) {
                    System.out.println("공백은 입력할 수 없습니다.");
                    continue;
                } else if (name.matches(".*\\d+.*")) {
                    System.out.println("숫자는 입력할 수 없습니다.");
                    continue;
                } else {
                    System.out.println("올바른 형식을 입력하세요!");
                    continue;
                }
            } else {
                break;
            }

        }

        while (true) {                   //**********아이디 등록**********//
            System.out.print("아이디 : ");
            id = sc.nextLine();
            id = id.trim();
            if (id.contentEquals("q")) {
                return null;
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

        while (true) {                   //**********전화번호 등록**********//
            System.out.print("전화번호 : ");
            PhoneNum = sc.nextLine();
            PhoneNum = PhoneNum.trim();
            if (PhoneNum.contentEquals("q")) {
                return null;
            }
            if (PhoneNum.length() < 1 || PhoneNum.length() > 15) {
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
        while (true) {                   //**********비밀번호 등록**********//
            System.out.print("비밀번호 : ");
            Password = sc.nextLine();
            Password = Password.trim();
            if (Password.contentEquals("q")) {
                return null;
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
        if (!isUniqueID(id) && !isUniquePhoneNum(PhoneNum)) {
            System.out.println("이미 등록된 전화번호, 아이디입니다.");
            System.out.println("아무 키를 누르면 메인 메뉴로 돌아갑니다.");
            sc.nextLine();
            return null;
        }
        if (!isUniqueID(id)) {
            System.out.println("이미 등록된 아이디입니다.");
            System.out.println("아무 키를 누르면 메인 메뉴로 돌아갑니다.");
            sc.nextLine();
            return null;
        }
        if (!isUniquePhoneNum(PhoneNum)) {
            System.out.println("이미 등록된 전화번호입니다.");
            System.out.println("아무 키를 누르면 메인 메뉴로 돌아갑니다.");
            sc.nextLine();
            return null;
        }
//        User newuser = new User(name, PhoneNum, id, Password, usingSeatNum);
        User newuser = new User(id, Password, name, PhoneNum, usingSeatNum);
        System.out.println("회원가입에 성공하였습니다.\n");
        users.add(newuser);
        toCsv();
        System.out.println("아무 키를 누르면 메인 메뉴로 이동합니다.");
        sc.nextLine();
        return newuser;
    }

    private boolean isUniquePhoneNum(String PhoneNum) {
        for (User user : users) {
            if (user.getUserPhoneNum().equals(PhoneNum)) {
                return false;
            }
        }
        return true;
    }

    private boolean isUniqueID(String id) {
        for (User user : users) {
            if (user.getUserId().equals(id)) {
                return false;
            }
        }
        return true;
    }

    public boolean user_Login() { //메뉴 들어가면 true, 로그인 취소면 false return
        Scanner sc = new Scanner(System.in);
        fromCsv();
        String uid;
        String upwd;
        System.out.println("사용자 로그인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        while (true) {
            System.out.print("아이디 : ");
            uid = sc.nextLine();
            uid = uid.trim();
            if (uid.equals("q")) {
                return false;
            }
            System.out.print("비밀번호 : ");
            upwd = sc.nextLine();
            upwd = upwd.trim();
            if (upwd.equals("q")) {
                return false;
            }
            for (User u : users) {       //회원 정보 일치 체크!
                if (u.getUserId().equals(uid))
                    if (u.getUserPassword().equals(upwd)) {
                        System.out.println("로그인 성공!");
                        System.out.println("아무 키를 누르면 예약 메뉴로 돌아갑니다.");
                        sc.nextLine();
                        Seat seat = new Seat(u);
                        seat.reservation_Menu(); //seat menu
                        return true;
                    }
                System.out.println("ID: "+u.getUserId()+"PW: "+u.getUserPassword());
            }
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다."); //로그인 실패!
            System.out.println("사용자 로그인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        }
    }

    public boolean admin_Login() { //메뉴 들어가면 true, 로그인 취소면 false return
        Scanner sc = new Scanner(System.in);
        String uid;
        String upwd;
        System.out.println("사용자 로그인 메뉴로 돌아가려면 'q'를 누르세요.\n");
        while (true) {
            System.out.print("아이디 : ");
            uid = sc.nextLine();
            uid = uid.trim();
            if (uid.equals("q")) {
                return false;
            }
            System.out.print("비밀번호 : ");
            upwd = sc.nextLine();
            upwd = upwd.trim();
            if (upwd.equals("q")) {
                return false;
            }
            if (uid.equals("admin")) {
                if (upwd.equals("1234")) {
                    Seat seat = new Seat();
                    seat.admin_Menu();
                    return true;
                }
            }
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다."); //로그인 실패!
        }
    }
    public void toCsv() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (User u : users) {
                writer.write(u.getUserId() + "," + u.getUserPassword() + "," + u.getUserName() + "," + u.getUserPhoneNum() + "," + u.getUsingSeatNum() + "\n");
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
                User user = new User(array[0], array[1], array[2], array[3],Integer.parseInt((array[4])));
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("회원 정보 파일이 없습니다.\n프로그램을 종료합니다.");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("회원 정보 파일이 없습니다.\n프로그램을 종료합니다.");
            System.exit(0);
        }
    }
}
