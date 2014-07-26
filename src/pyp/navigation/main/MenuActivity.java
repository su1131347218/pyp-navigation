package pyp.navigation.main;

import pyp.navigation.settings.SettingsFragment;
import pyp.navigation.theme.ISkin;
import pyp.navigation.theme.SkinSettingManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * @Title: MenuActivity
 * @Description: 程序主入口
 * @author qsuron
 * @date 2014-7-26
 * @email admin@qiushurong.cn
 */
@SuppressLint("UseValueOf")
public class MenuActivity extends FragmentActivity implements OnClickListener,ISkin {
	/**
	 * 字段 int ： DISABLE_DIRECTION -1 : 启用双边菜单栏 0 : 禁用左边菜单栏 1 : 禁用右边菜单栏
	 */
	private static final int DISABLE_DIRECTION = -1;
	private static int CURRENT_DIRECTION = 0; 					// 当前打开的菜单栏方向
	private static String CURRENT_FRAGMENT = ""; 				// 当前打开的Fragment
	private ResideMenu resideMenu; 								// 菜单
	public SkinSettingManager mSkinSettingManager; 				// 用于换肤
	private FragmentCache fragments = new FragmentCache(); 		// Fragments缓存池
	private long close_waittime = 2000; 						// 关闭程序确认延迟时间(毫秒)
	private long close_touchtime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initData();
		initListensers();
	}

	/**
	 * 方法 initView 初始化界面 void
	 */
	public void initViews() {
		setContentView(R.layout.main);

		// 初始化菜单栏
		resideMenu = new ResideMenu(this); 							// 实例化对象
		resideMenu.setBackground(R.drawable.main_menu_background); 	// 背景图
		resideMenu.attachToActivity(this); 							// 使菜单附在activity上
		resideMenu.setScaleValue(0.6f); 							// 设置透明度(0.0f-1.0f)

		// 把菜单按钮添加到菜单栏 (标题,方向,资源,包名)
		this.addToMenu("主页", ResideMenu.DIRECTION_LEFT,
				R.drawable.main_icon_home, "pyp.navigation.home.HomeFragment");
		this.addToMenu("社团", ResideMenu.DIRECTION_LEFT,
				R.drawable.main_icon_association,
				"pyp.navigation.association.AssociationFragment");
		this.addToMenu("日历", ResideMenu.DIRECTION_RIGHT,
				R.drawable.main_icon_calendar,
				"pyp.navigation.calendar.CalendarFragment");
		this.addToMenu("设置", ResideMenu.DIRECTION_RIGHT,
				R.drawable.main_icon_settings,
				"pyp.navigation.settings.SettingsFragment");
		this.addToMenu("地图", ResideMenu.DIRECTION_LEFT,
				R.drawable.main_icon_map, "pyp.navigation.map.MapFragment");
		//this.addToMenu("百度地图", ResideMenu.DIRECTION_LEFT,
		//		R.drawable.main_icon_map, "pyp.navigation.map.BaiduMapActivity");

	}

	public void initListensers() {
	}

	/**
	 * 方法 addToMenu 把菜单按钮添加到菜单栏 (标题,方向,资源)
	 * 
	 * @param title
	 *            按钮标题
	 * @param direction
	 *            所在菜单栏方向
	 * @param r
	 *            按钮资源
	 * @param onclick
	 *            对应的类
	 */
	private void addToMenu(String title, int direction, int r, String onclick) {

		ResideMenuItem temp = new ResideMenuItem(this, r, title);
		resideMenu.addMenuItem(temp, direction);
		temp.setOnClickListener(this);
		temp.setTag(R.id.addToMenu_onclick, onclick);
		temp.setTag(R.id.addToMenu_direction, direction);
	}

	public void initData() {
		// 禁掉左边或者右边菜单栏
		disableDirection(DISABLE_DIRECTION);
		// 初始化皮肤
		mSkinSettingManager = new SkinSettingManager(this);
		mSkinSettingManager.initSkins();
		// 实例化Setting页面,放到缓存
		fragments.putCache("pyp.navigation.settings.SettingsFragment");
		// 切换到Home页面
		changeFragment("pyp.navigation.home.HomeFragment");
		// 设置Setting页面的皮肤接口
		SettingsFragment temp = (SettingsFragment) fragments
				.getCache("pyp.navigation.settings.SettingsFragment");
		temp.setSkin(this);
		temp = null;
	}

	/**
	 * 方法 disableDirection 禁掉左边或者右边菜单栏
	 * 
	 * @param num
	 *            -1 : 启用双边菜单栏 0 : 禁用左边菜单栏 1 : 禁用右边菜单栏
	 */
	private void disableDirection(int num) {
		switch (num) {
		case ResideMenu.DIRECTION_RIGHT:
			resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
			break;
		case ResideMenu.DIRECTION_LEFT:
			resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
			break;
		}
	}

	/**
	 * 重写 onKeyDown 监听-返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 如果菜单栏打开了，就判断是否退出
			// 如果菜单栏没打开，就判断是否有fragment需要返回
			if (resideMenu.isOpened()) {
				long currentTime = System.currentTimeMillis();
				if ((currentTime - close_touchtime) >= close_waittime) {
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					close_touchtime = currentTime;
				} else {
					finish();
				}
			} else {
				// 如果fragment管理器里面有可以后退的，那么就先后退，不退出
				FragmentManager fm = this.getSupportFragmentManager();
				if (fm.getBackStackEntryCount() > 0) {
					return super.onKeyDown(keyCode, event);
				}
				resideMenu.openMenu(CURRENT_DIRECTION);
			}
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 重写 dispatchTouchEvent
	 * 
	 * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	/**
	 * 菜单按钮反馈
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		// 下一个要打开的fragment
		String nextFragment = null;

		nextFragment = (String) view.getTag(R.id.addToMenu_onclick);

		CURRENT_DIRECTION = new Integer(""
				+ view.getTag(R.id.addToMenu_direction)).intValue();
		changeFragment(nextFragment);

		// 打开对应页面后关闭菜单栏
		resideMenu.closeMenu();
	}

	/**
	 * 方法 changeFragment 切换页面方法-再次封装-重载 查找是否缓存区已经有了，有就直接拿出来，没有就放进去，再切换
	 * 
	 * @param nextFragment
	 *            void
	 */
	public void changeFragment(String nextFragment) {

		// 如果切换的界面不是原来的界面，就需要清理掉无视手势的区域
		if (!CURRENT_FRAGMENT.equals(nextFragment))
			resideMenu.clearIgnoredViewList();

		CURRENT_FRAGMENT = nextFragment;

		// 判断是否缓存区已经存在
		if (fragments.isExist(nextFragment)) {
			// Log.i("qsuron", "Cache - Is Exist - " + nextFragment);
			changeFragment(fragments.getCache(nextFragment));
		} else {
			// Log.i("qsuron", "Cache - Not Exist - " + nextFragment);
			Fragment tempFragment = fragments.putCache(nextFragment);
			changeFragment(tempFragment);
		}
	}

	/**
	 * 切换页面
	 * 
	 * @param targetFragment
	 *            要切换的页面
	 */
	public void changeFragment(Fragment targetFragment) {
		// 切换页面
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	/**
	 * 切换页面-带返回栈
	 * 
	 * @param targetFragment
	 *            要切换的页面
	 */
	public void changeFragmentAddToBackStack(Fragment targetFragment) {
		// 切换页面
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.addToBackStack(null)
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	/**
	 * 获取resideMenu实例
	 * 
	 * @return ResideMenu
	 */
	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	/**
	 * 重写 getSkinKey
	 * 
	 * @see pyp.navigation.theme.ISkin#getSkinKey()
	 */
	@Override
	public String getSkinKey() {
		return "主页菜单栏背景";
	}

	/**
	 * 重写 changeSkin
	 * 
	 * @see pyp.navigation.theme.ISkin#changeSkin(int)
	 */
	@Override
	public boolean changeSkin(int skin_num) {
		int[] skinResources = { R.drawable.main_menu_background,
				R.drawable.main_menu_background2 };
		this.getResideMenu().setBackground(skinResources[skin_num]);
		return true;
	}

}
