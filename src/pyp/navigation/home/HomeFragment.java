package pyp.navigation.home;

import pyp.navigation.global.PypBaseFragment;
import pyp.navigation.main.MenuActivity;
import pyp.navigation.main.R;
import pyp.navigation.main.ResideMenu;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

/**
 * @Title: HomeFragment
 * @Description: 主页模块页面
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class HomeFragment extends Fragment implements PypBaseFragment {

	private View parentView;
	private Button btn_open_menu;
	private GifView gif_loading;
	private ResideMenu resideMenu;
	private Button btn_main_titlebar_left_menu; // 界面做左菜单按钮
	private Button btn_main_titlebar_right_menu; // 界面做右菜单按钮
	private MenuActivity parentActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initData();
		initViews(inflater, container);
		initListensers();
		return parentView;
	}

	@Override
	public void initData() {
	}

	@Override
	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.main_home, container, false);
		parentActivity = (MenuActivity) this.getActivity();
		// 从xml中得到GifView的句柄
		gif_loading = (GifView) parentView.findViewById(R.id.main_imageView);
		// 设置Gif图片源
		gif_loading.setGifImage(R.drawable.main_loading);
		// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
		gif_loading.setGifImageType(GifImageType.COVER);
		gif_loading.setShowDimension(170, 160);
		MenuActivity parentActivity = (MenuActivity) getActivity();
		resideMenu = parentActivity.getResideMenu();
		btn_open_menu = (Button) parentView
				.findViewById(R.id.main_btn_open_menu);
		FrameLayout ignored_view = (FrameLayout) parentView
				.findViewById(R.id.main_ignored_view);
		resideMenu.addIgnoredView(ignored_view);
		// 菜单栏左右按钮
		btn_main_titlebar_left_menu = (Button) parentView
				.findViewById(R.id.main_titlebar_left_menu);
		btn_main_titlebar_right_menu = (Button) parentView
				.findViewById(R.id.main_titlebar_right_menu);
	}

	@Override
	public void initListensers() {
		btn_open_menu.setOnClickListener(btn_open_menu_listener);
		btn_main_titlebar_left_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});
		btn_main_titlebar_right_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
					}
				});
	}

	/**
	 * 按钮(打开菜单栏)监听器
	 */
	View.OnClickListener btn_open_menu_listener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
		}
	};

}