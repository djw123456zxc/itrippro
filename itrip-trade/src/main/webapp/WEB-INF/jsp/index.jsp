<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>微信支付</title>
</head>
<body>
<form id="wxform" action="">
订单号：${orderNo}<input type="hidden" name="orderNo" id="orderNo" value="${orderNo}"/><br/>
商品描述：${body}<input type="hidden" name="body" id="body" value="${body}"/><br/>
商品金额：${money}<input type="hidden" name="money" id="money" value="0.01"/><br/>
<input type="button" value="支付" id="pay"/>
</form>
<div id="code">
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/qrcode.js"></script>
<script type="text/javascript">
    $("#pay").click(function(){
        var orderNo = $("#orderNo").val();
        var body = $("#body").val();
        var money = $("#money").val();
        $.ajax({
            cache : true,
            type : "post",
            url : "${pageContext.request.contextPath}/api/pay?orderNo="+orderNo+"&body="+body+"&money="+money,
            async : false,
            contentType: false,   //jax 中 contentType 设置为 false 是为了避免 JQuery 对其操作，从而失去分界符，而使服务器不能正常解析文件
            processData: false,   //当设置为true的时候,jquery ajax 提交的时候不会序列化 data，而是直接使用data
            success:function(data){
                //参数1表示图像大小，取值范围1-10；参数2表示质量，取值范围'L','M','Q','H'
                var qr = qrcode(10, 'H');
                qr.addData(data);
                qr.make();
                $("#code").html(qr.createImgTag());
                setInterval(query,5000);
            }

        });
    });

    function query(){
        $.ajax({
            cache : true,
            type : "post",
            url : "${pageContext.request.contextPath}/api/query",
            async : false,
            contentType: false,   //jax 中 contentType 设置为 false 是为了避免 JQuery 对其操作，从而失去分界符，而使服务器不能正常解析文件
            processData: false,   //当设置为true的时候,jquery ajax 提交的时候不会序列化 data，而是直接使用data
            success:function(data){
                console.log(data);
                if(data==true){
                    location.href="http://localhost:8080/itriptrade/api/success";
                }
                if(data=="timeout"){
                    location.href="http://localhost:8080/itriptrade/api/notice";
                }
            },
            error:function(e){
                console.log(e);
            }
        });
    }
</script>

</body>
</html>
