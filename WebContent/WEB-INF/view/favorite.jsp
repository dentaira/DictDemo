<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,model.*" %>
<%
//最初はnullなので初期値を入れてしまうのが良い。
/*
Integer limit=(Integer)request.getAttribute("limit");
if(limit ==null){
	limit=20;
}
String msg=(String)request.getAttribute("msg");
if(msg == null){
	msg="";
}
*/
String title=(String)request.getAttribute("title");
if(title==null){
	title="";
}

String matchMode=(String)request.getAttribute("match");
if(matchMode==null){
	matchMode="lm";
}
/*
Integer count=(Integer)request.getAttribute("count");
if(count == null){
	count=-1;
}
*/
Integer pageNo=(Integer)request.getAttribute("pageNo");
if(pageNo==null){
	pageNo=-1;
}
String pager=(String)request.getAttribute("pager");

ArrayList<MyWord> favList=(ArrayList<MyWord>)request.getAttribute("favList");

String retUrl = (String) session.getAttribute("retUrl");

User user = (User) session.getAttribute("user");

String msg = (String) request.getAttribute("msg");
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

</div>
<input type="submit" value="検索" class="btn btn-primary" id="submit">
</form>

<!-- tab -->
<ul class="nav nav-tabs">
	<li role="presentation" ><a href="<%= retUrl %>">Dictionary</a></li>
	<li role="presentation" class="active"><a href="#">Favorite</a></li>
	<li role="presentation"><a href="#">Option</a></li>
</ul>

<% if(favList != null) { %>
<table class="table table-striped">
<tr><th>検索語</th><th>意味</th><th>編集</th></tr>
<%for(MyWord w:favList) {%>
<tr><td><%=w.getTitle() %></td><td><%=w.getBody() %></td><td><a href="/DictDemo/Edit?id=<%= w.getId() %>">編集</a></tr>
<% } %>
</table>
<% } else {%>
<%= msg %>
<% } %>
</div>

 <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
 <!-- Include all compiled plugins (below), or include individual files as needed -->
 <script src="js/bootstrap.min.js"></script>
</body>
</html>