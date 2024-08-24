<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>相关</title>
    <script src="/jquery-3.2.1.min.js"></script>
    <script src="/vue.js"></script>
    <script src="/elementUI/elementUI.js"></script>
    <script src="/common.js"></script>
    <script src="/layer/layer.js"></script>
    <script src="/jquerySticky/sticky.js"></script>
    <link rel="stylesheet" href="/common.css"/>
    <link rel="stylesheet" href="/elementUI/elementUI.css"/>
</head>
<body>
<div id="info">
    <div style="background: gainsboro;margin: 5px 0px;" v-for="item in 8" @click.right="rightClick(item,$event)">
        {{item}}
    </div>
</div>

<script>
    new Vue({
        el: "#info",
        mounted:function(){
            window.oncontextmenu = function(){return false;}//去掉鼠标右键
        },
        methods: {
            rightClick:function (item,e) {
                console.log("item:"+item);
                console.log(e.pageX+":"+e.pageY);
            }
        }
    })
</script>
</body>
</html>