(function () {
    console.log("%c Created By LiYibo Email: @1570194845@qq.com ", "color:#fff;background:#000;font-size:24px;");
    window.lyb = {};
    lyb.info = function () {
        console.log("%c Created By LiYibo Email: @1570194845@qq.com ", "color:#fff;background:#000;font-size:24px;");
        console.log("%c Gitee地址: https://gitee.com/haust_lyb/HTTPFTP ", "color:#fff;background:#000;font-size:24px;");
    };
    $.ajaxSetup({
        layerIndex:-1,
        beforeSend: function () {
            this.layerIndex = layer.load(0, { shade: [0.5, '#393D49'] });
        },
        complete: function () {
            layer.close(this.layerIndex);
        },
        error: function () {
            layer.alert('部分数据加载失败，可能会导致页面显示异常，请刷新后重试', {
                skin: 'layui-layer-molv'
                , closeBtn: 0
                , shift: 4 //动画类型
            });
        }
    });
    lyb.ajax=function(path,param,callback,options){
        if (path==""){
            return;
        };
        param = param||{};
        callback = callback||function(rs){console.log(JSON.stringify(rs));};
        $.ajax({
            type:"POST",
            url:path,
            data:param,
            success:function (res) {
                callback(res);
            }
        })
    };
    lyb.ajaxget=function (path) {
        $.ajax({
            type:"GET",
            url:path
        })
    }
})();