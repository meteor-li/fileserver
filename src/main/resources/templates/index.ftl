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
    <script src="/jquerySticky/sticky.js"></script>
    <link rel="stylesheet" href="/common.css"/>
    <link rel="stylesheet" href="/elementUI/elementUI.css"/>


    <style>
        * {
            padding: 0px;
            margin: 0px;
        }


        .header {
            padding: 10px;
            background: #409EFF;
            box-shadow: 2px 2px 5px gray;
            margin-bottom: 2px;
        }

        .is-sticky{
            z-index: 9999;
            position: relative;
        }


    </style>
</head>
<body>

<form action="" id="fileForm" method="post" style="display: none;">
    <input type="text" name="path" value="">
</form>
<div id="home" class="funnyftpcontainer" style="box-sizing: border-box;">

    <div v-if="toUpload"
         style="position: fixed;z-index:99999999;width: 100vw;height: 100vh;background: rgba(220,220,220,0.23);display: flex;justify-content: center;align-items: center;">
        <div style="padding: 10px;border-radius: 5px;box-shadow: 0px 0px 5px 5px green;background: #fff;">
            <div style="display: flex;justify-content: flex-end;padding-bottom: 10px;">
                <el-button type="danger" @click="toUpload=false" icon="el-icon-close" size="mini" plain
                           circle></el-button>
            </div>
            <el-upload
                    class="upload-demo"
                    ref="upload"
                    action="/file/upload"
                    :data="{path:nowBasePath}"
                    :on-preview="handlePreview"
                    :on-remove="handleRemove"
                    :file-list="upFileList"
                    :auto-upload="false">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器
                </el-button>
                <div slot="tip" class="el-upload__tip">上传同名文件将会被覆盖</div>
            </el-upload>
        </div>
    </div>

    <div class="header">
        <div v-if="nowBasePath.length>0">
            <span>当前位置:</span><strong>{{nowBasePath}}</strong>
        </div>
        <div v-if="nowBasePath.length==0">
            <span>当前位置:</span><strong>根路径</strong>
        </div>
        <div style="padding: 5px;"></div>
        <el-button @click="getBack" type="success" size="mini" plain>返回上一级</el-button>
        <el-button type="primary" size="mini" plain @click="toUpload=true" v-if="nowBasePath.length>0">上传文件</el-button>
        <el-button type="success" size="mini" @click="newPackage" plain v-if="nowBasePath.length>0">新建文件夹</el-button>
    </div>


    <div class="fileLists">
        <el-table
                :data="fileList"
                style="width: 100%">
            <el-table-column
                    prop="type"
                    sortable
                    label="类型">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="simpleName"
                    label="文件或目录名">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="readableSize"
                    label="大小">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="info"
                    label="描述">
            </el-table-column>

            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" plain type="primary" v-if="scope.row.type=='dir'"
                               @click="into(scope.row.path)">进入文件夹
                    </el-button>
                    <el-button size="mini" plain type="success" @click="package(scope.row.path)"
                               v-if="nowBasePath.length>0">压缩为zip
                    </el-button>
                    <el-button size="mini" plain type="danger" v-if="scope.row.type=='dir' && nowBasePath.length>0"
                               @click="delDir(scope.row.path)">删除
                    </el-button>
                    <el-button size="mini" plain type="success" v-if="scope.row.type=='file'"
                               @click="fileDownLoad(scope.row.path)">下载
                    </el-button>
                    <el-button v-if="scope.row.simpleName.endWith('.zip')" size="mini" plain type="success"
                               v-if="scope.row.type=='file'" @click="unPackage(scope.row.path)">
                        解压
                    </el-button>
                    <el-button size="mini" plain type="danger" v-if="scope.row.type=='file' && nowBasePath.length>0"
                               @click="delFile(scope.row.path)">删除
                    </el-button>
                </template>
            </el-table-column>

        </el-table>
    </div>
</div>

<script>
    new Vue({
        el: "#home",
        data: {
            upFileList: [],
            fileList: [],
            toUpload: false,
            backSteps: [],
            nowBasePath: ""
        },
        watch: {
            // 如果 `question` 发生改变，这个函数就会运行
            toUpload: function (newvalue, oldvalue) {
                console.log("hehehe")
                var that = this;
                var path = that.nowBasePath;
                lyb.ajax("/file/getFileList", {path: path}, function (rs) {
                    that.fileList = rs.rs;
                });
            }
        },
        mounted: function () {
            String.prototype.endWith = function (endStr) {
                var d = this.length - endStr.length;
                return (d >= 0 && this.lastIndexOf(endStr) == d)
            };
            var that = this;
            lyb.ajax("/file/getRootFileList", {}, function (rs) {
                that.fileList = rs.rs;
            });
            $(".header").sticky({topSpacing: 0});
        },
        methods: {
            newPackage: function () {
                var that = this;
                this.$prompt('请输入文件夹名称', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(function (value) {
                    lyb.ajax("/file/newPackage", {path: that.nowBasePath, name: value["value"]}, function (rs) {
                        lyb.ajax("/file/getFileList", {path: that.nowBasePath}, function (rs) {
                            that.fileList = rs.rs;
                        });
                    })
                })
            },
            into: function (path) {
                var that = this;
                that.backSteps.push(path);
                console.log("that.backSteps:" + that.backSteps);
                that.nowBasePath = path;
                lyb.ajax("/file/getFileList", {path: path}, function (rs) {
                    that.fileList = rs.rs;
                });
            },
            package: function (path) {
                console.log("压缩打包下载：" + path);
                var that = this;
                lyb.ajax("/file/package", {path: path}, function (rs) {
                    lyb.ajax("/file/getFileList", {path: that.nowBasePath}, function (rs) {
                        that.fileList = rs.rs;
                    });
                })
            },
            unPackage: function (path) {
                console.log("解压缩到当前目录：" + path);
                var that = this;
                lyb.ajax("/file/unPackage", {path: path}, function (rs) {
                    lyb.ajax("/file/getFileList", {path: that.nowBasePath}, function (rs) {
                        that.fileList = rs.rs;
                    });
                })
            },
            getBack: function () {
                var that = this;
                if (that.backSteps.length == 1) {
                    that.backSteps.pop();
                    that.nowBasePath = "";
                    lyb.ajax("/file/getRootFileList", {}, function (rs) {
                        that.fileList = rs.rs;
                    });
                } else if (that.backSteps.length > 1) {
                    that.backSteps.pop();
                    path = that.backSteps[that.backSteps.length - 1];
                    that.nowBasePath = path;
                    console.log(path)
                    lyb.ajax("/file/getFileList", {path: path}, function (rs) {
                        that.fileList = rs.rs;
                    });
                } else {
                    that.nowBasePath = "";
                    lyb.ajax("/file/getRootFileList", {}, function (rs) {
                        that.fileList = rs.rs;
                    });
                }
            },
            fileDownLoad: function (path) {
                var url = "/file/download";//下载文件url
                $("#fileForm").attr('action', url);
                $("#fileForm input").val(path);
                $("#fileForm").submit();
            },
            submitUpload: function () {
                this.$refs.upload.submit();
            },
            handleRemove: function (file, fileList) {
                console.log(file, fileList);
            },
            handlePreview: function (file) {
                console.log(file);
            },
            delFile: function (path) {
                console.log(path);
                var that = this;
                that.$confirm('此操作将用就删除该文件，是否继续？','警告！！',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type:'warning'
                }).then(()=>{
                    lyb.ajax("/file/delFile", {path: path}, function (rs) {
                        that.fileList = rs.rs;
                        lyb.ajax("/file/getFileList", {path: that.nowBasePath}, function (rs) {
                            that.fileList = rs.rs;
                        });
                    });
                });

            },
            delDir: function (path) {
                console.log(path);
                var that = this;
                lyb.ajax("/file/delDir", {path: path}, function (rs) {
                    that.fileList = rs.rs;
                    lyb.ajax("/file/getFileList", {path: that.nowBasePath}, function (rs) {
                        that.fileList = rs.rs;
                    });
                });
            }
        }
    })
</script>
</body>
</html>
