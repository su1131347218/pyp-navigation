package pyp.navigation.calendar;

import pyp.navigation.global.PypBaseFragment;
import pyp.navigation.main.MenuActivity;
import pyp.navigation.main.R;
import pyp.navigation.main.ResideMenu;
import pyp.navigation.main.R.layout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @Title: CalendarFragment
 * @Description: 日历模块页面
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class CalendarFragment extends Fragment implements PypBaseFragment {

	private MenuActivity parentActivity;
	private View parentView;;
	private Button btn_main_titlebar_left_menu; // 界面做左菜单按钮
	private Button btn_main_titlebar_right_menu; // 界面做右菜单按钮

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
		parentView = inflater.inflate(R.layout.main_calendar, container, false);
		parentActivity = (MenuActivity) this.getActivity();
		// 菜单栏左右按钮
		btn_main_titlebar_left_menu = (Button) parentView
				.findViewById(R.id.main_titlebar_left_menu);
		btn_main_titlebar_right_menu = (Button) parentView
				.findViewById(R.id.main_titlebar_right_menu);
	}

	@Override
	public void initListensers() {
		btn_main_titlebar_left_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						parentActivity.getResideMenu().openMenu(
								ResideMenu.DIRECTION_LEFT);
					}
				});
		btn_main_titlebar_right_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						parentActivity.getResideMenu().openMenu(
								ResideMenu.DIRECTION_RIGHT);
					}
				});
	}
}