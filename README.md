## TeamPlugin
原作者: Youajing
原作链接: https://github.com/YouaJing/TeamPlugin

### 使用方式
创建新的团队，消耗50级经验，需在线时长满24H
```
/team new <团队名>
```
删除团队，消耗30级经验
```
/team del <团队名> 
```
改团队名，消耗100级经验
```
/team rename <新的团队名> 
```
改为团队选择展示的颜色，消耗3级经验，团队规模需≥5人
```
/team color <颜色代称> 
```
邀请目标玩家加入你的团队，目标玩家在线时长需满24H
```
/team invite <玩家id> 
```
接受团队邀请，加入团队
```
#/team accept 
```
拒绝团队邀请
```
/team reject 
```
为团队设置大本营传送点，重复使用将覆盖此前设置的传送点，团队规模需≥5人
```
/team sethome 
```
为团队设置参观传送点
```
/team setvisit 
```
传送至该团队的参观传送点
```
/team visit <团队名> 
```
设置团队副手，继承部分队长权限，重复使用将替换此前指定的副手
```
/team set副手 <玩家id> 
```
令该团队的副手不再担任团队副手
```
/team unset副手 
```
传送至团队大本营
```
/team home 
```
从你的团队中踢除玩家
```
/team kick <玩家id> 
```
退出你所在的团队，队长不可退出
```
/team quit 
```
查看已注册的团队列表（按照人数规模排序）
```
/team list <页码> 
```
查看目标团队成员列表
```
/team members <团队名> 
```
查看所在团队在线成员
```
/team online 
```
设置团队名缩写简称，展示于TAB界面，消耗3级经验，团队规模需≥5人
```
/team abbr <简称> 
```
暂时封禁该团队参观传送点，禁止他人使用该传送点，需要拥有 team.use 权限
```
/team ban visit <名称> 
```
