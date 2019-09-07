package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.MyWordDAO;
import model.User;
import model.Word;
import model.WordDAO;

/**
 * Servlet implementation class Home
 */
@WebServlet("/home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private WordDAO dao;
	private MyWordDAO myDao;
	private static final int LIMIT = 10;
	private static final int PAGERMAX = 10;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = new WordDAO();
		myDao = new MyWordDAO();
		// ここら辺まで書いたらjspへ行く。並行して作っていく
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// 検索ワードの取得
		String title = request.getParameter("title");

		if (title != null && title.equals("")) {
			// titleは送信されているが空白の場合はメッセージをリクエストスコープに登録
			request.setAttribute("msg", "検索ワードを入力してください!");
		} else if (title != null) {
			int pageNo = 1;// 何ページ目か？初期値1
			if (request.getParameter("page") != null) {
				// クエリが来てればその値
				pageNo = Integer.parseInt(request.getParameter("page"));
			}
			// 検索モード取得
			String matchMode = request.getParameter("match");
			if (matchMode == null) {
				// 来てなければ前方一致
				matchMode = "lm";
			}
			// リクエストスコープにtitleとモードを登録
			request.setAttribute("title", title);
			request.setAttribute("matchMode", matchMode);
			// sqlのLIKE以降の文字列作成
			String match = "";
			switch (matchMode) {
			case "pfm":
				match = title;
				break;
			case "lm":
				match = title + "%";
				break;
			case "rm":
				match = "%" + title;
				break;
			case "pm":
				match = "%" + title + "%";
				break;

			}
			// 何件あるか？
			int count = dao.getCount(match);
			// 件数をリクエストスコープに登録
			request.setAttribute("count", count);
			// リクエストスコープに詰めるリストを準備
			ArrayList<Word> list = null;
			if (count <= LIMIT) {
				// 件数が１ページ最大件数以内だったら全件取得
				list = dao.findAll(match, user);
			} else {
				// そうでない場合はページ番号と１ページ表示件数を渡して結果を得る
				list = dao.findAll(match, pageNo, LIMIT, user);
				// //pager作成
				String pager = createPager(title, matchMode, pageNo, count, PAGERMAX);
				// リクエストスコープに情報を詰める
				request.setAttribute("pageNo", pageNo);
				request.setAttribute("limit", LIMIT);
				request.setAttribute("pager", pager);
			}
			// 上のifelseで作成したリストをリクエストスコープに詰める
			request.setAttribute("list", list);
		}

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/home.jsp");
		rd.forward(request, response);
	}

	/**
	 * ページャー作成
	 *
	 * @param title
	 *            検索語
	 * @param matchMode
	 *            検索モード
	 * @param pageNo
	 *            現在表示しているページ
	 * @param count
	 *            検索にマッチした総数
	 * @param pagerMax
	 *            ページャーに表示するページ数
	 * @return 生成されたHTML
	 */
	private String createPager(String title, String matchMode, int pageNo, int count, int pagerMax) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='pagination' id='pager'>");
		sb.append("<ul>");
		if (pageNo != 1) {
			sb.append("<li><a href='?title=" + title + "&match=" + matchMode + "&page=1'>&laquo;</a></li>");
			sb.append("<li><a href='?title=" + title + "&match=" + matchMode + "&page=" + (pageNo - 1)
					+ "'>&lt;</a></li>");
		}
		// LIMITで割り切れた場合とそうでない場合の両方を想定する。
		int pageCount = count % LIMIT == 0 ? count / LIMIT : count / LIMIT + 1;// 先生のソース修正
		int start, end;
		if (pageCount < pagerMax) {
			start = 1;
			end = pageCount;
		} else {
			start = (pageNo - (pagerMax / 2) >= 1) ? pageNo - (pagerMax / 2) : 1;
			end = pageNo + pagerMax / 2 <= pageCount ? pageNo + (pagerMax / 2) : pageCount;
		}
		for (int i = start; i <= end; i++) {
			sb.append("<li class='" + (pageNo == i ? "active" : "") + "'><a href='?title=" + title + "&match="
					+ matchMode + "&page=" + i + "'>" + i + "</a></li>");
		}
		if (pageNo != pageCount) {
			sb.append("<li><a href='?title=" + title + "&match=" + matchMode + "&page=" + (pageNo + 1)
					+ "'>&gt;</a></li>");
			sb.append("<li><a href='?title=" + title + "&match=" + matchMode + "&page=" + pageCount
					+ "'>&raquo;</a></li>");
		}
		sb.append("</ul>");
		sb.append("</div>");

		return sb.toString();
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

// TODO match と matchMode がこんがらがるので見直すこと
// TODO DAOをシングルトンにする
// TODO お気に入りの削除・編集機能を追加
// TODO 登録したら登録済みと表示されるようにする