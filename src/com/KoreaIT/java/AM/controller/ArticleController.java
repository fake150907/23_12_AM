package com.KoreaIT.java.AM.controller;

import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.AM.dto.Article;
import com.KoreaIT.java.AM.dto.Member;
import com.KoreaIT.java.AM.service.ArticleService;
import com.KoreaIT.java.AM.service.MemberService;
import com.KoreaIT.java.AM.util.Util;

public class ArticleController extends Controller {
	private Scanner sc;
	private List<Article> articles;
	private String cmd;
	private String actionMethodName;

	private List<Member> members;

	private ArticleService articleService;
	private MemberService memberService;

	public ArticleController(Scanner sc) {
		this.articles = Container.articleDao.articles;
		this.articleService = Container.articleService;
		this.memberService = Container.memberService;
		members = memberService.getMembers();
		this.sc = sc;
	}

	private int lastArticleId = 3;

	public void doAction(String actionMethodName, String cmd) {
		this.cmd = cmd;
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {
		case "write":
			if (isLogined() == false) {
				System.out.println("로그인 후 이용해주세요.");
				break;
			}
			doWrite();
			break;
		case "list":
			showList(cmd);
			break;
		case "detail":
			showDetail(cmd);
			break;
		case "delete":
			if (isLogined() == false) {
				System.out.println("로그인 후 이용해주세요.");
				break;
			}
			doDelete(cmd);
			break;
		case "modify":
			if (isLogined() == false) {
				System.out.println("로그인 후 이용해주세요.");
				break;
			}
			doModify(cmd);
			break;
		default:
			System.out.println("잘못된 명령입니다. 주인님");
			break;
		}
	}

	private void doWrite() {
		System.out.println("==게시글 작성==");
		int id = lastArticleId + 1;
		Member writer = loginedMember;
		String regDate = Util.getNowDate_TimeStr();
		String updateDate = regDate;
		System.out.print("제목 : ");
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();

		Article article = new Article(id, regDate, updateDate, writer.getId(), title, body);
		articles.add(article);
		articleService.add(article);
		System.out.printf("%d번 글이 생성 되었습니다. 주인님.\n", id);
		lastArticleId++;
	}

	private void showList(String cmd) {
		System.out.println("==게시글 목록==");
		if (articleService.getSize() == 0) {
			System.out.println("아무것도 없어");
			return;
		}

		String searchKeyword = cmd.substring("article list".length()).trim();
		List<Article> forPrintArticles = articleService.getForPrintArticles(searchKeyword);
		String writerName = null;

		System.out.println("  번호  /  작성일  /  작성자  /  제목   /   조회");
		for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
			Article article = forPrintArticles.get(i);

			for (Member member : members) {
				if (article.getMemberId() == member.getId()) {
					writerName = member.getName();
					break;
				}
			}
			if (Util.getNowDate_TimeStr().split(" ")[0].equals(article.getRegDate().split(" ")[0])) {
				System.out.printf("  %4d  /   %s    /   %s   /   %s   /   %d\n", article.getId(), article.getTitle(),
						writerName, article.getRegDate().split(" ")[1], article.getHit());
			} else {
				System.out.printf("  %4d  /   %s    /   %s   /   %s   /   %d\n", article.getId(), article.getTitle(),
						writerName, article.getRegDate().split(" ")[0], article.getHit());
			}

		}
	}

	private void showDetail(String cmd) {
		String[] cmdDiv = cmd.split(" ");

		int id = 0;

		try {
			id = Integer.parseInt(cmdDiv[2]);
		} catch (Exception e) {
			System.out.println("번호는 정수로 입력해주세요. 주인님");
			return;
		}

		Article foundArticle = articleService.getArticleById(id);

		String writerName = null;

		for (Member member : members) {
			if (foundArticle.getMemberId() == member.getId()) {
				writerName = member.getName();
				break;
			}
		}

		System.out.println("번호 : " + foundArticle.getId());
		System.out.println("작성 날짜 : " + foundArticle.getRegDate());
		System.out.println("수정 날짜 : " + foundArticle.getUpdateDate());
		System.out.println("작성자 : " + writerName);
		System.out.println("제목 : " + foundArticle.getTitle());
		System.out.println("내용 : " + foundArticle.getBody());
		System.out.println("조회 : " + foundArticle.getHit());

		foundArticle.increaseHit();

	}

	private void doDelete(String cmd) {
		String[] cmdDiv = cmd.split(" ");

		int id = 0;

		try {
			id = Integer.parseInt(cmdDiv[2]);
		} catch (Exception e) {
			System.out.println("번호는 정수로 입력해주세요.주인님.");
			return;
		}

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시글은 없습니다. 주인님.\n", id);
			return;
		}

		if (foundArticle.getMemberId() != loginedMember.getId()) {
			System.out.printf("%d번 글에 대한 권한이 없습니다. \n", id);
			return;
		}

		articleService.remove(foundArticle);
		System.out.println(id + "번 글이 삭제되었습니다. 주인님.");

	}

	private void doModify(String cmd) {
		String[] cmdDiv = cmd.split(" ");

		int id = 0;

		try {
			id = Integer.parseInt(cmdDiv[2]);
		} catch (Exception e) {
			System.out.println("번호는 정수로 입력해주세요.주인님.");
			return;
		}

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시글은 없습니다. 주인님.\n", id);
			return;
		}

		if (foundArticle.getMemberId() != loginedMember.getId()) {
			System.out.printf("%d번 글에 대한 권한이 없습니다. \n", id);
			return;
		}

		System.out.println("기존 제목 : " + foundArticle.getTitle());
		System.out.println("기존 내용 : " + foundArticle.getBody());
		System.out.print("새 제목 : ");
		String newTitle = sc.nextLine();
		System.out.print("새 내용 : ");
		String newBody = sc.nextLine();

		foundArticle.update(newTitle, newBody);
		System.out.println(id + "번 글이 수정되었습니다. 주인님.");

	}

	private Article getArticleById(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				return article;
			}
		}
		return null;
	}

	public void makeTestArticleData() {
		System.out.println("테스트를 위한 게시글 데이터를 생성하겠습니다.");

		articleService.add(new Article(1, "2023-12-12 12:12:12", Util.getNowDate_TimeStr(), 1, "냥냥", "펀치", 11));
		articleService.add(new Article(2, "2024-01-01 12:12:12", Util.getNowDate_TimeStr(), 2, "삑삑", "뭘봐", 22));
		articleService.add(new Article(3, Util.getNowDate_TimeStr(), Util.getNowDate_TimeStr(), 3, "멍멍", "펀치", 33));
	}

}
