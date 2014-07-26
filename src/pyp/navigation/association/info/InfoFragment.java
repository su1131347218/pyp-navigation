package pyp.navigation.association.info;

import pyp.navigation.global.PypBaseFragment;
import pyp.navigation.main.MenuActivity;
import pyp.navigation.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title: InfoFragment
 * @Description: 社团模块 - 社团信息
 * @author qsuron
 * @date 2014-7-26
 * @email admin@qiushurong.cn
 */
public class InfoFragment extends Fragment implements PypBaseFragment {

	private MenuActivity parentActivity;
	private View parentView;;
	private Button btn_main_titlebar_left_menu; // 界面左菜单按钮
	private Button btn_main_titlebar_right_menu; // 界面右菜单按钮
	private TextView titlebar_title; // 界面ActionBar中央文本

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = (MenuActivity) activity;
	}

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
		Toast.makeText(parentActivity, "社团ID：" + getArguments().getInt("id"),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.association_info_layout_main,
				container, false);
		// 菜单栏左右按钮
		btn_main_titlebar_left_menu = (Button) parentView
				.findViewById(R.id.association_info_menu_left);
		btn_main_titlebar_right_menu = (Button) parentView
				.findViewById(R.id.association_info_menu_right);
		// 菜单栏中央文本
		titlebar_title = (TextView) parentView
				.findViewById(R.id.association_info_title);
		titlebar_title.setText(getArguments().getString("name"));
	}

	@Override
	public void initListensers() {
		btn_main_titlebar_left_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						parentActivity.getSupportFragmentManager()
								.popBackStack();
					}
				});
		btn_main_titlebar_right_menu
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(parentActivity, "收藏", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

}