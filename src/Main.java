import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("== 프로그램 시작 == ");

		Scanner sc = new Scanner(System.in);
		int lastArticleId = 0;
		Article content;
		List<Article> articlelist = new ArrayList<>();
		while (true) {
			System.out.print("명령어 > ");
			String cmd = sc.nextLine();

			if (cmd.equals("exit")) {
				break;
			} else if (cmd.equals("article write")) {
				int id = lastArticleId + 1;
				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();
				
				System.out.printf("%d번 글이 생성 되었습니다.\n", id);
				content = new Article(title, body, id);
				lastArticleId++;
				
				articlelist.add(content);
			} else if (cmd.equals("article list")) {

				System.out.println("번호  |  제목  |  내용");
				
				for (int i = articlelist.size() - 1; i >= 0; i--) {
					Article list = articlelist.get(i);
					System.out.printf("%d  |  %s  |  %s\n", list.lasteArticleId, list.title, list.body);
				}
				
			} else {
				System.out.println("명령을 똑바로 내려주세요 주인님.");
			}
		}

		System.out.println("== 프로그램 끝 == ");

		sc.close();
	}
}