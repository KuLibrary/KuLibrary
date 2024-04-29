import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class RegexManager {
    public boolean checkFourMenu(String input) {
        String pattern = "^[1-4]$";

        // 입력 값이 정규식 패턴과 일치하는지 확인
        if (Pattern.matches(pattern, input)) {
            return true;
        } else {
            System.out.println("잘못된 입력입니다. 1부터 4 사이의 숫자를 입력해야 합니다.");
            return false;

        }
    }

    public boolean checkDate(String date) {
        // YYYYMMDD 형식의 정규식
        String regex = "^[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";

        if (!date.matches(regex)) {
            System.out.println("날짜는 YYYYMMDD 형식의 숫자로 입력해야합니다.");
            return false;
        } else if (!checkDateFormat(date)) {
            System.out.println("달력에 존재하는 날짜를 입력해주세요");
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


}
