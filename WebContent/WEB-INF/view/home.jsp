<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,model.*" %>
<%
//最初はnullなので初期値を入れてしまうのが良い。
Integer limit=(Integer)request.getAttribute("limit");
if(limit ==null){
	limit=20;
}
String msg=(String)request.getAttribute("msg");
if(msg == null){
	msg="";
}
String title=(String)request.getAttribute("title");
if(title==null){
	title="";
}
String matchMode=(String)request.getAttribute("matchMode");
if(matchMode==null){
	matchMode="lm";
}
Integer count=(Integer)request.getAttribute("count");
if(count == null){
	count=-1;
}
Integer pageNo=(Integer)request.getAttribute("pageNo");
if(pageNo==null){
	pageNo=-1;
}
String pager=(String)request.getAttribute("pager");

ArrayList<Word> list = null;
if (request.getAttribute("list") != null) {
	list=(ArrayList<Word>)request.getAttribute("list");
} else if (session.getAttribute("list") != null) {
	list = (ArrayList<Word>)session.getAttribute("list");
}

//現在のurlを詰める（お気に入り登録後戻ってくるのに使う）
StringBuilder retUrl = new StringBuilder();
retUrl.append("/DictDemo/home");
if(!title.equals("")) {
	//現在のクエリを取得
	retUrl.append("?");
	retUrl.append(request.getQueryString());
}
session.setAttribute("retUrl", retUrl.toString());

User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
<meta content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>home</title>
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
<div class="container-fluid"><!-- レスポンシブ対応 -->
<div class="header clearfix">
        <nav>

          <ul class="nav nav-pills pull-right">
          <% if(user != null) { %>
          	<%= "<li>ようこそ" + user.getEmail() + "さん！</li>" %>
          	<li role="presentation"><a href="/DictDemo/Logout">ログアウト</a></li>
          <% } else { %>
            <li role="presentation"><a href="/DictDemo/Login">ログイン</a></li>
            <li role="presentation"><a href="/DictDemo/SignUp">新規登録</a></li>
          <% } %>
          </ul>
        </nav>
        <h2 class="text-muted">英和辞書</h2>
      </div>

<form method="GET" action="home"><!-- actionをつけること。クエリを初期化してくれる -->
<div class="form-group">
    <!-- <label for="name">検索したい語句を入力してください:</label> -->
	<input type="text" name="title" value="<%=title %>" class="form-control">
</div>
<div class="form-group">
<%
Map<String,String> map=new LinkedHashMap<>();
map.put("lm","前方一致");
map.put("rm","後方一致");
map.put("pfm","完全一致");
map.put("pm","部分一致");
for(Map.Entry<String,String> entry:map.entrySet()){ %>
<label class="radio inline">
<input type="radio" name="match" value="<%=entry.getKey()%>"<%if(matchMode.equals(entry.getKey())){out.print(" checked");} %>><%=entry.getValue() %>
</label>
<%} %>
<!--
  	<label class="radio inline">
    <input type="radio" name="match" value="lm"<%if(matchMode.equals("lm")){out.print(" checked");} %>>前方一致
  	</label>
  	<label class="radio inline">
    <input type="radio" name="match" value="rm"<%if(matchMode.equals("rm")){out.print(" checked");} %>>後方一致
  	</label>
  	<label class="radio inline">
   	<input type="radio" name="match" value="pfm"<%if(matchMode.equals("pfm")){out.print(" checked");} %>>完全一致
   	</label>
  	<label class="radio inline">
    <input type="radio" name="match" value="pm"<%if(matchMode.equals("pm")){out.print(" checked");} %>>部分一致
  	</label>
-->
</div>
<input type="submit" value="検索" class="btn btn-primary" id="submit">
</form>

<!-- tab -->
<ul class="nav nav-tabs">
  <li role="presentation" class="active"><a href="#">Dictionary</a></li>
  <li role="presentation"><a href="/DictDemo/favorite">Favorite</a></li>
  <li role="presentation"><a href="#">Option</a></li>
</ul>


<%if(count != -1) {%>
<p>全<%=count %>件<%if(pageNo !=-1){out.print("中"+"("+((pageNo-1)*limit+1)+"-"+(pageNo*limit<count? pageNo*limit:count)+")表示");} %></p>
<table class="table table-striped">
<tr><th>検索語</th><th>意味</th><th>お気に入り</th></tr>
<%for(Word w:list) {%>
<tr><td><%=w.getTitle() %></td><td><%=w.getBody() %></td><td><a href="/DictDemo/register?regId=<%=w.getId()%>">登録</a></td></tr>
<%} %>
</table>
<%} else {%>
	<p><%="英単語を検索できます" %></p>
<% } %>
<%if(pager != null) {%>
	<%=pager %>
<%} %>

</div>

 <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
 <!-- Include all compiled plugins (below), or include individual files as needed -->
 <script src="js/bootstrap.min.js"></script>
</body>
</html>