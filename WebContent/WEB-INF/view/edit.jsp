<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.MyWord" %>
<%
request.setCharacterEncoding("utf-8");
MyWord myWord = (MyWord) request.getAttribute("myWord");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>編集</title>
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
<div class="container-fluid"><!-- レスポンシブ対応 -->

<form method="POST">
<div class="form-group row">
    <label class="col-sm-2 col-form-label">単語</label>
    <div class="col-sm-10">
      <p class="form-control-static"><%= myWord.getTitle() %></p>
    </div>
</div>
<div class="form-group row">
    <label for="inputPassword" class="col-sm-2 col-form-label">意味</label>
    <div class="col-sm-10">
      <textarea name="body"><%= myWord.getBody() %></textarea>
    </div>
</div>
<input type="hidden" name="id" value="<%= myWord.getId() %>">
<input type="hidden" name="title" value="<%= myWord.getTitle() %>">
<input type="submit" name="edit" value="編集">
<input type="submit" name="delete" value="削除" onclick="return confirm('削除してよろしいですか？')">
</form>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>