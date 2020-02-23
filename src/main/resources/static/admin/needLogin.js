function needLogin() {
    if ($.cookie("token") == undefined || $.cookie("token") == ""){
        layer.alert("请登录！",function () {
            window.location.href = "/user/admin/login.html";
        })
    }
}