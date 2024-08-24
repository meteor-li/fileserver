<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>首页</title>

    <script src="/jquery-3.2.1.min.js"></script>
    <script src="/vue.js"></script>
    <script src="/elementUI/elementUI.js"></script>
    <script src="/common.js"></script>
    <script src="/layer/layer.js"></script>
    <link rel="stylesheet" href="/common.css"/>
    <link rel="stylesheet" href="/elementUI/elementUI.css"/>


    <style>

    </style>
</head>
<body>

<div id="home" style="padding: 10px;display: flex;justify-content: center;align-items: center;">
    <div style="width: 100vw;height: 80vh;display: flex;flex-direction: column;justify-content: center;align-items: center">
        <div>
            <div style="padding: 10px;">
                <h1 style="text-align: center;text-shadow: 2px 3px gainsboro;">funnyFTP</h1>
            </div>
            <el-card>
                <div style="display: flex;flex-direction: column;justify-content: center;align-items: flex-end">
                    <el-input type="password" placeholder="管理员密码" v-model="pass"></el-input>
                    <br>
                    <el-button style="width: 100%;" @click="login" type="success" plain>登&nbsp;&nbsp;&nbsp;&nbsp;录</el-button>
                </div>
            </el-card>
        </div>
    </div>
</div>

<script>
    new Vue({
        el: "#home",
        data: {
            pass:""
        },
        watch: {

        },
        mounted: function () {

        },
        methods: {
            login:function () {
                var that = this;
                console.log(that.pass);
                lyb.ajax("/page/loginValiData",{pass:that.pass},function (rs) {
                    console.log("redirect");
                    window.location.href = window.location.origin;
                })
            }
        }
    })
</script>
</body>
</html>