# Git

## 1. 初始配置

查看版本

```bash
git --version
```

初始化当前目录（一个非空目录），作为本地仓库

```bash
git init
```

 不要轻易修改 .git

![image-20230519131233445](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191312503.png)

查看设置：

```bash
git config --list
```

```bash
$ git config --list
diff.astextplain.textconv=astextplain
filter.lfs.clean=git-lfs clean -- %f
filter.lfs.smudge=git-lfs smudge -- %f
filter.lfs.process=git-lfs filter-process
filter.lfs.required=true
http.sslbackend=openssl
http.sslcainfo=D:/GIT-2.29.2/Git/mingw64/ssl/certs/ca-bundle.crt
core.autocrlf=true
core.fscache=true
core.symlinks=false
pull.rebase=false
credential.helper=manager-core
credential.https://dev.azure.com.usehttppath=true
user.email=1255817186@qq.com
user.name=Lilongqi
core.repositoryformatversion=0
core.filemode=false
core.bare=false
core.logallrefupdates=true
core.symlinks=false
core.ignorecase=true
remote.main.url=https://github.com/RonnieLee24/Battle_City.git
remote.main.fetch=+refs/heads/*:refs/remotes/main/*
:...skipping...
diff.astextplain.textconv=astextplain
filter.lfs.clean=git-lfs clean -- %f
filter.lfs.smudge=git-lfs smudge -- %f
filter.lfs.process=git-lfs filter-process
filter.lfs.required=true
http.sslbackend=openssl
http.sslcainfo=D:/GIT-2.29.2/Git/mingw64/ssl/certs/ca-bundle.crt
core.autocrlf=true
core.fscache=true
core.symlinks=false
pull.rebase=false
credential.helper=manager-core
credential.https://dev.azure.com.usehttppath=true
user.email=1255817186@qq.com
user.name=Lilongqi
core.repositoryformatversion=0
core.filemode=false
core.bare=false
core.logallrefupdates=true
core.symlinks=false
core.ignorecase=true
remote.main.url=https://github.com/RonnieLee24/Battle_City.git
remote.main.fetch=+refs/heads/*:refs/remotes/main/*
branch.master.remote=main
branch.master.merge=refs/heads/master
```

个人信息配置：用户名、邮箱【一次即可】

```bash
git config --global user.email "you@example.com"
git config --global user.name "Your Name"
```

查看系统配置

```bash
git config --system --list
```

查看当前用户配置

```bash
git config --global --list
```



git status

`git status`命令用于显示工作目录和暂存区的状态。使用此命令能看到那些修改被暂存到了, 哪些没有, 哪些文件没有被Git tracked到。

已经加入git控制（***\*Tracked\****）的文件解除git控制（取消文件跟踪）(untracked)

git status`不显示已经`commit`到项目历史中去的信息。看项目历史的信息要使用`git log

![image-20230519131405058](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191314099.png)

管起来的html文件变绿了，没有管起来的文件依然是红色

![image-20230519131432307](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191314348.png)

git add . 管理所有文件

![image-20230519131536100](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191315138.png)

## 2. 东北热

### 2.1 单枪匹马干

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191317480.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191317397.jpeg)

现在我们在用git status 查看一下：（发现 readme发生改变了，即 modifed了）—— 同时现在可以发现现在有2个版本了

![image-20230519131810423](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191318458.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191318187.png)

 现在又想添加一个约饭的功能：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191318086.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191319859.png)

#### 回滚

1. 回滚之前的版本

   ```bash
   git log ---> (git reset --hard 版本号)
   ```

2. 回滚之后的版本

   ```bash
   git relog ---> (git reset) --hard 版本号)
   ```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305191337090.png)





