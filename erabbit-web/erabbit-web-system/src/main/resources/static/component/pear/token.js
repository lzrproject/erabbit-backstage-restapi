$.ajaxSetup({
    cache: false,
    crossDomain: true,
    headers :{'Authorization':null},
    complete: function (xhr) {
        console.log(xhr)
        console.log(111)
        if(xhr.responseJSON&&xhr.responseJSON.status==401) {
            console.log(xhr.responseJSON);
            layer.alert('权限不足,请重新登陆', {
                icon: 5,
                title: "提示"
            },function() {
                window.parent.location.reload(); //刷新父页面
                window.parent.location.href="http://localhost:10012/login/main";
            });
        }
    }
});