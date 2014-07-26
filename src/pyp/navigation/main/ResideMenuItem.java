package pyp.navigation.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Title: ResideMenuItem
 * @Description: 菜单栏按钮实现类
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class ResideMenuItem extends LinearLayout{

	/** 菜单按钮图标  */
    private ImageView iv_icon;
    /** 菜单按钮标题 */
    private TextView tv_title;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    /**
     * 驻留菜单栏
     * @param context
     * @param icon
     * @param title
     */
    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    /**
     * 驻留菜单栏
     * @param context
     * @param icon
     * @param title
     */
    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    /**
     * 初始化视图
     * @param context
     */
    private void initViews(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.main_iv_icon);
        tv_title = (TextView) findViewById(R.id.main_tv_title);
    }


	/**
     * 设置图标的颜色
     * @param icon
     */
    public void setIcon(int icon){
        iv_icon.setImageResource(icon);
    }

    /**
     * 设置标题资源
     * @param title
     */
    public void setTitle(int title){
        tv_title.setText(title);
    }

    /**
     * 设置标题字符串
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }
}