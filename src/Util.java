import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

	/** 포맷팅 된 날짜 및 시각정도 반환 Str */
	public static String getNowDate_TimeStr() {
		LocalDateTime regDate = null;
		LocalDateTime now = regDate.now();
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));

		return formatedNow;
	}
}
