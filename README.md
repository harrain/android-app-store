- brief introduction of application
> A imitate the GooglePlay mobile application store, there are six Fragment: home page, applications, games, feature, classification and ranking.

- Using the technology:

	- extract "BaseActivity",manage all the Activities;
	- use FragmentFactory bulid "Map Cache" manage each Fragment ;
	- each Fragment common code extract into "BaseFragment" class，custom the "FrameLayout"to build UI shelf，create 4 views，to show the corresponding view according to the state constant from servers;
	- Tab by "PagerTabStrip"
	- The thread pool was established to access servers based on the principle of three-level cache ;
	- ViewHolder-Oriented programming、multi-type items of ListView
	- "value animator" realize the expansion and shrinkage of a view;
	- custom the "ViewGroup" to realize the effect like steps or waterfall.