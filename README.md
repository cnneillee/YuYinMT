#语音机器人MT
##功能简介
###V1:

- 语音识别
- 语音应答

###……

##实现原理

- 通过**语音识别**将用户语音识别成文字
- 将文字内容上传至服务器
- 服务器返回回答内容(Json)至android终端
- android终端**（利用GSON）**解析Json成文本
- 运用**语音合成**将返回回来的文本合成语音播放

##使用工具
- **[讯飞开放平台][1]**的**语音服务**（提供**语音识别**与**语音合成**功能支持）
- **[图灵机器人][2]**接入**虚拟机器人**（提供**虚拟机器人应答**功能支持）

##Demo
###V1:
![screenshot][3]
[1]:http://www.xfyun.cn/
[2]:http://www.tuling123.com/
[3]:https://github.com/neilleecn/YuYinMT/blob/master/screenshoot.gif
