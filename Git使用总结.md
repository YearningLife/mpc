# Git使用笔记

## 一.参考链接

[廖雪峰的官方网站](https://www.liaoxuefeng.com/wiki/896043488029600)

## 二.git切换分支

查看分支：`git branch`

创建分支：`git branch <name>`

切换分支：`git checkout <name>`或者`git switch <name>`

创建+切换分支：`git checkout -b <name>`或者`git switch -c <name>`

合并某分支到当前分支：`git merge <name>`

删除分支：`git branch -d <name>`

修改



## 三.git注意事项

1.  解决git pull/push每次都需要输入密码问题:

    `git config --global credential.helper store`

   ​	本地生成一个文本，上边记录你的账号和密码,使用上述的命令配置好之后，再操作一次git pull，然后它会提示你输入账号密码，这一次之后就不需要再次输入密码了

2. Git修改本地或远程分支名称[参考链接](https://juejin.cn/post/6844903880115896327)

   `git branch -m oldBranch newBranch`

   3.