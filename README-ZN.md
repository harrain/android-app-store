- 应用简介
>一个仿照GooglePlay的手机应用商店，有六大碎片：首页、应用、游戏、专题、分类、排行。

- 使用到的技术有：

	- 抽取BaseActivity，管理所有Activity；
	- 利用FragmentFactory建立Map缓存管理各个Fragment；
	- 各个Fragment共性代码提取到BaseFragment类中，自定义FrameLayout搭建界面架子，创建四种视图，根据服务器传来的状态常量显示对应视图；
	- 用PagerTabStrip做的Tab栏；
	- 利用三级缓存原理建立线程池访问服务器；
	- 面向ViewHolder编程、多类型条目ListView；
	- 值动画设计界面的展开收缩；
	- 自定义控件ViewGroup实现台阶、瀑布流式的视图效果。
